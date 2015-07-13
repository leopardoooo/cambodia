PaymentPanel = Ext.extend(Ext.Panel,{
	form:null,//公共账目form
	grid:null,//专用账目grid
	publicAcctitem:[],//公共账目数据集合
	specAcctitem:[],//专用账目数据集合
	batchPay : false,//批量缴费标志
	constructor:function(parent){
		this.parent = parent;
		this.doInit();
		PaymentPanel.superclass.constructor.call(this,{
			border:false,
			layout:'anchor',
			items:[
				{anchor:'100% 26%',border:false,layout:'fit',defaults:{border:false},items:[this.form]},
				{anchor:'100% 74%',border:false,layout:'fit',defaults:{border:false},items:[this.grid]}
			]
		});
		if(this.publicAcctitem.length == 0 || this.batchPay){
			this.items.removeAt(0);
			this.items.itemAt(0).anchor='100% 100%';
			this.doLayout();
		}else{
			rows = this.publicAcctitem.length%2==0?this.publicAcctitem.length/2:(this.publicAcctitem.length+1)/2
			this.items.itemAt(0).anchor='100% '+(rows*7+6)+'%';
			this.items.itemAt(1).anchor='100% '+(100-(rows*7+6))+'%';
		}
		if(this.specAcctitem.length == 0){
			this.items.removeAt(1);
			this.items.itemAt(0).anchor='100% 100%';
			this.doLayout();
		}
	},
	doInit:function(){
		//用户选中的账户行
		var records = App.getApp().main.infoPanel.acctPanel.acctGrid.getSelectionModel().getSelections();
		
		var dataArr = [];
		Ext.each(records,function(record){
			dataArr.push(record.data);
		},this);
		
		var acctIds=[];//用户选中的所有账户ID,账户类型,用户ID
		for(var i=0;i<dataArr.length;i++){
			acctIds.push([dataArr[i]['acct_id'],dataArr[i]['acct_type_text'],dataArr[i]['user_id']
				,dataArr[i]['user_name'],dataArr[i]['user_type_text'],dataArr[i]['stb_id']
				,dataArr[i]['card_id'],dataArr[i]['modem_mac']]);
		}
		
		//账户面板store
		var acctstore = App.getApp().main.infoPanel.acctPanel.acctGrid.getStore();
		
//		var publicAcctitem = [];
//		var specAcctitem = [];
		acctstore.each(function(record){
			for(var j=0;j<acctIds.length;j++){
				var acctItems = record.get('acctitems');
				if(record.get('acct_id') == acctIds[j][0]){
					if(record.get('acct_type') =='PUBLIC' && record.get('status') != 'DATACLOSE'){
						if(acctItems && acctItems.length>0){
							for(var k=0;k<acctItems.length;k++){
								if(acctItems[k]["allow_pay"]=='T'){//当前账户是否允许缴费
									//给当前账目附加2个属性:账户类型,用户ID
									acctItems[k]["acct_type_text"] = acctIds[j][1];
									acctItems[k]["user_id"] = acctIds[j][2];
									acctItems[k]["user_name"] = acctIds[j][3];
									acctItems[k]["user_type_text"] = acctIds[j][4];
									acctItems[k]["stb_id"] = acctIds[j][5];
									acctItems[k]["card_id"] = acctIds[j][6];
									acctItems[k]["modem_mac"] = acctIds[j][7];
									this.publicAcctitem.push(acctItems[k]);
								}
							}
							break;
						}
					} else if (record.get('acct_type') == 'SPEC' //待销户 ，休眠 不能进行缴费
							&& record.get('status') != 'DORMANCY' && record.get('status') != 'WAITLOGOFF') {
						if(acctItems && acctItems.length>0){
							for(var k=0;k<acctItems.length;k++){
								if(acctItems[k]["allow_pay"]=='T'&&acctItems[k]["prod_status"]!='REQSTOP'){//当前账户是否允许缴费
									//给当前账目附加2个属性:账户类型,用户ID
									acctItems[k]["acct_type_text"] = acctIds[j][1];
									acctItems[k]["user_id"] = acctIds[j][2];
									acctItems[k]["user_name"] = acctIds[j][3];
									acctItems[k]["user_type_text"] = acctIds[j][4];
									acctItems[k]["stb_id"] = acctIds[j][5];
									acctItems[k]["card_id"] = acctIds[j][6];
									acctItems[k]["modem_mac"] = acctIds[j][7];
									this.specAcctitem.push(acctItems[k]);
								}
							}
							break;
						}
					}
				}
			}
		},this);
		
		function getAcctItem(acctItems){
			var arr = [];
			for(var k=0;k<acctItems.length;k++){
				if(acctItems[k]["allow_pay"]=='T'){//当前账户是否允许缴费
					//给当前账目附加2个属性:账户类型,用户ID
					acctItems[k]["acct_type_text"] = acctIds[j][1];
					acctItems[k]["user_id"] = acctIds[j][2];
					acctItems[k]["user_name"] = acctIds[j][3];
					acctItems[k]["user_type_text"] = acctIds[j][4];
					acctItems[k]["stb_id"] = acctIds[j][5];
					acctItems[k]["card_id"] = acctIds[j][6];
					acctItems[k]["modem_mac"] = acctIds[j][7];
					arr.push(acctItems[k]);
				}
			}
			return arr;
		}
		
		
		if(this.publicAcctitem.length>0){
			this.form = new PublicPaymentForm(this.publicAcctitem);
		}else{
			this.form = {};
		}
		
		if(this.specAcctitem.length>0){
			if(!App.getApp().getData().batchPayFee){
				//不是批量缴费
				if(this.specAcctitem.length < 30){
					this.grid = new SinglePayFeesGrid(this.specAcctitem);
				}else{
					//普通计费大于30个账目，自动变为批量缴费
					this.grid = new SpecPanel(this.specAcctitem);
					this.batchPay = true;
				}
			}else{
				this.grid = new SpecPanel(this.parent,this.specAcctitem);
				this.batchPay = true;
			}
		}else{
			this.grid = {};
		}
	}
});

