package com.example.android.auth;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;

import com.example.android.util.Constants;
import com.example.android.util.Tools;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;
 

public class QQWeiboApi extends BaseApi  {
    private static final String TAG="QQWeiboApi";
    private static final String API_BASE_URL = "https://open.t.qq.com/api";

    
    //多类型发表（可同时发表带音频、视频、图片的微博）请求url
    private static final int SERVER_URL_ADD=0;
    // 获取视频信息 请求url
    private static final int SERVER_URL_VIDEO = 1;
  //发表一条微博请求url
    private static final int SERVER_URL_ADDWEIBO         = 2; 
    //发表一条带图片的微博 请求url
    private static final int SERVER_URL_ADDPIC          = 3;
    //获取单条微博的转发列表或评论列表请求url
    private static final int SERVER_URL_RLIST          = 4;
    //用图片URL发表带图片的微博请求url
    private static final int SERVER_URL_ADDPICURL          = 5;
    //发送多媒体微博的URL
    private static final int SERVER_URL_ADDMULTIURL = 6;

 
    /**
     * 用图片URL发表带图片的微博请求url
     */
    //private static final String SERVER_URL_ADDPICURL = API_SERVER
     //       + "/t/add_pic_url";

    /**
     * 发送多媒体微博的URL
     */
    //private static final String SERVER_URL_ADDMULTIURL = API_SERVER
    //        + "/t/add_multi";
    
    private static final SparseArray<String> sAPIList = new SparseArray<String>();
    public  QQWeiboApi(AccessToken accessToken){
        this.accessToken=accessToken;
    }
    static {
        sAPIList.put(SERVER_URL_ADD, API_BASE_URL + "/t/add_multi");
        sAPIList.put(SERVER_URL_VIDEO,         API_BASE_URL + "/t/getvideoinfo");
        sAPIList.put(SERVER_URL_ADDWEIBO,          API_BASE_URL + "/t/add");
        sAPIList.put(SERVER_URL_ADDPIC,          API_BASE_URL + "/t/add_pic");
        sAPIList.put(SERVER_URL_RLIST,          API_BASE_URL + "/t/re_list");
        sAPIList.put(SERVER_URL_ADDPICURL, API_BASE_URL + "/t/add_pic_url");
        sAPIList.put(SERVER_URL_ADDMULTIURL, API_BASE_URL + "/t/add_multi");
    }
    
    
    /**
     * 多类型发表（可同时发表带音频、视频、图片的微博）
     * 
     * @param context
     *            上下文
     * @param content
     *            微博内容（若在此处@好友，需正确填写好友的微博账号，而非昵称），不超过140字
     * @param picUrl
     *            图片URL，可填空（URL最长为1024字节）
     * @param videoUrl
     *            视频URL，可填空（URL最长为1024字节）
     * @param musicUrl
     *            音乐URL，可填空（如果填写该字段，则music_title和music_author都必须填写）
     * @param musicTitle
     *            歌曲名 （UTF8编码，最长200字节）
     * @param musicAuthor
     *            歌曲作者（UTF8编码，最长64字节）
     * @param mCallBack
     *            回调对象
     * @param mTargetClass
     *            返回对象类，如果返回json则为null
     * @param resultType
     *            BaseVO.TYPE_BEAN=0 BaseVO.TYPE_LIST=1 BaseVO.TYPE_OBJECT=2
     *            BaseVO.TYPE_BEAN_LIST=3 BaseVO.TYPE_JSON=4
     */
    public void reAddWeibo(Context context, String content, String picUrl,
                           String videoUrl, String musicUrl, String musicTitle,
                           String musicAuthor,RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content,context);
        //params.put("content", content);
        params.put("pic_url", picUrl);
        params.put("video_url", videoUrl);
        params.put("music_url", musicUrl);
        params.put("music_title", musicTitle);
        params.put("music_author", musicAuthor);
       
