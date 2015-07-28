var custInfoHTML =
'<table width="100%" border="0" cellpadding="0" cellspacing="0">' +
	'<tr height=24>'+
		'<td class="label" width=20%>' + langUtils.main("cust.base.name") +':</td>' +
		'<td class="input_bold" width=30%>&nbsp;{[values.cust.cust_name || ""]}</td>'+
		'<td class="label" width=20%>'+ langUtils.main("cust.base.busiId") +':</td>' +
		'<td class="input_bold" width=30%>&nbsp;{[values.cust.cust_no || ""]}</td>'+
	'</tr>'+
	'<tr height=24>'+
	'<td class="label">'+ langUtils.main("cust.base.openDate") +':</td>' +
		'<td class="input" >&nbsp;{[fm.dateFormat(values.cust.open_time) || ""]}</td>'+
		'<td class="label">'+ langUtils.main("cust.base.addr") +':</td>' +
		'<td class="input">&nbsp;{[values.cust.address || ""]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">'+ langUtils.main("cust.base.status") +':</td>' +
		'<td class="input">&nbsp;{[Ext.util.Format.statusShow(values.cust.status_text) || ""]}</td>'+
		'<td class="label">'+ langUtils.main("cust.base.type") +':</td>' +
		'<td class="input">&nbsp;{[values.cust.cust_type_text || ""]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">'+ langUtils.main("cust.base.certType") +':</td>' +
		'<td class="input">&nbsp;{[values.linkman.cert_type_text || ""]}</td>'+
		'<td class="label">'+ langUtils.main("cust.base.certNum") +':</td>' +
		'<td class="input">&nbsp;{[values.linkman.cert_num || ""]}</td>'+
	'</tr>' +
	'<tpl if="values.cust.cust_type == \'RESIDENT\'">' +
		'<tr height=24>'+
			'<td class="label">优惠类型:</td>' +
			'<td class="input">&nbsp;{[values.cust.cust_class_text || ""]}&nbsp;&nbsp;{[values.cust.cust_class_date || ""]}</td>'+
			'<td class="label">客户群体:</td>' +
			'<td class="input">&nbsp;{[values.cust.cust_colony_text || ""]}</td>'+
		'</tr>' + 
	'</tpl>' +
	'<tpl if="values.cust.cust_type == \'UNIT\' && !Ext.isEmpty(values.cust.cust_colony)">' +
		'<tr height=24>'+
			'<td class="label">客户群体:</td>' +
			'<td class="input">&nbsp;{[values.cust.cust_colony_text || ""]}</td>'+
		'</tr>' + 
	'</tpl>' +
	'<tr height=24>'+
		'<td class="label">'+ langUtils.main("cust.base.linkMan") +':</td>' +
		'<td class="input">&nbsp;{[values.linkman.linkman_name || ""]}</td>'+
		'<td class="label">'+ langUtils.main("cust.base.tel") +':</td>' +
		'<td class="input">&nbsp;{[values.linkman.tel || ""]}</td>'+	
	'</tr>' +'<tr height=24>'+
		'<td class="label">'+ langUtils.main("cust.base.barthday") +':</td>' +
		'<td class="input">&nbsp;{[fm.dateFormat(values.linkman.birthday) || ""]}</td>'+
		'<td class="label">'+ langUtils.main("cust.base.mobile") +':</td>' +
		'<td class="input">&nbsp;{[values.linkman.mobile || ""]}</td>'+
	'</tr>'+'<tr height=24>'+
	'<td class="label">'+ langUtils.main("cust.base.areaCateory") +':</td>' +
		'<td class="input">&nbsp;{[values.cust.addr_id_text || ""]}</td>'+
		'<td class="label">'+ langUtils.main("cust.base.houseNetType") +':</td>' +
		'<td class="input">&nbsp;{[values.cust.add_net_type_text || ""]}</td>'+
	'</tr>'+'<tr height=24>'+
	'<td class="label">'+ langUtils.main("cust.base.houseManager") +':</td>' +
		'<td class="input">&nbsp;{[values.cust.busi_optr_name || ""]}</td>'+
		'<td class="label">'+ langUtils.main("cust.base.houseOptr") +':</td>' +
		'<td class="input">&nbsp;{[values.cust.serv_optr_name || ""]}</td>'+
	'</tr>' +
		'<tr height=24>'+
		'<td class="label">'+ langUtils.main("cust.base.postalAddr") +':</td>' +
		'<td class="input" colspan="3">&nbsp;{[values.linkman.mail_address ? values.linkman.mail_address + "&nbsp;&nbsp;&nbsp;&nbsp;postal:&nbsp;" + [values.linkman.postcode || ""] : "" ]}</td>'+
	'</tr>' +	
	'<tpl if="values.cust.cust_colony == \'MNDKH\' || values.cust.cust_colony == \'XYKH\' ">' +
	'<tr height=24>'+
		'<td class="label">容量:</td>' +
		'<td class="input">&nbsp;{[values.cust.cust_count || 0]}</td>'+
		'<td class="label">实际数量:</td>' +
		'<td class="input">&nbsp;{[values.cust.real_cust_count || 0]}</td>'+
	'</tr>' +
	'</tpl>' +
	'<tpl if="values.cust.unit_name">' +
	'<tr height=24>'+
		'<td class="label">单位名称:</td>' +
		'<td class="input"%>&nbsp;{[values.cust.unit_name || ""]}</td>'+
	'</tr>' +
	'</tpl>' +
	'<tr height=24>'+
		'<td class="label">'+ langUtils.main("cust.base.remark") +':</td>' +
		'<td class="input" colspan="3">&nbsp;{[values.cust.remark || ""]}</td>'+
	'</tr>' +	
	'<tpl if="values.acctBank && values.acctBank.bank_account && (values.acctBank.status==\'ACTIVE\' || values.acctBank.status==\'STOP\')">' +
	'<tr height=24>'+
		'<td class="label">签约银行:</td>' +
		'<td class="input">&nbsp;{[values.acctBank.bank_code_text || ""]}</td>'+
		'<td class="label">银行账号:</td>' +
		'<td class="input">&nbsp;{[values.acctBank.bank_account || ""]}</td>'+
	'</tr>' + 
	'<tr height=24>'+ 
		'<td class="label">签约状态:</td>' +
		'<td class="input">&nbsp;{[values.acctBank.status=="STOP"?"暂停":"正常"]}</td>'+
		'<td class="label">账户名称:</td>' +
		'<td class="input">&nbsp;{[values.acctBank.account_name || ""]}</td>'+
	'</tr>' + 
	'</tpl>' +
