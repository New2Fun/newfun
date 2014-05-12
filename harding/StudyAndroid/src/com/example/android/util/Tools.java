package com.example.android.util;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class Tools {

    public static class MyprocessDialog extends ProgressDialog {

        public MyprocessDialog(Context context, String message) {
            super(context);
            this.setMessage(message);
            this.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        }

    }

    /**
     * @param path    请求的服务器URL地址
     * @param encode    编码格式
     * @return    将服务器端返回的数据转换成String
     */
    public static String PostMessage(String path) {
        String result = "";
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(path);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity, HTTP.UTF_8);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return result;
    }

    /**
     * @param path    请求的服务器URL地址
     * @param encode    编码格式
     * @return    将服务器端返回的数据转换成String
     */
    public static String PostJsonMessage(String path, String json) {
        String result = "";
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost(path);
            StringEntity params = new StringEntity(json);
            request.addHeader("content-type", "application/json;charset=utf-8");
            request.setEntity(params);
            HttpResponse httpResponse = httpClient.execute(request);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();

                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity, HTTP.UTF_8);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return result;
    }
    
    /**安装apk文件 
     * @param file
     * @param context
     */
    public static  void installApkFile(File file,Context context) {
        // TODO Auto-generated method stub
        Log.e("install file", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");

        //startActivity(intent);
        context.startActivity(intent);
    }

    public static String getLocalIPAddress(Context context) {
        // try {
        // for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
        // .getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
        // NetworkInterface intf = mEnumeration.nextElement();
        // for (Enumeration<InetAddress> enumIPAddr = intf
        // .getInetAddresses(); enumIPAddr.hasMoreElements();) {
        // InetAddress inetAddress = enumIPAddr.nextElement();
        // // 如果不是回环地址
        // if (!inetAddress.isLoopbackAddress()) {
        // // 直接返回本地IP地址
        // int i = Integer.parseInt(inetAddress.getHostAddress());
        // String ipStr=(i & 0xFF)+"."+((i>>8) & 0xFF)+"."+
        // ((i>>16) & 0xFF)+"."+(i>>24 & 0xFF);
        // return ipStr;
        // }
        // }
        // }
        // } catch (SocketException ex) {
        // Log.e("Error", ex.toString());
        // }
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        int i = info.getIpAddress();
        String ipStr = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
                + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
        return ipStr;

        // return intToIp(ip);
        // return null;
    }
}
