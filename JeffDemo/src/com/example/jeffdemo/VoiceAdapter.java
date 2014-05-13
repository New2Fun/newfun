package com.example.jeffdemo;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
//import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VoiceAdapter extends BaseAdapter {

	private List<VoiceEntity> _voicelist = null;
	private LayoutInflater _inflater = null;
	private AnimationDrawable _anim = null; 
	private Context _ctxt;
	private TextView _textview = null;
	private MediaPlayer mp = null;
		
	public VoiceAdapter(Context context, List<VoiceEntity> coll){
		_voicelist = coll;
		_inflater = LayoutInflater.from(context);
		_ctxt = context;
		
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _voicelist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position < 0 || position >= _voicelist.size())
		    return null;
		
		return _voicelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	static class ViewHolder {
		//@ViewInject(R.id.tv_sendtime)
		public TextView tvSendTime;
		//@ViewInject(R.id.tv_username)
		//public TextView tvUserName;
		//@ViewInject(R.id.tv_chatcontent)
		public TextView tvContent;
		//@ViewInject(R.id.tv_time)
		public TextView tvTime;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final VoiceEntity entity = _voicelist.get(position);

		ViewHolder viewHolder = new ViewHolder();
		
		if (convertView == null) {
		    convertView = _inflater.inflate(
						R.layout.voice_msg, null);
		    
			viewHolder.tvSendTime = (TextView) convertView
			.findViewById(R.id.tv_sendtime);
			//viewHolder.tvUserName = (TextView) convertView
			//.findViewById(R.id.tv_username);
	        viewHolder.tvContent = (TextView) convertView
			.findViewById(R.id.tv_chatcontent);
	        viewHolder.tvTime = (TextView) convertView
			.findViewById(R.id.tv_time);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSendTime.setText(entity.getDate());
		
		viewHolder.tvContent.setText("         ");		
		viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.playanim, 0);
		viewHolder.tvTime.setText(entity.getTime());
		
		viewHolder.tvContent.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				StopAnim();
				if(_textview != v) {
				    StartAnim(v);
				    StartPlayer();
				}
				else
				{
					_textview = null;
				}
			}
		});
		
		//viewHolder.tvUserName.setText(entity.getName());
		
		return convertView;
	}
	
	private void StopAnim() {
		if(_anim != null)
		{
			_anim.stop();
			if(_textview != null)
			    _textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.playanim, 0);
			_anim = null;
		}
	}
	
	private void StartAnim(View v) {
		TextView tmp = (TextView)v;		
		_anim = (AnimationDrawable)VoiceAdapter.this._ctxt.getResources().getDrawable(R.drawable.playanim);
		_textview = tmp; 
		tmp.setCompoundDrawablesWithIntrinsicBounds(null, null, _anim, null);
		_anim.start();
        Log.e("VoiceAdapter", "播放动画");
	}
	
	private void StartPlayer()
	{
		 try {
			 mp = new MediaPlayer();
             mp.setDataSource(android.os.Environment.getExternalStorageDirectory()+ "/test");
             mp.prepare();
             mp.start();
          } catch (IllegalArgumentException e) {
             e.printStackTrace();
          } catch (IllegalStateException e) {
             e.printStackTrace();
          } catch (IOException e) {
             e.printStackTrace();
          }
          
          mp.setOnCompletionListener(new OnCompletionListener(){
             @Override
             public void onCompletion(MediaPlayer mp) {
                 mp.release();
                 StopAnim();
                 mp = null;
             }
          });
	    
	}

}
