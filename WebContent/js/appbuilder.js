 function init(){
   var itemToJS = function(store, item) {
      		// summary: Function to convert an item into a simple JS object.
      		// store:
      		//    The datastore the item came from.
      		// item:
      		//    The item in question.
      			var js = {};
      			if (item && store) {  //Determine the attributes we need to process.
        			var attributes = store.getAttributes(item);
        			if (attributes && attributes.length > 0) {
          				var i;
          				for (i = 0; i < attributes.length; i++) {
            					var values = store.getValues(item, attributes[i]);
            					if (values) { //Handle multivalued and single-valued attributes.
              					if (values.length > 1 ) {
                						var j;
                						js[attributes[i]] = [];
                						for (j = 0; j < values.length; j++ ) {
                  							var value = values[j];
                  						//Check that the value isn't another item. If it is, process it as an item.
                  							if (store.isItem(value)) {
                    								js[attributes[i]].push(itemToJS(store, value));
                  							} else {
                    								js[attributes[i]].push(value);
                  							}
                					}
              				} else {
                					if (store.isItem(values[0])) {
                  						js[attributes[i]] = itemToJS(store, values[0]);
                					} else {
                  						js[attributes[i]] = values[0];
                					}
              				}
            				}
          			}
        		}
      		}
      		return js;
     };

   jsonStore._saveCustom = function(saveComplete, saveFailed) {
           console.debug("save jsonstore");
           // < newItems, modified, deleted >
           var changeSet  = jsonStore._pending;
           var changes = {};
           changes.modified = [];
           var params = [];
           for (var i in changeSet._modifiedItems) { // modified item
          	var item = null;
          	if (jsonStore._itemsByIdentity) {
             		item = jsonStore._itemsByIdentity[i];
          	} else {
             		item = jsonStore._arrayOfAllItems[i];
          	}
          	changes.modified.push(itemToJS(jsonStore, item));
           }
           for (var i in changeSet._newItems) { // new item
          	var item = null;
          	if (jsonStore._itemsByIdentity) {
             		item = jsonStore._itemsByIdentity[i];
          	} else {
             		item = jsonStore._arrayOfAllItems[i];
          	}
          	changes.modified.push(itemToJS(jsonStore, item));
           }
           for (var i in changeSet._deletedItems) { // deleted item
          	var item = null;
          	if (jsonStore._itemsByIdentity) {
             		item = jsonStore._itemsByIdentity[i];
          	} else {
             		item = jsonStore._arrayOfAllItems[i];
          	}
          	changes.modified.push(itemToJS(jsonStore, item));
           }
           
           alert(dojo.toJson(changes, true));
           // all the items
           for(i=0; i<jsonStore._arrayOfAllItems.length; i++){
                params.push(itemToJS(jsonStore, jsonStore._arrayOfAllItems[i]));
           }
           alert(dojo.toJson(params,true));
           //send the changed data to sidgrid service
           var i;
           for(i=0; i<changes.modified.length; i++){
              var modified = changes.modified[i];
              console.debug(modified.name);
           }
           createXML(params);
	    saveComplete();
     } // end of _saveCustom
     //var button = dojo.byId("savexml");
     //dojo.connect(button, "onClick", jsonStore, "save");

 }
 function showXML(){
   //var jsonrpc = new JSONRpcClient("/jobsubportlets/JSON-RPC");
   var gadgetName = document.getElementById("title").value;
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
     console.log("have not define the name for the application yet");

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
// add a new Parameter
function addParameter(){
  var paraName = document.getElementById("paraName").value;
  var paraPrompt = document.getElementById("paraPrompt").value;
  var dataType = document.getElementById("datatype").value;
  var paraType = document.getElementById("paraType").value;
  // add this parameter to the list
  //validate the input for this argument
  //jsonrpc.AppMgrService.addParameter(paraName, paraPrompt, dataType, paraType);
  jsonStore.newItem({ name: paraName, prompt:paraPrompt, dataType:dataType, argType:paraType} );
  grid.edit.apply();
}
// TODO: edit a parameter, delete a parameter
function remParameter(){
  console.debug("to be implemented");
  grid.removeSelectedRows();
  
}
function loadParameters(){
  var paralist = jsonrpc.AppMgtService.getParameters();
  
}
// request the appMgtService to create a Mobyle XML
 function createXML(params){
   //validate the input
   var name = document.getElementById("name").value;
   var title = document.getElementById("title").value;
   var command = document.getElementById("command").value;
   var version = document.getElementById("version").value;
   var paralist = new Object();
   paralist.javaClass = 'java.util.List';
   paralist.list = [];
   for(var i=0; i<params.length; i++){
      var tmp = new Object;
      tmp.javaClass = 'org.uc.sidgrid.app.AppParameter';
      tmp.name = params[i].name;
      tmp.prompt = params[i].prompt;
      tmp.dataType = params[i].dataType;
      tmp.argType = params[i].argType;
      paralist.list[i]=tmp;
   }
   jsonrpc.AppMgr.createMobyleXML(name,command, title,paralist);
 }
 // request the appMgtService to create a gadget XML
 function genGadget(){
    var xmlfile = document.getElementById("mobyle_infile").value;
    if ( xmlfile == "" )
      alert("Please upload the XML file before submitting your application to the SIDGrid Gateway");
    else {
      var res = jsonrpc.AppMgr.uploadMobyleXML(xmlfile);
      if (res.status != 0 )
          alert(res.message);
      else
          alert("the application "+res.appName+" has been created !");
    }
 }
