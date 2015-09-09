/**
 * 缴费
 */
SinglePayFeesGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	payFeesStore:null,
	disctCombo:null,
	transferPayWindow: null,
	//转移支付数据明细
	transferPayData: [],
	totalAmount: 0,
	busiFeeAmount:0,	
	feeName:null,
	busiFeeDate:null,
	busiFee:null,
	constructor:function(data){
		payFeeThis = this;
		this.payFeesStore = new Ext.data.GroupingStore({
				url: Constant.ROOT_PATH + '/core/x/ProdOrder!queryFollowPayOrderDto.action', 
				reader: new Ext.data.JsonReader({ 
                    fields:[
                    	{name:'acct_id',mapping:'acct_id'},
                    	{name:'acctitem_id',mapping:'acctitem_id'},
                    	{name:'user_id',mapping:'user_id'},
                    	{name:'cust_id',mapping:'cust_id'},
                    	{name:'prod_id',mapping:'prod_id'},
						{name:'tariff_id',mapping:'tariff_id'},
						{name:'tariff_name',mapping:'tariff_name'},
						{name:'disct_id',mapping:'disct_id'},
						{name:'disct_name',mapping:'disct_name'},							
						{name:'user_name',mapping:'user_name'},
						{name:'user_type_text',mapping:'user_type_text'},
						{name:'stb_id',mapping:'stb_id'},
						{name:'card_id',mapping:'card_id'},
						{name:'modem_mac',mapping:'modem_mac'},
						{name:'order_sn',mapping:'order_sn'},
						{name:'acctitem_name',mapping:'acctitem_name'},
						{name:'status',mapping:'status'},
						{name:'fee',mapping:'fee'},
						{name:'fee_date',dataFormat:'Y-m-d',mapping:'fee_date'},
						{name:'exp_date',dataFormat:'Y-m-d',mapping:'exp_date'},
						{name:'exp_date_back',dataFormat:'Y-m-d',mapping:'exp_date'},
						{name:'active_fee',mapping:'active_fee'},
						{name:'order_time',dataFormat:'Y-m-d',mapping:'order_time'},
						{name:'status_text',mapping:'status_text'},
						{name:'eff_date',dataFormat:'Y-m-d',mapping:'eff_date'},
						{name:'rent',mapping:'rent'},{name:'billing_type',mapping:'billing_type'},
						{name:'feeOther',mapping:'feeOther'},//缴费金额备份字段
						{name:'billing_cycle',mapping:'billing_cycle'},
						{name:'fee_month',mapping:'fee_month'},//自定义字段
						{name:'pay_month',mapping:'pay_month'},
						{name:'prod_type_text',mapping:'prod_type_text'},
						{name:'prod_type',mapping:'prod_type'},
						{name:'canFollowPay',mapping:'canFollowPay'},
						{name:'currentTariffStatus',mapping:'currentTariffStatus'},
						{name:'tariffList',mapping:'tariffList'},
						{name:'terminal_type',mapping:'terminal_type'},
						{name:'terminal_type_text',mapping:'terminal_type_text'},
						{name:'protocol_date',dataFormat:'Y-m-d',mapping:'protocol_date'},
						{name:'prod_name',mapping:'prod_name'},
						{name:'tariff_name_next',mapping:'tariff_name'},
						{name:'tariff_id_next',mapping:'tariff_id'},
						{name:'busiFee',mapping:'busiFee'},
						{name:'busiFeeAmount',mapping:'busiFeeAmount'},
						{name:'busiFeeDate',mapping:'busiFeeDate'}
				//实际支付金额字段，转移支付字段
				,{name:'fee_back'},{name:'transfer_fee'},{name:'transfer_fee_back'},{name:'groupSelected',mapping:'groupSelected'}
				]}),
			groupField:'user_id'//分组字段(根据账目分组)
		});

		this.payFeesStore.load({
			params:{
				cust_id:App.getData().custFullInfo.cust.cust_id
			}
		});
		
		this.payFeesStore.on('load',this.doLoadResult,this);
		
		var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : false
				},
				columns:[new Ext.grid.RowNumberer(),
					{header:'用户编号',dataIndex:'user_id',hidden:true},
					{header:'用户名称',dataIndex:'user_name',hidden:true},
					{header:'产品名称',dataIndex:'prod_name',width:160,renderer:App.qtipValue},
					{header:'原资费',dataIndex:'tariff_name',width:100,renderer:App.qtipValue},
					{id:'tariff_name_next_id',header:'新资费',dataIndex:'tariff_name_next',width:100,
						editor:new Ext.form.ComboBox({
							store:new Ext.data.JsonStore({
								fields:['disct_name','tariff_id']
							}),displayField:'disct_name',valueField:'disct_name',triggerAction:'all',
							scope:this,
							listeners:{
								scope:this,
								select:function(combo,record){
									this.getSelectionModel().getSelected().set('tariff_id_next',record.get('tariff_id'));
								}
							}})},
					{header:'原到期日',dataIndex:'exp_date',width:110,renderer:Ext.util.Format.dateFormat},
					{header:'计费期日',dataIndex:'eff_date',width:110,renderer:Ext.util.Format.dateFormat},		
					{id:'pay_month_id',header:'缴费月数',dataIndex:'pay_month',width:100,
						scope:this
						,editor: new Ext.form.NumberField({
							allowDecimals:false,//不允许输入小数 
			    			allowNegative:false,
//			    			minValue:1,//enableKeyEvents: true,
								scope:this,
								listeners:{
									specialKey : function(field, e) {
						                if (e.getKey() == Ext.EventObject.ENTER) {//响应回车
						                	var thiz = this.scope;
						                	if(thiz.getStore().getCount()>currentRow+1)
						                		setTimeout(function(){thiz.startEditing(currentRow,thiz.getColumnModel().getIndexById('pay_month_id'))},200);
						                }  
						            }
								}
							})
					},
					{header:'新到期日',dataIndex:'fee_month',width:120, renderer:Ext.util.Format.dateFormat},
					{header:'转移支付金额',dataIndex:'transfer_fee_back',width:100,renderer:function(value,metaData,record){
						that = this;
						if(value != ''){
							return '<div style="text-decoration:underline;font-weight:bold"  onclick="Ext.getCmp(\'SinglePayFeesId\').doTransferFeeShow();"  ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
						}else{
							return '<div ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
						}
					}},
					{header:'缴费金额',dataIndex:'fee_back',width:100}
				],
				scope:this,
				isCellEditable:this.isCellEditable
		});
		
		SinglePayFeesGrid.superclass.constructor.call(this,{
			title:'产品信息',
			store:this.payFeesStore,
			stripeRows:true,
			columnLines:true,
			cm:cm,
			id:'SinglePayFeesId',
			sm:new Ext.grid.RowSelectionModel({}),
			height:450,
			autoWidth:true,
			clicksToEdit:1,
			view: new Ext.grid.GroupingView({
	            forceFit:true,
	            groupTextTpl:'用户名称:{[values.rs[0].data["user_name"]]}'+
	            '{[values.rs[0].data["user_type_text"]?"&nbsp;&nbsp;用户类型:"+[values.rs[0].data["user_type_text"]]+" ":""]}',       
	            getRowClass: function(record,index){
		            if(record.get('canFollowPay') == false ){ 
		                return 'x-grid-record-green';  
	                }
	                return '';  
		        }  
	            
        	})
		});
	},
	doLoadResult : function(store){
		store.each(function(record){
			if(record.get('currentTariffStatus') != true){
				record.set('tariff_name_next','');
				record.set('tariff_id_next','');
			}
			var fee = record.get('busiFee');
			if(fee && payFeeThis.feeName == null){
				payFeeThis.feeName = fee.fee_name
			}
			
		})
		if(this.feeName){
			Ext.getCmp('orderFeeItemId').columnWidth = 0.40;
			Ext.getCmp('feesItemId').add({  
					    columnWidth:.6,
			         	xtype:'fieldset',  
			         	height: 75,
			         	id:'busiFeesItemId',
			         	title:'-',
			         	style:'margin-left:10px;padding: 10px 0 10px 10px; color: red',
			         	items:[{
			         		bodyStyle:'padding-top:4px',
							html: "* 应收$:<span id='busiFeeAmount'>--</span>"
			         	},{
			         		bodyStyle:'padding-top:4px',
							html: "时间段:<span id='busiFeeTime'>--</span>"
			         	}]
			         });
			Ext.getCmp('busiFeesItemId').setTitle(this.feeName);
			
		}else{
			Ext.getCmp('orderFeeItemId').columnWidth = 0.99;
		}
		Ext.getCmp('feesItemId').doLayout();
	},
	//是否可编辑
	isCellEditable:function(colIndex,rowIndex){
		var grid = this.scope;//当前grid引用
		var record = grid.getStore().getAt(rowIndex);//当前编辑行对应record
		if(colIndex == this.getIndexById('pay_month_id')){
			var canFollowPay = record.get("canFollowPay");			
			if(canFollowPay == false){
				return false;
			}
		}else if(colIndex == this.getIndexById('tariff_name_next_id')){
			if(record.get('currentTariffStatus') != true){
				var store = this.getCellEditor(colIndex,rowIndex).field.getStore();
				store.removeAll();//清空上一次选中行中 该列的数据
				var tariff = record.get('tariffList');
				if(tariff !=null){ 
					if(tariff.length>0){
						store.loadData(tariff);
					}
				}
				return false;
			}else{
				return false;
			}
		}
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	},
	initEvents : function(){
		SinglePayFeesGrid.superclass.initEvents.call(this);
		this.on('afterrender',function(){
			this.swapViews();
		},this,{delay:10});
		
		this.on("afteredit",this.afterEdit,this);
		this.on("beforeedit",this.beforeedit,this);
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
	afterEdit : function(obj){
		var record = obj.record;
		var fieldName = obj.field;//编辑的column对应的dataIndex
		var value = obj.value;
		if(fieldName=='pay_month'){
			if(!this.doAfterFindDateFee(record,value)){
				this.doIntNullValue(record);
				this.startEditing(obj.row,obj.column);
			}else{
			}
			
			
			if(record.get('busiFee')){
				this.busiFee = record.get('busiFee');
				this.doBusiFeeDate(record,value);
			}
			this.doAmountDate();
			
		}else if(fieldName = 'tariff_name_next'){
			
			
		}
	},
	doBusiFeeDate:function(record,value){
		var busiFee = record.get('busiFee');
	 	var startDate = Ext.util.Format.subStringLength(busiFee.last_prod_exp,10);
	 	var feeCount=busiFee.fee_count;
		var feeMonths = 0;//月份数
		var days = 0;//结束时间和开始计费时间相差天数
		//业务费开始计费日期=产品开始计费日期
		var dfExpDate = record.get('fee_month').format("Y-m-d");
		if(startDate == Ext.util.Format.subStringLength(record.get('eff_date'),10)){
			feeMonths = value;
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
		var amount = 0
		if(days != 0){
			var v = parseInt(days/30*100)/100;
			busiFeeAmount = v*feeCount*busiFee.default_value;
		}else{
			busiFeeAmount = feeMonths*feeCount*busiFee.default_value;
		}
		record.set('busiFeeAmount',busiFeeAmount);
		record.set('busiFeeDate',Date.parseDate(startDate,"Y-m-d").format("Ymd")+"-"+Date.parseDate(dfExpDate, "Y-m-d").format("Ymd"));
		
	},
	doAmountDate:function(record,value){
		var amount = 0,busiAmount = 0,busiDate="";
		this.getStore().each(function(record){
			if(!Ext.isEmpty(record.get('fee'))){
				amount = amount + parseInt(record.get('fee'));
			}
			if(!Ext.isEmpty(record.get('busiFeeAmount'))){
				busiAmount = busiAmount + parseInt(record.get('busiFeeAmount'));
			}
			if(!Ext.isEmpty(record.get('busiFeeDate'))){
				if(Ext.isEmpty(busiDate)){
					busiDate = record.get('busiFeeDate');
				}else{
					busiDate = busiDate+"," + record.get('busiFeeDate');
				}
			}
			
		})
		this.totalAmount = amount;
		this.busiFeeAmount = busiAmount;
		this.busiFeeDate = busiDate;
		Ext.get('totalAmount').update(Ext.util.Format.convertToYuan(this.totalAmount));
		Ext.get('busiFeeAmount').update(Ext.util.Format.convertToYuan(this.busiFeeAmount));
		Ext.get('busiFeeTime').update(this.busiFeeDate);
	},
	beforeedit:function(obj){
		var record = obj.record;
		var fieldName = obj.field;//编辑的column对应的dataIndex
		var value = obj.value;
		if(fieldName == 'pay_month'){
			//编辑行全局变量
			currentRow = obj.row;
			
		//套餐考虑 转移支付
		if(Ext.isEmpty(record.get('user_id'))){
			Ext.Ajax.request({
				url :root + '/core/x/ProdOrder!loadTransferFee.action',
				scope : this,
				params: { 
					"orderProd": Ext.encode(this.getTransferValues(record)),
					"busi_code": App.getData().currentResource.busicode
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
					record.set('transfer_fee', sumAmount);
					record.set('transfer_fee_back', Ext.util.Format.convertToYuan(sumAmount));
				}
			});
		}
		}
	},
	doTransferFeeShow:function(){
		if(!App.getApp().getCustId()){
			Alert('请查询客户之后再做操作.');
			return false;
		}
		var recs = this.selModel.getSelections();
		if(!recs || recs.length !=1){
			Alert('请选择且仅选择一条记录!');
			return false;
		}
		var rec = recs[0];
		
		if(!this.transferPayWindow){
			this.transferPayWindow = new TransferPayWindow();
		}
		if(this.transferPayData && this.transferPayData.length > 0){
			this.transferPayWindow.show(this.transferPayData, Date.parseDate(rec.get('eff_date'),"Y-m-d H:i:s").format('Y-m-d'));
		}else{
			Alert("没有转移支付项目!");
		}
	}	
	,doAfterFindDateFee:function(record,value){
		if(Ext.isEmpty(record.get('tariff_id_next'))){
			 Alert("原资费已经失效，请选新的资费!");  
			 return false;  
		}
		//计算开始计费期日
		var startDate =  this.getNextBeginDate(record.get('exp_date'));
		record.set('eff_date', startDate.format("Y-m-d H:i:s"));
		if(value != null){
			if(value >0 ){
				//输入正整数
				var re = /^[1-9]+[0-9]*]*$/;
			    if (!re.test(value)){  
			        Alert("请输入正整数");  
			        return false;  
			    }
			    //判断 包多月的 输入月数 是周期数的倍数
				var tariff = record.get('tariffList');
				var tariffRecord = null;
				for(var i=0;tariff.length;i++){
					if(tariff[i].tariff_id == record.get('tariff_id_next')){
						tariffRecord = tariff[i];
						break;
					}
				}
				if(tariffRecord.billing_cycle>1 && value%tariffRecord.billing_cycle != 0){
					Alert("请输入"+tariffRecord.billing_cycle+"的倍数");
					return false;
				}
				
				//计算金额
				var fee = value*tariffRecord.disct_rent/tariffRecord.billing_cycle;
				//转移支付金额
				var transferFee =  record.get('transfer_fee')?record.get('transfer_fee'):0;
				//实际缴费金额（计算金额-转移支付金额）
				var payfee = fee - transferFee;
				if(payfee<0){
					Alert("缴费金额"+Ext.util.Format.convertToYuan(fee)+"必须大于等于转移支付金额"+Ext.util.Format.convertToYuan(transferFee)+"！请重新输入月数！");
					return false;
				}else{
					record.set('fee', payfee);
					record.set('fee_back', Ext.util.Format.convertToYuan(payfee));
				}
				
				var invalidDate =  this.getNextBeginDate(record.get('exp_date'))
				invalidDate = Date.parseDate(Ext.util.Format.addMoth(invalidDate.format("Y-m-d"),value),"Y-m-d");
				
				invalidDate.setDate(invalidDate.getDate()-1)
				record.set('fee_month', invalidDate);
				
			}else if(value == 0){
				return false;
			}
			return true;
		}
	},
	doIntNullValue:function(record){
		record.set('pay_month','');
		record.set('fee_month', '');
		record.set('fee', '');
		record.set('fee_back', '');
	},
	getValues:function(){
		this.stopEditing();
		var arr=[],data;
		var store = this.getStore();
		store.each(function(record){
			data = record.data;
			//缴费月数大于0的时候，是进行缴费操作
			if (data['pay_month'] && data['pay_month']>0 ){
				var values = this.getTransferValues(record);
				values["order_months"] = record.get('pay_month');
				// 实际支付金额（小计金额）
				values["pay_fee"] =record.get('fee')?record.get('fee'):0;
				// 转移支付
				values["transfer_fee"] = record.get('transfer_fee')?record.get('transfer_fee'):0;
				// 失效日期
				values["exp_date"] = record.get('fee_month');
				values["order_fee_type"] = Ext.getCmp('payFeeTypeId').getValue();
				arr.push(values);
			}
		},this);
		return arr;
	},
	getBusiValues:function(){
		var feeInfo = [];
		if(this.busiFee && this.busiFeeAmount != 0){
			var obj = {};
			obj['fee_id'] = this.busiFee.fee_id;
			obj['fee_std_id'] = this.busiFee.fee_std_id;
			obj['count'] = this.busiFee.fee_count;
			obj['should_pay'] = this.busiFeeAmount;
			obj['real_pay'] = this.busiFeeAmount;
			obj['disct_info'] = this.busiFeeDate;
			feeInfo.push(obj);
		}
		return feeInfo;
	},
	getNextBeginDate:function(date){
		var invalidDate = new Date();
		//原到期日小于当天的，到期日按当天算
		if(this.compareTodayDate(date)){
			invalidDate = Date.parseDate(date,  "Y-m-d H:i:s");
			invalidDate.setDate(invalidDate.getDate()+1);
		}
		return invalidDate;
	}
	,
	compareTodayDate:function(date){
		var invalidDate = Date.parseDate(date,  "Y-m-d H:i:s");
		var nowDate = Date.parseDate(new Date().format('Y-m-d'),'Y-m-d');
		//原到期日小于当天的，到期日按当天算
		if(invalidDate.getTime() - nowDate >=0 ){
			return true;
		}else{
			return false;
		}
	},
	getTransferValues: function(record){
		var values = {};
		// 基础信息
		values["cust_id"] = record.get('cust_id');
		values["user_id"] = record.get('user_id');
		
		values["groupSelected"] = record.get('groupSelected');;
		
		var invalidDate = new Date();
		//原到期日小于当天的，到期日按当天算
		if(this.compareTodayDate(record.get('exp_date'))){
			values["last_order_sn"] = record.get('order_sn');
		}
		values["eff_date"] = this.getNextBeginDate(record.get('exp_date')).format("Y-m-d H:i:s");
		
		// 产品信息
		values["prod_id"] = record.get('prod_id');
		values["tariff_id"] = record.get('tariff_id_next');
		
		return values;
	}
});
