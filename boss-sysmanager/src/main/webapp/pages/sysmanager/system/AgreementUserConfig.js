/**
 * 协议用户配置
 */

AgreementWin = Ext.extend(Ext.Window, {
	constructor: function(){
		var currentDate = new Date();
		this.form = new Ext.form.FormPanel({
			bodyStyle: 'padding-top: 10px',
			labelWidth: 80,
			items: [{
				xtype: 'hidden', 
				name: 'sp_id'
			},{
				xtype: 'textfield',
				fieldLabel: '协议编号',
				name: 'spkg_sn',
				//vtype: 'numZero',
				allowBlank: false
			},{
				xtype: 'textfield',
				fieldLabel: '协议标题',
				name: 'spkg_title'
			},{
				xtype: 'datefield',
				fieldLabel: '生效时间',
				name: 'eff_date',
				allowBlank: false,
				format: 'Y-m-d',
				value: currentDate.format('Y-m-d'),
				//minValue: currentDate.format('Y-m-d'),
				vtype: 'daterange',
                endDateField: 'agreement_exp_date_id',
                customDay: 1
			},{
				id: 'agreement_exp_date_id',
				xtype: 'datefield',
				fieldLabel: '失效时间',
				name: 'exp_date',
				format: 'Y-m-d'
			},{
				xtype: 'textarea',
				fieldLabel: '协议内容',
				name: 'spkg_text',
				anchor: '95%',
				height: 120
			},{
				xtype: 'textarea',
				fieldLabel: '备注',
				name: 'remark',
				anchor: '95%',
				height: 100
			}]
		});
		AgreementWin.superclass.constructor.call(this, {
			border: false,
			width: 400,
			height: 450,
			layout: 'fit',
			closeAction: 'close',
			items: [this.form],
			buttons:[
				{text: '保存', iconCls: 'icon-save', scope: this, handler: this.doSave},
				{text: '关闭', iconCls: 'icon-close', scope: this, handler: this.close}
			]
		});
	},
	show: function(record){
		AgreementWin.superclass.show.call(this);
		if(record){
			this.form.getForm().loadRecord(record);
			if(record.get('status') != 'IDLE'){
				this.form.getForm().findField('spkg_sn').setReadOnly(true);		//协议编号不能修改
			}
		}
	},
	doSave: function(){
		var form = this.form.getForm();
		if(form.isValid()){
			var values = form.getValues(), obj = {};
			for(var v in values){
				obj['spkg.'+v] = values[v];
			}
			
			Ext.Ajax.request({
				url: root+'/config/Config!saveSpkg.action',
				params: obj,
				scope: this,
				success: function(res, opt){
					Alert('保存成功!');
					Ext.getCmp('agreementGridId').doRefresh();
					this.close();
				}
			});
		}
	}
});

