package org.uc.sidgrid.services;

/**
 * This class records the absolute path for 
 * [ Root, SwiftScript, mobyleXML and gadget-template directory ]
 * apache-tomcat--+
 *                +--webapps+==(Root)
 *                          +
 *                          +SIDGridPortal+==(PortalAppRoot)
 *                          |             +
 *                          |             +--swiftscript
 *                          |             +--mobylexml
 *                          |
 *                          +SIDGridGadgets+==(GadgetAppRoot)
 *                        
 * @author wenjun wu
 *
 */
public class PortalPathContxt {
  private String webRoot; // root file path
  private String urlRoot; // root url path
  private String portalAppRoot;  // http://host:port/webapps/APPNAME
  private String portalAppName;  // APPNAME
  private static PortalPathContxt instance;
  
  public static PortalPathContxt getInstance() {
	   if(instance == null) {
	         instance = new PortalPathContxt();
	   }
	   return instance;
}
  //	
  // Definition of filesystem paths used by portal
  //
  
  // - Deprecated. This app should not IMO care about the server root; everything
  // 				should be relative to the portalAppRoot below
  // e.g. /usr/local/apache-tomcat-6.0.29/webapps/
  public void setRoot(String root){
	  System.out.println("ppx setRoot = " + root );
	  this.webRoot = root;
  }
  public String getRoot(){
	  System.out.println("ppx getRoot = " + this.webRoot );
	  return this.webRoot;
  }
  // - end deprecated
  
  // e.g. /usr/local/apache-tomcat-6.0.29/webapps/SIDGridPortal/  
  public void setPortalAppRoot(String portalAppRoot){
	  System.out.println("setPortalAppRoot: " + portalAppRoot);
	  this.portalAppRoot = portalAppRoot;
  }
  public String getPortalAppRoot(){
	  System.out.println("getPortalAppRoot: " + this.portalAppRoot);
	  return this.portalAppRoot;
  }
  
  // e.g. /usr/local/apache-tomcat-6.0.29/webapps/SIDGridPortal/swiftscript  
  public String getScriptRoot(){
	  return this.portalAppRoot+"/swiftscript/";
  }
  // e.g. /usr/local/apache-tomcat-6.0.29/webapps/SIDGridPortal/mobylexml
  public String getMobyleXMLRoot(){
	  return this.portalAppRoot+"/mobylexml/";
  }
  // e.g. /usr/local/apache-tomcat-6.0.29/webapps/SIDGridPortal/SIDGridGadgets
  public String getGadgetRoot(){
	  return this.portalAppRoot+"/SIDGridGadgets/";
  }
  // e.g. /usr/local/apache-tomcat-6.0.29/webapps/SIDGridPortal/mobylexml/gadget
  public String getGadgetTemplateRoot(){
	  return this.portalAppRoot+"/mobylexml/gadget/";
  }
  
  //	
  // Definition of web URLs used by portal
  //

  // e.g. http://127.0.0.1:8080/  
  public void setRootURL(String rootURL){
	  System.out.println("ppx setRootURL = " + rootURL );
	  this.urlRoot = rootURL;
  }
  public String getRootURL(){
	  return this.urlRoot;
  }
  
  // e.g. http://127.0.0.1:8080/SIDGridPortal
  public String getAppRootURL(){
	  return this.urlRoot+"/"+this.portalAppName+"/";
  }  
  // e.g. http://127.0.0.1:8080/SIDGridPortal/SIDGridGadgets 
  // deprecate this one in favor of getGadgetRootURL for naming consistency
  public String getGadgetAppRoot(){
	  System.out.println("Deprecated: Call to getGadgetAppRoot");
	  return this.getGadgetRootURL();
  }
  public String getGadgetRootURL(){
	  return this.urlRoot+"/"+this.portalAppName+"/SIDGridGadgets/";
  }
  
  //
  // - Other definitions
  //
  
  public void setPortalAppName(String portalAppName){
	  System.out.println("ppx setPortalAppName = " + portalAppName );
	  this.portalAppName = portalAppName;
  }
  public String getPortalAppName(){
	  return this.portalAppName;
  }
  
  
}
