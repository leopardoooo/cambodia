/**
 * 用户状态修改为“休眠”或“关模隔离”
 * @class
 * @extends Ext.Window
 */

var UserStatusForm = Ext.extend(Ext.form.FormPanel,{
	parent:null,
	constructor:function(p){
		this.parent = p;
		UserStatusForm.superclass.constructor.call(this,{
			id:'userStatusFormId',
			region:'center',
			border:false,
			bodyStyle:'padding-top:10px',
			labelWidth:100,
			fileUpload: true,
			items:[
				{xtype:'combo',fieldLabel:'新用户状态',hiddenName:'userStatus',
					store:new Ext.data.JsonStore({
						fields:['text','value'],
						data:[{'text':'休眠','value':'DORMANCY'},{'text':'关模隔离','value':'ATVCLOSE'}]
					}),displayField:'text',valueField:'value',value:'DORMANCY',allowBlank:false
				},
				{anchor:'100%',layout:'column',border:false,items:[
						{columnWidth:.5,layout:'form',border:false,items:[
							{xtype:'textfield',fieldLabel:'客户编号',name:'custNo',
								listeners:{
									scope:this,
									specialkey:function(comp,e){
										if(e.getKey() == e.ENTER){
											this.doQuery();
										}
									}
								}
							}
						]},
						{columnWidth:.3,layout:'form',border:false,items:[
							{xtype:'button',text:'查询',
								listeners:{
									scope:this,
									click:this.doQuery
								}
							}]}
						]
				},
				{id:'userStatusFileId',xtype:'fileuploadfield',fieldLabel:'用户ID文件',name:'file',anchor:'90%',
					buttonText:'浏览...'
				}
			]
		});
	},
	doQuery:function(){
		var form = this.getForm();
		var custNo = form.findField('custNo').getValue();
		if(custNo){
			var userStatus = form.findField('userStatus').getValue();
			
			this.parent.grid.getStore().load({
				params:{
					custNo:custNo,
					userStatus:userStatus
				}
			});
		}
	}
});

var UserInfoGrid = Ext.extend(Ext.grid.GridPanel,{
	parent:null,
	constructor:function(p){
		this.parent = p;
		this.userStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/commons/x/QueryUser!queryUserByCustNoAndStatus.action",
			fields: ['user_id','user_type_text','user_name','status','status_text','stb_id','card_id','modem_mac']
		});
		var sm = new Ext.grid.CheckboxSelectionModel();
		var columns = [
			sm,
			{header:'用户类型',dataIndex:'user_type_text',width:80},
			{header:'用户名',dataIndex:'user_name',	width:80},
			{header:'状态',dataIndex:'status_text',	width:60,renderer:Ext.util.Format.statusShow},
			{header:'机顶盒',dataIndex:'stb_id',	width:130,renderer:App.qtipValue},
			{header:'智能卡',dataIndex:'card_id',width:110,renderer:App.qtipValue},
			{header:'Modem号',dataIndex:'modem_mac',width:90,renderer:App.qtipValue}
		];
		UserInfoGrid.superclass.constructor.call(this,{
			title: '用户信息',
			region:'south',
			height:200,
			store:this.userStore,
			sm:sm,
			columns:columns
		});
	}
});

var EditUserStatusWin = Ext.extend(Ext.Window,{
	formPanel:null,
	grid:null,
	constructor:function(){
		this.formPanel = new UserStatusForm(this);
		this.grid = new UserInfoGrid(this);
		EditUserStatusWin.superclass.constructor.call(this,{
			id:'userStatusWinId',
			title:'批量修改用户状态',
			border:false,
			width:500,
			height:400,
			layout:'border',
			items:[this.formPanel,this.grid],
			closeAction:'close',
			buttonAlign:'center',
			buttons:[
				{text:'保存',scope:this,handler:this.doSave},
				{text:'关闭',scope:this,handler:this.closeWin}
			]
		});
	},
	doSave:function(){
		var records = this.grid.getSelectionModel().getSelections();
		var userIds = [];
		
		//是否包含报停用户
		var flag = false;
		Ext.each(records,function(record){
//			if(record.get('status') == 'REQSTOP'){
//				flag = true;
//				return false;
//			}else{
				userIds.push(record.get('user_id'));
//			}
		});
//		if(flag){
//			Alert('报停用户不能操作');
//			return;
//		}
		var form = this.formPanel.getForm();
		var userStatus = form.findField('userStatus').getValue();
		var fileText = Ext.getCmp('userStatusFileId').getValue();
		if( userIds.length > 0 || fileText ){
			var ps={},all={};
			ps['busiCode'] = '1603';
			ps['optr'] = App.getData().optr;
			all[CoreConstant.JSON_PARAMS] = Ext.encode(ps);
			all['userIds'] = userIds.join(',');
			var msg = Show();
			form.submit({
				url:root+'/core/x/User!updateUserStatus.action',
				scope:this,
				params:all,
				success:function(form,action){
					msg.hide();msg = null;
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
						}else{
							Alert('修改成功!',function(){
								this.closeWin();
							},this);
						}
					}
				}
			});
		}else{
			Alert('请选择用户ID文件或输入客户编号查询用户');
		}
	},
	closeWin:function(){
		this.close();
	}
});