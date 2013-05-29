<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" 
"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%
 response.setHeader("Cache-Control","no-store"); 
 response.setHeader("Pragma","no-cache"); 
 response.setDateHeader ("Expires", 0);

%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="language" content="en" />

	<title>Gadget Workspace Run Applications</title>

	<link rel="stylesheet" type="text/css" href="../css/layout-default-latest.css" />
<!--
        <LINK rel="stylesheet" type="text/css" href="../css/themes/base/ui.core.css">
        <LINK rel="stylesheet" type="text/css" href="../css/themes/base/ui.tabs.css">
        <LINK rel="stylesheet" type="text/css" href="../css/themes/base/ui.all.css">
        <LINK rel="stylesheet" type="text/css" href="../css/ui-1.7.1-demos.css">
-->
        <LINK rel="stylesheet" type="text/css" href="../css/layout-default.css">
<link type="text/css" href="http://www.mcs.anl.gov/~turam/css/cupertino/jquery-ui-1.7.2.custom.css" rel="stylesheet" />

<!-- CUSTOMIZE/OVERRIDE THE DEFAULT CSS -->
	<style type="text/css">

	    /* remove padding and scrolling from elements that contain an Accordion OR a content-div */
	    .ui-layout-center ,	/* has content-div */
	    .ui-layout-west 	/* has Accordion */
	    .ui-layout-content { /* content-div has Accordion */
		    padding: 0;
		    //overflow: scroll;
	    }
	    .ui-layout-north P.ui-layout-content {
		    line-height:	1.4em;
            }
	    .ui-layout-northcentral P.ui-layout-content {
		    line-height:	1.4em;
            }
	    .ui-layout-center P.ui-layout-content {
		    line-height:	1.4em;
		    margin:			0; /* remove top/bottom margins from <P> used as content-div */
                    overflow: visible;
		    //overflow: scroll;
	    }
	    h3, h4 { /* Headers & Footer in Center & East panes */
		    font-size:		1.1em;
		    background:		#EEF;
		    border:			1px solid #BBB;
		    border-width:	0 0 1px;
		    padding:		7px 10px;
		    margin:			0;
	    }
            body {
			font-size: 12px;
			font-family: Myriad,Helvetica,Tahoma,Arial,clean,sans-serif;
	    }
	</style>


        <STYLE type="text/css">

                /*
                 *      TAB-THEME ADJUSTMENTS
                 */
                .ui-tabs-nav li {
                        white-space:    nowrap;
                }
                .ui-tabs-nav li a {
                        font-size:              1em !important;
                        padding:                4px 1.5ex 3px !important;
                }
                .ui-tabs-panel {
                        font-size:              1em !important;
                        padding:                0 1em !important;
                }

                /*
                 *      WEST-PANE TABS
                 *
                 *      These tabs 'fill' the pane,
                 *      so the pane-border acts as the tab-border
                 */
                .ui-layout-pane-west {
                        padding:        0;
                        }
                        .ui-layout-pane-west .ui-tabs-nav {
                                /* don't need border or rounded corners - tabs 'fill' the pane */
                                border-top:             0;
                                border-left:    0;
                                border-right:   0;
                                padding-bottom: 0 !important;
                                -moz-border-radius:             0;
                                -webkit-border-radius:  0;
                        }

         </STYLE>


	<!-- REQUIRED scripts for layout widget -->
	<script type="text/javascript" src="oopsdemo/lib/js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="oopsdemo/lib/js/jquery-ui-1.7.2.js"></script>
	<script type="text/javascript" src="oopsdemo/lib/js/jquery.layout-1.3.rc5.js"></script>
	<script type="text/javascript" src="oopsdemo/lib/js/debug.js"></script>
       <script type="text/javascript" src="<%=request.getContextPath()%>/js/jsonrpc.js"></script>

        <SCRIPT type="text/javascript">



               $(document).ready( function() {

                        document.getElementById("username").innerHTML = user;

                        // TABS-WEST - sortable
                        $(".ui-layout-west-tabs")
                                .tabs()
                                .find(".ui-tabs-nav")
                                        .sortable({ axis: 'x', zIndex: 2 })
                        ;

                        // TABS-CENTER - sortable
                        $(".ui-layout-center-tabs")
                                //.tabs({selected:selectedTab})
                                .tabs({selected:0})
                                .find(".ui-tabs-nav")
                                        .sortable({ axis: 'x', zIndex: 2 })
                        ;
 /*

                        // PAGE LAYOUT
                        $('body').layout({
                                west__size:                     $('body').innerWidth() * 0.2
			,	west__onresize:		'westLayout.resizeAll'
			,	center__onresize:	'eastLayout.resizeAll'
                        });
*/
                });
        </SCRIPT>

	<script type="text/javascript">
	    $(document).ready( function() {
	    	alert('<%=request.getContextPath()%>');
	    	alert("1")
               my.init();
               alert("2")
               my.renderGadgets();
               alert("3")
               my.listGadgets();
               alert("4")
               my.selectGadget()
               alert("5")
               my.changeSimGadget()
               alert("6")
	    });
	</script>

