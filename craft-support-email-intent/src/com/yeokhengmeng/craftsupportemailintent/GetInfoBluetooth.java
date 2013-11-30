package com.yeokhengmeng.craftsupportemailintent;

import java.util.ArrayList;

import android.Manifest.permission;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class GetInfoBluetooth extends GetInfoAbstract{

	public static final String PERMISSION_ACCESS_BLUETOOTH_STATE = permission.BLUETOOTH;
	
	private BluetoothAdapter btAdapter;
	public GetInfoBluetooth(Context context) {
		super(context);
		if(checkPermission(PERMISSION_ACCESS_BLUETOOTH_STATE)){
			btAdapter = BluetoothAdapter.getDefaultAdapter();
		}
	}
	
	

	@TargetApi(Build.VERSION_CODES.FROYO)
	public boolean isBluetoothSupported(){
		if(getVersion() >= android.os.Build.VERSION_CODES.FROYO){
			return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
		} else if(btAdapter == null){
			return false;
		} else {
			return true;
		}
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public boolean isBluetoothLowEnergySupported(){
		if(getVersion() >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2){
			return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
		} else {
			return false;
		}
	}
	
	public String getBluetoothAdapterName(){
		if(btAdapter == null){
			return NO_PERMISSION;
		} else {
			return btAdapter.getName();
		}
	}
	
	public String getBluetoothAdapterMac(){
		if(btAdapter == null){
			return NO_PERMISSION;
		} else {
			return btAdapter.getAddress();
		}
	}
	
	public boolean isBluetoothEnabled(){
		if(btAdapter == null){
			return false;
		} else {
			return btAdapter.isEnabled();
		}
	}
	
	public boolean isBluetoothDiscovering(){
		if(btAdapter == null){
			return false;
		} else {
			return btAdapter.isDiscovering();
		}
	}



	@Override
	public String getBasicDetailsOnly() {
		String phoneDetails = "<<Bluetooth>>\n";
		
		ArrayList<String> details = new ArrayList<String>();
		
		details.add("Exists: " + isBluetoothSupported());
		details.add("BLE: " + isBluetoothLowEnergySupported());
		details.add("Enabled: " + isBluetoothEnabled());
		details.add("Discovering: " + isBluetoothDiscovering());

		
		for(String detail : details){
			phoneDetails += detail + "\n";
		}
		
		return phoneDetails;
	}


	@Override
	public String getAllDetails() {
		String phoneDetails = getBasicDetailsOnly();
		ArrayList<String> details = new ArrayList<String>();
		
		details.add("Name: " + getBluetoothAdapterName());
		details.add("Mac: " + getBluetoothAdapterMac());
		
		
		for(String detail : details){
			phoneDetails += detail + "\n";
		}
		
		return phoneDetails;
	}

}
