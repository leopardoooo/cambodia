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
		Ext.getBody().mask(lbc("common.loadingText"),"x-mask-loading");
		
		var q = Ext.getDom('q').value;
		var t = Ext.getDom("type").value;
		if(!Ext.isEmpty(value)&&!Ext.isEmpty(name)){
			q = value;
			t = name;
		}
		if(/.*\u0027.*/gi.test(q.trim())){
			Alert(lmsg("searchCustTip2"));
			return;
		}
		
		var searchValue = q.trim();
		if(searchValue.length == 0){
			Ext.getBody().unmask();
			Alert(lmsg("searchCustTip1"));
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
						Alert(lmsg("searchNoCust"),function(){
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
	  	App.refreshFeeView();
	  	App.refreshPayInfo();
	    
	    App.main.infoPanel.getCustPanel().setReload(false);
	    App.main.infoPanel.getUnitPanel().setReload(false);
	  	App.main.infoPanel.getUserPanel().setReload(false);
	  	App.main.infoPanel.getPayfeePanel().setReload(true);
	  	App.main.infoPanel.getBillPanel().setReload(true);
	  	App.main.infoPanel.getDocPanel().setReload(true);
	    App.main.infoPanel.getDoneCodePanel().setReload(true);
	    App.main.infoPanel.getCommandInfoPanel().setReload(true);
	  	FilterWindow.clearRelatedCmpsTitle();
	},
	searchCustUnPrint:function(v){
		if (App.getApp().isPrintInvoice()=='T'){
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
						Alert(lmsg("searchToPayOrPrint"),function(){
							App.getData().paySearch  = false;
							Ext.getDom('q').value = '';
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

SearchCustWindow = Ext.extend( Ext.Window , {//复杂查询
	form : null,
	searchStore : null,
	constructor : function(){
		this.LU_CS = langUtils.bc('home.tools.CustSearch');
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
				fieldLabel : this.LU_CS['labelCustName'],
				width : 140,
				name : 'cust.cust_name'
			},{
				fieldLabel :this.LU_CS['labelStatus'],
				name:'cust.status',
				xtype:'checkbox',
				inputValue:'PREOPEN'
			},{
				fieldLabel : this.LU_CS['labelAddress'],
				width : 140,
				name : 'cust.address'
			},{
				fieldLabel : this.LU_CS['certNum'],
				width : 140,
				name : 'cust.net_type'
			},{
				fieldLabel : this.LU_CS['labelLoginName'],
				width : 140,
				name : 'cust.login_name'
			}]
		});
		
		SearchCustWindow.superclass.constructor.call(this,{
			title : this.LU_CS['_title'],
			id : 'SearchCustWindow',
			maximizable : false,
			layout : 'fit',
			width : 300,
			height : 250,
			closeAction : 'close',
			items : [this.form],
			buttons : [{
				text : langUtils.bc('home.searchBtns')[0],
				scope : this,
				iconCls : 'query',
				handler : this.doSearch
			}, {
				text : langUtils.bc('common.close'),
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
		if(Ext.isEmpty(all['cust.cust_name']) && Ext.isEmpty(all['cust.address']) && Ext.isEmpty(all['cust.login_name'])
			&& Ext.isEmpty(all['cust.status']) && Ext.isEmpty(all['cust.net_type'])){
			Alert(this.LU_CS['tipInputAnyField']);
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
			{header: lmain("cust.base.busiId"), dataIndex: 'cust_no',width:85},
			{header: lmain("cust.base.name"), dataIndex: 'cust_name',width:80,renderer:App.qtipValue},
			{header: lmain("cust.base.addr"), dataIndex: 'addr_id_text', width: 240,
				renderer: function(value,md,record){
					value = record.get('address');
					return value;
				}},
			{header: lmain("cust.base.status"), dataIndex: 'status_text',width: 60},
			{header: lmain("cust.base.type"), dataIndex: 'cust_type_text',width: 70},
			{header: lmain("cust.base.cust_level"), dataIndex: 'cust_level_text',width: 70},
			{header: lmain("cust.base.blackList"), dataIndex: 'is_black_text',width: 50},
			{id: 'autoCol', header: lmain("cust.base.openDate"), dataIndex: 'open_time',renderer:App.qtipValue}
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
			title: lmain("cust.base.switchCustTitle"),
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
			Confirm(lmsg("confirmSwitchThisCust"), this ,function(){
				var record = grid.getStore().getAt(index);
//				this.parent.doRefreshCust( record.data );
				App.search.doRefreshCust( record.data );
				this.hide();
			});
		}, this);
		ChooseCustWindow.superclass.initEvents.call(this);
	}
});