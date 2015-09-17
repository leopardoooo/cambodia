QueryFilterTree = Ext.extend(Ext.ux.QueryFilterTreePanel,{
	treeParams:null,
	parent:null,
	constructor: function(parent){
		this.parent = parent;
		this.treeParams = {editId:this.parent.editNodeId}
		QueryFilterTree.superclass.constructor.call(this, {
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
		addrThat = this;
		if(addrId == 9){//意向客户 不需要查
			addrId = null;
		}
		this.editNodeId = addrId;
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
			{ header: lbc("common.optr"), width: 50,renderer: function(v , md, record , i  ){
				return "<DIV><a href='#' onclick='addrThat.doGridCheckRoom();'>"+ lbc("common.confirm") +"</a></DIV>";
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
			addrThat.doGridrest();
			addrThat.doLoadRoom(node.id);
		})
		
		AddrCustSelectWin.superclass.initEvents.call(this);
	},
	doLoadRoom:function(addrId){
		Ext.getCmp('addRoomNewBoxId').setDisabled(false);
			Ext.getCmp('newRoomBoxId').setDisabled(true);
			Ext.getCmp('saveRoomNewBoxId').setDisabled(true);
			Ext.getCmp('custAddrId').setValue(addrId);
			Ext.get('newAddressId').update('');
			addrThat.nodeId = addrId;
			addrThat.custStore.baseParams = {addrId:addrId};
		
			addrThat.custStore.load({
				params: { start:0, limit: addrThat.pageSize}
			});
			
			addrThat.custGrid.setTitle(lmain("cust._form.roomTitle"));
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
					addrThat.custGrid.setTitle(lmain("cust._form.roomTitle2", null, [rec.districtName, 
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
		Ext.getCmp('linkman.mail_address').setValue(name);
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


/**
 * 联系人面板
 * @class LinkPanel
 * @extends Ext.Panel
 */
LinkPanel = Ext.extend(Ext.Panel,{
	parent : null,
	constructor : function(p){
		this.parent = p;
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
						fieldLabel: langUtils.main("cust.base.linkMan"),
						name:'linkman.linkman_name',
						xtype:'textfield'
					},{
						fieldLabel: langUtils.main("cust.base.certType"),
						xtype:'paramcombo',
						allowBlank:false,
						hiddenName:'linkman.cert_type',
						paramName:'CERT_TYPE',
						defaultValue:'SFZ'
					},{
						fieldLabel: langUtils.main("cust.base.tel"),
						name:'linkman.tel',
						xtype:'textfield',
						id: 'linkmanTel'
					},{
						fieldLabel: langUtils.main("cust.base.email"),
						name:'linkman.email',
						xtype:'textfield',
						vtype:'email'
					},{
						fieldLabel:langUtils.main("cust.base.postcode"),
						name:'linkman.postcode',
						xtype:'textfield'
					}]
				},{
					items:[{
						fieldLabel:langUtils.main("cust.base.sex"),
						id : 'linkmanSex',
						hiddenName:'linkman.sex',
						xtype:'paramcombo',
						paramName:'SEX'
					},{
						fieldLabel: langUtils.main("cust.base.certNum"),
						xtype:'textfield',
						vtype: 'alphanum',
						width : 130,
						allowBlank:false,
						name:'linkman.cert_num',
						id: 'linkman_cert_num_el',
						listeners: {
							scope: this,
							change: this.parent.generatePwd
						}
					},{
						fieldLabel: langUtils.main("cust.base.mobile"),
						name:'linkman.mobile',
						xtype:'numberfield',						
						id: 'linkmanMobile'
					},{
						fieldLabel: langUtils.main("cust.base.barthday"),
						width : 125,
						id : 'linkmanBirthday',
						name:'linkman.birthday',
						xtype:'datefield',
						format:'Y-m-d'
					}]
				},{
					columnWidth:1,
					items:[{
						fieldLabel:langUtils.main("cust.base.postalAddr"),
						width : 400,
						id : 'linkman.mail_address',
						name:'linkman.mail_address',
						xtype:'textfield'
					}]
					
				}]
			},{
				fieldLabel: langUtils.bc("common.remark"),
				name:'cust.remark',
				preventScrollbars : true,
				height : 30,
				width : 350,
				xtype:'textarea'
			}]
		})
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
						fieldLabel: langUtils.main("cust.base.type"),
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
						fieldLabel: langUtils.main("cust.base.name"),
						xtype:'textfield',
						id : 'cust.cust_name',
						name:'cust.cust_name',
						allowBlank:false,
						listeners:{
							scope: this,
							'change': this.doCustNameChange
						}
					}]	
				},{
					items:[{
							xtype: 'checkbox',
						    fieldLabel: langUtils.main("cust._form.thinkCust"),
						    id: "isCanToCustId",
						    listeners:{
				            	scope: this,
				            	check: this.doCheckedChange
				            }
						},{
							xtype:'combo',
							id : 'provinceId',
							fieldLabel: "省",
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
						}
					]
				},{
					columnWidth:0.80,
					items:[{
						width:300,
						fieldLabel: langUtils.main("cust.base.addr"),						
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
						text: langUtils.bc("common.switchor"),
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
						fieldLabel: langUtils.main("cust.base.cust_level"),
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
						fieldLabel: langUtils.main("cust.base.developName"),
						xtype:'paramcombo',
						hiddenName: 'cust.str9',
						paramName:'OPTR',
						editable:true,
						allowBlank:false
					}]
				},{
					id:'addCustItemsTwo',
					items:[{
							id: 'cust_pwd_id',
							fieldLabel: langUtils.bc("common.pswd"),
							vtype : 'loginName',
							xtype:'textfield',
							name:'cust.password'
						},{
							fieldLabel: langUtils.main("cust.base.languageType"),
							xtype: 'paramcombo',
							paramName: 'LANGUAGE_TYPE',
							allowBlank: false,
							hiddenName:'cust.str6'
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
			win = new AddrCustSelectWin(Ext.getCmp('custAddrId').getValue());
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
					fieldLabel: langUtils.main("cust.base.businessLicence"),
					xtype:'textfield',
					name:'cust.str7',
					allowBlank:false
				});
			Ext.getCmp('addCustItemsTwo').add({
					id:'cust_str8_id',
					fieldLabel: langUtils.main("cust.base.unitNumber"),
					xtype:'textfield',
					name:'cust.str8'
				});
			Ext.getCmp('addCustItemsTwo').add({
					id:'cust_spkg_sn_id',
					fieldLabel:langUtils.main("cust.base.spkgSn"),
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
			obj['msg'] = lmsg("phoneOrFixedPhoneMustBeEnterOne");
			return obj;
		}
		return CustBaseForm.superclass.doValid.call(this);
	},
	generatePwd: function(field, custCertNum){
		//如果没有输入密码，则根据证件号生成
		//证件号后六位数字，如果数字不足6位就前补零补满六位，无证件号默认为'000000'
		var pwdComp = Ext.getCmp('cust_pwd_id');
		//新建开户时生成，修改客户不改变
		if(pwdComp && App.getData().currentResource.busicode == '1001'){
			var pwd = '000000';
			if(custCertNum){
					custCertNum = custCertNum.substring(custCertNum.length-6, custCertNum.length);
					var ss = '';
					for(var i=0,len=custCertNum.length;i<len;i++){
						var s = custCertNum[i];
						if(/[0-9]/.test(s)){
							ss+=s;
						}
					}
					pwd = String.leftPad(ss, 6, '0');
			}
			pwdComp.setValue(pwd);
		}
		
	}
});