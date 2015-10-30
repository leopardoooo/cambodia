
//taskTime最好大于winHideTime,由于请求的原因，时间有差异，所以尽量设置明显的时间差
//以下时间为测试数据。正式 循环执行时间最好大于1分钟！
var taskTime = 300*1000;//公告任务循环执行时间(单位为毫秒)
//var winHideTime = 10000;//公告框显示多少时间后隐藏(单位为毫秒)

//公告任务，每10秒查询一次
var bulletinTask = {
	run:function(){showBulletin();},
	interval:taskTime
};

//公告
var showBulletin = function(){
	Ext.Ajax.request({
		url:root + '/system/x/Index!queryUnCheckBulletin.action' ,
		scope:this,
		success:function(res,opt){
			var data = Ext.decode(res.responseText);
			if(data){
		
				if(Ext.isEmpty(data))return;
				var bulletinContent = Ext.util.Format.ellipsis(data['bulletin_content'],120,true);
				var win = Ext.getCmp('bulletWinId');
				if(!win){
					win = new TipsWindow({
						id:'bulletWinId',
						title:'公告',
						shadow:true,
//						autoHide:winHideTime,//在显示一段时间后自动隐藏
						autoHide:false,//不自动隐藏
						html:"<a href='#' onclick='javascript:doBulletinClick()'><div id='bulletinDivId'>"+bulletinContent+"</div></a>"
						,buttons : [{text : '确认',handler : function() {saveBulletin('bulletWinId');}
									},{text : '查看',handler : function() {doBulletinClick();}}]
						,listeners:{
							hide:function(){
								//确认公告或者关闭window，重新开始公告任务，延迟10秒执行
								Ext.TaskMgr.start.defer(taskTime,null,[bulletinTask]);
							},
							show:function(){
								//停止任务
								Ext.TaskMgr.stop(bulletinTask);
							}
						}
					});
				}else{
					Ext.get('bulletinDivId').update(bulletinContent);
				}
				win.show();
				//不看明细直接保存公告
				saveBulletin = function(v) {
					Ext.Ajax.request({
								url : root+ '/system/x/Index!checkBulletin.action',
								params : {bulletin_id : data['bulletin_id']},
								success : function() {Ext.getCmp(v).close();}
							});
				}
				
				//打开公告详细window后停止循环公告任务
				doBulletinClick = function(){
					win.hide();
					//停止任务
					Ext.TaskMgr.stop(bulletinTask);
					var w = Ext.getCmp('bulletinWinId');
					if(!w)
						w = new Ext.Window({
							id:'bulletinWinId',
							title:'公告信息',
							height:300,
							width:400,
							border:false,
							closeAction:'close',
							layout:'fit',
							items:[
								{layout:'form',labelAlign:'right',labelWidth:75,
									bodyStyle:'padding-top:10px',
									bodyStyle: "background:#F9F9F9",
									border:false,items:[
									{fieldLabel:'标题',xtype:'displayfield',value:data['bulletin_title']},
									{fieldLabel:'发布人',xtype:'displayfield',value:data['bulletin_publisher']},
									{fieldLabel:'内容',xtype:'textarea',value:data['bulletin_content'],width:270,height:150,readOnly :true}
								]}
							],
							listeners:{
								scope:this,
								close:function(){
									//确认公告或者关闭window，重新开始公告任务，延迟10秒执行
									Ext.TaskMgr.start.defer(taskTime,this,[bulletinTask]);
								}
							},
							buttonAlign:'right',
							buttons:[{text:'确认',handler:function(){saveBulletin('bulletinWinId');}}]
						});
					w.show();
			 	}
			}
		}
	});
}