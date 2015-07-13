//【资费变更】
TariffChangeForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH+"/core/x/User!changeTariff.action",
	prodTariffStore : null,
	oldTariff : null,		//产品旧资费数据
	validDate : nowDate(),	//生效日期默认值
	dataRight: null,		//操作员是否有权限
	days: 0,				//增加的最大有效日期天数
	constructor: function(){
		var record = null;
		var activeId = App.getApp().main.infoPanel.getActiveTab().getId();
		if(activeId == 'USER_PANEL'){
			record = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelectionModel().getSelected();
//		}else if(activeId == 'CUST_PANEL'){
//			record = App.getApp().main.infoPanel.getCustPanel().packageGrid.getSelectionModel().getSelected();
		}
		
		this.oldTariff = record.data;
		
		this.prodTariffStore = new Ext.data.JsonStore({
			fields : ['tariff_id','tariff_name','tariff_desc','billing_cycle',
			{name : 'rent',type : 'float'}]
		});
		this.prodTariffStore.on('load',this.validTariff,this);
		
		//产品资费
		var baseParams ={};
		baseParams[CoreConstant.JSON_PARAMS] = Ext.encode(App.getApp()
				.getValues());
		Ext.apply(baseParams,{
			prodId : record.get('prod_id'),
			userId : App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectedUserIds().join(","),
			tariffId : record.get('tariff_id')
		});
		Ext.Ajax.request({
			url : Constant.ROOT_PATH+"/core/x/Prod!queryEditProdTariff.action",
			params:baseParams,
			scope:this,
			success:function(res){
				var data = Ext.decode(res.responseText);
				if(data){
					this.dataRight = data['dataRight'];
					this.prodTariffStore.loadData(data['tariffList']);
					
					if(this.dataRight){		//有修改资费有效日期权限
						cfgData = App.getApp().findCfgData('CHANGE_TARIFF_DAYS');
						if(cfgData) 
							this.days = parseInt(cfgData['config_value']);
					}
				}
			}
		});
		
		TariffChangeForm.superclass.constructor.call(this,{
			trackResetOnLoad:true,
			border : false,
			labelWidth: 100,
			layout:'form',
			baseCls: 'x-plain',
			bodyStyle: "background:#F9F9F9; padding: 10px;",
			items : [{
				xtype : 'hidden',
				value : record.get('prod_sn'),
				id : 'prodSn'
			},{
				xtype : 'textfield',
				fieldLabel : '产品名称',
				style : Constant.TEXTFIELD_STYLE,
				value : record.get('prod_name'),
				id : 'prodName'
			},{
				xtype : 'combo',
				id : 'prodTariffId',
				width : 150,
				store : this.prodTariffStore,
				fieldLabel : '资费',
				emptyText: '请选择',
				allowBlank : false,
				mode: 'local',
				hiddenName : 'tariff_id',
				hiddenValue : 'tariff_id',
				valueField : 'tariff_id',
				displayField : "tariff_name",
				forceSelection : true,
				triggerAction : "all",
				editable : false,
				listeners:{
					scope : this,
					'select' : this.doSelectProdTariff
				}
			},{
				xtype : 'datefield',
				width : 150,
				fieldLabel : '生效日期',
                format: 'Y-m-d', 	
                allowBlank:false,
                editable: false,
				id : 'prodeffdate',
				listeners:{
					scope:this,
					'select':this.doSelectDate
				}
			}]
		})
	},doSelectDate:function(combo){
		var comp = Ext.getCmp('prodexpdate');
		if(comp){
			if(this.oldTariff['is_base'] == 'T'){
				var expDate = "";
				var date = combo.getValue();
				date.setFullYear(date.getFullYear() + 30);
				expDate = date.format("Y-m-d");
				comp.setValue(expDate);
			}else{
				var date = combo.getValue();
				comp.setMinValue(date);
				comp.setValue(date);
			}
		}
	},
	doSelectProdTariff : function(combo,rec){
		this.setValidTariff(rec);
		
		if(this.items.itemAt(4)){
			this.remove(this.items.itemAt(4),true);
		}
		
		if(rec.get('rent') == 0){
			if(this.oldTariff['is_base'] == 'T'){
				var expDate = "";
				var _expDate = Ext.getCmp('prodeffdate').getValue();
					_expDate.setFullYear(_expDate.getFullYear() + 30);
					expDate = _expDate.format("Y-m-d");
				this.add(
				{
					xtype : 'textfield',
					fieldLabel : '截止日期',
					style : Constant.TEXTFIELD_STYLE,
					value : expDate,
					id : 'prodexpdate'
				});
			}else{
				var effMinValue = Ext.getCmp('prodeffdate').minValue;
				this.add({
					xtype : 'datefield',
					width : 150,
					fieldLabel : '截止日期',
					minValue : effMinValue,
					value:effMinValue,
					format : 'Y-m-d',
					allowBlank : false,
					id : 'prodexpdate'
				});
			}
				this.doLayout();
		}
		
	},
	setValidTariff: function(newTariffRecord){
		var now = nowDate();
		var date = now, key = false, minDate = null, maxDate = null;	//默认日期、是否禁用、最小日期、最大日期
		
		if(this.oldTariff['billing_cycle']>1 && this.oldTariff['is_zero_tariff'] == 'F'){
			date = Date.parseDate(this.oldTariff['invalid_date'],'Y-m-d h:i:s').add(Date.DAY, 1);
			//小于当前日期
			if(date < now)	date = now;
			minDate = date;
			maxDate = date.add(Date.DAY, this.days);
		}else{
			//相同周期资费更换
			if(this.oldTariff['billing_cycle'] == newTariffRecord.get('billing_cycle')){
				if (this.oldTariff["is_zero_tariff"] == 'T'
						|| this.oldTariff['tariff_rent'] == 0
						|| this.oldTariff['is_invalid_tariff'] == 'T') {
					//零资费，生效日期最小值为当天，无最大限值随时可生效
					minDate = date;
				} else if(Date.parseDate(this.oldTariff['order_date'], 'Y-m-d h:i:s').format("Y-m-d") 
							== now.format("Y-m-d")){			//当天订购
					minDate = now;
					maxDate = date.add(Date.DAY, this.days);
				} else {
					//默认下月1号
					date.setDate(1);
					date = date.add(Date.MONTH, 1);
					minDate = date;
					if(this.days > 0){		//有修改资费有效日期权限,生效日期最小值为下个月一号
						maxDate = date.add(Date.DAY, this.days);
					}else{					//无权限，默认为下个月第一天
						maxDate = date;
						key = true;
					}
				}
			}else{	//不同周期资费更换
				var billingCycle = this.oldTariff['billing_cycle'];		//旧资费周期
				var oldInvalidDate = this.oldTariff['invalid_date'];	//到产品到期日
				
				if(this.oldTariff["is_zero_tariff"] == 'T'
						|| this.oldTariff['rent'] == 0
						|| this.oldTariff['is_invalid_tariff'] == 'T'){	//零资费
					minDate = date;
				}else{
					date = Date.parseDate(oldInvalidDate,'Y-m-d h:i:s').add(Date.DAY, 1);
					//小于当前日期，默认为当前日期
					if(date < now)	date = now;
					minDate = date;
					maxDate = date.add(Date.DAY, this.days);
				}
			}
		}
		
		var validDateCmp = Ext.getCmp('prodeffdate');
		validDateCmp.setValue(date.format('Y-m-d'));
		if(minDate){
			validDateCmp.setMinValue(minDate.format('Y-m-d'));
		}
		if(maxDate){
			validDateCmp.setMaxValue(maxDate.format('Y-m-d'));
		}
		validDateCmp.setDisabled(key);
	},
	validTariff : function(s,recs){
		var count = s.getCount()
		for(var i=0;i<recs.length;i++){
			if(this.oldTariff['tariff_id'] == recs[i].get('tariff_id')){
				this.prodTariffStore.remove(recs[i]);
				break;
			}
		}
	},
	getValues : function(){
		var params = {};
		params['prodSn'] = Ext.getCmp('prodSn').getValue();
		params['newTariffId'] = Ext.getCmp('prodTariffId').getValue();
		params['effDate'] = Ext.getCmp('prodeffdate').getValue().format("Y-m-d");
		if(Ext.getCmp('prodexpdate') && this.oldTariff['is_base'] != 'T'){
			params['expDate'] = Ext.getCmp('prodexpdate').getValue().format("Y-m-d");
		}
		return params;
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});
Ext.onReady(function(){
	var buy = new TariffChangeForm();
	var box = TemplateFactory.gTemplate(buy);
});