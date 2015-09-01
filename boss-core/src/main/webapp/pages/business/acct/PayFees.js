PaymentPanel = Ext.extend(Ext.Panel,{
	form:null,//公共账目form
	grid:null,//专用账目grid
	publicAcctitem:[],//公共账目数据集合
	specAcctitem:[],//专用账目数据集合
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
		if(this.publicAcctitem.length == 0){
			this.items.removeAt(0);
			this.items.itemAt(0).anchor='100% 100%';
			this.doLayout();
		}else{
			rows = this.publicAcctitem.length%2==0?this.publicAcctitem.length/2:(this.publicAcctitem.length+1)/2
			this.items.itemAt(0).anchor='100% '+(rows*7+6)+'%';
			this.items.itemAt(1).anchor='100% '+(100-(rows*7+6))+'%';
		}
//		if(this.specAcctitem.length == 0){
//			this.items.removeAt(1);
//			this.items.itemAt(0).anchor='100% 100%';
//			this.doLayout();
//		}
	},
	doInit:function(){
		//用户选中的账户行
//		var records = App.getApp().main.infoPanel.acctPanel.acctGrid.getSelectionModel().getSelections();
		
//		//账户面板store
//		var acctstore = App.getApp().main.infoPanel.acctPanel.acctGrid.getStore();
//		
//		acctstore.each(function(record){
//			var acctItems = record.get('acctitems');
//			if(record.get('acct_type') =='PUBLIC' && record.get('status') != 'DATACLOSE'){
//				if(acctItems && acctItems.length>0){
//					for(var k=0;k<acctItems.length;k++){
//						if(acctItems[k]["allow_pay"]=='T'){//当前账户是否允许缴费
//							//给当前账目附加2个属性:账户类型,用户ID
//							acctItems[k]["acct_type_text"] = acctIds[j][1];
//							acctItems[k]["user_id"] = acctIds[j][2];
//							acctItems[k]["user_name"] = acctIds[j][3];
//							acctItems[k]["user_type_text"] = acctIds[j][4];
//							acctItems[k]["stb_id"] = acctIds[j][5];
//							acctItems[k]["card_id"] = acctIds[j][6];
//							acctItems[k]["modem_mac"] = acctIds[j][7];
//							this.publicAcctitem.push(acctItems[k]);
//						}
//					}
//					break;
//				}
//			}
//		},this);
		
		
		if(this.publicAcctitem.length>0){
			this.form = new PublicPaymentForm(this.publicAcctitem);
		}else{
			this.form = {};
		}
		
		this.grid = new SinglePayFeesGrid(this.specAcctitem);
//		if(this.specAcctitem.length>0){
//			this.grid = new SinglePayFeesGrid(this.specAcctitem);
//		}else{
//			this.grid = {};
//		}
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
			},{
				 region: "south",
				 height: 80, 
				 buttonAlign:'center',
				 flex:1,
       			 frame:true,  
				 labelAlign:'right',  
				 layout:'column',
				 labelWidth:50,  
				 border: false,
		         items:[{ 
		         	columnWidth:.55,
		         	xtype:'fieldset',  
				    height: 60, 
				    title:'产品费',
         			style:'margin-left:10px;padding: 10px 0 10px 10px; color: red',
         			layout:'column',
         			items:[{
         				columnWidth:.50,
         				items:[{
         						bodyStyle:'padding-top:4px',
		         				html: "* 应收$:<span id='totalAmount'>--</span>"
			         			}]
         				},{
         				columnWidth:.50,
         				layout : 'form',
         				labelWidth:75,  
         				items:[{
								fieldLabel : '处理方式',
								id : 'payFeeTypeId',
								name:'order_fee_type',
								allowBlank : false,
								xtype:'paramcombo',
								width: 80,
								emptyText: '请选择',
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
		         },{  
				    columnWidth:.45,
		         	xtype:'fieldset',  
		         	height: 60, 
		         	title:'业务费',
		         	style:'margin-left:10px;padding: 10px 0 10px 10px; color: red',
		         	items:[{
		         		bodyStyle:'padding-top:4px',
						html: "* 应收$:<span id='totalAmount'>--</span>"
		         	}]
		         }] 
			}]
		});
	},
	success:function(){
		//刷新支付
		App.getApp().refreshPayInfo(parent);
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
		delete this.panel;
		delete this.data;
	},
	getValues: function(){
		var all = {'payFeesData':Ext.encode(this.data),'busi_code':App.getData().currentResource.busicode};
		return all;
	},
	doValid: function(){
		this.url = Constant.ROOT_PATH+"/core/x/ProdOrder!savePayFee.action"
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
	}

});

/**
 * 转移支付
 */
TransferPayWindow = Ext.extend(Ext.Window, {
	store: new Ext.data.JsonStore({
		fields: ["prod_name","tariff_name", "user_name", "month_count", "active_fee", "last_order_end_date", "eff_date", "exp_date"]
	}),
	constructor: function(){
		var columns = [
       	    {header: "产品名称", width: 100,sortable:true, dataIndex: 'prod_name'},
       	    {header: "资费", width: 70, sortable:true, dataIndex: 'tariff_name'},
       	    {header: "用户", width: 60, sortable:true, dataIndex: 'user_name'},
       	    {header: "开始计费日", width: 80, sortable:true, dataIndex: 'eff_date',renderer: Ext.util.Format.dateFormat},
       	    {header: "结束计费日", width: 80, sortable:true, dataIndex: 'exp_date',renderer: Ext.util.Format.dateFormat},
       	    {header: "转移金额$", width: 60, sortable:true, dataIndex: 'active_fee',renderer: Ext.util.Format.convertToYuan}
       	];
		// Window Construct instance
		return TransferPayWindow.superclass.constructor.call(this, {
			layout:"fit",
			title: "转移支付明细",
			width: 450,
			height: 200,
			resizable: false,
			maximizable: false,
			closeAction: 'hide',
			minimizable: false,
			items: [{
				xtype: 'grid',
				stripeRows: true,
				border: false,
				store: this.store,
				columns: columns,
		        stateful: true
			}]
		});
	},
	show: function(data, effDate){
		this.store.loadData(data);
		this.setTitle("转移明细（开始计费日：" + effDate + "）");
		return TransferPayWindow.superclass.show.call(this);
	}
});



Ext.onReady(function(){
	var payFees = new PayFeesForm();
	TemplateFactory.gTemplate(payFees);
})