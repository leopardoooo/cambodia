var MaterialDeviceGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	store:null,
	tariffData:null,//所有产品对应的资费信息
	constructor:function(custIds){
		
		this.store = new Ext.data.JsonStore({
			url :root + '/commons/x/QueryDevice!queryDeviceCanBuy.action',
			fields:['user_id','user_name','card_id','prod_id','prod_name','tariff_id','tariff_name',
					'prod_sn','invalid_date','next_tariff_id','next_tariff_name','next_invalid_date']
		});
		
		this.store.load();
		
		var cm = new Ext.ux.grid.LockingColumnModel({ 
			columns : [
				{header:'用户名',dataIndex:'user_name',	width:80},
				{header:'智能卡号',dataIndex:'card_id',width:150,renderer:App.qtipValue},
				{header:'产品名称',dataIndex:'prod_name',width:120,renderer:App.qtipValue},
				{header:'当前资费',dataIndex:'tariff_name',width:80,renderer:App.qtipValue},
				{header:'未生效资费',dataIndex:'next_tariff_name',width:80,renderer:App.qtipValue},
				{header:'预计到期日',dataIndex:'invalid_date',width:100,renderer:Ext.util.Format.dateFormat},
				{header:'下次预计到期日',dataIndex:'next_invalid_date',width:100,renderer:Ext.util.Format.dateFormat,
					editor : new Ext.form.DateField({format : 'Y-m-d'})
				}
			]
        });
		
		MaterialDeviceGrid.superclass.constructor.call(this,{
			title : '用户总数：'+this.store.getCount(),
			id:'specGridId',
			store:this.store,
			cm:cm,
			clicksToEdit:1,
			region : 'center',
			border:false,
			view: new Ext.ux.grid.ColumnLockBufferView({}),
			sm:new Ext.grid.RowSelectionModel({}),
			tbar : [{
				id:'tbarCobId',
				xtype:'combo',
				fieldLabel : '产品名称',
				store: new Ext.data.JsonStore({
					fields:[{name:'prod_name',mapping:0}]
				}),
				mode:'local',
				triggerAction:'all',
				emptyText:'请选择产品',
				displayField:'prod_name',
				valueField:'prod_name',
				editable:true,
				forceSelection:true,
				eableKeyEvents:true,
				selectOnFocus:true
				,listeners:{
					scope:this,
					keyup:this.doKeyupProd,
					select:this.doSelectProd
				}
			},'-',{
				xtype:'combo',
				id:'oldExpDate',
				fieldLabel : '旧预计到期日',
				store:new Ext.data.JsonStore({
					fields:[{name:'exp_date',mapping:0}]
				}),displayField:'exp_date',valueField:'exp_date',emptyText:'请选择预计到期日',
				editable:true,forceSelection:true,eableKeyEvents:true
				,listeners:{
					scope:this,
					keyup:this.doKeyupExpDate,
					select:this.doSelectExpDate
				}
			},'-',{
				xtype : 'datefield',
				width : 120,
				allowBlank : false,
				fieldLabel : '下次预计到期日',
				format : 'Y-m-d',
				id : 'newExpDate'
			},'-',{
				text : '统一修改',
				scope : this,
				handler : this.modifyAll
			}]
		});
		
		var prods = this.store.collect('prod_name',false);//所有产品集合
		var data = [];
		for(var i=0;i<prods.length;i++){
			data.push([prods[i]]);
		}
		Ext.getCmp('tbarCobId').getStore().loadData(data);
	},
	initEvents : function(){
		MaterialDeviceGrid.superclass.initEvents.call(this);
		this.on('afterrender',function(){
			this.swapViews();
		},this,{delay:10});
		
		this.on("afteredit",this.afterEdit,this);
	},
	swapViews : function(){
		if(this.view.lockedWrap){
			this.view.lockedWrap.dom.style.right = "0px";
		}
        this.view.mainWrap.dom.style.left = "0px"; 
        if(this.view.updateLockedWidth){
        	this.view.updateLockedWidth = this.view.updateLockedWidth.createSequence(function(){ 
	            this.view.mainWrap.dom.style.left = "0px"; 
	        }, this); 
        }
          
	},
	afterEdit : function(obj){
		var oldDate = Date.parseDate(obj.record.get('invalid_date'),'Y-m-d h:i:s');
		
		if(oldDate.getTime() > obj.value.getTime()){
			Alert("下次预计到期日比旧预计到期日小无法修改");
			obj.record.set('next_invalid_date','');
		}
	},
	doKeyupProd:function(combo){
		if(Ext.isEmpty(combo.getRawValue())){
			var store = this.getStore();
			store.clearFilter();
			
			Ext.getCmp('oldExpDate').getStore().removeAll();
		}
		
		this.setGridTitle();
	},
	doSelectProd:function(combo,record){
		var store = this.getStore();
		store.filter("prod_name",combo.getValue());
		
		var arr = store.collect('invalid_date',false);//所有到期日集合
		var data = [];
		for(var i=0;i<arr.length;i++){
			data.push([arr[i].substring(0,10)]);
		}
		Ext.getCmp('oldExpDate').getStore().loadData(data);
		
		this.setGridTitle();
	},
	doKeyupExpDate : function(combo){
		if(Ext.isEmpty(combo.getRawValue())){
			var store = this.getStore();
			store.clearFilter();
			
			store.filter('prod_name',Ext.getCmp('tbarCobId').getValue());
		}
		
		this.setGridTitle();
	},
	doSelectExpDate : function(combo){
		if(combo.getValue()){
			var store = this.getStore();
			store.filter("invalid_date",combo.getValue());
			
			
			var date = Date.parseDate(combo.getValue(),'Y-m-d');
			Ext.getCmp('newExpDate').setMinValue(date);
		}
		
		this.setGridTitle();
	},
	setGridTitle : function(){
		this.setTitle('用户总数：'+this.store.getCount());
	},
	modifyAll : function(){
		var date = Ext.getCmp('newExpDate').getValue();
		var flag = false;
		this.getStore().each(function(record){
			var oldDate = Date.parseDate(record.get('invalid_date'),'Y-m-d h:i:s');
			if(oldDate.getTime() < date.getTime()){
				record.set('next_invalid_date',date);
			}else{
				flag = true;
			}
		},this)
		
		if(flag){
			Alert("下次预计到期日比旧预计到期日小的无法修改");
		}
	},
	getValues:function(){
		var arr = [],data;
		this.store.each(function(record){
			data = record.data;
			if(data.next_invalid_date){
				var obj = {};
				obj["user_id"] = data['user_id'];
				obj["prod_sn"] = data['prod_sn'];
				obj['next_invalid_date'] = data['next_invalid_date'];
				arr.push(obj);
			}
		},this);
		return arr;
	}
});

BuyMaterialForm = Ext.extend( BaseForm , {
	url : Constant.ROOT_PATH + "/core/x/Cust!buyMaterial.action",
	success : function(){
		App.getApp().refreshPayInfo(parent);
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	},
	materialDeviceGrid : null,
	data : [],//提交数据
	constructor: function(){
		this.materialDeviceGrid = new MaterialDeviceGrid();
		BuyMaterialForm.superclass.constructor.call(this,{
			autoScroll:true,
			layout:'border',
            border:false,
            bodyStyle: Constant.TAB_STYLE,
            items:[{
				region:'center',
				layout : 'fit',
				items:[this.materialDeviceGrid]
			}]
		});
	},
	getValues: function(){
		var all = {'payFeesData':Ext.encode(this.materialDeviceGrid.getValues())};
		return all;
	},
	doValid: function(){
		var data = this.panel.getValues();
		
		if(data.length==0){
			var obj = {};
			obj['msg'] = '器材不能为空';
			obj['isValid'] = false;
			return obj;
		}
		return BuyMaterialForm.superclass.doValid.call(this);
	}
});

Ext.onReady(function(){
	var buy = new BuyMaterialForm();
	var box = TemplateFactory.gTemplate(buy);
});