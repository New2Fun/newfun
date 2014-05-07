package com.example.android.textswitcher;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.util.Encrypt;
import com.example.android.util.Tools;

public class LoginActivity extends Activity {

    private EditText          userName, password;
    private CheckBox          rem_pw, auto_login;
    private Button            btn_login;
    private Button            btnRegister;
    private Button            btnQuit;
    private String            userNameValue, passwordValue;
    private SharedPreferences sp;
    private String            httppResponseMsg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除标题  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_main);

        //获得实例对象  
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userName = (EditText) findViewById(R.id.et_zh);
        password = (EditText) findViewById(R.id.et_mima);
        rem_pw = (CheckBox) findViewById(R.id.cb_mima);
        auto_login = (CheckBox) findViewById(R.id.cb_auto);
        btn_login = (Button) findViewById(R.id.btn_login);
        btnQuit = (Button) findViewById(R.id.quit);
        btnRegister = (Button) findViewById(R.id.register);
        initView();
    }

    public void initView() {
        //判断记住密码多选框的状态  
        if (sp.getBoolean("ISCHECK", false)) {
            //设置默认是记录密码状态  
            rem_pw.setChecked(true);
            userName.setText(sp.getString("USER_NAME", ""));
            password.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态  
            if (sp.getBoolean("AUTO_ISCHECK", false)) {
                //设置默认是自动登录状态  
                auto_login.setChecked(true);
                //跳转界面  
                Intent intent = new Intent(LoginActivity.this, LogoActivity.class);
                LoginActivity.this.startActivity(intent);

            }
        }

        // 登录监听事件  
        btn_login.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
               
               //登陆操作
                new Thread(new LoginThread()).start();
            }
        });

        //监听记住密码多选框按钮事件  
        rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rem_pw.isChecked()) {

                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                } else {

                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();
                    userName.setText("");
                    password.setText("");
                }

            }
        });

        //监听自动登录多选框事件  
        auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login.isChecked()) {
                    System.out.println("自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });

        btnQuit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

    }

    private Handler loginHandler = new Handler() {
          public void handleMessage(Message msg) {
              switch (msg.what) {
                  case 0:
                      callLoginSuccess();
                      break;
                  case 1:
                      Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
                      break;
                  default:
                      break;
              
             }
          }
    };
    
    private void callLoginSuccess(){
        Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();  
        //登录成功和记住密码框为选中状态才保存用户信息  
        if(rem_pw.isChecked())  
        {  
         //记住用户名、密码、  
          Editor editor = sp.edit();  
          editor.putString("USER_NAME", userNameValue);  
          editor.putString("PASSWORD",passwordValue);  
          editor.commit();  
        }  
        //跳转界面  
        Intent intent = new Intent(LoginActivity.this,LogoActivity.class);  
        LoginActivity.this.startActivity(intent); 
    }
    
   
    private boolean loginServer(String username, String password) {
        boolean loginValidate = false;
        // 使用apache HTTP客户端实现  
        String urlStr = "http://10.192.1.193:9090/Fun/user/login";

        try {
            JSONObject json = new JSONObject();
            json.put("loginId", username);
            json.put("password", password);

            httppResponseMsg = Tools.PostJsonMessage(urlStr, json.toString());
            loginValidate = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loginValidate;
    }

    // RegisterThread线程类  
    class LoginThread implements Runnable {

        @Override
        public void run() {
            String username = userName.getText().toString();
            String passwd = Encrypt.md5(password.getText().toString());

            // URL合法，但是这一步并不验证密码是否正确  
            boolean registerValidate = loginServer(username, passwd);
            // System.out.println("----------------------------bool is :"+registerValidate+"----------response:"+responseMsg);  
            Message msg = loginHandler.obtainMessage();
            try {
                JSONObject jsonObj = new JSONObject(httppResponseMsg);
                String state = jsonObj.getString("code");
                if (registerValidate) {
                    if (state.equals("200")) {
                        msg.what = 0;
                        loginHandler.sendMessage(msg);
                    } else {
                        msg.what = 1;
                        loginHandler.sendMessage(msg);
                    }

                } else {
                    msg.what = 2;
                    loginHandler.sendMessage(msg);
                }
            } catch (JSONException e) {
                Log.e("json parse error", e.getMessage());
            }
        }

    }
}