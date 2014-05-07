package com.example.android.auth;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboAuthException;
import com.sina.weibo.sdk.exception.WeiboDialogException;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.sina.weibo.sdk.utils.ResourceManager;
import com.sina.weibo.sdk.utils.Utility;
 

public class QQDialog   extends Dialog
{
    private static final String TAG = "TencentDialog";
    private static final int WEBVIEW_CONTAINER_MARGIN_TOP = 25;
    private static final int WEBVIEW_MARGIN = 10;
    private Context mContext;
    private RelativeLayout mRootContainer;
    private RelativeLayout mWebViewContainer;
    private ProgressDialog mLoadingDlg;
    private WebView mWebView;
    private boolean mIsDetached = false;
    
    private String mAuthUrl;
    
    private WeiboAuthListener mListener;
    
    private WeiboAuth mWeibo;
    
    private static int theme = 16973840;
    
    public QQDialog(Context context, String authUrl, WeiboAuthListener listener, WeiboAuth weibo)
    {
      super(context, theme);
      mAuthUrl = authUrl;
      mListener = listener;
      mContext = context;
      mWeibo = weibo;
    }
    
    public void onBackPressed()
    {
      super.onBackPressed();
      
      if (mListener != null) {
        mListener.onCancel();
      }
    }
    
    public void dismiss()
    {
      if (!mIsDetached) {
        if ((mLoadingDlg != null) && (mLoadingDlg.isShowing())) {
          mLoadingDlg.dismiss();
        }
        
        super.dismiss();
      }
    }
    
    public void onAttachedToWindow()
    {
      mIsDetached = false;
      super.onAttachedToWindow();
    }
    
    public void onDetachedFromWindow()
    {
      if (mWebView != null) {
        mWebViewContainer.removeView(mWebView);
        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
      }
      
      mIsDetached = true;
      super.onDetachedFromWindow();
    }
    
    protected void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      
      initWindow();
      
      initLoadingDlg();
      
      initWebView();
      
