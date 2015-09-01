/**
 * 修改地址
 */
 
QueryFilterTree = Ext.extend(Ext.ux.QueryFilterTreePanel,{
	doSearch:function(){
		var _v = this.searchT.getValue();
		Ext.Ajax.request({
			url: Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrTree.action",
			scope : this,
			params : {
				comboQueryText : _v
			},
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				var root = new Ext.tree.AsyncTreeNode({
						  text:'gen',
						  id:'root',
						  draggable:false,
						  children:data
						 });
				this.setRootNode(root);
			}
		});				
	}
});

AddressTreeCombo = Ext.extend(Ext.ux.TreeCombo,{
	initList : function() {
		var rootVisible = false;
		if (this.rootNodeCfg) {
			rootVisible = true;
		} else {
			this.rootNodeCfg = {
				expanded : true
			};
		}
		this.list = new QueryFilterTree({
			root : new Ext.tree.AsyncTreeNode(this.rootNodeCfg),
			loader : new Ext.tree.TreeLoader({
						dataUrl : this.treeUrl,
						baseParams : this.treeParams
						,listeners : {
							beforeload : this.onBeforeLoad,
							scope : this
						}
					}),
			floating : true,
			height : this.treeHeight,
			autoScroll : true,
			animate : false,
			searchFieldWidth : this.treeWidth - 80,
			rootVisible : rootVisible,
			listeners : {
				click : this.onNodeClick,
				checkchange : this.onCheckchange,
				scope : this
			},
			alignTo : function(el, pos) {
				this.setPagePosition(this.el.getAlignToXY(el, pos));
			}
		});
		if (this.minChars == 0) {
			this.doQuery("");
		}
	},
	listeners:{
		expand:function(thisCmp){
			function getFocus(){
				var search = thisCmp.list.searchT;//如果第一次获取焦点,list会为空
				if(search){
//					search.setValue('输入关键字搜索');
					var dom = search.el.dom;
					dom.select();
				}
			};
			getFocus.defer(200,this);
		}
	},
	onNodeClick : function(node, e) {		
		if (!this.isCanClick(node)) {
			return;
		}
		this.setRawValue('');
		Ext.Ajax.request({
			scope : this,
			url: Constant.ROOT_PATH+"/commons/x/QueryParam!queryCustAddrName.action",
			params : {
				addrId : node.id
			},
			success : function(res,opt){
				var rec = Ext.decode(res.responseText);
				//		// 显示隐藏值
				this.addOption(node.id, rec.addrName);
				this.setValue(node.id);
				this.setRawValue(rec.addrName);
				this.collapse();
				this.fireEvent('select', this, node, node.attributes);
			}
		});
		
	},
	firstExpand: true,
	doQuery : function(q, forceAll) {
		if(!this.firstExpand){
			this.firstExpand = true;
			this.list.expandAll();
		}
		this.expand();
	},
	onBeforeLoad:function(l,node){
		l.on('beforeload',function(loader,node){
  			l.baseParams.addrId=node.id; //通过这个传递参数，这样就可以点一个节点出来它的子节点来实现异步加载
		},l);
	}
	
});

 
QueryAddressForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	value:'UPDATE',
	constructor:function(p){
		this.parent = p;
		var mycounty = App.data.optr.county_id;
		QueryAddressForm.superclass.constructor.call(this,{
			border:false,
			bodyStyle:'padding-top:10px',
			layout:'column',
			labelWidth:55,
			items:[
				{columnWidth:.3,labelWidth:100,layout:'form',border:false,items:[{
					fieldLabel: '操作类型',
                    paramName: 'ADDRESS_UPDATE_TYPE',
                    xtype: 'paramcombo',
                    defaultValue: 'MX',
                    width:80,
                    id:'typeId',
                    hiddenName: 'type',
                    listeners:{
                       scope:this,
                       select:this.queryType
                    }
			  	}]},{
					xtype : 'hidden',
					id : 'tempCustAddress'
				},{id:'custAddressId',columnWidth:.3,layout:'form',border:false,items:[
					{xtype:'textfield',name:'cust.address',fieldLabel:'客户地址'}
				]},{columnWidth:.3,layout:'form',border:false,items:[ 
					new AddressTreeCombo({
						xtype : 'treecombo',
						fieldLabel:'小区',
						width : 120,
						id:'comboAddressId',
						treeWidth : 400,
						minChars : 2,
						height : 22,
						emptyText : '两个字搜索地址',
						blankText : '客户地址',
						treeUrl: Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrTree.action",
						hiddenName:'cust.addr_id'
					})]
				},
				{
				columnWidth:.70,
				layout:'table',
				layoutConfig: { columns:12 },
				baseCls: 'x-plain',
				anchor: '100%',
				labelWidth: 75,
				id:'queryItemsId',
				defaults : {
					xtype : 'textfield'
				},
			    items: [{xtype:'displayfield',width:60},{
			    	width : 60,
			    	id : 'cust.t1',
			    	vtype : 'alphanum',
			    	name : 'cust.t1'
			    },{
			    	xtype : 'label',
			    	html : '<font size="2"/>'+'号'
			    },{
			    	width : 50,
			    	id : 'cust.t2',
			    	vtype : 'alphanum',
			    	name : 'cust.t2'
			    },{
			    	xtype : 'label',
			    	html : '<font size="2"/>'+'栋'
			    },{
			    	width : 50,
			    	id : 'cust.t3',
			    	vtype : 'alphanum',
			    	name : 'cust.t3'
			    },{
			    	xtype : 'label',
			    	html : '<font size="2"/>'+'单元'
			    },{
			    	width : 50,
			    	id : 'cust.t4',
			    	vtype : 'alphanum',
			    	name : 'cust.t4'
			    },{
			    	xtype : 'label',
			    	html : '<font size="2"/>'+'楼'
			    },{
			    	width : 50,
			    	id : 'cust.t5',
			    	vtype : 'alphanum',
			    	name : 'cust.t5'
			    },{
			    	xtype : 'label',
			    	html : '<font size="2"/>'+'室'
			    },{
			    	width : 80,
			    	id : 'cust.note',
			    	name : 'cust.note'
			    }]
			},{
				columnWidth:.02,width : 50,id:'queryDeviceBtnId',xtype:'button',
				text:'查  询',iconCls:'icon-query',scope:this,handler:this.doQuery
				}]
		});
	},
	initComponent:function(){
		QueryAddressForm.superclass.initComponent.call(this);
		App.form.initComboData(this.findByType('paramcombo'));	
	},
	doQuery:function(){
		if(!this.getForm().isValid())return;
		var type = Ext.getCmp('typeId').getValue();
		if(type == 'PL'){
			var panle = Ext.getCmp('AddressFormPanelId');
			var addrId =  Ext.getCmp('comboAddressId').getValue();
			var addrText =  Ext.getCmp('comboAddressId').getRawValue();
			Ext.Ajax.request({
					scope : this,
					url:Constant.ROOT_PATH + "/commons/x/QueryCust!queryAddressAll.action",
					params : {
						addrId : addrId
					},
					success : function(res, opt) {
						var rs = Ext.decode(res.responseText);
						Ext.getCmp('beforeAddrTextId').setValue(addrText);
						Ext.getCmp('custNumId').setValue(rs.simpleObj);
						Ext.getCmp('beforeAddrId').setValue(addrId);
					}
				})
			
		}else{
			var values = this.getForm().getValues();
			if(Ext.isEmpty(values['cust.addr_id']) && Ext.isEmpty(values['cust.address'])){
				Alert('请输入客户地址或选择小区!');
				return;
			}
			
			var grid = Ext.getCmp('queryCustAddressId');
			grid.getStore().rejectChanges(); //撤销所有修改
			grid.getStore().removeAll();
			var store = grid.getStore();
			store.baseParams = values;
			store.load({params: { start: 0, limit: 50 }});
		}
	},queryType:function(combo,record){
		var value = combo.getValue();
		if(this.value != value){
			this.value = value;
			this.parent.items.itemAt(1).removeAll();
			this.parent.items.itemAt(2).removeAll();
			if(value == 'PL'){
				Ext.getCmp('queryItemsId').hide();
				this.parent.items.itemAt(1).add(new AddressFormPanel());
				Ext.getCmp('custAddressId').hide();
			}else{
				Ext.getCmp('queryItemsId').show();
				this.parent.items.itemAt(1).add(new QueryCustGrid());
				this.parent.items.itemAt(2).add(new AddressUpdateAllForm());
				Ext.getCmp('custAddressId').show();
			}
			this.parent.doLayout();
		}
	}
});

