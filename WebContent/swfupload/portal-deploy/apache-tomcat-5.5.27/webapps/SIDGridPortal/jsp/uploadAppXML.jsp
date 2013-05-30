<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>
<link type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.0/themes/cupertino/jquery-ui.css" rel="stylesheet" />

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/scienceworkflow.css" />


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
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.1/build/uploader/uploader-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.0r4/build/datatable/datatable-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.1/build/paginator/paginator-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/connection/connection-min.js"></script>


 <script type="text/javascript" src="../swfupload/swfupload.js"></script>
 <script type="text/javascript" src="../swfupload/swfupload.cookies.js"></script>
 <script type="text/javascript" src="../js/swfupload.queue.js"></script>
 <script type="text/javascript" src="../js/fileprogress.js"></script>
 <script type="text/javascript" src="../js/handlers.js"></script>
 <script type="text/javascript" src="../js/yui-script-builder.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jsonrpc.js"></script>

<script type="text/javascript">
     sessionID = "<%= request.getSession().getId() %>";
     var upload1;
     var jsonrpc;

     var Dom = YAHOO.util.Dom,
     Event = YAHOO.util.Event,
     layout = null,
     resize = null;

     Event.onDOMReady(function() {
       
          //layout = new YAHOO.widget.Layout( {
                    //minWidth: 1000,
                    //units: [
                        //{ position: 'top', height: 28, body: 'menu', scroll: null, zIndex: 2 },
                        //{ position: 'left', header: 'script  description', body: 'appdesc', gutter: '2px', width: 200, collapse:true  }, 
                        //{ position: 'center',  header: 'parameter list', width: 800, resize: true, body: 'appdef', gutter: '0 5 0 2', minWidth: 600, maxWidth: 800 },
//
                    //]
                //});
       //layout.render();

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
var user = "<%=session.getAttribute("username") %>";
       upload1 = loadSwfUploader(user);
       uploadSettings = upload1.settings
       document.getElementById('maxFileSize').innerHTML = uploadSettings.file_size_limit;

       //var uploader = new YAHOO.widget.Uploader( "uploaderOverlay" );
       	

       listDefinedApps();
    });

    jsonrpc = new JSONRpcClient("../Old-JSONRPC");

    // define a group of variables for the application builder
  
  //console.log("created json store");


</script>

</head>
<body class=" yui-skin-sam" style="text-align:left">
<%@ include file="nav.html" %>

 
 <div id="appdef" class="sw-main ui-widget" >
   <div id="gadget-preview" class="yui-pe-content">
      <div class="bd"></div>
   </div>



  <div id="appdesc" >
   <br/><br/>

        <div class="ui-widget-header">Define Application </div>
        <div class="ui-widget-content">
        <div style="width:300px">


        <table col="2">
       <tr>
          <td>Name</td>
          <td>
             <select id="defined-app-list">             </select>
          </td>
        <tr>
            <td>Script Name</td>
            <td><input type="text" class="text" id = "name" name="name" size="10"  /> </td>
       </tr>
<!--
       <tr>
                <td>Title</td>
            <td><input type="text" class="text" id = "title" name="title" size="10"  /> </td>
       </tr>
-->
<!-- 
       <tr>
            <td>Command</td>
            <td><input type="text" class="text" id = "command" name="command" size="10" /> </td>
       </tr>
-->
       <tr>
               <td>Version</td>
            <td><input type="text" class="text" id = "version" name="version" size="10" /> </td>
    </tr>
<!--
        <tr>
            <td>Website</td>
            <td><input type="text" class="text" name="web site" size="10" /> </td>
    </tr>
-->
       </table>
       <div style="float:right;text-align:right">
         <a href="javascript:openApplication();">Load existing application</a><br>
       </div>
      <div style="clear:both"></div>
      </div>
      </div>
   </div>


<script type="text/javascript">

function addRow() { myDataTable.addRow({}); }

</script>


<br/>


<div class="ui-widget-header">Parameter Definitions</div>
<div class="ui-widget-content">
  <div style="font-size:77%">Define the parameters for your application.</div>


   
   <div id="paradef">
<!-- hide pagination controls, who need em? -->
    <div id="dt-pag-nav" style='display:none'> </div>
    <div id="paralist">
    </div>
  <a onClick="addRow()" style="padding:5px;" href="#" class="insert-button" alt="Add row">&nbsp;&nbsp;</a>

   </div>
</div>   

<br/>
<div class="ui-widget-header">Application Files</div>
<div class="ui-widget-content">
  <div style="font-size:77%">
     Upload the script package files for your application here (max file size <span id="maxFileSize"></span> kB). </div>


   <div id="upload">
    
    <table>
    <tbody>
    <tr><td>
    <div id="uploadFileTable" class="uploadTable" style='float:left'></div><div style='float:left' id='uploadFileTableAlert'></div>
     <div id="flashUI1">
	<div class="fieldset flash" id="fsUploadProgress1" style="display:none">
		<span class="legend">Upload Script Package Files</span>
	</div>
      <div style="padding-left: 5px;">
    	   <span id="spanButtonPlaceholder1"></span>
    	  <input id="btnCancel1" type="button" value="Cancel Uploads" onclick="cancelQueue(upload1);" disabled="disabled" style="margin-left: 2px; height: 22px;     font-size: 8pt;" />
     </div>
    </td><td>
   </div>
    <input type="hidden" name="mobyle_infile"  id="mobyle_infile">
   </div>
   </td></tr>
   </tbody>
   </table>   
    <button onClick="createXML()">Preview Application</button>

</div>

</div>

<br/>

<div class="ui-widget-header">Application Preview</div>
<div class="ui-widget-content">
  <div style="font-size:77%">This is how your application will appear to users. Adjust the parameters and preview the application until you're satisfied, then create the application.</div>
  <div id="placeholder" style="width:300px;margin:10px"><div class="ui-state-highlight"><br/><span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-alert"></span>Application preview not yet generated.<br/><br/></div></div>
</div>


<br/>
   <div id="buttons">
    <button onClick="commitScript()">Create Application</button>
 </div>

 </div>


<div id="notification" style="font-size:12px;display:none; position: absolute; width: 600px; height: 30px;margin-left:200px;margin-right:auto; top: 10px; padding: 10px;line-height:2em" class="ui-widget ui-widget-content ui-corner-all ui-state-highlight"><div id="msg" style="float:left"></div>    <div style="float:right"><a href=javascript:hideNotification()>Close</a>        </div>
</div>



 <div id="dialog1" class="yui-pe-content">
    <div class="hd">Open Application</div>
    <div id="apptable"> </div>
 </div>


</body>
