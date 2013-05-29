package org.uc.sidgrid.services;

import java.sql.*;
import javax.sql.DataSource;
import javax.servlet.*;
import org.uc.sidgrid.swift.WorkflowMgrThread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServletInitializer implements ServletContextListener{
	private static Log log = LogFactory.getLog(ServletInitializer.class);
	WorkflowMgrThread mgr;
	
	public void contextInitialized(ServletContextEvent event)
	  {
	    ServletContext servletContext = event.getServletContext();
	    log.info("Initializing Project SIDGridPortal");
	    String sidgridDataPath = servletContext.getInitParameter("SidGridDataPath");
	    WorkflowFactory wfFac = WorkflowFactory.getInstance();
	    wfFac.setSidgridDataPath(sidgridDataPath);
	    String root = servletContext.getRealPath("/");
	    wfFac.setLocalPath(root+"/workflow");
	    wfFac.setTemplatePath(root+"/mobylexml/gadget");
	    wfFac.setUploadDataPath(root+"/temp");
	    
	    // get the wfmgr script
	    //String wfmgr = servletContext.getInitParameter("WorkflowManager");
	    //mgr = new WorkflowMgrThread(wfmgr, "");
	    //Thread worker = new Thread(mgr);
		//worker.start();
	  }
	 public void contextDestroyed(ServletContextEvent event)
	  {
	    log.info("Destorying Project SIDGrid Portal");
	    //destory the mgr
	    //mgr.stop();
	  }
	 
	 
}
