
ReclaimDeviceGrid = Ext.extend(Ext.grid.GridPanel,{
	reclaimStore:null,
	constructor:function(){
		this.reclaimStore = new Ext.data.JsonStore({
			url:root+'/resource/Device!queryDeviceReclaim.action',
			root:'records',
			totalProperty:'totalProperty',
			fields:['done_code','create_time','device_id','device_type','device_type_text','reclaim_reason_text','reclaim_reason',
				'device_code','depot_id','depot_text','optr_name','depot_status','pair_device_code','pair_device_modem_code',
				'status','status_text','confirm_optr','confirm_optr_text','confirm_time'
			]
		});
		this.reclaimStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var deptId = App.getApp().data.optr.dept_id;
		var columns = [
			{header:'业务日期',dataIndex:'create_time',width:120},
			{header:'设备类型',dataIndex:'device_type_text',width:60},
			{header:'设备编号',dataIndex:'device_code',width:130,renderer:App.qtipValue},
			{header:'配对卡',dataIndex:'pair_device_code',width:120,renderer:App.qtipValue},
			{header:'配对modem',dataIndex:'pair_device_modem_code',width:120,renderer:App.qtipValue},
			{header:'营业厅',dataIndex:'depot_text',width:100,renderer:App.qtipValue},
			{header:'营业员',dataIndex:'optr_name',width:70,renderer:App.qtipValue},
			{header:'状态',dataIndex:'status_text',width:65},
			{header:'回收原因',dataIndex:'reclaim_reason_text',width:100},
			{header:'操作',dataIndex:'done_code',renderer:function(v,mete,record){
					var text = '';
					var status = record.get('status');
					if(status == 'CONFIRM'){
						if (record.get('depot_status') == 'IDLE'
									&& deptId == record.get('depot_id')){
							text = "<a href='#' onclick=Ext.getCmp('reclaimDeviceGridId').doCancel("+v+")>取消确认</a>";
						}
					}/*else if(status == 'UNCONFIRM'){//未确认
						
					}else if(status == 'NOTCONFIRM'){//不确认
						
					}*/
					else{
						text = "<a href='#' onclick=Ext.getCmp('reclaimDeviceGridId').doReclaim("+v+")>确认</a>";
					}
					return text;
				}
			},
			{header:'确认人',dataIndex:'confirm_optr_text',width:70,renderer:App.qtipValue},
			{header:'确认时间',dataIndex:'confirm_time',width:120}
		];
		ReclaimDeviceGrid.superclass.constructor.call(this,{
			id:'reclaimDeviceGridId',
			title:'设备回收',
			border:false,
			store:this.reclaimStore,
			columns:columns,
			sm:new Ext.grid.RowSelectionModel({}),
			tbar:['-','输入关键字&nbsp;',
				new Ext.ux.form.SearchField({  
	                store: this.reclaimStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: '支持设备编号模糊查询'
	            }),'-','日期:',
	            '-',
	            {id:'reclaim_startDate_id',xtype:'datefield',name:'reclaim_startDate',format:'Y-m-d',
	            	emptyText:'开始日期',width:100,listeners:{
	            		scope:this,
	            		blur: function(comp){
	            			var v = comp.getValue();
	            			if(v){
	            				Ext.getCmp('reclaim_endDate_id').minValue=v.add(Date.DAY,1);
	            			}else{
	            				Ext.getCmp('reclaim_endDate_id').setMinValue(null);
	            			}
	            		}
	            	}
	            },'-',
	            {id:'reclaim_endDate_id',xtype:'datefield',name:'reclaim_endDate',
	            	width:100,format:'Y-m-d',emptyText:'结束日期',listeners:{
	            		scope:this,
	            		blur: function(comp){
	            			var v = comp.getValue();
	            			if(v){
	            				Ext.getCmp('reclaim_startDate_id').setMaxValue(v.add(Date.DAY,-1));
	            			}else{
	            				Ext.getCmp('reclaim_startDate_id').setMaxValue(null);
	            			}
	            		}
	            	}},'-','设备类型',
	            	{ id:'device_type_id',hiddenName:'device_type',xtype:'combo',allowBlank:true,
           				 store:new Ext.data.ArrayStore({
              						fields:['device_type','device_name'],
             						data:[['','请选择...'],['STB','单机顶盒'],['CARD','单智能卡'],
									['MODEM','单MODEM'],['STBCARD','机卡配对']]
						}),
						displayField:'device_name',valueField:'device_type',width:80 
					},'-',{
		            	xtype:'combo',store:new Ext.data.ArrayStore({
		            			fields:['reclaimTypeText','reclaimType'],
		            			data:[['所有回收','ALL'],['执行回收','NOW'],['历史回收','HISTORY']]
		            		}),displayField:'reclaimTypeText',valueField:'reclaimType',
		            	emptyText:'执行中或历史回收',width:80,
		            	listeners:{
		            		scope:this,select:this.queryTransferByType
		            	}
	            	},'-','  操作类型',
					{ id:'confirm_type_id',hiddenName:'confirm_type',xtype:'combo',allowBlank:true,
           				 store:new Ext.data.ArrayStore({
              						fields:['confirm_type','confirm_name'],
             						data:[[' ','请选择...'],['CONFIRM','取消确认'],['UNCONFIRM','确认']]
						}),
						displayField:'confirm_name',valueField:'confirm_type',width:80 
					},'-',
	            {xtype:'button',text:'查询',iconCls:'icon-query',scope:this,handler:this.doDateQuery},'-'
			],
			bbar : new Ext.PagingToolbar({
				store : this.reclaimStore,
				pageSize : Constant.DEFAULT_PAGE_SIZE
			})
		});
	},
	doDateQuery:function(){
		var startDate = Ext.getCmp('reclaim_startDate_id').getRawValue();
		var endDate = Ext.getCmp('reclaim_endDate_id').getRawValue();
		var deviceType = Ext.getCmp('device_type_id').getValue();
		var confirmType = Ext.getCmp('confirm_type_id').getValue();
		var store = this.getStore();
		store.baseParams = {
			startDate: startDate,
			endDate: endDate,
			deviceType : deviceType,
			confirmType : confirmType
		};
		store.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
	},
	queryTransferByType: function(combo){
		var value = combo.getValue(),isHistory='';
		if(value == 'NOW'){
			isHistory = 'F';
		}else if(value == 'HISTORY'){
			isHistory = 'T';
		}else{
			isHistory = 'All';
		}
		var store = this.getStore();
		store.baseParams['isHistory'] = isHistory;
		store.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
	},
	doCancel:function(doneCode){//取消回收
		Confirm('确认取消回收吗？',this,function(){
			var record = this.getSelectionModel().getSelected();
			Ext.Ajax.request({
				url:root+'/resource/Device!cancelReclaimDevice.action',
				params:{
					doneCode:doneCode,
					deviceId:record.get('device_id')
				},
				scope:this,
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					if(data['success'] === true){
						Alert('保存成功!');
						Ext.getCmp('reclaimDeviceGridId').getStore().reload();

					}
				}
			});
		});
	},
	doReclaim:function(doneCode){//回收
		var win = Ext.getCmp('confirmReclaimWinId');
		if(!win){
			win = new ConfirmReclaimWin();
		}
		win.show();
	}
});

