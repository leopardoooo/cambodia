/**
 * 变更产品的 公用账目使用类型.
 * @class PublicAcctItemTypeForm
 * @extends BaseForm
 */
PublicAcctItemTypeForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/Prod!updatePublicAcctItemType.action",
	record:null,
	doInit:function(){
		var recData = this.record.data;
		var type = recData.public_acctitem_type;
		if(!type || Ext.isEmpty(type)){
			return ;
		}
		var store = Ext.getCmp('public_acctitem_type_combo').getStore();
		var index = store.find('item_value',type);
		store.removeAt(index);
	},
	constructor: function(){
		this.record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelectionModel().getSelected();
//		if (this.record == null){
//			this.record = App.getApp().main.infoPanel.getCustPanel().packageGrid.getSelectionModel().getSelected();
//		}
		
		PublicAcctItemTypeForm.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			buttonAlign:'center',
			labelWidth: 130,
			layout:'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			buttons:[{text:'保存',handler:this.doSave,scope:this}],
			items : [{
				xtype : 'hidden',
				value : this.record.get('prod_sn'),
				id : 'prodSn'
			},{
				xtype : 'textfield',
				fieldLabel : '产品名称',
				style : Constant.TEXTFIELD_STYLE,
				value : this.record.get('prod_name'),
				id : 'prodName'
			},{
				width : 150,
				fieldLabel : '原公用账目使用类型',
				xtype : 'textfield',
				style : Constant.TEXTFIELD_STYLE,
                value:this.record.get('public_acctitem_type_text')
			},{
				fieldLabel:'新公用账目使用类型',
				allowBlank : false,
				emptyText:'公用账目使用类型不能为空!',
				xtype:'paramcombo',
				paramName:'PUBLIC_ACCTITEM_TYPE',
				hiddenName:'publicAcctitemType',
				id:'public_acctitem_type_combo'
			}]
		})
		
	},
	validate:function(){
		var val = Ext.getCmp('public_acctitem_type_combo').getValue();
		return !Ext.isEmpty(val);
	},
	getValues : function(){
		var params = this.getForm().getValues();
		var ps = App.getApp().getValues();
		ps.busi_code = App.getApp().getData().currentResource.busicode;
		params['countyId'] = this.record.data.county_id;
		params.jsonParams = Ext.encode(ps);
		return params;
	},
	doSave:function(){
		if(!this.validate()){
			return false;
		}
		var param = this.getValues();
		var msg = '确定修改？';
		tip = Show();
		Confirm(msg,this,function(){
			Ext.Ajax.request({
				url:this.url,
				params:param,
				scope:this,
				timeout:99999999999999,//12位 报异常
				success:this.success
			});
		});
	},
	success : function(){
		Alert('操作成功',function(){
			App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
			tip.hide();
			tip = null;
			App.getApp().menu.bigWindow.hide();
		},this);
	}
});
Ext.onReady(function(){
	var form = new PublicAcctItemTypeForm();
//	var box = TemplateFactory.gTemplate(form);
	var box = TemplateFactory.gViewport(form);
});