AgreementUserWin = Ext.extend(Ext.Window, {
	agreementUserData: {},
	constructor: function(){
		this.form = new Ext.form.FormPanel({
			bodyStyle: 'padding-top: 10px',
			labelWidth: 80,
			defaultType: 'paramcombo',
			items: [{
				xtype: 'hidden',
				name: 'id'
			},{
				xtype: 'hidden', 
				name: 'sp_id'
			},{
				xtype: 'hidden', 
				name: 'fee_id'
			},{
				paramName: 'USER_TYPE',
				fieldLabel: '用户类型',
				hiddenName: 'user_type',
				allowBlank: false,
				listeners: {
					scope: this,
					select: this.doSelectUserType
				}
			},{
				paramName: 'DEVICE_MODEL',
				fieldLabel: '设备型号',
				hiddenName: 'device_model',
				allowBlank: false,
				listeners: {
					scope: this,
					select: this.doSelectBuyType
				}
				
			},{
				paramName: 'BUSI_BUY_MODE',
				fieldLabel: '购买方式',
				hiddenName: 'buy_type',
				defaultValue: 'PRESENT',
				allowBlank: false,
				listeners: {
					scope: this,
					select: this.doSelectBuyType
				}
			},{
				xtype: 'numberfield',
				fieldLabel: '开户数量',
				name: 'open_num',
				allowDecimals: false,
				allowNegative: false,
				allowBlank: false
			},{
				xtype: 'numberfield',
				fieldLabel: '收费金额',
				name: 'fee',
				value: 0
			},{
				xtype: 'displayfield',
	            fieldLabel: '费用名称',
	            name: 'fee_name'
			}]
		});
		AgreementUserWin.superclass.constructor.call(this, {
			border: false,
			width: 400,
			height: 400,
			layout: 'fit',
			closeAction: 'close',
			items: [this.form],
			buttons:[
				{text: '保存', iconCls: 'icon-save', scope: this, handler: this.doSave},
				{text: '关闭', iconCls: 'icon-close', scope: this, handler: this.close}
			]
		});
	},
	show: function(record){
		AgreementUserWin.superclass.show.call(this);
		App.form.initComboData(this.form.findByType('paramcombo'), function(){
			if(!Ext.isEmpty(record) && !Ext.isEmpty(record.get('id'))){
				this.agreementUserData = record.data;
	 			this.form.getForm().loadRecord(record);
				this.doSelectUserType();
//				this.doSelectBuyType();
	 		}else{
	 			this.form.getForm().findField('sp_id').setValue(record.get('sp_id'));
	 		}
	 		var store = this.form.getForm().findField('user_type').getStore();
			store.each(function(r){
				if(r.get('item_value') == 'OTT_MOBILE'){
					store.remove(r);
					return false;
				}
			});
		}, this);
	},
	doSelectUserType: function(){
		var userType = this.form.getForm().findField('user_type').getValue();
		var REF = { 'DTT': '1', 'OTT': '2', 'BAND': '3' };
		var type = REF[userType];
		var combo = this.form.getForm().findField('device_model');
		var store = combo.getStore();
		if(!this.deviceDataBak){
			this.deviceDataBak = [];
			store.each(function(rs){
				this.deviceDataBak.push(rs.data);
			}, this);
		}
		
		var data = [];
		// 开始过滤
		for(var i = 0; i < this.deviceDataBak.length; i++){
			if(this.deviceDataBak[i]["item_idx"] == type){
				data.push(this.deviceDataBak[i]);
			}
		}
		store.removeAll();
		store.loadData(data);
		if(data.length > 0 && Ext.isEmpty(this.agreementUserData['id'])){
			combo.setValue(data[0]['item_value']);
		}
		this.doSelectBuyType();
		this.form.getForm().findField('buy_type').focus();
	},
	doSelectBuyType: function(){
		var deviceModel = this.form.getForm().findField('device_model').getValue();
		var buyType = this.form.getForm().findField('buy_type').getValue();
		
		if(!deviceModel || !buyType){
			return ;
		}
		
		Ext.Ajax.request({
			scope : this,
			url : root + '/config/Config!queryDeviceFee.action',
			params : {
				deviceModel : deviceModel,
				buyMode : buyType
			},
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				var dfFeeName = this.form.getForm().findField('fee_name');
				var txtFee = this.form.getForm().findField('fee');
				if(data && data.length > 0 ){
					data = data[0];
					txtFee.setValue(this.agreementUserData['fee']/100.0 || data["fee_value"]/100.0);
					/*txtFee.setMaxValue(data["max_fee_value"]/100.0);
					txtFee.setMinValue(data["min_fee_value"]/100.0);*/
					txtFee.clearInvalid();
					txtFee.setReadOnly(false);
					this.form.getForm().findField('fee_id').setValue(data['fee_id']);
					dfFeeName.setValue(data["fee_name"]);
				}else{
					dfFeeName.setValue("--");
					txtFee.setValue(0.00);
					/*txtFee.setMaxValue(0);
					txtFee.setMinValue(0);*/
					txtFee.clearInvalid();
					txtFee.setReadOnly(true);
					this.form.getForm().findField('fee_id').setValue('');
				}
			}
		});
	},
	doSave: function(){
		var form = this.form.getForm();
		if(form.isValid()){
			var values = form.getValues(), obj = {};
			for(var v in values){
				obj['spkgUser.'+v] = values[v];
			}
			obj['spkgUser.fee'] = obj['spkgUser.fee'] * 100;
			Ext.Ajax.request({
				url: root+'/config/Config!saveSpkgUser.action',
				params: obj,
				scope: this,
				success: function(res, opt){
					Alert('保存成功!');
					Ext.getCmp('agreementGridId').doLoadSpkgData(values['sp_id']);
					this.close();
				}
			});
		}
	}
});

