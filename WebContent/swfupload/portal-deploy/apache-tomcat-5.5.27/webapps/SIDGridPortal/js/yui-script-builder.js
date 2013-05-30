 /**
  * This JavaScript presents a Script Builder UI for the gadget workspace framework.
  * Models: MyDataTable -- Cmd Argument List, FileUploaderTable
  * Operations: load arguments into MyDataTable, Add/Remove arguments into the MyDataTable, 
 **/
 YAHOO.widget.BigTextAreaCellEditor = function () {
  YAHOO.widget.DataTable.Editors.bigtextarea.superclass.constructor.apply(this,arguments);
 }
 YAHOO.widget.DataTable.Editors.bigtextarea = YAHOO.widget.BigTextAreaCellEditor
 YAHOO.lang.extend(YAHOO.widget.BigTextAreaCellEditor,YAHOO.widget.TextareaCellEditor,
 {
   width: null,
   height:null,
   move : function() {
     this.textarea.style.width = this.width ||
     this.getTdEl().offsetWidth + "px";
     this.textarea.style.height = this.height || "3em";
     YAHOO.widget.TextareaCellEditor.superclass.move.call(this);
   }
 });

 var paralist;
 var paraHash;
 var selectedParam = null;
 var myDataTable;
 var overLi = null;
 var MainScript = null;
 
 // function for creating arg representation from ParamName and ParamType
 function CreateArgFmt(ParamName, ParamType) {
    var argfmt = "";
    switch (ParamType) {
	case 0:
	  argfmt = "-"+ParamName;
	case 1:
	  argfmt = "value";
	case 2:
	  argfmt = "-"+ParamName+"=value";
	  
	}
	return argfmt;
 }
 // function for translating arg representation to ParamName and ParamType 
 function ParseArgFmt(ArgFmt) {
   var re_0 = new RegExp("^-");
   var re_1 = new RegExp("^-.*=");
   var ParamDef = new Object();
   if (ArgFmt.match(re_1)){
      ParamDef.ParamType = 2;
	  ArgFmt = ArgFmt.substring(1);
	  var paramName = ArgFmt.split("=")[0];
	  ParamDef.ParamName = paramName;
	  return ParamDef;
   }
   if (ArgFmt.match(re_0)){
      ParamDef.ParamType = 0;
	  ArgFmt = ArgFmt.substring(1);
	  ParamDef.ParamName = ArgFmt;
	  return ParamDef;
   }
   
 }
 // CellEditor function
 function updateParameter(callback, newValue){
    var record = this.getRecord();
	var oldValue = this.value;
	var column = this.getId();
	var column = this.getColumn();
	// update the paralist for this newValue
	
	console.log("update the column "+column+" with the new value "+newValue+ " for "+record);
	callback(true, newValue);
 }
 
 function init(){
   var paginator = new YAHOO.widget.Paginator( {
          containers : [ "dt-pag-nav"],
          rowsPerPage: 10
        } );
    var myConfig = { 
	        paginator : paginator, 
	        initialLoad : false 
      }; 
   var myColumnDefs = [
      {key:"Select", label:"Select", resizeable:true,formatter:"checkbox"}, 
      {key:"argpos",resizeable:true, sortable:true, editor:new YAHOO.widget.TextboxCellEditor({asyncSubmitter: updateParameter})},
      {key:"ArgFmt",label: "Argument", resizeable:true, sortable:true, editor:new YAHOO.widget.TextareaCellEditor({asyncSubmitter: updateParameter})}, 
      {key:"ParamPrompt",label: "Argument Prompt", resizeable:true, editor:new YAHOO.widget.BigTextAreaCellEditor({width:'30em',height:'10em'})},
      {key:"DataType",resizeable:true, editor: 
	     new YAHOO.widget.DropdownCellEditor({dropdownOptions:["Boolean","String","File", "Integer", "Float", "Choice"]})},
      //{key:"ParamType",resizeable:true, editor:
	  //   new YAHOO.widget.DropdownCellEditor({dropdownOptions:[{label: "format: -a", value: "1"},
	  // {label:"format: value",value:"0"}, {label:"format: -a=value", value:"2"}]})},
	  
	  {key:"ParamAttributes", editor:
	     new YAHOO.widget.CheckboxCellEditor({checkboxOptions:["isInput","isOutput","isHidden", "isStdout", "isLoop" ]})},
	  {key:"Insert", label:'Insert', className:'insert-button'},
	  {key:"Delete", lable:'Delete', className:'delete-button'}
   ];
   var myDataSource = new YAHOO.util.DataSource([]); 
   myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
   //myDataSource.responseSchema = { fields: ["ParamName", "ParamPrompt", "DataType", "ParamType"] };
   myDataSource.responseSchema = { fields: ["ArgFmt", "ParamPrompt", "DataType", "ParamAttributes"] };
   
   myDataTable = new YAHOO.widget.DataTable("paralist", myColumnDefs, myDataSource, myConfig); 
   myDataTable.subscribe("rowMouseoverEvent", myDataTable.onEventHighlightRow);
   myDataTable.subscribe("rowMouseoutEvent", myDataTable.onEventUnhighlightRow);
   /** myDataTable.subscribe("cellClickEvent", function(ev){
                 var target = YAHOO.util.Event.getTarget(ev);
                 var record = this.getRecord(target);
                 console.log(record.getId());
                 var data = record.getData();
				 console.log(data.argpos);
				 if (data.Select){
                   data.Select = false;
				   paralist[data.argpos].Select = false;
                 } else {
				   data.Select = true;
				   paralist[data.argpos].Select = true;
				 }
                 record.setData(data);
                 selectedParam = data.argpos;
                 //TODO: read the values from the paralist ( setIsInput ... )
                 var datatype_input = document.getElementById("datatype");
                 datatype_input.value = data.DataType;
                 var paraName_input = document.getElementById("paraName");
                 paraName_input.value = data.ParamName; 
                 var paraPrompt_input = document.getElementById("paraPrompt");
                 paraPrompt_input.value = data.ParamPrompt;
                 var paraType_input = document.getElementById("paraType");
                 paraType_input.value = data.ParamType;
                 var isInput_in = document.getElementById("isInput");
				 isInput_in.checked = data.IsInput;
                 var isOutput_in = document.getElementById("isOutput");
				 isOutput_in.checked = data.IsOut;
                 var isHidden_in = document.getElementById("isHidden");
				 paralist[data.argpos].Select = true;
                 
            }); **/
   myDataTable.subscribe("cellClickEvent", function(oArgs){ 
     var target = oArgs.target;
	 var column = myDataTable.getColumn(target);
	 //console.log('action is '+column.key);
	 if (column.key == 'Delete'){
	   myDataTable.deleteRow(target);
	   //delete this record from paralist
	 } else if (column.key == 'Insert'){
	   myDataTable.addRow( {}, this.getRecordIndex(target));
	   // add this record into paralist
	 } else {
	   myDataTable.onEventShowCellEditor(oArgs);
	 }
   });
   myDataTable.subscribe("rowMouseoverEvent", myDataTable.onEventHighlightRow);
   myDataTable.subscribe("rowMouseoutEvent", myDataTable.onEventUnhighlightRow);
   //myDataTable.subscribe("rowClickEvent", myDataTable.onEventSelectRow); **/

   
 }
 function onSelectCell(){
   console("to be implemented");
 }
 function  MyUploadSuccess(file, serverData) {
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		if (serverData.substring(0, 7) === "FILEID:") {
		  progress.setComplete();
		  progress.setStatus("Complete.");
		  progress.toggleCancel(false);
		  // set the value of the input field
		  var url = serverData.substring(7);
		  alert(url);
		  var fileInput = document.getElementById(this.customSettings.fileInputTarget);
		  fileInput.value=url;
		  var newfile = new Object();
		  newfile.name = "<a href='"+url+"'>"+file.name+"</a>";
		  newfile.size = file.size;
		  FileUploaderTable.addRow(newfile); 
		  FileUploaderList.push(url)
        } else {
          progress.setStatus("Error.");
		  progress.toggleCancel(false);
		  alert(serverData);
        }
        
	} catch (ex) {
		this.debug(ex);
	}
}

 function loadSwfUploader(){
   var uploader = new SWFUpload({
				// Backend Settings
				upload_url: "../fileupload",	// Relative to the SWF file (or you can use absolute paths)
				post_params: { "ChannelID": "28", "Type": "Type2" },

				// File Upload Settings
				file_size_limit : "200",	// 1000MB
				file_types : "*.*",
				file_types_description : "All Files",
				file_upload_limit : "10",
				file_queue_limit : "0",

				// Event Handler Settings (all my handlers are in the Handler.js file)
				file_dialog_start_handler : fileDialogStart,
				file_queued_handler : fileQueued,
				file_queue_error_handler : fileQueueError,
				file_dialog_complete_handler : fileDialogComplete,
				upload_start_handler : uploadStart,
				upload_progress_handler : uploadProgress,
				upload_error_handler : uploadError,
				upload_success_handler : MyUploadSuccess,
				upload_complete_handler : MyUploadComplete,

				// Button Settings
				button_image_url : "../images/XPButtonUploadText_61x22.png",
				button_placeholder_id : "spanButtonPlaceholder1",
				button_width: 61,
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
	// create the file uploads table
	 var dataArr = [];
	 var myDataSource = new YAHOO.util.DataSource(dataArr);
	 myDataSource.responseSchema = {
          fields: ["name","size"]
      };
	 var myColumnDefs = [
	        {key:"name", label: "File Name", sortable:false},
	     	{key:"size", label: "Size", sortable:false},
	   ];
     FileUploaderTable = new YAHOO.widget.DataTable("uploadFileTable",
	           myColumnDefs, myDataSource, {
	              rowSingleSelect: true
	           });
	FileUploaderTable.subscribe("cellMousedownEvent",FileUploaderTable.onEventSelectRow);
    FileUploaderList = [];
	// define the OnRowSelect
	var onRowSelect = function(ev) { 	         
 	        //thisStatus.innerHTML = ''; 
 	        var par = FileUploaderTable.getTrEl(Event.getTarget(ev)); //The tr element 
 	        selectedRow = FileUploaderTable.getSelectedRows(); 
 	        ddRow = new YAHOO.util.DDProxy(par.id); 
 	        ddRow.handleMouseDown(ev.event); 
 	 
 	        ddRow.onDragOver = function() { 
 	            Dom.addClass(arguments[1], 'over'); 
 	            if (overLi && (overLi != arguments[1])) { 
 	                Dom.removeClass(overLi, 'over'); 
 	            } 
 	            overLi = arguments[1]; 
 	        } 
 	        ddRow.onDragOut = function() { 
 	            Dom.removeClass(overLi, 'over'); 
 	        } 
 	        ddRow.onDragDrop = function(ev) { 
 	            //thisStatus.innerHTML = 'You dropped this row (' + selectedRow + ') on this li (' + Dom.get(overLi).innerHTML + ')'; 
				var selectedRow = FileUploaderTable.getSelectedRows();
				var uploadFileUrl = FileUploaderTable.getRecordSet().getRecord(selectedRow[0]).getData('name');
				console.log("populate the paralist with this file "+uploadFileUrl);
				var tmp_div = document.createElement('div');
				tmp_div.innerHTML = uploadFileUrl;
				var href = tmp_div.getElementsByTagName('a')[0].href;
 	            Dom.removeClass(overLi, 'over'); 
 	            FileUploaderTable.unselectAllRows(); 
 	            YAHOO.util.DragDropMgr.stopDrag(ev,true); 
 	            Dom.get(this.getDragEl()).style.visibility = 'hidden'; 
 	            Dom.setStyle(this.getEl(), 'position', 'static'); 
 	            Dom.setStyle(this.getEl(), 'top', ''); 
 	            Dom.setStyle(this.getEl(), 'left', ''); 
				console.log("parse this swift file "+href);
				var paralist = jsonrpc.AppMgr.guessParameters(sessionID, href).list;
				loadParamToTable(paralist, myDataTable);
				// TODO: remember the main swift script that the user select
				var tmplist = href.split("/");
				MainScript = tmplist[tmplist.length-1];
 	        }  
 	    }; 
	var ddtarget = document.getElementById("paralist");
	new YAHOO.util.DDTarget(ddtarget);
	FileUploaderTable.subscribe('cellMousedownEvent', onRowSelect); 
    return uploader;
 }
 function listDefinedApps(){
    var applist = jsonrpc.AppMgr.showAllApplication();
	var appSelList = document.getElementById("defined-app-list");
	for( var i=0; i<applist.list.length; i++){
	   appSelList.options[appSelList.length] = new Option(applist.list[i].appName, applist.list[i].appName);
	}
 }
 function showGadgetHTML(){
   //var jsonrpc = new JSONRpcClient("/jobsubportlets/JSON-RPC");
   var gadgetName = document.getElementById("title").value;
   var gadgetHtml = jsonrpc.AppMgtService.getGadgetHTML(gadgetName);
   var div = document.getElementById("displayGadget");
   div.innerHTML = gadgetHtml;
 }
 //  delete a parameter
function remParameter(){
  myDataTable.deleteRowsBy(function (data) {
					return data.Select;
				});
  paralist.splice(selectedParam, 1);
  //update all the argposes
}
//load all the parameters for an application and display them in a table
function loadParameters(appName, scriptName, scriptVersion){
  var appinfo = jsonrpc.AppMgr.getScriptInfo(appName, scriptName, scriptVersion);
  var name = document.getElementById("name"); name.value = appinfo.map['name'];
  var title = document.getElementById("title");
  if (appinfo.map['title'] != null ) 
    title.value = appinfo.map['title'];
  var command = document.getElementById("command");
  if (appinfo.map['command'] != null )
    command.value = appinfo.map['command'];
  var version = document.getElementById("version");
  if (appinfo.map['version'] != null )
    version.value = appinfo.map['version'];
  
  var res = jsonrpc.AppMgr.getParameters(appName, scriptName, scriptVersion);
   paralist = res.list;
  
  // update the paratable
  loadParamToTable(paralist, myDataTable);
}
function loadParamToTable(paralist, targetTable){
  targetTable.deleteRows(targetTable.getRecordSet().getLength() - 1, -1* targetTable.getRecordSet().getLength()); 
  
  for(var i=0; i<paralist.length;i++){
    var newparam = new Object();
	// construct a command arg representation from <ParamName, DataType>
    newparam.ParamName = paralist[i].name;
    newparam.ParamPrompt = paralist[i].prompt;
    newparam.DataType = paralist[i].dataType;
    newparam.ParamType = paralist[i].argType;
	newparam.IsInput = paralist[i].input;
	newparam.IsOut = paralist[i].out;
    newparam.argpos = i;
	newparam.ArgFmt = CreateArgFmt(newparam.ParamName, newparam.ParamType);
	// set the param attribute
	newparam.ParamAttributes = new Array();
	if (paralist[i].input == true ){
	  newparam.ParamAttributes.push("IsInput");
	}
	if (paralist[i].out == true ){
	  newparam.ParamAttributes.push("IsOutput");
	}
	
    newparam.Select = false;
    targetTable.addRow(newparam, i); 
  }
  targetTable.render();

}
// request the appMgtService to create a Mobyle XML
 function createXML(){
   //validate the input
   var name = document.getElementById("name").value;
   if (name==null || name==""){
     alert("Application Name needs to be specified");
     // mark the name input field
     return;
   }
   var title = document.getElementById("title").value;
   var command = document.getElementById("command").value;
   var version = document.getElementById("version").value;
   
   //get the records directly from the datatable
   var records = myDataTable.getRecordSet();
   console.log(records);
   
   var params = new Object();
   params.javaClass = 'java.util.List';
   params.list = [];
   for(var i=0; i<records.getLength(); i++){
      var record = records.getRecord(i);
      var tmp = new Object;
      tmp.javaClass = 'org.uc.sidgrid.app.AppParameter';
	  var argfmt = record.getData("ArgFmt");
	  if (argfmt == null ) {
	     alert("the argument format for row "+i+" is not defined");
	  }
	  var ParamDef = ParseArgFmt(argfmt);
	  tmp.name = ParamDef.ParamName;
	  tmp.argType = ParamDef.ParamType;
	  
      //tmp.name = record.getData('ParamName');
	  // if name is null, hightlight the record, then show the error
	  /** if (tmp.name == null ){
	     alert("the parameter name for row "+i+" is not defined");
		 var name_cell = myDataTable.getCell(i,1);
		 myDataTable.highlightCell(name_cell);
	  }
	  tmp.argType = record.getData('ParamType');
	  if (tmp.argType == null ){
	     alert("the parameter argtype for row "+i+" is not defined");
	  } **/
	  
	  tmp.dataType = record.getData('DataType');
	  if (tmp.dataType == null ){
	     alert("the parameter datatype for row "+i+" is not defined");
	  }
      tmp.prompt = record.getData('ParamPrompt');
      var attrs = record.getData("ParamAttributes");
	  if (attrs == null ){
	     alert("the parameter attribute for row "+i+" is not defined");
	  }
	  if ( attrs != null ) {
	   for( var j=0; j<attrs.length; j++){
	     if (attrs[j] == "isInput"){
		   tmp.input = true; }
		 if (attrs[j] == "isOutput"){
		   tmp.output = true;}
		 if (attrs[j] == "isStdout"){
		   tmp.isStdout = true;}
		 if (attrs[j] == "isLoop"){
		   tmp.isloop = true; }
		//TODO: add more attributes here
	    }
	  }
	  tmp.argpos = record.getData('argpos');
      params.list[i]=tmp;
	  
	  // highlight the rows if any validation error
	  //myDataTable.get
   }
   jsonrpc.AppMgr.createMobyleXML(sessionID, name,command, title,params);
   // display the created mobyle xml
   
   var handleSubmit = function() {
		console.log("submit");
        this.cancel();
	};
    var handleCancel = function() {
		console.log("cancel ...");
        this.cancel();
	};
   var panel2 = new YAHOO.widget.Dialog("gadget-preview", 
						{ width : "600px", height: "400px", title: "gadget preview", 
							  fixedcenter : true,
							  visible : false, 
							  constraintoviewport : true, autofillheight : "body", 
							  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
								      { text:"Cancel", handler:handleCancel } ]
						});
   var handleSuccess = function(o){
            console.log("get the file ");
			var iframe = "<div style=\"overflow: auto; height: 300px; width: 500px;\" >"
			    +o.responseText+"</div>";
	        o.argument.setBody(iframe);
	        o.argument.render(YAHOO.util.Dom.get('appdef'));
			o.argument.cfg.setProperty("visible", true);
        }
        var handleFailure = function(o){
            console.log("can't get the file ");
        }
        var callback = { success:handleSuccess, failure:handleFailure, argument:panel2 };
        var sUrl = "/SIDGridPortal/temp/"+sessionID+"/gadget.xml";
        var request = YAHOO.util.Connect.asyncRequest('GET', sUrl, callback);
   
    //panel2.render(YAHOO.util.Dom.get('paralist'));  
    //panel2.cfg.setProperty("visible", true);
	
 }
 // request the appMgtService to create a gadget XML
 function getBaseURL() {
    var url = location.href;  // entire url including querystring - also: window.location.href;
    var baseURL = url.substring(0, url.indexOf('/', 14));
    if (baseURL.indexOf('http://localhost') != -1) {
        // Base Url for localhost
        var url = location.href;  // window.location.href;
        var pathname = location.pathname;  // window.location.pathname;
        var index1 = url.indexOf(pathname);
        var index2 = url.indexOf("/", index1 + 1);
        var baseLocalUrl = url.substr(0, index2);

        return baseLocalUrl + "/";
    }
    else {
        // Root Url for domain name
        return baseURL + "/";
    }
}
// create a new script application
 function commitScript(){
    var name = document.getElementById("name").value;
    if (name==null || name==""){
     alert("Application Name needs to be specified");
     // mark the name input field
     return;
    }
	// create a pkg zip
	// create a zipped script pkg
	var zipFile = name+".zip";
	var fileUrl_list = new Object();
	fileUrl_list.javaClass = "java.util.List";
	fileUrl_list.list = [];
	for(var i=0; i<FileUploaderList.length;i++){
	   fileUrl_list.list[i]=FileUploaderList[i];
	}
	jsonrpc.AppMgr.createScriptPkg(sessionID, fileUrl_list, zipFile);
	// add the new script to the system
	var version = document.getElementById("version").value;
	if (version==null || version==""){
	  alert("Script Version needs to be specified");
     // mark the name input field
     return;
	}
	var baseURL = getBaseURL();
    var scriptPkg = baseURL+"SIDGridPortal/temp/"+sessionID+"/"+name+".zip";
	var myscript = MainScript;
	if (myscript == null)
	   myscript = name+".swift";
	var xmlfile = name+".xml";
    var res = jsonrpc.AppMgr.addNewScript(sessionID, name, name, version, myscript, xmlfile);
      if (res.status != 0 )
          alert(res.message);
      else
          alert("the application "+res.appName+" has been created !");
  }
  // list the applications and select one for editing
  function openApplication(){
   
    var tableContentGenerator = function() {
				var response = [];
				// check the application name
				var appSelList = document.getElementById("defined-app-list");
				var appName = appSelList.options[appSelList.selectedIndex].value;
                var scriptList = jsonrpc.AppMgr.showAllScripts(appName);
				for (var i = 0;i < scriptList.list.length;i++) {
					var dataRow ={};
                    dataRow['name'] = scriptList.list[i].scriptName;
                    dataRow['mainscript'] = scriptList.list[i].mainScript;
                    dataRow['version'] = scriptList.list[i].version;
                    dataRow['status'] = scriptList.list[i].status;
					console.log(dataRow[1]);
					response.push(dataRow);
				    // console.log("check the scripts for the app "+appName);
  	                // var gadgetList = document.getElementById("gadgetList");
				}
				return response;
			};
    var ds = new YAHOO.util.DataSource(tableContentGenerator);
   // this tells the datasource that the content generator will return an array
   ds.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
   ds.responseSchema = {fields: ['name','mainscript','version','status']  };
					
   var appTable = new YAHOO.widget.DataTable(
						'apptable',	
						[
							{key:'name'},
							{key:'mainscript'},
							{key:'version'},
							{key:'status'}
						], 
						ds
					);
    appTable.subscribe("rowMouseoverEvent", appTable.onEventHighlightRow); 
    appTable.subscribe("rowMouseoutEvent", appTable.onEventUnhighlightRow); 
    appTable.subscribe("rowClickEvent", appTable.onEventSelectRow); 

    appTable.subscribe('cellClickEvent',function (ev) {
         var target = YAHOO.util.Event.getTarget(ev);
	     var record = this.getRecord(target);
         console.log(record.getData('name'));
         selScriptName = record.getData('name');
  	            // var gadgetList = document.getElementById("gadgetList");
		 selScriptVersion = record.getData('version');
		  }

      );
    // render a dialog window for selection
    var handleSubmit = function() {
		console.log("load the application into the parameter list");
             // this.submit();
              // if this app's type is 'script', we should redirect the page to Script-App-Builder
			  var appSelList = document.getElementById("defined-app-list");
			  var appName = appSelList.options[appSelList.selectedIndex].value;
              loadParameters(appName, selScriptName, selScriptVersion);
              this.cancel();
	};
    var handleCancel = function() {
		console.log("cancel ...");
              this.cancel();
	};

    var panel = new YAHOO.widget.Dialog("dialog1", 
						{ width : "30em",
							  fixedcenter : true,
							  visible : false, 
							  constraintoviewport : true,
							  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
								      { text:"Cancel", handler:handleCancel } ]
						});

   
    panel.render(YAHOO.util.Dom.get('paralist'));  
    panel.cfg.setProperty("visible", true);

  }


