package org.uc.sidgrid.mobyle.core;

public class Argument {
  String type;
  String name;
  String value;
  String cmdline;
  boolean isinput;
  boolean isout;
  boolean isloop;
  boolean isstdout;
  
  public Argument(String type, String name, String value){
	  this.type = type;
	  this.name = name;
	  this.value = value;
	  this.isinput = false;
	  this.isout = false;
	  this.isloop = false;
  }
  public void setType(String type){
	  this.type = type;
  }
  public String getType(){
	  return type;
  }
  public void setName(String name){
	  this.name = name;
  }
  public String getName(){
	  return this.name;
  }
  public void setValue(String value){
	  this.value = value;
  }
  public String getValue(){
	  return this.value;
  }
  public void setCmdLine(String cmdline){
	  this.cmdline = cmdline;
  }
  public String getCmdLine(){
	  return this.cmdline;
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
  //hack: for streamtemplate
  public boolean getIsBoolean(){
	  return this.type.equalsIgnoreCase("Boolean");
  }
  public boolean getIsText(){
	  boolean res = this.type.equalsIgnoreCase("Text") || this.type.equalsIgnoreCase("String") || this.type.equalsIgnoreCase("Integer");
	  return res;
  }
  public boolean getIsChoice(){
	  //System.out.println("type is choice");
	  return this.type.equalsIgnoreCase("Choice");
  }
  public boolean getIsFile(){
	  return this.type.equalsIgnoreCase("File");
  }
  public boolean getIsMChoice(){
	  return this.type.equalsIgnoreCase("MultipleChoice");
  }
  public boolean getIsWorkflow(){
	  return this.type.equalsIgnoreCase("Workflow");
  }
}
