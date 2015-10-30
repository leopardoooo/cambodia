
Ext.ns('Template');

/**
 * 模板菜单树
 * @class TemplateTree
 * @extends Ext.tree.TreePanel
 */
TemplateTree = Ext.extend(Ext.tree.TreePanel,{
	rootNode : null,
	treeEditor : null,
	changeCountyIds: null,	//操作员切换县市权限
	constructor : function(){
		var thiz = this;
		var loader = new Ext.tree.TreeLoader({
			dataUrl  : root + '/config/Template!queryTemplateTree.action',
			listeners:{
				load: function(treeLoader, node, response){
					var data = Ext.decode(response.responseText);
					var list = [];
					var rawDataList = data.treeList || [];
					var optr = App.getApp().data.optr;
					if(optr.dept_id != Constant.COUNTY_ALL){//非省公司的操作员屏蔽 发票打印，计费，信息修改
						var arr = ['INVOICE_type','BILLING_type','UPDPROP_type'];
						for(var index =0;index<rawDataList.length;index++){
							var item = rawDataList[index];
							var attr = item.others.attr;
							if(!arr.contain(attr)){
								list.push(item);
							}
						}
					}else{
						list = rawDataList;
					}
					thiz.getRootNode().appendChild(list);
					thiz.changeCountyIds = data.changeCountyIds;
					thiz = null;
				}
			}
		});
		TemplateTree.superclass.constructor.call(this,{
			id : 'TemplateTree',
			onlyLeafCheckable : true,
			checkModel : 'single',
			split : true,
			region : 'west',
			bodyStyle	:'padding:3px',
			width : '20%',
			autoScroll	:true,
	        collapseMode:'mini',
	        loader : loader,
	        tbar : [{
	        	text : '增加模板',
	        	iconCls : 'icon-add',
	        	id : 'addTemplate',
	        	disabled : true,
	        	scope : this,
	        	handler : this.addTemplate
	        }],
		    root: {
				id 		: '0',
				iconCls : 'x-tree-root-icon',
				nodeType:'async',
				expanded : true,
				text: '模板配置'
			},
		    listeners : {
		    	scope : this,
		    	click : function(n,e){
		    		if(!Ext.getCmp('saveTemplate').disabled){
		    			Confirm('当前模板配置尚未保存，是否离开？',this,function(){
		    				this.doClick(n);
		    			},function(){
		    				//选中当前编辑模板节点
		    				var path = Ext.getCmp('TemplateMng').node.getPath();
		    				this.selectPath(path);
		    			})
		    		}else{
		    			this.doClick(n);
		    		}
		    	},
		    	beforestartedit: this.beforeEdit,
		    	beforecomplete: this.modifyTemplate
		    }
		});
		
		this.treeEditor = new Ext.tree.TreeEditor(this,{
			allowBlank : false,
			cancelOnEsc : true
		});
	},
	doClick : function(n){
		if(n.parentNode && n.attributes.others.attr.indexOf("type") == -1){//模板节点
			var arr = n.attributes.others.attr.split("_");
			var templateId = arr[0];
			if(Ext.getCmp('TemplateMng').templateId != templateId){
				Ext.getCmp('TemplateMng').isEdit = true;
				Ext.getCmp('TemplateMng').addItems(n);
			}
			
			Ext.getCmp('addTemplate').setDisabled(true);
		}else if(n.parentNode && n.attributes.others.attr.indexOf("type") > -1){//模板类型节点
			if(Ext.getCmp('TemplateMng').isBtnEdit)
				Ext.getCmp('addTemplate').setDisabled(false);
		}else{//根节点
			Ext.getCmp('addTemplate').setDisabled(true);
		}
	},
	beforeEdit : function(editor){
		var n = editor.editNode;
		//模板节点才允许编辑,模板类型和根节点不允许
		if(n.parentNode && n.attributes.others.attr.indexOf("type") == -1){
			return true;
		}else{
			return false;
		}
	},
	modifyTemplate : function(editor,newValue,oldValue){
		if(newValue != oldValue){
			Confirm("确定修改吗?", this ,function(){
				var all = {};
				all['templateId'] = editor.editNode.attributes.others.attr;
				all['templateName'] = editor.editNode.text;
				Ext.Ajax.request({
					scope : this,
					url :root + '/config/Template!editTemplate.action',
					params : all,
					success : function(res,opt){
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
						}else{
							Alert('操作失败');
				 		}
				 		return true;
					}
				})
			},function(){
				editor.editNode.setText(oldValue);
			})
		}
	},
	addTemplate : function(){
		var node = this.getSelectionModel().getSelectedNode();
		new TemplateWin(node).show();
	},
	deleteTemplate :function(){
		Confirm("确定删除吗?", this ,function(){
			Ext.Ajax.request({
				scope : this,
				url :root + '/config/Template!deleteTemplate.action',
				params : {
					templateId : this.getSelectionModel().getSelectedNode().attributes.others.attr
				},
				success : function(res,opt){
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						var node = this.getSelectionModel().getSelectedNode();
						var parentNode = node.parentNode;
						if(parentNode.childNodes.length == 1){
							node.remove();
							parentNode.leaf = true;
							parentNode.attributes.leaf = true;
						}else{
							node.remove();
						}
						Ext.getCmp('deleteTemplate').setDisabled(true);
						Ext.getCmp('TemplateMng').resetPanel();
					}else{
						Alert('操作失败');
			 		}
				}
			})
		});
	}
});

/**
 * 模板增加窗口
 * @class TemplateWin
 * @extends Ext.Window
 */