AddressUpdateAllForm = Ext.extend(Ext.form.FormPanel, {

	constructor: function(){
		AddressUpdateAllForm.superclass.constructor.call(this, {
			border:false,
			lableWidth:75,
			layout:'column',
			bodyStyle:'padding-top:10px',
			items:[
				{columnWidth:.3,layout:'form',border:false,items:[
					{xtype:'checkbox',labelSeparator:'',boxLabel:'全部更新',name:'isSelectAll',
						inputValue:'T',
						listeners:{
							check:function(field,newValue,oldValue){
								if(newValue === true){
									Ext.getCmp('queryCustAddressId').disable();
									Ext.getCmp('newAddrId').enable();
									Ext.getCmp('newAddressId').enable();
								}else{
									Ext.getCmp('queryCustAddressId').enable();
									Ext.getCmp('newAddrId').disable();
									Ext.getCmp('newAddrId').allowBlank = true;
									Ext.getCmp('newAddrId').clearInvalid();
									
									Ext.getCmp('newAddressId').disable();
									Ext.getCmp('newAddressId').allowBlank = true;
									Ext.getCmp('newAddressId').clearInvalid();
								}
							}
						}
					}
				]},
				{columnWidth:.35,layout:'form',border:false,items:[
					new AddressTreeCombo({
						xtype : 'treecombo',
						fieldLabel:'新小区',
						width : 120,
						id:'newAddrId',
						treeWidth : 400,
						minChars : 2,
						height : 22,
						disabled:true,
						emptyText : '两个字搜索地址',
						blankText : '客户地址',
						treeUrl: Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrTree.action",
						hiddenName:'new_addr_id'
					})
				]},
				{columnWidth:.35,layout:'form',border:false,items:[
					{
						xtype : 'textfield',
						fieldLabel:'新地址',
						name:'new_address',
						id:'newAddressId',
						disabled:true
					}
				]}
			]
		});
	}
	
});

QueryCustGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	store:null,	
	Columns:null,
	constructor:function(p){
		this.store = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH+ "/commons/x/QueryCust!queryAddrList.action",
			root : 'records' ,
			totalProperty: 'totalProperty',
			fields:['cust_id','cust_name','addr_id','address','addr_id_text','t1','t2','t3','t4','t5','note','old_address','county_id','area_id']
		});
		if(p==null){
			this.store.on('load',function(){Ext.getCmp('queryDeviceBtnId').enable();},this);
		}else{
			this.store.baseParams = {custId:p};
			this.store.load({params: { start: 0, limit: 50 }});
		}
		this.Columns = [
			{header:'客户姓名',dataIndex:'cust_name',width:100,renderer:App.qtipValue},
			{id:'addrTextId',header:'小区',dataIndex:'addr_id_text',width:130,renderer:App.qtipValue},
			{header:'号(可改)',dataIndex:'t1',width:60,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'栋(可改)',dataIndex:'t2',width:60,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'单元(可改)',dataIndex:'t3',width:60,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'楼(可改)',dataIndex:'t4',width:60,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'室(可改)',dataIndex:'t5',width:60,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'备注(可改)',dataIndex:'note',width:80,renderer:App.qtipValue,editor : new Ext.form.TextField({})},
			{header:'新地址',dataIndex:'address',width:250,renderer:App.qtipValue},
			{header:'旧地址',dataIndex:'old_address',width:250,renderer:App.qtipValue}
		];
		QueryCustGrid.superclass.constructor.call(this,{
			title:'客户信息',
			border:false,
			ds:this.store,
			id:'queryCustAddressId',
			columns:this.Columns,
			sm : new Ext.grid.CheckboxSelectionModel(),
			clicksToEdit : 1,				
			bbar: new Ext.PagingToolbar({store: this.store,pageSize: 50})
		});
	},initEvents:function(){
		QueryCustGrid.superclass.initEvents.call(this);
		this.on('beforeedit',this.beforeEdit,this);
		this.on('afteredit',this.afterEdit,this);
	},beforeEdit:function(obj){
			var record = obj.record;
			var fieldName = obj.field;//编辑的column对应的dataIndex
			if(fieldName === 'addr_id_text'){//属性列
				var propIdCm = this.getColumnModel().getColumnById('addrTextId');
					propIdCm.editor = new AddressTreeCombo({
						store:new Ext.data.JsonStore({
							fields:['id','text','others']
						}),valueField:'text',
				    	width:140,
						treeWidth:400,
						minChars:2,
						height: 22,
						allowBlank: false,
						treeUrl: Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrTree.action",
						hiddenName:'addr_id'
						,listeners:{
							delay:100,
							scope:this,
							select:function(combo,record,index){
								var condRecord = Ext.getCmp('queryCustAddressId').getSelectionModel().getSelected();
								condRecord.set('addr_id',record.id);
							},
							change:function(){
								this.changeAddr(obj);
							}
						}
					})
			}
	},afterEdit:function(obj){
		this.changeAddr(obj);
	},changeAddr:function(obj){
		var condRecord = obj.record;
		var addr = "";
		condRecord.set('address','');
		if(!Ext.isEmpty(condRecord.get('addr_id_text')))
		addr +=condRecord.get('addr_id_text');
		if(!Ext.isEmpty(condRecord.get('t1')))
		addr +=condRecord.get('t1')+'号';
		if(!Ext.isEmpty(condRecord.get('t2')))
		addr +=condRecord.get('t2')+'栋';
		if(!Ext.isEmpty(condRecord.get('t3')))
		addr +=condRecord.get('t3')+'单元';
		if(!Ext.isEmpty(condRecord.get('t4')))
		addr +=condRecord.get('t4')+'楼';
		if(!Ext.isEmpty(condRecord.get('t5')))
		addr +=condRecord.get('t5')+"室";
		if(!Ext.isEmpty(condRecord.get('note')))
		addr +=condRecord.get('note');
		condRecord.set('address',addr);
	}
});