      initCloseButton();
    }
    
    private void initWindow()
    {
      requestWindowFeature(1);
      getWindow().setFeatureDrawableAlpha(0, 0);
      getWindow().setSoftInputMode(16);
      
      mRootContainer = new RelativeLayout(getContext());
      mRootContainer.setBackgroundColor(0);
      addContentView(mRootContainer, new ViewGroup.LayoutParams(-1, -1));
    }
    
    private void initLoadingDlg()
    {
      mLoadingDlg = new ProgressDialog(getContext());
      
      mLoadingDlg.requestWindowFeature(1);
      
      mLoadingDlg.setMessage(ResourceManager.getString(mContext, 1));
    }
    
    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView()
    {
      mWebViewContainer = new RelativeLayout(getContext());
      mWebView = new WebView(getContext());
      mWebView.getSettings().setJavaScriptEnabled(true);
      mWebView.getSettings().setUseWideViewPort(true);
      mWebView.getSettings().setLoadWithOverviewMode(false);
      mWebView.getSettings().setSavePassword(false);
      mWebView.setWebViewClient(new WeiboWebViewClient());
      mWebView.requestFocus();
      mWebView.setScrollBarStyle(0);
      mWebView.setVisibility(4);
      
      NetworkHelper.clearCookies(mContext, mAuthUrl);
      mWebView.loadUrl(mAuthUrl);
      
      RelativeLayout.LayoutParams webViewContainerLayout = new RelativeLayout.LayoutParams(
        -1, -1);
      
      RelativeLayout.LayoutParams webviewLayout = new RelativeLayout.LayoutParams(
        -1, -1);
      
      DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
      float density = dm.density;
      int margin = (int)(10.0F * density);
      webviewLayout.setMargins(margin, margin, margin, margin);
      Drawable background = ResourceManager.getNinePatchDrawable(mContext, 1);
      
      mWebViewContainer.setBackgroundDrawable(background);
      
      mWebViewContainer.addView(mWebView, webviewLayout);
      mWebViewContainer.setGravity(17);
      
      Drawable drawable = ResourceManager.getDrawable(mContext, 2);
      int width = drawable.getIntrinsicWidth() / 2 + 1;
      
      webViewContainerLayout.setMargins(width, (int)(25.0F * dm.density), width, width);
      mRootContainer.addView(mWebViewContainer, webViewContainerLayout);
    }
    
    private void initCloseButton()
    {
      ImageView closeImage = new ImageView(mContext);
      Drawable drawable = ResourceManager.getDrawable(mContext, 2);
      closeImage.setImageDrawable(drawable);
      closeImage.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View v) {
          dismiss();
          
          if (mListener != null) {
            mListener.onCancel();
          }
          
        }
        
      });
      RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
        -2, -2);
      RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mWebViewContainer.getLayoutParams();
      layoutParams.leftMargin = (params.leftMargin - drawable.getIntrinsicWidth() / 2 + 5);
      layoutParams.topMargin = (params.topMargin - drawable.getIntrinsicHeight() / 2 + 5);
      mRootContainer.addView(closeImage, layoutParams);
    }
    
    private class WeiboWebViewClient
      extends WebViewClient
    {
      private boolean isCallBacked = false;
      
      private WeiboWebViewClient() {}
      
      public boolean shouldOverrideUrlLoading(WebView view, String url) { LogUtil.i("TencentDialog", "load URL: " + url);
        
        if (url.startsWith("sms:")) {
          Intent sendIntent = new Intent("android.intent.action.VIEW");
          sendIntent.putExtra("address", url.replace("sms:", ""));
          sendIntent.setType("vnd.android-dir/mms-sms");
          mContext.startActivity(sendIntent);
          return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
      }
      
      public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
      {
        LogUtil.d("TencentDialog", "onReceivedError: errorCode = " + errorCode + 
          ", description = " + description + 
          ", failingUrl = " + failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);
        
        if (mListener != null) {
            mListener.onWeiboException(new WeiboDialogException(description, errorCode, failingUrl));
        }
        dismiss();
      }
      
      public void onPageStarted(WebView view, String url, Bitmap favicon)
      {
        super.onPageStarted(view, url, favicon);
        LogUtil.d("WeiboDialog", "onPageStarted URL: " + url);
        if (url.indexOf("access_token") != -1 && (!isCallBacked)) {
          isCallBacked = true;
          handleRedirectUrl(url);
          view.stopLoading();
          dismiss();
          
          return;
        }
        
        if ((!mIsDetached) && (mLoadingDlg != null) && (!mLoadingDlg.isShowing())) {
            mLoadingDlg.show();
        }
      }
      
      public void onPageFinished(WebView view, String url)
      {
        LogUtil.d("TencentDialog", "onPageFinished URL: " + url);
        
        super.onPageFinished(view, url);
        if ((!mIsDetached) && (mLoadingDlg != null)) {
          mLoadingDlg.dismiss();
        }
        
        if (mWebView != null) {
          mWebView.setVisibility(0);
        }
      }
    }
    
    private void handleRedirectUrl(String url) {
        Bundle values = new Bundle();
        String resultParam = url.split("#")[1];
        String params[] = resultParam.split("&");
        String accessToken = params[0].split("=")[1];
        //accesstoken过期时间，以返回的时间的准，单位为秒，注意过期时提醒用户重新授权
        String expiresIn = params[1].split("=")[1];
        String openid = params[2].split("=")[1];
        String openkey = params[3].split("=")[1];
        String refreshToken = params[4].split("=")[1];
        String state = params[5].split("=")[1];
        String name = params[6].split("=")[1];
        String nick = params[7].split("=")[1];
        
        if (accessToken != null && !"".equals(accessToken)) {
           
            values.putString( "access_token", accessToken);
            values.putString("expires_in", expiresIn);
            values.putString("uid", openid);
 
            //Util.saveSharePersistent(context, "AUTHORIZETIME", String.valueOf(System.currentTimeMillis() / 1000l));
            //Toast.makeText(mContext, "授权成功", Toast.LENGTH_SHORT).show();
        }
        String errorType = values.getString("error");
        String errorCode = values.getString("error_code");
        String errorDescription = values.getString("error_description");
        if ((errorType == null) && (errorCode == null)) {
            mListener.onComplete(values);
        } else {
            mListener.onWeiboException(new WeiboAuthException(errorCode, errorType, errorDescription));
        }
    }
}
