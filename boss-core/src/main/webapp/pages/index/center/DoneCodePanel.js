DoneResultGrid =Ext.extend(Ext.grid.GridPanel,{ 
	constructor:function(doneCode,busiCode){
		var columns=[],fields=[];showRemark = false;
		if(busiCode == '1020'){		//用户开户
			columns = [
				{header: '终端类型',dataIndex: 'user_name',width:100},
	         	{header: '用户状态',dataIndex: 'status_text',width:70,renderer:Ext.util.Format.statusShow},
	         	{header: '用户类型',dataIndex: 'user_type_text'},
	         	{header: '机顶盒号',dataIndex: 'stb_id',width:140},
	         	{header: '智能卡号',dataIndex: 'card_id',width:140},
	         	{header: 'Modem号',dataIndex: 'modem_mac',width:140}
	        ];
	        fields = [
	        	'user_name','status_text','user_type',
			    'user_type_text','stb_id','card_id','modem_mac'
			];
		}else if(busiCode == '1015'){		//用户订购
	        columns = [
	        	{header: '产品名称',dataIndex: 'prod_name',width:100,renderer:App.qtipValue},
				{header: '产品状态',dataIndex: 'status_text',width:60,renderer:Ext.util.Format.statusShow},
				{header: '资费',dataIndex: 'tariff_name',width:55},
				{header: '到期日',dataIndex: 'invalid_date',width:75,renderer:Ext.util.Format.dateFormat},
				{header: '用户类型',dataIndex: 'user_type_text',width:65},
				{header: '机顶盒号',dataIndex: 'stb_id',width:130},
				{header: '智能卡号',dataIndex: 'card_id',width:125},
				{header: 'Modem号',dataIndex: 'modem_mac',width:120}
	         ];
	         fields = [
	         	'user_type','status_text','user_type_text','stb_id',
	         	'card_id','modem_mac','tariff_name','invalid_date','prod_name'
		     ];
		}else if(busiCode == '1007'){ //购买设备
			 columns = [
	        	{header: '设备类型',dataIndex: 'device_type_text',width:100,renderer:App.qtipValue},
				{header: '设备编号',dataIndex: 'device_code',width:120,renderer:App.qtipValue},
				{header: '状态',dataIndex: 'status_text',width:60,renderer:Ext.util.Format.statusShow},
				{header: '配对卡号',dataIndex: 'pair_card_code',width:120,renderer:App.qtipValue},
				{header: '配对MODEM号',dataIndex: 'pair_modem_code',width:120,renderer:App.qtipValue},
				{header: '购买类型',dataIndex: 'buy_mode_text',width:80}
	         ];
	         fields = [
	         	'device_type_text','device_code','status_text','pair_card_code',
	         	'pair_modem_code','buy_mode_text'
		     ];
		
		}else if(busiCode == '1009'){ //更换设备
		 	columns = [
		 		{header: '用户类型',dataIndex: 'user_type_text',width:100,renderer:App.qtipValue},
	        	{header: '修改属性',dataIndex: 'column_name_text',width:100,renderer:App.qtipValue},
				{header: '变更前',dataIndex: 'old_value',width:140,renderer:App.qtipValue},
				{header: '变更后',dataIndex: 'new_value',width:140,renderer:App.qtipValue}
	         ];
	         fields = ['column_name_text','old_value','new_value','user_type_text'];
			
		}else if(busiCode == '1064' || busiCode =='9003'){ //促销,自动促销
			 columns = [
		 		{header: '促销名称',dataIndex: 'promotion_name',width:250,renderer:App.qtipValue},
	        	{header: '状态',dataIndex: 'status_text',width:100,renderer:Ext.util.Format.statusShow}
	         ];
	         fields = ['promotion_name','status_text'];
		}else if(busiCode == '1688'){ //套餐
			 columns = [
		 		{header: '用户名称',dataIndex: 'user_name',width:200,renderer:App.qtipValue},
		 		{header: '产品名称',dataIndex: 'prod_name',width:120,renderer:App.qtipValue},
	        	{header: '缴费月份',dataIndex: 'months',width:60},
	        	{header: '实缴金额',dataIndex: 'real_pay',width:70,renderer : Ext.util.Format.formatFee},
	        	{header: '应缴金额',dataIndex: 'should_pay',width:70,renderer : Ext.util.Format.formatFee},
	        	{header: '退款金额',dataIndex: 'refund_pay' ,width:70,renderer:Ext.util.Format.formatFee},
	        	{header: '绑定到期日',dataIndex: 'bind_invalid_date',width:100,renderer:Ext.util.Format.dateFormat}
	         ];
	         showRemark=true;
	         fields = ['user_name','months','real_pay','should_pay','bind_invalid_date','prod_name','refund_pay','remark'];
		}else if(busiCode == '1127'){	//宽带升级
			columns = [
		 		{header: '原产品',dataIndex: 'old_prod_name',width:100},
		 		{header: '新产品',dataIndex: 'prod_name',width:100},
	        	{header: '原资费',dataIndex: 'old_tariff_name',width:70},
	        	{header: '新资费',dataIndex: 'tariff_name',width:70},
	        	{header: '资金类型',dataIndex: 'fee_type_text',width:70},
	        	{header: '变更类型',dataIndex: 'change_type_text',width:70},
	        	{header: '变更前金额',dataIndex: 'pre_fee',width:80,renderer : Ext.util.Format.formatFee},
	        	{header: '变更金额',dataIndex: 'change_fee',width:80,renderer:Ext.util.Format.formatFee},
	        	{header: '变更后金额',dataIndex: 'fee',width:80,renderer:Ext.util.Format.formatFee}
	         ];
	         fields = ['old_prod_name','prod_name','old_tariff_name',
	         	'tariff_name','pre_fee','change_fee','fee','change_type_text','fee_type_text'];
		}
		var queryStore = new Ext.data.JsonStore({ 
			url  : root + '/core/x/DoneCode!getGridDate.action' ,
			root : 'records' ,
			totalProperty: 'totalProperty',
			baseParams: {limit: App.pageSize,start: 0 ,doneCode : doneCode },
			fields: fields       
		}); 
		queryStore.load({
			params:{doneCode:doneCode}
		});
		DoneResultGrid.superclass.constructor.call(this,{
			store:queryStore,
			columns:columns,
			region : 'center',
			viewConfig: {
	            forceFit:true,
	            enableRowBody:true,
	            showPreview:showRemark,
	            getRowClass : function(record, rowIndex, p, store){
	                if(this.showPreview && rowIndex==0){
	                    p.body = '<p style="font-size:14px;font-family:NSimSun;color:#337FE5;">'+record.data.remark+'</p>';
	                    return 'x-grid3-row-expanded';
	                }
	                return 'x-grid3-row-collapsed';
	            }
	        },
			bbar: new Ext.PagingToolbar({store: queryStore ,pageSize:App.pageSize})
		});
	}
});

