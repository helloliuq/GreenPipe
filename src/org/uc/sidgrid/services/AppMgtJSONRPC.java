package org.uc.sidgrid.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.swift.language.ProgramDocument;
import org.uc.sidgrid.app.AppMgtResponse;
import org.uc.sidgrid.app.AppParameter;
import org.uc.sidgrid.app.AppScript;
import org.uc.sidgrid.app.Application;
import org.uc.sidgrid.dao.ApplicationDAO;
import org.uc.sidgrid.dao.DAOFactory;
import org.uc.sidgrid.mobyle.core.SwiftGadgetGenerator;
import org.uc.sidgrid.mobyle.core.SwiftToMobyle;
import org.uc.sidgrid.util.ServiceUtil;
import org.uc.sidgrid.util.Chmod;

/**
 * this is an extended class for supporting front-end JSONRPC javascript codes
 * @author wenjun
 *
 */
public class AppMgtJSONRPC extends AppMgtService{
	private static Log log = LogFactory.getLog(AppMgtJSONRPC.class);
	
	/**
	 * get an parameter list from a defined application or script
	 * @param appName : the application name
	 * @return
	 */
	public String createScriptPkg(String sessionID, List<String> fileUrls, String zipfile){
		List<String> filenames = new ArrayList<String>();
		PortalPathContxt ctx = PortalPathContxt.getInstance();
	    log.info("start to move the script pkg files");
		for(String url : fileUrls){
			log.info("file url is "+url);
			String pkgfile = ServiceUtil.locatePhysicalFile(url);
			//Attention: pkgfile includes the webapp path
			pkgfile = ctx.getRoot()+"/"+pkgfile;
			
			//copy all the files to the sessionID folder
			String basename = ServiceUtil.baseName(pkgfile);
			filenames.add(basename);
			String desfile = ctx.getPortalAppRoot()+"/temp/"+sessionID+"/"+basename;
			log.info("copy the file "+pkgfile+" to "+desfile);
			try {
			  ServiceUtil.copyFile(pkgfile, desfile);
			//Attention: keep the external mapper files as "exectuable"
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		// zip all the files
		//ServiceUtil.zipFile(filenames, zipfile);
		String zippkgUrl = ctx.getPortalAppRoot()+"/temp/"+sessionID+"/"+zipfile;
		return(zippkgUrl);
	 }
	 /*
	  * create a new application gadget from the uploaded swift script file
	  *@param appName: the name of the application
	  *@param scriptName: the script name
	  *@param version: the (svn?) version from the script
	  *@param swiftScript: the name of the zipped file of the swift script
	 * @param mobyleXml: the name of the mobyle xml 
	  */
	 public AppMgtResponse addNewScript(String sessionID, String appName, String scriptName, String version, 
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
		} catch (Exception e) {
			e.printStackTrace();
			tmp = null;
		}
		// step1: copy the scriptPkg files to root/appName/swiftScript
		try {
			// TODO: we will add it back
			/**
			 * if ( !filelist.contains(mainScript)){ AppMgtResponse response =
			 * new AppMgtResponse(); response.setStatus(-1);
			 * response.setMessage(
			 * "Can't find the main script in the application package"); return
			 * response; }
			 **/
			PortalPathContxt ctx = PortalPathContxt.getInstance();
			String srcdir = ctx.getPortalAppRoot() + "/temp/" + sessionID;
			String scriptDir = ServiceUtil.scriptVersionPath(scriptName,
					version);
			String desdir = ctx.getScriptRoot() + "/" + appName + "/"
					+ scriptDir;
			log.info("put the new script package to " + desdir);
			File appdir = new File(desdir);
			if (!appdir.exists()) {
				appdir.mkdirs();
			}
			System.out.println("Copying files from " + srcdir + " to " + desdir );
			Chmod.nativeCpDir(srcdir, desdir);
			// step2: write mobyleXml to the xmlName
			String des_mobyle_xml = ctx.getMobyleXMLRoot() + "/" + appName
					+ "/" + scriptDir + "/" + mobylexml;
			// if(filelist.contains(mobylexml) ) {
			String src_mobyle_xml = ctx.getScriptRoot() + "/" + appName + "/"
					+ scriptDir + "/" + mobylexml;
			ServiceUtil.copyFile(src_mobyle_xml, des_mobyle_xml);
			// step3: create a new gadget when a new mobylexml exists in the
			// application package
			log.info("creating the gadget using the xml file "
							+ des_mobyle_xml);
			String gadgetTmpDir = ctx.getGadgetTemplateRoot();
			log.info("using gadgetTempdir " + gadgetTmpDir);
			SwiftGadgetGenerator generator = SwiftGadgetGenerator.getInstance();
			generator.setTemplateDir(ctx.getGadgetTemplateRoot());
			generator.setUploadURL(ctx.getAppRootURL());
			generator.setWebURL(ctx.getAppRootURL());
			String gadgetXML = generator.createSwiftGadget(appName, scriptName,
					version, des_mobyle_xml);
			//String des_gadget_xml = ctx.getGadgetRoot() + "/" + scriptName + ".xml";
			String des_gadget_xml = new File(ctx.getGadgetRoot(), scriptName + ".xml").toString();
			System.out.println("Writing gadget to " + des_gadget_xml );
			ServiceUtil.writeNewFile(des_gadget_xml, gadgetXML);
			/**
			 * } else { AppMgtResponse response = new AppMgtResponse();
			 * response.setStatus(-2); response.setMessage(
			 * "Can't find the interface xml description in the application package"
			 * ); return response; }
			 **/
			// step4: create Application and/or AppScript persistent data object
			if (tmp == null) {
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
			// TODO: use the basename of the mobylexml instead??
			// TODO: just for now. most JS files for the gadget still need to
			// point to the portal URL
			log.info("gadgetLink is " + this.gadgetUrl);
			// String gadgetLink = this.gadgetUrl.replace("SIDGridPortal",
			// "SIDGridGadgets");
			// String gadgetLink = ctx.getGadgetAppRoot();
			String gadgetLink = ctx.getGadgetRootURL() + scriptName + ".xml";
			// gadgetLink +="/"+appName+"/"+scriptDir+"/"+scriptName+".xml";
			script.setGadgetLink(gadgetLink);
			script.setMainscript(mainScript);
			script.setType("swift");
			script.setVersion(version);
			// TODO: do we need to check whether the same version of script
			// exists or not
			script.setApplication(tmp);
			if (OldScript == null)
				appdao.create(script);
			// else
			// appdao.update(script);

		} catch (Exception e) {
			e.printStackTrace();
			AppMgtResponse res = new AppMgtResponse();
			res.setStatus(-1);
			res.setMessage(e.getMessage());
			return res;
		}
		AppMgtResponse res = new AppMgtResponse();
		res.setAppName(appName);
		res.setStatus(0);
		return res;
	 }
	 public List<AppParameter> guessParameters(String sessionId, String swiftfileurl){
		 String swiftfile = ServiceUtil.locatePhysicalFile(swiftfileurl);
		 SwiftToMobyle parser = new SwiftToMobyle();
		 
		 PortalPathContxt ctx = PortalPathContxt.getInstance();
		 swiftfile = ctx.getRoot()+"/"+swiftfile;
		 log.info("parsing the swift script file "+swiftfile);
		 String outputdir = ctx.getPortalAppRoot()+"/temp/"+sessionId;
		 File outputDir = new File(outputdir);
		 if (!outputDir.exists()){
			 outputDir.mkdirs();
			 log.info("create a temp folder "+outputDir);
		 } else
			 log.info("the temp folder "+outputDir+" exists");
		 String templateDir = ctx.getPortalAppRoot()+"/mobylexml/swift";
		 parser.setTemplateDir(templateDir);
		 System.out.println("guessParameters using templatedir:" + templateDir + " swiftfile: " + swiftfile + " outputdir:"+outputdir);
		 try { 
			 ProgramDocument programDoc = parser.compile(swiftfile, outputdir);
			 List<AppParameter> params = parser.findArgParameters(programDoc);
			 for(AppParameter param : params){
					System.out.println(param.getName()+" "+param.getPrompt() + " " + param.getValue());
			 }
			 return params;
		 } catch (Exception e){
			 e.printStackTrace();
			 return null;
		 }
		 
	 }
	 // parse the commandline type-in by users and return a list of AppParameter
}
