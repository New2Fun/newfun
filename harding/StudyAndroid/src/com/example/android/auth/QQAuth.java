package com.example.android.auth;

import android.content.Context;

import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.WeiboDialog;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.sina.weibo.sdk.utils.ResourceManager;
import com.sina.weibo.sdk.utils.UIUtils;

public class QQAuth extends WeiboAuth{
    public static final String TAG = "tencent_web_login";
    private static final String OAUTH2_BASE_URL = "https://open.t.qq.com/cgi-bin/oauth2/authorize?";
    private AuthInfo mAuthInfo;
    private Context mContext;
    public QQAuth(Context context, String appKey, String redirectUrl, String scope)
    {
      super(context,appKey,redirectUrl,scope);
      this.mContext = context;
      this.mAuthInfo = new AuthInfo(context, appKey, redirectUrl, scope);
    }
    public QQAuth(Context context, AuthInfo authInfo) {
       
        super(context, authInfo);
        this.mAuthInfo=mAuthInfo;
        this.mContext=mContext;
    }
    
    public void authorize(WeiboAuthListener listener, int type)
    {
      startDialog(listener, type);
    }
    
    
    private void startDialog(WeiboAuthListener listener, int type)
    {
      if (listener == null) {
        return;
      }
      int state = (int) Math.random() * 1000 + 111;
      
      WeiboParameters requestParams = new WeiboParameters();
      requestParams.put("client_id", this.mAuthInfo.getAppKey());
      requestParams.put("redirect_uri", this.mAuthInfo.getRedirectUrl());
      //requestParams.put("scope", this.mAuthInfo.getScope());
      requestParams.put("response_type", "token");
      requestParams.put("state", state);
      
      if (1 == type) {
        //requestParams.put("packagename", this.mAuthInfo.getPackageName());
        //requestParams.put("key_hash", this.mAuthInfo.getKeyHash());
      }
      
      String url = OAUTH2_BASE_URL + requestParams.encodeUrl();
      if (!NetworkHelper.hasInternetPermission(this.mContext)) {
        UIUtils.showAlert(this.mContext, "Error", "Application requires permission to access the Internet");
      }
      else if (NetworkHelper.isNetworkAvailable(this.mContext)) {
        new QQDialog(this.mContext, url, listener, this).show();
      } else {
        String networkNotAvailable = ResourceManager.getString(this.mContext, 2);
        LogUtil.i("Weibo_web_login", "String: " + networkNotAvailable);
        UIUtils.showToast(this.mContext, networkNotAvailable, 0);
      }
    }

}
