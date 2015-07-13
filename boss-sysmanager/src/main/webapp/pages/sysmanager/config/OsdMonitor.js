function shake(mask) {
	var flag = false;
	var counts = 0;
	flag = setInterval(function(){
		var color = mask.dom.style['background-color'];
		if(color == 'red'){
			mask.dom.style['background-color']='';
		}else{
			mask.dom.style['background-color']='red';
		}
		counts++;
		if(counts>=16){
			clearInterval(flag);
			mask.hide();
			mask=null;
		}
	},200);
}

CancelAllOsdFormWin = Ext.extend(Ext.Window,{
	form:null,width:450,height:280,parent:null,title:'取消全部OSD',
	constructor:function(p){
		this.parent = p;
		var minDate = new Date();
		minDate = Date.parseDate(minDate.format('Y-m-d'),'Y-m-d');
		this.form = new Ext.FormPanel({
			layout:'form',bodyStyle:'padding-top:10px',
			items:[
				{fieldLabel:'截至日期',xtype:'datefield',name:'eff_end_time',format:'Y-m-d',minValue:minDate},
				{xtype : 'textarea',fieldLabel:'说明',maxLength:400,
					name : 'remark',height:150,width:250
				}
			]
		});
		
		CancelAllOsdFormWin.superclass.constructor.call(this,{
			layout:'fit',items:this.form,buttonAlign:'center',
			buttons:[
			{text:'确定 ',scope:this,handler:this.doSave},
			{text:'取消 ',scope:this,handler:this.doCancel}
			]
		});
	},
	doSave:function(){
		var values = this.form.getForm().getValues();
		var param = {};
		for(var key in values){
			param ['stop.'+key] = values[key];
		}
		
		Ext.Ajax.request({
			url: root + '/resource/Osd!stopAll.action',
			params:param,scope:this,
			success:function(req){
				var result = Ext.decode(req.responseText);
				if(result == true){
					Alert('操作成功!');
				}
				this.doCancel();
			}
		});
		
	},
	doReset:function(){
		var newRec = new Ext.data.Record({eff_end_time:null,remark:null});
		this.form.getForm().loadRecord(newRec);
	},
	doCancel:function(){
		this.doReset();
		this.hide();
	}
});
/**
 * @class OsdMonitor
 * @extends Ext.Panel
 */
