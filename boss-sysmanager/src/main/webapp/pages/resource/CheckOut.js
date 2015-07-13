//调拨
var transferNo = {
	fieldLabel:'调拨单号',xtype:'textfield',vtype:'alphanum',name:'deviceTransfer.transfer_no',allowBlank:false
};

//仓库
var depot = {fieldLabel:'仓库',hiddenName:'deviceTransfer.depot_order',xtype:'combo',allowBlank:false,
		store:new Ext.data.JsonStore({
			url:'resource/Device!queryTransferDepot.action',
			fields:['depot_id','depot_name']
		}),displayField:'depot_name',valueField:'depot_id',triggerAction:'all',mode:'local'
};

//备注
var outRemark = {fieldLabel:'备注',name:'deviceTransfer.remark',xtype:'textarea',anchor:'90%',height:45};
//调拨状态
var statusCmp = {fieldLabel:'设备状况',xtype:'paramcombo',paramName:'TRANSFER_STATUS',allowBlank:false,
		hiddenName:'deviceTransfer.transfer_status'
};

/**
 * 调拨信息
 * @class
 * @extends Ext.grid.GridPanel
 */
var TransferGrid = Ext.extend(Ext.grid.GridPanel,{
	transferGridStore: null,
	deviceModelStore:null,
	constructor:function(){
		this.transferGridStore = new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceTransfer.action',
			totalProperty:'totalProperty',
			root:'records',
			fields:['device_done_code','transfer_no','tran_type','tran_type_text','optr_id','transfer_status_text','transfer_status',
				'depot_text','status','status_text','confirm_date','create_time','remark','is_history',
				'device_type','device_model','count','device_model_text','device_type_text']
		});
		this.deviceModelStore = new Ext.data.JsonStore({
			url : root + '/resource/Device!queryAllModel.action',
			fields : ['model_type_name','device_model']
		});
		this.deviceModelStore.load();
        this.deviceModelStore.on("load", function (s) {
            s.insert(0, new Ext.data.Record({
                model_type_name: '全选',
                device_model: ''
            }));
        });
		this.transferGridStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var sm = new Ext.grid.RowSelectionModel({singleselect:true});
		var currentOptrId = App.data.optr['optr_id'];
		var columns = [
			{header:'调拨单号',dataIndex:'transfer_no',width:75,renderer:App.qtipValue},
			{header:'调拨类别',dataIndex:'tran_type_text',width:55},
			{header:'仓库',dataIndex:'depot_text',width:100,renderer:App.qtipValue},
			{header:'状态',dataIndex:'status_text',width:50,renderer:Ext.util.Format.statusShow},
			{header:'创建日期',dataIndex:'create_time',width:105,renderer:Ext.util.Format.timeFormat},
			{header:'确认日期',dataIndex:'confirm_date',width:105,renderer:Ext.util.Format.timeFormat},
			{header:'设备类型',dataIndex:'device_type_text',width:55},
			{header:'调拨状态',dataIndex:'transfer_status_text',width:55},
			{header:'型号',dataIndex:'device_model_text',width:120,renderer:App.qtipValue},
			{header:'数量',dataIndex:'count',width:40},
			{id:'checkout_remark_id',header:'备注',dataIndex:'remark'},
			{header:'操作',dataIndex:'device_done_code',width:200,renderer:function(value,meta,record){
				var result = "";
				if(currentOptrId == record.get('optr_id')){
					result = "<a href='#' title='修改单号' onclick=Ext.getCmp('transferGridId').editTransferNo("+value+")>修改单号</a>";
				}
				if(record.get('status') === 'UNCONFIRM' && record.get('tran_type') === 'TRANIN'){
					result += "&nbsp;&nbsp;<a href='#' title='调拨确认' onclick=Ext.getCmp('transferGridId').transferConfirm("+value+")>调拨确认</a>";
				}
				if(record.get('status') === 'CONFIRM' && record.get('tran_type') === 'TRANIN'){
					result += "&nbsp;&nbsp;<a href='#' title='转发' onclick=Ext.getCmp('transferGridId').retransfer("+record.get('device_done_code')+")>转发</a>";
				}
				var isHistory = record.get('is_history');
				if(isHistory == 'F'){
						result += "&nbsp;&nbsp;<a href='#' title='历史调拨' onclick=Ext.getCmp('transferGridId').editHisTransfer("+value+",'T')>历史</a>";
				}else{
					result += "&nbsp;&nbsp;<a href='#' title='恢复调拨' onclick=Ext.getCmp('transferGridId').editHisTransfer("+value+",'F')>恢复</a>";
				}
				result += "&nbsp;&nbsp;<a href='#' title='下载明细' onclick=Ext.getCmp('transferGridId').downloadExcel("+value+")>下载</a>";
				return result;
			}}
		];
		
		this.transferConfirm = function(value){
			var win = Ext.getCmp('transferConfirmWinId');
			if(!win){
				win = new TransferConfirmWin();
			}
			win.show();
			Ext.getCmp('transferNoId').setValue(Ext.getCmp('transferGridId').getSelectionModel().getSelected().get('transfer_no'));
			Ext.getCmp('transfer_device_done_code_id').setValue(value);
		};
		this.editTransferNo = function(deviceDoneCode){
			var win = Ext.getCmp('transferNoWinId');
			if(!win){
				win = new TransferNoWin();
			}
			win.show(deviceDoneCode);
		}
		TransferGrid.superclass.constructor.call(this,{
			id:'transferGridId',
			title:'调拨信息',
			region:'center',
			autoExpandColumn:'checkout_remark_id',
			autoScroll:true,
			border:false,
			ds:this.transferGridStore,
			columns:columns,
			sm:sm,
			tbar:['-','关键字&nbsp;',
				new Ext.ux.form.SearchField({  
	                store: this.transferGridStore,
	                width: 120,
	                hasSearch : true,
	                emptyText: '支持编号模糊查询'
	            }),'-',
	            new Ext.form.ComboBox({
					store : this.deviceModelStore,
					forceSelection:true,
					selectOnFocus:true,
					triggerAction:'all',
					boxMinWidth : 250,
					minListWidth : 250,
					mode:'local',
					emptyText:'选择型号',
					displayField : 'model_type_name',
					valueField : 'device_model',
					listeners : {
						scope : this,
						select : this.doSelectDeviceModel
					}
				}),'-',
	            {
	            	xtype:'combo',store:new Ext.data.ArrayStore({
	            			fields:['transferTypeText','transferType'],
	            			data:[['所有调拨','ALL'],['执行调拨','NOW'],['历史调拨','HISTORY']]
	            		}),displayField:'transferTypeText',valueField:'transferType',
	            	emptyText:'执行中或历史调拨',width:80,
	            	listeners:{
	            		scope:this,select:this.queryTransferByType
	            	}
	            },'-',
	            {id:'checkout_startDate_id',xtype:'datefield',name:'checkout_startDate',format:'Y-m-d',
	            	emptyText:'开始日期',width:90,listeners:{
	            		scope:this,
	            		blur: function(comp){
	            			var v = comp.getValue();
	            			if(v){
	            				Ext.getCmp('checkout_endDate_id').minValue=v.add(Date.DAY,1);
	            			}else{
	            				Ext.getCmp('checkout_endDate_id').setMinValue(null);
	            			}
	            		}
	            	}
	            },'-',
	            {id:'checkout_endDate_id',xtype:'datefield',name:'checkout_endDate',
	            	width:90,format:'Y-m-d',emptyText:'结束日期',listeners:{
	            		scope:this,
	            		blur: function(comp){
	            			var v = comp.getValue();
	            			if(v){
	            				Ext.getCmp('checkout_startDate_id').setMaxValue(v.add(Date.DAY,-1));
	            			}else{
	            				Ext.getCmp('checkout_startDate_id').setMaxValue(null);
	            			}
	            		}
	            	}},'-',
	            {xtype:'button',text:'查询',iconCls:'icon-query',scope:this,handler:this.doDateQuery},'-',
				{text:'文件调拨',iconCls:'icon-excel',scope:this,handler:this.fileTransfer,tooltip:'文件入库：只有1列设备编号'},'-',
				{text:'手工调拨',iconCls:'icon-hand',scope:this,handler:this.handTransfer},'-',
				{text:'批号调拨',iconCls:'icon-batch-number',scope:this,handler:this.batchNumTransfer}
			],
			bbar : new Ext.PagingToolbar({
										store : this.transferGridStore,
										pageSize : Constant.DEFAULT_PAGE_SIZE
									}),
			listeners:{
				scope:this,
				rowdblclick:this.doDblclick
			}
		});
	},
	doDblclick:function(){
		var record = this.getSelectionModel().getSelected();
		var deviceDoneCode = record.get('device_done_code');
		var deviceType = record.get('device_type');
		var deviceModel = record.get('device_model');
		
		var win = Ext.getCmp('deviceDetailInfoWinId');
		if(!win){
			win = new TransferDetailWin();
		}
		win.show(deviceDoneCode,deviceType,deviceModel);
	},
	doDateQuery: function(){
		var startDate = Ext.getCmp('checkout_startDate_id').getRawValue();
		var endDate = Ext.getCmp('checkout_endDate_id').getRawValue();
		var store = this.getStore();
		store.baseParams['startDate'] = startDate;
		store.baseParams['endDate'] = endDate
		store.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
	},
	doSelectDeviceModel: function(combo){
		var store = this.getStore();
		store.baseParams['deviceModel'] = combo.getValue();
		store.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
	},
	queryTransferByType: function(combo){
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
	editHisTransfer: function(v,isHistory){
		var text = '确认转换为历史调拨吗？';
		if(isHistory == 'F'){
			text = '确认转换为执行中的调拨吗？';
		}
		Confirm(text,this,function(){
			var record = this.getSelectionModel().getSelected();
			Ext.Ajax.request({
				url:root+'/resource/Device!editDeviceTransferHistory.action',
				params:{deviceDoneCode:v,isHistory:isHistory},
				scope:this,
				success:function(res){
					var data = Ext.decode(res.responseText);
					if(data === true){
						this.getStore().remove(record);
					}
				}
			});
		});
	},
	downloadExcel: function(v){
		var record = this.getSelectionModel().getSelected();
		var deviceDoneCode = record.get('device_done_code');
		var deviceType = record.get('device_type');
		var deviceModel = record.get('device_model');
		
		window.open(root+'/resource/Device!queryAllTransferDeviceDtail.action?deviceDoneCode='
						+ deviceDoneCode+'&deviceType='+deviceType+'&deviceModel='+ deviceModel);
	},
	fileTransfer:function(){
		var fileWin = Ext.getCmp('transferFileWinId');
		if(!fileWin){
			fileWin = new TransferFileWin();
		}
		fileWin.show();
	},
	retransfer : function(value){
		var handWin = Ext.getCmp('retransferHandWinId');
		if(!handWin){
			handWin = new ReTransferHandWin();
		}
		handWin.prepareReTransfer(value);
		handWin.show();
	},
	handTransfer:function(){
		var handWin = Ext.getCmp('transferHandWinId');
		if(!handWin){
			handWin = new TransferHandWin();
		}
		handWin.show();
	},
	batchNumTransfer: function(){
		
		var batchNumWin = Ext.getCmp('transferBatchNumWinId');
		if(!batchNumWin){
			batchNumWin = new TransferBatchNumWin();
		}
		batchNumWin.show();
	}
});

