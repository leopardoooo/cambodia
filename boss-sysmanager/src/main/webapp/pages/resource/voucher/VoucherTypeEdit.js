/**
 * 代金券类型维护.
 * @class VoucherTypeEdit
 * @extends Ext.Panel
 */
VoucherTypeEditWin = Ext.extend(Ext.Window,{
	formPanel:null,parent:null,
	loadRecord:function(rec,p){
		this.parent = p;
		this.formPanel.getForm().reset();
		var title = '';
		var data = new Ext.data.Record({});
		var editTypeFlag = false;
		if(!rec){
			title = '添加类型';
		}else{
			editTypeFlag = true;
			title = '编辑类型';
			for(var key in rec.data){
				data.set('vtype.' + key,rec.data[key]);
			}
			this.formPanel.getForm().loadRecord(data);
			this.formPanel.getForm().findField('vtype.rule_id').setRawValue(data.get('vtype.rule_name'));
		}
		this.setTitle(title);
		this.formPanel.getForm().findField('vtype.voucher_type').setDisabled(editTypeFlag);
	},
	
	
	constructor:function(p){
		this.parent = p;
		this.formPanel = new Ext.FormPanel({
			border:false,plain:true,
			layout:'form',
			items:[
				{fieldLabel:'代金券类型',xtype:'textfield',name:'vtype.voucher_type',allowBlank:false},
				{fieldLabel:'代金券名称',xtype:'textfield',name:'vtype.voucher_type_name',allowBlank:false},
				{fieldLabel:'规则',xtype:'combo',valueField:'rule_id',displayField:'rule_name',
					editable:false,allowBlank:true,
					store:new Ext.data.JsonStore({
						fields:['rule_id','rule_name'],autoLoad:true,
						url : root + '/config/Rule!queryAllRules.action',
						baseParams :{start:0,limit:Constant.DEFAULT_PAGE_SIZE*10,dataType:'BUSI'},
						root: 'records',totalProperty: 'totalProperty',
						listeners:{
							add : true,
     						load:function(st){
								alert(st.getCount());
								var recordType = st.recordType;
								st.add(new recordType({rule_id:'',rule_name:''}));
							}
						}
					}),
				name:'vtype.rule_id'}
			]
		});
		VoucherTypeEditWin.superclass.constructor.call(this,{
			border:false,width:300,height:180,title:'编辑',layout:'fit',plain:true,
			items:this.formPanel,bodyStyle:'padding-top:10px;',buttonAlign:'center',
			buttons:[
				{text:'保存',scope:this,handler:this.doSave},
				{text:'取消',scope:this,handler:this.doCancel}
			]
		});
	},
	doSave:function(){
		var form =  this.formPanel.getForm();
		var values = form.getValues();
		values['vtype.voucher_type'] = form.findField('vtype.voucher_type').getValue();
		values['vtype.rule_id'] = form.findField('vtype.rule_id').getValue();
		Ext.Ajax.request({
			url:root+'/resource/Voucher!editVoucherType.action',
	        params: values,
	        scope:this,
	        success: function (res, ops) {
	            var rs = Ext.decode(res.responseText);
	            if (true === rs) {
	                Alert('操作成功!', function () {
	                	this.parent.grid.getStore().reload();
	                	this.hide();
	                }, this);
	            }
	            else {
	                Alert('操作失败!');
	            }
	        }
	    });
	},
	doCancel:function(){
		this.hide();
	}
});
VoucherTypeEdit = Ext.extend(Ext.Panel,{
	title:'代金券类型维护',editWin:null,
	grid:null,closable:true,
	id:'VoucherTypeEdit',
	store:null,
	constructor:function(){
		this.store = new Ext.data.JsonStore({
			autoLoad:true,
			url:root+'/resource/Voucher!queryVoucherTypes.action',
			fields:['voucher_type','voucher_type_name','rule_id','rule_name']
		});
		this.grid = new Ext.grid.GridPanel({
			border:false,store:this.store,autoHeight:false,
			sm: new Ext.grid.RowSelectionModel(),
			columns: [
				{header:'类型',dataIndex:'voucher_type',width:100,renderer:App.qtipValue},
				{header:'类型名称',dataIndex:'voucher_type_name',width:100,renderer:App.qtipValue},
				{header:'规则名称',dataIndex:'rule_name',width:200,renderer:App.qtipValue},
				{header:'操作',dataIndex:'voucher_type',width:100,renderer:function(){
					return "<a href='#' onclick=Ext.getCmp(\'"+"VoucherTypeEdit"+"\').editType(); style='color:blue'>修改</a>";
				}}
			]
		});
		VoucherTypeEdit.superclass.constructor.call(this,{
			border:false,items:this.grid,layout:'fit',
			tbar:[
				{text:'添加',scope:this,handler:this.addType}
//				,'-',{text:'编辑',scope:this,handler:this.editType},'-',
//				{text:'刷新',scope:this,handler:function(){this.grid.store.reload();}}
			]
		});
	},
	addType:function(){
		if(!this.editWin){
			this.editWin = new VoucherTypeEditWin(this);
		}
		this.editWin.show();
		this.editWin.loadRecord(null,this);
	},
	editType:function(){
		var records = this.grid.selModel.getSelections();
		if(!records || records.length !=1){
			Alert('请选择且仅选择一条记录!');
			return false;
		}
		if(!this.editWin){
			this.editWin = new VoucherTypeEditWin(this);
		}
		this.editWin.show();
		this.editWin.loadRecord(records[0],this);
	}
});