OsdMonitor = Ext.extend(Ext.Panel,{
	monitorPanel:null,closable:true,id:'OsdMonitor',
	sendedGrid:null,sendedStore:null,
	queueGrid:null,queueStore:null,
	errGrid:null,errStore:null,
	layout:'fit',title:'OSD监控',taskTime:2000 * 60,
	constructor:function(){
		this.sendedStore = new Ext.data.JsonStore({
			fields : ['transnum', 'job_id', 'cas_id',
									'cas_type', 'user_id', 'cust_id',
									'done_code', 'cmd_type', 'stb_id',
									'card_id', 'prg_name', 'boss_res_id',
									'control_id', 'auth_begin_date',
									'auth_end_date', 'result_flag',
									'error_info', 'area_id', 'is_sent',
									'record_date', 'send_date',
									'detail_params', 'priority', 'ret_date'],
			autoLoad:true,
			url: root + '/resource/Osd!querySended.action',
			root: 'records',
			totalProperty: 'totalProperty',
			params:{start:0,limit:20}
		});
		
		this.queueStore = new Ext.data.JsonStore({
			fields : ['transnum', 'job_id', 'cas_id',
									'cas_type', 'user_id', 'cust_id',
									'done_code', 'cmd_type', 'stb_id',
									'card_id', 'prg_name', 'boss_res_id',
									'control_id', 'auth_begin_date',
									'auth_end_date', 'result_flag',
									'error_info', 'area_id', 'is_sent',
									'record_date', 'send_date',
									'detail_params', 'priority',
									'merge_trunsnum', 'ret_date'],
			autoLoad:true,
			url: root + '/resource/Osd!queryQueued.action',
			root: 'records',
			totalProperty: 'totalProperty',
			params:{start:0,limit:20},
			listeners:{
				scope:this,
				load:function(store,records){
					if(!this.queueGrid){
						return;
					}
					var shouldShake = false;
					for(var index =0;index<records.length;index++){
						var data = records[index].data;
						var color = false;
						if(data.error_info && !Ext.isEmpty(data.error_info)){
							color = '#FF0000';
							shouldShake = true;
						}else if(data.is_sent == 'I'){
							color = '#FFFF37';
						}
						if(color){
							this.queueGrid.getView().getRow(index).style.backgroundColor=color;
						}
					}
					if(shouldShake){//闪屏提示
						var el =this.queueGrid.getView().mainBody.dom;
						//this.queueGrid.getView().mainBody.dom.style['background-color'];
						mask = Ext.get(this.queueGrid.getView().mainBody.dom).mask();
						shake(mask);
					}
				}
			}
		});
		
		this.errStore = new Ext.data.JsonStore({
			fields : ['transnum', 'job_id', 'cas_id',
									'cas_type', 'user_id', 'cust_id',
									'done_code', 'cmd_type', 'stb_id',
									'card_id', 'prg_name', 'boss_res_id',
									'control_id', 'auth_begin_date',
									'auth_end_date', 'result_flag',
									'error_info', 'area_id', 'is_sent',
									'record_date', 'send_date',
									'detail_params', 'priority',
									'merge_trunsnum', 'ret_date'],
			autoLoad:true,
			url: root + '/resource/Osd!queryErrorData.action',
			root: 'records',
			totalProperty: 'totalProperty',
			params:{start:0,limit:20}
		});
		
		this.sendedGrid = new Ext.grid.GridPanel({
			store:this.sendedStore,
			columns:[
				{header:'指令流水',dataIndex:'transnum',hidden:true},
				{header:'任务编号',dataIndex:'job_id',hidden:true},
				{header:'CAS编号',dataIndex:'cas_id',hidden:true},
				{header:'CAS类型',dataIndex:'cas_type',width:60,renderer:App.qtipValue},
				{header:'指令类型',dataIndex:'cmd_type',width:80,renderer:App.qtipValue},
				{header:'盒号',dataIndex:'stb_id',width:120,renderer:App.qtipValue},
				{header:'卡号',dataIndex:'card_id',width:120,renderer:App.qtipValue},
				{header:'节目名称',dataIndex:'prg_name',width:120,renderer:App.qtipValue},
				{header:'结果标记（正确,错误）',dataIndex:'result_flag',hidden:true},
				{header:'错误信息',dataIndex:'error_info',renderer:App.qtipValue},
				{header:'是否处理',dataIndex:'is_sent',hidden:false,renderer:function(v){
					var res = '';
					switch(v){
						case 'N': res = '未处理'; break;
						case 'Y': res = '已处理'; break;
						case 'E': res = '自动错误'; break;
						case 'I': res = '手工错误'; break;
						case '1': res = '待处理'; break;
						case '2': res = '重复指令'; break;
					}
					return res;
				}},
				{header:'参数明细',dataIndex:'detail_params',renderer:App.qtipValue,width:300},
				{header:'优先级',dataIndex:'priority',renderer:App.qtipValue}
			],
			bbar: new Ext.PagingToolbar({
	        	pageSize: 20,
				store: this.sendedStore
			})
		});
		
		this.queueGrid = new Ext.grid.GridPanel({
			store:this.queueStore,
			columns:[
				{header:'指令流水',dataIndex:'transnum',hidden:true},
				{header:'任务编号',dataIndex:'job_id',hidden:true},
				{header:'CAS编号',dataIndex:'cas_id',hidden:true},
				{header:'CAS类型',dataIndex:'cas_type',width:60,renderer:App.qtipValue},
				{header:'指令类型',dataIndex:'cmd_type',width:80,renderer:App.qtipValue},
				{header:'盒号',dataIndex:'stb_id',width:120,renderer:App.qtipValue},
				{header:'卡号',dataIndex:'card_id',width:120,renderer:App.qtipValue},
				{header:'节目名称',dataIndex:'prg_name',width:120,renderer:App.qtipValue},
				{header:'结果标记（正确,错误）',dataIndex:'result_flag',hidden:true},
				{header:'错误信息',dataIndex:'error_info',renderer:App.qtipValue},
				{header:'是否处理',dataIndex:'is_sent',hidden:false,renderer:function(v){
					var res = '';
					switch(v){
						case 'N': res = '未处理'; break;
						case 'Y': res = '已处理'; break;
						case 'E': res = '自动错误'; break;
						case 'I': res = '手工错误'; break;
						case '1': res = '待处理'; break;
						case '2': res = '重复指令'; break;
					}
					return res;
				}},
				{header:'参数明细',dataIndex:'detail_params',renderer:App.qtipValue,width:300},
				{header:'优先级',dataIndex:'priority',renderer:App.qtipValue}
			],
			bbar: new Ext.PagingToolbar({
	        	pageSize: 20,
				store: this.queueStore
			})
		});
		
		this.errGrid = new Ext.grid.GridPanel({
			store:this.errStore,
			columns:[
				{header:'指令流水',dataIndex:'transnum',hidden:true},
				{header:'任务编号',dataIndex:'job_id',hidden:true},
				{header:'CAS编号',dataIndex:'cas_id',hidden:true},
				{header:'CAS类型',dataIndex:'cas_type',width:60,renderer:App.qtipValue},
				{header:'指令类型',dataIndex:'cmd_type',width:80,renderer:App.qtipValue},
				{header:'盒号',dataIndex:'stb_id',width:120,renderer:App.qtipValue},
				{header:'卡号',dataIndex:'card_id',width:120,renderer:App.qtipValue},
				{header:'节目名称',dataIndex:'prg_name',width:120,renderer:App.qtipValue},
				{header:'结果标记（正确,错误）',dataIndex:'result_flag',hidden:true},
				{header:'错误信息',dataIndex:'error_info',renderer:App.qtipValue},
				{header:'是否处理',dataIndex:'is_sent',hidden:false,renderer:function(v){
					var res = '';
					switch(v){
						case 'E': res = '自动错误'; break;
						case 'I': res = '<span style="color:blue;">手工错误</span>'; break;
					}
					return res;
				}},
				{header:'参数明细',dataIndex:'detail_params',renderer:App.qtipValue,width:300},
				{header:'优先级',dataIndex:'priority',renderer:App.qtipValue}
			],
			bbar: new Ext.PagingToolbar({
	        	pageSize: 20,
				store: this.errStore
			})
		});
		
		this.monitorPanel = new Ext.TabPanel({
			activeTab: 0,border : false,defaults: {layout: 'fit',border: false},title:'OSD监控',
			items: [{
				tbar:[
				{text:'标记非法',scope:this,handler:this.invalidOsd},'-',
				{text:'OSD全部停止发送',scope:this,handler:this.stopAll},'-',
				{text:'取消全部停止发送',scope:this,handler:this.cancelStopAll},'-'
			],
			    title: '待发送队列'
			    , items: [this.queueGrid]
			},{
			    title: '已发送OSD'
			    , items: [this.sendedGrid]
			},{
				title: '错误OSD'
				,items: [this.errGrid]
			}]
		});
		OsdMonitor.superclass.constructor.call(this,{
			items:[this.monitorPanel]
		});
		Ext.TaskMgr.start.defer(this.taskTime,this,[this.startLoopQuery]);
	},
	startLoopQuery:{
		run : function() {
					Ext.getCmp('OsdMonitor').loogQueryTask();
				},
		interval:this.taskTime
	},
	loogQueryTask:function(){
//		this.sendedStore.reload();
		this.queueStore.reload();
	},
	initEvents:function(){
		OsdMonitor.superclass.initEvents.call(this);
		this.on('close', function() {
							Ext.TaskMgr.stop(this.startLoopQuery);
						}, this);
	},
	initComponent:function(){
		OsdMonitor.superclass.initComponent.call(this);
	},
	invalidOsd:function(){
		var records = this.queueGrid.selModel.getSelections();
		if(!records || records.length !=1 ){
			Alert('请选择且仅选择一条记录.');
			return ;
		}
		var record = records[0];
		var params = {transnum:record.get('transnum')};
		Confirm('是否确认标记该条记录为非法!' , this , function(){
			Ext.Ajax.request({
				url: root + '/resource/Osd!invalidOsd.action',
				scope : this,
				params : params,
				success : function(req) {
					this.queueStore.reload();
				}
			})});
	},
	stopAll:function(){
		if(!this.formWin){
			this.formWin =new CancelAllOsdFormWin(this);
		}
		this.formWin.doReset();
		this.formWin.show();
		Ext.Ajax.request({
			url: root + '/resource/Osd!queryLatestStopAllDate.action',
			scope : this,
			success : function(req) {
				var result = Ext.decode(req.responseText);
				if(result){
					var latestDate = Date.parseDate(result.eff_end_time,'Y-m-d H:i:s').format('Y年m月d日');
					Alert('当前有效的最晚停止全部OSD的时间是 <span style="color:red;">' + latestDate + '</span>' );
				}
			}

		})
	},
	cancelStopAll:function(){
		Ext.Ajax.request({
			url: root + '/resource/Osd!queryLatestStopAllDate.action',
			scope : this,
			success : function(req) {
				var result = Ext.decode(req.responseText);
				var msg = '是否确认取消全部停止OSD!';
				if(result){
					var latestDate = Date.parseDate(result.eff_end_time,'Y-m-d H:i:s').format('Y年m月d日');
					msg = '当前有效的最晚停止全部OSD的时间是 <span style="color:red;">' + latestDate + '</span>' + msg;
				}
				
				Confirm(msg , this , function(){
					Ext.Ajax.request({
						url: root + '/resource/Osd!cancelStopAll.action',
						scope : this,
						success : function(req) {
							var result = Ext.decode(req.responseText);
							if(result == true){
								Alert('操作成功!');
							}
						}
			
					})
				});
				
			}

		})
		
	}
});