/**
 * 文件调拨form
 * @class
 * @extends Ext.form.FormPanel
 */
var TransferFileForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		TransferFileForm.superclass.constructor.call(this,{
			id:'transferFileFormId',
			labelWidth: 70,
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[
				transferNo,
				{fieldLabel:'设备类型',xtype:'paramcombo',typeAhead:false,paramName:'DEVICE_TYPE',
					hiddenName:'deviceType',allowBlank:false
				},
				depot,statusCmp,
				{id:'checkOutFileId',fieldLabel:'设备文件',name:'files',xtype:'textfield',inputType:'file',allowBlank:false,anchor:'95%'},
				outRemark
				]
		});
	},
	initComponent:function(){
		TransferFileForm.superclass.initComponent.call(this);
		App.form.initComboData( this.findByType("paramcombo"));
		var comboes = this.findByType('combo');
		if(comboes.length>0)
			for(var i=0;i<comboes.length;i++){
				if(comboes[i].hiddenName !='deviceTransfer.transfer_status'){
					if(!(comboes[i] instanceof Ext.ux.ParamCombo))
						comboes[i].getStore().load();
				}
			}
	}
});

/**
 * 文件调拨弹出框
 * @class
 * @extends Ext.Window
 */
var TransferFileWin = Ext.extend(Ext.Window,{
	fileForm:null,
	constructor:function(){
		this.transferFileForm = new TransferFileForm();
		TransferFileWin.superclass.constructor.call(this,{
			id : 'transferFileWinId',
			title:'文件调拨',
			closeAction:'hide',
			maximizable:false,
			width: 450,
			height: 300,
			layout: 'fit',
			border: false,
			items:[this.transferFileForm],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.transferFileForm.getForm().reset();
					resetFileCompContent(this.transferFileForm.getForm().findField('files'));//清空上传文件内容
					this.hide();
				}
			}
		});
	},
	doSave:function(){
		if(this.transferFileForm.getForm().isValid()){
			
			var file = Ext.getCmp('checkOutFileId').getValue();
			var flag = checkFileType(file);
			if(!flag)return;
			
			var values = this.transferFileForm.getForm().getValues();
			this.transferFileForm.getForm().submit({
				url:'resource/Device!saveTransferFile.action',
				waitTitle:'提示',
				waitMsg:'正在上传中,请稍后...',
				scope:this,
				success:function(form,action){
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
						}else{
							Alert('文件上传成功!',function(){
								this.hide();
								Ext.getCmp('transferGridId').getStore().reload();
							},this);
						}
					}
				},  
				failure : function(form, action) {  
					alert("文件上传失败!");  
				}
			});
		}
	}
});