'</table>';

/**
 * 客户信息面板构造
 */
CustInfoPanel = Ext.extend( Ext.ux.Gpanel , {
	region:'center',
	tpl: null,
	extInfoPanel: null,
	constructor: function(cmpId){
		this.tpl = new Ext.XTemplate( custInfoHTML );
		this.tpl.compile();
		this.extInfoPanel = new ExtInfoPanel();
		CustInfoPanel.superclass.constructor.call(this, {
			id:cmpId,
			title:"<font style='font-family:微软雅黑;font-size:12'>" + langUtils.main("cust.base._title") +"</font>",
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
						html : this.tpl.applyTemplate( App.getData().custFullInfo)
					},this.extInfoPanel]
		});
	},
	refresh: function(){
		this.tpl.overwrite( this.items.itemAt(0).body, App.getData().custFullInfo);
		this.extInfoPanel.remoteRefresh();
//		App.getApp().main.infoPanel.changeDisplay();//已经销户的让他可以显示返销户按钮
		//过滤tbar按钮
		this.doFilterTBar();
	},
	remoteRefresh:function(custId){
		if(App.getCust().cust_id){
			//显示数据加载提示框
			App.showTip();
			Ext.Ajax.request({
				scope : this,
				url:  root + '/commons/x/QueryCust!searchCustById.action',
				params: { 
					"custId" : custId || App.getCustId()
				},
				success: function(res,ops){
					var data = Ext.decode(res.responseText);
					if(data){
						App.data.custFullInfo = data;
					}else{
						App.data.custFullInfo.cust = {};
						App.data.custFullInfo.linkman = {};
					}
					
					this.refresh();
					//隐藏数据加载提示框
					App.hideTip();
	  			 }
			});
		}else{
			App.getApp().data.custFullInfo.cust = {};
			App.getApp().data.custFullInfo.linkman = {};
			this.refresh();
		}
		
	},
	doFilterTBar : function(){//过滤tbar按钮
		var custTBar = this.getTopToolbar();
		Ext.getBody().unmask();
		
		var canRestore = App.getCust().status == 'INVALID' || App.getCust().status == 'LOGOFF' 
		|| App.getCust().status == 'RELOCATELOGOFF' || App.getCust().status == 'TRANSFERLOGOFF';//新增两种 客户迁移相关的客户状态
		
		if(!Ext.isEmpty(custTBar)){
			
			//显示按钮统一处理，禁用按钮特殊处理！！！
			//显示过户、修改资料、移机、加入单位、退出单位、拆迁, 恢复状态 
			if(!canRestore){
				App.disableBarByBusiCode(custTBar,['1002','1003','1004','1005','1006','1010','1119','1105','1106','1220','2001'],false);
				App.disableBarByBusiCode(custTBar,['1087'],true);//隐藏返销户 
				App.disableBarByBusiCode(custTBar,['156'],false);//显示详细修改
			}else{
				App.disableBarByBusiCode(custTBar,['1002','1003','1004','1005','1006','1010','1119','1105','1106','1220','2001'],true);
				App.disableBarByBusiCode(custTBar,['1087'],false);//显示返销户
				App.disableBarByBusiCode(custTBar,['156'],true);//禁用详细修改  
			}
			if(App.getCust().status!='PREOPEN'){//溧阳,非预开户客户,不能进行客户迁移操作
				App.disableBarByBusiCode(custTBar,['1985','1330'],true);//隐藏 客户迁移,预开户修改
			}else{
				var cust = App.getCust();
				App.disableBarByBusiCode(custTBar,['1330'],false);//显示,预开户修改
				if(cust.cust_type=='RESIDENT' ||cust.cust_type=='NONRES'){//居民集团可以
					App.disableBarByBusiCode(custTBar,['1985'],false);//显示 客户迁移
				}else{
					App.disableBarByBusiCode(custTBar,['1985'],true);//显示 客户迁移
				}
			}
			
			var cust = App.getCust();
			var custType = cust.cust_type;
			if(custType=='RESIDENT'){//居民
				if(cust.unit_name){//已加入单位
					App.disableBarByBusiCode(custTBar,['1005'],true);//隐藏加入单位
				}else{//未加入单位
					App.disableBarByBusiCode(custTBar,['1006'],true);//隐藏退出单位
				}
				if(cust.mn_cust_id){//已加入模拟大客户
					App.disableBarByBusiCode(custTBar,['1105'],true);//隐藏 加入模拟大客户
				}else{//未加入模拟大客户
					App.disableBarByBusiCode(custTBar,['1106'],true);//隐藏 退出模拟大客户
				}
			}else if(custType=='UNIT'){//单位
				//隐藏过户、移机、加入单位、退出单位、拆迁
				App.disableBarByBusiCode(custTBar,['1003','1010','1005','1006','1119','2001'],true);
			}else{//集团
				//加入单位、退出单位
				App.disableBarByBusiCode(custTBar,['1005','1006','2001'],true);
			}
			
			var status = cust.status;
			if(status=='LYRELOCATE'){//已拆迁客户
				App.disableBarByBusiCode(custTBar,['1220'],true);//隐藏拆迁按钮
			}else if(status == 'DATACLOSE'){//资料隔离
				App.disableBarByBusiCode(custTBar,['1002','1003','1004','1005','1006','1010','1105','1106','1119'],true);
			}else if(status =='LOGOFF'){
				App.disableBarByBusiCode(custTBar,['1002','1003','1004','1005','1006','1010','1119','1105','1106','1220'],true);
			}else{
				App.disableBarByBusiCode(custTBar,['1220'],true);//隐藏 恢复状态 按钮
			}
			
			if(App.getCust().cust_colony == 'MNDKH' || App.getCust().cust_colony == 'XYKH'){//模拟大客户
				App.disableBarByBusiCode(custTBar,['1105','1106'],true);//隐藏 加入模拟大客户按钮、退出模拟大客户
			}
			
			var bank = App.getApp().data.custFullInfo.acctBank;
			App.disableBarByBusiCode(custTBar,['1249'],true);//隐藏恢复卡扣
			App.disableBarByBusiCode(custTBar,['1248'],true);//隐藏暂停卡扣
			App.disableBarByBusiCode(custTBar,['1146'],true);//隐藏解约
			if(bank){
				if( bank.status =='ACTIVE'){
					App.disableBarByBusiCode(custTBar,['1248'],false);//显示暂停卡扣
					App.disableBarByBusiCode(custTBar,['1249'],true);//隐藏恢复卡扣
				}else if( bank.status =='STOP'){
					App.disableBarByBusiCode(custTBar,['1249'],false);//显示恢复卡扣
					App.disableBarByBusiCode(custTBar,['1248'],true);//隐藏暂停卡扣
				}
				if(bank.status !='INVALID'){
					App.disableBarByBusiCode(custTBar,['1146'],false);//显示解约
				}
			}
			
		}
	}
});

