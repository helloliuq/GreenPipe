package org.uc.sidgrid.oauth;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;
import net.oauth.server.OAuthServlet;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import org.gridlab.gridsphere.portlet.User; //SIDGrid: change to sidgrid user class 
import org.uc.sidgrid.app.User;

public class AuthCheckFilter implements Filter{
	 private FilterConfig filterConfig = null;
	 private  Set<String> supportedOAuthParameters;
	 private String loginPage;
	 
	 private final static String FILTER_APPLIED = "_security_filter_applied";
	 private final static int buf_size = 4096;
	 
	 private static Log log = LogFactory.getLog(AuthCheckFilter.class);
	    
	 public AuthCheckFilter() { //called once. no method arguments allowed here!
		 supportedOAuthParameters = new HashSet<String>();
	 }
	 public void init(FilterConfig conf) throws ServletException {
		 this.filterConfig = conf;
		 for (OAuthConsumerParameter supportedParameter : OAuthConsumerParameter.values()) {
		      supportedOAuthParameters.add(supportedParameter.toString());
		 }
		 this.supportedOAuthParameters = Collections.unmodifiableSet(supportedOAuthParameters);
		 this.loginPage = filterConfig.getInitParameter("LoginPage");
	 }
	 public void destroy() {
	 }
	 public void doFilter(ServletRequest request,ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
		
		 HttpServletRequest hreq = (HttpServletRequest)request;
	     HttpServletResponse hres = (HttpServletResponse)response;
	     HttpSession session = hreq.getSession();
	     //print some session info for debugging
	     String sessCreateTime = (new java.sql.Timestamp(session.getCreationTime())).toString();
	     String sessAccessTime = (new java.sql.Timestamp(session.getLastAccessedTime())).toString();
	     long idle_time = (System.currentTimeMillis()- session.getLastAccessedTime())/1000;
	     log.info("session info=>"+session.isNew()+":"+sessAccessTime+":"+idle_time);
	     
	     String requestor = hreq.getRemoteHost();
	     
	     String checkURL = hreq.getRequestURL().toString();
	     log.info("pass the URL "+checkURL);
	     if (checkURL.contains("JSON-RPC") || checkURL.contains("echo")){ // check the JSON-RPC and Old-JSON-RPC
	    	 try { 
	    		 log.info("request from "+requestor);
	    	     String header = hreq.getHeader("Authorization");
	    	     /** This is a local request not a gadget request **/
	    	     long timeout = Long.parseLong(filterConfig.getInitParameter("timeout"));
	    	     /** if (idle_time > 30){ // the session has been idle for too long
	    	    	 String context = hreq.getContextPath();
	    	    	 log.info("the session has expired");
	    	    	 //session.invalidate();
	    	    	 //((HttpServletResponse)response).setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
	    	    	 ((HttpServletResponse)response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		    		 //((HttpServletResponse)response).sendRedirect(context+"/jsp/login.jsp");
		    		 return;
	    	     } **/
	    	     
	    	     if (header == null){
	    	    	 //TODO: we should add our own header information for checking
	    	    	 log.info("NO OAUTH information in header. so it must come from our gridsphere page");
	    	    	 //chain.doFilter(request, response);
	    	    	 String defaultRPC = filterConfig.getInitParameter("oldrpc");
	    	    	 filterConfig.getServletContext().getRequestDispatcher(defaultRPC).forward(request, response);
	    	    	 return;
	    	     }	 
	    		 //if it is lsgw webservice key authorization header: Authorization = lsgw wskey='xxxx'
	    		 //check the validity of the key
	    		 //if it is oauth authorization header
	    		 OAuthMessage requestMessage = getMessage(hreq, null);
	    		 OAuthAccessor accessor = SampleOAuthProvider.getAccessor(requestMessage);
	    		 //TODO: if the validation doesn't work, comment the line out
	    		 //SampleOAuthProvider.VALIDATOR.validateMessage(requestMessage, accessor);
	    		 String userId = (String) accessor.getProperty("user");
	    		 User user = (User)accessor.getProperty("userObject");
                 log.info("username :"+userId);
	    		 log.info("username from session:"+session.getAttribute("username"));
                 hreq.setAttribute("OAuthUser", user);
                 hreq.setAttribute("username",userId);
                 session.setAttribute("username",userId);
	    		 //logPostData(hreq); 
	    		 chain.doFilter(hreq, response);
	    	 }catch (Exception e){
	    		 e.printStackTrace();
	    		 log.warn("AuthCheck error "+e.toString());
	             SampleOAuthProvider.handleException(e, hreq, hres, false);
	         }
	     } else if (!checkURL.endsWith("login.jsp") && !checkURL.endsWith("simpleauth") && checkURL.endsWith(".jsp")){
	    	 if (session.getAttribute("username") == null ){
	    		 //let's check whether the request has a long-alive cookie for these user
	    		 Cookie[] cookies = hreq.getCookies();
	    		 boolean hascookie = false;
	    		 if (cookies !=null ){
	    		   for(Cookie tmp : cookies){
	    			 if (tmp.getName().equalsIgnoreCase("MyCookie")){
	    				 hascookie = true;
	    				 String value = tmp.getValue();
	    				 session.setAttribute("username", value);
	    			  }
	    		    }
	    		 }
	    		 if (!hascookie) {
	    		   // remember the current URL and keep it in the session so that we can forward to that URL 
	    		   // after the authentication is done
	    			 Enumeration params = request.getParameterNames();
                     while (params.hasMoreElements()) {
                            String paramName = (String)params.nextElement();
                            String paramValue = request.getParameter(paramName);
                            checkURL += "?"+paramName+"="+paramValue;
                            //TODO: the following params should be appended using '&'
                     }
                     log.info("keep the old URL "+checkURL);
                     session.setAttribute("pastURL", checkURL);
	    		     String context = hreq.getContextPath();
	    		     ((HttpServletResponse)response).sendRedirect(context+"/"+this.loginPage);
	    		 } 
	    	 }
	    	 chain.doFilter(request, response);
	     } else { //login.jsp or simpleauth
	    	 chain.doFilter(request, response);
	     }
	 }
	 public OAuthMessage getMessage(HttpServletRequest request, String URL) {
		 if (URL == null) {
	            URL = request.getRequestURL().toString();
	        }
	        int q = URL.indexOf('?');
	        if (q >= 0) {
	            URL = URL.substring(0, q);
	            // The query string parameters will be included in
	            // the result from getParameters(request).
	        }
	        Map paraMap = parseParameters(request);
	        return new OAuthMessage(request.getMethod(), URL,paraMap.entrySet());
	                
	 }
	 public Map parseParameters(HttpServletRequest request) {
		    Map parameters = new HashMap<String, String>();

		    String header = request.getHeader("Authorization");
		    if ((header != null) && (header.toLowerCase().startsWith("oauth "))) {
		      String authHeaderValue = header.substring(6);

		      //create a map of the authorization header values per OAuth Core 1.0, section 5.4.1
		      String[] headerEntries = StringSplitUtils.splitIgnoringQuotes(authHeaderValue, ',');
		      for (Object o : StringSplitUtils.splitEachArrayElementAndCreateMap(headerEntries, "=", "\"").entrySet()) {
		        Map.Entry entry = (Map.Entry) o;
		        String key;
		        String value;
		        try {
		          key = OAuthCodec.oauthDecode((String) entry.getKey());
		          value = OAuthCodec.oauthDecode((String) entry.getValue());
		        }
		        catch (DecoderException e) {
		          throw new IllegalStateException(e);
		        }

		        if ((this.supportedOAuthParameters.contains(key)) || ("realm".equals(key))) {
		          parameters.put(key, value);
		        }
		      }
		    }
		    else {
		      for (String supportedOAuthParameter : this.supportedOAuthParameters) {
		        String param = request.getParameter(supportedOAuthParameter);
		        if (param != null) {
		          parameters.put(supportedOAuthParameter, param);
		        }
		      }
		    }

		    return parameters;
		  }
}
