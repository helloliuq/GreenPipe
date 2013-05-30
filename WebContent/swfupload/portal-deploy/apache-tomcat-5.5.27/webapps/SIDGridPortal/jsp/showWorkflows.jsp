<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head>
<script type="text/javascript" src="../js/jsonrpc.js"></script>
<script>
var totalJobs;
var jobsPerPage = 5;
var user = "wwjag";
function init(){
    totalJobs=getWfNums(user);
    var pages = totalJobs/jobsPerPage;
    updatePageSel(pages);
    updateWfHistory(1);
}
function getWfNums(user){
  jsonrpc = new JSONRpcClient("/SIDGridPortal/Old-JSON-RPC");
  result = jsonrpc.WorkflowService.getTotalWfs(user);
  return result;
}
function getWfHistory(user,pageNum){
  //document.getElementById("detailFrame").className = "loading";
  jsonrpc = new JSONRpcClient("/SIDGridPortal/Old-JSON-RPC");
  result = jsonrpc.WorkflowService.getWorkflowsPerPage(user, pageNum, jobsPerPage);
  //document.getElementById("detailFrame").className = "";
  return result;
}
function changePage(){
  var pageSel = document.getElementById("pageSel");
  var newPage = pageSel.options[pageSel.selectedIndex].value;
  updateWfHistory(newPage);
}
//TODO: can we use dojo.data to build a better datagrid for this?
function updateWfHistory(newPage){
  var joblist = getWfHistory(user,newPage);
  //update the job history table
  var table=document.getElementById("workflowHistory");
  var tbody = table.tBodies[0];
  while (tbody.firstChild) 
           tbody.removeChild(tbody.firstChild);
  for( var i=0; i<joblist.list.length; i++){
    var tmp_row = document.createElement("tr");
    addCell(tmp_row, joblist.list[i].workflowID);
    addCell(tmp_row, joblist.list[i].status);
    var input_cell = addCell(tmp_row, "input");
    input_cell.workflowID = joblist.list[i].workflowID;
    input_cell.onclick=function(evt){fetchInput(this.workflowID);}
    input_cell.class='detail';
    var cmd_cell = addCell(tmp_row, joblist.list[i].program);
    cmd_cell.workflowID = joblist.list[i].workflowID;
    cmd_cell.onclick=function(evt){fetchCmd(this.workflowID);}
    var result_cell = addCell(tmp_row, "result");
    result_cell.workflowID = joblist.list[i].workflowID;
    result_cell.onclick=function(evt){fetchResult(this.workflowID);}
    addCell(tmp_row, joblist.list[i].createTime);
    addCell(tmp_row, joblist.list[i].finishTime);
    tbody.appendChild(tmp_row);
  }
  //check the total job number, if changed, update the pageSel option
  
}
function addCell(row, data){
  var cell_txt = document.createTextNode(data);
  var cell = document.createElement("td");
  cell.appendChild(cell_txt);
  row.appendChild(cell);
  return cell;
}
function updatePageSel(totalPages){
 var pageSel=document.getElementById("pageSel");
 
 for(var i=1; i<=totalPages; i++){
   var option = document.createElement("option");
   option.value = i;
   option.text = i;
   pageSel.appendChild(option);
 }
}
function fetchCmd(wfid){
  document.getElementById("detailFrame").className = "loading";
  jsonrpc = new JSONRpcClient("/SIDGridPortal/Old-JSON-RPC");
  result = jsonrpc.WorkflowService.getCmd(user,wfid);
  //alert('the feature is '+result);
  // display the output
  var testFrame = document.getElementById("detailFrame");
  iframeDoc = testFrame.contentDocument;
  if (iframeDoc == undefined || iframeDoc == null)
         iframeDoc = testFrame.contentWindow.document;
  iframeDoc.open();
  var report = result.replace(/\n/g,"<br>");
  var innerHtml = "<html><head><\/head><body>"+report+"<\/body><\/html>";
  iframeDoc.writeln(innerHtml);
  iframeDoc.close();
  document.getElementById("detailFrame").className = "";
}
function fetchInput(wfid){
  document.getElementById("detailFrame").className = "loading";
  jsonrpc = new JSONRpcClient("/SIDGridPortal/Old-JSON-RPC");
  result = jsonrpc.WorkflowService.getInputLinks(user,wfid);
  //alert('the feature is '+result);
  // display the input
  var testFrame = document.getElementById("detailFrame");
  iframeDoc = testFrame.contentDocument;
  if (iframeDoc == undefined || iframeDoc == null)
         iframeDoc = testFrame.contentWindow.document;
  iframeDoc.open();
  var report = result.replace(/\n/g,"<br>");
  var innerHtml = "<html><head><\/head><body>"+report+"<\/body><\/html>";
  iframeDoc.writeln(innerHtml);
  iframeDoc.close();
  document.getElementById("detailFrame").className = "";
}
function fetchResult(wfid){
  document.getElementById("detailFrame").className = "loading";
  jsonrpc = new JSONRpcClient("/SIDGridPortal/Old-JSON-RPC");
  result = jsonrpc.WorkflowService.getOutputLinks(user,wfid);
  //alert('the feature is '+result);
  // display the output
  var testFrame = document.getElementById("detailFrame");
  iframeDoc = testFrame.contentDocument;
  if (iframeDoc == undefined || iframeDoc == null)
         iframeDoc = testFrame.contentWindow.document;
  iframeDoc.open();
  var report = result.replace(/\n/g,"<br>");
  var innerHtml = "<html><head><\/head><body>"+report+"<\/body><\/html>";
  iframeDoc.writeln(innerHtml);
  iframeDoc.close();
  document.getElementById("detailFrame").className = "";
}
</script>
</head>
<body onload="init();">
  <fieldset>
   <legend>Workflow List </legend>
   <table border="1" id="workflowHistory">
	  <thead>
	     <tr>
	       <th> Job Handle </th>
	       <th> Status </th>
	       <th> Input  </th>
	       <th> Cmd Script </th>
	       <th> Result </th>
	       <th> Start Time </th>
	       <th> End Time </th>
	     </tr>
	  </thead>
	  <tbody>
	  </tbody>
  </table>
 </fieldset>
 <p> Select Pages
  <select name="pageSel" id="pageSel" onchange="changePage();">
  </select>
  
  <fieldset>
	<legend>Workflow Detail</legend>
	<iframe width=90% name="details" id="detailFrame"></iframe>
  </fieldset>
</body>
</html>