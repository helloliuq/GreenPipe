 
 var myDataTable;
 var panel;
 var appResponse;

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




 function init(){

   alert = showError

   var myColumnDefs = [
      {key:"argpos",resizeable:true,sortable:true, editor:new YAHOO.widget.TextboxCellEditor()},
      {key:"ParamName",label:"Option", resizeable:true, sortable:true, editor:new YAHOO.widget.TextareaCellEditor()}, 
      {key:"ParamPrompt",label:"Prompt",resizeable:true,editor:new YAHOO.widget.BigTextAreaCellEditor({width:'30em',height:'10em'})},
      {key:"DataType",resizeable:true, editor: 
	     new YAHOO.widget.DropdownCellEditor({dropdownOptions:["Boolean","String","File", "Integer", "Float", "Choice"]})},
      {key:"ParamType",resizeable:true, editor:
	     new YAHOO.widget.DropdownCellEditor({dropdownOptions:[
             { label: "format: -a", value: "1"},
		     {label:"format: value",value:"0"}, 
             {label:"format: -a=value", value:"2"},
             {label:"format: -a value", value:"3"},
             {label:"format: --a=value", value:"4"},
             {label:"format: --a value", value:"5"},
             {label:"format: --a", value:"6"}
           ]})},
	  {key:"ParamAttributes", label:"Attributes", editor:
	     new YAHOO.widget.CheckboxCellEditor({checkboxOptions:["isInput","isOutput","isHidden", "isStdout"]})},
	  {key:"Insert", label:'Insert', className:'insert-button'},
	  {key:"Delete", lable:'Delete', className:'delete-button'} 
   ];
   var myDataSource = new YAHOO.util.DataSource([]); 
   myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
   myDataSource.responseSchema = { fields: ["ParamName", "ParamPrompt", "DataType", "ParamType"] };
   
   myDataTable = new YAHOO.widget.DataTable("paralist", myColumnDefs, myDataSource, {}); 
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

    panel = new YAHOO.widget.Dialog("dialog1", 
						{ width : "30em",
							  fixedcenter : true,
							  visible : false, 
							  close : true, 
							  constraintoviewport : true,
							  //buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
						//	      { text:"Cancel", handler:handleCancel } ]
						});

   
 }
 function onSelectCell(){
   //console("to be implemented");
 }
 function showXML(){
   //var jsonrpc = new JSONRpcClient("/jobsubportlets/JSON-RPC");
   //var gadgetName = document.getElementById("title").value;
   var gadgetName = document.getElementById("name").value;
   var url = window.location.protocol + "//" + window.location.host + "/gadgets/"+gadgetName+".xml";
   dojo.xhrGet( {
      url: url,
      // handler: callback,
	  load: function(data) {
          var div = document.getElementById("displayXML");
          div.innerHTML = "";
          var textNode = document.createTextNode(data);
          div.appendChild(textNode);
      },
	  error: function(data) {
         alert('Server error');
      }
    });
 }
 function callback(type, data, evt){
   var div = document.getElementById("displayXML");
   var textNode = document.createTextNode(data);
   div.appendChild(textNode);
 }
 function showGadgetHTML(){
   //var jsonrpc = new JSONRpcClient("/jobsubportlets/JSON-RPC");
   var gadgetName = document.getElementById("title").value;
   var gadgetHtml = jsonrpc.AppMgtService.getGadgetHTML(gadgetName);
   var div = document.getElementById("displayGadget");
   div.innerHTML = gadgetHtml;
 }
 function showXML2(){
   var gadgetName = document.getElementById("title").value;
   if (gadgetName != null ){
    var url = window.location.protocol + "//" + window.location.host + "/gadgets/"+gadgetName+".xml";
    //TODO: use dijit.tree and dojo.data.XmlStore??
    var xml = xmlMicoxLoader(url);
    var html = xmlMicoxTree(xml,"");
   
    var div = document.getElementById("displayXML");
    div.innerHTML = html;
   } else
   {
     //console.log("have not define the name for the application yet");
   }

 }
 function xmlMicoxLoader(url){
  //by Micox: micoxjcg@yahoo.com.br.
  //http://elmicoxcodes.blogspot.com
    if(window.XMLHttpRequest){
        var Loader = new XMLHttpRequest();
        //assyncronous mode to load before the 'return' line
        Loader.open("GET", url ,false); 
        Loader.send(null);
        return Loader.responseXML;
    }else if(window.ActiveXObject){
        var Loader = new ActiveXObject("Msxml2.DOMDocument.3.0");
        //assyncronous mode to load before the 'return' line
        Loader.async = false;
        Loader.load(url);
        return Loader;
    }
 }
 function xmlMicoxTree(xmlNode,ident){
  //by Micox: micoxjcg@yahoo.com.br
    var treeTxt=""; //var to content temp
    for(var i=0;i<xmlNode.childNodes.length;i++){//each child node
  if(xmlNode.childNodes[i].nodeType == 1){//no white spaces
     //node name
   treeTxt = treeTxt + ident + xmlNode.childNodes[i].nodeName + ": "
   if(xmlNode.childNodes[i].childNodes.length==0){
     //no children. Get nodeValue
     treeTxt = treeTxt + xmlNode.childNodes[i].nodeValue 
     for(var z=0;z<xmlNode.childNodes[i].attributes.length;z++){
       var atrib = xmlNode.childNodes[i].attributes[z];
       treeTxt = treeTxt + " (" + atrib.nodeName + " = " + atrib.nodeValue + ")";
     }
     treeTxt = treeTxt + "<br />\n";
   }else if(xmlNode.childNodes[i].childNodes.length>0){
    //children. get first child
    treeTxt = treeTxt + xmlNode.childNodes[i].firstChild.nodeValue;
    for(var z=0;z<xmlNode.childNodes[i].attributes.length;z++){
     var atrib = xmlNode.childNodes[i].attributes[z];
     treeTxt = treeTxt + " (" + atrib.nodeName + " = " + atrib.nodeValue + ")";
    }
    //recursive to child of children
    treeTxt = treeTxt + "<br />\n" + xmlMicoxTree(xmlNode.childNodes[i],ident + "> > ");
   }
      }
    }
    return treeTxt;
}
//load all the parameters for an application and display them in a table
function loadParameters(appName){
  var appinfo = jsonrpc.AppMgr.getAppInfo(appName);
  var name = document.getElementById("name"); name.value = appinfo.map['name'];
  
  // update the paratable
  myDataTable.deleteRows(myDataTable.getRecordSet().getLength() - 1, -1* myDataTable.getRecordSet().getLength()); 
  var res = jsonrpc.AppMgr.getParameters(appName);
  var paralist = res.list;
  loadParamToTable(paralist,myDataTable);
}


