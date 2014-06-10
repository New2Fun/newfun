package com.example.jeffdemo;

import android.content.Context;
import android.media.AudioManager;

public class PlayerVolume {
   
	private AudioManager _audiomgr;
	private int _maxvolume = 0;
	private int _curvolume = 0;
	private int _prevolume = 0;
	private int _stepvolume = 0;
	
	public PlayerVolume(Context context){
		_audiomgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		
		_maxvolume = _audiomgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		_curvolume = _audiomgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		_stepvolume = _maxvolume / 6;
		_prevolume = _curvolume;
    }
	
	public void ResumeVolume(){
		_audiomgr.setStreamVolume(AudioManager.STREAM_MUSIC, _prevolume,   
                AudioManager.FLAG_PLAY_SOUND);
		_curvolume = _prevolume;
	}
	
	public void MaxVolume() {
		_prevolume = _audiomgr.getStreamVolume(AudioManager.STREAM_MUSIC);;
		
		_audiomgr.setStreamVolume(AudioManager.STREAM_MUSIC, _maxvolume,   
                AudioManager.FLAG_PLAY_SOUND);
		
		_curvolume = _maxvolume;
	}
	
	public void MinVolume() {
		_prevolume = _audiomgr.getStreamVolume(AudioManager.STREAM_MUSIC);;
		
		_audiomgr.setStreamVolume(AudioManager.STREAM_MUSIC, 0,   
                AudioManager.FLAG_PLAY_SOUND);	
		
		_curvolume = 0;
	}
	
	public void MiddleVolume() {
		_prevolume = _audiomgr.getStreamVolume(AudioManager.STREAM_MUSIC);;
		
		_audiomgr.setStreamVolume(AudioManager.STREAM_MUSIC, _maxvolume / 2,   
                AudioManager.FLAG_PLAY_SOUND);
		
		_curvolume = _maxvolume / 2;
	}
	
	public void AddVolume() {
		_prevolume = _audiomgr.getStreamVolume(AudioManager.STREAM_MUSIC);;
		
		_curvolume += _stepvolume;
		
		if(_curvolume >= _maxvolume){
			_curvolume = _maxvolume;
		}
		_audiomgr.setStreamVolume(AudioManager.STREAM_MUSIC, _curvolume,   
                AudioManager.FLAG_PLAY_SOUND);
	}
	
	public void DecVolume() {
		_prevolume = _audiomgr.getStreamVolume(AudioManager.STREAM_MUSIC);;
		
        _curvolume -= _stepvolume;
		
		if(_curvolume <= 0){
			_curvolume = 0;			
		}
		_audiomgr.setStreamVolume(AudioManager.STREAM_MUSIC, _curvolume,   
                AudioManager.FLAG_PLAY_SOUND);
	}
}
