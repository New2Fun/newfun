package com.example.jeffdemo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VoiceAdapter extends BaseAdapter {

	private List<VoiceEntity> _voicelist = null;
	private LayoutInflater _inflater = null;
	
	public VoiceAdapter(Context context, List<VoiceEntity> coll){
		_voicelist = coll;
		_inflater = LayoutInflater.from(context);
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

		ViewHolder viewHolder = null;
		if (convertView == null) {
		    convertView = _inflater.inflate(
						R.layout.voice_msg, null);

			viewHolder = new ViewHolder();			

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
		viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chatto_voice_playing, 0);
		viewHolder.tvTime.setText(entity.getTime());
		
		/*viewHolder.tvContent.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (entity.getText().contains(".amr")) {
					playMusic(android.os.Environment.getExternalStorageDirectory()+"/"+entity.getText()) ;
				}
			}
		});*/
		
		//viewHolder.tvUserName.setText(entity.getName());
		
		return convertView;
	}

}
