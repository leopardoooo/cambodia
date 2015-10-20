//中文表达式
CondInfo_CN = new Ext.XTemplate(
	'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
			'<tr height=24>',
				'<td class="input_bold" width=30%>&nbsp;{[values.rule_str_cn ||""]}</td>',
			'</tr>',
	'</table>'
);

//英文表达式
CondInfo_EN = new Ext.XTemplate(
	'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
		'<tpl if="values.dataArr != null">',
			'<tpl for="dataArr">',
				'<tr height=24 rowspan=2>',
					'<td class="input_bold" width=30%>{[values.value ||""]}</td>',
				'</tr>',
			'</tpl>',
		'</tpl>',
	'</table>'
);

/**
 * 规则grid
 * @class RuleGrid
 * @extends Ext.grid.GridPanel
 */
var RuleGrid = Ext.extend(Ext.grid.GridPanel,{
	ruleStore:null,
	constructor:function(){
		this.ruleStore = new Ext.data.JsonStore({
			url : root + '/config/Rule!queryAllRules.action',
			root:'records',
			totalProperty:'totalProperty',
			baseParams :{start:0,limit:Constant.DEFAULT_PAGE_SIZE},
			fields:['rule_id','rule_name','rule_str','remark','rule_str_cn',
				'rule_type','rule_type_text','data_type','cfg_type','cfg_type_text',
				'type_name','table_name','result_column','select_column','optr_id',
				{name:'eff_date',type:'date',dateFormat:'Y-m-d 00:00:00'},
				{name:'exp_date',type:'date',dateFormat:'Y-m-d 00:00:00'},
				'county_id','county_name']
		});
		
		this.ruleStore.load();
		
		var sm = new Ext.grid.CheckboxSelectionModel({});
		var currentOptrId = App.data.optr['optr_id'];
		var columns = [
			sm,
			{header:'规则ID',width:50,dataIndex:'rule_id',renderer:App.qtipValue},
			{header:'规则名称',dataIndex:'rule_name',renderer:App.qtipValue},
			{header:'规则类型',dataIndex:'rule_type_text',width:70},
			{header:'数据类型',dataIndex:'type_name',width:75},
			{header:'配置方式',dataIndex:'cfg_type_text',width:70},
			{header:'适用地区',dataIndex:'county_name',renderer:App.qtipValue},
			{header:'生效日期',dataIndex:'eff_date',width:80,renderer:Ext.util.Format.dateFormat},
			{header:'失效日期',dataIndex:'exp_date',width:80,renderer:Ext.util.Format.dateFormat},
			{id:'rule_remark_id',header:'描述',dataIndex:'remark'},
			{header:'操作',dataIndex:'optr_id',renderer:function(value,cellmeta,record,i){
				//admin账号 和 本人 才允许修改
				if(currentOptrId == '0' || currentOptrId == record.get('optr_id')){
					return "<a href='#' style='color:blue' onclick=Ext.getCmp('ruleGridId').doModify();>修改</a>&nbsp;&nbsp;&nbsp;";
				}else{
					return '';
				}
			}}
		];
		
		RuleGrid.superclass.constructor.call(this,{
			id:'ruleGridId',
			title:'规则信息',
			region:'north',
			height:300,
			collapsible:true,
			store:this.ruleStore,
			autoExpandColumn:'rule_remark_id',
			sm:sm,
			columns : columns,
			tbar:[' ', ' ', '输入关键字', ' ',
					new Ext.ux.form.SearchField({
								store : this.ruleStore,
								width : 200,
								hasSearch : true,
								emptyText : '支持规则名模糊查询'
							}),'-','配置:','-',
				{
					text : '手工配置',
					iconCls:'icon-hand',
					xtype : 'button',
					handler : this.doHand,
					scope : this/*,
					disabled : App.data.optr['county_id'] == '4501' ? false : true*/
				},'-',
				{text:'条件配置',iconCls:'icon-condition',handler:this.doSave,scope:this},'-',
				{text:'明细配置',iconCls:'icon-data',handler:function(){this.doDetail();},scope:this/*,
					disabled : App.data.optr['county_id'] == '4501' ? false : true*/},'-'
			],
			bbar:new Ext.PagingToolbar({store:this.ruleStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
			listeners:{
				rowclick:this.doRowDblClick
			}
		});
	},
	doRowDblClick:function(grid,rowIndex,e){
		var record = grid.getSelectionModel().getSelected();
		ruleCondPanel.tplCN.overwrite(ruleCondPanel.items.itemAt(0).body,record.data);
		ruleCondPanel.tplEN.overwrite(ruleCondPanel.items.itemAt(1).body,this.insertNull(record.data));
		var ruleId = record.get('rule_id');
		Ext.Ajax.request({
			url : root + '/config/Rule!queryRuleObjByRuleId.action',
			params:{ruleId:ruleId},
			success:function(res,opt){
				refObjGrid.getStore().loadData(Ext.decode(res.responseText));
			}
		});
	},
	insertNull:function(re){
		//表达式信息，如果是非汉字的话，不会显示全部，现在采用加空格来显示
		var record = re.rule_str;
		var num = 40;
		var arr = [];
		for(var i=0;i<record.length/num;i++){
			arr.push({value:record.substring(i*num,(i+1)*num)});
		}
		re.dataArr = arr;
		return re;
	},
	doSave:function(){
//		var win = Ext.getCmp('condWinId');
//		if(!win){
			win = new RuleWin();
//		}
		win.show.defer(1,win);
	},
	doHand:function(record){
		var win = new RuleHandWin(record);
		
		win.show();
	},
	doDetail:function(record){
		var win = Ext.getCmp('ruleDetailWinId');
		if(!win){
			win = new RuleDetailWin(record);
		}
		win.show.defer(100,win);
	},
	doModify:function(){
		var grid = Ext.getCmp('ruleGridId');
		var record = grid.getSelectionModel().getSelected();
		var cfgType = record.get('cfg_type');
		if(cfgType === 'COND'){
			
			win = new RuleWin(record);
			win.show();
			
			Ext.Ajax.request({
				url:root+'/config/Rule!queryRuleEditByRuleId.action',
				params:{ruleId:record.get('rule_id')},
				scope:this,
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					Ext.each(data,function(obj){
						obj['prop_id_name'] = obj['prop_name'];
						if(obj['data_type'] === 'F'){
							//属性类型:例如'地址编号' 值为function,要去除首位括号
							var propValue = 
								!Ext.isEmpty(obj['prop_value_text'])?
									obj['prop_value_text'].substring(1,obj['prop_value_text'].length-1):'';
							obj['prop_value_name'] = obj['prop_value_text'] = propValue;
						}else if(obj['data_type'] === 'N'){// 属性类型为缴费 money
							var propValue = !Ext.isEmpty(obj['prop_value_text'])?
								parseFloat(obj['prop_value']):'';
							obj['prop_value'] = obj['prop_value_name'] = obj['prop_value_text'] = propValue;
						}else{
							obj['prop_value_name'] = obj['prop_value_text'];
						}
					});
					
					var condGrid = Ext.getCmp('condGridId');
					condGrid.getStore().loadData(data);
					if(data.length>0){
						var num = 0;
						Ext.each(data,function(r){
							if(parseInt(r['row_idx']) > num){
								num = parseInt(r['row_idx']);
							}
						});
						dataRow = num + 1;
					}
				}
			});
		}else if(cfgType === 'HAND'){
			this.doHand(record);
		}else if(cfgType === 'DETAIL'){
			this.doDetail(record);
			var form = Ext.getCmp('ruleDetailFormId').getForm();
			form.loadRecord(record);
			var dataType = form.findField('data_type');
			
			//规则表达式
			var ruleStr = record.get('rule_str');
			//规则表达式中文名
//			var ruleStrCN = record.get('rule_str_cn');
			
			ruleStr = ruleStr.substring(ruleStr.indexOf('(')+1,ruleStr.lastIndexOf(')'));
			ruleStr = ruleStr.replace(/'([^']*)'/g, "$1");//将单引号更换为空字符
			ruleStr = ruleStr.split(',');
			
//			ruleStrCN = ruleStrCN.substring(ruleStrCN.indexOf('(')+1,ruleStrCN.lastIndexOf(')'));
//			ruleStrCN = ruleStrCN.split(',');
			var arr = [];
			for(var i=0;i<ruleStr.length;i++){
				arr.push([ruleStr[i],'']);
			}
			//已选项 数据
			form.findField('itemselector').multiselects[1].store.loadData(arr);
			
			dataType.fireEvent('select',dataType,record,0,arr);//触发data_type数据类型组件,查询“选择项”数据
		}
	}
});