/**
 * 单位信息表格
 */ 
UnitGrid = Ext.extend(Ext.ux.Grid,{
	region:'center',
	border:false,
	unitStore:null,
	tbar:[],
	isReload : true,
	isReQuery : true,
	constructor:function(){
		this.unitStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/commons/x/QueryCust!queryCustUnit.action",
			fields: ["cust_id","cust_name","address","create_time"]
		}); 
		var sm = new Ext.grid.CheckboxSelectionModel();
		
		var cm = [
			sm,
			{header:'单位名称',dataIndex:'cust_name',width:110	},
			{header:'地址',dataIndex:'address',	width:140},
			{header:'加入时间',dataIndex:'create_time',	width:80}
		];
		UnitGrid.superclass.constructor.call(this,{
			id:'C_UNIT',
			region: 'center',
			title: '单位信息',
			loadMask: null,
			store:this.unitStore,
			sm:sm,
			view: new Ext.ux.grid.ColumnLockBufferView(),
			columns:cm
			,tools:[{id:'search',qtip:'查询',cls:'tip-target',scope:this,handler:function(){
				
				var comp = this.tools.search;
				if(this.unitStore.getCount()>0){
					if(win)win.close();
						win = FilterWindow.addComp(this,[
							{text:'单位名称',field:'cust_name'},
							{text:'地址',field:'address',type:'textfield'}
							],300,null,false);
					if(win){
						win.setPosition(comp.getX()-win.width, comp.getY()-50);//弹出框右对齐
						win.show();
					}
				}else{
					Alert('请先查询数据！');
				}
		    }}]
			
		})
	},
	remoteRefresh:function(){
		this.unitStore.baseParams.residentCustId=App.data.custFullInfo.cust.cust_id;
		this.unitStore.load();
	},
	localRefresh:function(){
		var unitRecords = this.getSelectionModel().getSelections();
		for (var i in unitRecords){
			this.unitStore.remove(unitRecords[i]);
		}
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});


/**
 * 异动信息表格
 */ 
