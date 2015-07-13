<%@ page language="java"pageEncoding="GBK"%>
<%@ page import="com.runqian.report4.demo.HTMLTreeMaker"%>
<%
	HTMLTreeMaker htMaker = new HTMLTreeMaker(request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head>    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link href="<%=request.getContextPath()%>/style.css" rel="stylesheet" type="text/css">
<style>
<% out.print(htMaker.buildCSS()); %>
</style>
<script type="text/javascript">
	<% out.print(htMaker.buildJavaScript("content_area")); %>
</script>

<script language="javascript">
	function showFile(reportUrl, commentUrl){
		var frm = document.createElement('FORM');
		frm.action = 'showContent.jsp';
		frm.method = 'get';
		frm.target = 'content_area';
		document.body.appendChild(frm);

		var url1 = document.createElement('INPUT');
		url1.type = 'HIDDEN';
		url1.name = 'report_url';
		url1.value = reportUrl;
		frm.appendChild(url1);

		var url2 = document.createElement('INPUT');
		url2.type = 'HIDDEN';
		url2.name = 'comment_url';
		url2.value = commentUrl;
		frm.appendChild(url2);

		frm.submit();
	}
</script>

</head><body>
<div id="tree-bg">
	<div id="tree">
		<%
			out.print(htMaker.buildHTMLTree());
		%>
	</div>
</div>
</body></html>
