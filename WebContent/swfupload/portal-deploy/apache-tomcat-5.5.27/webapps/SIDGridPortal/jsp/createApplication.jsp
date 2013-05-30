<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<html>
<head>


<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/jquery-ui.min.js"></script>
<link type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.0/themes/cupertino/jquery-ui.css" rel="stylesheet" />

<link href="<%=request.getContextPath()%>/css/scienceworkflow.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/css/swfupload.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.7.0/build/reset-fonts-grids/reset-fonts-grids.css" />
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.7.0/build/container/assets/skins/sam/container.css" />
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.7.0/build/resize/assets/skins/sam/resize.css" />
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.7.0/build/layout/assets/skins/sam/layout.css" />
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.7.0/build/button/assets/skins/sam/button.css" />
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.0r4/build/paginator/assets/skins/sam/paginator.css" />
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.0r4/build/datatable/assets/skins/sam/datatable.css" />

<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/event/event-min.js"></script>

<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/dom/dom-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/element/element-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/dragdrop/dragdrop-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/container/container-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/resize/resize-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/animation/animation-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/layout/layout-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/datatable/datatable-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/connection/connection-min.js"></script>


 <script type="text/javascript" src="../swfupload/swfupload.js"></script>
 <script type="text/javascript" src="../js/swfupload.queue.js"></script>
 <script type="text/javascript" src="../js/fileprogress.js"></script>
 <script type="text/javascript" src="../js/handlers.js"></script>
 <script type="text/javascript" src="../js/yui-app-builder.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jsonrpc.js"></script>
<script type=text/javascript src="<%=request.getContextPath()%>/js/commandLineParser.js"></script>

<script type="text/javascript">
     sessionID = "<%= request.getSession().getId() %>";

     var Dom = YAHOO.util.Dom,
     Event = YAHOO.util.Event,
     layout = null,
     resize = null;
     var commandlinedialog = null;

     Event.onDOMReady(function() {


       
          //layout = new YAHOO.widget.Layout( {
                    //minWidth: 1000,
                    //units: [
                        //{ position: 'top', height: 28, body: 'menu', scroll: null, zIndex: 2 },
                        ////{ position: 'left', header: 'application description', body: 'appdesc', gutter: '2px', width: 200, collapse:true  }, 
                        //{ position: 'center',  header: 'parameter list', width: 800, resize: true, body: 'appdef', gutter: '0 5 0 2', minWidth: 600, maxWidth: 800 },
//
                    //]
                //});
       //layout.render();

        commandlinedialog = new YAHOO.widget.Dialog("commandlinedialog",
                                                { width : "auto",
                                                          fixedcenter : true,
                                                          hideaftersubmit : true,
                                                          visible : false,
                                                          close : true,
                                                          constraintoviewport : true,
                                                          buttons : [ { text:"Done", handler:handleCommandLineSubmit, isDefault:true },
                                                            { text:"Cancel", handler:handleCommandLineCancel } ]
                                                });

       YAHOO.widget.DataTable.prototype.deleteRowsBy = function (condition) {
				var start = 0, count = 0, current = 0, 
					recs = this.getRecordSet().getRecords();
				
				while (current < recs.length) {
					if (condition(recs[current].getData())) {
						if (count === 0) {
							start = current;
							//log('got one to delete at ' + start);
						} else {
							//log('got 1 more to delete');
						}
						count++;
						current++;
					} else {
						if (count) {
							//log('deleting ' + count + ' rows starting at ' + start);
							recs.splice(start, count);
							count = 0;
							current = start;
						} else {
							current++;
						}
					}
				}
				if (count) {
					//log('deleting ' + count + ' rows starting at ' + start);
					recs.splice(start, count);
				}
				//log('rendering');
				this.render();
			};

       init();
    });

    function parseCommandLine() {
      cmdline = document.getElementById('cmdline').value
//console.log("cmdline = " + cmdline )
      parsed = parseCommandLineArgsTypesEx2(cmdline)
      cmd = parsed['cmd']
      document.getElementById('name').value = cmd
      //document.getElementById('title').value = cmd
      document.getElementById('command').value = cmd

      arglist = parsed['list']
      loadParamToTable(arglist,myDataTable)
    }

    function handleCommandLineSubmit() {
       //console.log("command line submitted")
       parseCommandLine()
       this.cancel()
    }

    function handleCommandLineCancel() {
       ////console.log("command line cancelled")
       this.cancel()
    }

    function enterCommandLine() {
        commandlinedialog.render(YAHOO.util.Dom.get('appdef'));
        commandlinedialog.cfg.setProperty("visible", true);
        document.getElementById('cmdline').focus()

    }

    // define a group of variables for the application builder
  jsonrpc = new JSONRpcClient("<%=request.getContextPath()%>/Old-JSONRPC");
 
  //console.log("created json store");


 </script>

