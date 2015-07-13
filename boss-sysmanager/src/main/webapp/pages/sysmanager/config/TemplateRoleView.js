/**
 * 模板权限分配
 * @class TemplateRoleView
 * @extends Ext.Panel
 */

TemplateFeeTree = Ext.extend(Ext.tree.TreePanel,{
	rootNode : null,
	currentTemplateId: null,
	constructor : function(){
		var loader = new Ext.tree.TreeLoader({
			dataUrl  : root + '/config/Template!queryFeeTemplateTree.action',
			listeners:{
				scope:this,
				load: this.doLoad
			}
		});
		TemplateFeeTree.superclass.constructor.call(this,{
			onlyLeafCheckable : true,
			checkModel : 'single',
			split : true,
			region : 'west',
			bodyStyle	:'padding:3px',
			width : '16%',
			autoScroll	:true,
	        collapseMode:'mini',
	        loader : loader,
		    root: {
				id 		: '0',
				iconCls : 'x-tree-root-icon',
				nodeType:'async',
				expanded : true,
				text: '模板配置'
			},
		    listeners : {
		    	scope : this,
		    	click : this.doClick
		    }
		});
	},
	doLoad:function(){
    	var childarr = this.getRootNode().childNodes;
		if (childarr.length > 0) {
			Ext.each(childarr, function(node){
				if(node.loaded == false){
					node.expand();
				}
			},this);
			
			/*var currentCountyid = App.getData().optr['county_id'];
			var nodes = childarr[0].childNodes;
			Ext.each(nodes, function(n){
				var attr = n.attributes.others.attr
				var countyId =attr.substring(attr.lastIndexOf('_')+1,attr.length);
				if(countyId == currentCountyid){
					n.select();
					this.doClick(n);
					return false;
				}
			},this);*/
		}
	},
	doClick : function(n){
		Ext.getCmp('templateRoleGridId').preMyReadioId = null;
		if(n.parentNode && n.attributes.others.attr.indexOf("type") == -1){
			var arr = n.attributes.others.attr.split("_");
			var templateId = arr[0];
			if(this.currentTemplateId != templateId){
				this.currentTemplateId = templateId;
				var store = Ext.getCmp('templateRoleGridId').getStore();
				store.baseParams['templateId'] = this.currentTemplateId;
				store.load();
			}
		}
	}
});

TemplateRoleGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	templateRoleStore: null,
	selectValueComp: null,
	minValueComp: null,
	maxValueComp: null,
	defaultValueComp: null,
	preMyReadioId: null,
	constructor: function(){
		this.templateRoleStore = new Ext.data.GroupingStore({
			url: root+'/config/Template!queryFeeTemplateColumn.action',
			groupField:'fee_std_id',
			pruneModifiedRecords:true,
			reader: new Ext.data.JsonReader({
				fields:[
					{name: 'column_id',mapping:'column_id'},
					{name: 'template_id',mapping:'template_id'},
					{name: 'template_name',mapping:'template_name'},
					{name: 'fee_std_id',mapping:'fee_std_id'},
					{name: 'fee_id',mapping:'fee_id'},
					{name: 'fee_name',mapping:'fee_name'},
					{name: 'column_name',mapping:'column_name'},
					{name: 'column_text',mapping:'column_text'},
					{name: 'type',mapping:'type'},
					{name: 'is_editable',mapping:'is_editable'},
					{name: 'min_value',mapping:'min_value',type:'int'},
					{name: 'default_value',mapping:'default_value',type:'int'},
					{name: 'max_value',mapping:'max_value',type:'int'},
					{name: 'item_key',mapping:'item_key'},
					{name: 'item_key_text',mapping:'item_key_text'},
					{name: 'select_value',mapping:'select_value'},
					{name: 'select_value_text',mapping:'select_value_text'},
					{name: 'create_time',mapping:'create_time'},
					{name: 'fee_min_value',mapping:'fee_min_value'},
					{name: 'fee_max_value',mapping:'fee_max_value'},
					{name: 'fee_default_value',mapping:'fee_default_value'},
					{name: 'device_buy_mode',mapping:'device_buy_mode'},
					{name: 'device_type',mapping:'device_type'},
					{name: 'device_type_text',mapping:'device_type_text'},
					{name: 'device_buy_mode_text',mapping:'device_buy_mode_text'}
			]})
		});
		
		this.selectValueComp = new Ext.ux.ParamLovCombo({listWidth:200});
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		
		
		var cm = new Ext.grid.ColumnModel({
			columns: [
				sm,
				{header:'fee_std_id',dataIndex:'fee_std_id',hidden:true},
				{header:'列名',dataIndex:'column_text',width:80},
				{id:'type_id',header:'类型',dataIndex:'type',width:60,scope:this,
					renderer: function(v){
						var text = '';
						if(v == 'string') text = '字符';
						else if(v == 'number') text = '数字';
						return text;
					}},
				{id:'is_editable_id',header:'编辑',dataIndex:'is_editable',width:45,
					editor:new Ext.form.ComboBox({
						store:new Ext.data.JsonStore({
							fields:['name','value'],
							data:[{name:'是',value:'T'},{name:'否',value:'F'}]
						}),displayField:'name',valueField:'value',triggerAction:'all'
					}),scope:this,
					renderer: function(v){
						if(v == 'T') return '是';
						else if(v == 'F') return '否';
						return '';
					}
				},
				{id:'min_value_id',header:'最小值',dataIndex:'min_value',width:60,scope:this,
					editor: new Ext.form.NumberField({
						allowNegative:false,
						listeners:{
							scope: this,
							focus: this.doFocusFee
						}
					}),
					renderer: Ext.util.Format.formatFee
				},
				{id:'default_value_id',header:'默认值',dataIndex:'default_value',width:60,scope:this,
					editor: new Ext.form.NumberField({
						allowNegative:false,
						listeners:{
							scope: this,
							focus: this.doFocusFee
						}
					}),
					renderer: Ext.util.Format.formatFee
				},
				{id:'max_value_id',header:'最大值',dataIndex:'max_value',width:60,scope:this,
					editor: new Ext.form.NumberField({
						allowNegative:false,
						listeners: {
							scope: this,
							focus: this.doFocusFee
						}
					}),
					renderer: Ext.util.Format.formatFee
				},
				{id:'item_key_id',header:'键值',dataIndex:'item_key_text',width:75},
				{id:'select_value_text_id',header:'选择值',dataIndex:'select_value',width:150,
					scope:this,
					editor: this.selectValueComp,
					renderer: this.paramComboRender.createDelegate(this.selectValueComp)
				},
				{header:'创建时间',dataIndex:'create_time',width:120},
				{header:'操作',dataIndex:'column_id',
					renderer: function(v){
						return "<a href='#' onclick=Ext.getCmp('templateRoleGridId').goToOptr('edit');>分配操作员(修改)</a>";
					}
				}
			],
			defaults : {
				align:'center',
				sortable : false
			},
			scope:this,
			isCellEditable:this.isCellEditable
		});
		
		TemplateRoleGrid.superclass.constructor.call(this, {
			id:'templateRoleGridId',
			region:'center',
			store: this.templateRoleStore,
			cm: cm,
			sm: sm,
			stripeRows: true,
			clicksToEdit:1,
			view: new Ext.grid.GroupingView({
	            forceFit: true,
			    interceptMouse : function(e){
			        var hd = e.getTarget('.x-grid-group-hd', this.mainBody);
			        if(hd){
			        	//点击分组信息时，选择同一组所有数据行,并反选中上次选中行
			        	var grid = this.grid;
			        	grid.getSelectionModel().clearSelections();
			    		if(grid.preMyReadioId)
			    			document.getElementById(grid.preMyReadioId).checked = false;
				        var groupId = hd.id;
				        var left = groupId.indexOf("fee_std_id-")+11;
				        var right = groupId.indexOf("-hd");
				        var feeStdId = groupId.substring(left,right);	//当前分组字段值
				        var myRadioId = "myRadio"+feeStdId;
				        
				        if(myRadioId != grid.preMyReadioId){
				        	
					        document.getElementById(myRadioId).checked = true;
					        
					        grid.preMyReadioId = myRadioId;
					        
					        var store = grid.getStore();
					        store.each(function(record){
					        	if(record.get('fee_std_id') == feeStdId){
					        		this.getSelectionModel().selectRow(store.indexOf(record), true);
					        	}
					        },grid);
				        }else{
				        	grid.preMyReadioId = null;	
				        }
			        }
			    },
	            groupTextTpl:"<input type='radio' id='myRadio{[values.rs[0].data[\'fee_std_id\']]}' />"
	            	+ "{[values.rs[0].data['fee_name']]}"
	            	+ "{[values.rs[0].data['device_type_text'] ? '&nbsp;&nbsp;设备类型：'+values.rs[0].data['device_type_text']+'':'']}"
	            	+ "{[values.rs[0].data['device_buy_mode_text'] ? '&nbsp;&nbsp;购买方式：'+values.rs[0].data['device_buy_mode_text']+'':'']}"
	            	+ "&nbsp;&nbsp;最小值：{[Ext.util.Format.formatFee(values.rs[0].data['fee_min_value'])]}"
	            	+ "&nbsp;&nbsp;默认值：{[Ext.util.Format.formatFee(values.rs[0].data['fee_default_value'])]}"
	            	+ "&nbsp;&nbsp;最大值：{[Ext.util.Format.formatFee(values.rs[0].data['fee_max_value'])]}"
	            	+ "&nbsp;&nbsp;<a href='#' onclick=Ext.getCmp('templateRoleGridId').goToOptr('edit')>批量分配</a>"
        	}),
        	tbar:[
        		'-',' 输入关键字  ', new Ext.ux.form.SearchField({
	                store: this.templateRoleStore,
	                width: 130,
	                hasSearch: true,
	                emptyText: '支持费用名模糊查询'
	            }),'-',
    			{text:'刷新',iconCls:'icon-refresh',scope:this,handler:this.doReLoad},
    			'-',
    			{text: '分配操作员(添加)',iconCls:'icon-condition',scope:this,handler:function(v){
    					this.goToOptr('add');
    				}
    			},'-',
    			'->','-',
    			{text:'保存配置',iconCls:'icon-save',scope:this,handler:this.doSave},
        		'-'
        	],
        	listeners:{
        		scope:this,
        		rowclick: function(){
        			if(this.preMyReadioId){
	        			Ext.getDom(this.preMyReadioId).checked = false;
	        			this.preMyReadioId = null;
        			}
        		},
        		beforeedit: this.doBeforeEdit,
        		afteredit:  this.doAfterEdit
        	}
		});
	},
	paramComboRender: function(values,meta,record){
		if(Ext.isEmpty(values)) return '';
		
		var res = '';
		if(Ext.isEmpty(this.paramName)){
			res = record.get('select_value_text');
		}else{
			var arr = values.split(',');
			for(var i=0,len=arr.length;i<len;i++){
				var index = this.getStore().find('item_value',arr[i]);
				var record = this.getStore().getAt(index);
				res += record.get('item_name')+',';
			}
			if(res.length > 0){
				res = res.substring(0, res.lastIndexOf(','));
			}
		}
		return '<div ext:qtitle="" ext:qtip="' + res + '">' + res +'</div>';;
	},
	doReLoad: function(){
		if(this.getStore().baseParams['templateId']){
			this.getStore().removeAll();
			this.getStore().reload();
		}
	},
	isCellEditable:function(colIndex,rowIndex){
		var grid = this.scope;
		var record = grid.getStore().getAt(rowIndex);//当前编辑行对应record
		var type = record.get('type');
		var isEditable = record.get('is_editable');
		
		if( this.getIndexById('is_editable_id') != colIndex && isEditable == 'F')	return false;
		
		if(this.getIndexById('min_value_id') == colIndex
			|| this.getIndexById('max_value_id') == colIndex
			|| this.getIndexById('default_value_id') == colIndex){
			if(type == 'string') return false;
			
		}else if(this.getIndexById('item_key_id') == colIndex){
			if(type == 'number') return false;
		}else if(this.getIndexById('select_value_text_id') == colIndex){
			if(type == 'number') return false;
			var selectValueComp = this.getCellEditor(colIndex,rowIndex).field;
			selectValueComp.paramName = record.get('item_key');
			App.form.initComboData([selectValueComp]);
		}
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	},
	doBeforeEdit: function(obj){
		var record = obj.record;
		var fieldName = obj.field;
		var value = obj.value;
		
		if(fieldName == 'min_value'){
			record.get('min_value',Ext.util.Format.formatFee(value));
			record.commit();
		}else if(fieldName == 'max_value'){
			record.get('max_value',Ext.util.Format.formatFee(value));
		}else if(fieldName == 'default_value'){
			record.get('default_value',Ext.util.Format.formatFee(value));
		}
	},
	doAfterEdit: function(obj){
		var record = obj.record;
		var fieldName = obj.field;
		var value = obj.value;
		
		if(fieldName == 'min_value'){
			if(value > Ext.util.Format.formatFee(record.get('max_value'))
				|| value > Ext.util.Format.formatFee(record.get('default_value')) ){
				Alert('最小值不能大于最大值 或 不能大于默认值!',function(){
					this.startEditing(obj.row, obj.column);
				},this);
			}
			record.set('min_value',value * 100);
		}else if(fieldName == 'max_value'){
			if(value < Ext.util.Format.formatFee(record.get('min_value'))
				|| value < Ext.util.Format.formatFee(record.get('default_value')) ){
				Alert('最大值不能小于最小值 或 不能小于默认值!',function(){
					this.startEditing(obj.row, obj.column);
				},this);
			}
			record.set('max_value',value * 100);
		}else if(fieldName == 'default_value'){
			if( (record.get('min_value') != 0 && record.get('max_value') != 0)
			&& (value < Ext.util.Format.formatFee(record.get('min_value'))
				|| value > Ext.util.Format.formatFee(record.get('max_value'))) ){
				Alert('默认值不能小于最小值 或 不能大于最大值!',function(){
					this.startEditing(obj.row, obj.column);
				},this);
			}
			record.set('default_value',value * 100);
		}
	},
	doFocusFee: function(field){
		field.setValue(Ext.util.Format.formatFee(field.getValue()));
		field.focus(true);
	},
	goToOptr: function(type){
		var records = Ext.getCmp('templateRoleGridId').getSelectionModel().getSelections();
		if(records.length > 0 ){
			var columnIds = [];
	    	Ext.each(records, function(r){
	    		columnIds.push(r.get('column_id'));
	    	},this);
	        var win = new TemplateOptrWindow(type, columnIds);
	    	win.setTitle('分配模板->操作员');
	 		win.setIconClass('icon-add-user');   	
	 		win.show();
		}else{
			Alert('请选择要分配的数据行!');
		}
	},
	doSave: function(){
		this.stopEditing();
		var records = this.getStore().getModifiedRecords();
		if(records && records.length > 0){
			Confirm('确定保存吗?',this,function(){
				var arr = []
				Ext.each(records, function(record){
					var obj = {};
					Ext.apply(obj,record.data);
					arr.push(obj);
				});
				Ext.Ajax.request({
					url: root+'/config/Template!updateFeeColumn.action',
					params:{feeColumnStr:Ext.encode(arr)},
					scope:this,
					success:function(res,opt){
						var data = Ext.decode(res.responseText);
						if(data === true){
							this.getStore().removeAll();
							this.getStore().reload();
						}
					}
				});
			});
		}
	}
});

