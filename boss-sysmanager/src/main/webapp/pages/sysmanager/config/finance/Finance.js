Ext.ns('Finance');

/**
 * 公用账目配置
 * @class PublicAcctItemGrid
 * @extends Ext.grid.GridPanel
 */
PublicAcctItemGrid = Ext.extend(Ext.grid.GridPanel,{
	publicAcctItemStore : null,
	prodRecs : null,
	constructor : function(){
		
		this.publicAcctItemStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/config/PubAcctItem!queryAllPubAcctItems.action' ,
			fields : ['acctitem_id','acctitem_name','printitem_id','printitem_name','prodIds','prodNames']
		});
		
		var cm = [{
			header : '账目ID',
			dataIndex : 'acctitem_id',
			hidden : true
		},{
			header : '账目名称',
			dataIndex : 'acctitem_name'
		},{
			header : '打印项名称',
			dataIndex : 'printitem_name'
		},{
			header : '产品目录',
			dataIndex : 'prodNames'
		},{
			header : '操作',
			dataIndex : 'acctitem_id',
			renderer : function(){
				return "<a href='#' onclick=Ext.getCmp(\'"+"PublicAcctItemGrid"+"\').modifyRec(); style='color:blue'> 修改 </a>";
			}
		}];
		PublicAcctItemGrid.superclass.constructor.call(this,{
			id : 'PublicAcctItemGrid',
			title : '公用账目配置',
			region : 'north',
			height : 300,
			autoExpandColumn : 'prodIds',
			store : this.publicAcctItemStore,
			frame : true,
			columns : cm,
			viewConfig:{
	        	forceFit : true
	        },
	        tbar : [' ',{
	        	text : '添加公用账目',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	handler : this.addRecord
	        }]
		})
		
		//获取所有产品信息
		Ext.Ajax.request({
			scope : this,
			async: false,
			url : root + '/config/PubAcctItem!queryAllProds.action' ,
			success : function(res,opt){
				var rec = Ext.decode(res.responseText);
				this.prodRecs = rec;
			}
		});
		
	},
	modifyRec : function(){
		var record = this.getSelectionModel().getSelected();
		var win = new PublicAcctItemWin(this.prodRecs,record);
		win.setTitle('修改公用账目');
		win.show();
	},
	addRecord : function(){
		var win = new PublicAcctItemWin(this.prodRecs);
		win.setTitle('增加公用账目');
		win.show();
	}
});

/**
 * 产品FieldSet
 * @class ProdFieldSet
 * @extends Ext.form.FieldSet
 */
ProdFieldSet = Ext.extend(Ext.form.FieldSet,{
	isLoaded : false,
	checkBoxGroup : null,
	constructor : function(prodRecs,prodIds,isLoaded){
		this.isLoaded = isLoaded;
		
		var checkboxs = [];
		for(var i=0;i<prodRecs.length;i++){
			checkboxs.push({
				xtype : 'checkbox',
				boxLabel : prodRecs[i].prod_name,
				checked : prodIds.indexOf(prodRecs[i].prod_id) == -1 ? false : true,
				value : prodRecs[i].prod_id
			})
		};
		
		this.checkBoxGroup = {
			xtype : 'checkboxgroup',
			columns : 3,
			items : checkboxs
		};
		
		var items = [];
		if(this.isLoaded){
			items.push(this.checkBoxGroup);
		}
		CustomFieldSet.superclass.constructor.call(this,{
            checkboxToggle: true,
            collapsible: true,
            collapsed : true,
			labelWidth : 1,
			anchor : '95%',
            items : items
		});
	},
	onCollapse : function(doAnim, animArg){
        Ext.form.FieldSet.superclass.onCollapse.call(this, doAnim, animArg);

    },
    onExpand : function(doAnim, animArg){
    	if(this.isLoaded == false){
    		if(this.items.length == 0){
    			this.add(this.checkBoxGroup);
    			this.doLayout();
    		}
    	}
    	this.isLoaded = true;
    	Ext.form.FieldSet.superclass.onExpand.call(this, doAnim, animArg);
    },
	onCheckClick : function(n,cb){
		if(this.collapsed){
			this.expand();
		}
		var checkboxs = this.items.itemAt(0);
		for(var i=0;i<checkboxs.items.length;i++){
			checkboxs.items.itemAt(i).setValue(cb.checked);
		}
	},
	getValue : function(){
		return this.items.itemAt(0).getValue();
	}
});

/**
 * 产品选择面板
 * @class ProdPanel
 * @extends Ext.Panel
 */
