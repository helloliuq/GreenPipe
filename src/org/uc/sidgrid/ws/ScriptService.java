package org.uc.sidgrid.ws;

import java.util.List;
import java.util.ArrayList;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.uc.sidgrid.services.AppMgtService;
import org.uc.sidgrid.app.*;
import org.uc.sidgrid.dao.DAOFactory;
import org.uc.sidgrid.dao.UserDAO;

/**
 * An Axis2 Service class
 * 
 * @author wenjun wu
 *
 */
public class ScriptService {
	 private AppMgtService mgt;
	 private List<String> userlist;
	 
	 private String mobyleRoot; // the directory that holds the application xml files
	 private String gadgetRoot; // the directory that holds the generated gadget XML files
	 private String gadgetUrl; // the root URL for the gadget
	 
	 /** session related methods **/
	 public void init(ServiceContext serviceContext){
		 AxisService service = serviceContext.getAxisService();
		 userlist = new ArrayList();
		 //temp implementation
		 mobyleRoot = (String)service.getParameterValue("mobyleRoot");
		 gadgetRoot = (String)service.getParameterValue("gadgetRoot");
		 gadgetUrl = (String)service.getParameterValue("gadgetUrl");
		 mgt = new AppMgtService();
		 mgt.setAppFilePath(mobyleRoot);
		 mgt.setGadgetPath(gadgetRoot);
		 mgt.setGadgetUrl(gadgetUrl);
	 }
	 public void destroy(ServiceContext serviceContext){
		 
	 }
	 public boolean login(String userName, String passWord) throws AxisFault{
		 DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
	     UserDAO userdao = daoFactory.getUserDAO();
	    	try {
	    	  User user = userdao.getUserByName(userName);
	    	  userlist.add(user.getName());
	    	  //test code here
	    	  //urgly coding: somehow axis2 classloader cannot load the stringtemplate class because the stingtemplate uses its own classloader
	    	  //so we have to copy the stringtemplate.jar to axis2 lib. this code is just a test reminder!!!
	    	  antlr.Utils.loadClass("org.antlr.stringtemplate.language.ChunkToken");
	    	  return true;
	    	} catch(Exception e){
	    		e.printStackTrace();
	    		return false;
	    	}
	 }
	 public AppMgtResponse addNewScript(String userName, String appName, String scriptName, String version, String scriptPkgUrl, 
			                                  String mainScript, String mobylexml) 
	 throws AxisFault{
		 if (!userlist.contains(userName)){
			 throw new AxisFault("The user has not logged in");
		 }
		 return mgt.addNewScript(appName, scriptName, version, scriptPkgUrl, mainScript, mobylexml);
	 }
	 public List<Application> listAllApplications(String userName)throws AxisFault{
		 if (!userlist.contains(userName)){
			 throw new AxisFault("The user has not logged in");
		 }
		 return mgt.showEveryApplication();
	 }
	 public AppScript[] listAllScripts(String userName, String appName)throws AxisFault{
		 if (!userlist.contains(userName)){
			 throw new AxisFault("The user has not logged in");
		 }
		 List<AppScript> scripts = mgt.showAllScripts(appName);
		 /** AppMgtResponse reply = new AppMgtResponse();
		 if (scripts == null){
			 reply.setStatus(-1);
			 reply.setMessage("can find the application");
		 } else {
			 reply.setStatus(0);
			 reply.setContent(scripts);
		 }
		 return reply; **/
		 AppScript[] res = new AppScript[0];
		 return scripts.toArray(res);
	 }
	 public AppMgtResponse removeScript(String userName, String appName, String scriptName, String version)throws AxisFault{
		 if (!userlist.contains(userName)){
			 throw new AxisFault("The user has not logged in");
		 }
		 return mgt.removeScript(appName, scriptName, version);
	 }
	 
	 //TODO: removeScript, updateScript
	
}
