package org.uc.sidgrid.dao;

import java.sql.*;
import java.security.*;
import java.math.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uc.sidgrid.services.WorkflowService;
// This class can be split to two. one for returning mediapath and another for authentication
public class sidgridDAO{
	private static Log log = LogFactory.getLog(sidgridDAO.class);
	
	private String header= "https://sidgrid.ci.uchicago.edu/sidgrid/experiment_files/";
	private Connection conn;
	String location="no value";;
    String user=null; 
    PreparedStatement st_id=null,st_cache=null;
    ResultSet rs=null,rs_cache=null;
    
	public void createDBConnection (ServletContext servletContext){
			try{
		        String url = servletContext.getInitParameter("databaseUrl");
		        String password = servletContext.getInitParameter("databasePassword");
		        String dbuserName = servletContext.getInitParameter("databaseUser");
                
				System.out.println("db url is"+url);
			    Class.forName("com.mysql.jdbc.Driver").newInstance();
			    conn = DriverManager.getConnection (url, dbuserName, password);
			    System.out.println("Database connection established");
			} catch (SQLException e) {
				System.out.println("Error connecting to DB: "+e);
			} catch (Exception e1) {
				System.out.println("Exception thrown:" + e1.getMessage());
			}
		}
	  private void createDBConnection(){
		  //make sure DBconnection is created
	  }
	  private void closeDBConnection(){
			if (conn != null)
			    {
				try
				    {   
					conn.close ();
					System.out.println("Database connection terminated");
				    }
				catch (Exception e) {   
					System.out.println("ERROR with Database connection termination"+e.getMessage());
				}
			    }
				
		    } 
	  public String getMediaPath(HttpSession session, String expname){
		      //TODO:ASK FOR  type like eaf or chat  from user and append the proper extension
			  createDBConnection();
                          System.out.println("session:"+session.getAttributeNames());
                          System.out.println("session:"+session.getId());
                          // Can use Httprequest object if desired 
                           user=session.getAttribute("username").toString();
                       //User userobj=(User) session.getAttribute("User");
                        //System.out.println("user  obj:"+userobj);
                        //user = userobj.getUserName();
                        //user=request.getRemoteUser();
                          System.out.println("user is:"+user); 
		          try{
			   st_id=conn.prepareStatement("SELECT ID from EXPERIMENT where STATUS=0 and NAME=? and ID in(select distinct(EXPERIMENT_ID)  from EXPERIMENT_GROUPS,USER_GROUPS where USER_GROUPS.USER=? and EXPERIMENT_GROUPS.GROUP_ID=USER_GROUPS.GROUP_ID)",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			   st_cache=conn.prepareStatement("SELECT CACHE_ID FROM MEDIA_CACHE C ,MEDIA_DESC D WHERE D.MEDIA_ID=C.MEDIA_ID AND C.PLATFORM ='F'  AND D.EXPERIMENT_ID=?");
			  st_id.setString(1,expname+".eaf");
			  st_id.setString(2,user);
			   rs=st_id.executeQuery();
			  rs.last();
			  int rowCount = rs.getRow();
                          System.out.println("row:"+rowCount);
			  rs.first();
			  if(rowCount==0)
			{   System.out.println("Experiment unavailable for "+user); 
				return " Experiment unavailable";  
			}
			  else{
				int expid=  rs.getInt(1); 
				System.out.println("expid:"+expid);
                                 st_cache.setInt(1,expid);
				 rs_cache=st_cache.executeQuery();
				//rs_cache.last();
				//int rowCount = rs_cache.getRow();
				rs_cache.first();
				int cache_id=rs_cache.getInt(1);
				System.out.println("cache_id:"+cache_id);
			        location=header+expid+"/transcoded/"+cache_id+".flv";
		                
                		//location="http://sidgrid.ci.uchicago.edu:8080/webdav/31010.flv";
				//return location;
			  }  
                          }catch(Exception e){
                                System.out.println("Exception:"+e);
                           }finally{
                             closeDBConnection();
}
                                  return location;	 
 }

public static String  md5(String pass)
{
	MessageDigest m=null;
	try{
		m=MessageDigest.getInstance("MD5");
	}catch(Exception r){
		/// System.out.println("exp:"+r);
		r.printStackTrace();
	}
		m.update(pass.getBytes(),0,pass.length());
		return (new BigInteger(1,m.digest()).toString(16));
}
public boolean authenticateUser(String loginname,String loginpass)
{
	
	loginpass= md5(loginpass);
	
	createDBConnection();
	boolean status=false;
	try{
		String sql=" SELECT * FROM USER WHERE NAME=? AND PASSWORD=?";
		PreparedStatement st_auth=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		st_auth.setString(1,loginname);
		st_auth.setString(2,loginpass);
		ResultSet rset=st_auth.executeQuery();
        rset.last();
        int rowCount = rset.getRow();
        System.out.println("row:"+rowCount+loginname+":"+loginpass);
        rset.first();
        if(rowCount==1){
              System.out.println(loginname+" authentication success");
              status=true;
        } else {
              System.out.println(loginname+" authentication failure");
              status=false;
        }
	}catch(Exception r){  System.out.println("exception"+r);
	}
	finally{
		closeDBConnection();

	}
	return status;

	}
}
