/**
 * 资费
 */
ProdPkgGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	packTariffStore:null,
	constructor: function (p,v,ispkg) {
		this.upackStore = new Ext.data.GroupingStore({
			url: root + '/system/Prod!queryPackProdById.action',
			reader: new Ext.data.JsonReader({
				
				fields:[{name:'prod_id',mapping:'prod_id'},
					{name:'prod.prod_name',mapping:'prod.prod_name'},
					{name:'package_id',mapping:'package_id'},
					{name:'tariff_id',mapping:'tariff_id'},
					{name:'max_prod_count',mapping:'max_prod_count'},
					{name:'tariff_name',mapping:'tariff_name'},
					{name:'percent_value',mapping:'percent_value'},
					{name:'type',mapping:'type'},
					{name:'type_text',mapping:'type_text'},
					{name:'package_tariff_id',mapping:'package_tariff_id'}
				]
			}),
			groupField:'type',
			sortInfo:{
				field:'type',direction:'DESC'
			}
        });
        
        this.upackStore.baseParams = {doneId: p,query: v};
        this.upackStore.load();
        
        this.packTariffStore = new Ext.data.JsonStore({
            url: root + '/system/Prod!queryTariffByProdId.action',
            fields: ['tariff_id', 'tariff_name']
        });
        
        this.upackStore.on("load",function(s){
        	s.each(function(record){
	        	if (ispkg == cfgProdType["U"]) {
		        	if(Ext.isEmpty(record.get('percent_value'))){
		        		record.set('percent_value',0);
		        	}
	        	}else{
		        	if(Ext.isEmpty(record.get('max_prod_count'))&& record.get('type') == 'M'){
		        		record.set('max_prod_count',1);
		        	}
	        	}
	        });
        	
        })
		
        var hiddenvalue;
        if (ispkg == cfgProdType["U"]) {
            hiddenvalue = true;
        }
        else {
            hiddenvalue = false;
        }
		ProdPkgGrid.superclass.constructor.call(this,{
			title: '子产品',
            ds: this.upackStore,
            sm: new Ext.grid.CheckboxSelectionModel(),
            height: 280,
            autoScroll: true,
            border: false,
            clicksToEdit: 1,
            columns: [
            	{header:'',dataIndex:'type',hidden:true},
	            {header: '产品名称',dataIndex: 'prod.prod_name',sortable: true,width: 100,renderer: App.qtipValue},
	            {header: '产品资费',dataIndex: 'tariff_name',sortable: true,width: 80,
	                editor: new Ext.form.ComboBox({
	                    store: this.packTariffStore,emptyText: '请选择',
	                    mode: 'local',editable: true,valueField: 'tariff_name',displayField: 'tariff_name'
	                })
	            },
	            {header: '分成值',sortable: true,width: 60,dataIndex: 'percent_value',
	                editor: new Ext.form.NumberField({
	                    allowNegative : false,minValue : 0,value:0
	                })
	            },
	            {header: '最大产品数',dataIndex: 'max_prod_count',width: 80,sortable: true,hidden: hiddenvalue,
	                editor: new Ext.form.NumberField({
	                    minValue: 1,allowBlank: true,allowDecimals: false,allowNegative: false
	                })
	            }
            ]
            ,view: new Ext.grid.GroupingView({
	            forceFit:true,
	            groupTextTpl:'分成类型：{[values.rs[0].data["type_text"]]}'
        	})
		});
		
	},
	  initEvents: function () {
        this.on("beforeedit", function (e) {
            if (e.field == "tariff_name") {
            	if(Ext.getCmp('countyTreeId').getCheckedIds() == ""){
            		Alert("请先选择应用地市!");
            		return false;
            	}
                this.packTariffStore.removeAll();
                this.packTariffStore.baseParams = {doneId:e.record.get("prod_id"),prodCountyIds: Ext.getCmp('countyTreeId').getCheckedIds()};
                this.packTariffStore.load();
                this.packTariffStore.on("load", function (s) {
                    for (var i = 0; i < this.packTariffStore.getCount(); i++) {
                        if (Ext.isEmpty(this.packTariffStore.getAt(i).data.tariff_id)) {
                            this.packTariffStore.removeAt(i);
                        }
                    }
                }, this)
            }
        }, this);
        this.on("afteredit", function (e) {
            if (e.field == "tariff_name") {
                var index = this.packTariffStore.find('tariff_name', e.value);
                if(index > -1){
                	e.record.data.tariff_id = this.packTariffStore.getAt(index).get('tariff_id');
                }else{
                	e.record.data.tariff_id = '';
                }
            }
        }, this);
        ProdPkgGrid.superclass.initEvents.call(this);
    }
});
 
