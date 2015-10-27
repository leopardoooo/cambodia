/**
 * Ext 构建产品页面
 */
cfgProdType = {
	"P" : "BASE",
	"U" : "SPKG",
	"C" : "CPKG",
	"ADDPROD" : "增加产品",
	"UPDATEPROD" : "修改产品",
	"ADDUPROD" : "增加用户套餐",
	"ADDCPROD" : "增加客户套餐"
};

// 产品树
ProdTree = Ext.extend(Ext.tree.TreePanel, {
	prodId : null,
	prodTreeStore : null,
	showAll : 'F',
	constructor : function() {
		var loader = new Ext.tree.TreeLoader({
					url : root + "/system/Prod!getProdTree.action",
					baseParams : {
						showAll : this.showAll
					}
				});
		var twoTbar = new Ext.Toolbar({
					items : [new Ext.form.TextField({
								width : 140,
								emptyText : '快速检索产品',
								enableKeyEvents : true,
								id : 'selectQuickId',
								listeners : {
									scope : this,
									render : function(f) {
										this.filter = new Ext.ux.TreeFilter(
												this, {
													clearAction : 'expand'
												});// 初始化TreeFilter
									},
									keyup : {// 添加键盘点击监听
										fn : function(t, e) {
											this.filter.filter(t.getValue());
										},
										buffer : 350
									}
								}
							}), ' ', ' ', {
						iconCls : 'icon-expand-all',
						tooltip : '展开所有资源',
						scope : this,
						handler : this.doExpandAll
					}, '-', {
						iconCls : 'icon-collapse-all',
						tooltip : '合并所有资源',
						scope : this,
						handler : this.doCollapseAll
					}]
				});
		var threeTbar = new Ext.Toolbar({
					items : [new Ext.form.DateField({
										id : 'prod_start_eff_date_id',
										format : 'Y-m-d',
										width : 80,
										emptyText : '生效日期'
									}), new Ext.form.DisplayField({
										value : '&nbsp;至&nbsp;'
									}), new Ext.form.DateField({
										id : 'prod_end_eff_date_id',
										format : 'Y-m-d',
										width : 80,
										emptyText : '生效日期'
									})]
				});
		var fourTbar = new Ext.Toolbar({
					items : [new Ext.form.DateField({
										id : 'prod_start_exp_date_id',
										format : 'Y-m-d',
										width : 80,
										emptyText : '失效日期'
									}), new Ext.form.DisplayField({
										value : '&nbsp;至&nbsp;'
									}), new Ext.form.DateField({
										id : 'prod_end_exp_date_id',
										format : 'Y-m-d',
										width : 80,
										emptyText : '失效日期'
									}), new Ext.Button({
										text : '查询',
										pressed : true,
										scope : this,
										handler : this.doDateQuery
									})]
				});
		ProdTree.superclass.constructor.call(this, {
					region : 'west',
					width : 220,
					split : true,
					minSize : 180,
					maxSize : 260,
					id : 'prodTreeId',
					margins : '0 0 3 2',
					autoScroll : true,
					animCollapse : true,
					animate : true,
					rootVisible : false,
					collapseMode : 'mini',
					bodyStyle : 'padding:3px',
					loader : loader,
					root : {
						id : '0',
						iconCls : 'x-tree-root-icon',
						nodeType : 'async',
						text : '产品'
					},
					tbar : [{
								tooltip : '一般产品,套餐产品',
								text : '添加',
								iconCls : 'icon-add-user',
								scope : this,
								id : 'addprodIdItems',
								handler : this.openProd
							}, '-', {
								text : '修改',
								iconCls : 'icon-edit-user',
								scope : this,
								id : 'updateprodIdItems',
								handler : this.updateProd
							}, '-', {
								text : '查看所有产品',
								scope : this,
								handler : this.queryProd,
								id : 'queryProdButton'
							}],
					listeners : {
						'render' : function() {
							twoTbar.render(this.tbar);
							threeTbar.render(this.tbar);
							fourTbar.render(this.tbar);
						}
					}
				});
		this.getRootNode().expand();
	},
	// 展开所有节点
	doExpandAll : function() {
		this.expandAll();
	},
	// 合并节点
	doCollapseAll : function() {
		this.collapseAll();
	},
	doDateQuery : function() {
		var startEffDate = Ext.getCmp('prod_start_eff_date_id');
		var endEffDate = Ext.getCmp('prod_end_eff_date_id');
		var startExpDate = Ext.getCmp('prod_start_exp_date_id');
		var endExpDate = Ext.getCmp('prod_end_exp_date_id');

		if (startEffDate.isValid() && endEffDate.isValid()
				&& startExpDate.isValid() && endExpDate.isValid()) {
			this.getLoader().baseParams["start_eff_date"] = startEffDate
					.getRawValue();
			this.getLoader().baseParams["end_eff_date"] = endEffDate
					.getRawValue();
			this.getLoader().baseParams["start_exp_date"] = startExpDate
					.getRawValue();
			this.getLoader().baseParams["end_exp_date"] = endExpDate
					.getRawValue();
			this.getLoader().load(this.root);
			this.getRootNode().expand();
		} else {
			Alert('日期格式不正确');
		}

	},
	initEvents : function() {
		this.on("click", function(node, e) {
			var id = node.id;
//			Ext.getCmp('addLowestPrice').hide();
			Ext.getCmp('addTariff').show();
			if (node.attributes.others.att == "prodId") {
				if (!this.prodId || this.prodId != id) {
					this.prodId = id;
					this.queryAll(id);
					// 省级，创建人，新建人上级部门均能修改
					if (App.data.optr['county_id'] == '4501'
							|| App.data.optr['old_county_id'] == '4501'
							|| node.attributes.others.optrId == App.data.optr['optr_id']
							|| App.data.optr['county_id'] == node.attributes.others.area_id) {
						Ext.getCmp('updateprodIdItems').setDisabled(false);
					} else {
						Ext.getCmp('updateprodIdItems').setDisabled(true);
					}
				}
			} else {
				Ext.getCmp('updateprodIdItems').setDisabled(true);

				if (node.attributes.others.att == "areaProdId") {
					if (!this.prodId || this.prodId != id) {
						this.prodId = id;
						this.queryAll(id);
					}
				}

			}
		}, this);
		ProdTree.superclass.initEvents.call(this);
	},
	queryAll : function(id) {
		Ext.Ajax.request({
			url : root + '/system/Prod!queryProdById.action',
			params: {
				doneId: id
			},
			scope : this,
			success : function(res, ops) {
				var rs = Ext.decode(res.responseText);
				this.prodTreeStore = rs.simpleObj;
				Ext.getCmp('tariffGrid').tprodId = this.prodTreeStore.prod_id;
				Ext.getCmp('tariffGrid').ispkg = this.prodTreeStore.prod_type;
				Ext.getCmp('tariffGrid').prodServId = this.prodTreeStore.serv_id;
				Ext.getCmp('tariffGrid').pkgStore = this.prodTreeStore.packList;
				Ext.getCmp('tariffGrid').forAreaId = App.data.optr['area_id'];
				if (!Ext.isEmpty(this.prodTreeStore.prodTariffList)) {
					Ext.getCmp('tariffGrid')
							.loadBasem(this.prodTreeStore.prodTariffList);
				} else {
					Ext.getCmp('tariffGrid').getStore().removeAll();
				}
				Ext.getCmp('prodForm').doint(this.prodTreeStore);

				/**
				 * 如果产品是省公司为操作员所在地区单独建立的产品，而且操作有相应权限（修改省公司产品，增加资费），就可以进行相应操作
				 */
				if (this.prodTreeStore.county_id == '4501'
						&& App.data.optr['county_id'] != '4501'
						&& this.prodTreeStore.countyList.length == 1
						&& this.prodTreeStore.countyList[0] == App.data.optr['county_id']) {
					var editProd = false;// 修改产品
					var addTariff = false;// 增加资费
					for (var i = 0; i < App.subMenu.length; i++) {
						if (App.subMenu[i]['handler'] == 'EditProd') {
							editProd = true;
						} else if (App.subMenu[i]['handler'] == 'AddTariff') {
							addTariff = true;
						}
					}

					if (editProd) {
						Ext.getCmp('updateprodIdItems').setDisabled(false);
					}
					if (addTariff) {
						Ext.getCmp('addTariff').show();
					}

				}
				// 套餐不使用最低定价功能
//				if (this.prodTreeStore.prod_type == cfgProdType["P"]
//						? true
//						: false) {
//					Ext.getCmp('addLowestPrice').show();
//				}

			}
		});
	},
	openProd : function() {
//		this.prodWindow = new ProdWindow(cfgProdType["P"],
//				cfgProdType["ADDPROD"], "icon-add");
//		this.prodWindow.show();
		this.loginWindow = new LoginWindow();
		this.loginWindow.show();
	},
	updateProd : function() {
		var data = Ext.getCmp('prodForm').prodAllStore;
		if (!data) {
			Alert("未选择产品!");
			return false;
		} else {
			this.updateWindow = new ProdWindow(data.prod_type,
					cfgProdType["UPDATEPROD"], "icon-add", data);
			this.updateWindow.show();
		}
	},
	queryProd : function() {
		var text = '查看所有产品';
		if (this.showAll == 'T') {
			this.showAll = 'F';
		} else {
			text = '查看可用产品';
			this.showAll = 'T';
		}
		Ext.getCmp('queryProdButton').setText(text);
		this.getLoader().baseParams.showAll = this.showAll;
		this.getLoader().load(this.root);
		this.getRootNode().expand();
		this.filter.filter("");
		Ext.getCmp('selectQuickId').setValue("");
	}
});
// 产品form
ProdListForm = Ext.extend(Ext.form.FormPanel, {
	resForm : null,
	prodList : null,
	userType : null,
	store : null,
	pordDictWindow : null,
	prodId : null,
	extAttrForm : null,
	updateProd : false,
	constructor : function(v, t, s) {
		Ext.apply(this, v || {});
		this.userType = v;

		if (s) {// 修改产品
			this.store = s;
			this.prodId = s.prod_id;
			this.doInitAttrForm({
						tabName : Constant.P_PROD,
						pkValue : s.prod_id
					});

			// 如果是修改产品,且已生效
			if (Date.parseDate(this.store["eff_date"], 'Y-m-d h:i:s')
					.format('Y-m-d') <= nowDate().format('Y-m-d')) {
				this.updateProd = true;
			}
		} else {// 增加产品
			this.doInitAttrForm({
						tabName : Constant.P_PROD
					});
		}

		ProdListForm.superclass.constructor.call(this, {
					region : 'center',
					title : "基础信息",
					id : 'prodFormList',
					layout : 'form',
					border : false,
					collapsible : true,
					items : [{
						border : false,
						anchor : '100%',
						layout : 'column',
						baseCls : 'x-plain',
						bodyStyle : 'padding-top: 5px',
						defaults : {
							baseCls : 'x-plain',
							columnWidth : 0.5,
							layout : 'form',
							defaultType : 'textfield',
							labelWidth : 90
						},
						items : [{
							items : [{
										fieldLabel : '产品名称',
										name : 'prod_name',
										id : 'prodNameId',
										width : 130,
										allowBlank : false,
										blankText : '你没填写产品名称'
									}, {
										fieldLabel : '服务类型',
										xtype : 'paramcombo',
										id : 'servId',
										width : 130,
										paramName : 'SERV_ID',
										allowBlank : false,
										hiddenName : 'serv_id',
										disabled : this.updateProd,
										listeners : {
											scope : this,
											'select' : function(combo, record,
													index) {
												this.selectRes(combo.getValue(),App.data.optr['area_id']);
//												this.treeCheckfalse();// 清空适用地区树
											}

										}
									}, {
										fieldLabel : '退款',
										xtype : 'paramcombo',
										width : 100,
										paramName : 'BOOLEAN',
										defaultValue : 'F',
										disabled : this.updateProd,
										hiddenName : 'refund'
									}, {
										xtype : 'datefield',
										editable : false,
										fieldLabel : '生效日期',
										width : 130,
										id : 'startdt',
										vtype : 'daterange',
										endDateField : 'enddt',
										name : 'eff_date',
										format : 'Y-m-d',
										minText : '不能选择当日之前',
										allowBlank : false,
										value : nowDate(),
										minValue : nowDate().format('Y-m-d')
									}, new PrintItemPanel({
												allowBlank : false
											})]
						}, {
							defaults : {
								width : 130
							},
							items : [ {
										fieldLabel : '基本产品',
										xtype : 'paramcombo',
										paramName : 'BOOLEAN',
										allowBlank : false,
										defaultValue : 'T',
										disabled : this.updateProd,
										hiddenName : 'is_base'
									}, {
										fieldLabel : '转账',
										xtype : 'paramcombo',
										width : 100,
										paramName : 'BOOLEAN',
										defaultValue : 'F',
										disabled : this.updateProd,
										hiddenName : 'trans'
									}, {
										fieldLabel : '绑定基本产品',
										allowBlank : false,
										xtype : 'paramcombo',
										paramName : 'BOOLEAN',
										defaultValue : 'T',
										hiddenName : 'is_bind_base'
									}, {
										xtype : 'datefield',
										editable : false,
										vtype : 'daterange',
										id : 'enddt',
										startDateField : 'startdt',
										fieldLabel : '失效日期',
										name : 'exp_date',
										format : 'Y-m-d',
										minValue : nowDate().format('Y-m-d')
									}, {
										fieldLabel : '一次性产品',
										allowBlank : false,
										xtype : 'paramcombo',
										paramName : 'BOOLEAN',
										defaultValue : 'F',
										hiddenName : 'just_for_once',
										disabled : this.updateProd,
										listeners : {
											'select' : function(combo) {
												Ext
														.getCmp('prodFormList')
														.addProdInvalidDate(combo
																.getValue());
											}
										}
									}, {
										fieldLabel : '银行扣费',
										xtype : 'paramcombo',
										width : 100,
										paramName : 'BOOLEAN',
										defaultValue : 'F',
//										disabled : this.updateProd,
										hiddenName : 'is_bank_pay'
									}]
						}, {
							items : []
						}, {
							xtype : 'hidden',
							name : 'prod_id'
						}, 
						{
							columnWidth : 1,
							items:[{
								fieldLabel : '产品描述',
								name : 'prod_desc',
								xtype:'textarea',
								height:40,
								width : 400
							}]
						}]
					},
					this.extAttrForm]
				})
	},
	initEvents : function() {
		this.on('afterrender', function() {
					Ext.getCmp('prodNameId').focus(true, 500);
				});
		ProdListForm.superclass.initEvents.call(this);
	},
	initComponent : function() {
		ProdListForm.superclass.initComponent.call(this);
		// 初始化下拉框的参数
		var comboes = this.findByType("paramcombo");
		App.form.initComboData(comboes, this.addRes, this);
	},
	doInitAttrForm : function(params) {
		var cfg = [{
					columnWidth : .5,
					layout : 'form',
					baseCls : 'x-plain',
					labelWidth : 90
				}, {
					columnWidth : .5,
					layout : 'form',
					baseCls : 'x-plain',
					labelWidth : 90
				}];
		// 扩展信息面板
		this.extAttrForm = ExtAttrFactory.gExtForm(params, cfg);
	},
	addRes : function() {
		if (this.userType != cfgProdType["P"]) {
			Ext.getCmp('servId').setDisabled(true);
		}else{
			if (!this.store) {
				Ext.getCmp('servId').setDisabled(false);
			}
		}
		
		if (this.store) {
			this.selectRes(this.store.serv_id, App.data.optr['area_id']);
			if (this.store.just_for_once == "T") {
				this.addProdInvalidDate(this.store.just_for_once);
			}
			this.updateLoad(this.store);
		}
	},
	selectRes : function(servId, areaId) {
		if (Ext.isEmpty(areaId)) {
			Alert("应用地区未选择");
			return false;
		} else if (this.userType == cfgProdType["P"] && Ext.isEmpty(servId)) {
			Alert("服务类型未选择");
			return false;
		}
//		if (this.userType != cfgProdType["P"]) {
//			var grid = Ext.getCmp('prodPkgForm');
//			grid.uprodStroe.removeAll();
////			 grid.upackStore.removeAll();
//		}

		if (!Ext.isEmpty(areaId)) {
			Ext.Ajax.request({
						url : root + "/system/Prod!queryResByServId.action",
						scope : this,
						params : {
							ServId : servId,
							areaId : areaId,
							doneId : this.prodId,
							query : this.userType
						},
						success : function(response, opt) {
							var res = Ext.decode(response.responseText);
							if (this.userType == cfgProdType["P"]) {
								if (Ext.getCmp('resForm'))
									Ext.getCmp('resForm').showData(
													res.simpleObj.dynamicResList,
													res.simpleObj.staticResList,
													areaId);
							} else {
								// 用户客户套餐加载数据，解除适用地区阴影
								if (Ext.getCmp('prodPkgForm'))
									Ext.getCmp('prodPkgForm')
											.showData(this.prodId);
							}
						}
					});
		}
	},
	treeCheckfalse : function() {
		var nodes = Ext.getCmp("ProdCountyTree").getChecked();
		if (nodes && nodes.length) {
			for (var i = 0; i < nodes.length; i++) {
				// 设置UI状态为未选中状态
				nodes[i].getUI().toggleCheck(false);
				// 设置节点属性为未选中状态
				nodes[i].attributes.checked = false;
			}
		}
	},
	addProdInvalidDate : function(v) {
		this.items.itemAt(0).items.itemAt(2).removeAll();

		if (v == 'T') {
			this.items.itemAt(0).items.itemAt(2).add({
						xtype : 'datefield',
						width : 130,
						fieldLabel : '截止日期',
						name : 'invalid_date',
						allowBlank : false,
						format : 'Y-m-d',
						id : 'prodstartdate',
						minValue : nowDate().format('Y-m-d')
					})

			this.doLayout();
		}
	},
	updateLoad : function(data) {
		var prodInfo = {};
		for (var prop in data) {
			prodInfo[prop] = data[prop];
		}
		prodInfo['eff_date'] = Date.parseDate(prodInfo["eff_date"],
				'Y-m-d h:i:s');
		prodInfo['exp_date'] = Date.parseDate(prodInfo["exp_date"],
				'Y-m-d h:i:s');
		prodInfo['invalid_date'] = Date.parseDate(prodInfo["invalid_date"],
				'Y-m-d h:i:s');
		if (!Ext.isEmpty(prodInfo['eff_date'])) {
			if (prodInfo['eff_date'].format('Y-m-d') <= nowDate()
					.format('Y-m-d')) {
				Ext.getCmp('startdt').setDisabled(true);
			}
		} else {
			prodInfo['eff_date'] = nowDate().format('Y-m-d');
		}
		this.getForm().loadRecord(new Ext.data.Record(prodInfo));
	}
});

