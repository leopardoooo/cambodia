/**
 * 用户信息表格
 */ 
App.pageSize = 15 ;
App.userRecord = Ext.data.Record.create([
	{name: 'user_id'},
	{name: 'cust_id'},
	{name: 'acct_id'},
	{name: 'user_type'},
	{name: 'user_addr'},
	{name: 'user_class'},
	{name: 'user_class_exp_date'},
	{name: 'stop_type'},
	{name: 'status'},
	{name: 'net_type'},
	{name: 'stb_id'},
	{name: 'card_id'},
	{name: 'modem_mac'},
	{name: 'open_time'},
	{name: 'area_id'},
	{name: 'county_id'},
	{name: 'status_date'},
	{name: 'user_type_text'},
	{name: 'status_text'},
	{name: 'stop_type_text'},
	{name: 'user_class_text'},
	{name: 'net_type_text'},
	{name: 'user_name'},
	{name: 'terminal_type'},
	{name: 'terminal_type_text'},
	{name: 'serv_type'},
	{name: 'serv_type_text'},
	{name: 'check_type'},
	{name: 'check_type_text'},
	{name: 'login_name'},
	{name: 'password'},
	{name: 'login_password'},
	{name: 'bind_type'},
	{name: 'bind_type_text'},
	{name: 'max_connection'},
	{name: 'max_user_num'},
	{name: 'is_rstop_fee'},
	{name: 'str1'},
	{name: 'str2'},
	{name: 'str3'},
	{name: 'str4'},
	{name: 'str5'},
	{name: 'str6'},
	{name: 'str7'},{name: 'str7_text'},//用户类别
	{name: 'str8'},
	{name: 'str9'},
	{name: 'str10'},
	{name: 'str11'},{name: 'str11_text'},//双向用户类型
	{name: 'str19'},{name: 'str19_text'},
	{name: 'user_count'},
	{name: 'rejectRes'},//排斥资源
	{name: 'stop_date'},
	{name: 'tv_model_text'},
	{name: 'device_model'},
	{name: 'device_model_text'},
	{name: 'buy_model_text'},
	{name: 'protocol_date'}
]);
UserGrid = Ext.extend(Ext.ux.Grid,{
	border:false,
	userStore:null,
	region: 'center',
	parent: null ,
	oldTitle: langUtils.main("user.list._title"),
	constructor:function( parent){
		this.parent = parent;
		this.userStore = new Ext.data.JsonStore({
//			url:Constant.ROOT_PATH + "/commons/x/QueryUser!queryUser.action",
			fields: App.userRecord
		}); 
		//不能放initEvents里
		this.userStore.on("load", this.doLoadResult ,this);
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
            sm,
			{header:lmain("user.base.type"),dataIndex:'user_type_text',width:90},
			{header:lmain("user.base.name"),dataIndex:'user_name',	width:200, renderer:App.qtipValue},
			{header:lmain("user.base.status"),dataIndex:'status_text',	width:100,renderer:function(v){
				return App.qtipValue(Ext.util.Format.statusShow(v));
			}},
			{header:lmain("user.base.stbId"),dataIndex:'stb_id',	width:130,renderer:App.qtipValue},
			{header:lmain("user.base.cardId"),dataIndex:'card_id',width:90,renderer:App.qtipValue},
			{header:lmain("user.base.modem"),dataIndex:'modem_mac',width:120,renderer:App.qtipValue}
	        ]
	      });
		
		UserGrid.superclass.constructor.call(this,{
			title: this.oldTitle,
			id:'U_USER',
			store:this.userStore,
			sm:sm,
			cm:cm,
			view: new Ext.ux.grid.ColumnLockBufferView(),
			tools:[{id:'search',qtip:langUtils.main("user.list.tools")[0],cls:'tip-target',scope:this,handler:function(){
				
					var comp = this.tools.search;
					if(this.userStore.getCount()>0){
						if(win)win.close();
							win = FilterWindow.addComp(this,[
							    {text:lmain("user.base.type"),field:'user_type',showField:'user_type_text'},
							    {text:lmain("user.base.name"),field:'user_addr',type:'textfield'},
								{text:lmain("user.base.status"),field:'status',showField:'status_text'},
								{text:lmain("user.base.stbId"),field:'stb_id',type:'textfield'},
								{text:lmain("user.base.cardId"),field:'card_id',type:'textfield'},
								{text:lmain("user.base.modem"),field:'modem_mac',type:'textfield'}
								], 690,"1",false);
							
						if(win){
							win.setPosition(comp.getX()-win.width, comp.getY()-50);//弹出框右对齐
							win.show();
						}
					}else{
						Alert(lbc('common.emptyMsg'));
					}
		    	}
		    }]
		});
	},
	initEvents: function(){
		this.on("rowclick", this.doClickRecord, this );
		this.on("afterrender",function(){
			this.swapViews();
		},this,{delay:10});
		
		UserGrid.superclass.initEvents.call(this);
	},
	doLoadResult:function(_store, _rs, ops){
		//隐藏数据加载提示框
		App.hideTip();
		
		var prodGrid = this.parent.prodGrid;
		var len = _rs.length;
		App.getApp().data.users=[];
		if(len>0){
			for(i=0;i<len; i++){
				App.getApp().data.users.push(_rs[i].data);
			}
		}
		//用户信息加载完后再加载产品信息,类似与同步
		//当用户只有一个时，产品信息还没加载完，就去取产品数据了
		if(len == 1){
			prodGrid.userId = _rs[0].get('user_id');
		}
		prodGrid.remoteRefresh();
		//刷新userDetailTab
		var userId = this.parent.userDetailTab.userId;
		if(userId){
			var isExist = false;//用户是否存在
			for(i=0;i<_rs.length;i++){
				if(userId == _rs[i].get('user_id')){
					var userDetailTab = this.parent.userDetailTab;
					userDetailTab.resetPanel();
					userDetailTab.userId = userId;
					userDetailTab.type = _rs[i].get("user_type");
					userDetailTab.userRecord = _rs[i];
					userDetailTab.refreshPanel(userDetailTab.getActiveTab());
					isExist = true;
					break;
				}
			}
			//如果不存在
			if(!isExist){
				this.parent.userDetailTab.resetPanel();
			}
		}
	},
	remoteRefresh:function(){
		//显示数据加载提示框
		App.showTip();
		var cust = App.data.custFullInfo.cust;
//		this.userStore.baseParams={custId:cust['cust_id'],custStatus:cust['status']};
//		this.refresh();
		
		Ext.Ajax.request({
			url: Constant.ROOT_PATH + "/commons/x/QueryUser!queryUserAndTotal.action",
			params: {custId:cust['cust_id'],custStatus:cust['status']},
			scope: this,
			success: function(res, opt){
				var data = Ext.decode(res.responseText);
				if(data){
					this.userStore.loadData(data['records']);
					if(data['simpleObj']){
						this.setTitle(this.oldTitle+'  ('+data['simpleObj']+')');
					}else{
						this.setTitle(this.oldTitle);
					}
				}else{
					this.userStore.removeAll();
					this.setTitle(this.oldTitle);
				}
			}
		});
		
		//过滤tbar按钮
		if(App.getCust().status == 'ACTIVE'){
			if(this.getTopToolbar())
				this.getTopToolbar().show();
			App.getApp().disableBarByBusiCode(this.getTopToolbar(),['1020','1025','1015'],false);
		}else if(App.getCust().status == 'RELOCATE'){
			App.getApp().disableBarByBusiCode(this.getTopToolbar(),['1020','1025','1015'],true);
		}else if(App.getCust().status == 'DATACLOSE'){
			if(this.getTopToolbar())
				this.getTopToolbar().hide();
			this.doLayout();
		}
	},
	refresh:function(){
//		this.userStore.load();
	},
	doClickRecord: function(g, i, e){
		//选中一条时才显示
		var records = g.getSelectionModel().getSelections();
		if(records.length == 1){
			var uid = records[0].get("user_id");
			var type = records[0].get("user_type");
			var record = records[0];
			
			var userDetailTab = this.parent.userDetailTab;
			userDetailTab.resetPanel();
			userDetailTab.userId = uid;
			userDetailTab.type = type;
			userDetailTab.userRecord = record;
			userDetailTab.refreshPanel(userDetailTab.getActiveTab());
			
			//刷新产品信息
			this.parent.prodGrid.userId = uid;
			this.parent.prodGrid.refresh();
		}else{
			var userDetailTab = this.parent.userDetailTab;
			userDetailTab.resetPanel();
			
			//刷新产品信息
			this.parent.prodGrid.userId = null;
			this.parent.prodGrid.reset();
		}
		
	},
	getSelections:function(){
		return this.getSelectionModel().getSelections();
	},
	getSelectedUsers:function(){
		//获取user面板中 选中的users
		var params = [];
		var users = App.getApp().data.users
		var a = this.getSelectionModel().getSelections();
		for (var i=0;i<a.length;i++){
			userId = a[i].get("user_id");
			for(var j=0;j<users.length;j++){
				if(userId == users[j].user_id){
					params.push(users[j]);
					break;
				}
			}
		}
		return params;
	},
	getSelectedUserIds:function(){
		//获取user面板中 选中的users
		var params = [];
		var users = App.getApp().data.users
		var a = this.getSelectionModel().getSelections();
		for (var i=0;i<a.length;i++){
			userId = a[i].get("user_id");
			params.push(userId);
		}
		return params;
	}
});

