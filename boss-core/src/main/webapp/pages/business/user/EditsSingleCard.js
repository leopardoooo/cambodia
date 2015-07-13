/**
 * 一体机转换
 * @class EditsSingleCardPanel
 * @extends BaseForm
 */
EditsSingleCardPanel = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH + "/core/x/User!toSingleCard.action",
	constructor: function(){
		EditsSingleCardPanel.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			id : 'EditsSingleCardPanel',
			border : false,
			labelWidth: 90,
			layout:'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			defaults : {
				width : 150
			},
			items: [{
				xtype : 'paramcombo',
				fieldLabel : '电视机厂家',
				paramName : 'TV_MODEL',
				hiddenName : 'str4'
			},{
				xtype : 'textfield',
				fieldLabel : '大卡号',
				name : 'str5'
			},{
				xtype:'combo',
				allowBlank : false,
				fieldLabel:'新智能卡号',
				id:'newCardCode',
				forceSelection : true,
				displayField:'device_code',
				valueField:'device_code',
				store : new Ext.data.ArrayStore({
					fields : ['device_code']
				})
			},{
				xtype : 'paramcombo',
				fieldLabel : '回收旧设备',
				paramName : 'BOOLEAN',defaultValue: 'T',
				id : 'reclaim',
				hiddenName : 'reclaimDevice'
			}
			//设备回收：设备状态由仓库确认回收时，确定回收的设备状态
			/*,{
				fieldLabel : '旧设备状态',
				xtype : 'paramcombo',
				paramName : 'DEVICE_STATUS_R_DEVICE',
				hiddenName : 'deviceStatus',
				id : 'status',
				defaultValue:'ACTIVE'}*/]
		});
	},
	doInit: function(){
		EditsSingleCardPanel.superclass.doInit.call(this);
		var cdStore =  App.getApp().main.infoPanel.custPanel.custDeviceGrid.getStore();
		var device = cdStore.query('status','IDLE');
		var cardArr = [];
		device.each(function(item,index,length){
			var data = item.data;
			if(data.loss_reg == 'F'){//空闲且没挂失
				if(data['device_type'] == 'CARD'){
					cardArr.push([data['device_code']]);
				}
			}
		},this);
		var userRecord = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		cardArr.push([userRecord.get('card_id')]);
		if(cardArr.length>0)
			Ext.getCmp('newCardCode').getStore().loadData(cardArr);
	},
	getValues : function(){
		var all = this.getForm().getValues();
		
		return all;
	},
	success: function(form, resultData){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var nuf = new EditsSingleCardPanel();
	var box = TemplateFactory.gTemplate(nuf);
});