TemplateWin = Ext.extend(Ext.Window,{
	form : null,
	node : null,
	templateType : null,
	oldTemplateStore : null,//供新模板复制的模板数据
	constructor : function(node){
		//操作节点
		this.node = node;
		//模板类型
		var templateType = node.attributes.others.attr;
		this.templateType = templateType.substring(0,templateType.indexOf('_type'))
		
		this.oldTemplateStore = new Ext.data.JsonStore({
			url :root + '/config/Template!queryTplsByType.action',
			fields : ['template_id','template_name']
		})
		this.oldTemplateStore.load({
			params : {
				templateType : this.templateType
			}
		})
		
		var items = [
				{xtype : 'textfield',fieldLabel : '模板名称',id : 'templateName',
					allowBlank :false,blankText : '请输入模板名称'
				},
				{xtype : 'combo',id :'copyTemplateId',fieldLabel : '复制模板于',store : this.oldTemplateStore,
				emptyText : '请选择...',mode: 'local',forceSelection : true,triggerAction : 'all',
				displayField : 'template_name',valueField : 'template_id',editable : false
			}];
		if(this.templateType == 'CONFIG'){
			items[1].allowBlank = false;
		}
			
		this.form = new Ext.form.FormPanel({
			layout : 'form',
			border : false,
			labelWidth : 85,
			bodyStyle : 'padding : 5px;padding-top : 10px;',
			items : items
		});
		TemplateWin.superclass.constructor.call(this,{
			title : '增加模板',
			layout : 'fit',
			height : 250,
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
	initEvents : function(){
		this.oldTemplateStore.on('load',this.onLoad,this);
		TemplateWin.superclass.initEvents.call(this);
	},
	onLoad : function(store,records){
		//插入一条空数据
		var Plant= store.recordType;
		var p = new Plant({
			'template_id': '','template_name': '请选择...'
		});
		store.insert(0,p);
		if(this.templateType == 'CONFIG'){
			if(store.getCount() == 1){
				Alert('基础数据t_config_template没导入');
				this.close();
				return;
			}
			Ext.getCmp('copyTemplateId').setValue(store.getAt(1).get('template_id'));
		}
	},
	doSave : function(){
		if(this.form.getForm().isValid()){
			Confirm("确定保存吗?", this ,function(){
				var templateName = Ext.getCmp('templateName').getValue();
				var copyTemplateId = Ext.getCmp('copyTemplateId').getValue();
				
				Ext.Ajax.request({
					url :root + '/config/Template!createTemplate.action',
					params : {
						templateName : templateName,
						templateType : this.templateType,
						copyTemplateId : copyTemplateId
					},
					scope : this,
					success : function(res,opt){
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							var node = Ext.getCmp('TemplateTree').getNodeById(this.node.id);
							node.leaf = false;
							node.attributes.leaf = false;
							node.appendChild(new Ext.tree.TreeNode({
								id : rs.simpleObj.template_id+"_"+node.id,
								text : rs.simpleObj.template_name,
								others : {
									attr : rs.simpleObj.template_id
								}
							}))
							this.close();
						}else{
							Alert('操作失败');
				 		}
					}
				})
			})
		}
	}
})

/**
 * 公共模板配置表格，提供共同需要的方法
 * @class ComTemplateGrid
 * @extends Ext.grid.EditorGridPanel
 */
ComTemplateGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	templateId : null,
	busiCodeStore : null,
	constructor : function(cfg){
		ComTemplateGrid.superclass.constructor.call(this,cfg);
	},
	initEvents : function(){
		this.on("afteredit", this.afterEdit, this);
		this.on('beforeedit',this.beforeEdit,this);
		ComTemplateGrid.superclass.initEvents.call(this);
	},
	beforeEdit : function(obj){
		if(Ext.getCmp('TemplateMng').isEdit === false){
			return false;
		}
	},
	afterEdit : function(obj){//编辑完业务名称后设置相应的业务代码
		if(obj.field == 'busi_name'){
			var index = this.busiCodeStore.find('busi_name',obj.value);
			obj.record.data.busi_code = this.busiCodeStore.getAt(index).get('busi_code');
		}
	},
	checkValid : function(){//验证配置不能重复,且不允许出现单元为空
		if(this.getStore().getCount() > 0){//检验第一条数据是否输入完整
			var rec = this.getStore().getAt(0).data;
			for(var key in rec){
				if(key !='config_value_text' && key != 'form_type'){
					if(Ext.isEmpty(rec[key])){
						return "请编辑完所有数据";
					}
				}
			}
		}
		//检验第一条以后的数据是否输入完整以及是否存在两个配置相同
		for(var i=1;i<this.getStore().getCount();i++){//第二个开始依次向前进行比较
			for(var key in this.getStore().getAt(i).data){
				if(key !='config_value_text' && key != 'form_type'){
					if(Ext.isEmpty(this.getStore().getAt(i).data[key])){//检验第一条以后的数据是否输入完整
						return "请编辑完所有数据";
					}
				}
			}
			for(var k=i;k>0;k--){
				var flag = true;//默认两个对象的属性值相同
				for(var key in this.getStore().getAt(i).data){
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
	},
	addRecord : function(rec){
		var Plant= this.getStore().recordType;
		var p = new Plant(rec);
		this.stopEditing();
		this.getStore().insert(0,p);
		this.startEditing(0,0);
	},
	refreshGrid : function(templateId,templateType){
		this.templateId = templateId;
		this.getStore().load({
			params : {
				templateId : templateId,
				templateType :templateType
			}
		});
	},
	deleteRow : function(){//删除行
		Confirm("确定要删除该数据吗?", this ,function(){
			if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
				Ext.getCmp('saveTemplate').setDisabled(false);//激活保存按钮
			}else{
				Ext.getCmp('saveTemplate').setDisabled(true);
			}
			var record= this.getSelectionModel().getSelected();
			this.getStore().remove(record);
		});
	}
})

/**
 * 费用模板可编辑表
 * @class TemplateGrid
 * @extends Ext.grid.EditorGridPanel
 */
FeeTemplateGrid = Ext.extend(Ext.grid.GridPanel,{
	feeTplStore : null,
	templateId : null,
	columnList: null,
	columnFeeStd: {},
	constructor : function(busiCodeStore){
		this.feeTplStore = new Ext.data.JsonStore({
			fields : ['template_id', 'fee_std_id','fee_id', 'fee_name','min_value','max_value','default_value',
				'fee_type','device_buy_mode','device_type','device_model','optr_id','optr_name',
				'device_type_text',"device_buy_mode_text",'device_model_text'],
			sortInfo : {
				field:'fee_type',
				direction:'ASC'
			}
		});
		var currentOptrId = App.getData().optr['optr_id'];
		var cm = new Ext.grid.ColumnModel({
	        columns: [
	        	{dataIndex : 'template_id',hidden : true},
	        	{header : '费用名称',dataIndex : 'fee_name',width:85},
	        	{header : '最小收费值',dataIndex : 'min_value',width:75,renderer : Ext.util.Format.formatFee},
	        	{header : '最大收费值',dataIndex : 'max_value',width:75,renderer : Ext.util.Format.formatFee},
	        	{header : '默认收费值',dataIndex : 'default_value',width:75,renderer : Ext.util.Format.formatFee},
	        	{header : '购买方式',dataIndex : 'device_buy_mode_text',width:70},
	        	{header : '设备类型',dataIndex : 'device_type_text',width:70},
	        	{header : '设备型号',dataIndex : 'device_model_text',width:150,renderer:App.qtipValue},
	        	{header : '创建人',dataIndex : 'optr_name',width:75,renderer:App.qtipValue},
	        	{header: '操作',dataIndex: 'template_id',scope:this,
		            renderer:function(value,meta,record){
		            	var text = "";
		            	if(record.get('device_buy_mode') == 'UPGRADE'){
		            		return text;
		            	}
		            	if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
		            		text = "<a href='#' onclick=Ext.getCmp(\'"+"FeeTemplateGrid"+"\').modifyRec(); style='color:blue'> 修改 </a>"
		            			+"&nbsp;<a href='#' onclick=Ext.getCmp(\'"+"FeeTemplateGrid"+"\').deleteRec(); style='color:blue'> 删除 </a>"
		            	}else if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === false
		            		&& this.columnFeeStd[record.get('fee_std_id')]){	//有费用模板限制权限，没有编辑模板权限，包含适用分公司，一样可以修改
		            			text = "<a href='#' onclick=Ext.getCmp(\'"+"FeeTemplateGrid"+"\').modifyRec(); style='color:blue'> 修改 </a>";
		            		}
		            	return text;
					}
			}]
		});
		
		FeeTemplateGrid.superclass.constructor.call(this,{
			id : 'FeeTemplateGrid',
			border:false,
			forceValidation: true,
	        store : this.feeTplStore,
	        enableColumnMove : false,
	        cm: cm,
			tbar: [' 业务费用配置','->',{
            	text:'增加配置',id : 'addButton',pressed : true,
            	disabled : !Ext.getCmp('TemplateMng').isEdit || !Ext.getCmp('TemplateMng').isBtnEdit,
            	iconCls : 'icon-add',scope : this,handler : this.addRecord
            },'   ']
		});
	},
	refreshGrid : function(templateId){
		this.templateId = templateId;
		Ext.Ajax.request({
			url : root + '/config/Template!queryFeeTpls.action',
			params: {templateId : templateId},
			scope: this,
			success: function(res){
				var data = Ext.decode(res.responseText);
				if(data){
					this.columnList = data.columnList;
					Ext.each(this.columnList, function(column){
						if(!this.columnFeeStd[column['fee_std_id']]){
							var arr = [];
							arr.push(column);
							this.columnFeeStd[column['fee_std_id']] = arr;
						}else{
							this.columnFeeStd[column['fee_std_id']].push(column);
						}
					},this);
					this.getStore().loadData(data.feeStbList);
				}
			}
		});
		
	},
	addRecord : function(){
		var countyIds = Ext.getCmp('CountyPanel').getValues();
		if(countyIds.length>0){
			var win = new FeeTemplateWin(this.templateId);
			win.setTitle('增加配置');
			win.show();
		}else{
			Alert('请先选择模板适用县市!');
		}
	},
	modifyRec : function(){
		var countyIds = Ext.getCmp('CountyPanel').getValues();
		if(countyIds.length>0){
			var record = this.getSelectionModel().getSelected();
			var win = new FeeTemplateWin(this.templateId,record);
			win.setTitle('修改配置');
			win.show();
		}else{
			Alert('请先选择模板适用县市!');
		}
	},
	deleteRec : function(){
		Confirm("确定删除吗?", this ,function(){
			var mb = Show();//显示正在提交
			
			var feeStdId = this.getSelectionModel().getSelected().get('fee_std_id');
			
			Ext.Ajax.request({
				url : root + '/config/Template!deleteFeeConfig.action',
				params : {
					feeStdId : feeStdId	
				},
				scope : this,
				success : function(res,opt){
					mb.hide();//隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						Ext.getCmp('FeeTemplateGrid').refreshGrid(this.templateId);
					}else{
						Alert('操作失败');
			 		}
				}
			})
		})
	}
});
FeeTemplateWin = Ext.extend(Ext.Window,{
	form : null,
	devicePanel : null,
	record : null,
	templateId : null,
	countys : null,
	constructor : function(templateId,record){
		this.templateId = templateId;
		this.record = record;
		
		var countyIds = Ext.getCmp('CountyPanel').getValues();
		countyIds = "'"+countyIds.join("','")+"'";
		var feeStore = new Ext.data.JsonStore({
			autoLoad : true,
			baseParams : {
				templateId : templateId,
				countyIds: countyIds
			}, 
			url : root + '/config/Template!queryBusiFeeForStdCfg.action',
			fields : ['fee_id','fee_name','fee_type','county_id','county_name'],
			listeners:{
				scope:this,
				load:function(store){
					store.each(function(r){
						r.set('fee_name',r.get('fee_name')+"("+r.get('county_name')+")");
						r.commit();
					});
				}
			}
		});
		
		//设备类型费用的相关面板
		this.devicePanel = {
				id : 'devicePanel',xtype : 'panel',border: false,
				baseCls: 'x-plain',anchor : '95%',layout : 'form',labelWidth: 100,
				items : [{
					id : 'deviceBuyModeForTemplate',xtype:'paramcombo',width:150,editable:false,
					allowBlank:false,hiddenName:'device_buy_mode',defaultValue:'BUY',
					paramName:'BUSI_BUY_MODE',fieldLabel : '购买方式',
					listeners : {
						scope : this,
						select : this.doSelectDevice
					}
				},{
					id : 'deviceTypeForTemplate',fieldLabel:'设备类型',xtype:'paramcombo',width:150,editable:false,
					allowBlank:false,hiddenName:'device_type',paramName:'ALL_DEVICE_TYPE',defaultValue : 'STB',
					listeners : {
						scope : this,
						select : this.doSelectDevice
					}
				}]
			};
			
		this.form = new Ext.form.FormPanel({
			layout : 'form',
			border : false,
			labelWidth : 100,
			bodyStyle : 'padding : 5px;padding-top : 10px;',
			items : [
				{xtype : 'hidden',id : 'fee_std_id',name : 'fee_std_id'},
				{xtype : 'hidden',name : 'fee_id',id : 'fee_id'},
				{fieldLabel : '费用名称',xtype : 'combo',width : 150,store : feeStore,id : 'fee_name',
					displayField : 'fee_name',valueField : 'fee_id',editable : true,allowBlank : false,
					listWidth:250,forceSelection:true,
					listeners : {
						scope : this,
						select : this.doSelectFee
					}
			},
			{xtype:'numberfield',fieldLabel:'最小收费值',allowNegative:false,
				id:'min_valueForTemplate',name:'min_value',width:150,
				listeners : {
					change : function(txt,newValue,oldValue){
						var cmp = Ext.getCmp('max_valueForTemplate');
						if( cmp.getValue() != 0){
							if(parseFloat(newValue) >  parseFloat(cmp.getValue())){
								Alert('最小收费值不能大于最大收费值');
								txt.setValue(oldValue);
							}
							if(parseFloat(newValue) > parseFloat(Ext.getCmp('default_valueForTemplate').getValue())){
								Ext.getCmp('default_valueForTemplate').setValue(newValue);
							}
						}
					}
				}
			},
			{xtype : 'numberfield',fieldLabel : '最大收费值',id : 'max_valueForTemplate',
				name : 'max_value',width : 150,allowNegative:false,
				listeners : {
					change : function(txt,newValue,oldValue){
						var cmp = Ext.getCmp('min_valueForTemplate');
						if( cmp.getValue() != 0){
							if(parseFloat(newValue) <  parseFloat(cmp.getValue())){
								Alert('最大收费值不能小于最小收费值');
								txt.setValue(oldValue);
							}
						}
					}
				}
			},
			{xtype : 'numberfield',fieldLabel : '默认收费值',allowBlank : false,allowNegative:false,
				id : 'default_valueForTemplate',name : 'default_value',width : 150,
				listeners : {
					change : function(txt,newValue,oldValue){
						var min = Ext.getCmp('min_valueForTemplate').getValue();
						var max = Ext.getCmp('max_valueForTemplate').getValue();
						if( min != 0 ){
							if(parseFloat(newValue) <  parseFloat(min)){
								Alert('不能小于最小收费值');
								txt.setValue(oldValue);
							}
						}
						if(max != 0){
							if(parseFloat(newValue) >  parseFloat(max)){
								Alert('不能大于最大收费值');
								txt.setValue(oldValue);
							}
						}
					}
				}
			}]
		});
		
		FeeTemplateWin.superclass.constructor.call(this,{
			layout : 'fit',
			height : 300,
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
	initComponent : function(){
		if(this.record){
			if (this.record.get('device_type')){
				this.form.add(this.devicePanel);
			}
			Ext.getCmp('fee_name').setReadOnly(true);
			this.doLayout();
		}
		
		if(this.record){
			this.form.getForm().loadRecord(this.record);
			var minValue = Ext.getCmp('min_valueForTemplate');
			var maxValue = Ext.getCmp('max_valueForTemplate');
			var defaultValue = Ext.getCmp('default_valueForTemplate');
			minValue.setValue(Ext.util.Format.formatFee(minValue.getValue()));
			maxValue.setValue(Ext.util.Format.formatFee(maxValue.getValue()));
			defaultValue.setValue(Ext.util.Format.formatFee(defaultValue.getValue()));
		}
		//模板适用分公司数据
			var fieldSets = Ext.getCmp('CountyPanel').items;
			for(var i=0;i<fieldSets.length;i++){
				if(fieldSets.itemAt(i).collapsed){
					fieldSets.itemAt(i).expand();
					fieldSets.itemAt(i).collapse();
				}
			}
			var countyIds = []; 
			var fieldSets = Ext.getCmp('CountyPanel').items;
			for(var i=0;i<fieldSets.length;i++){
				var cbs = fieldSets.itemAt(i).items.itemAt(0).getValue();
				for(var k=0;k<cbs.length;k++){
					countyIds.push(cbs[k].value);
				}
			}
			this.countys = countyIds.join(',');
		
		var compArr = this.form.findByType("paramcombo");
		if(compArr.length > 0){
			App.form.initComboData( this.form.findByType("paramcombo"),this.doInit,this);
		}else{
			if(App.getData().optr['optr_id'] != '101676' 
				&& this.record &&App.getData().optr['optr_id']!= this.record.get('optr_id')){
				this.filterData();
			}
		}
		
		FeeTemplateWin.superclass.initComponent.call(this);
	},
	doInit : function(){
		if(this.record){
			this.form.getForm().loadRecord(this.record);
			var minValue = Ext.getCmp('min_valueForTemplate');
			var maxValue = Ext.getCmp('max_valueForTemplate');
			var defaultValue = Ext.getCmp('default_valueForTemplate');
			minValue.setValue(Ext.util.Format.formatFee(minValue.getValue()));
			maxValue.setValue(Ext.util.Format.formatFee(maxValue.getValue()));
			defaultValue.setValue(Ext.util.Format.formatFee(defaultValue.getValue()));
			
			this.queryModels(this.record.data);//加载设备型号
		}
	},
	doSelectFee : function(combo,rec){//选择费用
		Ext.getCmp('fee_id').setValue(rec.get('fee_id'));
		if(Ext.getCmp('devicePanel')){
			this.items.itemAt(0).remove(Ext.getCmp('devicePanel'),true);
		}
		if(rec.get('fee_type') == 'DEVICE'){
			this.items.itemAt(0).add(this.devicePanel);
			App.form.initComboData( this.form.findByType("paramcombo"));
			
			var obj = {
				device_buy_mode : 'BUY',
				device_type : 'STB'
			};
			this.queryModels(obj);
			
			this.doLayout();
		}else{
			this.setSize(350,300);
		}
	},
	doSelectDevice : function(combo){
		var obj = {
			device_buy_mode : Ext.getCmp('deviceBuyModeForTemplate').getValue(),
			device_type : Ext.getCmp('deviceTypeForTemplate').getValue()
		}
		if(this.record){
			obj.feeStbId = this.record.get('fee_stb_id');
		}
		this.queryModels(obj);
	},
	queryModels : function(record){
		if(Ext.isEmpty(this.countys)){
			Alert('请先选择适用地区!', function() {this.close();}, this);
			return false;
		}
		this.setSize(500,450);
		Ext.Ajax.request({
			scope : this,
			url : root + '/config/Template!qeuryDeviceModelForStdCfg.action',
			params : {
				templateId : this.templateId,
				feeStdId : record.fee_std_id,
				deviceBuyMode : record.device_buy_mode,
				deviceType : record.device_type,
				feeId: Ext.getCmp('fee_id').getValue(),
				countyIds : this.countys 
			},
			success : function(res,opt){
				var models = Ext.decode(res.responseText);
				this.addDeviceMoel(models);
				if(App.getData().optr['optr_id'] != '101676' 
					&& this.record &&App.getData().optr['optr_id']!= this.record.get('optr_id')){
					this.filterData();
				}
			}
		});
	},
	addDeviceMoel : function(models){//生成设备型号复选框组
		var recs = null;
		if(this.record){
			recs = this.record.get('device_model');
		}
		var checkboxs = [];
		for(var i=0;i<models.length;i++){
			var box = {
				boxLabel : models[i].item_name,
				value : models[i].item_value
			};
			if(recs){
				box.checked = recs.split(',').contain(models[i].item_value);
			}
			checkboxs.push(box);
		}
		if(checkboxs.length == 0){
			checkboxs.push({
				boxLabel : '没有相关型号',
				disabled : true
			})
		}
		var deviceModel = {
			xtype: 'checkboxgroup',
			fieldLabel : '设备型号',
			allowBlank : false,
			blankText : '至少选择一项',
			id : 'device_model',
			name: 'device_model',
			columns : 3,
			items : checkboxs
		};
		
		var cmp = Ext.getCmp('device_model');
		if(cmp){
			Ext.getCmp('devicePanel').remove(cmp,true);
		}
		Ext.getCmp('devicePanel').add(deviceModel);
		this.doLayout();
		
	},
	filterData: function(){
		var columnFeeStd = Ext.getCmp('FeeTemplateGrid').columnFeeStd;
		var currentOptrId = App.getData().optr['optr_id'];
		//没有编辑模板权限，有模板权限限制权限，包含分公司权限，按照模板权限限制来
		if(Ext.getCmp('TemplateMng').isBtnEdit === false
			&& Ext.getCmp('TemplateMng').isEdit === true
			&& columnFeeStd ){
			var basicForm = this.form.getForm();
			
			var columnList = columnFeeStd[this.record.get('fee_std_id')];
			if(columnList){
				var feeNameFlag = false, minValueFlag = false, maxValueFlag = false, defaultValueFlag = false, deviceTypeFlag = false, buyModeFlag = false, deviceModelFlag = false;
				for(var i=0,len=columnList.length;i<len;i++){
					var column = columnList[i];
					
					//创建者不受约束，可以随意修改
					if(column['optr_id'] == currentOptrId)	continue;
					
					var field = column['column_name'];
					if(field == 'fee_name') feeNameFlag = true;
					else if(field == 'min_value') minValueFlag = true;
					else if(field == 'max_value') maxValueFlag = true;
					else if(field == 'default_value') defaultValueFlag = true;
					else if(field == 'device_type') deviceTypeFlag = true;
					else if(field == 'device_buy_mode') buyModeFlag = true;
					else if(field == 'device_model') deviceModelFlag = true;
					
					if(column['is_editable'] === 'T'){
						if(column['type'] == 'number'){
							if(column['min_value']) basicForm.findField(field).setMinValue(Ext.util.Format.formatFee(parseFloat(column['min_value'])));
							if(column['max_value']) basicForm.findField(field).setMaxValue(Ext.util.Format.formatFee(parseFloat(column['max_value'])));
							if(column['default_value']) basicForm.findField(field).setValue(Ext.util.Format.formatFee(parseFloat(column['default_value'])));
						}else if(column['type'] == 'string'){
							if(column['item_key'] && column['select_value'] != 'ALL'){
								var selectValue = column['select_value'];
								Ext.Ajax.request({
									url:root + '/ps.action',
									params:{comboParamNames: [column['item_key']]},
									scope:this,
									async:false,	//同步请求
									success:function(res,opt){
										var data = Ext.decode(res.responseText);
										if(data){
											var arr = [];
											Ext.each(data[0], function(d){
												if(selectValue.indexOf(d['item_value']) >= 0){
													arr.push(d);
												}
											});
											
											if(arr.length > 0){
												var comp = basicForm.findField(field);
												if(comp){
													var compDefaultValue = comp.defaultValue;
													comp.getStore().loadData(arr);
													var flag = false;
													Ext.each(arr,function(obj){
														if(obj['item_value'] == compDefaultValue){
															comp.setValue(obj['item_value']);
															flag = true;
															return false;
														}
													});
													if(flag === false){
														comp.setValue(arr[0]['item_value']);
													}
												}
											}
										}
									}
								});
							}
						}
					}else{
						if(field == 'device_model' && Ext.getCmp('device_model')){
							Ext.getCmp('device_model').disable();
						}else{
							basicForm.findField(field).setReadOnly(true);
						}
					}
				}
				if(feeNameFlag === false) basicForm.findField('fee_name').setReadOnly(true);
				if(minValueFlag === false) basicForm.findField('min_value').setReadOnly(true);
				if(maxValueFlag === false) basicForm.findField('max_value').setReadOnly(true);
				if(defaultValueFlag === false) basicForm.findField('default_value').setReadOnly(true);
				if(deviceTypeFlag === false){
					var comp = basicForm.findField('device_type');
					if(comp) comp.setReadOnly(true);
				}
				if(buyModeFlag === false){
					var comp = basicForm.findField('device_buy_mode');
					if(comp) comp.setReadOnly(true);
				}
				if(deviceModelFlag === false){
					var comp = basicForm.findField('device_model');
					if(comp) comp.setReadOnly(true);
					if(Ext.getCmp('device_model'))
						Ext.getCmp('device_model').disable();
				}
			}else{
				basicForm.findField('fee_name').setReadOnly(true);
				basicForm.findField('min_value').setReadOnly(true);
				basicForm.findField('max_value').setReadOnly(true);
				basicForm.findField('default_value').setReadOnly(true);
				var comp = basicForm.findField('device_type');
				if(comp) comp.setReadOnly(true);
				var comp = basicForm.findField('device_buy_mode');
				if(comp) comp.setReadOnly(true);
				var comp = basicForm.findField('device_model');
				if(comp) comp.setReadOnly(true);
				if(Ext.getCmp('device_model'))
					Ext.getCmp('device_model').disable();
			}
		}
	},
	getValues : function(){
		var all = this.form.getForm().getValues(),params={};
		for(var key in all){
			if(key.indexOf('ext') == -1){
				params['busiFeeStd.'+key] = all[key]
			}
		}
		params['busiFeeStd.template_id'] = this.templateId;
		params['busiFeeStd.min_value'] = params['busiFeeStd.min_value'] * 100;
		params['busiFeeStd.max_value'] = params['busiFeeStd.max_value'] * 100;
		params['busiFeeStd.default_value'] = params['busiFeeStd.default_value'] * 100;
		
		var cmp = Ext.getCmp('device_model');
		if(cmp){
			var models = cmp.getValue();
			var deviceModelListStr = [];
			for(var i=0;i<models.length;i++){
				deviceModelListStr.push(models[i].value);
			}
			params['deviceModelListStr'] = deviceModelListStr.join(",");
		}
		return params;
	},
	doSave : function(){
		if(this.form.getForm().isValid()){
			Confirm("确定保存吗?", this ,function(){
				var mb = Show();//显示正在提交
				
				Ext.Ajax.request({
					url : root + '/config/Template!saveFeeConfig.action',
					params : this.getValues(),
					scope : this,
					success : function(res,opt){
						mb.hide();//隐藏提示框
						mb = null;
						var rs = Ext.decode(res.responseText);
						if(true === rs.success){
							Alert('操作成功!');
							Ext.getCmp('FeeTemplateGrid').refreshGrid(this.templateId);
							this.close();
						}else{
							Alert('操作失败');
				 		}
					}
				})
			})
		}
	}
})


/**
 * 业务单据可编辑表
 * @class DocTemplateGrid
 * @extends Ext.grid.EditorGridPanel
 */
DocTemplateGrid = Ext.extend(ComTemplateGrid,{
	docTplStore : null,//业务单据模板数据
	docTypeStore : null,//业务单据类型
	busiCodeStore : null,//业务编号
	constructor : function(busiCodeStore){
		this.busiCodeStore = busiCodeStore;
		//业务单据模板配置数据
		this.docTplStore = new Ext.data.JsonStore({
			url : root + '/config/Template!queryDocTpls.action',
			sortInfo : {
				field : 'busi_code',
				direction : 'ASC'
			},
			fields : ['template_id','busi_code', 'busi_name','doc_type', 'doc_name']
		});
		//业务单据类型
		this.docTypeStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/config/Template!querydoc.action',
			fields : ['doc_type','doc_name']
		})
		
		var cm = new Ext.grid.ColumnModel({
	        defaults: {sortable: false},
	        columns: [
	        	{dataIndex : 'template_id',hidden : true},
	        	{header : '业务名称',dataIndex : 'busi_name',
		        	editor : new Ext.form.ComboBox({
							store: this.busiCodeStore,allowBlank : false,mode: 'local',
							selectOnFocus:true,editable : true,forceSelection : true,triggerAction : 'all',
							valueField: 'busi_name',displayField: 'busi_name'
					})
	        	},
	        	{header : '单据名称',dataIndex : 'doc_name',
		        	editor : new Ext.form.ComboBox({
							store: this.docTypeStore,allowBlank : false,mode: 'local',
							selectOnFocus:true,editable : true,forceSelection : true,triggerAction : 'all',
							valueField: 'doc_name',displayField: 'doc_name'
					})
	        	},
	        	{header: '操作',dataIndex: 'template_id',
		            renderer:function( v , md, record , i  ){
		            	var text = "";
		            	if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
		            		text = "<a href='#' onclick=Ext.getCmp(\'"+"DocTemplateGrid"+"\').deleteRow(); style='color:blue'> 删除 </a>";
		            	}
		            	return text;
					}
			}]
		});
		
		DocTemplateGrid.superclass.constructor.call(this,{
			id : 'DocTemplateGrid',
			forceValidation: true,
	        clicksToEdit: 1,
	        store : this.docTplStore,
	        enableColumnMove : false,
	        cm: cm,
			viewConfig : {
				forceFit : true
			},
			tbar: [' 业务单据配置','->',{
            	text:'增加配置',id : 'addButton',pressed : true,disabled : !Ext.getCmp('TemplateMng').isEdit || !Ext.getCmp('TemplateMng').isBtnEdit,
            	iconCls : 'icon-add',scope : this,handler : this.addRecord
            },'   ']
		});
	},
	afterEdit : function(obj){
		if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
			Ext.getCmp('saveTemplate').setDisabled(false);//激活保存按钮
		}else{
			Ext.getCmp('saveTemplate').setDisabled(true);
		}
		if(obj.field == 'doc_name'){
			var index = this.docTypeStore.find('doc_name',obj.value);
			obj.record.data.doc_type = this.docTypeStore.getAt(index).get('doc_type');
		}
		
		//必须调用父类方法
		DocTemplateGrid.superclass.afterEdit.call(this,obj);
	},
	addRecord : function(){
		var obj = {
			'template_id' : this.templateId,'busi_code' : '','busi_name' : '','doc_type' : '','doc_name' : ''
		}
		//必须调用父类方法
		DocTemplateGrid.superclass.addRecord.call(this,obj);
	}
})

