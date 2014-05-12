package com.example.android.auth;

import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class AccessToken {
    private String mUid          = "";

    private String mAccessToken  = "";

    private String mRefreshToken = "";

    private long   mExpiresTime  = 0L;

    public AccessToken() {
    }

    public boolean isSessionValid()
    {
      return (!TextUtils.isEmpty(this.mAccessToken)) && 
        (this.mExpiresTime != 0L) && (
        System.currentTimeMillis() < this.mExpiresTime);
    }
    
    public AccessToken(Oauth2AccessToken authWeiboToken) {
        this.setToken(authWeiboToken.getToken());
        this.setUid(authWeiboToken.getUid());
        this.setExpiresTime(authWeiboToken.getExpiresTime());
        this.setRefreshToken(authWeiboToken.getRefreshToken());
    }
    
    public static AccessToken parseAccessToken(Bundle bundle)
    {
      if (bundle != null) {
        AccessToken accessToken = new AccessToken();
        accessToken.setUid(getString(bundle, "uid", ""));
        accessToken.setToken(getString(bundle, "access_token", ""));
        accessToken.setExpiresIn(getString(bundle, "expires_in", ""));
        accessToken.setRefreshToken(getString(bundle, "refresh_token", ""));
        
        return accessToken;
      }
      
      return null;
    }
    public String toString() {
        return

        "uid: " + this.mUid + ", " + "access_token" + ": " + this.mAccessToken + ", " + "refresh_token" + ": "
                + this.mRefreshToken + ", " + "expires_in" + ": " + Long.toString(this.mExpiresTime);
    }

    public String getUid() {
        return this.mUid;
    }

    public void setUid(String uid) {
        this.mUid = uid;
    }

    public String getToken() {
        return this.mAccessToken;
    }

    public void setToken(String mToken) {
        this.mAccessToken = mToken;
    }

    public String getRefreshToken() {
        return this.mRefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.mRefreshToken = refreshToken;
    }

    public long getExpiresTime() {
        return this.mExpiresTime;
    }

    public void setExpiresTime(long mExpiresTime) {
        this.mExpiresTime = mExpiresTime;
    }

    public void setExpiresIn(String expiresIn) {
        if ((!TextUtils.isEmpty(expiresIn)) && (!expiresIn.equals("0"))) {
            setExpiresTime(System.currentTimeMillis() + Long.parseLong(expiresIn) * 1000L);
        }
    }

    private static String getString(Bundle bundle, String key, String defaultValue) {
        if (bundle != null) {
            String value = bundle.getString(key);
            return value != null ? value : defaultValue;
        }

        return defaultValue;
    }
}
