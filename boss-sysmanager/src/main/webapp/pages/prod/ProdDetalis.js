var custInfoHTML = '<table width="100%" border="0" cellpadding="0" cellspacing="0">'
		+ '<tr height=24>'
		+ '<td class="label" width=15%>产品名称:</td>'
		+ '<td class="input_bold" width=30%>&nbsp;{[values.prod_name || ""]}</td>'
		+ '<td class="label" width=20%>产品类型:</td>'
		+ '<td class="input_bold" width=35%>&nbsp;{[values.prod_type_text || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label">服务类型:</td>'
		+ '<td class="input_bold">&nbsp;{[values.serv_id_text || ""]}</td>'
		+ '<td class="label" >基本节目:</td>'
		+ '<td class="input_bold">&nbsp;{[values.is_base_text || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label">可以转账:</td>'
		+ '<td class="input_bold">&nbsp;{[values.trans_text || ""]}</td>'
		+ '<td class="label">可以退款:</td>'
		+ '<td class="input_bold">&nbsp;{[values.refund_text || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >绑定基本节目:</td>'
		+ '<td class="input_bold">&nbsp;{[values.is_bind_base_text || ""]}</td>'
		+ '<td class="label">打印名称:</td>'
		+ '<td class="input_bold">&nbsp;{[values.printitem_name || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label">生效日期:</td>'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.eff_date) || ""]}</td>'
		+ '<td class="label">失效日期:</td>'
		+ '<tpl if="values.is_exp == \'T\'">'
		+ '<td class="input_red">&nbsp;{[fm.dateFormat(values.exp_date) || ""]}</td>'
		+ '</tpl>' 
		+ '<tpl if="values.is_exp == \'F\'">'
		+ '<td class="input_bold">&nbsp;{[fm.dateFormat(values.exp_date) || ""]}</td>'
		+ '</tpl>' 
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label">操作员:</td>'
		+ '<td class="input_bold">&nbsp;{[values.optr_name]}</td>'
		+ '<td class="label">产品描述:</td>'
		+ '<td class="input_bold">&nbsp;{[values.prod_desc || ""]}</td>'
		+ '</tr>'
		+ '<tr height=24>'
		+ '<td class="label" >一次性产品:</td>'
		+ '<td class="input_bold" >&nbsp;{[values.just_for_once_text || ""]}</td>'
		+ '<tpl if="values.just_for_once == \'T\'">'
		+ '<td class="label" >到期日期:</td>'
		+ '<td class="input_bold" >&nbsp;{[fm.dateFormat(values.invalid_date) || ""]}</td>'
		+ '</tpl>' + '</tr>' + '</tpl>' + '</table>';
		
/**
 * 产品明细
 */