ProdPanel = Ext.extend(Ext.Panel,{
	constructor : function(prodRecs,prodIds){
		var items = [];
		for(var i=0;i<prodRecs.length;){
			if(i == 0){
				items.push(this.createFieldSets(prodRecs.slice(i,i+30),prodIds,true));
			}else{
				items.push(this.createFieldSets(prodRecs.slice(i,i+30),prodIds,false));
			}
			i = i+30;
		};
		
		ProdPanel.superclass.constructor.call(this,{
			title : '产品目录选择',
			bodyStyle : 'padding : 10px;',
			border : false,
			region : 'center',
			layout : 'anchor',
			autoScroll : true,
			items :items
		})
	},
	createFieldSets : function(prodRecs,prodIds,isLoaded){
		var fieldSet = new ProdFieldSet(prodRecs,prodIds,isLoaded);
		return fieldSet;
	},
	getValues : function(){
		var selectedProd = [];
		for(var i=0;i<this.items.length;i++){
			var checkboxs = this.items.itemAt(i).getValue();
			for(var k=0;k<checkboxs.length;k++){
				selectedProd.push(checkboxs[k]);
			}
		}
		return selectedProd;
	}
});

/**
 * 账目添加修改窗口
 * @class PublicAcctItemWin
 * @extends Ext.Window
 */