/**
 * 产品应用地市树
 * 
 * @class ProdCountyTree
 * @extends Ext.tree.TreePanel
 */
ProdCountyTree = Ext.extend(Ext.tree.TreePanel, {
			prodId : null,
			prodType : null,
			checkchange : false,
			threadId : null,
			constructor : function(prodId, type) {
				this.prodId = prodId;
				this.prodType = type;
				var loader = new Ext.tree.TreeLoader({
							url : root
									+ "/system/Prod!getProdCountyTree.action?query="
									+ prodId
						});
				ProdCountyTree.superclass.constructor.call(this, {
							id : 'ProdCountyTree',
							split : true,
							title : '应用地市',
							region : 'east',
							width : '30%',
							margins : '0 0 3 2',
							lines : false,
							autoScroll : true,
							animCollapse : true,
							animate : false,
							collapseMode : 'mini',
							bodyStyle : 'padding:3px',
							loader : loader,
							root : {
								id : '-1',
								iconCls : 'x-tree-root-icon',
								checked : false,
								text : '地市结构'
							}
						});
				this.expandAll();
			},
			initComponent : function() {
				// 每隔一秒，判断是否需要过滤子产品
//				this.threadId = setInterval(function() {
//							if (Ext.getCmp('ProdCountyTree')
//									&& Ext.getCmp('ProdCountyTree').checkchange) {
//								if (Ext.getCmp('prodPkgForm')) {
//									Ext.getCmp('prodPkgForm').doFilter(Ext
//											.getCmp('ProdCountyTree')
//											.getCheckedIds());
//								}
//								Ext.getCmp('ProdCountyTree').checkchange = false;
//							}
//						}, 1000);
				ProdCountyTree.superclass.initComponent.call(this);
			},
			initEvents : function() {
				this.on("afterrender", function() {
							var node = this.getRootNode();
							node.attributes.checked = true;
							node.ui.toggleCheck(true);
							node.fireEvent('checkchange', node, true);
//							// 如果不是修改
//							if (!this.prodId) {
//								// 如果是基本包或者分公司操作员，
//								if (this.prodType == cfgProdType["P"]
//										|| App.data.optr['county_id'] != '4501') {
//									var node = this.getRootNode();
//									node.attributes.checked = true;
//									node.ui.toggleCheck(true);
//									node.fireEvent('checkchange', node, true);
//								}
//							}else{
//								 //如果是修改，且不是基本包
//								 if(this.prodType != cfgProdType["P"] ){
//								 	this.disable();
//								 }
//							}

						}, this, {
							delay : 1000
						});

				this.on('checkchange', function(node, checked) {
							this.checkchange = true;
							node.expand();
							node.attributes.checked = checked;
							node.eachChild(function(child) {
										child.ui.toggleCheck(checked);
										child.attributes.checked = checked;
										child.fireEvent('checkchange', child,
												checked);
									});

						}, this);

				ProdCountyTree.superclass.initEvents.call(this);
			},
			getCheckedIds : function() {
				var prodCountyIds = [];
				var nodes = this.getChecked();
				for (var i in nodes) {
					if (nodes[i].leaf) {
						prodCountyIds.push(nodes[i].id);
					}
				}
				return prodCountyIds;
			}
		});

