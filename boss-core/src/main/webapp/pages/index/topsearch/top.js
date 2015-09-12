/**
 * 顶部面板的组件封装
 **/
 
 
 /**
 * 为搜索面板初始化事件
 */
TopSearch = function(){}
CUST_PAGE_SIZE = 10 ;

Ext.apply(TopSearch.prototype , {
	
	//搜索客户使用的store
	searchStore: null,

	current : null ,
	init : function(){
		//实例化store
		this.searchStore = new Ext.data.JsonStore({
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
				"open_time"
			])
		});
		//添加处理查询结果的函数
		this.searchStore.on("load", this.doProcessResult ,this);
		
		var tabList = Ext.get('TabList');
		var lis = tabList.dom.getElementsByTagName("LI");
		var scope = this;
		for(var i=lis.length-1 ;i>=0;i--){
			if(i == lis.length -1){
				var ele = Ext.get(lis[i]);
				if(ele.hasClass("selected")){
					this.current = ele ;
					this.setType( this.current );
				}
				var a = ele.child("A");
				a.on("click" , function( e , _ele ,o){
					new SearchCustWindow().show()
				});
			}else{
				var ele = Ext.get(lis[i]);
				if(ele.hasClass("selected")){
					this.current = ele ;
					this.setType( this.current );
				}
				var a = ele.child("A");
				a.on("click" , function( e , _ele ,o){
					this.blur();
					var _te = this.findParent("LI" , 2 , true) ;
					if(scope.current == _te){
						return false ;
					}
					scope.toggleSelected( _te );
					scope.setType( _te );
				});
			}
		}
		
	},
	toggleSelected: function( newNode ){
		this.current.removeClass("selected");
		newNode.addClass("selected");
		this.current = newNode ;
	},
	setType: function( newNode ){
		Ext.getDom('type').value = newNode.child("A").getAttributeNS("boss","name");
	},
	getSelected: function(){
		return this.current;
	},
	// 是否正在加载标示
	loading: false,
	//搜索客户
	searchCust: function(value,name){
		Ext.getBody().mask("正在查询，请稍等...","x-mask-loading");
		
		var q = Ext.getDom('q').value;
		var t = Ext.getDom("type").value;
		if(!Ext.isEmpty(value)&&!Ext.isEmpty(name)){
			q = value;
			t = name;
		}
		if(/.*\u0027.*/gi.test(q.trim())){
			Alert('请不要输入单引号');
			return;
		}
		
		var searchValue = q.trim();
		if(searchValue.length == 0){
			Ext.getBody().unmask();
			Alert('查询客户请输入至少一个关键字');
			return;
		}
		
		var ps = {};
		if(t == 'cust'){
			if(/^[0-9]*$/.test(searchValue)){
				t = 'cust_no';
			}else{
				t = 'cust_name'
			}
		}
		
		ps = {
			"search_type": t,
			"search_value": searchValue
		};
		
		this.searchStore.baseParams = ps ;
		if(this.loading){
			return;
		} else {
			this.loading = true;		
		}
		this.searchStore.load({
			params: { start:0, limit: CUST_PAGE_SIZE }
		});
	},
	//处理查询结果的函数
	doProcessResult: function(_store, _rs, ops){
		
		switch (_rs.length){
			case 0  : 	
						Alert("没有查询到符合条件的客户，请确认后重新输入!",function(){
							Ext.getDom('q').focus();
						});
					  	break ;
			case 1  :   
						this.doRefreshCust( _rs[0].data );
						if(Ext.getCmp('SearchCustWindow')){
							Ext.getCmp('SearchCustWindow').close();
						}
					  	break ; 
			default : 
						if(Ext.getCmp('SearchCustWindow')){
							Ext.getCmp('SearchCustWindow').close();
						}
						if(!this.custWin)
							this.custWin = new ChooseCustWindow( _store, this );
						this.custWin.show();
		}

		Ext.getBody().unmask();
		this.loading = false;
	},
	//刷新客户信息
	doRefreshCust: function( data ){
		App.data.custFullInfo.cust = data;
		App.main.infoPanel.changeDisplay();
		App.main.infoPanel.activeDefaultTab();
		
		
		App.main.infoPanel.getCustPanel().refresh();
		App.main.infoPanel.getUnitPanel().refresh();
	  	App.main.infoPanel.getUserPanel().refresh();
//	  	App.main.infoPanel.getAcctPanel().refresh();
	  	App.refreshFeeView();
	  	App.refreshPayInfo();
	    
	    App.main.infoPanel.getCustPanel().setReload(false);
	    App.main.infoPanel.getUnitPanel().setReload(false);
	  	App.main.infoPanel.getUserPanel().setReload(false);
//	  	App.main.infoPanel.getAcctPanel().setReload(false);
	  	App.main.infoPanel.getPayfeePanel().setReload(true);
	  	App.main.infoPanel.getBillPanel().setReload(true);
	  	App.main.infoPanel.getDocPanel().setReload(true);
	    App.main.infoPanel.getDoneCodePanel().setReload(true);
	    App.main.infoPanel.getCommandInfoPanel().setReload(true);
	  	FilterWindow.clearRelatedCmpsTitle();
	},
	searchCustUnPrint:function(v){
		if (App.getApp().isPrintInvoice()=='F'){
			//必须打印发票，检查未打印发票
			App.showTip();
			Ext.Ajax.request({
				url: root + '/commons/x/QueryCust!queryUnPrintCustByOptr.action',
				scope:this,
				success : function(res, ops) {
					//去除数据加载提示框
					App.removeTip();
					
					var rs = Ext.decode(res.responseText);
					if(!Ext.isEmpty(rs)){
						Alert('请处理未支付或未打印发票客户，点击确定后跳转，请为该客户支付费用和打印发票',function(){
							App.getData().paySearch  = false;
							this.searchCust(rs,'cust_no');
							//App.openPrint();
						},this);
					}else{
						if(v==true){
							App.getData().paySearch = true;
						}else{
							App.getData().paySearch = false;
						}
						this.searchCust();
					}
				}
			});
		}else{
			if(v==true){
				App.getData().paySearch = true;
			}else{
				App.getData().paySearch = false;
			}
			//this.queryAdjustByOptr();
			this.searchCust();
		}
	},
	queryAdjustByOptr: function(){
		this.searchCust();
//		Ext.getBody().mask("正在查询，请稍等...","x-mask-loading");
//		//当前操作员下是否有客户 执行调账可退后未退款
//		Ext.Ajax.request({
//			url: root + '/commons/x/QueryCust!queryUnRefundByOptr.action',
//			scope:this,
//			success: function(res, ops) {
//				Ext.getBody().unmask();
//				var rs = Ext.decode(res.responseText);
//				if(!Ext.isEmpty(rs)){
//					Alert('请处理调账可退后未退款的客户，点击确定后跳转，请为该客户退款',function(){
//						this.searchCust(rs,'cust_no');
//					},this);
//				}else{
//					this.searchCust();
//				}
//			}
//		});
	}
});

