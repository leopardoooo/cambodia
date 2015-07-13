ChangeDetailWin = Ext.extend(Ext.Window, {
	panel : null,
	constructor : function(changeInfo){
		var content = changeInfo.content ;
		var raRegExp = new RegExp(':',"g");
		
		var reg = new RegExp('{splitFlag}',"g");
		content = content.replace(reg,'\n');
		
		changeInfo.content = content.replace(raRegExp,'：');//将半个输入的冒号换成全格输入的冒号，避免解析错误
		
		var itemArr = [];
		this.changeInfoTpl = new Ext.XTemplate(
			'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
				'<tr height=24>',
					'<td class="label" width=20%>异动对象：</td>',
					'<td class="input_bold" width=30%>&nbsp;{change_type_text}</td>',
					'<td class="label" width=20%>键值内容：</td>',
					'<td class="input_bold" width=30%>&nbsp;{key}</td>',
				'</tr>',
				'<tr height=24>',
					'<td class="label" width=20%>变化对象：</td>',
					'<td class="input_bold" width=30%>&nbsp;{change_desc}</td>',
			      	'<td class="label" width=20%>键值说明：</td>',
					'<td class="input_bold" width=30%>&nbsp;{key_desc}</td>',
		    	'</tr>',
		    	'<tr height=24>',
		      		'<td class="label" width=20%>操作员：</td>',
			      	'<td class="input_bold" width=30%>&nbsp;{optr_name}</td>',	
		    		'<td class="label" width=20%>操作日期：</td>',
		      		'<td class="input_bold" width=30%>&nbsp;{create_time}</td>',
		    	'</tr>',
			'</table>',
			'<hr/>',
			'<textarea editable="true" class="input_bold" style="overflow-x:hidden; overflow-y:scroll;width:100%;height:78%" onkeypress="return false;" onpaste="return false;" oncut="return false;">' +
				"{content}" + 
			'</textarea>'
		);
		this.changeInfoTpl.compile();
		
		this.panel = new Ext.Panel({
			border : false,
			width:450,height:400,autoDestory:true,
			region : 'north',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			html : this.changeInfoTpl.applyTemplate(changeInfo)
		})
		
		ChangeDetailWin.superclass.constructor.call(this,{
			title:'内容信息',
			width: 600, 
			height:450,
			border:false,
			closeAction : 'close',
			layout : 'fit',
			items:this.panel
		})
	}
})

