var DeviceDetailForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	constructor:function(p){
		this.parent = p;
		DeviceDetailForm.superclass.constructor.call(this,{
			border:false,
			layout:'column',
			region:'north',
			height:50,
			labelWidth:100,
			bodyStyle:'padding:10px',
			items:[
				{columnWidth:.45,layout:'form',border:false,items:[
						{xtype:'textfield',fieldLabel:'设备序列号',allowBlank:false,
							name:'device_code',width:200,
							listeners:{
								scope:this,
								specialkey:function(field,e){
									if(e.getKey() === Ext.EventObject.ENTER){
										this.doQuery();
									}
								}
							}
						}
					]},
					{columnWidth:.55,border:false,items:[
						{xtype:'button',text:'查  询',iconCls:'icon-query',
							listeners:{
								scope:this,
								click:this.doQuery
							}
						}
					]}
			]
		});
	},
	doQuery:function(){
		if(!this.getForm().isValid())return;
		var deviceCodeComp = this.getForm().findField('device_code');
		var value = deviceCodeComp.getValue();
		var parentEle = this.parent.getEl();
		parentEle.mask();
		Ext.Ajax.request({
			url:'resource/Device!queryDeviceInfoByCode.action',
			params:{deviceCode:value},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				var item = this.parent.panel.items.itemAt(0);
				var tpl = this.parent.panel.deviceTpl;
				parentEle.unmask();
				if(data){
					if(item.getEl()){
						if(data['tran_status'] == 'IDLE'){
							data['tran_status_text'] = "确认";
						}else if(data['tran_status'] == 'UNCONFIRM'){
							data['tran_status_text'] = "未确认";
						}
						//设备有对应客户时，该设备为使用状态
						//物流要求：设备状态分为 空闲、损坏和使用三种。
						if(data['depot_status'] == 'USE'){
							data['device_status_text'] = '使用';	
						}else if(data['device_status'] == 'ACTIVE'){
							data['device_status_text'] = '空闲';	
						}
						if(Ext.isEmpty(data['deviceInput'])){
							data['deviceInput'] = {};
						}
						
						if(data.device_type == 'STB'){
							data['pair_card_label'] = '配对卡编号';
							data['pair_card_type_label'] = '配对卡型号';
							data['pair_modem_label'] = '配对MODEM编号';
							data['pair_modem_type_label'] = '配对MODEM型号';
						}else if(data.device_type == 'CARD'){
							data['pair_card_label'] = '配对机顶盒编号';
							data['pair_card_type_label'] = '配对机顶盒型号';
							data['pair_modem_label'] = '配对MODEM编号';
							data['pair_modem_type_label'] = '配对MODEM型号';
							data.pair_device_code = data.pair_device_stb_code;
							data.pair_device_model_text = data.pair_device_stb_model_text;
						}else {
							data['pair_card_label'] = '配对卡编号';
							data['pair_card_type_label'] = '配对卡型号';
							data['pair_modem_label'] = '配对机顶盒编号';
							data['pair_modem_type_label'] = '配对机顶盒型号';
							data.pair_device_code = data.pair_device_stb_code;
							data.pair_device_model_text = data.pair_device_stb_model_text;
						}
						
						tpl.overwrite( item.body, data);
					}
					var transferData;
					if(transferData = data.deviceTransferList){
						this.parent.panel.deviceTransferGrid.getStore().loadData(transferData,false);
					}
					
					if(data.deviceUseRecordsList){
						this.parent.panel.deviceUseRecordsGrid.getStore().loadData(data.deviceUseRecordsList,false);
					}
				}else{
					Alert('查询设备不存在！',function(){
						if(item.getEl()){
							tpl.overwrite( item.body, this.parent.panel.deviceInfo);
						}
						deviceCodeComp.reset();
						this.parent.panel.deviceTransferGrid.getStore().removeAll();
						this.parent.panel.deviceUseRecordsGrid.getStore().removeAll();
						deviceCodeComp.focus(true,100);
					},this);
				}
			},clearData:function(){
				parentEle.unmask();
			}
		});
	}
});

