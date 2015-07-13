var projectLimit = 20;

var ProjectQueryForm = Ext.extend(Ext.form.FormPanel,{

	constructor: function(){
		this.countyStore = new Ext.data.JsonStore({
			url:root+'/project/Project!queryByCountyId.action',
			fields:['county_id','county_name','project_county_id']
		});
		this.countyStore.load();
		ProjectQueryForm.superclass.constructor.call(this,{
			border:false,
			region:'north',
			height:65,
			labelWidth:75,
			labelAlign:'right',
			layout:'column',
			bodyStyle:'padding-top:5px',
			defaults:{
				layout:'form',
				border:false,
				defaults:{xtype:'textfield'}
			},
			items:[
				{columnWidth:.33,items:[
					{fieldLabel:'项目编号',name:'project_number'},
					{fieldLabel:'项目名称',name:'project_name'}
				]},
				{columnWidth:.33,items:[
					{fieldLabel:'县市名称',xtype:'combo',hiddenName:'county_id',store:this.countyStore,
						displayField:'county_name',valueField:'county_id',triggerAction:'all',
						editable:true,forceSelection:true}
				]},
				{columnWidth:.33,items:[
					{fieldLabel:'小区名称',name:'addr_name'}
				]},
				{columnWidth:.5,items:[
					{fieldLabel:'申报时间',name:'create_time',xtype:'compositefield',
						combineErrors:false,items:[
						{xtype:'datefield',name:'start_date',format:'Y-m-d',width:120,height:22},
				        {xtype:'displayfield',value:'至'},
				        {xtype:'datefield',name:'end_date',format:'Y-m-d',width:120,height:22}
					]}
				]},
				{columnWidth:.17,items:[
					{xtype:'button',text:'搜索',scope:this,handler:this.doQuery}
				]}
				
			]
		});
	},
	doQuery: function(){
		var values = this.getForm().getValues();
		var obj = {};
		for(var i in values){
			obj['queryProject.'+i] = values[i];
		}
		var store = Ext.getCmp('projectQueryGridId').getStore();
		store.baseParams = obj;
		store.load({
			params:{
				start: 0,
				limit: projectLimit
			}
		});
	}
});

