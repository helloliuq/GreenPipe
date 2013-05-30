<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Login Page For SIDGrid Gadgets</title>
<script src="http://yui.yahooapis.com/2.8.0r4/build/yuiloader/yuiloader-min.js"></script> 

<script type="text/javascript">
 var loader = new YAHOO.util.YUILoader({  
     require: ["container","connection",  
               "cookie",  
               "animation",  
               "button",  
               "dom","event"  
               ],  
    loadOptional: true,  
    onSuccess: function() {  
     Yevent = YAHOO.util.Event;  
     Yconnect = YAHOO.util.Connect;  
     Ydom = YAHOO.util.Dom;  
     Ycook = YAHOO.util.Cookie;  
 // fire of init() function once the document has finished loading and the dom is ready  
     //YAHOO.util.Event.onDOMReady(init);  
     },  
     timeout: 10000,  
     allowRollup: true,  
     base: 'http://yui.yahooapis.com/2.8.0r4/build/',  
     combine: false  
 });  
 loader.insert();  

 var callback = {
    success: function (oResponse) {
        //handle a successful response
        alert(oResponse.responseText);
    },
    failure: function (oResponse) {
        //handle an unsuccessful request
        alert("test ...");
        alert(oResponse.responseText);
    }
  }

  function signup()
  {
     YAHOO.util.Connect.setForm(document.getElementById("signup"));
     YAHOO.util.Connect.asyncRequest('POST', '/SIDGridPortal/signup', callback);  
  }
</script>

</head>
<body>
<h1>Login to SIDGrid Gateway</h1>
<p>
If you have been issued a username and password, key them in here now!
</p>
<form method="POST" action="/SIDGridPortal/simpleauth">
Username : <input type="text" size="15" maxlength="25" name="username"><br><br>
Password : <input type="password" size="15" maxlength="25" name="password"><br><br>
<input type="submit" name="Authorize" value="Authorize"/>
<input value="Clear" type="reset">

</form>

<p>
If you don't have an account, please sign up here!
</p>
<form id="signup">
<fieldset>
          <legend>Please sign up!</legend>
          <label>First name:</label> <input type="text" name="fname"><br>
          <label>Last name:</label> <input type="text" name="lname"><br>
          <label>Username:</label> <input type="text" name="username"><br>
          <label>Password:</label> <input type="password" name="password"><br>
          <label>Retype Password:</label> <input type="password" name="password"><br>
          <button type="submit" id="join" onclick="signup();">Join!</button>
          <button type="reset">Clear</button>
</fieldset>

</body>
</html>
