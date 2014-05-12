package com.example.jeffdemo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class VoiceActivity extends Activity {

	@ViewInject(R.id.voicelist)
	ListView _voicelist;
	
	VoiceAdapter _adapter = null;

	private List<VoiceEntity> _dataarrays = new ArrayList<VoiceEntity>();
	
	private String[] msgArray = new String[] { "有人就有恩怨","有恩怨就有江湖","人就是江湖","你怎么退出？ ","生命中充满了巧合","两条平行线也会有相交的一天。"};

	private String[] dataArray = new String[] { "2012-10-31 18:00",
			"2012-10-31 18:10", "2012-10-31 18:11", "2012-10-31 18:20",
			"2012-10-31 18:30", "2012-10-31 18:35"};
	private final static int COUNT = 6;
	
	/*private List<? extends Map<String, ?>> getData() {
		// TODO Auto-generated method stub
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "IP切换");
		map.put("info", "通过域名切换IP");
		map.put("img", R.drawable.host);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "定位");
		map.put("info", "使用高德SDK实现LBS功能");
		map.put("img", R.drawable.place);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "语音");
		map.put("info", "录音、播放功能");
		map.put("img", R.drawable.voice);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "图片");
		map.put("info", "照相、图片选择以及图片剪切");
		map.put("img", R.drawable.camera);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "微信分享");
		map.put("info", "通过微信SDK实现分享");
		map.put("img", R.drawable.share);
		list.add(map);
    	
		return list;
	}*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice);
		
		ViewUtils.inject(this);
		
		for (int i = 0; i < COUNT; i++) {
			VoiceEntity entity = new VoiceEntity();
			entity.setDate(dataArray[i]);
			if (i % 2 == 0) {
				entity.setName("白富美");
			} else {
				entity.setName("高富帅");
			}

			entity.setText(msgArray[i]);
			_dataarrays.add(entity);
		}
	
		_adapter = new VoiceAdapter(this, _dataarrays);
		
		//SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.mainlist,
		//		new String[]{"title","info","img"},
		//		new int[]{R.id.listtitle,R.id.listinfo,R.id.listimg});
		
		_voicelist.setAdapter(_adapter);
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

}
