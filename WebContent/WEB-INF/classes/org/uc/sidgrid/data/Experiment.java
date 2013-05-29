package org.uc.sidgrid.data;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import org.uc.sidgrid.social.Group;

public class Experiment {
  private long id;
  private String name;
  private Timestamp tstamp;
  private String description;
  
  private Set<Group> groups = new HashSet<Group>(0);
  
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
  public void setTstamp(Timestamp tstamp){
	  this.tstamp = tstamp;
  }
  public Timestamp getTstamp(){
	  return this.tstamp;
  }
  public void setDescription(String description){
	  this.description = description;
  }
  public String getDescription(){
	  return this.description;
  }
  public Set<Group> getGroups(){
		return this.groups;
  }
  public void setGroups(Set<Group> groups){
		this.groups = groups;
  }
  
}
