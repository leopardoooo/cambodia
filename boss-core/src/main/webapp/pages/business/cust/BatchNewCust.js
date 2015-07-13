/**
 * 客户批量开户
 * @class BatchNewCustForm
 * @extends BaseForm
 */
 
AddressTreeCombo = Ext.extend(Ext.ux.TreeCombo,{
	onNodeClick : function(node, e) {
		if (!this.isCanClick(node)) {
			return;
		}
		var attrs = this.dispAttr.split(".");
		
		 var level = node.attributes.others.tree_level;
		if(!Ext.isEmpty(level)){
			this.level =level;
		}
		
		var temp = null;
		
		//区域对应的文本
		var temp = node.parentNode.attributes;
		var addrText = temp[this.dispAttr];
		
		temp = node.attributes;
		addrText = addrText + temp[this.dispAttr];
		
		
		// 显示隐藏值
		this.addOption(node.id, addrText);
		this.setValue(node.id);
		// 设置显示值
		this.setRawValue(addrText);
		this.collapse();
		this.fireEvent('select', this, node, node.attributes);
	}
})

BatchNewCustForm = Ext.extend(BaseForm,{
	url:  Constant.ROOT_PATH + "/core/x/Cust!batchNewCust.action",
	constructor : function(){
		BatchNewCustForm.superclass.constructor.call(this,{
			layout : 'form',
			bodyStyle : Constant.TAB_STYLE,
			border : false,
			fileUpload: true,
			items : [new AddressTreeCombo({
		    	width:140,
		    	id : 'address',
				treeWidth:400,
				allowBlank : false,
				minChars:2,
				height: 22,
				fieldLabel : '小区',
				emptyText :'请输入两个字进行搜索',
				blankText:'请选择客户地址',
				treeUrl: Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrTree.action",
				hiddenName:'addrId',
				listeners:{
					scope: this,
					'select': function(combo){
						Ext.getCmp('CustNameId').setValue(combo.getRawValue());
					}
				}
			}),{xtype : 'textfield',
				width :160,
				allowBlank : false,
				id:'CustNameId',
				name : 'custName',
				fieldLabel : '客户名前缀'
			},{
				xtype : 'numberfield',
				width :160,
				allowBlank : false,
				allowNegative : false,
				allowDecimals : false,
				name : 'custNum',
				fieldLabel : '开户总数'
			},{
				id:'addressInFielId',
				xtype:'textfield',
				fieldLabel:'地址文件',
				inputType:'file',
		        anchor:'90%',
		        name:'file'}]
		})
	},
	doValid:function(){
		var fileText = Ext.getCmp('addressInFielId').getValue();			
		if(fileText){
			if(fileText.lastIndexOf('xlsx')>0 || fileText.lastIndexOf('.xlsx')==fileText.length-5){
				var obj = {};
				obj['isValid'] = false;
				obj['msg'] = '请选择excel2003文件进行上传,文件后缀名为.xls!';
				return obj;
			}else if(fileText.lastIndexOf('.xls') ==-1 || fileText.lastIndexOf('.xls')!=fileText.length-4){
				var obj = {};
				obj['isValid'] = false;
				obj['msg'] = '请选择excel文件进行上传!';
				return obj;
			}
		}
		return BatchNewCustForm.superclass.doValid.call(this);
	},
	getValues : function(){
		var all =  this.getForm().getValues();
		all['address'] = Ext.getCmp('address').getRawValue();
		return all;
	}
});

Ext.onReady(function(){
	var buy = new BatchNewCustForm();
	var box = TemplateFactory.gTemplate(buy);
});