AgreementBusiFeeWin = Ext.extend(Ext.Window, {
	constructor: function(){
		this.feeStore = new Ext.data.JsonStore({
			url: root+'/config/Config!queryBulkUserBusiFee.action',
			fields: ['fee_id', 'fee_name', 'default_value', 'min_value', 'max_value']
		});
		this.form = new Ext.form.FormPanel({
			bodyStyle: 'padding-top: 10px',
			labelWidth: 80,
			items: [{
				xtype: 'hidden', 
				name: 'id'
			},{
				xtype: 'hidden', 
				name: 'sp_id'
			},{
				xtype: 'combo',
				fieldLabel: '杂费名称',
				hiddenName: 'fee_id',
				store: this.feeStore,
				displayField: 'fee_name', valueField: 'fee_id',
				allowBlank: false,
				listeners: {
					scope: this,
					select: this.doSelectBusiFee
				}
			},{
				xtype: 'numberfield',
				fieldLabel: '金额',
				name: 'fee',
				allowBlank: false
			}]
		});
		AgreementBusiFeeWin.superclass.constructor.call(this, {
			border: false,
			width: 400,
			height: 250,
			layout: 'fit',
			closeAction: 'close',
			items: [this.form],
			buttons:[
				{text: '保存', iconCls: 'icon-save', scope: this, handler: this.doSave},
				{text: '关闭', iconCls: 'icon-close', scope: this, handler: this.close}
			]
		});
	},
	show: function(record){
		AgreementBusiFeeWin.superclass.show.call(this);
		
		this.feeStore.load();
		
		this.feeStore.on('load', function(){
			if(!Ext.isEmpty(record) && !Ext.isEmpty(record.get('id'))){
				
				alert(Ext.encode(record.data));
				
				this.doSelectBusiFee();
				this.form.getForm().loadRecord(record);
	 		}else{
	 			this.form.getForm().findField('sp_id').setValue(record.get('sp_id'));
	 		}
		}, this);
	},
	doSelectBusiFee: function(){
		var feeField = this.form.getForm().findField('fee');
		var combo = this.form.getForm().findField('fee_id');
		var store = combo.getStore();
		var feeId = combo.getValue();
		var record = null;
		store.each(function(r){
			if(r.get('fee_id') == feeId){
				record = r;
				return false;
			}
		}, this);
		
		/*feeField.minValue = Ext.util.Format.formatFee( record.get('min_value') );
		feeField.maxValue = Ext.util.Format.formatFee( record.get('max_value'));*/
		feeField.setValue( Ext.util.Format.formatFee( record.get('default_value') ) );
	},
	doSave: function(){
		var form = this.form.getForm();
		if(form.isValid()){
			var values = form.getValues(), obj = {};
			for(var v in values){
				obj['spkgBusiFee.'+v] = values[v];
			}
			obj['spkgBusiFee.fee'] = obj['spkgBusiFee.fee'] * 100;
			Ext.Ajax.request({
				url: root+'/config/Config!saveSpkgBusiFee.action',
				params: obj,
				scope: this,
				success: function(res, opt){
					Alert('保存成功!');
					Ext.getCmp('agreementGridId').doLoadSpkgData(values['sp_id']);
					this.close();
				}
			});
		}
	}
});