SysChangeGrid = Ext.extend(Ext.grid.GridPanel,{
	changeStore : null,idDescCombo:null,changeTypeCombo:null,
	initEvents : function(){
		this.on('celldblclick',this.cellDblClick,this);
		SysChangeGrid.superclass.initEvents.call(this);
	},
	cellDblClick:function(grid,rowIndex,columnIndex,e){
		var grid = new Ext.grid.GridPanel();
		var record = this.changeStore.getAt(rowIndex);
		new ChangeDetailWin(record.data).show();
	},
	constructor: function(){
		this.changeStore = new Ext.data.JsonStore({
			autoLoad : false,
			url:root+'/system/SysChange!query.action',
			fields : ['change_type', 'change_type_text','done_code', 'key', 'key_desc',
						'change_desc', 'content', 'optr_id','optr_name', 'create_time'],
			totalProperty:'totalProperty',
			root:'records',
			listeners:{
				scope:this,
				beforeload:function(){
					var param = this.checkValid(true);
					if(!param){
						return false;
					}
					Ext.apply(this.changeStore.baseParams,{id : param.key,changeType : param.changeType,beginDate:param.beginDate,endDate:param.endDate});
					Ext.applyIf(this.changeStore.baseParams,{start: 0,limit: 20});
					return true;
				}
			}
		});
		var cm =[
			{header:'异动对象',width:160,dataIndex:'change_type_text'},
			{header:'键值内容',width:160,dataIndex:'key'},
			{header:'键值说明',width:160,dataIndex:'key_desc'},
			{header:'变化对象',width:160,dataIndex:'change_desc',renderer:App.qtipValue},
			{header:'变化内容',width:160,dataIndex:'content',renderer:this.rendererLargeString,scope:this},
			{header:'操作员',width:160,dataIndex:'optr_name'},
			{header:'操作日期',width:160,dataIndex:'create_time'}
		];
		this.changeTypeCombo = new Ext.form.ComboBox({
			editable:true,
			typeAhead:true,
			store:new Ext.data.JsonStore({
				autoLoad:true,
				url:root+'/system/SysChange!listChangeTypes.action',
				fields : ['id','desc']
			}),
			hiddenName:'id_desc',displayField:'desc',valueField:'id',emptyText:'请选择。。',
			minListWidth : 100,allowBlank:false,
			listeners:{
				scope:this,
				change:function(combo,record){
					this.idDescCombo.setValue('');
					this.idDescStore.removeAll();
				}
			}
		});
		
		this.idDescStore = new Ext.data.JsonStore({
				url:root+'/system/SysChange!queryKey.action',
				fields : ['item_value','item_name']
			});
		
		this.idDescCombo = new Ext.form.ComboBox({
			store:this.idDescStore,
			hiddenName:'id_desc',displayField:'item_name',valueField:'item_value',emptyText:'请输入至少两个字。。',
			minListWidth : 150,editable:true,minChars:2,
			listeners:{
				scope:this,
				beforequery:function(e){
					var combo = e.combo;
					var store = combo.getStore();
		            var query = e.query;
		            store.removeAll();
		            var param = this.checkValid(true);
					if(!param){
						return false;
					}
		            if(query.length >= combo.minChars){//最少输入两个字才给查询
		            	Ext.apply(store.baseParams,{id_desc : query,changeType : param.changeType});
		            	store.load();
		            }
				},
				change:function(){
					this.changeStore.removeAll();
				}
			}
		});
		
		this.startDate = new Ext.form.DateField({
			listeners:{
				scope:this,
				change:function(field,newVal,oldVal){
					var endDate = this.endDate.getValue();
					if(Ext.isEmpty(endDate)){
						return true;
					}
					if(newVal.getTime() == endDate.getTime()){
						Alert('起始日期不能一样');
						field.setValue(null);
						return false;
					}
				}
			}
		});
		this.endDate = new Ext.form.DateField({
			listeners:{
				scope:this,
				change:function(field,newVal,oldVal){
					var endDate = this.startDate.getValue();
					if(Ext.isEmpty(endDate)){
						return true;
					}
					if(newVal.getTime() == endDate.getTime()){
						Alert('起始日期不能一样');
						field.setValue(null);
						return false;
					}
				}
			}
		});
		
		SysChangeGrid.superclass.constructor.call(this, {
			border:false,
			region:'center',
			store:this.changeStore,
			columns:cm,
			sm : new Ext.grid.CheckboxSelectionModel({}),
			viewConfig : {
				forceFit : true
			},
			bbar:new Ext.PagingToolbar({store:this.changeStore,pageSize:20}),
			tbar : [' ', '开始时间：',this.startDate,'-','结束日期：',this.endDate,'-' , '变更类型', ' ',this.changeTypeCombo,
					'-', '键值说明', ' ',this.idDescCombo,'-',
					{text:'&nbsp;&nbsp;查询',scope:this,handler:this.doQuery}
					]
		});
	},
	rendererLargeString:function(value, metadata, record, rowIndex, columnIndex, store){
		if(Ext.isEmpty(value))return "";
		if(value == 'null') return "";
		var tmp = value.replace('>','&gt;');
		
		var reg = new RegExp('{splitFlag}',"g");
		tmp = tmp.replace(reg,'');
		var result = '<div ext:qtitle="变更信息" ext:qtip="' + tmp + '">' + tmp +'</div>';
    	return result;
	},
	checkValid:function(onlyChangeType){//参数标明是否仅仅检查变更类型
		var changeType = this.changeTypeCombo.getValue();
		var key = this.idDescCombo.getValue();
		var result = {changeType:changeType,key:key,beginDate:this.startDate.getValue(),endDate:this.endDate.getValue()};
		if(Ext.isEmpty(changeType) ){
			this.changeTypeCombo.markInvalid('不能为空');
			return false;
		}
		if(onlyChangeType){
			return result;
		}
		if(Ext.isEmpty(key)){
			this.idDescCombo.markInvalid('不能为空');
			return false;
		}
		return  result;
	},
	doQuery:function(){
		if(!this.checkValid(true)){
			return false;
		}
		this.changeStore.load();
	}
})

SysChange = Ext.extend(Ext.Panel,{
	sysChangeGrid : null,layout:'fit',
	constructor:function(){
		this.sysChangeGrid  = new SysChangeGrid();		
		SysChange.superclass.constructor.call(this,{
			id:'SysChange',
			title:'变更记录',
			closable:true,
			border:false,
			layout:'border',
			items:[this.sysChangeGrid ]
		});
	},
	initEvents : function(){
//		this.on('activate',function(){this.sysChangeGrid .getStore().reload();},this)
		SysChange.superclass.initEvents.call(this);
	}
});