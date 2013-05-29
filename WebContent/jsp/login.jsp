<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>GreenPipe Login Page</title>
<link rel="stylesheet" type="text/css" media="screen" href="../css/screen.css" />
<style type="text/css">
.warning { color: red; }
label {width:100px; }
#main {width:500px;margin-left:auto;margin-right:auto;}
</style>
<script src="<%=request.getContextPath()%>/js/jquery.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.form.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery.validate.js" type="text/javascript"></script>

<script src="<%=request.getContextPath()%>/js/cmxforms.js" type="text/javascript"></script>
<script type="text/javascript">
	jQuery(function() {
		// show a simple loading indicator
		var loader = jQuery('<div id="loader"><img src="../images/loading.gif" alt="loading..." /></div>')
			.css({position: "relative", top: "1em", left: "25em"})
			.appendTo("body")
			.hide();
		jQuery().ajaxStart(function() {
			loader.show();
		}).ajaxStop(function() {
			loader.hide();
		}).ajaxError(function(a, b, e) {
			throw e;
		});
		
		var login_form = jQuery("#form").validate({
			submitHandler: function(form) {
                            form.submit();
			}
		});

              var signup_form = jQuery("#signup").validate({
			submitHandler: function(form) {
				jQuery(form).ajaxSubmit({
					target: "#output"
				});
			}
		});


		jQuery("#reset").click(function() {
			v.resetForm();
		});
	});
</script>

</head>
<body>
<%@ include file="navlogin.html" %>

<h1 id="banner">Login to Gadget Workspace</h1>
<div id="main">
<!--<script type="text/javascript">
alert('<%=request.getContextPath()%>')</script>-->
<form method="post" class="cmxform" id="form" action="<%=request.getContextPath()%>/simpleauth">
	<fieldset>
		<legend>Please login</legend>
		<p>
			<label for="user">Username</label>
			<input id="user" name="username" title="Please enter your username (at least 3 characters)" class="required" minlength="3" />
		</p>
		<p>
			<label for="pass">Password</label>
			<input type="password" name="password" id="login_password" class="required" minlength"5" />
		</p>
		<p>
			<input class="submit" type="submit" value="Login"/>
                     <input value="Clear" type="reset">

		</p>
	</fieldset>
</form>

<p>
If you don't have an account, please sign up here!
</p>
<form class="cmxform" id="signup" method="POST" action="<%=request.getContextPath()%>/signup">
<fieldset>
          <legend>Please sign up</legend>
          <label>First name</label> 
            <input type="text" name="fname"><br>
          <label>Last name</label> 
            <input type="text" name="lname"><br>
          <label>Username</label> 
            <input type="text" name="username" id="username" class="required" minlength="3" ><br>
          <label>Password</label> 
            <input type="password" name="password" id="password" minlength"5" class="required"><br>
          <label>Password (again)</label> 
            <input type="password" name="repasswd" id="repasswd" minlength"5" class="required" equalTo="#password"><br>
          <p>
          <input class="submit" type="submit" id="join" value="Register" />
          <input type="reset" value="clear" />
          </p>
</fieldset>
</form>

<div id="output></div>
</body>
</html>
