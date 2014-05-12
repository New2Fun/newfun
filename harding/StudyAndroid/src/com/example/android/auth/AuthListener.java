package com.example.android.auth;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

public class AuthListener implements WeiboAuthListener {
    private static final String TAG="SinaAuthListener";
    private Context context;
    private String support;
    public AuthListener(Context context,String support){
        this.context=context;
        this.support=support;
    }
    @Override
    public void onComplete(Bundle values) {
        AccessToken   mAccessToken = AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token
        if (mAccessToken.isSessionValid()) {
            AccessTokenKeeper.writeAccessToken(context, mAccessToken,support); //保存Token
            Log.i(TAG,"auth sucess token:"+mAccessToken.getToken());
            Toast.makeText(context,"授权成功",Toast.LENGTH_LONG);
        } else {
            // 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
            String code = values.getString("code", "");
            Log.e(TAG, "auth fail: "+code);
            Toast.makeText(context,"授权失败",Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onCancel() {
        Log.i(TAG,"cancle");
    }

    @Override
    public void onWeiboException(WeiboException e) {
        Log.e(TAG,  "Auth exception : " + e.getMessage());
    }

}
