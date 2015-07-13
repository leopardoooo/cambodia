WarnCfgForm = Ext.extend(Ext.FormPanel,{
	editFlag:false,//是否正在编辑
	storeSelected:null,storeToBeSelect:null,parent:null,
	typeStore:null,levelStore:null,conditionStore:null,optrStore:null,
	typeStoreValue:[{id:'datatype',name:'数据类型'},{id:'rolsign',name:'行标签'},{id:'colhead',name:'列头'}],
	disabledMap:{
		warnrowtype : true,	//类型
		level : true,			//项目
		warnvaluetype : true,	//条件
		optr1 : true,			//条件1
		value1 : true,			//值1
		optr2 : true,			//条件2
		value2 : true,			//值2
		colour : true			//配色
	},
	initStores:function(){
		this.storeSelected = new Ext.data.JsonStore({
			fields:['id','name'],
			listeners:{
				scope:this,
				remove:function(){
					var total = this.storeSelected.getCount();
					var type = this.getForm().findField('warnrowtype').getValue();
					if(type == 'colhead'){//列头的时候,已选为空,条件禁用
						if(total==0){
							this.getForm().findField('warnvaluetype').setValue('');
							this.getForm().findField('warnvaluetype').setDisabled(total==0);
						}
					}
				},
				add:function(){
					var type = this.getForm().findField('warnrowtype').getValue();
					if(type == 'colhead'){//列头的时候,已选为空,条件禁用
						this.getForm().findField('warnvaluetype').setDisabled(false);
					}
				}
			}
		});
		
		
		this.storeToBeSelect = new Ext.data.JsonStore({
			fields:['id','name']
		});
		
		this.typeStore = new Ext.data.JsonStore({
			fields:['id','name'],
			data: this.typeStoreValue
		});
		
		this.levelStore = new Ext.data.JsonStore({
			fields:['id','name','dim','level'],
			data: []
		});
		
		this.conditionStore = new Ext.data.JsonStore({
			fields:['id','name','dim','level'],
			data: []
		});
		
		this.optrStore = new Ext.data.JsonStore({
			fields:['id','name','dim','level'],
			data: []
		});
		
	},
	constructor:function(parent){
		this.parent = parent;
		this.initStores();
		
		WarnCfgForm.superclass.constructor.call(this,{
			buttonAlign:'center',title:'条件配置',border:false,
			layout:'column',defaults:{xtype:'fieldset',border:false,
			defaults:{border:false,layout:'form'}},labelWidth:45,
			bodyStyle:'padding-top:10px',
			
			items:[
				{columnWidth:.5,anchor:'90%',items:{fieldLabel:'类型',xtype:'combo',disabled:true,hiddenName:'warnrowtype',store:this.typeStore,valueField:'id',displayField:'name',
					listeners:{
						scope:this,select:this.changeType
					}
				}},
				{columnWidth:.5,anchor:'90%',items:{fieldLabel:'项目',disabled:true,xtype:'combo',hiddenName:'level',store:this.levelStore,valueField:'id',displayField:'name',
					listeners:{
						scope:this,select:this.changeLevel
					}
				}},
				{columnWidth:1,items:[this.createSelector()]},//warnvaluelist
				
				{columnWidth:1,layout:'column',defaults:{border:false,layout:'fit'},border:false,//labelWidth:1,
					items:[
					{columnWidth:.08,items:{width:20,value:'条件:',xtype:'displayfield'}},
						{columnWidth:.16,items:{allowBlank:false,fieldLabel:'',name:'warnvaluetype',hiddenName:'warnvaluetype',disabled:true,xtype:'combo',store:this.conditionStore,valueField:'id',displayField:'name'}},//条件
						{columnWidth:.16,items:{allowBlank:false,fieldLabel:'',hiddenName:'optr1',disabled:true,xtype:'combo',store:this.optrStore,valueField:'id',displayField:'name'}},//逻辑1
						{columnWidth:.16,items:{allowBlank:false,fieldLabel:'',name:'value1',disabled:true,xtype:'numberfield'}},//值1
						{columnWidth:.06,items:{value:'并且',xtype:'displayfield'}},
						{columnWidth:.16,items:{allowBlank:false,fieldLabel:'',name:'optr2',hiddenName:'optr2',disabled:true,xtype:'combo',store:this.optrStore,valueField:'id',displayField:'name'}},//逻辑2
						{columnWidth:.16,items:{allowBlank:false,fieldLabel:'',name:'value2',disabled:true,xtype:'numberfield'}}//值2
				]},
				
				
				{columnWidth:1,items:{allowBlank:false,fieldLabel:'配色', name:'colour',xtype:'colorfield',disabled:true}}
									//颜色控件,
			],
			buttons:[
			{text:'确定',handler:function(btn,event){
				
				if(this.storeSelected.getCount() ==0){
					Alert('已选框至少要选择一个');
					return false;
				}
				
				if(!this.getForm().isValid()){
					Alert('请完成表单!');
					return false;
				}
				
				var type = this.getForm().findField('warnrowtype').getValue();
				
				var typeData = null;
				Ext.each(this.typeStoreValue,function(data){
					if(data.id == type){
						typeData = data;
					}
				},this);
				var formValues = this.getForm().getValues();
				var rec = new Ext.data.Record(formValues);
				
				var selectedData = [];
				this.storeSelected.each(function(r){
					selectedData.push(r.get('name'));
				},this);
						
				switch (type) {
					case 'datatype' :// 数据类型
						rec.set('text1',typeData.name);
						rec.set('text2','='+selectedData.join());
						break;
					case 'rolsign' ://
						rec.set('text1',typeData.name);
						rec.set('text2','='+selectedData.join());
						break;
					case 'colhead' :
						var condition = this.conditionStore.getAt(this.conditionStore.find('id',formValues.warnvaluetype));
						rec.set('text1',typeData.name + '('+selectedData.join() + ')' + condition.get('name') );
						
						var optr1 = this.optrStore.getAt(this.optrStore.find('id',formValues.optr1));
						var optr2 = this.optrStore.getAt(this.optrStore.find('id',formValues.optr2));
						
						var text2Val = '' + optr1.get('name') + '  ' +formValues.value1 + ' 并且 ' + optr2.get('name') + '  ' +formValues.value2 ;
						rec.set('text2',text2Val );
						
						break;
				}
				
				
				this.parent.formToGridHandler(rec);
				
				this.disableAllField();
				this.editFlag = false;
			},scope:this},
			{text:'取消编辑',handler:function(btn,event){
				this.storeToBeSelect.removeAll();
				var map = this.disabledMap;
				Ext.apply(map,{warnrowtype : true,level : true});
				this.disabledFields(map);
				this.getForm().reset();
				this.editFlag = false;
			},scope:this}
			]
		});
		
	},
	startEditRecord:function(record){
		var warnrowtype = this.getForm().findField('warnrowtype');
		warnrowtype.setDisabled(false);
		warnrowtype.focus();
		if(!record){//新增,如果有record ,就是更新
			return true;
		}
		var data = record.data;
		this.getForm().setValues(data);
		var val = warnrowtype.getValue();
		
		var wvlistStoreData = {data:{id:'data',name:'数据'},group:{id:'group',name:'小计'}};
		
		var warnvaluelist = data.warnvaluelist;
		if(!Ext.isArray(warnvaluelist)){
			warnvaluelist = Ext.isEmpty(warnvaluelist) ? []:warnvaluelist.split('"').join(',').split('\'');
		}
		
		if(val == 'datatype'){
			Ext.each(warnvaluelist,function(id){
				if(wvlistStoreData.hasOwnProperty('data')){
					this.storeSelected.add(new Ext.data.Record(wvlistStoreData[id]));
				}else{
					this.storeToBeSelect.add(new Ext.data.Record(wvlistStoreData[id]));
				}
			},this);
			return;
		}
		
		if(val == 'rolsign'){
			var map = this.disabledMap;
			Ext.apply(map,{warnrowtype : false,level : false});
			this.disabledFields(map);
		}else{//列头
			var map = {
				warnrowtype : false,	//类型
				level : false,			//项目
				warnvaluetype : false,	//条件
				optr1 : false,			//条件1
				value1 : false,			//值1
				optr2 : false,			//条件2
				value2 : false,			//值2
				colour : false			//配色
			} ;
			this.disabledFields(map);
		}
		
		this.loadFormComboData(val,data);
		this.editFlag = true;
	},
	loadFormComboData:function(val,data){//加载各个combo的数据
		if(val =='datatype'){
			return;
		}
		//TODO 测试完毕删除
//		var template_id = '-4';//-4的时候才能成功,其他或者返回空,或者报错
		
		var wait = Show();//进度条
		Ext.Ajax.request({
			url : root + '/query/Key!queryWarnDimLevels.action',
			scope:this,timeout:9999999999,
			params : {key:val,key_value:this.parent.parent.currentRecord.get('template_id')},
			success : function(res, opt) {
				var list = Ext.decode(res.responseText);
				wait.hide();
				wait = null;
				this.levelStore.loadData(list);
			},
			failure:function(){
				wait.hide();
				wait = null;
				Alert('警戒配置数据初始化出错');
			}
		});
		
		if(val =='colhead'){
			var wait2 = Show();//进度条
			Ext.Ajax.request({
				url : root + '/query/RepDesign!queryWarnOperators.action',
				scope:this,timeout:9999999999,
				success : function(res, opt) {
					var list = Ext.decode(res.responseText);
					wait2.hide();
					wait2 = null;
					this.optrStore.loadData(list);
				},
				failure:function(){
					wait2.hide();
					wait2 = null;
					Alert('警戒配置数据初始化出错');
				}
			});
			
			var wait3 = Show();//进度条
			Ext.Ajax.request({
				url : root + '/query/RepDesign!queryWarnValueTypes.action',
				scope:this,timeout:9999999999,
				success : function(res, opt) {
					var list = Ext.decode(res.responseText);
					wait3.hide();
					wait3 = null;
					this.conditionStore.loadData(list);
				},
				failure:function(){
					wait3.hide();
					wait3 = null;
					Alert('警戒配置数据3初始化出错');
				}
			})
		}
		
		if(!data){
			return;
		}
		var levelRec = this.getForm().findField('level').findRecord('id',data.level);
		
		var wait4 = Show();//进度条
		Ext.Ajax.request({
			url : root + '/query/Key!queryLevelValues.action',
			scope:this,timeout:9999999999,
			params : {key:levelRec.get('dim'),key_value:levelRec.get('level')},
			success : function(res, opt) {
				var list = Ext.decode(res.responseText);
				wait4.hide();
				wait4 = null;
				this.storeSelected.removeAll();
				this.storeToBeSelect.removeAll();
				
				var warnvaluelist = data.warnvaluelist;
				//去除 双选框组合 value时候加的 引号,使用下面的分割方法能分出来 ,作为数组里的一个元素,但是干净的剥离了 引号,下面的遍历判断的时候,这些可以被忽略的
				warnvaluelist = Ext.isEmpty(warnvaluelist) ? []:warnvaluelist.split('"').join(',').split('\'');
				
				Ext.each(list,function(d){
					if(warnvaluelist.contain(d.id)){
						this.storeSelected.add(new Ext.data.Record(d));
					}else{
						this.storeToBeSelect.add(new Ext.data.Record(d));
					}
				},this);
			},
			failure:function(){
				wait4.hide();
				wait4 = null;
				Alert('警戒配置数据初始化出错');
			}
		})
		
	},
	
	disableAllField:function(){
		
		Ext.apply(this.disabledMap,{warnrowtype : true,level : true});
		this.disabledFields(this.disabledMap);
		
		this.storeSelected.removeAll();
		this.storeToBeSelect.removeAll();
		
		try{
			this.findBy(function(cmp,scope){
				if(!Ext.isEmpty(cmp.name || cmp.hiddenName) && !Ext.isEmpty(cmp.xtype) && Ext.isFunction(cmp.setValue)){
					cmp.setValue('');
					cmp.setDisabled(true);
				}
			},this);
		}catch(e){
		}
	},
	disabledFields:function(map){
		this.findBy(function(cmp,scope){
			for(var name in map){
				var cmpName =cmp.name || cmp.hiddenName;
				if(!cmpName){
					continue;
				}
				if(cmpName == name){
					cmp.setDisabled(map[name]);
				}
			}
			
		},this);
	},
	changeType:function(combo,record,index){
		this.storeSelected.removeAll();
		this.storeToBeSelect.removeAll();
		this.getForm().findField('level').setValue('');
		/**
		 * 1.类型=数据类型 
		 * 		双栏选择框 store=固定值 [{id:'data',name:'数据'},{id:'group':name:'小计'}] 
		 * 		项目、条件、逻辑1、值1、逻辑2、值2、配色 不可用
		 * 		
		 * 2.类型=行标签  　　
		 * 	  	项目=stroe 数据源 keyaction.queryWarnDimLevels 参数 key=类型.value,key_value=模板ID 返回List<WarnDimLevel>
		 * 
		 * 3.类型=列头 select时
		 * 　　	项目 store=调用keyaction.queryWarnDimLevels 参数 key=类型.value,key_value=模板ID 返回List<WarnDimLevel>
		 * 
		 */
		
		var val = record.get('id');
		
		switch(val){
			case 'datatype' ://数据类型
				var map = this.disabledMap;
				Ext.apply(map,{warnrowtype : false,level : true});
				this.disabledFields(map);
				this.storeToBeSelect.loadData([{id:'data',name:'数据'},{id:'group',name:'小计'}] );
			break;
			case 'rolsign' ://行标签
				var map = this.disabledMap;
				Ext.apply(map,{warnrowtype : false,level : false});
				this.disabledFields(map);
			break;
			case 'colhead' ://列头
				var map = {
					warnrowtype : false,	//类型
					level : false,			//项目
					warnvaluetype : true,	//条件
					optr1 : false,			//条件1
					value1 : false,			//值1
					optr2 : false,			//条件2
					value2 : false,			//值2
					colour : false			//配色
				} ;
				this.disabledFields(map);
				
			break;
		}
		
		this.loadFormComboData(val);
		this.getForm().reset();
		combo.setValue(val);
	},
	changeLevel:function(combo,record,index){
		/**
		 * 1.类型=行标签   当项目select时 
		 * 		双栏选择框 store=调用keyaction.queryLevelValues 参数key=WarnDimLevel.dim,key_value=WarnDimLevel.level 返回List<QueryKeyValue>
		 * 　　	条件、逻辑1、值1、逻辑2、值2、配色 不可用
		 * 2.类型=列头   当项目select时
		 * 		store=调用keyaction.queryLevelValues 参数key=WarnDimLevel.dim,key_value=WarnDimLevel.level 返回List<QueryKeyValue> 
		 *  	逻辑1，逻辑2 加载 stroe=调用repdesignaction.queryWarnOperators 值1，值2 可编辑（只能填数字，可填小数） 配色可选择
		 *  
		 */
		
		var type = this.getForm().findField('warnrowtype').getValue();
		
		switch(type){
			case 'rolsign' ://行标签
				var map = this.disabledMap;
				map.level = false;
				this.disabledFields(map);
			break;
			case 'colhead' :
				var map = {
					warnrowtype : false,	//类型
					level : false,			//项目
					warnvaluetype : true,	//条件
					optr1 : false,			//条件1
					value1 : false,			//值1
					optr2 : false,			//条件2
					value2 : false,			//值2
					colour : false			//配色
				};
				this.disabledFields(map);
			break;
		}
		
		var wait = Show();//进度条
		Ext.Ajax.request({
			url : root + '/query/Key!queryLevelValues.action',
			scope:this,timeout:9999999999,
			params : {key:record.get('dim'),key_value:record.get('level')},
			success : function(res, opt) {
				var list = Ext.decode(res.responseText);
				wait.hide();
				wait = null;
				this.storeSelected.removeAll();
				this.storeToBeSelect.loadData(list);
			},
			failure:function(){
				wait.hide();
				wait = null;
				Alert('警戒配置数据初始化出错');
			}
		})
		
	},
	createSelector:function(){
		
		var selector = {
		htmlcode : 'itemselector',hideNavIcons:true,
		fieldLabel:'',//测试
		name:'warnvaluelist',
		delimiter : "','",
		imagePath : '/' + Constant.ROOT_PATH_LOGIN + '/resources/images/itemselectorImage',
		multiselects : [{
					legend : '待选1111111',width:135,height:140,
					store : this.storeToBeSelect,displayField : 'name',valueField : 'id'
				}, {
					legend : '已选1111111',width:135,height:140,
					store : this.storeSelected,displayField : 'name',valueField : 'id'
				}]
		};
		
		return new Ext.ux.ItemSelector(selector);
	}
	
});

