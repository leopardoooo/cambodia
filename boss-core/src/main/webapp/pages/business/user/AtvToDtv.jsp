<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
  <head>
  	<%@ include file="/pages/commons/library-ext.jsp" %>
  	<%@ include file="/pages/commons/library-busi.jsp" %>
  	<style type="text/css">
  	</style>
  </head>
  <body onload="loadActiveX('<%=boss_res %>');">
	  	<div id="activeId"></div>
  </body>
  <script type="text/javascript" src="<%=root %>/pages/business/user/ReadPassword.js" ></script>
  <script type="text/javascript" src="<%=root %>/pages/business/user/UserBaseForm.js" ></script>
  <script type="text/javascript" src="<%=root %>/pages/business/user/AtvToDtv.js" ></script>
  <script type="text/javascript">
		Ext.onReady(function(){
		
			window.setTimeout(function(){
				App.clearLoadImage();
			}, 200);
			
			var adf = new AtvToDtvForm();
			var box = TemplateFactory.gTemplate(adf);
			
		});
  </script>
</html>
