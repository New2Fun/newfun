package com.example.jeffdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	@ViewInject(R.id.mainlist)
	ListView _mainlist;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        AmapActivity._lbsdemo = new AmapLbsDemo(this);
         
        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.mainlist,
				new String[]{"title","info","img"},
				new int[]{R.id.listtitle,R.id.listinfo,R.id.listimg});
        
        ViewUtils.inject(this);
        
        _mainlist.setAdapter(adapter);
        
        _mainlist.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				ListView myListView=(ListView)parent;  
				   HashMap<String, Object> item = (HashMap<String, Object>)myListView.getItemAtPosition(position);  
				   Toast.makeText(MainActivity.this, item.get("title").toString() , Toast.LENGTH_SHORT).show(); 
				   
				   Integer itemid = (Integer) item.get("img");
				   			       				   
				   switch(itemid)
				   {
				   case R.drawable.host:
					   Intent swhostintent = new Intent(MainActivity.this, SwitchHostActivity.class);
					   MainActivity.this.startActivity(swhostintent);
					   break;
				   case R.drawable.place:
					   Intent amapintent = new Intent(MainActivity.this, AmapActivity.class);
					   MainActivity.this.startActivity(amapintent);
					   break;
				   case R.drawable.voice:
					   Intent voiceintent = new Intent(MainActivity.this, VoiceActivity.class);
					   MainActivity.this.startActivity(voiceintent);
					   break;
				   case R.drawable.camera:
					   Intent cameraintent = new Intent(MainActivity.this, CameraActivity.class);
					   MainActivity.this.startActivity(cameraintent);
					   break;
				   case R.drawable.share:
					   Intent wxshareintent = new Intent(MainActivity.this, WXShareActivity.class);
					   MainActivity.this.startActivity(wxshareintent);
					   break;
				   }
			}
        	
        });
    }
 
    private List<? extends Map<String, ?>> getData() {
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
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