var ProjectQueryGrid = Ext.extend(Ext.grid.GridPanel,{
	projectStore: null,
	constructor: function(){
		var convertDate = function(v,r){
			if(v){return v.substring(0,10);}
			return v;
		}
		this.projectStore = new Ext.data.JsonStore({
			url:root+'/project/Project!queryProject.action',
			root:'records',
			totalProperty:'totalProperty',
			fields:['project_id','project_number','project_name','county_id','county_name',
				'project_type','project_type_text',
				{name:'create_date',convert:convertDate},
				{name:'change_date',convert:convertDate},
				{name:'pre_start_date',convert:convertDate},
				{name:'start_date',convert:convertDate},
				{name:'pre_end_date',convert:convertDate},
				{name:'end_date',convert:convertDate},
				'status','status_text','remark','is_valid','is_valid_text','addr_name',
				'pre_start_date','start_date','pre_end_date','end_date','plan_num','real_num',
				'plan_users_1','plan_income_1','plan_users_2','plan_income_2','plan_users_3','plan_income_3',
				'plan_users_4','plan_income_4','plan_users_5','plan_income_5','optic_cable_length',
				'electric_cable_length','optical_node_number','project_plan_money','project_final_money']
		});
		this.projectStore.load({params:{start:0,limit:projectLimit}});
		var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
		var columns = [
			{header:'项目编号',dataIndex:'project_number',width:95,renderer:App.qtipValue},
			{header:'项目名称',dataIndex:'project_name',width:85,renderer:App.qtipValue},
			{header:'所属县市',dataIndex:'county_name',width:80},
			{header:'项目类型',dataIndex:'project_type_text',width:60},
			{header:'创建日期',dataIndex:'create_date',width:65},
			{header:'修改日期',dataIndex:'change_date',width:65},
			{header:'项目状态',dataIndex:'status_text',width:60},
			{header:'关联小区',dataIndex:'addr_name',width:120,renderer:App.qtipValue},
			{header:'是否有效',dataIndex:'is_valid_text',width:55},
			{header:'备注',dataIndex:'remark'}
		];
		ProjectQueryGrid.superclass.constructor.call(this,{
			id:'projectQueryGridId',
			title:'项目信息',
			region:'center',
			border:false,
			store:this.projectStore,
			columns:columns,
			sm:sm,
			tbar:['-',
				{id:'project_add_btn_id',hidden:true,text:'新增',scope:this,handler:this.doAdd},'-',
				{id:'project_modify_btn_id',hidden:true,disabled:true,text:'修改',scope:this,handler:this.doModify},'-',
				{id:'project_addr_linked_btn_id',hidden:true,disabled:true,text:'小区关联',scope:this,handler:this.doLinked},'-',
				{id:'project_valid_btn_id',hidden:true,disabled:true,text:'生效',scope:this,handler:this.doValid},'-',
				{id:'project_invalid_btn_id',hidden:true,disabled:true,text:'失效',scope:this,handler:this.doValid},'-'
			],
			bbar:new Ext.PagingToolbar({store: this.projectStore,pageSize:projectLimit}),
			listeners:{
				scope:this,
				rowclick:this.doRowClick
			}
		});
	},
	doRowClick: function(grid){
		var record = this.getSelectionModel().getSelected();
		var addBtn = Ext.getCmp('project_add_btn_id');
		var modifyBtn = Ext.getCmp('project_modify_btn_id');
		var linkBtn = Ext.getCmp('project_addr_linked_btn_id');
		var validBtn = Ext.getCmp('project_valid_btn_id');
		var invalidBtn = Ext.getCmp('project_invalid_btn_id');
		if(record){
			
			var isValid = record.get('is_valid');	//是否生效
			var addrName = record.get('addr_name');	//关联小区
			
			modifyBtn.enable();
			
			//只有有效状态的项目可以与小区关联
			if( !linkBtn.hidden){
				if( isValid == 'T'){
					linkBtn.enable();
				}else{
					linkBtn.disable();
				}
			}
			
			if( !validBtn.hidden){
				if( isValid == 'F'){
					validBtn.enable();
				}else{
					validBtn.disable();	
				}
			}
			
			//没有关联小区的才能失效
			if( !invalidBtn.hidden){
				if( isValid == 'T' && Ext.isEmpty(record.get('addr_name'))){
					invalidBtn.enable();	
				}else{
					invalidBtn.disable();
				}
			}
		}else{
			modifyBtn.disable();
			linkBtn.disable();
			validBtn.disable();
			invalidBtn.disable();
		}
		
	},
	doAdd: function(){
		var win = new ProjectWindow();
		win.show();
	},
	doModify: function(projectId){
		var record = this.getSelectionModel().getSelected();
		if(record){
			var win = new ProjectWindow(record.data);
			win.show();
		}
	},
	doLinked: function(projectId){
		var record = this.getSelectionModel().getSelected();
		if(record){
			var win = new ProjectAddrWindow(record.data);
			win.show();
		}
	},
	/**
	 * 项目生效或失效
	 * @param {} projectId
	 * @param {} isValid 'T' 生效, 'F' 失效
	 */
	doValid: function(projectId,isValid){
		var record = this.getSelectionModel().getSelected();
		if(record){
			Confirm('确定操作吗?',this,function(){
				Ext.Ajax.request({
					url:root+'/project/Project!isInvalid',
					params:{
						'projectId': projectId,
						'isValid': isValid
					},
					scope: this,
					success: function(res){
						Alert('保存成功');
						record.set('is_valid',isValid);
						record.set('is_valid_text',(isValid == 'T' ? '是' : '否') );
						record.commit();
					}
				});
			});
		}
	}
});

/**
 * 项目新增修改窗口
 * @class
 * @extends Ext.Window
 */
