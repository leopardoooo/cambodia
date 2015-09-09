
QueryFilterTree = Ext.extend(Ext.ux.QueryFilterTreePanel,{
	
	constructor: function(parent){
		QueryFilterTree.superclass.constructor.call(this, {
			root : new Ext.tree.AsyncTreeNode({expanded : true}),
			loader : new Ext.tree.TreeLoader({
						dataUrl : Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrTree.action",
//						baseParams : this.treeParams,
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
//				click : this.onNodeClick,
				scope : this
			}
		})
	},
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
				this.addOption(node.id, rec);
				this.setValue(node.id);
				this.setRawValue(rec);
				this.collapse();
				this.fireEvent('select', this, node, node.attributes);
			}
		});
	},
	onBeforeLoad:function(l,node){
		l.on('beforeload',function(loader,node){
  			l.baseParams.addrId=node.id; //通过这个传递参数，这样就可以点一个节点出来它的子节点来实现异步加载
		},l);
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
	constructor: function (){
		addrThat = this;
		this.addrTree = new QueryFilterTree(this);
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
			{header: '房间号', dataIndex: 'note', width: 160},
			{header: '房间状态', dataIndex: 'note_status_type_text',width: 80},
			{header: '客户名称', dataIndex: 'cust_name'},
			{header: '受理编号', dataIndex: 'cust_no'},
		    { header: '操作', width: 50,renderer: function(v , md, record , i  ){
				return "<DIV><a href='#' onclick='addrThat.doGridCheckRoom();'>确认</a></DIV>";
			}}
		];
		
		//实例化cust grid panel
		this.custGrid = new Ext.grid.GridPanel({
			stripeRows: true, 
			title:'房间信息',
	        store: this.custStore,
	        columns: cm,
	        region : "center" ,
	        bbar: new Ext.PagingToolbar({
	        	pageSize: this.pageSize,
				store: this.custStore
			})
	    })
	    
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
	                anchor:"95%",
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
		                {xtype:'button',text:'新增房间',id:'addRoomNewBoxId',iconCls:'icon-add',disabled:true,scope:this,handler:this.doCheckedChangeRoom},
		                {xtype:'textfield',width:150 ,id:'newRoomBoxId',disabled:true,enableKeyEvents:true,listeners:{
							scope:this,
							keyup:this.doNewRoom
						}},
		                {xtype:'button',text:'提交',iconCls:'icon-save',id:'saveRoomNewBoxId',disabled:true,scope:this,handler:this.doSaveNewRoom}
		            ]
		       	}]
			
			}]
	    })
	    
		AddrCustSelectWin.superclass.constructor.call(this,{
			title:'地址管理',
			id:'addrCustSelectWinId',
			border : false ,
			maximizable:true,
			width: 250,
			height: 300,
			layout : 'border',
			items: [this.addrTree,{
				layout : 'border',
				region : "center" ,
				border : false ,
				items:[this.custGrid,this.roomPanel]
			}]
		});
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
	initEvents: function(){
		this.custGrid.on("rowdblclick", function(grid ,index, e){
			var record = grid.getStore().getAt(index);
			this.setData(record.get('note'));
			this.close();
		}, this);
		this.custGrid.on("rowclick", function(grid ,index, e){
			var record = grid.getStore().getAt(index);
			this.setData(record.get('note'));
		}, this)
		this.addrTree.on("click",function(node, e) {
			if (!this.isCanClick(node)) {
				return;
			}
			Ext.getCmp('addRoomNewBoxId').setDisabled(false);
			Ext.getCmp('newRoomBoxId').setDisabled(true);
			Ext.getCmp('saveRoomNewBoxId').setDisabled(true);
			Ext.getCmp('custAddrId').setValue(node.id);
			addrThat.nodeId = node.id;
			addrThat.custStore.baseParams = {addrId:node.id};
		
			addrThat.custStore.load({
				params: { start:0, limit: addrThat.pageSize}
			});
			
			addrThat.custGrid.setTitle("房间信息");
			Ext.Ajax.request({
				scope : this,
				url: Constant.ROOT_PATH+"/commons/x/QueryParam!queryCustAddrName.action",
				params : {
					addrId : addrThat.nodeId
				},
				success : function(res,opt){
					var rec = Ext.decode(res.responseText);
					// 显示隐藏值
					addrThat.addrName = rec.addrName;
					var title = "行政区域:"+rec.districtName+";  "
					title = title + "服务类型:"+(Ext.isEmpty(rec.netType)?'':rec.netType);
					addrThat.custGrid.setTitle(title);
					Ext.get('newAddressId').update(rec.addrName);
				}
			});
			
		})
		
		AddrCustSelectWin.superclass.initEvents.call(this);
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
		Ext.getCmp('linkman.mail_address').setValue(name);
	},
	show:function(){
		var oldAddr, custAddress = Ext.getCmp('tempCustAddress').getValue();
		if(custAddress){
			oldAddr = "<font style='font-size:12px'><b>old:</b>"+custAddress+"</font><br>";
		}
		Ext.getCmp('feeDescId').setValue( oldAddr==null?"":oldAddr+
			"<font style='font-size:12px'><b>new:</b></font><font style='color:red'><span id='newAddressId'>--</span></font>");
		AddrCustSelectWin.superclass.show.call(this);
	}
});