// 产品弹出窗口
ProdWindow = Ext.extend(Ext.Window, {
	prodListForm : null,
	resProdForm : null,
	prodCountyTree : null,
	prodList : null,
	formvalue : null,
	FORM : null,
	updateProd : false,
	constructor : function(v, t, c, store) {

		var prodId = null;
		if (store) {
			prodId = store.prod_id;
		}

		this.prodListForm = new ProdListForm(v, t, store);
		this.prodCountyTree = new ProdCountyTree(prodId, v);

		this.resProdForm = new ResProdForm(prodId);
		// 如果是修改产品,且已生效
//		if (store
//				&& Date.parseDate(store["eff_date"], 'Y-m-d h:i:s')
//						.format('Y-m-d') <= nowDate().format('Y-m-d')) {
//			this.updateProd = true;
//			var title = '';
//			if (v == cfgProdType["P"]) {
//				this.FORM = this.resProdForm;
//				title = '资源配置';
//			} else {
//				title = '产品已生效，不能修改子产品';
//				this.FORM = {
//					xtype : 'panel',
//					title : '提示',
//					region : 'center',
//					bodyStyle : Constant.SET_STYLE,
//					layout : 'fit',
//					items : [{
//						bodyStyle : "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
//						html : "<font style='font-family:微软雅黑;font-size:20'>"
//								+ title + "</font>"
//					}]
//				}
//			}
//
//		} else {
			if (v == cfgProdType["P"]) {
				this.FORM = this.resProdForm;
			} else {
				this.prodList = new ProdList(prodId);
				this.FORM = this.prodList;
			}
//		}
		this.formvalue = v;

		// 如果是新建产品
		if (!store) {
			Ext.Ajax.request({
				scope : this,
				url : root + '/config/Template!queryConfigByConfigName.action',
				params : {
					configName : 'PROD_DEFAULT_EFF_DATE'
				},
				success : function(res, opt) {
					var rs = Ext.decode(res.responseText);
					// 如果配置了生效天数
					if (rs) {
						var date = nowDate(), effDate = parseInt(rs.config_value);
						date = date.add(Date.DAY, effDate);
						Ext.getCmp('startdt').setValue(date);
					}

				}
			})
		}

		// 如果不是省公司操作员,但是有修改生效日期权限
		if (App.data.optr['county_id'] != '4501') {
			var editEffDate = false;
			for (var i = 0; i < App.subMenu.length; i++) {
				if (App.subMenu[i]['handler'] == 'EditEffDate') {
					editEffDate = true;
				}
			}

			Ext.getCmp('startdt').setReadOnly(!editEffDate);
		}

		ProdWindow.superclass.constructor.call(this, {
					title : t,
					iconCls : c,
					layout : 'border',
					width : 820,
					height : 600,
					closeAction : 'close',
					border : false,
					items : [{
								region : 'north',
								height : 250,
								layout : 'border',
								items : [this.prodListForm, this.prodCountyTree]
							}, this.FORM],
					buttons : [{
								text : '保存',
								scope : this,
								handler : this.doSave

							}, {
								text : '关闭',
								scope : this,
								handler : this.close
							}]
				});
	},
	initEvents : function() {
		this.on('close', function() {
					window.clearInterval(this.prodCountyTree.threadId);
				}, this)
		ProdWindow.superclass.initEvents.call(this);
	},
	doSave : function() {
		var old = this.prodListForm.getForm().getValues(), all = {}, prodItemId = [];

		var prodCountyIds = [];
		if (this.prodCountyTree) {
			var nodes = this.prodCountyTree.getChecked();
			for (var i in nodes) {
				if (nodes[i].leaf) {
					prodCountyIds.push(nodes[i].id);
				}
			}
		}

		if (prodCountyIds.length == 0) {
			Alert('请选择适用地区');
			return;
		}

		// 产品适用地区
		all['prodCountyIds'] = prodCountyIds;

		if (this.updateProd && this.formvalue != cfgProdType["P"]) {
			if (Ext.getCmp('prodDictTreeId')) {
				var nodes = Ext.getCmp('prodDictTreeId').getChecked();
				for (var i in nodes) {
					if (nodes[i].leaf) {
						prodItemId.push(nodes[i].id);
					}
				}
			}
			if (prodItemId.length > 0) {
				all["dictProdIds"] = prodItemId;
			}

			for (var key in old) {
				all["prodDto." + key] = old[key];
			}

			all["prodDto.prod_type"] = this.formvalue;

			mb = Show();// 显示正在提交
			Ext.Ajax.request({
						url : root + '/system/Prod!editProd.action',
						params : all,
						scope: this,
						success : function(res, ops) {
							mb.hide();// 隐藏提示框
							mb = null;
							var rs = Ext.decode(res.responseText);
							if (true === rs.success) {
								var tree = Ext.getCmp('prodTreeId');
								tree.getLoader().load(tree.root);
								tree.root.expand();
								Alert('操作成功!', function() {
											Ext
													.getCmp('prodTreeId')
													.queryAll(rs.simpleObj.prod_id);
											Ext.getCmp('updateprodIdItems')
													.setDisabled(false);
											Ext.getCmp('addTariff').show();
											this.close();
										}, this);
							} else {
								Alert('操作失败!');
							}
						}
					});
		} else {
			if (Ext.getCmp('dynResGrid')) {
				Ext.getCmp('dynResGrid').stopEditing();
			}
			if (Ext.getCmp('prodDictTreeId')) {
				var nodes = Ext.getCmp('prodDictTreeId').getChecked();
				for (var i in nodes) {
					if (nodes[i].leaf) {
						prodItemId.push(nodes[i].id);
					}
				}
			}
			if (prodItemId.length > 0) {
				all["dictProdIds"] = prodItemId;
			}
			if (!this.prodListForm.getForm().isValid()) {
				return;
			}
			for (var key in old) {
				all["prodDto." + key] = old[key];
			}
			all["prodDto.prod_type"] = this.formvalue;

			if (this.formvalue == cfgProdType["P"]) {
				if (Ext.getCmp('countyResId')) {
					var resarr = Ext.getCmp('countyResId').getCountyRes();
					if (resarr.length > 0) {
						all['staticResList'] = Ext.encode(resarr);
					}
				}
				if (Ext.getCmp('dynResGrid')) {
					var dynstoreselect = Ext.getCmp('dynResGrid').getStore();
					var dyndata = [];
					num = 0;
					if (dynstoreselect.getCount() > 0) {
						for (var i = 0; i < dynstoreselect.getCount(); i++) {
							dyndata.push(dynstoreselect.getAt(i).data);
							num += dynstoreselect.getAt(i).data['res_number'];
						}
					}
					if (num > 0) {
						var dynrecords = Ext.encode(dyndata);
						all["dynamicResList"] = dynrecords;
					}
				}

				if (Ext.isEmpty(all['staticResList'])
						&& Ext.isEmpty(all["dynamicResList"])) {
					// Alert("未配置任何资源!");
					// return false;
				}
				mb = Show();// 显示正在提交
				Ext.Ajax.request({
					url : root + '/system/Prod!save.action',
					params : all,
					scope: this,
					success : function(res, ops) {
						mb.hide();// 隐藏提示框
						mb = null;
						var rs = Ext.decode(res.responseText);
						if (true === rs.success) {
							var tree = Ext.getCmp('prodTreeId');
							tree.getLoader().load(tree.root);
							tree.root.expand();
							Alert('操作成功!', function() {
										Ext.getCmp('prodTreeId')
												.queryAll(rs.simpleObj.prod_id);
										Ext.getCmp('updateprodIdItems')
												.setDisabled(false);
										Ext.getCmp('addTariff').show();
										this.close();
									}, this);
						} else {
							Alert('操作失败!');
						}
					}
				});
			} else {
				var packdata = this.prodList.getValues();
				if(packdata.length == 0){
					Alert(lsys('msgBox.tipPleaseEditWell'));
					return;
				}
				var packrecords = Ext.encode(packdata);
				all["packList"] = packrecords;
				mb = Show();// 显示正在提交
				Ext.Ajax.request({
					url : root + '/system/Prod!savePack.action',
					params : all,
					scope: this,
					success : function(res, ops) {
						mb.hide();// 隐藏提示框
						mb = null;
						var rs = Ext.decode(res.responseText);
						if (true === rs.success) {
							var tree = Ext.getCmp('prodTreeId');
							tree.getLoader().load(tree.root);
							tree.root.expand();
							Alert('操作成功!', function() {
										Ext.getCmp('prodTreeId')
												.queryAll(rs.simpleObj.prod_id);
										Ext.getCmp('updateprodIdItems')
												.setDisabled(false);
										Ext.getCmp('addTariff').show();
										this.close();
									}, this);
						} else {
							Alert('操作失败!');
						}
					}
				});
			}

		}
	}
});

