/**
 * 【模拟转数】
 */


AtvAcctItemPanel = Ext.extend(Ext.Panel,{
	totalRecord : null,
	tariffRent : null,
	constructor : function(atvAcctInfo){
		this.totalRecord = {};
		this.totalRecord['active_balance'] = 0;//余额
		this.totalRecord['owe_fee'] = 0;//往月欠费
		this.totalRecord['real_bill'] = 0;//本月费用
		if(atvAcctInfo.acctitems){
			//只取第一个账目的信息
			if(atvAcctInfo.acctitems.length > 0){
				this.totalRecord.active_balance = atvAcctInfo.acctitems[0].active_balance + this.totalRecord.active_balance;
				this.totalRecord.owe_fee = atvAcctInfo.acctitems[0].owe_fee + this.totalRecord.owe_fee;
				this.totalRecord.real_bill = atvAcctInfo.acctitems[0].real_bill;
				
			}
		}
		
		var store = App.getApp().main.infoPanel.getUserPanel().prodGrid.getStore();
		if(store.getCount() > 0){
			this.tariffRent = Ext.util.Format.formatFee(store.getAt(0).get('tariff_rent'));
		}
		
		AtvAcctItemPanel.superclass.constructor.call(this,{
			title : '模拟用户账目信息',
			layout : 'form',
			anchor : '100%',
			border : false,
			bodyStyle : Constant.TAB_STYLE,
			labelWidth : 75,
			items : [{
				xtype : 'numberfield',
				fieldLabel : '余额',
				readOnly: true,
				id : 'balance',
				value : Ext.util.Format.formatFee(this.totalRecord.active_balance),
				style: Constant.TEXTFIELD_STYLE
			},{
				fieldLabel : '往月欠费',
				xtype : 'numberfield',
				id : 'ownfee',
				value : Ext.util.Format.formatFee(this.totalRecord.owe_fee),
				readOnly: true,
				style: Constant.TEXTFIELD_STYLE
			},{
				fieldLabel : '本月费用',
				id : 'curMonthFee',
				xtype : 'numberfield',
				value : Ext.util.Format.formatFee(this.totalRecord.real_bill),
				allowNegative : false,
				maxValue : this.tariffRent,
				maxText : '最大金额为'+this.tariffRent,
				listeners : {
					scope : this,
					change : this.doFeeChange
				}
			},{
				fieldLabel : '实际余额',
				xtype : 'numberfield',
				id : 'real_balance',
				value : Ext.util.Format.formatFee(this.totalRecord.active_balance-this.totalRecord.owe_fee-this.totalRecord.real_bill),
				style: Constant.TEXTFIELD_STYLE,
				readOnly: true
			}]
			
		})
	},
	doFeeChange : function(txt){
		if(txt.isValid()){
			var fee = Ext.getCmp('balance').getValue() - Ext.getCmp('ownfee').getValue()
				- Ext.getCmp('curMonthFee').getValue();
			Ext.getCmp('real_balance').setValue(Ext.util.Format.formatFee(Ext.util.Format.formatToFen(fee)));
		}
		
	}
})

AtvToDtvForm = Ext.extend(UserBaseForm,{
	url:Constant.ROOT_PATH + "/core/x/User!atvToDtv.action",
	atvAcctInfo:null,
	constructor:function(){
		var record = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
		var acctStore = App.getApp().main.infoPanel.acctPanel.acctGrid.getStore();
		for(var i=0;i<acctStore.getCount();i++){
			if(acctStore.getAt(i).get('user_id') === record.get('user_id')){
				this.atvAcctInfo = acctStore.getAt(i).data;
				break;
			}
		}
		AtvToDtvForm.superclass.constructor.call(this);
	},
	doInit:function(){
		var record = new Ext.data.Record({
			item_name : '数字电视',
			item_value : 'DTV'
		});
		Ext.getCmp('comboForUserBaseForm').getStore().add(record);
		Ext.getCmp('comboForUserBaseForm').setValue('DTV');
		this.changeSubFrom('DTV');
		//用户数据
		var userStore = App.getApp().main.infoPanel.getUserPanel().userGrid.getStore();
		var dtvCount = userStore.query('user_type','DTV').getCount();
		if( dtvCount ==0 ){
			this.obj.dtv = 0;
		}else{
			this.obj.dtv = 1;
			this.doTerminalTypeCount();
		}
		this.doFilterTerminal();
		
		var item = this.items.itemAt(1).items;
		item.insert(2,new AtvAcctItemPanel(this.atvAcctInfo));
		this.doLayout();
		Ext.getCmp('comboForUserBaseForm').setDisabled(true);
	},
	doFilterTerminal : function(){//过滤、设置终端类型
		this.doConfigsData();
		AtvToDtvForm.superclass.doFilterTerminal.call(this);
	},
	doValid : function(){
		if(this.getForm().isValid()){
			if(Ext.getCmp('real_balance').getValue() < 0){
				var obj = {};
				obj["isValid"] = true;
				obj["msg"] = "您没有输入机顶盒,";
				return obj;
			}
			
			return true;
		}else{
			return false;
		}
	},
	getFee : function(){
		if(Ext.getCmp('real_balance').getValue() < 0){
			return -Ext.getCmp('real_balance').getValue();
		}
		return 0;
	},
	getValues :function(){
		var values = this.getForm().getValues();
		var obj={};
		var type = 'userDtv';
		//给需要的属性添加前缀，以供后台action中组合不同对象
		for(var i in values){
			if(i != 'balance' && i != 'ownfee' &&
			 	i != 'curMonthFee' && i != 'real_balance'){
			 	obj[type+'.'+i]=values[i];
			}
		}
		obj['curMonthFee'] = Ext.util.Format.formatToFen(values['curMonthFee']);
		if(values['real_balance'] < 0){
			obj['payFee'] = -Ext.util.Format.formatToFen(values['real_balance']);
		}else{
			obj['payFee'] = 0;
		}
		return obj;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

