<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.runqian.report4.usermodel.Context"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.InputStreamReader"%>
<%@ page import="java.io.BufferedReader"%>
<%
	String comment = request.getParameter("comment");
	if(comment.charAt(0)!='/') comment = "/"+comment;
	comment = Context.getMainDir() + comment;
	comment = application.getRealPath(comment);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>My JSP 'showComment.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
</head><body>
<pre style="font-size: 14px;">
<%
	try{
		FileInputStream fis = new FileInputStream(comment);
		InputStreamReader isReader = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isReader);

		for(String line=null;(line=br.readLine())!=null;)
			out.println(line);
	}
	catch(Exception e){
		e.printStackTrace();
		out.println("·¢Éú´íÎó£º"+e.getMessage());
	}
%>
</pre>
</body></html>
