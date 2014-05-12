package com.example.jeffdemo;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Service;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class AmapActivity extends Activity implements SensorEventListener {

	SensorManager sensorManager = null;  
    Vibrator vibrator = null;
    
    public AmapLbsDemo _lbsdemo = null;
    
    int _successcnt = 0;
    
    @ViewInject(R.id.textPlace)
    TextView _placeinfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amap);
		
		ViewUtils.inject(this);		
		
		_lbsdemo = new AmapLbsDemo(this);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
	}

	public void RefreshPlaceInfo(String info) {
		_placeinfo.setText(info);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.amap, menu);
		return true;
	}
	
	@Override  
    protected void onPause()  
    {  
        super.onPause();  
        sensorManager.unregisterListener(this); 
        _successcnt = 0;
        _lbsdemo.stopLocation();
    }  
  
    @Override  
    protected void onResume()  
    {  
        super.onResume();  
        _successcnt = 0;
        sensorManager.registerListener(this,  
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),  
                SensorManager.SENSOR_DELAY_NORMAL);  
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		 int sensorType = event.sensor.getType();  
	        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴  
	        float[] values = event.values;  
	        if (sensorType == Sensor.TYPE_ACCELEROMETER)  
	        {  
	            if ((Math.abs(values[0]) > 17 || Math.abs(values[1]) > 17 || Math  
	                    .abs(values[2]) > 17))  
	            {  
	            	_successcnt++;
	                Log.d("sensor x ", "============ values[0] = " + values[0]);  
	                Log.d("sensor y ", "============ values[1] = " + values[1]);  
	                Log.d("sensor z ", "============ values[2] = " + values[2]);  
	                //tv.setText("摇一摇成功!!!");  
	                //摇动手机后，再伴随震动提示~~ 
	                if(_successcnt > 2)
	                {
		                vibrator.vibrate(500);  
		                _lbsdemo.startLocation();
	                	_successcnt = 0;
	                }
	            }  
	  
	        }
	}

}
