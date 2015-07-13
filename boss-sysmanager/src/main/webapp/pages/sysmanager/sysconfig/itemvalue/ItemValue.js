Ext.ns('ItemValue');

ComEditorGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	constructor : function(cfg){
		ComEditorGrid.superclass.constructor.call(this,cfg);
	},
	initEvents : function(){
		this.on("afteredit", this.afterEdit, this);
		ComEditorGrid.superclass.initEvents.call(this);
	},
	afterEdit : function(obj){
		
	},
	addRecord : function(rec){
		var Plant= this.getStore().recordType;
		var p = new Plant(rec);
		this.stopEditing();
		this.getStore().insert(0,p);
		this.startEditing(0,0);
	},
	doValid : function(){//验证配置不能重复,且不允许出现单元为空
		if(this.getStore().getCount() > 0){//检验第一条数据是否输入完整
			var rec = this.getStore().getAt(0).data;
			for(var key in rec){
				if(Ext.isEmpty(rec[key])){
					return "数据没有编辑完整";
				}
			}
		}
		//检验第一条以后的数据是否输入完整以及是否存在两个配置相同
		for(var i=1;i<this.getStore().getCount();i++){//第二个开始依次向前进行比较
			for(var k=i;k>0;k--){
				var flag = true;//默认两个对象的属性值相同
				for(var key in this.getStore().getAt(i).data){
					if(Ext.isEmpty(this.getStore().getAt(i).data[key])){//检验第一条以后的数据是否输入完整
						return "数据没有编辑完整";
					}
					if(this.getStore().getAt(i).data[key] != this.getStore().getAt(k-1).data[key]){
						flag = false;//属性值不同
						break;
					} 
				}
				if(flag){
					return "第"+(i+1)+"个配置和第"+(k)+"个配置相同，请重新编辑";
				}
			}
		}
    	return true;
	}
})

/**
 * 参数定义编辑表
 * @class ItemDefineGrid
 * @extends Ext.grid.EditorGridPanel
 */
ItemDefineGrid = Ext.extend(Ext.grid.GridPanel,{
	itemDefineStore : null,
	constructor : function(){
		this.itemDefineStore = new Ext.data.JsonStore({
			url  : root + '/system/Param!queryItemDefines.action' ,
			fields : ['item_key','item_desc']
		});
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true
		});
		var columns = [sm,{
			header : '参数键值',
			dataIndex : 'item_key'
		},{
			header : '参数描述',
			dataIndex : 'item_desc'
		},{
			header : '操作',
			dataIndex : 'item_key',
			renderer : function(){
				return "<a href='#' onclick=Ext.getCmp(\'"+"ItemDefineGrid"+"\').modifyRec(); style='color:blue'> 修改 </a>";
//				return "<a href='#' onclick=Ext.getCmp(\'"+"ItemDefineGrid"+"\').modifyRec(); style='color:blue'> 修改 </a>"+"&nbsp;<a href='#' onclick=Ext.getCmp(\'"+"ItemDefineGrid"+"\').deleteRec(); style='color:blue'> 删除 </a>";
			}
		}];
		ItemDefineGrid.superclass.constructor.call(this,{
			id : 'ItemDefineGrid',
			title : '参数键值管理',
			region : 'center',
			border : false,
			store : this.itemDefineStore,
			columns : columns,
			sm : sm,
			viewConfig:{
	        	forceFit : true
	        },
	        tbar : [' ',' ','输入关键字' , ' ',       //搜索功能
				new Ext.ux.form.SearchField({  
	                store: this.itemDefineStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: '支持键值,描述模糊查询'
	            }),'->',{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	handler : this.addRecord
	        },' ']
		});
	},
	initEvents : function(){
		this.on('rowclick',this.doClick,this);
		ItemDefineGrid.superclass.initEvents.call(this);
	},
	doClick : function(grid,rowIndex){
		if(Ext.getCmp('saveItemValue').disabled == false){
			Confirm("参数配置未保存，确定重置?", this ,function(){
				var record = grid.getStore().getAt(rowIndex);
				Ext.getCmp('ItemValueConfig').loadData(record.get('item_key'));
			},function(){
				return;
			});
		}else{
			var record = grid.getStore().getAt(rowIndex);
			Ext.getCmp('ItemValueConfig').loadData(record.get('item_key'));
		}
	},
	addRecord : function(){
		var win = new ItemDefineWin();
		win.setTitle('增加参数');
		win.setIconClass('icon-add-user');
		win.show();
	},
	modifyRec : function(){
		var win = new ItemDefineWin(this.getSelectionModel().getSelected());
		win.setTitle('修改参数名称');
		win.setIconClass('icon-edit-user');
		win.show();
	},
	deleteRec : function(){
		Ext.getCmp('ItemValueConfig').getStore().on('load',function(store,recs){
			if(recs.length > 0){
				Alert("该参数还有配置，不能删除");
				return;
			}
			
			Confirm("确定删除吗?", this ,function(){
				mb = Show();
				var record= this.getSelectionModel().getSelected();
				Ext.Ajax.request({
					scope : this,
					url  : root + '/system/Param!deleteItemDefine.action',
					params : {
						itemKey : record.get('item_key')
					},
					success : function(res,opt){
						mb.hide();//隐藏提示框
						mb = null;
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							Ext.getCmp('ItemDefineGrid').getStore().reload();
						}else{
							Alert('操作失败');
				 		}
					}
				})
				
				this.getStore().remove(record);
				this.deletedItemKey = this.deletedItemKey + record.get('item_key')+",";
			})
			
		},this, {single: true});
	}
});

