package com.example.android.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * <p>保存授权信息上下文
 * </p>
 *
 * @author harding.he@fmjk.com
 * @version $Id: AccessTokenKeeper.java, v 0.1 2014-5-7 下午1:54:45 harding.he Exp $
 */
public class AccessTokenKeeper {
    private static final String PREFERENCES_NAME = "com_weibo_sdk_android";
    private static final String PREFERENCES_NAME_QQ = "com_qq_sdk_android";
    private static final String PREFERENCES_NAME_FUN = "com_fun_sdk_android";
    public static final String SUPPORT_FACTOCRY_QQ="qq";
    public static final String SUPPORT_FACTOCRY_SINA="sina";
    public static final String SUPPORT_FACTOCRY_NEWFUN="newfun";
    private static final String KEY_UID           = "uid";
    private static final String KEY_ACCESS_TOKEN  = "access_token";
    private static final String KEY_EXPIRES_IN    = "expires_in";
    
    private static String getPreferencesName(String support){
        if(support.equals(SUPPORT_FACTOCRY_SINA))
            return PREFERENCES_NAME;
        else if(support.equals(SUPPORT_FACTOCRY_QQ))
            return PREFERENCES_NAME_QQ;
        else return PREFERENCES_NAME_FUN;
    }
    /**
     * 保存 Token 对象到 SharedPreferences。
     * 
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     * @param support 厂商
     */
    public static void writeAccessToken(Context context, AccessToken token,String support) {
        if (null == context || null == token) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(getPreferencesName(support), Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_UID, token.getUid());
        editor.putString(KEY_ACCESS_TOKEN, token.getToken());
        editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
        editor.commit();
    }

    /**
     * 从 SharedPreferences 读取 Token 信息。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 返回 Token 对象
     */
    public static AccessToken readAccessToken(Context context,String support) {
        if (null == context) {
            return null;
        }
        
        AccessToken token = new AccessToken();
        SharedPreferences pref = context.getSharedPreferences(getPreferencesName(support), Context.MODE_APPEND);
        token.setUid(pref.getString(KEY_UID, ""));
        token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
        token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
        return token;
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     * 
     * @param context 应用程序上下文环境
     */
    public static void clear(Context context,String support) {
        if (null == context) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(getPreferencesName(support), Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