PublicAcctItemWin = Ext.extend(Ext.Window,{
	form : null,
	prodPanel : null,
	record : null,
	prodRecs : null,
	prodIds : "",
	constructor : function(prodRecs,record){
		this.record = record;
		this.prodRecs = prodRecs;
		
		
		this.form = new Ext.form.FormPanel({
			layout : 'column',
			border : false,
			region : 'north',
			height : 80,
			bodyStyle : 'padding : 5px;padding-top : 10px;',
			labelWidth : 85,
			defaults : {
				columnWidth:0.5,
				layout: 'form',
				labelWidth : 85,
				border : false,
				bodyStyle : 'padding : 5px;padding-top : 10px;'
			},
			items : [{
				columnWidth:0.45,
				items : [{
					xtype : 'textfield',
					fieldLabel : '账目名称',
					name : 'acctitem_name',
					allowBlank : false
				}]
			},{
				columnWidth:0.45,
				items : [new PrintItemPanel({
					allowBlank : false
				})]
			},{
				columnWidth:0.1,
				items : [{
					xtype : 'hidden',
					name : 'acctitem_id'
				}]
			}]
		});
		
		if(this.record){
			this.prodIds = this.record.get('prodIds');
		}
		this.prodPanel = new ProdPanel(this.prodRecs,this.prodIds);
		
		PublicAcctItemWin.superclass.constructor.call(this,{
			layout : 'border',
			height : 500,
			width : 700,
			closeAction : 'close',
			items : [this.form,this.prodPanel],
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
	initEvents: function(){
		PublicAcctItemWin.superclass.initEvents.call(this);
		this.on("afterrender",function(){
			if(this.record){
				this.items.itemAt(0).getForm().loadRecord(this.record);
			}
		},this,{delay:10});
	},
	doSave : function(){
		if(!this.form.getForm().isValid()){
			return;
		};
		
		
		Confirm('确定保存吗',this,function(){
			mb = Show();//显示正在提交
			
			var fieldSets = this.items.itemAt(1).items;
			for(var i=0;i<fieldSets.length;i++){
				if(fieldSets.itemAt(i).collapsed){
					fieldSets.itemAt(i).expand();
					fieldSets.itemAt(i).collapse();
				}
			}
			var selectedProds = this.items.itemAt(1).getValues();
			if(selectedProds.length == 0){
				mb.hide();
				mb = null;
				Alert('请选择产品');
				return false;
			}
			
			var old = this.form.getForm().getValues(),all = {};
			for(var key in old){
				all["publicAcctitem." + key] = old[key];
			}
			
			var prodIds = [];
			for(var i=0;i<selectedProds.length;i++){
				prodIds.push(selectedProds[i].value);
			}
			all['prodIds'] = prodIds.join(',');
			
			Ext.Ajax.request({
				scope : this,
				url : root + '/config/PubAcctItem!savePublicAcctItem.action',
				params : all,
				success : function(res,opt){
					mb.hide();//隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						Ext.getCmp('PublicAcctItemGrid').getStore().reload();
						this.close();
					}else{
						Alert('操作失败');
			 		}
				}
			})
		});
	}
});


/**
 * 资金项配置
 * @class AcctFeeTypeGrid
 * @extends Ext.grid.EditorGridPanel
 */
AcctFeeTypeGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	acctFeeTypeStore : null,
	paramcombo : null,
	constructor : function(){
		this.acctFeeTypeStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/config/BusiFee!queryAllFeeType.action' ,
			fields : ['fee_type','type_name','priority','can_refund','can_refund_text','can_trans','can_trans_text','is_cash','is_cash_text']
		});
		
		this.paramcombo = new Ext.ux.ParamCombo({
			xtype : 'paramcombo',
			allowBlank:false,
			forceSelection:true,
			selectOnFocus:true,
			valueField : 'item_name',
			typeAhead:false,
			paramName: 'BOOLEAN'
		});
		App.form.initComboData([this.paramcombo]);
		var sm = new Ext.grid.CheckboxSelectionModel({});
		var columns = [{
			header : '名称',
			dataIndex : 'type_name',
			editor: new Ext.form.TextField({
                allowBlank: false
            })
		},{
			header : '优先级',
			dataIndex : 'priority',
			editor: new Ext.form.NumberField({
                allowBlank: false
            })
		},{
			header : '可退',
			dataIndex : 'can_refund_text',
			editor: this.paramcombo
		},{
			header : '可转',
			dataIndex : 'can_trans_text',
			editor: this.paramcombo
		},{
			header : '是否现金',
			dataIndex : 'is_cash_text',
			editor: this.paramcombo
		}];
		AcctFeeTypeGrid.superclass.constructor.call(this,{
			id : 'AcctFeeTypeGrid',
			title : '资金项配置',
			region : 'center',
			store : this.acctFeeTypeStore,
			sm:sm,
	        columns : columns,
	        enableColumnMove : false,
	        frame: true,
	        forceValidation: true,
	        clicksToEdit: 1,
	        viewConfig : {
				forceFit : true
			},
	        tbar: [' ','请直接点击单元格编辑','->',{
            	text:'保存配置',
            	id : 'saveFeeTypeBtn',
            	iconCls : 'icon-save',
            	scope : this,
            	disabled : true,
            	handler : this.doSave
            },'   ']
		})
	},
	initEvents : function(){
		this.on("afteredit", this.afterEdit, this);
		AcctFeeTypeGrid.superclass.initEvents.call(this);
	},
	afterEdit : function(obj){
		if(obj.field == 'can_refund_text'){
			var index = this.paramcombo.getStore().find('item_name',obj.value);
			obj.record.data.can_refund = this.paramcombo.getStore().getAt(index).get('item_value');
		}else if(obj.field == 'can_trans_text'){
			var index = this.paramcombo.getStore().find('item_name',obj.value);
			obj.record.data.can_trans = this.paramcombo.getStore().getAt(index).get('item_value');
		}else if(obj.field == 'is_cash_text'){
			var index = this.paramcombo.getStore().find('item_name',obj.value);
			obj.record.data.is_cash = this.paramcombo.getStore().getAt(index).get('item_value');
		}
		
		if(this.getStore().getModifiedRecords().length > 0){
			Ext.getCmp('saveFeeTypeBtn').setDisabled(false);
		}
	},
	checkPriorityValid : function(){//验证优先级
		var flag = false;
		for(var i=1;i<this.getStore().getCount();i++){//第二个开始依次向前进行比较
			for(var k=i;k>0;k--){
				if(this.getStore().getAt(i).get('priority') == this.getStore().getAt(k-1).get('priority')){
    				flag = true;
    				break;
				}
			}
		}
		return flag;
	},
	doSave : function(){
		if(this.checkPriorityValid()){
			Alert('优先级不能重复');
			return;
		}		
		
		Confirm('确定保存吗',this,function(){
			mb = Show();//显示正在提交
			var records = [];
			var modifiedRec = this.getStore().getModifiedRecords();
			
			for(var i=0;i<modifiedRec.length;i++){
				records.push(modifiedRec[i].data);
			}
			
			Ext.Ajax.request({
				scope : this,
				url : root + '/config/BusiFee!updateFeeType.action' ,
				params : {
					feeTypeListStr : Ext.encode(records)
				},
				success : function(res,opt){
					mb.hide();//隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						this.getStore().reload();
						Ext.getCmp('saveFeeTypeBtn').setDisabled(true);
					}else{
						Alert('操作失败');
			 		}
				}
			})
		})
	}
});

/**
 * 财务配置视图
 * @class FinanceView
 * @extends Ext.Panel
 */
FinanceView = Ext.extend(Ext.Panel,{
	constructor : function(){
		FinanceView.superclass.constructor.call(this,{
			id : 'FinanceView',
			layout : 'border',
			title : '财务配置',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			items : [new AcctFeeTypeGrid(),new PublicAcctItemGrid()]
		})
	}
});