/**
 * 产品信息表格
 */ 
ProdGrid = Ext.extend(Ext.TabPanel,{
	border:false,
	userProdStore:null,
	custPkgStore:null,
	region: 'center',
	userId : null,
	parent: null,
	prodMap : null,
	constructor:function(p){
		this.parent = p;
		var lc = langUtils.main("user.prod.base.columns");
		// 列定义
		this.baseProdCm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
    		{header:lc[0],dataIndex:'order_sn',width:70},
			{header:lc[1],dataIndex:'prod_name',width:120, renderer: App.qtipValue},
			{header:lc[2],dataIndex:'package_name',width:120, renderer: App.qtipValue},
			{header:lc[3],dataIndex:'tariff_name',	width:110, renderer: App.qtipValue},
			{header:lc[4],dataIndex:'eff_date',width:90,renderer: Ext.util.Format.dateFormat},
			{header:lc[5],dataIndex:'exp_date',width:100,renderer: Ext.util.Format.dateFormat},
			{header:lc[6],dataIndex:'status_text',	width:110,renderer:Ext.util.Format.statusShow},
			{header:lc[7],dataIndex:'status_date',width:130,renderer: Ext.util.Format.dateFormat},
			{header:lc[8],dataIndex:'order_time',width:80,renderer: Ext.util.Format.dateFormat},
			{header:lc[9],dataIndex:'order_months',width:90},
			{header:lc[10],dataIndex:'done_code',width:80},
			{header:lc[11],dataIndex:'is_pay_text',width:100}
	        ]
	      });
		
		// 基本产品
		this.userProdStore = new Ext.data.JsonStore({
			fields: ["tariff_name","disct_name","prod_type","prod_name","prod_type_text","serv_id",
			         "serv_id_text","is_base","is_base_text","public_acctitem_type_text","package_name",
			         "order_sn","package_sn","package_id","cust_id","user_id","prod_id","tariff_id","disct_id",
			         "status","status_text","status_date","eff_date","exp_date","active_fee","bill_fee",
			         "rent_fee","last_bill_date","next_bill_date","order_months","order_fee","order_time",
			         "order_type","package_group_id","remark","public_acctitem_type","done_code","is_pay","is_pay_text"],			
			sortInfo : {
				field : 'prod_name',
				direction:'DESC'
			}
		});
		this.userProdStore.on('load',this.doLoadResult,this);
		// 基本产品
		this.baseProdGrid = new Ext.ux.Grid({
			id:'U_PROD',
			stripeRows: true, 
			border: false,
			store:this.userProdStore,
			sm:new Ext.grid.RowSelectionModel(),
			view: new Ext.ux.grid.ColumnLockBufferView(),
			cm: this.baseProdCm,
			listeners: {
				scope: this,
				rowclick: this.doClickRecord
			}
		});
		
		// 客户套餐
		// 列定义
		var lc = langUtils.main("user.prod.pkg.columns");
		this.custPkgCm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
    		{header:lc[0],dataIndex:'order_sn',width:70},
			{header:lc[1],dataIndex:'prod_name',width:120},
			{header:lc[2],dataIndex:'tariff_name',	width:110},
			{header:lc[3],dataIndex:'status_text',	width:110,renderer:Ext.util.Format.statusShow},
			{header:lc[4],dataIndex:'eff_date',width:90,renderer: Ext.util.Format.dateFormat},
			{header:lc[5],dataIndex:'exp_date',width:100,renderer: Ext.util.Format.dateFormat},
			{header:lc[6],dataIndex:'prod_type_text',width:100},
			{header:lc[7],dataIndex:'order_time',width:80,renderer: Ext.util.Format.dateFormat}
	        ]
	      });
		this.custPkgStore = new Ext.data.JsonStore({
			fields: ["tariff_name","disct_name","prod_type","prod_name","prod_type_text","serv_id",
			         "serv_id_text","is_base","is_base_text","public_acctitem_type_text","package_name",
			         "order_sn","package_sn","package_id","cust_id","user_id","prod_id","tariff_id","disct_id",
			         "status","status_text","status_date","eff_date","exp_date","active_fee","bill_fee",
			         "rent_fee","last_bill_date","next_bill_date","order_months","order_fee","order_time",
			         "order_type","package_group_id","remark","public_acctitem_type","is_pay"]
		});
		this.custPkgGrid = new Ext.ux.Grid({
			id:'U_CUST_PKG',
			stripeRows: true, 
			border: false,
			store:this.custPkgStore,
			sm:new Ext.grid.RowSelectionModel(),
			view: new Ext.ux.grid.ColumnLockBufferView(),
			cm: this.custPkgCm,
			listeners: {
				scope: this,
				rowclick: this.doClickRecord
			}
		});
		
		ProdGrid.superclass.constructor.call(this,{
			activeTab: 0,
			border: false,
			items: [{
				title: langUtils.main("user.prod.base._title"),
				border: false,
				layout: 'fit',
				items: [this.baseProdGrid]
			},{
				title: langUtils.main("user.prod.pkg._title"),
				border: false,
				layout: 'fit',
				items: [this.custPkgGrid]
			}],
			tbar:[
			      '->', '-', {text:langUtils.main("user.prod.tools")[0], scope:this, handler:function(){
			    	  this.remoteRefresh();
			      }}, '-',
			      {text:langUtils.main("user.prod.tools")[1], scope:this, handler:function(){
			    	  this.remoteRefresh('ALL');
			      }},'-'
			]
		})
	},
	initEvents: function(){
		this.baseProdGrid.on("afterrender",function(){
			this.baseProdGrid.swapViews();
		},this,{delay:10});
		
		this.custPkgGrid.on("afterrender",function(){
			this.custPkgGrid.swapViews();
		},this,{delay:10});
		
		ProdGrid.superclass.initEvents.call(this);
	},
	doLoadResult : function(_store, _rs, ops){//加载完后刷新产品详细面板
		//刷新prodDetailTab
		var prodSn = this.parent.prodDetailTab.prodSn;
		var tariffId = this.parent.prodDetailTab.tariffId;
		if(null != prodSn && null != tariffId){
			var isExist = false;//产品是否存在
			for(i=0;i<_rs.length;i++){
				if(prodSn == _rs[i].get('order_sn')){
					var prodDetailTab = this.parent.prodDetailTab;
					prodDetailTab.resetPanel();
					prodDetailTab.prodSn = prodSn;
					prodDetailTab.tariffId = tariffId;
					prodDetailTab.userRecord = _rs[i];
					prodDetailTab.refreshPanel(prodDetailTab.getActiveTab());
					isExist = true;
					break;
				}
			}
			if(!isExist){
				this.parent.prodDetailTab.resetPanel();
			}
		}
	},
	doClickRecord: function(grid,rowIndex,e){
		var record = grid.getStore().getAt(rowIndex);
		var prodSn = record.get('order_sn');
		var tariffId = record.get('tariff_id');
		var prodDetailTab = this.parent.prodDetailTab;
		prodDetailTab.resetPanel();
		prodDetailTab.prodSn = prodSn;
		prodDetailTab.tariffId = tariffId;
		prodDetailTab.userRecord = record;
		prodDetailTab.refreshPanel(prodDetailTab.getActiveTab());
	},
	getSelections:function(){
		var panelId = this.getActiveTab().items.itemAt(0).getId();
		if(panelId === "U_CUST_PKG"){
			return this.custPkgGrid.getSelectionModel().getSelections();
		}else{
			return this.baseProdGrid.getSelectionModel().getSelections();
		}
		
	},
	refresh:function(){
		this.userProdStore.removeAll();
		this.setActiveTab(0);
		if(this.userId){
			var userProd = null;
			if(this.prodMap && (userProd=this.prodMap[this.userId]) ){
				var userRecord = this.parent.userGrid.getSelectionModel().getSelected();
				if(Ext.isEmpty(userRecord)){
					userRecord = this.parent.userGrid.getStore().getAt(0);
				}
				if(userRecord){
					var status = userRecord.get('status');
					for(var i=0,len=userProd.length;i<len;i++){
						userProd[i]['user_status'] = status;
					}
					this.userProdStore.loadData(userProd);
				}
			}
		}
		if(this.prodMap['CUST'])
			this.custPkgGrid.getStore().loadData(this.prodMap['CUST']);
	},
	remoteRefresh:function(loadType){
		var cust = App.getData().custFullInfo.cust;
		//显示数据加载提示框
		App.showTip();
		Ext.Ajax.request({
			scope : this,
			url : Constant.ROOT_PATH + "/core/x/ProdOrder!queryCustEffOrder.action",
			params : {
				cust_id : App.getCustId(),
				loadType:loadType
			},
			success : function(res,opt){
				var data = Ext.decode(res.responseText);
				// 过滤出客户套餐及用户产品
				this.prodMap = {};
				for(var key in data){
					if(key !== "CUST"){
						this.prodMap[key] = data[key];
					}
				}
				this.refresh();
				this.custPkgGrid.getStore().removeAll();
				//隐藏数据加载提示框
				App.hideTip();
				if(data["CUST"]){
					this.custPkgGrid.getStore().loadData(data["CUST"]);
					this.prodMap['CUST'] = data["CUST"];
				}
//				this.setActiveTab(0);
			}
		});
	},
	getSelectedProdSns:function(){
		var panelId = App.getData().currentPanelId;
		var prodGrid = null;
		// 套餐
		if(panelId === "U_CUST_PKG"){
			prodGrid = this.custPkgGrid;
		}else{
			prodGrid = this.baseProdGrid;
		}
		//获取prod面板中 选中的prodSns
		var params = [];		
		var recs = prodGrid.getSelectionModel().getSelections();
		for (var i=0;i<recs.length;i++){
			var prodSn = recs[i].get("order_sn");
			params.push(prodSn);
		}
		return params;
	},
	reset : function(){//重置面板信息
		this.baseProdGrid.getStore().removeAll();
		this.custPkgGrid.getStore().removeAll();
		this.userId = null;
	},
	getProdByUserId: function(user_id, prod_sn){
		var prods = this.prodMap[user_id];
		for(var i =0; i< prods.length; i++){
			if(prods[i]["order_sn"] == prod_sn){
				return prods[i];
			}
		}
		return null;
	}
});
/**
 * 用户详细信息
 * @class UserDetailTemplate
 */
