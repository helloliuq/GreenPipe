<script>
function getElement(x) {
        return document.getElementById(x);
}
function showOneSection(toshow) {
        var sections = [ 'main', 'approval', 'waiting' ];
        for (var i=0; i < sections.length; ++i) {
          var s = sections[i];
          var el = getElement(s);
          if (s === toshow) {
            el.style.display = "block";
          } else {
            el.style.display = "none";
          }
        }
}

function fetchData() {
        url = "$JSONRPCURL$";
        var params = {};
        params[gadgets.io.RequestParameters.CONTENT_TYPE] =
          gadgets.io.ContentType.TEXT;
        params[gadgets.io.RequestParameters.AUTHORIZATION] =
          gadgets.io.AuthorizationType.OAUTH;
        params[gadgets.io.RequestParameters.METHOD] =
          gadgets.io.MethodType.POST;
        var data = {rpc: "{\"id\": 1, \"method\": \"system.listMethods\"}" };
        params[gadgets.io.RequestParameters.POST_DATA]= gadgets.io.encodeValues(data);
        
        gadgets.io.makeRequest(url, function (response) {
          var popup = null;
          if (response.oauthApprovalUrl) {
            popup = shindig.oauth.popup({
              destination: response.oauthApprovalUrl,
              windowOptions: null,
              onOpen: function() { showOneSection('waiting'); },
              onClose: function() { fetchData(); }
            });
            getElement('personalize').onclick = popup.createOpenerOnClick();
            getElement('approvaldone').onclick = popup.createApprovedOnClick();
            showOneSection('approval');
          } else if (response.data) {
            jsonrpc = new JSONRpcClient("$JSONRPCURL$");
            showOneSection('main');
          } else {
            var whoops = document.createTextNode(
                'OAuth error: ' + response.oauthError + ': ' +
                response.oauthErrorText);
            getElement('main').appendChild(whoops);
            showOneSection('main');
          }
        }, params);
        
}

var callback = function(result, e) {
    alert(result);
    if(e) {
        reportError(e);
    } else {
        // Continuation - client is ready to use
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

function run$Application$(){
  //document.getElementById("seqinput").className = "loading";
  params.javaClass = 'java.util.Hashtable';
  params.map = {}; 
  $inputArgs:{ arg | $arg$ }$
  
  result = jsonrpc.BlastService.runBlast(callback,'unknown', params);
}

</script>