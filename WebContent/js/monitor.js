(function() {

	/**
	 * Create a XMLHttpRequest
	 * @Method createXHR
	 */
	createXHR = function(){
		if (typeof XMLHttpRequest != "undefined"){
			return new XMLHttpRequest();
		} else if (typeof ActiveXObject != "undefined"){
			if (typeof arguments.callee.activeXString != "string"){
				var versions = ["MSXML2.XMLHttp.6.0", "MSXML2.XMLHttp.3.0",
				                "MSXML2.XMLHttp"];

				for (var i=0,len=versions.length; i < len; i++){
					try {
						var xhr = new ActiveXObject(versions[i]);
						arguments.callee.activeXString = versions[i];
						return xhr;
					} catch (ex){
						//skip
					}
				}
			}

			return new ActiveXObject(arguments.callee.activeXString);
		} else {
			throw new Error("No XHR object available.");
		}
	};

	/**
	 * Select workflows
	 */
	(function(){

		var workflows;

		var xhr = createXHR();

		xhr.onreadystatechange = function() {
			if(xhr.readyState == 4) {
				if((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304) {
					workflows = eval("(" + xhr.responseText + ")");
				} else {
					alert("Request was unsuccessful 1: " + xhr.statusText);
				}
			}
		};

		xhr.open("get", "/GreenPipe/servlet/DisplayWorkflow", false);
		xhr.send(null);

		var selectNode =  document.getElementById("workflows");

		EventUtil.addHandler(selectNode, "change", display);

		var length = workflows.length;
		for(var i = 0; i < length; i++) {
			var workflow = workflows[i];

			var optionNode = document.createElement("option");
			optionNode.value = workflow.id;;
			var optionText = document.createTextNode(workflow.id + " " + workflow.name);
			optionNode.appendChild(optionText);

			if(i == length - 1) {
				optionNode.selected = true;
			}

			selectNode.appendChild(optionNode);	
		}

		//init workflow
		display();

	})();


	var workflowInterval;
	var blocksInterval;

	function display(){

		clearInterval(workflowInterval);
		clearInterval(blocksInterval);

		var id;
		var status;
		var jobs = [];
		var pipe = {};

		/**
		 * display workflow
		 */
		(function(){

			var workflow;

			/*
			 * get the workflow id
			 */
			var selectNode = document.getElementById("workflows");
			var options = selectNode.options;
			var selectedIndex = selectNode.selectedIndex;
			id = options[selectedIndex].value;

			/*
			 * use ajax to communicate with server 
			 */
			var xhr = createXHR();

			xhr.onreadystatechange = function() {
				if(xhr.readyState == 4) {
					if((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304) {
						workflow = eval("(" + xhr.responseText + ")");
					} else {
						alert("Request was unsuccessful2: " + xhr.statusText);
					}
				}
			};

			xhr.open("get", "/GreenPipe/servlet/DisplayWorkflow?id=" + id, false);
			xhr.send(null);

			/*
			 * clean the old workflow information
			 */
			var workflowSheet = document.getElementById("workflow");

			var nodeList = workflowSheet.getElementsByTagName("tr");

			var nodeSize = nodeList.length - 1;

			for(var t = 0; t < nodeSize; t++) {
				var node = document.getElementById("greenpipe");
				workflowSheet.removeChild(node);
			}

			/*
			 * write the new workflow information
			 */
			pipe.workflowNode = document.createElement("tr");
			pipe.workflowNode.id = "greenpipe";

			//id
			pipe.idNode = document.createElement("td");
			pipe.idNode.className = "format";
			pipe.idText = document.createTextNode(workflow.id);
			pipe.idNode.appendChild(pipe.idText);
			pipe.workflowNode.appendChild(pipe.idNode);

			//name
			pipe.nameNode = document.createElement("td");
			pipe.nameNode.className = "format";
			pipe.nameText = document.createTextNode(workflow.name);
			pipe.nameNode.appendChild(pipe.nameText);
			pipe.workflowNode.appendChild(pipe.nameNode);

			//username
			pipe.usernameNode = document.createElement("td");
			pipe.usernameNode.className = "format";
			pipe.usernameText = document.createTextNode(workflow.username);
			pipe.usernameNode.appendChild(pipe.usernameText);
			pipe.workflowNode.appendChild(pipe.usernameNode);
			
			//status
			pipe.statusNode = document.createElement("td");
			pipe.statusNode.className = "format";
			pipe.statusText = document.createTextNode("");
			transformStatus(pipe.statusText, workflow.status);
			pipe.statusNode.appendChild(pipe.statusText);
			pipe.workflowNode.appendChild(pipe.statusNode);
			
			//start_time
			pipe.startTimeNode = document.createElement("td");
			pipe.startTimeNode.className = "format";
			pipe.startTimeText = document.createTextNode("");
			if(workflow.startTime != null) {
				pipe.startTimeText.nodeValue = workflow.startTime;
			}
			pipe.startTimeNode.appendChild(pipe.startTimeText);
			pipe.workflowNode.appendChild(pipe.startTimeNode);

			//running_time
			pipe.runningTimeNode = document.createElement("td");
			pipe.runningTimeNode.className = "format";
			pipe.runningTimeText = document.createTextNode("");
			if(workflow.runningTime != null) {
				pipe.runningTimeText.nodeValue = workflow.runningTime;
			}
			pipe.runningTimeNode.appendChild(pipe.runningTimeText);
			pipe.workflowNode.appendChild(pipe.runningTimeNode);

			//finish_time
			pipe.finishTimeNode = document.createElement("td");
			pipe.finishTimeNode.className = "format";
			pipe.finishTimeText = document.createTextNode("");
			if(workflow.finishTime != null) {
				pipe.finishTimeText.nodeValue = workflow.finishTime;
			}
			pipe.finishTimeNode.appendChild(pipe.finishTimeText);
			pipe.workflowNode.appendChild(pipe.finishTimeNode);

			workflowSheet.appendChild(pipe.workflowNode);

			/*
			 * get the status of this workflow
			 */
			status = workflow.status;

		})();

		/**
		 * display blocks
		 */
		(function(){

			var blocks;

			/*
			 * use ajax to communicate with server 
			 */
			var xhr = createXHR();

			xhr.onreadystatechange = function() {
				if(xhr.readyState == 4) {
					if((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304) {
						blocks = eval("(" + xhr.responseText + ")");
					} else {
						alert("Request was unsuccessful3: " + xhr.statusText);
					}
				}
			};

			xhr.open("get", "/GreenPipe/servlet/DisplayBlock?id=" + id, false);
			xhr.send(null);

			/*
			 * clean the old blocks information
			 */
			var blockSheet = document.getElementById("blocks");

			var nodeList = blockSheet.getElementsByTagName("tr");

			var nodeSize = nodeList.length - 1;

			for(var t = 0; t < nodeSize; t++) {
				var node = document.getElementById("block" + t);
				blockSheet.removeChild(node);
			}

			/*
			 * write the new blocks information
			 */
			var length = blocks.length;

			for(var i = 0; i < length; i++) {

				var job = {};

				var block = blocks[i];

				job.blockNode = document.createElement("tr");
				job.blockNode.id = "block" + i;

				//id
				job.idNode = document.createElement("td");
				job.idNode.id = "id" + i;
				job.idNode.className = "format";
				job.idText = document.createTextNode(block.id);
				job.idNode.appendChild(job.idText);
				job.blockNode.appendChild(job.idNode);

				//name
				job.nameNode = document.createElement("td");
				job.nameNode.id = "name" + i;
				job.nameNode.className = "format";
				job.nameText = document.createTextNode(block.name);
				job.nameNode.appendChild(job.nameText);
				job.blockNode.appendChild(job.nameNode);

				//status
				job.statusNode = document.createElement("td");
				job.statusNode.id = "status" + i;
				job.statusNode.className = "format";
				job.statusText = document.createTextNode("");
				transformStatus(job.statusText, block.status);
				job.statusNode.appendChild(job.statusText);
				job.blockNode.appendChild(job.statusNode);

				//wait_time
				job.waitTimeNode = document.createElement("td");
				job.waitTimeNode.id = "waitTime" + i;
				job.waitTimeNode.className = "format";
				job.waitTimeText = document.createTextNode("");
				if(block.waitTime != null) {
					job.waitTimeText.nodeValue = block.waitTime;
				}
				job.waitTimeNode.appendChild(job.waitTimeText);
				job.blockNode.appendChild(job.waitTimeNode);

				//start_time
				job.startTimeNode = document.createElement("td");
				job.startTimeNode.id = "startTime" + i;
				job.startTimeNode.className = "format";
				job.startTimeText = document.createTextNode("");
				if(block.startTime != null) {
					job.startTimeText.nodeValue = block.startTime;
				}
				job.startTimeNode.appendChild(job.startTimeText);
				job.blockNode.appendChild(job.startTimeNode);

				//progress
				job.progressNode = document.createElement("td");
				job.progressNode.id = "progress" + i;
				job.progressNode.className = "format";
				job.progressText = document.createTextNode(block.progress + "%");
				job.progressNode.appendChild(job.progressText);
				job.blockNode.appendChild(job.progressNode);

				//running_time
				job.runningTimeNode = document.createElement("td");
				job.runningTimeNode.id = "runningTime" + i;
				job.runningTimeNode.className = "format";
				job.runningTimeText = document.createTextNode("");
				if(block.runningTime != null) {
					job.runningTimeText.nodeValue = block.runningTime;
				}
				job.runningTimeNode.appendChild(job.runningTimeText);
				job.blockNode.appendChild(job.runningTimeNode);

				//finish_time
				job.finishTimeNode = document.createElement("td");
				job.finishTimeNode.id = "finishTime" + i;
				job.finishTimeNode.className = "format";
				job.finishTimeText = document.createTextNode("");
				if(block.finishTime != null) {
					job.finishTimeText.nodeValue = block.finishTime;
				}
				job.finishTimeNode.appendChild(job.finishTimeText);
				job.blockNode.appendChild(job.finishTimeNode);

				blockSheet.appendChild(job.blockNode);

				jobs.push(job);
			}

		})();

		/**
		 * check the status to decide if update this pipe
		 */
		if(status == 0 || status == 1 || status == 2 || status ==3) {
			workflowInterval = setInterval(updateWorkflow, 6000);
			blocksInterval = setInterval(updateBlocks, 3000);
		}

		/**
		 * update the workflow information
		 */
		function updateWorkflow() {

			var workflow;

			/*
			 * use ajax to communicate with server 
			 */
			var xhr = createXHR();

			xhr.onreadystatechange = function() {
				if(xhr.readyState == 4) {
					if((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304) {
						workflow = eval("(" + xhr.responseText + ")");
					} else {
						alert("Request was unsuccessful4: " + xhr.statusText);
					}
				}
			};

			xhr.open("get", "/GreenPipe/servlet/DisplayWorkflow?id=" + id, false);
			xhr.send(null);

			/*
			 * update the workflow data
			 */
			pipe.idText.nodeValue = workflow.id;
			pipe.nameText.nodeValue = workflow.name;
			//pipe.statusText.nodeValue = workflow.status;
			transformStatus(pipe.statusText, workflow.status);
			pipe.usernameText.nodeValue = workflow.username;
			if(workflow.startTime != null) {
				pipe.startTimeText.nodeValue = workflow.startTime;
			}
			if(workflow.runningTime != null) {
				pipe.runningTimeText.nodeValue = workflow.runningTime;
			}
			if(workflow.finishTime != null) {
				pipe.finishTimeText.nodeValue = workflow.finishTime;
			}
			

			/*
			 * update the status of this workflow
			 */
			status = workflow.status;

			/*
			 * check the status to stop updating
			 */
			if(status == 4 || status == 5 || status == 6) {
				clearInterval(workflowInterval);
				clearInterval(blocksInterval);
			}

			/*
			 * release the memory
			 */
			workflow = null;
			xhr = null;
		}


		/**
		 * update the blocks information
		 */
		function updateBlocks(){

			var blocks;

			/*
			 * use ajax to communicate with server 
			 */
			var xhr = createXHR();

			xhr.onreadystatechange = function() {
				if(xhr.readyState == 4) {
					if((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304) {
						blocks = eval("(" + xhr.responseText + ")");
					} else {
						alert("Request was unsuccessful5: " + xhr.statusText);
					}
				}
			};

			xhr.open("get", "/GreenPipe/servlet/DisplayBlock?id=" + id, false);
			xhr.send(null);

			/*
			 * update the blocks data
			 */
			var length = blocks.length;

			for(var i = 0; i < length; i++) {

				var job = jobs[i];

				var block = blocks[i];

				//id
				job.idText.nodeValue = block.id;
				//name
				job.nameText.nodeValue = block.name;
				//status
				//job.statusText.nodeValue = block.status;
				transformStatus(job.statusText, block.status);
				//wait_time
				if(block.waitTime != null) {
					job.waitTimeText.nodeValue = block.waitTime;
				}
				//start_time
				if(block.startTime != null) {
					job.startTimeText.nodeValue = block.startTime;
				}
				//progress
				job.progressText.nodeValue = block.progress + "%";
				//running_time
				if(block.runningTime != null) {
					job.runningTimeText.nodeValue = block.runningTime;
				}
				//finish_time
				if(block.finishTime != null) {
					job.finishTimeText.nodeValue = block.finishTime;
				}

				//release the memory
				job = null;
				block = null;
			}

			/*
			 * release the memory
			 */
			blocks = null;
			xhr = null;

		}
		
		function transformStatus(target, status) {
			switch(status) {
			case 0:target.nodeValue = "Waiting"; break;
			case 1:target.nodeValue = "Queuing"; break;
			case 2:target.nodeValue = "Ready"; break;
			case 3:target.nodeValue = "Running"; break;
			case 4:target.nodeValue = "Completed"; break;
			case 5:target.nodeValue = "Failed"; break;
			case 6:target.nodeValue = "Killed"; break;
			default:break;
			}
		}
		

	}


})();