UserTemplate = new Ext.XTemplate(
	'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
		'<tr height=24>',
			'<td class="label" width=20%>'+ lmain("user.base.type") +'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.user_type_text ||""]}</td>',
			'<td class="label" width=20%>'+ lmain("user.base.name") +'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.user_name || values.login_name ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+lmain("user.base.loginName")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.login_name ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.terminal")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.terminal_type_text ||""]}</td>',
		'</tr>',
		'<tr height=24>',
		'<td class="label" width=20%>'+ lmain("user.base.deviceModel") +'：</td>',
		'<td class="input" width=30%>&nbsp;{[values.device_model_text ||""]}</td>',
		'<td class="label" width=20%>'+ lmain("user.base.buyWay") +'：</td>',
		'<td class="input" width=30%>&nbsp;{[values.buy_model_text ||""]}</td>',
	'</tr>',
		'<tr height=24>',
		'<td class="label" width=20%>'+ lmain("user.base.status") +'：</td>',
		'<td class="input" width=30%>&nbsp;{[values.status_text ||""]}</td>',
			'<td class="label" width=20%>'+ lmain("user.base.statusTime") +'：</td>',
			'<td class="input" width=30%>&nbsp;{[fm.dateFormat(values.status_date) ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+ lmain("user.base.stopDate") +'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[fm.dateFormat(values.stop_date) ||""]}</td>',	
			'<td class="label" width=20%>'+ lmain("user.base.createTime") +'：</td>',
			'<td class="input" width=30%>&nbsp;{[fm.dateFormat(values.open_time) ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+ lmain("user.base.stopType") +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.stop_type_text ||""]}</td>',
			'<td class="label" width=20%>'+ lmain("user.base.protocolDate") +'：</td>',
			'<td class="input" width=30%>&nbsp;{[fm.dateFormat(values.protocol_date) ||""]}</td>',
		'</tr>',
		'<tr height=24>',
		'<td class="label" width=20%>'+ lmain("user.base.stbId") +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.stb_id ||""]}</td>',
			'<td class="label" width=20%>'+ lmain("user.base.cardMac") +'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.card_id || values.modem_mac ||""]}</td>',
		'</tr>',		
	'</table>'
);
UserBroadbandTemplate = new Ext.XTemplate(
		'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
		'<tr height=24>',
			'<td class="label" width=20%>'+lmain("user.base.type")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.user_type_text ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.name")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.user_name ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+lmain("user.base.loginName")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.login_name ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.password")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.password ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+lmain("user.base.deviceModel")+'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.device_model_text ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.buyWay")+'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.buy_model_text ||""]}</td>',
		'</tr>',
		'<tr height=24>',
		'<td class="label" width=20%>'+lmain("user.base.status")+'：</td>',
		'<td class="input" width=30%>&nbsp;{[values.status_text ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.statusTime")+'：</td>',
			'<td class="input" width=30%>&nbsp;{[fm.dateFormat(values.status_date) ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+lmain("user.base.stopDate")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[fm.dateFormat(values.stop_date) ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.createTime")+'：</td>',
			'<td class="input" width=30%>&nbsp;{[fm.dateFormat(values.open_time) ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>POE：</td>',
			'<td class="input" width=30%>&nbsp;{[values.str8 ||""]}</td>',
			'<td class="label" width=20%>OLT：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.str7 ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+lmain("user.base.str4")+'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.str4 ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.str6")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.str6 ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+lmain("user.base.stopType")+'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.stop_type_text ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.protocolDate")+'：</td>',
			'<td class="input" width=30%>&nbsp;{[fm.dateFormat(values.protocol_date) ||""]}</td>',
		'</tr>',
	'</table>'
);

