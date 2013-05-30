upload_$Application$_$arg_name$ = new SWFUpload({
				// Backend Settings
				upload_url: "$UploadServerUrl$/fileupload",	// Relative to the SWF file (or you can use absolute paths)
				post_params: { "ChannelID": "28", "Type": "Type2" },

				// File Upload Settings
				file_size_limit : "2000000",	//?? How can we set this, need a size attribute in the mobyle schema
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
				button_image_url : "$WebServerUrl$/images/XPButtonUploadText_61x22.png",
				button_placeholder_id : "spanButtonPlaceholder_$Application$_$arg_name$",
				button_width: 61,
				button_height: 22,
				
				// Flash Settings
				flash_url : "$WebServerUrl$/swfupload/swfupload.swf",
				

				custom_settings : {
					progressTarget : "fsUploadProgress_$Application$_$arg_name$",
					cancelButtonId : "btnCancel_$Application$_$arg_name$",
					currentFileId  : "currentFileId",
					fileInputTarget: "$Application$_$arg_name$",
				},
				
				// Debug Settings
				debug: false
			});
