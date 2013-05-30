package org.uc.sidgrid.app;

import java.sql.Timestamp;
import java.util.List;


public class WorkflowJson {
	String workflowId; // set by swift workflow engine
	String workflowCmd; // cmd for running this workflow
	String swiftScript; // swift workflow script
	String status;
    String  createTime;
    String  finishTime;
    String wfCreator;
    
    String provenance;
    
	private int totalJobs;
	private int activeJobs;
	private int queuedJobs;
	private int finishedJobs;
    
    List<String> inputs; // input file lists
	List<String> outputs; // output file lists
    
	public WorkflowJson(Workflow wf){
		this.workflowId = wf.getWorkflowID();
		this.workflowCmd = wf.getCmd();
		this.swiftScript = wf.getScript();
		this.status = wf.getStatus();
		if (wf.getCreateTime() != null )
			this.createTime = wf.getCreateTime().toString();
		else
			this.createTime = "";
		if (wf.getFinishTime() != null)
			this.finishTime = wf.getFinishTime().toString();
		else
			this.finishTime = "";
		this.inputs = wf.getInputs();
		this.outputs = wf.getOutputs();
		this.wfCreator = wf.getUser().getName();
		this.provenance = wf.getProvenance();
		this.totalJobs = wf.getTotalJobs();
		this.activeJobs = wf.getActiveJobs();
		this.queuedJobs = wf.getQueuedJobs();
		this.finishedJobs = wf.getFinishedJobs();
	}
    public void setWorkflowID(String id){
		this.workflowId = id;
	}
	public String getWorkflowID(){
		return this.workflowId;
	}
	public void setCmd(String cmd){
		this.workflowCmd = cmd;
	}
	public String getCmd(){
		return this.workflowCmd;
	}
	public void setScript(String script){
		this.swiftScript = script;
	}
	public String getScript(){
		return this.swiftScript;
	}
	public void setInputs(List<String> inputs){
		this.inputs = inputs;
	}
	public List<String> getInputs(){
		return this.inputs;
	}
	public void setOutputs(List<String> outputs){
		this.outputs = outputs;
	}
	public List<String> getOutputs(){
		return this.outputs;
	}
	public void setCreateTime(Timestamp createTime){
    	this.createTime = createTime.toString();
    }
    public String getCreateTime(){
    	return this.createTime;
    }
    public void setFinishTime(Timestamp finishTime){
    	this.finishTime = finishTime.toString();
    }
    public String getFinishTime(){
    	return this.finishTime;
    }
    public void setStatus(String status){
		this.status = status;
	}
	public String getStatus(){
		return this.status;
	}
	public void setCreator(String creator){
		this.wfCreator = creator;
	}
	public String getCreator(){
		return this.wfCreator;
	}
	public void setProvenance(String prov){
	   	this.provenance = prov;
	}
	public String getProvenance(){
	   	return this.provenance;
	}
    public void setTotalJobs(int jobs){
    	this.totalJobs = jobs;
    }
    public int getTotalJobs(){
    	return this.totalJobs;
    }
    public void setActiveJobs(int jobs){
    	this.activeJobs = jobs;
    }
    public int getActiveJobs(){
    	return this.activeJobs;
    }
    public void setQueuedJobs(int jobs){
    	this.queuedJobs = jobs;
    }
    public int getQueuedJobs(){
    	return this.queuedJobs;
    }
    public void setFinishedJobs(int jobs){
    	this.finishedJobs = jobs;
    }
    public int getFinishedJobs(){
    	return this.finishedJobs;
    }

}
