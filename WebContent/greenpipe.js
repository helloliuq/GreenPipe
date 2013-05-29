var demoLanguage = {

		// Set a unique name for the language
		languageName: "workflowLanguage",

		// inputEx fields for pipes properties
		propertiesFields: [
		                   // default fields (the "name" field is required by the WiringEditor):
		                   {"type": "string", inputParams: {"name": "name", label: "Name", typeInvite: "Enter a name" } },
		                   {"type": "text", inputParams: {"name": "description", label: "Description", cols: 30} },

		                   // Additional fields
		                   {"type": "boolean", inputParams: {"name": "isTest", value: true, label: "Test"}},
		                   {"type": "select", inputParams: {"name": "category", label: "Category", selectValues: ["Demo", "Test", "Other"]} }
		                   ],

		                   // List of node types definition
		                   modules: [

		                             {
		                            	 "name": "WorkFlow Block",
		                            	 "container": {
		                            		 "xtype": "WireIt.FormContainer",
		                            		 "title": "WorkFlowContainer",    
		                            		 "icon": "../../res/icons/application_edit.png",

		                            		 "collapsible": true,
		                            		 "fields": [ 
		                            		            {"type": "string", "inputParams": {"label": "Block Name", "name": "blockName", "required": true } },
		                            		            {"type": "string", "inputParams": {"label": "Command File", "name": "commandFile", "required": true } },
		                            		            {"type": "group",  "inputParams": {
		                            		            	"name": "output",
		                            		            	"collapsible": true,
		                            		            	"legend": "Ouput Data",
		                            		            	"fields":[
		                            		            	          {"type": "radio",  "inputParams": {"label": "Location Type:", "name": "locationType", "choices": ["LFS","DFS"], "values": ["lfs","dfs"], "required": true } },
		                            		            	          {"type": "string", "inputParams": {"label": "Location:", "name": "location", "required": true } },
		                            		            	          {"type": "select", "inputParams": {"label": "Data Type:", "name": "dataType", "selectValues": ["ss2","vv2","fasta","acc","pssm"], "required": true}}
		                            		            	          ]
		                            		            }
		                            		            }
		                            		            ],
		                            		            "legend": "Tell us about your workflow",
		                            		            "terminals":[
		                            		                         {"name": "input", "direction": [0,-1], "offsetPosition": {"left": 160, "top": -15}, "ddConfig": {"type":"input", "allowedTypes": ["output"]}},
		                            		                         {"name": "output", "direction": [0,1], "offsetPosition": {"left": 160, "top": 158}, "ddConfig": {"type":"output", "allowedTypes": ["input"]}}
		                            		                         ]
		                            	 }
		                             },

		                             {
		                            	 "name": "Input Data",
		                            	 "container": {
		                            		 "xtype": "WireIt.FormContainer",
		                            		 "icon": "../../res/icons/arrow_right.png",
		                            		 "title": "Input Data",
		                            		 "fields": [
		                            		            {"type":"radio",  "inputParams": {"label": "Location Type:", "name": "locationType", "choices": ["LFS","DFS"], "values":["lfs","dfs"], "required": true } },
		                            		            {"type":"string", "inputParams": {"label": "Location:", "name": "location", "required": true}},
		                            		            {"type":"select", "inputParams": {"label": "Data Type:", "name": "dataType", "selectValues": ["ss2","vv2","fasta","acc","pssm"], "required": true}}
		                            		            ],
		                            		            "terminals":[
		                            		                         {"name": "input", "direction": [0,1], "offsetPosition": {"left": 160, "bottom": -15}, "ddConfig": {"type":"output", "allowedTypes": ["input"]}}
		                            		                         ]

		                            	 }
		                             },

		                             {
		                            	 "name": "In&Out Combinationer",
		                            	 "container": {
		                            		 "xtype":"WireIt.ImageContainer", 
		                            		 "image": "../logicGates/images/gate_and.png",
		                            		 "icon": "../../res/icons/arrow_join.png",
		                            		 "terminals": [
		                            		               {"name": "input1", "direction": [0,-1], "offsetPosition": {"left": -3, "top": 2 }, "ddConfig": {"type":"input", "allowedTypes": ["output"] } },                        		    
		                            		               {"name": "input2", "direction": [0,-1], "offsetPosition": {"left": -3, "top": 37 }, "ddConfig": {"type":"input", "allowedTypes": ["output"] } },
		                            		               {"name": "output", "direction": [0,1], "offsetPosition": {"left": 103, "top": 20 }, "ddConfig": {"type":"output", "allowedTypes": ["input"] } }
		                            		               ]
		                            	 }
		                             }

		                             ]

};