<!-- default container look and feel -->
<link rel="stylesheet" href="../css/gadgets.css">
<script type="text/javascript" src="/gadgets/js/pubsub.js?c=1&debug=1"></script>
<script type="text/javascript" src="../js/util.js"></script>
<script type="text/javascript" src="../js/gadgets.js"></script>
<script type="text/javascript">

	var user = "<%=session.getAttribute("username") %>";
    var selectedTab = '<%= request.getParameter("app")==null?1:0 %>'
    var selectedApp = '<%= request.getParameter("app")==null?"":request.getParameter("app")  %>'
	var my = {};
	var webroot = "http://"+document.location.hostname+":"+document.location.port+"/";
	my.gadgetSpecUrls = [
	 webroot+"<%=request.getContextPath()%>/gadgets/stub.xml",
	 webroot+"<%=request.getContextPath()%>/gadgets/preview.xml",
	 webroot+"<%=request.getContextPath()%>/gadgets/wfhistory.xml",
	];
	
	alert(my.gadgetSpecUrls)
	
	my.LayoutManager = function() {
	  gadgets.LayoutManager.call(this);
	};
	
	my.LayoutManager.inherits(gadgets.LayoutManager);
	
	my.LayoutManager.prototype.getGadgetChrome = function(gadget) {
	  var chromeId = 'gadget-chrome-' + gadget.id;
	  return chromeId ? document.getElementById(chromeId) : null;
	};
	
	my.init = function() {
		alert("11")
		
	  gadgets.pubsubrouter.init(function(id) {
		  
	    return my.gadgetSpecUrls[parseInt(id[id.length - 1])];
	  }, {
	    onSubscribe: function(sender, channel) {
	      log(sender + " subscribes to channel '" + channel + "'");
	      // return true to reject the request.
	    },
	    onUnsubscribe: function(sender, channel) {
	      log(sender + " unsubscribes from channel '" + channel + "'");
	      // return true to reject the request.
	    },
	    onPublish: function(sender, channel, message) {
	      log(sender + " publishes '" + message + "' to channel '" + channel + "'");
	      // return true to reject the request.
	    }
	  });
		alert("12")
	  gadgets.container.layoutManager = new my.LayoutManager();
	};
	
	my.renderGadgets = function() {
	    var secureToken = user+":"+user+":appid:cont:url:0";
	    for (var i = 0; i < my.gadgetSpecUrls.length; ++i) {
	        var gadget = gadgets.container.createGadget(
	            {specUrl: my.gadgetSpecUrls[i], secureToken:secureToken});
	            //{specUrl: my.gadgetSpecUrls[i], title: (i ? "Subscriber" : "Publisher"), secureToken:secureToken });
	        gadgets.container.addGadget(gadget);
	        gadgets.container.renderGadget(gadget);
	    }
	};
	my.changeSimGadget = function(){
         var gadgetList = document.getElementById("gadgetList");
         var newUrl = gadgetList.options[gadgetList.selectedIndex].value;
         //var url = document.getElementById("gadgetUrl");
         //url.value = newUrl;
         var secureToken = user+":"+user+":appid:cont:newUrl:0";
         var gadget = gadgets.container.createGadget({specUrl: newUrl, secureToken: secureToken, id:0});
         // remove the old sim gadget in the container
         var chrome= document.getElementById("gadget-chrome-0");
         while(chrome.firstChild){
           chrome.removeChild(chrome.firstChild);
         }
         gadgets.container.gadgets_[0] = gadget;          
         gadgets.container.renderGadget(gadget);
       };
    my.selectGadget = function(){
        var gadgetList = document.getElementById("gadgetList");
        gadgetList.selectedIndex = gadgetList.options.length-1
    }

    my.listGadgets = function(){
        var jsonrpc = new JSONRpcClient("<%=request.getContextPath()%>/Old-JSONRPC");
        var result = jsonrpc.AppMgr.showAllApplication();
        result.list.sort(function(a,b) { if( a.appName < b.appName ) { return -1 } else { return 1 } } )
        var gadgetList = document.getElementById("gadgetList");
        gadgetList.remove(0)
        for( var i=0; i<result.list.length; i++){

            // do not show "script" type applications; they'll be included by the showAllScripts block below
            if( result.list[i].type === "cmd") {

                //display the gadgets
                // get the current server address
                var rootUrl = (document.location.href).split("/");
	            var gadgetUrl = rootUrl[0]+"//"+rootUrl[1]+rootUrl[2]+"/GreenPipe/SIDGridGadgets/"+result.list[i].appName+".xml";
	            o = new Option(result.list[i].appName, gadgetUrl);
	            gadgetList.options[gadgetList.length] = o;
            }
	 
            if (result.list[i].type == 'script'){
                var appName = result.list[i].appName;
  	            var script_result = jsonrpc.AppMgr.showAllScripts(appName);
                if( script_result )  {
                    script_result.list.sort(function(a,b) { 
                       if( a.scriptName < b.scriptName ) { return -1 } else { return 1 } 
                       });
 
  	                for( var j=0; j<script_result.list.length; j++) {
       	                var rootUrl = (document.location.href).split("/");
       	                var scriptname = script_result.list[j].scriptName;
                        var gadgetUrl;
       	                if (script_result.list[j].gadgetLink != null ) {
                            //gadgetUrl = script_result.list[j].gadgetLink;
                            var rootUrl = (document.location.href).split("/");
	                        var gadgetUrl = rootUrl[0]+"//"+rootUrl[1]+rootUrl[2]+"/GreenPipe/SIDGridGadgets/"+result.list[i].appName+".xml";
                            gadgetList.options[gadgetList.length] = new Option(scriptname , gadgetUrl);
                        }
                    }
                }
  	        }
       }
    }
	function log(message) {
	    document.getElementById("output").innerHTML += gadgets.util.escapeString(message) + "<br/>";
	}

