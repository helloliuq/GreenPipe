package org.uc.sidgrid.services;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlOptions;
import org.uc.sidgrid.dao.DAOFactory;
import org.uc.sidgrid.dao.ApplicationDAO;
import org.uc.sidgrid.mobyle.HeadDocument;
import org.uc.sidgrid.mobyle.ParameterDocument;
import org.uc.sidgrid.mobyle.ParametersDocument;
import org.uc.sidgrid.mobyle.ProgramDocument;
import org.uc.sidgrid.mobyle.HeadDocument.Head;
import org.uc.sidgrid.mobyle.core.MobyleBuilder;
import org.uc.sidgrid.mobyle.core.GadgetGenerator;
import org.uc.sidgrid.mobyle.core.SwiftGadgetGenerator;

import org.uc.sidgrid.app.*;
import org.uc.sidgrid.util.ServiceUtil;
/**
 * A service class that supports the ajax client to define a new sidgrid analysis application
 * 
 * The status of SIDGrid analysis application
 * created, deployed, activated, disabled 
 * @author wenjun wu
 *
 */

public class AppMgtService {
	
	public String rootPath; // the directory that holds the application xml files
	public String gadgetPath; // the directory that holds the generated gadget XML files
	public String gadgetUrl; // the root URL for the gadget
	//@deprecated
	private List<AppParameter> parameters = new ArrayList<AppParameter>();
	private ApplicationDAO appdao;
	
	private static Log log = LogFactory.getLog(AppMgtService.class);
	
