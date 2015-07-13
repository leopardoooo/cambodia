/**
 * 仓库配置管理
 * @class DepotManagePanel
 * @extends Ext.grid.GridPanel
 */
var rootNodeId = '4501';
DepotManagePanel = Ext.extend( Ext.grid.GridPanel , {
	depotManageStore: null ,
	constructor: function( cfg ){
		Ext.apply( this , cfg || {});
		depotManagePanel = this;
		
		this.depotManageStore = new Ext.data.JsonStore({
			url  : root + '/resource/ResourceCfg!queryAllDepot.action' ,
			root : 'records' ,
			totalProperty: 'totalProperty',
			fields : ['depot_id','depot_name','depot_pid','status','remark',
				'county_id','county_name','area_id','area_name','district']
		}) ;
		DepotManagePanel.superclass.constructor.call( this , {
			id:'depotManagePanelId',
			region : "center" ,
			title  : '符合条件的仓库【仓库管理】',
			ds: this.depotManageStore ,
			cm: new Ext.grid.ColumnModel([  //表头信息描述
				{header: '仓库名称', dataIndex: 'depot_name'},
				{header: '仓库所属区域', dataIndex: 'area_name'},
				{header: '仓库所属县市', dataIndex: 'county_name'},
				{id:'remark_id',header:'备注',dataIndex:'remark'},
				{header: '操作', dataIndex: 'depot_id' ,renderer:function( v , md, record , i  ){
					return "<a href='#' onclick='depotManagePanel.deleteInfo("+ v +","+ i +");' style='color:blue'> 删除 </a>";
				}}
			]),
			sm:new Ext.grid.RowSelectionModel({}),
			autoExpandColumn: 'remark_id',
			bbar: new Ext.PagingToolbar({store: this.depotManageStore }),
			tbar: [' ',' ','输入关键字' , ' ',
				new Ext.ux.form.SearchField({
	                store: this.depotManageStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: '支持仓库名称模糊查询'
	            })
			]
		});
	},
	deleteInfo:function(v ,rowIndex){
  		Confirm("确定要删除仓库吗?", null ,function(){
	  		Ext.Ajax.request({
	  			url: root + '/resource/ResourceCfg!updateDepotStatus.action',
	  			params: {depotId: v },
	  			success: function( res,ops ){
	  				var rs = Ext.decode(res.responseText);
	  				if(true === rs.success){
		  				Alert('操作成功!',function(){
		  					var record = depotManagePanel.getSelectionModel().getSelected();
		  					depotManagePanel.getStore().remove(record);
		  					Ext.getCmp('navigationDepotId').getRootNode().reload();
		  				});
	  				}else{
	  					Alert('操作失败');
	  				}
		  		}
	  		});
  		})
  	},
	loadData: function( ps ){
		this.depotManageStore.baseParams = ps ;
		this.depotManageStore.load({
			params: { start: 0, limit: Constant.DEFAULT_PAGE_SIZE }
		});
	},
	listeners:{
		 "rowdblclick" : function(g, i, e){
  			var formpanel = Ext.getCmp('depotManageFormId');
  			var form = formpanel.getForm();
  			form.reset();
  			
			var rs = g.getStore().getAt(i);
			
//			var id = [rs.get("area_id"),rs.get("county_id")];
//  			var text= [rs.get("area_name"),rs.get("county_name")];
//  			rs.set("district", {id: id ,text: text});
  			
  			var data = rs.data;
  			var dis = Ext.getCmp('depot_district_id');
  			if( !dis.disabled){//编辑时不可以选择地区、县市
  				dis.disable();
  			}
  			
  			form.findField('depotDto.depot_id').setValue(data['depot_id']);
  			form.findField('depotDto.depot_name').setValue(data['depot_name']);
  			dis.setRawValue(data['area_name']+" / "+data['county_name']);
  			Ext.getCmp('depot_county_id_id').setValue(data['county_id']);
  			Ext.getCmp('depot_area_id_id').setValue(data['area_id']);
  			Ext.getCmp('depot_depot_pid_id').setValue(data['depot_pid']);
  			Ext.getCmp('area_name_id').setValue(data['area_name']);
  			Ext.getCmp('county_name_id').setValue(data['county_name']);
  			Ext.getCmp('depot_pname_id').setValue(Ext.getCmp('navigationDepotId').getSelectionModel().getSelectedNode().text);
  			
  			formpanel.setTitle( formpanel.editTitle );
  			formpanel.setIconClass( formpanel.editCls );
		 }
	}
});