</script>
</head>
<body>
    <%@ include file="nav.html" %>

	<div class="ui-layout-north" style="display: none;">
            <%@ include file="nav.html" %>
	</div>
	<div class="ui-layout-west" style="float:left;width:18%;margin:5px;">
	    <DIV id="left_column_tabs" class="ui-widget">
	        <DIV class="sw-header ui-widget-header ui-corner-all" style="padding:5px;font-weight:normal;font-size:14px">
	        Workflows
            </DIV>
	        <DIV class="ui-widget-content">
	            <DIV id="tabs-west-1">     
	                <div id="gadget-chrome-2" class="gadgets-gadget-chrome" style="float:left;"></div>
                    </DIV>
	        </DIV>
	    </DIV>
	</div>
	
	
	<div class="ui-layout-center" style="display: block;float:left;width:79%;margin:0px;"> 
	    <DIV id="main_app_tabs" class="ui-layout-center-tabs" style="height:100%;margin:3px">
	        <UL style='padding:0'>                
	            <LI><A href="#tabs-center-1">Run Simulations</A></LI>
	            <LI><A href="#tabs-center-2">View Results</A></LI>
	        </UL>        
	    
	        <!-- add wrapper that Layout will auto-size to 'fill space' -->   
	        <DIV class="ui-layout-content">
	            <DIV id="tabs-center-1">     
                       <select id="gadgetList" onchange="my.changeSimGadget();">
                          <option value="/SIDGridGadgets/sgflow/sgflow(1)/sgflow.xml">sgflow(1)</option>
                       </select>

	                <div id="gadget-chrome-0" class="gadgets-gadget-chrome" style="float:left;width:100%"></div>
	            </DIV>
	            <DIV id="tabs-center-2">  
	                <div id="gadget-chrome-1" class="gadgets-gadget-chrome" style="float:left;width:100%"></div>
	            </DIV>
	        </DIV><!--- END content-body --->
	    </DIV>
	
	    <div id="output" style="clear: left;display:none"></div>
	
	</div>
	

</body>
</html> 