AgreementConfirmWin = Ext.extend(Ext.Window, {
	constructor: function(spId){
		this.feeStore = new Ext.data.JsonStore({
			url: root+'/config/Config!queryBulkUserBusiFee.action',
			fields: ['fee_id', 'fee_name', 'default_value', 'min_value', 'max_value']
		});
		this.form = new Ext.form.FormPanel({
			bodyStyle: 'padding-top: 30px',
			labelWidth: 100,
			items: [{
				xtype: 'hidden', 
				name: 'sp_id',
				value: spId
			},{
				xtype: 'combo',
				fieldLabel: '是否确认',
				hiddenName: 'status',
				store:new Ext.data.ArrayStore({
						fields:['statusDis','statusValue'],
						data:[['确认','CONFIRM'],['取消确认','INVALID']]
					}),displayField:'statusDis',valueField:'statusValue',
				allowBlank: false
			}]
		});
		AgreementConfirmWin.superclass.constructor.call(this, {
			title: '确认协议用户',
			border: false,
			width: 300,
			height: 150,
			layout: 'fit',
			closeAction: 'close',
			items: [this.form],
			buttons:[
				{text: '保存', iconCls: 'icon-save', scope: this, handler: this.doSave},
				{text: '关闭', iconCls: 'icon-close', scope: this, handler: this.close}
			]
		});
	},
	doSave: function(){
		var form = this.form.getForm();
		if(form.isValid()){
			var values = form.getValues(), obj = {};
			Ext.Ajax.request({
				url: root+'/config/Config!updateSpkgStatus.action',
				params: values,
				scope: this,
				success: function(res, opt){
					Alert('保存成功!');
					Ext.getCmp('agreementGridId').doRefresh();
					this.close();
				}
			});
		}
	}
});