/**
 * 工单可编辑表
 * @class TaskTemplateGrid
 * @extends Ext.grid.EditorGridPanel
 */
TaskTemplateGrid = Ext.extend(ComTemplateGrid,{
	docTplStore : null,//业务单据模板数据
	docTypeStore : null,//业务单据类型
	busiCodeStore : null,//业务编号
	constructor : function(busiCodeStore){
		this.busiCodeStore = busiCodeStore;

		//工单模板配置数据
		this.taskTplStore = new Ext.data.JsonStore({
			url : root + '/config/Template!queryTaskTpls.action',
			sortInfo : {
				field : 'busi_code',
				direction : 'ASC'
			},
			fields : ['template_id','busi_code','busi_name','detail_type_id', 'detail_type_name']
		});
		//工单类型
		this.taskDetailTypeStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/config/Template!querytask.action',
			fields : ['detail_type_id', 'detail_type_name']
		})
		
		var cm = new Ext.grid.ColumnModel({
	        defaults: {sortable: false},
	        columns: [
	        	{dataIndex : 'template_id',hidden : true},
	        	{header : '业务名称',dataIndex : 'busi_name',
		        	editor : new Ext.form.ComboBox({
							store: this.busiCodeStore,allowBlank : false,mode: 'local',
							selectOnFocus:true,editable : true,forceSelection : true,triggerAction : 'all',
							valueField: 'busi_name',displayField: 'busi_name'
					})
	        	},
        		{header : '工单名称',dataIndex : 'detail_type_name',
		        	editor : new Ext.form.ComboBox({
							store: this.taskDetailTypeStore,allowBlank:false,mode:'local',selectOnFocus:true,
							editable : true,forceSelection:true,triggerAction : 'all',
							valueField: 'detail_type_name',displayField: 'detail_type_name'
					})
	        	},
	        	{header: '操作',dataIndex: 'template_id',
		            renderer:function( v , md, record , i  ){
		            	var text = "";
						if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
							text = "<a href='#' onclick=Ext.getCmp(\'"+"TaskTemplateGrid"+"\').deleteRow(); style='color:blue'> 删除 </a>";
						}
						return text;
					}
			}]
		});
		
		TaskTemplateGrid.superclass.constructor.call(this,{
			id : 'TaskTemplateGrid',
			forceValidation: true,
	        clicksToEdit: 1,
	        store : this.taskTplStore,
	        enableColumnMove : false,
	        cm: cm,
			viewConfig : {
				forceFit : true
			},
			tbar: [' 工单配置','->',{
            	text:'增加配置',id : 'addButton',disabled : !Ext.getCmp('TemplateMng').isEdit || !Ext.getCmp('TemplateMng').isBtnEdit,
            	pressed : true,iconCls : 'icon-add',scope : this,handler : this.addRecord
            },'   ']
		});
	},
	afterEdit : function(obj){
		if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
			Ext.getCmp('saveTemplate').setDisabled(false);//激活保存按钮
		}else{
			Ext.getCmp('saveTemplate').setDisabled(true);
		}
		if(obj.field == 'detail_type_name'){
			var index = this.taskDetailTypeStore.find('detail_type_name',obj.value);
			obj.record.data.detail_type_id = this.taskDetailTypeStore.getAt(index).get('detail_type_id');
		}
		
		//必须调用父类方法
		TaskTemplateGrid.superclass.afterEdit.call(this,obj);
	},
	addRecord : function(){
		var obj = {
			'template_id' : this.templateId,'busi_code' : '','busi_name' : '','detail_type_id' : '','detail_type_name' : ''
		}
		//必须调用父类方法
		TaskTemplateGrid.superclass.addRecord.call(this,obj);
	}
})

