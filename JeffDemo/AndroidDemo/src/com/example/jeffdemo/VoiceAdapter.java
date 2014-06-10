package com.example.jeffdemo;

import java.util.List;

import com.example.jeffdemo.FilePlayer.FinishPlayer;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VoiceAdapter extends BaseAdapter {

	private List<VoiceEntity> _voicelist = null;
	private LayoutInflater _inflater = null;
	private AnimationDrawable _anim = null; 
	private Context _ctxt;
	private TextView _textview = null;
	private FilePlayer _fileplayer = null;
	private PlayerVolume _pvolume = null;
		
	public VoiceAdapter(Context context, List<VoiceEntity> coll){
		_voicelist = coll;
		_inflater = LayoutInflater.from(context);
		_ctxt = context;
		_pvolume = new PlayerVolume(context);
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
		viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chatto_voice_playing_f3, 0);
		viewHolder.tvTime.setText(entity.getTime());
		
		viewHolder.tvContent.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				boolean bstart = true;
				if(_textview == v) bstart = false;
				if(_fileplayer != null)
				{
					_fileplayer.stopPlayer();
				}
				if(bstart) {
					_fileplayer = new FilePlayer(entity.getText(),
							new FinishPlayer() {						
								@Override
								public void onCompletion() {
									// TODO Auto-generated method stub
									_pvolume.ResumeVolume();
									StopAnim();
								}						
					});
					StartAnim(v);
					_pvolume.MaxVolume();
					_fileplayer.startPlayer();
				}
			}
		});
		
		return convertView;
	}
	
	private void StopAnim() {
		if(_anim != null)
		{
			_anim.stop();			
			if(_textview != null)
			    _textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chatto_voice_playing_f3, 0);
			_textview = null;
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

}