AgreementGrid = Ext.extend(Ext.grid.GridPanel, {
	pageSize: 15,
	constructor: function(p){
		this.parent = p;
		this.store = new Ext.data.JsonStore({
			url: root+'/config/Config!querySpkg.action',
			fields: ['sp_id', 'spkg_sn', 'spkg_title', 'spkg_text', 'remark', 'create_time', 'optr_id', 'status', 'optr_name',       
				'status_text', 'confirm_optr_id', 'confirm_date', 'apply_optr_id', 'apply_date', 'optr_name', 'confirm_optr_name', 'apply_optr_name',
				{type:'date',name: 'eff_date', format: 'Y-m-d'}, {type:'date',name: 'exp_date', format: 'Y-m-d'},
				'cust_no', 'cust_name', 'prod_name'
			],
			totalProperty: 'totalProperty',
			root: 'records'
		});
		this.store.load({params: {start: 0, limit: this.pageSize}});
		var columns = [
			{header: '协议编号', 	dataIndex: 'spkg_sn', 			width: 60, renderer: App.qtipValue},
			{header: '协议标题', 	dataIndex: 'spkg_title', 		width: 100, renderer: App.qtipValue},
			{header: '协议内容', 	dataIndex: 'spkg_text', 		width: 100, renderer: App.qtipValue},
			{header: '生效时间', 	dataIndex: 'eff_date', 			width: 75, renderer: Ext.util.Format.dateFormat},
			{header: '失效时间', 	dataIndex: 'exp_date', 			width: 75, renderer: Ext.util.Format.dateFormat},
			{header: '状态', 		dataIndex: 'status_text', 		width: 50, renderer: Ext.util.Format.statusShow},			
			{header: '操作', 		dataIndex: 'status', 			width: 80, renderer: function(v, meta, r){
				var text = '';
				
				text += '<a href=# onclick=Ext.getCmp("agreementGridId").doUpdate()>修改</a>';
				
				if(v == 'UNCONFIRM'){
					text += '&nbsp;&nbsp;<a href=# onclick=Ext.getCmp("agreementGridId").doStatus()>确认</a>';
				}
				return text;
			}},
			{header: '客户编号',		dataIndex: 'cust_no',			width: 100, renderer: App.qtipValue},
			{header: '客户名称',		dataIndex: 'cust_name',			width: 100, renderer: App.qtipValue},
			{header: '申请操作员', 	dataIndex: 'apply_optr_name', 	width: 80, renderer: App.qtipValue},
			{header: '申请时间', 	dataIndex: 'apply_date', 		width: 120},	
			{header: '确认操作员', 	dataIndex: 'confirm_optr_name', width: 80, renderer: App.qtipValue},
			{header: '确认时间', 	dataIndex: 'confirm_date', 		width: 120},
			{header: '产品名称',		dataIndex: 'prod_name',			width: 100, renderer: App.qtipValue},
			{header: '创建时间', 	dataIndex: 'create_time', 		width: 120},
			{header: '创建操作员', 	dataIndex: 'optr_name', 		width: 80, renderer: App.qtipValue},			
			{header: '备注',			dataIndex: 'remark', 			width: 100, renderer: App.qtipValue}
		];
		AgreementGrid.superclass.constructor.call(this, {
			id: 'agreementGridId',
			title: '协议配置',
			region: 'center',
			store: this.store,
			columns: columns,
			sm: new Ext.grid.RowSelectionModel(),
			bbar: new Ext.PagingToolbar({store: this.store, pageSize: this.pageSize}),
			tbar:[
				' ',' ','输入关键字' , ' ',
				new Ext.ux.form.SearchField({
					id: 'agreementGridFieldId',
	                store: this.store,
	                width: 200,
	                hasSearch : true,
	                emptyText: '支持协议标题或文本模糊查询'
	            }),
 				'->', '-',
 				{text: '刷新', iconCls: 'icon-refresh', scope: this, handler: this.doRefresh}, '-',
 				{text: '添加', iconCls: 'icon-add', scope: this, handler: this.doAdd}, '-',
 				{text: '添加开户', iconCls: 'icon-add', scope: this, handler: this.doAddUser}, '-',
 				{text: '添加杂费', iconCls: 'icon-add', scope: this, handler: this.doAddBusiFee}, '-'
			],
			listeners:{
				scope: this,
				rowclick: this.doClick
			}
		});
	},
 	doRefresh: function(){
 		this.parent.agreementUserGrid.getStore().removeAll();
		this.parent.agreementFeeGrid.getStore().removeAll();
 		this.store.removeAll();
 		this.store.baseParams['query'] = Ext.getCmp('agreementGridFieldId').getValue();
 		this.store.load();
 	},
	doClick: function(){
		var record = this.getSelectionModel().getSelected();
		this.doLoadSpkgData(record.get('sp_id'))
	},
	doLoadSpkgData: function(spId){
		Ext.Ajax.request({
			url: root+'/config/Config!querySpkgInfoBySpkgId.action',
			params:{
				sp_id: spId
			},
			scope: this,
			success: function(res, opt){
				var data = Ext.decode(res.responseText);
				if(data){
					this.parent.agreementUserGrid.store.loadData(data['user']);
					this.parent.agreementFeeGrid.store.loadData(data['busifee']);
				}
			}
		});
	},
 	doAdd: function(){
 		var win = new AgreementWin();
 		win.show();
 		win.setTitle('添加协议');
 	},
 	doUpdate: function(){
 		var record = this.getSelectionModel().getSelected();
 		if(record){
	 		var win = new AgreementWin();
	 		win.show(record);
	 		win.setTitle('修改协议');
 		}else{
 			Alert('请选择一行修改!');
 		}
 	},
 	doAddUser: function(){
 		var record = this.getSelectionModel().getSelected();
 		if(record){
	 		var win = new AgreementUserWin();
	 		win.show(record);
	 		win.setTitle('添加协议用户');
 		}else{
 			Alert('请选择一行添加协议用户!');
 		}
 	},
 	doAddBusiFee: function(){
 		var record = this.getSelectionModel().getSelected();
 		if(record){
	 		var win = new AgreementBusiFeeWin();
	 		win.show(record);
	 		win.setTitle('添加协议杂费');
 		}else{
 			Alert('请选择一行添加协议杂费!');
 		}
 	},
 	doStatus: function(){
 		/*var record = this.getSelectionModel().getSelected();
 		Confirm('确认该协议吗？', this, function(){
			Ext.Ajax.request({
				url: root+'/config/Config!updateSpkgStatus.action',
				params: {
					sp_id: record.get('sp_id'),
					status: 'CONFIRM'
				},
				scope: this,
				success: function(res, opt){
					this.doRefresh();
				}
			});
		});*/
 		var record = this.getSelectionModel().getSelected();
 		var win = new AgreementConfirmWin(record.get('sp_id'));
 		win.show();
 	}
});

