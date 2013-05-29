package org.uc.sidgrid.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uc.sidgrid.app.User;
import org.uc.sidgrid.dao.DAOFactory;
import org.uc.sidgrid.dao.UserDAO;
import org.uc.sidgrid.dao.sidgridDAO;
 
/**
 * a signup servlet to handle the user registration request
 * @author wenjun wu
 *
 */
public class SignUpServlet extends HttpServlet{
	private static Log log = LogFactory.getLog(SignUpServlet.class);
	private final static String USER_EXIST = "the user id already exist!";
	private final static String SignUp_OK = "signup successful. Please login in.";
	
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		RequestDispatcher rd;
		res.setContentType("text/plain;charset=utf-8");
		OutputStream out = res.getOutputStream();
		//res.setContentType("text/html");
		//PrintWriter out = res.getWriter();
		
		String username = req.getParameter("username");
        String password = req.getParameter("password");
        String emailid = req.getParameter("email");
        
        //log.info("signup request"+username+":"+password);
        User user = new User();
        user.setName(username);
        String md_passwd = sidgridDAO.md5(password);
        user.setPasswd(md_passwd);
        user.setRole(User.Anonymous);
        
        DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
        UserDAO dao = daoFactory.getUserDAO();
        String response;
        try {
          //TODO: check whether the user exists or not
          User tmp = dao.getUserByName(username);
          if (tmp!=null){
        	  response = USER_EXIST;
        	  
          } else {
             dao.create(user);
             response = SignUp_OK;
          }
          log.info("signup servlet sends: "+response);
          byte[] bout = response.getBytes(); 
    	  res.setIntHeader("Content-Length", bout.length);
    	  out.write(bout);
    	  out.flush();
    	  out.close();
    	  res.setStatus(HttpServletResponse.SC_OK);
    	  //TODO:maybe we should do redirect here
    	  rd = req.getRequestDispatcher("/jsp/login.jsp");
          //rd = req.getRequestDispatcher("/jsp/main.jsp");
        } catch(Exception e){
          e.printStackTrace();	
          //rd = req.getRequestDispatcher("/jsp/login.jsp");
        }
        
	}
    
}
