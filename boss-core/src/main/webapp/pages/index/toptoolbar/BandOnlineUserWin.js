/**
 * 宽带在线用户查看
 * @class
 * @extends Ext.grid.GridPanel
 */
var BandOnlinUserForm = Ext.extend(Ext.form.FormPanel, {
	grid:null,
	constructor: function(grid){
		this.grid = grid;
		BandOnlinUserForm.superclass.constructor.call(this, {
			layout:'column',
			region:'north',
			height:70,
			bodyStyle:'padding-top:12px',
			defaults:{
				layout:'form',
				border:false,
				defaults:{
					border:false
				}
			},
			labelWidth:75,
			items:[
				{columnWidth:.42,items:[
					{fieldLabel:'宽带账号',name:'loginName',xtype:'textfield',allowBlank:false,
						minLength:3,maxLength:20,
						minLengthText:'账号最小长度为3',
						maxLengthText:'账号最小长度为20'
					},
					{fieldLabel:'开始日期',xtype:'datefield',name:'startDate',format:'Y-m-d'}
				]},
				{columnWidth:.43,items:[
					{fieldLabel:'资源名',xtype:'combo',hiddenName:'resName',
						store:new Ext.data.JsonStore({
							url: Constant.ROOT_PATH + '/commons/x/QueryUser!queryZteBandRes.action',
							fields:["server_id","res_name","boss_res_id"]
						}),displayField:'res_name',valueField:'res_name',triggerAction:'all',
						editable:true,forceSelection:true,selectOnFocus:true
					},
					{fieldLabel:'结束日期',xtype:'datefield',name:'endDate',format:'Y-m-d'}
				]},
				{columnWidth:.15,items:[
					{xtype:'button',text:'查询',iconCls:'icon-query',scope:this,handler:this.doQuery},
					{xtype:'button',text:'重置',iconCls:'icon-reset',scope:this,handler:this.doRest}
				]}
			]
		});
		this.getForm().findField('resName').getStore().load();
	},
	doQuery: function(){
		if(this.getForm().isValid()){
			var values = this.getForm().getValues();
			var msg = Show();
			Ext.Ajax.request({
				url: Constant.ROOT_PATH + '/commons/x/QueryUser!queryBandOnlineUser.action',
				scope:this,
				params:values,
				success:function(res,opt){
					msg.hide();
					msg = null;
					var data = Ext.decode(res.responseText);
					if(data['count'] > 0 && data['userOnlineList']){
						this.grid.getStore().proxy = new Ext.data.PagingMemoryProxy(data['userOnlineList']),
						this.grid.getStore().load({params:{start:0,limit:20}});
					}else{
						Alert("无在线用户数据");
						this.grid.getStore().removeAll();
					}
				}
			});
		}
	},
	doRest: function(){
		this.getForm().reset();
	}
}); 

var BandOnlineUserGrid = Ext.extend(Ext.grid.GridPanel, {
	constructor:function(){
		this.store = new Ext.data.JsonStore({
			fields:['accessTime','account','clientIp','clientMac','nasIp','nasPort','nasPortType',
				'userGroupName','userServiceName','vlan']
		});
		var columns = [
			{header:'宽带账号',dataIndex:'account',width:80,renderer : App.qtipValue},
			{header:'接入服务器IP',dataIndex:'nasIp',width:100,renderer : App.qtipValue},
			{header:'接入端口',dataIndex:'nasPort',width:120,renderer : App.qtipValue},
			{header:'接入时间',dataIndex:'accessTime',width:130},
			{header:'客户端IP',dataIndex:'clientIp',width:100,renderer : App.qtipValue},
			{header:'客户端Mac',dataIndex:'clientMac',width:120,renderer : App.qtipValue},
			{header:'接入vlan',dataIndex:'vlan',width:60},
			{header:'用户组名称',dataIndex:'userGroupName',width:80,renderer : App.qtipValue},
			{header:'服务名称',dataIndex:'userServiceName',width:80,renderer : App.qtipValue},
			{header:'物理端口类型',dataIndex:'nasPortType',width:85,renderer : App.qtipValue} 
		];
		BandOnlineUserGrid.superclass.constructor.call(this,{
			id:'bandOnlineUserGridId',
			region:'center',
			ds:this.store,
			loadMask : true,
			columns:columns,
			bbar:new Ext.PagingToolbar({store: this.store ,pageSize: 20}),
			tbar:[
				'-','输入宽带账号过滤：','-',
				{id:'band_onlin_acctout_id',xtype:'textfield',scope:this,
					listeners: {
	                    scope: this,
	                    specialkey: function (txt, e) {
	                        if (e.getKey() == Ext.EventObject.ENTER) {
	                            this.doFilter(txt.getValue());
	                        }
	                    }
                	}
                },'-',
				{xtype:'button',text:'过滤',scope:this,handler:function(){
						this.doFilter(Ext.getCmp('band_onlin_acctout_id').getValue());
					}
				},'-'
			]
		});
	},
	doFilter: function(value){
		if(!Ext.isEmpty(value)){
			this.getStore().filterBy(function(record){
				return record.get('account').indexOf(value) > -1;
			});
		}else{
			 this.getStore().clearFilter();
		}
	}
});
var BandOnlineUserWin = Ext.extend(Ext.Window,{
	constructor:function(){
		var grid = new BandOnlineUserGrid();
		var form = new BandOnlinUserForm(grid);
		BandOnlineUserWin.superclass.constructor.call(this,{
			id:'bandOnlineUserWinId',
			closeAction : 'close',
			title:'在线宽带用户信息',
			width:800,
			height : 500,
			border:false,
			layout : 'border',
			items:[form,grid]
		});
	}
});