ProdDetalisForm = Ext.extend(Ext.Panel, {
	tariffGrid : null,
	extAttrView : null,
	prodAllStore:null,
	prod : null,
	constructor : function(v) {
		this.prod = v;
		this.tariffGrid = new TariffGrid();
		ProdDetalisForm.superclass.constructor.call(this, {
					border : false,
					id : 'prodForm',
					layout : 'border',
					defaults : {bodyStyle : "background:#F9F9F9"},
					items : [{
								region : "north",
								height : 220,
								layout : 'form',
								anchor : '100%',
								title : '基本信息',
								collapsible : true,
								split : true,
								items : []
							}, {
								region : 'center',
								layout : 'fit',
								split : true,
								items : []
							}, {
								region : 'south',
								layout : 'fit',
								height : 170,
								split : true,
								items : [this.tariffGrid]
							}]

				})
	},
	doInitAttrView : function(v){
		var cfg = [{
	   	   columnWidth: .4,
	   	   layout: 'form',
	   	   baseCls: 'x-plain',
	   	   labelWidth: 125,
		   labelAlign: 'right'
	    },{
		   columnWidth: .5,
		   layout: 'form',
		   baseCls: 'x-plain',
		   labelWidth: 155,
		   labelAlign: 'right'
	    }];
	    this.extAttrView = ExtAttrFactory.gExtView({
	     		tabName: Constant.P_PROD,
	     		pkValue: v.prod_id
	       	},cfg)
	},
	doint : function(v) {
		this.doInitAttrView(v);
		
		var tpl = new Ext.XTemplate(custInfoHTML, {
					isNull : function(str) {
						if (str != null && str != "") {
							return str;
						} else {
							return false;
						}
					}
				});
		tpl.compile();
		
		if (v != null) {
			this.prodAllStore = v;
			var statdata = v.staticResList;
			var dyndata = v.dynamicResList;
			var nextdata = v.packList;
		
			StatGrid = Ext.extend(Ext.grid.GridPanel, {
				staticProdStore : null,
				constructor : function(data) {
					this.staticProdStore = new Ext.data.JsonStore({
								fields : ['res_name', 'res_desc', 'county_name','serverIds']
							});
					var cm = [{header : '地市名称',
								dataIndex : 'county_name',
								width : 120,
								renderer : App.qtipValue
							}, {
								header : '资源名称',
								dataIndex : 'res_name',
								width : 120,
								renderer : App.qtipValue
							}, {
								header : '控制字',
								dataIndex : 'serverIds',
								width : 120,
								renderer : App.qtipValue
							}]
					var sm = new Ext.grid.CheckboxSelectionModel();
					this.staticProdStore.loadData(data);
					StatGrid.superclass.constructor.call(this, {
								ds : this.staticProdStore,
								sm : sm,
								columns : cm,
								border : false
							});
				}
			});
			
			DynGrid = Ext.extend(Ext.grid.GridPanel, {
				dynProdStore : null,
				constructor : function(data) {
					this.dynProdStore = new Ext.data.JsonStore({
								fields : ['group_name', 'group_desc','res_number']});
					var cm = [{
								header : '资源组',
								dataIndex : 'group_name',
								width : 120,
								renderer : App.qtipValue
							}, {
								header : '最大资源数',
								dataIndex : 'res_number',
								width : 120,
								renderer : App.qtipValue
							}]
					var sm = new Ext.grid.CheckboxSelectionModel({singleSelect : true});
					if (!Ext.isEmpty(data)) {
						this.dynProdStore.loadData(data);
					}
					DynGrid.superclass.constructor.call(this, {
						ds : this.dynProdStore,
						sm : sm,
						columns : cm,
						border : false
					});
				}
			});
			
			//资源信息
			ResFrom = Ext.extend(Ext.Panel, {
				statGrid : null,
				dynGrid : null,
				constructor : function(title, statdata, dyndata) {
					this.statGrid = new StatGrid(statdata);
					this.dynGrid = new DynGrid(dyndata);
					ResFrom.superclass.constructor.call(this, {
								layout : 'border',
								defaults : {
									xtype : 'panel',
									layout : 'fit',
									border : false
								},
								items : [{
											region : 'center',
											title : '静态资源',
											items : [this.statGrid]
										}, {
											region : 'east',
											width : '45%',
											split : true,
											title : '动态资源',
											items : [this.dynGrid]
										}]
							})
			
				}
			});
			
			NextProdGrid = Ext.extend(Ext.grid.GridPanel, {
				nextProdStore : null,
				constructor : function(title, Ndata) {
					var fields = ['package_id','package_group_id','package_group_name','user_type','max_user_cnt','prod_list',
			'precent','user_type_text','terminal_type','terminal_type_text','prod_list_text'];
					var cm = [{
								header : '套餐内容组名称',
								dataIndex : 'package_group_name',
								width : 150,
								renderer : App.qtipValue
							}, {
								header : '产品内容组',
								dataIndex : 'prod_list_text',
								width : 350,
								renderer : App.qtipValue
							},{
								header : '用户类型',
								dataIndex : 'user_type_text',
								width : 80								
							},{
								header : '终端类型',
								dataIndex : 'terminal_type_text',
								width : 80
							},{
								header : '用户数',
								dataIndex : 'max_user_cnt',
								width : 70
							}, {
								header : '权重',
								dataIndex : 'precent',
								width : 80
							}]
					this.nextProdStore = new Ext.data.JsonStore({
						fields : fields,
						sortInfo:{
							field:'user_type',direction:'DESC',
							field:'package_group_id',direction:'DESC'
						}
					});
					var sm = new Ext.grid.CheckboxSelectionModel({singleSelect : true});
			
					this.nextProdStore.loadData(Ndata);
					NextProdGrid.superclass.constructor.call(this, {
								title : title,
								ds : this.nextProdStore,
								sm : sm,
								columns : cm
//								,tbar : [{text : '分成值操作记录',iconCls : 'query',scope : this,handler : function(){
//									new ProdPackageHisWin(v).show();
//								}}]
							});
				}
			});

			ProdInfo = Ext.extend(Ext.Panel, {
				border : false,
				constructor : function(title, html) {
					ProdInfo.superclass.constructor.call(this, {
								border : false,
								anchor : '100% 90%',
								autoScroll:true,
								bodyStyle : "background:#F9F9F9; padding: 2px",
								html : html
							});
				}
			});
			var ppp = Ext.getCmp('prodForm').items.itemAt(0);
			var rrr = Ext.getCmp('prodForm').items.itemAt(1);
			ppp.removeAll();
			rrr.removeAll();
			var panel = new ProdInfo("<font style='font-family:微软雅黑;font-size:12'>基本信息</font>",tpl.applyTemplate(v));
			ppp.add(panel);
			ppp.add(this.extAttrView);
			if (!Ext.isEmpty(statdata) || !Ext.isEmpty(dyndata)) {
				var statgrid = new ResFrom("<font style='font-family:微软雅黑;font-size:12'>资源信息</font>",statdata, dyndata);rrr.add(statgrid);
			}
			if (!Ext.isEmpty(nextdata)) {
				var prodform = new NextProdGrid("<font style='font-family:微软雅黑;font-size:12'>套餐内容组信息</font>",nextdata)
				rrr.add(prodform);
			}
			ppp.doLayout();
			rrr.doLayout();
		}
	}
});

