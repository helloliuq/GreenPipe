package org.uc.sidgrid.dao;

import java.util.List;

import org.uc.sidgrid.app.User;

public class UserDAO extends GenericDAO{
	public User getUserByName(String name) throws Exception{
		 String queryStr = "from User where Name='"+name+"'";
	     //List result = getSession().createQuery(queryStr).list();
		 List result = restoreList(queryStr);
	     if (result.size() < 1)
	    	 return null;
	     else
	    	 return (User)result.get(0);
	}
	
	public boolean authenticateUser(String loginname,String loginpass)
	{
	  try {	
		loginpass= sidgridDAO.md5(loginpass);
		String queryStr = "from User where Name='"+loginname+"'";
		List result = restoreList(queryStr);
	     if (result.size() < 1)
	    	 return false;
	     else{
	    	 User tmp = (User)result.get(0);
	    	 if (loginpass.equals(tmp.getPasswd()))
	    		 return true;
	    	 else
	    		 return false;
	     }
	  } catch (Exception e){
		  e.printStackTrace();
		  return false;
	  }
	    	 
	}
}