var AddressFormPanel = Ext.extend(Ext.form.FormPanel,{
	addr_id:null,
	addr_text:null,
	constructor:function(){
		AddressFormPanel.superclass.constructor.call(this,{
			border:false,
			labelWidth: 120,
			title:'批量变更小区',
			id:'AddressFormPanelId',
			height:350,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[	{xtype : 'hidden',id:'beforeAddrId',name : 'oldAddrId'},		
				{
					xtype:'textfield',fieldLabel:'变更前小区',name:'before_addr_text',id:'beforeAddrTextId',width:250,readOnly: true					
				},{
					xtype:'textfield',fieldLabel:'客户数',name:'num'	,id:'custNumId',width:100,readOnly: true
				},{layout:'form',border:false,items:[ new AddressTreeCombo({
						xtype : 'treecombo',
						fieldLabel:'变更后小区',
						width : 250,
						treeWidth : 400,
						minChars : 2,
						allowBlank:false,
						height : 22,
						emptyText : '两个字搜索地址',
						blankText : '客户地址',
						treeUrl: Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrTree.action",
						hiddenName:'newAddrId'
					})]
				}]
		});
	}
});
CustAddrWin = Ext.extend(Ext.Window,{
	addrgrid:null,
	constructor:function(data){
		this.addrgrid = new QueryCustGrid(data);
		CustAddrWin.superclass.constructor.call(this,{
			layout:'border',
			border:false,
			width:800,
			height:500,
			items:[{region : 'center',layout:'fit',items:[this.addrgrid]}],
			buttons : [{text : '保存',scope : this,iconCls : 'icon-save',handler : this.doSave
					}, {text : '关闭',scope : this,handler : function() {this.close();}}]
		})
	},doSave:function(){
		var grid = Ext.getCmp('queryCustAddressId');
			grid.stopEditing();// 停止编辑
			var records = [];
			var modifiedRec = grid.getStore().getModifiedRecords();
			if(modifiedRec.length == 0){Alert("没有修改任何数据!"); return false};
			for (var i = 0; i < modifiedRec.length; i++) {
				records.push(modifiedRec[i].data);
			}
			grid.getStore().commitChanges();//将Store中所有的Record的原始版本修改为当前版本
			Confirm("确定保存吗?", this, function () {
				mb = Show();
				Ext.Ajax.request({
							scope : this,
							url:Constant.ROOT_PATH + "/core/x/Cust!updateAddressList.action",
							params : {
								addrListStr : Ext.encode(records)
							},
							success : function(res, opt) {
								mb.hide();// 隐藏提示框
								mb = null;
								var rs = Ext.decode(res.responseText);
								if (true === rs.success) {
									grid.getStore().reload();
									Confirm("操作成功!【是】：继续操作，【否】：关闭所有窗口",this, this,function () {
										this.close();
									});
								} else {
									Alert('操作失败');
								}
							}
						})
			});	
	}

})

