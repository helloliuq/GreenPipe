<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<title>Application Builder</title>
        
        <!-- this one needs to be removed, but I'll have to incorporate some of the styles in it first -->
		<!-- <link rel="stylesheet" href="../css/examples.css" type="text/css" media="screen" charset="utf-8" /> -->
        
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

        
        <style>
            .cell-title {
                font-weight: bold;
            }
            .cell-effort-driven {
                text-align: center;
            }
            .dialog-target {
                cursor:pointer;
            }
            .method {
                float:left;
                width:20%;
                padding:5px;
                /* margin-left:5px; */
            }
            label {
                float:left;
            }
            
            fieldlabel {
                display:block;
                width:95%; 
                color:gray;
                margin: 5px 10px 0px;
                padding: 2px;
                font-size:62%;
            }
            .parameter {
                margin: 0px 10px;
                padding: 5px;
                width:95%;
            }
            .parameter.text { 
                margin-bottom:12px; 
                width:95%; 
                padding: 5px; 
                margin:0px 10px; 
            }
            
            .progressWrapper {
                float:left;
                width:100%;
            }
            .progressCancel {
                background-image: url("../images/delete.gif")
            }
            /* .ui-widget-header { padding-left:5px; padding-right:5px } */
            /* .ui-widget-content { padding:5px; } */

            .header { text-align:center;width:250px;float:left;height:75px;margin:20px 10px;padding-top:10px;padding-bottom:10px;border:1px solid lightgray; }
            //.margin-fix { margin:0px 20px;padding:0px 20px; }
            .stepnumber { float:left; width:25px }
            
            
            
            
            
        </style>
    
        <script>
            var DefaultApplicationName='Your Application'
            var sessionID = "<%= request.getSession().getId() %>";
            var pathtojsfiles = "../js/"
            var parameterTable;
            var action='<%= request.getParameter("action") %>'
            var application='<%= request.getParameter("application") %>'
            var appResponse;

            var commandLineDialog;
            var uploadDialog;
            var uploader;
            var lastsel; 
            var mode;
            var DefaultMode='commandline'
            var uploadedFiles = new Object()
            var jsonrpc;
            var swiftScript;

            function showNotification(msg) {
                $("#notification #msg").html(msg)  
                $("#notification").removeClass('ui-state-error').addClass('ui-state-highlight').show()
            }
            function hideNotification() {  $("#notification").fadeOut() }
            function showError(msg) {
                $("#notification #msg").html(msg)
                $("#notification").removeClass('ui-state-highlight').addClass('ui-state-error').show() 
            }
            function hideError() {  $("#notification").fadeOut() }

            function showCommandLineDialog()
            {
                mode = 'commandline'
                commandLineDialog = $("#commandLineDialog").dialog({position:[10,80],width:350,title:'Command line dialog',minHeight:100});
                $('#cmdline').focus()
            }
            function handleCommandLine() 
            {
                parseCommandLine(); 
                applicationNameUpdate()
                previewApplication()
            }



function  MyFileQueueError(file, errorCode, message) {
    //console.log("my upload queue error " + file + errorCode + message )
    var newfile = new Object();
    newfile.name = file.name;          
    newfile.size = message + "(" + file.size + " bytes)"
 //       FileUploaderTable.addRow(newfile); 
}


function  MyUploadStart(file) {
    //console.log("my upload start " + file )
    this.refreshCookies()
    this.uploadStart(file)


} 

function  MyUploadError(file, serverData) {
    //console.log("upload error " + file.name + ", " + serverData )
    var newfile = new Object();
    newfile.name = file.name;
    newfile.size = "X";
//          FileUploaderTable.addRow(newfile);
 }


function  MyUploadSuccess(file, serverData) {
    //console.log("upload success: " + file.name )
    //jQuery('#filelist_placeholder').html("")
    //fileUrl = serverData.substring(7)
    //uploadedFiles.push(fileUrl)
   // uploadedFiles[file.id] = fileUrl
   //return true;
   fileUrl = serverData.substring(7)
    uploadedFiles[file.id] = fileUrl
    if( file.name.match(/swift$/) ) {
        //console.log("You uploaded a swift file, it will be parsed")
        //var paralist = jsonrpc.AppMgr.guessParameters(sessionID, fileUrl).list;
    }
}

