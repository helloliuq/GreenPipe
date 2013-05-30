package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class login_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
    _jspx_dependants.add("/jsp/navlogin.html");
  }

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

      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
      out.write("<head>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />\n");
      out.write("<title>GreenPipe Login Page</title>\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"../css/screen.css\" />\n");
      out.write("<style type=\"text/css\">\n");
      out.write(".warning { color: red; }\n");
      out.write("label {width:100px; }\n");
      out.write("#main {width:500px;margin-left:auto;margin-right:auto;}\n");
      out.write("</style>\n");
      out.write("<script src=\"");
      out.print(request.getContextPath());
      out.write("/js/jquery.js\" type=\"text/javascript\"></script>\n");
      out.write("<script src=\"");
      out.print(request.getContextPath());
      out.write("/js/jquery.form.js\" type=\"text/javascript\"></script>\n");
      out.write("<script src=\"");
      out.print(request.getContextPath());
      out.write("/js/jquery.validate.js\" type=\"text/javascript\"></script>\n");
      out.write("\n");
      out.write("<script src=\"");
      out.print(request.getContextPath());
      out.write("/js/cmxforms.js\" type=\"text/javascript\"></script>\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("\tjQuery(function() {\n");
      out.write("\t\t// show a simple loading indicator\n");
      out.write("\t\tvar loader = jQuery('<div id=\"loader\"><img src=\"../images/loading.gif\" alt=\"loading...\" /></div>')\n");
      out.write("\t\t\t.css({position: \"relative\", top: \"1em\", left: \"25em\"})\n");
      out.write("\t\t\t.appendTo(\"body\")\n");
      out.write("\t\t\t.hide();\n");
      out.write("\t\tjQuery().ajaxStart(function() {\n");
      out.write("\t\t\tloader.show();\n");
      out.write("\t\t}).ajaxStop(function() {\n");
      out.write("\t\t\tloader.hide();\n");
      out.write("\t\t}).ajaxError(function(a, b, e) {\n");
      out.write("\t\t\tthrow e;\n");
      out.write("\t\t});\n");
      out.write("\t\t\n");
      out.write("\t\tvar login_form = jQuery(\"#form\").validate({\n");
      out.write("\t\t\tsubmitHandler: function(form) {\n");
      out.write("                            form.submit();\n");
      out.write("\t\t\t}\n");
      out.write("\t\t});\n");
      out.write("\n");
      out.write("              var signup_form = jQuery(\"#signup\").validate({\n");
      out.write("\t\t\tsubmitHandler: function(form) {\n");
      out.write("\t\t\t\tjQuery(form).ajaxSubmit({\n");
      out.write("\t\t\t\t\ttarget: \"#output\"\n");
      out.write("\t\t\t\t});\n");
      out.write("\t\t\t}\n");
      out.write("\t\t});\n");
      out.write("\n");
      out.write("\n");
      out.write("\t\tjQuery(\"#reset\").click(function() {\n");
      out.write("\t\t\tv.resetForm();\n");
      out.write("\t\t});\n");
      out.write("\t});\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"../css/screen.css\" />\n");
      out.write("\n");
      out.write("<style type=\"text/css\">\n");
      out.write("\n");
      out.write("#menu {\n");
      out.write("\tfloat:left;\n");
      out.write("\twidth:95%;\n");
      out.write("\tbackground:#efefef;\n");
      out.write("\t#font-size:93%;\n");
      out.write("\t#font-size:20;\n");
      out.write("\tline-height:normal;\n");
      out.write("\tborder-bottom:1px solid #666;\n");
      out.write("    padding: 30px;\n");
      out.write("\t}\n");
      out.write("#menu ul {\n");
      out.write("\tmargin:0;\n");
      out.write("\tpadding:10px 10px 0 50px;\n");
      out.write("\tlist-style:none;\n");
      out.write("\t}\n");
      out.write("#menu li {\n");
      out.write("\tdisplay:inline;\n");
      out.write("\tmargin:0;\n");
      out.write("\tpadding:0;\n");
      out.write("\t}\n");
      out.write("/*\n");
      out.write("#menu a {\n");
      out.write("\tfloat:left;\n");
      out.write("\tbackground:url(\"../images/tableftF.gif\") no-repeat left top;\n");
      out.write("\tmargin:0;\n");
      out.write("\tpadding:0 0 0 4px;\n");
      out.write("\ttext-decoration:none;\n");
      out.write("\t}\n");
      out.write("#menu a span {\n");
      out.write("\tfloat:left;\n");
      out.write("\tdisplay:block;\n");
      out.write("\tbackground:url(\"../images/tabrightF.gif\") no-repeat right top;\n");
      out.write("\tpadding:5px 15px 4px 6px;\n");
      out.write("\tcolor:#666;\n");
      out.write("\t}\n");
      out.write("*/\n");
      out.write("\t\n");
      out.write("/* Commented Backslash Hack hides rule from IE5-Mac \\*/\n");
      out.write("#menu a span {float:none;}\n");
      out.write("/* End IE5-Mac hack */\n");
      out.write("#menu a:hover span {color:#FFFFFF;}\n");
      out.write("#menu a:hover {background-position:0% -42px;}\n");
      out.write("#menu a:hover span {background-position:100% -42px;}\n");
      out.write("</style>\n");
      out.write("\n");
      out.write("<div id=\"menu\" class=\"ui-widget ui-widget-header\">\n");
      out.write("    <div>\n");
      out.write("        GreenPipe\n");
      out.write("    </div>\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("\n");
      out.write("<h1 id=\"banner\">Login to Gadget Workspace</h1>\n");
      out.write("<div id=\"main\">\n");
      out.write("<!--<script type=\"text/javascript\">\n");
      out.write("alert('");
      out.print(request.getContextPath());
      out.write("')</script>-->\n");
      out.write("<form method=\"post\" class=\"cmxform\" id=\"form\" action=\"");
      out.print(request.getContextPath());
      out.write("/simpleauth\">\n");
      out.write("\t<fieldset>\n");
      out.write("\t\t<legend>Please login</legend>\n");
      out.write("\t\t<p>\n");
      out.write("\t\t\t<label for=\"user\">Username</label>\n");
      out.write("\t\t\t<input id=\"user\" name=\"username\" title=\"Please enter your username (at least 3 characters)\" class=\"required\" minlength=\"3\" />\n");
      out.write("\t\t</p>\n");
      out.write("\t\t<p>\n");
      out.write("\t\t\t<label for=\"pass\">Password</label>\n");
      out.write("\t\t\t<input type=\"password\" name=\"password\" id=\"login_password\" class=\"required\" minlength\"5\" />\n");
      out.write("\t\t</p>\n");
      out.write("\t\t<p>\n");
      out.write("\t\t\t<input class=\"submit\" type=\"submit\" value=\"Login\"/>\n");
      out.write("                     <input value=\"Clear\" type=\"reset\">\n");
      out.write("\n");
      out.write("\t\t</p>\n");
      out.write("\t</fieldset>\n");
      out.write("</form>\n");
      out.write("\n");
      out.write("<p>\n");
      out.write("If you don't have an account, please sign up here!\n");
      out.write("</p>\n");
      out.write("<form class=\"cmxform\" id=\"signup\" method=\"POST\" action=\"");
      out.print(request.getContextPath());
      out.write("/signup\">\n");
      out.write("<fieldset>\n");
      out.write("          <legend>Please sign up</legend>\n");
      out.write("          <label>First name</label> \n");
      out.write("            <input type=\"text\" name=\"fname\"><br>\n");
      out.write("          <label>Last name</label> \n");
      out.write("            <input type=\"text\" name=\"lname\"><br>\n");
      out.write("          <label>Username</label> \n");
      out.write("            <input type=\"text\" name=\"username\" id=\"username\" class=\"required\" minlength=\"3\" ><br>\n");
      out.write("          <label>Password</label> \n");
      out.write("            <input type=\"password\" name=\"password\" id=\"password\" minlength\"5\" class=\"required\"><br>\n");
      out.write("          <label>Password (again)</label> \n");
      out.write("            <input type=\"password\" name=\"repasswd\" id=\"repasswd\" minlength\"5\" class=\"required\" equalTo=\"#password\"><br>\n");
      out.write("          <p>\n");
      out.write("          <input class=\"submit\" type=\"submit\" id=\"join\" value=\"Register\" />\n");
      out.write("          <input type=\"reset\" value=\"clear\" />\n");
      out.write("          </p>\n");
      out.write("</fieldset>\n");
      out.write("</form>\n");
      out.write("\n");
      out.write("<div id=\"output></div>\n");
      out.write("</body>\n");
      out.write("</html>\n");
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