// 产品弹出窗口
LoginWindow = Ext.extend(Ext.Window, {
	constructor : function() {
		LoginThist = this;
		LoginWindow.superclass.constructor.call(this, {
			title : '增加产品',
			width : 180,
			height : 240,
			closeAction : 'close',
			border : false,
			resizable : false,
			bodyStyle : Constant.FORM_STYLE,
			defaults : {
				width : 100,
				height : 60,
				iconAlign : 'center'
			},
			items : [{
				xtype : 'button',
				scale : 'up',
				text : "<FONT style='font-family:隶书;FONT-SIZE: 34; FILTER: Glow(color=#ffccbb,strength=2); COLOR: #4433aa; HEIGHT: 8pt'>基本产品</font>",
				scope : this,
				handler : this.ppordLogin
			}, {
				xtype : 'button',
				scale : 'up',
				text : "<FONT style='font-family:隶书;FONT-SIZE: 34; FILTER: Glow(color=#ffccbb,strength=2); COLOR: #4433aa; HEIGHT: 8pt'>套餐产品</font>",
				scope : this,
				handler : this.cpordLogin
			}, {
				xtype : 'button',
				scale : 'up',
				text : "<FONT style='font-family:隶书;FONT-SIZE: 34; FILTER: Glow(color=#ffccbb,strength=2); COLOR: #4433aa; HEIGHT: 8pt'>协议套餐</font>",
				scope : this,
				handler : this.upordLogin
			}]
		});
	},
	ppordLogin : function() {
		this.prodWindow = new ProdWindow(cfgProdType["P"],
				cfgProdType["ADDPROD"], "icon-add");
		this.prodWindow.show();
		LoginThist.close();
	},
	cpordLogin : function() {
		this.prodWindow = new ProdWindow(cfgProdType["C"],
				cfgProdType["ADDCPROD"], "icon-add");
		this.prodWindow.show();
		LoginThist.close();
	},
	upordLogin : function() {
		this.prodWindow = new ProdWindow(cfgProdType["U"],
				cfgProdType["ADDUPROD"], "icon-add");
		this.prodWindow.show();
		LoginThist.close();
	}
})
// 产品视图
ProdView = Ext.extend(Ext.Panel, {
			prodPanel : null,
			prodTree : null,
			constructor : function() {
				this.prodPanel = new ProdDetalisForm();
				this.prodTree = new ProdTree();
				ProdView.superclass.constructor.call(this, {
							id : 'ProdView',
							layout : 'border',
							closable : true,
							title : '产品增加修改',
							border : false,
							baseCls : "x-plain",
							items : [this.prodTree, {
										region : 'center',
										layout : 'fit',
										items : [this.prodPanel]
									}]
						})
			}
		})

