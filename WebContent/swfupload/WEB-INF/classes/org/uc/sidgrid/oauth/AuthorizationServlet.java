package org.uc.sidgrid.oauth;

/*
 * Copyright 2007 AOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.uc.sidgrid.app.User;
import org.uc.sidgrid.dao.sidgridDAO;
//import org.gridlab.gridsphere.portlet.User;
//import org.gridlab.gridsphere.portlet.impl.SportletRequestImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*import org.teragrid.uc.lsgw.services.ClustalwServiceImpl;
import org.teragrid.uc.lsgw.services.LSGWUser;
*/

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;
import net.oauth.server.OAuthServlet;
import net.oauth.OAuthException;
import net.oauth.OAuthProblemException;

/**
 * Autherization request handler.
 *
 * @author Praveen Alavilli
 */
public class AuthorizationServlet extends HttpServlet {
	
	private static final String COOKIE_REQUEST = "cookie-request";
    private static final int COOKIE_EXPIRATION_TIME = 60 * 60 * 24 * 7;  // 1 week (in secs)
    private static Log log = LogFactory.getLog(AuthorizationServlet.class);
    
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.err.println("in init of AuthServlet");
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        try{
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            OAuthAccessor accessor = SampleOAuthProvider.getAccessor(requestMessage);
           
            if (Boolean.TRUE.equals(accessor.getProperty("authorized"))) {
                // already authorized send the user back
            	log.info("user "+accessor.consumer.consumerKey+" get authorized");
                //returnToConsumer(request, response, accessor);
            	log.info("in this version, redo the authorization");
                sendToAuthorizePage(request, response, accessor);
            } else {
            	log.info("user "+accessor.consumer.consumerKey+" is not authorized yet. send the auth page");
                sendToAuthorizePage(request, response, accessor);
            }
        
        } catch (Exception e){
        	e.printStackTrace();
            SampleOAuthProvider.handleException(e, request, response, true);
        }
    }
    
    @Override 
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException{
        
        try{
        	processRequest(request, response); 
        } catch (Exception e){
            SampleOAuthProvider.handleException(e, request, response, true);
        }
    }
    
    private void sendToAuthorizePage(HttpServletRequest request, 
            HttpServletResponse response, OAuthAccessor accessor)
    throws IOException, ServletException{
        String callback = request.getParameter("oauth_callback");
        if(callback == null || callback.length() <=0) {
            callback = "none";
        }
        String consumer_description = (String)accessor.consumer.getProperty("description");
        request.setAttribute("CONS_DESC", consumer_description);
        request.setAttribute("CALLBACK", callback);
        request.setAttribute("TOKEN", accessor.requestToken);
        request.getRequestDispatcher("/jsp/authorize.jsp").forward(request, response);
        
    }
    
    private void returnToConsumer(HttpServletRequest request, 
            HttpServletResponse response, OAuthAccessor accessor)
    throws IOException, ServletException{
        // send the user back to site's callBackUrl
        String callback = request.getParameter("oauth_callback");
        log.info("callback is:"+callback);
        if("none".equals(callback) 
            && accessor.consumer.callbackURL != null 
                && accessor.consumer.callbackURL.length() > 0){
            // first check if we have something in our properties file
            callback = accessor.consumer.callbackURL;
        }
        if( "none".equals(callback) ) {
            // no call back it must be a client
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.println("You have successfully authorized '" 
                    + accessor.consumer.getProperty("description") 
                    + "'. Please close this browser window and click continue"
                    + " in the client.");
            out.close();
        } else {
            // if callback is not passed in, use the callback from config
            if(callback == null || callback.length() <=0 )
                callback = accessor.consumer.callbackURL;
            String token = accessor.requestToken;
            if (token != null) {
                callback = OAuth.addParameters(callback, "oauth_token", token);
            }

            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            log.info("before setting location:"+callback);
            response.setHeader("Location", callback);
        }
    }
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, OAuthException,
                              OAuthProblemException, ServletException {
    	OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
        OAuthAccessor accessor = SampleOAuthProvider.getAccessor(requestMessage);
		// check whether this session has already been logged in
		User user = null;
		String loginName = request.getParameter("username");
        String loginPassword = request.getParameter("password");

        if (Boolean.TRUE.equals(accessor.getProperty("authorized"))) {
            // already authorized send the user back
            returnToConsumer(request, response, accessor);
        }
        //TODO: how to deal with the session in oauth context
		HttpSession session = request.getSession();
		if (session.getAttribute("User")!=null){
			user = (User)session.getAttribute("User");
			if (user.getName().equals(loginName)){
				SampleOAuthProvider.markAsAuthorized(accessor, loginName);
		        returnToConsumer(request, response, accessor);
			}
		}
		// if not, check the database for user name/password
		if ((loginName == null) || (loginPassword == null)) {
			sendToAuthorizePage(request, response, accessor);
            //throw new AuthorizationException( "LOGIN_AUTH_BLANK");
        }
        /** Perform SIDGRid authentication here **/
                //kavithaa:test 
		sidgridDAO auth = new sidgridDAO();

                boolean  res=auth.authenticateUser(loginName, loginPassword);
                 if(!res){
                   throw new OAuthProblemException("LOGIN_AUTH_DISABLED");
                 }
                 user=new User(); user.setName(loginName);
                             
	 	/**user = loginService.login( new SportletRequestImpl((HttpServletRequest)request));
        String accountStatus = (String)user.getAttribute(User.DISABLED);
        if ((accountStatus != null) && ("TRUE".equalsIgnoreCase(accountStatus)))
            throw new OAuthProblemException("LOGIN_AUTH_DISABLED"); **/
        
        log.info("user logged in "+user.toString());
        session.setAttribute("User", user);
        session.setAttribute("username",loginName);
        log.info("set user in session");
        // set userId in accessor and mark it as authorized
        SampleOAuthProvider.markAsAuthorized(accessor, loginName);
        log.info("set accessor"); 
        returnToConsumer(request, response, accessor);
	}
 
    private static final long serialVersionUID = 1L;

}