/**
 * 手动调拨form
 * @class
 * @extends Ext.form.FormPanel
 */
var TransferHandForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		TransferHandForm.superclass.constructor.call(this,{
			id:'transferHandFormId',
			region:'north',
			height:140,
			labelWidth: 80,
			fileUpload: true,
			bodyStyle:'padding-top:10px',	
			defaults : {
				baseCls : 'x-plain'
			},
			items:[transferNo,depot,statusCmp,outRemark]
		});
	},
	initComponent:function(){
		TransferHandForm.superclass.initComponent.call(this);
		App.form.initComboData( this.findByType("paramcombo"));
		var comboes = this.findByType('combo');
		if(comboes.length>0)
			for(var i=0;i<comboes.length;i++){
				if(comboes[i].hiddenName !='deviceTransfer.transfer_status'){
					if(!(comboes[i] instanceof Ext.ux.ParamCombo))
						comboes[i].getStore().load();	
				}
			}
			
	}
});

var TransferDeviceGrid = Ext.extend(QueryDeviceGrid,{
	constructor:function(){
		TransferDeviceGrid.superclass.constructor.call(this);
		var config = this.getColumnModel().config;
		//为第二列"编号" 添加编辑组件
		config[0].editor = new Ext.form.TextField({
			vtype:'alphanum',
			listeners:{
				scope:this,
				change:this.queryAndAddDevice
			}
		});
		//重新注册
		this.reconfigure(
			this.getStore(),new Ext.grid.ColumnModel(config)
		);
	},
	queryAndAddDevice:function(field,newValue,oldValue){
		if(newValue && newValue!==oldValue){
			var that = this;
			Ext.Ajax.request({
				url:'resource/Device!queryDeviceInfo.action',
				params:{deviceCode:newValue},
				scope:this,
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					if(data.device_id){
						var record = this.getSelectionModel().getSelected();
						record.fields.each(function(field){
							record.set(field.name,data[field.name]);
						},this);
//						Confirm('需要继续添加吗?',this,function(){
							this.doAdd();
//						});
					}
				},
				clearData:function(){
					try{
						that.getStore().remove(that.getSelectionModel().getSelected());
					}catch(e){}
				}
			});
		}
	}
});

