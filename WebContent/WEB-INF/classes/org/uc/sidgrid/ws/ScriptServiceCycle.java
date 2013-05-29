package org.uc.sidgrid.ws;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.engine.ServiceLifeCycle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uc.sidgrid.mobyle.core.SwiftGadgetGenerator;
import org.uc.sidgrid.services.PortalPathContxt;
import java.io.File;

public class ScriptServiceCycle implements ServiceLifeCycle{
	
	public void startUp(ConfigurationContext configctx,
            AxisService service) {
		String webRootURL = configctx.getContextRoot();
	    System.out.println("webRootURL via ContextRoot = " + webRootURL );
		File tmp = configctx.getRealPath("/");
		// the root should axis2/WEB-INF
		System.out.println("current root path is "+tmp.getAbsolutePath());
	    File parent = tmp.getParentFile();
	    System.out.println("looking for the parent "+parent.getAbsolutePath());
		String webroot = parent.getParentFile().getParent();
		
		try {
		 Parameter para = service.getParameter("WebURL");
		 webRootURL = (String)para.getValue();
	     System.out.println("webRootURL via WebUrl parameter = " + webRootURL );
		 service.addParameter(new Parameter("mobyleRoot", ""));
		 service.addParameter(new Parameter("gadgetRoot", webroot+"/SIDGridGadgets"));
		 service.addParameter(new Parameter("gadgetUrl", webRootURL+"/SIDGridPortal"));
		} catch(Exception e){
		     System.out.println("Failed to get WebUrl parameter" );
			e.printStackTrace();
		}
		
		String webAppName = "Uninitialized";
		try {
		 Parameter para = service.getParameter("AppName");
		 webAppName = (String)para.getValue();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		PortalPathContxt ctx = PortalPathContxt.getInstance();
		//Attention: here the webroot is set to the webapps path
		ctx.setRoot(webroot);
		ctx.setPortalAppRoot(webRootURL);
		ctx.setPortalAppName(webAppName);
		
		
		SwiftGadgetGenerator generator = SwiftGadgetGenerator.getInstance();
		generator.setTemplateDir(ctx.getGadgetTemplateRoot());
		//generator.setUploadURL(webRootURL+"/SIDGridPortal");
		//generator.setWebURL(webRootURL+"/SIDGridPortal");
		generator.setUploadURL(webRootURL);
		generator.setWebURL(webRootURL);
	}
	public void shutDown(ConfigurationContext configctx,
            AxisService service) {
		
	}
}
