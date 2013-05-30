<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<% session.invalidate(); 
   response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
%>

<html>
<head>
<title>Logout Page</title>
<script>
 function del_cookie(name) {
  document.cookie = name +'=; expires=Thu, 01-Jan-70 00:00:01 GMT;path='+request.getContextPath() + '/';
  // jump to the login page
  window.location.href ="login.jsp";
} 
</script>
</head>
<body onload="del_cookie('MyCookie');">
  You have logged out.
</body>
</html>