WarnCfgTab = Ext.extend(Ext.TabPanel,{
	initialized:false,
	dataPassed:[],parent:null,grids:[],
	
	beforeDestroy:function(){//被销毁前执行的动作
		
	},
	initAll:function(parent,list){
		if(this.initialized){
			this.removeAll();
			this.grids = [];
		}
		this.parent = parent;
		this.dataPassed = list;
		
		//创建grid
		Ext.each(list,function(item){
			
			var gridStore = new Ext.data.JsonStore({
				fields:['warnrowtype','warnvaluetype','warnvaluelist','optr1','value1','optr2','value2','colour','text1','text2']
				,data:item.rowlist
			});
		
			var title = '<div style="font-size:36;height:40px;">' + item.mea + '</div>';
			var gridSelectModel = new Ext.grid.CheckboxSelectionModel();
			
			var gridPanel = new Ext.grid.GridPanel({
				width:'100%',border:2,
				sm:gridSelectModel,
	//			title:title,
				title:item.mea,
				store:gridStore,
				columns:[
					gridSelectModel,
					//new Ext.grid.RowNumberer(),
					{header:'类型',dataIndex:'warnrowtype',hidden:true},
					{header:'warnrowtype',dataIndex:'warnrowtype',hidden:true},
					{header:'optr1',dataIndex:'optr1',hidden:true},
					{header:'value1',dataIndex:'value1',hidden:true},
					{header:'optr2',dataIndex:'optr2',hidden:true},
					{header:'value2',dataIndex:'value2',hidden:true},
					{header:'colour',dataIndex:'colour',hidden:true},
					
					{header:'条件',dataIndex:'text1',width:'48%'},
					{header:'逻辑判断',dataIndex:'text2',width:'48%'}
				]
			});
			
			this.grids.push(gridPanel);
			
		},this);
		this.add(this.grids);
		this.activate(0);
		this.initialized = true;
	},
	constructor:function(parent,list){//构造函数
		this.parent = parent;
		this.dataPassed = list;
//		this.initAll(parent,list);//
		
		WarnCfgTab.superclass.constructor.call(this, {
			deferredRender:false,
			border:false,
			defaults : {closable : false,border:false},
			minTabWidth: 115,
			tabWidth:135,
			enableTabScroll:true,
			activeTab:0,
			items:this.grids,
			
			tbar:[
			{text:'增加',scope:this,handler:this.addBtnHandler},'-',
			{text:'修改',scope:this,handler:this.modifyBtnHandler},'-',
			{text:'删除',scope:this,handler:this.removeBtnHandler}
			],
			listeners:{
				scope:this,
				beforetabchange :function(tabPanel,newTab,currentTab ){
					var formPanel = this.parent.formPanel;
					if(!formPanel.rendered){
						return true;
					}
					
					if(formPanel.editFlag == true){
						alert('有正在编辑的数据,请先提交或者取消编辑');
						return false;
					}
					return true;
				},
				tabchange:function(tabPanel,panel){
					this.parent.currentGrid = panel;
					this.parent.formPanel.disableAllField();
				}
			}
		});
	},
	addBtnHandler:function(){
		this.parent.formPanel.startEditRecord(false);
	},
	modifyBtnHandler:function(){
		var records = this.activeTab.selModel.getSelections();
		if(records.length != 1){
			Alert('请选择且仅选择一条记录操作');
			return false;
		}
		this.parent.formPanel.startEditRecord(records[0]);
		this.activeTab.store.remove(records);
	},
	removeBtnHandler:function(){
		var records = this.activeTab.selModel.getSelections();
		if(records.length ==0 ){
			Alert('请选择至少选择一条记录');
			return false;
		}
		Confirm('是否确认删除选中的记录?',this,function(){
			this.activeTab.store.remove(records);
		});
		
	}
});


