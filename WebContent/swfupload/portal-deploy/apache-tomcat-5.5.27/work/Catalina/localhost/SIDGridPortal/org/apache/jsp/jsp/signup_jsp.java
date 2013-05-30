package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class signup_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<title>Registration for Gateway</title>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("<h1>Registration for Gateway</h1>\r\n");
      out.write("<p>\r\n");
      out.write("Please provide the following information\r\n");
      out.write("</p>\r\n");
      out.write("<form method=\"POST\" action=\"");
      out.print(request.getContextPath());
      out.write("/signup\">\r\n");
      out.write("<p>Username : <input type=\"text\" size=\"15\" maxlength=\"25\" name=\"username\"></p>\r\n");
      out.write("<p>Password : <input type=\"password\" size=\"15\" maxlength=\"25\" name=\"password\"></p>\r\n");
      out.write("<p>Password (again): <input type=\"password\" size='15\" maxlength=\"25\"></p>\r\n");
      out.write("<p>Email : <input type=\"text\" size=\"15\" maxlength=\"25\" name=\"username\"></p>\r\n");
      out.write("\r\n");
      out.write("<input type=\"submit\" name=\"Authorize\" value=\"Authorize\"/>\r\n");
      out.write("<input value=\"Clear\" type=\"reset\">\r\n");
      out.write("\r\n");
      out.write("</form>\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
