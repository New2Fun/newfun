package com.example.android.textswitcher;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.util.Encrypt;
import com.example.android.util.Tools;

public class RegisterActivity extends Activity implements OnClickListener {
    //private ImageView        user_name_clear, password_clear, confirm_password_clear;
    private TextView         user_name_error, password_error, confirm_password_error;
    private Button           register_button;
    private Button           register_back;
    private Button           register_quit;
    private EditText         user_name_edit, password_edit, confirm_password_edit;
    private Dialog           registerDialog;
    private String           responseMsg     = "";
    private static final int REQUEST_TIMEOUT = 5 * 1000;                                            // 设置请求超时10秒钟  
    private static final int SO_TIMEOUT      = 10 * 1000;                                           // 设置等待数据超时时间10秒钟  
    private static final int LOGIN_OK        = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.register);
        //SysApplication.getInstance().addActivity(this);  
        InitView();

    }

    private void InitView() {
        // TODO Auto-generated method stub  
        register_quit = (Button) findViewById(R.id.quit);
        register_back = (Button) findViewById(R.id.reback);

        //confirm_password_clear = (ImageView) findViewById(R.id.confirm_password_clear);  
        register_button = (Button) findViewById(R.id.register_button);
        user_name_edit = (EditText) findViewById(R.id.user_name_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
        confirm_password_edit = (EditText) findViewById(R.id.confirm_password_edit);
        user_name_error = (TextView) findViewById(R.id.user_name_error);
        password_error = (TextView) findViewById(R.id.password_error);
        confirm_password_error = (TextView) findViewById(R.id.confirm_password_error);

        register_back.setOnClickListener(this);
        register_button.setOnClickListener(this);
        user_name_edit.setOnClickListener(this);
        password_edit.setOnClickListener(this);
        confirm_password_edit.setOnClickListener(this);
 
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reback:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);  
                RegisterActivity.this.startActivity(intent);  
                break;
            case R.id.quit:
                this.finish();
                break;
            case R.id.register_button:

                RegisterUser();
                break;
            case R.id.user_name_edit:
                user_name_error.setVisibility(View.GONE);

                break;
            case R.id.password_edit:
                password_error.setVisibility(View.GONE);


                break;
            case R.id.confirm_password_edit:
                confirm_password_error.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    public void RegisterUser() {

        if (user_name_edit.getText().toString().trim().equals("")
            || user_name_edit.getText().toString().trim().length() > 20
            || user_name_edit.getText().toString().trim().length() < 4) {
            user_name_error.setVisibility(View.VISIBLE);
        } else if (password_edit.getText().toString().trim().equals("")
                   || password_edit.getText().toString().trim().length() > 16
                   || password_edit.getText().toString().trim().length() < 6) {
            password_error.setVisibility(View.VISIBLE);
        } else if (!confirm_password_edit.getText().toString().trim().equals(password_edit.getText().toString().trim())) {
            confirm_password_error.setVisibility(View.VISIBLE);
        } else {

            String newusername = user_name_edit.getText().toString();
            String newpassword = Encrypt.md5(password_edit.getText().toString());
            String confirmpwd = Encrypt.md5(confirm_password_edit.getText().toString());

             registerDialog = new Tools.MyprocessDialog(this, "注册中，请稍后...");

             registerDialog.show();
            Thread loginThread = new Thread(new RegisterThread());
            loginThread.start();
        }

    }

    // 初始化HttpClient，并设置超时  
    public HttpClient getHttpClient() {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpClient client = new DefaultHttpClient(httpParams);
        return client;
    }

    private boolean registerServer(String username, String password) {
        boolean loginValidate = false;
        // 使用apache HTTP客户端实现  
        String urlStr = "http://192.168.0.111:9090/Fun/user/register";
       
        try {
            JSONObject json = new JSONObject();
            json.put("loginId", username);
            json.put("password", password);
            
            responseMsg = Tools.PostJsonMessage(urlStr, json.toString());
            loginValidate=true;
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return loginValidate;
    }

    // Handler  
    Handler handler = new Handler() {
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case 0:
                                    registerDialog.cancel();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", user_name_edit.getText().toString());
                                    bundle.putString("password", password_edit.getText().toString());
                                    Intent intent = new Intent();
                                    intent.putExtras(bundle);
                                    // 返回intent  
                                    setResult(RESULT_OK, intent);
                                    RegisterActivity.this.finish();
                                    Toast.makeText(RegisterActivity.this, "注册成功", 1).show();
                                    break;
                                case 1:
                                    registerDialog.cancel();
                                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    registerDialog.cancel();
                                    Toast.makeText(getApplicationContext(), "服务器连接失败！", Toast.LENGTH_SHORT).show();
                                    break;

                            }

                        }
                    };

    // RegisterThread线程类  
    class RegisterThread implements Runnable {

        @Override
        public void run() {
            String username = user_name_edit.getText().toString();
            String password = Encrypt.md5(password_edit.getText().toString());

            // URL合法，但是这一步并不验证密码是否正确  
            boolean registerValidate = registerServer(username, password);
            // System.out.println("----------------------------bool is :"+registerValidate+"----------response:"+responseMsg);  
            Message msg = handler.obtainMessage();
            try {
                JSONObject jsonObj = new JSONObject(responseMsg);
                String state=jsonObj.getString("code");
                if (registerValidate) {
                    if (state.equals("200")) {
                        msg.what = 0;
                        handler.sendMessage(msg);
                    } else {
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                } else {
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            } catch (JSONException e) {
                Log.e("json parse error", e.getMessage());
            }
        }

    }

}