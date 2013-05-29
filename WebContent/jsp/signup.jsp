<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Registration for Gateway</title>
</head>
<body>
<h1>Registration for Gateway</h1>
<p>
Please provide the following information
</p>
<form method="POST" action="<%=request.getContextPath()%>/signup">
<p>Username : <input type="text" size="15" maxlength="25" name="username"></p>
<p>Password : <input type="password" size="15" maxlength="25" name="password"></p>
<p>Password (again): <input type="password" size='15" maxlength="25"></p>
<p>Email : <input type="text" size="15" maxlength="25" name="username"></p>

<input type="submit" name="Authorize" value="Authorize"/>
<input value="Clear" type="reset">

</form>
</body>
</html>