var ProjectWindow = Ext.extend(Ext.Window,{
	projectForm: null,
	constructor: function(projectInfo){
		this.projectForm = new ProjectForm(projectInfo);
		ProjectWindow.superclass.constructor.call(this,{
			id:'projectWindowId',
			title:'新建项目',
			closeAction:'close',
			resizable:false,
			maximizable:false,
			width:750,
			height:370,
			border:false,
			layout:'fit',
			items:[this.projectForm]/*,
			buttonAlign:'center',
			buttons:[
				{text:'保  存',scope:this,handler:this.doAdd},
				{text:'取  消',scope:this,handler:this.doCancel}
			]*/
		});
		if(projectInfo)this.setTitle('修改项目');
	}/*,
	show: function(projectInfo){
		ProjectWindow.superclass.show.call(this);
		
	},
	doAdd: function(){
		if( this.projectForm.doValid() ){
			var project = this.projectForm.doValue();
			Ext.Ajax.request({
				url: root+'/project/Project!saveProject',
				params: project,
				success: function(res){
					var data = Ext.decode(res.responseText);
					if(data.success === true){
						Alert('保存成功!');
						Ext.getCmp('projectQueryGridId').getStore().load({params:{start:0,limit:projectLimit}});
					}
				}
			});
		}
	},
	doCancel: function(){
		this.projectForm.doReset();
		this.close();
	}*/
});


/**
 * 项目小区关联窗口
 * @class
 * @extends Ext.Window
 */