DepotManageForm = Ext.extend(Ext.form.FormPanel,{
	addTitle:'新增仓库',editTitle:'修改仓库',
	addCls:'icon-add-user',editCls:'icon-edit-user',
	constructor : function() {
		DepotManageForm.superclass.constructor.call(this, {
			id:'depotManageFormId',
			title:  this.addTitle,
			iconCls: this.addCls,
			region: "south" ,
			height: 135, 
			split: true,
			bodyStyle: 'padding: 5px',
			layout:'column',
			defaults: { 
				labelWidth: 75,
				baseCls: 'x-plain'
			},
			items: [{xtype: 'hidden',name: 'depotDto.depot_id'},
					{id:'depot_county_id_id',xtype: 'hidden',name: 'depotDto.county_id'},
					{id:'depot_area_id_id',xtype: 'hidden',name: 'depotDto.area_id'},
					{id:'depot_depot_pid_id',xtype: 'hidden',name: 'depotDto.depot_pid'},
					
					{columnWidth:.3,layout:'form',items:[
						{fieldLabel: '仓库名称',xtype:'textfield',
							name: 'depotDto.depot_name',    
							allowBlank: false,
							maxLength: 30
						},{
							id:'depot_district_id',
							fieldLabel: '区域/县市',
							allowBlank: false,
							hiddenName:'depotDto.district',
							model: DISTRICT_MODEL_AREA_COUNTY,
						    xtype: 'districtcombo',
						    listeners:{
						    	scope:this,
						    	select:function(combo){
						    		var datas = combo.getRawValue().split(' / ');
						    		Ext.getCmp('area_name_id').setValue(datas[0]);
						    		Ext.getCmp('county_name_id').setValue(datas[1]);
						    	}
						    }
					}
					]},
					{columnWidth:.3,layout:'form',defaultType:'textfield',items:[
						{id:'area_name_id',fieldLabel:'地区',name:'area_name',allowBlank:false,readOnly:true},
						{id:'county_name_id',fieldLabel:'县市',name:'county_name',allowBlank:false,readOnly:true}
					]},
					{columnWidth:.3,layout:'form',items:[
						{id:'depot_pname_id',fieldLabel:'上级仓库',allowBlank:false,
							xtype:'textfield',name:'depot_pname',readOnly:true}
					]}],
			buttons: [{
				text: ' 重 置 ',
				scope: this,
				handler:this.doReset
			},{
				text: ' 保 存 ',
				scope: this,
				handler:this.doSave
			}]
		})
	},
	doSave: function(){
		if(!this.getForm().isValid()){return ;}
		var old =  this.getForm().getValues(), newValues = {};
		for(var key in old){
			if(key.indexOf('depotDto.')!=-1)
				newValues[key] = old[key];
		}
		
		Confirm("确定要保存吗?", this , function(){
			Ext.Ajax.request({
				params: newValues,
				url: root + '/resource/ResourceCfg!saveDepot.action',
				scope:this,
				success: function(res,ops){
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert("操作成功!",function(){
							Ext.getCmp('depotManagePanelId').getStore().reload();
							Ext.getCmp('navigationDepotId').getRootNode().reload();
							this.doReset();
							this.setIconClass( this.addCls  );
							this.setTitle( this.addTitle );
						},this);
					}else{
						Alert("操作失败!");
					}
				}
			});
		});
	},
	doReset: function(){
		this.getForm().reset();
		this.setIconClass( this.addCls  );
		this.setTitle( this.addTitle );
		Ext.getCmp('depot_district_id').setRawValue('');
		var tree = Ext.getCmp('navigationDepotId');
//		tree.fireEvent('click',tree.getSelectionModel().getSelectedNode(),tree);
	}
 });
  