PayFeesForm = Ext.extend( BaseForm , {
	url : null,
//	url : Constant.ROOT_PATH+"/core/x/Acct!payFees.action",
	panel:null,
	data : [],//提交数据
	constructor: function(){
		this.panel = new PaymentPanel(this);
		PayFeesForm.superclass.constructor.call(this,{
			autoScroll:true,
			layout:'border',
            bodyStyle: Constant.TAB_STYLE,
            defaults:{
				layout: 'form',
           		border:false,
           		bodyStyle:'background:#F9F9F9;'
           	},
            items:[{
				region:'center',
				layout : 'fit',
				items:[this.panel]
			}]
		});
	},
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
		delete this.panel;
		delete this.data;
	},
	getValues: function(){
		var all = {'payFeesData':Ext.encode(this.data)};
		if(Ext.getCmp('shouldPay')){
			all['shouldPay'] = Ext.util.Format.formatToFen(Ext.getCmp('shouldPay').getValue()) ;
			all['realPay'] = Ext.util.Format.formatToFen(Ext.getCmp('realPay').getValue()) ;
		}
		return all;
	},
	doValid: function(){
		if(Ext.getCmp('shouldPay')){
			this.url = Constant.ROOT_PATH+"/core/x/Acct!payBatchFees.action"
			var shouldPay = Ext.util.Format.formatToFen(Ext.getCmp('shouldPay').getValue()) ;
			var realPay = Ext.util.Format.formatToFen(Ext.getCmp('realPay').getValue()) ;
			var lowestDisct=0;
			if('BAND'==Ext.getCmp('servId').getValue()){
				lowestDisct = App.getApp().getBandLowestDisct();
			}else{
				lowestDisct = App.getApp().getLowestDisct();
			}
			if(realPay/shouldPay < lowestDisct){
				var obj = {};
				obj['msg'] = '实收金额小于配置的最小的折扣，请重新输入';
				obj['isValid'] = false;
				return obj;
			}
		}else{
			this.url = Constant.ROOT_PATH+"/core/x/Acct!payFees.action"
		}
		this.data = [];
		//提取需要提交的字段集合
		this.createData();
		
		if(this.data.length==0){
			var obj = {};
			obj['msg'] = '请输入缴费金额';
			obj['isValid'] = false;
			return obj;
		}
		return PayFeesForm.superclass.doValid.call(this);
	},
	createData:function (){
		var publicFee,specFee;//公共、专用账户下所有缴费记录
		var p = this.panel;
		if(p.form.items)
			publicFee = p.form.getValues();
		//不是批量缴费，p.grid为GridPanel
		//是批量缴费，p.grid为Panel
		if(p.grid.colModel || p.grid.specGrid){
			specFee = p.grid.getValues();
		}
		
		if(publicFee && publicFee.length > 0){
			Ext.each(publicFee,function(obj){
				this.data.push(obj);
			},this);
		}
			
		if(specFee && specFee.length >0 ){
			Ext.each(specFee,function(obj){
				this.data.push(obj);
			},this);
		}
		this.data;
	},
	getFee:function(){
		if(!Ext.getCmp('realPay')){
			var num = 0;
			Ext.each(this.data,function(d){
				if(d['fee']){
					num +=d['fee'];
				}
			},this);
			return Ext.util.Format.formatFee(num);
		}else{
			return Ext.util.Format.formatFee(num) + parseFloat(Ext.getCmp('realPay').getValue());
		}
		
	}
});

Ext.onReady(function(){
	var payFees = new PayFeesForm();
	TemplateFactory.gTemplate(payFees);
})