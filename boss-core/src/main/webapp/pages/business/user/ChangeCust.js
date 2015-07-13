/**
 * 转户
 */
 
/**
 * 用于显示多个客户的信息，及选择要办理业务的客户
 */
CustSelectWindow = Ext.extend( Ext.Window , {
	//客户信息的store
	custStore: null,
	custGrid: null,
	constructor: function (store){
		this.custStore =  store;
		
		var cm = [
			{header: '受理编号', dataIndex: 'cust_no'},
			{header: '客户名称', dataIndex: 'cust_name'},
			{header: '客户地址', dataIndex: 'addr_id_text', width: 240,
				renderer: function(value,md,record){
					value = record.get('address');
					return value;
				}},
			{header: '网络类型', dataIndex: 'net_type_text',width: 80},
			{header: '客户状态', dataIndex: 'status_text',width: 80},
			{header: '客户类型', dataIndex: 'cust_type_text',width: 80},
			{header: '客户级别', dataIndex: 'cust_level_text',width: 80},
			{header: '黑名单', dataIndex: 'is_black_text',width: 60},
			{id: 'autoCol', header: '开户时间', dataIndex: 'open_time'}
		];
		
		//实例化cust grid panel
		this.custGrid = new Ext.grid.GridPanel({
			stripeRows: true, 
	        store: this.custStore,
	        columns: cm,
	        bbar: new Ext.PagingToolbar({
	        	pageSize: 10,
				store: this.custStore
			})
	    })
		CustSelectWindow.superclass.constructor.call(this,{
			title: '选择客户',
			maximizable : false,
			width: 520,
			height: 400,
			layout: 'fit',
			border: false,
			closeAction : 'close',
			items: this.custGrid
		});
	},
	//注册事件
	initEvents: function(){
		this.custGrid.on("rowclick", function(grid ,index, e){
//			Confirm("确定要选择该客户吗?", this ,function(){
			var record = grid.getStore().getAt(index);
			Ext.getCmp('myCustInfoPanel').remoteRefresh(record.get('cust_id'));
			this.close();
//			});
		}, this);
		CustSelectWindow.superclass.initEvents.call(this);
	}
});

/**
 * 客户信息面板构造
 */
CustInfo = Ext.extend( Ext.Panel , {
	id : 'myCustInfoPanel',
	tpl: null,
	custData: {
 		// 客户信息 @type Object
 		cust: {},
 		// 联系人信息 @type Object
 		linkman: {},
 		//积分信息
 		bonuspoint: {},
 		//签约信息
 		acctBank : {}
 	}, 
	constructor: function(){
		this.tpl = App.getApp().main.infoPanel.getCustPanel().custInfoPanel.tpl;
		this.tpl.compile();
		CustInfo.superclass.constructor.call(this, {
			border: false,
			autoScroll:true,
			bodyStyle : Constant.TAB_STYLE,
			html: this.tpl.applyTemplate(this.custData)
		});
	},
	refresh: function(){
		if(this.custData.cust.cust_id == App.getData().custFullInfo.cust.cust_id){
			Alert("您查找的客户没变，请选择其他客户");
			return;
		}
		this.tpl.overwrite( this.body, this.custData);
	},
	remoteRefresh:function(custId){
		if(custId){
			Ext.Ajax.request({
				scope : this,
				url:  root + '/commons/x/QueryCust!searchCustById.action',
				params: { 
					"custId" : custId
				},
				success: function(res,ops){
					var data = Ext.decode(res.responseText);
					
					if(data){
						this.custData = data;
					}else{
						this.reset();
					}
					
					this.refresh();
	  			 }
			});
		}else{
			this.reset();
			this.refresh();
		}
		
	},
	reset : function(){
		this.custData.cust = {};
		this.custData.linkman = {};
		this.custData.acctBank = {};
	}
});

