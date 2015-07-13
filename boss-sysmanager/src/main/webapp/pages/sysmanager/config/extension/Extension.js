Ext.ns('Extension');

/**
 * 扩展属性可编辑表格
 * @class ExtensionGrid
 * @extends Ext.grid.EditorGridPanel
 */
ExtensionGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	extendType : null,//扩展类型
	extendValue :null,//扩展类型对应参数(表名或busiCode)
//	deleteAttriID : "",//记录删除的ID
//	isModified : false,//是否修改
	attributeStore : null,//扩展属性数据
	groupStore : null,//分组类型数据
	nullableStore : null,//是否允许为空数据
	inputtypeStore : null,//输入类型数据
	columnStore : null,//首列字段名数据	
	paramStore :　null,//下拉框参数数据
	cm : null,
	extensionId : null,
	custExtGroup : null,
	userExtGroup :　null,
	constructor : function(){
		//分组类型数据
	 	this.custExtGroup = [
	 		['3','非居民扩展'],
 			['2','居民扩展'],
 			['1','集团扩展']
 		];
 		
 		this.userExtGroup = [
 			['1','数字电视扩展'],
 			['2','模拟电视扩展'],
 			['3','宽带扩展']
 		];
	 		
	 	this.groupStore = new Ext.data.SimpleStore({
	 		fields : ['group_id','group_name'],
	 		data : this.custExtGroup
	 	});
		
	 	//首列字段名数据
		this.columnStore = new Ext.data.SimpleStore({
			fields :['value','text'],
			data : [
				['str1','扩展字段1'],['str2','扩展字段2'],['str3','扩展字段3'],['str4','扩展字段4'],['str5','扩展字段5'],
				['str6','扩展字段6'],['str7','扩展字段7'],['str8','扩展字段8'],['str9','扩展字段9'],['str10','扩展字段10'],
				['str11','扩展字段11'],['str12','扩展字段12'],['str13','扩展字段13'],['str14','扩展字段14'],['str15','扩展字段15'],
				['str16','扩展字段16'],['str17','扩展字段17'],['str18','扩展字段18'],['str19','扩展字段19'],['str20','扩展字段20']
			]
		});
			
		//是否允许为空数据
		this.nullableStore = new Ext.data.SimpleStore({
			fields:['item_value', 'item_name'],
			data: [
				['F','否'],
				['T','是']
			]
		});
	 	//是否允许显示
		this.showStore = new Ext.data.SimpleStore({
			fields:['item_value', 'item_name'],
			data: [
				['F','否'],
				['T','是']
			]
		});
		//输入类型数据
		this.inputtypeStore = new Ext.data.JsonStore({
					autoLoad : true,
					url  : root + '/config/ExtendTable!queryInputtype.action' ,
					fields : ['item_name','item_value']
			});	
			
		//下拉框参数数据
		this.paramStore = new Ext.data.JsonStore({
					autoLoad : true,
					url  : root + '/config/ExtendTable!queryParams.action' ,
					fields : ['item_key','item_desc']
			});	
		
		//扩展属性数据
		this.attributeStore = new Ext.data.JsonStore({
				url  : root + '/config/ExtendTable!queryAttribute.action' ,
				fields: [{name: 'extend_id'},{name: 'attribute_id'},
		           {name: 'attribute_name'},{name: 'attribute_order'},{name: 'col_name' },{name: 'col_name_text' },
		           {name: 'group_name'},{name: 'group_id'},{name:'is_show'},{name:'is_show_text'},
		           {name: 'input_type'},{name: 'input_type_text'},{name: 'is_null'},{name: 'default_value'},{name:'default_value_text'},
		           {name: 'is_null_text'},{name: 'param_name'},{name: 'param_name_text'},{name: 'busi_code' }
		        ],
		        sortInfo: {
		            field: 'attribute_id', direction: 'ASC'
		        },
		        listeners:{
		        	scope:this,
		        	load:function(store,records){
		        		this.paramComboDataMap = {};
		        		var params = [];
						for(var i=0;i < records.length;i++){
							records[i].set('default_value_text',records[i].get('default_value'))
							if(records[i].data.input_type =='paramcombo'){
								params.push(records[i].data.param_name);
							}
						}
						if(params.length > 0 ){
							Ext.Ajax.request({
								params: {comboParamNames: params},
								async : false,
								url: root + "/ps.action",scope:this,
								success: function( res, ops){
									var data = Ext.decode(res.responseText );
									for(var index=0;index<data.length;index++){
										var arr = data[index];
										if(arr && arr.length >0){
											var item = arr[0];
											this.paramComboDataMap[item.item_key] = arr;
										}
									}
									for(var index=0;index<records.length;index++){
										var record = records[index];
										var text = '';
										if(record.get('input_type') =='paramcombo'){
											var arr = this.paramComboDataMap[record.get("param_name")] ||[];
											for(var idx =0;idx<arr.length;idx++){
												var item=arr[idx];
												if(item && item.item_value == record.get('default_value')){
													text = item.item_name;
													break;
												}
											}
										}else{
											text = record.get('default_value');
										}
										
										record.set('default_value_text',text); 
									}
								}
							});
						}
		        	}
		        }
							
		});
		
//		//复选框列CheckColumn
//		var checkColumn = new Ext.grid.CheckColumn({
//			       header: '主键',
//			       dataIndex: 'PK',
//			       width: 55
//	    });
		
	    //ColumnModel
		this.cm = new Ext.grid.ColumnModel({
	        defaults: {
	            sortable: false          
	        },
	        columns: [
	        	{header: '字段名',dataIndex:'col_name_text',menuDisabled : true,width : 80,
	                editor : new Ext.form.ComboBox({
							store: this.columnStore,
							emptyText: '请选择',
							allowBlank : false,
							selectOnFocus:true,
							mode: 'local',
							editable : false,
							valueField: 'text',
							displayField: 'text'
					})
	            }, {
	                header:'显示名',
	                dataIndex:'attribute_name',
	                menuDisabled : true,
	                width : 80,
	                editor: new Ext.form.TextField({
	                    allowBlank: false
	                })
	            }, {
	                header:'位置',
	                dataIndex:'attribute_order',
	                width : 60,
	                menuDisabled : true,
	                editor: new Ext.form.NumberField({
	                    allowBlank: false,
	                    allowNegative: false
	                })
	                
	            },{
	                header:'允许空',
	                dataIndex:'is_null_text',
	                width : 80,
	                menuDisabled : true,
	                editor : new Ext.form.ComboBox({
							store: this.nullableStore,
							emptyText: '请选择',
							allowBlank : false,
							mode: 'local',
							selectOnFocus:true,
							editable : false,
							valueField: 'item_name',
							displayField: 'item_name'
					})
	                
	            },{
	                header:'允许显示',
	                dataIndex:'is_show_text',
	                width : 80,
	                menuDisabled : true,
	                editor : new Ext.form.ComboBox({
							store: this.showStore,
							emptyText: '请选择',
							allowBlank : false,
							mode: 'local',
							selectOnFocus:true,
							editable : false,
							valueField: 'item_name',
							displayField: 'item_name'
					})
	                
	            },{
	                header:'输入类型',
	                width : 80,
	                dataIndex:'input_type_text',
	                menuDisabled : true,
	                editor : new Ext.form.ComboBox({
							store: this.inputtypeStore,
							emptyText: '请选择',
							mode: 'local',
							selectOnFocus:true,
							editable : true,
							forceSelection : true,
							triggerAction : 'all',
							valueField: 'item_name',
							displayField: 'item_name'
					})
	                
	            },{
	                header:'下拉框参数值',
	                width : 100,
	                dataIndex:'param_name_text',
	                menuDisabled : true,
	                editor : new Ext.form.ComboBox({
							store: this.paramStore,
							emptyText: '请选择',
							mode: 'local',
							boxMinWidth : 150,
							selectOnFocus:true,
							editable : true,
							forceSelection : true,
							triggerAction : 'all',
							valueField: 'item_desc',
							displayField: 'item_desc'
					})
	            },{
	                header:'组名',
	                dataIndex:'group_name',
	                width : 80,
	                menuDisabled : true,
	                editor : new Ext.form.ComboBox({
							store: this.groupStore,
							mode: 'local',
							selectOnFocus:true,
							editable : false,
							valueField: 'group_name',
							displayField: 'group_name'
					})
	            },{
	                header:'默认值',
	                dataIndex:'default_value_text',
	                width : 80,
	                menuDisabled : true,editor : {}
	            }
	            ,{header: '操作',width : 60,dataIndex: 'attribute_id',
		            renderer:function( v , md, record , i  ){
						return "<a href='#' onclick=Ext.getCmp(\'"+"ExtensionGrid"+"\').deleteRow(); style='color:blue'> 删除 </a>";
					}
				}
	            
	        ]
	    });
		
		ExtensionGrid.superclass.constructor.call(this,{
			id : 'ExtensionGrid',
			store : this.attributeStore,
	        cm: this.cm,
	        enableColumnMove : false,
	        forceValidation: true,
	        clicksToEdit: 1,
//	        viewConfig : {
//	        	forceFit : true
//	        },
	        tbar: [' ',{
            	text:'增加字段',
            	id : 'addButton',
            	iconCls : 'icon-add',
            	scope : this,
            	disabled : true,
            	handler : this.addRecord
            }]
		})
	},
	initEvents : function(){
		this.on('beforeedit',this.beforeEdit,this);
		this.on("afteredit", this.afterEdit, this);
		ExtensionGrid.superclass.initEvents.call(this);
	},
	beforeEdit : function(obj){
		if(obj.field == 'param_name_text'){
			if(obj.record.get('input_type') != 'paramcombo'){
				return false;
			}
		}
		if(obj.field == 'default_value_text'){
			var data = obj.record.data;
			var inputType = data.input_type;
			if(inputType =='textarea'){
				if(!this.defValTextAreaEditor){
					this.defValTextAreaEditor = new Ext.form.TextArea();
				}
				obj.grid.colModel.columns[8].editor= this.defValTextAreaEditor;
			}else if(inputType =='paramcombo'){
				if(!this.comboEditor){
					this.comboEditor = new Ext.form.ComboBox({
								store: new Ext.data.JsonStore({fields : ['item_value','item_name'],data:[]}),
								emptyText: '请选择',
								mode: 'local',
								boxMinWidth : 50,
								selectOnFocus:true,
								editable : false,
								forceSelection : true,
								triggerAction : 'all',
								valueField: 'item_name',
								displayField: 'item_name'
						})
				}
				var arr = this.paramComboDataMap[data.param_name];
				if(arr && arr.length>0){
					this.comboEditor.store.loadData(arr);
					obj.grid.colModel.columns[8].editor = this.comboEditor;
				}else{
					var param = [];
					if(Ext.isEmpty(data.param_name)){
						Alert('请选择下拉框参数值!');
						return;
					}
					param.push(data.param_name);
					Ext.Ajax.request({
						params: {comboParamNames: param},
						async : false,
						url: root + "/ps.action",scope:this,
						success: function( res, ops){
							var data = Ext.decode(res.responseText );
							for(var index=0;index<data.length;index++){
								var arr = data[index];
								if(arr && arr.length >0){
									var item = arr[0];
									this.paramComboDataMap[item.item_key] = arr;
								}
							}
							this.comboEditor.store.loadData(arr);
							obj.grid.colModel.columns[8].editor = this.comboEditor;
						}
					})
					
					
				}
			}else{
				if(!this.defValTextEditor){
					this.defValTextEditor = new Ext.form.TextField();
				}
				obj.grid.colModel.columns[8].editor= this.defValTextEditor;
			}
		}
	},
	afterEdit : function(obj){
		if(obj.field =="group_name"){
	    	var index = this.groupStore.find('group_name',obj.value);
	    	//修改record对应的id
	    	obj.record.data.group_id = this.groupStore.getAt(index).get('group_id');
	    }else if(obj.field =="input_type_text"){
	    	var index = this.inputtypeStore.find('item_name',obj.value);
	    	obj.record.data.input_type = this.inputtypeStore.getAt(index).get('item_value');
	    	if(obj.record.get('input_type') != 'paramcombo'){
				obj.record.set('param_name',''); 
				obj.record.set('param_name_text',''); 
			}
	    }else if(obj.field =="is_null_text"){
	    	var index = this.nullableStore.find('item_name',obj.value);
	    	obj.record.data.is_null = this.nullableStore.getAt(index).get('item_value');
	    }else if(obj.field =="is_show_text"){
	    	var index = this.showStore.find('item_name',obj.value);
	    	obj.record.data.is_show = this.showStore.getAt(index).get('item_value');
	    }else if(obj.field == 'param_name_text'){
	    	if(obj.value){
	    		var index = this.paramStore.find('item_desc',obj.value);
	    		obj.record.set('param_name',this.paramStore.getAt(index).get('item_key')); 
	    	}
	    }else if(obj.field =="col_name_text"){
	    	var index = this.columnStore.find('text',obj.value);
	    	obj.record.data.col_name = this.columnStore.getAt(index).get('value');
	    }else if(obj.field =="default_value_text"){
	    	var data = obj.record.data;
			if(data.input_type == "paramcombo"){
				var arr = this.paramComboDataMap[data.param_name] ||[];
				for(var index =0;index<arr.length;index++){
					var item = arr[index];
					if(item.item_name == obj.value){
						obj.record.set('default_value',item.item_value); 
					}
				}
			}else{
				obj.record.set('default_value',obj.value); 
			}
	    }
	},
	resetCol : function(extensionId){//重置显示的列
		if(extensionId == '1'){
    		this.getColumnModel().setHidden(0,false);
    		this.getColumnModel().setEditable(0,true);
    		
    		this.getColumnModel().setHidden(7,false);
    		this.getColumnModel().setEditable(7,true);
    		this.groupStore.loadData(this.custExtGroup);
    	}else if(extensionId == '2'){
    		this.getColumnModel().setHidden(0,false);
    		this.getColumnModel().setEditable(0,true);
    		
    		this.getColumnModel().setHidden(7,false);
    		this.getColumnModel().setEditable(7,true);
    		this.groupStore.loadData(this.userExtGroup);
    	}else if(extensionId == '3'){
    		this.getColumnModel().setHidden(7,true);
    		this.getColumnModel().setEditable(7,false);
    	}else{
    		this.getColumnModel().setHidden(0,true);
    		this.getColumnModel().setEditable(0,false);
    		
    		this.getColumnModel().setHidden(7,true);
    		this.getColumnModel().setEditable(7,false);
    	}
    	
//    	//重新加载后deleteAttriID重置
//    	this.deleteAttriID="";
//    	this.isModified = false;
	},
	addRecord : function(){//增加字段
		var Plant = this.getStore().recordType;
		var p = new Plant({
            extend_id: '',
            attribute_id: '',
            attribute_name: '',
            attribute_order: 0,
            col_name: '',
            group_name: '',
            group_id :'',
            input_type_text:'',
            input_type: '',
            is_null: '',
            is_null_text:'',
            is_show: '',
            is_show_text: '',
            busi_code:'',
            param_name_text : '',
            default_value:'',
            default_value_text : ''
        });
        this.stopEditing();
        this.getStore().insert(this.getStore().getCount(), p);
        this.startEditing(this.getStore().getCount()-1, 0);
	},
	checkColumnValid : function(){//字段名不能重复
		var flag = false;
    	if(this.extendType == "TABLE"){
    		for(var i=1;i<this.attributeStore.getCount();i++){//第二个开始依次向前进行比较
    			for(var k=i;k>0;k--){
    				var atRec = this.attributeStore.getAt(i);
    				var atRecPre = this.attributeStore.getAt(k-1);
    				if(atRec.get('col_name') == atRecPre.get('col_name') && atRec.get('group_id') == atRecPre.get('group_id') ){
	    				flag = true;
	    				break;
    				}
    			}
    		}
    	}
    	return flag;
	},
	checkOrderValid : function(){//验证显示位置
		var flag = false;
		for(var i=1;i<this.attributeStore.getCount();i++){//第二个开始依次向前进行比较
			for(var k=i;k>0;k--){
				if(this.attributeStore.getAt(i).get('attribute_order') == this.attributeStore.getAt(k-1).get('attribute_order')
					&& this.attributeStore.getAt(i).get('group_id') == this.attributeStore.getAt(k-1).get('group_id')){
    				flag = true;
    				break;
				}
			}
		}
		return flag;
	},
	checkParamValid : function(){//输入类型选择下拉框必须选择参数
		var flag = false;
    	for(var i=0;i<this.attributeStore.getCount();i++){
			if(this.attributeStore.getAt(i).get('input_type_text') == "下拉框" && this.attributeStore.getAt(i).get('param_name') == null){
    			flag = true;
    			break;
			}
    	}
    	return flag;
	},
	checkValid : function(){//验证是否输入完整
		var attributeStore = this.attributeStore;
		var flag = false;
    	for(var i=0;i<attributeStore.getCount();i++){
    		if(attributeStore.getAt(i).get('PK') == null || !attributeStore.getAt(i).get('PK')){
    			if(attributeStore.getAt(i).get('attribute_name') == null || attributeStore.getAt(i).get('attribute_name').trim().length == 0 ||
	    			attributeStore.getAt(i).get('attribute_order') == null  ||attributeStore.getAt(i).get('is_show') == null  ||
	    			attributeStore.getAt(i).get('input_type') == null || attributeStore.getAt(i).get('is_null') == null ){
	    			flag = true;
	    			break;
    			}
    		}
    		if(this.extendType == "TABLE"){
    			if(attributeStore.getAt(i).get('col_name').length == 0){
	    			flag = true;
	    			break;
    			}
    		}
    		
    	}
    	return flag;
	},
	search : function(extensionId,extendType,extendValue,groupId){
		this.extensionId = extensionId;
		this.extendType = extendType;
		this.extendValue = extendValue;
	    
		this.resetCol(extensionId);
		
		//加载数据
		this.attributeStore.removeAll();
		this.attributeStore.load({
					params : {
						extensionId: extensionId,
						extendType : extendType,
						groupId : groupId
					}
				});
		Ext.getCmp('addButton').setDisabled(false);
		Ext.getCmp('saveExtension').setDisabled(false);
	},
	deleteRow : function(){
		var record = this.getSelectionModel().getSelected();
		if(record.get('attribute_id')){
			Alert('原有配置不允许删除');
		}else{
			Confirm("确定删除吗?", this ,function(){
				var record= this.getSelectionModel().getSelected();
				this.getStore().remove(record);
			})
		}
	}
});