UserOTTMobileTemplate = new Ext.XTemplate(
		'<table width="100%" border="0" cellpadding="0" cellspacing="0">',
		'<tr height=24>',
			'<td class="label" width=20%>'+lmain("user.base.type")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.user_type_text ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.name")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.user_name ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+lmain("user.base.loginName")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.login_name ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.terminal")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[values.terminal_type_text ||""]}</td>',
		'</tr>',
		'<tr height=24>',
		'<td class="label" width=20%>'+lmain("user.base.status")+'：</td>',
		'<td class="input" width=30%>&nbsp;{[values.status_text ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.statusTime")+'：</td>',
			'<td class="input" width=30%>&nbsp;{[fm.dateFormat(values.status_date) ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+lmain("user.base.stopDate")+'：</td>',
			'<td class="input_bold" width=30%>&nbsp;{[fm.dateFormat(values.stop_date) ||""]}</td>',
			'<td class="label" width=20%>'+lmain("user.base.createTime")+'：</td>',
			'<td class="input" width=30%>&nbsp;{[fm.dateFormat(values.open_time) ||""]}</td>',
		'</tr>',
		'<tr height=24>',
			'<td class="label" width=20%>'+lmain("user.base.stopType")+'：</td>',
			'<td class="input" width=30%>&nbsp;{[values.stop_type_text ||""]}</td>',
		'</tr>',
	'</table>'
);

