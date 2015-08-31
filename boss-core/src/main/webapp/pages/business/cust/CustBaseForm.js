
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





//UnitForm = Ext.extend(Ext.Panel, {
//	parent : null,
//	constructor: function(p){
//		this.parent = p;
//		UnitForm.superclass.constructor.call(this, {
//			layout:'column',
//			border : false,
//			anchor: '100%',
//			bodyStyle: "background:#F9F9F9",
//			defaults:{
//				baseCls: 'x-plain',
//				columnWidth:0.5,
//				labelWidth: 75
//			},
//			items:[{
//				layout:'form',border:false,items:[{
//					id:'cust_level_id',
//					fieldLabel:'客户群体',
//					xtype:'combo',
//					hiddenName:'cust.cust_level',
//					store:new Ext.data.JsonStore({
//						fields:['text','value'],
//						data:[{text:'协议客户',value:'XYKH'},{text:'模拟大客户',value:'MNDKH'}]
//					}),displayField:'text',valueField:'value',editable:true,forceSelection:true,
//					listeners : {
//						scope : this,
//						select : function(combo){
//							var value = combo.getValue();
//							this.parent.custLevel = value;
//							var custCountCmp = Ext.getCmp('custcount_itemId');
//							if(value){
//								custCountCmp.show();
//								Ext.getCmp('cust_count_id').allowBlank = false;
//							}else{
//								custCountCmp.hide();
//								Ext.getCmp('cust_count_id').allowBlank = true;
//							}
//					},
//					blur:function(combo){
//						var value = combo.getValue();
//						if(Ext.isEmpty(value)){
//							Ext.getCmp('custcount_itemId').hide();
//							Ext.getCmp('cust_count_id').allowBlank = true;
//						}
//					}
//				}
//			}]
//			},{
//				id:'custcount_itemId',
//				layout:'form',border:false,
//				items:[{
//					id:'cust_count_id',
//					fieldLabel:'容量',
//					xtype:'numberfield',
//					name:'cust.cust_count',
//					minValue:1,
//					allowDecimals:false,
//					allowNegative:false
//				}]
//			}]
//		})
//	},
//    initEvents: function () {
//        this.on('afterrender', function () {
//	        Ext.getCmp('custcount_itemId').hide();
//        });
//	    this.doLayout();
//        UnitForm.superclass.initEvents.call(this);
//    }
//});

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
	constructor: function(config){
		Ext.apply(this, config);
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
					columnWidth:0.95,
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
					columnWidth:0.25,
					items:[{
				    	width : 40,
				    	id : 'cust.note',
				    	name : 'cust.note',
				    	fieldLabel : 'Room',
				    	xtype : 'textfield',
				    	listeners:{
							scope: this,
							'change': this.doAddressChange
						}
				    }]
				},{
					labelWidth: 45,
					columnWidth:0.75,
					items:[new AddressTreeCombo({
						id : 'addrTreeCombo',
				    	width:280,
						treeWidth:350,
						treeHeight : 300,
						minChars:0,
						height: 22,
						fieldLabel : '地址',
						allowBlank: false,
						emptyText :'选择地址..',
						hideTrigger: false,
						editable: false,
						blankText:'请选择客户地址',
						treeUrl: Constant.ROOT_PATH+"/commons/x/QueryParam!queryAddrTree.action",
						hiddenName:'cust.addr_id',
						listeners:{
							scope: this,
							'select': function(combo){
								Ext.getCmp('tempCustAddress').setValue(combo.getRawValue());
								this.doAddressChange();
							}
						}
					})]
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
		CustBaseForm.superclass.doInit.call(this);
		this.removecustLevel();
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
	initComponent : function(){
		if(!this.oldCustType){
			this.oldCustType = 'RESIDENT';
		}
		CustBaseForm.superclass.initComponent.call(this);
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
			if(!Ext.isEmpty(Ext.getCmp('cust.note').getValue())){
				this.custAddress = "Room "+Ext.getCmp('cust.note').getValue()+ ","+this.custAddress;
			}
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