/*
 * 【用户产品订购】
 */
ProdOrderForm = Ext.extend( BaseForm, {
	url : Constant.ROOT_PATH+"/core/x/ProdOrder!saveOrderProd.action",
	selectUserPanel: null,
	transferPayWindow: null,
	// 单产品订购102，升级100，续订101, 套餐订购1015
	busiCode: null,
	// 单用户
	userId: null,
	// 初始化参数，包括产品列表、资费、上一次订购记录
	baseData: null,
	// 转移支付数据明细
	transferPayData: [],
	//  套餐订购用户
	packageGroups: null,
	busiFeeTime:null,
	prodId:null,
	isBusiFee:false,
	constructor: function(p){
		this.selectUserPanel = new SelectUserPanel(this);
		
		// 订购月份
		var orderMonthField = new Ext.ux.form.SpinnerField({
			xtype: 'spinnerfield',
            fieldLabel: lmain("user._form.prodOrderMonths"),
           // allowBlank: false,
            readOnly: true,
            id: 'sfOrderCycle',
            minValue: 0,
		    listeners: {
		    	scope: this,
		    	spin: function(){
		    		this.doChangeEndDate();
		    		this.doChangeAmount();
		    	}
		    }
		});
		
		// 重写spinner的触发规则，支持readonly
		orderMonthField.spinner.isSpinnable = function(){
			return true;
		}
		orderMonthField.spinner.onTriggerClick = function(){
	        var middle = orderMonthField.spinner.getMiddle();
	        var ud = (Ext.EventObject.getPageY() < middle) ? 'Up' : 'Down';
	        orderMonthField.spinner['onSpin' + ud]();
		}
		
		// 构造表单
		ProdOrderForm.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			autoScroll:true,
            border: false,
            layout: 'border',
			items:[{
				bodyStyle:'background:#F9F9F9;padding-top:10px; border-bottom: 0px;',
				region: "north",
				layout: 'form',
				title: lmain("user._form.titleSwitchProd"),
				height: 94,
				items: [{
					xtype: 'combo',
				    fieldLabel: lmain("user._form.prodName"),
				    width: 200,
				    displayField: 'prod_name',
				    valueField: 'prod_id',
				    allowBlank: false,
				    mode: 'local',
				    id: 'boxProdId',
				    store: new Ext.data.JsonStore({
						fields : ['prod_id', 'prod_name', 'prod_desc',"prod_type"]
					}),
					listeners: {
						scope: this,
						select: this.doSelectProd
					}
				},{
					anchor: '90%',
		            xtype: 'displayfield',
		            fieldLabel: lmain("user._form.prodDesc"),
		            id: 'dfProdDesc',
		            value: ''
				}]
			},{
				region: 'center',
				layout: 'border',
				border: false,
				items: [{
					region: 'west',
					width: 280,
					title: lmain("user._form.titleDetemineUser"),
					split: true,
			        minSize: 150,
			        maxSize: 350,
					layout: 'fit',
					bodyStyle: "border-bottom: none;",
					items: [this.selectUserPanel]
				},{
					region: 'center',
					layout: 'form',
//					labelAlign: 'top',
//					labelPad: 20,
					labelWidth:90,
					defaults: {anchor: "90%"},
					title: lmain("user._form.titleOrderInfo"),
					bodyStyle: 'padding: 15px 0 0 20px;border-bottom: none;',
					items: [{
					    xtype: 'combo',
					    fieldLabel: lmain("user._form.prodTariff"),
					    displayField: 'disct_name',
					    allowBlank: false,
					    valueField: 'tariff_id',
					    mode: 'local',
					    emptyText: lbc("common.plsSwitch"),
					    id: 'boxProdTariff',
					    store: new Ext.data.JsonStore({
							fields: ['tariff_id', 'disct_name', 'billing_cycle','max_order_cycle', 'disct_rent']
						}),
						listeners: {
							scope: this,
							select: this.doSelectTariff
						}
					},{
						id:'orderTypeItemsId',
						border: false,
						layout: 'form',
						items:[
						{
							id:'order_type_id',
							fieldLabel: lmain("user._form.prodOrderType"),
							xtype:'paramcombo',
							allowBlank:false,
							hiddenName:'order_type',
							paramName:'ORDER_TYPE',
							defaultValue:'ORDER',
							listeners : {
								scope : this,
								select:function(box,record,index){
									this.doPayOrderType(box.getValue());
								}
							}
						}
						]},{
						id:'orderMonthItemsId',
						border: false,
						layout: 'form',
						items:[orderMonthField]
					},{
						xtype: 'panel',
						anchor: '100%',
						baseCls: 'x-plain',
						style: 'padding-bottom: 10px;',
						html: '<b>【'+ lmain("user._form.lastOrderExpDate") +'：<span id="lastOrderProdExtDate">--</span>】</b>'
					},{
						xtype: 'datefield',
					    fieldLabel: lmain("user._form.prodStartDate"),
					    id: 'dfStartDate',
					    format: 'Y-m-d',
					    allowBlank: false,
					    readOnly: true,
					    vtype: 'daterange',
                		endDateField: 'dfExpDate',
                		customDay: 1
					},{
						xtype: 'datefield',
						id: 'dfExpDate',
						allowBlank: false,
						readOnly: true,
			            fieldLabel: lmain("user._form.prodExpDate"),
			            format: 'Y-m-d',
			            listeners: {
			            	scope: this,
			            	select: this.doSelectExpDate
			            }
					}]
				}]
			},{  
		         region: "south",
				 height: 90, 
				 buttonAlign:'center',
				 flex:1,
       			 frame:true,  
				 labelAlign:'right',  
				 layout:'column',
				 id:'feeItemId',
				 labelWidth:50,
				 border: false,
		         items:[{ 
		         	columnWidth:0.99,
		         	xtype:'fieldset',  
		         	id:'orderFeeItemId',
				    height: 75, 
				    title: lmain("user._form.titleOrderFee"),
         			style:'margin-left:2px;padding: 10px 0 10px 10px; color: red',
         			layout:'column',
         			items:[{
         				columnWidth:.65,
         				items:[{
         						bodyStyle:'padding-top:4px',
		         				html: "*"+ lmain("user._form.shouldPay") + "$:<span id='totalAmount'>--</span>"
								+"（"+ lmain("user._form.addOrderFee") +":<b id='addAmount'>--</b> "
								+" - "
								+" <a id='transferHrefTag' href='#'>"+lmain("user._form.transferPay")+":<b id='transferAmount'>--</b></a>"
								+" ）"
			         			}]
         				},{
         				columnWidth:.35,
         				layout : 'form',
         				labelWidth:50,  
         				items:[{
								fieldLabel : lbc("common.pay"),
								id : 'orderFeeTypeId',
								name:'order_fee_type',
								allowBlank : false,
								xtype:'paramcombo',
								width: 60,
								emptyText: lbc("common.plsSwitch"),
								defaultValue:'CFEE',
								paramName:'ORDER_FEE_TYPE',
								listeners: {
									scope: this,
									'expand': function(combo){
										var store = combo.getStore();
										store.removeAt(store.find('item_value','TRANSFEE'));
									}
								}
							}]
         				}]
		         }]  
			}]
		});
	},
	// 加载产品，资费等数据
	doLoadBaseData: function(){
		this.busiCode = App.getData().currentResource.busicode;
		var filterOrderSn = null;
		// 单用户订购
		if(this.busiCode === "102"){
			var users = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections();
			this.userId = users[0].get("user_id");
		// 续订和升级
		}else if(this.busiCode === "100" || this.busiCode === "101"){
			var panelId = App.getData().currentPanelId;
			var prodData = null;
			var prodGrid = App.getApp().main.infoPanel.getUserPanel().prodGrid;
			// 套餐
			if(panelId === "U_CUST_PKG"){
				prodData = prodGrid.custPkgGrid.selModel.getSelected().data;
			}else{
				prodData = prodGrid.baseProdGrid.selModel.getSelected().data;
				this.userId = prodGrid.userId;
			}
			filterOrderSn  = prodData["order_sn"];
		}
		if(this.busiCode === "101" || this.busiCode === "102" || this.busiCode ==="1015"){
			Ext.getCmp('orderTypeItemsId').hide();
		}
		
		Ext.Ajax.request({
			url :root + '/core/x/ProdOrder!loadProdList.action',
			scope : this,
			params: {
				busi_code: this.busiCode,
				cust_id: App.getCustId(),
				user_id: this.userId,
				filter_order_sn: filterOrderSn
			},
			success : function(response,opts){
				var responseObj = Ext.decode(response.responseText);
				this.baseData = responseObj;
				Ext.getCmp("boxProdId").getStore().loadData(this.baseData["prodList"]);
				//ip外挂费用
				var busiFee = this.baseData["busiFee"];
				if(busiFee){
					Ext.getCmp('orderFeeItemId').columnWidth = 0.6;
					Ext.getCmp('feeItemId').add({ 
			         	id:'busiFeeItemId',
					    columnWidth:.4,
			         	xtype:'fieldset',
			         	height: 75, 
			         	title:lmain("user._form.ipFee"),
			         	style:'margin-left:10px;padding: 10px 0 10px 10px; color: red',
						items:[{
			         		bodyStyle:'padding-top:4px',
							html: "*"+lmain('user._form.shouldPay')+"$:<span id='busiFeeAmount'>--</span>"
			         	},{
			         		bodyStyle:'padding-top:4px',
							html: lmain('user._form.timeSlot')+":<span id='busiFeeTime'>--</span>"
			         	}]
			         });
					Ext.getCmp('busiFeeItemId').setTitle(busiFee.fee_name);
				}else{
					Ext.getCmp('orderFeeItemId').columnWidth = 0.99;
				}
				this.doLayout();
			}
		});
	},
	doInit:function(){
		this.doLoadBaseData();
		App.form.initComboData(this.findByType('paramcombo'));
		ProdOrderForm.superclass.doInit.call(this);
		//this.on("render",function(){
		var that = this;
		setTimeout(function(){
			Ext.get("transferHrefTag").on("click", function(e, t, o){
				if(!that.transferPayWindow){
					that.transferPayWindow = new TransferPayWindow();
				}
				if(that.transferPayData && that.transferPayData.length > 0){
					that.transferPayWindow.show(that.transferPayData, Ext.getCmp("dfStartDate").getValue());
				}else{
					Alert(lmsg('notTransProject'));
				}
			});
		}, 2000);
		//}, this);
	},
	//产品下拉框选中事件
	doSelectProd: function(box, record, index){
		// 当前选中的产品
		this.currentSelectedProd = record;
		var prodId = record.get("prod_id");
		this.prodId = prodId;
		Ext.getCmp("dfProdDesc").setValue(record.get("prod_desc"));
		var boxProdTariff = Ext.getCmp("boxProdTariff");
		boxProdTariff.reset();
		var tariffs = this.baseData["tariffMap"][prodId] || [];
		boxProdTariff.getStore().loadData(tariffs);
		// 如果只有一个默认选中, 
		// Update: 尽管只有1个资费，也不选中，因为转移支付的初始化问题
//		if(tariffs.length == 1){
//			boxProdTariff.setValue(tariffs[0]["tariff_id"]);
//		}else{
//			boxProdTariff.setRawValue("");
//		}
		// 上期结束日期
		var lastOrderProdDate = this.getLastProdEndDate(prodId);
		Ext.get("lastOrderProdExtDate").update(lastOrderProdDate || "--");
		
		// 开始计费日期 默认为当前时间
		var startDate = nowDate(); 
		// 有上期结束日并且当前不是升级业务
		if(lastOrderProdDate && this.busiCode != "100"){
			var tmpDate = Date.parseDate(lastOrderProdDate, "Y-m-d");
			tmpDate.setDate(tmpDate.getDate() + 1);
			startDate = tmpDate;
		}
		Ext.getCmp("dfStartDate").setValue(startDate);
		// 结束计费日期
		this.doChangeEndDate();
		// 加载终端用户
		this.doLoadUsers(record);
	},
	doBusiFeeTime:function(){
		var busiFee = this.baseData["busiFee"];
		//ip外挂费用
		if(busiFee){
			var months = Ext.getCmp("sfOrderCycle").getValue();
			if(Ext.isEmpty(months)){
				return;
			}
			var startDate ;
			var feeCount=0;
			if(this.isPkg()){
				//所有的用户
				var allUserList=this.selectUserPanel.selectUserData.userList;
				var ipUserList=[];
				var ipUserMap = {};
				//选中的用户
				this.selectUserPanel.store.each(function(rs){
					var key = rs.get('user_id');
					if(!ipUserMap[key]){
						ipUserMap[key] = [];
					}
					ipUserMap[key].push(rs.get('user_id'));
				});
				//满足条件的用户
				for(var i=0 ;i<allUserList.length;i++){
					if(!Ext.isEmpty(allUserList[i].str5)
						&&!Ext.isEmpty(allUserList[i].str6)
						&&!Ext.isEmpty(allUserList[i].prod_exp_date)){
							if(ipUserMap[allUserList[i].user_id]){
								ipUserList.push(allUserList[i]);
							}
					}
				}
				
				if(ipUserList.length == 0){
					return ;				
				}
				for(var i=0 ;i<ipUserList.length;i++){
					var ipTime = Date.parseDate(Ext.util.Format.subStringLength(ipUserList[i].prod_exp_date,10), "Y-m-d");
					if(startDate == null){
						startDate = ipTime;
					}else{
						if(startDate.getTime()<ipTime.getTime()){
							startDate = ipTime;
						}
					}
					//多个用户满足条件时候，ip数量相加
					feeCount = feeCount + parseInt(ipUserList[i].str6);
				}
				
				//满足条件的用户中选出最晚的到期日
				startDate = startDate.format("Y-m-d");
				
			}else{
				 startDate = Ext.util.Format.subStringLength(busiFee.last_prod_exp,10);
			     feeCount=busiFee.fee_count;
			}
			
			var feeMonths = 0;//月份数
			var days = 0;//结束时间和开始计费时间相差天数
			//业务费开始计费日期=产品开始计费日期
			var dfExpDate = Ext.getCmp("dfExpDate").getValue();
			if(startDate == Ext.getCmp("dfStartDate").getValue().format("Y-m-d")){
				feeMonths = months;
			}else{
				//2个时间之间相差的月数
				var bmonth = Ext.util.Format.getMonths(startDate,dfExpDate);
				//业务费开始计费日期与产品开始计费日期，相差整数月数
				if(Ext.util.Format.addMoth(startDate,bmonth) == dfExpDate){
					feeMonths = bmonth;
				}else{
					days = Ext.util.Format.getDays(startDate,dfExpDate)
				}
			}
			if(days != 0){
				var v = parseInt(days/30*100)/100;
				this.busiFeeAmount = (v*feeCount*busiFee.default_value);
			}else{
				this.busiFeeAmount = feeMonths*feeCount*busiFee.default_value;
			}
			Ext.get("busiFeeAmount").update(Ext.util.Format.convertToYuan(this.busiFeeAmount));
			this.busiFeeTime =  Date.parseDate(startDate,"Y-m-d").format("Ymd")+"-"+Date.parseDate(dfExpDate, "Y-m-d").format("Ymd");
			Ext.get('busiFeeTime').update(this.busiFeeTime);
		}
	},
	// 加载终端用户
	doLoadUsers: function(selectProdRecord){
		if(this.isPkg()){
			var prodId = this.currentSelectedProd.get("prod_id");
			var lastProd = this.baseData["lastOrderMap"][prodId];
			lastProd = lastProd ? lastProd["order_sn"] : null;
			this.selectUserPanel.loadPackageUsers(selectProdRecord.get("prod_id"), lastProd);
		}else{
			var user = this.baseData["userDesc"];
			this.selectUserPanel.loadSingleUser(user);
		}
	},
	doPayOrderType:function(v){
		if(v == 'TRANSFER'){
			Ext.getCmp('orderMonthItemsId').hide();
			//转移支付的金额和到期日变动
			this.doTransferAmount()
			this.doTransferDate()
		}else{
			Ext.getCmp('orderMonthItemsId').show();
			//设置订购月的默认值			
			var sfOrderCycle = Ext.getCmp("sfOrderCycle");
			var boxProdTariff = Ext.getCmp("boxProdTariff");
			var disctId = boxProdTariff.getValue();
			if(Ext.isEmpty(disctId)){
				Alert(lmsg('chooseTariff'));
				return false;
			}
			var index = boxProdTariff.getStore().find("tariff_id", disctId);
			var tariffRecord = boxProdTariff.getStore().getAt(index);
			sfOrderCycle.setValue(tariffRecord.get('billing_cycle'));
			
			// 结算结束计费日
			this.doChangeEndDate();
			// 计算结算金额
			this.doLoadTransferAmount();
		}
		
	},
	doTransferAmount:function(){
		this.totalAmount = 0;
		this.addAmount = this.transferAmount;
		Ext.get("totalAmount").update(String(this.totalAmount/100));
		Ext.get("addAmount").update(String(this.addAmount/100));
		// 转移支付
		Ext.get("transferAmount").update(String(this.transferAmount/100));
	},
	doTransferDate:function(){
		var boxProdTariff = Ext.getCmp("boxProdTariff");
		var disctId = boxProdTariff.getValue();
		if(Ext.isEmpty(disctId)){
			Alert(lmsg('chooseTariff'));
			return false;
		}
		var index = boxProdTariff.getStore().find("tariff_id", disctId);
		var tariffRecord = boxProdTariff.getStore().getAt(index);
		
		var tAmount = this.transferAmount;
		var disctRent = tariffRecord.get("disct_rent");
		//根据转移支付的金额计算出多少月（带小数，小数再转换为天）
		var mothAllNum =(tAmount/disctRent)*tariffRecord.get("billing_cycle");
		var mothNum = parseInt(mothAllNum);
		var dayNum = Math.ceil((mothAllNum - mothNum)*30);
		//月数赋值
		Ext.getCmp("sfOrderCycle").setMinValue(0);
		Ext.getCmp("sfOrderCycle").setValue(mothNum+Ext.util.Format.ceil((dayNum/30),2));
		//alert(Ext.getCmp("sfOrderCycle").getValue());
		var startDate = Ext.getCmp("dfStartDate").getValue();
		//开始日期加mothNum月，dayNum天,,startDate是+1天的，所以到期日要-1天，dayNum-1;
		Ext.getCmp("dfExpDate").setValue(Ext.util.Format.addTime(startDate.format("Y-m-d"),mothNum,dayNum-1));
	},
	// 资费下拉框选中事件
	doSelectTariff: function(box, record, index){
		var billingCycle = record.get("billing_cycle");
		var maxOrderCycle = record.get("max_order_cycle");
		var sfOrderCycle = Ext.getCmp("sfOrderCycle");
		// 递增值
		sfOrderCycle.spinner.incrementValue = billingCycle;
		//最小值
		if(this.isProtocolPkgAndUpgradeBusiCode()){
			sfOrderCycle.setMinValue(0);
		}else{
			sfOrderCycle.setMinValue(billingCycle);
			sfOrderCycle.setValue(billingCycle);
		}
		//最大值
		if(maxOrderCycle){
			sfOrderCycle.setMaxValue(maxOrderCycle*billingCycle);
		}else{
			sfOrderCycle.setMaxValue(null);
		}
		//0资费的话，按月算
		//零资费可以修改结束计费日期，订购月数可以是大于零的小数，按天计算
		var field = Ext.getCmp('dfExpDate');
		if(record.get('disct_rent') == 0){
			Ext.getCmp('order_type_id').setValue('ORDER');
			Ext.getCmp('orderMonthItemsId').show();
			field.setReadOnly(false);
			sfOrderCycle.setMinValue(0);
		}else{
			field.setReadOnly(true);
			sfOrderCycle.setMinValue(billingCycle);
		}
		var orderType = Ext.getCmp('order_type_id').getValue();
		if(orderType == 'TRANSFER'){
			this.doTransferAmount()
			this.doTransferDate()
		}else{
			// 结算结束计费日
			this.doChangeEndDate();
			// 计算结算金额
			this.doLoadTransferAmount();
		}
	},
	doSelectExpDate: function(dateField, expDate){
		var effDate = Ext.getCmp('dfStartDate').getValue();
		var days = Ext.util.Format.getDays( effDate.format('Y-m-d'), expDate.add(Date.DAY, 1).format('Y-m-d') )*1.0;
		var orderMonths = Ext.util.Format.round(days/30.0,2);
		Ext.getCmp('sfOrderCycle').setValue(orderMonths);
	},
	// 上期结束计费日
	getLastProdEndDate: function(prodId){
		var lastOrderProd = this.baseData["lastOrderMap"][prodId];
		return lastOrderProd ? lastOrderProd["exp_date"].split(" ")[0] : null;
	},
	// 结束日期
	doChangeEndDate: function(){
		var startDate = Ext.getCmp("dfStartDate").getValue();
		var months = Ext.getCmp("sfOrderCycle").getValue();
		//如果是协议套餐，并且当前周期为0，则结束日为上期结束计费日
		if(this.isProtocolPkgAndUpgradeBusiCode() && months == 0){
			var lastOrderProdDate = this.getLastProdEndDate(prodId);
			Ext.getCmp("dfExpDate").setValue(lastOrderProdDate);
			return;
		}
		
		// 其它根据周期进行计算
		if(startDate){
			// 计算结束日
			var endDate = Date.parseDate(Ext.util.Format.addMoth(startDate.format("Y-m-d"),months),"Y-m-d");
			endDate.setDate(endDate.getDate()-1);
			Ext.getCmp("dfExpDate").setValue(endDate.format("Y-m-d"));
		}else{ 
			Ext.getCmp("dfExpDate").setValue(null);
		}
		
		//加挂IP费
		this.doBusiFeeTime();
	},
	doLoadTransferAmount: function(){
		var validObj = this.doBaseValid(); 
		if(validObj["isValid"] !== true){
//			Alert(validObj["msg"]);
			return ;
		}
		Ext.Ajax.request({
			url :root + '/core/x/ProdOrder!loadTransferFee.action',
			scope : this,
			params: { 
				"orderProd": Ext.encode(this.getTransferValues()),
				"busi_code": this.busiCode
			},
			success : function(response,opts){
				var responseData = Ext.decode(response.responseText);
				//修改转移金额
				this.transferPayData = responseData;
				var sumAmount = 0;
				if(responseData){
					for(var i = 0; i< responseData.length; i++){
						sumAmount += responseData[i]["active_fee"];
					}
				}
				this.transferAmount = sumAmount;
				// 修改小计信息
				this.doChangeAmount();
			},
			clearData:function(){
				//清空组件
				Ext.getCmp('boxProdTariff').reset();				
			}
		});
	},
	// 实际应付
	totalAmount: -1,
	// 转移支付
	transferAmount: 0,
	// 新增订购
	addAmount: -1,
	//当前选中的产品
	currentSelectedProd: null,
	busiFeeAmount:0,
	doChangeAmount: function(){
		//计算新增金额
		var boxProdTariff = Ext.getCmp("boxProdTariff");
		var disctId = boxProdTariff.getValue();
		if(Ext.isEmpty(disctId)){
			Alert(lmsg('chooseTariff'));
			return false;
		}
		var index = boxProdTariff.getStore().find("tariff_id", disctId);
		var tariffRecord = boxProdTariff.getStore().getAt(index);
		this.addAmount = Ext.getCmp("sfOrderCycle").getValue() * tariffRecord.get("disct_rent")/tariffRecord.get("billing_cycle");
		Ext.get("addAmount").update(String(this.addAmount/100));
		// 实付
		this.totalAmount = this.addAmount - this.transferAmount;
		Ext.get("totalAmount").update(String(this.totalAmount/100));
		// 转移支付
		Ext.get("transferAmount").update(String(this.transferAmount/100));

		
	},
	// 是否为套餐
	isPkg: function(){
		if(this.currentSelectedProd){
			var prodType = this.currentSelectedProd.get("prod_type");
			return prodType === "SPKG" || prodType === "CPKG";
		}
		return false;
	},
	// 是否为协议套餐
	isProtocolPkgAndUpgradeBusiCode: function(){
		if(this.currentSelectedProd){
			var prodType = this.currentSelectedProd.get("prod_type");
			return prodType === "SPKG" && this.busiCode === "100";
		}
		return false;
	},
	doValid : function(){
		var validObj = this.doBaseValid(); 
		if(validObj["isValid"] !== true){
			return validObj;
		}
		var orderCycle = Ext.getCmp("sfOrderCycle").getValue();
		if(String(orderCycle).trim() === ""){
			return {
				isValid: false,
				msg: lmsg('MustBeOrderMonth')
			}
		}
		
		// 升级业务，失效日期必须比上期结束日大
//		var prodId = this.currentSelectedProd.get("prod_id");
//		var lastOrderProdDate = this.getLastProdEndDate(prodId);
//		if( lastOrderProdDate && this.busiCode == "100"){
//			var tmpDate = Date.parseDate(lastOrderProdDate, "Y-m-d");
//			// 如果失效日期小雨上期结束日，则不给于提交
//			if(Date.parseDate(Ext.getCmp("dfExpDate").getValue(), "Y-m-d").getTime() < tmpDate.getTime()){
//				return {
//					isValid: false,
//					msg: lmsg('upgradeEndDateMoreThanBeginDate')
//				}
//			}
//		}
		
		if(this.totalAmount < 0){
			return {
				isValid: false,
				msg: lmsg('realpayMustBeGreaterThanZero')
			}
		}
		return true;
	},
	doBaseValid : function(){
		var validObj = ProdOrderForm.superclass.doValid.call(this); 
		if(validObj["isValid"] !== true){
			return validObj;
		}
		
		if(this.selectUserPanel.store.getCount() == 0){
			return {
				isValid: false,
				msg: lmsg('notMustBeOrderUser')
			}
		}
		
		return {
			isValid: true
		};
	},	
	getValues : function(){
		var values = this.getTransferValues();
		values["order_months"] = Ext.getCmp("sfOrderCycle").getValue();
		
		// 实际支付金额（小计金额）
		values["pay_fee"] = this.totalAmount;
		// 转移支付
		values["transfer_fee"] = this.transferAmount;
		
		values["order_fee_type"] = Ext.getCmp('orderFeeTypeId').getValue();
		// 失效日期
		values["exp_date"] = Ext.getCmp("dfExpDate").getValue().format('Y-m-d') + " 00:00:00";
		
	
		var all = {
			"busi_code": this.busiCode,
			"orderProd": Ext.encode(values)
		};
		
		var data = [];
		var obj = {};
		var busiFee = this.baseData["busiFee"];
		if(busiFee){
			obj['fee_id'] = busiFee.fee_id;
			obj['fee_std_id'] = busiFee.fee_std_id;
			obj['count'] = busiFee.fee_count;
			obj['should_pay'] = this.busiFeeAmount;
			obj['real_pay'] = this.busiFeeAmount;
			obj['disct_info'] = this.busiFeeTime;
			data.push(obj);		
			//其他杂费busiFees 专用
			all["busiFees"] = data;
		}
		
		return all;
	},
	getTransferValues: function(){
		var values = {};
		
		// 基础信息
		values["cust_id"] = App.getCustId();
		values["user_id"] = this.userId;
		// 上期产品SN
		var prodId = this.currentSelectedProd.get("prod_id");
		var lastProd = this.baseData["lastOrderMap"][prodId];
		values["last_order_sn"] = lastProd ? lastProd["order_sn"] : null;
		
		//计算新增金额
		var boxProdTariff = Ext.getCmp("boxProdTariff");
		var disctId = boxProdTariff.getValue();
		var index = boxProdTariff.getStore().find("disct_id", disctId);
		
		// 如果当前是套餐，则封装分组用户
		if(this.isPkg()){
			// 获得选中的用户
			/**var groupUserMap = {};
			this.selectUserPanel.store.each(function(rs){
				var gid = rs.get("package_group_id");
				if(!groupUserMap[gid]){
					groupUserMap[gid] = [];
				}
				groupUserMap[gid].push(rs.get("user_id"));
			}, this);
			// 封装后台数据结构
			var groupSelected = [];
			for(var key in groupUserMap){
				groupSelected.push({
					"package_group_id": key,
					"userSelectList": groupUserMap[key]
				})
			}
			values["groupSelected"] = groupSelected;**/
			values["groupSelected"]=this.getGroupSelected();
		}
		
		// 产品信息
		values["prod_id"] = Ext.getCmp("boxProdId").getValue();
		values["tariff_id"] = Ext.getCmp("boxProdTariff").getValue();
		values["eff_date"] = Ext.getCmp("dfStartDate").getValue().format("Y-m-d H:i:s");
		
		return values;
	},
	getGroupSelected:function(){
		if(this.isPkg()){
			// 获得选中的用户
		var groupUserMap = {};
			this.selectUserPanel.store.each(function(rs){
				var gid = rs.get("package_group_id");
				if(!groupUserMap[gid]){
					groupUserMap[gid] = [];
				}
				groupUserMap[gid].push(rs.get("user_id"));
			}, this);
			// 封装后台数据结构
			var groupSelected = [];
			for(var key in groupUserMap){
				groupSelected.push({
					"package_group_id": key,
					"userSelectList": groupUserMap[key]
				})
			}
			return groupSelected;
		}
		return null;
	}
	,
	success : function(form,res){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

/** 入口 */
Ext.onReady(function(){
	TemplateFactory.gTemplate(new ProdOrderForm());
});