PropChangeGrid = Ext.extend(Ext.grid.GridPanel,{
	region:'center',
	border:false,
	propChangeStore:null,
	isReload : true,
	constructor:function(){
		this.propChangeStore = new Ext.data.JsonStore({
			fields: ["column_name_text","old_value","new_value","old_value_text",
				"new_value_text","change_time"]
		}); 
		
		var cm = [
			{header:'属性',dataIndex:'column_name_text',width:120},
			{header:'修改前',	dataIndex:'old_value_text',width:120},
			{header:'修改后',	dataIndex:'new_value_text',width:120},
			{header:'修改日期',dataIndex:'change_time',width:130}
		];
		var pageTbar = new Ext.PagingToolbar({store: this.propChangeStore ,pageSize : App.pageSize});
		pageTbar.refresh.hide();
		PropChangeGrid.superclass.constructor.call(this,{
			region: 'center',
			store:this.propChangeStore,
			columns:cm,
			bbar:pageTbar
		})
	},
	remoteRefresh:function(){
		if(App.getCustId()){
			Ext.Ajax.request({
				url:Constant.ROOT_PATH + "/commons/x/QueryCust!queryCustPropChange.action",
				scope:this,
				params:{custId:App.getCustId(),custType:App.getData().custFullInfo.cust.cust_type},
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					//PagingMemoryProxy() 一次性读取数据
					this.propChangeStore.proxy = new Ext.data.PagingMemoryProxy(data),
					//本地分页
					this.propChangeStore.load({params:{start:0,limit:App.pageSize}});
				}
			});
		}
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});



//PromFeeGrid = Ext.extend(Ext.grid.GridPanel,{
PromFeeGrid = Ext.extend(Ext.ux.Grid,{
	region:'center',
	border:false,
	promStore:null,
	isReload : true,
	showJDJE:function(){//查看解冻金额
		var record = App.getApp().main.infoPanel.custPanel.custDetailTab.promFeeGrid.selModel.getSelected();
		if(!record || Ext.isEmpty(record.get('done_code'))){
			Alert('未能正确获取数据！');return false;
		}
		Ext.Ajax.request({
			url : Constant.ROOT_PATH+"/core/x/Acct!queryPromAcctItemInactive.action",
			params:{doneCode:record.get('done_code'),custId:App.getCustId(),query:'T'},
			scope:this,
			timeout:99999999999999,//12位 报异常
			success:function(res,opt){
				var datas = Ext.decode(res.responseText);
				var actInfo = '';
				var prodMap = App.getApp().main.infoPanel.userPanel.prodGrid.prodMap;
				if(!datas || datas.length <=0){
					datas = [];
				}
				for(var index =0;index<datas.length;index++){
					for(var userid in prodMap){
						var arr = prodMap[userid];
						if(arr && arr.length>0){
							if(datas[index].acct_id != arr[0].acct_id){
								continue;
							}
							for(var idx =0;idx<arr.length;idx++){
								var item = arr[idx];
								if(datas[index].acct_id == item.acct_id && datas[index].acctitem_id == item.prod_id){
									actInfo += '' + item.prod_name + ' 冻结总金额  '  + datas[index].init_amount/100 + ' 元,其中已解冻 ' + datas[index].use_amount + ' 元</br>';
								}
							}
						}
					}
				}
				Alert(actInfo);
			}
		})
	},
	showDetail:function() {
					var record = this.selModel.getSelections()[0];
					var busiCode = record.get('busi_code');
					var doneCode = record.get('done_code');
					new DoneCodeInfoWindow(doneCode,busiCode).show();
				},
	constructor:function(){
		this.promStore = new Ext.data.JsonStore({
			fields: ['prom_fee_sn','prom_fee_id','status',"status_text","done_code","prom_fee_name","prom_fee","optr_name","create_time","busi_code"]
		}); 
		this.columns = [
			{header:'流水号',dataIndex:'done_code',width:100,renderer:function(value,metaData,record){
				 return '<div style="text-decoration:underline;font-weight:bold" onclick="App.getApp().main.infoPanel.custPanel.custDetailTab.promFeeGrid.showDetail();" ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
				}},
			{header:'套餐名称',	dataIndex:'prom_fee_name',width:120},
			{header:'状态',	dataIndex:'status_text',width:60,renderer:Ext.util.Format.statusShow},
			{header:'金额',	dataIndex:'prom_fee',width:100,renderer : Ext.util.Format.formatFee},
			{header:'日期',dataIndex:'create_time',width:130},
			{header:'操作员',	dataIndex:'optr_name',width:120},
			{header:'查看',dataIndex:'done_code',width:80,renderer:function(value,metaData,record){
				if(record.get('status') == 'ACTIVE'){
					return '';
				}
				 return '<div style="text-decoration:underline;font-weight:bold" ext:qtitle="" ext:qtip="' + value + '">' +
				 		'<a href="#" onclick="App.getApp().main.infoPanel.custPanel.custDetailTab.promFeeGrid.showJDJE();">已解冻金额</a>' +
				 		'</div>';
				}}
		];
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : this.columns });
    	this.selM = new Ext.grid.CheckboxSelectionModel();
		var pageTbar = new Ext.PagingToolbar({store: this.promStore ,pageSize : App.pageSize});
		pageTbar.refresh.hide();
		PromFeeGrid.superclass.constructor.call(this,{
			region: 'center',
			id:'C_PROMFEE',
			store:this.promStore,
			bbar:pageTbar,
			sm:this.selM,
			cm:cm,
			view: new Ext.ux.grid.ColumnLockBufferView()
		})
	},
	remoteRefresh:function(){
		if(App.getCustId()){
			Ext.Ajax.request({
				url:Constant.ROOT_PATH + "/commons/x/QueryCust!queryCustPromFee.action",
				scope:this,
				params:{custId:App.getCustId()},
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					//PagingMemoryProxy() 一次性读取数据
					this.promStore.proxy = new Ext.data.PagingMemoryProxy(data),
					//本地分页
					this.promStore.load({params:{start:0,limit:App.pageSize}});
				}
			});
		}
	},
	reset : function(){
		this.getStore().removeAll();
		this.isReload = true;
	}
});