NavigationDepot = Ext.extend( Ext.ux.FilterTreePanel , {
	searchFieldWidth: 140,
	constructor: function(){
		var loader = new Ext.tree.TreeLoader({
			url : root+"/resource/ResourceCfg!queryDepotTreeByCountyId.action"
		});
		NavigationDepot.superclass.constructor.call(this, {
			id:'navigationDepotId',
			region: 'west',
			width 	: 210,
			split	: true,
			minSize	: 210,
	        maxSize	: 260,
	        margins		:'0 0 3 2',
	        lines		:false,
	        autoScroll	:true,
	        animCollapse:true,
	        animate		: false,
	        collapseMode:'mini',
			bodyStyle	:'padding:3px',
			loader 		: loader,
			rootVisible: false,
	        root: {
				id 		: '0',
				iconCls : 'x-tree-root-icon',
				nodeType:'async',
				text: '系统仓库结构'
			}
		});
		this.getRootNode().expand();
	},
	initComponent:function(){
		NavigationDepot.superclass.initComponent.call(this);
		this.on('load',function(){
			var node = this.getRootNode().firstChild;
			//默认选中根节点的第一个子节点，即页面上显示的第一个节点
			this.getSelectionModel().select(this.getRootNode().firstChild);
			if(node.id != rootNodeId)//如果不是总节点，不能选择地区、县市
				Ext.getCmp('depot_district_id').disable();
			Ext.getCmp('depotManagePanelId').getStore().load({
				params: {pid: node.id,start: 0, limit: Constant.DEFAULT_PAGE_SIZE}
			});
		},this,{delay:100});
		
	},
	initEvents : function(){
		NavigationDepot.superclass.initEvents.call(this);
		this.on("click" , function( node , e){
			var id = node.id ;
			var formPanel = Ext.getCmp('depotManageFormId')
			formPanel.getForm().reset();//重置form
			formPanel.setIconClass( formPanel.addCls  );
			formPanel.setTitle( formPanel.addTitle );
			
			var depotDistrict = Ext.getCmp('depot_district_id');
			depotDistrict.setRawValue('');
			
			if( id === rootNodeId){
				if(depotDistrict.disabled)
					depotDistrict.enable();
				var form = Ext.getCmp('depotManageFormId');
				form.setIconClass( form.addCls  );
				form.setTitle( form.addTitle );
			}else{
				//如果不是总节点
				if(!depotDistrict.disabled){
					depotDistrict.disable();
				}
				var attr = node.attributes;
				if(attr && attr['others']){
					var others = attr['others'];
					Ext.getCmp('depot_county_id_id').setValue(others['county_id']);
					Ext.getCmp('depot_area_id_id').setValue(others['area_id']);
					Ext.getCmp('area_name_id').setValue(others['area_name']);
					Ext.getCmp('county_name_id').setValue(others['county_name']);
				}
			}
			//将要添加的节点的父节点为当前选中节点
			Ext.getCmp('depot_depot_pid_id').setValue(id);
			Ext.getCmp('depot_pname_id').setValue(node.text);
			//查询当前节点和其子节点信息
			Ext.getCmp('depotManagePanelId').getStore().load({
				params: {pid: id,start: 0, limit: Constant.DEFAULT_PAGE_SIZE}
			});
		} , this);
	}
});


DepotManager = Ext.extend(Ext.Panel,{
	constructor:function(){
		var depotManagePanel = new DepotManagePanel();
		var depotManageForm = new DepotManageForm();
		var navigationDepot = new NavigationDepot();
		DepotManager.superclass.constructor.call(this,{
			id:'DepotManager',
			title:'仓库管理',
			closable: true,
			border : false ,
			layout : 'border',
			baseCls:"x-plain"
			,items : [depotManagePanel,depotManageForm, navigationDepot ] 
		});
	}
});