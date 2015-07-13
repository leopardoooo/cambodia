var SpecGrid = Ext.extend(Ext.grid.GridPanel,{
	store:null,
	tariffData:null,//所有产品对应的资费信息
	constructor:function(custIds){
		this.store = new Ext.data.JsonStore({
			baseParams : {
				custId : custIds.join(",")
			},
			url : Constant.ROOT_PATH+"/core/x/Acct!queryBaseProdAcct.action",
			fields:['cust_id','cust_name','acct_id','acctitem_id','user_id','prod_id','tariff_id','tariff_name','tariff_rent','disct_id','disct_name',
					'card_id','prod_sn','acctitem_name','fee','fee_date','invalid_date','real_balance','prod_name',
					'next_tariff_id','next_tariff_name','min_pay','rent','billing_type',
					//缴费金额备份字段
					'feeOther','fee_month']
		});
		
		this.store.on('load',this.doLoadResult,this);
		
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		defaults : {
				sortable : false
			},
			columns : [
				{header:'客户名',dataIndex:'cust_name',width:105,renderer:App.qtipValue},
				{header:'智能卡号',dataIndex:'card_id',width:150,renderer:App.qtipValue},
				{header:'账目名称',dataIndex:'prod_name',width:85,renderer:App.qtipValue},
				{header:'当前资费',dataIndex:'tariff_name',width:85,renderer:App.qtipValue},
				{header:'未生效资费',dataIndex:'next_tariff_name',width:85,renderer:App.qtipValue},
				{header:'预计到期日',dataIndex:'invalid_date',width:120,renderer:Ext.util.Format.dateFormat},
				{header:'实时余额',dataIndex:'real_balance',width:75,renderer:Ext.util.Format.formatFee}
			]
        });
		
		SpecGrid.superclass.constructor.call(this,{
			id:'specGridId',
			title:'专用账目缴费',
			store:this.store,
			cm:cm,
			region : 'center',
			border:false,
			view: new Ext.ux.grid.ColumnLockBufferView({}),
			sm:new Ext.grid.RowSelectionModel({})
		});
	},
	initEvents : function(){
		SpecGrid.superclass.initEvents.call(this);
		this.on('afterrender',function(){
			this.swapViews();
		},this,{delay:10})
	},
	swapViews : function(){
		if(this.view.lockedWrap){
			this.view.lockedWrap.dom.style.right = "0px";
		}
        this.view.mainWrap.dom.style.left = "0px"; 
        if(this.view.updateLockedWidth){
        	this.view.updateLockedWidth = this.view.updateLockedWidth.createSequence(function(){ 
	            this.view.mainWrap.dom.style.left = "0px"; 
	        }, this); 
        }
          
	},
	doLoadResult : function(store){
		Ext.getCmp('BusiPanel').hideTip();
		
		Ext.getCmp('userNum').setValue(store.getCount());
		Ext.getCmp('userNum').fireEvent('change',Ext.getCmp('userNum'));
		
		//资费列表 先清空数据
		var tariffNameComp = Ext.getCmp('tariffNameId');
		tariffNameComp.reset();
		var tariffStore = tariffNameComp.getStore();
		tariffStore.removeAll();
		
		
		//tariffArr 产品对应的资费信息，tariffIdArr 资费id，用来过滤相同的资费
		var tariffArr = [],tariffIdArr=[];
		Ext.getCmp('specGridId').getStore().each(function(record){
			var data = record.data;
			if(tariffIdArr.indexOf(data['tariff_id']) == -1){//当前资费不存在则存储
				tariffIdArr.push(data['tariff_id']);
				tariffArr.push([data['tariff_id'],data['tariff_name'],data['tariff_rent']]);
			}
		},this);
		tariffStore.loadData(tariffArr);
		
		//产品对应只有一个资费时，默认选中这个资费，并触发其select事件
		if(tariffStore.getCount()==1){
			tariffNameComp.setValue(tariffStore.getAt(0).get('tariff_id'));
			tariffNameComp.fireEvent('select',tariffNameComp,tariffStore.getAt(0));
		}
	},
	//统一缴费,若选择了折扣，金额必须大于折扣对应的最小数
	//获取编辑表格中缴费列大于零的记录
	getValues:function(){
		var arr = [],data;
		this.store.each(function(record){
			data = record.data;
				var obj = {};
				obj["cust_id"] = data['cust_id'];
				obj["acct_id"] = data['acct_id'];
				obj["acctitem_id"] = data['acctitem_id'];
				obj["user_id"] = data['user_id'];
				obj['card_id'] = data['card_id'];
				obj["prod_sn"] = data['prod_sn'];
				obj["tariff_id"] = data['tariff_id'];
				obj["disct_id"] = data['disct_id'];
				obj["invalid_date"] = data['invalid_date'];
				arr.push(obj);
		},this);
		return arr;
	}
});

