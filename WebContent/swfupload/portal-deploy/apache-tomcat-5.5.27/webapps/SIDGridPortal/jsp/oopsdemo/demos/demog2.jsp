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

	<title>OOPS Science Portal</title>

	<link rel="stylesheet" type="text/css" href="css/accordion.css" />
	<link rel="stylesheet" type="text/css" href="../lib/css/layout-default-latest.css" />
	        <LINK rel="stylesheet" type="text/css" href="css/themes/base/ui.core.css">
        <LINK rel="stylesheet" type="text/css" href="css/themes/base/ui.tabs.css">
        <LINK rel="stylesheet" type="text/css" href="css/themes/base/ui.all.css">
        <LINK rel="stylesheet" type="text/css" href="css/ui-1.7.1-demos.css">
        <LINK rel="stylesheet" type="text/css" href="css/layout-default.css">


<!-- CUSTOMIZE/OVERRIDE THE DEFAULT CSS -->
	<style type="text/css">

	/* remove padding and scrolling from elements that contain an Accordion OR a content-div */
	.ui-layout-center ,	/* has content-div */
	.ui-layout-west ,	/* has Accordion */
	.ui-layout-east ,	/* has content-div ... */
	.ui-layout-east .ui-layout-content { /* content-div has Accordion */
		padding: 0;
		overflow: scroll;
	}
	.ui-layout-center P.ui-layout-content {
		line-height:	1.4em;
		margin:			0; /* remove top/bottom margins from <P> used as content-div */
	}
	h3, h4 { /* Headers & Footer in Center & East panes */
		font-size:		1.1em;
		background:		#EEF;
		border:			1px solid #BBB;
		border-width:	0 0 1px;
		padding:		7px 10px;
		margin:			0;
	}
	.ui-layout-east h4 { /* Footer in East-pane */
		font-size:		0.9em;
		font-weight:	normal;
		border-width:	1px 0 0;
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


        <SCRIPT type="text/javascript" src="js/jquery-1.3.2.js"></SCRIPT>
        <!--
        <SCRIPT type="text/javascript" src="js/ui-1.7.1.custom.js"></SCRIPT>
        -->
        <SCRIPT type="text/javascript" src="js/ui-1.7.1/ui.core.js"></SCRIPT>
        <SCRIPT type="text/javascript" src="js/ui-1.7.1/ui.tabs.js"></SCRIPT>
        <SCRIPT type="text/javascript" src="js/ui-1.7.1/ui.sortable.js"></SCRIPT>

        <!-- using a beta Layout version because contains some bug-fixes relevant to this page  -->
        <SCRIPT type="text/javascript" src="jquery.layout_1.2.2beta.js"></SCRIPT>
        <SCRIPT type="text/javascript" src="js/ui-1.7.1/ui.draggable.js"></SCRIPT>
        <SCRIPT type="text/javascript" src="js/ui-1.7.1/effects.core.js"></SCRIPT>
        <SCRIPT type="text/javascript" src="js/ui-1.7.1/effects.slide.js"></SCRIPT>

        <SCRIPT type="text/javascript">

                $(document).ready( function() {

                        document.getElementById("username").innerHTML = user;

                        // TABS-WEST - sortable
                        $(".ui-layout-west-tabs")
                                .tabs()
                                .find(".ui-tabs-nav")
                                        .sortable({ axis: 'x', zIndex: 2 })
                        ;

                        // TABS-EAST - sortable
                        $(".ui-layout-east")
                                .tabs()
                                .find(".ui-tabs-nav")
                                        .sortable({ axis: 'x', zIndex: 2 })
                        ;

                        // TABS-CENTER - sortable
                        $(".ui-layout-center")
                                .tabs({
                                        change: function () {  }
                                })
                                .find(".ui-tabs-nav")
                                        .sortable({ axis: 'x', zIndex: 2 })
                        ;

                        // PAGE LAYOUT
                        $('body').layout({
                                west__size:                     $('body').innerWidth() * 0.4
                        ,       east__size:                     200
                        ,       south__initClosed:      true
                        });

                });

        </SCRIPT>


	<!-- REQUIRED scripts for layout widget -->
	<script type="text/javascript" src="../lib/js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../lib/js/jquery-ui-1.7.2.js"></script>
	<script type="text/javascript" src="../lib/js/jquery.layout-1.3.rc5.js"></script>
	<script type="text/javascript" src="../lib/js/debug.js"></script>

	<script type="text/javascript">

	var myLayout; // init global vars

	$(document).ready( function() {

		myLayout = $('body').layout({
			west__size:			300
		,	east__size:			300
			// RESIZE Accordion widget when panes resize
		,	west__onresize:		function () { $("#accordion1").accordion("resize"); }
		,	east__onresize:		function () { $("#accordion2").accordion("resize"); }
		});

		// ACCORDION - in the West pane
		$("#accordion1").accordion({
			fillSpace:	true
		});
		
		// ACCORDION - in the East pane - in a 'content-div'
		$("#accordion2").accordion({
			fillSpace:	true
		,	active:		1
		});

           my.init();my.renderGadgets();

	});

	</script>

 





<!-- default container look and feel -->
<link rel="stylesheet" href="gadgets.css">
<script type="text/javascript" src="http://sidgrid.ci.uchicago.edu:8888/gadgets/js/pubsub.js?c=1&debug=1"></script>
<script type="text/javascript" src="util.js"></script>
<script type="text/javascript" src="gadgets.js"></script>
<script type="text/javascript">

var user = "<%=session.getAttribute("username") %>";
var my = {};

my.gadgetSpecUrls = [
"http://communicado.ci.uchicago.edu:8888/SIDGridGadgets/oops2.xml",
"http://communicado.ci.uchicago.edu:8888/SIDGridGadgets/oops-preview3.xml",
"http://communicado.ci.uchicago.edu:8888/SIDGridGadgets/filebrowser4.xml",
"http://communicado.ci.uchicago.edu:8888/SIDGridGadgets/wfhistory-oops3.xml",
];

my.LayoutManager = function() {
  gadgets.LayoutManager.call(this);
};

my.LayoutManager.inherits(gadgets.LayoutManager);

my.LayoutManager.prototype.getGadgetChrome = function(gadget) {
  var chromeId = 'gadget-chrome-' + gadget.id;
  return chromeId ? document.getElementById(chromeId) : null;
};

my.init = function() {
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

function log(message) {
  document.getElementById("output").innerHTML += gadgets.util.escapeString(message) + "<br/>";
}
</script>









</head>
<body>

<div class="ui-layout-north" style="display: none;">OOPS Science Portal
<div style="float:right"><div style="float:left">Logged in as&nbsp; </div><div id=username style="font-weight:bold;float:left"></div>&nbsp;<a href=/SIDGridPortal/jsp/logout.jsp>Logout</a></div></div>

<div class="ui-layout-center" style="display: none;"> 





<DIV id="main_app_tabs" class="ui-layout-west-tabs">        
<UL>                
    <LI><A href="#tabs-west-1">Run Simulation</A></LI>                
    <LI><A href="#tabs-west-2">View Results</A></LI>
    <!-- <LI><A href="#tabs-west-3">Something Else</A></LI>         -->
</UL>        

<!-- add wrapper that Layout will auto-size to 'fill space' -->   
        <DIV class="ui-layout-content" style="height:1000px">                
            <DIV id="tabs-west-1">     
                <div id="gadget-chrome-0" class="gadgets-gadget-chrome"></div>
            </DIV>
            <DIV id="tabs-west-2">  
                <div id="gadget-chrome-1" class="gadgets-gadget-chrome"></div>
            </DIV>
        </DIV><!--- END content-body --->
</DIV>

  <div id="output" style="clear: left;display:none"></div>



</div>

<div class="ui-layout-west" style="display: none;">
	<div id="accordion1" class="basic">
			<a href="#">Inputs</a>
			<div>
                            <div id="gadget-chrome-2" class="gadgets-gadget-chrome"></div>
			</div>

			<a href="#">Workflows</a>
			<div>
                            <div id="gadget-chrome-3" class="gadgets-gadget-chrome"></div>
			</div>
	</div>
</div>


</body>
</html> 
