package com.example.android.textswitcher;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.android.auth.AccessToken;
import com.example.android.auth.AccessTokenKeeper;
import com.example.android.auth.AuthListener;
import com.example.android.auth.QQWeiboApi;
import com.example.android.auth.SinaWeiboApi;
import com.example.android.auth.QQAuth;
import com.example.android.util.Constants;
import com.example.android.util.ContactsHelper;
import com.example.android.util.Encrypt;
import com.example.android.util.FileUtils;
import com.example.android.util.SystemInfo;
import com.example.android.util.Tools;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class WelcomeActivity extends Activity implements android.view.View.OnClickListener {
    private static final String TAG = "WelcomeActivity";
    private ListView contactView;
    private WeiboAuth mWeiboAuth;
    private Button authSinaButton;
    private Button authQQButton;
    private EditText shareContentEdit;
    private RadioGroup shareRadionGroup;
    private Button shareSend;
    private String support;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome);
        contactView=(ListView) findViewById(R.id.contact_view);
        authSinaButton=(Button) findViewById(R.id.btn_sina_auth);
        authQQButton=(Button) findViewById(R.id.btn_tencent_auth);
        authSinaButton.setOnClickListener(this);
        authQQButton.setOnClickListener(this);
        shareContentEdit=(EditText) findViewById(R.id.share_content);
        shareRadionGroup=(RadioGroup) findViewById(R.id.radio_share_rg);
        shareRadionGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                //根据用户勾选的单选按钮来动态改变tip字符串的值
                support=checkedId==R.id.radio_weibo?AccessTokenKeeper.SUPPORT_FACTOCRY_SINA:AccessTokenKeeper.SUPPORT_FACTOCRY_QQ;
                  //修改show组件中的文本
               Log.i(TAG, "选中了"+support);
            }
            
        });
        shareSend=(Button) findViewById(R.id.btn_share);
        shareSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sina_auth:
                authSinaClick();
                break;
            case R.id.btn_tencent_auth:
                authQQClick();
                break;
            case R.id.btn_share:
                share();
                break;
            default:
                break;
        }
    }
    
    private void share(){
        AccessToken accessToken=  AccessTokenKeeper.readAccessToken(WelcomeActivity.this, support);
        if(accessToken==null){
            Toast.makeText(WelcomeActivity.this, "请先授权", Toast.LENGTH_LONG).show();
            authSinaClick();
            Log.w(TAG, "请先授权");
        }
        
        RequestListener listener=   new RequestListener(){

            @Override
            public void onComplete(String arg0) {
                Log.i(TAG, arg0);
            }

            @Override
            public void onWeiboException(WeiboException exception) {
                Log.e(TAG, exception.getMessage());
            }
            
        };
        if(support.equals(AccessTokenKeeper.SUPPORT_FACTOCRY_SINA)){
            SinaWeiboApi api= new SinaWeiboApi(accessToken);
            api.update(shareContentEdit.getText().toString(), null, null, listener);
        }else{
            QQWeiboApi api= new QQWeiboApi(accessToken);
            api.addWeibo(WelcomeActivity.this, shareContentEdit.getText().toString(), 0, 0, 1, 0, listener);
        }
      
    }
    
    private void authSinaClick(){
        mWeiboAuth = new WeiboAuth(this, Constants.WEIBO_APP_KEY,
            Constants.REDIRECT_URL, Constants.SCOPE);
        mWeiboAuth.anthorize(new AuthListener(WelcomeActivity.this,AccessTokenKeeper.SUPPORT_FACTOCRY_SINA));
    }
    
    private void authQQClick(){
        mWeiboAuth = new QQAuth(this, Constants.QQ_APP_KEY,
            Constants.QQ_REDIRECT_URL, Constants.SCOPE);
        mWeiboAuth.anthorize(new AuthListener(WelcomeActivity.this,AccessTokenKeeper.SUPPORT_FACTOCRY_QQ));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.v(TAG, "populate context menu");

        // set context menu title

        int group1 = 1;
        int gourp2 = 2;
        menu.add(group1, 1, 1, "获取通讯录");
        menu.add(group1, 2, 2, "同步到服务器");
        menu.add(gourp2, 3, 3, "版本更新");
        menu.add(gourp2, 4, 4, "item 4");
        return true;
    }

    private List<String> getData(){
        
        List<String> data = new ArrayList<String>();
        List<String[]> list=  ContactsHelper.getPhoneContacts(this);
        for (String[] row:list){
            data.add(row[0]+":"+row[1]);
        }
        list =null;
        list=  ContactsHelper.getSIMContacts(this);
        for (String[] row:list){
            data.add(row[0]+":"+row[1]);
        }
        list =null;
        return data;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        //响应每个菜单项(通过菜单项的ID)
            case 1:
                contactView.setAdapter(new ArrayAdapter<String>(this,R.layout.simple_text,R.id.list_text1,getData()));
                break;
            case 2:
                
                break;
            case 3:
                 new Thread(new UpdateVersion()).start();
                break;
            case 4:

                break;
            default:
                //对没有处理的事件，交给父类来处理
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    
    class UpdateVersion implements Runnable{
        private String url="http://10.192.1.193:9090/Fun/system/checkupdate";
        @Override
        public void run() {
            StringBuilder params=new StringBuilder();
            params.append("?appName="+SystemInfo.appName);
            params.append("&os="+SystemInfo.os);
            params.append("&curVersion="+SystemInfo.appVersion);
            String resut= Tools.PostMessage(url+params.toString());
            Message msg = handler.obtainMessage();
            try {
                JSONObject json = new JSONObject(resut);
                String code=json.getString("code");
                if("200".equals(code)){
                    Object content=json.get("content");
                    if(content==null || "".equals(content)){
                        msg.what=1;
                        handler.sendMessage(msg);
                    }else{
                        msg.what=0;
                        msg.obj=content;
                        handler.sendMessage(msg);
                    }
                }
            } catch (JSONException e) {
                Log.e("json parse error", e.getMessage());
            }
        }
        
    }
    
    /**
     * 
     * <p>下载更新apk后，自动安装 
     * </p>
     */
    class DownFile implements Runnable{
        private String softId;
        private String fileName;
        private String hashCode;
        private String url="http://10.192.1.193:9090/Fun/system/download";
        public DownFile(String softId,String fileName,String hashCode){
            this.softId=softId;
            this.fileName=fileName;
            this.hashCode=hashCode;
        }
        @Override
        public void run() {
            try {
                StringBuilder params = new StringBuilder();
                params.append("?id=" + softId);
                String dir = "Down/";
                FileUtils futil = new FileUtils();

                if (!futil.isFileExist(dir)) {
                    futil.createSDDir(dir);
                }
                InputStream inputStream = futil.getInputStreamFromURL(url + params.toString());
                File saveFile = futil.write2SDFromInput(dir, fileName, inputStream);
                String localMd5= Encrypt.md5sum(saveFile.getPath());
                //验证文件是否下载完整
                if (saveFile != null && localMd5!=null && localMd5.equals(hashCode)) {
                    Tools.installApkFile(saveFile, WelcomeActivity.this);
                }else{
                    Log.e("WelcomeActivity", "文件下载失败");
                }
            } catch (Exception e) {
                Log.e("WelcomeActivity", e.getMessage());
            }
        }
        
    }
    
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    JSONObject json= (JSONObject)msg.obj;
                    try {
                        final String version = json.getString("version");
                        final String appName = json.getString("appName");
                        final String softId = json.getString("id");
                        final String md5Hash = json.getString("md5Hash");
                        
                        Dialog dialog = new AlertDialog.Builder(WelcomeActivity.this).setTitle("fun").setMessage(
                        "当前版本： "+SystemInfo.appVersion+"\n"+
                        "新版本："+version
                        ).setPositiveButton("后台更新",
                         new android.content.DialogInterface.OnClickListener() {
                         @Override
                          public void onClick(DialogInterface dialog, int which) {
                            new Thread(new DownFile(softId,version+".apk",md5Hash)).start();
                         }
                        }).create();
                      dialog.show();
                    } catch (JSONException exc) {
                        Log.e("json parse error", exc.getMessage());
                    }
                    break;
                case 1:
                    Toast.makeText(WelcomeActivity.this, "当前是最新版", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            
           }
        }
  };
}