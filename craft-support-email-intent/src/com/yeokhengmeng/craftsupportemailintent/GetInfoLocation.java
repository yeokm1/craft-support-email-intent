package com.yeokhengmeng.craftsupportemailintent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.Manifest.permission;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

public class GetInfoLocation extends GetInfoAbstract {


	public static final String PERMISSION_ACCESS_FINE_LOCATION = permission.ACCESS_FINE_LOCATION;

	private LocationManager locMgr;

	public GetInfoLocation(Context context) {
		super(context);
		if(checkPermission(PERMISSION_ACCESS_FINE_LOCATION)){
			locMgr =  (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	public boolean doesGPSExist(){
		if(getVersion() >= android.os.Build.VERSION_CODES.FROYO){
			return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
		} else if(locMgr == null){
			return false;
		} else {
			List<String> allProviders = locMgr.getAllProviders();
			return allProviders.contains(LocationManager.GPS_PROVIDER);
		}
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	public boolean doesNetworkExist(){
		if(getVersion() >= android.os.Build.VERSION_CODES.FROYO){
			return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK);
		} else if(locMgr == null){
			return false;
		} else {
			List<String> allProviders = locMgr.getAllProviders();
			return allProviders.contains(LocationManager.NETWORK_PROVIDER);
		}
	}

	public boolean isGPSEnabled(){
		if(locMgr == null){
			return false;
		} else {
			return locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
		}
	}


	public boolean isNetworkEnabled(){
		if(locMgr == null){
			return false;
		} else {
			return locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}
	}

	public String getGPSLastLocation(){
		if(locMgr == null){
			return UNKNOWN;
		} else {
			Location loc =  locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			return formatLocationString(loc);
		}
	}

	public String getNetworkLastLocation(){
		if(locMgr == null){
			return UNKNOWN;
		} else {
			Location loc =  locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			return formatLocationString(loc);
		}
	}


	public String formatLocationString(Location loc){
		if(loc == null){
			return UNKNOWN;
		}
		double latitude = loc.getLatitude();
		double longitude = loc.getLongitude();
		double altitude = loc.getAltitude();
		double accuracy = loc.getAccuracy();
		double speed = loc.getSpeed();
		long time = loc.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.US);
		String dateFormated = sdf.format(new Date(time));

		return "\nLatitude: " + latitude 
				+ "\nLongitude: " + longitude 
				+ "\nAltitude: " + altitude
				+"\nAccuracy: " + accuracy
				+"\nSpeed: " + speed
				+"\nTime: " + dateFormated;


	}

	@Override
	public String getBasicDetailsOnly() {
		String phoneDetails = "<<Location Service>>\n";

		ArrayList<String> details = new ArrayList<String>();
		try{
			details.add("Access Fine Location Permission: " + checkPermission(PERMISSION_ACCESS_FINE_LOCATION));
			details.add("GPS exists: " + doesGPSExist());
			details.add("Network exists: " + doesNetworkExist());

			details.add("GPS enabled: " + isGPSEnabled());
			details.add("Network Enabled: " + isNetworkEnabled());
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
		try{
			details.add("Last GPS Location: " + getGPSLastLocation());
			details.add("Last Network Location: " + getNetworkLastLocation());
		} catch (Exception e){
			details.add(e.toString());
		}
		for(String detail : details){
			phoneDetails += detail + "\n";
		}

		return phoneDetails;
	}





}