function ImageDragZone(view, config) {
	this.view = view;
	if (!Ext.isEmpty(view)) {
		ImageDragZone.superclass.constructor.call(this, view.getEl(), config);
	}
};
Ext.extend(ImageDragZone, Ext.dd.DragZone, {
			getDragData : function(e) {
				var target = e.getTarget('.viewres');
				if (target) {
					var view = this.view;
					if (!view.isSelected(target)) {
						view.onClick(e);
					}
					var selNodes = view.getSelectedNodes();
					var dragData = {
						nodes : selNodes
					};
					if (selNodes.length == 1) {
						dragData.ddel = target;
						dragData.multi = true;
					} else {
						var div = document.createElement('div');
						div.className = 'multi-proxy';
						for (var i = 0, len = selNodes.length; i < len; i++) {
							div.appendChild(selNodes[i].firstChild.firstChild
									.cloneNode(true)); // image nodes only
							if ((i + 1) % 3 == 0) {
								div.appendChild(document.createElement('br'));
							}
						}
						var count = document.createElement('div');
						count.innerHTML = i + ' images selected';
						div.appendChild(count);

						dragData.ddel = div;
						dragData.multi = true;
					}
					return dragData;
				}
				return false;
			},
			getRepairXY : function(e) {
				if (!this.dragData.multi) {
					var xy = Ext.Element.fly(this.dragData.ddel).getXY();
					xy[0] += 3;
					xy[1] += 3;
					return xy;
				}
				return false;
			}
		});