UserTransferPanel = Ext.extend(BaseForm,{
	userInfoPanel : null,
	custStore : null,
	custWin : null,
	constructor: function(){
		this.custStore = new Ext.data.JsonStore({
			url: root + '/commons/x/QueryCust!searchCust.action',
			root: 'records',
			totalProperty: 'totalProperty',
			fields: Ext.data.Record.create([
				"cust_id",
				"cust_no",
				"cust_name",
				"addr_id_text",
				"address",
				"status",
				"cust_type",
				"status_text",
				"cust_type_text",
				"cust_level_text",
				"net_type_text",
				"is_black_text",
				"open_time",
				"county_id",
				"county_name",
				"area_id"
			])
		});
		this.custStore.on('load',this.doLoadResult,this);
		
		this.userInfoPanel = new UserInfoPanel();
//		this.userInfoPanel.height = 190;
		
		var type_store = new Ext.data.SimpleStore({
			fields:['value', 'text'],
			data: [
				['cust_no','受理编号'],
				['cust_name','客户名称'],
				['device_id','设备编号']
			]
		});
		
		UserTransferPanel.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			border : false,
			labelWidth: 65,
			layout:'border',
			baseCls: 'x-plain',
			bodyStyle: Constant.TAB_STYLE,
			items: [this.userInfoPanel,{
				xtype : 'panel',
				region : 'center',	
				title : '新客户',
				layout : 'fit',
				border: false,
				autoScroll:true,
				bodyStyle: "background:#F9F9F9",
				items : [new CustInfo()],
				tbar : [' ',
					new Ext.form.ComboBox({
						id : 'searchType',
						store : type_store,
						hiddenName : 'value',
						modal : 'local',
						valueField: 'value',
						displayField: 'text', 
						value : 'cust_no',
						forceSelection : true,
						allowBlank:false
				}),'-',
					{
						xtype : 'textfield',
						id : 'cust.cust_name',
						allowBlank:false,
						height : 20,
						name:'cust.cust_name',
						listeners :{
							scope : this,
							"specialkey":function(_this,_e){
								if(13 == _e.getKey()){ 
									this.searchCust(this);
								}
							}
						}
				},{
					text : ' 查找',
					xtype : 'button',
					height : 20,
					iconCls : "icon-query",
					scope : this,
					handler : this.searchCust
				}]
			}]
		});
	},
	doLoadResult : function(_store, _rs, ops){
		switch (_rs.length){                                                                                                                                     
			case 0  : 	
						Alert("没有查询到符合条件的客户，请检查条件是否有误!",function(){
							Ext.getCmp('cust.cust_name').focus();
						});
					  	break ;
			case 1  :  
						Ext.getCmp('myCustInfoPanel').remoteRefresh(_store.getAt(0).get('cust_id'));
					  	break ; 
			default : 
						this.custWin = new CustSelectWindow( _store);
						this.custWin.show();
		}
	},
	searchCust : function(){
		if(!Ext.getCmp('cust.cust_name').getValue()){
			Alert("请输入查询值");
			return;
		}
		var searchType = Ext.getCmp('searchType').getValue();
		var ps = {
			"search_type": searchType,
			"search_value": Ext.getCmp('cust.cust_name').getValue()
		};
		this.custStore.baseParams = ps ;
		
		this.custStore.load({
			params: { start:0, limit: 10}
		});
	},
	url: Constant.ROOT_PATH + "/core/x/User!transferUsers.action",
	getValues: function(){
		var o = {};
		//客户ID
		o["custId"] = Ext.getCmp('myCustInfoPanel').custData.cust.cust_id;
		return o ;
	},
	doValid: function(){
		if(!Ext.getCmp('myCustInfoPanel').custData.cust.cust_id){
			return "请指定新的客户";
		}
		UserTransferPanel.superclass.doValid.call(this);
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var nuf = new UserTransferPanel();
	var box = TemplateFactory.gTemplate(nuf);
});