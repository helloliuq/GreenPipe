package org.uc.sidgrid.app;

import java.sql.Timestamp;

/**
 * Each application can have many AppScript with different versions.
 * Each AppScript -> script, mobylexml, gadgetxml
 * @author wenjun wu
 *
 */
public class AppScript {
  private long scriptId;
  private String version;
  private String scriptName;
  private String mainscript;
  private String scriptType;
  private String xmlDescription;
  private String gadgetUrl;
  private Application application;
  
  private Timestamp   checkInTime;
  private User user;
  
  public void setScriptId(long id){
	  this.scriptId = id;
  }
  public long getScriptId(){
	  return this.scriptId;
  }
  public void setVersion(String version){
	  this.version = version;
  }
  public String getVersion(){
	  return this.version;
  }
  public void setScriptName(String name){
	  this.scriptName = name;
  }
  public String getScriptName(){
	  return this.scriptName;
  }
  public void setMainscript(String filename){
	  this.mainscript = filename;
  }
  public String getMainscript(){
	  //return this.scriptName+".swift";
	  return this.mainscript;
  }
  public void setType(String type){
	  this.scriptType = type;
  }
  public String getType(){
	  return this.scriptType;
  }
  public void setXmlDesc(String xmldesc){
		this.xmlDescription = xmldesc;
  }
  public String getXmlDesc(){
		return this.xmlDescription;
  }
  public void setGadgetLink(String gadget){
	  this.gadgetUrl = gadget;
  }
  public String getGadgetLink(){
	  return this.gadgetUrl;
  }
  public void setApplication(Application app){
	  this.application = app;
  }
  public Application getApplication(){
	  return this.application;
  }
  //public void setCheckInTime(Timestamp time){
  //	  this.checkInTime = time;
  //}
  //public Timestamp getCheckInTime(){
  //	  return this.checkInTime;
  //}
  public void setByUser(User user){
	  this.user = user;
  }
  public User getByUser(){
	  return this.user;
  }
}
