<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="java.util.*,java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<center>
<%
    String channel=request.getParameter("ChannelID");
	System.out.println("test up:"+channel);
	org.apache.commons.fileupload.DiskFileUpload upload = new org.apache.commons.fileupload.DiskFileUpload();
	String tempPath = request.getRealPath("/temp");
	upload.setSizeThreshold(16000);
	upload.setSizeMax(-1);
    upload.setRepositoryPath(tempPath);

    java.util.List items;
    items = upload.parseRequest(request);
    java.util.Iterator iter = items.iterator();
    String ChannelID="";
    String Type="";
    while (iter.hasNext()) {
    	FileItem item = (FileItem) iter.next();

    	if (item.isFormField()) {
			String FieldName = item.getFieldName();
			if(FieldName.equals("ChannelID"))
				ChannelID = item.getString();
			if(FieldName.equals("Type"))
				Type = item.getString();
    	}
	}
    System.err.println("do the real uploading");
	iter = items.iterator();
	while (iter.hasNext()) {
    FileItem item = (FileItem) iter.next();
    if (!item.isFormField()) {
		String FieldName = item.getFieldName();
		String FileName = item.getName();
		String FileExt = "";

		FileName = FileName.substring(FileName.lastIndexOf("\\")+1);
		int index = FileName.lastIndexOf(".");
		if(index!=-1){
			FileExt = FileName.substring(index+1);
		}
		System.out.println("ChannelID="+ChannelID);
		System.out.println("path="+tempPath);

		if(!FileName.equals("")){
			File uploadedFile = new File(tempPath+"\\"+FileName);
    		item.write(uploadedFile);
		}
    }
}
%>
</center>
</body> 
</html>