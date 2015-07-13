StbCardFilled = Ext.extend(Ext.FormPanel, {
	stbId:null,
	cardId:null,
	constructor: function() {
		StbCardFilled.superclass.constructor.call(this, {
			id: 'StbCardFilled',
			title: '机卡解绑',
			closable: true,
			border: false,
			baseCls: "x-plain",
			layout: 'column',
			bodyStyle: 'padding-top:15px',
			autoScroll: true,
			defaults: {
				labelWidth: 100,
				layout: 'form',
				border: false,
				baseCls: "x-plain"
			},
			items: [{
				columnWidth: .35,
				border: false,
				items: [{
					xtype: 'textfield',
					fieldLabel: '机顶盒号',
					name: 'stb_id',
					vtype: 'alphanum',
					allowBlank: false,
					width: 160,
					id: 'stbCardFilledId',
					listeners: {
						scope: this,
						specialkey: function(field, e) {
							if (e.getKey() === Ext.EventObject.ENTER) {
								this.doCheck('stbCardFilledId','STB');
							}
						},
						change: function() {
							this.doCheck('stbCardFilledId','STB');
						}
					}
				}]
			},
			{
				columnWidth: .35,
				border: false,
				id:'pairStbCardItem',
				items: [{
					xtype: 'textfield',
					fieldLabel: '智能卡号',
					name: 'card_id',
					width: 150,
					id: 'pairStbCardFilledId',
					vtype: 'alphanum',
					allowBlank: false,
					listeners: {
						scope: this,
						specialkey: function(field, e) {
							if (e.getKey() === Ext.EventObject.ENTER) {
								this.doCheck('pairStbCardFilledId','CARD');
							}
						},
						change: function() {
							this.doCheck('pairStbCardFilledId','CARD');
						}
					}
				}]
			},
			{
				columnWidth: .3,
				border: false,
				items: [{
					xtype: 'button',
					text: '机卡解绑',
					iconCls: 'icon-save',
					scope: this,
					handler: this.doSave
				}]
			}]
		});
	},
    initEvents: function () {
        this.on('afterrender', function () {
            Ext.getCmp('stbCardFilledId').focus(true, 500);
        });
        CardSendForm.superclass.initEvents.call(this);
    },
	doCheck: function(key,type) {
		var cmp = Ext.getCmp(key);
		txt = this;
		Ext.Ajax.request({
			url: root + '/resource/Device!getStbCardById.action',
			params: {
				deviceCode: cmp.getValue(),
				deviceType: type
			},
			scope: this,
			success: function(res, ops) {
				var rs = Ext.decode(res.responseText);
				if (rs != null) {
					if(Ext.isEmpty(rs.pair_device_code) || Ext.isEmpty(rs.device_code)){
						Alert("该设备已经解除绑定!");
						this.resetItem();
					}else{
						Ext.getCmp('pairStbCardFilledId').setValue(rs.pair_device_code);
						Ext.getCmp('stbCardFilledId').setValue(rs.device_code);
						this.stbId = rs.device_id;
						this.cardId = rs.pair_card_id;
					}
				} else {
					Alert('无设备信息!');
				}
			},
			clearData: function() {
				txt.resetItem();
			}
		});
	},
	resetItem : function(){
		Ext.getCmp('stbCardFilledId').reset();
		Ext.getCmp('pairStbCardFilledId').reset();
		Ext.getCmp('stbCardFilledId').focus();
	},
	doSave: function() {
		var card = Ext.getCmp('pairStbCardFilledId');
		var stb = Ext.getCmp('stbCardFilledId');
		if (Ext.isEmpty(card.getValue())) return;
		Ext.Ajax.request({
			url: root + '/resource/Job!cancelStbCardFilled.action',
			params: {
				cardId: this.cardId,
				stbId: this.stbId
			},
			scope: this,
			success: function(res, opt) {
				var data = Ext.decode(res.responseText);
				if (data.success === true) {
					Alert('解除绑定成功!',
					function() {
						card.reset();
						stb.reset();
						stb.focus();
					},
					this);
				}
			}
		});
	}
});