AgreementUserGrid = Ext.extend(Ext.grid.GridPanel, {
	constructor: function(p){
		this.parent = p;
		this.store = new Ext.data.JsonStore({
			url: root+'/config/Config!querySpkgUserBySpkgId.action',
			fields: ['id','sp_id', 'user_type', 'device_model', 'device_model_text', 'buy_type', 'buy_mode_name', 'open_num', 
				'fee_id', 'fee_name', 'fee', 'status', 'status_text', 'create_time', 'optr_id', 'use_done_code'
			]
		});
		var columns = [
			{header: '用户类型', dataIndex: 'user_type', width: 60, renderer: App.qtipValue},
			{header: '设备型号', dataIndex: 'device_model_text', width: 160, renderer: App.qtipValue},
			{header: '开户数量', dataIndex: 'open_num', width: 60, renderer: App.qtipValue},
			{header: '购买方式', dataIndex: 'buy_mode_name', width: 65, renderer: App.qtipValue},
			{header: '费用名称', dataIndex: 'fee_name', width: 150, renderer: App.qtipValue},
			{header: '金额', dataIndex: 'fee', width: 60, renderer : function(v){
                return Ext.util.Format.usMoney( Ext.util.Format.formatFee(v) );
            }},
			{header: '状态', dataIndex: 'status_text', width: 50, renderer: Ext.util.Format.statusShow},
			{header: '流水号', dataIndex: 'use_done_code', width: 70, renderer: App.qtipValue},
			{header: '操作', dataIndex: 'status', width: 80, renderer: function(v, meta, r){
				/*var text = '<a href=# onclick=Ext.getCmp("agreementUserGridId").doUpdateUser()>修改</a>';
				if(r.get('status') == 'IDLE'){
					text += '&nbsp;&nbsp;<a href=# onclick=Ext.getCmp("agreementUserGridId").doStatus()>禁用</a>';
				}else if(r.get('status') == 'INVALID'){
					text += '&nbsp;&nbsp;<a href=# onclick=Ext.getCmp("agreementUserGridId").doStatus()>启用</a>';
				}
				return text;*/
				if(v == 'IDLE'){
					return '<a href=# onclick=Ext.getCmp("agreementUserGridId").doUpdateUser()>修改</a>&nbsp;&nbsp;<a href=# onclick=Ext.getCmp("agreementUserGridId").doDel()>删除</a>';
				}
				return '';
			}}
		];
		AgreementUserGrid.superclass.constructor.call(this, {
			id: 'agreementUserGridId',
			title: '协议用户',
			region: 'center',
			store: this.store,
			columns: columns
		});
	},
	doUpdateUser: function(){
		var win = new AgreementUserWin();
 		win.show(this.getSelectionModel().getSelected());
 		win.setTitle('修改协议用户');
	},
	doDel: function(){
		var record = this.getSelectionModel().getSelected();
		Confirm('确定删除协议用户吗？', this, function(){
			Ext.Ajax.request({
				url: root+'/config/Config!deleteSpkgUser.action',
				params: {
					id: record.get('id')
				},
				scope: this,
				success: function(res, opt){
					Ext.getCmp('agreementGridId').doLoadSpkgData(record.get('sp_id'));
				}
			});
		});
	},
	doStatus: function(){
		var record = this.getSelectionModel().getSelected();
		var text = '禁用';
		if(record.get('status') == 'INVALID'){
			text = '启用';
		}
		Confirm('确定'+text+'协议用户吗？', this, function(){
			Ext.Ajax.request({
				url: root+'/config/Config!updateSpkgUserStatus.action',
				params: {
					id: id,
					status: status
				},
				scope: this,
				success: function(res, opt){
					Ext.getCmp('agreementGridId').doLoadSpkgData(record.get('sp_id'));
				}
			});
		});
	}
});

