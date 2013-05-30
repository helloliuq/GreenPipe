package org.uc.sidgrid.services;

/**
 * the class retrieve the mediaURL from the project name
 * @author wenjun wu
 *
 */
public class Path{
	
	public String getMediaPath(String val){
      if(val.equals("DemoProject")){
        System.out.println("match found");        
		return "http://sidgrid.ci.chicago.edu:8080/webdav/31010.flv";
      }else{
        System.out.println("match not found");     
        return val;
	  }
	}
	
}
