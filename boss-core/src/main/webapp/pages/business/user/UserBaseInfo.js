//选中用户面板
UserSelectGrid = Ext.extend(Ext.grid.GridPanel,{
	userStore : null,
	height : 200,
	region : 'north',
	title: '用户数 &nbsp;&nbsp;'+ App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections().length+'个',
	constructor:function(){
		this.userStore = new Ext.data.JsonStore({
			root : 'records' ,
			totalProperty: 'totalProperty',
			fields: App.userRecord,
			autoLoad: false
		}); 
		this.userStore.add(App.getApp().main.infoPanel.getUserPanel().userGrid.getSelections());
		
		var cm = [
			{header:'用户类型',dataIndex:'user_type_text',width:60},
			{header:'机顶盒',dataIndex:'stb_id',	width:100},
			{header:'智能卡',dataIndex:'card_id',width:100},
			{header:'Modem 号',dataIndex:'modem_mac',	width:100}
		]
		
		UserSelectGrid.superclass.constructor.call(this,{
			store:this.userStore,
			columns:cm
		})
	},
	remoteRefresh:function(){
		this.userStore.baseParams.custId=App.getData().custFullInfo.cust.cust_id;
		this.userStore.load();
	},
	localRefresh:function(){
	}
});
