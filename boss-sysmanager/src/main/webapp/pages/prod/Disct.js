/**
 * 折扣
 */
DisctInfoPanel = Ext.extend(Ext.form.FormPanel, {
    parent: null,
    store: null,
    tariffrent:null,
    disctRuleStore:null,
    constructor: function (p,tariff,record) {
        this.parent = p;
        this.store = record;
        this.disctRuleStore = new Ext.data.JsonStore({
            url: root + '/system/Index!queryBusiRule.action',
            fields: ['rule_id', 'rule_name']
        })
        this.disctRuleStore.load();
        this.disctRuleStore.on("load", function (s) {
            s.insert(0, new Ext.data.Record({
                rule_name: '无规则...',
                rule_id: ''
            }));
            
            this.doInit();
        },this);
        
        this.tariffrent = Ext.util.Format.formatFee(tariff.get('rent'));
        DisctInfoPanel.superclass.constructor.call(this, {
            id: 'disctInfoPanel',
            labelAlign: "right",
            border: false,
            bodyStyle: 'padding-top: 10px',
            labelWidth: 120,
            defaults: {
                disabled : this.store ? true : false,
                baseCls:'x-plain'
            },
            items: [{
                fieldLabel: '折扣名称',
                name: 'disct_name',
                allowBlank: false,
                xtype: 'textfield',
                width: 120,
                disabled : false
            },{
                fieldLabel: '折后租金(元)',
                width: 120,
                allowBlank: false,
                id:'finalRentId',
                xtype: 'numberfield',
                minValue : 0,
                maxValue:Ext.util.Format.formatFee(tariff.get('rent')),
                name: 'final_rent',
                listeners:{
                	scope:this,
                	change: this.changeRent
                }
            },{
                fieldLabel: '最低实缴金额(元)',
                width: 120,
                xtype: 'numberfield',
                id:'minPayId',
                miniValue : 0,
                name: 'min_pay'
            },{
	            fieldLabel: '可退可转',
	            hiddenName: 'refund',
	            allowBlank: false,
	            xtype: 'paramcombo',
	            paramName: 'BOOLEAN',
	            defaultValue: 'T',
	            disabled : false,
	            width: 120
	        },{
                fieldLabel: '业务规则',
                xtype: 'combo',
                store: this.disctRuleStore,
                emptyText: '请选择',
                mode: 'local',
                editable: true,
                forceSelection:true,
				selectOnFocus:true,
				triggerAction:'all',
                disabled : false,
                valueField: 'rule_id',
                displayField: 'rule_name',
                hiddenName: 'rule_id'
           	}, {
				xtype : 'datefield',
				editable : false,
				width : 100,
				fieldLabel : '生效日期',
				id : 'startdt',
				vtype : 'daterange',
				endDateField : 'enddt',
				name : 'eff_date',
				format : 'Y-m-d',
				minText : '不能选择当日之前',
				allowBlank : false,
				value : nowDate(),
				minValue : nowDate().format('Y-m-d')
			}, {
				xtype : 'datefield',
				editable : false,
				vtype : 'daterange',
				id : 'enddt',
				startDateField : 'startdt',
				width : 100,
				fieldLabel : '失效日期',
				name : 'exp_date',
				format : 'Y-m-d',
				disabled : false,
				minValue : nowDate().format('Y-m-d')
			},{
				xtype: 'hidden',
				disabled : false,
			    id:'disctRentId',
			    name: 'disct_rent'
			},{
				xtype: 'hidden',
                disabled : false,
                name: 'disct_id'
			},{
				xtype: 'hidden',
                disabled : false,
                name: 'tariff_id',
                value:tariff.get('tariff_id')
            }]
        })
    },
    initComponent: function () {
        DisctInfoPanel.superclass.initComponent.call(this);
        App.form.initComboData(this.findByType("paramcombo"), this.doInit, this);
    },
    doInit: function () {
        if (this.store) {
        	this.loadRecord(this.store.data);
        }
    },changeRent : function(obj){
    	Ext.getCmp('disctRentId').setValue(Ext.util.Format.formatToFen(this.tariffrent-obj.getValue()));
    },loadRecord:function(data){
        var prodInfo = {};
			for (var prop in data) {
				prodInfo[prop] = data[prop];
			}
			prodInfo['eff_date'] = Date.parseDate(prodInfo["eff_date"],'Y-m-d h:i:s');
			prodInfo['exp_date'] = Date.parseDate(prodInfo["exp_date"],'Y-m-d h:i:s');
			prodInfo['final_rent'] = Ext.util.Format.formatFee(prodInfo['final_rent']);
			prodInfo['min_pay'] = Ext.util.Format.formatFee(prodInfo['min_pay']);
			if (!Ext.isEmpty(prodInfo['eff_date'])) {
				if (prodInfo['eff_date'].format('Y-m-d') <= nowDate().format('Y-m-d')) {
					Ext.getCmp('startdt').setDisabled(true);
				}
			} else {
				prodInfo['eff_date'] = nowDate().format('Y-m-d');
			}
			this.getForm().loadRecord(new Ext.data.Record(prodInfo));
    }
});

