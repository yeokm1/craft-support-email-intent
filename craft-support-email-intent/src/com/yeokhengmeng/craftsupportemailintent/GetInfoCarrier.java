package com.yeokhengmeng.craftsupportemailintent;

import java.util.ArrayList;

import android.Manifest.permission;
import android.content.Context;
import android.telephony.TelephonyManager;

public class GetInfoCarrier extends GetInfoAbstract {
	
	public static final String PERMISSION_READ_PHONE_STATE  = permission.READ_PHONE_STATE;

	
	private TelephonyManager teleManager;
	
	
	public GetInfoCarrier(Context context) {
		super(context);
		if(checkPermission(PERMISSION_READ_PHONE_STATE)){
			teleManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		}
	}
	
	public String getDataState(){
		if(teleManager == null){
			return NO_PERMISSION;
		}
		
		int state = teleManager.getDataState();
		switch(state){
		case TelephonyManager.DATA_DISCONNECTED : return "Disconnected";
		case TelephonyManager.DATA_CONNECTING : return "Connecting";
		case TelephonyManager.DATA_CONNECTED : return "Connected";
		case TelephonyManager.DATA_SUSPENDED : return "Suspended";
		default: return UNKNOWN;
		}
		
	}
	
	//Imei
	public String getDeviceID(){
		if(teleManager == null){
			return NO_PERMISSION;
		} else {
			String id =  teleManager.getDeviceId();
			if(id== null){
				return UNKNOWN;
			} else {
				return id;
			}
		}
	}
	
	public String getCountryCode(){
		if(teleManager == null){
			return NO_PERMISSION;
		} else {
			String countryCode =  teleManager.getNetworkCountryIso();
			if(countryCode== null){
				return UNKNOWN;
			} else {
				return countryCode;
			}
		}
	}
	
	public String getNetworkOperator(){
		if(teleManager == null){
			return NO_PERMISSION;
		} else {
			String networkOperator =  teleManager.getNetworkOperator();
			String networkOperatorName = teleManager.getNetworkOperatorName();
			if(networkOperator == null || networkOperatorName == null){
				return UNKNOWN;
			} else {
				return networkOperatorName + " (" + networkOperator + ")";
			}
		}
	}
	
	public String getPhoneType(){
		if(teleManager == null){
			return NO_PERMISSION;
		}
		
		int type = teleManager.getPhoneType();
		switch(type){
		case TelephonyManager.PHONE_TYPE_CDMA : return "CDMA";
		case TelephonyManager.PHONE_TYPE_GSM : return "GSM";
		case TelephonyManager.PHONE_TYPE_SIP : return "SIP";
		case TelephonyManager.PHONE_TYPE_NONE : return "NONE";
		default: return UNKNOWN;
		}
	}

	@Override
	public String getBasicDetailsOnly() {
		String phoneDetails = "<<Carrier>>\n";
		
		ArrayList<String> details = new ArrayList<String>();
		
		details.add("Data State: " + getDataState());
		details.add("Phone Type: " + getPhoneType());
		
		for(String detail : details){
			phoneDetails += detail + "\n";
		}
		
		return phoneDetails;
	}


	@Override
	public String getAllDetails() {
		String phoneDetails = getBasicDetailsOnly();
		ArrayList<String> details = new ArrayList<String>();
		
		details.add("Network Operator: " + getNetworkOperator());
		details.add("DeviceID: " + getDeviceID());
		details.add("Country Code: " + getCountryCode());
		
		for(String detail : details){
			phoneDetails += detail + "\n";
		}
		
		return phoneDetails;
	}
		
		
		


}
