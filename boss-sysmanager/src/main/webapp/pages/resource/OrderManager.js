/**
 * 订单管理
 * @type 
 */

var addTitle="添加订单";
var modifyTitle="修改订单";

/**
 * 订单信息
 * @class
 * @extends Ext.grid.GridPanel
 */
var OrderGrid = Ext.extend(Ext.grid.GridPanel,{
	orderGridStore: null,
	orderWin: null,
	constructor:function(){
		this.orderWin = new OrderWin();
		this.orderGridStore = new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceOrder.action',
			totalProperty: 'totalProperty',
			root:'records',
			fields:[{name:'device_done_code'},{name:'order_no'},{name:'depot_id'},{name:'confirm_id'},{name:'supplier_id'}
			,{name:'supply_date'},{name:'create_time'},{name:'optr_id'},{name:'remark'},{name:'supplier_name'},{name:'device_type'}
			,{name:'device_model'},{name:'price',convert:function(v){return Ext.util.Format.formatFee(v)}},{name:'optr_id'}
			,{name:'order_num'},{name:'supply_num'},{name:'device_type_text'},{name:'device_model_text'},{name:'is_history'}
			]
		});
		this.orderGridStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
		var currentOptrId = App.data.optr['optr_id'];
		var columns = [
			{header:'编号',dataIndex:'order_no',width:90,renderer:App.qtipValue},
			{header:'供应商',dataIndex:'supplier_name',width:70,renderer:App.qtipValue},
			{header:'供货日期',dataIndex:'supply_date',width:85,renderer:Ext.util.Format.dateFormat},
			{header:'设备类型',dataIndex:'device_type_text',width:60},
			{header:'型号',dataIndex:'device_model_text',width:120,renderer:App.qtipValue},
			{header:'单价',dataIndex:'price',renderer:App.qtipValue,width:60},
			{header:'订购数量',dataIndex:'order_num',width:60},
			{header:'到货数量',dataIndex:'supply_num',width:60},
			{header:'订单类型',dataIndex:'is_history',width:70,renderer:function(v){
					if(v == 'T'){
						return '历史订单';
					}else if(v == 'F'){
						return '执行中订单';
					}
				}
			},
			{id:'order_remark_id',header:'备注',dataIndex:'remark'}
			,{header:'操作',dataIndex:'device_done_code',renderer:function(v,meta,record){
					var isHistory = record.get('is_history'),result = "";
					if(currentOptrId == record.get('optr_id')){
						result = "<a href='#' title='修改订单' onclick=Ext.getCmp('orderGridId').editOrder()>修改</a>";
					}
					if(isHistory == 'F'){
						result += "&nbsp;&nbsp;<a href='#' title='历史订单' onclick=Ext.getCmp('orderGridId').editHisOrder("+v+",'T')>历史</a>";
					}else{
						result += "&nbsp;&nbsp;<a href='#' title='恢复订单' onclick=Ext.getCmp('orderGridId').editHisOrder("+v+",'F')>恢复</a>";
					}
					return result;
				},scope:this
			}
		];
		CheckInGrid.superclass.constructor.call(this,{
			id:'orderGridId',
			title:'订单信息',
			height:300,
			region:'north',
			split:true,
			ds:this.orderGridStore,
			autoExpandColumn:'order_remark_id',
			columns:columns,
			sm:sm,
			tbar:['-','输入关键字&nbsp;',
				new Ext.ux.form.SearchField({  
	                store: this.orderGridStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: '支持订单编号模糊查询'
	            }),'-',
	            {
	            	xtype:'combo',store:new Ext.data.ArrayStore({
	            			fields:['orderTypeText','orderType'],
	            			data:[['所有订单','ALL'],['执行订单','NOW'],['历史订单','HISTORY']]
	            		}),displayField:'orderTypeText',valueField:'orderType',
	            	emptyText:'执行中的订单或历史订单',width:170,
	            	listeners:{
	            		scope:this,select:this.queryOrderByType
	            	}
	            },'-',
	            '->','-',
	            {text:'添加',iconCls : 'icon-add',scope:this,handler:this.addOrder},'-'
			],
			bbar : new Ext.PagingToolbar({
										store : this.orderGridStore,
										pageSize : Constant.DEFAULT_PAGE_SIZE
									}),
			listeners:{
				scope:this,
				rowclick:this.doClick	
			}
		});
	},
	doClick:function(){
		var deviceDoneCode = this.getSelectionModel().getSelected().get('device_done_code');
		Ext.getCmp('orderInputDetailGridId').getStore().load({params:{deviceDoneCode:deviceDoneCode}});
	},
	addOrder:function(){
		var orderWin = Ext.getCmp('orderWinId');
		if(!orderWin){
			orderWin = new OrderWin();
		}
		if(orderWin.title !== addTitle)
			orderWin.setTitle(addTitle);
		orderWin.show();
	},
	editOrder:function(){
		var vRecord = this.getSelectionModel().getSelected();
		var obj = {},keys = vRecord.fields.keys;
		for(var i=0;i<keys.length;i++){
			obj['deviceOrder.'+keys[i]] = vRecord.data[keys[i]];
		}
		obj['deviceOrder.supply_date'] = Date.parseDate(obj['deviceOrder.supply_date'],'Y-m-d H:i:s');
		
		var orderWin = Ext.getCmp('orderWinId');
		if(!orderWin){
			orderWin = new OrderWin();
		}
		if(orderWin.title !== modifyTitle)
			orderWin.setTitle(modifyTitle);
		orderWin.show();
		
		orderWin.orderForm.getForm().setValues(obj);
		
		var deviceStore = orderWin.deviceInfoGrid.getStore();
		var orderStore = Ext.getCmp('orderGridId').getStore().queryBy(function(record){
			return record.get('order_no') == vRecord.get('order_no');
		});
		var data = [];
		orderStore.each(function(record){
			data.push(record.data);
		})
		deviceStore.loadData(data);
		
	},
	queryOrderByType: function(combo){
		var value = combo.getValue(),isHistory='';
		if(value == 'NOW'){
			isHistory = 'F';
		}else if(value == 'HISTORY'){
			isHistory = 'T';
		}else{
			isHistory = 'All';
		}
		var store = this.getStore();
		store.baseParams['isHistory'] = isHistory;
		store.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
	},
	editHisOrder: function(v,isHistory){
		var text = '确认转换为历史订单吗？';
		if(isHistory == 'F'){
			text = '确认转换为执行中的订单吗？';
		}
		Confirm(text,this,function(){
			var record = this.getSelectionModel().getSelected();
			Ext.Ajax.request({
				url:root+'/resource/Device!editDeviceOrder.action',
				params:{deviceDoneCode:v,isHistory:isHistory},
				scope:this,
				success:function(res){
					var data = Ext.decode(res.responseText);
					if(data === true){
						Ext.getCmp('orderGridId').getStore().reload();
						Ext.getCmp('orderInputDetailGridId').getStore().reload();
					}
				}
			});
		});
	}
});