/**
 * 客户设备信息表
 * @class DeviceGrid
 * @extends Ext.grid.GridPanel
 */
CustDeviceGrid = Ext.extend(Ext.ux.Grid,{
	region:'center',
	border:false,
	custDeviceStore:null,
	GDDeviceArray : [], //产权属于广电的设备
	CustDeviceArray :[],//产权属于个人的设备(自购)
	isReQuery:false,//是否重新查询客户
	parent:null,
	constructor:function(parent){
		this.parent = parent;
		this.custDeviceStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/commons/x/QueryCust!queryCustDevices.action",
			sortInfo: {field: 'buy_time', direction: 'DESC'},
			fields : ["device_id", "buy_mode", "buy_time",
				"status_date", "device_code", "status",'replacoverDate',"device_type","device_model","device_model_text",
				"device_type_text", "buy_mode_text","loss_reg","loss_reg_text","status_text",'replacover_date',
				"ownership_text","ownership","pair_card_id","pair_card_code","pair_stb_device_id",'interactive_type',
				"pair_modem_id","pair_modem_code","is_virtual_card","is_virtual_modem","definition_type","definition_type_text",
				"depot_id","depot_name","change_reason","change_reason_text"]
		}); 
		
		var cm = new Ext.ux.grid.LockingColumnModel({ 
    		columns : [
			{header:'设备类型',dataIndex:'device_type_text',width:60	},
			{header:'清晰类型',dataIndex:'definition_type_text',width:80},
//			{header:'设备型号',dataIndex:'device_model_text',width:80,renderer:App.qtipValue},
			{header:'设备号',dataIndex:'device_code',width:130,renderer:App.qtipValue},
			{header:'智能卡',dataIndex:'pair_card_code',width:110,renderer:App.qtipValue},
			{header:'MODEM',dataIndex:'pair_modem_code',width:110,renderer:App.qtipValue},
			{header:'状态',dataIndex:'status_text',width:60,renderer:Ext.util.Format.statusShow},
			{header:'产权',dataIndex:'ownership_text',width:60}/*,
			{header:'包换期',dataIndex:'replacover_date',width:80,renderer:function(v){if(!Ext.isEmpty(v)){return v.substr(0,10);}return v;}},
			{header:'购买时间',dataIndex:'buy_time',width:80,hidden:true},
			{header:'清晰类型',dataIndex:'definition_type_text',width:80},
			{header:'挂失',dataIndex:'loss_reg_text',width:60} */
	        ]
	      });

		CustDeviceGrid.superclass.constructor.call(this,{
			id:'C_DEVICE',
			store: this.custDeviceStore,
			cm: cm,
			sm: new Ext.grid.RowSelectionModel({}),
			title: '设备信息',
			view: new Ext.ux.grid.ColumnLockBufferView(),
			tools:[{id:'search',qtip:'查询',cls:'tip-target',scope:this,handler:function(){
				if(this.custDeviceStore.getCount()>0){
					var comp = this.tools.search;
					if(win)win.close();
					win = FilterWindow.addComp(this,[
							{text:'设备类型',field:'device_type',showField:'device_type_text'},
							{text:'设备号',field:'device_code',type:'textfield'},
							{text:'状态',field:'status',showField:'status_text'},
							{text:'清晰类型',field:'definition_type',showField:'definition_type_text'}
						],444,null,false);
					if(win){
						win.setPosition(comp.getX()-win.width, comp.getY()-50);//弹出框右对齐
						win.show();
					}
				}else{
					Alert('请先查询数据！');
				}
		    }}]
		})
	},
	initEvents :function(){
		this.custDeviceStore.on('load',this.getGDDevices,this);
		
		this.on("rowclick", this.doClickRecord, this );
		this.on("afterrender",function(){
			this.swapViews();
		},this,{delay:10});
		CustDeviceGrid.superclass.initEvents.call(this);
	},
	doClickRecord: function(g, i, e){
		//选中一条时才显示
		var records = g.getSelectionModel().getSelections();
		if(records.length == 1){
			var did = records[0].get("device_id");
			var type = records[0].get("device_type");
			var record = records[0];
			
			var deviceDetailTab = this.parent.deviceDetailTab;
			deviceDetailTab.resetPanel();
			deviceDetailTab.deviceId = did;
			deviceDetailTab.deviceType = type;
			deviceDetailTab.deviceRecord = record;
			deviceDetailTab.refreshPanel();
			
		}else{
			var deviceDetailTab = this.parent.deviceDetailTab;
			deviceDetailTab.resetPanel();
		}
		
	},
	getGDDevices : function(store,records){//查询产权是广电的设备
		//隐藏数据加载提示框
		App.hideTip();
		this.GDDeviceArray = [];
		this.CustDeviceArray = [];
		for(var i=0;i<records.length;i++){
			if(records[i].get('ownership') == CoreConstant.OWNERSHIP_GD){
				this.GDDeviceArray.push(records[i]);
			}else if(records[i].get('ownership') == CoreConstant.OWNERSHIP_CUST){
				this.CustDeviceArray.push(records[i]);
			}
		}
	},
	remoteRefresh: function(){
		this.isReQuery = true;
		this.GDDeviceArray = [];
		this.CustDeviceArray = [];
		this.custDeviceStore.baseParams.custId=App.getCustId();
		this.custDeviceStore.load();
		//显示数据加载提示框
		App.showTip();
		
		var status = App.getCust().status;
		//过滤tbar按钮
		if(status == 'RELOCATE' || status == 'DATACLOSE'){
			if(this.getTopToolbar()){
				this.getTopToolbar().hide();
			}
			this.doLayout();
		}else if(App.getCust().status == 'ACTIVE'){
			if(this.getTopToolbar()){
				this.getTopToolbar().show();
			}
		}
		
	},
	getSelectedDeviceIds:function(){
		//获取user面板中 选中的userids
		var params = [];
		var a = this.getSelectionModel().getSelections();
		for (var i=0;i<a.length;i++){
			params[i] = a[i].get("device_id");
		}
		return params;
	}
});



