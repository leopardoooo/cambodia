QueryVoucherForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	constructor:function(p){
		this.parent = p;
		QueryVoucherForm.superclass.constructor.call(this,{
			border:false,
			bodyStyle:'padding:10px',
			labelWidth:75,
			layout:'column',
			items:[
				{columnWidth:.5,layout:'form',border:false,items:[
						{fieldLabel:'代金券号',allowBlank:false,
						    xtype:'compositefield',combineErrors:false,
						    defaults:{minLength:11,maxLength:11,
						    	allowNegative:false,allowDecimals:false},
						    items: [
						        {xtype:'numberfield',name:'voucherDto.start_voucher_id',width:120},
						        {xtype:'displayfield',value:'至'},
						        {xtype:'numberfield',name:'voucherDto.end_voucher_id',width:120}
					    	]
						},
						{fieldLabel:'创建日期',
						    xtype:'compositefield',combineErrors:false,
						    items: [
						        {xtype:'datefield',name:'voucherDto.start_create_time',style:'width:103px;height:22px',format:'Y-m-d'},
						        {xtype:'displayfield',value:'至'},
						        {xtype:'datefield',name:'voucherDto.end_create_time',style:'width:103px;height:22px',format:'Y-m-d'}
					    	]
						},{fieldLabel:'使用状态',hiddenName:'voucherDto.status',
								xtype:'lovcombo',
								store:new Ext.data.ArrayStore({
									fields:['item_name','item_value'],
									data:[['空闲','IDLE'],['使用','USED'],['失效','INVALID']]
								}),displayField:'item_name',valueField:'item_value',
								triggerAction:'all',mode:'local',editable:false,
								beforeBlur:function(){}
						}
				]},
				{columnWidth:.5,layout:'form',border:false,width:150,items:[
						{fieldLabel:'失效日期',
						    xtype:'compositefield',combineErrors:false,
						    items: [
						        {xtype:'datefield',name:'voucherDto.start_invalid_time',style:'width:103px;height:22px',format:'Y-m-d'},
						        {xtype:'displayfield',value:'至'},
						        {xtype:'datefield',name:'voucherDto.end_invalid_time',style:'width:103px;height:22px',format:'Y-m-d'}
					    	]
						},
						{fieldLabel:'使用日期',
						    xtype:'compositefield',combineErrors:false,
						    items: [
						        {xtype:'datefield',name:'voucherDto.start_used_time',style:'width:103px;height:22px',format:'Y-m-d'},
						        {xtype:'displayfield',value:'至'},
						        {xtype:'datefield',name:'voucherDto.end_used_time',style:'width:103px;height:22px',format:'Y-m-d'}
					    	]
						},{fieldLabel:'是否领用',hiddenName:'voucherDto.is_procured',
								xtype:'paramcombo',paramName:'BOOLEAN',forceSelection:true,editable:true
							}
				]},
				{columnWidth:.5,layout:'form',border:false,width:150,items:[
					{xtype:'combo',fieldLabel:'地区',hiddenName:'voucherDto.for_county_id',
								store:new Ext.data.JsonStore({
									url:root+'/resource/Voucher!queryFgsByDeptId.action',
									fields:['dept_id','dept_name']
								}),displayField:'dept_name',valueField:'dept_id',
								allowBlank: false,
								emptyText :'请选择地区'
							}
				]},
				{columnWidth:.5,layout:'column',border:false,items:[
					{columnWidth:.6,layout:'form',border:false,items:[
							{fieldLabel:'面额',
							    xtype:'numberfield',name:'voucherDto.voucher_value'
							}
						]}
				]},
				{columnWidth:.6,layout:'form',border:false,items:[
						{fieldLabel:'类型',hiddenName:'voucherDto.voucher_type',allowBlank:true,
							xtype:'paramcombo',paramName:'VOUCHER_TYPE',forceSelection:true,editable:false
						}
					]},
				{columnWidth:.35,bodyStyle:'padding-bottom:5px',border:false,items:[
					{id:'queryVoucherBtnId',xtype:'button',text:'查  询',iconCls:'icon-query',
						scope:this,handler:this.doQuery}
				]}
			]
		});	
	},
	initComponent:function(){
		QueryVoucherForm.superclass.initComponent.call(this);
		App.form.initComboData(this.findByType("paramcombo"));
		this.getForm().findField('voucherDto.for_county_id').getStore().load();
	},
	doQuery:function(){
		if(this.getForm().isValid()){
			
			Ext.getCmp('queryVoucherBtnId').disable();//禁用查询按钮，数据加载完再激活
			
			var store = this.parent.grid.getStore();
			store.removeAll();
			
			var values = this.getForm().getValues();
			var obj = {};
			for(var v in values){
				if(v.indexOf("voucherDto.")==0){
					obj[v] = values[v];	
				}
			}
			//面额转为分进行查询
			if(obj['voucherDto.voucher_value'])
				obj['voucherDto.voucher_value'] = obj['voucherDto.voucher_value']*100;
			store.baseParams = obj;
			try{
				store.load({params: { start: 0, limit: Constant.DEFAULT_PAGE_SIZE }});
			}catch(e){
				Ext.getCmp('queryVoucherBtnId').enable();
			}
		}
	}
});

QueryVoucherGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor:function(){
		this.store = new Ext.data.JsonStore({
			url:root+'/resource/Voucher!queryMulitVoucher.action',
			root : 'records' ,
			totalProperty: 'totalProperty',
			fields:['voucher_id','status','status_text','voucher_value','optr_id','optr_name',
				'for_county_id','for_county_id_text','create_time','voucher_type','voucher_type_name',
				'used_time','used_money','unused_money','invalid_time','is_procured','is_procured_text',
				'procure_no', 'procure_dept', 'procurer','cust_name'
			]
		});
		this.store.on("load",function(store){
			Ext.getCmp('queryVoucherBtnId').enable();
		},this);
		var cm = new Ext.grid.ColumnModel({
			columns:[
				{header:'代金券号',dataIndex:'voucher_id',width:100,renderer:App.qtipValue},
				{header:'类型',dataIndex:'voucher_type_name',width:50,renderer:App.qtipValue},
				{header:'状态',dataIndex:'status_text',width:55,renderer:Ext.util.Format.statusShow},
				{header:'面额',dataIndex:'voucher_value',width:50,renderer:Ext.util.Format.formatFee},
				{header:'地区',dataIndex:'for_county_id_text',width:80,renderer:App.qtipValue},
				{header:'是否领用',dataIndex:'is_procured_text',width:55},
				{header:'生成时间',dataIndex:'create_time',width:70,renderer:Ext.util.Format.dateFormat},
				{header:'使用时间',dataIndex:'used_time',width:70,renderer:Ext.util.Format.dateFormat},
				{header:'失效时间',dataIndex:'invalid_time',width:70,renderer:Ext.util.Format.dateFormat},
				{header:'使用金额',dataIndex:'used_money',width:60,renderer:Ext.util.Format.formatFee},
				{header:'未使用金额',dataIndex:'unused_money',width:70,renderer:Ext.util.Format.formatFee},
				{header:'使用操作员',dataIndex:'optr_name',width:65,renderer:App.qtipValue},
				{header:'使用者客户',dataIndex:'cust_name',width:65,renderer:App.qtipValue},
				{header:'领用单号',dataIndex:'procure_no',width:65,renderer:App.qtipValue},
				{header:'领用部门',dataIndex:'procure_dept',width:65,renderer:App.qtipValue},
				{header:'领用部门',dataIndex:'procurer',width:65,renderer:App.qtipValue}
			]
		});
		
		QueryVoucherGrid.superclass.constructor.call(this,{
			title:'代金券信息',
			autoScroll:true,
			border:false,
			ds:this.store,
			cm:cm,
			bbar: new Ext.PagingToolbar({store: this.store ,pageSize : Constant.DEFAULT_PAGE_SIZE})
		});
	}
});

QueryVoucher = Ext.extend(Ext.Panel,{
	form:null,
	grid:null,
	constructor:function(){
		this.form = new QueryVoucherForm(this);
		this.grid = new QueryVoucherGrid();
		QueryVoucher.superclass.constructor.call(this,{
			id:'QueryVoucher',
			title:'代金券查询',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'anchor',
			items:[
				{anchor:'100% 32%',border:false,items:[this.form]},
				{anchor:'100% 68%',layout:'fit',border:false,items:[this.grid]}
			]
		});
	}
});