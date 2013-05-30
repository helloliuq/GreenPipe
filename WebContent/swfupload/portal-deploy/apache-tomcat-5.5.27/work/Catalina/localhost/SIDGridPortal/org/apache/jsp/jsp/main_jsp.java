package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class main_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
    _jspx_dependants.add("/jsp/nav.html");
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

      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<title>Main Page For Science  Gadget WorkSpace</title>\n");
      out.write(" \n");
      out.write("<style type=\"text/css\">\n");
      out.write("        @import \"http://o.aolcdn.com/dojo/1.0.0/dijit/themes/tundra/tundra.css\";\n");
      out.write("        @import \"http://o.aolcdn.com/dojo/1.0.0/dojo/resources/dojo.css\"\n");
      out.write("</style>\n");
      out.write("<style>\n");
      out.write("        .myIcon {\n");
      out.write("           /*  Note: Drupal may add an anchor tag here.  Don't include it in your code */\n");
      out.write("           background-image: \n");
      out.write("              url(http://o.aolcdn.com/dojo/1.0.0/dijit/themes/tundra/images/checkmark.gif);\n");
      out.write("           background-position: -16px 0px;\n");
      out.write("           width: 16px;\n");
      out.write("           height: 16px;\n");
      out.write("        }\n");
      out.write("\n");
      out.write("ul.MenuBarHorizontal {\n");
      out.write("cursor:default;\n");
      out.write("font-size:100%;\n");
      out.write("list-style-type:none;\n");
      out.write("margin:0;\n");
      out.write("padding:0;\n");
      out.write("width:auto;\n");
      out.write("}\n");
      out.write("ul.MenuBarActive {\n");
      out.write("z-index:1000;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal li {\n");
      out.write("cursor:pointer;\n");
      out.write("float:left;\n");
      out.write("font-size:100%;\n");
      out.write("list-style-type:none;\n");
      out.write("margin:0;\n");
      out.write("padding:0;\n");
      out.write("position:relative;\n");
      out.write("text-align:left;\n");
      out.write("width:8em;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal ul {\n");
      out.write("cursor:default;\n");
      out.write("font-size:100%;\n");
      out.write("left:-1000em;\n");
      out.write("list-style-type:none;\n");
      out.write("margin:0;\n");
      out.write("padding:0;\n");
      out.write("position:absolute;\n");
      out.write("width:8.2em;\n");
      out.write("z-index:1020;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal ul.MenuBarSubmenuVisible {\n");
      out.write("left:auto;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal ul li {\n");
      out.write("width:28.2em;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal ul ul {\n");
      out.write("margin:-5% 0 0 95%;\n");
      out.write("position:absolute;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal ul.MenuBarSubmenuVisible ul.MenuBarSubmenuVisible {\n");
      out.write("left:auto;\n");
      out.write("top:0;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal ul {\n");
      out.write("border:1px solid #CCCCCC;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal a {\n");
      out.write("background-color:#EEEEEE;\n");
      out.write("color:#333333;\n");
      out.write("cursor:pointer;\n");
      out.write("display:block;\n");
      out.write("padding:0.5em 0.75em;\n");
      out.write("text-decoration:none;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal a:hover, ul.MenuBarHorizontal a:focus {\n");
      out.write("background-color:#3333CC;\n");
      out.write("color:#FFFFFF;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal a.MenuBarItemHover, ul.MenuBarHorizontal a.MenuBarItemSubmenuHover, ul.MenuBarHorizontal a.MenuBarSubmenuVisible {\n");
      out.write("background-color:#3333CC;\n");
      out.write("color:#FFFFFF;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal a.MenuBarItemSubmenu {\n");
      out.write("background-image:url(SpryMenuBarDown.gif);\n");
      out.write("background-position:95% 50%;\n");
      out.write("background-repeat:no-repeat;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal ul a.MenuBarItemSubmenu {\n");
      out.write("background-image:url(SpryMenuBarRight.gif);\n");
      out.write("background-position:95% 50%;\n");
      out.write("background-repeat:no-repeat;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal a.MenuBarItemSubmenuHover {\n");
      out.write("background-image:url(SpryMenuBarDownHover.gif);\n");
      out.write("background-position:95% 50%;\n");
      out.write("background-repeat:no-repeat;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal ul a.MenuBarItemSubmenuHover {\n");
      out.write("background-image:url(SpryMenuBarRightHover.gif);\n");
      out.write("background-position:95% 50%;\n");
      out.write("background-repeat:no-repeat;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal iframe {\n");
      out.write("position:absolute;\n");
      out.write("z-index:1010;\n");
      out.write("}\n");
      out.write("ul.MenuBarHorizontal li.MenuBarItemIE {\n");
      out.write("background:#FFFFFF none repeat scroll 0 0;\n");
      out.write("display:inline;\n");
      out.write("float:left;\n");
      out.write("}\n");
      out.write("</style>\n");
      out.write("<script type=\"text/javascript\" src=\"http://o.aolcdn.com/dojo/1.0.0/dojo/dojo.xd.js\" \n");
      out.write("        djConfig=\"parseOnLoad: true\">\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("       dojo.require(\"dojo.parser\");\n");
      out.write("       dojo.require(\"dijit.Menu\");\n");
      out.write("</script>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"../css/screen.css\" />\n");
      out.write("<link type=\"text/css\" href=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/themes/cupertino/jquery-ui.css\" rel=\"stylesheet\" />\n");
      out.write("\n");
      out.write("<style type=\"text/css\">\n");
      out.write("\n");
      out.write("#menu {\n");
      out.write("\t/* float:left; */\n");
      out.write("\twidth:60%;\n");
      out.write("        margin-left:auto;\n");
      out.write("        margin-right:auto;\n");
      out.write("\tbackground:#efefef;\n");
      out.write("\tline-height:normal;\n");
      out.write("\tborder-bottom:1px solid #666;\n");
      out.write("    /* padding: 10px 0px 25px 0px; */\n");
      out.write("    font-size: 14px;\n");
      out.write("    font-style: normal;\n");
      out.write("\t}\n");
      out.write("#menu ul {\n");
      out.write("\tmargin:0;\n");
      out.write("\t/* padding:10px 10px 0 50px; */\n");
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
      out.write("\t/* padding:5px 15px 4px 6px;*/\n");
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
      out.write("\n");
      out.write("\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("    $(document).ready( function() {\n");
      out.write("            document.getElementById(\"username\").innerHTML = user;\n");
      out.write("    });\n");
      out.write("    var user = \"");
      out.print(session.getAttribute("username") );
      out.write("\";\n");
      out.write("    </script>\n");
      out.write("<div id=\"menu\">\n");
      out.write("    <div style=\"float:left;padding-left:20px;padding-top:20px\">\n");
      out.write("        <img src=\"");
      out.print(request.getContextPath());
      out.write("/images/logo.png\" height=\"40px\" title=\"Science Portal\">\n");
      out.write("    </div>\n");
      out.write("    <div style=\"float:right;padding-right:20px;padding-top:20px\">\n");
      out.write("        <div style=\"float:left\">Logged in as&nbsp;</div>\n");
      out.write("        <div id=username style=\"font-weight:bold;float:left\"></div>&nbsp;\n");
      out.write("        <a href=");
      out.print(request.getContextPath());
      out.write("/jsp/runApplications.jsp>Jobs</a>&nbsp;\n");
      out.write("        <a href=");
      out.print(request.getContextPath());
      out.write("/jsp/applist.jsp>Settings</a>&nbsp;\n");
      out.write("        <a href=");
      out.print(request.getContextPath());
      out.write("/jsp/logout.jsp>Logout</a>\n");
      out.write("    </div>\n");
      out.write("</div>\n");
      out.write("<div style=\"clear:both\"></div>\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("\n");
      out.write("\r\n");
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