var ConfirmReclaimWin = Ext.extend(Ext.Window,{
	constructor:function(){
		ConfirmReclaimWin.superclass.constructor.call(this,{
			id:'confirmReclaimWinId',
			title:'回收确认',
			border:false,
			width:300,
			height:180,
			layout:'fit',
			items:[{
				id:'reclaimFormId',xtype:'form',bodyStyle:'padding-top:10px',
				border:false,
				items:[
					{fieldLabel:'是否回收',xtype:'paramcombo',hiddenName:'flag',
						paramName:'BOOLEAN',allowBlank:false
						,listeners:{
							scope:this,
							select:function(com){
								var deviceComp = Ext.getCmp('status_panel');
								if(com.getValue() == 'F'){
									deviceComp.hide()
								}else{
									deviceComp.show();
								}
							}
						}	
					},{
				xtype: 'panel',
				baseCls: 'x-plain',
				layout: 'form',
				id: 'status_panel',
				width: 280,
				labelWidth: 100,
				items: [
					{fieldLabel:'设备状态',hiddenName:'deviceStatus',allowBlank:false,id:'deviceStatusId',
					xtype:'paramcombo',paramName:'DEVICE_STATUS_R_DEVICE',defaultValue:'CORRUPT'},{fieldLabel:'是否新机',xtype:'paramcombo',hiddenName:'isNewStb',
						id:'isNewStbId',paramName:'BOOLEAN',allowBlank:false,defaultValue:'F'}
				]}
				]
			}],
			buttonAlign:'center',
			buttons:[
				{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
						this.hide();
					}
				}
			],
			listeners:{
				scope:this,
				hide:function(){
					Ext.getCmp('reclaimFormId').getForm().reset();
				}
			}
		});
	},
	initComponent:function(){
		ConfirmReclaimWin.superclass.initComponent.call(this);
		App.form.initComboData( this.findByType("paramcombo") );
	},
	doSave:function(){
		var form = Ext.getCmp('reclaimFormId').getForm();
		if(!form.isValid())return;
		var record = Ext.getCmp('reclaimDeviceGridId').getSelectionModel().getSelected();

		var values = form.getValues();
		values['doneCode'] = record.get('done_code');
		values['deviceId'] = record.get('device_id');
		var msg = Show();
		Ext.Ajax.request({
			url:root+'/resource/Device!reclaimDevice.action',
			params:values,
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data['success'] === true){
					msg.hide();msg=null;
					Alert('保存成功!');
					if(values['flag'] == 'T'){
						Ext.getCmp('reclaimDeviceGridId').getStore().load({
							params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}
						});
					}else{
						record.set('status','NOTCONFIRM');
						record.set('status_text','不确认');
					}
					record.commit();
					this.hide();
				}
			}
		});
	}
});

ReclaimDevice = Ext.extend(Ext.Panel,{
	constructor:function(){
		var grid = new ReclaimDeviceGrid();
		ReclaimDevice.superclass.constructor.call(this,{
			id:'ReclaimDevice',
			title:'设备回收',
			closable: true,
			border : false ,
//			baseCls: "x-plain",
			layout:'fit',
			items:[grid/*,checkInDeviceInfoGrid*/]
		});
	}
});












