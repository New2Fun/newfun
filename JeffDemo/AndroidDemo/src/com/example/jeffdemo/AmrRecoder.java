package com.example.jeffdemo;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

public class AmrRecoder implements Runnable {
	static final private double EMA_FILTER = 0.6;

	private MediaRecorder _recoder = null;
	private double _EMA = 0.0;
	private Handler _handler = new Handler();
	public static final int MAX_LENGTH = 1000 * 60;
	
	private String _filepath = "";
	private long _starttime = 0;
	private long _endtime = 0;
	
    public void startRecoder(String filepath) {
    	if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
    	
    	_filepath = "";
    	
		if (_recoder == null) {
            _recoder = new MediaRecorder();
			_recoder.setAudioSource(MediaRecorder.AudioSource.MIC);
			_recoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			_recoder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			_recoder.setOutputFile(filepath);
			_recoder.setMaxDuration(MAX_LENGTH);
			_filepath = filepath;
			_handler.post(this);
		}
    }

	public String getFilePath() {
		return _filepath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(_recoder != null)
		{
			try {
				_recoder.prepare();
				_recoder.start();
				_starttime = System.currentTimeMillis();
				_EMA = 0.0;
			} catch (IllegalStateException e) {
				e.printStackTrace();
				_filepath = "";
			} catch (IOException e) {
				e.printStackTrace();
				_filepath = "";
			}
		}
	}
	
    public long stopRecoder() {
		if (_recoder != null) {			
			_recoder.stop();
			_recoder.release();
			_recoder = null;
			_filepath = "";
			_endtime = System.currentTimeMillis();
			
			return (long) ((_endtime - _starttime) / 1000.0 + 0.5);
		}
		
		return 0;
	}
    
    public double getAmplitude() {
		if (_recoder != null)
			return (_recoder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		_EMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * _EMA;
		return _EMA;
	}
    
}
