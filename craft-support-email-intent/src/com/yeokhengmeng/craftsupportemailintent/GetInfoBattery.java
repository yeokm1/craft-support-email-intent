package com.yeokhengmeng.craftsupportemailintent;

import java.util.ArrayList;

import android.Manifest.permission;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class GetInfoBattery extends GetInfoAbstract {

	public static final String PERMISSION_BATTERY_STATE = permission.BATTERY_STATS;
	public static final int DEFAULT_BATTERY_TEMP = -2732;
	
	private Intent batteryIntent;

	private BroadcastReceiver receiver = null;

	public GetInfoBattery(Context context) {
		super(context);
		if(checkPermission(PERMISSION_BATTERY_STATE)){
			receiver = new BattReceiver();
			batteryIntent = context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			unregisterReceiver();
		}
	}


	public boolean isBatteryExisting(){
		if(batteryIntent == null){
			return false;
		}

		boolean exist = batteryIntent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
		return exist;
	}

	public int getBatteryTemp(){
		if(batteryIntent == null){
			return DEFAULT_BATTERY_TEMP;
		}

		//As return value seems to be in integer form *10
		int temp = batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, DEFAULT_BATTERY_TEMP);


		return temp;
	}

	public String getBatteryTech(){
		if(batteryIntent == null){
			return UNKNOWN;
		}
		String battTech = batteryIntent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
		if(battTech == null){
			return UNKNOWN;
		} else {
			return battTech;
		}
	}


	public float getBatteryLevel() {
		if(batteryIntent == null){
			return -1.0f;
		}

		int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		// Error checking that probably isn't needed but I added just in case.
		if(level == -1 || scale == -1) {
			return -1.0f;
		}

		return ((float)level / (float)scale) * 100.0f; 
	}

	public String getBatteryStatus(){
		if(batteryIntent == null){
			return NO_PERMISSION;
		}
		int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		switch(status){
		case BatteryManager.BATTERY_STATUS_CHARGING : return "Charging";
		case BatteryManager.BATTERY_STATUS_DISCHARGING : return "Discharging";
		case BatteryManager.BATTERY_STATUS_FULL : return "Battery Full";
		case BatteryManager.BATTERY_STATUS_NOT_CHARGING : return "Not Charging";
		case BatteryManager.BATTERY_STATUS_UNKNOWN : 
			//Fallthrough
		default: 
			return UNKNOWN;
		}
	}

	public String getBatteryPlugged(){
		if(batteryIntent == null){
			return NO_PERMISSION;
		}

		int plugged = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		switch(plugged){
		case BatteryManager.BATTERY_PLUGGED_AC : return "AC";
		case BatteryManager.BATTERY_PLUGGED_USB : return "USB";
		case BatteryManager.BATTERY_PLUGGED_WIRELESS : return "Wireless";
		default: return UNKNOWN;
		}
	}

	public String getBatteryHealth(){
		if(batteryIntent == null){
			return NO_PERMISSION;
		}
		int health = batteryIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
		switch(health){
		case BatteryManager.BATTERY_HEALTH_COLD : return "Cold";
		case BatteryManager.BATTERY_HEALTH_DEAD : return "Dead";
		case BatteryManager.BATTERY_HEALTH_GOOD : return "Good";
		case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE : return "Over Voltage";
		case BatteryManager.BATTERY_HEALTH_OVERHEAT : return "Overheat";
		case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE : return "Unspecified Failure";
		case BatteryManager.BATTERY_HEALTH_UNKNOWN : 
			//Fallthrough
		default: 
			return UNKNOWN;
		}
	}


	@Override
	public String getBasicDetailsOnly() {
		String phoneDetails = "<<Battery>>\n";

		ArrayList<String> details = new ArrayList<String>();

		try{
			details.add("Access Battery State Permission: " + checkPermission(PERMISSION_BATTERY_STATE));
			details.add("Exists: " + isBatteryExisting());
			details.add("Level: " + getBatteryLevel() + "%");
			details.add("Status: " + getBatteryStatus());
			details.add("Plugged: " + getBatteryPlugged());
		} catch (Exception e){
			details.add(e.toString());
		}

		for(String detail : details){
			phoneDetails += detail + "\n";
		}

		return phoneDetails;
	}


	@Override
	public String getAllDetails() {
		String phoneDetails = getBasicDetailsOnly();
		ArrayList<String> details = new ArrayList<String>();

		
		float actualTemperature = ((float) getBatteryTemp()) * 0.1f;
		try{
			details.add("Temperature: " + String.format("%.1f", actualTemperature) + "C");
			details.add("Tech: " + getBatteryTech());
			details.add("Health: " + getBatteryHealth());
		} catch (Exception e){
			details.add(e.toString());
		}

		for(String detail : details){
			phoneDetails += detail + "\n";
		}


		return phoneDetails;
	}

	public class BattReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			//Do nothing
		}
	}

	private void unregisterReceiver(){
		if(receiver != null){
			context.unregisterReceiver(receiver);
			receiver = null;
		}
	}

}
