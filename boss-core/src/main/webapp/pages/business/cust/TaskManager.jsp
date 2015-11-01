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
	 	for(var i=0,len=resources.length;i<len;i++){
			var res = resources[i];
			if(res['handler'] == 'removeTaskBtnId'){//作废
				Ext.getCmp('removeTaskBtnId').show();
			}else if(res['handler'] == 'device_btn_id'){//回填
				Ext.getCmp('device_btn_id').show();
			}else if(res['handler'] == 'end_btn_id'){//完工
				Ext.getCmp('end_btn_id').show();
			}else if(res['handler'] == 'team_btn_id'){//施工队
				//Ext.getCmp('team_btn_id').enable();
				Ext.getCmp('team_btn_id').show();
			}else if(res['handler'] == 'withdraw_btn_id'){//撤回
				Ext.getCmp('withdraw_btn_id').show();
			}else if(res['handler'] == 'send_btn_id'){//发送ZTE
				Ext.getCmp('send_btn_id').show();
			}else if(res['handler'] == 'custsignno_btn_id'){//修改客户签字单号
				Ext.getCmp('custsignno_btn_id').show();
			}
		} 
	});
  </script>
</html>