/**
 * 信息修改
 * @class UpdateCfgTemplateGrid
 * @extends Ext.grid.EditorGridPanel
 */
UpdateCfgTemplateGrid = Ext.extend(ComTemplateGrid,{
	updCfgStore : null,//信息修改模板数据
	updCfgFields : null,//信息修改字段数据
	updFields:null,//过滤的修改字段名
	busiCodeStore :null,//业务代码
	constructor : function(){
		
		//信息修改模板数据
		this.updCfgStore = new Ext.data.JsonStore({
			url : root + '/config/Template!queryUpdCfgTpls.action',
			sortInfo : {
				field : 'busi_code',
				direction : 'ASC'
			},
			fields : ['template_id','busi_code','busi_name','field_name', 'remark']
		});
		//信息修改字段数据
		this.updCfgFields = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/config/Template!queryFields.action',
			fields : ['field_name', 'remark','table_name']
		});
		this.updFields = new Ext.data.JsonStore({
			fields:[{name:'field_name',mapping:0},{name:'remark',mapping:1},{name:'table_name',mapping:2}]
		});
		this.busiCodeStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/config/Template!queryBusiForUpdate.action',
			fields : ['busi_code', 'busi_name','table_name']
		});
		
		var cm = new Ext.grid.ColumnModel({
	        columns: [
	        	{dataIndex : 'template_id',hidden : true},
	        	{header : '业务名称',dataIndex : 'busi_name',
		        	editor : new Ext.form.ComboBox({
							store: this.busiCodeStore,allowBlank : false,mode: 'local',editable : true,
							selectOnFocus:true,editable : true,forceSelection : true,triggerAction : 'all',
							valueField: 'busi_name',displayField: 'busi_name'
					})
	        	},
	        	{header : '字段名',dataIndex : 'remark',
		        	editor : new Ext.form.ComboBox({
							store: this.updFields,allowBlank : false,editable : true,mode:'local',
							selectOnFocus:true,editable : true,forceSelection : true,triggerAction : 'all',
							valueField: 'remark',displayField: 'remark'
					})
	        	},
	        	{header: '操作',dataIndex: 'template_id',
		            renderer:function( v , md, record , i  ){
		            	var text = "";
		            	if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
		            		text = "<a href='#' onclick=Ext.getCmp(\'"+"UpdateCfgTemplateGrid"+"\').deleteRow(); style='color:blue'> 删除 </a>";
		            	}
		            	return text;
					}
			}]
		});
		
		UpdateCfgTemplateGrid.superclass.constructor.call(this,{
			id : 'UpdateCfgTemplateGrid',
			forceValidation: true,
	        clicksToEdit: 1,
	        store : this.updCfgStore,
	        enableColumnMove : false,
	        cm: cm,
			viewConfig : {
				forceFit : true
			},
			tbar: [' 信息修改配置','->',{
            	text:'增加配置',id : 'addButton',pressed : true,disabled : !Ext.getCmp('TemplateMng').isEdit || !Ext.getCmp('TemplateMng').isBtnEdit,
            	iconCls : 'icon-add',scope : this,handler : this.addRecord
            },'   ']
		});
	},
	afterEdit : function(obj){
		if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
			Ext.getCmp('saveTemplate').setDisabled(false);//激活保存按钮
		}else{
			Ext.getCmp('saveTemplate').setDisabled(true);
		}
		if(obj.field == 'remark'){
			var index = this.updCfgFields.find('remark',obj.value);
			obj.record.data.field_name = this.updCfgFields.getAt(index).get('field_name');
		}
		
		//必须调用父类方法
		UpdateCfgTemplateGrid.superclass.afterEdit.call(this,obj);
	},
	beforeEdit : function(obj){
		var result = UpdateCfgTemplateGrid.superclass.beforeEdit.call(this,obj);
		if(result === false) return false;
		if(obj.field == 'remark'){
			var index = this.busiCodeStore.find('busi_name',obj.record.get('busi_name'));
			var table_name = this.busiCodeStore.getAt(index).get('table_name');
			var arr=[];
			this.updCfgFields.each(function(record){
				if(record.get('table_name') == table_name){
					arr.push([record.get('field_name'),record.get('remark'),record.get('table_name')])
				}
			},this)
			this.updFields.loadData(arr);
		}
	}
	,
	addRecord : function(){
		var obj = {
			'template_id' : this.templateId,'busi_code' : '','busi_name' : '','field_name' : '','remark' : ''
		}
		//必须调用父类方法
		UpdateCfgTemplateGrid.superclass.addRecord.call(this,obj);
	}
})

