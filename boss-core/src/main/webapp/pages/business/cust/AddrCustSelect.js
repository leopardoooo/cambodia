/**
 * 客户开户地址选择框
 */
AddrQueryFilterTree = Ext.extend(Ext.ux.QueryFilterTreePanel,{
	treeParams:null,
	parent:null,
	constructor: function(parent){
		this.parent = parent;
		this.treeParams = {editId:this.parent.editNodeId}
		AddrQueryFilterTree.superclass.constructor.call(this, {
			root : new Ext.tree.AsyncTreeNode({expanded : true}),
			loader : new Ext.tree.TreeLoader({
						dataUrl : Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrTree.action",
						baseParams : this.treeParams,
						listeners : {
							beforeload : this.onBeforeLoad,
							scope : this
						}
					}),
			floating : true,
			autoScroll : true,
			region: 'west',
			animate : false,
			rootVisible : false,
			width 	: 180,
			split	: true,
			minSize	: 180,
	        maxSize	: 260,
	        margins		:'0 0 3 2',  //元素上右下左和外面元素的距离都是0
	        lines		:false,  // 去掉树的线
	        animCollapse:true,
	        collapseMode:'mini',
			bodyStyle	:'padding:3px',
			listeners : {
				scope : this,
				expandnode: function(node) { //节点展开时高亮显示
					var childNodes = node.childNodes;
					var that = this;
					if(childNodes && childNodes.length > 0){
						Ext.each(childNodes, function(node){
							if (node.id == that.parent.editNodeId) {
								node.getUI().getTextEl().style.color = "red";
								that.getSelectionModel().select(node);
							}
						});
					};
				}

			}
		})
	},	
	doSearchEditId:function(v){
		this.doLoadTreeData(null,v);
	},
	doSearch:function(){
		var _v = this.searchT.getValue();
		this.doLoadTreeData(_v,null);				
	},
	doLoadTreeData:function(n,v){
		this.parent.doGridrest();
		var params = {};
		if(!Ext.isEmpty(n)){
			params['comboQueryText'] = n;
		}
		if(!Ext.isEmpty(v)){
			params['editId'] = v;
		}
		Ext.Ajax.request({
			url: Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrTree.action",
			scope : this,
			params : params,
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
	},
	onBeforeLoad:function(l,node){
		if(!node.isRoot){
  			l.baseParams.addrId=node.id; //通过这个传递参数，这样就可以点一个节点出来它的子节点来实现异步加载
		}
	},
	// 检查当前的节点是否能够被点击
	isCanClick : function(node) {
		if (this.onlySelectLeaf) {
			if (!node.leaf || node.attributes['is_leaf'] == 'F')
				return false;
		}
		return true;
	}
});

AddrCustSelectWin = Ext.extend( Ext.Window , {
	//客户信息的store
	custStore: null,
	custGrid: null,
	addrTree:null,
	addrName:null,
	nodeId:null,
	pageSize:15,
	roomPanel:null,
	editNodeId:null,
	constructor: function (addrId){
		if(addrId == 9){//意向客户 不需要查
			addrId = null;
		}
		this.editNodeId = addrId;
		this.addrTree = new AddrQueryFilterTree(this);
		this.custStore = new Ext.data.JsonStore({
			url: Constant.ROOT_PATH+"/commons/x/QueryParam!queryNoteCust.action",
			root: 'records',
			totalProperty: 'totalProperty',
			fields: Ext.data.Record.create([
				"note_status_type",
				"cust_no",
				"cust_name",
				"note",
				"note_status_type_text"
			])
		});
		
		var cm = [
			{ header: lbc("common.optr"), width: 50,renderer: function(v , md, record , i  ){
				return "<DIV><a href='#' onclick=Ext.getCmp('addrCustSelectWinId').doGridCheckRoom()>"+ lbc("common.confirm") +"</a></DIV>";
			}},
			{header: lmain("cust._form.roomNumber"), dataIndex: 'note', width: 120},
			{header: lmain("cust._form.roomStatus"), dataIndex: 'note_status_type_text',width: 80},
			{header: lmain("cust.base.name"), dataIndex: 'cust_name',width: 80},
			{header: lmain("cust.base.busiId"), dataIndex: 'cust_no',width: 80}
		    
		];
		
		//实例化cust grid panel
		this.custGrid = new Ext.grid.GridPanel({
			stripeRows: true, 
			title: lmain("cust._form.roomTitle"),
	        store: this.custStore,
	        columns: cm,
	        region : "center" ,
	        bbar: new Ext.PagingToolbar({
	        	pageSize: this.pageSize,
				store: this.custStore
			}),
			listeners: {
				scope: this,
				rowdblclick: function(grid ,index, e){
					var record = grid.getStore().getAt(index);
					this.setData(record.get('note'));
					this.close();
				},
				rowclick: function(grid ,index, e){
					var record = grid.getStore().getAt(index);
					this.setData(record.get('note'));
				}
			}
	    });
	    
	    this.roomPanel = new Ext.Panel({
	    	layout:'column',
			anchor: '100%',
			region : "south" ,
			height:120,
			border : false,
			defaults:{
				baseCls: 'x-plain'
			},
			items:[{
				columnWidth:1,
				labelWidth: 1,
				layout: 'form',
				items:[{
	                xtype: 'displayfield',
	                autoWidth:true,
	                autoHeight:true,
	                id:'feeDescId'
				}]
			},{
				columnWidth:1,
				items:[{
			    	height:50,
			    	border : false,
			     	fullscreen: true,
		            layout : {
						type:'hbox',
						padding : '5',
						pack:'center',
						align : 'top'
					},
					defaults : {
						height: '100%',
						margins : '0 5 0 0',
						height:30
					},
		            items:[
		                {xtype:'button',text:lmain("cust._form.addRoom"),id:'addRoomNewBoxId',iconCls:'icon-add',disabled:true,scope:this,handler:this.doCheckedChangeRoom},
		                {xtype:'textfield',width:150 ,id:'newRoomBoxId',disabled:true,enableKeyEvents:true,listeners:{
							scope:this,
							keyup:this.doNewRoom
						}},
		                {xtype:'button',text:lbc("common.submit"),iconCls:'icon-save',id:'saveRoomNewBoxId',disabled:true,scope:this,handler:this.doSaveNewRoom}
		            ]
		       	}]
			
			}]
	    })
	    
		AddrCustSelectWin.superclass.constructor.call(this,{
			title: lmain("cust._form.addrManager"),
			id:'addrCustSelectWinId',
			border : false ,
			maximizable:true,
			width: 700,
			height: 510,
			layout : 'border',
			items: [this.addrTree,{
				layout : 'border',
				region : "center" ,
				border : false ,
				items:[this.custGrid,this.roomPanel]
			}]
		});
		
		this.addrTree.on("click",function(node, e) {
			if (!this.addrTree.isCanClick(node)) {
				return;
			}
			this.doGridrest();
			this.doLoadRoom(node.id);
		}, this);
	},
	doGridrest:function(){
		this.custStore.removeAll();
	},
	doNewRoom:function(comp){
		var name = "";
		if(!Ext.isEmpty(comp.getValue())){
			var name = "Room "+comp.getValue()+",";
		}
		name = name + this.addrName;
		Ext.get('newAddressId').update(name);
	},
	doSaveNewRoom:function(){
		var note = Ext.getCmp('newRoomBoxId').getValue();
		if(Ext.isEmpty(note)){
			Alert("房间号不能为空");
			return;
		}
		this.setData(note);
		this.close();
	},
	doGridCheckRoom:function(){
		var rec = this.custGrid.getSelectionModel().getSelected();
		this.setData(rec.get('note'));
		this.close();
	},
	doCheckedChangeRoom:function(){
		Ext.getCmp('newRoomBoxId').setDisabled(false);
		Ext.getCmp('saveRoomNewBoxId').setDisabled(false);
	},
	//注册事件
	/*initEvents: function(){
		this.addrTree.on("click",function(node, e) {
			if (!this.isCanClick(node)) {
				return;
			}
			this.doGridrest();
			this.doLoadRoom(node.id);
		}, this);
		AddrCustSelectWin.superclass.initEvents.call(this);
	},*/
	doLoadRoom:function(addrId){
		Ext.getCmp('addRoomNewBoxId').setDisabled(false);
			Ext.getCmp('newRoomBoxId').setDisabled(true);
			Ext.getCmp('saveRoomNewBoxId').setDisabled(true);
			Ext.getCmp('custAddrId').setValue(addrId);
			Ext.get('newAddressId').update('');
			this.nodeId = addrId;
			this.custStore.baseParams = {addrId:addrId};
		
			this.custStore.load({
				params: { start:0, limit: this.pageSize}
			});
			
			this.custGrid.setTitle(lmain("cust._form.roomTitle"));
			Ext.Ajax.request({
				scope : this,
				url: Constant.ROOT_PATH+"/commons/x/QueryParam!queryCustAddrName.action",
				params : {
					addrId : this.nodeId
				},
				success : function(res,opt){
					var rec = Ext.decode(res.responseText);
					// 显示隐藏值
					this.addrName = rec.addrName;
					this.custGrid.setTitle(lmain("cust._form.roomTitle2", null, [rec.districtName, 
					                                                                 Ext.isEmpty(rec.netType)?'':rec.netType]));
				}
			});
	
	},
	setData:function(note){
		var name = "";
		if(!Ext.isEmpty(note)){
			var name = "Room "+note+",";
		}
		name = name + this.addrName;
		Ext.get('newAddressId').update(name);
		Ext.getCmp('custNoteId').setValue(note);
		Ext.getCmp('tempCustAddress').setValue(name);
		if(Ext.getCmp('linkman.mail_address')){
			Ext.getCmp('linkman.mail_address').setValue(name);
		}
	},
	show:function(){
		var oldAddr, custAddress = Ext.getCmp('tempCustAddress').getValue();
		if(custAddress){
			oldAddr = "<font style='font-size:12px'><b>old:</b>"+custAddress+"</font><br>";
		}
		Ext.getCmp('feeDescId').setValue((oldAddr==null?"":oldAddr)+
			"<font style='font-size:12px'><b>new:</b></font><font style='color:red'><span id='newAddressId'>--</span></font>");
		AddrCustSelectWin.superclass.show.call(this);
		if(!Ext.isEmpty(this.editNodeId)){
			this.doLoadRoom(this.editNodeId);
		}
	}
});