function ParseSwiftScript(url)
{
    //console.log("parse swift script url = " + url )
    parts = url.split("/")
    swiftScript = parts[parts.length-1];
    var paralist = jsonrpc.AppMgr.guessParameters(sessionID, url).list;
    parameterTable.clearGridData()
    loadParamToTable(paralist, parameterTable);
}


function MyUploadSuccess2(file,serverData,receivedResponse) {
    try {
        //console.log("MyUploadSuccess2 file " + file.name +"|"+serverData+"|"+receivedResponse)
        var progress = new FileProgress(file, this.customSettings.progressTarget);
        if (serverData.substring(0, 7) === "FILEID:") {
          progress.setComplete();
          progress.setStatus("Complete.");
          progress.toggleCancel(true);
          // set the value of the input field
          var url = serverData.substring(7);
          //alert(url);
          //var fileInput = document.getElementById(this.customSettings.fileInputTarget);
          //fileInput.value=url;
          fileUrl = serverData.substring(7)
        //console.log("server data = " + serverData )
        //console.log("fileUrl = " + fileUrl )
        //console.log("url = " + url )
          uploadedFiles[file.id] = fileUrl
          if( file.name.match(/swift$/) ) {
              //console.log("You uploaded a swift file")
              var link = document.createElement('a');
              //link.setAttribute('href', 'mypage.htm');
              link.setAttribute('href', "javascript:ParseSwiftScript(\""+fileUrl+"\")" );
              link.appendChild( document.createTextNode("Parse") )
              progress.fileProgressElement.appendChild(link)
              //var paralist = jsonrpc.AppMgr.guessParameters(sessionID, fileUrl).list;
          } else {
              //console.log("Guess it wasn't a swift file")
          }
        } else {
            //console.log("serverData: " + serverData )
            progress.setStatus("Error.");
            progress.toggleCancel(false);
            alert(serverData);
        }

    } catch (ex) {
            //console.log("exception: " + ex )
        this.debug(ex);
    }
}


function RemoveFile(file_id) { 
    delete uploadedFiles[file_id]
    jQuery("#"+file_id).fadeOut()
}

function  MyUploadComplete(file) {
    progress = document.getElementById(file.id)
    sel = "#" + file.id + " .progressCancel"
    jQuery(sel).attr("href", "javascript:RemoveFile(\""+file.id+"\")" )


    if( this.getFile() != null ) {
        this.startUpload()
    }
        return true
}


function MyFileQueued(file)
{
  //console.log("MyFileQueued:" + file.name )
  document.getElementById('filelist_placeholder').innerHTML=""

}

