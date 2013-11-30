package com.yeokhengmeng.craftsupportemailintent;

import android.content.Context;
import android.content.pm.PackageManager;

public abstract class GetInfoAbstract {
	
	protected static final String UNKNOWN = "Unknown";
	protected static final String NO_PERMISSION = "No Permission";
	
	protected Context context;
	
	public GetInfoAbstract(Context context){
		this.context = context;
	}
	
	public boolean checkPermission(String permission) {
	    int res = context.checkCallingOrSelfPermission(permission);
	    return (res == PackageManager.PERMISSION_GRANTED);            
	}
	
	public int getVersion(){
		return android.os.Build.VERSION.SDK_INT;
	}
	
	public abstract String getBasicDetailsOnly();
	
	public abstract String getAllDetails();

}