/**
 * 联系人面板
 * @class LinkPanel
 * @extends Ext.Panel
 */
LinkPanel = Ext.extend(Ext.Panel,{
	parent : null,
	constructor : function(){
		LinkPanel.superclass.constructor.call(this,{
			layout : 'form',
			anchor: '100%',
			border : false,
			baseCls: 'x-plain',
			items : [{
				layout:'column',
				baseCls: 'x-plain',
				anchor: '100%',
				defaults:{
					baseCls: 'x-plain',
					columnWidth:0.5,
					layout: 'form',
					labelWidth: 75
				},
				items: [{
					items:[{
						fieldLabel:'联系人',
						name:'linkman.linkman_name',
						xtype:'textfield'
					},{
						fieldLabel:'证件类型',
						xtype:'paramcombo',
						allowBlank:false,
						hiddenName:'linkman.cert_type',
						paramName:'CERT_TYPE',
						defaultValue:'SFZ'
					},{
						fieldLabel:'固定电话',
						name:'linkman.tel',
						xtype:'textfield',
						id: 'linkmanTel'
					},{
						fieldLabel:'邮箱',
						name:'linkman.email',
						xtype:'textfield',
						vtype:'email'
					},{
						fieldLabel:'邮编',
						name:'linkman.postcode',
						xtype:'textfield'
					}]
				},{
					items:[{
						fieldLabel:'性别',
						id : 'linkmanSex',
						hiddenName:'linkman.sex',
						xtype:'paramcombo',
						paramName:'SEX'
					},{
						fieldLabel:'证件号码',
						xtype:'textfield',
						vtype: 'alphanum',
						width : 130,
						allowBlank:false,
						name:'linkman.cert_num',
						id: 'linkman_cert_num_el'
					},{
						fieldLabel:'手机',
						name:'linkman.mobile',
						xtype:'numberfield',						
						id: 'linkmanMobile'
					},{
						fieldLabel:'出生日期',
						width : 125,
						id : 'linkmanBirthday',
						name:'linkman.birthday',
						xtype:'datefield',
						format:'Y-m-d'
					}]
				},{
					columnWidth:1,
					items:[{
						fieldLabel:'邮寄地址',
						width : 400,
						id : 'linkman.mail_address',
						name:'linkman.mail_address',
						xtype:'textfield'
					}]
					
				}]
			},{
				fieldLabel:'备注',
				name:'cust.remark',
//						grow : true,
				preventScrollbars : true,
				height : 30,
				width : 350,
				xtype:'textarea'
			}]
		})
	},
	/**
	 * 证件号码改变后，判断如果证件类型是身份证，则号码必须为15或者18位
	 */
	doCertNumChange:function(txt,value,oldValue){
		var certType = this.find('hiddenName','linkman.cert_type')[0].getValue();
		if (certType =="SFZ"){
			if (!Ext.form.VTypes.IDNum(value)){
				if(value.length != 18 && value.length != 15){
					txt.setValue(oldValue);
				}
				Alert(Ext.form.VTypes.IDNumText);
				return false
			}else{
				if(value.length == 18){//18位身份证
					//设置生日
					var dateStr = value.substring(6,14);
					var year = dateStr.substring(0,4);
					var month = dateStr.substring(4,6);
					var day = dateStr.substring(6,8);
					var date = Date.parseDate(year+"-"+month+"-"+day,'Y-m-d');
					Ext.getCmp('linkmanBirthday').setValue(date);
					
					//设置性别
					var num = value.substring(value.length -2 ,value.length-1);
					var index = 0;
					var sex = Ext.getCmp('linkmanSex');
					if(num%2 == 0){
						index = sex.getStore().find('item_value','FEMALE');
					}else{
						index = sex.getStore().find('item_value','MALE');
					}
					sex.setValue(sex.getStore().getAt(index).get('item_value'))
				}else{//15位身份证
					//设置生日
					var dateStr = value.substring(6,12);
					var year = '19' + dateStr.substring(0,2);
					var month = dateStr.substring(2,4);
					var day = dateStr.substring(4,6);
					var date = Date.parseDate(year+"-"+month+"-"+day,'Y-m-d');
					Ext.getCmp('linkmanBirthday').setValue(date);
					
					//设置性别
					var num = value.substring(value.length -1 ,value.length);
					var index = 0;
					var sex = Ext.getCmp('linkmanSex');
					if(num%2 == 0){
						index = sex.getStore().find('item_value','FEMALE');
					}else{
						index = sex.getStore().find('item_value','MALE');
					}
					sex.setValue(sex.getStore().getAt(index).get('item_value'))
				}
				
				
				
			}
		}
		
		return true;
	}
});
/**
 * 客户的Formg构造,这是一个基类，
 * 为客户开户、修改资料等功能提供的基类
 */