/*条件配置开始*/
/*******************************************************************************/
/**
 * 当前规则对应中英文条件信息
 * @class RuleCondPanel
 * @extends Ext.Panel
 */
var RuleCondPanel = Ext.extend(Ext.Panel,{
	tplCN :null,
	tplEN :null,
	constructor:function(){
		this.tplCN = CondInfo_CN;
		this.tplEN = CondInfo_EN;
		this.tplCN.compile();
		this.tplEN.compile();
		RuleCondPanel.superclass.constructor.call(this,{
			id:'ruleCondPanelId',
			title:'条件信息',
			width:'40%',
			region:'west',
			layout:'anchor',
			collapsible:true,
			autoScroll:true,
			defaults: {
				anchor:'95%',
				style:'margin-left:15px;margin-top:15px;margin-right:15px;'
			},
			items:[{
				xtype:'fieldset',
				title:'中文信息',
				html : this.tplCN.applyTemplate({})
			},{
				xtype:'fieldset',
				title:'表达式信息',
				html : this.tplEN.applyTemplate({})
			}]
		});
	}
});

var RefObjGrid = Ext.extend(Ext.grid.GridPanel,{
	refObjStore :null,
	constructor:function(){
		this.refObjStore = new Ext.data.JsonStore({
			fields:['obj_type','obj_name','rule_id']
		});
		var columns = [
			{header:'对象类型',fields:'obj_type',width:100},
			{header:'对象名称',fields:'obj_name',width:250}
		];
		
		RefObjGrid.superclass.constructor.call(this,{
			id:'refObjGridId',
			title:'引用对象信息',
			region:'center',
			collapsible:true,
			store:this.refObjStore,
			columns:columns
		});
	}
});

var CondGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	condGridStore : null,
	leftBracketCombo: null,
	propIdCombo: null,
	operatorCombo: null,
	logicCombo: null,
	rightBarcketCombo: null,
	
	parent:null,
	constructor:function(p){
		this.parent = p;
		condGridThis = this;
		this.condGridStore = new Ext.data.JsonStore({
			pruneModifiedRecords:true,//每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
			fields:['rule_id','row_idx','left_bracket','prop_id','operator','prop_value','logic',
				'right_barcket','data_type','param_name','prop_name','prop_value_text'
				,'prop_id_name','prop_value_name'
				]
		});
		
		this.leftBracketCombo = new Ext.form.ComboBox({
			store:['(','((','(((','((((',')))))'],
			forceSelection:true,selectOnFocus:true,editable:true
		});
		
		this.logicCombo = new Ext.form.ComboBox({
			store:[' or ',' and '],
			forceSelection:true,selectOnFocus:true,editable:true
		});
		
		this.operatorCombo = new Ext.form.ComboBox({
			store:['==','!=','>','>=','<','<=','+','-','*','/','%'],
			forceSelection:true,selectOnFocus:true,editable:true
		});
		
		this.rightBarcketStore = new Ext.form.ComboBox({
			store:[')','))',')))','))))',')))))'],
			forceSelection:true,selectOnFocus:true,editable:true
		});
		
		var cm = new Ext.grid.ColumnModel([
			{header:'行号',dataIndex:'row_idx',width:35},
			{header:'括号',dataIndex:'left_bracket',width:45,editor:this.leftBracketCombo},
			{id:'prop_id_id',header:'属性',dataIndex:'prop_id_name',width:150,
				editor:new Ext.form.TextField({})
			},
			{id:'operator_id',header:'运算符',dataIndex:'operator',width:65,editor:this.operatorCombo},
			{id:'prop_value_id',header:'值',dataIndex:'prop_value_name',width:180,
				editor:new Ext.form.TextField({})
			},
			{header:'括号',dataIndex:'right_barcket',width:45,editor:this.rightBarcketStore},
			{header:'逻辑符',dataIndex:'logic',width:50,editor:this.logicCombo},
			{header:'操作',dataIndex:'rule_id',width:40,renderer:function(value,meta,record){
					return "<a href='#' onclick=condGridThis.deleteRow()>删除</a>"
				}
			}
		]);
		
		cm.isCellEditable = this.cellEditable;
		
		CondGrid.superclass.constructor.call(this,{
			id:'condGridId',
			title:'条件信息',
			region:'center',
			collapsible:true,
			height:300,
			clicksToEdit:1,
			store:this.condGridStore,
			cm:cm,
			sm:new Ext.grid.RowSelectionModel({}),
			tbar:[{text:'新增',handler:this.doAdd,scope:this}]
		});
	},
	deleteRow:function(){
		this.getStore().remove(this.getSelectionModel().getSelected());
	},
	setPropNameForTreeCombo:function(combo,record,index){
		var condRecord = Ext.getCmp('condGridId').getSelectionModel().getSelected();
		condRecord.set('prop_value_text',record.text);

	},
	setPropNameForParamCombo:function(combo,record,index){
		var condRecord = Ext.getCmp('condGridId').getSelectionModel().getSelected();
		condRecord.set('prop_value_text',record.get('item_name'));
		condRecord.set('prop_value',record.get('item_value'));

	},
	initEvents:function(){
		CondGrid.superclass.initEvents.call(this);
		this.on("beforeedit",function(obj){
			var record = obj.record;
			var fieldName = obj.field;//编辑的column对应的dataIndex
			var rowIndex = obj.row;
			var dataType = record.get('data_type');
			
			if(fieldName === 'prop_id_name'){//属性列
				var propIdCm = this.getColumnModel().getColumnById('prop_id_id');
				if(ruleDataType){
					propIdCm.editor = new Ext.form.ComboBox({
						store:new Ext.data.JsonStore({
							url:root+'/config/Rule!queryRulePropByDataRightType.action',
							fields:['prop_id','prop_name','param_name','data_type']
						}),
						forceSelection:true,selectOnFocus:true,editable:true,
						triggerAction:'all',mode:'local',displayField:'prop_name',valueField:'prop_name',
						listeners:{
							scope:this,
							delay:100,
							select:function(combo,record){
								var condRecord = Ext.getCmp('condGridId').getSelectionModel().getSelected();
								condRecord.set('param_name',record.get('param_name'));
								condRecord.set('data_type',record.get('data_type'));
								condRecord.set('prop_name',record.get('prop_name'));
								condRecord.set('prop_id',record.get('prop_id'));
								
								condRecord.set('prop_value_name','');
								condRecord.set('prop_value_text','');
								condRecord.set('prop_value','');
							}
						}
					});
					propIdCm.editor.getStore().load({params:{dataType:ruleDataType}});
				}else
					propIdCm.editor = new Ext.ux.TreeCombo({
						store:new Ext.data.JsonStore({
							fields:['id','text','others']
						}),valueField:'text',
				    	width:140,
						treeWidth:400,
						minChars:2,
						height: 22,
						allowBlank: false,
						treeUrl: root + '/config/Rule!queryRuleProp.action'
						,listeners:{
							delay:100,
							scope:this,
							select:function(combo,record,index){
								var condRecord = Ext.getCmp('condGridId').getSelectionModel().getSelected();
								condRecord.set('param_name',record.attributes.others['param_name']);
								condRecord.set('data_type',record.attributes.others['data_type']);
								condRecord.set('prop_name',record.text);
								condRecord.set('prop_id',record.id);
								
								condRecord.set('prop_value_name','');
								condRecord.set('prop_value_text','');
								condRecord.set('prop_value','');
							}
						}
					});
			}else if(fieldName == 'prop_value_name'){//值列
				//"属性"列号columnIndex
				var propValueCm = this.getColumnModel().getColumnById('prop_value_id');//"属性值"编辑组件
				if(dataType === 'D' || dataType === 'S'){
					var paramNameValue = record.get('param_name');
					if(paramNameValue){
						var paramCombo = new Ext.ux.ParamCombo({
							allowBlank:false,forceSelection:false,selectOnFocus:true,typeAhead:false,
							editable : true,
							paramName:record.get('param_name'),valueField:'item_name'
							,listeners:{
								scope:this,
								select:this.setPropNameForParamCombo
							}
						});
						propValueCm.editor = paramCombo;
						App.form.initComboData( [paramCombo]);
					}else{
						var paramTextField = new Ext.form.TextField({
							allowBlank:false,
							listeners:{
							scope:this,
							blur:function(field){
								var condRecord = Ext.getCmp('condGridId').getSelectionModel().getSelected();
								var value = field.getValue();
								condRecord.set('prop_value_text',value);
								condRecord.set('prop_value',value);
							}
						}
						});
						propValueCm.editor = paramTextField;
					}
					record.set('prop_name',record.get('prop_name'));
				}else if(dataType === 'F'){
					var propTreeCombo = new Ext.ux.TreeCombo({
				    	width:140,
						treeWidth:400,
						minChars:2,
						height: 22,valueField:'text',
						allowBlank: false,forceSelection:true,selectOnFocus:true,
						treeUrl: root+"/config/Rule!findAllAddress.action"
						,listeners:{
							delay:100,
							scope:this,
							select:function(combo,record,index){
								var condRecord = Ext.getCmp('condGridId').getSelectionModel().getSelected();
								condRecord.set('prop_value_text','('.concat(record.text,')'));
								condRecord.set('prop_value',record.id);
							}
						}
				    });
				    propValueCm.editor = propTreeCombo;
						
				}else if(dataType === 'N'){
					propValueCm.editor = new Ext.form.NumberField({
						allowNegative:false,allowBlank:false,
						listeners:{
							scope:this,
							blur:function(field){
								var condRecord = Ext.getCmp('condGridId').getSelectionModel().getSelected();
								var money = parseFloat(field.getValue());
								condRecord.set('prop_value_text',money);
								condRecord.set('prop_value',money);
							}
						}
					});
					propValueCm.renderer =function(value){if(value)return value;return 0};
				}
			}
		},this);
	},
	cellEditable:function(colIndex, rowIndex){
		var operatorIndex = this.getIndexById("operator_id");
		var propValueIndex = this.getIndexById("prop_value_id");
		
		var record = condGridThis.getStore().getAt(rowIndex);
		var propIdValue = record.get('prop_id');
		
		var dataType = record.get('data_type');
		//"值"列
		if(propValueIndex == colIndex){
			if(Ext.isEmpty(propIdValue))
				return false;
		}else if(operatorIndex == colIndex){
			if(dataType == 'F'){
				record.set('operator','');
				//为函数时,无运算符
				return false;
			}
		}
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	},
	doAdd:function(){
		var dataType = Ext.getCmp('rule_data_type_id');
		
		if(Ext.getCmp('rule_rule_type_id').getValue() === 'DATA' && Ext.isEmpty(dataType.getValue())){
			Alert('请先选择数据类型',function(){
			 	dataType.focus(true,100);
			 });
			return;
		}
		
		var recordData = this.parent.ruleCondForm.recordData;
		var ruleId='';
		if(recordData){
			ruleId = recordData.get('rule_id');
		}
		var ruleEditRecord = this.getStore().recordType;
		var record = new ruleEditRecord({
			rule_id:ruleId,row_idx:dataRow,left_bracket:'',prop_id:'',prop_id_name:'',prop_value_name:'',
			operator:'',prop_value:'',logic:'',right_barcket:'',data_type:'',param_name:''
		});
		dataRow++;
		this.stopEditing();
		
		var selectRecord = this.getSelectionModel().getSelected();
		var index = this.getStore().indexOf(selectRecord);
		if(index > 0){
			index = index + 1;
			var currRowIdx = selectRecord.get('row_idx') + 1;
			record.set('row_idx',currRowIdx);
			this.getStore().insert(index,record);
			this.startEditing(index,1);
			this.getSelectionModel().selectRow(index);
			this.getStore().each(function(r){
				if(r.get('row_idx') >= currRowIdx && r != record ){
					r.set('row_idx',r.get('row_idx')+1);
				}
			});
		}else{
			this.getStore().insert(this.getStore().getCount(),record);
			this.startEditing(this.getStore().getCount()-1,1);
			this.getSelectionModel().selectRow(this.getStore().getCount()-1);
		}
	}
});
var dataRow=0;
//条件规则配置中，用户选中的数据类型，
//用于条件信息编辑框中选择对应属性和值
//有且仅当规则类型为数据规则时才不为空，其它需清空
var ruleDataType=null;