DoneCodeInfoWindow = Ext.extend( Ext.Window , {
	doneGrid:null,
	constructor: function ( doneCode,busiCode){
		this.doneGrid = new DoneResultGrid(doneCode,busiCode);
		DoneCodeInfoWindow.superclass.constructor.call(this,{
			width: 760,
			height: 400,
			title: '操作明细',
			layout: 'fit',
			border: false,
			items: this.doneGrid
		});
	}
});
/**
 * 受理记录表格
 */
DoneCodeGrid = Ext.extend(Ext.ux.Grid,{
	pageSize:20,
	doneCodeStore:null,
	constructor:function(){
		this.doneCodeStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/commons/x/QueryCust!queryCustDoneCode.action",
			totalProperty:'totalProperty',
			root:'records',
			fields: ["done_code","busi_code","done_date","optr_id",
				"busi_name","optr_name","status","status_text","remark", "extAttrs","attr_remark",
				'cancel','ignore','busi_fee','cancel_text','ignore_text','dept_name','real_pay','fee_id'],
			sortInfo:{
				field:'done_date',
				direction:'DESC'
			}
		}); 
		var lc = langUtils.main("doneCode.columns");
		var cm = [
			{header:lc[0],dataIndex:'done_code',width:100,renderer:function(value,metaData,record){
				var busiCode = record.get('busi_code');
					if(busiCode == '1020' || busiCode == '1015' || busiCode == '1064' 
						|| busiCode == '1009' || busiCode == '9003'|| busiCode == '1007' || busiCode == '1127'){
						return '<div style="text-decoration:underline;font-weight:bold" ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
					}else{
						return '<div ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
					}
				}
			},
			{header:lc[1],dataIndex:'busi_name',	width:100},
			{header:lc[2],dataIndex:'status_text',	width: 60,renderer:Ext.util.Format.statusShow},
			{header:lc[3],dataIndex:'done_date',	width:125},
			{header:lc[4],dataIndex:'optr_name',	width:100},
			{header:lc[5],dataIndex:'dept_name',width:120},
			{header:lc[6],dataIndex:'cancel_text',	width: 50},
			{header:lc[7],dataIndex:'ignore_text',	width: 50},
			{header:lc[8],dataIndex:'real_pay',	width: 50,renderer: Ext.util.Format.formatFee},
			{header:lc[9],dataIndex:'attr_remark',width: 200},
			{header:lc[10],dataIndex:'remark',	width: 200,renderer:App.qtipValue}
		];
		
		DoneCodeGrid.superclass.constructor.call(this,{
			id:'D_DONE',
			title:langUtils.main("doneCode._title"),
			region: 'center',
			border:false,
			store:this.doneCodeStore,
			columns:cm,
			sm : new Ext.grid.CheckboxSelectionModel(),
			bbar: new Ext.PagingToolbar({store: this.doneCodeStore ,pageSize : this.pageSize}),
			tools:[{id:'search',qtip:'查询',cls:'tip-target',scope:this,handler:function(){
				var comp = this.tools.search;
				if(this.doneCodeStore.getCount()>0){
					if(win)win.close();
					win = FilterWindow.addComp(this,[
						{text:'业务名称',field:'busi_name',type:'textfield'},
						{text:'操作员',field:'optr_name',type:'textfield'},
						{text:'状态',field:'status',showField:'status_text',
							data:[
								{'text':'状态','value':''},
								{'text':'正常','value':'ACTIVE'},
								{'text':'失效','value':'INVALID'}
							]
						}
					],365,null,true,"queryFeeInfo");
					if(win){
						win.setPosition(comp.getX()-win.width, comp.getY() - 50);//弹出框右对齐
						win.show();
					}
				}else{
					Alert('请先查询数据！');
				}
		    }}],	
			listeners : {
				"rowdblclick" : function(g, i, e) {
					var record = g.getStore().getAt(i);
					var busiCode = record.get('busi_code');
					if(busiCode == '1020' || busiCode == '1015'|| busiCode == '1064'
					|| busiCode == '1009'|| busiCode == '1007' || busiCode == '9003' || busiCode == '1127'){
						var doneCode = record.get('done_code');
						new DoneCodeInfoWindow( doneCode,busiCode ).show();
					}
				}
			}
		})
	},
	remoteRefresh:function(){
		this.doneCodeStore.baseParams = {custId: App.getCustId()};
		this.reloadCurrentPage();
	}
});

DoneCodePanel = Ext.extend(BaseInfoPanel,{
	 doneCodeGrid : null,
	// 面板属性定义
	
	constructor: function(){
		this.doneCodeGrid = new DoneCodeGrid();
		DoneCodePanel.superclass.constructor.call(this, {
			layout:"border",
			border:false,
			items:[this.doneCodeGrid]
		});
	},
	refresh:function(){
		this.doneCodeGrid.remoteRefresh();
	}
});
Ext.reg( "doneCodePanel" , DoneCodePanel );