/**
 * 订单修改
 */
OrderProdEditForm = Ext.extend(BaseForm, {
	url: Constant.ROOT_PATH+"/core/x/ProdOrder!saveOrderEdit.action",
	baseData: null,
	constructor: function(){
		this.prodStore = new Ext.data.JsonStore({
			fields: ['prod_id', 'prod_name', 'prod_type']
		});
		this.tariffStore = new Ext.data.JsonStore({
			fields: ['tariff_id', 'disct_name', 'disct_rent', 'billing_cycle']
		});
		OrderProdEditForm.superclass.constructor.call(this, {
			bodyStyle: 'padding-top:15px',
			labelWidth: 180,
			/*layout: 'column',
			defaults:{
				border: false,
				layout: 'form',
				columnWidth: .5
			},*/
			items:[
				{
					id: 'cmProdId',
					fieldLabel: lmain("user._form.prodName"),
					xtype: 'combo',
					store: this.prodStore,
					displayField: 'prod_name', valueField: 'prod_id',
					allowBlank: false, listWidth: 200,
					listeners: {
						scope: this,
						select: this.doSelectedProd
					}
				},{
					style: 'margin-left:185px;padding:5px 0 8px 0;',
					xtype: 'button',
					text: lmain("user._form.switchUsers"),
					iconCls: 'icon-add-user',
					scope: this,
		        	handler: function(){
		        		this.openDispatchUserWindow();
		        	}
				},{
					id: 'cmTariffId',
					fieldLabel: lmain("user._form.prodTariff"),
					xtype: 'combo',
					store: this.tariffStore,
					displayField: 'disct_name', valueField: 'tariff_id',
					allowBlank: false,
					listeners: {
						scope: this,
						select: this.doSelectTariff
					}
				},{
					id: 'nfMonthId',
					fieldLabel: lmain("user._form.prodOrderMonths"),
					xtype: 'numberfield',
					allowNegative: false,
					allowDecimals: false,
					allowBlank: false,
					minValue: 0.01,
					listeners: {
						scope: this,
						change: this.doChangeOrderMonth
					}
				},{
					id: 'dfStartDateId',
					fieldLabel: lmain("user._form.prodStartDate"),
					xtype: 'displayfield'
				},{
					id: 'dfEndDateId',
					fieldLabel: lmain("user._form.prodExpDate"),
					xtype: 'datefield',
					format: 'Y-m-d',
					editable: false,
					allowBlank: false,
					listWidth: 200,
					listeners: {
						scope: this,
						select: this.doSelectEndDate
					}
				},{
					id: 'oldOrderFeeId',
					fieldLabel: lmain("user._form.oldOrderFee"),
					xtype: 'textfield',
					readOnly: true
				},{
					id: 'oldTransFeeId',
					fieldLabel: lmain("user._form.oldTransFee"),
					xtype: 'textfield',
					readOnly: true
				},{
					id: 'realOrderFeeId',
					fieldLabel: lmain("user._form.realOrderFee"),
					xtype: 'textfield',
					readOnly: true
				},{
					id: 'dfDiffFeeId',
					fieldLabel: lmain("user._form.newAddFee"),
					xtype: 'numberfield',
					allowBlank: false,
					listeners: {
						scope: this,
						change: this.doChangeDiffFee
					}
				}
			]
			/*items: [{
				items:[{
					id: 'cmProdId',
					fieldLabel: lmain("user._form.prodName"),
					xtype: 'combo',
					store: this.prodStore,
					displayField: 'prod_name', valueField: 'prod_id',
					allowBlank: false, listWidth: 200,
					listeners: {
						scope: this,
						select: this.doSelectedProd
					}
				},{
					id: 'cmTariffId',
					fieldLabel: lmain("user._form.prodTariff"),
					xtype: 'combo',
					store: this.tariffStore,
					displayField: 'disct_name', valueField: 'tariff_id',
					allowBlank: false,
					listeners: {
						scope: this,
						select: this.doSelectTariff
					}
				},{
					id: 'dfStartDateId',
					fieldLabel: lmain("user._form.prodStartDate"),
					xtype: 'displayfield'
				},{
					id: 'oldOrderFeeId',
					fieldLabel: lmain("user._form.oldOrderFee"),
					xtype: 'textfield',
					readOnly: true
				},{
					id: 'realOrderFeeId',
					fieldLabel: lmain("user._form.realOrderFee"),
					xtype: 'textfield',
					readOnly: true
				}]
			},{
				items:[{
					borderStyle: 'padding: 100px',
					xtype: 'button',
					text: lmain("user._form.switchUsers"),
					iconCls: 'icon-add-user',
					scope: this,
		        	handler: function(){
		        		this.openDispatchUserWindow();
		        	}
				},{
					id: 'nfMonthId',
					fieldLabel: lmain("user._form.prodOrderMonths"),
					xtype: 'numberfield',
					allowNegative: false,
					allowDecimals: false,
					allowBlank: false,
					minValue: 0.01,
					listeners: {
						scope: this,
						change: this.doChangeOrderMonth
					}
				},{
					id: 'dfEndDateId',
					fieldLabel: lmain("user._form.prodExpDate"),
					xtype: 'datefield',
					format: 'Y-m-d',
					editable: false,
					allowBlank: false,
					listWidth: 200,
					listeners: {
						scope: this,
						select: this.doSelectEndDate
					}
				},{
					id: 'oldTransFeeId',
					fieldLabel: lmain("user._form.oldTransFee"),
					xtype: 'textfield',
					readOnly: true
				},{
					id: 'dfDiffFeeId',
					fieldLabel: lmain("user._form.newAddFee"),
					xtype: 'numberfield',
					allowBlank: false,
					listeners: {
						scope: this,
						change: this.doChangeDiffFee
					}
				}]
				}]*/
		});
	},
	doInit:function(){
		this.doLoadBaseData();
		OrderProdEditForm.superclass.doInit.call(this);
	},
	doLoadBaseData: function(){
		var record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelections()[0];
		Ext.Ajax.request({
			url :root + '/core/x/ProdOrder!queryOrderToEdit.action',
			scope : this,
			params: {
				order_sn: record.get('order_sn')
			},
			success : function(res,opts){
				var data = Ext.decode(res.responseText);
				this.baseData = data;
				var prodList = data['prodList'];
				var tariffList = data['tariffMap'];
				
				Ext.getCmp('cmProdId').getStore().loadData(prodList);
				Ext.getCmp('cmProdId').setValue(data['prod_id']);
				Ext.getCmp('nfMonthId').setValue(data['order_months']);
				this.doSelectedProd();
			}
		});
	},
	doSelectedProd: function(){
		var prodId = Ext.getCmp('cmProdId').getValue();
		var tariffCmp = Ext.getCmp('cmTariffId');
		this.tariffStore.loadData(this.baseData['tariffMap'][prodId]);
		
		if(this.tariffStore.find('tariff_id', this.baseData['tariff_id']) >= 0){
			tariffCmp.setValue( this.baseData['tariff_id'] );
		}else{
			tariffCmp.setValue('');
		}
		
		Ext.getCmp('oldOrderFeeId').setValue( this.baseData['old_order_fee']/100.0 );
		Ext.getCmp('oldTransFeeId').setValue( this.baseData['old_transfer_fee']/100.0 );
		
		this.baseData['eff_date'] = this.baseData['eff_date'].substring(0, 10);
		Ext.getCmp('dfStartDateId').setValue( this.baseData['eff_date'] );
		
		Ext.getCmp('dfEndDateId').minValue = Date.parseDate( this.baseData['eff_date'], 'Y-m-d' );
		
		Ext.getCmp('dfEndDateId').setValue( Date.parseDate(this.baseData['exp_date'], 'Y-m-d H:i:s') );
		
		this.prodStore.each(function(record){
			if(record.get('prod_id') == prodId){
				this.prodType = record.get('prod_type');
				return false;
			}
		}, this);
		this.prodId = prodId;
//		this.openDispatchUserWindow();
	},
	openDispatchUserWindow: function(){
		if(this.isPkg()){
			this.selectUserPanel = new SelectUserPanel(this);
			this.selectUserPanel.loadPackageUsers(this.prodId, this.baseData['last_order_sn']);
		}
	},
	isPkg: function(){
		return this.prodType === "SPKG" || this.prodType === "CPKG";
	},
	doSelectTariff: function(){
		this.calRealOrderFee();
	},
	doSelectEndDate: function(df, endDate){
		//选择结束计费日期，计算包月数
		var orderMonths = this.calOrderMonths();
		Ext.getCmp('nfMonthId').setValue( Ext.util.Format.round(orderMonths,2) );
		this.calRealOrderFee();
	},
	doChangeOrderMonth: function(filed, newValue, oldValue){
		//根据输入整月数，计算计费结束日期
		var date = Date.parseDate( this.baseData['eff_date'], 'Y-m-d' );
		Ext.getCmp('dfEndDateId').setValue( date.add(Date.MONTH, newValue).add(Date.DAY, -1).format('Y-m-d') );
		this.calRealOrderFee();
	},
	calOrderMonths: function(){
		//下个月的前一天算整月(开始日期2015-09-27，结束日期2015-10-26算一个月)
		var endDate = Ext.getCmp('dfEndDateId').getValue();
		var endDateTom = endDate.add(Date.DAY, 1);
		var months = Ext.util.Format.getMonths( this.baseData['eff_date'], endDateTom.format('Y-m-d') )*1.0;
		var days = Ext.util.Format.getDays( this.baseData['eff_date'], endDateTom.add(Date.MONTH, months*-1).format('Y-m-d') )*1.0;
		
		return days/30.0 + months;
	},
	calRealOrderFee: function(){
		//计算新订单金额
		var tariffComb = Ext.getCmp('cmTariffId');
		var tariffRecord = this.tariffStore.getAt( this.tariffStore.find('tariff_id', tariffComb.getValue()) );
		var orderMonths = this.calOrderMonths();

		var realOrderFee = orderMonths*(tariffRecord.get('disct_rent')/100.0)/(tariffRecord.get('billing_cycle')*1.0);
		realOrderFee = Ext.util.Format.round(realOrderFee, 2);
		
		//新、旧订单差额; 差额为负表示退款，为正表示补收费用
		var orderFee = Ext.getCmp('oldOrderFeeId').getValue()*1.0
		Ext.getCmp('dfDiffFeeId').setValue( Ext.util.Format.round(realOrderFee - orderFee, 2) );
		Ext.getCmp('realOrderFeeId').setValue( realOrderFee );
	},
	doChangeDiffFee: function(field, newValue){
		Ext.getCmp('realOrderFeeId').setValue('');
		var realOrderFee = newValue*1.0 + Ext.getCmp('oldOrderFeeId').getValue()*1.0;
		if(realOrderFee < 0){
			Alert(lmsg("user._form.newOrderAmountCantBeLessThanZero"));
			return;
		}
		if(realOrderFee < Ext.getCmp('oldTransFeeId').getValue()*1.0){
			Alert(lmsg("user._form.newOrderAmountCantBeLessThanTransAmount"));
			return;
		}
		Ext.getCmp('realOrderFeeId').setValue( realOrderFee );
	},
	doValid: function(){
		var formValid =  OrderProdEditForm.superclass.doValid.call(this);
		if(formValid !== true){
			return formValid;
		}
		/*return {
				"isValid": false,
				"msg": lmsg('notChooseDeviceMustBeInput')
		}*/
		return true;
	},
	setTargetData:function(targetData){
		if(this.isPkg()){
			// 获得选中的用户
			var groupUserMap = {};
			Ext.each(targetData, function(rs){
				var gid = rs["package_group_id"];
				if(!groupUserMap[gid]){
					groupUserMap[gid] = [];
				}
				groupUserMap[gid].push(rs["user_id"]);
			}, this);
			// 封装后台数据结构
			var groupSelected = [];
			for(var key in groupUserMap){
				groupSelected.push({
					"package_group_id": key,
					"userSelectList": groupUserMap[key]
				});
			}
			this.groupSelectedData = groupSelected;
		}
	},
	getValues: function(){
		this.baseData['prod_id'] = Ext.getCmp('cmProdId').getValue();
		this.baseData['tariff_id'] = Ext.getCmp('cmTariffId').getValue();
		this.baseData['pay_fee'] = Ext.getCmp('dfDiffFeeId').getValue()*100.0;
		this.baseData['order_months'] = Ext.getCmp('nfMonthId').getValue();
		this.baseData['eff_date'] = this.baseData['eff_date']+' 00:00:00';	//JSON解析日期格式 年月日时分秒
		this.baseData['exp_date'] = Ext.getCmp('dfEndDateId').getValue().format("Y-m-d H:i:s");
		
		if(Ext.isEmpty(this.groupSelectedData)){
			this.groupSelectedData = this.baseData['groupSelected'];
		}
		this.baseData["groupSelected"] = this.groupSelectedData;
		return {
			orderProd: Ext.encode(this.baseData)
		}
	},
	success: function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	TemplateFactory.gTemplate(new OrderProdEditForm());
});