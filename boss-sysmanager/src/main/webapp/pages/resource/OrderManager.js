/**
 * 订单管理
 * @type 
 */
var ORDER_LU = lsys('OrderManager');

var addTitle=ORDER_LU.addTitle;
var modifyTitle=ORDER_LU.modifyTitle;


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
			{header:ORDER_LU.columnsOrderGrid[0],dataIndex:'order_no',width:90,renderer:App.qtipValue},
			{header:ORDER_LU.columnsOrderGrid[1],dataIndex:'supplier_name',width:70,renderer:App.qtipValue},
			{header:ORDER_LU.columnsOrderGrid[2],dataIndex:'supply_date',width:85,renderer:Ext.util.Format.dateFormat},
			{header:ORDER_LU.columnsOrderGrid[3],dataIndex:'device_type_text',width:90},
			{header:ORDER_LU.columnsOrderGrid[4],dataIndex:'device_model_text',width:120,renderer:App.qtipValue},
			{header:ORDER_LU.columnsOrderGrid[5],dataIndex:'price',renderer:App.qtipValue,width:60},
			{header:ORDER_LU.columnsOrderGrid[6],dataIndex:'order_num',width:60},
			{header:ORDER_LU.columnsOrderGrid[7],dataIndex:'supply_num',width:60},
			{header:ORDER_LU.columnsOrderGrid[8],dataIndex:'is_history',width:70,renderer:function(v){
					if(v == 'T'){
						return ORDER_LU.orderStatus.HISTORY;
					}else if(v == 'F'){
						return ORDER_LU.orderStatus.NOW;
					}
				}
			},
			{id:'order_remark_id',header:ORDER_LU.columnsOrderGrid[9],dataIndex:'remark'}
			,{header:ORDER_LU.columnsOrderGrid[10],dataIndex:'device_done_code',renderer:function(v,meta,record){
					var isHistory = record.get('is_history'),result = "";
					if(currentOptrId == record.get('optr_id')){
						result = "<a href='#' title='" + ORDER_LU.modifyTitle + "' onclick=Ext.getCmp('orderGridId').editOrder()>" + lsys('common.update') + "</a>";
					}
					if(isHistory == 'F'){
						result += "&nbsp;&nbsp;<a href='#' title='" + ORDER_LU.orderStatus.HISTORY + "' onclick=Ext.getCmp('orderGridId').editHisOrder("+v+",'T')>" + ORDER_LU.btnMakeHistory + "</a>";
					}else{
						result += "&nbsp;&nbsp;<a href='#' title='" + ORDER_LU.btnResumeOrderTip +
								"' onclick=Ext.getCmp('orderGridId').editHisOrder("+v+",'F')>" + ORDER_LU.btnResumeOrder + "</a>";
					}
					return result;
				},scope:this
			}
		];
		CheckInGrid.superclass.constructor.call(this,{
			id:'orderGridId',
			title:ORDER_LU.titleOrderGrid,
			height:300,
			region:'north',
			split:true,
			ds:this.orderGridStore,
			autoExpandColumn:'order_remark_id',
			columns:columns,
			sm:sm,
			tbar:['-',lsys('common.inputKeyWork'),
				new Ext.ux.form.SearchField({  
	                store: this.orderGridStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: lsys('msgBox.supportOrderFuzzyQuery')
	            }),'-',
	            {
	            	xtype:'combo',store:new Ext.data.ArrayStore({
	            			fields:['orderTypeText','orderType'],
	            			data:[[ORDER_LU.orderStatus.ALL,'ALL'],[ORDER_LU.orderStatus.NOW,'NOW'],[ORDER_LU.orderStatus.HISTORY,'HISTORY']]
	            		}),displayField:'orderTypeText',valueField:'orderType',
	            	emptyText:ORDER_LU.tipOrderStatus,width:170,
	            	listeners:{
	            		scope:this,select:this.queryOrderByType
	            	}
	            },'-',
	            '->','-',
	            {text:lsys('common.addNewOne'),iconCls : 'icon-add',scope:this,handler:this.addOrder},'-'
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
		var text = lsys('msgBox.confirmConvert2HisOrder');
		if(isHistory == 'F'){
			text = lsys('msgBox.confirmConvert2NowOrder');
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
			{header:ORDER_LU.columnsOrderInputDetailGrid[0],dataIndex:'device_type_text'},
			{header:ORDER_LU.columnsOrderInputDetailGrid[1],dataIndex:'device_model_text'},
			{header:ORDER_LU.columnsOrderInputDetailGrid[2],dataIndex:'create_time',width:120},
			{header:ORDER_LU.columnsOrderInputDetailGrid[3],dataIndex:'count',width:70}
		];
		OrderInputDetailGrid.superclass.constructor.call(this,{
			id:'orderInputDetailGridId',
			title:ORDER_LU.titleOrderInputDetailGrid,
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
						{fieldLabel:ORDER_LU.labelOrderNo,xtype:'textfield',vtype:'alphanum',name:'deviceOrder.order_no',allowBlank:false},
						{fieldLabel:ORDER_LU.labelSupplier,xtype:'combo',hiddenName:'deviceOrder.supplier_id',
							store:new Ext.data.JsonStore({
								url:'resource/Device!queryDeviceSupplier.action',
								autoLoad:true,
								fields:['supplier_id','supplier_name']
							}),displayField:'supplier_name',emptyText:lsys('common.pleaseSelect'),
							valueField:'supplier_id',model:'local',triggerAction:'all'
						}
					]
				},
				{
					columnWidth:.5,layout:'form',
					items:[
//						{fieldLabel:'确认单号',xtype:'textfield',vtype:'alphanum',name:'deviceOrder.confirm_id',allowBlank:false},
						{fieldLabel:ORDER_LU.labelSupplyDate,xtype:'datefield',format:'Y-m-d',width:125,
							name:'deviceOrder.supply_date',allowBlank:false,emptyText:lsys('common.pleaseSelect')}
					]
				},{columnWidth:1,layout:'form',
					items:[
						{fieldLabel:lsys('common.remarkTxt'),name:'deviceOrder.remark',xtype:'textarea',anchor:'90%',height:50}
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
			xtype:'paramcombo',typeAhead:false,paramName:'ALL_DEVICE_TYPE',
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
			{header:lsys('DeviceCommon.labelDeviceType'),dataIndex:'device_type',width:90
				,editor:this.deviceTypeCombo
				,renderer:this.paramComboRender.createDelegate(this.deviceTypeCombo.getStore())
				,scope:this},
			{id:'device_model_id',header:lsys('DeviceCommon.labelDeviceModel'),dataIndex:'device_model_text',width:150,
				editor:this.deviceModelCombo
				,scope:this},
			{id:'pair_device_model_id',header:lsys('DeviceCommon.labelPrice'),dataIndex:'price',width:100,
				editor:new Ext.form.NumberField({allowNegative:false,minValue:0.01})
				,scope:this},
			{header:lsys('DeviceCommon.labelOrderNum'),dataIndex:'order_num',width:100,
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
			{header:lsys('common.doActionBtn'),dataIndex:'',width:70,renderer:function(value,metavalue,record,i){
				return "<a href='#' onclick=dinfoGrid.doDel("+i+")>" + lsys('common.remove') + "</a>";
			}}
		]);
		
		DeviceInfoGrid.superclass.constructor.call(this,{
			id:'deviceInfoGridId',
			title:lsys('DeviceCommon.titleDeviceInfo'),
			border:false,
			height:230,
			ds:this.deviceInfoGridStore,
			clicksToEdit:1,
			cm:cm,
			sm:new Ext.grid.RowSelectionModel({}),
			tbar:[{text:lsys('common.addNewOne'),scope:this,handler:this.doAdd}]
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
//		App.form.initComboData([this.deviceTypeCombo]);
		var that = this;
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
                    Alert(lsys('msgBox.orderNumCantLessThanGoodsNum'),function(){
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
		Confirm(lsys('msgBox.confirmDelete'),this,function(){
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
			title:ORDER_LU.titleAddOrder,
			closeAction:'hide',
			maximizable:false,
			width: 535,
			height: 400,
			border: false,
			items:[this.orderForm,this.deviceInfoGrid],
			buttonAlign:'right',
			buttons:[{text:lsys('common.saveBtn'),iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:lsys('common.cancel'),iconCls:'icon-close',scope:this,handler:function(){
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
				Alert(lsys('msgBox.selectDevType'),function(){
					this.deviceInfoGrid.startEditing(i,0);
				},this);
				return;
			}
			if(Ext.isEmpty( data['device_model'] )){
				Alert(lsys('msgBox.selectModel'),function(){
					this.deviceInfoGrid.startEditing(i,1);
				},this);
				return;
			}
			if(data['price'] == 0 ){
				Alert(lsys('msgBox.numberShouldBiggerThan0'),function(){
					this.deviceInfoGrid.startEditing(i,2);
				},this);
				return;
			}
			if(data['order_num'] == 0 ){
				Alert(lsys('msgBox.orderNumShouldBigThan0'),function(){
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
			
			var msg = lsys('common.addSuccess');
			var ms = Show();
			Ext.Ajax.request({
				url:'resource/Device!saveDeviceOrder.action',
				params:obj,
				scope:thiz,
				success:function(res,opt){
					ms.hide();
					ms=null;
					if(obj['device_done_code'])
						msg=lsys('common.updateSuccess');
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
			Confirm(lsys('msgBox.confirmOverideSaveDevModel'),this,function(){
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
			title:ORDER_LU._title,
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'border',
			items:[orderGrid/*,orderDetailGrid*/,orderInputDetailGrid]
		});
	}
});


