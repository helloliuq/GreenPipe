package org.uc.sidgrid.test;

import java.util.*;
import org.uc.sidgrid.app.*;
import org.uc.sidgrid.social.*;
import org.uc.sidgrid.dao.GenericDAO;
import net.sf.hibernate.*;
import net.sf.hibernate.cfg.Configuration;

public class TestUserGroups {
	
  public static void main(String[] args) throws Exception{
	  GenericDAO dao = new GenericDAO();
	  // test 1: search group by user
	  String queryStr = "from User where name='wwjag'";
	  List<User> result = dao.restoreList(queryStr);
	  for(User user : result){
		  Set<Group> grps = user.getGroups();
		  for (Group tmp : grps){
			  System.out.println("the user group is "+tmp.getName());
		  }
	  }
	  // test 2: search user by group
	  String queryStr2 = "from Group where name='wwjag'";
	  Group grp = (Group)dao.restore(queryStr2);
	  Set<User> users = grp.getUsers();
	  for (User tmp : users){
			  System.out.println("the group has "+tmp.getName());
	  }
	  // test 3: find all the friends of the user
	  
  }
}
