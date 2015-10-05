
/**
 * 加入单位的表单。
 */
JoinUnit = Ext.extend( BaseForm ,{
	url: Constant.ROOT_PATH + "/core/x/Cust!jionUnit.action",
	unitStore:null,
	constructor:function(){
		//实例化store
		this.unitStore = new Ext.data.JsonStore({
			url:Constant.ROOT_PATH + "/commons/x/QueryCust!queryUnitByNameAndAddr.action",
			fields:['cust_id','cust_name','address','status','status_text']
		});
		var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
		this.unitStore.on('load',this.doLoadData,this);
		JoinUnit.superclass.constructor.call(this,{
           	bodyStyle:Constant.TAB_STYLE,
            labelWidth:75,
            border:false,
            labelAlign:'right',
            defaults:{defaultType:'textfield'},
            bodyStyle:'padding-top:10px',
            layout:'column',
            items:[{
            	xtype:'hidden',
            	name:'cust_id',
            	value:App.getCustId()
            },
            {columnWidth:1,layout:'form',border:false,items:[{
            	xtype:'displayfield',
            	fieldLabel:langUtils.bc('home.tools.CustSearch.labelCustName'),
            	value:App.getData().custFullInfo.cust.cust_name
            }]}	
            ,{columnWidth:1,layout:'form',border:false,items:[{
            	fieldLabel:lmain("cust._form.unitName"),
            	name:'unit_name',
            	allowBlank: false
            }]},
            {columnWidth:.5,layout:'form',border:false,items:[{fieldLabel:lmain("cust._form.unitAddress"),name:'address'}]},
            {columnWidth:.5,layout:'form',border:false,items:[{xtype:'button',text:langUtils.bc('common.queryBtnWithBackSpace'),iconCls:'icon-query',scope:this,handler:this.doQuery}]},
            {columnWidth:1,layout:'fit',border:false,items:[
            	new Ext.grid.GridPanel({
            		id:'unitGridId',
            		border:false,
            		store:this.unitStore,
            		sm:sm,
            		height:200,
            		title:langUtils.bc("home.main.tabs.[1]"),
            		columns:[
            			sm,
            			{header:lmain("cust._form.unitName"),dataIndex:'cust_name',width:150,renderer:App.qtipValue},
            			{header:langUtils.main("cust.base.addr"),dataIndex:'address',width:200,renderer:App.qtipValue},
            			{header:langUtils.main("cust.base.status"),dataIndex:'status_text',width:75,renderer:Ext.util.Format.statusShow}
            		]
            	})
            ]}
            ]
		})
	},
	doQuery:function(){
		var form = this.getForm();
		if(!form.isValid())return;
		var unitName = form.findField('unit_name').getValue();
		var address = form.findField('address').getValue();
		this.unitStore.load({
			params:{unitName:unitName,address:address}
		});
	},
	doLoadData:function(){
		if(this.unitStore.getCount()>0){
			var custId = this.getForm().findField('cust_id');
			this.unitStore.each(function(record){
				if(record.get('cust_id') == custId){
					this.unitStore.remove(record);
					return false;
				}
			},this);
		}else{
			Alert('无记录，请重新输入单位信息查询！');
		}
	},
	doValid:function(){
		var form = this.getForm();
		var record = Ext.getCmp('unitGridId').getSelectionModel().getSelected();
		var obj = {};
		if(!record){
			obj['isValid'] = false;
			obj['msg'] = '请选择一条单位记录';
			return obj;
		}
		return JoinUnit.superclass.doValid.call(this);
	},
	getValues: function(){
		var ps = {};
		ps['unitId'] = Ext.getCmp('unitGridId').getSelectionModel().getSelected().get('cust_id');
		return ps;
	},
	//覆盖该函数处理刷新的功能
	success: function(f , resultData){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

/**
 * 入口
 */
Ext.onReady(function(){
	var tf = new JoinUnit();
	var box = TemplateFactory.gTemplate(tf);
});
