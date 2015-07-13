<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.ycsoft.commons.action.SessionListener"%>
<script type="text/javascript">
<!--
	var loginUrl = '<%=SessionListener.getSsoLoginUrl(request) %>';
	if(window.parent){
		window.parent.location.href = loginUrl;
	}else{
		window.location.href = loginUrl;
	}
//-->
</script>