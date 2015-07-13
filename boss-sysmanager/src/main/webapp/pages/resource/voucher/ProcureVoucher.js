/**
 * 代金券领用
 * @class ProcureVoucher
 * @extends Ext.Panel
 */
 
ProcureVoucherForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		ProcureVoucherForm.superclass.constructor.call(this,{
			id:'procureVoucherFormId',
			height:80,
			border:false,
			layout:'column',
			labelWidth:65,
			bodyStyle:'padding-top:10px',
			defaults:{
				columnWidth:.33,
				layout:'form',
				border:false
			},
			items:[
				{columnWidth:1,items:[
						{fieldLabel:'代金券号',
							defaults:{
								maxLength:11,minLength:11,enableKeyEvents:true,
								allowDecimals:false,allowNegative:false,allowBlank:false},
						    xtype:'compositefield',combineErrors:false,
						    items: [
						        {xtype:'numberfield',name:'voucherProcureDto.start_voucher_id'},
						        {xtype:'displayfield',value:'至'},
						        {xtype:'numberfield',name:'voucherProcureDto.end_voucher_id'}
					    	]
						}]
				},
				{items:[
					{xtype:'textfield',fieldLabel:'领用单号',name:'voucherProcureDto.procure_no',allowBlank:false}
				]},
				{items:[
					{xtype:'textfield',fieldLabel:'领用部门',name:'voucherProcureDto.procure_dept',allowBlank:false}
				]},
				{items:[
					{xtype:'textfield',fieldLabel:'领用人',name:'voucherProcureDto.procurer',allowBlank:false}
				]},
				{columnWidth:1,items:[
					{xtype:'textarea',fieldLabel:'备份',name:'voucherProcureDto.remark',anchor:'98%',height:80}
				]}
			]
		});
	}
});

ProcureVoucherWin = Ext.extend(Ext.Window,{
	constructor: function(){
		this.form = new ProcureVoucherForm();
		ProcureVoucherWin.superclass.constructor.call(this,{
			id:'procureVoucherWinId',
			title:'领用代金券',
			border:false,
			width:630,
			height:250,
			layout:'fit',
			items:[this.form],
			buttons:[
				{text:'保  存',iconCls:'icon-add',scope:this,handler:this.doSave},
				{text:'取  消',iconCls:'icon-reset',scope:this,handler:this.doCancel}
			]
		});
	},
	doSave: function(){
		var form = this.form.getForm();
		if(!form.isValid())return;
		var values = form.getValues();
		var start_voucher_id = values['voucherProcureDto.start_voucher_id'],
			end_voucher_id = values['voucherProcureDto.end_voucher_id'];
		if(start_voucher_id > end_voucher_id){
			Alert('结束值应该大于起始值！');
			return;
		}
		Ext.Ajax.request({
			url:root+'/resource/Voucher!saveVoucherProcure.action',
			params:values,
			scope:this,
			success:function(res){
				var data = Ext.decode(res.responseText);
				if(data.success === true){
					var result = data['simpleObj'];//空闲 可领用的代金券
					if(result == 0){
						Alert("没有可领用的代金券,请确认代金券是否存在或已被使用!");
					}else{
						Alert("可领用 "+ result +" 张代金券! 具体统计情况见备注!");
						Ext.getCmp('procureVoucherGridId').getStore().load({
							params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}
						});
						Ext.getCmp('procureVoucherListGridId').getStore().removeAll();
						this.doCancel();
					}
				}
			}
		});
	},
	doCancel: function(){
		this.form.getForm().reset();
		this.close();
	}
});