UserDetailTemplate = {
	"DTT": UserTemplate,
	"OTT": UserTemplate,
	"BAND": UserBroadbandTemplate,
	"OTT_MOBILE": UserOTTMobileTemplate
};

/**
 * 用户异动信息
 * @class PropChangeGrid
 * @extends Ext.grid.GridPanel
 */
UserPropChangeGrid = Ext.extend(Ext.grid.GridPanel,{
	region: 'center',
	border: false,
	changeStore: null,
	isReload : true,//用于判断第一次激活时加载
	constructor: function(){
		this.changeStore = new Ext.data.JsonStore({
			fields: ["cust_id","user_id","column_name","column_name_text","old_value","old_value_text","new_value","new_value_text",
					"done_code","change_time","optr_name","busi_name"]
		});
		var lc = langUtils.main("user.userDetail.change");
		var cm = [
			{header:lc[0],dataIndex:'busi_name',		width:150,	renderer:App.qtipValue},
			{header:lc[1],dataIndex:'column_name_text',	width:100, 	renderer:App.qtipValue},
			{header:lc[2],dataIndex:'old_value_text',	width:120,	renderer:App.qtipValue},
			{header:lc[3],dataIndex:'new_value_text',	width:120,	renderer:App.qtipValue},
			{header:lc[4],dataIndex:'change_time',		width:130,	renderer:App.qtipValue},
			{header:lc[5],dataIndex:'optr_name',					renderer:App.qtipValue}
		];
		var pageTbar = new Ext.PagingToolbar({store: this.changeStore ,pageSize : App.pageSize});
		pageTbar.refresh.hide();
		UserPropChangeGrid.superclass.constructor.call(this,{
			region: 'center',
			store:this.changeStore,
			columns:cm,
			bbar: pageTbar
		})
	},
	
	remoteRefresh:function(uid,utype){
		Ext.Ajax.request({
			url: Constant.ROOT_PATH + "/commons/x/QueryUser!queryUserPropChange.action",
			scope:this,
			params:{userId:uid,userType:utype},
			success:function(res,opt){
				var data = Ext.decode(res.responseText);
				//PagingMemoryProxy() 一次性读取数据
				this.changeStore.proxy = new Ext.data.PagingMemoryProxy(data),
				//本地分页
				this.changeStore.load({params:{start:0,limit:App.pageSize}});
			}
		});
	},
	reset : function(){//重置面板信息
		this.getStore().removeAll();
		this.isReload = true;
	}
});

