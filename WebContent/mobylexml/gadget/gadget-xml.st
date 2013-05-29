<?xml version="1.0" encoding="UTF-8"?>
<Module>
<ModulePrefs title="$GadgetName$" scrolling="true" width="1000" height="600">
<Require feature="opensocial-0.8" />
<Require feature="views"/>
<Require feature="dynamic-height"/>
<Require feature="pubsub"/>
<OAuth>
      <Service>
        <Request url="$WebServerUrl$/request_token" />
        <Access url="$WebServerUrl$/access_token" />
        <Authorization url="$WebServerUrl$/authorize?oauth_callback=http://wwjmac.mcs.anl.gov:8080/gadgets/oauthcallback" />
      </Service>
    </OAuth>
    <Preload authz="oauth" href="$WebServerUrl$/JSON-RPC" />
</ModulePrefs>
<Content type="html">
<![CDATA[

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/jquery-ui.min.js"></script>
<link type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/themes/cupertino/jquery-ui.css" rel="stylesheet" />


<style>
            .parameters {
                width:95%;
            }
            .field_input {
                width:95%;
            }
            label {
                display:block;
                width:95%;
                color:gray;
                margin: 5px 10px 0px;
                padding: 2px;
                font-size:62%;
            }
            .form_parameter {
                margin: 0px 10px;
                padding: 5px;
                width:95%;
            }
            .form_parameter.text {
                margin-bottom:12px;
                width:95%;
                padding: 5px;
                margin:0px 10px;
            }
</style>



<link href="$WebServerUrl$/css/form.css" type="text/css" rel="stylesheet">
<link href="$WebServerUrl$/css/mobyle.css" type="text/css" rel="stylesheet">
<link href="$WebServerUrl$/css/swfupload.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="$WebServerUrl$/js/jsonrpc.js"></script>
<script type="text/javascript" src="$WebServerUrl$/js/util.js"></script>
<script type="text/javascript" src="$WebServerUrl$/js/popup.js"></script>
<script type="text/javascript" src="$WebServerUrl$/swfupload/swfupload.js"></script>
<script type="text/javascript" src="$WebServerUrl$/js/swfupload.queue.js"></script>
<script type="text/javascript" src="$WebServerUrl$/js/fileprogress.js"></script>
<script type="text/javascript" src="$WebServerUrl$/js/handlers.js"></script>
$JavaScript$

$HTMLSnippet$
]]>
</Content>
</Module>