CustBaseForm = Ext.extend( BaseForm , {
	//扩展属性面板
	extAttrForm: null,
	linkPanel : null,
	oldCustType:'RESIDENT',
	custLevel : null,
	navMenu:null,
	provinceStore:null,
	custCode:null,
	constructor: function(config){
		Ext.apply(this, config);
		this.provinceStore = new Ext.data.JsonStore({
 				url: root + '/commons/x/QueryCust!queryProvince.action',
 				fields : ['name','cust_code']
 		});
		//居民信息扩展
		this.doInitAttrForm(2);
		this.linkPanel = new LinkPanel(this);
		CustBaseForm.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			labelWidth: 75,
			bodyStyle: Constant.TAB_STYLE,
			items:[{
				fieldLabel:'客户类型',
				xtype:'paramcombo',
				allowBlank:false,
				width : 125,
				hiddenName:'cust.cust_type',
				paramName:'CUST_TYPE',
				defaultValue:'RESIDENT',
				listeners:{
					scope: this,
					'select': function(combo,rec){
						this.doCustTypeChange(combo.getValue());
					}
				}
			},{
				layout:'column',
				baseCls: 'x-plain',
				anchor: '100%',
				defaults:{
					baseCls: 'x-plain',
					columnWidth:0.5,
					layout: 'form',
					labelWidth: 75
				},
				items: [{
					columnWidth:0.5,
					items:[{
						fieldLabel:'客户名称',
						xtype:'textfield',
						id : 'cust.cust_name',
						name:'cust.cust_name',
						allowBlank:false,
						blankText:'请输入客户名称',
						listeners:{
							scope: this,
							'change': this.doCustNameChange
						}
					}]
				},{
					columnWidth:0.2,
					items:[{
						xtype: 'checkbox',
					    labelWidth: 80,
					    fieldLabel: "意向客户",
					    id: "isCanToCustId",
					    listeners:{
			            	scope: this,
			            	check: this.doCheckedChange
			            }
					}]
				},{
					columnWidth:0.3,
					labelWidth: 1,
					items:[{
						xtype:'combo',
						id : 'provinceId',
						forceSelection : true,
						store : this.provinceStore,
						triggerAction : 'all',
						mode: 'local',
						displayField : 'name',
						valueField : 'cust_code',
						emptyText: '请选择省',
						disabled:true,
						editable : false,
						listeners:{
							scope:this,
							select:function(combo){
								this.custCode = combo.getValue();
							}
						}
					}]
				},{
					columnWidth:0.85,
					items:[{
						fieldLabel:'地址',
						width:350,
						xtype:'textfield',
						id : 'tempCustAddress',
						name:'cust.address',
						allowBlank:false,
						disabled:true,
						listeners:{
							scope:this,
							change:this.doAddressChange
						}
					}]
				},{
					columnWidth:0.15,
					items:[{
						id:'clickAddrId',
						xtype : 'button',
						text: '&nbsp;选择',
						scope: this,
						width: 60,
						height : 18,
						iconCls:'icon-tree',
						handler: this.doClickAddr
					}]
				},{
					id:'addCustItemsOne',
					items:[{
						id:'cust_level_id',
						fieldLabel:'客户等级',
						xtype:'paramcombo',
						allowBlank:false,
						hiddenName:'cust.cust_level',
						paramName:'CUST_LEVEL',
						defaultValue:'YBKH',
						listeners : {
							scope : this,
							select : function(combo){
								this.custLevel = combo.getValue();
							}
						}
					},{
						fieldLabel: '发展人',
						xtype:'paramcombo',
						hiddenName: 'cust.str9',
						paramName:'OPTR',
						editable:true
					}]
				},{
					id:'addCustItemsTwo',
					items:[{
							fieldLabel:'密码',
							allowBlank:false,
							vtype : 'loginName',
							xtype:'textfield',
							name:'cust.password'
						}]
				}]
			},{
				xtype : 'hidden',
				name:'cust.note',
				id : 'custNoteId'
			}
			,{
				xtype : 'hidden',
				id : 'custAddrId',
				name:'cust.addr_id'
			},{
				xtype : 'hidden',
				id : 'custStatusId',
				name:'cust.status'
			},this.linkPanel,this.extAttrForm]
		});
	},
	doClickAddr:function(btn){
		var win = Ext.getCmp('addrCustSelectWinId');
		if(!win){
			win = new AddrCustSelectWin();
		}
		win.show();
		win.maximize();
	},
	doCheckedChange:function(box, checked){
		Ext.getCmp('tempCustAddress').setValue('');
		Ext.getCmp('linkman.mail_address').setValue('');
		Ext.getCmp('provinceId').setValue('');
		this.custCode = null;
		if(checked){
			//意向客户，addrId 指定为9
			Ext.getCmp('custAddrId').setValue(9);
			Ext.getCmp('provinceId').setDisabled(false);
			Ext.getCmp('tempCustAddress').setDisabled(false);
			Ext.getCmp('clickAddrId').setDisabled(true);
			Ext.getCmp('custStatusId').setValue('PREOPEN');
		}else{
			Ext.getCmp('tempCustAddress').setDisabled(true);
			Ext.getCmp('provinceId').setDisabled(true);
			Ext.getCmp('clickAddrId').setDisabled(false);
		}
	},
	removeCustLevel:function(){
		var store = this.findById('cust_level_id').getStore();
		store.each(function(record){
			if(record.get('item_value') != 'YBKH'){
				store.remove(record);
			}
		});		
	},
	removeCustType:function(){
		var comp = this.find("hiddenName" ,'cust.cust_type')[0];
		var typeStore = comp.getStore();
		typeStore.each(function(record){
			if(record.get('item_value') == 'UNIT'){
				typeStore.remove(record);
			}
		});
	},
	doCustTypeChange:function(custType){//客户类型修改
		if (this.oldCustType == custType)
			return ;
		this.remove(this.linkPanel,true);
		this.remove(this.extAttrForm,true);
		
		this.linkPanel = new LinkPanel();
		if (custType == 'RESIDENT'){
			this.doInitAttrForm(2);
			this.add(this.linkPanel);
			this.add(this.extAttrForm);
		}else{
			this.doInitAttrForm(1);
			var countyId = App.getData().optr.county_id;
			
			this.add(this.linkPanel);
			this.add(this.extAttrForm);
		}
		
		//非居民客户，需要营业执照,税号,协议编号
		if(custType == 'NONRES'){
			Ext.getCmp('addCustItemsOne').add({
					id:'cust_str7_id',
					fieldLabel:'营业执照',
					xtype:'textfield',
					name:'cust.str7',
					allowBlank:false
				});
			Ext.getCmp('addCustItemsTwo').add({
					id:'cust_str8_id',
					fieldLabel:'税号',
					xtype:'textfield',
					name:'cust.str8'
				});
			Ext.getCmp('addCustItemsTwo').add({
					id:'cust_spkg_sn_id',
					fieldLabel:'协议编号',
					xtype:'textfield',
					name:'cust.spkg_sn'
				})
			
		}else{
			var comp = Ext.getCmp('cust_str7_id');
			if(comp){
				Ext.getCmp('addCustItemsOne').remove(comp,true);
				Ext.getCmp('addCustItemsTwo').remove(Ext.getCmp('cust_str8_id'),true);
				Ext.getCmp('addCustItemsTwo').remove(Ext.getCmp('cust_spkg_sn_id'),true);
			}
		}
		App.form.initComboData( this.linkPanel.findByType("paramcombo"));
		this.doLayout();
		this.oldCustType = custType;
	},
	//默认为新增的扩展信息
	doInitAttrForm: function(group){
		var cfg = [{
	   	   columnWidth: .5,
	   	   layout: 'form',
	   	   baseCls: 'x-plain',
	   	   labelWidth: 75
	    },{
		   columnWidth: .5,
		   layout: 'form',
		   baseCls: 'x-plain',
		   labelWidth: 75
	    }];
		this.extAttrForm = ExtAttrFactory.gExtForm({
			tabName: Constant.C_CUST,
			group: group,
			prefixName : 'cust.'
		}, cfg,this);
	},
	/**
	 * 客户名称修改后，如果名称小于5位，设置联系人名称同客户名相同
	 */
	doCustNameChange:function(c,r,i){
		var name = c.getValue();
		var linkManName = this.find('name','linkman.linkman_name')[0];
		if(this.oldCustType = 'RESIDENT'){
			linkManName.setValue(name);
		}
	},
	doAddressChange : function(obj){
		Ext.getCmp('linkman.mail_address').setValue(obj.getValue());
	},
	doValid:function(){
		if(!Ext.getCmp('linkmanTel').getValue() && !Ext.getCmp('linkmanMobile').getValue()){
			var obj = {};
			obj['isValid'] = false;
			obj['msg'] = '电话号码或固定电话必须输入一个！';
			return obj;
		}
		return CustBaseForm.superclass.doValid.call(this);
	}
});