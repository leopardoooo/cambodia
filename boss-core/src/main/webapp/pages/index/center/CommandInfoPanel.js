/**
 * ca指令面板
 * @class
 * @extends Ext.grid.GridPanel
 */
var CaGrid = Ext.extend(Ext.grid.GridPanel,{
	caStore:null,
	constructor:function(){
		this.caStore = new Ext.data.JsonStore({
			url:  root + '/commons/x/QueryCust!queryCaCommand.action',
			root:'records',
			totalProperty:'totalProperty',
			fields:['done_code','stb_id','card_id','prg_name','result_flag','control_id','auth_end_date',
				'error_info','record_date','send_date','ret_date','cmd_type_text']
		});
		var lc = langUtils.main("cmd.dtt.columns");
		var cm = [
			{header:lc[0],dataIndex:'done_code',width:90,sortable:true,renderer:App.qtipValue},
			{header:lc[1],dataIndex:'stb_id',width:130,sortable:true,renderer:App.qtipValue},
			{header:lc[2],dataIndex:'card_id',width:90,sortable:true,renderer:App.qtipValue},
			{header:lc[3],dataIndex:'control_id',width:90,sortable:true},
			{header:lc[4],dataIndex:'prg_name',width:100,sortable:true,renderer:App.qtipValue},
			{header:lc[5],dataIndex:'cmd_type_text',width:120,sortable:true,renderer:App.qtipValue},
			{header:lc[6],dataIndex:'result_flag',width:100,sortable:true,renderer:App.qtipValue},
			{header:lc[7],dataIndex:'record_date',width:120,sortable:true,renderer:App.qtipValue},
			{header:lc[8],dataIndex:'send_date',width:120,sortable:true,renderer:App.qtipValue},
			{header:lc[9],dataIndex:'ret_date',width:120,sortable:true,renderer:App.qtipValue},
			{header:lc[10],dataIndex:'error_info',width:100,sortable:true,renderer:App.qtipValue},
			{header:lc[11],dataIndex:'auth_end_date',width:100,sortable:true,renderer:function(val){
				return Ext.isEmpty(val)?val:val.substr(0,4) +'-' + val.substr(4,2)+'-' + val.substr(6,2);
			}}
			
		];
		CaGrid.superclass.constructor.call(this,{
			id:'caGridId',
			title: langUtils.main("cmd.dtt._title"),
			region:'center',
			autoScroll:true,
			ds:this.caStore,
			columns:cm,
			/*tbar : [{
				text : '业务指令',iconCls:'ca_1',scope : this,handler : function(){
					this.doLoad('J_CA_COMMAND_DAY');
				}
			},'-',{
				text : '优化指令',iconCls:'ca_2',scope : this,handler : function(){
					this.doLoad('J_CA_COMMAND_OUT_BAK');
				}
			},'-',{
				text : '历史指令',iconCls:'ca_3',scope : this,handler : function(){
					this.doLoad('J_CA_COMMAND_HIS');
				}
			}],*/
			bbar: new Ext.PagingToolbar({store: this.caStore ,pageSize : App.pageSize})
		});
	},
	remoteRefresh:function(){
		this.caStore.baseParams.custId = App.getData().custFullInfo.cust.cust_id;
		this.caStore.baseParams.type = 'J_CA_COMMAND_OUT';
		this.refresh();
	},
	refresh:function(){
		this.caStore.load({
			params : { start: 0, limit:App.pageSize }
		});
	},
	doLoad : function(type){
		if(App.getData().custFullInfo.cust.cust_id){
			this.caStore.baseParams.custId = App.getData().custFullInfo.cust.cust_id;
			this.caStore.baseParams.type = type;
			this.refresh();
		}
	}
});

/**
 * vod指令面板
 * @class
 * @extends Ext.grid.GridPanel
 */
var VodGrid = Ext.extend(Ext.grid.GridPanel,{
	store : null,
	constructor:function(p){
		this.store = new Ext.data.JsonStore({
			url:  root + '/commons/x/QueryCust!queryVodCommand.action',
			root:'records',
			totalProperty:'totalProperty',
			fields:['done_code','stb_id','card_id','modem_mac','is_success','transnum',
				'error_info','send_time','cmd_type_text','is_success_text']
		});
		var lc = langUtils.main("cmd.ott.columns");
		var cm = [
			{header:lc[0],dataIndex:'transnum',width:100,sortable:true},
			{header:lc[1],dataIndex:'done_code',width:80,sortable:true,renderer:App.qtipValue},
			{header:lc[2],dataIndex:'cmd_type_text',width:120,sortable:true,renderer:App.qtipValue},
			{header:lc[3],dataIndex:'stb_id',width:130,sortable:true,renderer:App.qtipValue},
//			{header:lc[4],dataIndex:'card_id',width:130,sortable:true,renderer:App.qtipValue},
			{header:lc[5],dataIndex:'modem_mac',width:130,sortable:true,renderer:App.qtipValue},
			{header:lc[6],dataIndex:'is_success_text',width:130,sortable:true},
			{header:lc[7],dataIndex:'error_info',width:90,sortable:true,renderer:App.qtipValue},
			{header:lc[8],dataIndex:'send_time',width:120,sortable:true,renderer:App.qtipValue}
		];
		VodGrid.superclass.constructor.call(this,{
			id:'vodGridId',
			region:'center',
			title: langUtils.main("cmd.ott._title"),
			autoScroll:true,
			ds:this.store,
			columns:cm,
			/*tbar : [{
				text : '业务指令',iconCls:'ca_1',scope : this,handler : function(){
					this.doLoad('J_VOD_COMMAND');
				}
			},'-',{
				text : '历史指令',iconCls:'ca_2',scope : this,handler : function(){
					this.doLoad('J_VOD_COMMAND_HIS');
				}
			}],*/
			bbar: new Ext.PagingToolbar({store: this.store ,pageSize : App.pageSize})
		});
	},
	remoteRefresh:function(){
		this.store.baseParams.custId = App.getData().custFullInfo.cust.cust_id;
		this.store.baseParams.type='J_VOD_COMMAND';
		this.refresh();
	},
	refresh:function(){
		this.store.load({
			params : { start: 0, limit:App.pageSize }
		});
	},
	doLoad : function(type){
		if(App.getData().custFullInfo.cust.cust_id){
			this.store.baseParams.custId = App.getData().custFullInfo.cust.cust_id;
			this.store.baseParams.type = type;
			this.refresh();
		}
	}
});