TemplateOptrTree = Ext.extend(Ext.ux.FilterTreePanel, {
    constructor: function (type,columnIds) {
    	
        var loader = new Ext.tree.TreeLoader({
        	url: root + "/config/Template!queryTemplateOptr.action",
        	baseParams: {
       			columnIds : columnIds,
       			type: type
        	},
			listeners:{
				scope:this,
				load: this.openNext
			}
        });
        TemplateOptrTree.superclass.constructor.call(this, {
            id: 'TemplateOptrTreeId',
            border:false,
            margins: '0 0 3 2',
            lines: false,
            autoScroll: true,
            animCollapse: true,
            animate: false,
            rootVisible : false,
            collapseMode: 'mini',
            bodyStyle: 'padding:3px',
            loader: loader,
            root: new Ext.tree.AsyncTreeNode()
        });
    },
    listeners: {
        'checkchange': function (node, checked) {
            node.expand();
            node.attributes.checked = checked;
            node.eachChild(function (child) {
                child.ui.toggleCheck(checked);
                child.attributes.checked = checked;
                child.fireEvent('checkchange', child, checked);
            });
        }
    },
    openNext:function(){
    	var childarr = this.getRootNode().childNodes;
		 if (childarr.length > 0) {
			for (var i = 0; i < childarr.length; i++) {
				if (childarr[i].loaded == false) {
                    childarr[i].expand();
                }
			}
		}
	}
});