AgreementFeeGrid = Ext.extend(Ext.grid.GridPanel, {
	constructor: function(){
		this.store = new Ext.data.JsonStore({
			fields: ['id', 'sp_id', 'fee_id', 'fee_name', 'fee', 'status', 'status_text', 'status_date', 'create_time', 'optr_id', 'use_done_code']
		});
		var columns = [
			{header: '费用名称', dataIndex: 'fee_name', width: 150},
			{header: '金额', dataIndex: 'fee', width: 60, renderer : function(v){
                return Ext.util.Format.usMoney( Ext.util.Format.formatFee(v) );
            }},
			{header: '状态', dataIndex: 'status_text', width: 50, renderer: Ext.util.Format.statusShow},
			{header: '流水号', dataIndex: 'use_done_code', width: 70, renderer: App.qtipValue},
			{header: '操作', dataIndex: 'status', width: 80, renderer: function(v, meta, r){
				/*var text = '<a href=# onclick=Ext.getCmp("agreementFeeGridId").doUpdateFee('+v+')>修改</a>';
				if(r.get('status') == 'IDLE'){
					text += '&nbsp;&nbsp;<a href=# onclick=Ext.getCmp("agreementFeeGridId").doStatus('+v+')>禁用</a>';
				}else if(r.get('status') == 'INVALID'){
					text += '&nbsp;&nbsp;<a href=# onclick=Ext.getCmp("agreementFeeGridId").doStatus('+v+')>启用</a>';
				}
				return text;*/
				if(v == 'IDLE'){
					return '<a href=# onclick=Ext.getCmp("agreementFeeGridId").doDel()>删除</a>';
				}
				return '';
			}}
		];
		AgreementFeeGrid.superclass.constructor.call(this, {
			id: 'agreementFeeGridId',
			title: '协议杂费',
			width: 400,
			region: 'east',
			store: this.store,
			columns: columns,
			sm: new Ext.grid.RowSelectionModel()
		});
	},
	doUpdateFee: function(v){
		var win = new AgreementBusiFeeWin();
 		win.show(this.getSelectionModel().getSelected());
 		win.setTitle('修改协议杂费');
	},
	doDel: function(){
		var record = this.getSelectionModel().getSelected();
		Confirm('确定删除协议杂费吗？', this, function(){
			Ext.Ajax.request({
				url: root+'/config/Config!deleteSpkgBusiFee.action',
				params: {
					id: record.get('id')
				},
				scope: this,
				success: function(res, opt){
					Ext.getCmp('agreementGridId').doLoadSpkgData(record.get('sp_id'));
				}
			});
		});
	},
	doStatus: function(){
		var record = this.getSelectionModel().getSelected();
		var text = '禁用';
		if(record.get('status') == 'INVALID'){
			text = '启用';
		}
		Confirm('确定'+text+'协议杂费吗？', this, function(){
			Ext.Ajax.request({
				url: root+'/config/Config!updateSpkgBusiFeeStatus.action',
				params: {
					id: id,
					status: status
				},
				scope: this,
				success: function(res, opt){
					Ext.getCmp('agreementGridId').doLoadSpkgData(record.get('sp_id'));
				}
			});
		});
	}
});


AgreementUserConfig = Ext.extend(Ext.Panel, {
	constructor: function () {
		this.agreementGrid = new AgreementGrid(this);
		this.agreementUserGrid = new AgreementUserGrid(this);
		this.agreementFeeGrid = new AgreementFeeGrid(this);
        AgreementUserConfig.superclass.constructor.call(this, {
            id: 'AgreementUserConfig',
            title: '协议用户配置',
            closable: true,
            border: false,
            layout : 'border',
            items: [{
            	region: 'center',
            	border: false,
            	layout: 'fit',
            	items:[this.agreementGrid]
            }, {
            	region: 'south',
            	height: 200,
            	border: false,
            	split:true,
            	layout: 'border',
            	items:[this.agreementUserGrid, this.agreementFeeGrid]
            }]
        });
    }
});