/**
 * 计费模板可编辑表
 * @class PriceTemplateGrid
 * @extends Ext.grid.EditorGridPanel
 */
BillTemplateGrid = Ext.extend(ComTemplateGrid,{
	prodStatusStore : null,
	billTplStore : null,
	isCalStore : null,
	constructor : function(){
		//产品状态数据
		this.prodStatusStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/config/Template!queryProdStatus.action',
			fields : ['status_id','status_desc']
		})
		
		//计费模板数据
		this.billTplStore = new Ext.data.JsonStore({
			url : root + '/config/Template!queryBillTpls.action',
			fields : ['template_id','status_id','status_desc','is_cal_rent','is_cal_rent_text']
		})
		
		//是否计费
		this.isCalStore = new Ext.data.SimpleStore({
			fields:['is_cal_rent', 'is_cal_rent_text'],
			data: [
				['0','否'],
				['1','是']
			]
		});
		
		var cm = new Ext.grid.ColumnModel({
	        defaults: {sortable: false},
	        columns: [
	        	{dataIndex : 'template_id',hidden : true},
	        	{header : '产品状态',dataIndex : 'status_desc',
		        	editor : new Ext.form.ComboBox({
							store: this.prodStatusStore,allowBlank : false,mode: 'local',
							selectOnFocus:true,editable : true,forceSelection : true,triggerAction : 'all',
							valueField: 'status_desc',displayField: 'status_desc'
					})
	        },{
	        	header : '计费',dataIndex : 'is_cal_rent_text',
		        	editor : new Ext.form.ComboBox({
							store: this.isCalStore,allowBlank : false,mode: 'local',
							selectOnFocus:true,editable : true,forceSelection : true,triggerAction : 'all',
							valueField: 'is_cal_rent_text',displayField: 'is_cal_rent_text'
					})
	        },{
		        header: '操作',dataIndex: 'template_id',
		            renderer:function( v , md, record , i  ){
		            	var text = "";
		            	if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
		            		text = "<a href='#' onclick=Ext.getCmp(\'"+"BillTemplateGrid"+"\').deleteRow(); style='color:blue'> 删除 </a>";
		            	}
		            	return text;
					}
			}]
		});
		
		BillTemplateGrid.superclass.constructor.call(this,{
			id : 'BillTemplateGrid',
			forceValidation: true,
	        clicksToEdit: 1,
	        store : this.billTplStore,
	        enableColumnMove : false,
	        cm: cm,
			viewConfig : {
				forceFit : true
			},
			tbar: [' 计费配置','->',{
            	text:'增加配置',id:'addButton',pressed:true,disabled : !Ext.getCmp('TemplateMng').isEdit || !Ext.getCmp('TemplateMng').isBtnEdit,
            	iconCls : 'icon-add',scope : this,handler : this.addRecord
            },'   ']
		});
	},
	afterEdit : function(obj){//不调用父类方法
		if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
			Ext.getCmp('saveTemplate').setDisabled(false);//激活保存按钮
		}else{
			Ext.getCmp('saveTemplate').setDisabled(true);
		}
		if(obj.field == 'status_desc'){
			var index = this.prodStatusStore.find('status_desc',obj.value);
			obj.record.data.status_id = this.prodStatusStore.getAt(index).get('status_id');
		}else if(obj.field == 'is_cal_rent_text'){
			var index = this.isCalStore.find('is_cal_rent_text',obj.value);
			obj.record.data.is_cal_rent = this.isCalStore.getAt(index).get('is_cal_rent');
		}
	},
	addRecord : function(){
		var obj = {
			'template_id' : this.templateId,
			'status_id' : '',
			'status_desc' : '',
			'is_cal_rent' : '',
			'is_cal_rent_text' : ''
		}
		//必须调用父类方法
		BillTemplateGrid.superclass.addRecord.call(this,obj);
	}
});

