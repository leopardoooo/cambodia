PrintCfgGrid = Ext.extend(Ext.grid.GridPanel,{
	invoiceStore : null,
	docCombo:null,
	constructor : function(){
		this.invoiceStore = new Ext.data.JsonStore({
			autoLoad:true,
			baseParams : {
				templateType : 'INVOICE'
			},
			url  : root + '/config/Template!queryInvoiceType.action',
			fields : ['doc_name','doc_type']
		});
		this.invoiceStore.on("load",this.loadData,this);
	
		var sm = new Ext.grid.CheckboxSelectionModel();
		PrintCfgGrid.superclass.constructor.call(this,{
			id : 'batchPayFeeGrid',
			title : '发票选择',
			region : 'center',
			viewConfig : {
				forceFit : true
			},
			border : false,
			store : this.invoiceStore,
			sm:sm,
			columns : [
            sm,
			{header:'发票类型',dataIndex:'doc_name',width:100}
        	]
		})
		
	},
	loadData:function(store){
		store.removeAt(terStore.find('item_value','1'));
	},
	swapViews : function(){
		if(this.view.lockedWrap){
			this.view.lockedWrap.dom.style.right = "0px";
		}
        this.view.mainWrap.dom.style.left = "0px"; 
        if(this.view.updateLockedWidth){
        	this.view.updateLockedWidth = this.view.updateLockedWidth.createSequence(function(){ 
	            this.view.mainWrap.dom.style.left = "0px"; 
	        }, this); 
        }
	},

	getValues:function(){
		var arr = [];
		var records = this.getSelectionModel().getSelections();
		Ext.each(records,function(r){
			var obj = {};
				obj["doc_type"] = r.get('doc_type');
				arr.push(obj);
		});
		return arr;
	}
})

PrintCfgWindow = Ext.extend(Ext.Window, {
	printCfgGrid : null,
	parent:null,
	constructor : function(p) {
		this.printCfgGrid = new PrintCfgGrid();
		winThis = this;
		winThis.parent = p;
		PrintCfgWindow.superclass.constructor.call(this, {
			title : '增加打印项',
			layout : 'border',
			width : 450,
			height :300,
			closeAction : 'close',
			border : false,
			items : [{
				region : 'north',
				height : 60,
				items : [{
						baseCls : 'x-plain',
						layout : 'form',
						labelWidth : 90,
						id:'printNameItemId',
						bodyStyle: "background:#F9F9F9; padding: 20px;",
						items:[{
								xtype : 'textfield',
								fieldLabel : '打印项名称',
								allowBlank : false,
								id:'printitemNameId',
								name : 'printitemName',
								listeners : {
									scope : this,
									specialkey : function(txt,e){
										if(e.getKey() == 13){
											this.doSave();
										}
									}
								}
						}]
				
				}]
			},this.printCfgGrid],
			buttons : [{
						text : '保存',
						scope : this,
						handler : this.doSave

					}, {
						text : '关闭',
						scope : this,
						handler : function() {
							winThis.close();
						}
					}]
		});
	},
	doSave : function() {
		if(!Ext.getCmp('printNameItemId').items.itemAt(0).isValid()){
			return;
		};
		var all = {};
		var data = this.printCfgGrid.getValues();
		if(Ext.isEmpty(data)){Alert("请先配置模板对应的发票类型后,再保存!");  return false;}
		var all = {'templateArr':Ext.encode(data)};
		all["printitemName"]= Ext.getCmp('printitemNameId').getValue();
		Confirm('确定保存吗',this,function(){
//			mb = Show();//显示正在提交
			Ext.Ajax.request({
				scope : this,
				url : root + '/config/PubAcctItem!savePrintItem.action',
				params : all,
				success : function(res,opt){
//					mb.hide();//隐藏提示框
//					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						winThis.parent.doInit(all.printitemName);
//						var index = winThis.parent.printitemStore.find('printitem_name',all.printitemName);
//						winThis.parent.combo.setValue(winThis.parent.printitemStore.getAt(index).get('printitem_id'))
						winThis.close();
					}else{
						Alert('操作失败');
			 		}
				}
			})
		});
	}
});
/**
 * 打印项下拉框
 * @class PrintItemPanel
 * @extends Ext.form.CompositeField
 */
PrintItemPanel = Ext.extend(Ext.form.CompositeField,{
	win : null,
	combo : null,
	printitemStore : null,
	constructor : function(cfg){
		if(cfg){
			Ext.apply(this,cfg);
		}else{
			cfg = {};
			cfg['allowBlank'] = true;
		}
		
		this.printitemStore = new Ext.data.JsonStore({
			fields : ['printitem_id','printitem_name']
		});
		this.combo = new Ext.form.ComboBox({
			store : this.printitemStore,
			allowBlank : cfg.allowBlank,
			hiddenName : 'printitem_id',
			displayField : 'printitem_name',
			valueField : 'printitem_id',
			plugins:[new Ext.plugin.PinyinFilter],
			triggerAction : 'all',
			editable : true,
			forceSelection : true,
			mode : 'local'
		});
		
		this.doInit();

		PrintItemPanel.superclass.constructor.call(this,{
			combineErrors: false,
			fieldLabel : '打印项',
//			id:'PrintItemPanelId',
			items : [this.combo,{
				xtype : 'button',
				text : '添加',
				scope : this,
				handler : this.addRecord
			}]
		})
	},
	doPrintInit:function(callback,scope){
			Ext.Ajax.request({
			url : root + '/config/PubAcctItem!queryAllPrintitems.action' ,
			scope : this,
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				this.printitemStore.loadData(data);
				
				//回调函数
				if(Ext.isFunction(callback)){
					callback.call(scope);
				}
			}
		})
	}
	,
	doInit : function(name){
		Ext.Ajax.request({
			url : root + '/config/PubAcctItem!queryAllPrintitems.action' ,
			scope : this,
			async: true,
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				this.printitemStore.loadData(data);
				if(name){
					var index = this.printitemStore.find('printitem_name',name);
					this.combo.setValue(this.printitemStore.getAt(index).get('printitem_id'))
				}				
			}
		})
	},
	addRecord : function(){
		var pctComp = Ext.getCmp('ProdCountyTree'),ids = null;
		if(pctComp){
			ids = pctComp.getCheckedIds();
			if(ids.length > 0){
				this.win = new PrintCfgWindow(this);
				this.win.show();
			}else{
				Alert('请先选择产品适用地区!');
			}
		}else{
			this.win = new PrintCfgWindow(this);
			this.win.show();
		}
	}
});