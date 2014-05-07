package com.example.android.util;

public interface Constants {
    public static final String WEIBO_APP_KEY = "2787301249"; // 应用的APP_KEY
    public static final String QQ_APP_KEY = "801498879"; // 应用的APP_KEY
    public static final String QQ_APP_SECRET = "18191822fa5f16e71367f577b8f07433";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";// 应用的回调页
    public static final String QQ_REDIRECT_URL = "www.liduole.com";// 应用的回调页
    public static final String SCOPE = // 应用申请的高级权限
    "email,direct_messages_read,direct_messages_write,"
    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
    + "follow_app_official_microblog," + "invitation_write";
}