/**
 * 发票打印模板可编辑表
 * @class InvoiceTemplateGrid
 * @extends ComTemplateGrid
 */
InvoiceTemplateGrid = Ext.extend(ComTemplateGrid,{
	invoiceTypeStore : null,//发票打印种类
	invoiceTplStore : null,//发票打印模板数据
	printItemStore : null,//打印项种类
	constructor : function(){
		this.invoiceTypeStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/config/Template!queryInvoiceType.action',
			fields : ['doc_name','doc_type']
		});
		this.invoiceTypeStore.on("load",this.doFilter,this);
		
		this.printItemStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/config/Template!queryPrintItem.action',
			fields : ['printitem_id','printitem_name']
		});
		
		this.invoiceTplStore = new Ext.data.JsonStore({
			url : root + '/config/Template!queryInvoiceTpls.action',
			fields : ['template_id','printitem_id','printitem_name','doc_name','doc_type']
		});
		
		var cm = new Ext.grid.ColumnModel({
	        columns: [
	        	{dataIndex : 'template_id',hidden : true},
	        	{header : '打印项',dataIndex : 'printitem_name',
		        	editor : new Ext.form.ComboBox({
							store: this.printItemStore,allowBlank : false,mode: 'local',
							selectOnFocus:true,editable:true,forceSelection:true,triggerAction:'all',
							valueField: 'printitem_name',displayField: 'printitem_name'
					})
	        	},{
	        	header : '发票类型',
	        	dataIndex : 'doc_name',
	        	editor : new Ext.form.ComboBox({
						store: this.invoiceTypeStore,allowBlank : false,mode: 'local',
						selectOnFocus:true,editable : true,forceSelection : true,triggerAction : 'all',
						valueField: 'doc_name',displayField: 'doc_name'
				})
	        },{
		        header: '操作',
		        dataIndex: 'template_id',
	            renderer:function( v , md, record , i  ){
					var text = "";
					if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
						text = "<a href='#' onclick=Ext.getCmp(\'"+"InvoiceTemplateGrid"+"\').deleteRow(); style='color:blue'> 删除 </a>";
					}
					return text;
				}
			}]
		});
		
		InvoiceTemplateGrid.superclass.constructor.call(this,{
			id : 'InvoiceTemplateGrid',
			forceValidation: true,
	        clicksToEdit: 1,
	        store : this.invoiceTplStore,
	        enableColumnMove : false,
	        cm: cm,
			viewConfig : {
				forceFit : true
			},
			tbar: [' 发票打印配置','->',{
            	text:'增加配置',
            	id : 'addButton',
            	pressed : true,
            	iconCls : 'icon-add',
            	disabled : !Ext.getCmp('TemplateMng').isEdit || !Ext.getCmp('TemplateMng').isBtnEdit,
            	scope : this,
            	handler : this.addRecord
            },'   ']
		});
	},
	afterEdit : function(obj){//不调用父类方法
		if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
			Ext.getCmp('saveTemplate').setDisabled(false);//激活保存按钮
		}else{
			Ext.getCmp('saveTemplate').setDisabled(true);
		}
		if(obj.field == 'doc_name'){
			var index = this.invoiceTypeStore.find('doc_name',obj.value);
			obj.record.data.doc_type = this.invoiceTypeStore.getAt(index).get('doc_type');
		}else if(obj.field == "printitem_name"){
			var index = this.printItemStore.find('printitem_name',obj.value);
			obj.record.data.printitem_id = this.printItemStore.getAt(index).get('printitem_id');
		}
	},
	addRecord : function(){
		var obj = {
			'template_id' : this.templateId,
			'doc_name' : '',
			'doc_type' : '',
			'printitem_id' : '',
			'printitem_name' : ''
		}
		//必须调用父类方法
		InvoiceTemplateGrid.superclass.addRecord.call(this,obj);
	},
	doFilter : function(store,records,options){
		if(this.templateId == '2'){
			store.each(function(record){
				if(record.get('doc_type') == Constant.WH_INVOICE_TYPE){
					store.remove(record);
					return false;
				}
			});
		}
	}
});

/**
 * 配置模板可编辑表
 * @class ConfigTemplateGrid
 * @extends Ext.grid.EditorGridPanel
 */
ConfigTemplateGrid = Ext.extend(ComTemplateGrid,{
	configTplStore : null,
	constructor : function(){
		this.configTplStore = new Ext.data.JsonStore({
			url : root + '/config/Template!queryConfigTpls.action',
			fields : ['template_id','config_name', 'remark','param_name','config_value', 'config_value_text','form_type']
		});
		
		var cm = new Ext.grid.ColumnModel({
	        defaults: {sortable: false},
	        columns: [
	        	{dataIndex : 'template_id',hidden : true},
	        	{header : '配置名称',dataIndex : 'remark'},
	        	{header : '参数值',id : 'config_value_text',dataIndex : 'config_value_text',
		        	editor: new Ext.form.TextField({allowBlank: false}),
	                renderer : function(value,meta,record,rowIndex,columnIndex,store){
	                	if(!value){return record.get('config_value');}
	                	return value;
	                }
	        }]
		});
		
		ConfigTemplateGrid.superclass.constructor.call(this,{
			id : 'ConfigTemplateGrid',
			forceValidation: true,
	        clicksToEdit: 1,
	        store : this.configTplStore,
	        enableColumnMove : false,
	        cm: cm,
			viewConfig : {
				forceFit : true
			}
		});
	},
	initEvents : function(){
		this.getStore().on('load',this.afterLoad,this);
		ConfigTemplateGrid.superclass.initEvents.call(this);
	},
	beforeEdit : function(obj){
		if(Ext.getCmp('TemplateMng').isEdit === false){
			return false;
		}
		if(obj.field == 'config_value_text'){
			if(obj.record.data.param_name == 'NUMBER'){
				var numberField = new Ext.form.NumberField({
					allowBlank : false,
					allowNegative : false
				});
				this.getColumnModel().getColumnById('config_value_text').editor = numberField;
			}else if(obj.record.data.param_name){
				var combo = null;
				if(obj.record.data.form_type == 'paramcombo'){
					combo = new Ext.ux.ParamCombo({
							allowBlank:false,forceSelection:true,selectOnFocus:true,
							editable : true,valueField : 'item_name',
							typeAhead:false,paramName:obj.record.data.param_name
						});
				}else if(obj.record.data.form_type == 'paramlovcombo'){
					combo = new Ext.ux.ParamLovCombo({
							allowBlank:false,forceSelection:true,selectOnFocus:true,
							editable : true,valueField : 'item_name',separator:';',
							typeAhead:false,paramName:obj.record.data.param_name,
							beforeBlur:function(){}
						});
				}
				if(combo){
					App.form.initComboData([combo]);
					this.getColumnModel().getColumnById('config_value_text').editor = combo;
				}
			}else{
				Alert("请先选择配置名称");
				this.stopEditing();
				return;
			}
		}
	},
	afterEdit : function(obj){//不调用父类方法
		if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
			Ext.getCmp('saveTemplate').setDisabled(false);//激活保存按钮
		}else{
			Ext.getCmp('saveTemplate').setDisabled(true);
		}
		if(obj.field == 'config_value_text'){
			var editor = this.getColumnModel().getColumnById('config_value_text').editor;
			if(editor.getStore){
				var formType = obj.record.get('form_type');
				if(formType == 'paramcombo'){
					var index = editor.getStore().find('item_name',obj.value);
					obj.record.data.config_value = editor.getStore().getAt(index).get('item_value');
				}else if(formType == 'paramlovcombo'){
					var arr = obj.value.split(';'), values='';
					for(var i=0,len=arr.length;i<len;i++){
						var index = editor.getStore().find('item_name',arr[i]);
						values += editor.getStore().getAt(index).get('item_value')+';';
					}
					obj.record.data.config_value = values.substring(0,values.length-1);
				}
			}else{
				obj.record.data.config_value = obj.value;
			}
		}
	},
	afterLoad : function(store){
		if(store.getCount() == 0){
			Alert('')
		}
	},
	addRecord : function(){
		var obj = {
			'template_id' : this.templateId,
			'config_name' : '','remark' : '','param_name' : '','config_value' : '','config_value_text' : ''
		}
		//必须调用父类方法
		ConfigTemplateGrid.superclass.addRecord.call(this,obj);
	}
});

