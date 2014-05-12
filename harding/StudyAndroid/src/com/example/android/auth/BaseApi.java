package com.example.android.auth;

import android.text.TextUtils;

import com.example.android.network.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;

public class BaseApi {
    private static final String TAG="BaseApi";
    protected  AccessToken accessToken;
    protected static final String HTTPMETHOD_POST  = "POST";
    /** GET 请求方式 */
    protected static final String HTTPMETHOD_GET   = "GET";
    protected static final String KEY_ACCESS_TOKEN = "access_token";
    /**
     * HTTP 异步请求。
     * 
     * @param url        请求的地址
     * @param params     请求的参数
     * @param httpMethod 请求方法
     * @param listener   请求后的回调接口
     */
    protected void requestAsync(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        if (null == accessToken
                || TextUtils.isEmpty(url)
                || null == params
                || TextUtils.isEmpty(httpMethod)
                || null == listener) {
            LogUtil.e(this.TAG, "Argument error!");
            return;
        }
        
        params.put(KEY_ACCESS_TOKEN, accessToken.getToken());
        AsyncWeiboRunner.requestAsync(url, params, httpMethod, listener);
    }
    
    /**
     * HTTP 同步请求。
     * 
     * @param url        请求的地址
     * @param params     请求的参数
     * @param httpMethod 请求方法
     * 
     * @return 同步请求后，服务器返回的字符串。
     */
    protected String requestSync(String url, WeiboParameters params, String httpMethod) {
        if (null == accessToken
                || TextUtils.isEmpty(url)
                || null == params
                || TextUtils.isEmpty(httpMethod)) {
            LogUtil.e(this.TAG, "Argument error!");
            return "";
        }
        
        params.put(KEY_ACCESS_TOKEN, accessToken.getToken());
        return AsyncWeiboRunner.request(url, params, httpMethod);
    }
    

}
