
ResGrid = Ext.extend(Ext.grid.GridPanel, {
	constructor: function(){
		this.resStore = new Ext.data.JsonStore({
			url:root+'/config/Res!queryRes.action',
			fields:['res_name','res_desc','status','status_text','res_type','res_type_text','isRecommend',
				'currency','currency_text','create_time','serv_id','serv_id_text','res_id','optr_id','county_id','area_id', 'band_width'
			],
			totalProperty:'totalProperty',
			root:'records'
		});
		this.resStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true
		});
		var countyId = App.data.optr['county_id'];
		var colModel = new Ext.grid.ColumnModel([
			sm,
			{header:'BOSS资源名称',dataIndex:'res_name',width:150,renderer:App.qtipValue},
			{header:'BOSS服务类型',dataIndex:'serv_id_text',width:100},
			{header:'状态',dataIndex:'status_text',width:80	,renderer:Ext.util.Format.statusShow},
//			{header:'BOSS资源类型',dataIndex:'res_type_text',width:80},
//			{header:'全省通用',dataIndex:'currency_text',width:65},
			{header: '带宽', dataIndex: 'band_width', width:80},
			{header:'创建时间',dataIndex:'create_time',width:135},
			{header:'描述',dataIndex:'res_desc',id:'res_desc_id'},
			{header:'操作',dataIndex:'county_id',renderer:function(v, md, record, i){
					var str = "";
					if(countyId == '4501' || countyId == v){
						str = "<a href='#' style='color:blue' onclick=Ext.getCmp('resGridId').editRecord()>修改</a>&nbsp;&nbsp;&nbsp;"
						if(record.get('status') == 'INVALID'){
							str +="<a href='#' onclick=Ext.getCmp('resGridId').doActive()>启用</a>";
						}else if(record.get('isRecommend') == 'F'){
							str += "<a href='#' onclick=Ext.getCmp('resGridId').doDel()>禁用</a>";
						}
					}
					return str;
				}
			}
		]);
		ResGrid.superclass.constructor.call(this, {
			id:'resGridId',
			title:'BOSS资源信息',
			border:false,
			region:'north',
			height:350,
			autoExpandColumn:'res_desc_id',
			store:this.resStore,
			colModel:colModel,
			sm:sm,
			bbar:new Ext.PagingToolbar({store:this.resStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
			tbar : [' ', ' ', '输入关键字', ' ',
					new Ext.ux.form.SearchField({
								store : this.resStore,
								width : 200,
								hasSearch : true,
								emptyText : '支持BOSS资源名模糊查询'
							}), '->', 
					{
						text : '添加',
						iconCls : 'icon-add',
						scope : this,
						handler : this.addRecord
					}],
			listeners:{
				scope:this,
				rowclick:function(){
					var store = Ext.getCmp('serverResGridId').getStore();
					store.baseParams['resId'] = this.getSelectionModel().getSelected().get('res_id');
					store.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
				}
			}
		});
	},
	addRecord: function(){
		var win = Ext.getCmp('resWinId');
		if(!win){
			win = new ResWin();
		}
		win.show();
	},
	editRecord: function(){
		var records = this.getSelectionModel().getSelections();
		if(records && records.length == 1){
			var win = Ext.getCmp('resWinId');
			if(!win){
				win = new ResWin();
				win.show.defer(500,win,[records[0]]);
			}else{
				win.show(records[0]);
			}
		}else{
			Alert('请选择一行进行修改');
		}
	},
	doDel : function(){
		Confirm('确定禁用该资源吗吗?',this,function(){
			var record = this.getSelectionModel().getSelected(),obj={};
			obj['resId'] = record.get('res_id');
			Ext.Ajax.request({
				url:root+'/config/Res!deleteRes.action',
				params:obj,
				scope:this,
				success:function(res,opt){
					Alert('操作成功!');
					this.getStore().reload();
				}
			});
		});
	},
	doActive : function(){
		var record = this.getSelectionModel().getSelected(),obj={};
		obj['resId'] = record.get('res_id');
		Ext.Ajax.request({
			url:root+'/config/Res!activeRes.action',
			params:obj,
			scope:this,
			success:function(res,opt){
				Alert('操作成功!');
				this.getStore().reload();
			}
		});
	}
});

