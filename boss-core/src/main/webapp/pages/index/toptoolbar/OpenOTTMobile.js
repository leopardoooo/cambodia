/**
 * OTT_MOBILE批量开客、用户并订购产品
 */

OpenOTTMobileWin = Ext.extend(Ext.Window, {
	constructor: function(){
		this.prodStore = new Ext.data.JsonStore({
			url: root+'/core/x/Account!queryOTTMobileFreeProd.action',
			fields: ['prod_id', 'prod_name', 'tariff_id', 'tariff_name'],
			autoLoad: true
		});
		var ott = lbc('home.tools.openOTT');
		this.formPanel = new Ext.FormPanel({
			border: false,
			bodyStyle: 'padding-top: 10px',
			layout: 'column',
			fileUpload: true,
			defaults: {
				layout: 'form',
				columnWidth: 1,
				labelWidth: 150,
				border: false
			},
			items:[{
				items:[{
					xtype: 'textfield',
					fieldLabel: ott['custNamePrefix'],
					name: 'custOtt.cust_name_prefix',
					allowBlank: false
				}]
			},{
				border: false,
				columnWidth: .8,
				items:[{
					width:220,
					fieldLabel: lmain("cust.base.addr"),						
					xtype:'textfield',
					id : 'tempCustAddress',
					name:'custOtt.address',
					allowBlank:false,
					readOnly:true
				}]
			},{
				border: false,
				columnWidth: .15,
				items:[{
					id:'clickAddrId',
					xtype : 'button',
					text: lbc("common.switchor"),
					scope: this,
					width: 60,
					height : 18,
					iconCls:'icon-tree',
					handler: this.doClickAddr
				}]
			},{
				items: [{
					xtype: 'combo',
					fieldLabel: lmain("user._form.prodName"),
					hiddenName: 'custOtt.prod_id',
					store: this.prodStore,
					displayField: 'prod_name', valueField: 'prod_id',
					listWidth: 350,
					allowBlank: false,
					listeners: {
						scope: this,
						select: function(combo, record){
							this.formPanel.getForm().findField('custOtt.tariff_id').setValue(record.get('tariff_id'));
						}
					}
				},{
					fieldLabel: lmain("user._form.expDate"),
					xtype: 'datefield',
					name: 'custOtt.invalid_date',
					format: 'Y-m-d',
					allowBlank: false
				},{
					fieldLabel: ott['num'],
					xtype: 'numberfield',
					name: 'custOtt.cust_number',
					allowDecimals: false,
					allowNegative: false,
					allowBlank: false
				}]},{
					id : 'custAddrId',
					xtype : 'hidden',
					name:'custOtt.addr_id'
				},{
					xtype : 'hidden',
					name:'custOtt.note',
					id : 'custNoteId'
				},{
					xtype: 'hidden',
					name: 'custOtt.tariff_id'
				}]
		});
		OpenOTTMobileWin.superclass.constructor.call(this, {
			title: '批量订购手机基本包',
			width: 500,
			height: 500,
			layout : 'fit',
			closeAction: 'close',
			items: [this.formPanel],
			buttons: [
				{text: lbc('common.save'), iconCls: 'icon-save', scope: this, handler: this.doSave},
				{text: lbc('common.close'), iconCls: 'icon-close', scope: this, handler: this.close}
			]
		});
	},
	doClickAddr:function(btn){
		var win = Ext.getCmp('addrCustSelectWinId');
		if(!win){
			win = new AddrCustSelectWin();
		}
		win.show();
	},
	doSave: function(){
		var form = this.formPanel.getForm();
		if(!form.isValid())return;
		var all = {}, busiParams = {};
		busiParams['busiCode'] = '2500';
		all['jsonParams'] = Ext.encode(busiParams);
		var msg = Show();
		form.submit({
			url: root+'/core/x/Account!createOttMobile.action',
			scope: this,
			params: all,
			success: function(form,action){
			},
			failure: function(form, action){
			}
		});
		setTimeout(function(){
			msg.hide();
			Alert(lbc('home.tools.openOTT.dowlnloadSuccess'));
		}, 3000);
	}
});