/**
 * 用户详细信息
 * @class UserDetailInfo
 * @extends Ext.Panel
 */
UserDetailInfo = Ext.extend(Ext.Panel,{
	type : 'DTT',//默认为数字电视用户的模板
	tpl: null,
	constructor: function(){
		this.tpl = UserDetailTemplate[this.type];
		this.tpl.compile();
		UserDetailInfo.superclass.constructor.call(this, {
			border: false,
			layout: 'anchor',
			anchor: '100%',
			autoScroll:true,
			bodyStyle: "background:#F9F9F9",
			defaults: {
					bodyStyle: "background:#F9F9F9"
				},
			items : [{xtype : "panel",
						border : false,
						bodyStyle: "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
						html : this.tpl.applyTemplate({})
					}]
		});
	},
	reset:function(){//重置详细信息
		if(this.items.itemAt(0).getEl()){
			this.tpl.overwrite( this.items.itemAt(0).body, {});
		}
	},
	refresh:function(type,record){
		this.tpl = UserDetailTemplate[type];
		this.tpl.overwrite( this.items.itemAt(0).body, record.data);
	}
});

UserDetailTab = Ext.extend(CommonTab,{
	region:"center",
	userPropChangeGrid: null,
	userValidResGrid : null,
	userDetailInfo: null,
	promotionGrid : null,
	userId : null,
	type : null,
	isReload: null,
	userRecord : null,
	parent:null,
	constructor:function(p){
		this.parent = p;
		this.userPropChangeGrid = new UserPropChangeGrid();
		this.userDetailInfo = new UserDetailInfo();
		UserDetailTab.superclass.constructor.call(this, {
			activeTab: 0,
			border: false,
			defaults: {
				layout: 'fit',
				border:false
			},
			items:[{
				title:langUtils.main("user.userDetail.tabs")[0],
				items:[this.userDetailInfo]
			},{
				title:langUtils.main("user.userDetail.tabs")[1],
				items:[this.userPropChangeGrid]
			}]
		});
	},
	refreshPanel : function(p){
		var content = p.items.itemAt(0);
		if(content.type){
			if(this.isReload && this.type && this.userRecord){
				this.refreshUserDetail(this.type,this.userRecord);
				this.isReload = false;
			}
		}else{
			if(content.isReload && this.userId){
				content.remoteRefresh(this.userId,this.type);
				content.isReload = false;
			}
		}
	},
	refreshUserDetail: function(type,record){
		this.userDetailInfo.refresh(type,record);
		this.parent.prodDetailTab.resetPanel();
		
	},
	resetPanel : function(){//重置TAB面板
		this.userPropChangeGrid.reset();
		this.userId = null;
		this.type = null;
		this.userRecord = null;
		this.isReload = true;
	}
});

