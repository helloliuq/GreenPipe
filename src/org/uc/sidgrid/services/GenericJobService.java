package org.uc.sidgrid.services;

import java.util.Hashtable;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GenericJobService {
	private static Log log = LogFactory.getLog(GenericJobService.class);
	
	public  String run(String ws_services_key, String program, Hashtable params){
		try {
	  	   	   String endpoint = "http://localhost:8080/axis/services/Generic";
	  	   	   //String endpoint = "http://lsgw.uc.teragrid.org:8080/axis/services/JobService";
	  	   	   Service  service = new Service();
	  	   	   Call     call    = (Call) service.createCall();
	  	   	   call.setTargetEndpointAddress( new java.net.URL(endpoint) );
	  	   	   call.setOperationName(new QName("", "run"));
	  	   	   
	  	   	   call.addParameter( "in0", XMLType.SOAP_STRING, ParameterMode.IN);
	  	   	   call.addParameter( "in1", XMLType.SOAP_STRING, ParameterMode.IN);
	  	   	   call.addParameter( "in2", XMLType.SOAP_MAP, ParameterMode.IN);

	  	   	   call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING );	  	   	  
	  		   String jobid = (String)call.invoke( new Object[] { ws_services_key,  program, params} );
	  		   System.out.println("job id is "+jobid);
	  	   	   return jobid;
	  	      } catch (Exception e){
	  	   	   e.printStackTrace();
	  	      }
	  	   return "unknown";
	}
	public String run(String ws_services_key, String site, String program, Hashtable params){
		try {
	  	   	   String endpoint = "http://localhost:8080/axis/services/Generic";
	  	   	   //String endpoint = "http://lsgw.uc.teragrid.org:8080/axis/services/JobService";
	  	   	   Service  service = new Service();
	  	   	   Call     call    = (Call) service.createCall();
	  	   	   call.setTargetEndpointAddress( new java.net.URL(endpoint) );
	  	   	   call.setOperationName(new QName("", "run"));
	  	   	   
	  	   	   call.addParameter( "in0", XMLType.SOAP_STRING, ParameterMode.IN);
	  	   	   call.addParameter( "in1", XMLType.SOAP_STRING, ParameterMode.IN);
	  	   	   call.addParameter( "in2", XMLType.SOAP_STRING, ParameterMode.IN);
	  	   	   call.addParameter( "in3", XMLType.SOAP_MAP, ParameterMode.IN);

	  	   	   call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING );	  	   	  
	  		   String jobid = (String)call.invoke( new Object[] { ws_services_key,  site, program, params} );
	  		   System.out.println("job id is "+jobid);
	  	   	   return jobid;
	  	      } catch (Exception e){
	  	   	   e.printStackTrace();
	  	      }
	  	   return "unknown";
	}
	public  String run(String ws_services_key, String site, String program, String input, int maxTime){
		try {
	  	   	   String endpoint = "http://localhost:8080/axis/services/Generic";
	  	   	   //String endpoint = "http://lsgw.uc.teragrid.org:8080/axis/services/JobService";
	  	   	   Service  service = new Service();
	  	   	   Call     call    = (Call) service.createCall();
	  	   	   call.setTargetEndpointAddress( new java.net.URL(endpoint) );
	  	   	   call.setOperationName(new QName("", "runMaxTime"));
	  	   	   
	  	   	   call.addParameter( "in0", XMLType.SOAP_STRING, ParameterMode.IN);
	  	   	   call.addParameter( "in1", XMLType.SOAP_STRING, ParameterMode.IN);
	  	   	   call.addParameter( "in2", XMLType.SOAP_STRING, ParameterMode.IN);
	  	   	   call.addParameter( "in3", XMLType.SOAP_STRING, ParameterMode.IN);
	  	   	   call.addParameter( "in4", XMLType.SOAP_INTEGER, ParameterMode.IN);

	  	   	   call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING );	  	   	  
	  		   String jobid = (String)call.invoke( new Object[] { ws_services_key,  site, program, input, new Integer(maxTime)} );
	  		   System.out.println("job id is "+jobid);
	  	   	   return jobid;
	  	      } catch (Exception e){
	  	   	   e.printStackTrace();
	  	      }
	  	   return "unknown";
	}
	 // get the result of the job
    public static String getResult(String ws_services_key, String jobid){
   	    String res = getAttribute(ws_services_key, jobid, "getResults");
   	    if (res == null)
   	    	return("the result file is empty");
   	    else
   	    	return res;
    }
    //  get the status of the job
    public String getCheckStatus(String ws_services_key, String jobid){
  	  try {
  	   	   String endpoint = "http://localhost:8080/axis/services/Generic";
  	   	   Service  service = new Service();
  	   	   Call     call    = (Call) service.createCall();
  	   	   call.setTargetEndpointAddress( new java.net.URL(endpoint) );
  	   	   call.setOperationName(new QName("", "checkStatus"));
  	   	   call.addParameter( "in0", XMLType.SOAP_STRING, ParameterMode.IN);
  	   	   call.addParameter( "jobid", XMLType.SOAP_STRING, ParameterMode.IN);
  	   	   call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING );
  	   	   String ret = (String)call.invoke( new Object[] { ws_services_key, jobid } );
  	   	   return ret;
  	      } catch (Exception e){
  	   	   e.printStackTrace();
  	      }
  	    return null;
    }
    // get the input of the job
    //TODO: have to extend the function if there are multiple inputs
    public static String getInput(String ws_services_key, String jobId) throws AxisFault{
    	return getAttribute(ws_services_key, jobId, "getInput");
    }
    // get the script of the job
    public static String getCmd(String ws_services_key, String jobId) {
    	return getAttribute(ws_services_key, jobId, "getCmd");
    }
    // skeleton function for getXXX from the jobservice
    public static String getAttribute(String ws_services_key, String jobId, String QName) {
      try {
    	String endpoint = "http://localhost:8080/axis/services/Generic";
    	Service  service = new Service();
    	Call     call    = (Call) service.createCall();
    	call.setTargetEndpointAddress( new java.net.URL(endpoint) );
    	call.setOperationName(new QName("", QName));
    	call.addParameter( "ws_services_key", XMLType.SOAP_STRING, ParameterMode.IN);
    	call.addParameter( "jobid", XMLType.SOAP_STRING, ParameterMode.IN);
    	call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING );
    	String ret = (String)call.invoke( new Object[] { ws_services_key, jobId } );
    	//TODO: if the result is too large, say, it is more than 1 Mbytes. we should show part of it.
    	return ret;
      } catch (Exception e){
    	  e.printStackTrace();
    	  //return "the result file is empty";
    	  return null;
      }
    }
    public static String[] listFiles(String ws_services_key, String jobId){
    	try {
	    	String endpoint = "http://localhost:8080/axis/services/Generic";
	    	Service  service = new Service();
	    	Call     call    = (Call) service.createCall();
	    	call.setTargetEndpointAddress( new java.net.URL(endpoint) );
	    	call.setOperationName(new QName("", "listFiles"));
	    	call.addParameter( "ws_services_key", XMLType.SOAP_STRING, ParameterMode.IN);
	    	call.addParameter( "jobid", XMLType.SOAP_STRING, ParameterMode.IN);
	    	call.setReturnType( org.apache.axis.encoding.XMLType.SOAP_ARRAY );
	    	String[] ret = (String[])call.invoke( new Object[] { ws_services_key, jobId } );
	    	return ret;
	      } catch (Exception e){
	    	  e.printStackTrace();
	    	  return null;
	      }
    }
}