/**
 * 临时授权配置
 * @class OpenTempGrid
 * @extends Ext.grid.GridPanel
 */
OpenTempGrid = Ext.extend(ComTemplateGrid,{
	openTempStore : null,
	constructor : function(){
		this.openTempStore = new Ext.data.JsonStore({
			url : root + '/config/Template!queryOpenTemps.action',
			fields : ['user_type','template_id','cycle','times','days']
		});
		var userTypeCombo = new Ext.form.ComboBox({
			store:new Ext.data.JsonStore({
				fields:['user_type','user_type_text'],
				data:[{'user_type':'DTV','user_type_text':'数字'},{'user_type':'BAND','user_type_text':'宽带'}]
			}),displayField:'user_type_text',valueField:'user_type',allowBlank:false
		});
		
		var cm = new Ext.grid.ColumnModel({
	        columns: [
	        	{dataIndex : 'template_id',hidden : true},
	        	{header: '用户类型',dataIndex:'user_type',
	        		editor:userTypeCombo ,scope:userTypeCombo.getStore(),
	        		renderer:function(value){
						var index = this.find('user_type',value);
						var record = this.getAt(index);
						if(!Ext.isEmpty(record)){
							return record.get('user_type_text');
						}
						return '';
					}
	        	},
	        	{header : '周期（单位月）',dataIndex : 'cycle',
	        		editor : new Ext.form.NumberField({allowBlank : false,allowNegative : false})
	        	},
	        	{header : '次数',dataIndex : 'times',
	        		editor : new Ext.form.NumberField({allowBlank : false,allowNegative : false})
	        	},
	        	{header : '天数',dataIndex : 'days',
		        	editor : new Ext.form.NumberField({
		        		allowBlank : false,allowNegative : false})
	        	},
	        	{header: '操作',dataIndex: 'template_id',renderer:function( v , md, record , i  ){
	        			var text = "";
	        			if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
	        				text = "<a href='#' onclick=Ext.getCmp(\'"+"OpenTempGrid"+"\').deleteRow(); style='color:blue'> 删除 </a>";
	        			}
	        			return text;
					}
				}
			]
		});
		
		OpenTempGrid.superclass.constructor.call(this,{
			id : 'OpenTempGrid',
			forceValidation: true,
	        clicksToEdit: 1,
	        store : this.openTempStore,
	        enableColumnMove : false,
	        cm: cm,
			viewConfig : {
				forceFit : true
			},
			tbar: [' 临时授权配置','->',{
            	id : 'addButton',text:'增加配置',pressed : true,iconCls : 'icon-add',
            	disabled : !Ext.getCmp('TemplateMng').isEdit || !Ext.getCmp('TemplateMng').isBtnEdit,
            	scope : this,handler : this.addRecord
            },'   ']
			
		});
	},
	afterEdit : function(){
		if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
			Ext.getCmp('saveTemplate').setDisabled(false);//激活保存按钮
		}else{
			Ext.getCmp('saveTemplate').setDisabled(true);
		}
	},
	addRecord : function(){
		var obj = {
			'template_id' : this.templateId,
			'user_type':'',
			'cycle' : 0,
			'times' : 0,
			'days' : 0
		}
		//必须调用父类方法
		OpenTempGrid.superclass.addRecord.call(this,obj);
		Ext.getCmp('addButton').setDisabled(true);
	},
	deleteRow : function(){
		Confirm("确定要删除该数据吗?", this ,function(){
			if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
				Ext.getCmp('saveTemplate').setDisabled(false);//激活保存按钮
			}else{
				Ext.getCmp('saveTemplate').setDisabled(true);
			}
			var record= this.getSelectionModel().getSelected();
			this.getStore().remove(record);
		});
	}
});

StbFilledGrid = Ext.extend(ComTemplateGrid,{
	stbFilledStore : null,
	resStore : null,
	constructor : function(){
		this.stbFilledStore = new Ext.data.JsonStore({
			url : root + '/config/Template!queryStbFilleds.action',
			fields : ['template_id','res_id', 'months','res_name']
		})
		
		this.resStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/config/Template!queryDtvRes.action',
			fields : ['res_id','res_name']
		});
		
		var cm = new Ext.grid.ColumnModel({
	        defaults: {sortable: false},
	        columns: [
	        	{dataIndex : 'template_id',hidden : true},
	        	{header : '资源名称',dataIndex : 'res_name',editor : new Ext.form.ComboBox({
						store: this.resStore,allowBlank : false,mode: 'local',selectOnFocus:true,
						editable : true,forceSelection : true,triggerAction : 'all',
						valueField: 'res_name',displayField: 'res_name'})},
	        	{header : '授权天数',dataIndex : 'months',editor: new Ext.form.TextField({allowBlank: false})},
	        	{header: '操作',dataIndex: 'template_id',renderer:function( v , md, record , i  ){
	        			var text = "";
	        			if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
	        				text = "<a href='#' onclick=Ext.getCmp(\'"+"StbFilledGrid"+"\').deleteRow(); style='color:blue'> 删除 </a>";
	        			}
	        			return text;
					}
				}
			]
		});
		
		StbFilledGrid.superclass.constructor.call(this,{
			id : 'StbFilledGrid',
			forceValidation: true,
	        clicksToEdit: 1,
	        store : this.stbFilledStore,
	        enableColumnMove : false,
	        cm: cm,
			viewConfig : {
				forceFit : true
			},
			tbar: [' 机顶盒灌装配置','->',{
            	id : 'addButton',text:'增加配置',pressed : true,iconCls : 'icon-add',
            	disabled : !Ext.getCmp('TemplateMng').isEdit || !Ext.getCmp('TemplateMng').isBtnEdit,scope : this,handler : this.addRecord
            },'   ']
			
		});
	},
	afterEdit : function(obj){//不调用父类方法
		if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
			Ext.getCmp('saveTemplate').setDisabled(false);//激活保存按钮
		}else{
			Ext.getCmp('saveTemplate').setDisabled(true);
		}
		if(obj.field == 'res_name'){
			var index = this.resStore.find('res_name',obj.value);
			obj.record.data.res_id = this.resStore.getAt(index).get('res_id');
		}
	},
	addRecord : function(){
		var obj = {
			'template_id' : this.templateId,'res_name' : '','res_id' : '','months' : 0
		}
		//必须调用父类方法
		StbFilledGrid.superclass.addRecord.call(this,obj);
	}
});

/**
 * 自定义fieldSet类，使得前面复选框变成全选框
 * @class CustomFieldSet
 * @extends Ext.form.FieldSet
 */