/**
 * 宽带指令面板
 * @class
 * @extends Ext.grid.GridPanel
 */
var BandGrid = Ext.extend(Ext.grid.GridPanel,{
	bandStore:null,
	constructor:function(p){
		
		this.bandStore = new Ext.data.JsonStore({
			url:  root + '/commons/x/QueryCust!queryBandCommand.action',
			root:'records',
			totalProperty:'totalProperty',
			fields:['done_code','stb_id','modem_mac','is_success','transnum',
				'error_info','send_time','cmd_type_text','is_success_text']
		});
		var lc = langUtils.main("cmd.band.columns");
		var cm = [
			{header:lc[0],dataIndex:'transnum',width:100,sortable:true},
			{header:lc[1],dataIndex:'done_code',width:80,sortable:true,renderer:App.qtipValue},
			{header:lc[2],dataIndex:'cmd_type_text',width:120,sortable:true,renderer:App.qtipValue},
//			{header:lc[3],dataIndex:'stb_id',width:130,sortable:true,renderer:App.qtipValue},
			{header:lc[4],dataIndex:'modem_mac',width:130,sortable:true,renderer:App.qtipValue},
			{header:lc[5],dataIndex:'is_success_text',width:130,sortable:true},
			{header:lc[6],dataIndex:'error_info',width:90,sortable:true,renderer:App.qtipValue},
			{header:lc[7],dataIndex:'send_time',width:120,sortable:true,renderer:App.qtipValue}
		];
		BandGrid.superclass.constructor.call(this,{
			id:'bandGridId',
			region:'center',
			title:langUtils.main("cmd.band._title"),
			ds:this.bandStore,
			autoScroll:true,
			columns:cm,		
//			tbar : [{text : '业务指令',iconCls:'ca_1',scope : this,handler : function(){this.doLoad();}}],
			bbar: new Ext.PagingToolbar({store: this.bandStore ,pageSize : App.pageSize})
		});
	},
	remoteRefresh:function(){
		this.bandStore.baseParams.custId = App.getData().custFullInfo.cust.cust_id;
		this.refresh();
	},
	refresh:function(){
		this.bandStore.load({
			params : { start: 0, limit:App.pageSize }
		});
	},
	doLoad : function(){
		if(App.getData().custFullInfo.cust.cust_id){
			this.store.baseParams.custId = App.getData().custFullInfo.cust.cust_id;
			this.refresh();
		}
	}
});

CommandInfoPanel = Ext.extend(BaseInfoPanel,{
	caGrid:null,
	vodGrid:null,
	bandGrid:null,
	constructor:function(){
		this.caGrid = new CaGrid();
		this.vodGrid = new VodGrid(this);
		this.bandGrid = new BandGrid(this);
		CommandInfoPanel.superclass.constructor.call(this,{
			id:'commandInfoPanelId',
			border:false,
			layout:'border',
			items:[{
				region:'center',
				border:false,
				layout:'fit',
				bodyStyle: 'border-right-width: 1px',
				defaults: {border: false},
				items:[this.caGrid]
			},{
				width:'45%',
				split:true,
				region:'east',
				layout:'anchor',
				border:false,
				items:[{
					anchor:'100% 50%',
					layout:'border',
					border:false,
					bodyStyle: 'border-left-width: 1px',
					defaults: {border: false},
					items:[this.vodGrid]
				},{
					anchor:'100% 50%',
					layout:'border',
					border:false,
					bodyStyle: 'border-left-width: 1px; border-top-width: 1px',
					defaults: {border: false},
					items:[this.bandGrid]
				}]
			}]
		});
	},
	refresh:function(){
		this.caGrid.remoteRefresh();
		this.vodGrid.remoteRefresh();
		this.bandGrid.remoteRefresh();
	}
});
Ext.reg( "commandInfoPanel" , CommandInfoPanel );