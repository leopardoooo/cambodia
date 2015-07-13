/**
 * 客户数量配置
 */
YearGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	yearStore : null,
	date:nowDate().format('Y'),
	constructor : function(){
		this.yearStore = new Ext.data.JsonStore({
			fields : ['year_date','is_user']
		})
		
		var cm = new Ext.grid.ColumnModel([
				{header:'年份',dataIndex:'year_date',width:40,editor : new Ext.form.TextField({
					allowDecimals:false,allowNegative:false,minLength:4,maxLength:4,
					allowBlank : false
				})},
				{header : '操作',width : 30,
					renderer : function(v, md, record, i) {
						var rs = Ext.encode(record.data);
						return String.format("&nbsp;<a href='#'  style='color:blue' onclick=Ext.getCmp(\'"+"YearGridId"+"\').deleteRow()> 删除 </a>",rs, i);
					}
				}
		        ]);
		YearGrid.superclass.constructor.call(this,{
			id : 'YearGridId',
			border : false,
			store : this.yearStore,
			cm : cm,
			height:100,
			enableColumnMove : false,
			forceValidation: true,
	        clicksToEdit: 1,
			viewConfig:{
	        	forceFit : true
	        },
	        tbar : [{
						xtype:'numberfield',
						id:'insertNumId',
						width:30,
						minValue:1,
						allowDecimals:false, //不允许输入小数 
				    	allowNegative:false, //不允许输入负数
				    	maxValue:10,  
						value:1,
						listeners:{
							scope:this,
		            		change: function(comp){
		            			var v = comp.getValue();
		            			if(v>10){		            				
		            				Ext.getCmp('addYearId').setDisabled(true);
		            			}else{
		            				Ext.getCmp('addYearId').setDisabled(false);
		            			}
		            		}
						}
					},'年',{
	        	text : '添加',
	        	scope : this,
	        	id:'addYearId',
	        	iconCls : 'icon-add',
	        	handler : this.addRecord
	        }]
		})
	},
	initEvents: function(){
		this.on("afterrender",function(){
			this.swapViews();
		},this,{delay:10});
		
		YearGrid.superclass.initEvents.call(this);
	},
	swapViews : function(){
		var obj = {
			'year_date' : this.date		
		};
		var Plant= this.getStore().recordType;
		var p = new Plant(obj);
		this.stopEditing();
		this.getStore().insert(0,p);
		this.startEditing(0,0);
	},
	deleteRow : function(){//删除行
		var record= this.getSelectionModel().getSelected();
		this.getStore().remove(record);
	},
	addRecord : function(){
		var num = Ext.getCmp('insertNumId').getValue();
		if(this.yearStore.getCount()==0){
			this.date = nowDate().format('Y')-1;
		}
		for(var i=1;i<=num;i++){
			this.date = this.date+1;
			this.gridAdd(Ext.getCmp('YearGridId'), {
				'year_date' : this.date		
			});
		}
	},
	gridAdd : function(scopeThis, fieldsObj, editColumn) {
		editColumn = editColumn || 0;
		var store = scopeThis.getStore();
		var recordType = store.recordType;
		var recordData = new recordType(fieldsObj);
		scopeThis.stopEditing();
		store.insert(0, recordData);
		scopeThis.startEditing(0, editColumn);
		scopeThis.getSelectionModel().selectRow(0);
	}
}) 
 