        params.put("pageflag", "0");
        params.put("type", "0");
        params.put("reqnum", "30");
        params.put("pagetime", "0");
        params.put("contenttype", "0");
        requestAsync(sAPIList.get(SERVER_URL_ADDWEIBO), params, HTTPMETHOD_POST, listener);
    }

   

    /**
     * 发表一条微博
     * 
     * @param context
     *            上下文
     * @param content
     *            微博内容（若在此处@好友，需正确填写好友的微博账号，而非昵称），不超过140字
     * @param format
     *            返回数据的格式（json或xml）
     * @param longitude
     *            经度，为实数，如113.421234（最多支持10位有效数字，可以填空）不是必填
     * @param latitude
     *            纬度，为实数，如22.354231（最多支持10位有效数字，可以填空） 不是必填
     * @param syncflag
     *            微博同步到空间分享标记（可选，0-同步，1-不同步，默认为0），目前仅支持oauth1.0鉴权方式 不是必填
     * @param compatibleflag
     *            容错标志，支持按位操作，默认为0。 0x20-微博内容长度超过140字则报错 0-以上错误做容错处理，即发表普通微博
     *            不是必填
     * @param mCallBack
     *            回调函数
     * @param mTargetClass
     *            返回对象类，如果返回json则为null
     * @param resultType
     *            BaseVO.TYPE_BEAN=0 BaseVO.TYPE_LIST=1 BaseVO.TYPE_OBJECT=2
     *            BaseVO.TYPE_BEAN_LIST=3 BaseVO.TYPE_JSON=4
     */
    public void addWeibo(Context context, String content, 
            double longitude, double latitude, int syncflag,
            int compatibleflag,RequestListener listener) {
        WeiboParameters mParam = buildUpdateParams(content,context);
         
        if (longitude != 0d) {
            mParam.put("longitude", longitude);
        }
        if (latitude != 0d) {
            mParam.put("latitude", latitude);
        }
        mParam.put("syncflag", syncflag);
        mParam.put("compatibleflag", compatibleflag);
        requestAsync(sAPIList.get(SERVER_URL_ADD), mParam, HTTPMETHOD_POST, listener);
    }

    /**
     * 发表一条带图片的微博
     * 
     * @param context
     *            上下文
     * @param content
     *            微博内容（若在此处@好友，需正确填写好友的微博账号，而非昵称），不超过140字
     * @param format
     *            返回数据的格式（json或xml）
     * @param longitude
     *            经度，为实数，如113.421234（最多支持10位有效数字，可以填空）不是必填
     * @param latitude
     *            纬度，为实数，如22.354231（最多支持10位有效数字，可以填空） 不是必填
     * @param bm
     *            本地图片bitmap对象
     * @param syncflag
     *            微博同步到空间分享标记（可选，0-同步，1-不同步，默认为0），目前仅支持oauth1.0鉴权方式 不是必填
     * @param compatibleflag
     *            容错标志，支持按位操作，默认为0。 0x20-微博内容长度超过140字则报错 0-以上错误做容错处理，即发表普通微博
     *            不是必填
     * @param mCallBack
     *            回调函数
     * @param mTargetClass
     *            返回对象类，如果返回json则为null
     * @param resultType
     *            BaseVO.TYPE_BEAN=0 BaseVO.TYPE_LIST=1 BaseVO.TYPE_OBJECT=2
     *            BaseVO.TYPE_BEAN_LIST=3 BaseVO.TYPE_JSON=4
     */
    public void addPic(Context context, String content,
            double longitude, double latitude, Bitmap bm, int syncflag,int compatibleflag,RequestListener listener) {
        WeiboParameters mParam = buildUpdateParams(content,context);
        
      
        if (content == null || "".equals(content)) {
            content = "#分享图片#";
        }

        if (longitude != 0d) {
            mParam.put("longitude", longitude);
        }
        if (latitude != 0d) {
            mParam.put("latitude", latitude);
        }
        mParam.put("syncflag", syncflag);
        mParam.put("compatibleflag", compatibleflag);
        mParam.put("pic",bm);
 
        requestAsync(sAPIList.get(SERVER_URL_ADD), mParam, HTTPMETHOD_POST, listener);
    }

    /**
     * 用图片URL发表带图片的微博
     * 
     * @param context
     *            上下文
     * @param content
     *            微博内容（若在此处@好友，需正确填写好友的微博账号，而非昵称），不超过140字
     * @param format
     *            返回数据的格式（json或xml）
     * @param longitude
     *            经度，为实数，如113.421234（最多支持10位有效数字，可以填空）不是必填
     * @param latitude
     *            纬度，为实数，如22.354231（最多支持10位有效数字，可以填空） 不是必填
     * @param pic
     *            网络图片url
     * @param syncflag
     *            微博同步到空间分享标记（可选，0-同步，1-不同步，默认为0），目前仅支持oauth1.0鉴权方式 不是必填
     * @param compatibleflag
     *            容错标志，支持按位操作，默认为0。 0x20-微博内容长度超过140字则报错 0-以上错误做容错处理，即发表普通微博
     *            不是必填
     * @param mCallBack
     *            回调函数
     * @param mTargetClass
     *            返回对象类，如果返回json则为null
     * @param resultType
     *            BaseVO.TYPE_BEAN=0 
     *            BaseVO.TYPE_LIST=1 
     *            BaseVO.TYPE_OBJECT=2
     *            BaseVO.TYPE_BEAN_LIST=3 
     *            BaseVO.TYPE_JSON=4
     */
    public void addPicUrl(Context context, String content,
            double longitude, double latitude, String pic, int syncflag,
            int compatibleflag, RequestListener listener) {
        WeiboParameters mParam = buildUpdateParams(content,context);
        
        if (longitude != 0d) {
            mParam.put("longitude", longitude);
        }
        if (latitude != 0d) {
            mParam.put("latitude", latitude);
        }
        mParam.put("syncflag", syncflag);
        mParam.put("compatibleflag", compatibleflag);
        mParam.put("pic_url", pic);

        requestAsync(sAPIList.get(SERVER_URL_ADD), mParam, HTTPMETHOD_GET, listener);
    }

    /**
     * 发表多媒体微博
     * 
     * @param context
     *            上下文
     * @param format
     *            返回数据的格式（json或xml）
     * @param content
     *            微博内容（若在此处@好友，需正确填写好友的微博账号，而非昵称），不超过140字
     * @param longitude
     *            经度，为实数，如113.421234（最多支持10位有效数字，可以填空）
     * @param latitude
     *            纬度，为实数，如22.354231（最多支持10位有效数字，可以填空）
     * @param picUrl
     *            图片URL，可填空（URL最长为1024字节）
     * @param videoUrl
     *            视频URL，可填空（URL最长为1024字节）
     * @param videoTitle
     * @param musicUrl
     *            音乐URL，可填空（如果填写该字段，则music_title和music_author都必须填写）
     * @param musicTitle
     *            歌曲名 （UTF8编码，最长200字节）
     * @param musicAuthor
     *            歌曲作者（UTF8编码，最长64字节）
     * @param syncFlag
     *            微博同步到空间分享标记（可选，0-同步，1-不同步，默认为0），目前仅支持oauth1.0鉴权方式
     * @param compatibleflag
     *            容错标志，支持按位操作，默认为0。 0x1-下载图片失败则报错 0x2-下载到的图片大小错误则报错
     *            0x4-检查图片格式不支持则报错 0x8-上传图片失败则报错 0x10-获取视频信息失败则报错
     *            0x20-微博内容长度超过140字则报错 0x40-url参数非法（如pic_url=aaa）则报错
     *            0-以上错误均做容错处理，即发表普通微博
     *            0x1|0x2|0x4|0x8|0x10|0x20|0x40-同旧模式，以上各种情况均报错，不做兼容处理
     * @param mCallBack
     *            回调函数
     * @param mTargetClass
     *            返回对象类，如果返回json则为null
     * @param resultType
     *            BaseVO.TYPE_BEAN=0 
     *            BaseVO.TYPE_LIST=1 
     *            BaseVO.TYPE_OBJECT=2
     *            BaseVO.TYPE_BEAN_LIST=3 
     *            BaseVO.TYPE_JSON=4
     */
    public void addMulti(Context context, String content,
            double longitude, double latitude, String picUrl, String videoUrl,
            String videoTitle, String musicUrl, String musicTitle,
            String musicAuthor, int syncFlag, int compatibleflag, RequestListener listener) {
        // FIXME
        WeiboParameters mParam = buildUpdateParams(content,context);
 
        //mParam.put("clientip", Util.getLocalIPAddress(context));

        if (longitude != 0d && latitude != 0d) {
            mParam.put("longitude", longitude);
            mParam.put("latitude", latitude);
        }

        if (null != picUrl && !"".equals(picUrl)) {
            mParam.put("pic_url", picUrl);
        }

        if (null != videoUrl && !"".equals(videoUrl)) {
            mParam.put("video_url", videoUrl);
            if (null != videoTitle && !"".equals(videoTitle)) {
                mParam.put("video_title", videoTitle);
            }
        }

        if (null != musicUrl && !"".equals(musicUrl) && null != musicTitle
                && !"".equals(musicTitle) && null != musicAuthor
                && !"".equals(musicAuthor)) {
            mParam.put("music_url", musicUrl);
            mParam.put("music_title", musicTitle);
            mParam.put("music_author", musicAuthor);
        }

        mParam.put("syncflag", syncFlag);
        mParam.put("compatibleflag", compatibleflag);

        requestAsync(sAPIList.get(SERVER_URL_ADD), mParam, HTTPMETHOD_POST, listener);
    }

   
    // 组装微博请求参数
    private WeiboParameters buildUpdateParams(String content, Context context) {
        WeiboParameters params = new WeiboParameters();
        params.put("content", content);
        params.put("clientip", Tools.getLocalIPAddress(context));
        params.put("oauth_version", "2.a");
        params.put("oauth_consumer_key",Constants.QQ_APP_KEY);
        params.put("scope", "all");
        params.put("format", "json");
        params.put("openid",AccessTokenKeeper.readAccessToken(context, AccessTokenKeeper.SUPPORT_FACTOCRY_QQ).getUid());
        params.put("access_token", AccessTokenKeeper.readAccessToken(context, AccessTokenKeeper.SUPPORT_FACTOCRY_QQ).getToken());
        return params;
    }
}
