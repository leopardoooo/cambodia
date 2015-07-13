/**
 * 宽带认真失败日志查询
 * @class
 * @extends Ext.grid.GridPanel
 */
var BandUserFaildLogForm = Ext.extend(Ext.form.FormPanel, {
	grid:null,
	constructor: function(grid){
		this.grid = grid;
		BandUserFaildLogForm.superclass.constructor.call(this, {
			layout:'column',
			region:'north',
			height:40,
			bodyStyle:'padding-top:10px',
			defaults:{
				layout:'form',
				border:false,
				defaults:{
					border:false
				}
			},
			labelWidth:75,
			items:[
				{columnWidth:.28,items:[
					{fieldLabel:'宽带账号',name:'loginName',xtype:'textfield',allowBlank:false,
						minLength:3,maxLength:20,
						minLengthText:'账号最小长度为3',
						maxLengthText:'账号最小长度为20'
					}
				]},
				{columnWidth:.26,items:[
					{fieldLabel:'开始日期',xtype:'datefield',name:'startDate',format:'Y-m-d'}
				]},
				{columnWidth:.26,items:[
					{fieldLabel:'结束日期',xtype:'datefield',name:'endDate',format:'Y-m-d'}
				]},
				{columnWidth:.1,items:[
					{xtype:'button',text:'查询',iconCls:'icon-query',scope:this,handler:this.doQuery}
				]},
				{columnWidth:.1,items:[
					{xtype:'button',text:'重置',iconCls:'icon-reset',scope:this,handler:this.doRest}
				]}
			]
		});
	},
	doQuery: function(){
		if(this.getForm().isValid()){
			var values = this.getForm().getValues();
			var msg = Show();
			Ext.Ajax.request({
				url: Constant.ROOT_PATH + '/commons/x/QueryUser!queryBandUserFailedLog.action',
				scope:this,
				params:values,
				success:function(res,opt){
					msg.hide();
					msg = null;
					var data = Ext.decode(res.responseText);
					if(data['count'] > 0 && data['userFailedList']){
						this.grid.getStore().proxy = new Ext.data.PagingMemoryProxy(data['userFailedList']),
						this.grid.getStore().load({params:{start:0,limit:20}});
					}else{
						Alert("无认证失败日志");
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

var BandUserFailedLogGrid = Ext.extend(Ext.grid.GridPanel, {
	constructor:function(p){
		this.store = new Ext.data.JsonStore({
			fields:['account','authFailedCause','authFailedTime','nasGutter','nasIp','nasPort',
				'userIp','userMac','userGroupName','userServiceName','vlanId']
		});
		var columns = [
			{header:'宽带账号',dataIndex:'account',width:80,renderer: App.qtipValue},
			{header:'接入服务器IP',dataIndex:'nasIp',width:100,renderer: App.qtipValue},
			{header:'接入端口',dataIndex:'nasPort',width:100,renderer: App.qtipValue},
			{header:'nas槽位',dataIndex:'nasGutter',width:60},
			{header:'认证失败时间',dataIndex:'authFailedTime',width:130},
			{header:'客户端IP',dataIndex:'userIp',width:100,renderer: App.qtipValue},
			{header:'客户端Mac',dataIndex:'userMac',width:120,renderer: App.qtipValue},
			{header:'接入vlan',dataIndex:'vlanId',width:55},
			{header:'用户组名称',dataIndex:'userGroupName',width:80,renderer: App.qtipValue},
			{header:'服务名称',dataIndex:'userServiceName',width:80,renderer: App.qtipValue},
			{header:'认证失败原因',dataIndex:'authFailedCause',width:200,renderer:App.qtipValue} 
		];
		BandUserFailedLogGrid.superclass.constructor.call(this, {
			region:'center',
			ds:this.store,
			loadMask: true,
			columns:columns,
			bbar:new Ext.PagingToolbar({store: this.store ,pageSize: 20})
		});
	}
});

var BandUserFailedLogWin = Ext.extend(Ext.Window,{
	constructor:function(){
		var grid = new BandUserFailedLogGrid();
		var form = new BandUserFaildLogForm(grid);
		BandUserFailedLogWin.superclass.constructor.call(this,{
			id:'bandUserFailedLogWinId',
			closeAction : 'close',
			title:'宽带认证失败日志',
			width:800,
			height: 500,
			border:false,
			layout: 'border',
			items:[form,grid]
		});
	}
});
