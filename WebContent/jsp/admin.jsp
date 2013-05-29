<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<html>
<head>
<title>Application Administration</title>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/jquery-ui.min.js"></script>
<link type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.0/themes/cupertino/jquery-ui.css" rel="stylesheet" />

<link type="text/css" href="<%=request.getContextPath()%>/css/scienceworkflow.css" rel="stylesheet" />

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
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.1/build/paginator/paginator-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.7.0/build/connection/connection-min.js"></script>

<script type="text/javascript" src="../js/jsonrpc.js"></script>

<script>
  var selectedAppName = null;
  var jsonrpc;

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

 var Dom = YAHOO.util.Dom,
     Event = YAHOO.util.Event,
     layout = null,
     resize = null;

     Event.onDOMReady(function() {
        //layout = new YAHOO.widget.Layout( {
                    //minWidth: 1000,
                    //units: [
                        //{ position: 'top', height: 28, body: 'menu', scroll: null, zIndex: 2 },
                        //{ position: 'left', header: 'application list', body: 'apptable', gutter: '2px', width: 600, collapse:true  }, 
                        //{ position: 'center',  header: 'script list', width: 800, resize: true, body: 'scriptlist', gutter: '0 5 0 2', minWidth:  600, maxWidth: 800 }
                    //]
                //});
        //layout.render();
        jsonrpc = new JSONRpcClient("<%=request.getContextPath()%>/Old-JSONRPC");
            
       var myColumnDefs = [
     // {key:"Select", label:"Select", resizeable:true,formatter:"radio",hidden:true}, 
      {key:"name", label:"Application", resizeable:true, sortable:true },
     // {key:"url", label: "XML Description", resizeable:true, sortable:true, hidden:true, 
        //editor:new YAHOO.widget.TextareaCellEditor()
     //     }, 
      {key:"type", label:"Type", resizable:true, sortable:true },
      {key:"status", label: "Enabled", resizeable:true, sortable:true, 
       //editor:new YAHOO.widget.DropdownCellEditor({dropdownOptions:["created", "deployed"], asyncSubmitter: updateAppStatus})
       }
      ];
     
      var myDataSource = new YAHOO.util.DataSource([]); 
      myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
      myDataSource.responseSchema = { fields: ["name", "url", "status"] };
   
      myDataTable = new YAHOO.widget.ScrollingDataTable("apptable", myColumnDefs, myDataSource, {}); 
      myDataTable.subscribe("rowMouseoverEvent", myDataTable.onEventHighlightRow);
      myDataTable.subscribe("rowMouseoutEvent", myDataTable.onEventUnhighlightRow);
      //myDataTable.subscribe("cellClickEvent", function(oArgs){ 
          //var target = oArgs.target;
	  //var column = myDataTable.getColumn(target);
          //if (column.key == 'Select'){
            //var rec = this.getRecord(target);
            //displayScriptApps(rec.getData('name'));
          //} else 
             //myDataTable.onEventShowCellEditor(oArgs);
      //});

      var result = jsonrpc.AppMgr.showEveryApplication();
      result.list.sort( function(a,b) { if( a.appName < b.appName ) { return -1 } else { return 1 } } )
      for( var i=0; i<result.list.length; i++){
          var newparam = new Object();
          newparam.name = result.list[i].appName;
 	      var url = result.list[i].xmldesfile;
          if (url== null) 
              url = "not defined";
          newparam.url = url;
          newparam.type = result.list[i].type;
          //newparam.status = result.list[i].status;
          if( result.list[i].status === "deployed" )
              newparam.status = "<input type=checkbox checked=true name=status value=created onClick=onStatusChange(this)>"
          else
              newparam.status = "<input type=checkbox name=status value=created onClick=onStatusChange(this)>"
          myDataTable.addRow(newparam, i);
      }

      myDataTable.render();
      
   });   

   
   function saveUpdate (){
     /** for(i=0; i<changes.modified.length; i++){
              var modified = changes.modified[i];
              if (modified.status == 'deployed')
                 jsonrpc.AppMgr.deployApplication(modified.name);
     } **/

     // handle the deleted script application
    }
</script>


</head>
<body class="yui-skin-sam">
  <%@ include file="nav.html" %>
  <div id='apptable' class='ui-widget'>
	  <div class="heading">Science Portal Applications</div>
  </div>
</body>
</html>

