/**
 * 排斥资源
 */

RejectResForm = Ext.extend(BaseForm,{
	rejectResStore :null,//已排斥的资源
	unRejectResStore:null,//未排斥的资源
	userId:null,
	custId:null,
	constructor:function(){
		this.rejectResStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH+'/commons/x/QueryUser!queryRejectRes.action',
			fields:['res_id','res_name']
		});
		this.userId = App.getApp().main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected().get('user_id');
		this.custId = App.getApp().getCustId();
		
		this.rejectResStore.load({params:{userId:this.userId,custId:this.custId}});
		this.unRejectResStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH+'/commons/x/QueryUser!queryUnRejectRes.action',
			fields:['res_id','res_name']
		});
		this.unRejectResStore.load({params:{userId:this.userId,custId:this.custId}});
		
		this.rejectSm = new Ext.grid.CheckboxSelectionModel();
		this.unRejectSm = new Ext.grid.CheckboxSelectionModel();
		var resNameCol = {header:'资源名称',dataIndex:'res_name',width:150,renderer:App.qtipValue};
		var rejectColumns = [this.rejectSm,resNameCol];
		var unRejectColumns = [this.unRejectSm,resNameCol];
		
		this.rejectResGrid = new Ext.grid.GridPanel({
			title:'排斥资源',
			border:false,
			region:'center',
			store:this.rejectResStore,
			sm:this.rejectSm,
			columns:rejectColumns
		});
		this.unRejectResGrid = new Ext.grid.GridPanel({
			title:'未排斥资源',
			border:false,
			region:'center',
			store:this.unRejectResStore,
			sm:this.unRejectSm,
			columns:unRejectColumns,
			tbar:['过滤:',{xtype:'textfield',enableKeyEvents:true,
				listeners:{
					scope:this,
					keyup:function(txt,e){
						if(e.getKey() == Ext.EventObject.ENTER){
							var value = txt.getValue();
								this.unRejectResStore.filterBy(function(record){
									if(Ext.isEmpty(value))
										return true;
									else
										return record.get('res_name').indexOf(value)>=0;
								},this);
						}
					}
				}
			}]
		});
		
		RejectResForm.superclass.constructor.call(this,{
			border:false,
			layout:'border',
			items:[
				this.unRejectResGrid,
				{
					layout:'border',
					region:'east',
					border:false,
					width:'55.5%',
					items:[
						this.rejectResGrid,
						{
					border : false,
					bodyStyle : 'background-color: #DFE8F6;',
					region : 'west',
					width : 60,
					layout : {
						type : 'vbox',
						pack : 'center',
						align : 'center'
					},
					items : [{
								xtype : 'button',
								iconCls : 'move-to-right',
								width : 40,
								scale : 'large',
								tooltip : '将左边已勾选的功能分配给该角色!',
								iconAlign : 'center',
								scope : this,
								handler : function() {
									this.doResIn(this.unRejectSm.getSelections());
								}
							}, {
								height : 30,
								baseCls : 'x-plain'
							}, {
								xtype : 'button',
								iconCls : 'move-to-left',
								tooltip : '将右边勾选中的功能取消!',
								width : 40,
								scale : 'large',
								iconAlign : 'center',
								scope : this,
								handler : function() {
									this.doResOut(this.rejectSm.getSelections());
								}
							}]
				}
					]
				}
			]
		});
	},
	doResIn : function(records) {
		if (records.length == 0) {
			Alert("请在左边的列表中选择系统功能!");
			return;
		}
		this.unRejectResStore.remove(records);
		this.rejectResStore.add(records);
	},
	doResOut : function(records) {
		if (records.length == 0) {
			Alert("请在右边的列表中选择系统功能!");
			return;
		}
		this.rejectResStore.remove(records);
		this.unRejectResStore.add(records);
	},
	getValues:function(){
		var arr = [];
		var obj = {};
		if(this.rejectResStore.getCount()>0){
			this.rejectResStore.each(function(record){
				arr.push(record.get('res_id'),',');
			},this);
			var values = arr.join('');
			obj['resIds'] = values.substring(0,values.length-1);
		}
		
		obj['userId'] = this.userId;
		obj['custId'] = this.custId;
		//设置提交的参数包含客户的编号
		obj["parameter.custFullInfo.cust.cust_id"] = App.getApp().getData().custFullInfo.cust.cust_id;
		return obj;
	},
	url : Constant.ROOT_PATH+"/core/x/User!saveRejectRes.action",
	success:function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var adf = new RejectResForm();
	var box = TemplateFactory.gTemplate(adf);
});