/**
 * 扩展信息面板 
 */
ExtInfoPanel = Ext.extend( Ext.Panel , {
	constructor: function(){
		ExtInfoPanel.superclass.constructor.call(this,{
			region: 'south',
			anchor: '100%',
			border: false
		});
	},
	remoteRefresh: function(){
		if(this.items && this.items.getCount() > 0){
       		this.removeAll(true);
       	}
       	var cfg = [{
	   	   columnWidth: .4,
	   	   layout: 'form',
	   	   baseCls: 'x-plain',
	   	   labelWidth: 80,
		   labelAlign: 'right'
	    },{
		   columnWidth: .5,
		   layout: 'form',
		   baseCls: 'x-plain',
//		   labelWidth: 80,
		   labelAlign: 'right'
	    }];
	    
	    if(App.getCustId()){
	    	var groupId = 1;
		    if(App.getData().custFullInfo.cust.cust_type == "RESIDENT"){
		    	groupId = 2;
		    }
			var panel = ExtAttrFactory.gExtView({
	     		tabName: Constant.C_CUST,
	     		pkValue: App.getCustId(),
	     		prefixName : 'cust.',
	     		group: groupId
	       	},cfg)
	       	this.add(panel);
	       	this.doLayout();
	    }
	}
});

var GeneralInfoHtml = 
'<table width="100%" border="0" cellpadding="0" cellspacing="0">' +
	'<tr height=24>'+
		'<td class="label" width="20%">用户数:</td>' +
		'<td class="input_bold">&nbsp;{[values.users.totalAmount|| "0"]}</td>'+
		'<td class="label">正常:</td>' +
		'<td class="input_bold">&nbsp;{[values.users.activeAmount|| "0"]}</td>'+
		'<td class="label">报停:</td>' +
		'<td class="input_bold">&nbsp;{[values.users.stopAmount || "0"]}</td>'+
		'<td class="label">&nbsp;</td>' +
		'<td class="input_bold">&nbsp;</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">模拟:</td>' +
		'<td class="input_bold">&nbsp;{[values.users.atvAmount|| "0"]}</td>'+
		'<td class="label">数字:</td>' +
		'<td class="input_bold">&nbsp;{[values.users.dtvAmount|| "0"]}</td>'+
		'<td class="label">宽带:</td>' +
		'<td class="input_bold">&nbsp;{[values.users.bandAmount|| "0"]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">产品总数:</td>' +
		'<td class="input_bold">&nbsp;{[values.prods.totalAmount || "0"]}</td>'+
		'<td class="label">正常:</td>' +
		'<td class="input_bold">&nbsp;{[values.prods.activeAmount || "0"]}</td>'+
		'<td class="label">机顶盒:</td>' +
		'<td class="input_bold">&nbsp;{[values.devices.stbs|| "0"]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">欠费未停:</td>' +
		'<td class="input_bold">&nbsp;{[values.prods.ownNotStopAmount || "0"]}</td>'+
		'<td class="label">欠费停:</td>' +
		'<td class="input_bold">&nbsp;{[values.prods.ownStopAmount || "0"]}</td>'+
		'<td class="label">智能卡:</td>' +
		'<td class="input_bold">&nbsp;{[values.devices.cards|| "0"]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">当前积分:</td>' +
		'<td class="input_bold">&nbsp;{[values.custs.balance || "0"]}</td>'+
		'<td class="label">历史积分:</td>' +
		'<td class="input_bold">&nbsp;{[values.custs.his_balance || "0"]}</td>'+
		'<td class="label">猫:</td>' +
		'<td class="input_bold">&nbsp;{[values.devices.modem|| "0"]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label">挂失设备:</td>' +
		'<td class="input_bold">&nbsp;{[values.devices.regLoss|| "0"]}</td>'+
	'</tr>' +
'</table>';
/**
 * 客户综合信息
 * @class GeneralPanel
 * @extends Ext.Panel
 */
