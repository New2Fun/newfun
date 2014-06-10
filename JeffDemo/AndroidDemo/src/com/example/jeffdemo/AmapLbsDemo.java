package com.example.jeffdemo;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

public class AmapLbsDemo implements AMapLocationListener, Runnable {

	private AmapActivity _activity = null;

	private LocationManagerProxy _maplocmanager = null;
	private AMapLocation _maplocation = null;// 用于判断定位超时
	private Handler _handler = new Handler();

	public AmapLbsDemo(AmapActivity amapActivity) {
		_activity = amapActivity;
		
		_activity.RefreshPlaceInfo("");
	}

	public void startLocation() {
		stopLocation();
		_maplocmanager = LocationManagerProxy.getInstance(_activity);
		_maplocmanager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 10 * 60 * 1000, 10, this);
		_handler.postDelayed(this, 12000);// 设置超过12秒还没有定位到就停止定位
		_activity.RefreshPlaceInfo("定位中...");
	}

	public void stopLocation() {
		// TODO Auto-generated method stub
		if (_maplocmanager != null) {
			_maplocmanager.removeUpdates(this);
			_maplocmanager.destory();
		}
		_maplocation = null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (_maplocation == null) {
			_activity.RefreshPlaceInfo("12秒内还没有定位成功，停止定位");
			stopLocation();// 销毁掉定位
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		if (location != null) {
			this._maplocation = location;// 判断超时机制
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
					+ "\n精    度    :" + location.getAccuracy() + "米"
					+ "\n定位方式:" + location.getProvider() + "\n城市编码:" + cityCode
					+ "\n位置描述:" + desc + "\n省:" + location.getProvince()
					+ "\n市:" + location.getCity() + "\n区(县):"
					+ location.getDistrict() + "\n区域编码:" + location.getAdCode());
			_activity.RefreshPlaceInfo(str);		
		}
	}

}
