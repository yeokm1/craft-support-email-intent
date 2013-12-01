package com.yeokhengmeng.craftsupportemailintent;

import java.util.ArrayList;

import android.content.Context;

public class CraftSupportEmail extends CraftIntentEmail {


	private ArrayList<GetInfoAbstract> getInfos = new ArrayList<GetInfoAbstract>();
	
	private Context context;


	public CraftSupportEmail(Context context){
		super(context);
		this.context = context;
		initAllGetInfos(context);
	}

	public CraftSupportEmail(Context context, String recipient, String subject) {
		super(context, recipient, subject);
		initAllGetInfos(context);
	}

	public CraftSupportEmail(Context context, String recipient, String subject, String content) {
		super(context, recipient, subject, content);
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
	
	public void appendMinimumDetailsToContent(){
		appendContent(getMinimumSummary());
	}

	public void appendBasicDetailsToContent(){
		appendContent(getBasicDetails());
	}

	public void appendAllDetailsToContent(){
		appendContent(getAllDetails());
	}
	
	public String getMinimumSummary(){
		GetInfoSummary summ = new GetInfoSummary(context);
		return summ.getMinimumDetails();
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