/**
 * 扩展属性配置管理
 * @class ExtensionMng
 * @extends Ext.Panel
 */
ExtensionMng = Ext.extend(Ext.Panel,{
	grid : null,
	constructor : function(){
		this.grid = new ExtensionGrid(); 
		ExtensionMng.superclass.constructor.call(this,{
			title :　'扩展属性配置管理',
			region : 'center',
			layout : 'fit',
			items : [this.grid],
			buttons : [{
            	text:'保存',
            	id : 'saveExtension',
            	disabled : true,
            	iconCls : 'icon-save',
            	scope : this,
            	handler : this.doSave
            }]
			
		});
	},
	doSave : function(){
		
		//确保所有数据不为空
		if(this.grid.checkValid(this.grid.attributeStore))
		{
			Alert("数据请编辑完整");
			return;
		}
	
		
		if(this.grid.checkColumnValid(this.grid.attributeStore)){
			Alert("字段名和组名的组合不能重复");
			return;
		}
		if(this.grid.checkOrderValid()){
			Alert('显示位置不能重复');
			return;
		}
		
		if(this.grid.checkParamValid(this.grid.attributeStore)){
			Alert("输入类型为下拉框时，必须选择参数");
			return;
		}
		Confirm('确定保存吗',this,function(){
			mb = Show();//显示正在提交
		
			var data = [];
			var count = this.grid.attributeStore.getCount();
			for(var k=0;k<count;k++){
				data.push(this.grid.attributeStore.getAt(k).data);
			}
			var records = Ext.encode(data);
			all = {};
			all['extendValue'] = this.grid.extendValue;
			all['extendType'] = this.grid.extendType;
			all['extensionId'] = this.grid.extensionId;
			all['records'] = records;
//			all['delAttributeIds'] = this.grid.deleteAttriID;
//			if(this.grid.PKRowIndex != -1){
//				all['extendTablePk'] = this.grid.attributeStore.getAt(this.grid.PKRowIndex).get('col_name');
//			}
			
			//保存数据
			Ext.Ajax.request({
				scope : this,
				url  : root + '/config/ExtendTable!saveAttribute.action' ,
				params: all,
				success:function( res,ops ){
					mb.hide();//隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
					}else{
						Alert('操作失败');
			 		}
					this.grid.attributeStore.reload();
					if(!this.grid.attributeStore.getAt(0).get('extend_id')){
						Ext.getCmp('ExtensionTree').getRootNode().reload();
					}
				},
				failure:function(){
					Alert('错误','请与后台服务人员联系');
				}
			}); 
		});
	}
})