/**
 * 手工调拨弹出框
 * @class
 * @extends Ext.Window
 */
var TransferHandWin = Ext.extend(Ext.Window,{
	handForm:null,
	transInfoPanel: null,
	constructor:function(){
		this.handForm = new TransferHandForm();
		this.transferDeviceGrid = new TransferDeviceGrid();
		FileCheckInWin.superclass.constructor.call(this,{
			id : 'transferHandWinId',
			title:'手工调拨',
			closeAction:'hide',
			maximizable:false,
			width: 800,
			height: 500,
			border: false,
			layout:'border',
			items:[this.handForm,this.transferDeviceGrid],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.handForm.getForm().reset();
					this.transferDeviceGrid.getStore().removeAll();
				}
			}
		});
	},
	doSave:function(){
		var form = this.handForm.getForm();
		if(!form.isValid())return;
		
		var formValues = form.getValues();
		
		this.transferDeviceGrid.stopEditing();
		
		var store = this.transferDeviceGrid.getStore();
		
		var arrCode = [];
		for(var i=0;i<store.getCount();i++){
			var data = store.getAt(i).data;
			if(data['device_id']){
				//过滤掉重复调拨的设备
				if(arrCode.indexOf(data['device_id']) >=0){
					Alert('编号有相同的，请检查！');
					return ;
				}
				arrCode.push(data);
			}
		}
		var arr = [];//只传递device_id到后台
		Ext.each(arrCode,function(d){
			var obj = {};
			obj['device_id'] = d['device_id'];
			obj['device_type'] = d['device_type'];
			obj['device_code'] = d['device_code'];
			arr.push(obj);
		});
		
		if(arr.length === 0){
			Alert('请正确输入设备信息！');
			return;
		}
		
		var obj={};
		Ext.apply(obj,formValues);
		obj['deviceDtoList'] = Ext.encode(arr);
	
		var msg = Show();
		Ext.Ajax.request({
			url:'resource/Device!saveTransfer.action',
			params:obj,
			scope:this,
			success:function(res,opt){
				msg.hide();
				msg = null;
				Alert('添加成功',function(){
					this.hide();
					Ext.getCmp('transferGridId').getStore().reload();
				},this);
			}
		});
	}
});