function CreateArgFmt(ParamName, ParamType) {
    var argfmt = "";
    switch (ParamType) {
        case 0:
          argfmt = "-"+ParamName;
        case 1:
          argfmt = "value";
        case 2:
          argfmt = "-"+ParamName+"=value";
        case 3:
          argfmt = "-"+ParamName+" value";
        case 4:
          argfmt = "--"+ParamName+"=value";
        case 5:
          argfmt = "--"+ParamName+" value";
        case 6:
          argfmt = "--"+ParamName;

        }
        return argfmt;
 }


function loadParamToTable(paralist, targetTable){
  //targetTable.deleteRows(targetTable.getRecordSet().getLength() - 1, -1* targetTable.getRecordSet().getLength());
  targetTable.deleteRows(0, targetTable.getRecordSet().getLength());

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
          newparam.ParamAttributes.push("isInput");
        }
        if (paralist[i].out == true ){
          newparam.ParamAttributes.push("isOutput");
        }
        if (paralist[i].isStdout == true ){
          newparam.ParamAttributes.push("isStdout");
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
   var command = document.getElementById("command").value;
   //get the records directly from the datatable
   var records = myDataTable.getRecordSet();
   //console.log(records);
   
   var params = new Object();
   params.javaClass = 'java.util.List';
   params.list = [];
   for(var i=0; i<records.getLength(); i++){
      var record = records.getRecord(i);
      var tmp = new Object;
      tmp.javaClass = 'org.uc.sidgrid.app.AppParameter';
      tmp.name = record.getData('ParamName');
	  // if name is null, hightlight the record, then show the error
	  if (tmp.name == null ){
	     alert("the parameter name for row "+i+" is not defined");
		 var name_cell = myDataTable.getCell(i,1);
		 myDataTable.highlightCell(name_cell);
	  }
      tmp.prompt = record.getData('ParamPrompt');
      tmp.argType = record.getData('ParamType');
	  if (tmp.argType == null ){
	     alert("the parameter argtype for row "+i+" is not defined");
	  }
      tmp.dataType = record.getData('DataType');
	  if ((tmp.argType == 0 || tmp.argType == 2 ) && tmp.dataType == null ){
	     alert("the parameter datatype for row "+i+" is not defined");
	  }
	  var attrs = record.getData("ParamAttributes");
//console.log("attrsX = " + attrs.toSource() )
	  //if (attrs == null ){
	  //   alert("the parameter attribute for row "+i+" is not defined");
	  //}
	  if ( attrs != null ) {
	   for( var j=0; j<attrs.length; j++){
	     if (attrs[j] == "isInput"){
               tmp.input = true; 
             }
             if (attrs[j] == "isOutput"){
               tmp.out = true;
             }
             if (attrs[j] == "isStdout"){
               tmp.isStdout = true;
             }
	    }
	  }
	  tmp.argpos = record.getData('argpos');
      params.list[i]=tmp;
	 } 
	  // highlight the rows if any validation error
	  //myDataTable.get
      // 100729 turam - Use name for title; we're not using title currently anyway
	  //jsonrpc.AppMgr.createMobyleXML(sessionID, name,command, title,params);
//console.log("params = " + params.toSource() )
	  appResponse=jsonrpc.AppMgr.createMobyleXML(sessionID, name,command, name,params);
   
   // display the created mobyle xml
   
   var handleSubmit = function() {
        this.cancel();
	};
    var handleCancel = function() {
        this.cancel();
	};

   var panel2 = new YAHOO.widget.Module("gadget-preview")

   var handleSuccess = function(o){
	        var iframe = "<div style=\"overflow: auto; height: auto; width: 500px; \" >"
                            +o.responseText+"</div><div style='clear:both'></div>";

            o.argument.setBody(iframe);
            document.getElementById('placeholder').innerHTML = "";
	        o.argument.render(YAHOO.util.Dom.get('placeholder'));
		    o.argument.cfg.setProperty("visible", true);
   };
   var handleFailure = function(o){
            //console.log("can't get the file ");
   };
   var callback = { success:handleSuccess, failure:handleFailure, argument:panel2 };
   var sUrl = appResponse['gadgetURL']
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
 function commitApplication(){
    createXML()

    var name = document.getElementById("name").value;
    if (name==null || name==""){
     alert("Application Name needs to be specified");
     // mark the name input field
     return;
    }
	var baseURL = getBaseURL();
    var xmlfile = appResponse['mobyleXMLURL']
    var res = jsonrpc.AppMgr.uploadMobyleXML(xmlfile);
      if (res.status != 0 ) 
      {
          patt = /(cann)/g;
          if( res.message.match(patt) ) {
            alert("You must preview the application before it can be created.")
            return
          } 
          alert(res.message);
      }
      else
          showNotification("Application "+res.appName+" created successfully.");
  }
  // list the applications and select one for editing
  function openApplication(){
   
    var tableContentGenerator = function() {
		var response = [];
        var applist = jsonrpc.AppMgr.showEveryApplication();
        if(applist) {
            applist.list.sort( function(a,b) { if( a.appName < b.appName ) { return -1 } else { return 1 } } ) 
            for (var i = 0;i < applist.list.length;i++) {
                if( applist.list[i].type === "script" ) continue;
                var dataRow ={};
                dataRow['Name'] = applist.list[i].appName;
                dataRow['Title'] = applist.list[i].title;
                dataRow['Type'] = applist.list[i].type;
                dataRow['Status'] = applist.list[i].status;
                response.push(dataRow);
            }
		return response;
	};
    var ds = new YAHOO.util.DataSource(tableContentGenerator);
   // this tells the datasource that the content generator will return an array
   ds.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
   ds.responseSchema = {fields: ['Name','Title','Type','Status']  };
					
   var appTable = new YAHOO.widget.DataTable(
						'apptable',	
						[
							{key:'Name'},
							{key:'Title'},
							{key:'Type'},
							{key:'Status'}
						], 
						ds
					);
    appTable.subscribe("rowMouseoverEvent", appTable.onEventHighlightRow); 
    appTable.subscribe("rowMouseoutEvent", appTable.onEventUnhighlightRow); 
    appTable.subscribe("rowClickEvent", appTable.onEventSelectRow); 

    appTable.subscribe('cellClickEvent',function (ev) {
         var target = YAHOO.util.Event.getTarget(ev);
	 var record = this.getRecord(target);
         selAppName = record.getData('Name');
  	            // var gadgetList = document.getElementById("gadgetList");
         panel.cancel()
         handleSubmit()
       });
    // render a dialog window for selection
    var handleSubmit = function() {
		//console.log("load the application into the parameter list");
             // this.submit();
              // if this app's type is 'script', we should redirect the page to Script-App-Builder
              loadParameters(selAppName);
              this.cancel();
	};
    var handleCancel = function() {
		//console.log("cancel ...");
              this.cancel();
	};

   
    panel.render(YAHOO.util.Dom.get('appdef'));  
    panel.cfg.setProperty("visible", true);

  }

}
