/**
 * 批量退订产品
 */
 
CancelProdForm = Ext.extend(Ext.form.FormPanel,{
	userType: 'DTV,ITV',
	oldProdID : null,
	constructor: function(){
		var prodCombo = new PinyinTreeCombo({
			id : 'batchCancelProdTreeId',
	    	fieldLabel: '产品名称',
	    	readOnly : true,
			treeWidth:300,
			minChars:0,
			allowBlank: false,
			treeUrl: Constant.ROOT_PATH+"/core/x/Prod!queryBatchProdTree.action",
			listeners : {
				scope:this,
				'focus' : function(comp){
					prodCombo.treeParams['userType'] = this.userType;
					if(comp.list){
						comp.doQuery('');
					}
				},
				'select' : function(node,obj){
					Ext.getCmp('batchCancelProdTreeId').hasFocus = false;
					var pordId = obj.attributes.id.split(',');
					if(!this.oldProdID){
						this.oldProdID = pordId[1];
					}else{
						if(this.oldProdID == pordId[1]){
							return;
						}
						this.oldProdID = pordId[1];
					}
				}
			}
		});
		
		prodCombo.treeParams['userType'] = this.userType;
		
		CancelProdForm.superclass.constructor.call(this,{
			border:false,
			bodyStyle:'padding-top:10px',
			fileUpload:true,
			items:[
				{fieldLabel:'用户类型',xtype:'paramcombo',
					paramName:'USER_TYPE',defaultValue:'DTV',allowBlank:false,id:'user_type_id',
					listeners:{
						scope: this,
						select: function(combo){
							this.userType = combo.getValue();
							prodCombo.setValue(null);
						}
					}	
				},prodCombo,
				{
					xtype:'textfield',
					inputType:'file',
					fieldLabel:'用户ID文件',
					allowBlank : false,
					name:'file',
					width : 200,
					anchor:'90%',allowBlank:false,
					buttonText:'浏览...'
				}
			]
		});
	}
});

var BatchCancelProdWin = Ext.extend(Ext.Window,{
	form : null,
	constructor:function(){
		this.form = new CancelProdForm();
		BatchCancelProdWin.superclass.constructor.call(this,{
			title:'批量退订产品',
			border:false,
			width:500,
			height:350,
			layout:'fit',
			items:[this.form],
			closeAction:'close',
			buttons:[
				{text:'保存',scope:this,handler:this.doSave},
				{text:'关闭',scope:this,handler:this.closeWin}
			]
		});
		App.form.initComboData(this.findByType('paramcombo'),this.doUserType,this);
	},
	doUserType : function(){//设置用户类型
		var userType = Ext.getCmp('user_type_id');
		if(userType){
			var terStore = userType.getStore();
			terStore.each(function(record){
				if(record.get('item_value') == 'DTV'){
					record.set('item_value','DTV,ITV');
				}
			});
			userType.setValue('DTV,ITV');
		}
	},
	doSave:function(){
		if(this.form.getForm().isValid()){
			var ps={},all={};
			ps['busiCode'] = '1915';
			ps['optr'] = App.getData().optr;
			all[CoreConstant.JSON_PARAMS] = Ext.encode(ps);
			
			all['prodId'] = this.form.oldProdID;
			
			var msg = Show();
			this.form.getForm().submit({
				url:root+'/core/x/User!batchCancelProd.action',
				scope:this,
				params:all,
				success:function(form,action){
						msg.hide();msg = null;
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
						}else{
							Alert('操作成功!',function(){
								this.closeWin();
							},this);
						}
					}
				}
			});
		}
	},
	closeWin:function(){
		this.close();
	}
});