/**
 * 转发..
 * @class ReTransferHandWin
 * @extends Ext.Window
 */
ReTransferHandWin = Ext.extend(Ext.Window,{
	handForm:null,
	transInfoPanel:null,
	constructor:function(){
		this.handForm = new TransferHandForm();
		
		this.transferInfoTemplate = '<table width="100%" border="0" cellpadding="0" cellspacing="0">' +
			'<tr height=24>'+
				'<td class="label" width=20%>原调拨机顶盒数量:</td>' +
				'<td class="input_bold" width=30%>&nbsp;{[values.old.STB || 0]}</td>'+
				'<td class="label" width=20%>本次调拨机顶盒数量:</td>' +
				'<td class="input_bold" width=30%>&nbsp;{[values.now.STB || 0]}</td>'+
			'</tr>'+
			'<tr height=24>'+
			'<td class="label">原调拨智能卡数量:</td>' +
				'<td class="input">&nbsp;{[values.old.CARD || 0]}</td>'+
				'<td class="label">本次调拨智能卡数量:</td>' +
				'<td class="input">&nbsp;{[values.now.CARD || 0]}</td>'+
			'</tr>' +
			'<tr height=24>'+
				'<td class="label">原调拨猫数量:</td>' +
				'<td class="input">&nbsp;{[values.old.MODEM || 0]}</td>'+
				'<td class="label">本次调拨猫数量:</td>' +
				'<td class="input">&nbsp;{[values.now.MODEM || 0]}</td>'+
			'</tr>' +
			'</tpl>' +
		'</table>';

		this.tpl = new Ext.XTemplate( this.transferInfoTemplate );
		Ext.apply(this.handForm,{height:145,border:false,bodyStyle: "background:#F9F9F9;padding-top: 4px;"})
		ReTransferHandWin.superclass.constructor.call(this,{
			id : 'retransferHandWinId',
			title:'转发',
			closeAction:'hide',
			maximizable:false,
			width: 600,
			height: 320,
			border: false,
			layout:'border',
			items:[this.handForm,
				{xtype : "panel",border : false,
					bodyStyle: "background:#F9F9F9; padding: 10px;padding-top: 4px;padding-bottom: 0px;",
					html : this.tpl.applyTemplate({old:{},now:{}}),
					buttonAlign : 'center',region:'center'
				}
			],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.handForm.getForm().reset();
				}
			}
		});
	},
	prepareReTransfer:function(value){
		//TODO 转发功能
		msg = Show();
		this.fromDoneCode = value;
		Ext.Ajax.request({
			url:'resource/Device!queryReTransDevices.action',
			params:{doneCode:value},
			scope:this,
			success:function(res,opt){
				msg.hide();
				msg = null;
				var data = Ext.decode(res.responseText);
				if(!data || !data.now || 
						(data.now.STB ==0 && data.now.CARD ==0 && data.now.MODEM ==0) ){
					Alert('本批次调拨的设备已没有符合可调拨条件的设备!');
					return;
				}
				this.tpl.applyTemplate(data)
				this.tpl.overwrite( this.items.itemAt(1).body, data);
			}
		});
	},
	doSave:function(){
		var form = this.handForm.getForm();
		if(!form.isValid())return;
		
		var formValues = form.getValues();
		
		var obj={doneCode:this.fromDoneCode};
		Ext.apply(obj,formValues);
		
		var msg = Show();
		Ext.Ajax.request({
			url:'resource/Device!reTransDevices.action',
			params:obj,
			scope:this,
			success:function(res,opt){
				msg.hide();
				msg = null;
				Alert('转发成功',function(){
					this.hide();
					Ext.getCmp('transferGridId').getStore().reload();
				},this);
			}
		});
	}
});