ColonyCountyTree = Ext.extend(Ext.tree.TreePanel, {
    checkchange : false,
    constructor: function () {
        var loader = new Ext.tree.TreeLoader({
            url: root + "/system/Prod!getCustCountyTree.action" 
        });
        ColonyCountyTree.superclass.constructor.call(this, {
        	id : 'CountyTreeId',
            margins: '0 0 3 2',
            layout: 'fit',
            lines: false,
            autoScroll: true,
            animCollapse: true,
            animate: false,
            collapseMode: 'mini',
            bodyStyle: 'padding:3px',
            loader: loader,
            root: {
                id: '-1',
                iconCls: 'x-tree-root-icon',
                checked : false,
                text: '应用地市'
            }
        });
        this.expandAll();
    },
    initEvents: function(){
    	this.on("afterrender",function(){
	        	if(App.data.optr['county_id'] != '4501'){
	        		var node = this.getRootNode();
	        		node.attributes.checked = true;
	        		node.ui.toggleCheck(true);
	        		node.fireEvent('checkchange', node, true);
	        	}
	    },this,{delay:1000});
		this.on('checkchange',function (node, checked) {
			this.checkchange = true;
            node.expand();
            node.attributes.checked = checked;
        	node.eachChild(function (child) {
                child.ui.toggleCheck(checked);
                child.attributes.checked = checked;
                child.fireEvent('checkchange', child, checked);
            });
            
        },this);
		
		ColonyCountyTree.superclass.initEvents.call(this);
	}
	,getCheckedIds : function(){
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


var SelectPanel = Ext.extend(Ext.form.FormPanel, {
	countyTree:null,
	colonyStore : null,
	classStore : null,
    constructor: function () {
    	this.countyTree = new ColonyCountyTree();
		this.colonyStore = new Ext.data.JsonStore({
				url : root+ '/system/CustColony!getCustColony.action',
				fields : ['item_name', 'item_value']
			});
		this.classStore = new Ext.data.JsonStore({
				url : root+ '/system/CustColony!getCustClass.action',
				fields : ['item_name', 'item_value']
			});
			
        SelectPanel.superclass.constructor.call(this, {
            border: false,
            layout : 'border',
            baseCls: "x-plain",
            items: [
            	{region:'west',width:540,layout:'column',
				defaults:{
					baseCls: 'x-plain',
					columnWidth:0.5
				},
			items:[{
					items:[ {
									xtype : 'itemselector',
									name : 'cust_colony',
									id : 'selectColonyId',
									imagePath : '/'
											+ Constant.ROOT_PATH_LOGIN
											+ '/resources/images/itemselectorImage',
									multiselects : [{
										legend : '待选客户群体',
										width : 100,
										height : 230,
										store : new Ext.data.ArrayStore({
													fields : ['item_value',
															'item_name']
												}),
										displayField : 'item_name',
										valueField : 'item_value'
									}, {
										legend : '已选客户群体',
										width : 100,
										height : 230,
										store : new Ext.data.ArrayStore({
													fields : ['item_value',
															'item_name']
												}),
										displayField : 'item_name',
										valueField : 'item_value'
									}]
								}]
			},{items:[ {
									xtype : 'itemselector',
									name : 'cust_class',
									id : 'selectClassId',
									imagePath : '/'
											+ Constant.ROOT_PATH_LOGIN
											+ '/resources/images/itemselectorImage',
									multiselects : [{
										legend : '待选优惠类型',
										width : 100,
										height : 230,
										store : new Ext.data.ArrayStore({
													fields : ['item_value',
															'item_name']
												}),
										displayField : 'item_name',
										valueField : 'item_value'
									}, {
										legend : '已选优惠类型',
										width : 100,
										height : 230,
										store : new Ext.data.ArrayStore({
													fields : ['item_value',
															'item_name']
												}),
										displayField : 'item_name',
										valueField : 'item_value'
									}]
								}]
            	}]},{region:'center',border: false,layout:'fit',items:[this.countyTree]}]
		})
    },
	initComponent : function() {
		SelectPanel.superclass.initComponent.call(this);
		this.colonyStore.load();
		this.colonyStore.on("load", this.doLoadColonyData, this);
		this.classStore.load();
		this.classStore.on("load", this.doLoadClassData, this);
	},
	doLoadColonyData : function() {
		var arr = [];
		this.colonyStore.each(function(record) {
					arr.push([record.data.item_value, record.data.item_name]);
				});
		Ext.getCmp('selectColonyId').multiselects[0].store.loadData(arr);
	},
	doLoadClassData : function(){
		var arr = [];
		this.classStore.each(function(record) {
					arr.push([record.data.item_value, record.data.item_name]);
				});
		Ext.getCmp('selectClassId').multiselects[0].store.loadData(arr);
	}
	
})

ColonyFormPanel = Ext.extend(Ext.Panel, {
	yearGrid:null,
	selectPanel:null,
    constructor: function () {
    	this.selectPanel =  new SelectPanel()
    	this.yearGrid =  new YearGrid();
        ColonyFormPanel.superclass.constructor.call(this, {
            border: false,
            layout : 'border',
            closable: true,
            baseCls: "x-plain",
            items: [
            	{region : 'center',layout : 'fit',border: false,items : [this.yearGrid]},
            	{region:'east',height:150,width:'88%',border: false,layout:'fit',items:[this.selectPanel]} 
            ],

            buttonAlign: 'center',
            buttons: [{
                text: '保存',
                scope: this,
                handler: this.doSave
            }, {
                text: '重置',
                scope: this,
                handler: function () {
                	this.formReset();
                }
            }],
            bbar:[' ', ' ', '客户开户总数:',
							{	
			    	width : 70,
			    	xtype:'numberfield',
			    	allowDecimals:false, //不允许输入小数 
			    	allowNegative:false, //不允许输入负数
			    	id:'custNumId',
			    	name : 'custNum'
			    },{xtype:'displayfield',width:40}, '用户开户总数:',
							{	
			    	width : 70,
			    	xtype:'numberfield',
			    	allowDecimals:false, //不允许输入小数 
			    	allowNegative:false, //不允许输入负数
			    	id:'userNumId',
			    	name : 'userNum'
			    }]
        });
    },
    doSave: function () {
        if (!this.selectPanel.getForm().isValid()) return;
        
        var colonyNum = Ext.getCmp('selectColonyId').multiselects[1].store.getCount();
        var classNum = Ext.getCmp('selectClassId').multiselects[1].store.getCount();
        
        if(colonyNum>1 && classNum>1){
        	Alert("客户群体和客户优惠类型所选数不能都大于1!");
            return false;
        };
        if(colonyNum == 0 && classNum == 0){
        	Alert("客户群体和客户优惠类型必须选择一项!");
            return false;
        };
        var countys = [],years = [],values = {};
        values['custNum'] = Ext.getCmp('custNumId').getValue();
        values['userNum'] = Ext.getCmp('userNumId').getValue();
        
       	countys =Ext.getCmp('CountyTreeId').getCheckedIds();
        if (countys.length > 0) {
            values["countys"] = countys.join(",");
        }else {
            Alert("请选择地市!");
            return false;
        }
        
        var selectForm = this.selectPanel.getForm().getValues();
       	for (var key in selectForm) {
			values[key] = selectForm[key];
		}
		var store = this.yearGrid.getStore();
		store.each(function(record) {
			years.push(record.get('year_date'))
		});
		if(years.length==0){
			Alert("请选择年份!");
            return false;
		}
		values['years'] = years.join(",");
		
        Ext.Ajax.request({
            url: root + '/system/CustColony!saveCustColony.action',
            params: values,
            scope: this,
            success: function (res, opt) {
                var data = Ext.decode(res.responseText);
                if (data.success === true) {
                     Alert('保存成功!', function () {
                        Ext.getCmp('CustColonyGridId').getStore().load({
                            params: {
                                start: 0,
                                limit: Constant.DEFAULT_PAGE_SIZE
                            }
                        });
                    });
                   this.formReset();
                }
            }
        });
    },
    formReset:function(){
		this.selectPanel.getForm().reset();
        this.yearGrid.getStore().removeAll();
        this.yearGrid.date = nowDate().format('Y')-1;
    }
});

UpdateColonyWindow = Ext.extend(Ext.Window, {
	form:null,
	recordDate:null,
    constructor: function (record) {
    	this.recordDate = record;
    	updateThis = this;
    	this.form = new Ext.form.FormPanel({
			layout : 'column',
			border : false,
			region : 'center',
			height : 130,
			bodyStyle : 'padding : 5px;padding-top : 10px;',
			labelWidth : 85,
			defaults : {
				columnWidth:1,
				layout: 'form',
				labelWidth : 125,
				border : false
			},
			items : [{
				labelWidth : 1,
				items : [{
		                xtype: 'displayfield',
		                value: "<font style='font-family:微软雅黑;font-size:15px;color:red'><b>" + 
		                		record.get('year_date')+"年份,"
		                		+record.get('county_name_for')		                		
		                		+"</b><br>客户开户总数:"+record.get('cust_num')+",已开户总数:"+record.get('use_num')+"</br>" +
		                				"用户开户总数:"+record.get('user_num')+"</font>"
	            	}]
			},{
				items : [{
				    	width : 70,
				    	fieldLabel : '新的客户开户总数',
				    	xtype:'numberfield',
				    	allowDecimals:false, //不允许输入小数 
				    	allowNegative:false, //不允许输入负数
				    	value:record.get('cust_num'),
				    	minValue:record.get('use_num'),  
				    	name : 'custNum'
				    },{
				    	width : 70,
				    	fieldLabel : '新的用户开户总数',
				    	xtype:'numberfield',
				    	allowDecimals:false, //不允许输入小数 
				    	allowNegative:false, //不允许输入负数
				    	value:record.get('user_num'),
				    	name : 'userNum'
				    }]
			}]
		});
    	
        UpdateColonyWindow.superclass.constructor.call(this, {
            title: '修改开户总数',
            border: false,
        	layout : 'border',
			height : 200,
			width : 350,
			closeAction : 'close',
			items : [this.form],
            buttons: [
                {
                text: '保存',
                scope: this,
                handler: this.doSave},
            {
                text: '关闭',
                scope: this,
                handler: function () {
                    this.close();
                }}]
        })
    },
    doSave: function () {
        if (!this.form.getForm().isValid()) return;
        var values = {};
        
		values['custNum'] = this.form.getForm().findField('custNum').getValue();
		values['userNum'] = this.form.getForm().findField('userNum').getValue();
		values['years'] = this.recordDate.get('year_date');
		values['cust_colony'] = this.recordDate.get('cust_colony');
		values['cust_class'] = this.recordDate.get('cust_class');
		values['countys'] = this.recordDate.get('county_id_for');
        Ext.Ajax.request({
            url: root + '/system/CustColony!updateCustColony.action',
            params: values,
            scope: this,
            success: function (res, opt) {
                var data = Ext.decode(res.responseText);
                if (data.success === true) {
                     Alert('保存成功!', function () {
                        Ext.getCmp('CustColonyGridId').getStore().load({
                            params: {
                                start: 0,
                                limit: Constant.DEFAULT_PAGE_SIZE
                            }
                        });
                        updateThis.close();
                    });
                }else{
                 Alert('操作失败!');
                }
            }
        });
    }
});

CustColonyGrid = Ext.extend(Ext.grid.GridPanel, {
	sendstore : null,
	constructor : function(cfg) {
		Ext.apply(this, cfg || {});
		this.sendstore = new Ext.data.JsonStore({
					url : root + '/system/CustColony!queryCustColony.action',
					fields : ['year_date', 'cust_colony', 'cust_colony_text', 'cust_num','county_id','cust_class_text','cust_class',
							'user_num','optr_name','use_num','county_id_for','county_name_for'],
					root : 'records',
					totalProperty : 'totalProperty',
					autoDestroy : true
				});

		this.sendstore.load({
					params : {
						start : 0,
						limit : Constant.DEFAULT_PAGE_SIZE
					}
				});
		CustColonyGrid.superclass.constructor.call(this, {
			id : 'CustColonyGridId',
			ds : this.sendstore,
			border: false,
			sm : new Ext.grid.CheckboxSelectionModel(),
			cm : new Ext.grid.ColumnModel([{
						header : '年份',
						width : 60,
						dataIndex : 'year_date'						
					},{
						header : '客户群体',
						width : 80,
						dataIndex : 'cust_colony_text'
					},{
						header : '客户优惠类型',
						width : 80,
						dataIndex : 'cust_class_text'
					}, {
						header : '地市',
						width : 80,
						dataIndex : 'county_name_for'						
					}, {
						header : '客户开户总数',
						width : 80,
						dataIndex : 'cust_num'						
					}, {
						header : '客户已开户数',
						width : 80,
						dataIndex : 'use_num'
					}, {
						header : '用户开户总数',
						width : 80,
						dataIndex : 'user_num'						
					},{
						header : '创建人',
						width : 80,
						dataIndex : 'optr_name'
					}, {
				       header: '操作',
				       width:180,
				       scope:this,
			           renderer:function(value,meta,record,rowIndex,columnIndex,store){
			           		var btns = this.doFilterBtns(record);
			            	return btns;
					   }
			
					}]),
			bbar : new Ext.PagingToolbar({
						store : this.sendstore
					}),
			tbar : [' ', '查询条件:', new Ext.ux.form.SearchField({
								store : this.sendstore,
								width : 220,
								hasSearch : true,
								emptyText : '支持年份,地市名称,客户群体名称查询'
							})]

		})
	},
	doFilterBtns : function(record){
		var btns = "";
		if(App.data.optr['county_id'] == '4501' || record.get('county_id') == App.data.optr['county_id']){
			btns = btns + "&nbsp;&nbsp;<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"CustColonyGridId"+"\').updateRecord()> 修改开户总数 </a>";
			btns = btns + "&nbsp;&nbsp;<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"CustColonyGridId"+"\').deleteRecord()> 删除 </a>";
		}
		return btns;
	},
	updateRecord:function(){
	 	var grid = Ext.getCmp('CustColonyGridId');
        var record = grid.getSelectionModel().getSelected();
        new UpdateColonyWindow(record).show();
	}
	,
    deleteRecord: function () {
    	var grid  = Ext.getCmp('CustColonyGridId');
    	var record = grid.getSelectionModel().getSelected();
    	var year_date = record.get('year_date');
    	var cust_colony = record.get('cust_colony');
    	var cust_class = record.get('cust_class');
    	var county_id_for = record.get('county_id_for');
    	var str="";
    	if(record.get('use_num')>0){
    		str = "<font style='font-family:微软雅黑;font-size:15px;color:red'><b>" + 
		                		record.get('year_date')+"年"
		                		+record.get('county_name_for')
		                		+"的"+record.get('cust_colony_text')
		                		+"<br>客户开户总数:["+record.get('cust_num')+"]</br>客户已开户总数:["+record.get('use_num')+"]</b></font><br>确定要删除吗?</br>";
    	}else{
    		str="确定要删除吗?";
    	}
        Confirm(str, null, function () {
            Ext.Ajax.request({
            	url : root + '/system/CustColony!deleteCustColony.action',
                params: {
                    years:year_date,cust_colony:cust_colony,cust_class:cust_class,countys:county_id_for
                },
                success: function (res, ops) {
                    var rs = Ext.decode(res.responseText);
                    if (true === rs.success) {
                        Alert('操作成功!', function () {
                            Ext.getCmp('CustColonyGridId').getStore().load({
                                params: {
                                    start: 0,
                                    limit: Constant.DEFAULT_PAGE_SIZE
                                }
                            });
                        });
                    } else {
                        Alert('操作失败');
                    }
                }
            });
        }) 
	}
});

CustColonyView = Ext.extend(Ext.Panel, {
    constructor: function () {
        var fp = new ColonyFormPanel();
        var grid = new CustColonyGrid();
        CustColonyView.superclass.constructor.call(this, {
            id: 'CustColonyView',
            title: '客户限制',
            closable: true,
            border: false,
            layout : 'border',
            baseCls: "x-plain",
            items: [{region:'north',border: false,height:300,hidden:(App.data.optr['county_id']!='4501')? true : false,layout:'fit',split : true,items:[fp]}, 
            	{region : 'center',border: false,split : true,layout : 'fit',items : [grid]}            
            ]
        });
    }
});