var RuleCondForm = Ext.extend(Ext.form.FormPanel,{
	recordData:null,
	parent:null,
	constructor:function(p,data){
		this.parent=p;
		this.recordData = data;
		RuleCondForm.superclass.constructor.call(this,{
			id:'ruleCondFormId',
			title:'规则信息',
			split : true,
			height : 210,
			layout : 'border',
			defaults : {
				bodyStyle : "background:#F9F9F9"
			},
			items : [{
				region : 'center',
				layout : 'column',
				border : false,
				bodyStyle : 'padding: 10px',
				defaults : {
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100
				},
				items : [
					{xtype:'hidden',name:'rule_id'},
					{xtype:'hidden',name:'rule_str'},
					{xtype:'hidden',name:'rule_str_cn'},
					{xtype:'hidden',name:'cfg_type'},
					{columnWidth:.5,border:false,items:[
						{fieldLabel:'规则名',xtype:'textfield',name:'rule_name',allowBlank:false},
						{xtype:'combo',fieldLabel:'规则类型',
							hiddenName:'rule_type',allowBlank:false,id:'rule_rule_type_id',
							store:[['BUSI','业务规则'],['DATA','数据规则']],
							listeners:{
									scope:this,
									select:function(combo,record){
										var comp = Ext.getCmp('rule_data_type_Itemid');
										var value=combo.getValue();
										if(value === 'DATA'){
											if(comp.hidden){
												comp.show();
												comp.items.itemAt(0).getStore().load();
											}
										}else{
											if(!comp.hidden)comp.hide();
											//当用户选择业务类型时
											ruleDataType = null;
										}
									}
								}
							}
					]},
					{columnWidth:.5,border:false,items:[
						{xtype:'datefield',fieldLabel:'生效日期',name:'eff_date',id:'rule_eff_date_id',
								vtype:'daterange',endDateField : 'rule_exp_date_id',
								format:'Y-m-d',allowBlank:false,value:new Date().format('Y-m-d')},
							{xtype:'datefield',fieldLabel:'失效日期',name:'exp_date',id:'rule_exp_date_id',
								vtype:'daterange',startDateField : 'rule_eff_date_id',
								format:'Y-m-d',minValue:nowDate().format('Y-m-d')}
					]},
					{id:'rule_data_type_Itemid',columnWidth:1,items:[
						{id:'rule_data_type_id',fieldLabel:'数据类型',hiddenName:'data_type',
							xtype:'combo',allowBlank:false,
							store:new Ext.data.JsonStore({
								url:root+'/config/Rule!queryAllSDataType.action',
								fields:['data_right_type','type_name','table_name','result_column','select_column']
								,listeners:{
									scope:this,
									load:function(store,records,options){
										//store加载完后再加载选中要修改的记录
										if(this.recordData)
											Ext.getCmp('rule_data_type_id').setValue(this.recordData.get('data_type'));
									}
								}
							}),displayField:'type_name',valueField:'data_right_type'
							,listeners:{
								scope:this,
								select:function(combo,record){
									ruleDataType = combo.getValue();
									Ext.getCmp('condGridId').getStore().removeAll();
								}
							}
					}]},
					{columnWidth:1,border:false,items:[
						{xtype:'lovcombo',fieldLabel:'适用地区',hiddenName:'county_id',
							store:new Ext.data.JsonStore({
								url:root+'/config/Rule!queryRuleCounty.action',
								fields:['county_name','county_id'],
								listeners:{
									scope:this,
									load:function(store,records){
										if(store.getCount() == 1){
											this.getForm().findField('county_id')
												.setValue(records[0].get('county_id'));
										}
										if(data)
											this.getForm().findField('county_id')
												.setValue(data.get('county_id'));
									}
								}
							}),displayField:'county_name',valueField:'county_id',
							triggerAction:'all',mode:'local',editable:false,allowBlank:false,width:300,
							beforeBlur:function(){}
						}
					]},
					{columnWidth:1,border:false,items:[
						{xtype:'textarea',fieldLabel:'描述',name:'remark',anchor:'90%',height:60}
					]}
				]
			}]
		});
	},
	initComponent:function(){
		RuleCondForm.superclass.initComponent.call(this);
		if(this.recordData){
			this.getForm().loadRecord(this.recordData);
			if(this.recordData.get('rule_type')=='DATA'){
				Ext.getCmp('rule_rule_type_id').fireEvent('select',Ext.getCmp('rule_rule_type_id'));
			}
		}
		this.getForm().findField('county_id').getStore().load();
	},
    initEvents: function () {
        this.on('afterrender', function () {
        	Ext.getCmp('rule_data_type_Itemid').hide();
        });
	    this.doLayout();
        RuleCondForm.superclass.initEvents.call(this);
    },
	loadData:function(){
		if(this.recordData){
			if(this.recordData.get('data_type')){
				Ext.getCmp('rule_data_type_Itemid').show();
				Ext.getCmp('rule_data_type_Itemid').items.itemAt(0).getStore().load();
				
				//条件信息编辑框中用于过滤属性和值
				ruleDataType = this.recordData.get('data_type');
				
				Ext.getCmp('rule_rule_type_id').setReadOnly(true);
				
			}else
				this.getForm().loadRecord(this.recordData);
		}
	}
});