TemplateOptrWindow = Ext.extend(Ext.Window, {
	templateOptrTree : null,
	columnIds:[],
	constructor : function(type, columnIds) {
		this.columnIds = columnIds;
		this.templateOptrTree = new TemplateOptrTree(type, columnIds);
		TemplateOptrWindow.superclass.constructor.call(this, {
					layout : 'fit',
					width : 400,
					height : 500,
					closeAction : 'hide',
					items : this.templateOptrTree,
					buttons : [{
						text : '保存',
						scope : this,
						handler :this.saveOptr
					},{
						text : '关闭',
						scope:this,
						handler:function(){
							this.hide();
						}
					}]
				});
	},
	saveOptr:function(){
		var optrItemId = [],all={};
        var nodes = this.templateOptrTree.getChecked();
        for (var i in nodes) {
            if (nodes[i].leaf) {
                optrItemId.push(nodes[i].id);
            }
        }
        if (optrItemId.length > 0) {
            all["optrIds"] = optrItemId;
        }
        all["columnIds"] = this.columnIds;
		Ext.Ajax.request({
			url : root + '/config/Template!saveTemplateToOptrs.action',
			params : all,
			scope: this,
			success : function(res, ops) {
				var rs = Ext.decode(res.responseText);
				if (true === rs.success) {
					Alert('操作成功!', function() {this.close();}, this);
				} else {Alert('操作失败!');}
			}
		});
	}
});

TemplateRoleView = Ext.extend(Ext.Panel, {
	constructor : function() {
		TemplateRoleView.superclass.constructor.call(this, {
					id : 'TemplateRoleView',
					title : '模板权限分配',
					closable : true,
					border : false,
					layout : 'border',
					items : [new TemplateFeeTree(), new TemplateRoleGrid()]
				});
	}
});