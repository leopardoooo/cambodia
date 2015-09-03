
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
				this.addOption(node.id, rec);
				this.setValue(node.id);
				this.setRawValue(rec);
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

AddrCustSelectWin = Ext.extend( Ext.Window , {
	//客户信息的store
	custStore: null,
	custGrid: null,
	parent: null,
	constructor: function (parent){
		this.custStore = new Ext.data.JsonStore({
			url: root + '/commons/x/QueryCust!searchCust.action',
			root: 'records',
			totalProperty: 'totalProperty',
			fields: Ext.data.Record.create([
				"cust_id",
				"cust_no",
				"cust_name",
				"addr_id_text",
				"address",
				"status",
				"cust_type",
				"status_text",
				"cust_type_text",
				"cust_level_text",
				"net_type_text",
				"is_black_text",
				"open_time",
				"county_id",
				"county_name",
				"area_id"
			])
		});
		this.custStore.on('load',this.doLoadResult,this);
		this.parent = parent;
		var cm = [
			{header: '受理编号', dataIndex: 'cust_no'},
			{header: '客户名称', dataIndex: 'cust_name'},
			{header: '客户地址', dataIndex: 'addr_id_text', width: 240,
				renderer: function(value,md,record){
					value = record.get('address');
					return value;
				}},
			{header: '网络类型', dataIndex: 'net_type_text',width: 80},
			{header: '客户状态', dataIndex: 'status_text',width: 80},
			{header: '客户类型', dataIndex: 'cust_type_text',width: 80},
			{header: '客户级别', dataIndex: 'cust_level_text',width: 80},
			{header: '黑名单', dataIndex: 'is_black_text',width: 60},
			{id: 'autoCol', header: '开户时间', dataIndex: 'open_time'}
		];
		
		//实例化cust grid panel
		this.custGrid = new Ext.grid.GridPanel({
			stripeRows: true, 
	        store: this.custStore,
	        columns: cm,
	        region : "center" ,
	        bbar: new Ext.PagingToolbar({
	        	pageSize: 10,
				store: this.custStore
			})
	    })
		AddrCustSelectWin.superclass.constructor.call(this,{
			title:'地址管理',
			border : false ,
			closable: true,
			layout : 'border',
			baseCls: "x-plain",
			items: [this.custGrid]
		});
	},
	//注册事件
	initEvents: function(){
		this.custGrid.on("rowclick", function(grid ,index, e){
//			Confirm("确定要选择该客户吗?", this ,function(){
			var record = grid.getStore().getAt(index);
			Ext.getCmp('taskCustInfoPanel').remoteRefresh(record.get('cust_id'));
			this.close();
//			});
		}, this);
		AddrCustSelectWin.superclass.initEvents.call(this);
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
						defaultValue:'SFZ',
						listeners:{
							scope:this,
							'select': function(){
								var field = Ext.getCmp("linkman_cert_num_el");
								var value = field.getValue();
								if(value && this.doCertNumChange(field, value, value) === false){
									field.reset();
								}
							}
						}
					},{
						fieldLabel:'固定电话',
						name:'linkman.tel',
						xtype:'textfield',
						regex: /^(\d{7}|\d{8})$/,
						id: 'linkmanTel',
						invalidText : '请输入7位或8位电话号码'
					},{
						fieldLabel:'邮箱',
						name:'linkman.email',
						xtype:'textfield',
						vtype:'email'
					},{
						fieldLabel:'邮编',
						name:'linkman.postcode',
						xtype:'textfield',
						regex: /^[1-9]{1}[0-9]{5}$/,
						invalidText : '请输入6位不以0开头的数字'
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
						id: 'linkman_cert_num_el',
						listeners:{
							scope:this,
							'change':this.doCertNumChange
						}
					},{
						fieldLabel:'手机',
						name:'linkman.mobile',
						xtype:'numberfield',
						regex: /^1[\d]{10}$/,
						id: 'linkmanMobile',
						invalidText : '请输入11位数字'
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
//	unitForm :null,
	linkPanel : null,
	oldCustType:'RESIDENT',
	custAddress : null,//客户拼接后的地址
	custLevel : null,
	navMenu:null,
	provinceStore:null,
	constructor: function(config){
		Ext.apply(this, config);
		
		this.provinceStore = new Ext.data.JsonStore({
 				url: root + '/commons/x/QueryCust!queryProvince.action',
 				fields : ['name','cust_code']
 		});
		//居民信息扩展
		this.doInitAttrForm(2);
		this.linkPanel = new LinkPanel(this);
//		this.unitForm = new UnitForm(this);
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
					columnWidth:0.6,
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
					columnWidth:0.4,
					items:[{
						xtype: 'checkbox',
					    labelWidth: 120,
					    fieldLabel: "意向客户",
					    id: "isCanToCustId",
//					    checked: true,
					    listeners:{
			            	scope: this,
			            	check: this.doCheckedChange
			            }
					}]
				},{
					columnWidth:0.4,
					items:[{
						fieldLabel : '所在省',
						xtype:'combo',
						id : 'provinceId',
						forceSelection : true,
						store : this.provinceStore,
						triggerAction : 'all',
						mode: 'local',
						displayField : 'name',
						valueField : 'cust_code',
						emptyText: '请选择',
	//					allowBlank : false,
						editable : false
	//					,listeners:{
	//						scope: this,
	//						'select': this.doBuyModeSelect
	//					}
					}]
				},{
					columnWidth:0.75,
					items:[{
						fieldLabel:'地址',
						width:300,
						xtype:'textfield',
						id : 'custAddrId',
						name:'cust.addr_id',
						allowBlank:false,
						disabled:true
					}]
				},{
					columnWidth:0.25,
					items:[{
						id:'clickAddrId',
						xtype : 'button',
						text: '&nbsp;选择',
						scope: this,
						width: 80,
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
			}
			,{
				xtype : 'hidden',
				id : 'tempCustAddress'
			},this.linkPanel,this.extAttrForm]
//            }]
		});
	},
	doInit:function(){
		if(!this.oldCustType){
			this.oldCustType = 'RESIDENT';
		}
		CustBaseForm.superclass.doInit.call(this);
		this.provinceStore.load();
		this.removecustLevel();
	},
	doClickAddr:function(btn){
		alert(1);
	},
	doCheckedChange:function(box, checked){
		if(checked){
			Ext.getCmp('custAddrId').setDisabled(false);
			Ext.getCmp('clickAddrId').setDisabled(true);
			
		}else{
			Ext.getCmp('custAddrId').setDisabled(true);
			Ext.getCmp('clickAddrId').setDisabled(false);
		}
	},
	removecustLevel:function(){
		var store = this.findById('cust_level_id').getStore();
		store.each(function(record){
			if(record.get('item_value') != 'YBKH'){
				store.remove(record);
			}
		});
		
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
//		this.remove(this.unitForm,true);
		
		
//		var custToUserBtn = Ext.getCmp('newCustToUserId');
//		var custToDeviceBtn = Ext.getCmp('newCustToDeviceId');
//		if (custType != 'UNIT') {
//			if (custToUserBtn && custToUserBtn.isHidden)
//				custToUserBtn.hide();
//			if (custToDeviceBtn && custToDeviceBtn.isHidden)
//				custToDeviceBtn.hide();
//		} else {
//			if (custToUserBtn && !custToUserBtn.isHidden)
//				custToUserBtn.hide();
//			if (custToDeviceBtn && !custToDeviceBtn.isHidden)
//				custToDeviceBtn.hide();
//		}
		
		this.linkPanel = new LinkPanel();
		if (custType == 'RESIDENT'){
			this.doInitAttrForm(2);
			this.add(this.linkPanel);
			this.add(this.extAttrForm);
		}else{
			this.doInitAttrForm(1);
			var countyId = App.getData().optr.county_id;
			//单位，选择了客户群体，则为模拟大客户(武汉、直属)
//			if(custType == 'UNIT' && (countyId == '0101' || countyId == '0102') ){
//				this.unitForm = new UnitForm(this);
//				this.add(this.unitForm);
//			}
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
	doAddressChange : function(){
		if(!Ext.isEmpty(Ext.getCmp('addrTreeCombo').getRawValue())){
			this.custAddress = Ext.getCmp('tempCustAddress').getValue();			
			Ext.getCmp('linkman.mail_address').setValue(this.custAddress);
		}
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