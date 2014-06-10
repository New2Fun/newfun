package com.example.jeffdemo;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class VoiceActivity extends Activity {

	@ViewInject(R.id.voicelist)
	ListView _voicelist;

	@ViewInject(R.id.saybtn)
	Button _saybtn;
	
	@ViewInject(R.id.rcdcancel)
	LinearLayout _rcdcancel;
	
	@ViewInject(R.id.rcdhint)
	LinearLayout _rcdhint;
	
	@ViewInject(R.id.ampbkg)
	ImageView _ampbkg;

	VoiceAdapter _adapter = null;

	private List<VoiceEntity> _dataarrays = new ArrayList<VoiceEntity>();

	private int _screenwidth = 0;
	private int _screenheight = 0;

	private AmrRecoder _amrrecoder = null;
	private Handler _handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice);

		ViewUtils.inject(this);

		_screenwidth = getWindowManager().getDefaultDisplay().getWidth();
		_screenheight = getWindowManager().getDefaultDisplay().getHeight();

		_adapter = new VoiceAdapter(this, _dataarrays);

		_voicelist.setAdapter(_adapter);
		
		_rcdcancel.setVisibility(View.GONE);
		_rcdhint.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.voice, menu);
		return true;
	}

	private String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}

	@OnTouch({ R.id.saybtn })
	public boolean TouchSayBtn(View v, MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (_screenheight * 0.8 <= event.getRawY()) {
				if (!Environment.getExternalStorageDirectory().exists()) {
					Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
					return false;
				}
				// Log.i("VoiceActivity", "松开 结束");

				_amrrecoder = new AmrRecoder();

				_amrrecoder.startRecoder(Environment
						.getExternalStorageDirectory()
						+ "/"
						+ SystemClock.currentThreadTimeMillis() + ".amr");
				
				_rcdhint.setVisibility(View.VISIBLE);
				
				_handler.postDelayed(mPollTask, POLL_INTERVAL);
			}
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (_screenheight * 0.8 <= event.getRawY()) {
				_saybtn.setText("松开 结束");
				_rcdcancel.setVisibility(View.GONE);
				_rcdhint.setVisibility(View.VISIBLE);
			} else {
				_saybtn.setText("松开手指   取消发送");
				_rcdcancel.setVisibility(View.VISIBLE);
				_rcdhint.setVisibility(View.GONE);
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			_saybtn.setText(R.string.say);
			
			if (_screenheight * 0.8 <= event.getRawY()) {
				// Log.i("VoiceActivity", "松开 结束");
				
				if (_amrrecoder != null) {	
					String filepath = _amrrecoder.getFilePath();
					long duration = _amrrecoder.stopRecoder();
					Log.i("VoiceActivity", "录音时间:" + duration);
					VoiceEntity entity = new VoiceEntity();
					entity.setDate(getDate());
					entity.setTime(duration + "\"");
					entity.setText(filepath);	
					_dataarrays.add(entity);
					_adapter.notifyDataSetChanged();
					_voicelist.setSelection(_voicelist.getCount() - 1);
					
					_amrrecoder = null;
				}
				
			} else {
				// Log.i("VoiceActivity", "松开手指   取消发送");
				
				if (_amrrecoder != null) {
					
					File file = new File(_amrrecoder.getFilePath());
					if (file.exists()) {
						file.delete();
					}
					
					_amrrecoder.stopRecoder();
					
					_amrrecoder = null;
				}

			}
			
			_rcdcancel.setVisibility(View.GONE);
			_rcdhint.setVisibility(View.GONE);
			
			_handler.removeCallbacks(mPollTask);
		}

		return true;
	}
	
	private static final int POLL_INTERVAL = 300;
	
    private void updateDisplay(double signalEMA) {
		
		switch ((int) signalEMA) {
		case 0:
		case 1:
			_ampbkg.setBackgroundResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			_ampbkg.setBackgroundResource(R.drawable.amp2);			
			break;
		case 4:
		case 5:
			_ampbkg.setBackgroundResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			_ampbkg.setBackgroundResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			_ampbkg.setBackgroundResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			_ampbkg.setBackgroundResource(R.drawable.amp6);
			break;
		default:
			_ampbkg.setBackgroundResource(R.drawable.amp7);
			break;
		}
	}
	
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = _amrrecoder.getAmplitude();
			updateDisplay(amp);
			_handler.postDelayed(mPollTask, POLL_INTERVAL);
		}
	};

}