	public AppMgtService(){
		DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		this.appdao = daoFactory.getAppDAO();
		//TODO: create swift gadget generator??
		
	}
	/**
	 * set the root path for all the mobyle xml files
	 * it is typically under SIDGridPortal/mobylexml
	 * @param path
	 */
	public void setAppFilePath(String path){
		//TODO: we should not use this context variable any more. use PortalPathContxt instead !!
		this.rootPath = path;
	}
	// set the absolute path of the gadget space
	public void setGadgetPath(String path){
		//TODO: we should not use this context variable any more. use PortalPathContxt instead !!
		this.gadgetPath = path;
	}
	// 
	public void setGadgetUrl(String url){
		//TODO: we should not use this context variable any more. use PortalPathContxt instead !!
		this.gadgetUrl = url;
	}
	/**
	 * create a new parameter for the command-line application
	 * @param paraName
	 * @param prompt
	 * @param dataType
	 * @param argType
	 * @deprecated
	 */
	public void addParameter(String paraName, String prompt, String dataType, String argType){
		AppParameter tmp = new AppParameter();
		tmp.setName(paraName);
		tmp.setPrompt(prompt);
		tmp.setDataType(dataType);
		tmp.setArgType(mappingArgType(argType));
	    this.parameters.add(tmp);
	}
	//@deprecated
	public List<AppParameter> getParameters(){
		return this.parameters;
	}
	/**
	 * get all the application metainfo from the application mobylexml description
	 */
	public Hashtable<String,String> getAppInfo(String appName){
		Hashtable valuepair = new Hashtable();
		String filename = this.rootPath+"/"+appName+".xml";
		File xmlFile = new File(filename);
		try {
			  ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile); 
			  Head head = programDoc.getProgram().getHead();
			  String name = head.getName(); valuepair.put("name", name);
			  String version = head.getVersion(); valuepair.put("version", version);
			  if (head.getDoc()!=null ){
			   String title = head.getDoc().getTitle(); 
			   if (title != null)
			    valuepair.put("title", title);
			  }
			  if (head.getCommand()!=null){
			   org.uc.sidgrid.mobyle.CommandDocument.Command command = head.getCommand();
			   String commV = MobyleBuilder.readTextValue(command);
			   log.info("get the command "+commV);
			   if (command != null)
			    valuepair.put("command", commV);
			  }
			  return valuepair;
	    } catch (Exception e){
			 e.printStackTrace();
			 return null;
		 }
	}
	public Hashtable<String, String> getScriptInfo(String appName, String scriptName, String version){
		Hashtable valuepair = new Hashtable();
		String scriptDir = ServiceUtil.scriptVersionPath(scriptName, version);
		String filename = this.rootPath+"/"+appName+"/"+scriptDir+"/"+scriptName+".xml";
		File xmlFile = new File(filename);
		try {
			  ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile); 
			  Head head = programDoc.getProgram().getHead();
			  String name = head.getName(); valuepair.put("name", name);
			  //String script_version = head.getVersion(); 
			  valuepair.put("version", version);
			  if (head.getDoc()!=null ){
			   String title = head.getDoc().getTitle(); 
			   if (title != null)
			    valuepair.put("title", title);
			  }
			  if (head.getCommand()!=null){
			   org.uc.sidgrid.mobyle.CommandDocument.Command command = head.getCommand();
			   String commV = MobyleBuilder.readTextValue(command);
			   log.info("get the command "+commV);
			   if (command != null)
			    valuepair.put("command", commV);
			  }
			  return valuepair;
	    } catch (Exception e){
			 e.printStackTrace();
			 return null;
		}
	}
	/**
	 * get an parameter list from a defined application or script
	 * @param appName : the application name
	 * @return
	 */
	public List<AppParameter> getParameters(String appName, String scriptName, String version){
		String scriptDir = ServiceUtil.scriptVersionPath(scriptName, version);
		String filename = this.rootPath+"/"+appName+"/"+scriptDir+"/"+scriptName+".xml";
		 // parse the mobyle xml file
		 File xmlFile = new File(filename);
		 // create the parameter list
		 ArrayList paralist = new ArrayList();
		 try {
		  ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile); 
		  org.uc.sidgrid.mobyle.ParameterDocument.Parameter[] paras  = org.uc.sidgrid.mobyle.core.CmdGenerator.getAllParameterNameByArgpos(programDoc);
		  for(org.uc.sidgrid.mobyle.ParameterDocument.Parameter param : paras){
			  AppParameter appParam = MobyleBuilder.readMobyleParam(param);
			  paralist.add(appParam);
		  }
		  return paralist;
		 } catch (Exception e){
			 e.printStackTrace();
			 return null;
		 }
	}
	public List<AppParameter> getParameters(String appName){
		 String filename = this.rootPath+"/"+appName+".xml";
		 // parse the mobyle xml file
		 File xmlFile = new File(filename);
		 // create the parameter list
		 ArrayList paralist = new ArrayList();
		 try {
		  ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile); 
		  org.uc.sidgrid.mobyle.ParameterDocument.Parameter[] paras  = org.uc.sidgrid.mobyle.core.CmdGenerator.getAllParameterNameByArgpos(programDoc);
		  for(org.uc.sidgrid.mobyle.ParameterDocument.Parameter param : paras){
			  AppParameter appParam = MobyleBuilder.readMobyleParam(param);
			  paralist.add(appParam);
		  }
		  return paralist;
		 } catch (Exception e){
			 e.printStackTrace();
			 return null;
		 }
	}
	/**
	 * create an entry for this application including its gadget XML and registry entry
	 * @param appName
	 * @param appXML
	 */
	public AppMgtResponse createApplication(String appName, String appXML){
		try {
		  // copy the xml string to a file
		  String filename = this.rootPath+"/"+appName+".xml";
		  FileWriter writer = new FileWriter(filename);
		  writer.write(appXML);
		  writer.close();
		  // create the serialized object in the cmdline directory
		  //createServStub(appName, localpath);
		  
		  // create a gadget file based on that appXML
		  String gadgetTmpDir = rootPath+"/gadget";
		  GadgetGenerator generator = new GadgetGenerator();
		  generator.setTemplateDir(gadgetTmpDir);
		  String gadgetXML = generator.createGadget(appName, this.gadgetUrl, this.gadgetUrl, filename);
		  filename = this.gadgetPath+"/"+appName+".xml";
		  writer = new FileWriter(filename);
		  writer.write(gadgetXML);
		  writer.close();
		  
		  // create a database entry for this application and set the status to 'created'
		  Application app = new Application();
		  app.setAppName(appName);
		  app.setTitle(appName);
		  app.setXmlDesc(filename);
		  //app.setStatus(Application.created);
		  // set to deployed upon creation, for sake of demo at least, or permanently
		  app.setStatus(Application.deployed);
		  app.setType(Application.cmdType);
		  //TODO: set applicaiton type
		  DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		  ApplicationDAO appdao = daoFactory.getAppDAO();
		  appdao.create(app);
		  AppMgtResponse res = createSuccessResponse(appName);
		  return res;
		} catch(Exception e){
			e.printStackTrace();
			String message = e.getMessage();
			AppMgtResponse res = createFailResponse(message);
			return res;
		}
	}
	/**
	 * set the application's status to be Deployed
	 * @param appName
	 */
	public void deployApplication(String appName){
		 // set the application status to "deployed"
		 DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		 ApplicationDAO appdao = daoFactory.getAppDAO();
		 try {
			 Application app = appdao.getAppByName(appName);
			 app.setStatus(Application.deployed);
			 appdao.update(app);
		 } catch (Exception e){
			 e.printStackTrace();
		 }
	 }
	/**
	 * set the application's status to be Activated
	 * @param appName
	 */
	 public void approveApplication(String appName){
		 // set the application status to "activated"
//		 set the application status to "deployed"
		 try {
			 DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
			 ApplicationDAO appdao = daoFactory.getAppDAO();
			 appdao.setAppStatus(appName, Application.activated);
			} catch (Exception e){
				 e.printStackTrace();
			 }
	 }
	 public void disableApplication(String appName) {
         //	set the application status to "disabled"
		try {
		 DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		 ApplicationDAO appdao = daoFactory.getAppDAO();
		 appdao.setAppStatus(appName, Application.disabled);
		} catch (Exception e){
			 e.printStackTrace();
		 }
	 }
	 // show all deployed Applications
	 public List<Application> showAllApplication(){
		 try {
			 DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
			 ApplicationDAO appdao = daoFactory.getAppDAO();
			 List<Application>res = appdao.getAllApplications(Application.deployed);
			 return res;
		} catch (Exception e){
			 e.printStackTrace();
			 return null;
		}
	 }
	 // show All Applications
	 public List<Application> showEveryApplication(){
		try {
			 List<Application>res = appdao.getAllApplications();
			 return res;
		} catch (Exception e){
			 e.printStackTrace();
			 return null;
		}
	 }
	 // show all the scripts for the application
	 public List<AppScript> showAllScripts(String appName){
	   try {
		 Application app = this.appdao.getAppByName(appName);
		 List<AppScript> scripts = appdao.getAllScripts(appName);
		 return scripts;
	   } catch(Exception e){
		   e.printStackTrace();
		   return null;
	   }
	 }
	 //TODO: show all the versions of the script
	 /**
	  * create the mobyle XML file
	  */
	 public AppMgtResponse createMobyleXML(String sessionID, String name,String command, String title){
		 return createMobyleXML(sessionID, name,command, title, this.parameters);
	 }
	 /**
	  * This method is called by the UI to create a temp mobyle xml and gadget xml for defining a new application
	  * @param name
	  * @param command
	  * @param title
	  * @param parameters
	  * @return
	  */
	 public AppMgtResponse createMobyleXML(String sessionID, String name,String command, String title, 
			                               List<AppParameter> parameters){
		    //AppMgtResponse res = checkAppExist(name);
		    //if (res!=null)
		    //	return res;
		    PortalPathContxt ctx = PortalPathContxt.getInstance();
		    AppMgtResponse response = new AppMgtResponse();
			try {
				  ProgramDocument programDoc = MobyleBuilder.createMobyleDom(name,command,title,parameters);
				 // output the mobyle xml file
				 // TODO: do we need to lock the file??
				  String mobyle_fileName = this.rootPath+"/../temp/"+sessionID+"/"+name+".xml";
				  File appdir = new File(this.rootPath+"/../temp/"+sessionID+"/");
				  if (!appdir.exists()){
					  appdir.mkdir();
				  }
				  FileWriter out = new FileWriter(mobyle_fileName);
				  XmlOptions opts = new XmlOptions();
				  opts.setSavePrettyPrint();
				  opts.setSavePrettyPrintIndent(4);
				  programDoc.save(out,opts);
				  out.close();
				  
				  /** XmlOptions opts = new XmlOptions();
				  opts.setSavePrettyPrint();
				  opts.setSavePrettyPrintIndent(4);
				  String xmlStr = programDoc.xmlText(opts); **/
				  String gadgetTmpDir = rootPath+"/gadget";
				  SwiftGadgetGenerator generator = SwiftGadgetGenerator.getInstance();
				  generator.setTemplateDir(gadgetTmpDir);
				  String gadgetXML = generator.createSwiftGadgetHTML(mobyle_fileName);
				  String filename = this.rootPath+"/../temp/"+sessionID+"/gadget.xml";
				  FileWriter writer = new FileWriter(filename);
				  writer.write(gadgetXML);
				  writer.close();
				  //TODO: return the xml file back to the web interface
				  
				  String gadgetInstanceUrl = this.gadgetUrl+"/temp/"+sessionID+"/gadget.xml";
				  System.out.println("gadgetUrl = " + this.gadgetUrl );
				  System.out.println("gadgetInstanceUrl = " + gadgetInstanceUrl );
				  
				  response.setStatus(AppMgtResponse.SUCCESS);
			      response.setMessage(name);
				  response.setGadgetURL(gadgetInstanceUrl);
				  response.setMobyleXMLURL(ctx.getRootURL()+"/temp/"+sessionID+"/"+name+".xml");
				  
				  //return fileName;
				}catch (Exception e){
					e.printStackTrace();
					//return "xml error";
					response.setStatus(AppMgtResponse.FAILURE);
					response.setMessage(e.toString());
				}
				return response;
	}

	 /*
	  * create a new application gadget from the uploaded mobyle xml file 
	  * @param fileName: the mobyle xml file that needs to be uploaded
	  */
	 public AppMgtResponse uploadMobyleXML(String fileName) {
		 // validate the uploaded mobyle description file
		 //String path = sess.getServletContext().getRealPath("/")+"temp/";
		 WorkflowFactory factory = WorkflowFactory.getInstance();
		 String realFileName = factory.locatePhysicalFile(fileName);
		 File uploadedXML = new File(realFileName);
		 // validate the uploaded file
		 String appName = "";
		 try {
		  ProgramDocument programDoc = ProgramDocument.Factory.parse(uploadedXML);
		  appName = programDoc.getProgram().getHead().getName();
		 } catch (Exception e) {
			 e.printStackTrace();
			 AppMgtResponse res = createFailResponse("the upload file "+fileName+" cann't be parsed. Please check the format of the xml file");
			 return (res);
		 }
		 // - check whether the application with the same name has been created.
		 /* - Allow applications to be overwritten
		 Application tmp = null;
		 try {
		   tmp = appdao.getAppByName(appName);
		 } catch (Exception e){
			 e.printStackTrace();
			 AppMgtResponse res = createFailResponse("can't verify whether the application "+appName+" exists or not");
			 return res;
		 }
		 
		 if (tmp !=null){
	     		 AppMgtResponse res = createFailResponse("An application named "+appName+" already exists. Please use a different name for this application.");
		 	 return res;
		 }
		 */
		 
		 // read the XML from the file
		 String appXML = "";
		 try {
			 FileReader fr = new FileReader(uploadedXML);
			 BufferedReader br = new BufferedReader(fr);
			 String s;
			 while((s = br.readLine()) != null) {
				 appXML += s+"\n";
			 }
			 fr.close();
		 } catch (Exception e){
			 e.printStackTrace();
			 AppMgtResponse res = createFailResponse("cann't read the xml content from the uploaded file");
			 return (res);
		 }
		 return createApplication(appName, appXML);
		 //TODO: return error code if the application is not correctly created
	 }
	 /*
	  * a UI method for checking in new script packages 
	  */
	 /** public AppMgtResponse commitXMLForScript(String appName, String scriptName, String version) {
		 addNewScript(String appName, String scriptName, String version, 
				      String scriptPkgUrl, String mainScript, String mobylexml);
	 } **/
	 
	 //TODO: remove the application
	 public AppMgtResponse removeApplication(String appName){
		 Application tmp = null;
		 // find the application
		 try {
		   tmp = appdao.getAppByName(appName);
		 } catch (Exception e){
			 e.printStackTrace();
			 AppMgtResponse res = createFailResponse("cann't verify whether the application "+appName+" exists or not");
			 return res;
		 }
		 // remove the mobyle xml file
		 String filename = this.rootPath+"/"+appName+".xml";
		 File mobylefile = new File(filename);
		 mobylefile.delete();
		 filename = this.gadgetPath+"/"+appName+".xml";
		 File gadgetfile = new File(filename);
		 gadgetfile.delete();
		 // remove the application database object
		 try {
		  appdao.delete(tmp);
		 } catch (Exception e){
			 e.printStackTrace();
			 AppMgtResponse res = createFailResponse("cann't remove the application "+appName+" from the database");
			 return res;
		 }
		 AppMgtResponse res = createSuccessResponse(appName);
		 return res;
	 }
	 /*
	  * create a new application gadget from the uploaded swift script file
	  *@param appName: the name of the application
	  *@param scriptName: the script name
	  *@param version: the (svn?) version from the script
	  *@param swiftScript: the name of the zipped file of the swift script
	 * @param mobyleXml: the name of the mobyle xml 
	  */
	 public AppMgtResponse addNewScript(String appName, String scriptName, String version, String scriptPkgUrl, 
			                            String mainScript, String mobylexml) {
		 // if it is a new application, create an Application object
		 // else just add a new AppScript object
		 // check in the swift script ( or any script )
		 // validate the uploaded mobyle description file
		 // 
		 DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		 ApplicationDAO appdao = daoFactory.getAppDAO();
		 Application tmp = null;
		 try {
		   tmp = appdao.getAppByName(appName);
		 } catch (Exception e){
			 e.printStackTrace();
			 tmp = null;
		 }
		 // locate the Pkg file or fetch the remote file
		 String scriptFilePkg = ServiceUtil.locatePhysicalFile(scriptPkgUrl);
		 PortalPathContxt ctx = PortalPathContxt.getInstance();
		 //Attention: scriptfilePkg already has the webapp path
		 scriptFilePkg = ctx.getRoot()+"/"+scriptFilePkg;
		 log.info("unzip the scrip package "+scriptFilePkg);
		 //step1: unzip the scriptPkg to root/appName/swiftScript
		 try { 
		  List<String> filelist = ServiceUtil.listZipfiles(scriptFilePkg);
		  if ( !filelist.contains(mainScript)){
			  AppMgtResponse response = new AppMgtResponse();
			  response.setStatus(-1);
			  response.setMessage("Can't find the main script in the application package");
			  return response;
		  }
		  String scriptDir = ServiceUtil.scriptVersionPath(scriptName, version);
		  File appdir = new File(ctx.getScriptRoot()+"/"+appName);
		  if (!appdir.exists()){
			  appdir.mkdir();
		  }
		  String desdir = ctx.getScriptRoot()+"/"+appName+"/"+scriptDir;
		  ServiceUtil.nativeUnzip(scriptFilePkg, desdir);
		 //step2: write mobyleXml to the xmlName
		  String des_mobyle_xml = ctx.getMobyleXMLRoot()+"/"+appName+"/"+scriptDir+"/"+mobylexml;
		  if(filelist.contains(mobylexml) ) {
		   String src_mobyle_xml = ctx.getScriptRoot()+"/"+appName+"/"+scriptDir+"/"+mobylexml;
		   ServiceUtil.copyFile(src_mobyle_xml, des_mobyle_xml);
		   //step3: create a new gadget when a new mobylexml exists in the application package
		   log.info("creating the gadget using the xml file "+des_mobyle_xml);
			  String gadgetTmpDir = ctx.getGadgetTemplateRoot();
			  log.info("using gadgetTempdir "+gadgetTmpDir);
			  SwiftGadgetGenerator generator = SwiftGadgetGenerator.getInstance();
			  String gadgetXML = generator.createSwiftGadget(appName,  scriptName, version, des_mobyle_xml);
			  String des_gadget_xml = this.gadgetPath+"/"+appName+"/"+scriptDir+"/"+scriptName+".xml";
			  ServiceUtil.writeNewFile(des_gadget_xml, gadgetXML);
		  } else {
			  AppMgtResponse response = new AppMgtResponse();
			  response.setStatus(-2);
			  response.setMessage("Can't find the interface xml description in the application package");
			  return response;
		  }
		 //step4: create Application and/or AppScript persistent data object
		 if (tmp==null){
			  Application app = new Application();
			  app.setAppName(appName);
			  app.setTitle(appName);
			  app.setXmlDesc(mobylexml);
			  app.setStatus(Application.deployed);
			  app.setType("script");
			  appdao.create(app);
			  tmp = app;
		 } 
		 // check whether the script exists or not
		 AppScript OldScript = appdao.getScript(appName, scriptName, version);
		
			  AppScript script = new AppScript();
			  script.setScriptName(scriptName);
			  script.setXmlDesc(des_mobyle_xml);
			  //TODO: use the basename of the mobylexml instead??
			  //TODO: just for now. most JS files for the gadget still need to point to the portal URL
			  log.info("gadgetLink is "+this.gadgetUrl);
			  String gadgetLink = ctx.getGadgetAppRoot();
			  gadgetLink +="/"+appName+"/"+scriptDir+"/"+scriptName+".xml";
			  script.setGadgetLink(gadgetLink);
			  script.setMainscript(mainScript);
			  script.setType("swift");
			  script.setVersion(version);
			  //TODO: do we need to check whether the same version of script exists or not
			  script.setApplication(tmp);
		if (OldScript==null)
			  appdao.create(script);
		//else
		//	  appdao.update(script);
		 
		 } catch(Exception e){
			 e.printStackTrace(); 
			 AppMgtResponse res = new AppMgtResponse();
			 res.setStatus(-1);
			 res.setMessage(e.getMessage());
			 return res;
		 }
		 AppMgtResponse res = new AppMgtResponse();
		 res.setStatus(0);
		 return res;
	 }
	 /**
	  * remove a swift script from the application
	  * @param appName : the application name
	  * @param swiftScript : the script package name
	  * @param version: the script package version number
	  * @return
	  */
	 public AppMgtResponse removeScript(String appName, String swiftScript, String version){
		 // check whether the application is defined
		 DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		 ApplicationDAO appdao = daoFactory.getAppDAO();
		 Application tmp = null;
		 try {
		   tmp = appdao.getAppByName(appName);
		 } catch (Exception e){
			 e.printStackTrace();
			 AppMgtResponse res = new AppMgtResponse();
			 res.setStatus(-1);
			 res.setMessage("No such application "+appName);
		 }
		 // remove the script entry from the table
		 
		 try {
		   AppScript script = appdao.getScript(appName, swiftScript, version);
		   String des_mobyle_xml = script.getXmlDesc();
		   String scriptDir = ServiceUtil.scriptVersionPath(swiftScript, version);
		   String des_gadget_xml = this.gadgetPath+"/"+appName+"/"+scriptDir+"/"+appName+".xml";
		   // remove the script and mobyle file from the directory
			 PortalPathContxt ctx = PortalPathContxt.getInstance();
			 String desdir = ctx.getScriptRoot()+"/"+appName+"/"+scriptDir;
			 ServiceUtil.removeDir(desdir);
		     
			 File mobyle_file = new File(des_mobyle_xml);
			 mobyle_file.delete();
			 
			 File gadget_file = new File(des_gadget_xml);
			 gadget_file.delete();
			 
		     appdao.delete(script);
		 } catch(Exception e){
			 e.printStackTrace();
			 AppMgtResponse res = new AppMgtResponse();
			 res.setStatus(-1);
			 res.setMessage("No such script "+swiftScript);
			 return res;
		 }
		 AppMgtResponse res = new AppMgtResponse();
		 res.setStatus(0);
		 res.setMessage("Success");
		 return res;
	 }
	 
	 /**
	  * @deprecated
	  * @param name
	  * @return
	  */
	 public String getGadgetHTML(String name){
		 String xmlFile =  "E:/prj/life-science-gw/LSGWGrid/apache-tomcat-5.5.25/webapps/gadgets/"+name+".xml";
		 String xsltFile = "E:/prj/life-science-gw/LSGWGrid/apache-tomcat-5.5.25/webapps/gadget-template/sidgrid.xsl";
		 try {
		   String html = GadgetGenerator.createGadgetHTML(xmlFile, xsltFile);
		   return html;
		 } catch (Exception e){
			 e.printStackTrace();
			 return e.toString();
		 }
	 }
	 /**
	  * @deprecated
	  * create a temp gadget HTML for the sidgrid application builder
	  * @param name
	  */
     public void createGadgetHTML(String name){
    	 String xmlFile =  "E:/prj/life-science-gw/LSGWGrid/apache-tomcat-5.5.25/webapps/gadgets/"+name+".xml";
    	 String htmlFile = "E:/prj/life-science-gw/LSGWGrid/apache-tomcat-5.5.25/webapps/gadgets/"+name+".html";
    	 String xsltFile = "E:/prj/life-science-gw/LSGWGrid/apache-tomcat-5.5.25/webapps/gadgets/form.xsl";
    	 try {
    		 String gadgetTmpDir = rootPath+"/gadget";
    		 GadgetGenerator generator = new GadgetGenerator();
    		 generator.createGadget(xmlFile, htmlFile, xsltFile, gadgetTmpDir);
    	 } catch(Exception e){
    		 e.printStackTrace();
    	 }
     }
	 public int mappingArgType(String type){
		 if (type==null)
			 return 0;
		 if (type.equalsIgnoreCase("PlainArgument")) return 0;
		 if (type.equalsIgnoreCase("SwitchArgument")) return 1;
		 if (type.equalsIgnoreCase("LongArgument"))  return 2;
		 return 0;
	 }
	 private AppMgtResponse checkAppExist(String appName){
		 DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		 ApplicationDAO appdao = daoFactory.getAppDAO();
		 Application tmp = null;
		 try {
		   tmp = appdao.getAppByName(appName);
		 } catch (Exception e){
			 e.printStackTrace();
			 AppMgtResponse res = createFailResponse("can't verify whether the application "+appName+" exists or not");
			 return res;
		 }
		 if (tmp !=null){
			 AppMgtResponse res = createFailResponse("the application "+appName+" already exists. Please change the name");
			 return res;
		 } else
			 return null;
	 }
	 private AppMgtResponse createFailResponse(String message){
		 AppMgtResponse response = new AppMgtResponse();
		 response.setStatus(AppMgtResponse.FAILURE);
		 response.setAppName("");
		 response.setMessage(message);
		 return response;
	 }
	 private AppMgtResponse createSuccessResponse(String appName){
		 AppMgtResponse response = new AppMgtResponse();
		 response.setStatus(AppMgtResponse.SUCCESS);
		 response.setAppName(appName);
		 response.setMessage("");
		 return response;
	 }
}

