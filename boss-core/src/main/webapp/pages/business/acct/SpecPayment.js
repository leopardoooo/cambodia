var SpecGrid = Ext.extend(Ext.grid.GridPanel,{
	store:null,
	dataArr:null,
	tariffData:null,//所有产品对应的资费信息
	parent:null,
	superParent:null,
	constructor : function(data,superParent,parent){
		this.superParent = superParent;
		this.parent = parent;
		this.dataArr = data;
		this.store = new Ext.data.JsonStore({
			fields:['acct_id','acctitem_id','user_id','prod_id','tariff_id','tariff_name','tariff_rent','disct_id','disct_name',
					'user_name','user_type_text','stb_id','card_id','modem_mac','prod_sn','acctitem_name',
					'real_bill','status','fee','fee_date','invalid_date','real_balance','acct_type_text',
					'next_tariff_id','next_tariff_name','prod_status','prod_status_text','min_pay','rent','billing_type',
					//缴费金额备份字段
					'feeOther',
					//自定义字段
					'billing_cycle','fee_month','serv_id']
		});
		if(this.dataArr)
			this.store.loadData(this.dataArr);
		
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		defaults : {
				sortable : false
			},
			columns : [
				{header:'用户名',dataIndex:'user_name',width:55,renderer:App.qtipValue},
				{header:'用户类型',dataIndex:'user_type_text',width:55,renderer:App.qtipValue},
				{header:'智能卡号',dataIndex:'card_id',width:110,renderer:App.qtipValue},
				{header:'账目名称',dataIndex:'acctitem_name',width:75,renderer:App.qtipValue},
				{header:'当前资费',dataIndex:'tariff_name',width:65,renderer:App.qtipValue},
				{header:'未生效资费',dataIndex:'next_tariff_name',width:70,renderer:App.qtipValue},
				{header:'状态',dataIndex:'prod_status_text',width:55,renderer:Ext.util.Format.statusShow},
				{header:'预计到期日',dataIndex:'invalid_date',width:110,renderer:Ext.util.Format.dateFormat},
				{header:'实时余额',dataIndex:'real_balance',width:55,renderer:Ext.util.Format.formatFee},
				{header:'折扣',dataIndex:'disct_name',width:60,renderer:App.qtipValue},
				{header:'缴费数额',dataIndex:'fee',hidden:true}
//				,{header:'缴费金额',dataIndex:'fee',width:60,renderer:App.qtipValue}
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
	initEvents: function(){
		SpecGrid.superclass.initEvents.call(this);
		this.on("afterrender",function(){
			this.swapViews();
		},this,{delay:10});
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
	//统一缴费,若选择了折扣，金额必须大于折扣对应的最小数
	//获取编辑表格中缴费列大于零的记录
	getValues:function(){
		var arr = [],data;
		this.store.each(function(record){
			data = record.data;
//			if (data['fee'] && data['fee'] >0){
				var obj = {};
				obj["acct_id"] = data['acct_id'];
				obj["acctitem_id"] = data['acctitem_id'];
				obj["user_id"] = data['user_id'];
				obj["prod_sn"] = data['prod_sn'];
				obj["tariff_id"] = data['tariff_id'];
				obj["disct_id"] = data['disct_id'];
				obj["invalid_date"] = data['invalid_date'];
				obj["fee"] = Ext.isEmpty(data['fee'])?0:data['fee'];
				arr.push(obj);
//			}
		},this);
		return arr;
	}
});

SpecForm = Ext.extend(Ext.Panel,{
	dataArr : null,
	tariffData :null,
	rent : null,
	parent:null,
	superParent:null,
	constructor : function(data,superParent,parent){
		this.superParent = superParent;
		this.parent = parent;
		this.dataArr = data;
		this.doInit();
		var minDateValue = new Date();
		SpecForm.superclass.constructor.call(this,{
			region : 'south',
			height : 130,
			bodyStyle: Constant.TAB_STYLE,
			layout : 'column',
			defaults : {
				layout : 'form',
				border :false,
				bodyStyle: "background:#F9F9F9",
				columnWidth : .5
			},
			items : [{
				items : [{
					id:'tbarCobId',
					xtype:'combo',
					fieldLabel : '账目名称',
        			store:new Ext.data.JsonStore({
        				fields:[{name:'acctitem_name',mapping:0},{name:'acctitem_name_value',mapping:1}]
        			}),
        			mode:'local',
        			triggerAction:'all',
        			emptyText:'请选择账目',
        			allowBlank : false,
    				displayField:'acctitem_name',
    				valueField:'acctitem_name_value',
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
					xtype:'combo',
					id:'oldExpDateId',
					fieldLabel : '原到期日',
					allowBlank : false,
    				store:new Ext.data.ArrayStore({
    					fields:[{name:'exp_date',mapping:0}]
    				}),displayField:'exp_date',valueField:'exp_date',emptyText:'请选择预计到期日',
    				editable:true,forceSelection:true,eableKeyEvents:true
    				,listeners:{
    					scope:this,
    					keyup:this.doKeyupExpDate,
    					select:this.doSelectExpDate
    				}
				}
				,{
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
				}
				,{
					xtype : 'displayfield',
					fieldLabel : '应收金额',
					id : 'shouldPay'
				}
				]
			},{
				items : [{
					xtype : 'displayfield',
					id : 'userNum',
					fieldLabel : '户数',
					value : '',
					listeners : {
						scope : this,
						change : this.calcShouldPay
					}
				}
				,{
					xtype : 'datefield',
					fieldLabel : '到期日',
					id : 'newInvalidDate',
					width:130,
					format:'Y-m-d',
					value:minDateValue.add(Date.DAY,1),
					minValue : minDateValue,
					listeners : {
						scope : this,
						change : this.calcShouldPay
					}
				}
//				,{
//					xtype:'combo',
//					fieldLabel : '折扣',
//					id:'disctName_id',
//					hiddenName:'disct_name',
//    				store:new Ext.data.JsonStore({
//    					fields:['disct_id','disct_name','min_pay','tariff_id']
//    				}),displayField:'disct_name',valueField:'disct_id',emptyText:'请选择折扣',
//    				editable:true,forceSelection:true,enableKeyEvents:true
//    				,listeners:{
//    					scope:this,
//    					keyup:this.doKeyupDisct,
//    					select:this.doSelectDisct
//    				}
//				}
				,{
					xtype : 'numberfield',
					fieldLabel : '实收金额',
					id : 'realPay',
					allowBlank : false,
					allowNegative:false,
					validate:function(field){
						var realPay = this.getValue();
						var shouldPay = 0;
						var spCmp = Ext.getCmp('shouldPay');
						if(!spCmp){
							return true;
						}
						shouldPay = parseInt(spCmp.getValue());
						if(shouldPay >= realPay){
							this.clearInvalid();
							return true;
						}else{
							this.markInvalid('实收金额 应小于等于 应收金额');
						}
						return false;
					}
				} 
				]
			},{
					items : [{
						xtype : 'hidden',
						id : 'servId',
						value : '' 
					}]
			}]
		})
		
		var ainArr = Ext.getCmp('specGridId').getStore().collect('acctitem_name',false);//所有账目名集合
		var arr = [];
		for(var i=0;i<ainArr.length;i++){
			arr.push([ainArr[i],ainArr[i]]);
		}
		Ext.getCmp('tbarCobId').getStore().loadData(arr);
	},
	doInit:function(){
	},
	doInitExpDateComboData:function(){
		var store = this.parent.specGrid.store;
//		this.parent.specGrid.store.filter("prod_name",combo.getValue());
		
		var arr = store.collect('invalid_date',false);//所有到期日集合
		var data = [];
		for(var i=0;i<arr.length;i++){
			data.push([arr[i].substring(0,10)]);
		}
		Ext.getCmp('oldExpDateId').getStore().loadData(data);
		
	},
	doKeyupAcctItem:function(combo){
		//清空账目选项时，清空相应资费、折扣列表
		if(Ext.isEmpty(combo.getRawValue())){
			Ext.getCmp('specGridId').getStore().clearFilter();//清楚所有过滤器
			var tariffNameComp = Ext.getCmp('tariffNameId');
			tariffNameComp.reset();
			var s = tariffNameComp.getStore();
			s.removeAll();
//			var disctComp = Ext.getCmp('disctName_id');
//			disctComp.reset();
//			disctComp.getStore().removeAll();
			
			Ext.getCmp('userNum').setValue('');
			Ext.getCmp('userNum').fireEvent('change',Ext.getCmp('userNum'));
		}
	},
	//tbar上账目下拉框select事件
	doSelectAcctItem:function(combo,record){
		var value = combo.getValue();
		var store = Ext.getCmp('specGridId').getStore();
		if(value){
			//过滤掉其他账目,只显示用户选择的账目列表
			store.filter("acctitem_name",value);
		}
		Ext.getCmp('userNum').setValue(store.getCount());
		Ext.getCmp('userNum').fireEvent('change',Ext.getCmp('userNum'));
		
		//资费列表 先清空数据
		var tariffNameComp = Ext.getCmp('tariffNameId');
		tariffNameComp.reset();
		var tariffStore = tariffNameComp.getStore();
		tariffStore.removeAll();
		
		//折扣列表 先清空数据
//		var disctComp = Ext.getCmp('disctName_id');
//		var disctStore = disctComp.getStore();
//		disctStore.removeAll();
		
		//tariffArr 产品对应的资费信息，tariffIdArr 资费id，用来过滤相同的资费
		var tariffArr = [],tariffIdArr=[]; 
		var servId='';
		Ext.getCmp('specGridId').getStore().each(function(record){
			var data = record.data;
			if(tariffIdArr.indexOf(data['tariff_id']) == -1){//当前资费不存在则存储
				tariffIdArr.push(data['tariff_id']);
				tariffArr.push([data['tariff_id'],data['tariff_name'],data['tariff_rent']]);
			}
			servId = data['serv_id'];
		},this);
		tariffStore.loadData(tariffArr);
		Ext.getCmp('servId').setValue(servId);
		//产品对应只有一个资费时，默认选中这个资费，并触发其select事件
		if(tariffStore.getCount()==1){
			tariffNameComp.setValue(tariffStore.getAt(0).get('tariff_id'));
			tariffNameComp.fireEvent('select',tariffNameComp,tariffStore.getAt(0));
		}
		this.doInitExpDateComboData();
	},
	//tbar上资费下拉框keyup事件
	doKeyupTariffName:function(combo){
		if(Ext.isEmpty(combo.getRawValue())){
//			var disctComp = Ext.getCmp('disctName_id');
//			disctComp.reset();
//			disctComp.getStore().removeAll();
			
			var store = Ext.getCmp('specGridId').getStore();
			store.clearFilter();
			store.filter('acctitem_name',Ext.getCmp('tbarCobId').getValue());
			Ext.getCmp('userNum').setValue(store.getCount());
			Ext.getCmp('userNum').fireEvent('change',Ext.getCmp('userNum'));
		}
	},
	//tbar上资费下拉框select事件
	doSelectTariffName:function(combo,record){
		var value = combo.getValue();
//		var disctComp = Ext.getCmp('disctName_id');
//		var disctStore = disctComp.getStore();
		if(value){//当选中'资费名称'项时,重新加载grid数据，清空折扣下拉框
			var store = Ext.getCmp('specGridId').getStore();
			store.filter("tariff_id",value);
			
			Ext.getCmp('userNum').setValue(store.getCount());
			Ext.getCmp('userNum').fireEvent('change',Ext.getCmp('userNum'));
			
//			disctComp.reset();
//			disctStore.removeAll();
			
			//加载当前资费的折扣信息
//			for(var i=0;i<this.tariffData.length;i++){
//				if(value == this.tariffData[i]['tariff_id']){
//					if(this.tariffData[i]['disctList']){
//						disctStore.loadData(
//							this.tariffData[i]['disctList']
//						);
//					}
//					break;
//				}
//			}
//			//默认选中第一个折扣
//			if(disctStore.getCount()>0){
//				disctComp.setValue(disctStore.getAt(0).get('disct_id'));
//				//若资费有对应的折扣信息，则触发折扣下拉框中第一个折扣 select事件 级联下拉
//				disctComp.fireEvent('select',disctComp,disctStore.getAt(0));
//			}
			
			var rent = Ext.util.Format.formatFee(record.get('tariff_rent'));
			this.rent = rent;
			/*
			var count = parseFloat(Ext.getCmp('userNum').getValue());
			var shouldPay = rent * count;
			if(Ext.getCmp('payMonths').getValue()){
				shouldPay = shouldPay * parseFloat(Ext.getCmp('payMonths').getValue());
			}
			Ext.getCmp('shouldPay').setValue(shouldPay);
			*/
			this.calcShouldPay();
		}
	},
	doKeyupExpDate : function(combo){
		if(Ext.isEmpty(combo.getRawValue())){
			var store = this.getStore();
			store.clearFilter();
			
			store.filter('prod_name',Ext.getCmp('tbarCobId').getValue());
		}
	},
	doSelectExpDate : function(combo){
		if(combo.getValue()){
			var defaultExpDate = new Date();
			defaultExpDate = Date.parseDate(defaultExpDate.format('Y-m-d'),'Y-m-d');
			var store = Ext.getCmp('specGridId').getStore();
			store.filter("invalid_date",combo.getValue());
			var date = Date.parseDate(combo.getValue(),'Y-m-d');
			var newInvalidDate = Ext.getCmp('newInvalidDate').getValue();
			if(!Ext.isEmpty(newInvalidDate)){
				newInvalidDate = Date.parseDate(newInvalidDate.format('Y-m-d'),'Y-m-d');
				if(date.getTime() - newInvalidDate.getTime() >0 ){
					Ext.getCmp('newInvalidDate').setMinValue(date);
					Ext.getCmp('newInvalidDate').setValue(date);
				}else if(date.getTime() - newInvalidDate.getTime() < 0 ){
					Ext.getCmp('newInvalidDate').setMinValue(defaultExpDate);
					Ext.getCmp('newInvalidDate').setValue(defaultExpDate);
				}
			}
			Ext.getCmp('userNum').setValue(store.getCount());
			Ext.getCmp('userNum').fireEvent('change',Ext.getCmp('userNum'));
		}
	},
	calcShouldPay:function(){//计算应付费用
		if(this.beeningCalc == true){//上一次的正在查询
			return;
		}
		var newInvalidDate = Ext.getCmp('newInvalidDate').getValue();
		if(Ext.isEmpty(newInvalidDate) ){//或者就是今天
			return ;
		}
		var userNum = Ext.getCmp('userNum').getValue();
		if(Ext.isEmpty(userNum)|| newInvalidDate == 0 ){
			return ;
		}
		var tariffNameId = Ext.getCmp('tariffNameId').getValue();
		if(Ext.isEmpty(tariffNameId) && this.rent == null ){
			return ;
		}
		this.superParent.doValid();
		var values = this.superParent.getValues();
		values.invalidDate = newInvalidDate;
		values.shouldPay = 0;
		values.custId = App.getApp().getCustId();
		this.beeningCalc = true;
		Ext.Ajax.request({
			url:Constant.ROOT_PATH+"/core/x/Acct!calcBatchPayFees.action",
			scope:this,
			params:values,
			success:function(req){
				this.beeningCalc = false;
				var data = Ext.decode(req.responseText);
				var records = this.parent.specGrid.store.getRange(0,this.parent.specGrid.getValues().length);
				var allFee = 0;
				for(var index =0;index<records.length;index++){
					var record = records[index];
					var key = record.get('acct_id') + '_' + record.get('acctitem_id');
					var d = data[key];
					if(d){
						record.set('fee',d.fee);
						allFee += d.fee;
					}
				}
				if(allFee <0){
					Alert('到期日有问题,请检查数据!');
				}
				Ext.getCmp('shouldPay').setValue(allFee/100);
			},
			failure:function(){
				this.beeningCalc = false;
			}
		});
	}
	//tbar上折扣下拉框keyup事件
//	doKeyupDisct:function(combo,e){//清空显示值时，清空缴费记录
//		var value = combo.getRawValue();
//		if(Ext.isEmpty(value)){
//			var store = Ext.getCmp('specGridId').getStore();
//			store.each(function(record){
//				record.set('disct_name',null);
//				record.set('disct_id',null);
//			},this);
//		}
//	},
	//tbar上折扣下拉框select事件
//	doSelectDisct:function(combo,record){
//		var disct_id = combo.getValue();
//		var disct_name = record.get('disct_name');
//		var store = Ext.getCmp('specGridId').getStore();
//		store.each(function(record){
//			record.set('disct_name',disct_name);
//			record.set('disct_id',disct_id);
//		},this);
//		
//	},
})

SpecPanel = Ext.extend(Ext.Panel,{
	specGrid : null,
	specForm : null,
	constructor : function(superParent,data){
		this.superParent = superParent;
		this.specGrid = new SpecGrid(data,superParent,this);
		this.specForm = new SpecForm(data,superParent,this);
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