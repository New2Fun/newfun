package com.example.jeffdemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AmapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.amap, menu);
		return true;
	}

}