/**
 * 到货记录
 * @class
 * @extends Ext.grid.GridPanel
 */
var OrderInputDetailGrid = Ext.extend(Ext.grid.GridPanel,{
	orderInputDetailGridStore :null,
	constructor:function(){
		this.orderInputDetailGridStore = new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceOrderInputDetail.action',
			fields:['device_done_code','device_type','device_model','device_model_text',
				'count','device_type_text','create_time']
		});
		
		var columns = [
			{header:'设备类型',dataIndex:'device_type_text'},
			{header:'型号',dataIndex:'device_model_text'},
			{header:'到货日期',dataIndex:'create_time',width:120},
			{header:'数量',dataIndex:'count',width:70}
		];
		OrderInputDetailGrid.superclass.constructor.call(this,{
			id:'orderInputDetailGridId',
			title:'到货明细',
			region:'center',
			ds:this.orderInputDetailGridStore,
			columns:columns
		});
	}
});

/**
 * 订单form
 * @class
 * @extends Ext.form.FormPanel
 */
var OrderForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		OrderForm.superclass.constructor.call(this,{
			id:'orderFormId',
			border:false,
			labelWidth: 80,
			layout : 'column',
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[{
					columnWidth:.5,layout:'form',
					items:[
						{name:'deviceOrder.device_done_code',xtype:'hidden'},
						{fieldLabel:'订单编号',xtype:'textfield',vtype:'alphanum',name:'deviceOrder.order_no',allowBlank:false},
						{fieldLabel:'供应商',xtype:'combo',hiddenName:'deviceOrder.supplier_id',
							store:new Ext.data.JsonStore({
								url:'resource/Device!queryDeviceSupplier.action',
								autoLoad:true,
								fields:['supplier_id','supplier_name']
							}),displayField:'supplier_name',emptyText:'请选择...',
							valueField:'supplier_id',model:'local',triggerAction:'all'
						}
					]
				},
				{
					columnWidth:.5,layout:'form',
					items:[
//						{fieldLabel:'确认单号',xtype:'textfield',vtype:'alphanum',name:'deviceOrder.confirm_id',allowBlank:false},
						{fieldLabel:'供货日期',xtype:'datefield',format:'Y-m-d',width:125,
							name:'deviceOrder.supply_date',allowBlank:false,emptyText:'请选择...'}
					]
				},{columnWidth:1,layout:'form',
					items:[
						{fieldLabel:'备注',name:'deviceOrder.remark',xtype:'textarea',anchor:'90%',height:50}
					]}
			]
		});
	}
});

