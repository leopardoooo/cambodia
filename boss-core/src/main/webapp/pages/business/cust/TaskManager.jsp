<%@ page language="java"  pageEncoding="UTF-8"%>
<html>
  <head>
  	<%@ include file="/pages/commons/library-ext.jsp" %>
  	<%@ include file="/pages/commons/library-busi.jsp" %>
  	<style type="text/css">
  		.red-row{ 
  			background-color: #F5C0C0 !important; 
  		}
  	</style>
  </head>
  <body>
  </body>  
  <script type="text/javascript" src="<%=root %>/pages/business/cust/TaskManager.js" ></script>
  <script type="text/javascript">
  	Ext.onReady(function(){
		var ecf = new TaskManagerPanel();
		var box = TemplateFactory.gViewport(ecf);
		
		window.setTimeout(function(){
			App.clearLoadImage();
		}, 200);
		
		var resources = App.getData().resources;
		/* for(var i=0,len=resources.length;i<len;i++){
			var res = resources[i];
			if(res['handler'] == 'ivalid_btn_id'){
				Ext.getCmp('ivalid_btn_id').enable();
			}else if(res['handler'] == 'send_btn_id'){
				Ext.getCmp('send_btn_id').enable();
			}else if(res['handler'] == 'add_btn_id'){
				Ext.getCmp('add_btn_id').enable();
			}
		} */
	});
  </script>
</html>
