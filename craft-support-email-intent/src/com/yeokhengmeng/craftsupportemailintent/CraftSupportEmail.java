package com.yeokhengmeng.craftsupportemailintent;

import java.text.DateFormat;
import java.util.ArrayList;

import android.content.Context;

public class CraftSupportEmail extends CraftIntentEmail {


	private ArrayList<GetInfoAbstract> getInfos = new ArrayList<GetInfoAbstract>();
	
	private Context context;


	public CraftSupportEmail(Context context){
		this.context = context;
		initAllGetInfos(context);
	}

	public CraftSupportEmail(Context context, String recipient, String subject) {
		super(recipient, subject);
		this.context = context;
		initAllGetInfos(context);
	}

	public CraftSupportEmail(Context context, String recipient, String subject, String content) {
		super(recipient, subject, content);
		this.context = context;
		initAllGetInfos(context);
	}


	private void initAllGetInfos(Context context){
		getInfos.add(new GetInfoSummary(context));
		getInfos.add(new GetInfoWifi(context));
		getInfos.add(new GetInfoBluetooth(context));
		getInfos.add(new GetInfoLocation(context));
		getInfos.add(new GetInfoCarrier(context));
		getInfos.add(new GetInfoBattery(context));
	}
	
	public void appendAppDetailsToContent(DateFormat dateFormatForCompileTime){
		appendContent(getAppDetails(dateFormatForCompileTime));
	}
	
	public void appendMinimalDetailsToContent(){
		appendContent(getMinimalDetails());
	}
	
	public void appendMinimumDetailsToContent(){
		appendContent(getMinimumDetails());
	}

	public void appendBasicDetailsToContent(){
		appendContent(getBasicDetails());
	}

	public void appendAllDetailsToContent(){
		appendContent(getAllDetails());
	}
	
	public String getAppDetails(DateFormat dateFormatForCompileTime){
		GetInfoSummary summ = new GetInfoSummary(context);
		return summ.getPackageVersionAndName(null);
	}
	
	public String getMinimumDetails(){
		GetInfoSummary summ = new GetInfoSummary(context);
		return summ.getMinimumDetails();
	}
	
	public String getMinimalDetails(){
		GetInfoSummary summ = new GetInfoSummary(context);
		return summ.getMinimalDetails();
	}

	public String getBasicDetails(){

		StringBuffer buf = new StringBuffer();

		for(GetInfoAbstract info : getInfos){

			try{	
				buf.append(info.getBasicDetailsOnly() + "\n");
			} catch(Exception e){
				String message = e.getMessage();
				if(message!= null) {
					buf.append(message + "\n");
				} else {
					buf.append(info.getClass() + "\n");
				}
			}
		}

		return buf.toString();

	}

	public String getAllDetails(){

		StringBuffer buf = new StringBuffer();

		for(GetInfoAbstract info : getInfos){

			try{	
				buf.append(info.getAllDetails() + "\n");
			} catch(Exception e){
				String message = e.getMessage();
				if(message!= null) {
					buf.append(message + "\n");
				} else {
					buf.append(info.getClass() + "\n");
				}
			}
		}

		return buf.toString();

	}



}
