package org.uc.sidgrid.client;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.gnu.getopt.Getopt;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.uc.sidgrid.app.AppMgtResponse;
import org.uc.sidgrid.app.Application;
import org.uc.sidgrid.app.AppScript;

public class ScriptAdminClient {
	 private RPCServiceClient rpcClient;
	 
	 public ScriptAdminClient(){
	   try {
		 this.rpcClient = new RPCServiceClient();
	     Options opts = new Options();
	     EndpointReference targetEPR = new EndpointReference(
         	"http://sidgrid.ci.uchicago.edu:8888/axis2/services/ScriptService"); 
         opts.setTo(targetEPR);
	     rpcClient.setOptions(opts);
	   } catch(Exception e){
		   e.printStackTrace();
	   }
	 }
	 public  boolean login(String userName,String passWord) throws Exception {
       rpcClient.getOptions().setAction("urn:login");
       ArrayList args = new ArrayList();
       args.add(userName);
       args.add(passWord);
       Object obj [] = rpcClient.invokeBlocking(new QName("http://ws.sidgrid.uc.org",
                        "login"), args.toArray(), new Class[]{Boolean.class});
       return ((Boolean) obj[0]).booleanValue();
    }
	// upload a file to http server
	//public String upload(String file){
	//	
	//}
	public void showApplications(String username) throws Exception{
		rpcClient.getOptions().setAction("urn:listAllApplications");
		ArrayList args = new ArrayList();
		args.add(username);
		OMElement apps = rpcClient.invokeBlocking(new QName("http://ws.sidgrid.uc.org","listAllApplications"), 
				           args.toArray());
		printAppData(apps);
		
	}
	public void showAllScripts(String username, String appName) throws Exception{
		rpcClient.getOptions().setAction("urn:listAllScripts");
		ArrayList args = new ArrayList();
		args.add(username);
		args.add(appName);
		OMElement scripts = rpcClient.invokeBlocking(new QName("http://ws.sidgrid.uc.org","listAllApplications"), 
				           args.toArray());
		
		Iterator values = scripts.getChildrenWithName(new QName("http://ws.sidgrid.uc.org", "return"));
        while (values.hasNext()) {
             OMElement omElement = (OMElement) values.next();
             AppScript script = (AppScript) BeanUtil.deserialize(AppScript.class, omElement, new DefaultObjectSupplier(), "appscript");
             System.out.println("Application Name is "+script.getApplication().getAppName());
             System.out.println("Script Name is "+script.getScriptName());
             System.out.println(script.getXmlDesc());
         }
	}
	
	public void delScript(String username, String appName, String scriptName, String version)throws Exception{
		rpcClient.getOptions().setAction("urn:removeScript");
		ArrayList args = new ArrayList();
		args.add(username);
		args.add(appName);
		args.add(scriptName);
		args.add(version);
		Object obj [] = rpcClient.invokeBlocking(new QName("http://ws.sidgrid.uc.org","removeScript"), 
		           args.toArray(), new Class[]{AppMgtResponse.class});
		AppMgtResponse result = (AppMgtResponse) (obj[0]);
		System.out.println(result.toString());
		System.out.println(result.getMessage());
	}
	private void printAppData(OMElement element) throws Exception {
	        if (element != null) {
	            Iterator values = element.getChildrenWithName(new QName("http://ws.sidgrid.uc.org", "return"));
	            while (values.hasNext()) {
	                OMElement omElement = (OMElement) values.next();
	                Application app = (Application) BeanUtil.deserialize(Application.class, omElement, new DefaultObjectSupplier(), "application");
	                System.out.println("Name is "+app.getAppName());
	                System.out.println("Title is "+app.getTitle());
	                System.out.println(app.getStatus());
	                System.out.println(app.getXmlDesc());
	            }

	        }
	 }
	public void addNewScript(String username, String appname, String scriptname, String version, 
			                 String pkgUrl, String mainScript, String mobylexml) 
	throws Exception {
		rpcClient.getOptions().setAction("urn:addNewScript");
		ArrayList<String> args = new ArrayList<String>();
		args.add(username);
		args.add(appname);
		args.add(scriptname);
		args.add(version);
		args.add(pkgUrl);
		args.add(mainScript);
		args.add(mobylexml);
		Object obj [] = rpcClient.invokeBlocking(new QName("http://ws.sidgrid.uc.org","addNewScript"), 
		           args.toArray(), new Class[]{AppMgtResponse.class});
		AppMgtResponse result = (AppMgtResponse) (obj[0]);
		System.out.println(result.toString());
		System.out.println(result.getMessage());
	}
	
	public static void main(String[] args) throws Exception {
		Getopt g = new Getopt("ScriptAdminClient", args, "u:p:");
		int c;
		String arg="";
		while ((c=g.getopt()) != -1){
			switch(c){
			case 'c':
				arg = g.getOptarg();
				System.out.println("the conf file is "+arg);
				break;
			}
		}
	   if (arg.length() <=0)
			System.exit(0);
	   
	   ScriptAdminClient client = new ScriptAdminClient();
       boolean test = client.login("wwjag", "WWJag@007");
       System.out.println(test);
       //client.addNewScript();
       //client.showApplications("wwjag");
       client.showAllScripts("wwjag", "oops");
       //client.delScript("wwjag", "modpipe", "modpipe-v1", "v1.0");
    }
}