GeneralPanel = Ext.extend(Ext.Panel,{
	tpl : null,
	constructor : function(){
		this.tpl = new Ext.XTemplate( GeneralInfoHtml );
		this.tpl.compile();
		GeneralPanel.superclass.constructor.call(this,{
			id : 'GeneralPanel',
			layout : 'fit',
			border : false,
			autoScroll:true,
			bodyStyle: "background:#F9F9F9",
			html : this.tpl.applyTemplate( App.getData().custGeneralInfo)
		})
	},
	refresh : function(){
		this.tpl.overwrite( this.body, App.getData().custGeneralInfo);
	},
	remoteRefresh : function(){
		var users = App.data.custGeneralInfo.users;
		var prods = App.data.custGeneralInfo.prods;
		var accts = App.data.custGeneralInfo.accts;
		var custs = App.data.custGeneralInfo.custs;
		var devices = App.data.custGeneralInfo.devices;
		var store = Ext.getCmp('C_DEVICE').custDeviceStore;//设备store
//		var store = App.main.infoPanel.custPanel.custDeviceGrid.custDeviceStore;
		Ext.apply(devices,{stbs:0,cards:0,modem:0,regLoss:0});
		
		store.each(function(rec){
			var data = rec.data;
			if(data.is_virtual_modem == 'T' || data.is_virtual_card== 'T'){
				return;
			}
			if(data.loss_reg == "T"){
				devices.regLoss +=1;
			}
			if(data.device_type == 'STB'){
				devices.stbs +=1;
			}else if(data.device_type == 'CARD'){
				devices.cards +=1;
			}else if(data.device_type == 'MODEM'){
				devices.modem +=1;
			}
		});
		
		var bonuspoint = App.data.custFullInfo.bonuspoint;
		if(bonuspoint){
			custs['balance'] = bonuspoint['balance'];
			custs['his_balance'] = bonuspoint['his_balance'];
		}else{
			custs['balance'] = custs['his_balance'] = 0;
		}
		var userStore = Ext.getCmp('U_USER').getStore();
		var prodData = Ext.getCmp('U_PROD').prodMap;
		var acctStore = Ext.getCmp('A_ACCT').getStore();
		
		if(userStore.getCount() == 0){
			for(var key in users){
				users[key] = 0;
			}
		}else{
			users.totalAmount = userStore.getCount();
			users.dtvAmount = 0;
			users.atvAmount = 0;
			users.bandAmount = 0;
			users.activeAmount = 0;
			users.stopAmount = 0;
			
			userStore.each(function(rec){
				var userType = rec.get('user_type');
				if(userType == 'DTV'){
					users.dtvAmount = users.dtvAmount + 1;
				}else if(userType == 'ATV'){
					users.atvAmount = users.atvAmount + 1;
				}else if (userType == 'BAND'){
					users.bandAmount == users.bandAmount + 1;
				}
				
				var status = rec.get('status');
				if(status == 'ACTIVE'){
					users.activeAmount = users.activeAmount + 1;
				}else{
					users.stopAmount = users.stopAmount + 1;
				}
			})
		}
		
		var hasProd = false;
		prods.totalAmount = 0;
		prods.activeAmount = 0;
		prods.ownNotStopAmount = 0;
		prods.ownStopAmount = 0;
		
		for(var key in prodData){
			prods.totalAmount = prods.totalAmount + prodData[key].length;
			for(var i=0;i<prodData[key].length;i++){
				
				if(prodData[key][i].status == 'ACTIVE'){
					prods.activeAmount = prods.activeAmount + 1;
				}else if(prodData[key][i].status == 'OUNSTOP'){//欠费未停
					prods.ownNotStopAmount = prods.ownNotStopAmount + 1;
				}else{//停机
					prods.ownStopAmount = prods.ownStopAmount + 1;
				}
			}
			hasProd = true;
		}
		if(!hasProd){
			for(var key in prods){
				prods[key] = 0;
			}
		}
		
		accts.totalBalance = 0;
		accts.totalOwn = 0;
		
		acctStore.each(function(rec){
			var acctitems = rec.get('acctitems');
			if(acctitems){
				for(var i=0;i<acctitems.length;i++){
					accts.totalBalance = accts.totalBalance
						+ acctitems[i].real_balance
						+ (acctitems[i].order_balance > 0 ? acctitems[i].order_balance : 0);
					accts.totalOwn = accts.totalOwn + acctitems[i].owe_fee;
				}
			}
		});
		
		this.refresh();
	}
});

CustDetailTab = Ext.extend(Ext.TabPanel,{
	propChangeGrid : null,
	generalPanel : null,
	promFeeGrid : null,
	constructor : function(){
		this.propChangeGrid = new PropChangeGrid();
		this.generalPanel = new GeneralPanel();
		this.promFeeGrid = new PromFeeGrid();
		CustDetailTab.superclass.constructor.call(this,{
			activeTab: 0,
			border : false,
			defaults: {
				layout: 'fit',
				border: false
			},
			items: [{
			    title: '综合信息',
			    items: [this.generalPanel]
			},{
				title: '异动信息',
			    items: [this.propChangeGrid]
			},{
				title: '套餐信息',
				items:[this.promFeeGrid]
			}]
		})
	}
})