DisctCountyTree = Ext.extend(Ext.tree.TreePanel, {
    disctId : null,
    constructor: function (v, disctId) {
    	this.disctId = disctId;
        var loader = new Ext.tree.TreeLoader({
            url: root + "/system/Prod!getTariffDisctCountyTree.action?query=" + disctId
        });
        DisctCountyTree.superclass.constructor.call(this, {
            split: true,
            title: '应用地市',
            id: 'DisctCountyTree',
            margins: '0 0 3 2',
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
                text: '地市结构'
            },
            listeners:{
            	scope:this,
            	afterrender: function(){
            		//如果不是修改
			        if(!this.disctId){
			        	if(App.data.optr['county_id'] != '4501'){
			        		var node = this.getRootNode();
			        		node.attributes.checked = true;
			        		node.ui.toggleCheck(true);
			        		node.fireEvent('checkchange', node, true);
			        	}
			        }
            	},
            	checkchange: function (node, checked) {
		            node.expand();
		            node.attributes.checked = checked;
		            node.eachChild(function (child) {
		                child.ui.toggleCheck(checked);
		                child.attributes.checked = checked;
		                child.fireEvent('checkchange', child, checked);
		            });
		        }
            }
        });
        this.expandAll();
    }
});

DisctFormWindow = Ext.extend(Ext.Window, {
    tariffInfoPanel: null,
    countyTree: null,
    constructor: function (t,tariff,s) {
        thiz = this;
        var disctId = null;
        if(!Ext.isEmpty(s)){
        	disctId = s.get('disct_id');
        }
        this.disctInfoPanel = new DisctInfoPanel(this,tariff,s);
        this.countyTree = new DisctCountyTree(this, disctId);
        DisctFormWindow.superclass.constructor.call(this, {
            title: t,
            width: 600,
            id: 'disctwin',
            height: 500,
            layout:'border',
            closeAction: 'close',
            border: false,
            items: [
            	{region:'center',layout:'fit',items:[this.disctInfoPanel]},
        		{region:'east',animCollapse: true,
		            animate: false,
		            collapseMode: 'mini',
//		            collapsed : (App.data.optr['county_id']!='4501'  && ispkg == cfgProdType["P"])? true : false,
	            	width : "50%",
	            	split : true,
	            	layout:'fit',
	            	items:[this.countyTree]}
            ],
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
        var formone = this.disctInfoPanel.getForm().getValues(),
            all = {};
        if (!this.disctInfoPanel.getForm().isValid()) {
            return;
        }
        for (var key in formone) {
            all["PProdTariffDisct." + key] = formone[key];
        }
        
        var disctCountyIds = [];
        var nodes = this.countyTree.getChecked();
        for (var i in nodes) {
            if (nodes[i].leaf) {
                disctCountyIds.push(nodes[i].id);
            }
        }
        if (disctCountyIds.length > 0) {
            all["disctCountyIds"] = disctCountyIds;
        }else {
            Alert("请选择应用地市!");
            return;
        }
        
        all["PProdTariffDisct.final_rent"] =  Ext.util.Format.formatToFen(Ext.getCmp('finalRentId').getValue());
        all["PProdTariffDisct.min_pay"] =  Ext.util.Format.formatToFen(Ext.getCmp('minPayId').getValue());
        all["PProdTariffDisct.disct_rent"] =  Ext.getCmp('disctRentId').getValue();
        var msg = Show();
        Ext.Ajax.request({
            url : root + '/system/Prod!saveDisct.action',
            params: all,
            success: function (res, ops) {
            	msg.hide();msg = null;
                var rs = Ext.decode(res.responseText);
                if (true === rs.success) {
                    Alert('操作成功!', function () {
                        thiz.close();
                        Ext.getCmp('disctgrid').doint();
                    }, thiz);
                }
                else {
                    Alert('操作失败!');
                }
            }
        });
    }
})

DisctGrid = Ext.extend(Ext.grid.GridPanel, {
	disctStore : null,
	is_statusStore:null,
	tariffId:null,
	tariffRent:null,
	record:null,
	constructor : function() {
		App.disct = Ext.data.Record.create([ // 加载资费
				{name : 'disct_id'}, {name : 'disct_name'}, {name : 'final_rent'}, {name : 'disct_rent'}, {name : 'min_pay'}, {name : 'rule_id'}, 
				{name : 'tariff_id'}, {name : 'status'}, {name : 'rule_name'}, {name : 'status_text'},{name : 'refund_text'},{name : 'refund'},
				{name : 'eff_date'},{name : 'exp_date'}]);
		disctGridThiz = this;				
		
		this.disctStore = new Ext.data.JsonStore({
					url : root + '/system/Prod!queryDisct.action',
					totalProperty : 'totalProperty',
					autoDestroy : true,
					fields : App.disct,
					sortInfo: {field: 'status_text', direction: 'DESC'}
				});		
		var disctcm = new Ext.grid.ColumnModel({
		 	defaults: {
	            sortable: false          
	        },
			columns: [{header : '折扣名称',dataIndex : 'disct_name',width : 80,
						editor : new Ext.form.TextField({allowBlank : false}),renderer : App.qtipValue},
					{header : '状态',dataIndex : 'status_text',width : 50, renderer:Ext.util.Format.statusShow} ,
					{header : '折后租金(元)',dataIndex : 'final_rent',width : 80,renderer : Ext.util.Format.formatFee}, 
					{header : '最低实缴金额(元)',dataIndex : 'min_pay',width : 80,renderer : Ext.util.Format.formatFee},
					{header : '可退可转',dataIndex :'refund_text',width : 60},
					{header : '业务规则',dataIndex :'rule_name',width : 80},
					{header : '生效时间',dataIndex :'eff_date',width : 80,renderer:Ext.util.Format.dateFormat},
					{header : '失效时间',dataIndex :'exp_date',width : 80,renderer:Ext.util.Format.dateFormat}, 
					{dataIndex : 'tariff_id',hidden : true}, 
					{dataIndex : 'status',hidden : true}, 
					{dataIndex : 'disct_id',hidden : true},
					{header : '操作',width : 100,scope:this,
					 renderer:function(value,meta,record,rowIndex,columnIndex,store){
		            	var btns = this.doFilterBtns(record);
		            	return btns;
					}
				}]});

		DisctGrid.superclass.constructor.call(this, {
					ds : this.disctStore,
					height : 340,
					region : 'center',
					id:'disctgrid',
					cm : disctcm,
					clicksToEdit : 1,					
					tbar : [{text : '增加折扣',iconCls : 'icon-add',scope : this,handler : this.onAddDisct}
							]
				})

	},
	doFilterBtns : function(record){
		var btns = "";
		if(record.get('status') == 'ACTIVE'){
			if(Ext.isEmpty(record.get('exp_date')) || Date.parseDate(record.get('exp_date'),'Y-m-d h:i:s').format('Y-m-d') >= nowDate().format('Y-m-d')){
				btns = btns + "&nbsp;<a href='#' onclick=Ext.getCmp(\'"+"disctgrid"+"\').updateDisct(); style='color:blue'>修改  </a>";
			}
				btns = btns + "&nbsp;&nbsp;<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"disctgrid"+"\').deleteDisct()>禁用 </>";
		}else{
			btns = btns + "&nbsp;&nbsp;<a href='#' style='color:blue' onclick=Ext.getCmp(\'"+"disctgrid"+"\').ToUseDisct()>启用 </>";
		}
		return btns;
	},
	loadBasem : function(v) {
		this.disctStore.baseParams.doneId = v.get('tariff_id');
		this.disctStore.load();
		this.record = v;		
	},	
	doint : function() {
		this.disctStore.load();
	},onAddDisct : function() {
		new DisctFormWindow("增加折扣",this.record).show();
	},updateDisct:function(){
		var grid = Ext.getCmp('disctgrid');
        var records = grid.getSelectionModel().getSelected();
		new DisctFormWindow("修改折扣",this.record,records).show();
	},ToUseDisct:function(){
		var grid = Ext.getCmp('disctgrid');
        var records = grid.getSelectionModel().getSelected();
		new DisctFormWindow("启用折扣",this.record,records).show();
	},
	deleteDisct : function(v, index) {
		var rec = this.getSelectionModel().getSelected();
		if (Ext.isEmpty(rec.get('disct_id'))) {
			this.disctStore.remove(rec);
		} else {
			Confirm("确定要禁用该折扣吗?", null, function() {
				Ext.Ajax.request({
					url : root + '/system/Prod!deleteDisct.action',
					params : {doneId : rec.get('disct_id')},
					success : function(res, ops) {
						var rs = Ext.decode(res.responseText);
						if (true === rs.success) {
							Alert('操作成功!');
							Ext.getCmp('disctgrid').doint();
						} else {
							Alert('操作失败!');
						}
					}
				});
			})
		}
	}
});

// 折扣
DisctWindow = Ext.extend(Ext.Window, {
	disctGrid : null,
	forAreaId:null,
	constructor : function(v) {
		that=this;
		this.forAreaId = v;
		this.disctGrid = new DisctGrid();
		DisctWindow.superclass.constructor.call(this, {
					title : '折扣',
					layout : 'fit',
					width : 700,
					height : 400,
					closeAction : 'close',
					items : this.disctGrid,
					buttons : [{text : '关闭',scope : this,handler : function() {this.hide();}}]
				});
	},
	loadBasem : function(v) {
//		if(App.data.optr.area_id !='4500'&& this.forAreaId == '4500'){
//    		Ext.getCmp('saveItemId').hide();
//    	}else{
//    		Ext.getCmp('saveItemId').show();
//    	}
		this.disctGrid.loadBasem(v);
	}
});


