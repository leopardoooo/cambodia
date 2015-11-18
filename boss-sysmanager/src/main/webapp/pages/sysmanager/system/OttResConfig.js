/**
 * 翻译配置
 */
 
 OttResGrid = Ext.extend(Ext.grid.EditorGridPanel, {
 	pageSize: 20,
 	constructor: function(){
 		this.ottResStore = new Ext.data.JsonStore({
			url: root+'/config/Config!queryOttAuth.action',
			fields: ['id', 'name', 'status', 'domain', 'need_sync', 'sync_date', 'fee_id', 'fee_name', 'type', 'price',
				'currency_type', 'unit', 'amount', 'pay_type', 'continue_buy', 'explanation',
				{type:'date',name: 'begin_time', format: 'Y-m-d'}, {type:'date',name: 'end_time', format: 'Y-m-d'}],
			totalProperty:'totalProperty',
			root:'records'
		});
		this.ottResStore.load({params:{start:0,limit:this.pageSize}});
		var columns = [
			{header: '产品ID',		dataIndex: 'id',				width: 80, renderer: App.qtipValue},
			{header: '产品名称', 	dataIndex: 'name', 				width: 150, renderer: App.qtipValue},
			{header: '状态', 		dataIndex: 'status', 			width: 55, renderer: function(v){
					return v == '0' ? '待审核' : '已审核';
				}
			},
			{header: '内外网', 		dataIndex: 'domain', 			width: 55, renderer: function(v){
					return v == '1' ? '内网' : '外网';
				}
			},
			{header: '同步', 		dataIndex: 'need_sync', 		width: 55, renderer: function(v){
					return v == 'T' ? '需要' : '不需要';
				}
			},
			{header: '上次同步时间', dataIndex: 'sync_date', 		width: 120, renderer: App.qtipValue},
			{header: '资费ID',		dataIndex: 'fee_id', 			width: 120, renderer: App.qtipValue},
			{header: '资费名称', 	dataIndex: 'fee_name', 			width: 180, renderer: App.qtipValue},
			{header: '操作', 		dataIndex: 'id', 			width: 80, renderer: function(v, meta, r){
				return '<a href=# onclick=Ext.getCmp("ottResGridId").doUpdate()>修改</a>';
			}},
			{header: '资费类型', 	dataIndex: 'type', 				width: 80, renderer: function(v){
					return v == '0' ? '周期性资费' : '单片';
				}
			},
			{header: '资费价格', 	dataIndex: 'price', 			width: 70, renderer: App.qtipValue},
			{header: '货币类型', 	dataIndex: 'currency_type', 	width: 65, renderer: function(v){
					return v == 'RMB' ? '人民币' : 'USD';
				}
			},
			{header: '计价单位', 	dataIndex: 'unit', 				width: 65, renderer: function(v){
					if(v == 'year') return '年';
					else if(v == 'month') return '月';
					else if(v == 'day') return '天';
				}
			},
			{header: '计价数量', 	dataIndex: 'amount', 			width: 70, renderer: App.qtipValue},
			{header: '支付类型', 	dataIndex: 'pay_type', 			width: 65, renderer: function(v){
					return v == '0' ? '预付费' : '后付费';
				}
			},
			{header: '生效时间', 	dataIndex: 'begin_time', 		width: 100, renderer: Ext.util.Format.dateFormat},
			{header: '失效时间', 	dataIndex: 'end_time', 			width: 100, renderer: Ext.util.Format.dateFormat},
			{header: '是否支持续费', dataIndex: 'continue_buy', 		width: 85, renderer: function(v){
					return v == '0' ? '不支持' : '支持';
				}
			},
			{header: '资费说明', 	dataIndex: 'explanation', 		width: 120, renderer: App.qtipValue}
		];
 		OttResGrid.superclass.constructor.call(this,{
 			id: 'ottResGridId',
 			columns: columns,
 			sm: new Ext.grid.RowSelectionModel(),
 			store: this.ottResStore,
 			border: false,
 			clicksToEdit:1,
 			tbar: [
 				' ',' ','输入关键字' , ' ',
				new Ext.ux.form.SearchField({
					id: 'ottResSearchFieldId',
	                store: this.ottResStore,
	                width: 200,
	                hasSearch : true,
	                pageSize: this.pageSize,
	                emptyText: '支持产品、资费名称模糊查询'
	            }),'-',
 				'->', '-',
 				{text: '刷新', iconCls: 'icon-refresh', scope: this, handler: this.doRefresh}, '-',
	            {text: '添加', iconCls: 'icon-add', scope: this, handler: this.doAdd}, '-'
 			],
 			bbar: new Ext.PagingToolbar({store: this.ottResStore, pageSize: this.pageSize})
 		});
 	},
 	doAdd: function(){
 		var win = new OttResWin();
 		win.setTitle('添加OTT资源');
 		win.show();
 	},
 	doUpdate: function(){
 		var win = new OttResWin();
 		win.setTitle('修改OTT资源');
 		win.show(this.getSelectionModel().getSelected());
 	},
 	doRefresh: function(){
 		this.ottResStore.removeAll();
 		this.ottResStore.baseParams['query'] = Ext.getCmp('ottResSearchFieldId').getValue();
 		this.ottResStore.load();
 	}
 });
 
 OttResWin = Ext.extend(Ext.Window, {
 	type: 'save',
 	constructor: function(){
 		this.formPanel = new Ext.form.FormPanel({
 			layout: 'column',
			border: false,
			plain: true,
			baseCls: 'x-plain',
			bodyStyle: 'background:#F9F9F9;padding-top:10px',
			labelWidth: 100,
			defaults:{
				columnWidth: .5,
				border: false,
				layout: 'form',
				baseCls: 'x-plain',
				defaultType: 'textfield'		
			},
			items:[{
				items:[{
					fieldLabel: '产品ID',
					name: 'id',
					allowBlank: false
				},{
					fieldLabel: '同步',
					hiddenName: 'need_sync',
					xtype: 'combo',
					store: new Ext.data.JsonStore({
						fields: ['text', 'value'],
						data:[{'text': '需要', 'value': 'T'},{'text': '不需要', 'value': 'F'}]
					}),
					displayField: 'text', valueField: 'value', value: 'F',
					editable: false,
					allowBlank: false
				},{
					fieldLabel: '资费名称',
					name: 'fee_name',
					allowBlank: false
				},{
					xtype: 'numberfield',
					fieldLabel: '资费价格',
					name: 'price',
//					allowDecimals: false,
					allowNegative: false,
					allowBlank: false
				},{
					fieldLabel: '计价单位',
					hiddenName: 'unit',
					xtype: 'combo',
					store: new Ext.data.JsonStore({
						fields: ['text', 'value'],
						data:[{'text': '年', 'value': 'year'}, {'text': '月', 'value': 'month'}, {'text': '天', 'value': 'day'}]
					}),
					displayField: 'text', valueField: 'value',
					editable: false,
					allowBlank: false
				},{
					fieldLabel: '支付类型',
					hiddenName: 'pay_type',
					xtype: 'combo',
					store: new Ext.data.JsonStore({
						fields: ['text', 'value'],
						data:[{'text': '预付费', 'value': '0'}, {'text': '后付费', 'value': '1'}]
					}),
					displayField: 'text', valueField: 'value', value: '0',
					editable: false,
					allowBlank: false
				},{
					fieldLabel: '支持续费',
					hiddenName: 'continue_buy',
					xtype: 'combo',
					store: new Ext.data.JsonStore({
						fields: ['text', 'value'],
						data:[{'text': '不支持', 'value': '0'}, {'text': '支持', 'value': '1'}]
					}),
					displayField: 'text', valueField: 'value', 
					editable: false,
					allowBlank: false
				},{
					id: 'ott_end_time_id',
					fieldLabel: '失效时间',
					xtype: 'datefield',
					name: 'end_time',
					format: 'Y-m-d',
					allowBlank: false
				}]
			},{
				items:[{
					fieldLabel: '产品名称',
					name: 'name',
					allowBlank: false
				},{
					fieldLabel: '内外网',
					hiddenName: 'domain',
					xtype: 'combo',
					store: new Ext.data.JsonStore({
						fields: ['text', 'value'],
						data:[{'text': '内网', 'value': '1'},{'text': '全网', 'value': '2'}]
					}),
					displayField: 'text', valueField: 'value',
					editable: false,
					allowBlank: false
				},{
					fieldLabel: '资费ID',
					name: 'fee_id',
					allowBlank: false
				},{
					fieldLabel: '资费类型',
					hiddenName: 'type',
					xtype: 'combo',
					store: new Ext.data.JsonStore({
						fields: ['text', 'value'],
						data:[{'text': '周期性收费', 'value': '0'},{'text': '单片', 'value': '1'}]
					}),
					displayField: 'text', valueField: 'value',
					editable: false,
					allowBlank: false
				},{
					xtype: 'numberfield',
					fieldLabel: '计价数量',
					name: 'amount',
					allowDecimals: false,
					allowNegative: false,
					allowBlank: false
				},{
					fieldLabel: '货币类型',
					hiddenName: 'currency_type',
					xtype: 'combo',
					store: new Ext.data.JsonStore({
						fields: ['text', 'value'],
						data:[{'text': '美元', 'value': 'USD'}, {'text': '人民币', 'value': 'RMB'}]
					}),
					displayField: 'text', valueField: 'value', value: 'USD',
					editable: false,
					allowBlank: false
				},{
					fieldLabel: '生效时间',
					xtype: 'datefield',
					name: 'begin_time',
					format: 'Y-m-d',
					minValue: nowDate().format('Y-m-d'),
					vtype: 'daterange',
	                endDateField: 'ott_end_time_id',
	                customDay: 1,
					allowBlank: false
				}]
			},{
				columnWidth: 1,
				items:[{
					xtype: 'textarea',
					anchor: '98%',
					height: 80,
					fieldLabel: '资费说明',
					name: 'explanation'
				}]
			}]
 		});
 		
 		OttResWin.superclass.constructor.call(this, {
 			layout: 'fit',
 			width: 550,
 			height: 400,
 			items: [this.formPanel],
 			buttonAlign: 'center',
 			closeAction: 'close',
 			buttons: [
 				{text: '保存', iconCls: 'icon-save', scope: this, handler: this.doSave},
 				{text: '关闭', iconCls: 'icon-close', scope: this, handler: this.close}
 			]
 		});
 	},
 	show: function(record){
 		if(record){
 			this.type = 'update';
 			var form = this.formPanel.getForm();
 			form.loadRecord(record);
 			
 			form.findField('id').setReadOnly(true);			//不能修改产品ID
 			form.findField('fee_id').setReadOnly(true);		//不能修改资费ID
 		}
 		OttResWin.superclass.show.call(this);
 	},
 	doSave: function(){
 		var form = this.formPanel.getForm();
 		if(form.isValid()){
 			
 			var values = form.getValues(), agent = {};
 			for(var key in values){
 				agent['ottAuth.'+key] = values[key];
 			}
 			agent['type'] = this.type;
 			Ext.Ajax.request({
 				url: root+'/config/Config!saveOttAuth.action',
 				params: agent,
 				scope: this,
 				success: function(res, opt){
 					Alert('保存成功',function(){
	 					this.close();
	 					Ext.getCmp('ottResGridId').doRefresh();
 					}, this);
 				}
 			});
 		}
 	}
 });
 
 OttResConfig = Ext.extend(Ext.Panel, {
    constructor: function () {
        this.grid = new OttResGrid(this);
        OttResConfig.superclass.constructor.call(this, {
            id: 'OttResConfig',
            title: 'OTT资源配置',
            closable: true,
            border: false,
            layout : 'fit',
            items: [this.grid]
        });
    }
});