/**
 * 套餐分成值操作记录
 * @class ProdDetalisWindow
 * @extends Ext.Window
 */
ProdPackageHisWin = Ext.extend(Ext.Window,{
	prodPackageHisGrid : null,
	prodPackageHisStore : null,
	constructor : function(prod) {
		this.prodPackageHisStore = new Ext.data.JsonStore({
			autoLoad : true,
			baseParams : {doneId : prod.prod_id},
			url : root + "/system/Prod!queryProdPackageHis.action",
			fields : ['prod_name', 'tariff_name', 'max_prod_count','percent_value','percent',
				'package_tariff_name','optr_name','done_date','type','type_text'],
			sortInfo:{
				field:'type',direction:'DESC',
				field:'package_tariff_name',direction:'DESC'
			}
		});
		
		this.ProdPackageHisGrid = new Ext.grid.GridPanel({
			border : true,
		    region : 'center',
		    store: this.prodPackageHisStore,
		    columns: [{
		        header: '操作员',
		        dataIndex: 'optr_name',
		        width : 80,
		        renderer: App.qtipValue
		    }, {
		        header: '套餐资费名称',
		        dataIndex: 'package_tariff_name',
		        width : 80,
		        renderer: App.qtipValue
		    }, {
		        header: '子产品',
		        dataIndex: 'prod_name',
		        renderer: App.qtipValue
		    }, {
		        header: '子产品资费',
		        hidden : false,
		        dataIndex: 'tariff_name',
		        renderer: App.qtipValue
		    },{
				header : '分成值',
				dataIndex : 'percent_value',
				width : 60
			},{
				header : '百分比',
				dataIndex : 'percent',
				width : 60
			}, {
				header : '最大产品数',
				dataIndex : 'max_prod_count',
				hidden : prod.prod_type == 'UPKG' ? true : false,
				width : 60
			}, {
				header : '分成类型',
				dataIndex : 'type_text',
				width : 65
			}, {
		        header: '操作时间',
		        dataIndex: 'done_date',
		        width : 120
		    }],
            tbar: ['过滤:',
            {
                xtype: 'textfield',
                emptyText : '根据套餐资费过滤',
                enableKeyEvents: true,
                listeners: {
                    scope: this,
                    keyup: function (txt, e) {
                        if (e.getKey() == Ext.EventObject.ENTER) {
                            var value = txt.getValue();
                            if (Ext.isEmpty(value)){
                            	this.prodPackageHisStore.clearFilter();
                            }else{
                            	this.prodPackageHisStore.filterBy(function (record) {
	                                return record.get('package_tariff_name').indexOf(value) >= 0;
	                            });
                            }
                        }
                    }
                }
            }]
		});
		ProdPackageHisWin.superclass.constructor.call(this, {
			title : '套餐分成值操作记录',
			layout : 'border',
			width : 700,
			height : 500,
			closeAction : 'close',
			items : [this.ProdPackageHisGrid],
			buttons : [{
				text : '关闭',
				scope : this,
				handler : function() {
					this.close();
				}
			}]
		});
	}
})



ProdDetalisWindow = Ext.extend(Ext.Window, {
			prodDetalisForm : null,
			constructor : function(v, rowIndex) {
				this.prodDetalisForm = new ProdDetalisForm(v);
				ProdDetalisWindow.superclass.constructor.call(this, {
							title : '产品明细',
							layout : 'fit',
							width : 700,
							height : 600,
							closeAction : 'close',
							items : [this.prodDetalisForm],
							buttons : [{
										text : '关闭',
										scope : this,
										handler : function() {
											this.close();
										}
									}]
						});
			}
		});