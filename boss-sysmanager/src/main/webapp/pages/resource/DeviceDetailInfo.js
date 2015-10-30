
var COMMON_LU = lsys('common');
var DEV_COMMON_LU = lsys('DeviceCommon');
var DEV_DET_LU = lsys('DeviceDetailInfo');
var MSG_LU = lsys('msgBox');

var DeviceDetailForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	constructor:function(p){
		this.parent = p;
		DeviceDetailForm.superclass.constructor.call(this,{
			border:false,
			layout:'column',
			region:'north',
			height:50,
			labelWidth:120,
			bodyStyle:'padding:10px',
			items:[
				{columnWidth:.40,layout:'form',border:false,items:[
						{xtype:'textfield',fieldLabel:DEV_COMMON_LU.labelDevCode,allowBlank:false,
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
					{columnWidth:.6,border:false,items:[
						{xtype:'button',text:COMMON_LU.query,iconCls:'icon-query',
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
							data['tran_status_text'] = COMMON_LU.confirm;
						}else if(data['tran_status'] == 'UNCONFIRM'){
							data['tran_status_text'] = COMMON_LU.unConfirm;
						}
						//设备有对应客户时，该设备为使用状态
						//物流要求：设备状态分为 空闲、损坏和使用三种。
						if(data['depot_status'] == 'USE'){
							data['device_status_text'] = COMMON_LU.statusEnum.USE;	
						}else if(data['device_status'] == 'ACTIVE'){
							data['device_status_text'] = COMMON_LU.statusEnum.IDLE;	
						}
						if(Ext.isEmpty(data['deviceInput'])){
							data['deviceInput'] = {};
						}
						
						if(data.device_type == 'STB'){
							data['pair_card_label'] = DEV_COMMON_LU.labelPairCardCode;
							data['pair_card_type_label'] = DEV_COMMON_LU.labelPairCardType;
//							data['pair_modem_label'] = DEV_COMMON_LU.labelPairModemCode;
//							data['pair_modem_type_label'] = DEV_COMMON_LU.labelPairModemType;
						}else if(data.device_type == 'CARD'){
							data['pair_card_label'] = DEV_COMMON_LU.labelPairStbCode;
							data['pair_card_type_label'] = DEV_COMMON_LU.labelPairStbType;
							data['pair_modem_label'] = DEV_COMMON_LU.labelPairModemCode;
							data['pair_modem_type_label'] = DEV_COMMON_LU.labelPairModemType;
							data.pair_device_code = data.pair_device_stb_code;
							data.pair_device_model_text = data.pair_device_stb_model_text;
						}else {
							data['pair_card_label'] = DEV_COMMON_LU.labelPairCardCode;
							data['pair_card_type_label'] = DEV_COMMON_LU.labelPairCardType;
							data['pair_modem_label'] = DEV_COMMON_LU.labelPairStbCode;
							data['pair_modem_type_label'] = DEV_COMMON_LU.labelPairStbType;
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
					Alert(MSG_LU.tipDevDosNotExists,function(){
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
					'<td class="label" width=20%>' + DEV_COMMON_LU.labelDeviceType + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_type_text}</td>',
					'<td class="label" width=20%>' + DEV_COMMON_LU.labelDeviceModel + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_model_text}</td>',
				'</tr>',
				'<tr height=24>',
					'<td class="label" width=20%>' + DEV_COMMON_LU.labelDevCode + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{device_code}</td>',
					'<td class="label" width=20%>' + DEV_COMMON_LU.labelDevStatus + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{device_status_text}</td>',
		    	'</tr>',
		    	'<tr height=24>',
					'<td class="label" width=20%>{pair_card_label}：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_code}</td>',
			      	'<td class="label" width=20%>{pair_card_type_label}：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_model_text}</td>',
		    	'</tr>',
//		    	'<tr height=24>',
//					'<td class="label" width=20%>{pair_modem_label}：</td>',
//			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_modem_code}</td>',
//			      	'<td class="label" width=20%>{pair_modem_type_label}：</td>',
//			      	'<td class="input_bold" width=30%>&nbsp;{pair_device_modem_model_text}</td>',
//		    	'</tr>',
		    	'<tr height=24>',
					'<td class="label" width=20%>' + DEV_COMMON_LU.labelCustNo + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{[values.cust_no || ""]}</td>',
			      	'<td class="label" width=20%>' + DEV_COMMON_LU.labelCustName + '：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{[values.cust_name || ""]}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		    		'<tpl if="values.device_type == \'MODEM\'">',
			      		'<td class="label" width=20%>' + DEV_COMMON_LU.labelModemModel + '：</td>',
						'<td class="input_bold" width=30%>&nbsp;{[values.modem_type_name || ""]}</td>',
					'</tpl>',
					'<tpl if="values.device_type != \'MODEM\'">',
			      		'<td class="label" width=20%>&nbsp;</td>',
						'<td class="input_bold" width=30%>&nbsp;</td>',
					'</tpl>',
		      		'<td class="label" width=20%>' + COMMON_LU.confirmDoAction + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.tran_status_text || ""]}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		    		'<td class="label" width=20%>' + COMMON_LU.depotText + '：</td>',
					'<td class="input_bold" width=30%>&nbsp;{depot_id_text}</td>',
		     		'<td class="label" width=20%>' + COMMON_LU.countyText + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{county_id_text}</td>',
		    	'</tr>',
		    	
		    	'<tr height=24>',
		     		'<td class="label" width=20%>' + DEV_COMMON_LU.vitualDevice + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.is_virtual_text || ""]}</td>',
		      		'<td class="label" width=20%>' + DEV_COMMON_LU.isDifferent + '：</td>',
		      		'<td class="input_bold" width=30%>{diffence_type_text}</td>',
		    	'</tr>',
		    	
		    	'<tr height=24>',
		     		'<td class="label" width=20%>' + lsys('CheckIn.labelInputNo') + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.deviceInput.input_no || ""]}</td>',
		      		'<td class="label" width=20%>' + lsys('CheckIn.labelOrderNo') + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.deviceInput.order_no || ""]}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		     		'<td class="label" width=20%>' + DEV_COMMON_LU.labelInputDate + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.deviceInput.create_time || ""]}</td>',
		      		'<td class="label" width=20%>' + DEV_COMMON_LU.labelDevInputDepot + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.deviceInput.depot_name || ""]}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		     		'<td class="label"  width=20%>' + lsys('CheckIn.labelInputOptr') + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.deviceInput.optr_name || ""]}</td>',
		      		'<td class="label" width=20%>' + lsys('CheckIn.labelInputBatchNum') + '：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{[values.batch_num || ""]}</td>',
		    	'</tr>',
			'</table>'
		);
		
		this.deviceTransferGrid = new Ext.grid.GridPanel({
			title:DEV_COMMON_LU.titleTransInfo,
			region:'center',
			height:100,
			store:new Ext.data.JsonStore({
				fields:['transfer_no','depot_source','depot_source_text','status_text',
					'depot_order','depot_order_text','create_time','confirm_optr_name','confirm_date']
			}),
			columns:[
				{header:DEV_COMMON_LU.labelTransNo,dataIndex:'transfer_no',width:75,renderer:App.qtipValue},
				{header:DEV_DET_LU.labelSourceDepot,dataIndex:'depot_source_text',width:100,renderer:App.qtipValue},
				{header:DEV_DET_LU.labelTargetDepot,dataIndex:'depot_order_text',width:100,renderer:App.qtipValue},
				{header:COMMON_LU.status,dataIndex:'status_text',width:50,renderer:Ext.util.Format.statusShow},
				{header:COMMON_LU.createDate,dataIndex:'create_time',width:120,renderer:Ext.util.Format.dateFormat},
				{header:DEV_COMMON_LU.labelConfirmOptr,dataIndex:'confirm_optr_name',width:70},
				{header:DEV_COMMON_LU.labelConfirmDate,dataIndex:'confirm_date',width:120,renderer:Ext.util.Format.dateFormat}
			]
		});
		
		this.deviceUseRecordsGrid = new Ext.grid.GridPanel({
			title:DEV_DET_LU.titleUseRecord,
			region:'east',
			width:380,
			height:100,
			split:true,
			store:new Ext.data.JsonStore({
				fields:['done_code','busi_name','old_value_text','new_value_text','cust_no','cust_name',
					'busi_name','change_date','optr_name','buy_mode_text'
				]
			}),
			columns:[
				{header:COMMON_LU.labelDoneCode,dataIndex:'done_code',width:75},
				{header:COMMON_LU.labelBusiName,dataIndex:'busi_name',width:80},
				{header:COMMON_LU.labelOldStatus,dataIndex:'old_value_text',width:80},
				{header:COMMON_LU.labelNewStatus,dataIndex:'new_value_text',width:80},
				{header:DEV_COMMON_LU.labelCustNo,dataIndex:'cust_no',width:90},
				{header:DEV_COMMON_LU.labelCustName,dataIndex:'cust_name',width:100,renderer:App.qtipValue},
				{header:COMMON_LU.optr,dataIndex:'optr_name',width:75},
				{header:DEV_COMMON_LU.labelOperateTime,dataIndex:'change_date',width:120},
				{header:COMMON_LU.labelBuyMode,dataIndex:'buy_mode_text',width:80}
			]
		});
		
		DeviceDetailPanel.superclass.constructor.call(this,{
			border:false,
			region:'center',
			title:DEV_COMMON_LU.titleDeviceInfo,
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
			title:DEV_DET_LU._title,
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