</head>
<body class=" yui-skin-sam" style="text-align:left">
<%@ include file="nav.html" %>
 
<div id="appdef" class="sw-main ui-widget">

   <div id="gadget-preview" class="yui-pe-content">
      <div class="bd"></div>
   </div>


   <div id="appdesc">
   <br/><br/>
        <div class="ui-widget-header">Define Application </div>
        <div class="ui-widget-content">
        <div style="width:300px">
     	<table col="2">
    	<tr>
      		<td>Name </td>
      		<td><input type="text" class="text" id = "name" name="name" size="30"  /> </td>
       </tr>
<!--
       <tr>
    	    	<td>Title </td>
      		<td><input type="text" class="text" id = "title" name="title" size="30"  /> </td>
       </tr>
-->
       <tr>
     		<td>Command </td>
      		<td><input type="text" class="text" id = "command" name="command" size="30" /> </td>
       </tr>
<!--
       <tr>
    	       <td>Version </td>
      		<td><input type="text" class="text" id = "version" name="version" size="30" /> </td>
   	</tr>
    	<tr>
      		<td>Web Site </td>
      		<td><input type="text" class="text" name="web site" size="30" /> </td>
   	</tr>
-->
   	
       </table>
       <div style="float:right;text-align:right">
         <a href="javascript:openApplication();">Load existing application</a><br>
         <a href="javascript:enterCommandLine();">Enter command line</a>
       </div>
       <div style="clear:both"></div>
       </div>
       </div>
   </div>


<br/>

<script type="text/javascript">

function addRow() { myDataTable.addRow({}); }

function goToApplication() {  hideNotification() } 

</script>

<div class="ui-widget-header">Parameter Definitions</div>
<div class="ui-widget-content">
  <div style="font-size:77%">Define the parameters for your application.</div>
  <div id="paralist"></div>
  <a onClick="addRow()" style="padding:5px;" href="#" class="insert-button" alt="Add row">&nbsp;&nbsp;</a>
  <button onClick="createXML()" >Preview Application</button>
</div>
<br/>

<div class="ui-widget-header">Application Preview</div>
<div class="ui-widget-content">
  <div style="font-size:77%">This is how your application will appear to users. Adjust the parameters and preview the application until you're satisfied, then create the application.</div>
  <div id="placeholder" style="width:300px;margin:10px"><div class="ui-state-highlight"><br/><span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-alert"></span>Application preview not yet generated.<br/><br/></div>
<div style="clear:both"></div>
</div>
<div style="clear:both"></div>
</div>
<br/>
<div id="buttons">
   <button onClick="commitApplication()">Create Application</button>
</div>

<div style="clear:both"></div>

<div id="notification" style="font-size:12px;display:none; position: absolute; width: 600px; height: 30px;margin-left:auto;margin-right:auto; top: 10px; padding: 10px;line-height:2em;" class="ui-widget ui-widget-content ui-corner-all ui-state-highlight"><div id="msg" style="float:left"></div>
	<div style="float:right"><a href=javascript:hideNotification()>Close</a>		</div>
</div>





</div>

<div id="dialog1" class="yui-pe-content">
    <div class="hd">Open Application</div>
    <div id="apptable"> </div>
</div>

<div id="commandlinedialog" class="yui-pe-content">
     <div class="hd">Command Line Dialog</div>
     <div class="bd">
       <div id="commandlineblock" style="display:hidden">
       Type the command line just as you would execute it in a shell to have it parsed into the table below.
       <p>
       <input id='cmdline' type=text onkeydown="if (event.keyCode == 13) { parseCommandLine(); commandlinedialog.cancel(); } " size=80 value="app -a 1 -b 2 --longarg='longarg' --longarg2"></input>
     </div>
</div>









</body>

