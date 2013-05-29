package org.uc.sidgrid.social;

import java.util.HashSet;
import java.util.Set;

import org.uc.sidgrid.app.User;
import org.uc.sidgrid.data.Experiment;

/**
 * create a mirror class for group to avoid the circular-reference exception in the process
 * of json serialization
 * @author wenjun wu
 *
 */
public class GroupJson {
	  private long id;
	  private String name;
	  private String owner;
	  
	  public GroupJson(Group group){
		  this.id = group.getId();
		  this.name = group.getName();
		  this.owner = group.getOwner();
	  }
	  
	  public void setId(long id){
		  this.id = id;
	  }
	  public long getId(){
		  return this.id;
	  }
	  public void setName(String name){
		  this.name = name;
	  }
	  public String getName(){
		  return this.name;
	  }
	  public void setOwner(String owner){
		  this.owner = owner;
	  }
	  public String getOwner(){
		  return this.owner;
	  }

}