// 资费弹出窗口
TariffWindow = Ext.extend(Ext.Window, {
    tprodId: null,
    forAreaId: null,
    tariffInfoPanel: null,
    ruleForm: null,
    countyTree: null,
    prodPkgGrid:null,
    key: false,
    ispkg:null,
    constructor: function (t, v,ispkg,store, areaId, record) {
    	//t:title,v:prodId,isPkg:是否基本包
        this.tprodId = v;
        this.forAreaId = areaId;
        this.ispkg = ispkg;
        var tariff = null;
        if (!Ext.isEmpty(record)) {
            tariff = record.get('tariff_id');
        }
//        this.prodPkgGrid = new ProdPkgGrid(this.tprodId,tariff,ispkg);
        this.ruleForm = new RuleForm(this);
        this.tariffInfoPanel = new TariffInfoPanel(this, record);
        this.countyTree = new CountyTree(this, areaId, tariff);
        
        TariffWindow.superclass.constructor.call(this, {
            title: t,
            layout: 'border',
            width: 770,
            id: 'tariffwin',
            height: 500,
            closeAction: 'close',
            border: false,
            items: [{
            	region: 'center',
            	border: false,
            	layout:'fit',
            	items:[{
            		layout: 'border',
            		border: false,
            		items:[{
		            	region: 'center',
		            	layout:'border',
		            	border: false,
		            	items:[
		            		{region:'center',layout:'fit',items:[this.tariffInfoPanel]},
		            		{region:'east',animCollapse: true,
					            animate: false,
					            collapseMode: 'mini',
				            	width : "48%",
				            	split : true,
				            	layout:'fit',
				            	items:[this.countyTree]}
		            	]
//		            },{
//		            	id:'pkgGridId',
//		            	region : 'east',
//		            	width:"39%",
//		            	layout : 'fit',
//		            	split : true,
//		            	items : [/*this.prodPkgGrid*/]
		            }]
            	}]
            },{
            	region: 'south',
           		height: 80,
           		id:'ruleFormId',
           		layout:'fit',
           		items:[this.ruleForm]
            }],
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
//        if(ispkg == cfgProdType["P"]){
//        	Ext.getCmp('pkgGridId').hide();
//        	this.width = 500;
//        	this.doLayout();
//        }else{
////        	Ext.getCmp('pkgGridId').add(new ProdPkgGrid(this.tprodId,tariff,ispkg));
//        }
    },
    doSave: function () {
        Ext.getCmp('billingCycle').setDisabled(false);
        var formone = this.tariffInfoPanel.getForm().getValues(),
            tariffall = {},
            prodId = this.tprodId;
       
        if (!this.tariffInfoPanel.getForm().isValid()) {
            return;
        }
        
        if(this.ruleForm.isVisible() && !this.ruleForm.getForm().isValid()){
        	return ;
        }
        
        var prodItemId = Ext.getCmp('countyTreeId').getCheckedIds();
        if (prodItemId.length > 0) {
            tariffall["dictProdIds"] = prodItemId;
        }else {
            Alert("请选择应用地市!");
            return;
        }
        
        if (Ext.getCmp('billingCycle').getValue() == 1) {
            var formtwo = this.ruleForm.getForm().getValues();
            for (var key in formtwo) {
                tariffall["prodTariffDto." + key] = formtwo[key];
            }
        }
        else {
            tariffall["prodTariffDto.day_rent_cal_type"] = "";
            tariffall["prodTariffDto.month_rent_cal_type"] = "";
        }
        for (var key in formone) {
            tariffall["prodTariffDto." + key] = formone[key];
        }
        
/*        if(thiz.ispkg != cfgProdType["P"]){
	        var packdata = [];
			packIds = null;
	        var store = this.prodPkgGrid.getStore();
	        this.prodPkgGrid.stopEditing();
	        
	        var typeArr = store.collect('type');	//分成类型
	        var percent = 0;
        	var flag = false;flagM = false;
        	for(var i=0,len=typeArr.length;i<len;i++){
        		var type = typeArr[i],type_text = '';
		        store.each(function(record){
		        	if(type == record.get('type')){
		        		type_text = record.get('type_text');
		        		if(type == 'M' && Ext.isEmpty(record.get('tariff_id'))){
			        		flag = true;
			        	}
			        	if(type == 'M' && Ext.isEmpty(record.get('max_prod_count'))){
				        	flagM = true;
			        	}
			        	if(type == 'F' && Ext.isEmpty(record.get('max_prod_count'))){
			        		record.set('max_prod_count',null);
			        	}
			        	if(Ext.isEmpty(record.get('percent_value'))){
			        		record.set('percent_value',0);
			        	}
				        percent += record.get('percent_value');
		        	}
		        });
		        if(thiz.ispkg == cfgProdType["U"]){
//				    if(Ext.getCmp('billingType').getValue() != 'BY'){
				        if(flag){
				        	Alert('<b>'+type_text+'</b>分成类型中子产品未选择资费!'); 
				        	return;
				        }
//			        }
		        }
		        if(flagM && thiz.ispkg == cfgProdType["C"]){
		        	Alert('<b>'+type_text+'</b>分成类型中子产品未选择最大产品数!'); 
				    return;
		        }
		        //市场分成必输，财务的可输可不输
		        if(type == 'M' && percent != Ext.getCmp('rent').getValue()){
		        	Alert('<b>'+type_text+'</b>类型中子产品分成值的和与套餐租费不一致!');
		        	return;
		        } else if(type == 'F' && percent > 0 && percent != Ext.getCmp('rent').getValue()){
		        	Alert('<b>'+type_text+'</b>类型中子产品分成值的和与套餐租费不一致!');
		        	return;
		        }
//		        flag = false;
		        percent = 0;
        	}
        	
        	var fince_percent = 0;
        	
        	store.each(function(record){
        		if(Ext.isEmpty(record.get('tariff_id'))){
	        		flag = true;
	        	}
        		if(record.get('type') == 'F'){
        			fince_percent += record.get('percent_value');
        		}
        	});
        	store.each(function(record){
        		var type = record.get('type');
        		if(type == 'M'){
    				packdata.push(record.data);
        		}else if(type == 'F' && fince_percent >0){
        			packdata.push(record.data);
        		}
        	});
	        
	        store.each(function(record){
	        	if(Ext.isEmpty(record.get('tariff_id'))){
	        		flag = true;
	        	}
	        	if(Ext.isEmpty(record.get('percent_value'))){
	        		record.set('percent_value',0);
	        	}
		        percent += record.get('percent_value');
	        	packdata.push(record.data);
	        },this)
	        
	        if(thiz.ispkg == cfgProdType["U"]){
			    if(Ext.getCmp('billingType').getValue() != 'BY'){
			        if(flag){Alert('子产品未选择资费!'); return;};
		        }
	        }
	        if(percent != Ext.getCmp('rent').getValue()){Alert('子产品分成值的和与套餐租费不一致!');return;};
	        var packrecords = Ext.encode(packdata);
			tariffall["packList"] = packrecords;
        }*/
        tariffall["prodTariffDto.rent"] =Ext.util.Format.formatToFen(Ext.getCmp('rent').getValue());
        tariffall["prodTariffDto.prod_id"] = this.tprodId;
        tariffall["prodTariffDto.for_area_id"] = this.forAreaId;
        var store = Ext.getCmp('tariffGrid').getStore(); key=false;
       
        store.each(function(record){
       		if(record.get('rent')==tariffall["prodTariffDto.rent"]&&record.get('billing_cycle')==tariffall["prodTariffDto.billing_cycle"]
       		&&record.get('billing_type')==tariffall["prodTariffDto.billing_type"]&&record.get('tariff_type')==tariffall["prodTariffDto.tariff_type"]
       		&& ((!tariffall["prodTariffDto.rule_id"] && !record.get('rule_id')) || (record.get('rule_id') == tariffall["prodTariffDto.rule_id"]))){
   				key =true;
       		} 
        })
		if(key){
			Alert('已经存在该资费!');return;			
		}
		
		
		mask = new Ext.LoadMask(this.body, {
			msg:"正在处理，请稍等..."
		});
		mask.show();
		
        Ext.Ajax.request({
            url: root + '/system/Prod!saveTariff.action',
            params: tariffall,
            scope: this,
            success: function (res, ops) {
            	mask.hide();
                var rs = Ext.decode(res.responseText);
                if (true === rs.success) {
                    Alert('操作成功!', function () {
                    	Ext.getCmp('prodTreeId').queryAll(this.tprodId);
                        this.close();
                    }, this);
                }
                else {
                    Alert('操作失败!');
                }
            },
            clearData: function(){
            	mask.hide();
            }
        });
    },
    initComponent: function () {
        TariffWindow.superclass.initComponent.call(this);
        // 初始化下拉框的参数
        var comboes = this.findByType("paramcombo");
        App.form.initComboData(comboes, this.loadRecord(),this);
    },
    loadRecord : function(){

    },
    changeFrom: function (v) {
        if (v == '1') {
            this.key = true;
            Ext.getCmp('ruleform').loadData(this.key);
            Ext.getCmp('ruleFormId').show();
            this.doLayout();
        }
        else {
            Ext.getCmp('dayrentrule').allowBlank = true;
            Ext.getCmp('monthrentrule').allowBlank = true;
        	Ext.getCmp('ruleFormId').hide();
            this.doLayout();
        }
    },
	//string:原始字符串,substr:子字符串,isIgnoreCase:忽略大小写
	contains:function(string,substr){
	     var startChar=substr.substring(0,1);
	     var strLen=substr.length;
	         for(var j=0;j<string.length-strLen+1;j++)
	         {
	             if(string.charAt(j)==startChar)//如果匹配起始字符,开始查找
	             {
	                   if(string.substring(j,j+strLen)==substr)//如果从j开始的字符与str匹配，那ok
	                   {
	                         return true;
	                   }  
	             }
	         }
	         return false;
	}
})
RuleForm = Ext.extend(Ext.form.FormPanel, {
    rentRuleStore: null,
    constructor: function () {
        this.rentRuleStore = new Ext.data.JsonStore({
            url: root + '/system/Index!queryRentRule.action',
            fields: ['rule_id', 'rule_name']
        })
        
        this.rentRuleStore.on("load",function(s){
        	if(Ext.getCmp('dayrentrule')){
	        	Ext.getCmp('dayrentrule').setValue('AT');
	        	Ext.getCmp('monthrentrule').setValue('AT');
        	}
        });       
        RuleForm.superclass.constructor.call(this, {
            layout: 'border',
            id: 'ruleform',
            border: false,
            title: "规则信息",
            items: [
                {
                region: 'center',
                border: false,
                layout: 'column',
                baseCls: 'x-plain',
                bodyStyle: 'padding: 10px',
                defaults: {
                    baseCls: 'x-plain',
                    columnWidth: 0.5,
                    layout: 'form',
                    defaultType: 'textfield',
                    labelWidth: 75
                },
                items: [
                    {
                    items: [
                        {
                        fieldLabel: '日租规则',
                        allowBlank : false,
                        xtype: 'combo',
                        id: 'dayrentrule',
                        store: this.rentRuleStore,
                        emptyText: '请选择',
                        mode: 'local',
                        editable: false,
                        valueField: 'rule_id',
                        displayField: 'rule_name',
                        hiddenName: 'day_rent_cal_type'}
                        ]},
                {
                    items: [
                        {
                        fieldLabel: '月租规则',
                        xtype: 'combo',
                        allowBlank : false,
                        id: 'monthrentrule',
                        store: this.rentRuleStore,
                        emptyText: '请选择',
                        mode: 'local',
                        editable : true,
                        forceSelection:true,
						selectOnFocus:true,
						triggerAction:'all',
                        valueField: 'rule_id',
                        displayField: 'rule_name',
                        hiddenName: 'month_rent_cal_type'}
                        ]}]}]
        })
    },
    changeData: function (r, key) {
        this.rentRuleStore.load();
        if(Ext.getCmp('dayrentrule')){
        	Ext.getCmp('dayrentrule').setDisabled(true);
	        Ext.getCmp('monthrentrule').setDisabled(true);
        }
    },
    loadData: function (key) {
        this.rentRuleStore.load();
    },
    addNull: function (store) {
        store.on("load", function (s) {
            store.insert(0, new Ext.data.Record({
                rule_name: '无规则...',
                rule_id: ''
            }));
        });
    }
});
CountyTree = Ext.extend(Ext.tree.TreePanel, {
    prodId: null,
    tariffId : null,
    constructor: function (v, areaId, tariffId) {
    	this.tariffId = tariffId;
        var loader = new Ext.tree.TreeLoader({
            url: root + "/system/Prod!getCountyTree.action?doneId=" + areaId + "&query=" + tariffId
        });
        CountyTree.superclass.constructor.call(this, {
            split: true,
            title: '应用地市',
            id: 'countyTreeId',
            margins: '0 0 3 2',
            lines: false,
            autoScroll: true,
            animCollapse: true,
            border: false,
            animate: false,
            collapseMode: 'mini',
            bodyStyle: 'padding:3px',
            loader: loader,
            root: {
                id: '-1',
                iconCls: 'x-tree-root-icon',
                checked : false,
                text: '地市结构'
            }
        });
        this.expandAll();
    },
    initEvents: function(){
		this.on("afterrender",function(){
			//如果不是修改
//	        if(!this.tariffId){
//	        	if(App.data.optr['county_id'] != '4501'){
	        		var node = this.getRootNode();
	        		node.attributes.checked = true;
	        		node.ui.toggleCheck(true);
	        		node.fireEvent('checkchange', node, true);
//	        	}
//	        }
		},this,{delay:500});
		
		CountyTree.superclass.initEvents.call(this);
	},
    listeners: {
        'checkchange': function (node, checked) {
            node.expand();
            node.attributes.checked = checked;
            node.eachChild(function (child) {
                child.ui.toggleCheck(checked);
                child.attributes.checked = checked;
                child.fireEvent('checkchange', child, checked);
            });
        }
    },getCheckedIds : function(){
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
TariffInfoPanel = Ext.extend(Ext.form.FormPanel, {
    parent: null,
    busiRuleStore: null,
    key: false,
    record: null,
    // 选中记录
    constructor: function (p, record) {
        this.parent = p;
        this.record = record;
        this.busiRuleStore = new Ext.data.JsonStore({
            url: root + '/system/Index!queryBusiRule.action',
            fields: ['rule_id', 'rule_name']
        })
       
        TariffInfoPanel.superclass.constructor.call(this, {
            title: "基础信息",
            id: 'tariffFormPanel',
            layout: 'form',
            labelAlign: "right",
            border: false,
            bodyStyle: 'padding-top: 10px',
            labelWidth: 75,
            defaults: {
                disabled : this.record ? true : false
            },
            items: [{
                fieldLabel: '资费名称',
                name: 'tariff_name',
                allowBlank: false,
                xtype: 'textfield',
                width: 120,
                disabled : false,
                blankText: '你没填写资费名称'
            },{
	            fieldLabel: '资费类型',
	            hiddenName: 'tariff_type',
	            allowBlank: false,
	            xtype: 'paramcombo',
	            paramName: 'TARIFF_TYPE',
	            width: 120
	        },{
                fieldLabel: '计费周期',
                id: 'billingCycle',
                width: 120,
                allowBlank: false,
                allowNegative: false,
                allowDecimals: false,
                xtype: 'numberfield',
                value: '1',
                name: 'billing_cycle',
                listeners: {
                    scope: this,
                    change: this.getFormByValue
                }
            },{
                fieldLabel: '计费方式',
                xtype: 'paramcombo',
                id: 'billingType',
                paramName: 'BILLING_TYPE',
                allowBlank: false,
                defaultValue: 'BY',
                width: 120,
                hiddenName: 'billing_type',
                listeners: {
                    scope: this,
                    'select': function (combo, record, index) {
                        if (combo.getValue() != 'BY') {
                            Ext.getCmp('billingCycle').setValue('1');
                            this.parent.changeFrom(1);
                            Ext.getCmp('billingCycle').setDisabled(true);
                        }
                        else {
                            Ext.getCmp('billingCycle').setDisabled(false);
                        }
                    }
                }
            },{
                fieldLabel: '租费(元)',
                width: 120,
                id:'rent',
                allowBlank: false,
                xtype: 'numberfield',
                miniValue : 0,
                name: 'rent'
            }, {
				xtype : 'datefield',
				editable : false,
				fieldLabel : '生效日期',
				vtype : 'daterange',
				endDateField : 'enddt',
				name : 'eff_date',
				id : 'startdt',
				format : 'Y-m-d',
				minText : '不能选择当日之前',
				allowBlank : false,
				value : new Date(),
				disabled : false,
				minValue : new Date().format('Y-m-d')
			}, {
				xtype : 'datefield',
				editable : false,
				vtype : 'daterange',
				id : 'enddt',
				startDateField : 'startdt',
				fieldLabel : '失效日期',
				name : 'exp_date',
				format : 'Y-m-d',
				disabled : false,
				minValue : new Date().format('Y-m-d')
			},{
				xtype: 'textfield',
				fieldLabel: '协议编号',
				name: 'spkg_sn',
				disabled : false
			},{
				fieldLabel: '服务渠道',
				xtype:'paramlovcombo',paramName:'SERVICE_CHANNEL',
				hiddenName:'service_channel',
	        	disabled : false,allowBlankItem:true,listWidth:200    
	        },{
                fieldLabel: '业务规则',
                xtype: 'combo',
                store: this.busiRuleStore,
                emptyText: '请选择',
                mode: 'local',
                editable: true,
                forceSelection:true,
				selectOnFocus:true,
				triggerAction:'all',
                disabled : false,
                valueField: 'rule_id',
                displayField: 'rule_name',
                listWidth:200,
                hiddenName: 'rule_id'},
            {
                fieldLabel: '资费描述',
                name: 'tariff_desc',
                xtype: 'textarea',
                width: 120,
                disabled : false,
                height: 40},
            {
                xtype: 'hidden',
                disabled : false,
                name: 'prod_id'},
            {
                xtype: 'hidden',
                disabled : false,
                name: 'tariff_id'}]
        })
    },
    initComponent: function () {
        TariffInfoPanel.superclass.initComponent.call(this);
        App.form.initComboData(this.findByType("paramlovcombo"));
        App.form.initComboData(this.findByType("paramcombo"), this.doInit, this);
    },
    getFormByValue: function (c) {
        this.parent.changeFrom(c.value);
        
    },
    doInit: function () {
    	this.busiRuleStore.load();
        this.busiRuleStore.on("load", function (s) {
            s.insert(0, new Ext.data.Record({
                rule_name: '无规则...',
                rule_id: ''
            }));
            
            if (this.record) {
            	var data = this.record.data;
        		var prodInfo = {};
				for (var prop in data) {
					prodInfo[prop] = data[prop];
				}
				prodInfo['eff_date'] = Date.parseDate(prodInfo["eff_date"],
						'Y-m-d h:i:s');
				prodInfo['exp_date'] = Date.parseDate(prodInfo["exp_date"],
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
	        this.changeData();
        },this);
    },
    changeData: function () {
        if (this.record) {
        	var cmp = Ext.getCmp('rent');
			cmp.setValue(Ext.util.Format.formatFee(cmp.getValue()));
            if (Ext.getCmp('billingCycle').getValue() != '1') {
                this.parent.changeFrom(Ext.getCmp('billingCycle').getValue());
            }
            else if (Ext.getCmp('billingCycle').getValue() == 1 && Ext.getCmp('billingType').getValue() == 'BY') {
                Ext.getCmp('ruleform').changeData(this.record, this.key);
            }
            else {
                Ext.getCmp('billingCycle').setDisabled(true);
                Ext.getCmp('ruleform').changeData(this.record, this.key);
            }
        }
    },
    loadData: function () {
        var comboes = this.findByType("paramcombo");
        App.form.initComboData(comboes);
        Ext.getCmp('ruleform').loadData(this.key);
        this.key = true;
    }
});

var LowestPriceGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	priceStore:null,
	prodId:null,
	constructor:function(parent,prodId){
		this.parent = parent;
		this.priceStore = new Ext.data.JsonStore({
			url: root + '/system/Prod!queryLowestPrice.action',
			baseParams : {doneId:prodId},
			fields:['prod_id','county_id','price','area_price','county_price',
				'county_name']
		});
		this.priceStore.load();
		this.prodId = prodId;
		var cm = new Ext.grid.ColumnModel([
			{header:'适用地区',dataIndex:'county_name',width:150},
			{id:'priceId',header:'省级定价(元)',dataIndex:'price',editor:new Ext.form.NumberField({allowNegative : false,allowDecimals : false,minValue : 0})},
			{id:'areaPriceId',header:'地区定价(元)',dataIndex:'area_price',editor:new Ext.form.NumberField({allowNegative : false,allowDecimals : false,minValue : 0})},
			{id:'countyPriceId',header:'地市定价(元)',dataIndex:'county_price',editor:new Ext.form.NumberField({allowNegative : false,allowDecimals : false,minValue : 0})}
		]);
		cm.isCellEditable = this.cellEditable;
		LowestPriceGrid.superclass.constructor.call(this,{
			id:'lowestPriceGrId',
			ds:this.priceStore,
			region : 'center',
			clicksToEdit:1,
			cm:cm,
			sm:new Ext.grid.RowSelectionModel({}),
			tbar:[
				'-',
				{text:'保存',iconCls:'icon-save',handler:this.doSave,scope:this},'-'
			]
		});
	},
	initEvents:function(){
		LowestPriceGrid.superclass.initEvents.call(this);
	},
	initComponent:function(){
		LowestPriceGrid.superclass.initComponent.call(this);
	},
	doSave:function(){
		this.stopEditing();// 停止编辑
		Confirm("确定保存吗?", this ,function(){
			mb = Show();//显示正在提交
			
			var records = [];
			for(var i=0;i<this.getStore().getCount();i++){
				var data = this.getStore().getAt(i).data;
				data.price = data.price == null?null:Ext.util.Format.formatToFen(data.price);
				data.area_price = data.area_price == null?null:Ext.util.Format.formatToFen(data.area_price);
				data.county_price = data.county_price == null?null:Ext.util.Format.formatToFen(data.county_price);
				records.push(data);
			}
			Ext.Ajax.request({
				scope : this,
				url: root + '/system/Prod!saveLowestPrice.action',
				params : {
					doneId : this.prodId,
					records : Ext.encode(records)
				},
				success : function( res,ops ){
					mb.hide();//隐藏提示框
					mb = null;
					var rs = Ext.decode(res.responseText);
					if(true === rs.success){
						Alert('操作成功!');
						this.getStore().reload();
						this.parent.close();
					}else{
						Alert('操作失败');
			 		}
				}
			})
		});
	},
	cellEditable:function(colIndex, rowIndex){
		var priceIdIndex = this.getIndexById('priceId');
		var aPriceIdIndex = this.getIndexById('areaPriceId');
		var cPriceIdIndex = this.getIndexById('countyPriceId');
		var count = this.getStore().getCount();
		if(count!=rowIndex){
			if(colIndex === priceIdIndex){
				//省级定价,只有4501能改
				if(App.data.optr['county_id']!='4501'){
					return false;
				}
			}
			if(colIndex === aPriceIdIndex){
				//地区定价，地区权限的或者省级能改
				if(count==1){
					return false;
				}
			}
//			if(colIndex === cPriceIdIndex){
//			}
		}
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	}
});

LowestPriceWindow = Ext.extend(Ext.Window, {
    lowestPriceGrid: null,
    constructor: function (p) {
        this.lowestPriceGrid = new LowestPriceGrid(this,p);
        LowestPriceWindow.superclass.constructor.call(this, {
            title: '最低定价配置',
            width: 600,
            id: 'lowestPricewin',
            height: 500,
            layout : 'fit',
            closeAction: 'close',
            border: false,
            items: this.lowestPriceGrid
        })
    }
})

TariffGrid = Ext.extend(Ext.grid.GridPanel, {
    tariffStore: null,
    pkgStore:null,
    tprodId: null,
    prodServId : null,//产品服务类型
    ispkg:null,
    forAreaId: null,
    editTariff:false,//修改资费
    constructor: function () {
    	
    	//修改资费的权限
		for(var i=0;i<App.subMenu.length;i++){
			if(App.subMenu[i]['handler'] == 'EditTariff'){
				this.editTariff = true;
				break;
			}
		}
    	
        this.tariffStore = new Ext.data.JsonStore({
            	fields: ['tariff_name','billing_type_text','billing_cycle','rent','status_text','tariff_desc','prod_id','tariff_id','billing_type',
            	'month_rent_cal_type','day_rent_cal_type','use_fee_rule','bill_rule','month_rent_cal_type_text','day_rent_cal_type_text',
            	'use_fee_rule_text','bill_rule_text','rule_id_text','rule_id','status','status_text','optr_id','county_id','countyList',
            	'tariff_type','tariff_type_text','area_id','eff_date','exp_date','service_channel','service_channel_text','spkg_sn'],
            	sortInfo: {field: 'status_text', direction: 'DESC'}
        });
        var tariffsm = new Ext.grid.CheckboxSelectionModel();
        var columns = [
            {
            header: '资费名称',
            dataIndex: 'tariff_name',
            sortable: true,
            width: 80,
            renderer: App.qtipValue},
        {
            header: '状态',
            sortable: true,
            width: 60,
            dataIndex: 'status_text',
            renderer:Ext.util.Format.statusShow},
        {
            header: '资费类型',
            sortable: true,
            width: 60,
            dataIndex: 'tariff_type_text'},
        {
            header: '计费方式',
            sortable: true,
            width: 60,
            dataIndex: 'billing_type_text'},
        {
            header: '计费周期',
            sortable: true,
            width: 60,
            dataIndex: 'billing_cycle'},
        {
            header: '费用(元)',
            sortable: true,
            dataIndex: 'rent',
            width: 60,
            renderer : Ext.util.Format.formatFee
            },
        {
        	header: '协议编号',
        	dataIndex: 'spkg_sn'
        },
        {
            header: '业务规则',
            sortable: true,
            width: 60,
            renderer: App.qtipValue,
            dataIndex: 'rule_id_text'},
        {header:'生效时间',dataIndex:'eff_date',sortable: true,width:80,renderer:Ext.util.Format.dateFormat},
		{header:'失效时间',dataIndex:'exp_date',sortable: true,width:80,renderer:Ext.util.Format.dateFormat},
		{header:'服务渠道',dataIndex:'service_channel_text',sortable: true,width:100,renderer:App.qtipValue},
        {
            header: '操作',
            width: 140,
            scope:this,
	        renderer:function(value,meta,record,rowIndex,columnIndex,store){
	            	var btns = this.doFilterBtns(record);
	            	return btns;
            }
        },{
            header: '日租规则',
            sortable: true,
            width: 60,
            renderer: App.qtipValue,
            dataIndex: 'day_rent_cal_type_text'},
        {
            header: '月租规则',
            sortable: true,
            width: 60,
            renderer: App.qtipValue,
            dataIndex: 'month_rent_cal_type_text'}
        
            ];
        TariffGrid.superclass.constructor.call(this, {
            ds: this.tariffStore,
            height: 300,
            id: 'tariffGrid',
            columns:columns,
            sm: tariffsm,
            border : false,
            clicksToEdit: 1,
            tbar: ['-',
            {
                text: '新增资费',
                scope: this,
                iconCls : 'icon-add',
                id: 'addTariff',
                handler: this.openTariff}
//                , '-',{
//                text: '最低定价配置',
//                scope: this,
//                id: 'addLowestPrice',
//                iconCls : 'icon-add',
//                handler: this.openLowestPrice}
                ]
        })
    },
    doFilterBtns : function(record){
		var btns = "";
		//如果是省公司操作员，或者该资费的创建者是操作员本身，或者资费只为操作员所在地区创建，且操作员具有修改权限
		if(App.data.optr['county_id'] =='4501'
			|| record.get('optr_id') == App.data.optr['optr_id']
			|| App.data.optr['county_id'] == record.get('area_id')
            || (this.editTariff && record.get('countyList').length == 1 
            		&& record.get('countyList')[0] == App.data.optr['county_id'])){
            		if(record.get('status') == 'ACTIVE'){
            			btns = btns + "&nbsp;<a href='#' onclick=Ext.getCmp('tariffGrid').UpDateTariff() style='color:blue'>修改</a>" 
            			+ "&nbsp;<a href='#' onclick=Ext.getCmp('tariffGrid').deleteTariff() style='color:blue'>禁用</a>" 
            			// + "&nbsp;<a href='#' onclick=Ext.getCmp('tariffGrid').ShowDisct() style='color:blue'>折扣</a>"
            		}else{
            			btns = btns + "&nbsp;<a href='#' onclick=Ext.getCmp('tariffGrid').UpToUseTariff() style='color:blue'>启用</a>"
            		}
            		
		}
		return btns;
    },
    openLowestPrice : function(){
   	 	if (Ext.isEmpty(this.tprodId)) {
            Alert("请先选择产品!");
            return false
        };
        var win = Ext.getCmp('lowestPricewin');
		if(!win){
			win = new LowestPriceWindow(this.tprodId);
		}
		win.show();

        
    
    },
    openTariff: function () {
        if (Ext.isEmpty(this.tprodId)) {
            Alert("请先选择产品!");
            return false
        };
//        if(this.tariffStore.getCount()>0&&this.ispkg != cfgProdType["P"]){
//        	Ext.getCmp('addTariff').hide();
//        	Alert("套餐产品不能多资费!");
//        	return false;
//        }
        new TariffWindow("增加资费", this.tprodId,this.ispkg,this.pkgStore, this.forAreaId,null).show();
        Ext.getCmp('tariffFormPanel').loadData();
    },
    loadBasem: function (s) {
        this.tariffStore.loadData(s);
    },
    UpDateTariff: function () {
        var grid = Ext.getCmp('tariffGrid');
        var record = grid.getSelectionModel().getSelected();
        new TariffWindow("修改资费", this.tprodId,this.ispkg,this.pkgStore, this.forAreaId, record).show();
    },
    UpToUseTariff :function(){
    	var grid = Ext.getCmp('tariffGrid');
        var record = grid.getSelectionModel().getSelected();
        new TariffWindow("启用资费", this.tprodId,this.ispkg,this.pkgStore, this.forAreaId, record).show();
    },
    ShowDisct: function () {
    	var grid = Ext.getCmp('tariffGrid');
        var record = grid.getSelectionModel().getSelected();
        this.disctWindow = new DisctWindow(this.forAreaId);
        this.disctWindow.loadBasem(record);
        this.disctWindow.show();
    },
    deleteTariff: function () {
    	    var grid = Ext.getCmp('tariffGrid');
        	var record = grid.getSelectionModel().getSelected();
    		var tariffId = record.get('tariff_id');
            Confirm("确定禁用该资费吗?", this, function () {
                Ext.Ajax.request({
                    url: root + '/system/Prod!deleteTariff.action',
                    params: {
                        doneId: tariffId
                    },
                    scope: this,
                    success: function (res, ops) {
                        var rs = Ext.decode(res.responseText);
                        if (true === rs.success) {
                            Alert('操作成功!', function () {
		                    	Ext.getCmp('prodTreeId').queryAll(this.tprodId);
								Ext.getCmp('updateprodIdItems').setDisabled(false);
		                    }, this);
                        }
                        else {
                            Alert('操作失败!');
                        }
                    }
                });
            })
    }
})