/**
 * 设备信息
 * @class
 * @extends Ext.grid.EditorGridPanel
 */
var DeviceInfoGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	deviceInfoGridStore:null,
	
	deviceTypeCombo :null,
	deviceModelCombo :null,
	constructor:function(){
		dinfoGrid = this;
		this.deviceInfoGridStore = new Ext.data.JsonStore({
			pruneModifiedRecords:true,//每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
			fields:['device_done_code','device_type','device_model','price',
				'order_num','supply_num','device_type_text','device_model_text']
		});
		
		this.deviceModelCombo = new Ext.ux.ParamCombo({
			displayField:'item_name',valueField:'item_name',
			editable:true,forceSelection:true,
			listeners:{
				scope:this,
				select:function(combo,record,index){
					var r = this.getSelectionModel().getSelected();
					r.set('device_model',record.get('item_value'));
				}
			}});
		this.deviceTypeCombo = new Ext.ux.ParamCombo({
			xtype:'paramcombo',typeAhead:false,paramName:'DEVICE_TYPE',
			listeners:{
				scope:this,
				select:function(combo){
					//当设备类型改变时，清空"型号"列
					var record = this.getSelectionModel().getSelected();
					record.set('device_model','');
					record.set('device_model_text','');
				}
			}
		});
		
		var cm = new Ext.grid.ColumnModel([
			{header:'设备类型',dataIndex:'device_type',width:90
				,editor:this.deviceTypeCombo
				,renderer:this.paramComboRender.createDelegate(this.deviceTypeCombo.getStore())
				,scope:this},
			{id:'device_model_id',header:'型号',dataIndex:'device_model_text',width:150,
				editor:this.deviceModelCombo
				,scope:this},
			{id:'pair_device_model_id',header:'单价',dataIndex:'price',width:100,
				editor:new Ext.form.NumberField({allowNegative:false,minValue:0.01})
				,scope:this},
			{header:'订购数量',dataIndex:'order_num',width:100,
				editor:new Ext.form.NumberField({
					allowNegative:false,minValue:1,allowDecimals:false,minValue:1,
					listeners:{
						scope:this,
						specialkey:function(field,e){
							if(e.getKey() === Ext.EventObject.ENTER){
								this.doAdd();
							}
						}
					}
				})
			},
			{header:'操作',dataIndex:'',width:70,renderer:function(value,metavalue,record,i){
				return "<a href='#' onclick=dinfoGrid.doDel("+i+")>删除</a>";
			}}
		]);
		
		DeviceInfoGrid.superclass.constructor.call(this,{
			id:'deviceInfoGridId',
			title:'设备信息',
			border:false,
			height:230,
			ds:this.deviceInfoGridStore,
			clicksToEdit:1,
			cm:cm,
			sm:new Ext.grid.RowSelectionModel({}),
			tbar:[{text:'添加',scope:this,handler:this.doAdd}]
		});
	},
	paramComboRender:function(value,combo){
		var index = this.find('item_value',value);
		var record = this.getAt(index);
		if(!Ext.isEmpty(record)){
			return record.get('item_name');
		}
		return '';
	},
	initComponent:function(){
		DeviceInfoGrid.superclass.initComponent.call(this);
		App.form.initComboData([this.deviceTypeCombo]);
	},
	initEvents:function(){
		DeviceInfoGrid.superclass.initEvents.call(this);
		this.on('beforeedit',function(obj){
			var record = obj.record;
			var fieldName = obj.field;//编辑的column对应的dataIndex
			
			if(fieldName == 'device_model_text'){
				var deviceType = record.get('device_type');
				if(Ext.isEmpty(deviceType))return false;
//				this.deviceModelCombo.paramName = deviceType.concat('_MODEL');
				var paramName = deviceType+'_MODEL';
				Ext.Ajax.request({
					url:root + '/ps.action',
					params:{comboParamNames: [paramName]},
					scope:this,
					success:function(res,opt){
						var data = Ext.decode(res.responseText)[0];
						var arr = [];
						Ext.each(data,function(d){
							var obj = {};
							obj['item_name'] = d['item_name']+'('+d['item_value']+')';
							obj['item_value'] = d['item_value'];
							arr.push(obj);
						});
						this.deviceModelCombo.getStore().loadData(arr);
					}
				});
			}
		},this);
		this.on('afteredit',function(obj){
			var record = obj.record;
			var fieldName = obj.field;//编辑的column对应的dataIndex
			if(fieldName == 'order_num'){
				if (obj.record.get('supply_num') > obj.value) {
                    Alert('订购数量不能小于到货数量!',function(){
                    	obj.record.set('order_num','');
						this.startEditing(obj.row, obj.column);
					},this);
                }
			}
		});
	},
	doAdd:function(){
		var count = this.getStore().getCount();
		var recordType = this.getStore().recordType;
		
		if(count >0){
			var record = this.getStore().getAt(count-1);
			var obj={};
			Ext.apply(obj,record.data);
			this.getStore().add(new recordType(obj));
		}else{
			var record = new recordType({
				device_done_code:0,device_type_text:'',
				device_type:'',device_model:'',device_model_text:'',
				price:0.00,order_num:0,supply_num:0
			});
			this.stopEditing();
			this.getStore().add(record);
		}
		this.startEditing(count,0);
		this.getSelectionModel().selectRow(count);
	},
	doDel:function(index){
		Confirm('确定删除吗?',this,function(){
			this.getStore().remove(this.getSelectionModel().getSelected());
		});
	}
});

