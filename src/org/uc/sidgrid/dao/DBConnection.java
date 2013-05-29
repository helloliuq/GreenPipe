package org.uc.sidgrid.dao;


import java.util.logging.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnection {
	//private Logger logger = Logger.getLogger (this.getClass ().getName ());
	private java.util.logging.Logger logger = java.util.logging.Logger.getLogger (this.
			 getClass ().getName ());

	// private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger (DBConnection.class);

	private Connection connection = null;
	private static DataSource pool;

	public DBConnection (String url, String username, String password) throws DataStoreException {
		logger.info ("DBConnection ... ");

		String driver = "";		
		String connectString = "";

		driver = "com.mysql.jdbc.Driver";
		//driver = "org.hsqldb.jdbcDriver";
		
		logger.info ("driver   :"+driver);
		logger.info ("url      :"+url);
		logger.info ("username :"+username);
		logger.info ("password :"+password);
        try {
		  Class.forName (driver);
		  //this.connection = DriverManager.getConnection (url, username, password);
		  /** autoReconnect must be added here to prevent the broken link after 8 hours idle period **/
		  this.connection = DriverManager.getConnection (url+"?"+"user="+
					 username+"&password="+password+"&autoReconnect=true");
        } catch (Exception e){
        	throw new DataStoreException (e.getMessage ());
        }
		logger.info ("connection success");
	}
	
	// create a DB connection from tomcat connection pool
	public DBConnection() {
        Context env = null;
         try {
             env = (Context) new InitialContext().lookup("java:comp/env");
             pool = (DataSource)env.lookup("jdbc/DBPool");
             if(pool==null) 
                 System.err.println("'DBPool' is an unknown DataSource");
              } catch(NamingException ne) {
                 ne.printStackTrace();
         }
     }
	public void closeConnection () {
		try {
			if (this.connection!=null) {
				this.connection.close ();
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}


	public Connection getConnection () {
		return connection;
	}
}