var ProjectAddrWindow = Ext.extend(Ext.Window,{
	projectId: null,
	projectNumber: null,
	constructor:function(projectInfo){
		this.projectId = projectInfo['project_id'];
		this.projectNumber = projectInfo['project_number'];
		this.projectAddrStore = new Ext.data.JsonStore({
			url:root+'/project/Project!queryAddrByProjectId.action',
			fields:['addr_id','addr_name']
		});
		this.projectAddrStore.load({
			params:{'projectId':this.projectId}
		});
		
		//项目所在县市 小区
		this.leafAddrStore = new Ext.data.JsonStore({
			url:root+'/project/Project!queryAddrDistrict.action',
			fields:['addr_id','addr_name']
		});
		this.leafAddrStore.load({params:{countyId:projectInfo['county_id']}});
		this.addrStore = new Ext.data.JsonStore({
			url:root+'/project/Project!queryAddrCommunity.action',
			fields:['addr_id','addr_name']
		});
		this.form = new Ext.form.FormPanel({
			bodyStyle:'padding-top:10px',
			hideBorders:true,
			layout:'column',
			labelAlign:'right',
			labelWidth:80,
			defaults:{
				layout:'form',
				defaults:{xtype:'textfield'}
			},
			items:[
				{columnWidth:1,items:[
					{fieldLabel:'项目编号',name:'project_number',readOnly:true}
				]},
				{columnWidth:1,items:[
					{fieldLabel:'项目名称',name:'project_name',readOnly:true}
				]},
				{columnWidth:.4,items:[
					{fieldLabel:'选择小区',xtype:'combo',hiddenName:'addr_pid',
						store:this.leafAddrStore,editable:true,forceSelection:true,
						displayField:'addr_name',valueField:'addr_id',triggerAction:'all',
						listeners:{
							scope:this,
							select: function(combo,record){
								this.addrStore.load({
									params:{addrPid:combo.getValue()}
								});
							}
						}
					}
				]},
				{columnWidth:.25,items:[
					{xtype:'combo',hiddenName:'addr_id',hideLabel:true,
						store:this.addrStore,
						displayField:'addr_name',valueField:'addr_id',triggerAction:'all'
					}
				]},
				{columnWidth:.1,items:[
					{xtype:'button',text:'选择↓',scope:this,handler:this.doAdd}
				]},
				{columnWidth:.1,items:[
					{xtype:'button',text:'删除↑',scope:this,handler:this.doDel}
				]},
				{columnWidth:1,items:[
					{xtype:'multiselect',fieldLabel:'已关联小区',name:'new_add_ids',
						store:this.projectAddrStore,
						displayField:'addr_name',valueField:'addr_id',
						width:250,height:200,
						legend:"",
						tbar:[{text:'清空',scope:this,handler:function(){
								this.form.getForm().findField("new_add_ids").store.removeAll();
							}
						}]
					}
				]}
			]
		});
		
		this.leafAddrStore.on('load',function(s){
			this.form.getForm().setValues(projectInfo);
		},this);
		
		ProjectAddrWindow.superclass.constructor.call(this,{
			id:'projectAddrWindowId',
			title:'小区关联',
			closeAction:'close',
			layout:'fit',
			resizable:false,
			maximizable:false,
			width:550,
			height:375,
			border:false,
			items:[this.form],
			buttonAlign:'center',
			buttons:[
				{text:'保  存',scope:this,handler:this.doSave},
				{text:'取  消',scope:this,handler:this.doCancel}
			]
		});
	},
	doAdd: function(){
		var form = this.form.getForm();
		var addrIdComp = form.findField('addr_id');
		var addrIdStore = addrIdComp.getStore();
		var addrId = addrIdComp.getValue();
		if(addrId){
			var store = form.findField('new_add_ids').store;	//项目关联小区列表
			var record = null;
			//获取下拉框小区record
			addrIdStore.each(function(r){
				if(r.get('addr_id') == addrId){
					record = r;
					return false;
				}
			});
			
			//小区不存在则添加
			if(record && store.indexOf(record) == -1){
				store.insert(0,record);
				store.commitChanges();
			}
		}
	},
	doDel: function(){
		var form = this.form.getForm();
		var comp = form.findField('new_add_ids');
		var records = comp.view.getSelectedRecords();
		if(records.length > 0){
			comp.store.remove(records);
			comp.store.commitChanges();
		}
	},
	doSave: function(){
		var addrIds = '';
		var store = this.form.getForm().findField('new_add_ids').store;
		store.each(function(r){
			addrIds += r.get('addr_id')+',';		
		});
		addrIds = addrIds.substring(0,addrIds.length-1);
		Ext.Ajax.request({
			url:root+'/project/Project!saveLinkedProject.action',
			params:{
				projectId: this.projectId,
				addrIds: addrIds
			},
			scope:this,
			success:function(res){
				var data = Ext.decode(res.responseText);
				if(data['success'] === true){
					if(Ext.isEmpty(data['simpleObj'])){
						Alert('保存成功');
						Ext.getCmp('projectQueryGridId').getStore().load({params:{start:0,limit:projectLimit}});
						this.doCancel();
					}else{
						Alert('项目编号顺序码:'+ this.projectNumber.substring(2, 6)
							+ '<br/>在小区 <font color=red>' + data['simpleObj']+ '</font> 已存在,'
							+ ' <br/>请修改项目编号顺序码');
					}
				}
			}
		});
	},
	doCancel: function(){
		Ext.getCmp('projectAddrWindowId').close();
	}
});

var ProjectQuery = Ext.extend(Ext.Panel,{
	
	constructor: function(){
		ProjectQuery.superclass.constructor.call(this,{
			id:'ProjectQuery',
			title:'项目查询',
			border:false,
			layout:'border',
			closable:true,
			items:[new ProjectQueryForm(),new ProjectQueryGrid()]
		});
		
		//操作员没有权限，隐藏按钮
		//有权限，但选择行数据不相符，禁用按钮
		var resources = App.subMenu;
		for(var i=0,len=resources.length;i<len;i++){
			var res = resources[i];
			if(res['sub_system_id'] == '8'){
				if(res['handler'] == 'project_add_btn_id'){
					Ext.getCmp('project_add_btn_id').show();
				}else if(res['handler'] == 'project_modify_btn_id'){
					Ext.getCmp('project_modify_btn_id').show();
				}else if(res['handler'] == 'project_addr_linked_btn_id'){
					Ext.getCmp('project_addr_linked_btn_id').show();
				}else if(res['handler'] == 'project_valid_btn_id'){
					Ext.getCmp('project_valid_btn_id').show();
				}else if(res['handler'] == 'project_invalid_btn_id'){
					Ext.getCmp('project_invalid_btn_id').show();
				}
			}
		}
	}
});