/**
 * 订单弹出框
 * @class
 * @extends Ext.Window
 */
var OrderWin = Ext.extend(Ext.Window,{
	orderForm:null,
	deviceInfoGrid:null,
	constructor:function(){
		this.orderForm = new OrderForm();
		this.deviceInfoGrid = new DeviceInfoGrid();
		OrderWin.superclass.constructor.call(this,{
			id : 'orderWinId',
			title:'添加订单',
			closeAction:'hide',
			maximizable:false,
			width: 535,
			height: 400,
			border: false,
			items:[this.orderForm,this.deviceInfoGrid],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.orderForm.getForm().reset();
					this.deviceInfoGrid.getStore().removeAll();
				}
			}
		});
	},
	show:function(){
		OrderWin.superclass.show.call(this);
		this.orderForm.getForm().findField('deviceOrder.order_no').focus(true,100);
	},
	doSave:function(){
		var form = this.orderForm.getForm();
		if(!form.isValid())return;
		this.deviceInfoGrid.stopEditing();
		var formValues = form.getValues();
		var store = this.deviceInfoGrid.getStore();
		var arr = [];
		//添加
		for(var i=0;i<store.getCount();i++){
			var data = store.getAt(i).data;
			if(Ext.isEmpty( data['device_type'] ) ){
				Alert('请选择设备类型!',function(){
					this.deviceInfoGrid.startEditing(i,0);
				},this);
				return;
			}
			if(Ext.isEmpty( data['device_model'] )){
				Alert('请选择型号!',function(){
					this.deviceInfoGrid.startEditing(i,1);
				},this);
				return;
			}
			if(data['price'] == 0 ){
				Alert('单价请输入大于零的数字!',function(){
					this.deviceInfoGrid.startEditing(i,2);
				},this);
				return;
			}
			if(data['order_num'] == 0 ){
				Alert('订购数量请输入大于零的数字!',function(){
					this.deviceInfoGrid.startEditing(i,3);
				},this);
				return;
			}
			var obj={};
			Ext.apply(obj,data);
			obj['price'] = parseFloat(obj['price'])*100;
			arr.push(obj);
		}
		var thiz = this;
		var func = function(){
			var obj={};
			Ext.apply(obj,formValues);
			obj['deviceOrderDetailList'] = Ext.encode(arr);
			
			var msg = "添加成功!";
			var ms = Show();
			Ext.Ajax.request({
				url:'resource/Device!saveDeviceOrder.action',
				params:obj,
				scope:thiz,
				success:function(res,opt){
					ms.hide();
					ms=null;
					if(obj['device_done_code'])
						msg="修改成功!";
					Alert(msg,function(){
						this.hide();
						Ext.getCmp('orderGridId').getStore().reload();
						Ext.getCmp('orderInputDetailGridId').getStore().reload();
					},thiz);
				}
			});
		}
		
		var deviceModelArr = [];
		Ext.each(arr,function(d){
			deviceModelArr.push(d['device_model']);
		});
		var flag = false;
		for(var i=0,len=deviceModelArr.length-1;i<len;i++){
			if(deviceModelArr[i] != deviceModelArr[i+1]){
				flag = true;
				break;
			}
		}
		if(flag === true){
			Confirm('存在不同的型号设备，是否保存',this,function(){
				func();
			});
		}else{
			func();
		}
		
	}
});


OrderManager = Ext.extend(Ext.Panel,{
	constructor:function(){
		var orderGrid = new OrderGrid();
//		var orderDetailGrid = new OrderDetailGrid();
		var orderInputDetailGrid = new OrderInputDetailGrid();
		CheckIn.superclass.constructor.call(this,{
			id:'OrderManager',
			title:'订单管理',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[orderGrid/*,orderDetailGrid*/,orderInputDetailGrid]
		});
	}
});