var RuleWin = Ext.extend(Ext.Window,{
	ruleCondForm: null,
	condGrid: null,
	constructor:function(data){
		this.ruleCondForm = new RuleCondForm(this,data);
		this.condGrid = new CondGrid(this);
		RuleWin.superclass.constructor.call(this,{
			height:550,
			width:650,
			title:'条件规则配置',
			plain:true,
			closeAction:'close',
			maximizable:false,
			animate:true,
			border:false,
			modal: true,
			items:[this.ruleCondForm,this.condGrid],
			buttons:[
				{text:'保存',iconCls:'icon-save',handler:this.doSave,scope:this},
				{text:'关闭',iconCls:'icon-close',handler:function(){this.close();},scope:this}
			]
			,listeners:{
				scope:this,
				close:function(){
					dataRow=0;
					ruleDataType = null;//清空数据类型
				}
			}
		});
	},
	//将null装换称空字符
	checkRecord :function(record){
		var obj = {};
		obj['rule_id']=record.get('rule_id');
		obj['row_idx']=record.get('row_idx');
		obj['left_bracket']=Ext.isEmpty(record.get('left_bracket'))?'':record.get('left_bracket');
		obj['prop_id']=record.get('prop_id');
		obj['operator']=Ext.isEmpty(record.get('operator'))?'':record.get('operator');
		obj['prop_value']=record.get('prop_value');
		obj['logic']=Ext.isEmpty(record.get('logic'))?'':record.get('logic');
		obj['right_barcket']=Ext.isEmpty(record.get('right_barcket'))?'':record.get('right_barcket');
		obj['data_type']=record.get('data_type');
		obj['param_name']=Ext.isEmpty(record.get('param_name'))?'':record.get('param_name');
		obj['prop_name']=record.get('prop_name');
		obj['prop_value_text']=record.get('prop_value_text');
		return obj;
	},
	filterRecord:function(record){
		var prop_value = record.get('prop_value');
		/**
		 * 'F'代表函数 'prop_id'为函数名 'prop_value'为字符型参数
		 *  (例：inAddr('wuhan')，其中"inAddr"为prop_id,"('wuhan')"为prop_value)
		*/
		if(record.get('data_type') == 'F'){
			prop_value = '("'+record.get('prop_value')+'")';
		}else if(record.get('data_type') == 'S'){
			//字符型 加引号
			prop_value = '"'.concat(record.get('prop_value').toString(),'"');
		}else if(record.get('data_type') == 'N'){
			//缴费 数字型 乘以100转化为"分"
			if(Ext.isEmpty(record.get('prop_value'))){
				Alert('请输入数字值');
				return;
			}
			prop_value = parseInt(record.get('prop_value'))*100;
		}
		var ruleStr='',propName='';
		ruleStr = ruleStr.concat(record.get('left_bracket')||'',record.get('prop_id'),record.get('operator')||'',
			prop_value,record.get('right_barcket')||'',' ',record.get('logic')||'',' ');
		propName = propName.concat(record.get('left_bracket')||'',record.get('prop_name')||'',record.get('operator')||'',
			record.get('prop_value_text')||'',record.get('right_barcket')||'',' ',record.get('logic')||'',' ')
		return {'ruleStr':ruleStr,'propName':propName};
	},
	doSave:function(){
		var form = this.ruleCondForm.getForm();
		
		var valid = true;
		//由于有2个组件会隐藏，修改basicForm验证方法
		form.items.each(function(f) {
			if (!f.validate()
					&& f.ownerCt.hidden === false) {
				valid = false;
			}
		});
        if(!valid)return;
		
		this.condGrid.stopEditing();//停止编辑
		var formValues = form.getValues();
		
		var store = this.condGrid.getStore();
		store.sort('row_idx','ASC');
		
		var records = store.getModifiedRecords();//更新过的record集合(只将修改过的record传后台)
		
		var arr = [];//当前规则的所有条件集合
		var ruleStr = '';//一条'条件'记录的规则表达式集合
		var propName = '';
		
		if(store.getCount() === 0){
			Alert('请输入条件信息！');
			return;
		}
		
		store.each(function(record){
			//必须输入"属性"列
			if(Ext.isEmpty(record.get('prop_id'))){
				return false;
			}
			
			arr.push(this.checkRecord(record));
			var fr = this.filterRecord(record);
			ruleStr = ruleStr.concat(fr['ruleStr']);
			propName = propName.concat(fr['propName']);
		},this);
		if(arr.length == 0){
			Alert('请完整输入规则条件后再保存!');
			return;
		}
		
		formValues['rule_str'] = ruleStr;
		formValues['rule_str_cn'] = propName;
		formValues['optr_id'] = App.data.optr['optr_id'];
		formValues['eff_date'] = Date.parseDate(formValues['eff_date'],'Y-m-d');
		formValues['exp_date'] = Date.parseDate(formValues['exp_date'],'Y-m-d');
		
		var ruleEditDto={};
		ruleEditDto.rule = formValues;
		ruleEditDto.ruleEdits = arr;
		
//		var msg = Show();
		Ext.Ajax.request({
			url:root+'/config/Rule!updateRuleAndRuleEdit.action',
			params:{ruleEditDto:Ext.encode(ruleEditDto)},
			scope:this,
			success:function(){
//				msg.hide();msg = null;
				Alert('保存成功',function(){
					this.close();
					Ext.getCmp('ruleGridId').getStore().reload();
				},this);
			}
		});
	}
});

