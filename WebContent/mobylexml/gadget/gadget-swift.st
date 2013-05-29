<script>
var username;
var token = shindig.auth.getSecurityToken();
username = token.split(':')[0];

function sub_callback(sender, message){
        proteinName = message.split('/').pop().split('.').shift()
        if( proteinName.charAt(0) == 'T' && proteinName.length == 5 )
        {
            current_val = document.getElementById("oops_plist_data").value
            if( current_val )
                document.getElementById("oops_plist_data").value = proteinName + "\n" + document.getElementById("oops_plist_data").value 
            else
                document.getElementById("oops_plist_data").value = proteinName
        }
}

function getRadioValue(name)
{
    for (var i = 0; i < document.getElementsByName(name).length; i++)
    {
        if (document.getElementsByName(name)[i].checked)
        {
                return document.getElementsByName(name)[i].value;
        }
    }
}

function reset(){

}
function run(){
  //document.getElementById("seqinput").className = "loading";
  var params = new Object();
  params.javaClass = 'java.util.Hashtable';
  params.map = {}; 
  $inputArgs:{ arg | 
    $if(arg.isBoolean)$
     $Boolean(arg_name=arg.name)$
    $endif$
    $if(arg.isText)$
     $Text(arg_name=arg.name)$
    $endif$
    $if(arg.isChoice)$
     $Choice(arg_name=arg.name)$
    $endif$
    $if(arg.isMChoice)$
     $MultipleChoice(arg_name=arg.name)$
    $endif$
    $if(arg.isFile)$
     $File(arg_name=arg.name)$
    $endif$
    $if(arg.isWorkflow)$
     $Workflow(arg_name=arg.name)$
    $endif$
  }$
  
  //var jsonrpc = new JSONRpcClient("/SIDGridPortal/Old-JSONRPC");
  var jsonrpc = new JSONRpcClient("$WebServerUrl$/Old-JSONRPC");
  result = jsonrpc.WorkflowService.runWorkflow(username,"$Application$", "$Script$", "$ScriptVersion$",  params);
  if( result == null ) {
    alert("Error submitting workflow; administrators have been notified")
    return;
  }
  gadgets.pubsub.publish("wfhistory_input", "update");
  
}

</script>
<script>
  $inFilesArgs: { arg | $arg$ }$
  window.onload = function() {
      gadgets.pubsub.subscribe("fileselect", sub_callback);
     jQuery("input[type='button']").button();
     $uploadArgs: { arg | $arg$ }$
     gadgets.window.adjustHeight();
  }
</script>
