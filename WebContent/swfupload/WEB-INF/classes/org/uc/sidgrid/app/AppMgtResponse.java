package org.uc.sidgrid.app;

public class AppMgtResponse {
	String appName; // unqiue application identifier
	int status;  //0: success -1: failed
	String message; // the reason for failure
	Object result;
	String gadgetURL;
	String mobyleXMLURL;
	
	public static int SUCCESS = 0;
	public static int FAILURE = -1;
	
	public void setAppName(String appName){
		this.appName = appName;
	}
	public String getAppName(){
		return this.appName;
	}
	public void setStatus(int status){
		this.status = status;
	}
	public int getStatus(){
		return this.status;
	}
	public void setMessage(String message){
		this.message = message;
	}
	public String getMessage(){
		return this.message;
	}
	public void setContent(Object result){
		this.result = result;
	}
	public Object getContent(){
		return this.result;
	}
	public void setGadgetURL(String gadgetURL){
		this.gadgetURL = gadgetURL;
	}
	public String getGadgetURL(){
		return this.gadgetURL;
	}
	public void setMobyleXMLURL(String mobyleXMLURL){
		this.mobyleXMLURL = mobyleXMLURL;
	}
	public String getMobyleXMLURL(){
		return this.mobyleXMLURL;
	}
}
