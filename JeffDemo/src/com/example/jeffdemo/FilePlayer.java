package com.example.jeffdemo;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class FilePlayer {

	private String _filepath = "";
	private MediaPlayer _player = null;
	private FinishPlayer _finishcb = null;	

	public FilePlayer(String filepath, FinishPlayer finishcb) {
		_filepath = filepath;
		_finishcb = finishcb;
	}
	
	public static int getDuration(String filepath) {
		int duration = 0;
		
		MediaPlayer player = new MediaPlayer();
		
		try {			
			player.setDataSource(filepath);
			player.prepare();
			duration = player.getDuration();
			player.release();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			player.release();
			return 0;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			player.release();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			player.release();
			return 0;
		}
		
		Log.e("FilePlayer", "Duration: " + duration);
		
		return (int)(duration / 1000.0 + 0.5);
	}

	public void startPlayer() {
		stopPlayer();
		
		try {
			_player = new MediaPlayer();
			_player.setDataSource(_filepath);
			_player.prepare();
			_player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			_player = null;
			return;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			_player = null;
			return;
		} catch (IOException e) {
			e.printStackTrace();
			_player = null;
			return;
		}

		_player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
				_player = null;
				if (_finishcb != null) {
					_finishcb.onCompletion();
				}
			}
		});
	}
	
	public void stopPlayer() {
		if(_player != null)
		{
			_player.stop();
			_player.release();
			_player = null;		
			if (_finishcb != null) {
				_finishcb.onCompletion();
			}
		}
	}

	public interface FinishPlayer {
		public abstract void onCompletion();
	}
}
