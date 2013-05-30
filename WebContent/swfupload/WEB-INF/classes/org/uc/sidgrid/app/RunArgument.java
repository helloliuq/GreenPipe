package org.uc.sidgrid.app;

public class RunArgument {
	int id;
	String workflowId; // set by swift workflow engine
	String argname;
	String argdes;
	String argvalue;
	
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	public void setWorkflowId(String workflowId){
		this.workflowId = workflowId;
	}
	public String getWorkflowId(){
		return this.workflowId;
	}
	public void setName(String name){
		this.argname = name;
	}
	public String getName(){
		return this.argname;
	}
	public void setDescription(String des){
		this.argdes = des;
	}
	public String getDescription(){
		return this.argdes;
	}
	public void setValue(String value){
		this.argvalue = value;
	}
	public String getValue(){
		return this.argvalue;
	}
}