SearchCustWindow = Ext.extend( Ext.Window , {
	form : null,
	searchStore : null,
	constructor : function(){
		//实例化store
		this.searchStore = App.search.searchStore;
		
		this.form = new Ext.form.FormPanel({
			layout : 'form',
			bodyStyle : Constant.TAB_STYLE,
			border : false,
			fieldLabel : 70,
			labelWidth:80,
			defaults : {
				xtype : 'textfield'
			},
			items : [{
				fieldLabel : '客户名称',
				width : 140,
				name : 'cust.cust_name'
			},{
				fieldLabel : '意向客户',
				name:'cust.status',
				xtype:'checkbox',
				inputValue:'PREOPEN'
			},{
				fieldLabel : '客户地址',
				width : 140,
				name : 'cust.address'
			},{
				fieldLabel : '账号',
				width : 140,
				name : 'cust.login_name'
			}]
		});
		
		SearchCustWindow.superclass.constructor.call(this,{
			title : '客户查询',
			id : 'SearchCustWindow',
			maximizable : false,
			layout : 'fit',
			width : 300,
			height : 200,
			closeAction : 'close',
			items : [this.form],
			buttons : [{
				text : '搜索',
				scope : this,
				iconCls : 'query',
				handler : this.doSearch
			}, {
				text : '关闭',
				scope : this,
				handler : function() {
					this.close();
				}
			}]
		});
	},
	doSearch : function(){
		if(!this.form.getForm().isValid()){
			return;
		}
		var all = this.form.getForm().getValues();
		if(Ext.isEmpty(all['cust.cust_name']) && Ext.isEmpty(all['cust.address']) && Ext.isEmpty(all['cust.login_name']) && Ext.isEmpty(all['cust.status'])){
			Alert('请任填一项进行搜索!')
		}else{
			all['search_type'] = 'MULTIPLE';
			this.searchStore.baseParams = all;
			this.searchStore.load({
				params: { start:0, limit: CUST_PAGE_SIZE }
			});
		}
	}
})

/**
 * 用于显示多个客户的信息，及选择要办理业务的客户
 */
ChooseCustWindow = Ext.extend( Ext.Window , {
	
	//客户信息的store
	custStore: null,
	//TopSearch 对象的引用
	parent: null,

	custGrid: null,
	
	constructor: function ( store , parent){
		this.custStore =  store;
		this.parent = parent;
		
		var cm = [
			{header: '受理编号', dataIndex: 'cust_no',width:85},
			{header: '客户名称', dataIndex: 'cust_name',width:80,renderer:App.qtipValue},
			{header: '客户地址', dataIndex: 'addr_id_text', width: 240,
				renderer: function(value,md,record){
					value = record.get('address');
					return value;
				}},
			{header: '客户状态', dataIndex: 'status_text',width: 60},
			{header: '客户类型', dataIndex: 'cust_type_text',width: 70},
			{header: '客户级别', dataIndex: 'cust_level_text',width: 70},
			{header: '黑名单', dataIndex: 'is_black_text',width: 50},
			{id: 'autoCol', header: '开户时间', dataIndex: 'open_time',renderer:App.qtipValue}
		];
		
		//实例化cust grid panel
		this.custGrid = new Ext.grid.GridPanel({
			stripeRows: true, 
      		autoExpandColumn: 'autoCol',
	        store: this.custStore,
	        columns: cm,
	        bbar: new Ext.PagingToolbar({
	        	pageSize: CUST_PAGE_SIZE,
				store: this.custStore
			})
	    })
		ChooseCustWindow.superclass.constructor.call(this,{
			title: '选择客户',
			width: 600,
			height: 400,
			layout: 'fit',
			border: false,
			items: this.custGrid
		});
	},
	//注册事件
	initEvents: function(){
		this.custGrid.on("rowclick", function(grid ,index, e){
			Confirm("确定要选择该客户吗?", this ,function(){
				var record = grid.getStore().getAt(index);
//				this.parent.doRefreshCust( record.data );
				App.search.doRefreshCust( record.data );
				this.hide();
			});
		}, this);
		ChooseCustWindow.superclass.initEvents.call(this);
	}
});
 
 