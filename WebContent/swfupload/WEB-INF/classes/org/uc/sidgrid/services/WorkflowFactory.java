package org.uc.sidgrid.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uc.sidgrid.app.User;
import org.uc.sidgrid.app.Workflow;
import org.uc.sidgrid.app.AppScript;
import org.uc.sidgrid.app.RunArgument;
import org.uc.sidgrid.dao.*;
import org.uc.sidgrid.mobyle.ProgramDocument;
import org.uc.sidgrid.mobyle.core.Argument;
import org.uc.sidgrid.mobyle.core.CmdGenerator;
import org.uc.sidgrid.mobyle.core.ScriptGenerator;

import org.uc.sidgrid.util.*;

public class WorkflowFactory {
	private WorkflowDAO wfdao;
	private ApplicationDAO appdao;
	
	private static long lastTime = 0;
	private static DateFormat UID_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmm");
	private static Log log = LogFactory.getLog(WorkflowFactory.class);
	private static WorkflowFactory instance = null;
	private static String localPath; // root path of the workflow
	private static String templatePath; // root path of the xml template 
	private static String sidgridDataPath; // root path of the sidgrid experiment data files
	private static String uploadDataPath; // root path of the uploaded files
	
	public static WorkflowFactory getInstance() {
		   if(instance == null) {
		         instance = new WorkflowFactory();
		   }
		   return instance;
	}
	private WorkflowFactory(){
		DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		wfdao = daoFactory.getWorkflowDAO();
		appdao = daoFactory.getAppDAO();
	}
	public void setLocalPath(String path){
		localPath = path;
	}
	public void setTemplatePath(String path){
		templatePath = path;
	}
	public void setSidgridDataPath(String path){
		sidgridDataPath = path;
	}
	public void setUploadDataPath(String path){
		uploadDataPath = path;
	}
	/**
	 * create a workflow object and script 
	 * @param user
	 * @param program
	 * @param programDoc
	 * @param params
	 * @return workflow object
	 */
	public Workflow createWorkflow(User user,String program, ProgramDocument programDoc, Hashtable params)throws IOException{
		String workflowId = program+"-"+getUUID();
		// create a temp directory for this workflow execution 
		String localJobdir = getLocalJobDir(workflowId);
		createLocalJobDir(localJobdir, workflowId);
		// run the program's xml description and generate the cmd line
		// margs will be filled with in/out parameters ( usually they are files )
		java.util.ArrayList<Argument> margs = new java.util.ArrayList<Argument>();
	    String cmd = CmdGenerator.buildLocalCmd(programDoc, params, margs);
	    //TODO: record the url links to the input files (or output file) into input-files/output-files in the workflow directory
	    String inputLinks  = "";
	    String outputLinks = "";
	    for(Argument arg : margs){
	    	if (arg.getInput()){
	    		inputLinks += arg.getName()+"=>"+arg.getValue()+"\n";
	    	}
	    	if (arg.getOut()){
	    		outputLinks += arg.getName()+"=>"+arg.getValue()+"\n";
	    	}
	    }
		// map the logic input files to the physical input files
	    MapFiles(margs, localJobdir);
	    String script = ScriptGenerator.buildSwiftScript(programDoc, margs, this.templatePath);
	    // create a workflow objectand set the status to "created"
	    Workflow workflow = saveNewWorkFlow(workflowId, cmd, script, "", localJobdir, program, inputLinks, outputLinks);
		saveRunArgs(workflowId, programDoc, params);

	    return workflow;
	}
	/**
	 * create a workflow object for an application with a predefined swift script
	 * @param user
	 * @param program: application name
	 * @param programDoc
	 * @param params
	 * @return
	 */
	public Workflow createWfFromScript(User user,String program, ProgramDocument programDoc, Hashtable params)throws IOException{
		String workflowId = program+"-"+getUUID();
		// create a temp directory for this workflow execution 
		String localJobdir = getLocalJobDir(workflowId);
		createLocalJobDir(localJobdir, workflowId);
		// run the program's xml description and generate the cmd line
		// margs will be filled with in/out parameters ( usually they are files )
		java.util.ArrayList<Argument> margs = new java.util.ArrayList<Argument>();
		// the command is actually the script here
	    String cmd = CmdGenerator.buildLocalCmd(programDoc, params, margs);
	    //TODO: do we need the swift script(?)
	    String scriptPath = templatePath+"/../../swiftscript/"+program;
	    log.info("read the script from "+scriptPath);
	    File scriptdir = new File(scriptPath);
	    File jobdir = new File(localJobdir);
	    //TODO: we need to copy all the subscripts and shell scripts over to the localJobdir!!
	    try { 
	       //Chmod.copyDirectory(scriptdir, jobdir);
	    	Chmod.nativeCpDir(scriptPath, localJobdir);
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
        //map the logic input files to the physical input files
	    MapFiles(margs, localJobdir);
	    // get the argument from the cmd
	    //int beginInx = cmd.indexOf(program);
	    //String args = cmd.substring(beginInx+program.length());
	    String args = "";
	    for(Argument arg : margs){
	    	String cmdline = arg.getCmdLine();
	    	int idx = cmdline.indexOf("@");
	    	if (idx != -1){
	    		cmdline = cmdline.substring(0,idx)+arg.getValue()+" ";
	    	}
	    	//This is very important, we also should remove the extra double quote in the cmdline
	    	log.info("old cmdline is "+cmdline);
	    	cmdline = cmdline.replaceAll("\"","");
	    	args += cmdline;
	    	
	    	log.info(arg.getName()+"=>"+arg.getValue()+":"+arg.getCmdLine()+":"+cmdline);
	    }
	    // saveNewWorkflow needs to be changed
	    Workflow workflow = new Workflow();
	    workflow.setWorkflowID(workflowId);
	    workflow.setCmd(cmd);
	    workflow.setArgs(args);
	    workflow.setStatus(Workflow.Created);
		java.util.Date now = new java.util.Date();
        Timestamp mynow = new Timestamp(now.getTime());
        workflow.setCreateTime(mynow);
	    String fileName = localJobdir+"/"+program+".swift";
	    workflow.setScript(fileName);
	    //Workflow workflow = saveNewWorkFlow(workflowId, cmd, "", args, localJobdir, program, "", "");
	    //save the argument value pairs into RunArg table
		saveRunArgs(workflowId, programDoc, params);
	    return workflow;
	}
	
	public Workflow createWfFromScript(User user,String appName, String scriptName, String version, Hashtable params)throws IOException{
		// find the appscript and parse the xml file ( can we cache the parse result? )
		ProgramDocument programDoc;
		AppScript scriptObj;
	   try {
		scriptObj = appdao.getScript(appName, scriptName, version);
		String script_mobyle = scriptObj.getXmlDesc();
		File xmlFile = new File(script_mobyle);  
	    programDoc = ProgramDocument.Factory.parse(xmlFile);
	   } catch(Exception e){
		   e.printStackTrace();
		   return null;
	   }
	    String workflowId = appName+"-"+getUUID();
		// create a temp directory for this workflow execution 
		String localJobdir = getLocalJobDir(workflowId);
		createLocalJobDir(localJobdir, workflowId);
		// run the program's xml description and generate the cmd line
		// margs will be filled with in/out parameters ( usually they are files )
		java.util.ArrayList<Argument> margs = new java.util.ArrayList<Argument>();
		// the command is actually the script here
	    String cmd = CmdGenerator.buildLocalCmd(programDoc, params, margs);
	    //copy all the subscripts and shell scripts over to the localJobdir
	    String scriptPath = templatePath+"/../../swiftscript/"+appName+"/"+scriptName+"("+version+")";
	    File scriptdir = new File(scriptPath);
	    File jobdir = new File(localJobdir);
	    try { 
	       //Chmod.copyDirectory(scriptdir, jobdir);
	    	Chmod.nativeCpDir(scriptPath, localJobdir);
	    	
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
        //map the logic input files to the physical input files
	    MapFiles(margs, localJobdir);
	    // get the argument from the cmd
	    //int beginInx = cmd.indexOf(program);
	    //String args = cmd.substring(beginInx+program.length());
	    String args = "";
	    String provenance = "";
	    List workflowids = new ArrayList<String>();
	    for(Argument arg : margs){
	    	String cmdline = arg.getCmdLine();
	    	int idx = cmdline.indexOf("@");
	    	if (idx != -1){
	    		cmdline = cmdline.substring(0,idx)+arg.getValue()+" ";
	    	}
	    	//This is very important, we also should remove the extra double quote in the cmdline
	    	log.info("old cmdline is "+cmdline);
	    	cmdline = cmdline.replaceAll("\"","");
	    	args += cmdline;
	    	
	    	log.info(arg.getName()+"=>"+arg.getValue()+":"+arg.getCmdLine()+":"+cmdline);
	    	if (arg.getIsWorkflow()){ // the input is the workflow id from previous run
	    		//TODO: get the provenance info from the workflow id
	    		//and append the info to it
	    		Workflow history = wfdao.getWorkflow(user.getName(), arg.getValue());
	    		// set the provenance as a JSON Object
	    		if (history.getProvenance()!=null && history.getProvenance().length()>0)
	    		  provenance = arg.getValue()+":"+history.getProvenance();
	    		else
	    		  provenance = arg.getValue();
	    		
	    		workflowids.add(arg.getValue());
	    	}
	    }
	    //TODO: in this workflow folder, create symbolic links to the parent workflows
	    try {
	      log.info("create symolic links");
	      Chmod.createSymLinks(localJobdir,workflowids);
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	    // saveNewWorkflow needs to be changed
	    Workflow workflow = new Workflow();
	    workflow.setWorkflowID(workflowId);
	    workflow.setCmd(cmd);
	    workflow.setArgs(args);
	    workflow.setStatus(Workflow.Created);
		java.util.Date now = new java.util.Date();
        Timestamp mynow = new Timestamp(now.getTime());
        workflow.setCreateTime(mynow);
	    String mainscript = scriptObj.getMainscript();
	    if (mainscript == null){
	    	mainscript = scriptObj.getScriptName()+".swift";
	    }
	    String fileName = localJobdir+"/"+mainscript;
	    workflow.setScript(fileName);
	    workflow.setProvenance(provenance);
	    //Workflow workflow = saveNewWorkFlow(workflowId, cmd, "", args, localJobdir, program, "", "");
	    //save the argument value pairs into RunArg table
		saveRunArgs(workflowId, programDoc, params);
	    return workflow;
	    
	}
	
	public Workflow createSIDGridWorkflow(User user, int expId, String expName,
			         String program, ProgramDocument programDoc, Hashtable params) throws IOException{
		String workflowId = program+"-"+getUUID();
		String localJobdir = getLocalJobDir(workflowId);
		createLocalJobDir(localJobdir, workflowId);
		java.util.ArrayList<Argument> margs = new java.util.ArrayList<Argument>();
	    String cmd = CmdGenerator.buildLocalCmd(programDoc, params, margs);
	    //TODO: record the url links to the input files (or output file) into input-files/output-files in the workflow directory
	    String inputLinks  = "";
	    String outputLinks = "";
	    for(Argument arg : margs){
	    	if (arg.getInput()){
	    		inputLinks += arg.getName()+"=>"+arg.getValue()+"\n";
	    	}
	    	if (arg.getOut()){
	    		outputLinks += arg.getName()+"=>"+arg.getValue()+"\n";
	    	}
	    }
	    MapFiles(margs, localJobdir);
		String script = ScriptGenerator.buildSIDGridSwiftScript(expId, expName, programDoc, margs, this.templatePath);
		Workflow workflow = saveNewWorkFlow(workflowId, cmd, script, "", localJobdir, program, inputLinks, outputLinks);
		//save the argument value pairs into RunArg table
		saveRunArgs(workflowId, programDoc, params);
		return workflow;
	}
	// write a workflow package ( db object, script file, input&output links ) into disk
	private Workflow saveNewWorkFlow(String workflowId, String cmd, String script, String args, 
			                  String localJobdir, String program,
			                  String inputLinks, String outputLinks){
		Workflow workflow = new Workflow();
	    workflow.setWorkflowID(workflowId);
	    workflow.setCmd(cmd);
	    workflow.setArgs(args);
	    workflow.setStatus(Workflow.Created);
		java.util.Date now = new java.util.Date();
        Timestamp mynow = new Timestamp(now.getTime());
        workflow.setCreateTime(mynow);
	    //workflow.setScript(script);
	    log.info("create the workflow with "+cmd);
	    log.info("create the workflow with "+script);
	    // output the script to a swift script file
	    String fileName = localJobdir+"/"+program+".swift";
	    workflow.setScript(fileName);
	    try {
	      FileWriter out1 = new FileWriter(fileName);
	      out1.write(script);
	      out1.flush();
	      out1.close();
	      //Chmod.setFileMod(fileName, "0766");
	      
	    } catch(Exception e){
	    	e.printStackTrace();
	    }
	    // output the inputLinks and outputLinks to a inputLinks and a outputLinks file
	    try {
	    	  String linkFiles = localJobdir+"/inputLinks";
		      FileWriter out2 = new FileWriter(linkFiles);
		      out2.write(inputLinks);
		      out2.flush();
		      out2.close();
		      linkFiles = localJobdir+"/outputLinks";
		      FileWriter out3 = new FileWriter(linkFiles);
		      out3.write(outputLinks);
		      out3.flush();
		      out3.close();
		 } catch(Exception e){
		    	e.printStackTrace();
		 }
	    
		 return workflow;
	}
	private void saveRunArgs(String workflowId, ProgramDocument programDoc, Hashtable params){
		//TODO: do we need to sort the key list?
		org.uc.sidgrid.mobyle.ParameterDocument.Parameter[] paras = CmdGenerator.getAllParameterNameByArgpos(programDoc);
		for (int i=0; i<paras.length; i++){
			org.uc.sidgrid.mobyle.ParameterDocument.Parameter cmdPara = paras[i];
    		String paramName = cmdPara.getName();
    	  if (params.containsKey(paramName)){
    		String para_value = (String)params.get(paramName);
			//get the param's description
			String para_des = CmdGenerator.getPrompt(cmdPara.getPromptArray(0));
			RunArgument runarg = new RunArgument();
			runarg.setWorkflowId(workflowId);
			runarg.setName(paramName);
			runarg.setValue(para_value);
			runarg.setDescription(para_des);
			DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
			WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
			try {
			  wfdao.create(runarg);
			} catch (Exception e){
				e.printStackTrace();
			}
    	  }
    	}
		
	}
	// workflow directory layout "rootpath/date/workflowId"
	public  String getLocalJobDir(String workflowId){
		 // local job dir has the date prefix
		java.util.Date date = new java.util.Date();
        DateFormat dateFormat = new SimpleDateFormat ("yyyyMMdd"); 
		String today = dateFormat.format (date); 
		String local_jobdir1 = localPath+"/"+today;
		String local_jobdir = local_jobdir1 +"/"+workflowId;
		return local_jobdir;
	}
	// create a temp directory for the exec of the workflow
	public void createLocalJobDir(String local_jobdir, String workflowId){
		// copy the replaced string to the job.cmd
	   try {
		int pos = local_jobdir.indexOf(workflowId);
		String local_jobdir1 = local_jobdir.substring(0,pos-1);
		File outDir1 = new File(local_jobdir1);
		if (!outDir1.exists() ){
			boolean status = outDir1.mkdir();
			log.info("create a temp dir 1 "+status);
			//Chmod.setFileMod(local_jobdir1, "0777");
		}
		File outDir2 = new File(local_jobdir);
		if (!outDir2.exists() ){
			boolean status = outDir2.mkdir();
			log.info("create a temp dir 2"+status);
			//Chmod.setFileMod(local_jobdir, "0777");
		}
		   
		//Chmod.createDirectory(local_jobdir);
		//Chmod.setFileMod(local_jobdir, "0766");
	   } catch (Exception e){
		   e.printStackTrace();
	   }
	}
	/**
	 * update the file location in margs from url links to the local file paths
	 * 
	 */
	private void MapFiles(java.util.ArrayList<Argument> margs, String localJobdir)throws IOException{
         //		 map the logic input files to the physical input files
	    for(Argument arg : margs){
	    	
	    	if( arg.getIsFile() == false )
	    		continue;
	    	
	    	String argV = arg.getValue();
	    	if (arg.getInput()){
	    		// assume the input is a restful link ?
	    		String dataFile = locatePhysicalFile(argV);
	    		File file = new File(dataFile);
				if (!file.exists()){
					log.error("can't find the data file "+dataFile);
					//TODO: need to think about throw Exception or return error code
					throw new IOException();
					
				}
	    		//TODO: so far we only assume that users only choose individual files
	    		//Later we need to support directory-mapping
	    		//it seems that it is not easy to create a link to the data file and use the link in JDK1.5
	    		//java.io.file is notoriously bad at handling symbolic links
	    		String name = getFileNameFromURL(argV);
	    		/** try {
	    			int exitCode = Chmod.createFileLink(dataFile, localJobdir+"/"+name);
	    			if (exitCode == 0)
	    				arg.setValue(name);
	    			else
	    				arg.setValue(dataFile);
	    		} catch (Exception e){
	    			System.out.println("can't create the symbolic links to the input files");
	    			arg.setValue(dataFile);
	    		} **/
	    		arg.setValue(dataFile);
	    		log.info("find the input arg "+arg+":"+dataFile);
	    	}
	    	if (arg.getOut()){
	    		// assume the output is just a contant value from the hidden parameter in the application gadget
	    		// normally it is the same as the name of the argument
	    	    arg.setValue(localJobdir+"/"+argV);
	    		//arg.setValue(argV);
	    	    
	    	}
	    }
	}
	//TODO: 
	private String getFileNameFromURL(String urlstr){
	  try {
		URL url = new URL(urlstr);
		String path = url.getPath();
		String [] tmp = path.split("/");
		String fileName = tmp[tmp.length-1];
		return fileName;
	  } catch (java.net.MalformedURLException e){
		    String [] tmp = urlstr.split("/");
			String filename = tmp[tmp.length-1];
			return filename;
	  }
	}
	public String locatePhysicalFile(String urlstr){
		try {
			URL url = new URL(urlstr);
			String path = url.getPath();
			String [] tmp = path.split("/");
			String fileName = tmp[tmp.length-1];
			int port = url.getPort();
			String expId = tmp[tmp.length-2];
			log.info("locate the file path "+expId+"/"+fileName);
			String real_file = "";
			String protocol = url.getProtocol();
			if ( protocol.equalsIgnoreCase("http") && (port == 80 || port == -1)){ // this is a data file from sidgrid repository
				// find the experiment id
				real_file = sidgridDataPath+"/"+expId+"/"+fileName;
			} else if (protocol.equalsIgnoreCase("http")){ // this is a temp input file uploaded by user. Normally it should be 8080
				real_file = uploadDataPath+"/"+expId+"/"+fileName;
			} else
				real_file = url.getFile();
			
			return real_file;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static synchronized String getUUID() {
		long l;
		// we want seconds and milliseconds from this one
		// that's 16 bits
		while (true) {
			l = System.currentTimeMillis() % (60*1000); 
			if (l != lastTime) {
				lastTime = l;
				break;
			}
			else {
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
				}
			}
		}
		// and for the msbs, some random stuff
		int rnd;
		try {
			SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
			rnd = prng.nextInt();
		}
		catch (NoSuchAlgorithmException e) {
			rnd = (int) (Math.random() * 0xffffff);
		}
		//and 24 bits
		rnd &= 0x00ffffff;
		l += ((long) rnd) << 16;
		return UID_DATE_FORMAT.format(new Date()) + '-' + alphanum(l);
	}
	public static final String codes = "0123456789abcdefghijklmnopqrstuvxyz";
	protected static String alphanum(long val) {
		StringBuffer sb = new StringBuffer();
		int base = codes.length();
		for (int i = 0; i < 8; i++) {
			int c = (int) (val % base);
			sb.append(codes.charAt(c));
			val = val / base;
		}
		return sb.toString();
	}
}