var DeviceInfoHtml = 
'<table width="100%" border="0" cellpadding="0" cellspacing="0">' +
	'<tr height=24>'+
		'<td class="label" width="20%">设备号:</td>' +
		'<td class="input_bold"  width=30%>&nbsp;{[values.device_code|| ""]}</td>'+
		'<td class="label" width="20%">设备型号:</td>' +
		'<td class="input_bold"  width=30%>&nbsp;{[values.device_model_text|| ""]}</td>'+		
	'</tr>' +
	'<tr height=24>'+
		'<td class="label" >配对卡号:</td>' +
		'<td class="input_bold"  width=30%>&nbsp;{[values.pair_card_code|| ""]}</td>'+
		'<td class="label" width="20%">配对modem:</td>' +
		'<td class="input_bold"  width=30%>&nbsp;{[values.pair_modem_code|| ""]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label" >产权:</td>' +
		'<td class="input_bold"  width=30%>&nbsp;{[values.ownership_text|| ""]}</td>'+
		'<td class="label" width="20%">清晰类型:</td>' +
		'<td class="input_bold"  width=30%>&nbsp;{[values.definition_type_text|| ""]}</td>'+
	'</tr>' +
	'<tr height=24>'+
		'<td class="label" width="20%">购买方式:</td>' +
		'<td class="input_bold"  width=30%>&nbsp;{[values.buy_mode_text|| ""]}</td>'+
		'<td class="label" >购买时间:</td>' +
		'<td class="input_bold"  width=30%>&nbsp;{[values.buy_time|| ""]}</td>'+
	'</tr>' +	
	'<tr height=24>'+	
		'<td class="label" width="20%">包换期:</td>' +
		'<td class="input_bold"  width=30%>&nbsp;{[fm.dateFormat(values.replacover_date)|| ""]}</td>'+
		'<td class="label" >挂失:</td>' +
		'<td class="input_bold"  width=30%>&nbsp;{[values.loss_reg_text|| ""]}</td>'+
	'</tr>' +
	'<tr height=24>'+	
		'<td class="label" width="20%"> 更换原因:</td>' +
		'<td class="input_bold"  width=30%>&nbsp;{[values.change_reason_text|| ""]}</td>'+
	'</tr>' +	
'</table>';
/**
 * 客户综合信息
 * @class GeneralPanel
 * @extends Ext.Panel
 */
DeviceDetailPanel = Ext.extend(Ext.Panel,{
	tpl : null,
	constructor : function(){
		this.tpl = new Ext.XTemplate( DeviceInfoHtml );
		this.tpl.compile();
		DeviceDetailPanel.superclass.constructor.call(this,{
			id : 'DevicePanel',
			layout : 'fit',
			border : false,
			autoScroll:true,
			bodyStyle: "background:#F9F9F9",
			html : this.tpl.applyTemplate({})
		})
	},
	refresh : function(record){
		this.tpl.overwrite( this.body, record.data);
	},
	reset:function(){//重置详细信息
		this.tpl.overwrite( this.body, {});
	}
});


DeviceDetailTab = Ext.extend(Ext.TabPanel,{
	deviceDetailPanel : null,
	deviceId:null,
	deviceType:null,
	deviceRecord:null,
	isReload:null,
	constructor : function(){
		this.deviceDetailPanel = new DeviceDetailPanel();
		DeviceDetailTab.superclass.constructor.call(this,{
			activeTab: 0,
			border : false,
			defaults: {
				layout: 'fit',
				border: false
			},
			items: [{
			    title: '综合信息',
			    items: [this.deviceDetailPanel]
			}]
		})
	},
	resetPanel : function(){//重置TAB面板
		this.deviceDetailPanel.reset();
		this.deviceId = null;
		this.deviceType = null;
		this.deviceRecord = null;
		this.isReload = true;
	},
	refreshPanel: function(){
		this.deviceDetailPanel.refresh(this.deviceRecord);
	}
})




/**
 * 客户查询的结果显示面板构造，
 * 包括客户基本信息、客户套餐、过户信息、移机信息、设备信息等面板
 */
CustPanel = Ext.extend( BaseInfoPanel , {
	// 面板属性定义
	custInfoPanel: null,
	custDetailTab : null,
	custDeviceGrid: null,
	unitGrid:null,
	packageGrid: null,
	deviceDetailTab: null,
	userPanel: null,
	
	constructor: function(){
		//子面板实例化
		this.custInfoPanel =new CustInfoPanel('C_CUST');
		this.custDetailTab = new CustDetailTab();
		this.custDeviceGrid = new CustDeviceGrid(this);
		this.deviceDetailTab = new DeviceDetailTab();
		this.unitGrid = new UnitGrid(); 
		
		this.userPanel = new UserPanel();
		
		CustPanel.superclass.constructor.call(this, {
			layout:"border",
			border:false,
			items:[{
				region:"center",
				layout:"anchor",
				border: false,
				items:[{
					anchor:"100% 62%",
					layout:'fit',
					border: false,
					bodyStyle: 'border-right-width: 1px',	
					items:[this.custInfoPanel]
				},{
					anchor:"100% 38.2%",
					layout:'fit',
					bodyStyle: 'border-left-width: 0; border-bottom-width: 0;',
					items:[this.custDetailTab]
				}]
			},{
				region:"east",
				split:true,
				width:"80%",
				layout:"fit",
				id: 'USER_PANEL',
				border: false,
				items: [this.userPanel]
//				items:[{
//					anchor:"100% 62%",
//					layout:'fit',
//					border: false,
//					bodyStyle: 'border-left-width: 1px',	
//					items:[this.custDeviceGrid]
//				},{
//					anchor:"100% 38%",
//					layout:'fit',
//					items:[this.deviceDetailTab]
//				}]
			}]
		});
	},
	refresh:function(){
		this.custInfoPanel.remoteRefresh();
		this.custDeviceGrid.remoteRefresh();
		this.deviceDetailTab.resetPanel();
		this.refreshPropChangeGrid();
		this.refreshPromFeeGrid();
	},
	refreshPropChangeGrid : function(){
		this.custDetailTab.propChangeGrid.remoteRefresh();
	},
	refreshPromFeeGrid : function(){
		this.custDetailTab.promFeeGrid.remoteRefresh();
	}
});
Ext.reg( "custpanel" , CustPanel );