UpdateAddressWin = Ext.extend(Ext.Window,{
	grid:null,
	form:null,
	updateForm:null,
	addressPanel:null,
	constructor:function(){
		this.form = new QueryAddressForm(this);
		this.grid = new QueryCustGrid();
		this.updateForm = new AddressUpdateAllForm();
		
		UpdateAddressWin.superclass.constructor.call(this,{
			id:'UpdateAddressWinId',
			layout : 'border',
			title:'批量地址修改',
			closeAction : 'close',
			closable: true,
			border : false ,
			width:750,
			height:500,
			items:[
				{region : 'north',layout : 'fit',height:70,items:[this.form]},
				{region : 'center',layout:'fit',items:[this.grid]},
				{region : 'south',layout:'fit',height:45,items:[this.updateForm]}
			],
			buttons : [{text : '保存',scope : this,iconCls : 'icon-save',handler : this.doSave
					}, {text : '关闭',scope : this,handler : function() {this.close();}}]
		});
	},doSave:function(){
		var type =Ext.getCmp('typeId').getValue();
		if(type == 'PL'){
			var panel = Ext.getCmp('AddressFormPanelId');
			if(!panel.getForm().isValid())return ;
			var values = panel.getForm().getValues();
			if(values['num']==0){Alert("该地区没有客户!");return;}
			if(values['oldAddrId'] == values['newAddrId']){Alert("相同小区不能变更");return;}
			
			Confirm("确定保存吗?", this, function () {
				var mb = Show();
				Ext.Ajax.request({
					url:Constant.ROOT_PATH + "/core/x/Cust!updateCustAddress.action",
					params:values,
					scope:this,
					timeout:36000000,
					success : function(res, opt) {
						mb.hide();// 隐藏提示框
						mb = null;
						var rs = Ext.decode(res.responseText);
						if (true === rs.success) {
							if(rs.simpleObj!=null){
								this.close();
								Confirm("操作成功!有部分客户地址需要手动修改，【是】将手动修改，【否】关闭窗口", this, function () {
									var win = new CustAddrWin(rs.simpleObj);
									win.show();
								});
							}else{
								Alert('操作成功!');
							}
						}else{
							Alert('操作失败');
						}
					}
				});
			});
			
		}else if(type == 'MX'){
			if(!this.updateForm.getForm().isValid()) return;
			var obj = {}, records = [];;
			var updateValues = this.updateForm.getForm().getValues();
			if(updateValues['isSelectAll'] && updateValues['isSelectAll'] == 'T'){
				Ext.apply(obj, this.form.getForm().getValues());
				obj['new_addr_id'] = updateValues['new_addr_id'];
				obj['new_address'] = updateValues['new_address'];
				if(Ext.isEmpty(obj['new_addr_id']) && Ext.isEmpty(obj['new_address'])){
					Alert('请选择新小区或新地址');
					return;
				}
			}else{
				var grid = Ext.getCmp('queryCustAddressId');
				grid.stopEditing();// 停止编辑
				var modifiedRec = grid.getStore().getModifiedRecords();
				if(modifiedRec.length == 0){Alert("没有修改任何数据!"); return false};
				for (var i = 0; i < modifiedRec.length; i++) {
					records.push(modifiedRec[i].data);
				}
				grid.getStore().commitChanges();//将Store中所有的Record的原始版本修改为当前版本
			}
			if(records.length>0){
				obj['addrListStr'] = Ext.encode(records);
			}
			Confirm("确定保存吗?", this, function () {
				var mb = Show();
				Ext.Ajax.request({
					scope : this,
					url:Constant.ROOT_PATH + "/core/x/Cust!updateAddressList.action",
					params : obj,
					timeout:36000000,
					success : function(res, opt) {
						mb.hide();// 隐藏提示框
						mb = null;
						var rs = Ext.decode(res.responseText);
						if (true === rs.success) {
							Ext.getCmp('queryCustAddressId').getStore().removeAll();
							Confirm("操作成功!【是】：继续操作，【否】：关闭所有窗口",this, this,function () {
								this.close();
							});
						} else {
							Alert('操作失败');
						}
					}
				});
			});
		}
	}
});