var DeviceDetailPanel = Ext.extend(Ext.Panel,{
	deviceTpl: null,
	deviceInfo:{
		deviceInput:{}
	},
	deviceTransferGrid: null,
	constructor:function(){
		this.deviceTpl = new Ext.XTemplate(
			'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
				'<tr height=24>',
					'<td class="label" width=20%>设备类型：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_type_text}</td>',
					'<td class="label" width=20%>设备型号：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_model_text}</td>',
				'</tr>',
				'<tr height=24>',
					'<td class="label" width=20%>设备编号：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_code}</td>',
					'<td class="label" width=20%>设备状态：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{device_status_text}</td>',
		    	'</tr>',
		    	'<tr height=24>',
					'<td class="label" width=20%>{pair_card_label}：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_code}</td>',
			      	'<td class="label" width=20%>{pair_card_type_label}：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_model_text}</td>',
		    	'</tr>',
		    	'<tr height=24>',
					'<td class="label" width=20%>{pair_modem_label}：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_modem_code}</td>',
			      	'<td class="label" width=20%>{pair_modem_type_label}：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_modem_model_text}</td>',
		    	'</tr>',
		    	'<tr height=24>',
					'<td class="label" width=20%>客户编号：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[values.cust_no || ""]}</td>',
			      	'<td class="label" width=20%>客户姓名：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{[values.cust_name || ""]}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		    		'<tpl if="values.device_type == \'MODEM\'">',
			      		'<td class="label" width=20%>猫类型：</td>',
						'<td class="input_bold" width=30%>&nbsp;{[values.modem_type_name || ""]}</td>',
					'</tpl>',
					'<tpl if="values.device_type != \'MODEM\'">',
			      		'<td class="label" width=20%>&nbsp;</td>',
						'<td class="input_bold" width=30%>&nbsp;</td>',
					'</tpl>',
		      		'<td class="label" width=20%>确认操作：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.tran_status_text || ""]}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		    		'<td class="label" width=20%>所在仓库：</td>',
					'<td class="input_bold" width=30%>&nbsp;{depot_id_text}</td>',
		     		'<td class="label" width=20%>所在县市：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{county_id_text}</td>',
		    	'</tr>',
		    	
		    	'<tr height=24>',
		     		'<td class="label" width=20%>虚拟设备：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.is_virtual_text || ""]}</td>',
		      		'<td class="label" width=20%>是否差异：</td>',
		      		'<td class="input_bold" width=30%>{diffence_type_text}</td>',
		    	'</tr>',
		    	
		    	'<tr height=24>',
		     		'<td class="label" width=20%>入库单号：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.deviceInput.input_no || ""]}</td>',
		      		'<td class="label" width=20%>订单号：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.deviceInput.order_no || ""]}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		     		'<td class="label" width=20%>入库日期：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.deviceInput.create_time || ""]}</td>',
		      		'<td class="label" width=20%>入库仓库：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.deviceInput.depot_name || ""]}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		     		'<td class="label"  width=20%>入库人员：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.deviceInput.optr_name || ""]}</td>',
		      		'<td class="label" width=20%>入库批号：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.batch_num || ""]}</td>',
		    	'</tr>',
			'</table>'
		);
		
		this.deviceTransferGrid = new Ext.grid.GridPanel({
			title:'调拨信息',
			region:'center',
			height:100,
			store:new Ext.data.JsonStore({
				fields:['transfer_no','depot_source','depot_source_text','status_text',
					'depot_order','depot_order_text','create_time','confirm_optr_name','confirm_date']
			}),
			columns:[
				{header:'调拨单号',dataIndex:'transfer_no',width:75,renderer:App.qtipValue},
				{header:'源仓库',dataIndex:'depot_source_text',width:100,renderer:App.qtipValue},
				{header:'目标仓库',dataIndex:'depot_order_text',width:100,renderer:App.qtipValue},
				{header:'状态',dataIndex:'status_text',width:50,renderer:Ext.util.Format.statusShow},
				{header:'创建日期',dataIndex:'create_time',width:120,renderer:Ext.util.Format.dateFormat},
				{header:'确认人',dataIndex:'confirm_optr_name',width:70},
				{header:'确认时间',dataIndex:'confirm_date',width:120,renderer:Ext.util.Format.dateFormat}
			]
		});
		
		this.deviceUseRecordsGrid = new Ext.grid.GridPanel({
			title:'使用记录',
			region:'east',
			width:380,
			height:100,
			split:true,
			store:new Ext.data.JsonStore({
				fields:['done_code','cust_id','cust_no','cust_name',
					'busi_code','busi_name','done_date','optr_name'
				]
			}),
			columns:[
				{header:'流水号',dataIndex:'done_code',width:75},
				{header:'业务名称',dataIndex:'busi_name',width:80},
				{header:'受理编号',dataIndex:'cust_no',width:90},
				{header:'客户名称',dataIndex:'cust_name',width:100,renderer:App.qtipValue},
				{header:'操作员',dataIndex:'optr_name',width:75},
				{header:'操作时间',dataIndex:'done_date',width:120}
			]
		});
		
		DeviceDetailPanel.superclass.constructor.call(this,{
			border:false,
			region:'center',
			title:'设备信息',
			layout:'border',
			items:[{xtype : "panel",region:'north',height:280,autoScroll:true,split:true,
					bodyStyle: "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
					html :this.deviceTpl.applyTemplate(this.deviceInfo)
			},{
				region:'center',
				border:false,
				layout:'border',
				items:[this.deviceTransferGrid,this.deviceUseRecordsGrid]
			}]
		});
	}
});

DeviceDetailInfo = Ext.extend(Ext.Panel,{
	form:null,
	panel:null,
	constructor:function(){
		this.form = new DeviceDetailForm(this);
		this.panel = new DeviceDetailPanel();
		DeviceDetailInfo.superclass.constructor.call(this,{
			id:'DeviceDetailInfo',
			title:'详细查询',
			border:false,
			closable:true,
			layout:'border',
			items:[
				this.form,this.panel
			]
		});
		this.form.getForm().findField('device_code').focus(true,100);
	}
});