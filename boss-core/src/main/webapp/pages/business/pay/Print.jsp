<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
  <head>
  	<%@ include file="/pages/commons/library-ext.jsp" %>
  	<%@ include file="/pages/commons/library-busi.jsp" %>
  	<style type="text/css">
  	</style>
  </head>
  <body>
  	<OBJECT classid="clsid:C80C0611-AE1F-45EC-BE4E-625366C3752F"
		    codebase="<%=boss_res %>/components/cab/PrintCtrl.cab#version=1,0,0,0" name="PrintCtrl"
		    width="0" height="0">
	</OBJECT>
  </body>
  <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/PrintTools.js"></script>
  <script type="text/javascript" src="<%=root %>/pages/business/pay/PrintPanel.js" ></script>
  <script type="text/javascript">
		Ext.onReady(function(){
		
			window.setTimeout(function(){
				App.clearLoadImage();
			}, 200);
			
			
				new Ext.Viewport({
					layout: 'fit',
					items: new PrintPanel()
				});
		});
  </script>
</html>