ServerResGrid = Ext.extend(Ext.grid.GridPanel, {
	constructor: function(){
		this.serverResStore = new Ext.data.JsonStore({
			url:root+'/config/Res!queryServerRes.action',
			fields:['res_name','server_id','external_res_id','boss_res_id',
				'boss_res_name','server_name','optr_id','county_id','area_id',
				'serv_type','serv_type_text'],
			totalProperty:'totalProperty',
			root:'records'
		});
		this.serverResStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true
		});
		var countyId = App.data.optr['county_id'];
		var colModel = new Ext.grid.ColumnModel([
			sm,
			{header:'服务器名称',dataIndex:'server_name',width:100,renderer:App.qtipValue},
			{header:'资源编号',dataIndex:'external_res_id',width:340,renderer:App.qtipValue},
			{header:'资源名称',dataIndex:'res_name',width:250,renderer:App.qtipValue},
			{header:'BOSS资源名称',dataIndex:'boss_res_name',width:250,renderer:App.qtipValue},
			{header:'服务类型',dataIndex:'serv_type_text',width:65},
			{header:'操作',dataIndex:'county_id',renderer:function(v, md, record, i){
					var res = '';
					if(countyId == '4501' || countyId == v){
						res = "<a href='#' style='color:blue' onclick=Ext.getCmp('serverResGridId').editRecord()>修改</a>&nbsp;&nbsp;&nbsp;";
						res += "<a href='#' onclick=Ext.getCmp('serverResGridId').doDel()>删除</a>";
					}
					return res;
				}
			}
		]);
		ServerResGrid.superclass.constructor.call(this, {
			id:'serverResGridId',
			title:'服务器资源信息',
			border:false,
			region:'center',
			store:this.serverResStore,
			colModel:colModel,
			sm:sm,
			bbar:new Ext.PagingToolbar({store:this.serverResStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
			tbar : [' ', ' ', '输入关键字', ' ',
					new Ext.ux.form.SearchField({
								store : this.serverResStore,
								width : 200,
								hasSearch : true,
								emptyText : '支持资源名模糊查询'
							}), '->', 
					{
						text : '添加',
						iconCls : 'icon-add',
						scope : this,
						handler : this.addRecord
					}]
		});
	},
	addRecord: function(){
		if(Ext.getCmp('resGridId').getSelectionModel().getSelected()){
			var win = Ext.getCmp('serverResWinId');
			if(!win){
				win = new ServerResWin();
			}
			win.show();
		}else{
			Alert('请选中一条BOSS资源记录，再添加服务器资源!');
		}
	},
	editRecord: function(){
		var records = this.getSelectionModel().getSelections();
		if(records && records.length == 1){
			var win = Ext.getCmp('serverResWinId');
			if(!win){
				win = new ServerResWin();
				win.show.defer(500,win,[records[0]]);
			}else{
				win.show(records[0]);
			}
		}else{
			Alert('请选择一行进行修改');
		}
	},
	doDel: function(){
		Confirm('确定删除吗?',this,function(){
			var record = this.getSelectionModel().getSelected(),obj={};
			var data = record.data;
			for(var d in data){
				obj['serverRes.'+d] = data[d];
			}
			Ext.Ajax.request({
				url:root+'/config/Res!deleteServerRes.action',
				params:obj,
				scope:this,
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					if(data.success === true){
						this.getStore().remove(record);
						this.getStore().commitChanges();
					}
				}
			});
		});
	}
});

