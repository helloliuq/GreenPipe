package org.uc.sidgrid.services;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uc.sidgrid.dao.sidgridDAO;

import java.net.URL;
import com.metaparadigm.jsonrpc.JSONRPCBridge;
/**
 * This is a factory class for initiating JSONRPC Service instances
 * Each time it receives a new http session, it creates a JSONRPC instance for this session.
 * @author 
 *
 */
public class Jsonlistener implements HttpSessionListener {
	private static Log log = LogFactory.getLog(Jsonlistener.class);
	
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		
			HttpSession session = sessionEvent.getSession();
		String sessionID = session.getId();
		log.info("Creating Session, exporting services... for " + sessionID);
		// get the root path and url
		ServletContext context = session.getServletContext();
		// Attention: here the webroot has the SIDGridPortal prefix
		String webAppRoot = context.getRealPath("/");
		//String webAppRoot = "/root/";
		log.info("webroot for this session is " + webAppRoot);
		String webUrl = "http://localhost:8080/";
		try {
			URL root = context.getResource("/");
			// TODO: need to figure out why it generates jndiURL
			// Comment: it is weird that servletcontext can not get the root url
			if (root.getProtocol().equals("JNDI")) {// TOMCAT, JBOSS
				// can only get the localhost instead of real host adress
			}
			webUrl = context.getInitParameter("WebUrl");
			// String[] tmp = jndiURL.split(":");
		} catch (java.net.MalformedURLException e) {
			e.printStackTrace();
		}
		String webAppName="";
		try {
			webAppName = context.getInitParameter("WebAppName");
		} catch (Exception e) {
			System.out.println("Error getting WebAppName parameter value");
			e.printStackTrace();
		}

		String mobyleXmlPath = webAppRoot + "/" + "mobylexml";
		// String gadgetPath = context.getInitParameter("GadgetPath");
		String gadgetPath = webAppRoot + "/" + "SIDGridGadgets";
		// create a json bridge for this session
		JSONRPCBridge json_bridge = null;
		json_bridge = (JSONRPCBridge) session.getAttribute("JSONRPCBridge");
		if (json_bridge == null) {
			json_bridge = new JSONRPCBridge();
			session.setAttribute("JSONRPCBridge", json_bridge);
			// System.out.println("bridge created");
		}
		json_bridge.setDebug(true);
		// register services
		// TODO: use Java annotation to load the jsonrpc services and init their
		// context

		Jobstate Jobobj = new Jobstate();
		System.out.println("object1 registered");

		json_bridge.registerObject("Jobobj", Jobobj);
		System.out.println("object2 registered");

		Path p = new Path();
		json_bridge.registerObject("p", p);
		System.out.println("object3 registered");

		sidgridDAO mediaobj = new sidgridDAO();
		json_bridge.registerObject("mediaobj", mediaobj);
		log.info("sidgridDAO registered");

		// AppMgtService appMgr = new AppMgtService();
		AppMgtService appMgr = new AppMgtJSONRPC();
		PortalPathContxt ctx = PortalPathContxt.getInstance();
		// better to move up the folder tree to point to the real root
		ctx.setRoot(webAppRoot + "/../");
		ctx.setPortalAppRoot(webAppRoot);
		ctx.setPortalAppName(webAppName);
		// from webapp URL to the root URL
		try {
			URL url = new URL(webUrl);
			String host = url.getHost();
			String port = Integer.toString(url.getPort());
			String rootUrl = "http://" + host + ":" + port + "/";
			System.out.println("set current root url " + rootUrl);
			ctx.setRootURL(rootUrl);
		} catch (Exception e) {
			System.out.println("error setting root url");
			e.printStackTrace();
		}

		System.out.println("init appMgr: " + mobyleXmlPath + " " + gadgetPath
				+ " " + webUrl);
		appMgr.setAppFilePath(mobyleXmlPath);
		appMgr.setGadgetPath(gadgetPath);
		appMgr.setGadgetUrl(webUrl);
		json_bridge.registerObject("AppMgr", appMgr);

		WorkflowService wfServ = new WorkflowService();
		wfServ.setAppFilePath(mobyleXmlPath);
		json_bridge.registerObject("WorkflowService", wfServ);

		GroupService grpServ = new GroupService();
		json_bridge.registerObject("GroupService", grpServ);

		FileBrowsingService fileServ = new FileBrowsingService();
		fileServ.setURL(webUrl);
		fileServ.setRootPath(webAppRoot);
		json_bridge.registerObject("FileService", fileServ);
            
            
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO remove the created ProteinAlignImpl object?
		
        // TODO remove the possible cookie associated with this session?
		
	}

}
