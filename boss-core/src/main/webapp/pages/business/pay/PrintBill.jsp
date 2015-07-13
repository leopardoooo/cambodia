<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
  <head>
  	<%@ include file="/pages/commons/library-ext.jsp" %>
  	<%@ include file="/pages/commons/library-busi.jsp" %>
  	<style type="text/css">
  		td{
  			border-top:0;
  			border-left:0;
  		}
  	</style>
  </head>
  <body>
  </body>
  <script type="text/javascript" src="<%=root %>/pages/business/pay/PrintBill.js" ></script>
  <script type="text/javascript">
		Ext.onReady(function(){
		
			window.setTimeout(function(){
				App.clearLoadImage();
			}, 200);
			
			
				Ext.onReady(function(){
					var panel = new BillPrintPanel();
					TemplateFactory.gViewport(panel);
				});
		});
  </script>
</html>
