package org.uc.sidgrid.social;

import java.util.HashSet;
import java.util.Set;
import org.uc.sidgrid.app.User;
import org.uc.sidgrid.data.Experiment;

public class Group {
  private long id;
  private String name;
  private String owner;
  private Set<User> users = new HashSet<User>(0);
  private Set<Experiment> exps = new HashSet<Experiment>(0);
  
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
  public void setUsers(Set<User> users){
	  this.users = users;
  }
  public Set<User> getUsers(){
	  return this.users;
  }
  public void setExps(Set<Experiment> exps){
	  this.exps = exps;
  }
  public Set<Experiment> getExps(){
	  return this.exps;
  }
}