/**
 * 批号调拨form
 * @class
 * @extends Ext.form.FormPanel
 */
var TransferBatchForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		TransferBatchForm.superclass.constructor.call(this,{
			id:'transferBatchNumFormId',
			region:'north',
			height:140,
			labelWidth: 80,
			fileUpload: true,
			bodyStyle:'padding-top:10px',		
			defaults : {
				baseCls : 'x-plain'
			},
			items:[transferNo,depot,statusCmp,outRemark]
		});
	},
	initComponent:function(){
		TransferBatchForm.superclass.initComponent.call(this);
		App.form.initComboData( this.findByType("paramcombo"));
		var comboes = this.findByType('combo');
		if(comboes.length>0)
			for(var i=0;i<comboes.length;i++){
				if(comboes[i].hiddenName !='deviceTransfer.transfer_status'){
					if(!(comboes[i] instanceof Ext.ux.ParamCombo))
						comboes[i].getStore().load();	
				}
			}
			
	}
});

/**
 * 批号表格查询
 */
BatchNumTransferDeviceGrid = Ext.extend(Ext.grid.GridPanel,{
	constructor:function(){
		this.queryField = new Ext.form.TextField({
			emptyText: '设备批号...',
			width: 160,
			listeners: {
				scope: this,
				'specialKey': function(field, e) {  
                    if (e.getKey() == Ext.EventObject.ENTER) {
                    	this.doQuery();
                    }  
                } 
			}
		});
		this.queryDeviceGridStore = new Ext.data.JsonStore({
			url: root+'/resource/Device!queryDeviceByBatch.action',
			fields:['device_id','device_type','device_type_text','device_model','device_model_text',
				'modem_mac','device_code','pair_device_model','pair_device_model_text','pair_device_code',
				'pair_card_id','pair_modem_id','pair_device_modem_model_text','pair_device_modem_code']
		});
		this.queryDeviceGridStore.on("load", this.setDefaults, this );
		var sm = new Ext.grid.CheckboxSelectionModel();
		var columns = [new Ext.grid.RowNumberer(), sm,
   			{header:'编号(Modem_mac)',dataIndex:'device_code',width:150},//,editor:deviceCodeComp
   			{header:'类型',dataIndex:'device_type_text',width:70},
   			{header:'型号',dataIndex:'device_model_text',width:90},
   			{header:'modem_mac',dataIndex:'modem_mac',width:75,hidden:true},
   			{header:'配对卡型号',dataIndex:'pair_device_model_text',width:90},
   			{header:'配对卡号',dataIndex:'pair_device_code',width:120},
   			{header:'配对MODEM型号',dataIndex:'pair_device_modem_model_text',width:90},
   			{header:'配对MODEM号',dataIndex:'pair_device_modem_code',width:120}
   		];
		
		BatchNumTransferDeviceGrid.superclass.constructor.call(this, {
			region: 'center',
			title:'设备信息',
			store: this.queryDeviceGridStore,
			columns: columns,
			maskDisabled: true,
			loadMask: {
				store: this.queryDeviceGridStore
			},
			sm: sm,
			tbar: [this.queryField ,'-',{
				iconCls: 'icon-query',
				text: '查询',
				scope: this,
				handler: this.doQuery
			}]
		});
	},
	setDefaults: function(){
		this.getSelectionModel().selectAll();
	},
	doQuery: function(){
		var keyword = this.queryField.getValue().trim();
		if(keyword){
			this.queryDeviceGridStore.load({
				params: {
					batch_num: keyword
				}
			});
		}else{
			Alert("请输入批号查询");
		}
	}
});

/**
 * 批号调拨弹出框
 * @class
 * @extends Ext.Window
 */
