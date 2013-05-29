package org.uc.sidgrid.services;

import java.io.*;
import java.util.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uc.sidgrid.oauth.AccessTokenServlet;
import javax.servlet.http.Cookie; 

public class FileUploadServlet extends HttpServlet{
	private static Log log = LogFactory.getLog(FileUploadServlet.class);
	
	protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String uploadedUrl = "";
		log.info("start file uploading");
		System.out.println("start file uploading");
		FileItemFactory factory = new DiskFileItemFactory();

       //		 Create a new file upload handler

		int i;
			
		ServletFileUpload upload = new ServletFileUpload(factory);
		String sessid = "";

		upload.setHeaderEncoding("UTF-8"); //Deal with Chinese/Japanese/.... file names
			try {
				List items = upload.parseRequest(request);
				if (items != null) {
					Iterator itr = items.iterator();
					while (itr.hasNext()) {
						FileItem item = (FileItem) itr.next();
						if( item.getFieldName().equals("JSESSIONID"))
							sessid = item.getString();
						if (item.isFormField()) {
							continue;
						} else {
							File fullFile=new File(item.getName());
							HttpSession sess = request.getSession(false);
							if( sess == null )
							{
								System.out.println("No session, creating one");
								sess = request.getSession();
							}
							String username = null;
							
							// handle cookies
							Cookie cookies[] = request.getCookies();
							if( cookies != null )
							{
								System.out.println("Num cookies: " + cookies.length );
								for( i=0; i<cookies.length; i++ )
								{
									System.out.println("cookie: " + i + " " + cookies[i].getName() + " : " + cookies[i].getValue() );
								}
							}
							else
							{
								System.out.println("No Cookies ! ");
							}
							
							// handle request parameters
						  	Enumeration e = (Enumeration)request.getParameterNames();
							while (e.hasMoreElements()) {
						  	    String name = (String)e.nextElement();
						  	    String value = request.getParameter(name);
						        if( name.equals("user") )
						            username = value;
							}
							
							// store the file and produce the file url
							// - location is dependent on whether the user is identified (which should always be the case)
							String path = sess.getServletContext().getRealPath("/")+"temp/"+sessid;
							File sessdir = new File(path);
							if ( !sessdir.exists()){
								sessdir.mkdir();
							}
							File savedFile=new File(path, fullFile.getName());
							if (savedFile.exists() )
								savedFile.delete();
							item.write(savedFile);
							uploadedUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
							if( false && username != null )
							{
								uploadedUrl +=request.getContextPath()+"/userfiles/"+username+"/"+item.getName();
							}
							else
							{
								System.out.println("file upload for anon user" );
								uploadedUrl +=request.getContextPath()+"/temp/"+sessid+"/"+item.getName();
							}
							System.out.println("file uploaded to " + savedFile + ", url=" + uploadedUrl );
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
				return;

			}
			//returns ok to swfupload. Otherwise swfupload will be at 100% for ever
			response.getOutputStream().println("FILEID:" +uploadedUrl);
			response.setStatus(HttpServletResponse.SC_OK);
			//response.getOutputStream().println("200 OK");
		}
}
