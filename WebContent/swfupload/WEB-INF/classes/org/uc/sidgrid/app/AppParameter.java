package org.uc.sidgrid.app;

public class AppParameter {
  private String name;
  private String prompt;
  private String dataType; // <File, String, Integer, Float, Choice, MultipleChoice >
  private int  argumentType; // <Plain, Switch,Long>
  private String value;
  
  boolean isinput;
  boolean isout;
  boolean isloop;
  boolean isstdout;
  
  public AppParameter(){
	  this.dataType = "String";
  }
  public AppParameter(org.uc.sidgrid.mobyle.ParameterDocument.Parameter param){
	  this.name = param.getName();
	  //this.prompt = param.
	  this.dataType = param.getType().toString();
	  //this.argumentType = 
	  //TODO: ....
  }
  public void setName(String name){
	  this.name = name;
  }
  public String getName(){
	  return this.name;
  }
  public void setPrompt(String prompt){
	  this.prompt = prompt;
  }
  public String getPrompt(){
	  return this.prompt;
  }
  public void setValue(String value){
	  this.value = value;
  }
  public String getValue(){
	  return this.value;
  }
  public void setDataType(String dataType){
	  this.dataType = dataType;
  }
  public String getDataType(){
	  return this.dataType;
  }
  public void setArgType(int type){
	  this.argumentType = type;
  }
  public int getArgType(){
	  return this.argumentType;
  }
  public void setInput(boolean in){
	  this.isinput = in;
  }
  public boolean getInput(){
	  return this.isinput;
  }
  public void setOut(boolean out){
	  this.isout = out;
  }
  public boolean getOut(){
	  return this.isout;
  }
  public void setIsloop(boolean loop){
	  this.isloop = loop;
  }
  public boolean getIsloop(){
	  return this.isloop;
  }
  public void setIsStdout(boolean stdout){
	  this.isstdout = stdout;
  }
  public boolean getIsStdout(){
	  return this.isstdout;
  }
  
}