CustomFieldSet = Ext.extend(Ext.form.FieldSet,{
	isLoaded : false,
	checkBoxGroup : null,
	constructor : function(data,isLoaded,disabled){
		this.isLoaded = isLoaded;
		
		var checkboxs = [];
		var counties = data.countyList;
		for(var i=0;i<counties.length;i++){
			checkboxs.push({
				boxLabel : counties[i].county_name,
				checked : counties[i].checked,
				value : counties[i].county_id
			})
		}
		this.checkBoxGroup = {
			xtype : 'checkboxgroup',
			columns : 3,
			items : checkboxs,
			disabled : !disabled,
			listeners : {
				change : function(){
					if(Ext.getCmp('TemplateMng').isEdit === true && Ext.getCmp('TemplateMng').isBtnEdit === true){
						Ext.getCmp('saveTemplate').setDisabled(false);
					}else{
						Ext.getCmp('saveTemplate').setDisabled(true);
					}
				}
			}
		};
		
		var items = [];
		if(this.isLoaded){
			items.push(this.checkBoxGroup);
		}
		CustomFieldSet.superclass.constructor.call(this,{
			title: data.area_name,
            checkboxToggle: disabled,//禁止修改的时候不显示复选框
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
	}
})

/**
 * 地区选择面板
 * @class CountyPanel
 * @extends Ext.Panel
 */
CountyPanel = Ext.extend(Ext.Panel,{
	countyData : null,
	constructor : function(countyData,disabled){
		this.countyData = countyData;
		var companyPanels = [];
		for(var i=0;i<this.countyData.length;i++){
			if(this.countyData[i].countyList && this.countyData[i].countyList.length){
				if(i==0){
					companyPanels.push(this.createCompanyPanel(this.countyData[i],true,disabled));
				}else{
					companyPanels.push(this.createCompanyPanel(this.countyData[i],false,disabled));
				}
			}
		}
		//TODO SDA 
		CountyPanel.superclass.constructor.call(this,{
			id : 'CountyPanel',
			bodyStyle : Constant.TAB_STYLE,
			title : '适用分公司选择',
			autoScroll : true,
			border : false,
			layout : 'anchor',
			items : companyPanels
		});
	},
	createCompanyPanel : function(data,isLoaded,disabled){
		var fieldSet = new CustomFieldSet(data,isLoaded,disabled);
		return fieldSet;
	},
	getValues: function(){
		var fieldSets = Ext.getCmp('CountyPanel').items;
		var countyIds = []; 
		for(var i=0,len=fieldSets.length;i<len;i++){
			if(fieldSets.itemAt(i).collapsed){
				fieldSets.itemAt(i).expand();
				fieldSets.itemAt(i).collapse();
			}
			var item = fieldSets.itemAt(i).items.itemAt(0);
			if(item){
				var cbs = item.getValue();
				for(var k=0;k<cbs.length;k++){
					countyIds.push(cbs[k].value);
				}
			}
		}
		return countyIds;
	}
});

/**
 * 模板配置管理面板
 * @class TemplateMng
 * @extends Ext.Panel
 */
TemplateMng = Ext.extend(Ext.Panel,{
	busiCodeStore : null,
	templateId : null,
	allowModified : false,//是否允许修改
	templateType : null,
	node : null,//模板节点
	cfgId:null,
	isBtnEdit: false,		//按钮编辑权限：是否能新增模板或复制模板
	isEdit:true,			//表格中行是否有修改功能
	constructor : function(){
		//业务编号
		this.busiCodeStore = new Ext.data.JsonStore({
			autoLoad : true,
			url : root + '/config/Template!querybusi.action',
			fields : ['busi_code', 'busi_name']
		});
		
		TemplateMng.superclass.constructor.call(this,{
			id : 'TemplateMng',
			region : 'center',
			title : '模板配置管理',
			layout:'border',
			items:[{
				region:'center',border:false,layout:'fit',items:[]
			},{
				region:'south',border:false,layout:'fit',height:130,split:true,collapseMode:'mini',items:[]
			}],
			buttons : [{
				text : '保存配置',
				id : 'saveTemplate',
				iconCls : 'icon-save',
				disabled : true,
				scope : this,
				handler : this.doSave
			}]
		});
	},
	addItems : function(n){
		this.resetPanel();//重置相关属性
		var arr = n.attributes.others.attr.split("_");
		var templateId = arr[0];
		var optrId = arr[1];
		var templateType = n.parentNode.attributes.others.attr;
		
		this.node = n;
		this.templateId = templateId;
		
		this.templateType = templateType.substring(0,templateType.indexOf('_type'));
		
		Ext.Ajax.request({
			scope : this,
			params:{
				templateId : templateId
			},
			url : root + '/common/District!queryCounties.action' ,
			success : function(res,opt){
				var countyDatas = Ext.decode(res.responseText);
				this.items.itemAt(1).add(new CountyPanel(countyDatas, Ext.getCmp('TemplateMng').isEdit && Ext.getCmp('TemplateMng').isBtnEdit));
				this.doLayout();
			}
		});
		
		Ext.Ajax.request({
			url : root + '/config/Template!queryTemplateCountyById.action' ,
			params:{
				templateId : templateId
			},
			scope : this,
			async : false,// 同步请求
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data && data.length > 0){
					var changeCountyIds = Ext.getCmp('TemplateTree').changeCountyIds;
					if(!Ext.isEmpty(changeCountyIds)){
						for(var i=0,len=data.length;i<len;i++){
							var countyId = data[i]['county_id'];
							if(changeCountyIds.indexOf(countyId) == -1){
//								alert('不能编辑 ： '+countyId);
								this.isEdit = false;
								break;
							}
						}
					}else{
						this.isEdit = false;
					}
				}
			}
		});
		
		
		if(templateType == 'FEE_type'){
			this.items.itemAt(0).add(new FeeTemplateGrid());
			Ext.getCmp('FeeTemplateGrid').refreshGrid(templateId,this.templateType);
			this.cfgId = 'FeeTemplateGrid';
		}else if(templateType == 'DOC_type'){
			this.items.itemAt(0).add(new DocTemplateGrid(this.busiCodeStore));
			Ext.getCmp('DocTemplateGrid').refreshGrid(templateId,this.templateType);
			this.cfgId = 'DocTemplateGrid';
		}else if(templateType == 'TASK_type'){
			this.items.itemAt(0).add(new TaskTemplateGrid(this.busiCodeStore));
			Ext.getCmp('TaskTemplateGrid').refreshGrid(templateId,this.templateType);
			this.cfgId = 'TaskTemplateGrid';
		}else if(templateType == 'UPDPROP_type'){
			this.items.itemAt(0).add(new UpdateCfgTemplateGrid(this.busiCodeStore));
			Ext.getCmp('UpdateCfgTemplateGrid').refreshGrid(templateId,this.templateType);
			this.cfgId = 'UpdateCfgTemplateGrid';
		}else if(templateType == 'BILLING_type'){
			this.items.itemAt(0).add(new BillTemplateGrid(this.busiCodeStore));
			Ext.getCmp('BillTemplateGrid').refreshGrid(templateId,this.templateType);
			this.cfgId = 'BillTemplateGrid';
		}else if(templateType == 'INVOICE_type'){
			this.items.itemAt(0).add(new InvoiceTemplateGrid(this.busiCodeStore));
			Ext.getCmp('InvoiceTemplateGrid').refreshGrid(templateId,this.templateType);
			this.cfgId = 'InvoiceTemplateGrid';
		}else if(templateType == 'CONFIG_type'){
			this.items.itemAt(0).add(new ConfigTemplateGrid(this.busiCodeStore));
			Ext.getCmp('ConfigTemplateGrid').refreshGrid(templateId,this.templateType);
			this.cfgId = 'ConfigTemplateGrid';
		}else if(templateType == 'OPEN_TEMP_type'){
			this.items.itemAt(0).add(new OpenTempGrid());
			Ext.getCmp('OpenTempGrid').refreshGrid(templateId,this.templateType);
			this.cfgId = 'OpenTempGrid';
		}else if(templateType == 'STB_FILLED_type'){
			this.items.itemAt(0).add(new StbFilledGrid());
			Ext.getCmp('StbFilledGrid').refreshGrid(templateId,this.templateType);
			this.cfgId = 'StbFilledGrid';
		}
		this.items.itemAt(0).doLayout();
		
	},
	resetPanel : function(){//重置相关属性
		this.templateId = null;
		this.templateType = null;
		this.items.itemAt(0).removeAll();
		this.items.itemAt(1).removeAll();
		Ext.getCmp('saveTemplate').setDisabled(true);
	},
	doSave : function(){
		if(this.templateType != 'FEE'){
			this.cfgId
			var msg = Ext.getCmp(this.cfgId).checkValid();
			if( msg != true){//验证配置模板数据是否正确
				Alert(msg);
				return;
			}
		}
		
		Confirm('确定保存吗',this,function(){
			mb = Show();//显示正在提交
			
			var all = {};
			all['templateId'] = this.templateId;
			all['templateType'] = this.templateType;
			
			//模板配置数据
			var data = [];
			Ext.getCmp(this.cfgId).getStore().each(function(item){
				data.push(item.data);
			});
			all['templateList'] = Ext.encode(data);
			
			//模板适用分公司数据
			var countyIds = Ext.getCmp('CountyPanel').getValues();
			all['countyIds'] = countyIds.join(',');
			
			var url = '';
			if(this.templateType == 'FEE'){
				url = root + '/config/Template!saveFeeTpls.action';
			}else if(this.templateType == 'DOC'){
				url = root + '/config/Template!saveDocTpls.action';
			}else if(this.templateType == 'TASK'){
				url = root + '/config/Template!saveTaskTpls.action';
			}else if(this.templateType == 'UPDPROP'){
				url = root + '/config/Template!saveUpdCfgTpls.action';
			}else if(this.templateType == 'BILLING'){
				url = root + '/config/Template!saveBillTpls.action';
			}else if(this.templateType == 'INVOICE'){
				url = root + '/config/Template!saveInvoiceTpls.action';
			}else if(this.templateType == 'CONFIG'){
				url = root + '/config/Template!saveConfigTpls.action';
			}else if(this.templateType == 'OPEN_TEMP'){
				url = root + '/config/Template!saveOpenTemps.action';
			}else if(this.templateType == 'STB_FILLED'){
				url = root + '/config/Template!saveStbFilled.action';
			}else{
				Alert('模板类型未配置');
				return;
			}
			
			
			Ext.Ajax.request({
				scope : this,
				url :  url,
				params : all,
				success : function(res,opt){
					mb.hide();//隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						Ext.getCmp(this.cfgId).refreshGrid(this.templateId,this.templateType);
						Ext.getCmp('saveTemplate').setDisabled(true);
					}else{
						Alert('操作失败');
			 		}
				}
			})
		})
		
	}
})

/**
 * 模板展示面板
 * @class TemplateView
 * @extends Ext.Panel
 */
TemplateView = Ext.extend(Ext.Panel,{
	constructor : function(){
		var tree = new TemplateTree();
		var panel = new TemplateMng();
		TemplateView.superclass.constructor.call(this,{
			id : 'TemplateView',
			layout : 'border',
			title : '模板配置管理',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			items : [tree, panel]
		});
		var resources = App.data.resources;
		for(var i=0,len=resources.length;i<len;i++){
			var res = resources[i];
			if(res['sub_system_id'] == '2'){
				if(res['handler'] == 'EditTemplate' && panel.isBtnEdit === false){
					panel.isBtnEdit = true;
				}
			}
		}
	}
})
