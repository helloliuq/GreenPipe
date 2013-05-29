package org.uc.sidgrid.dao;

import org.uc.sidgrid.data.Experiment;

public class ExperimentDAO extends GenericDAO{
	public Experiment getExpById(long expid){
	     String queryStr = "from Experiment where id='"+expid+"'";
	     try {
	    	   Experiment result = (Experiment)this.restore(queryStr);
			   return result;
		 } catch (Exception e){
		    	  e.printStackTrace();
		    	 return null;
		 }
  }
}
