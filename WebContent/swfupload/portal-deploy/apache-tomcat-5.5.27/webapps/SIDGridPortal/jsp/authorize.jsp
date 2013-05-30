<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%
    String appDesc = (String)request.getAttribute("CONS_DESC");
    String token = (String)request.getAttribute("TOKEN");
    String callback = (String)request.getAttribute("CALLBACK");
    if(callback == null)
        callback = "";
    
%>

<html>
<head>
<title>Login Page</title>
</head>
<body>
<h1>Login to Gateway</h1>
<p>
If you have been issued a username and password, key them in here now!
</p>
<form method="POST" action="<%=request.getContextPath()%>/authorize">
Username : <input type="text" size="15" maxlength="25" name="username"><br><br>
Password : <input type="password" size="15" maxlength="25" name="password"><br><br>
<input type="hidden" name="oauth_token" value="<%= token %>"/>
<input type="hidden" name="oauth_callback" value="<%= callback %>"/>
<input type="submit" name="Authorize" value="Authorize"/>
<input value="Clear" type="reset">

</form>
</body>
</html>