/**
 * 产品状态异动
 * @class ProdPropChangeGrid
 * @extends Ext.grid.GridPanel
 */
ProdPropChangeGrid = Ext.extend(Ext.grid.GridPanel,{
	region:'center',
	border:false,
	changeStore:null,
	isReload : true,
	constructor:function(){
		this.changeStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/commons/x/QueryUser!querProdPropChange.action",
			fields: ["sn","column_name","busi_name","optr_name","column_name_text","old_value","new_value","done_code","change_time"],
			root: 'records',
			totalProperty: 'totalProperty',
			params:{start:0,limit:20},
			sortInfo:{
				field:'change_time',
				direction:'DESC'
			}
		}); 
		var lc = langUtils.main("user.userDetail.change");
		var cm = [
			{header:lc[0],dataIndex:'busi_name', width:150,renderer:App.qtipValue},
			{header:lc[1],dataIndex:'column_name_text', width:100,renderer:App.qtipValue},
			{header:lc[2],dataIndex:'old_value', width:120,renderer:App.qtipValue},
			{header:lc[3],dataIndex:'new_value',width:120,renderer:App.qtipValue},
			{header:lc[4],dataIndex:'change_time',width:130},
			{header:lc[5],dataIndex:'optr_name',width:80}
		];
				  
		ProdPropChangeGrid.superclass.constructor.call(this,{
			region: 'center',
			store:this.changeStore,
			columns:cm,
			bbar: new Ext.PagingToolbar({
	        	pageSize: 20,
				store: this.changeStore
			})
		})
	},
	remoteRefresh:function(prodSn,prodStatus){
		this.changeStore.baseParams.prodSn = prodSn;
		this.changeStore.baseParams.prodStatus = prodStatus;
		this.changeStore.baseParams.start = 0;
		this.changeStore.baseParams.limit = 20;
		this.changeStore.load();
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});

/**
 * 订单金额明细
 */
OrderFeeDetailGrid = Ext.extend(Ext.grid.GridPanel,{
	region:'center',
	border:false,
	changeStore:null,
	isReload : true,
	constructor:function(){
		this.changeStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/commons/x/QueryUser!queryOrderFeeDetail.action",
			fields: ['order_fee_sn', 'order_sn', 'done_code', 'input_type', 'input_type_text',  'fee_type', 'fee_type_text',
			         'input_fee', 'create_time', 'input_prod_id', 'input_prod_name'],
			root: 'records',
			totalProperty: 'totalProperty',
			params:{start:0,limit:20},
			sortInfo:{
				field:'create_time',
				direction:'DESC'
			}
		}); 
		var lc = langUtils.main("user.prodDetail.detail");
		var cm = [
			{header:lc[0],dataIndex:'order_fee_sn', width:60},
			{header:lc[1],dataIndex:'fee_type_text',width:80},
			{header:lc[3],dataIndex:'input_type_text', width:100},
			{header:lc[4],dataIndex:'input_fee',width:80, renderer: Ext.util.Format.formatFee},
			{header:lc[2],dataIndex:'input_prod_name',width:150, renderer: App.qtipValue},
			{header:lc[8],dataIndex:'done_code', width:80}
		];
				  
		OrderFeeDetailGrid.superclass.constructor.call(this,{
			region: 'center',
			store:this.changeStore,
			columns:cm,
			bbar: new Ext.PagingToolbar({
	        	pageSize: 20,
				store: this.changeStore
			})
		})
	},
	remoteRefresh:function(orderSn){
		this.changeStore.baseParams.orderSn = orderSn;
		this.changeStore.baseParams.start = 0;
		this.changeStore.baseParams.limit = 20;
		this.changeStore.load();
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});

