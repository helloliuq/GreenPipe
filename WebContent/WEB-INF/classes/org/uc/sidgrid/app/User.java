package org.uc.sidgrid.app;

import java.util.HashSet;
import java.util.Set;
import org.uc.sidgrid.social.Group;
public class User{
	private String name; //user name
	private String passwd; // password
    private String role;
    
	private Set<Group> groups = new HashSet<Group>(0);
	
	public final static String Administrator = "admin";
	public final static String Anonymous = "anonymous";
	
	public  String getAttribute(String name)
	{
    	return this.name;
	}

	public String getName(){

		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getPasswd(){
		return this.passwd;
	}
	public void setPasswd(String passwd){
		this.passwd = passwd;
	}
	public void setRole(String role){
		this.role = role;
	}
	public String getRole(){
		return this.role;
	}
	public Set<Group> getGroups(){
		return this.groups;
	}
	public void setGroups(Set<Group> groups){
		this.groups = groups;
	}
}