/**
 * 扩展属性菜单树
 * @class ExtensionTree
 * @extends Ext.tree.TreePanel
 */
ExtensionTree = Ext.extend(Ext.ux.FilterTreePanel,{
	searchFieldWidth: 140,
	constructor : function(){
		
		var loader = new Ext.tree.TreeLoader({
			dataUrl  : root + '/config/ExtendTable!queryExtensionTree.action' 
		});
		
		ExtensionTree.superclass.constructor.call(this,{
			id : 'ExtensionTree',
			split : true,
			region : 'west',
			bodyStyle	:'padding:3px',
			width : '30%',
			autoScroll	:true,
	        collapseMode:'mini',
	        loader : loader,
	        root: {
				id 		: '0',
				iconCls : 'x-tree-root-icon',
				nodeType:'async',
				text: '扩展类型'
			},
		    listeners : {
		    	scope : this ,
		    	click : function(n,e){
		    		if(n.parentNode){//存在父节点
		    			if(n.attributes.others.attr != 'T'){//不是分组节点
		    				if(n.parentNode.attributes.others){
		    					var extendType = n.parentNode.attributes.others.attr;
		    					var extendValue = n.attributes.others.attr;
		    					
		    					if(n.id != Ext.getCmp('ExtensionGrid').extensionId){
		    						Ext.getCmp('ExtensionGrid').search(n.id,extendType,extendValue);
		    					}
			    			}
		    			}
//		    			else{//分组节点
//		    				var extendValue = n.parentNode.attributes.others.attr;
//		    				var extendType = n.parentNode.parentNode.attributes.others.attr;
//		    				var id = n.id + '';
//		    				var groupId = id.substring(id.indexOf('_')+1,id.length);
//		    				Ext.getCmp('ExtensionGrid').search(n.parentNode.id,extendType,extendValue,groupId);
//		    			}
		    			
		    		}
		    	}
		    }
		})
		this.getRootNode().expand();
	}
});
    
ExtensionView = Ext.extend(Ext.Panel,{
	constructor : function(){
		ExtensionView.superclass.constructor.call(this,{
			id : 'ExtensionView',
			layout : 'border',
			title : '扩展属性配置',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			items : [new ExtensionTree(),new ExtensionMng()]
		})
	}
})   
