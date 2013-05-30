package org.uc.sidgrid.dao;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.uc.sidgrid.app.User;
import org.uc.sidgrid.social.Group;
import org.uc.sidgrid.data.Experiment;

public class GroupDAO extends GenericDAO{
   public Group getGroupByName(String name){
	     String queryStr = "from Group where name='"+name+"'";
	     try {
			   Group result = (Group)this.restore(queryStr);
			   return result;
		 } catch (Exception e){
		    	  e.printStackTrace();
		    	 return null;
		 }
   }
   public List<Group> getGroupByUser(String username){
	      String queryStr = "from User where name='"+username+"'";
	      try {
		   List<User> result = this.restoreList(queryStr);
		   User user = result.get(0);
		   Set<Group> grps = user.getGroups();
		   List<Group> res = new ArrayList<Group>(grps);
		   return res;
	      } catch (Exception e){
	    	  e.printStackTrace();
	    	  List<Group> res = new ArrayList<Group>(0);
	    	  return res;
	      }
		  
   }
 
}