ProcureVoucherListGrid = Ext.extend(Ext.grid.GridPanel,{
	parent:null,
	constructor:function(p){
		this.parent = p;
		this.procureVoucherListStore = new Ext.data.JsonStore({
			url:root+'/resource/Voucher!queryProcureByDoneCode.action',
			fields:['voucher_id','voucher_value','status','status_time','for_county_id','optr_id',
					'used_time','used_money','unused_money','exp_time','status_text',
					'optr_name','for_county_id_text','is_procured','is_procured_text'],
			totalProperty:'totalProperty',
			root:'records'
		});
		this.sm = new Ext.grid.CheckboxSelectionModel();
		var columns = [
			this.sm,
			{header:'代金券号',dataIndex:'voucher_id',width:100},
			{header:'面额',dataIndex:'voucher_value',width:55,renderer:Ext.util.Format.formatFee},
			{header:'状态',dataIndex:'status_text',width:55,renderer:Ext.util.Format.statusShow},
			{header:'是否领用',dataIndex:'is_procured_text',width:65},
			{header:'状态改变时间',dataIndex:'status_time',width:115},
			{header:'适用地区',dataIndex:'for_county_id_text',width:100,renderer:App.qtipValue},
			{header:'已使用金额',dataIndex:'used_money',width:75,renderer:Ext.util.Format.formatFee},
			{header:'操作员',dataIndex:'optr_name',width:75,render:App.qtipValue},
			{header:'未使用金额',dataIndex:'unused_money',width:75,renderer:Ext.util.Format.formatFee},
			{header:'使用时间',dataIndex:'used_time',width:115},
			{header:'失效时间',dataIndex:'exp_time',width:115}
		];
		ProcureVoucherListGrid.superclass.constructor.call(this,{
			id:'procureVoucherListGridId',
			title:'代金券统计信息',
			border:false,
			region:'south',
			height:200,
			store:this.procureVoucherListStore,
			cm:new Ext.grid.ColumnModel({
				columns:columns
			}),
			sm:this.sm,
			tbar:[{text:'取消领用',handler:this.cancelProc,scope:this}],
			bbar:new Ext.PagingToolbar({store:this.procureVoucherListStore,pageSize:Constant.DEFAULT_PAGE_SIZE})
		});
	},
	cancelProc:function(){
		var records = this.selModel.getSelections();
		if(records.length ==0){
			Alert('请至少选择一条记录!');
			return false;
		}
		var voucherId = '';
		for(var index =0;index<records.length;index++){
			var rec = records[index];
			if(rec.get('is_procured') == 'T' && rec.get('status') == 'IDLE'){
				voucherId += rec.get('voucher_id') + ",";
			}
		}
		if(voucherId.length==0){
			Alert('未选中有效记录,请选择已领用,且未被使用的代金券!');
			return false;
		}
		Ext.Ajax.request({
			url:root+'/resource/Voucher!updateVoucherProcureStatus.action',
			params:{voucherId: voucherId},
			scope:this,
			success:function(res){
				var data = Ext.decode(res.responseText);
				if(data.success === true){
					this.procureVoucherListStore.reload();
					this.parent.procureVoucherGrid.procureVoucherStore.reload();
				}
			}
		});
	}
});
	

ProcureVoucherGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor:function(){
		this.procureVoucherStore = new Ext.data.JsonStore({
			url:root+'/resource/Voucher!queryVoucherProcure.action',
			fields:['procure_no','voucher_done_code','county_id','procure_dept','procurer','optr_id',
					'create_time','remark','count','optr_name','county_id_text'],
			totalProperty:'totalProperty',
			root:'records'
		});
		this.procureVoucherStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var columns = [
			{header:'领用单号',dataIndex:'procure_no',width:75},
			{header:'领用部门',dataIndex:'procure_dept',width:75},
			{header:'领用人',dataIndex:'procurer',width:60,renderer:App.qtipValue},
			{header:'操作员',dataIndex:'optr_name',width:60,renderer:App.qtipValue},
			{header:'创建时间',dataIndex:'create_time',width:120},
			{header:'数量',dataIndex:'count',width:75},
			{id:'procureVoucher_statue_id',header:'备注',dataIndex:'remark',width:75,renderer:App.qtipValue}
		];
		ProcureVoucherGrid.superclass.constructor.call(this,{
			id:'procureVoucherGridId',
			title:'代金券领用信息',
			autoExpandColumn:'procureVoucher_statue_id',
			border:false,
			region:'center',
			store:this.procureVoucherStore,
			sm:new Ext.grid.RowSelectionModel(),
			columns:columns,
			tbar:[
				' ', ' ', '输入关键字', ' ', new Ext.ux.form.SearchField({
	                store: this.procureVoucherStore,
	                width: 200,
	                hasSearch: true,
	                emptyText: '支持所有模糊查询'
	            }),'->','-',
				{text:'领用',scope:this,handler:this.doProcure},'-'
			],
			bbar:new Ext.PagingToolbar({store:this.procureVoucherStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
			listeners:{
				scope:this,
				rowclick:this.doClick	
			}
		});
	},
	doProcure: function(){
		var win = Ext.getCmp('procureVoucherWinId');
		if(!win){
			win = new ProcureVoucherWin();
		}
		win.show();
	},
	doClick: function(){
		var record = this.getSelectionModel().getSelected();
		var store = Ext.getCmp('procureVoucherListGridId').getStore();
		store.baseParams = {doneCode:record.get('voucher_done_code')};
		store.load({
				params:{
					start:0,
					limit:Constant.DEFAULT_PAGE_SIZE
				}
		});
	}
});

ProcureVoucher = Ext.extend(Ext.Panel,{
	constructor:function(){
		this.procureVoucherGrid = new ProcureVoucherGrid();
		this.procureVoucherListGrid = new ProcureVoucherListGrid(this);
		ProcureVoucher.superclass.constructor.call(this,{
			id:'ProcureVoucher',
			layout:'border',
			title:'代金券领用',
			closable: true,
			border : false ,
			items:[/*new ProcureVoucherForm(), */this.procureVoucherGrid,
				this.procureVoucherListGrid
			]
		});
	}
});