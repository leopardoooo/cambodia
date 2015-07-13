/**
*调账
*/
AdjustFrom = Ext.extend(BaseForm ,{
	url: Constant.ROOT_PATH + "/core/x/Acct!acctAdjust.action",
	acctRecord: null,
	constructor: function(addRadioGroup){
		this.addRadioGroup = addRadioGroup;
		this.acctRecord = App.getApp().main.infoPanel.acctPanel.acctItemGrid.getSelectionModel().getSelected();
		AdjustFrom.superclass.constructor.call(this,{
			border: false,
			labelWidth: 85,
			layout : 'form',
			baseCls: 'x-plain',
			bodyStyle: Constant.TAB_STYLE,
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
				xtype:'hidden',
				value: this.acctRecord.get("acct_id"),
				name:'acctId'
			},{
				xtype:'hidden',
				value: this.acctRecord.get("prod_sn"),
				name:'prodSn'
			},{
				xtype:'hidden',
				value: this.acctRecord.get("acctitem_id"),
				name:'acctItemId'
			},{
				fieldLabel:'账目名称',
				xtype:'textfield',
				style:Constant.TEXTFIELD_STYLE,
				value: this.acctRecord.get("acctitem_name")
			},{
				fieldLabel:'调账金额',
				allowBlank : false,minValue:0,
				xtype:'numberfield',
				name:'adjust_fee'
			},this.addRadioGroup == true ? {
				fieldLabel:'充/扣',//冲正、扣负
				allowBlank : false,
				xtype:'radiogroup',
				id:'positiveOrNegative',
				width:100,
	            items: [{boxLabel: '充值',name : "rg",value : true,inputValue:'true'},
	            	{boxLabel: '扣费',name : "rg",value : false,inputValue:'false'}]
	        
			}:{xtype:'hidden'},{
				fieldLabel:'调账原因',
				allowBlank : false,
				xtype:'paramcombo',
				paramName:'ADJUST_REASON',
				hiddenName:'reason'
			}]
		})
	},
	doInit: function(){
		AdjustFrom.superclass.doInit.call(this);
		var store = this.getForm().findField('reason').getStore();
		store.removeAt(store.find('item_value','CONFIG'));
		store.removeAt(store.find('item_value','BUG'));
	},
	getValues : function(){
		var all = this.getForm().getValues();
		var positiveOrNegative = Ext.getCmp('positiveOrNegative');
		var flag = true;
		if(positiveOrNegative){
			flag = positiveOrNegative.getValue().value;
		}
		var fee = Ext.util.Format.formatToFen(all['adjust_fee']);
		all['adjust_fee'] = !flag ?fee: 0-fee ;
		return all;
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});