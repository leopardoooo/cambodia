var BatchLogoffUserForm = Ext.extend(BaseForm, {
	returnData: null,
	grid: null,
	url: root+'/core/x/User!batchLogoffUser.action',
	constructor:function(){
		this.grid = new Ext.grid.GridPanel({
			title: '产品退款汇总',
			border: false,
			columns: [
				{header:'产品名称',dataIndex:'prod_name',width:250},
				{header:'退款金额',dataIndex:'active_fee',width:150, renderer: Ext.util.Format.moneyRenderer}
			],
			store: new Ext.data.JsonStore({
				fields: ['prod_name', 'active_fee']
			})
		});
		
		BatchLogoffUserForm.superclass.constructor.call(this,{
			border:false,
			layout: 'border',
			fileUpload: true,
			items:[{
				layout: 'form',
				region: 'center',
				border: false,
				bodyStyle: 'padding-top:20px',
				items:[{
					id : 'bathcLogoffUserId',
					xtype:'textfield',
					inputType:'file',
					fieldLabel:'用户ID文件',
					allowBlank : false,
					name:'file',
					anchor:'90%',
					buttonText:'浏览...',
					listeners: {
						scope: this,
						change: this.doSelect
					}
				}]
			}, {
				layout: 'fit',
				region: 'south',
				height: 320,
				border: false,
				items:[this.grid]
			}]
		});
	},
	doSelect: function(){
		var msg = Show();
		this.getForm().submit({
			url: root+'/core/x/ProdOrder!queryBatchLogoffUserProd.action',
			scope: this,
			params: {
				cust_id: App.getCust().cust_id
			},
			success: function(form,action){
				msg.hide();msg = null;
				var data = action.result.simpleObj;
				if(data){
					var store = this.grid.getStore();
					store.removeAll();
					if(data.showData.length > 0){
						store.loadData(data.showData);
						this.returnData = Ext.encode(data.returnData);
						this.grid.setTitle('产品退款汇总, '+'共'+data['userCount']+'个用户');
					}
				}
			},
			failure: function(form, action) {
				msg.hide();msg = null;
				Alert(action.result.simpleObj);
			}
		});
	},
	doValid : function(){
		var formValid =  BatchLogoffUserForm.superclass.doValid.call(this);
		if(formValid.isValid !== true){
			return formValid;
		}
		
		var fileText = Ext.getCmp('bathcLogoffUserId').getValue();
		if(fileText.lastIndexOf('.xls') ==-1 || fileText.lastIndexOf('.xls')!=fileText.length-4){
			return {
				"isValid": false,
				"msg": '请选择excel文件进行上传！'
			}
		}
		if(!this.returnData || this.returnData.length == 0){
			return {
				"isValid": false,
				"msg": '请导入要销户的用户ID!'
			}
		}
		return true;
	},
	getValues : function(){
		var all = {};
		all['cancelUserInfo'] = this.returnData;
		return all;
	},
	success : function(form,res){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var box = TemplateFactory.gTemplate(new BatchLogoffUserForm());
});