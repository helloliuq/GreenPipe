package org.uc.sidgrid.swift;

import java.io.*;

public class WorkflowMgrThread implements Runnable{
	String wfscript;
	String logfile;
	boolean stopFlag;
	public WorkflowMgrThread(String wfscript, String logfile){
		this.wfscript = wfscript;
		this.logfile = logfile;
		this.stopFlag = false;
	}
	public void stop(){
		stopFlag = true;
	}
	public void run(){
		System.out.println("start the workflow manager");
		ProcessBuilder processBuilder = new ProcessBuilder(this.wfscript);
		processBuilder.redirectErrorStream(true);
		File tmp = new File(wfscript);
		File workdir = tmp.getParentFile();
		if (workdir.isDirectory())
			processBuilder.directory(workdir);
	    try {
          Process process = processBuilder.start();
          InputStream is = process.getInputStream();
          InputStreamReader isr = new InputStreamReader(is);
          BufferedReader br = new BufferedReader(isr);
          String line;
          while ((line = br.readLine()) != null && !stopFlag) {
            System.out.println(line);
          }
          if (stopFlag){
        	  process.destroy();
        	  //TODO: need to destroy the child process create by the script
        	  //have to do it like this: kill -9 `cat /disks/space0/apache-tomcat/webapps/SIDGridPortal/workflow/daemon/mydaemon.pid`

          }
          System.out.println("Program terminated!");

	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	}
}
