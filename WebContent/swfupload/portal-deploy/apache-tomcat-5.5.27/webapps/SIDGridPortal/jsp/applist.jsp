<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="org.uc.sidgrid.dao.*" %>
<%@ page import="org.uc.sidgrid.app.*" %>
<%@ page import="java.util.*" %>

<html>
<head>
<title>Application Administration</title>

<style>
   .sw-table { 
      margin-left:auto;
      margin-right:auto;
      width:60%;
      padding:2px;
   }

</style>

        <link rel="stylesheet" type="text/css" media="screen" href="../css/ui.jqgrid.css" />
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/jquery-ui.min.js"></script>
        <link type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/themes/cupertino/jquery-ui.css" rel="stylesheet" />

        <link href="../swfupload/css/default.css" rel="stylesheet" type="text/css" />
        <script src="../js/grid.locale-en.js" type="text/javascript"></script>
        <script src="../js/jquery.jqGrid.min.js" type="text/javascript"></script>
        <script src="../js/commandLineParser.js" type="text/javascript"></script>
        <script type="text/javascript" src="../js/jsonrpc.js"></script>
        <script type="text/javascript" src="../swfupload/swfupload.js"></script>
        <script type="text/javascript" src="../swfupload/swfupload.cookies.js"></script>
        <script type="text/javascript" src="../js/swfupload.queue.js"></script>
        <script type="text/javascript" src="../js/fileprogress.js"></script>
        <script type="text/javascript" src="../js/handlers.js"></script>

<link type="text/css" href="<%=request.getContextPath()%>/css/scienceworkflow.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jsonrpc.js"></script>

<script>
  var selectedAppName = null;
  var jsonrpc;


$().ready(function(){

 jsonrpc = new JSONRpcClient("<%=request.getContextPath()%>/Old-JSONRPC");

 $("button").button();

 $(".jtable th").each(function(){
 
  $(this).addClass("ui-state-default");
 
  });
 $(".jtable td").each(function(){
 
  $(this).addClass("ui-widget-content");
 
  });
 $(".jtable tr").hover(
     function()
     {
      $(this).children("td").addClass("ui-state-hover");
     },
     function()
     {
      $(this).children("td").removeClass("ui-state-hover");
     }
    );
 //$(".jtable tr").click(function(){
   //$(this).children("td").toggleClass("ui-state-highlight");
  //});
}); 




  function displayScriptApps(appName){
    // remove the old selection
    if ( appName == selectedAppName )
         return;
    scriptTable.deleteRows(scriptTable.getRecordSet().getLength() - 1, -1* scriptTable.getRecordSet().getLength());
    var result = jsonrpc.AppMgr.showAllScripts(appName);
    for( var i=0; i<result.list.length; i++){
             var newparam = new Object();
             newparam.name = result.list[i].scriptName;
             newparam.version = result.list[i].version;
             newparam.checkInTime = "null";
             newparam.byuser = "null";
             scriptTable.addRow(newparam, i);
    }
    scriptTable.render();
    selectedAppName = appName;
  }

  function onStatusChange(el)
  {
      var record = myDataTable.getRecord(el);
      var appName = record.getData("name");
      var isEnabled = el.checked;
      if( isEnabled ) {
         jsonrpc.AppMgr.deployApplication(appName);
      } 
      else {
         jsonrpc.AppMgr.disableApplication(appName);
      }
  }


   // CellEditor function
  function updateAppStatus(callback, newValue){
   var record = this.getRecord();
   var oldValue = this.value;
   var column = this.getColumn();
   var appName = record.getData("name");
   if ( oldValue != newValue && newValue == 'deployed') {
        jsonrpc.AppMgr.deployApplication(appName);
   }
   callback(true, newValue);
  }

   
   function saveUpdate (){
     /** for(i=0; i<changes.modified.length; i++){
              var modified = changes.modified[i];
              if (modified.status == 'deployed')
                 jsonrpc.AppMgr.deployApplication(modified.name);
     } **/

     // handle the deleted script application
    }

    function onAdd()
    {
       console.log("in onAdd")
       $(window.location).attr('href', 'appbuilder.jsp');

    }
    function onEdit(appName)
    {
       console.log("in onEdit")
       $(window.location).attr('href', 'appbuilder.jsp?action=edit&application='+appName);
    }
    function onEnable(el,appName)
    {
       jsonrpc = new JSONRpcClient("<%=request.getContextPath()%>/Old-JSONRPC");
       if( el.checked )
           jsonrpc.AppMgr.deployApplication(appName)
       else
           jsonrpc.AppMgr.disableApplication(appName)
    }
    function onDelete(id,appName)
    {
        $(id).closest('tr').children("td").toggleClass("ui-state-highlight");
	msg='Delete application '+appName+'?'
	$( "#dialog-confirm" ).attr('title',msg)
	$( "#dialog-confirm" ).dialog({
		resizable: false,
		width:'500px',
		height:'auto',
		modal: true,
		buttons: {
			"Delete": function() {
				$( this ).dialog( "close" );
                    jsonrpc.AppMgr.removeApplication(appName)
                    jQuery(id).closest('tr').fadeOut()
			},
			Cancel: function() {
				$( this ).dialog( "close" );
                    $(id).closest('tr').children("td").toggleClass("ui-state-highlight");
			}
		}
	});
        
    }
</script>


</head>
<body class="ui-widget">
  <%@ include file="nav.html" %>
<button style="float:right;margin-right:20%" onClick="javascript:onAdd()" style=>Add Application</button>
<div style="clear:both"></div>
<table class="jtable sw-table">
<tr><th>Name</th><th>User</th><th>Enabled</th><th>Actions</th></tr>
<%
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
       %>
       <tr><td><%=  app.getAppName() %></td><td><%= app.getCreator() %></td><td align=center><input type="checkbox" onClick="javascript:onEnable(this,'<%=app.getAppName()%>')" <% if( app.getStatus().equals("deployed") ) { %>checked="checked"<% } %> </td><td align=right><button onClick="javascript:onEdit('<%=app.getAppName()%>')">Edit</button><button onClick="javascript:onDelete(this,'<%=app.getAppName()%>')">Delete</button></td></tr>
       <%
    }

%>
</table>

<div id="dialog-confirm" title="Delete this item?" style="display:none">
	<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>This item will be permanently deleted and cannot be recovered. Are you sure?</p>
</div>



</body>
</html>

