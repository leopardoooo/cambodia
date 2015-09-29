BatchLogoffUserForm = Ext.extend(Ext.form.FormPanel,{
	parent: null,
	constructor : function(p){
		this.parent = p;
		BatchLogoffUserForm.superclass.constructor.call(this,{
			region: 'center',
			layout : 'form',
			bodyStyle : Constant.TAB_STYLE,
			border : false,
			fileUpload: true,
			items : [{
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
		});
	},
	doSelect: function(){
		var msg = Show();
		this.getForm().submit({
			url: root+'/core/x/ProdOrder!queryBatchLogoffUserProd.action',
			scope: this,
			success: function(form,action){
				msg.hide();msg = null;
				var data = action.result;
				if(data && data.simpleObj){
					var store = this.parent.grid.getStore();
					store.removeAll();
					if(data.simpleObj && data.simpleObj.showData.length > 0){
						store.loadData(Ext.decode(data.simpleObj.showData));
						this.parent.returnData = data.simpleObj.returnData;	
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
		var flag = true;
		if(this.getForm().isValid()){
			var fileText = Ext.getCmp('bathcLogoffUserId').getValue();
			if(fileText.lastIndexOf('.xls') ==-1 || fileText.lastIndexOf('.xls')!=fileText.length-4){
				Alert('请选择excel文件进行上传！');
				flag =  false;
			}
		}else{
			flag =  false;
		}
		return flag
	},
	getValues : function(){
		var all =  this.getForm().getValues();
		return all;
	}
})

var BatchLogoffUserWin = Ext.extend(Ext.Window,{
	form : null,
	returnData: null,
	constructor:function(){
		this.form = new BatchLogoffUserForm(this);
		
		this.grid = new Ext.grid.GridPanel({
			region: 'south',
			title: '产品退款汇总',
			height: 340,
			border: false,
			columns: [
				{header:'产品名称',dataIndex:'prod_name',width:250},
				{header:'退款金额',dataIndex:'active_fee',width:120, renderer: Ext.util.Format.moneyRenderer}
			],
			store: new Ext.data.JsonStore({
				fields: ['prod_name', 'active_fee']
			})
		});
		
		BatchLogoffUserWin.superclass.constructor.call(this,{
			title:'批量销用户',
			border:false,
			width:400,
			height:450,
			layout:'border',
			items:[this.form, this.grid],
			closeAction:'close',
			buttons:[
				{text:'保存', iconCls:'icon-save', scope:this,handler:this.doSave},
				{text:'关闭', iconCls:'icon-close', scope:this,handler:this.closeWin}
			]
		});
	},
	doSave:function(){
		if(this.form.doValid()){
			var values = this.form.getValues();
			var ps={},all={};
			ps['busiCode'] = '1913';
			ps['optr'] = App.getData().optr;
			all[CoreConstant.JSON_PARAMS] = Ext.encode(ps);
			all['cancelUserInfo'] = this.returnData;
			
			Ext.Ajax.request({
				url:root+'/core/x/User!batchLogoffUser.action',
				params: all,
				scope: this,
				success : function(response,opts){
				
				}
			});
		}
	},
	closeWin:function(){
		this.close();
	}
});