WarnConfigWindow = Ext.extend(Ext.Window,{
	formPanel :null,tabPanel:null,title:'指标警戒',closeAction:'hide',
	currentGrid:null,
	parent:null,dataPassed:null,
	rebuildCmps:function(parent,dataPassed){
		this.initCmps(parent,dataPassed);
		this.doLayout();
	},
	initCmps:function(parent,dataPassed){
		this.parent = parent;
		this.dataPassed = dataPassed;
		if(!this.formPanel){
			this.formPanel = new WarnCfgForm(this);
		}
		if(!this.tabPanel){
			this.tabPanel = new WarnCfgTab(this,dataPassed);
		}
		this.tabPanel.initAll(this,dataPassed);
	},
	constructor:function(parent,dataPassed){
		
		this.initCmps(parent,dataPassed);
		
		WarnConfigWindow.superclass.constructor.call(this,{
			width:750,height:450,layout:'hbox',
			resizable:false,maximizable:false,
			defaults:{xtype:'panel',border:false},
			items : [
				{flex:1,height:380,layout:'fit',items:this.tabPanel},
				{width:2,height:380,html:'<div style="height:100%;background-color:gray;"></div>'},
				{flex:1,height:380,layout:'fit',items:this.formPanel}
			],
			buttonAlign:'center',
			buttons:[
				{text:'确定',handler:this.doSaveWarnCfg,scope:this},
				{text:'取消',handler:this.cancel,scope:this}
			],
			listeners:{
				scope:this
//				,afterrender:this.doAfterRender
			}
		});
		
	},
	cancel:function(){
		this.hide();
	},
	doSaveWarnCfg:function(){
		
//		var keyValue = '-4';//-4的时候才能成功,其他或者返回空,或者报错
		
		var warnlist = [];
		Ext.each(this.tabPanel.grids,function(grid){
			var mea = grid.title;
			var rowlist = [];
			grid.store.each(function(record){
				var data = record.data;
				var warnvaluelist = data.warnvaluelist.split('\'').join(',').replace('"','').split(',');
				var list = [];
				Ext.each(warnvaluelist,function(str){
					if(!Ext.isEmpty(str)){
						list.push(str);
					}
				});
				data.warnvaluelist = list;
				rowlist.push(data);
			});
			warnlist.push({mea:mea,rowlist:rowlist});
		},this);
		
		var wait = Show();//进度条
		Ext.Ajax.request({
			url : root + '/query/RepDesign!saveCubeWarn.action',
			scope:this,timeout:9999999999,
			params : {template_id:this.parent.currentRecord.get('template_id'),warnlist:Ext.encode(warnlist)},
			success : function(res, opt) {
				var list = Ext.decode(res.responseText);
				wait.hide();
				wait = null;
//				this.levelStore.loadData(list);
				this.hide();
			},
			failure:function(){
				wait.hide();
				wait = null;
				Alert('保存警戒配置数据出错');
			}
		})
	},
	formToGridHandler:function(record){//可能是增加,也可能是修改.
		try{
			this.currentGrid.store.add(record);
		}catch(e){
			//Alert(e);
			//TODO 初次添加报错
		}
	}
	
});
