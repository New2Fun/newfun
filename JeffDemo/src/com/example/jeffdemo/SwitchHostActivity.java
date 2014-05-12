package com.example.jeffdemo;

import java.io.IOException;
import java.net.InetAddress;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SwitchHostActivity extends Activity {

	@ViewInject(R.id.hostname)
	private EditText _hostname;

	@ViewInject(R.id.testaddr)
	private EditText _testaddr;

	@ViewInject(R.id.formaladdr)
	private EditText _formaladdr;

	@ViewInject(R.id.hostaddr)
	private TextView _hostaddr;

	private String _name = "test";

	private Process _process = null;
	private HostFileOperator _operator = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_switch_host);
		
		// 需要root权限
		try {
			if (this._process == null) {
				this._process = Runtime.getRuntime().exec("su");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		ViewUtils.inject(this);

		_hostname.setText(_name);
		_testaddr.setText("192.168.1.100");
		_formaladdr.setText("192.168.1.101");
		
		// 初始化/etc/hosts文件操作类
		_operator = new HostFileOperator(this.getApplicationContext());

		// 刷新主机ip
		RefreshIP();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.switch_host, menu);
		return true;
	}

	@OnClick({ R.id.testbtn })
	public void clickTestBtn(View v) {
		_name = _hostname.getText().toString();
		String test = _testaddr.getText().toString();

		if (_operator.isExistHost(_name)) {
			_operator.ModifyHost(_name, test);
		} else {
			_operator.AddHost(_name, test);
		}

		_operator.setActivityHost(_name, _process);
		
		try {
			this._process = Runtime.getRuntime().exec("su");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@OnClick({ R.id.formalbtn })
	public void clickFormalBtn(View v) {
		_name = _hostname.getText().toString();
		String formal = _formaladdr.getText().toString();

		if (_operator.isExistHost(_name)) {
			_operator.ModifyHost(_name, formal);
		} else {
			_operator.AddHost(_name, formal);
		}

		_operator.setActivityHost(_name, _process);
		
		try {
			this._process = Runtime.getRuntime().exec("su");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@OnClick({ R.id.refreshhostbtn })
	public void clickRefreshBtn(View v) {
		RefreshIP();
	}

	private void RefreshIP() {
		String host = _operator.getActivityHost();
		_hostaddr.setText(host);
	}

}