function uploadCancel()
{
//console.log("upload cancel")
}








            function createUploader(username) 
            {
   var upload = new SWFUpload({
                // Backend Settings
                upload_url: "/SIDGridPortal/fileupload?user=" + username,   // Relative to the SWF file (or you can use absolute paths)
                post_params: { "ChannelID": "28", "Type": "Type2", "sw_username":username },

                // File Upload Settings
                file_size_limit : "200",   
                file_types : "*.*",
                file_types_description : "All Files",
                file_upload_limit : "10",
                file_queue_limit : "0",
                //assume_success_timeout:5,

                // Event Handler Settings (all my handlers are in the Handler.js file)
                //file_dialog_start_handler : fileDialogStart,
                file_queued_handler : MyFileQueued,
                file_queue_error_handler : MyFileQueueError,
                file_dialog_complete_handler : fileDialogComplete,
                //upload_start_handler : uploadStart,
                upload_progress_handler : uploadProgress,
                upload_error_handler : MyUploadError,
                //upload_error_handler : uploadError,
                upload_complete_handler : MyUploadComplete,
                upload_success_handler : MyUploadSuccess2,
upload_cancel_callback : 'uploadCancel',
                // Button Settings
                button_image_url : "../swfupload/XPButtonUploadText_61x22.png",
                //button_text : "Select files...",
                button_placeholder_id : "spanButtonPlaceholder1",
                button_width: 75,
                button_height: 22,

                // Flash Settings
                flash_url : "../swfupload/swfupload.swf",
                custom_settings : {
                    progressTarget : "fsUploadProgress1",
                    cancelButtonId : "btnCancel1",
                    currentFileId  : "currentFileId",
                    fileInputTarget: "mobyle_infile",
                },

                // Debug Settings
                debug: false
        });

                return upload;
            }

            function setMode(mode_in)
            {
                mode = mode_in
                if( mode == 'swift' ) {
                    jQuery('label[for="command-line-radio"]').removeClass("ui-state-active") 
                    jQuery('#application-command-container').hide()
                    jQuery('#command-line-container').hide()
                    jQuery('#files').fadeIn()
                }
 
                if( mode == 'commandline' ) {
                    jQuery('label[for="swift-radio"]').removeClass("ui-state-active") 
                    jQuery('#files').hide()
                    jQuery('#application-command-container').fadeIn()
                    jQuery('#command-line-container').fadeIn()
                }
                
            }

            function showUploadForm()
            {
                mode = 'swift'
                uploader = createUploader('turam')
                uploadDialog = $("#uploadDialog").dialog({position:[10,80],width:300,
                                                    title:'Upload Files',minHeight:100,
                                                    close: function() {

                               jQuery('#upload_span_container').html(
                                ' <span id="fsUploadProgress1"></span> \
                                 <span id="spanButtonPlaceholder1"></span> \
                                  <input id="btnCancel1" type="button" value="Cancel Uploads" onclick="cancelQueue(uploader);" disabled="disabled"> '); } });
            }
            function parseCommandLine() {
              cmdline = document.getElementById('cmdline').value
              parsed = parseCommandLineArgsTypesEx3(cmdline)
              cmd = parsed['cmd']
              jQuery('#application-name').val(cmd)
              jQuery('#application-command').val(cmd)
              //document.getElementById('command').value = cmd

              arglist = parsed['list']
              parameterTable.clearGridData()
              loadParamToTable(arglist,parameterTable)
            }


            function applicationNameUpdate() {
                name = document.getElementById('application-name').value
                if( name === "" ) name = DefaultApplicationName
                jQuery('#preview-application-name').html(name)
            }
            function applicationDescriptionUpdate() {
                text = document.getElementById('application-description').value
                jQuery('#hack-preview-application-description').html(text)
            }
            function createArgFmt(ParamName, ParamType, ParamValue) {
                var argfmt = "";
                value = ParamValue;
                if( ! value ) value="value";
                switch (ParamType) {
                    case 0:
                      argfmt = "-"+ParamName;
                      break;
                    case 1:
                      argfmt = value;
                      break;
                    case 2:
                      argfmt = "-"+ParamName+"="+value;
                      break;
                    case 3:
                      argfmt = "-"+ParamName+" "+value;
                      break;
                    case 4:
                      argfmt = "--"+ParamName+"="+value;
                      break;
                    case 5:
                      argfmt = "--"+ParamName+" "+value;
                      break;
                    case 6:
                      argfmt = "--"+ParamName;
                      break;

                    }
                    //console.log("createArgFmt " + ParamName + ParamType + argfmt )
                    return argfmt;
             }
            function boolToOption(b) { 
                if( b==true ) return "Yes";
                if( b==false) return "No";
                return b;
            }
            function optionToBool(b) { 
                if( b=="Yes" ) return true;
                if( b=="No") return false;
                return b;
            }
            function loadParamToTable(paralist, targetTable){
                for(var i=0; i<paralist.length;i++){
                    var p=paralist[i];
                    p.input = boolToOption(p.input)  
                    p.out = boolToOption(p.out)  
                    p.isStdout = boolToOption(p.isStdout)  
                    p.option = createArgFmt(p.name,p.argType,p.value)
                    newRow={ Prompt:p.prompt,Option:p.option,DataType:p.dataType,Input:p.input,Output:p.out,Stdout:p.isStdout};
                    ret = targetTable.addRowData(i, newRow );
                }
            }

            function makeTable() {
                 parameterTable = jQuery("#rowed3").jqGrid({ 
                    url:'#', 
                    editurl:'#', 
                    width:800,
                    height:100,
                    datatype: "clientSide", 
                    colNames:['Prompt', 'Option','DataType','Input','Output','Stdout'], 
                    colModel:[ 
                    {name:'Prompt',index:'invdate', width:90, editable:true}, 
                    {name:'Option',index:'name', width:100,editable:true},
                    {name:'DataType',index:'datatype', width:50,editable:true,edittype:'select',
                        editoptions:{ dataUrl:'../jsp/datatypes.html' }
                        }, 
                    {name:'Input',index:'note2', width:30, sortable:false,editable:true,edittype:'checkbox'} , 
                    {name:'Output',index:'note3', width:30, sortable:false,editable:true,edittype:'checkbox'} , 
                    {name:'Stdout',index:'note4', width:30, sortable:false,editable:true,edittype:'checkbox'} 
                    ], 
                    rowNum:10, 
                    //rowList:[10,20,30], 
                    pager: '#prowed3', 
                    sortname: 'note', 
                    sortorder: "desc", 
                    loadComplete: function(data) { //console.log("in loadComplete" + data ) 
                                              },
                    //afterInsertRow: function(rowid,rowdata,rowelem) { console.log("in afterInsertData"); previewApplication()   },
                    serializeGridData: function(postData) { //console.log("in serializeGridData"); 
                                      return postData; },
                    onSelectRow: function(id){ 
                        if(1 || ( id && id!==lastsel)){ 
                                                jQuery("#rowed3").jqGrid('restoreRow',lastsel); 
                                                jQuery('#rowed3').jqGrid('editRow',id,true); 
                                                lastsel=id; 
                                                 } 
                        }, 
                    caption: "Application Parameters",
                    loadonce: true
                }); 
                
                ret = jQuery("#rowed3").addRowData(1, { Prompt:"Example Prompt",Option:"--option1=value","ArgType":"Integer",Input:"No",Output:"No",Stdout:"No"}  );
                ret = jQuery("#rowed3").addRowData(2, { Prompt:"Example Prompt",Option:"--option2=value","ArgType":"String",Input:"No",Output:"No",Stdout:"No"}  );
                jQuery("#rowed3").jqGrid('navGrid',
                     '#prowed3',
                     {edit:true,add:true,del:true,search:false,refresh:false,
                     delfunc: function(rowid) { 
                                 parameterTable.delRowData(rowid)
                     }
                 
                }); 
                jQuery('#prowed3_center').remove();
                jQuery('#prowed3_center').css('margin','50px');
                
                // how to programmatically select a row
                //jQuery("#rowed3").setSelection( 2,true)
                
            }
            
            function showAndPreviewApplication()
            {
                previewApplication()
                jQuery('#preview-container').show()
            }
            function previewApplication()
            {
                //jQuery('#preview-container').show()
                var name = document.getElementById("application-name").value;
                var command = document.getElementById("application-command").value;
                
                var params = new Object();
                params.javaClass = 'java.util.List';
                params.list = [];
                rowids = parameterTable.getDataIDs()
                //console.log("records = " + rowids )
                
                for(var i=0;i<rowids.length;i++)
                {
                    rowdata = parameterTable.getRowData(rowids[i]);
                    param = new Object;
                    param.javaClass = 'org.uc.sidgrid.app.AppParameter';
                    param.argpos = i
                    param.prompt = rowdata['Prompt']
                    paramobj = parseArgument2(rowdata['Option']);
                    param.name = paramobj['name']
                    param.value = paramobj['value']
                    param.argType = paramobj['argType']
                    param.dataType = rowdata['DataType']
                    param.input = optionToBool(rowdata['Input'])
                    param.out = optionToBool(rowdata['Output'])
                    param.isStdout = optionToBool(rowdata['Stdout'])
                    params.list[i] = param;
                }
                
                appResponse=jsonrpc.AppMgr.createMobyleXML(sessionID, name,command, name,params);
                jQuery('#preview-application-name').html(name);
                jQuery('#hack-preview-application-description').html(document.getElementById('application-description').value );
                jQuery.ajax({
                            url:        appResponse['gadgetURL'],
                            context:    document.body,
                            dataType: 	'text',
                            error:    function(request,textStatus,errorThrown) {
                               //console.log("textStatus = " + textStatus )
                               //console.log("errorThrown = " + errorThrown )
                            },
                            success:    function(data,textStatus,request) {
                                           //jQuery('#preview').html( data )
                                           document.getElementById('preview').innerHTML = data
                            }
                 })
            }
            function createCommandLineApplication() {
                var name = document.getElementById("application-name").value;
                if (name==null || name==""){
                    alert("Application Name needs to be specified");
                    // mark the name input field
                    return;
                }
                var xmlfile = appResponse['mobyleXMLURL']
                var res = jsonrpc.AppMgr.uploadMobyleXML(xmlfile);
                if (res.status != 0 ) 
                {
                    patt = /(cann)/g;
                    if( res.message.match(patt) ) {
                        showNotification("You must preview the application before it can be created.")
                        return
                    } 
                    showError(res.message);
                }
                else
                {
                    showNotification("Application "+res.appName+" created successfully.");
                    setTimeout("$(window.location).attr('href','applist.jsp')",5000)
                }
            }

            function createSwiftApplication() {
                var name = document.getElementById("application-name").value;
                if (name==null || name==""){
                    showError("Application Name needs to be specified");
                    return;
                }

                // create a zipped script pkg
                var zipFile = name+".zip";
                var fileUrl_list = new Object();
                fileUrl_list.javaClass = "java.util.List";
                fileUrl_list.list = [];
                for(var i=0; i<uploadedFiles.length;i++){
                   fileUrl_list.list[i]=uploadedFiles[i];
                }
                jsonrpc.AppMgr.createScriptPkg(sessionID, fileUrl_list, zipFile);

                // add the new script to the system
                var version = 1; //
                var mainScript = name+".swift";
                var xmlfile    = name+".xml"
                var res = jsonrpc.AppMgr.addNewScript(sessionID, name, name, version, swiftScript, xmlfile);
                if (res.status != 0 )
                    showError(res.message);
                else
                {
                    showNotification("Application "+res.appName+" created successfully.");
                    setTimeout("$(window.location).attr('href','applist.jsp')",5000)
                }

            }
            function createApplication() {
                previewApplication()

                if( mode ==='commandline' )	createCommandLineApplication()
                else if( mode==='swift')		createSwiftApplication()
            }
            function checkAndCreateApplication() {
                var appName = document.getElementById("application-name").value;
                appInfo = jsonrpc.AppMgr.getAppInfo(appName)
                //document.write(appName)
                document.write(appInfo)
                if( appInfo != null )
                {
                    msg='Replace existing application '+appName+'?'
                    $( "#dialog-confirm" ).attr('title',msg)
                    $( "#dialog-confirm p").text('This item will be overwritten and cannot be recovered. Are you sure?')
                	$( "#dialog-confirm" ).dialog({
                        resizable: false,
                        width:'500px',
                        height:'auto',
                        modal: true,
                        buttons: {
                            "Replace": function() {
                                $( this ).dialog( "close" );
                                createApplication()
                            },
                            Cancel: function() {
                                $( this ).dialog( "close" );
                                                $(id).closest('tr').children("td").toggleClass("ui-state-highlight");
                            }
                        }
                    });
                }
                else {
                    // application does not exist, so just create it
                    createApplication()
                }
            }
            
            function init() {
            	//document.write("Making Table1")
            	//alert("ssssss")
	            jQuery(function() {
	            	///document.write("Making Table2");
	            	alert("111")
                    jsonrpc = new JSONRpcClient("/SIDGridPortal/Old-JSONRPC");
	            	alert("222")
	            	//document.write("Making Table3");
                    uploader = createUploader('turam')
                    jQuery("#buttonset").buttonset();
                    jQuery("button").button();
                    //jQuery("button",".buttons").button();
                    //jQuery("input",".buttons").button();
                    document.write("Making Table4")
                    makeTable();
                    alert("ssssss")
                    document.getElementById('application-name').value = DefaultApplicationName
                    document.getElementById('preview-application-name').innerHTML = DefaultApplicationName
                    jQuery("#preview-container").hide()
                    jQuery("#files").hide()
                    setMode(DefaultMode)

                    //console.log("application = " + application )
                    //console.log("action = " + action )
                    if( action === "edit" && application ) {
                        parameterTable.clearGridData()
                        appInfo = jsonrpc.AppMgr.getAppInfo(application)['map']
                        //console.log("app: " + appInfo['name'] + appInfo['command'] )
                        //console.log("appInfo: " +  appInfo.toSource() )
                        jQuery('#application-name').val( appInfo['name'] )
                        jQuery('#application-command').val( appInfo['command'] )
                        appParameters = jsonrpc.AppMgr.getParameters(application)
                        //console.log("appParameters: " + appParameters.toSource() )
                        loadParamToTable(appParameters.list,parameterTable);



                    }


	            });
            }

            jQuery(window).load( init() )
            
            
        </script>

	</head>
	<body>
      <%@ include file="nav.html" %>
      <div style="width:60%;margin-left:auto;margin-right:auto;padding:0px 20px;">
         <div id="buttonset" class="buttons" style="float:left">
            <input type="radio" id="command-line-radio" onClick="setMode('commandline')" checked="checked"/><label for="command-line-radio">Command Line</label>
            <input type="radio" id="swift-radio" onClick="setMode('swift')" /><label for="swift-radio">Swift</label>

         </div>
         <div class="buttons" style="float:right">
            <button onClick=help()>Help</button>
            <button onClick=showAndPreviewApplication()>Preview</button>
            <button onClick=checkAndCreateApplication()>Submit</button>
         </div>
         <div style="clear:both"></div>
      <br>

        
        <div class="ui-widget ui-clearfix">
            <div class="margin-fix ui-widget-header ui-corner-top ui-helper-clearfix" style="padding:0.3em 0.2em 0.2em 0.3em;">
                Application Info
            </div>
            <div class="margin-fix ui-widget-content">
            <fieldlabel>Name of the application</fieldlabel><input id="application-name" class="parameter ui-widget-content ui-corner-all" type="text" onkeyup="applicationNameUpdate(this.value,event)">
            <div style="clear:both"></div>
            <div style="display:none"><fieldlabel>Description</fieldlabel><input id="application-description" class="parameter ui-widget-content ui-corner-all" size=40 type="text" onkeyup="applicationDescriptionUpdate()"></div>
            <div style="clear:both"></div>
            <div id="application-command-container"><fieldlabel>Executable Name</fieldlabel><input id="application-command" class="parameter ui-widget-content ui-corner-all" type="text"></div>
            <div style="clear:both;padding:5px;"></div>
            </div>
        </div>
        <br /> 
        
        <div id="command-line-container" class="ui-widget ui-clearfix">
            <div class="margin-fix ui-widget-header ui-corner-top ui-helper-clearfix" style="padding:0.3em 0.2em 0.2em 0.3em;">
                Application Command Line
            </div>
            <div class="margin-fix ui-widget-content">
            <div id="commandlineblock" style="display:hidden">
            <fieldlabel>Type the command line just as you would execute it in a shell to have it parsed into the table below.</fieldlabel>
            <p>
            <input class="parameter ui-widget-content ui-corner-all" style="width:85%" id='cmdline' type=text onkeydown="if (event.keyCode == 13) { handleCommandLine() } " size=50 value="app -a 1 -b 2 --longarg='longarg' --longarg2">
            <button onClick="handleCommandLine()">OK</button>
            </div>
            </div>
        <br /> 
        </div>
        
        <div id="files" class="margin-fix buttons ui-widget">
            <div class="ui-widget-header ui-corner-top ui-jqgrid-titlebar" style="padding-right:10px;">
                Application Files

                <div id="flashUI1" style="float:right">
                     <div style="padding-left: 0px;">
                       <span id="spanButtonPlaceholder1">Upload</span>
                      <input id="btnCancel1" type="button" value="Cancel Uploads" onclick="cancelQueue(upload1);" 
                             disabled="disabled" style="margin-left: 2px; height: 35px; font-size: 8pt; display:none" />
                     </div>
                </div>

                <div style="clear:both"></div>
            </div>
            <div id='filelist' class="ui-widget-content">
                 <div id="fsUploadProgress1">
                     <!-- <span class="legend"></span> -->
                 </div>
                 <div id='filelist_placeholder'>No files yet</div>
                 <div style="clear:both"></div>
            </div>
        <br />
        </div>


        
        <!-- application parameters table definition -->
        <table id="rowed3"></table> 
        <div id="prowed3"></div>
        <br/> 

 
        <div id="preview-container" style="float:left;width:auto;display:none">
           <div id="preview-container-child" class="margin-fix buttons ui-widget">
              <div class="ui-widget-header ui-corner-top" style="width:400px;padding:0.3em 0.2em 0.2em 0.3em;">
                  Application Preview
              </div>
              <div class="ui-widget-content" style="width:400px">
              <div class="ui-widget" style="margin-left:25px;margin-right:25px;margin-top:30px;width:auto">
              <div class="ui-widget" style="margin:25px 25px;width:auto">
                  <div id="preview-application-name" class="ui-widget-header" style="display:none"></div>
                  <div class="ui-widget-content">
                  <div style="display:none"><div id="hack-preview-application-description"></div></div>
                  <div id="preview">
  
                     <div style="height:300px">Not generated yet</div>
  
                  </div>
                  <div style="clear:both"></div>
                  </div>
                  <div style="clear:both"></div>
                  </div>
              </div>
          </div>
          <br /> 
        </div>
     </div>

      </div>
        <!-- dialog content starts here -->
        
        <!-- notification -->
        <div id="notification" style="font-size:12px;display:none; position: absolute; width: 70%; margin-left:auto;margin-right:auto;height: 50px; top: 0px; line-height:2em;padding:10px" class="ui-widget ui-widget-content ui-state-highlight">
	    <div style="float:right"><a href=javascript:hideNotification()>Close</a> </div>
        <div id="msg" style="float:left"></div>
        </div>

        <div id="dialog-confirm" title="Delete this item?" style="display:none">
            <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>This item will be permanently deleted and cannot be recovered. Are you sure?</p>
        </div>

        <!-- command line dialog -->
        <div id="commandLineDialog" title="Dialog Title" style='display:none'>
            <div class="bd">
            <div id="commandlineblock" style="display:hidden">
            Type the command line just as you would execute it in a shell to have it parsed into the table below.
            <p>
            <input id='cmdline' type=text onkeydown="if (event.keyCode == 13) { handleCommandLine() } " size=50 value="app -a 1 -b 2 --longarg='longarg' --longarg2">
            <input type='submit' value='OK' onClick="handleCommandLine()">
            </div>
            </div>
        </div>

        <!-- upload dialog -->
        <div id="uploadDialog" class="ui-widget" style='display:none'>
            <div class="bd">
            Upload a Swift script and other supporting files. They'll appear in the "Application Files" box below.
            <div id="upload_span_container">




              <input id="btnCancel1" type="button" value="Cancel Uploads" onclick="cancelQueue(upload1);" disabled="disabled          
             </div>
          </div>
        </div>
        

        

	</body>
</html>
