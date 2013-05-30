<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head>
    <link href="../css/default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../swfupload/swfupload.js"></script>
	<script type="text/javascript" src="../js/swfupload.queue.js"></script>
	<script type="text/javascript" src="../js/fileprogress.js"></script>
	<script type="text/javascript" src="../js/handlers.js"></script>
	<script type="text/javascript" src="../js/jsonrpc.js"></script>
	<script type="text/javascript">
		var upload1, upload2;

		window.onload = function() {
			upload1 = new SWFUpload({
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
				upload_success_handler : uploadSuccess,
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
					fileInputTarget: "praat_infile",
				},
				
				// Debug Settings
				debug: false
			});

			upload2 = new SWFUpload({
				// Backend Settings
				upload_url: "../fileupload",	// Relative to the SWF file (or you can use absolute paths)
				post_params: { "ChannelID": "28", "Type": "Type2" },

				// File Upload Settings
				file_size_limit : "1024000",	// 200 kb
				file_types : "*.wav",
				file_types_description : "Audio Wav Files",
				file_upload_limit : "10",
				file_queue_limit : "2",

				// Event Handler Settings (all my handlers are in the Handler.js file)
				file_dialog_start_handler : fileDialogStart,
				file_queued_handler : fileQueued,
				file_queue_error_handler : fileQueueError,
				file_dialog_complete_handler : fileDialogComplete,
				upload_start_handler : uploadStart,
				upload_progress_handler : uploadProgress,
				upload_error_handler : uploadError,
				upload_success_handler : uploadSuccess,
				upload_complete_handler : uploadComplete,

				// Button Settings
				button_image_url : "../images/XPButtonUploadText_61x22.png",
				button_placeholder_id : "spanButtonPlaceholder2",
				button_width: 61,
				button_height: 22,
				
				// Flash Settings
				flash_url : "../swfupload/swfupload.swf",

				swfupload_element_id : "flashUI2",		// Setting from graceful degradation plugin
				degraded_element_id : "degradedUI2",	// Setting from graceful degradation plugin

				custom_settings : {
					progressTarget : "fsUploadProgress2",
					cancelButtonId : "btnCancel2",
					currentFileId  : "currentFileId",
					fileInputTarget: "praat_inscript",
				},

				// Debug Settings
				debug: false
			});

	     }
	</script>
	<script>
	  function run(){
	    var params = new Object();
  	    params.javaClass = 'java.util.Hashtable';
  	    params.map = {};
        // find the url 
  		var inscript = document.getElementById("praat_inscript");
  		params.map['inscript'] = inscript.value; 
  		var infile = document.getElementById("praat_infile");
  		params.map['infile'] = infile.value; 
  		var outfile = document.getElementById("praat_outfile");
  		params.map['outfile'] = outfile.value; 
        var jsonrpc = new JSONRpcClient("/SIDGridPortal/Old-JSONRPC");
  		result = jsonrpc.WorkflowService.runWorkflow("wwjag","praat", params);
  		alert(result);
       }  
	</script>
</head>
<body>

<div class="formHead">
<span class="formTitle">
<h1>Praat: Audio Analysis</h1>
<h2>Do video/audio conversion</h2>
</span><span class="formCtrl"><label for="form_email_valuepraat">        
              E-mail:
            </label><input name="email" type="text" id="form_email_valuepraat" class="mandatory">
            <input type="button" value="Reset" id="reset_praat">
            <input type="submit" value="Run" id="submit_praat" onclick="run();">
            </span><span class="formCtrl"><input type="button" style="display:none" value="ask for help" id="helpdisplay_praat"><input type="hidden" name="programName" value="praat"></span>
</div>

<form id="form1" action="runPraat.jsp" method="post" enctype="multipart/form-data">
<div class="parameters">
    
<div class="parameter">
<fieldset class="fieldset">
<legend><label for="praat_inscript" id="for_praat_inscript" class=" mandatory">praat script file</label>
      (File)
      </legend><span class="field_input">
    	 the name of a <strong>file</strong>:
    	 <div id="flashUI1">
			<div class="fieldset flash" id="fsUploadProgress1">
					<span class="legend">praat script file</span>
			</div>
    	  <!-- input type="file" id="praat_inscript" name="praat_inscript" class="form_parameter mandatory" -->
    	 <div style="padding-left: 5px;">
    	   <span id="spanButtonPlaceholder1"></span>
    	  <input id="btnCancel1" type="button" value="Cancel Uploads" onclick="cancelQueue(upload1);" disabled="disabled" style="margin-left: 2px; height: 22px; font-size: 8pt;" />
    	 </div>
		</div>
</span>
</fieldset>
</div>
    
    
<div class="parameter">
<fieldset class="fieldset">
<legend><label for="praat_infile" id="for_praat_infile" class=" mandatory">praat data wave file</label>
      (File)
      </legend><span class="field_input">
      the name of a <strong>file</strong>:
    	  <!-- input type="file" id="praat_infile" name="praat_infile" class="form_parameter mandatory" -->
    	  <div>
				<div class="fieldset flash" id="fsUploadProgress2">
							<span class="legend">praat data wave file</span>
		  </div>
		 <div style="padding-left: 5px;">
							<span id="spanButtonPlaceholder2"></span>
							<input id="btnCancel2" type="button" value="Cancel Uploads" onclick="cancelQueue(upload2);" disabled="disabled" style="margin-left: 2px; height: 22px; font-size: 8pt;" />
							<br />
		 </div>
		</div>
</span>
</fieldset>
</div>

</form>

<input type="hidden" name="praat_infile"  id="praat_infile">
<input type="hidden" name="praat_inscript" id="praat_inscript" >
<input type="hidden" name="praat_outfile" id="praat_outfile" value="output">    
    
  
</div><fieldset class="program_information">
<div class="reference"> Praat: doing phonetics by computer http://www.fon.hum.uva.nl/praat/</div>
<div class="authors"></div>
</fieldset>

</body>
</html>
