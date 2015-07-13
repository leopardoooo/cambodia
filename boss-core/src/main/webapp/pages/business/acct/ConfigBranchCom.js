/**
 * 分公司账户配置
 */
 
GeneralAcctWin = Ext.extend(Ext.Window,{
	form : null,
	constructor : function(){
		this.companyStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : Constant.ROOT_PATH+"/core/x/Acct!queryCompanyWithOutAcct.action",
			fields : ["dept_id","dept_name"]
		})
		
		this.form = new Ext.FormPanel({
			border : false,
			bodyStyle : Constant.TAB_STYLE,
			layout : 'form',
			items : [{
				xtype : 'combo',
				fieldLabel : '分公司选择',
				store : this.companyStore,
				displayField : 'dept_name',
				valueField : 'dept_id',
				forceSelection : true,
				allowBlank : false,
				hiddenName : 'county_id'
			},{
				xtype : 'numberfield',
				allowBlank : false,
				allowNegative : false,
				name : 'balance',
				fieldLabel : '金额'
			}]
		});
		
		
		GeneralAcctWin.superclass.constructor.call(this,{
			title : '添加分公司账户',
			layout : 'fit',
			height : 200,
			width : 350,
			closeAction : 'close',
			items : [this.form],
			buttons : [{
				text : '保存',
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : '关闭',
				scope : this,
				handler : function() {
					this.close();
				}
			}]
		})
	},
	doSave : function(){
		if(this.form.getForm().isValid()){
			Confirm("确定保存吗?", this ,function(){
				var all = this.form.getForm().getValues(),newValues = {},busiParams = {};
				for(var key in all){
					newValues['generalAcct.'+key] = all[key];
				}
				newValues['generalAcct.balance'] = Ext.util.Format.formatToFen(newValues['generalAcct.balance']);
				
				busiParams['busiCode'] = App.getApp().getData().currentResource.busicode;
				newValues['jsonParams'] = Ext.encode(busiParams);
				
				var msg = Show();
				Ext.Ajax.request({
					url : Constant.ROOT_PATH+"/core/x/Acct!saveGeneralAcct.action",
					params:newValues,
					scope:this,
					timeout:99999999999999,//12位 报异常
					success:function(res,opt){
						msg.hide();
						msg = null;
						Alert('业务保存成功',function(){
							Ext.getCmp('GeneralAcctGrid').getStore().reload();
							this.close();
						},this);
					}
				})
			
			})
		}
	}
})
 
/**
 * 分公司账户表格构建
 * @class ServiceGrid
 * @extends Ext.grid.EditorGridPanel
 */
GeneralAcctGrid = Ext.extend( Ext.grid.EditorGridPanel, {
	generalAcctStore: null,
	constructor: function(){
		
		this.generalAcctStore = new Ext.data.JsonStore({
			autoLoad : true,
			url:Constant.ROOT_PATH+"/core/x/Acct!queryCompanyAcct.action",
			fields: [
				{name: 'g_acct_id'},
				{name: 'county_id'},
				{name: 'county_name'},
				{name: 'balance', type: 'float'},
				{name: 'oldBalance', type: 'float'},//保存旧记录
				{name: 'changeBalance', type: 'float'}
			]
		});
		
		this.generalAcctStore.on('load',this.doLoadData,this);
		
		
		GeneralAcctGrid.superclass.constructor.call(this, {
			id : 'GeneralAcctGrid',
			region : 'center',
			store : this.generalAcctStore,
			forceValidation: true,
	        clicksToEdit: 1,
	        viewConfig : {
	        	forceFit : true
	        },
			cm: new Ext.grid.ColumnModel({
				columns: [
					{ header: '分公司账户', dataIndex: 'county_name',
						editor : new Ext.data.JsonStore({
							autoLoad : true,
							url : Constant.ROOT_PATH+"/core/x/Acct!queryCompanyWithOutAcct.action",
							fields : ["dept_id","dept_name"]
						})},
					{ header: '余额', dataIndex: 'balance',
						editor : new Ext.form.NumberField({
							allowBlank : false,
							allowNegative : false
						})}
				]
			}),
			tbar : [{
				text : '添加分公司账户',
				scope : this,
				handler : this.addGeneralAcct
			}]
		});
	},
	initEvents : function(){
		this.on('afteredit',this.afterEdit,this);
		this.on('afterrender',function(){
			Ext.getCmp('BusiPanel').showTip();
		},this)
		
		GeneralAcctGrid.superclass.initEvents.call(this);
	},
	afterEdit :function(obj){
		obj.record.set('changeBalance',obj.value - obj.record.get('oldBalance'));
	},
	doLoadData : function(store){
		Ext.getCmp('BusiPanel').hideTip();
		store.each(function(rec){
			var balance = Ext.util.Format.formatFee(rec.get('balance'));
			rec.set('balance',balance);
			rec.set('oldBalance',balance);
		})
	},
	getValues: function(){
		var result = {};
		var store = this.getStore();
		var data = [];
		store.each(function(rec){
			var obj = {};
			obj['g_acct_id'] = rec.get('g_acct_id');
			obj['county_id'] = rec.get('county_id');
			obj['balance'] = Ext.util.Format.formatToFen(rec.get('balance'));
			obj['changeBalance'] = Ext.util.Format.formatToFen(rec.get('changeBalance'));
			data.push(obj);
		})
		result['generalAcctListStr'] = Ext.encode(data);
		return result;
	},
	addGeneralAcct : function(){
		new GeneralAcctWin().show();
	}
});

GeneralAcctForm = Ext.extend(BaseForm,{
	url:Constant.ROOT_PATH+"/core/x/Acct!editCompanyAcct.action",
	generalAcctGrid : null,
	constructor:function(){
		this.generalAcctGrid = new GeneralAcctGrid();
		
		GeneralAcctForm.superclass.constructor.call(this,{
			layout : 'fit',
			border : false,
			items:[{
				xtype : 'panel',
				layout : 'fit',
				items : [this.generalAcctGrid]
			}]
		});
	},
	success:function(){
//		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	getValues:function(){
		var result = this.generalAcctGrid.getValues();
		return result;
	}
});

Ext.onReady(function(){
	var panel = new GeneralAcctForm();
	TemplateFactory.gTemplate(panel);
});