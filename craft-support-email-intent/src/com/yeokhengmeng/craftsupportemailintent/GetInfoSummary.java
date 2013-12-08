package com.yeokhengmeng.craftsupportemailintent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.yeokhengmeng.craftsupportemailintent.GetInfoSummary.ExecShell.SHELL_CMD;

public class GetInfoSummary extends GetInfoAbstract {


	public GetInfoSummary(Context context) {
		super(context);
	}


	public String getBoard(){
		return android.os.Build.BOARD;
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	public String getBootloader(){
		if(getVersion() >= android.os.Build.VERSION_CODES.FROYO){
			return android.os.Build.BOOTLOADER;
		} else {
			return UNKNOWN;
		}
	}

	public String getBrand(){
		return android.os.Build.BRAND;
	}

	public String getCPU_ABI(){
		return android.os.Build.CPU_ABI;
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	public String getCPU_ABI2(){
		if(getVersion() >= android.os.Build.VERSION_CODES.FROYO){
			return android.os.Build.CPU_ABI2;
		} else {
			return UNKNOWN;
		}
	}

	public String getDevice(){
		return android.os.Build.DEVICE;
	}

	public String getFingerprint(){
		return android.os.Build.FINGERPRINT;
	}

	public String getDisplay(){
		return android.os.Build.DISPLAY;
	}

	public String getBuildTime(){
		long date = android.os.Build.TIME;
		Date dateFormat = new Date(date);
		return dateFormat.toString();
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	public String getHardware(){
		if(getVersion() >= android.os.Build.VERSION_CODES.FROYO){
			return android.os.Build.HARDWARE;
		} else {
			return UNKNOWN;
		}
	}

	public String getHost(){
		return android.os.Build.HOST;
	}

	public String getID(){
		return android.os.Build.ID;
	}

	public String getManufacturer(){
		return android.os.Build.MANUFACTURER;
	}

	public String getModel(){
		return android.os.Build.MODEL;
	}

	public String getProduct(){
		return android.os.Build.PRODUCT;
	}

	//May return null
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public String getRadio(){
		String radio;

		if(getVersion() >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			radio = android.os.Build.getRadioVersion();
		} else if(getVersion() >= android.os.Build.VERSION_CODES.FROYO){
			radio = android.os.Build.RADIO;
		} else {
			radio = UNKNOWN;
		}

		if(radio == null){
			return UNKNOWN;
		} else {
			return radio;
		}

	}

	public String getTags(){
		return android.os.Build.TAGS;
	}

	public String getType(){
		return android.os.Build.TYPE;
	}


	public String getVersionCodename(){
		return android.os.Build.VERSION.CODENAME;
	}

	public String getVersionIncremental(){
		return android.os.Build.VERSION.INCREMENTAL;
	}

	public String getVersionRelease(){
		return android.os.Build.VERSION.RELEASE;
	}

	public FeatureInfo[] getFeatureArray(){
		return context.getPackageManager().getSystemAvailableFeatures();
	}

	public String getAvailableFeatures(){
		FeatureInfo[] features = getFeatureArray();

		String featureString = "\n";

		for(FeatureInfo feature : features){
			featureString += feature.name + "\n";
		}

		return featureString;
	}


	public boolean isPlayStoreInstalled() {

		String[] packageNames = {"com.google.market","com.google.vending" , "com.android.vending" };
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo packageInfo : packages) {
			String currentPackageName = packageInfo.packageName;
			for(String gNames : packageNames){
				if(currentPackageName.equals(gNames)){
					return true;
				}
			}
		}

		return false;


	}

	@SuppressWarnings("unused")
	public boolean isGoogleMapsInstalled(){
		try  {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
			return true;
		} 
		catch(PackageManager.NameNotFoundException e)  {
			return false;
		}
	}


	public boolean isDeviceRooted() {
		return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
	}

	private boolean checkRootMethod1() {
		String buildTags = android.os.Build.TAGS;
		return buildTags != null && buildTags.contains("test-keys");
	}

	private boolean checkRootMethod2() {
		try {
			File file = new File("/system/app/Superuser.apk");
			return file.exists();
		} catch (Exception e) {
			return false;
		}
	}

	private boolean checkRootMethod3() {
		return new ExecShell().executeCommand(SHELL_CMD.check_su_binary)!=null;
	}


	/** @author Kevin Kowalewski */
	public static class ExecShell {

		private static String LOG_TAG = ExecShell.class.getName();

		public static enum SHELL_CMD {
			check_su_binary(new String[] { "/system/xbin/which", "su" });

			String[] command;

			SHELL_CMD(String[] command) {
				this.command = command;
			}
		}

		public ArrayList<String> executeCommand(SHELL_CMD shellCmd) {
			String line = null;
			ArrayList<String> fullResponse = new ArrayList<String>();
			Process localProcess = null;
			try {
				localProcess = Runtime.getRuntime().exec(shellCmd.command);
			} catch (Exception e) {
				return null;
			}
			new BufferedWriter(new OutputStreamWriter(localProcess.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					localProcess.getInputStream()));
			try {
				while ((line = in.readLine()) != null) {
					Log.d(LOG_TAG, "--> Line received: " + line);
					fullResponse.add(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.d(LOG_TAG, "--> Full response was: " + fullResponse);
			return fullResponse;
		}
	}

	public String getKernelVersion(){
		String kernelVersion = System.getProperty("os.version");
		if(kernelVersion == null){
			return UNKNOWN;
		} else {
			return kernelVersion;
		}
	}

	public String getPackageVersionAndName(){

		PackageManager manager = context.getPackageManager();
		String version = "<<App Version>>\n";
		try{
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

			version += "PackageName: " + info.packageName + "\n";
			version += "VersionCode: " + info.versionCode +  "\n"; 
			version += "VersionName: " + info.versionName + "\n";


		} catch (NameNotFoundException e){
			version = "Cannot find package\n";
		}

		return version;
	}


	public String getMinimumDetails(){
		String phoneDetails = "<<Phone Summary>>\n";
		ArrayList<String> details = new ArrayList<String>();
		try{

			details.add("Manufacturer: " + getManufacturer());
			details.add("Model: " + getModel());
			details.add("Product: " + getProduct());
			details.add("Android Version: " + getVersion());
			details.add("Version Release: " + getVersionRelease());
		} catch (Exception e){
			details.add(e.toString());
		}
		for(String detail : details){
			phoneDetails += detail + "\n";
		}

		return phoneDetails;
	}

	public String getMinimalDetails(){
		String phoneDetails = "<<Phone Summary>>\n";
		ArrayList<String> details = new ArrayList<String>();
		try{

			details.add("Manufacturer: " + getManufacturer());
			details.add("Model: " + getModel());
			details.add("Product: " + getProduct());
			details.add("Android Version: " + getVersion());
			details.add("Version Release: " + getVersionRelease());
			details.add("Play Store Installed: " + isPlayStoreInstalled());
			details.add("Google Maps Installed: " + isGoogleMapsInstalled());
			details.add("Rooted: " + isDeviceRooted());
		} catch (Exception e){
			details.add(e.toString());
		}
		for(String detail : details){
			phoneDetails += detail + "\n";
		}

		return phoneDetails;

	}

	@Override
	public String getBasicDetailsOnly() {

		String phoneDetails = "<<Phone Summary>>\n";

		ArrayList<String> details = new ArrayList<String>();
		try{
			details.add("Manufacturer: " + getManufacturer());
			details.add("Model: " + getModel());
			details.add("Product: " + getProduct());
			details.add("Android Version: " + getVersion());
			details.add("Version Release: " + getVersionRelease());
			details.add("Play Store Installed: " + isPlayStoreInstalled());
			details.add("Google Maps Installed: " + isGoogleMapsInstalled());
			details.add("Rooted: " + isDeviceRooted());
			details.add("Build Display: " + getDisplay());
			details.add("Build Time: " + getBuildTime());
			details.add("Kernel Version: " + getKernelVersion());
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
			details.add("Board: " + getBoard());
			details.add("Bootloader: " + getBootloader());
			details.add("CPU_ABI: " + getCPU_ABI());
			details.add("CPU_ABI2: " + getCPU_ABI2());
			details.add("Device: " + getDevice());
			details.add("Fingerprint: " + getFingerprint());
			details.add("Hardware: " + getHardware());
			details.add("Host: " + getHost());
			details.add("ID: " + getID());
			details.add("Radio (Baseband): " + getRadio());
			details.add("Tags: " + getTags());
			details.add("Type: " + getType());
			details.add("Version Codename: " + getVersionCodename());
			details.add("Version Incremental: " + getVersionIncremental());
			details.add("Features: " + getAvailableFeatures());
		} catch (Exception e){
			details.add(e.toString());
		}
		for(String detail : details){
			phoneDetails += detail + "\n";
		}
		
		return phoneDetails;
	}



}