/*******************************************************************************/
/*条件配置结束*/

/*手工配置开始*/
/*******************************************************************************/
var handRuleType = null;
var RuleHandWin = Ext.extend(Ext.Window,{
	record : null,
	constructor:function(record){
		if(undefined!=record.data){
			this.record = record;
		} 
		RuleHandWin.superclass.constructor.call(this,{
			id:'ruleHandWinId',
			title:'手工规则配置',
			closeAction:'close',
			height:470,
			width:500,
			border:false,
			items:[{
				layout:'column',xtype:'form',id:'ruleHandFormId',defaults:{layout:'form'},border:false,
					bodyStyle:'padding:10px',labelWidth:80,items:[
					{xtype:'hidden',name:'rule_id'},
					{xtype:'hidden',name:'rule_str_cn'},
					{xtype:'hidden',name:'cfg_type'},
					{columnWidth:.5,border:false,items:[
						{xtype:'textfield',fieldLabel:'规则名',allowBlank:false,name:'rule_name'},
						{xtype:'combo',fieldLabel:'规则类型',allowBlank:false,hiddenName:'rule_type',
							store:new Ext.data.ArrayStore({fields:['rule_type_text','rule_type']}),
							displayField:'rule_type_text',valueField:'rule_type',
							listeners:{
								scope:this,
								select:function(combo,record){
									var comp = Ext.getCmp('handRule_data_type_itemId');
									var value=combo.getValue();
									if(value === 'DATA'){
										handRuleType = 'DATA';
										if(comp.hidden){
											comp.show();
											comp.items.itemAt(0).getStore().load();
										}
									}else{
										if(!comp.hidden)comp.hide();
										//当用户选择业务类型时
										handRuleType = null;
									}
								}
							}
						}
					]},
					{columnWidth:.5,border:false,items:[
						{xtype:'datefield',fieldLabel:'生效日期',name:'eff_date',id:'hand_eff_date_id',
								vtype:'daterange',endDateField : 'hand_exp_date_id',
								format:'Y-m-d',allowBlank:false,value:new Date().format('Y-m-d')},
							{xtype:'datefield',fieldLabel:'失效日期',name:'exp_date',id:'hand_exp_date_id',
								vtype:'daterange',startDateField : 'hand_eff_date_id',
								format:'Y-m-d',minValue:nowDate().format('Y-m-d')}
					]},
					{id:'handRule_data_type_itemId',columnWidth:1,border:false,items:[
						{id:'hand_data_type_id',fieldLabel:'数据类型',hiddenName:'data_type',
							xtype:'combo',
							store:new Ext.data.JsonStore({
								url:root+'/config/Rule!queryAllSDataType.action',
								fields:['data_right_type','type_name','table_name',
									'result_column','select_column'],
								listeners:{
									scope:this,
									load:function(store,records,options){
										if(this.record)
											Ext.getCmp('hand_data_type_id').setValue(this.record.get('data_type'));
									}
								}
							}),displayField:'type_name',valueField:'data_right_type'
						}
					]},
					{columnWidth:1,border:false,items:[
						{xtype:'lovcombo',fieldLabel:'适用地区',hiddenName:'county_id',
							store:new Ext.data.JsonStore({
								url:root+'/config/Rule!queryRuleCounty.action',
								fields:['county_name','county_id'],
								listeners:{
									load:function(store,records){
										if(store.getCount() == 1){
											Ext.getCmp('ruleHandFormId').getForm().findField('county_id')
												.setValue(records[0].get('county_id'));
										}
										if(record)
											Ext.getCmp('ruleHandFormId').getForm().findField('county_id')
												.setValue(record.get('county_id'));
									}
								}
							}),displayField:'county_name',valueField:'county_id',
							triggerAction:'all',mode:'local',editable:false,allowBlank:false,width:300,
							beforeBlur:function(){}
						},
						{xtype:'textarea',fieldLabel:'规则表达式',allowBlank:false,name:'rule_str',width:350,height:150},
						{xtype:'textarea',fieldLabel:'备注',name:'remark',width:350,height:150}
					]}
				]
			}],
			buttonAlign:'center',
			buttons:[
				{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){this.close();} }
			]
		});
	},
	initComponent:function(){
		RuleHandWin.superclass.initComponent.call(this);
		
		var store = Ext.getCmp('ruleHandFormId').getForm().findField('rule_type').getStore();
		if(App.data.optr['county_id'] == '4501'){
			store.loadData([['业务规则','BUSI'],['数据规则','DATA'],['计费规则','BILL'],
				['批价规则','USEFEE'],['巡检规则','TASK_CJ'],['停机规则','TASK_TJ']]);
		}else{
			store.loadData([['业务规则','BUSI'],['数据规则','DATA']]);
		}
		
		Ext.getCmp('ruleHandFormId').getForm().findField('county_id').getStore().load();
		if(this.record){
 			if(this.record.get('rule_type')=='DATA'){
				Ext.getCmp('hand_data_type_id').fireEvent('select', Ext.getCmp('hand_data_type_id'));
			}
			Ext.getCmp('ruleHandFormId').getForm().loadRecord(this.record);
		}
	},
    initEvents: function () {
        this.on('afterrender', function () {
        	Ext.getCmp('handRule_data_type_itemId').hide();
        });
	    this.doLayout();
        RuleHandWin.superclass.initEvents.call(this);
    },
	show:function(){
		RuleHandWin.superclass.show.call(this);
		this.find('name','rule_name')[0].focus(true,100);
		if(this.record){
			if(this.record.get('rule_type') == 'DATA'){
				var comp = Ext.getCmp('handRule_data_type_itemId');
				comp.show();
				var combo = comp.items.itemAt(0);
				combo.getStore().load();
				combo.setReadOnly(true);
			}
		}
	},
	doSave:function(){
		
		var form = Ext.getCmp('ruleHandFormId').getForm();
		if( !form.isValid())return;
		var values = form.getValues();
		
		var obj={};
		for(var o in values){
			obj['rule.'.concat(o)]=values[o];
		}
		
		//操作员信息
		obj['rule.optr_id'] = App.data.optr['optr_id'];
		obj['rule.eff_date'] = Date.parseDate(obj['rule.eff_date'],'Y-m-d');
		obj['rule.exp_date'] = Date.parseDate(obj['rule.exp_date'],'Y-m-d');
		Ext.Ajax.request({
			url:root+'/config/Rule!updateHandRule.action',
			params:obj,
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data.success === true){
					Alert('保存成功',function(){
						this.close();
						Ext.getCmp('ruleGridId').getStore().reload();
					},this);
				}
			}
		});
	},
	loadData:function(){
		if(this.recordData){
			if(this.recordData.get('data_type')){
				Ext.getCmp('rule_data_type_Itemid').show();
				Ext.getCmp('rule_data_type_Itemid').items.itemAt(0).getStore().load();
				
				//条件信息编辑框中用于过滤属性和值
				ruleDataType = this.recordData.get('data_type');
				
				Ext.getCmp('rule_rule_type_id').setReadOnly(true);
				
			}else
				this.getForm().loadRecord(this.recordData);
		}
	}
});