/**
 * 参数定义增加修改窗口
 * @class ItemDefineWin
 * @extends Ext.Window
 */
ItemDefineWin = Ext.extend(Ext.Window,{
	form : null,
	constructor : function(record){
		this.form = new Ext.form.FormPanel({
			layout : 'form',
			border : false,
			labelWidth : 85,
			bodyStyle : 'padding : 5px;padding-top : 10px;',
			defaults : {
				xtype : 'textfield'
			},
			items : [{
				fieldLabel : '参数键值',
				name : 'item_key',
				id : 'item_keyForUpdate',
				width : 150,
				allowBlank : false
			},{
				fieldLabel : '参数描述',
				name : 'item_desc',
				width : 150,
				allowBlank : false
			}]
		});
		if(record){
			this.form.getForm().loadRecord(record);
			Ext.getCmp('item_keyForUpdate').setReadOnly(true);
		}
		
		ItemDefineWin.superclass.constructor.call(this,{
			layout : 'fit',
			height : 350,
			width : 300,
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
	doSave :function(){
		if(this.form.getForm().isValid()){
			Confirm("确定保存吗?", this ,function(){
				mb = Show();//显示正在提交
				
				var old =  this.form.getForm().getValues(), newValues = {};
				for(var key in old){
					newValues["itemDefine." + key] = old[key];
				}
				Ext.Ajax.request({
					scope : this,
					url  : root + '/system/Param!saveItemDefine.action' ,
					params : newValues,
					success : function( res,ops ){
						mb.hide();//隐藏提示框
						mb = null;
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							Ext.getCmp('ItemDefineGrid').getStore().reload();
							this.close();
						}else{
							Alert('操作失败');
				 		}
					}
				})
			});
		}
	}
});

/**
 * 参数配置编辑表
 * @class ItemValueConfig
 * @extends Ext.grid.EditorGridPanel
 */
ItemValueConfig = Ext.extend(ComEditorGrid,{
	itemKey :null,
	itemValueStore : null,
	constructor : function(){
		this.itemValueStore = new Ext.data.JsonStore({
			url  : root + '/system/Param!queryItemValues.action' ,
			fields : ['item_key','item_value','item_name','item_idx','show_county_id','show_county_text']
		});
		
		this.showCountyComb = new Ext.ux.ParamLovCombo({
			paramName:'COUNTY',
			isFilter:false,
			listWidth:200
		});
		
		App.form.initComboData([this.showCountyComb]);
		
		var paramComboRender = function(value){
			if(!Ext.isEmpty(value)){
				if(value.indexOf(',') == -1){
					var index = this.find('item_value',value);
					var record = this.getAt(index);
					if(!Ext.isEmpty(record)){
						return record.get('item_name');
					}
				}else{
					var countyIds = value.split(','), county_text = '';
					Ext.each(countyIds,function(c){
						var index = this.find('item_value',c);
						var record = this.getAt(index);
						county_text += record.get('item_name')+',';
					},this);
					
					county_text = county_text.substring(0,county_text.lastIndexOf(','));
					return '<div ext:qtitle="" ext:qtip="' + county_text + '">' + county_text +'</div>';
				}
			}
			return '';
		}
		
		var columns = [{
			header : '参数值',
			dataIndex : 'item_value',
			width:80,
			editor :new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : '参数名称',
			dataIndex : 'item_name',
			width:100,
			editor :new Ext.form.TextField({
				allowBlank : false
			})
		},{
			header : '显示县市',
			dataIndex : 'show_county_id',
			width:250,
			editor :this.showCountyComb,
			renderer:paramComboRender.createDelegate(this.showCountyComb.getStore())
		},{
			header : '参数索引',
			dataIndex : 'item_idx',
			width:70,
			editor :new Ext.form.NumberField({
				allowBlank : false
			})
		}/*,{
			header : '操作',
			dataIndex : 'item_key',
			renderer : function(){
				return "<a href='#' onclick=Ext.getCmp(\'"+"ItemValueConfig"+"\').deleteRec(); style='color:blue'> 删除 </a>";
			}
		}*/
		];
		ItemValueConfig.superclass.constructor.call(this,{
			id : 'ItemValueConfig',
			title : '参数配置管理',
			region : 'south',
			height : 250,
			border : false,
			enableColumnMove : false,
			forceValidation: true,
	        clicksToEdit: 1,
	        frame: true,
			store : this.itemValueStore,
			columns : columns,
			viewConfig:{
	        	forceFit : true
	        },
	        tbar : ['->',{
	        	text : '添加',
	        	iconCls : 'icon-add',
	        	scope : this,
	        	handler : this.addRecord
	        },'-',{
				text:'保存',
            	id : 'saveItemValue',
            	disabled : true,
            	iconCls : 'icon-save',
            	scope : this,
            	handler : this.doSave
			},' ']
		})
	},
	afterEdit : function(){
		if(this.getStore().getModifiedRecords().length > 0){
			Ext.getCmp('saveItemValue').setDisabled(false);//激活保存按钮
		}
	},
	addRecord : function(){
		if(Ext.getCmp('ItemDefineGrid').getSelectionModel().getSelections().length == 0){
			Alert('请先选择一条参数记录。');
			return;
		};
		var rec = {
			item_key : this.itemKey,
			item_value : '',
			item_name : '',
			item_idx : 0,
			show_county_id: '4501'
		};
		ItemValueConfig.superclass.addRecord.call(this,rec);
	},
	loadData : function(itemKey){
		Ext.getCmp('saveItemValue').setDisabled(true);//禁用保存按钮
		this.itemKey = itemKey;
		this.getStore().load({
			params : {
				itemKey : itemKey
			}
		});
	},
	updateRec: function(){
		
	},
	deleteRec : function(){
		Confirm("确定删除吗?", this ,function(){
			Ext.getCmp('saveItemValue').setDisabled(false);//激活保存按钮
			var record= this.getSelectionModel().getSelected();
			this.getStore().remove(record);
		})
	},
	doSave : function(){
		var msg = this.doValid();
		if(msg != true){
			Alert(msg);
			return;
		}
		this.stopEditing();
		Confirm("确定保存吗?", this ,function(){
			mb = Show();//显示正在提交
			
			var records = [];
			
			this.getStore().each(function(r){
				var data = r.data;
				if(Ext.isEmpty(data['show_county_id']) || data['show_county_id'].indexOf('4501') >= 0){
					data['show_county_id'] = '4501';
				}
				records.push(data);
			});
			
			Ext.Ajax.request({
				scope : this,
				url  : root + '/system/Param!saveItemValues.action' ,
				params : {
					itemKey : this.itemKey,
					records : Ext.encode(records)
				},
				success : function( res,ops ){
					mb.hide();//隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						this.getStore().reload();
						Ext.getCmp('saveItemValue').setDisabled(true);//禁用保存按钮
					}else{
						Alert('操作失败');
			 		}
				}
			})
		});
	}
});

/**
 * 参数配置视图
 * @class ItemValueView
 * @extends Ext.Panel
 */
ItemValueView = Ext.extend(Ext.Panel,{
	constructor : function(){
		ItemValueView.superclass.constructor.call(this,{
			id : 'ItemValueView',
			layout : 'border',
			title : '系统参数配置',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			items : [new ItemDefineGrid(),new ItemValueConfig()]
		})
	}
})