TransferBatchNumWin = Ext.extend(Ext.Window,{
	batchNumForm:null,
	queryDeviceGrid:null,
	constructor:function(){
		this.batchNumForm = new TransferBatchForm();
		this.queryDeviceGrid = new BatchNumTransferDeviceGrid();
		TransferBatchNumWin.superclass.constructor.call(this,{
			id : 'transferBatchNumWinId',
			title:'批号调拨',
			closeAction:'hide',
			maximizable:false,
			width: 800,
			height: 500,
			border: false,
			layout:'border',
			items:[this.batchNumForm, this.queryDeviceGrid],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.batchNumForm.getForm().reset();
					this.queryDeviceGrid.getStore().removeAll();
				}
			}
		});
	},
	doSave:function(){
		var form = this.batchNumForm.getForm();
		if(!form.isValid())return;
		var formValues = form.getValues();
		var selections = this.queryDeviceGrid.getSelectionModel().getSelections();
		
		var arr = [];
		Ext.each(selections, function(d){
			var obj = {};
			obj['device_id'] = d.get('device_id');
			obj['device_type'] = d.get('device_type');
			obj['device_code'] = d.get('device_code');
			arr.push(obj);
		});
		
		if(arr.length === 0){
			Alert('请选择需要调拨的设备！');
			return;
		}
		
		var obj={};
		Ext.apply(obj, formValues);
		obj['deviceDtoList'] = Ext.encode(arr);
	
		var msg = Show();
		Ext.Ajax.request({
			url:'resource/Device!saveTransfer.action',
			params:obj,
			scope:this,
			success:function(res,opt){
				msg.hide();
				msg = null;
				Alert('调拨成功！',function(){
					this.queryDeviceGrid.getStore().removeAll();
					Ext.getCmp('transferGridId').getStore().reload();
				},this);
			}
		});
	}
});


/**
 * 调拨确认form
 * @class
 * @extends Ext.form.FormPanel
 */
var TransferConfirmForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		TransferConfirmForm.superclass.constructor.call(this,{
			id:'transferConfirmFormId',
			border:false,
			labelWidth: 85,
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain',
				xtype:'textfield'
			},
			items:[
				{id:'transferNoId',fieldLabel:'调拨单号',name:'transferNo',readOnly:true},
				{id:'transfer_device_done_code_id',name:'deviceDoneCode',xtype:'hidden'},
				{fieldLabel:'有效',hiddenName:'status',xtype:'combo',allowBlank:false,
					store:new Ext.data.ArrayStore({
						fields:['statusDis','statusValue'],
						data:[['确认','CONFIRM'],['取消','INVALID']]
					}),displayField:'statusDis',valueField:'statusValue'
				},
				{fieldLabel:'确认信息',name:'confirmInfo',xtype:'textarea',height:60,anchor:'90%'}
			]
		});
	}
}); 

/**
 * 调拨确认Window
 * @class
 * @extends Ext.Window
 */
var TransferConfirmWin = Ext.extend(Ext.Window,{
	transferConfirmForm :null,
	constructor:function(){
		this.transferConfirmForm = new TransferConfirmForm();
		TransferConfirmWin.superclass.constructor.call(this,{
			id : 'transferConfirmWinId',
			title:'调拨确认',
			closeAction:'hide',
			maximizable:false,
			width: 300,
			height: 200,
			layout:'fit',
			items:[this.transferConfirmForm],
			buttonAlign:'right',
			buttons:[{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.transferConfirmForm.getForm().reset();
				}
			}
		});
	},
	doSave:function(){
		if(!this.transferConfirmForm.getForm().isValid())return;
		var values = this.transferConfirmForm.getForm().getValues();
		Ext.Ajax.request({
			url:'resource/Device!checkTransfer.action',
			params:values,
			scope:this,
			success:function(res,opt){
				Alert('调拨确认成功',function(){
					this.hide();
					Ext.getCmp('transferGridId').getStore().reload();
				},this);
			}
		});
	}
});

/**
 * 修改单号
 * @class
 * @extends Ext.Window
 */