/*******************************************************************************/
/*手工配置结束*/

/*明细配置开始*/
/*******************************************************************************/

var RuleDetailForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(record){
		RuleDetailForm.superclass.constructor.call(this,{
			id:'ruleDetailFormId',
			height : 460,
			bodyStyle : 'padding: 10px',
			layout:'column',
			defaults : {
				labelWidth : 70,
				layout:'form'
			},
			items:[
				{xtype:'hidden',name:'rule_id'},
				{xtype:'hidden',name:'rule_str'},
				{xtype:'hidden',name:'rule_str_cn'},
				
				{xtype:'hidden',name:'rule_type'},
				{xtype:'hidden',name:'cfg_type'},
				{xtype:'hidden',name:'result_column'},
				{xtype:'hidden',name:'select_column'},
				
				{columnWidth:.5,border:false,items:[
					{xtype:'textfield',fieldLabel:'规则名',name:'rule_name',allowBlank:false},
					{fieldLabel:'数据类型',hiddenName:'data_type',xtype:'combo',allowBlank:false,
					typeAhead: true,mode:'local',minChars:2,editable:true,
						store:new Ext.data.JsonStore({
							url:root+'/config/Rule!queryAllSDataType.action',
							fields:['data_right_type','type_name','table_name','result_column','select_column']
						}),displayField:'type_name',valueField:'data_right_type',
						listeners:{
							scope:this,
							select:this.doSelectedDataType
						}
					}
				]},
				{columnWidth:.5,border:false,items:[
					{xtype:'datefield',fieldLabel:'生效日期',name:'eff_date',id:'detail_eff_date_id',
								vtype:'daterange',endDateField : 'detail_exp_date_id',
								format:'Y-m-d',allowBlank:false,value:new Date().format('Y-m-d')},
					{xtype:'datefield',fieldLabel:'失效日期',name:'exp_date',id:'detail_exp_date_id',
						vtype:'daterange',startDateField : 'detail_eff_date_id',
						format:'Y-m-d',minValue:nowDate().format('Y-m-d')}
				]},
				{columnWidth:1,border:false,items:[
					{xtype:'lovcombo',fieldLabel:'适用地区',hiddenName:'county_id',
						store:new Ext.data.JsonStore({
							url:root+'/config/Rule!queryRuleCounty.action',
							fields:['county_name','county_id'],
							listeners:{
								scope:this,
								load:function(store,records){
									if(store.getCount() == 1){
										this.getForm().findField('county_id')
											.setValue(records[0].get('county_id'));
									}
									if(record)
										Ext.getCmp('ruleDetailFormId').getForm().findField('county_id')
											.setValue(record.get('county_id'));
								}
							}
						}),displayField:'county_name',valueField:'county_id',
						triggerAction:'all',mode:'local',editable:false,allowBlank:false,width:300,
						beforeBlur:function(){}
					},
					{xtype:'textarea',fieldLabel:'描述',name:'remark',width:320,height:80},
					{xtype:'itemselector',name:'itemselector',fieldLabel:'数据项',allowBlank:false,
						imagePath:'/'+Constant.ROOT_PATH_LOGIN+'/resources/images/itemselectorImage',
						multiselects:[{
			            	legend:'待选项',
			                width: 150,
			                height: 270,
			                store: new Ext.data.ArrayStore({fields:['result_column','select_column']}),
			                displayField: 'select_column',
			                valueField: 'result_column',
			                tbar:['过滤:',{
			                	xtype:'textfield',
			                	scope:this,
			                	enableKeyEvents: true,
                                listeners: {
                                    scope: this,
                                    keyup: function (txt, e) {
                                        if (e.getKey() == Ext.EventObject.ENTER) {
                                        	this.doItemSelect(txt.getValue(),this.getForm().findField('itemselector').multiselects[0].store);
                                        }
                                    }
                                }
			                }]
			            },{
				           	legend:'已选项',
			                width: 150,
			                height: 270,
			                store: new Ext.data.ArrayStore({fields:['result_column','select_column']}),
			                displayField:'select_column',valueField:'result_column',
			                tbar:['过滤:',{
			                	xtype:'textfield',
			                	scope:this,
			                	enableKeyEvents: true,
                                listeners: {
                                    scope: this,
                                    keyup: function (txt, e) {
                                        if (e.getKey() == Ext.EventObject.ENTER) {
                                        	this.doItemSelect(txt.getValue(),this.getForm().findField('itemselector').multiselects[1].store);
                                        }
                                    }
                                }
			                }]
					}
				]}
			]}
			]
		});
	},
	initComponent:function(){
		RuleDetailForm.superclass.initComponent.call(this);
		this.getForm().findField('data_type').getStore().load();
		this.getForm().findField('county_id').getStore().load();
	},
	doItemSelect: function(value,store){
        store.clearFilter();
        if (Ext.isEmpty(value)) return;
        store.filterBy(function (record){
            return record.get('select_column').indexOf(value) >= 0;
        }, this);
	},
	doSelectedDataType:function(combo,record,index,arr){//arr 为修改时传进来的‘已选项’数据
		var tableName = record.get('table_name');
		var resultColumn = record.get('result_column');
		var selectColumn = record.get('select_column');
		this.getForm().findField('result_column').setValue(resultColumn);
		this.getForm().findField('select_column').setValue(selectColumn);
		
		var item = this.getForm().findField('itemselector');
		//添加状态下，若选择数据类型，已选项框数据清空
		if(Ext.isEmpty(arr))
			item.multiselects[1].store.removeAll();
		
		Ext.Ajax.request({
			url:root+'/config/Rule!queryDynamicData.action',
			params:{
				tableName:tableName,
				resultColumn:resultColumn,
				selectColumn:selectColumn
			},
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data){
					if(arr){
						//data 为所有数据，arr为‘已选项数据’
						//将data中删除arr，为‘待选项’的数据
						for(var i=0;i<arr.length;i++){
							for(var j=0;j<data.length;j++){
								if(arr[i][0] === data[j][0]){
									arr[i][1]=data[j][1]
									data.remove(data[j]);
									break;
								}
							}
						}
					}
					//待选项加载数据
					item.multiselects[0].store.loadData(data);
					item.multiselects[1].store.loadData(arr);
				}else{
					item.multiselects[0].store.removeAll();
					item.multiselects[1].store.removeAll();
				}
			}
		});
	}
});

