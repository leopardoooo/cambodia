/**
 * 用户数扎账
 * @class
 * @extends Ext.Window
 */
 
var UserCountCheckGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor: function(){
		this.store = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH+'/commons/x/BusiCommon!queryRecordByDeptId.action',
			fields:['optr_id','optr_name','addr_id','addr_name','ods_date','done_date']
		});
		this.store.load();
		var sm = new Ext.grid.CheckboxSelectionModel();
		var columns = [
			sm,
			{header:'区域',dataIndex:'addr_name',width:120,renderer:App.qtipValue},
			{header:'操作员',dataIndex:'optr_name',width:75,renderer:App.qtipValue},
			{header:'扎账日期',dataIndex:'ods_date',width:80,renderer:App.qtipValue},
			{header:'操作时间',dataIndex:'done_date',width:120,renderer:App.qtipValue}
		];
		UserCountCheckGrid.superclass.constructor.call(this,{
			region:'center',
//			border:false,
			store:this.store,
			columns:columns,
			sm:sm
		});
	},
	getSelectValues: function(){
		var records = this.getSelectionModel().getSelections();
		var addrIds = [];
		if(records.length>0){
			Ext.each(records,function(r){
				addrIds.push(r.get('addr_id'));
			});
		}
		return addrIds;
	}
});

var UserCountCheckWin = Ext.extend(Ext.Window,{
	grid:null,
	constructor:function(){
		this.grid = new UserCountCheckGrid();
		UserCountCheckWin.superclass.constructor.call(this,{
			title:'用户数轧账',
			width:500,
			height:400,
			closeAction:'close',
			border:false,
			layout:'border',
			defaults:{
				labelWidth:100,
				labelAlign:'right'
			},
			items:[
				this.grid,
				{region:'south',layout:'form',height:50,bodyStyle:'padding-top:10px;',items:[
					{xtype:'datefield',name:'acctDate',allowBlank:false,minValue : App.data.optr.old_county_id=='4501' ? null : nowDate().format('Y-m'),
						id : 'UserCountCheckDate',fieldLabel:'轧账日期',format:'Y-m',editable:false,value:nowDate()}
				]}
			],
			buttonAlign:'center',
			buttons:[
				{text:'保存',scope:this,handler:this.doSave},
				{text:'关闭',scope:this,handler:function(){this.close();}}
			]
		});
	},
	doSave:function(){
		var addrIds = this.grid.getSelectValues();
		if(addrIds.length == 0){
			Alert('请选择区域');
			return;
		}
		var acctDate = Ext.getCmp('UserCountCheckDate').getRawValue();
		if(!Ext.isEmpty(acctDate)){
			addrIds = addrIds.toString();
			mb = Show();//显示正在提交
			Ext.Ajax.request({
				url:Constant.ROOT_PATH+'/commons/x/BusiCommon!checkUserCount.action',
				params:{
					acctDate:acctDate,
					addrIds:addrIds
				},
				scope:this,
				timeout:99999999999999,//12位 报异常
				success:function(res,opt){
					
					mb.hide();//隐藏提示框
					mb = null;
					
					Alert('保存成功！',function(){
						App.acctDate();
						this.close();
					},this);
				}
			});
		}
	}
})