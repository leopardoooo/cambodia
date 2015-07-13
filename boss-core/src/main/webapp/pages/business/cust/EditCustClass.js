/**
 * 修改客户优惠类型
 * @class TransferForm
 * @extends Ext.FormPanel
 */
EditCustClassForm = Ext.extend( BaseForm , {
	url:  Constant.ROOT_PATH + "/core/x/Cust!editCustClass.action",
	zzdUser: null,formerCustClass:null,
	zzdBaseProd: null,
	year: null,
	month: null,
	acctZzdRec:null,
	classStore:null,
	feeClear:function(){//判断是否费用结清//首先判断主终端.基本包是否欠费
		var items = this.acctZzdRec.data.acctitems;
		if(!items || items.length == 0 ){
			return true;
		}
		for(var index = 0;index < items.length;index++){
			var item = items[index];
			if(item.is_base != 'T'){
				continue;
			}
			if(item.owe_fee > 0 ){
				return false;
			}
		}
		return true;
	},
	constructor: function(){
		var _nowDate = nowDate();
		var _nextMonthFirstDate = _nowDate.clone();
		_nextMonthFirstDate.setDate(1);
		_nextMonthFirstDate.setMonth(_nextMonthFirstDate.getMonth() + 1);
		this.loadZZDInfo();
		
		var userStore = App.getApp().main.infoPanel.getUserPanel().userGrid.store; 
		var zzd = userStore.getAt(userStore.find('terminal_type','ZZD'));
		if(!zzd){
			Alert('数据错误,没有找到主终端!请刷新页面重新查找客户在继续业务.');
			return false;
		}
		var acctStore = App.getApp().main.infoPanel.acctPanel.acctGrid.store;
		this.acctZzdRec = acctStore.getAt(acctStore.find('user_id',zzd.get('user_id')));
		
		this.year = _nowDate.format("Y");
		this.month = _nowDate.format("m");
		
		this.classStore = new Ext.data.JsonStore({
			url : Constant.ROOT_PATH+"/commons/x/QueryParam!queryItemValues.action",
			fields : ['item_value','item_name']
		});
		this.classStore.load();
		this.formerCustClass = App.getCust().cust_class;
		EditCustClassForm.superclass.constructor.call(this, {
			border:false,
			bodyStyle:'padding-top:10px',
			labelWidth:100,
			items:[{
				fieldLabel:'优惠类型',
				xtype: 'combo',
				store : this.classStore,
				valueField : 'item_value',
				displayField : "item_name",					
				allowBlank: false,
				hiddenName: 'custClass',
				emptyText: '请选择...',
				id: 'custClassEl',
				listeners: {
					scope: this,
					"select": function(box, r, index){
						if(this.zzdBaseProd){
							var items = this.createZZDComps(_nextMonthFirstDate);
							var form = Ext.getCmp("dynamicForm");
							form.removeAll(true);
							form.add(items);
							this.doLayout();
						}
						
						if(box.getValue() == 'YBKH'){
							Ext.getCmp("custClassDate_year").setValue("");
							Ext.getCmp("custClassDate_month").setValue("");
							Ext.getCmp("custClassDate_panel").hide();
						}else{
							if(this.formerCustClass != 'YBKH' || this.feeClear()){
								Ext.getCmp("custClassDate_year").reset();
								Ext.getCmp("custClassDate_month").reset();
								Ext.getCmp("custClassDate_panel").show();
								App.getCust().cust_class = this.formerCustClass;//莫名其貌的 cust_class被修改.
							}else{
								App.getCust().cust_class = this.formerCustClass;
								Alert('主终端基本包费用尚未结清,不能修改优惠类型.',function(){
									App.getApp().menu.bigWindow.hide();
								});
							}
						}
						App.getCust().cust_class = this.formerCustClass;
					}
				}
			},{
				xtype: 'panel',
				baseCls: 'x-plain',
				layout: 'form',
				id: 'custClassDate_panel',
				width: 350,
				labelWidth: 100,
				items: [{
					xtype: 'compositefield',
				    fieldLabel: '优惠有效期',
				    allowBlank:false,
				    items: [{
			            height: 22,
			            xtype: 'spinnerfield',
		            	id: 'custClassDate_year',
		            	minValue: this.year,
		            	maxValue: this.year + 100,
			            value: this.year,
			            width: 60,
			            listeners: {
			            	scope: this,
			            	"spin": this.changeInvalidDate
			            }
				    },{
				    	xtype: 'displayfield',
				    	value: '年',
				    	width: 10
				    },{
				        xtype: 'combo',
				        triggerAction: 'all',
				        mode: 'local',
				        width: 50,
				        height: 25,
				        id: 'custClassDate_month',
				        value: this.month,
				        store: new Ext.data.ArrayStore({
				            fields: ['value',"text"],
				            data: [[1,"1月"],[2,"2月"],[3,"3月"],[4,"4月"],[5,"5月"],[6,"6月"],
				                   [7,"7月"],[8,"8月"],[9,"9月"],[10,"10月"],[11,"11月"],[12,"12月"]]
				        }),
				        valueField: 'value',
				        displayField: 'text',
			            listeners: {
			            	scope: this,
			            	"select": this.changeInvalidDate
			            }
				    },{
				    	xtype: 'displayfield',
				    	id: 'custClassDateTitle',
				    	width: 120
				    }]
				}]
			},{
				id: 'dynamicForm',
				xtype: 'panel',
				layout: 'form',
				width: '100%',
				baseCls: 'x-plain'
			}]
		});
	},
	loadZZDInfo:function(){
		var store = App.getApp().main.infoPanel.getUserPanel().userGrid.getStore();
		var that = this;
		store.each(function(item){
			if(item.get("terminal_type") == "ZZD"){
				that.zzdUser = item.data;
				return false;
			}
		});
		
		if(this.zzdUser){
			var prodMap = App.getApp().main.infoPanel.getUserPanel().prodGrid.prodMap;
			var zzdUserProdData = prodMap[this.zzdUser["user_id"]];
			if(zzdUserProdData){
				for(var i = 0; i < zzdUserProdData.length ; i++){
					if(zzdUserProdData[i]["is_base"] == "T"){
						this.zzdBaseProd = zzdUserProdData[i];
						break;
					}
				}
			}
		}
	},
	createZZDComps: function(_nextMonthFirstDate){
		//产品资费
		var baseParams ={};
		var _all = App.getApp().getValues();
		_all["custFullInfo"]["cust"]["cust_class"] = Ext.getCmp("custClassEl").getValue();
		_all["selectedDtvs"] = [this.zzdUser];
		baseParams[CoreConstant.JSON_PARAMS] = Ext.encode(_all);
		this.baseProdTariffStore = new Ext.data.JsonStore({
			baseParams: baseParams,
			url: Constant.ROOT_PATH + "/core/x/Prod!queryProdTariff.action",
			fields: ['tariff_id','tariff_name','tariff_desc','billing_cycle',{name:'rent', type: 'float'}]
		});
		
		this.baseProdTariffStore.load({
			params:{
				prodId:this.zzdBaseProd["prod_id"],
				userId:this.zzdUser["user_id"]
			}
		});
		
		return [{
			fieldLabel:'基本包资费',
			xtype : 'combo',
			id : 'prodTariffId',
			store : this.baseProdTariffStore,
			emptyText: '请选择',
			allowBlank : false,
			mode: 'local',
			hiddenName : 'tariff_id',
			hiddenValue : 'tariff_id',
			valueField : 'tariff_id',
			displayField : "tariff_name",
			forceSelection : true,
			triggerAction : "all",
			width: 130
		},{
			xtype : 'datefield',
			fieldLabel : '生效日期',
			format : 'Y-m-d',
			readOnly: true,
			value : _nextMonthFirstDate,
			allowBlank : false,
			width: 130,
			name: 'tariffStartDate',
			id : 'prodstartdate'
		}];
	},
	doValid:function(){
		var obj = this.getCmpDateObj();
		if(this.year == obj.y && this.month > obj.m){
			return {
				isValid: false,
				msg: "优惠有效期必须不能小于本月"
			};
		}
		
		return EditCustClassForm.superclass.doValid.call(this);
	},
	changeInvalidDate: function(){
		var obj = this.getCmpDateObj();
		var _now = nowDate();
		_now.setFullYear(obj.y);
		_now.setMonth(obj.m);
		_now.setDate(1);
		Ext.getCmp("custClassDateTitle").setValue("于" + _now.format("Y-m-d") + "失效！");
	},
	getCmpDateObj: function(){
		var year = Ext.getCmp("custClassDate_year").getValue();
		var month = Ext.getCmp("custClassDate_month").getValue();
		
		return {
			y: year,
			m: month
		}
	},
	getValues: function(){
		var ps = EditCustClassForm.superclass.getValues.call(this);
		
		if(this.zzdBaseProd){
			ps["user_id"] = this.zzdUser["user_id"];
			ps["prod_sn"] = this.zzdBaseProd["prod_sn"];
			// if the switch tariff is zero rent, default expDate is after 30 years.
			var expDate = "";
			var index = this.baseProdTariffStore.find("tariff_id", ps["tariff_id"]);
			var record = this.baseProdTariffStore.getAt(index);
			if(record.get("rent") == 0){
				var _expDate = nowDate();
				_expDate.setFullYear(_expDate.getFullYear() + 30);
				expDate = _expDate.format("Y-m-d");
			}
			ps["expDate"] = expDate;
		}
		// 一般客户 将客户优惠类型置空
		if(ps["custClass"] == 'YBKH'){
			ps["custClassDate"] = "";
		}else{
			var obj = this.getCmpDateObj();
			var _now = nowDate();
			_now.setFullYear(obj.y);
			_now.setMonth(obj.m);
			_now.setDate(1);
			ps["custClassDate"] = _now.format("Y-m-d");
		}
		return ps;
	},
	success:function(){
		var panel = App.getApp().main.infoPanel;
		panel.getCustPanel().custInfoPanel.remoteRefresh();
		panel.getUserPanel().prodGrid.remoteRefresh();
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});


/**
 * 入口
 */
Ext.onReady(function(){
	var ecf = new EditCustClassForm();
	var box = TemplateFactory.gTemplate(ecf);
});