var RuleDetailWin = Ext.extend(Ext.Window,{
	form:null,
	constructor:function(record){
		this.form = new RuleDetailForm(record);
		RuleDetailWin.superclass.constructor.call(this,{
			id:'ruleDetailWinId',
			title:'规则明细配置',
			border:false,
			width:500,
			height:500,
			items:[this.form],
			closeAction:'close',
			buttonAlign:'center',
			buttons:[
				{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){this.close();}}
			]
		});
	},
	doSave:function(){
		var form = this.form.getForm();
		if( !form.isValid())return;
		var values = form.getValues();
		var store = form.findField('itemselector').toMultiselect.view.store;
		if(store.getCount() === 0 ){
			Alert('请选择数据项!');
			return;
		}
		var arr = [];
		//规则表达式的显示名
		store.each(function(record){
			arr.push(record.get('select_column') );
		});
		
		var itemIdArr = values['itemselector'].split(',');//值id
		var ruleStr = '';
		Ext.each(itemIdArr,function(data){
			ruleStr += "''"+data+"'',";
		});
		
		ruleStr = ruleStr.substring(0,ruleStr.length-1);
		values['rule_str'] = values['result_column']+' in ('+ruleStr+')';//拼装where语句
		values['rule_str_cn'] = values['select_column'] + ' in ('+arr.join(',')+')';
		
		delete values['result_column'];
		delete values['select_column'];
		
		var obj = {};
		for(var v in values){
			if(v !== 'itemselector')
				obj['rule.'+v] = values[v];
		}
		
		//操作员信息
		obj['rule.optr_id'] = App.data.optr['optr_id'];
		obj['rule.eff_date'] = Date.parseDate(obj['rule.eff_date'],'Y-m-d');
		obj['rule.exp_date'] = Date.parseDate(obj['rule.exp_date'],'Y-m-d');
		var msg = Show();
		Ext.Ajax.request({
			url:root+'/config/Rule!updateDetailRule.action',
			params:obj,
			scope:this,
			success:function(res,option){
				msg.hide();
				msg=null;
				var data = Ext.decode(res.responseText);
				if(data.success === true){
					Alert('保存成功',function(){
						this.close();
						Ext.getCmp('ruleGridId').getStore().reload();
					},this)
				}
			}
		});
	},
	show:function(){
		RuleDetailWin.superclass.show.call(this);
		this.form.getForm().findField('rule_name').focus(true,100);
	}
});

/*******************************************************************************/
/*明细配置结束*/

Rule = Ext.extend(Ext.Panel,{
	constructor:function(){
		ruleGrid = new RuleGrid();
		ruleCondPanel = new RuleCondPanel();
		refObjGrid = new RefObjGrid();
		Rule.superclass.constructor.call(this,{
			id:'Rule',
			title:'规则配置',
			closable: true,
			border : false ,
			layout : 'border',
			baseCls: "x-plain",
			items:[ruleGrid,ruleCondPanel,refObjGrid]
		});
	}
});

