package org.uc.sidgrid.services;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import org.apache.axis.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.uc.sidgrid.mobyle.ProgramDocument;
import org.uc.sidgrid.mobyle.core.Argument;
import org.uc.sidgrid.mobyle.core.*;
import org.uc.sidgrid.app.Application;
import org.uc.sidgrid.app.Workflow;
import org.uc.sidgrid.app.RunArgument;
import org.uc.sidgrid.app.User;
import org.uc.sidgrid.app.WorkflowJson;
import org.uc.sidgrid.dao.*;
import org.uc.sidgrid.data.Experiment;

public class WorkflowService {
	private static Log log = LogFactory.getLog(WorkflowService.class);
	private String rootPath; // the directory that holds the application xml files
	
	public WorkflowService(){
		
	}
	public void setAppFilePath(String path){
		this.rootPath = path;
	}
	public String runWorkflow(String userName, String appName, String scriptName, String version, Hashtable<String,String> valuePairs){
		User user = checkUser(userName);
		if (user == null)
			return null;
		System.out.println("runWorkflow: script=" + scriptName );
		WorkflowFactory factory = WorkflowFactory.getInstance();
	    Workflow wf;
		try {
			
		  //Workflow wf = factory.createWfFromScript(user, appName, scriptName, version, valuePairs);
			// perhaps temporarily, determine whether to run a cmd app or a script based on the arguments given
		    if ( scriptName.length() == 0 && version.length() == 0 ){
				DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
			    ApplicationDAO appdao = daoFactory.getAppDAO();
			    Application app = appdao.getAppByName(appName);
				String filename = this.rootPath+"/"+appName+".xml";
				File xmlFile = new File(filename);  
				ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);    
				wf = factory.createWorkflow(null,appName, programDoc, valuePairs);
		    } else{
		    	wf = factory.createWfFromScript(user, appName, scriptName, version, valuePairs);
		    }
		  
		  wf.setUser(user);
		  DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		  WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
		  System.out.println("runWorkflow: wf script = " + wf.getScript());
		  wfdao.create(wf);
		  return wf.getWorkflowID();
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * this method runs a general swift workflow, the results will stay in a temp folder
	 * @param userName
	 * @param appName
	 * @param valuePairs
	 * @return workflow id
	 */
	public String runWorkflow(String userName, String appName, Hashtable<String,String> valuePairs){
	  try {
		DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		// generate the swift workflow script
		String filename = this.rootPath+"/"+appName+".xml";
		File xmlFile = new File(filename);  
	    ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);    
		// create a workflow object and set the status to "created"
	    WorkflowFactory factory = WorkflowFactory.getInstance();
	    //TODO: we need to check whether to create a new swift script or to just copy a pre-defined swift template !!
	    
	    Workflow wf;
	    ApplicationDAO appdao = daoFactory.getAppDAO();
	    Application app = appdao.getAppByName(appName);
	    if (app.getType().equals(Application.cmdType)){
	       wf = factory.createWorkflow(null,appName, programDoc, valuePairs);
	    } else{
	       wf = factory.createWfFromScript(null,appName, programDoc, valuePairs);
	    }
	    //TODO: need to check whether this user exists or not in the user table
	    User user = new User(); user.setName(userName);
	    wf.setUser(user);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
		wfdao.create(wf);
		return wf.getWorkflowID();
	  } catch(Exception e){
		  e.printStackTrace();
		  //TODO: can we return some error codes
		  return null;
	  }
	}
	// this method run a swift workflow for sidgrid that needs data importing step
	public String runSIDGridWorkflow(String userName, int expId, String expName, 
			                         String appName, Hashtable<String,String> valuePairs){
		 try {
				// generate the swift workflow script
				String filename = this.rootPath+"/"+appName+".xml";
				File xmlFile = new File(filename);  
			    ProgramDocument programDoc = ProgramDocument.Factory.parse(xmlFile);    
				// create a workflow object and set the status to "created"
			    WorkflowFactory factory = WorkflowFactory.getInstance();
			    Workflow wf = factory.createSIDGridWorkflow(null, expId, expName, appName, programDoc, valuePairs);
			    //TODO: need to check whether this user exists or not in the user table
			    User user = new User(); user.setName(userName);
			    wf.setUser(user);
                //find the matching experiment
			    DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
			    ExperimentDAO expdao = daoFactory.getExperimentDAO();
			    Experiment exp = expdao.getExpById(expId);
				wf.setExp(exp);
				WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
				wfdao.create(wf);
				return wf.getWorkflowID();
			  } catch(Exception e){
				  e.printStackTrace();
				  //TODO: can we return some error codes
				  return null;
		   }
	}
    public static int getTotalWfs(String userName){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
    	int num = wfdao.getTotalWorkflows(userName);
    	
    	return num;
	}
    public static int getTotalWfs(){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
    	int num = wfdao.getTotalWorkflows();
    	return num;
	}
    public static List<WorkflowJson> getAllWfs(String userName){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
		
    	List<Workflow> wflist = wfdao.getAllWorkflows(userName);
    	List<WorkflowJson> jsonlist = new ArrayList<WorkflowJson>();
    	for(Workflow tmp : wflist){
    		WorkflowJson json_tmp = new WorkflowJson(tmp);
    		jsonlist.add(json_tmp);
    	}
    	return jsonlist;
    }
    public static List getAllWfs(){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
		
    	List<Workflow> wflist = wfdao.getAllWorkflows();
    	List<WorkflowJson> jsonlist = new ArrayList<WorkflowJson>();
    	for(Workflow tmp : wflist){
    		WorkflowJson json_tmp = new WorkflowJson(tmp);
    		jsonlist.add(json_tmp);
    	}
    	return jsonlist;
    }
    public static WorkflowJson getWorkflow(String userName,String wfId){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
		Workflow wf = wfdao.getWorkflow(userName, wfId);
		WorkflowJson wfjson = new WorkflowJson(wf);
		return wfjson;
    }
    private List<Workflow> getAllWorkflows(String userName){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
		
    	List<Workflow> wflist = wfdao.getAllWorkflows(userName);
    	return wflist;
    }
    private List<Workflow> getAllWorkflows(){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
		
    	List<Workflow> wflist = wfdao.getAllWorkflows();
    	return wflist;
    }
    /**
     * only list the workflows for one web page
     * @param userName
     * @param pageNum
     * @param wfPerPage
     * @return 
     */
    public List getWorkflowsPerPage(String userName, int pageNum, int wfPerPage){
        //org.gridlab.gridsphere.portlet.User gridsphereUser = userManagerService.getLoggedInUser(loggedInUser);
   	   //String gsoid = gridsphereUser.getID();
        try {
        	if( pageNum <= 0 || wfPerPage <= 0 )
        		return null;
           List<Workflow> wflist = getAllWorkflows(userName);
   		   Collections.reverse(wflist);
   		   int totalWFs = wflist.size();
   		   int  totalWfPages = totalWFs / wfPerPage; // how many pages can be displayed
   		   int start_index = wfPerPage * (pageNum-1);
   		   // copy a new workflow list for the current page
   		   ArrayList jobBeanList = new java.util.ArrayList();
   		   if (wfPerPage > totalWFs) wfPerPage = totalWFs;
  		   int show_page_len = totalWFs > start_index+wfPerPage ? wfPerPage : wfPerPage-start_index;
  		  
  		   System.out.println("total "+totalWfPages+":start_index:"+start_index+":show_page_len"+show_page_len);
  		   for(int i=start_index; i < start_index+show_page_len; i++){
  			 Workflow wfBean = (Workflow)wflist.get(i);
  			 //log.info("job id "+wfBean.getJobID());
  			 WorkflowJson json = new WorkflowJson(wfBean);
  			//TODO: need to use JobJSON that can convert java.sql.timestamp to string format
  			jobBeanList.add(json);
  		  }
   		  return jobBeanList;
   	   } catch (Exception e){
   		   e.printStackTrace();
   		   return null;
   	   }
    }
    public List getWorkflowsPerPage(int pageNum, int wfPerPage){
        try {
        	if( pageNum <= 0 || wfPerPage <= 0 )
        		return null;
           List wflist = getAllWorkflows();
   		   Collections.reverse(wflist);
   		   int totalWFs = wflist.size();
   		   int  totalWfPages = totalWFs / wfPerPage; // how many pages can be displayed
   		   int start_index = wfPerPage * (pageNum-1);
   		   // copy a new workflow list for the current page
   		   ArrayList jobBeanList = new java.util.ArrayList();
   		   if (wfPerPage > totalWFs) wfPerPage = totalWFs;
  		   int show_page_len = totalWFs > start_index+wfPerPage ? wfPerPage : wfPerPage-start_index;
  		  
  		   System.out.println("total "+totalWfPages+":start_index:"+start_index+":show_page_len"+show_page_len);
  		   for(int i=start_index; i < start_index+show_page_len; i++){
  			 Workflow wfBean = (Workflow)wflist.get(i);
  			 //log.info("job id "+wfBean.getJobID());
  			 WorkflowJson json = new WorkflowJson(wfBean);
  			//TODO: need to use JobJSON that can convert java.sql.timestamp to string format
  			jobBeanList.add(json);
  		  }
   		  return jobBeanList;
   	   } catch (Exception e){
   		   e.printStackTrace();
   		   return null;
   	   }
    }
    //TODO: can we just return the whole workflow-json object(?)
    // get the swift script
    public String getCmd(String userName, String wfId){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
    	Workflow wf = wfdao.getWorkflow(userName, wfId);
    	String scriptFile = wf.getScript();
    	String content = "";
    	try { 
    		FileReader reader = new FileReader(scriptFile);
    		BufferedReader br = new BufferedReader(reader);
    		String s;
    		while((s = br.readLine()) != null) {
    		  //TODO: encode the line so that the special character could be displayed too
    		  content += s+"\n";
    		}
    		reader.close(); 
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    	return content;
    }
//  get the swift script
    public String getInputLinks(String userName, String wfId){
    	String content = readLinkFiles(userName, wfId, "inputLinks");
    	return content;
    }
    public String getOutputLinks(String userName, String wfId){
    	String content = readLinkFiles(userName, wfId, "outputLinks");
    	return content;
    }
    public String getWorkDir(String userName, String wfId){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
    	Workflow wf = wfdao.getWorkflow(userName, wfId);
    	if (wf == null)
    		return null;
    	String scriptFile = wf.getScript();
    	/** String [] tmp = wfId.split("-"); String programName = tmp[0];
    	int indx = scriptFile.indexOf(programName+".swift");
    	String localDir = scriptFile.substring(0, indx-1); **/
    	File file = new File(scriptFile);
		String localDir = file.getParent();
    	return localDir;
    }
    public List getArgs(String userName, String wfId){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
    	List<RunArgument> args = wfdao.getArgs(wfId);
    	return args;
    }
    public String getProvenance(String userName,String wfId){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
    	Workflow wf = wfdao.getWorkflow(userName, wfId);
    	if (wf == null)
    		return null;
    	String provenance = wf.getProvenance();
    	//TODO: better json-representation for provenance tree.
    	return provenance;
    }
    private String readLinkFiles(String userName, String wfId, String fileName){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
    	Workflow wf = wfdao.getWorkflow(userName, wfId);
    	String scriptFile = wf.getScript();
    	// get the local directory for this workflow
    	String [] tmp = wfId.split("-"); String programName = tmp[0];
    	int indx = scriptFile.indexOf(programName+".swift");
    	String localDir = scriptFile.substring(0, indx-1);
    	// read the file
    	String linkFile = localDir+"/"+fileName;
    	String content = "";
    	try { 
    		FileReader reader = new FileReader(linkFile);
    		BufferedReader br = new BufferedReader(reader);
    		String s;
    		while((s = br.readLine()) != null) {
    		  //create a hyper link point to the file ( filename:filelink )
    		  String [] tmp1 = s.trim().split("=>");
    		  if (tmp1.length>=2){
    			  content += "<a href=\""+tmp1[1]+"\">"+tmp1[0]+"</a><br>";
    		  }
    		}
    		reader.close(); 
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    	return content;
    }
    private String getLocalJobDir(Workflow wf){
    	String scriptFile = wf.getScript();
    	// get the local directory for this workflow
    	String wfId = wf.getWorkflowID();
    	String [] tmp = wfId.split("-"); String programName = tmp[0];
    	int indx = scriptFile.indexOf(programName+".swift");
    	String localDir = scriptFile.substring(0, indx-1);
    	return localDir;
    }
    public User checkUser(String userName){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
    	UserDAO userdao = daoFactory.getUserDAO();
    	try {
    	  User user = userdao.getUserByName(userName);
    	  return user;
    	} catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public int cancelWorkflow(String wfId){
    	DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
		Workflow wf = wfdao.getWorkflow(wfId);
		if( wf.getStatus().equals(Workflow.Created) || wf.getStatus().equals(Workflow.Submitted) || wf.getStatus().equals(Workflow.Active) ){
			System.out.println("Setting workflow status to RequestCancel");
			wf.setStatus(Workflow.RequestCancel);
			try{
				wfdao.saveOrUpdate(wf);
				return 0;
			} catch(Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
		else {
			System.out.println("Workflow cancel requested, but disallowed due to status: " + wf.getStatus());

		}
		// indicate that workflow could not be cancelled
		return 1;
    }

    
    
	/** public static String checkStatus(String ws_services_key, String jobId)throws AxisFault{
		
	}
	public static String[] getJobIds(String ws_services_key)throws AxisFault{
		
	}
	public static List<String> getResults(String ws_services_key, String jobId) throws AxisFault{
		
	} **/
}
