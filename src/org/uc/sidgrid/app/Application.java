package org.uc.sidgrid.app;

public class Application {
	long appId; // table id
	String appName; // unqiue application identifier
	String title; // the full name for the application
	String xmlDescription;
	String status; // created, deployed, activated, disabled 
	String creator;
	String type;  // cmd, script
	
	public static final String created = "created";
	public static final String deployed = "deployed";
	public static final String activated = "activated";
	public static final String disabled = "disabled";
	
	public static final String cmdType = "cmd";
	public static final String scriptType = "script";
	
	public void setId(long id){
		this.appId = id;
	}
	public long getId(){
		return this.appId;
	}
	public void setAppName(String name){
		this.appName = name;
	}
	public String getAppName(){
		return this.appName;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return this.title;
	}
	public void setXmlDesc(String xmldesc){
		this.xmlDescription = xmldesc;
	}
	public String getXmlDesc(){
		return this.xmlDescription;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public String getStatus(){
		return this.status;
	}
	public void setCreator(String creator){
		this.creator = creator;
	}
	public String getCreator(){
		return this.creator;
	}
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return this.type;
	}
}
