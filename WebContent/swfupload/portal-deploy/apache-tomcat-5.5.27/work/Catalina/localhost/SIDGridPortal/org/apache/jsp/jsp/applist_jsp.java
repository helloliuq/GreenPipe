package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.uc.sidgrid.dao.*;
import org.uc.sidgrid.app.*;
import java.util.*;

public final class applist_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<title>Application Administration</title>\n");
      out.write("\n");
      out.write("<style>\n");
      out.write("   .sw-table { \n");
      out.write("      margin-left:auto;\n");
      out.write("      margin-right:auto;\n");
      out.write("      width:60%;\n");
      out.write("      padding:2px;\n");
      out.write("   }\n");
      out.write("\n");
      out.write("</style>\n");
      out.write("\n");
      out.write("        <link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"../css/ui.jqgrid.css\" />\n");
      out.write("        <script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js\"></script>\n");
      out.write("        <script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/jquery-ui.min.js\"></script>\n");
      out.write("        <link type=\"text/css\" href=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/themes/cupertino/jquery-ui.css\" rel=\"stylesheet\" />\n");
      out.write("\n");
      out.write("        <link href=\"../swfupload/css/default.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
      out.write("        <script src=\"../js/grid.locale-en.js\" type=\"text/javascript\"></script>\n");
      out.write("        <script src=\"../js/jquery.jqGrid.min.js\" type=\"text/javascript\"></script>\n");
      out.write("        <script src=\"../js/commandLineParser.js\" type=\"text/javascript\"></script>\n");
      out.write("        <script type=\"text/javascript\" src=\"../js/jsonrpc.js\"></script>\n");
      out.write("        <script type=\"text/javascript\" src=\"../swfupload/swfupload.js\"></script>\n");
      out.write("        <script type=\"text/javascript\" src=\"../swfupload/swfupload.cookies.js\"></script>\n");
      out.write("        <script type=\"text/javascript\" src=\"../js/swfupload.queue.js\"></script>\n");
      out.write("        <script type=\"text/javascript\" src=\"../js/fileprogress.js\"></script>\n");
      out.write("        <script type=\"text/javascript\" src=\"../js/handlers.js\"></script>\n");
      out.write("\n");
      out.write("<link type=\"text/css\" href=\"");
      out.print(request.getContextPath());
      out.write("/css/scienceworkflow.css\" rel=\"stylesheet\" />\n");
      out.write("<script type=\"text/javascript\" src=\"../js/jsonrpc.js\"></script>\n");
      out.write("\n");
      out.write("<script>\n");
      out.write("  var selectedAppName = null;\n");
      out.write("  var jsonrpc;\n");
      out.write("\n");
      out.write("\n");
      out.write("$().ready(function(){\n");
      out.write("\n");
      out.write(" jsonrpc = new JSONRpcClient(\"");
      out.print(request.getContextPath());
      out.write("/Old-JSONRPC\");\n");
      out.write("\n");
      out.write(" $(\"button\").button();\n");
      out.write("\n");
      out.write(" $(\".jtable th\").each(function(){\n");
      out.write(" \n");
      out.write("  $(this).addClass(\"ui-state-default\");\n");
      out.write(" \n");
      out.write("  });\n");
      out.write(" $(\".jtable td\").each(function(){\n");
      out.write(" \n");
      out.write("  $(this).addClass(\"ui-widget-content\");\n");
      out.write(" \n");
      out.write("  });\n");
      out.write(" $(\".jtable tr\").hover(\n");
      out.write("     function()\n");
      out.write("     {\n");
      out.write("      $(this).children(\"td\").addClass(\"ui-state-hover\");\n");
      out.write("     },\n");
      out.write("     function()\n");
      out.write("     {\n");
      out.write("      $(this).children(\"td\").removeClass(\"ui-state-hover\");\n");
      out.write("     }\n");
      out.write("    );\n");
      out.write(" //$(\".jtable tr\").click(function(){\n");
      out.write("   //$(this).children(\"td\").toggleClass(\"ui-state-highlight\");\n");
      out.write("  //});\n");
      out.write("}); \n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("  function displayScriptApps(appName){\n");
      out.write("    // remove the old selection\n");
      out.write("    if ( appName == selectedAppName )\n");
      out.write("         return;\n");
      out.write("    scriptTable.deleteRows(scriptTable.getRecordSet().getLength() - 1, -1* scriptTable.getRecordSet().getLength());\n");
      out.write("    var result = jsonrpc.AppMgr.showAllScripts(appName);\n");
      out.write("    for( var i=0; i<result.list.length; i++){\n");
      out.write("             var newparam = new Object();\n");
      out.write("             newparam.name = result.list[i].scriptName;\n");
      out.write("             newparam.version = result.list[i].version;\n");
      out.write("             newparam.checkInTime = \"null\";\n");
      out.write("             newparam.byuser = \"null\";\n");
      out.write("             scriptTable.addRow(newparam, i);\n");
      out.write("    }\n");
      out.write("    scriptTable.render();\n");
      out.write("    selectedAppName = appName;\n");
      out.write("  }\n");
      out.write("\n");
      out.write("  function onStatusChange(el)\n");
      out.write("  {\n");
      out.write("      var record = myDataTable.getRecord(el);\n");
      out.write("      var appName = record.getData(\"name\");\n");
      out.write("      var isEnabled = el.checked;\n");
      out.write("      if( isEnabled ) {\n");
      out.write("         jsonrpc.AppMgr.deployApplication(appName);\n");
      out.write("      } \n");
      out.write("      else {\n");
      out.write("         jsonrpc.AppMgr.disableApplication(appName);\n");
      out.write("      }\n");
      out.write("  }\n");
      out.write("\n");
      out.write("\n");
      out.write("   // CellEditor function\n");
      out.write("  function updateAppStatus(callback, newValue){\n");
      out.write("   var record = this.getRecord();\n");
      out.write("   var oldValue = this.value;\n");
      out.write("   var column = this.getColumn();\n");
      out.write("   var appName = record.getData(\"name\");\n");
      out.write("   if ( oldValue != newValue && newValue == 'deployed') {\n");
      out.write("        jsonrpc.AppMgr.deployApplication(appName);\n");
      out.write("   }\n");
      out.write("   callback(true, newValue);\n");
      out.write("  }\n");
      out.write("\n");
      out.write("   \n");
      out.write("   function saveUpdate (){\n");
      out.write("     /** for(i=0; i<changes.modified.length; i++){\n");
      out.write("              var modified = changes.modified[i];\n");
      out.write("              if (modified.status == 'deployed')\n");
      out.write("                 jsonrpc.AppMgr.deployApplication(modified.name);\n");
      out.write("     } **/\n");
      out.write("\n");
      out.write("     // handle the deleted script application\n");
      out.write("    }\n");
      out.write("\n");
      out.write("    function onAdd()\n");
      out.write("    {\n");
      out.write("       console.log(\"in onAdd\")\n");
      out.write("       $(window.location).attr('href', 'appbuilder.jsp');\n");
      out.write("\n");
      out.write("    }\n");
      out.write("    function onEdit(appName)\n");
      out.write("    {\n");
      out.write("       console.log(\"in onEdit\")\n");
      out.write("       $(window.location).attr('href', 'appbuilder.jsp?action=edit&application='+appName);\n");
      out.write("    }\n");
      out.write("    function onEnable(el,appName)\n");
      out.write("    {\n");
      out.write("       jsonrpc = new JSONRpcClient(\"");
      out.print(request.getContextPath());
      out.write("/Old-JSONRPC\");\n");
      out.write("       if( el.checked )\n");
      out.write("           jsonrpc.AppMgr.deployApplication(appName)\n");
      out.write("       else\n");
      out.write("           jsonrpc.AppMgr.disableApplication(appName)\n");
      out.write("    }\n");
      out.write("    function onDelete(id,appName)\n");
      out.write("    {\n");
      out.write("        $(id).closest('tr').children(\"td\").toggleClass(\"ui-state-highlight\");\n");
      out.write("\tmsg='Delete application '+appName+'?'\n");
      out.write("\t$( \"#dialog-confirm\" ).attr('title',msg)\n");
      out.write("\t$( \"#dialog-confirm\" ).dialog({\n");
      out.write("\t\tresizable: false,\n");
      out.write("\t\twidth:'500px',\n");
      out.write("\t\theight:'auto',\n");
      out.write("\t\tmodal: true,\n");
      out.write("\t\tbuttons: {\n");
      out.write("\t\t\t\"Delete\": function() {\n");
      out.write("\t\t\t\t$( this ).dialog( \"close\" );\n");
      out.write("                    jsonrpc.AppMgr.removeApplication(appName)\n");
      out.write("                    jQuery(id).closest('tr').fadeOut()\n");
      out.write("\t\t\t},\n");
      out.write("\t\t\tCancel: function() {\n");
      out.write("\t\t\t\t$( this ).dialog( \"close\" );\n");
      out.write("                    $(id).closest('tr').children(\"td\").toggleClass(\"ui-state-highlight\");\n");
      out.write("\t\t\t}\n");
      out.write("\t\t}\n");
      out.write("\t});\n");
      out.write("        \n");
      out.write("    }\n");
      out.write("</script>\n");
      out.write("\n");
      out.write("\n");
      out.write("</head>\n");
      out.write("<body class=\"ui-widget\">\n");
      out.write("  ");
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
      out.write("<button style=\"float:right;margin-right:20%\" onClick=\"javascript:onAdd()\" style=>Add Application</button>\n");
      out.write("<div style=\"clear:both\"></div>\n");
      out.write("<table class=\"jtable sw-table\">\n");
      out.write("<tr><th>Name</th><th>User</th><th>Enabled</th><th>Actions</th></tr>\n");

    final Comparator<Application> APPLICATION_ALPHA_ORDER =
                                   new Comparator<Application>() {
        public int compare(Application e1, Application e2) {
            return e1.getAppName().compareTo(e2.getAppName());
        }
    };

    DAOFactory daoFactory = DAOFactory.instance(DAOFactory.class);
    ApplicationDAO appdao = daoFactory.getAppDAO();
    List<Application>res = appdao.getAllApplications();
    Collections.sort( res, APPLICATION_ALPHA_ORDER );
    Iterator<Application> iterator = res.iterator();
    while ( iterator.hasNext() ){
       Application app = iterator.next();
       
      out.write("\n");
      out.write("       <tr><td>");
      out.print(  app.getAppName() );
      out.write("</td><td>");
      out.print( app.getCreator() );
      out.write("</td><td align=center><input type=\"checkbox\" onClick=\"javascript:onEnable(this,'");
      out.print(app.getAppName());
      out.write("')\" ");
 if( app.getStatus().equals("deployed") ) { 
      out.write("checked=\"checked\"");
 } 
      out.write(" </td><td align=right><button onClick=\"javascript:onEdit('");
      out.print(app.getAppName());
      out.write("')\">Edit</button><button onClick=\"javascript:onDelete(this,'");
      out.print(app.getAppName());
      out.write("')\">Delete</button></td></tr>\n");
      out.write("       ");

    }


      out.write("\n");
      out.write("</table>\n");
      out.write("\n");
      out.write("<div id=\"dialog-confirm\" title=\"Delete this item?\" style=\"display:none\">\n");
      out.write("\t<p><span class=\"ui-icon ui-icon-alert\" style=\"float:left; margin:0 7px 20px 0;\"></span>This item will be permanently deleted and cannot be recovered. Are you sure?</p>\n");
      out.write("</div>\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n");
      out.write("\n");
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
