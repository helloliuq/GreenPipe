package org.uc.sidgrid.services;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.*;

public class Jobstate {
//  public static void main(String[] argv) {
 public String[][] getJobstate()
{  
System.out.println("Checking if Driver is registered with DriverManager.");
  
  try {
    Class.forName("org.postgresql.Driver");
  } catch (ClassNotFoundException cnfe) {
    System.out.println("Couldn't find the driver!");
    System.out.println("Let's print a stack trace, and exit.");
    cnfe.printStackTrace();
    System.exit(1);
  }
  
  System.out.println("Registered the driver ok, so let's make a connection.");
  //String[][] value= new String[10][]; 
  Connection c = null;
  String sql= null;
   PreparedStatement st=null;
ResultSet rs=null;
String value[][]=null;
  try {
    // The second and third arguments are the username and password,
    // respectively. They should be whatever is necessary to connect
    // to the database.
    c = DriverManager.getConnection("jdbc:postgresql://sidgrid.ci.uchicago.edu/s_vdc",
                                    "kavithaa", "sid09");
    if(c==null){
       value[0][0]=  "empty list";
      }
     //sql= "select  workflow from wf_work where vogroup=? order by mtime limit 10";
     sql="select w.workflow,j.wfid,j.jobid,j.state,j.mtime,j.site from wf_jobstate j,wf_work w where w.id=j.wfid and w.vogroup=? order by j.mtime desc  limit 20";
     st = c.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
     String user="kavithaa";
     st.setString(1,user);
     // Turn use of the cursor on.
     st.setFetchSize(20);
     //ResultSet rs = st.executeQuery("select * from wf_jobstate where wfid in(select id from wf_work where vogroup='%s' order by ctime desc) order by mtime desc");
    /* } catch (SQLException se) {
    System.out.println("Couldn't connect: print out a stack trace and exit.");    se.printStackTrace();    System.exit(1);  }
     */ rs= st.executeQuery();
     rs.last();
     int rows= rs.getRow();
     rs.first();
     int i=0;
      value = new String[rows][rs.getMetaData().getColumnCount()];
     while (rs.next()) {
//       System.out.print(rs.getString(1)+" "+rs.getInt(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getTimestamp(5)+" "+rs.getString(6)+'\n');
       value[i][0]=rs.getString(1); 
       value[i][1]=String.valueOf(rs.getInt(2));
       value[i][2]=rs.getString(3);
       value[i][3]=rs.getString(4);
       value[i][4]=rs.getTimestamp(5).toString(); 
       value[i][5]=rs.getString(6); 
       i++;    
// System.out.println(rs.getString(1)+'\n');
    }
    rs.close();
   

  } catch (SQLException se) {
    System.out.println("Couldn't connect: print out a stack trace and exit.");
    se.printStackTrace();
    System.exit(1);
  }
    return value;
  }
}
