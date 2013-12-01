package com.yeokhengmeng.craftsupportemailintent;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.ArrayList;

import android.Manifest.permission;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

public class GetInfoWifi extends GetInfoAbstract{

	public static final String PERMISSION_ACCESS_WIFI_STATE  = permission.ACCESS_WIFI_STATE;

	private WifiManager wifiMgr;

	public GetInfoWifi(Context context) {
		super(context);
		if(checkPermission(PERMISSION_ACCESS_WIFI_STATE)){
			wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
	}



	@TargetApi(Build.VERSION_CODES.FROYO)
	public boolean isWifiSupported(){
		if(getVersion() >= android.os.Build.VERSION_CODES.FROYO){
			return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI);
		} else if(wifiMgr == null){
			return false;
		} else {
			return true;
		}
	}


	public String getWifiStatus(){
		if(wifiMgr == null){
			return NO_PERMISSION;
		}

		int state =  wifiMgr.getWifiState();

		switch(state){
		case WifiManager.WIFI_STATE_DISABLED : return "Disabled";
		case WifiManager.WIFI_STATE_DISABLING : return "Disabling";
		case WifiManager.WIFI_STATE_ENABLED : return "Enabled";
		case WifiManager.WIFI_STATE_ENABLING : return "Enabling";
		case WifiManager.WIFI_STATE_UNKNOWN : 
			//Fallthrough
		default: return UNKNOWN;
		}
	}

	public String getWifiAdapterMac(){
		if(wifiMgr == null){
			return NO_PERMISSION;
		}

		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		String adapterMac = null;
		if(wifiInfo != null){
			adapterMac = wifiInfo.getMacAddress();
		}
		if(adapterMac == null){
			return UNKNOWN;
		} else {
			return adapterMac;
		}
	}


	public String getConnectedSSID(){
		if(wifiMgr == null){
			return NO_PERMISSION;
		}

		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		String ssid = null;
		if(wifiInfo != null){
			ssid = wifiInfo.getSSID();
		}
		if(ssid == null){
			return UNKNOWN;
		} else {
			return ssid;
		}
	}

	public String getConnectedIP(){
		if(wifiMgr == null){
			return UNKNOWN;
		}

		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

		if(wifiInfo != null){
			int ipAddress = wifiInfo.getIpAddress();

			// Convert little-endian to big-endianif needed
			if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
				ipAddress = Integer.reverseBytes(ipAddress);
			}

			byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

			String ipAddressString;
			try {
				ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
			} catch (UnknownHostException ex) {
				return UNKNOWN;

			}

			return ipAddressString;
		} else {
			return UNKNOWN;
		}


	}

	public String getConnectedMac(){
		if(wifiMgr == null){
			return NO_PERMISSION;
		}

		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		String mac = null;
		if(wifiInfo != null){
			mac = wifiInfo.getBSSID();
		}
		if(mac == null){
			return UNKNOWN;
		} else {
			return mac;
		}
	}

	public int getConnectedSpeed(){
		if(wifiMgr == null){
			return 0;
		}

		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		int speed = 0;
		if(wifiInfo != null){
			speed = wifiInfo.getLinkSpeed();
		}

		return speed;
	}

	public int getConnectedRSSI(){
		if(wifiMgr == null){
			return 0;
		}

		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		int ip = 0;
		if(wifiInfo != null){
			ip = wifiInfo.getRssi();
		}

		return ip;
	}


	@Override
	public String getBasicDetailsOnly() {
		String phoneDetails = "<<Wifi>>\n";

		ArrayList<String> details = new ArrayList<String>();
		
		details.add("Access Wifi State Permission: " + checkPermission(PERMISSION_ACCESS_WIFI_STATE));
		details.add("Exists: " + isWifiSupported());
		details.add("Status: " + getWifiStatus());

		for(String detail : details){
			phoneDetails += detail + "\n";
		}

		return phoneDetails;
	}


	@Override
	public String getAllDetails() {
		String phoneDetails = getBasicDetailsOnly();
		ArrayList<String> details = new ArrayList<String>();

		details.add("Mac address: " + getWifiAdapterMac());
		details.add("IP address: " + getConnectedIP());
		details.add("AP SSID: " + getConnectedSSID());
		details.add("AP address: " + getConnectedMac());
		details.add("Speed: " + getConnectedSpeed());
		details.add("RSSI: " + getConnectedRSSI());


		for(String detail : details){
			phoneDetails += detail + "\n";
		}

		return phoneDetails;
	}

}
