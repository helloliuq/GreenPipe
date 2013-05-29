package org.uc.sidgrid.services;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uc.sidgrid.app.User;
import org.uc.sidgrid.dao.ApplicationDAO;
import org.uc.sidgrid.dao.UserDAO;
import org.uc.sidgrid.dao.DAOFactory;
import org.uc.sidgrid.dao.sidgridDAO;


/**
 * An authentication servlet for gadget application
 * It checkes the user/password. If the pair is validate, the servlet set the cookie with gsuid and wskey
 * @author wenjun wu
 *
 */
public class SimpleAuthServlet extends HttpServlet{
	private String userPage, adminPage;
	
	private static final String COOKIE_REQUEST = "cookie-request";
    private static final int COOKIE_EXPIRATION_TIME = 60 * 60 * 24 * 7;  // 1 week (in secs)
    private static Log log = LogFactory.getLog(SimpleAuthServlet.class);
    
    public final void init(ServletConfig config) throws ServletException {
        super.init(config);

        //GridSphereConfig.setServletConfig(config);
        
        //SportletLog.setConfigureURL(GridSphereConfig.getServletContext().getRealPath("/WEB-INF/classes/log4j.properties"));
        //this.context = new SportletContext(config);
        this.userPage = config.getInitParameter("UserMainPage");
        this.adminPage = config.getInitParameter("AdminMainPage");
        
        log.info("in init of AuthServlet "+userPage+":"+adminPage);
        
    }
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
	  try {
          processRequest(req, res);
	  }catch(Exception e){
		  RequestDispatcher rd = req.getRequestDispatcher("/jsp/login-fail.jsp");
		  rd.forward(req, res);
          return;
	  }
    }
	public void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		// check whether this session has already been logged in
		User user = null;
		String loginName = req.getParameter("username");
        String loginPassword = req.getParameter("password");

		HttpSession session = req.getSession();
		if (session.getAttribute("User")!=null){
			user = (User)session.getAttribute("User");
			if (user.getName().equals(loginName)){
				return;
			}
		}
		// if not, check the database for user name/password
		if ((loginName == null) || (loginPassword == null)) {
            throw new ServletException( "LOGIN_AUTH_BLANK");
        }
        // first get user
		user = new User(); user.setName(loginName);
        //User user = activeLoginModule.getLoggedInUser(loginName);
        /** 
         * String accountStatus = (String)user.getAttribute(org.uc.sidgrid.dao.DISABLED);
           if ((accountStatus != null) && ("TRUE".equalsIgnoreCase(accountStatus)))
            throw new ServletException("LOGIN_AUTH_DISABLED");
         **/
		boolean checked = checkAuthentication(req,user,loginPassword);
		RequestDispatcher rd;
		String context = req.getContextPath();
		if (checked){
			DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
			UserDAO userdao = daoFactory.getUserDAO();
			try {
			 user = userdao.getUserByName(user.getName());
			} catch (Exception e){
				e.printStackTrace();
			}
			System.err.println("user logged in "+user.getName()+":"+user.getRole());
			//setUserCookie(user, res);
			//session.setAttribute("User", user);
			session.setAttribute("username", user.getName());
			// create a new cookie if there is no such a cookie
			req.getCookies();
			Cookie mycookie = new Cookie("MyCookie",user.getName());
			mycookie.setMaxAge(365 * 24 * 60 * 60);
		    res.addCookie(mycookie);
			//setUserSettings(event, user);
			//TODO: check the user's role to determin which page to go
			 req.setAttribute("userName", user.getName());
			if (user.getRole().equalsIgnoreCase(User.Anonymous)){
				System.err.println("redirect to the page "+this.userPage+" because "+user.getRole());
			    HttpSession sess = req.getSession();
			    String oldUrl = (String)sess.getAttribute("pastURL");
			    if (oldUrl != null)
			    	res.sendRedirect(oldUrl);
			    else 
			        res.sendRedirect(context+this.userPage);
			} else {
				System.err.println("redirect to the page "+this.adminPage+" because "+user.getRole());
				res.sendRedirect(context+this.adminPage);
			}
		} else{
			rd = req.getRequestDispatcher("/jsp/login-fail.jsp");
            rd.forward(req, res);
		}
	}
	
	public boolean checkAuthentication(HttpServletRequest req, User user, String password) {
        log.debug("Entering authenticate");
        // Check that password is not null
        if (password == null) {
            log.debug("Password is not provided.");
            return false;
        }
        DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
		UserDAO userdao = daoFactory.getUserDAO();
		boolean res = userdao.authenticateUser(user.getName(), password);
		return res;
        // Check that password maps to the given user
        /** sidgridDAO dao = new sidgridDAO();
        ServletContext context = req.getSession().getServletContext();
        dao.createDBConnection(context);
        boolean  res=dao.authenticateUser(user.getName(), password);
        return res;**/
    }

}
