<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.ycsoft.daos.helper.StringHelper, java.util.Date"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<%@ include file="/pages/commons/library-ext.jsp" %>
  	<%@ include file="/pages/commons/library-busi.jsp" %>
  </head>
  <body></body>
  <%
     Object scriptFiles = request.getAttribute("url");
     String dversion="2";
     if(null == scriptFiles || "".equals( scriptFiles )){
     	out.println("ErrorMsg: 该资源没有配置脚本文件!");
     }else{
	     String url = "<script type=\"text/javascript\" src=\"{0}\" charset=\"utf-8\"></script>";
	     String [] jsFile = scriptFiles.toString().split(",");
	     for(String fUrl : jsFile){
	     	if("".equals(fUrl)) continue;
	     	fUrl+="?d="+dversion;
	     	out.println(StringHelper.formatIgnoreType(url , root+ "/" + fUrl));
	     }
     }
  %>
  <script type="text/javascript">
	 	App.clearLoadImage();
  </script>
</html>
