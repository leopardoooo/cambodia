ThersholdCfgGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	therholdStore : null,taskCodeCombo:null,prodTypeCombo:null,
	orginalDataMap:[],
	initComponent: function(){
		ThersholdCfgGrid.superclass.initComponent.call(this);
//		App.form.initComboData( [this.taskCodeCombo] );//暂时不需要查询条件,先注释掉
	},
	genKey:function(data){
		var properties = ['prod_type','task_code','county_id','area_id'];
		var keyArr = [];
		for(var idx =0;idx<properties.length;idx++){
			keyArr.push(data[ properties[idx] ]);
		}
		return keyArr.join(',');
	},
	processOrginalData:function(recs){
		for(var index =0;index<recs.length;index++){
			var data = recs[index].data;
			this.orginalDataMap[this.genKey(data)] = data.new_threshold_day;
		}
	},
	checkModified:function(){
		var modified = [];
		this.store.each(function(record){
			var data = record.data;
			if(this.orginalDataMap[this.genKey(data)] != data.new_threshold_day){
				modified.push(record);
			}
		},this);
		return modified;
	},
	editRecord:function(){
		var records = this.checkModified();
		var array = [];
		for(var idx =0;idx<records.length;idx++){
			array.push(records[idx].data);
		}
		if(array.length ==0){
			Alert('没有修改过的记录!');
			return ;
		}
		Ext.getBody().mask();
		Ext.Ajax.request({
			url:root+'/system/ThersholdCfg!saveOrUpdate.action',
			params:{cfgs:Ext.encode(array)},scope:this,
			success:function(req){
				var data = Ext.decode(req.responseText);
				if(data != true){
					Alert('操作出错,请联系挂历月');
					return;
				}
				for(var index = 0;index<records.length;index++){
					var rec = records[index];
					rec.commit();
				}
				Ext.getBody().unmask();
				Alert('修改成功');
			}
		});
	},
	constructor: function(){
		this.therholdStore = new Ext.data.JsonStore({
			url:root+'/system/ThersholdCfg!query.action',
			fields : ['prod_type','prod_type_name','task_code','task_name',
						'threshold_day','new_threshold_day','county_id','county_name','area_id','area_name'],
			root:'records',totalProperty:'totalProperty',autoLoad : true,
			listeners:{
				scope:this,
				beforeload:function(){
//					var param = this.checkValid();
//					if(!param){return false;}
//					Ext.apply(this.therholdStore.baseParams,param);
					Ext.applyIf(this.therholdStore.baseParams,{start: 0,limit: 20});
					return true;
				},
				load:function(store,recs){
					this.processOrginalData(recs);
				}
			}
		});
		var cm =[
			{header:'地区',width:160,dataIndex:'area_name'},
			{header:'分公司',width:160,dataIndex:'county_name'},
			{header:'产品类型',width:160,dataIndex:'prod_type_name'},
			{header:'任务类型',width:160,dataIndex:'task_name'},
			{header:'延后停天数',width:160,dataIndex:'threshold_day'},
			{header:'天数修改',width:160,dataIndex:'new_threshold_day', editor : new Ext.form.NumberField({allowNegative : false,allowBlank : false})}
		];
		/*
		this.prodTypeCombo = new Ext.form.ComboBox({
			store:new Ext.data.JsonStore({
				fields : ['value','name'],
				data:[{name:'基本产品',value:'T'},{name:'增值产品',value:'F'},{name:'全部',value:''}]
			}),
			hiddenName:'prod_type',displayField:'name',valueField:'value',emptyText:'请选择。。',
			minListWidth : 80,allowBlank:false
		});
		this.taskCodeCombo = new Ext.ux.ParamCombo({
			paramName:'JOB_TASK_CODE',allowBlankItem:true,
			hiddenName:'taskCode',displayField:'item_name',valueField:'item_value',
			minListWidth : 80,editable:false
		});
		*/
		ThersholdCfgGrid.superclass.constructor.call(this, {
			border:false,
			region:'center',
			store:this.therholdStore,
			columns:cm,
			sm : new Ext.grid.CheckboxSelectionModel({}),
			viewConfig : {
				forceFit : true
			},
			bbar:new Ext.PagingToolbar({store:this.therholdStore,pageSize:20}),
			tbar : [' ',/*'产品类型', ' ',this.prodTypeCombo,
					'-', '任务代码', ' ',this.taskCodeCombo,'-',
					{text:'&nbsp;&nbsp;查询',scope:this,handler:this.doQuery},*/
					'->',{text:'提交修改',handler:this.editRecord,scope:this},'-'
					]
		});
	},
	checkValid:function(){//参数标明是否仅仅检查变更类型
		var prodType = this.prodTypeCombo.getValue();var key = this.taskCodeCombo.getValue();
		var result = {'cfg.prod_type':prodType,'cfg.task_code':key};
		return  result;
	},
	doQuery:function(){
		this.therholdStore.load();
	}
})

ThersholdCfg = Ext.extend(Ext.Panel,{
	grid : null,layout:'fit',
	constructor:function(){
		this.grid  = new ThersholdCfgGrid();		
		ThersholdCfg.superclass.constructor.call(this,{
			id:'ThersholdCfg',
			title:'变更记录',
			closable:true,
			border:false,
			layout:'border',
			items:[this.grid ]
		});
	},
	initEvents : function(){
		ThersholdCfg.superclass.initEvents.call(this);
	}
});