var TransferNoWin = Ext.extend(Ext.Window, {
	constructor: function(){
		TransferNoWin.superclass.constructor.call(this,{
			id:'transferNoWinId',
			title:'修改单号',
			closeAction:'hide',
			border:false,
			maximizable:false,
			width: 330,
			height: 250,
			items:[{id:'transferNoFormId',xtype:'form',border:false,
				bodyStyle:'padding-top:10px',labelWidth:65,items:[
					{xtype:'hidden',name:'deviceDoneCode'},
					{xtype:'textfield',fieldLabel:'新单号',name:'transferNo',width:200,vtype:'alphanum',allowBlank:false},
					{fieldLabel:'备注',name:'remark',maxLength:128,xtype:'textarea',width : 210,height : 140}//128个汉字
				]
			}],
			buttonAlign:'right',
			buttons:[
				{text:'保存',iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
						this.fireEvent('hide',this);
					}
				}
			],
			listeners:{
				scope:this,
				hide:function(){
					this.hide();
					Ext.getCmp('transferNoFormId').getForm().reset();
				}
			}
		});
	},
	show: function(deviceDoneCode){
		TransferNoWin.superclass.show.call(this);
		var form = Ext.getCmp('transferNoFormId').getForm();
		form.findField('deviceDoneCode').setValue(deviceDoneCode);
	},
	doSave: function(){
		var form = Ext.getCmp('transferNoFormId').getForm();
		if(!form.isValid())return;
		var values = form.getValues();
		Ext.Ajax.request({
			url:root+'/resource/Device!editTransferNo.action',
			params:values,
			scope:this,
			success:function(res){
				var data = Ext.decode(res.responseText);
				if(data === true){
					var record = Ext.getCmp('transferGridId').getSelectionModel().getSelected();
					record.set('transfer_no',values['transferNo']);
					record.set('remark',values['remark']);
					record.commit();
					this.fireEvent('hide',this);
				}
			}
		});
	}
});

var TransferDetailDeviceGrid = Ext.extend(DeviceGrid,{
	initEvents:function(){
		TransferDetailDeviceGrid.superclass.initEvents.call(this);
		this.on("rowclick",this.doRecordClick,this);
	},
	doRecordClick:function(){
		var record = this.getSelectionModel().getSelected();
		var deviceDoneCode = record.get('device_done_code');
		var deviceType = record.get('device_type');
		var deviceModel = record.get('device_model');
		
		var win = Ext.getCmp('transferDetailWinId');
		if(!win){
			win = new TransferDetailWin();
		}
		win.show(deviceDoneCode,deviceType,deviceModel);
	}
});

var TransferDetailWin = Ext.extend(Ext.Window,{
	dsStore:null,
	constructor:function(){
		this.dsStore = new Ext.data.JsonStore({
			url:'resource/Device!queryTransferDeviceDetail.action',
			totalProperty:'totalProperty',
			root:'records',
			fields:['device_model_text','device_code','is_new_stb_text','device_status_text']
		});
		var columns = [
			{header:'设备型号',dataIndex:'device_model_text',width:150,renderer:App.qtipValue},
			{header:'设备编号',dataIndex:'device_code',width:120,renderer:App.qtipValue},
			{header:'设备状态',dataIndex:'device_status_text',width:120,renderer:App.qtipValue},
			{header:'新机',dataIndex:'is_new_stb_text',width:120,renderer:App.qtipValue}

		];
		this.grid = new Ext.grid.GridPanel({
			border:false,
			store:this.dsStore,
			columns:columns,
			bbar:new Ext.PagingToolbar({store:this.dsStore,pageSize:Constant.DEFAULT_PAGE_SIZE})
		});
		TransferDetailWin.superclass.constructor.call(this,{
			id:'deviceDetailInfoWinId',
			title:'详细信息',
			closeAction:'close',
			maximizable:false,
			width:850,
			height:400,
			layout:'fit',
			items:[this.grid],
			buttons:[{text:'关闭',iconCls:'icon-close',scope:this,handler:function(){
					this.close();
				}
			}]
		});
	},
	show:function(deviceDoneCode,deviceType,deviceModel){
		this.dsStore.removeAll();
		this.dsStore.baseParams={
			deviceDoneCode:deviceDoneCode,
			deviceType:deviceType,
			deviceModel:deviceModel
		};
		this.dsStore.load({
			params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}
		});
		TransferDetailWin.superclass.show.call(this);
	}
});

CheckOut = Ext.extend(Ext.Panel,{
	constructor:function(){
		var transferGrid = new TransferGrid();
//		checkOutDeviceGrid = new TransferDetailDeviceGrid();
		CheckOut.superclass.constructor.call(this,{
			id:'CheckOut',
			title:'设备调拨',
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'fit',
			items:[transferGrid/*,checkOutDeviceGrid*/]
		});
	}
});