ResWin = Ext.extend(Ext.Window,{
	constructor:function(){
		ResWin.superclass.constructor.call(this,{
			id:'resWinId',
			title:'添加BOSS资源',
			border:false,
			width:350,
			height:280,
			layout:'fit',
			items:[
				{id:'resFormId',xtype:'form',labelWidth:120,
					bodyStyle:'padding-top:10px',items:[
					{xtype:'hidden',name:'res_id'},
					{xtype:'textfield',fieldLabel:'BOSS资源名称',name:'res_name',allowBlank:false,
						listeners:{
							scope:this,
							change:this.checkResName
						}
					},
					{xtype:'paramcombo',fieldLabel:'BOSS服务类型',allowBlank:false,
						hiddenName:'serv_id',paramName:'SERV_ID',
						listeners:{
							scope: this,
							select: function(combo,record,idx){
								var value = combo.getValue();
								var bandWidth = Ext.getCmp('resFormId').getForm().findField('band_width');
								if(value == 'BAND'){
									if(bandWidth.disabled)
										bandWidth.enable();
								}else{
									if(!bandWidth.disabled){
										bandWidth.setValue(null);
										bandWidth.disable();
									}
								}
							}
						}
					},
					/*{xtype:'paramcombo',fieldLabel:'BOSS资源类型',disabled:true,allowBlank:false,
						hiddenName:'res_type',paramName:'RES_TYPE'},
					{xtype:'checkbox',fieldLabel:'全省通用',name:'currency',inputValue:'T',
						disabled : App.data.optr['county_id'] == '4501'
											? false
											: true},*/
					{xtype: 'numberfield', fieldLabel: '带宽', name: 'band_width', allowDecimals: false, allowNegative: false},
					{xtype:'textarea',fieldLabel:'资源描述',name:'res_desc',anchor:'95%',height:60}
				]}
			],
			buttonAlign:'center',
			buttons:[
				{text:'保  存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关  闭',iconCls:'icon-close',scope:this,handler:this.doClose}
			],
			listeners:{
				scope:this,
				hide:this.doClose
			}
		});
		App.form.initComboData(this.findByType('paramcombo'));
	},
	show: function(record){
		ResWin.superclass.show.call(this);
		if(record){
			this.setTitle('修改BOSS资源');
			var form = Ext.getCmp('resFormId').getForm();
			Ext.getCmp('resFormId').getForm().loadRecord(record);
			if(record.get('serv_id') == 'BAND'){
				form.findField('band_width').enable(); 	
			}else{
				form.findField('band_width').disable();	
			}
			/*if(record.get('serv_id') == 'ITV'){
				var comp = form.findField('res_type'); 
				if(comp.disabled)comp.enable();
			}else{
				var comp = form.findField('res_type');
				if(!comp.disabled)comp.disable();
			}*/
			/*if(record.get('currency') === 'T'){
				form.findField('currency').setValue(true);
			}*/
		}else{
			this.setTitle('添加BOSS资源');
		}
	},
	checkResName: function(txt,newValue,oldValue){
		if(!Ext.isEmpty(newValue) && newValue != oldValue){
			Ext.Ajax.request({
				url:root+'/config/Res!getResByResName.action',
				params:{'resName':newValue},
				scope:this,
				success:function(res){
					var resName = Ext.decode(res.responseText);
					if(!Ext.isEmpty(resName)){
						if(resName == newValue){
							Alert('该资源名已存在!',function(){
								txt.setValue('');
								txt.focus();
							},this);
						}else{
							Alert('有相近的资源名：'+resName+', 请确认!');
						}
					}
				}
			});
		}
	},
	doSave:function(){
		var form = Ext.getCmp('resFormId').getForm();
		if(!form.isValid())return;
		var values = form.getValues();
		if(Ext.isEmpty(values['currency'])){
			values['currency'] = 'T';
		}
		var obj = {};
		for(var prop in values){
			obj['res.'+prop] = values[prop];
		}
		var url = root+'/config/Res!saveRes.action';
		if(!Ext.isEmpty(obj['res.res_id'])){
			url = root+'/config/Res!updateRes.action';
		}
		Ext.Ajax.request({
			url:url,
			params:obj,
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data.success === true){
					var servIdCmp = form.findField('serv_id');
					if(servIdCmp.disable){
						servIdCmp.enable();
					}
					Ext.getCmp('resGridId').getStore().load({
						params : {
							start : 0,
							limit : Constant.DEFAULT_PAGE_SIZE
						}
					});
					this.doClose();
				}
			}
		});
	},
	doClose:function(){
		Ext.getCmp('resFormId').getForm().reset();
		this.hide();
	}
});

