/**
 * 【用户产品订购】
 */
ProdOrderForm = Ext.extend( BaseForm, {
	url : Constant.ROOT_PATH+"/core/x/User!createUser.action",
	
	selectUserWindow: null,
	switchUser: null,
	
	transferPayWindow: null,
	
	constructor: function(p){
		// 续订下拉框
		this.storeWithValidOrderList = new Ext.data.JsonStore({
			url :root + '/commons/x/Order!loadGoonOrderList.action',
			fields : ['order_sn','order_desc'],
			autoLoad: true,
			listeners: {
				scope: this,
				beforeload: function(s, ops){
					//  cust_id:  客户编号必填
					//  single_user_id : 单用户入口填对应的user_id；通用入口不填
				}
			}
		});
		
		// 升级下拉框数据源
		this.storeWithUpgradeOrderList = new Ext.data.JsonStore({
			url :root + '/commons/x/Order!loadUpOrderList.action',
			fields : ['order_sn','order_desc'],
			autoLoad: true,
			listeners: {
				scope: this,
				beforeload: function(s, ops){
					//  cust_id:  客户编号必填
					//  single_user_id : 单用户入口填对应的user_id；通用入口不填
				}
			}
		});
		
		// 产品下拉框数据源
		this.storeWithProdList = new Ext.data.JsonStore({
			url :root + '/commons/x/Order!loadProdList.action',
			fields : ['prod_id','prod_name'],
			autoLoad: true,
			listeners: {
				scope: this,
				beforeload: function(s, ops){
					//cust_id:
					//single_user_id : 单用户入口填对应的user_id；通用入口不填
					//up_order_sn: 有选升级则填上
					//goon_order_sn:有选升级则填上
				}
			}
		}); 
		
		// 资费下拉框
		this.storeWithTariffList = new Ext.data.JsonStore({
			url :root + '/commons/x/Order!loadTariff.action',
			fields : ['tariff_id','disct_id',"disct_name","tariff_name"],
			autoLoad: true,
			listeners: {
				scope: this,
				beforeload: function(s, ops){
					//  cust_id:
					//  prod_id:
					//  user_id: prod_id是套餐时不填，否则填用户栏选中的user_id
				}
			}
		});  
		
		// 选择用户
		var that = this;
		this.switchUser = new Ext.form.TriggerField({
			fieldLabel: "选择用户",
			emptyText: '打开用户窗口选择用户..',
			editable: false,
			onTriggerClick: function(e){
				that.openSwitchUserWindow();
			}
		});
		
		ProdOrderForm.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			autoScroll:true,
            labelWidth:100,
            border: false,
			bodyStyle:'background:#F9F9F9;padding-top:4px',
			defaults: {
				xtype:'panel',
				anchor:'100%',
				layout:'column',
				bodyStyle:'background:#F9F9F9;padding-top:4px',
				baseCls: 'x-plain',
				defaults: { 
					layout: 'form',
					baseCls: 'x-plain',
					columnWidth:0.5,
					anchor: '100%',
					labelWidth:100,
					defaults: {width: 130}
				}
			},
			items:[/*{
				xtype: 'checkboxgroup',
				fieldLabel: '类型过滤',
			    anchor: "92%",
			    items: [
			        {boxLabel: '有线OTT', name: 'p_prod.serv_id',value: 'OTT'},
			        {boxLabel: '无线OTT', name: 'p_prod.serv_id', value: "OTT_MOBILE", checked: true},
			        {boxLabel: 'DTT', name: 'p_prod.serv_id', value: "DTT"},
			        {boxLabel: 'BAND', name: 'p_prod.serv_id',value: "BAND"},
			        {boxLabel: '普通套餐', name: 'p_prod.prod_type', value: 'cpkg'},
			        {boxLabel: '协议套餐', name: 'p_prod.prod_type', value: 'spkg'}
			    ]
			},{
				items: [{
					items:[{
						xtype: 'paramcombo',
						fieldLabel: "续订",
					    emptyText: "未到期的订购记录.."
					},]
				},{
					items:[{
			            xtype: 'paramcombo',
			            fieldLabel: '升级',
			            emptyText: "可升级的订购记录."
					}]
				}]
			},*/{
				items: [{
					items:[{
						xtype: 'paramcombo',
					    fieldLabel: "选择产品"
					},]
				},{
					items:[{
			            xtype: 'displayfield',
			            fieldLabel: '产品描述',
			            value: 'balabalabala..'
					}]
				}]
			},{
				layout: 'form',
				anchor: "92.3%",
				items: [this.switchUser]
			},{
				items: [{
					items:[{
						xtype: 'paramcombo',
					    fieldLabel: "产品资费"
					},]
				},{
					items:[{
			            xtype: 'spinnerfield',
			            fieldLabel: '订购月数',
			            value: 3,
			            minValue: 1,
			            maxValue: 100
					}]
				}]
			},{
				xtype: 'displayfield',
				fieldLabel: '上期订购结束日',
				labelWidth: 120,
				value: '2015-07-30'
			},{
				items: [{
					items:[{
						xtype: 'datefield',
					    fieldLabel: "开始计费日",
					    minValue: new Date()
					},]
				},{
					items:[{
			            xtype: 'datefield',
			            fieldLabel: '结束计费日',
			            minValue: new Date()
					}]
				}]
			},{
				items: [{
					columnWidth: .7,
					xtype: "panel",
					bodyStyle: 'padding: 10px 0 10px 30px; color: red',
					html: "<b> * 收费小计:$100.00（新增订购:<b>58.00</b> + <a id='transferHrefTag' href='#'>转移支付:<b>42.00</b></a>）</b>"
				},{
					columnWidth: .223,
					xtype: 'button',
					height: 30,
					style: 'margin-top: 3px;',
					iconCls: 'icon-add',
					text: '加入列表',
					scope: this,
					handler: this.doAddProdList
				}]
			}]
		});
	},
	doInit:function(){
		ProdOrderForm.superclass.doInit.call(this);
		Ext.get("transferHrefTag").on("click", function(e, t, o){
			if(!this.transferPayWindow){
				this.transferPayWindow = new TransferPayWindow();
			}
			this.transferPayWindow.show();
		}, this);
	},
	openSwitchUserWindow: function(){
		if(!this.selectUserWindow){
			this.selectUserWindow = new SelectUserWindow();
		}
		this.selectUserWindow.show(this.switchUser);
	},
	// 上期订购结束日
	loadLastOrderExpDate: function(){
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/Order.loadLastOrderExpDate.action',
			params : { /*cust_id: prod_id: user_id:prod_id是套餐则填空，否则填用户栏选中的user_id */},
			success : function(response,opts){
				var obj = Ext.decode(response.responseText);
				alert(response.responseText);
			}
		});
	},
	// 开始计费日
	loadLastOrderExpDate: function(){
		Ext.Ajax.request({
			scope : this,
			url : root + '/commons/x/Order.loadOrderEffDate.action',
			params : { /*cust_id: prod_id: user_id: prod_id是套餐则填空，否则填用户栏选中的user_id last_order_sn:上期订购结束日获得的order_sn order_months: 订购月数  */},
			success : function(response,opts){
				var obj = Ext.decode(response.responseText);
				alert("date: " + response.responseText);
			}
		});
	},
	//加入产品列表
	doAddProdList: function(){
		alert("加入产品列表..");
	},
	doValid : function(){
		return ProdOrderForm.superclass.doValid.call(this);
	},
	getValues : function(){
		var values = {};
		return values;
	},
	getFee: function(){
		return 0;
	},
	success : function(form,res){
		var userId = res.simpleObj;
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

// 产品临时列表
SelectedProdGrid = Ext.extend(Ext.grid.GridPanel, {
	store: new Ext.data.JsonStore({
		fields: ["cust_id","user_id","user_name","user_selected","prod_id", "prod_name","tariff_id","tariff_name","last_order_sn","last_exp_date"]
	}),
	constructor: function(){
		var sm = new Ext.grid.CheckboxSelectionModel();
		
		var columns = [sm,
    	    {header: "产品名称", width: 100,sortable:true, dataIndex: 'prod_name'},
    	    {header: "资费", width: 80, sortable:true, dataIndex: 'traff_name'},
    	    {header: "用户", width: 100, sortable:true, dataIndex: 'user_ids'},
    	    {header: "订购月数", width: 60, sortable:true, dataIndex: 'month_count'},
    	    {header: "金额", width: 60, sortable:true, dataIndex: 'fee'},
    	    {header: "开始计费日", width: 80, sortable:true, dataIndex: 'start_date'},
    	    {header: "结束计费日", width: 80, sortable:true, dataIndex: 'start_date'},
    	    {header: "上期结束日", width: 80, sortable:true, dataIndex: 'last_order_end_date'}
    	];
		
		return SelectedProdGrid.superclass.constructor.call(this, {
			stripeRows: true,
			region: "center",
			layotu: "fit",
			title: "已定产品列表",
			columns: columns,
	        stateful: true,
	        sm: sm,
	        bbar: [{
	        	text: '移除选中',
	        	iconCls: 'icon-del'
	        }]
		});
	}
});

/** 入口 */
Ext.onReady(function(){
	var main = new Ext.Panel({
		layout: "border",
		defaults: {border: false},
		items:[{
			region: "north",
			height: 200,
			layout: "fit",
			items: new ProdOrderForm()
		}, new SelectedProdGrid()]
	});
	var box = TemplateFactory.gTemplate(main);
});