ProdDetailTab = Ext.extend(CommonTab,{
	prodExpensesGrid:null,
	prodChangeGrid:null,
	prodResGrid:null,
	prodDetailInfo: null,
	region:"center",
	prodSn : null,
	tariffId : null,
	userRecord : null,
	isReload:true,
	constructor:function(){
		this.prodPropChangeGrid = new ProdPropChangeGrid();
		this.orderFeeDetailGrid = new OrderFeeDetailGrid();
		ProdDetailTab.superclass.constructor.call(this, {
			activeTab: 0,
			border: false,
			defaults: {
				layout: 'fit',
				border:false
			},
			items:[{
				title: langUtils.main("user.prodDetail.tabs")[0],
				items: [this.orderFeeDetailGrid]
			},
			{
				title:langUtils.main("user.prodDetail.tabs")[1],
				items:[this.prodPropChangeGrid]
			}]
		});
	},
	refreshPanel : function(p){
		var content = p.items.itemAt(0);
		if(content.templateData){
			if(this.isReload && this.userRecord && this.prodSn){
				this.refreshProdDetail(this.userRecord,this.prodSn);
				this.isReload = false;
			}
		}else{
			if(content.isReload){//如果需要重载
				if(content.getId() == 'prodExpensesGrid'){
					if(this.tariffId){//如果存在
						content.remoteRefresh(this.tariffId);
						content.isReload = false;	
					}
				}else{
					if(this.prodSn){//如果存在
						var prodRecord = App.getApp().main.infoPanel.getUserPanel().prodGrid.getSelections()[0];
						/*if(!prodRecord){
							throw new Error('获取数据出错,未能获取产品信息表格的数据!');
						}
						var status = prodRecord.get('status');
						content.remoteRefresh(this.prodSn,status);
						content.isReload = false;*/
						if(prodRecord){
							var status = prodRecord.get('status');
							content.remoteRefresh(this.prodSn,status);
							content.isReload = false;
						}
					}
				}
			}
		}
	},
	refreshProdDetail: function(record,prodSn){
		this.refresh(record);
	},
	refresh: function(record){
	},
	resetPanel : function(){//重置Tab面板的子面板信息
		this.orderFeeDetailGrid.reset();
		this.prodPropChangeGrid.reset();
		this.prodSn = null;
		this.tariffId = null;
		this.userRecord = null;
		this.isReload = true;
	}
});

/**
 * 用户信息面板
 * @class UserPanel
 * @extends Ext.Panel
 */
UserPanel = Ext.extend( BaseInfoPanel , {
	// 面板属性定义
	userGrid:null,
	prodGrid:null,
	mask: null ,
	userDetailTab: null,
	prodDetailTab: null,
	constructor: function(){
		this.userGrid = new UserGrid(this);
		this.prodGrid = new ProdGrid(this);
		this.userDetailTab =  new UserDetailTab(this);
		this.prodDetailTab = new ProdDetailTab();
		UserPanel.superclass.constructor.call(this, {
			layout:"border",
			border:false,
			items:[{
				region:"center",
				layout:"anchor",
				border: false,
				items:[{
					anchor:"100% 60%",
					layout:'border',
					border: false,
					bodyStyle: 'border-right-width: 1px; ',
					items:[this.userGrid]
				},{
					anchor:"100% 40%",
					layout:'border',
					border: false,
					bodyStyle: 'border-top-width: 1px;border-right-width: 1px; ',
					items:[this.prodGrid]
				}]
			},{
				region:"east",
				split:true,
				width:"38%",
				layout:"anchor",
				border: false,
				items:[{
					anchor:"100% 60%",
					layout:'border',
					border: false,
					bodyStyle: 'border-left-width: 1px; border-bottom-width: 1px; ',
					items:[this.userDetailTab]
				},{
					anchor:"100% 40.1%",
					layout:'border',
					border: false,
					bodyStyle: 'border-left-width: 1px; ',
					items:[this.prodDetailTab]
				}]
			}]
		});
	},
	refresh:function(){
		this.userDetailTab.resetPanel();
		this.prodGrid.reset();
		this.prodDetailTab.resetPanel();
		this.userGrid.remoteRefresh();
	},
	getUserDetailTemplate: function(){
		return UserDetailTemplate;
	}
});
Ext.reg( "userPanel" , UserPanel );
