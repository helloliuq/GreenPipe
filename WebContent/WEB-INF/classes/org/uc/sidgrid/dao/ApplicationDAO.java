package org.uc.sidgrid.dao;

import java.util.List;
import org.uc.sidgrid.app.Application;
import org.uc.sidgrid.app.AppScript;

public class ApplicationDAO extends GenericDAO{
	
	public Application getAppByName(String appName) throws Exception{
		 String queryStr = "from Application where AppName='"+appName+"'";
	     //List result = getSession().createQuery(queryStr).list();
		 List result = restoreList(queryStr);
	     if (result.size() < 1)
	    	 return null;
	     else
	    	 return (Application)result.get(0);
	}
	
	public void setAppStatus(String appName, String status)throws Exception{
		Application app = getAppByName(appName);
		if (app != null){
			app.setStatus(status);
		    update(app);
		}
	}
	public List<Application> getAllApplications(String status) throws Exception{
		 String queryStr = "from Application where status='"+status+"'";
		 List result = restoreList(queryStr);
	     return result;
	}
	public List<Application> getAllApplications() throws Exception{
		 String queryStr = "from Application";
		 List result = restoreList(queryStr);
	     return result;
	}
	public List<AppScript> getAllScripts(String app) throws Exception{
		String queryStr = "from AppScript script where script.application.appName='"+app+"'";
		List result = restoreList(queryStr);
		return result;
	}
	// query the script for the application
	public AppScript getScript(String app, String script)throws Exception{
		String queryStr = "from AppScript script where script.scriptName='"+script+"' and script.application.appName='"+app+"'";
		List result = restoreList(queryStr);
		if (result.size()>0)
			return (AppScript)result.get(0);
		else
			return null;
	}
	public AppScript getScript(String app, String script, String version)throws Exception{
		String queryStr = "from AppScript script where script.scriptName='"+script+"' and script.application.appName='"+app+
		                 "' and script.version='"+version+"'";
		List result = restoreList(queryStr);
		if (result.size()>0)
			return (AppScript)result.get(0);
		else
			return null;
	}
	// query the gadget for the application
}