SpecForm = Ext.extend(Ext.Panel,{
	rent : null,
	prodStore : null,
	editExpDatePanel : null,
	payPanel : null,
	constructor : function(custIds){
		this.prodStore = new Ext.data.JsonStore({
			autoLoad : true,
			baseParams : {
				custId : custIds.join(",")
			},
			url : Constant.ROOT_PATH+"/core/x/Acct!querySelectableProds.action",
			fields:[{name:'prod_id'},{name:'prod_name'}]
		});
		
		this.payPanel = {
			layout : 'column',
			anchor : '100%',
			defaults : {
				layout : 'form',
				border :false,
				bodyStyle: "background:#F9F9F9",
				columnWidth : .5
			},
			items : [{
				items : [{
					xtype:'combo',
					id:'oldExpDate',
					fieldLabel : '旧预计到期日',
    				store:new Ext.data.JsonStore({
    					fields:[{name:'exp_date',mapping:0}]
    				}),displayField:'exp_date',valueField:'exp_date',emptyText:'请选择预计到期日',
    				editable:true,forceSelection:true,eableKeyEvents:true
    				,listeners:{
    					scope:this,
    					keyup:this.doKeyupExpDate,
    					select:this.doSelectExpDate
    				}
				},{
					xtype : 'displayfield',
					fieldLabel : '应收金额',
					id : 'shouldPay'
				}]
			},{
				items : [{
					xtype : 'numberfield',
					fieldLabel : '缴费月份数',
					id : 'payMonths',
					value : 1,
					minValue : 1,
					allowBlank : false,
					allowDecimals : false,
					allowNegative:false,
					listeners : {
						scope : this,
						change : this.doMonthChange
					}
				},{
					xtype : 'numberfield',
					fieldLabel : '实收金额',
					id : 'realPay',
					allowBlank : false,
					allowNegative:false
				}]
			}]
		};
		
		this.editExpDatePanel = {
			xtype : 'panel',
			layout : 'column',
			anchor : '100%',
			defaults : {
				layout : 'form',
				border :false,
				bodyStyle: "background:#F9F9F9",
				columnWidth : .5
			},
			items : [{
				items : [{
					xtype:'combo',
					id:'oldExpDate',
					fieldLabel : '旧预计到期日',
					allowBlank : false,
    				store:new Ext.data.JsonStore({
    					fields:[{name:'exp_date',mapping:0}]
    				}),displayField:'exp_date',valueField:'exp_date',emptyText:'请选择预计到期日',
    				editable:true,forceSelection:true,eableKeyEvents:true
    				,listeners:{
    					scope:this,
    					keyup:this.doKeyupExpDate,
    					select:this.doSelectExpDate
    				}
				}]
			},{
				items : [{
					xtype : 'datefield',
					width : 120,
					allowBlank : false,
					fieldLabel : '预计到期日',
					format : 'Y-m-d',
					id : 'newExpDate'
				}]
			}]
		};
		
		SpecForm.superclass.constructor.call(this,{
			region : 'south',
			height : 130,
			bodyStyle: Constant.TAB_STYLE,
			layout : 'form',
			defaults : {
				border :false,
				bodyStyle: "background:#F9F9F9"
			},
			items : [{
				id:'tbarCobId',
				xtype:'combo',
				fieldLabel : '账目名称',
    			store: this.prodStore,
    			mode:'local',
    			triggerAction:'all',
    			emptyText:'请选择账目',
    			allowBlank : false,
				displayField:'prod_name',
				valueField:'prod_id',
				editable:true,
				forceSelection:true,
				eableKeyEvents:true,
				selectOnFocus:true
				,listeners:{
					scope:this,
					keyup:this.doKeyupAcctItem,
					select:this.doSelectAcctItem
				}
			},{
				layout : 'column',
				anchor : '100%',
				defaults : {
					layout : 'form',
					border :false,
					bodyStyle: "background:#F9F9F9",
					columnWidth : .5
				},
				items : [{
					items : [{
						xtype:'combo',
						id:'tariffNameId',
						fieldLabel : '资费',
						allowBlank : false,
	    				store:new Ext.data.ArrayStore({
	    					fields:['tariff_id','tariff_name','tariff_rent']
	    				}),displayField:'tariff_name',valueField:'tariff_id',emptyText:'请选择资费',
	    				editable:true,forceSelection:true,eableKeyEvents:true
	    				,listeners:{
	    					scope:this,
	    					keyup:this.doKeyupTariffName,
	    					select:this.doSelectTariffName
	    				}
					}]
				},{
					items : [{
						xtype : 'displayfield',
						id : 'userNum',
						fieldLabel : '户数',
						value : '',
						listeners : {
							scope : this,
							change : this.doUserNumChange
						}
					}]
				}]
				},this.payPanel]
		})
		
	},
	doKeyupAcctItem:function(combo){
		//清空账目选项时，清空相应资费、折扣列表
		if(Ext.isEmpty(combo.getRawValue())){
			Ext.getCmp('specGridId').getStore().removeAll();//清楚所有过滤器
			var tariffNameComp = Ext.getCmp('tariffNameId');
			tariffNameComp.reset();
			var s = tariffNameComp.getStore();
			s.removeAll();
			
			Ext.getCmp('userNum').setValue('');
			Ext.getCmp('userNum').fireEvent('change',Ext.getCmp('userNum'));
		}
	},
	//tbar上账目下拉框select事件
	doSelectAcctItem:function(combo,record){
		var value = combo.getValue();
		var store = Ext.getCmp('specGridId').getStore();
		store.load({
			params : {
				prodId : value
			}
		});
		Ext.getCmp('BusiPanel').showTip();
	},
	//tbar上资费下拉框keyup事件
	doKeyupTariffName:function(combo){
		if(Ext.isEmpty(combo.getRawValue())){
			var store = Ext.getCmp('specGridId').getStore();
			store.clearFilter();
			
			Ext.getCmp('userNum').setValue(store.getCount());
			Ext.getCmp('userNum').fireEvent('change',Ext.getCmp('userNum'));
		}
	},
	//tbar上资费下拉框select事件
	doSelectTariffName:function(combo,record){
		var value = combo.getValue();
		if(value){//当选中'资费名称'项时,重新加载grid数据，清空折扣下拉框
			var store = Ext.getCmp('specGridId').getStore();
			store.filter("tariff_id",value);
			Ext.getCmp('userNum').setValue(store.getCount());
			Ext.getCmp('userNum').fireEvent('change',Ext.getCmp('userNum'));
			
			var rent = Ext.util.Format.formatFee(record.get('tariff_rent'));
			if(rent == 0){
				this.remove(this.items.itemAt(2));
				this.add(this.editExpDatePanel);
				this.doLayout();
				
			}else{
				this.remove(this.items.itemAt(2));
				this.add(this.payPanel);
				
				this.doLayout();
				
				this.rent = rent;
				var count = parseFloat(Ext.getCmp('userNum').getValue());
				var shouldPay = rent * count;
				if(Ext.getCmp('payMonths').getValue()){
					shouldPay = shouldPay * parseFloat(Ext.getCmp('payMonths').getValue());
				}
				Ext.getCmp('shouldPay').setValue(shouldPay);
				Ext.getCmp("realPay").setValue(shouldPay);
			}
			
			var arr = Ext.getCmp('specGridId').getStore().collect('invalid_date',false);//所有到期日集合
			var data = [];
			for(var i=0;i<arr.length;i++){
				data.push([arr[i].substring(0,10)]);
			}
			Ext.getCmp('oldExpDate').getStore().loadData(data);
			
		}
	},
	doKeyupExpDate : function(combo){
		if(Ext.isEmpty(combo.getRawValue())){
			var store = Ext.getCmp('specGridId').getStore();
			store.clearFilter();
			var value = Ext.getCmp('tariffNameId').getValue();
			if(value){
				store.filter("tariff_id",value);
			}
			
			Ext.getCmp('userNum').setValue(store.getCount());
			Ext.getCmp('userNum').fireEvent('change',Ext.getCmp('userNum'));
		}
	},
	doSelectExpDate : function(combo){
		if(combo.getValue()){
			var store = Ext.getCmp('specGridId').getStore();
			var value = Ext.getCmp('tariffNameId').getValue();
			store.filterBy(function(record){
	           return record.get('tariff_id') == value && Ext.util.Format.dateFormat(record.get('invalid_date')) == combo.getValue();
	       	});  
			Ext.getCmp('userNum').setValue(store.getCount());
			Ext.getCmp('userNum').fireEvent('change',Ext.getCmp('userNum'));
			
			if(Ext.getCmp('newExpDate')){
				var date = Date.parseDate(combo.getValue(),'Y-m-d');
				Ext.getCmp('newExpDate').setMinValue(date);
			}
		}
	},
	doMonthChange : function(txt){
		if(this.rent && Ext.getCmp('userNum').getValue()){
			var fee = parseFloat(txt.getValue()) * parseFloat(Ext.getCmp('userNum').getValue()) * this.rent;
			Ext.getCmp('shouldPay').setValue(fee);	
			Ext.getCmp("realPay").setValue(fee);
		}else{
			Ext.getCmp('shouldPay').setValue("");
			Ext.getCmp("realPay").setValue("");
		}
	},
	doUserNumChange : function(txt){
		if(Ext.getCmp('shouldPay')){
			if(this.rent && txt.getValue()){
				var fee = parseFloat(txt.getValue()) * parseFloat(Ext.getCmp('payMonths').getValue()) * this.rent;
				Ext.getCmp('shouldPay').setValue(fee);	
				Ext.getCmp("realPay").setValue(fee);
			}else{
				Ext.getCmp('shouldPay').setValue("");
				Ext.getCmp("realPay").setValue("");
			}
		}
	}
})

SpecPanel = Ext.extend(Ext.Panel,{
	specGrid : null,
	specForm : null,
	constructor : function(){
		var records = App.getApp().main.infoPanel.unitPanel.unitMemberGrid.getSelectionModel().getSelections();
		var custIds = [];
		for(var i=0;i<records.length;i++){
			custIds.push(records[i].get('cust_id'))
		}
		this.specGrid = new SpecGrid(custIds);
		this.specForm = new SpecForm(custIds);
		SpecPanel.superclass.constructor.call(this,{
			layout : 'border',
			border : false,
			items : [this.specGrid,this.specForm]
		})
	},
	getValues : function(){
		return this.specGrid.getValues();
	}
});