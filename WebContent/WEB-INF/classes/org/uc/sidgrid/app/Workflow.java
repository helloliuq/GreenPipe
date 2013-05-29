package org.uc.sidgrid.app;

import java.sql.Timestamp;
import java.util.List;
import org.uc.sidgrid.data.Experiment;

//TODO: do we need to associate the workflow with SIDGrid experiments?
public class Workflow {
	String workflowId; // set by swift workflow engine
	String workflowCmd; // cmd for running this workflow
	String swiftScript; // swift workflow script
	String swiftArgs;   // parameters for initialize the script
	List<String> inputs; // input file lists
	List<String> outputs; // output file lists
	String status;
	 //timestamp for (created, queue, running, finished )
    private Timestamp   createTime;
    private Timestamp   finishTime;
    
    // foreign keys to <user, experiment>
    private User user;
	private Experiment exp;
	// in other portals, there is no concerpt of experiment
	// so we need to add provance between workflows;
	private String provenance; 
	
	private int totalJobs;
	private int activeJobs;
	private int queuedJobs;
	private int finishedJobs;
	

	public static final String Created   = "Created";
	public static final String Submitted = "Submitted";
	public static final String Active    = "Active";
	public static final String Failed    = "Failed";
	public static final String Completed = "Completed";
	public static final String RequestCancel = "RequestCancel";
	public static final String Cancelled = "Cancelled";
	public static final String ERROR     = "ERROR";
	
	
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
	public void setArgs(String args){
		this.swiftArgs = args;
	}
	public String getArgs(){
		return this.swiftArgs;
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
	public void setStatus(String status){
		this.status = status;
	}
	public String getStatus(){
		return this.status;
	}
	public void setCreateTime(Timestamp createTime){
	    	this.createTime = createTime;
	    }
	    public Timestamp getCreateTime(){
	    	return this.createTime;
	}
    public void setFinishTime(Timestamp finishTime){
    	this.finishTime = finishTime;
    }
    public Timestamp getFinishTime(){
    	return this.finishTime;
    }
    public void setUser(User user){
    	this.user = user;
    }
    public User getUser(){
    	return this.user;
    }
    public void setExp(Experiment exp){
    	this.exp = exp;
    }
    public Experiment getExp(){
    	return this.exp;
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