ServerResWin = Ext.extend(Ext.Window,{
	oldServerRes:{},//修改前的记录
	serverStore : null,
	bossResStore : null,
	currServType: null,		//当前选择的serv_id
	constructor:function(){
		this.serverStore = new Ext.data.JsonStore({
			url:root+'/config/Res!queryServerByServType.action',
			fields:['server_id','server_name','serv_type']
		});
		this.bossResStore = new Ext.data.JsonStore({
			url:root+'/config/Res!queryRes.action',
			root:'records',
			totalProperty:'totalProperty',
			fields:['res_id','res_name','serv_id']
		});
		this.bossResStore.load();
		
		this.ottResStore = new Ext.data.JsonStore({
			url: root+'/config/Config!queryAllOttAuth.action',
			fields: ['id', 'name']
		});
		
		ServerResWin.superclass.constructor.call(this,{
			id:'serverResWinId',
			title:'添加服务器资源',
			border:false,
			width:550,
			height:400,
			layout:'fit',
			closeAction: 'close',
			items:[
				{id:'serverResFormId',xtype:'form', labelWidth: 100,
					bodyStyle:'padding-top:5px',items:[
					{xtype:'combo',fieldLabel:'服务器名',hiddenName:'server_id',
						store: this.serverStore,
						displayField:'server_name',valueField:'server_id',allowBlank:false,
						listeners:{
							scope:this,
							select:function(combo,record){
								this.bossResStore.baseParams['servId'] = record.get('serv_type');
								this.bossResStore.load();
								
								this.doOutOttRes(combo.getValue());
							}
						}
					},
					{
						id:'externalResId', 
						layout: 'form',
						border: false,
						anchor: '100%',
						items:[{xtype:'textfield',fieldLabel:'资源编号',name:'external_res_id',allowBlank:false}]},
					{
						id:'resNameId', 
						layout: 'form',
						border: false,
						anchor: '100%',
						items:[{xtype:'textfield',fieldLabel:'资源名称',name:'res_name',anchor:'95%',allowBlank:false}]},
					{
						id:'outOttResPanelId', 
						layout: 'form',
						border: false,
						anchor: '100%',
						items:[{xtype: 'combo', fieldLabel: '外部资源', id: 'outOttResId',
						store: this.ottResStore, displayField: 'name', valueField: 'id', editable: false,
						listeners: {
							scope: this,
							select: function(cb, record){
								var form = Ext.getCmp('serverResFormId').getForm();
								form.findField('external_res_id').setValue(cb.getValue());
								form.findField('res_name').setValue(record.get('name'));
							}
						}
					}]},
					{xtype:'combo',fieldLabel:'BOSS资源名称',hiddenName:'boss_res_id',id:'boss_res_id_id',
//						store:this.bossResStore,
						store:new Ext.data.JsonStore({
							fields:['res_id','res_name','serv_id']
						}),
						displayField:'res_name',valueField:'res_id',allowBlank:false,
						listWidth:400,typeAhead: true,anchor:'95%',//readOnly:true,
						editable:true,forceSelection:true
					}
				]}
			],
			buttonAlign:'center',
			buttons:[
				{text:'保  存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关  闭',iconCls:'icon-close',scope:this,handler:this.doClose}
			],
			listeners:{
				scope:this,
				hide:this.doClose
			}
		});
	},
	show: function(record){
		ServerResWin.superclass.show.call(this);
		Ext.getCmp('boss_res_id_id').getStore().removeAll();
		this.bossResStore.reload();
		var servType = null;
		if(record){
			Ext.getCmp('serverResFormId').getForm().loadRecord(record);
			this.setTitle('修改服务器资源');
			var data = record.data,obj={};
			for(var i in data){
				obj['oldServerRes.'+i] = data[i];
			}
			this.oldServerRes = obj;
			servType = record.get('serv_type');
		}else{
			this.setTitle('添加服务器资源');
		}
		var bossResRecord = record || Ext.getCmp('resGridId').getSelectionModel().getSelected();
		var ResRecord = Ext.data.Record.create(['res_id','res_name']);
		var resRec = new ResRecord();
		if(record){
			resRec = new ResRecord({
				'res_id':bossResRecord.get('boss_res_id'),
				'res_name':bossResRecord.get('boss_res_name')
			});
		}else{
			resRec = new ResRecord({
				'res_id':bossResRecord.get('res_id'),
				'res_name':bossResRecord.get('res_name')
			});
		}
		Ext.getCmp('boss_res_id_id').getStore().add(resRec);
		Ext.getCmp('boss_res_id_id').setValue(resRec.get('res_id'));
		Ext.getCmp('boss_res_id_id').setReadOnly(true);
		
		
		if(Ext.isEmpty(servType)){
			servType = bossResRecord.get('serv_id');
		}
		this.serverStore.load({
			params:{
				serv_type: servType
			}
		});
		
		if(record){
			this.ottResStore.on('load', function(){
				this.doOutOttRes(record.get('server_id'), record.get('external_res_id'));
			}, this);
		}
		this.ottResStore.load();
	},
	doOutOttRes: function(serverId, externalResId){
		var resIdCmp = Ext.getCmp('externalResId');
		var resNameCmp = Ext.getCmp('resNameId');
		var outOttResCmp = Ext.getCmp('outOttResPanelId');
		if(serverId == 'SDK'){
			resIdCmp.setVisible(false);
			resNameCmp.setVisible(false);
			resIdCmp.allowBlank = true;
			resNameCmp.allowBlank = true;
			
			outOttResCmp.setVisible(true);
			outOttResCmp.allowBlank = false;
			if(externalResId)
				Ext.getCmp('outOttResId').setValue(externalResId);
		}else{
			resIdCmp.setVisible(true);
			resNameCmp.setVisible(true);
			resIdCmp.allowBlank = false;
			resNameCmp.allowBlank = false;
			
			outOttResCmp.setVisible(false);
			outOttResCmp.allowBlank = true;
		}
	},
	doSave:function(){
		var form = Ext.getCmp('serverResFormId').getForm();
		if(!form.isValid())return;
		var values = form.getValues();
		delete values['outOttResId'];
		var obj = {};
		for(var prop in values){
			obj['serverRes.'+prop] = values[prop];
		}
		Ext.apply(obj,this.oldServerRes);
		
		var url = root+'/config/Res!saveServerRes.action';
		Ext.Ajax.request({
			url:url,
			params:obj,
			scope:this,
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				if(data.success === true){
					Ext.getCmp('serverResGridId').getStore().load({
						params : {
							start : 0,
							limit : Constant.DEFAULT_PAGE_SIZE
						}
					});
					this.doClose();
				}
			}
		});
	},
	doClose:function(){
		Ext.getCmp('serverResFormId').getForm().reset();
		this.oldServerRes = {};
		this.hide();
	}
});

ResView = Ext.extend(Ext.Panel,{
	constructor:function(){
		ResView.superclass.constructor.call(this,{
			id:'ResView',
			title:'资源管理',
			closable:true,
			border:false,
			layout:'border',
			items:[new ResGrid(), new ServerResGrid()]
		});
	}
});