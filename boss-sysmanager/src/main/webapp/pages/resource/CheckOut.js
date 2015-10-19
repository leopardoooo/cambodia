var COMMON_LU = lsys('common');
var DEV_COMMON_LU = lsys('DeviceCommon');
var CHE_OUT_LU = lsys('CheckOut');
var MSG_LU = lsys('msgBox');

//调拨
var transferNo = {
	fieldLabel:DEV_COMMON_LU.labelTransNo,xtype:'textfield',vtype:'alphanum',name:'deviceTransfer.transfer_no',allowBlank:false
};

//仓库
var depot = {fieldLabel:lsys('DeviceDetailInfo.labelTargetDepot'),hiddenName:'deviceTransfer.depot_order',xtype:'combo',allowBlank:false,
		store:new Ext.data.JsonStore({
			url:'resource/Device!queryDeptByOptr.action',
			fields:['dept_id','dept_name']
		}),displayField:'dept_name',valueField:'dept_id',triggerAction:'all',mode:'local',width :150,minListWidth :250
};

//备注
var outRemark = {fieldLabel:lsys('common.remarkTxt'),name:'deviceTransfer.remark',xtype:'textarea',anchor:'90%',height:45};
//调拨状态
var statusCmp = {fieldLabel:DEV_COMMON_LU.labelDevStatus,xtype:'paramcombo',paramName:'TRANSFER_STATUS',allowBlank:false,
		hiddenName:'deviceTransfer.transfer_status',defaultValue:'NEWD'
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
                model_type_name: COMMON_LU.selectAll,
                device_model: ''
            }));
        });
		this.transferGridStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var sm = new Ext.grid.RowSelectionModel({singleselect:true});
		var currentOptrId = App.data.optr['optr_id'];
		var columns = [
			{header:DEV_COMMON_LU.labelTransNo,dataIndex:'transfer_no',width:70,renderer:function(value,metaData,record){
				return '<div style="text-decoration:underline;font-weight:bold"  onclick="Ext.getCmp(\'transferGridId\').doDblclick();"  ext:qtitle="" ext:qtip="' + value + '">' + value +'</div>';
			}},
			{header:CHE_OUT_LU.labelTransType,dataIndex:'tran_type_text',width:65},
			{header:DEV_COMMON_LU.labelDepot,dataIndex:'depot_text',width:100,renderer:App.qtipValue},
			{header:COMMON_LU.status,dataIndex:'status_text',width:50,renderer:Ext.util.Format.statusShow},
			{header:COMMON_LU.createDate,dataIndex:'create_time',width:125,renderer:Ext.util.Format.timeFormat},
			{header:CHE_OUT_LU.labelConfirmDate,dataIndex:'confirm_date',width:125,renderer:Ext.util.Format.timeFormat},
			{header:DEV_COMMON_LU.labelDeviceType,dataIndex:'device_type_text',width:65,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelTransStatus,dataIndex:'transfer_status_text',width:65},
			{header:DEV_COMMON_LU.labelDeviceModel,dataIndex:'device_model_text',width:130,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelNum,dataIndex:'count',width:40},
			{id:'checkout_remark_id',header:lsys('common.remarkTxt'),dataIndex:'remark',renderer:App.qtipValue},
			{header:COMMON_LU.doActionBtn,dataIndex:'device_done_code',width:240,renderer:function(value,meta,record){
				var result = "";
				if(currentOptrId == record.get('optr_id')){
					result = "<a href='#' title='"+ DEV_COMMON_LU.titleModifyOrderNum + "' onclick=Ext.getCmp('transferGridId').editTransferNo("+value+")>" + DEV_COMMON_LU.titleModifyOrderNum + "</a>";
				}
				if(record.get('status') === 'UNCONFIRM' && record.get('tran_type') === 'TRANIN'){
					result += "&nbsp;&nbsp;<a href='#' title='" + DEV_COMMON_LU.titleTransConfirm + "' onclick=Ext.getCmp('transferGridId').transferConfirm("+value+")>" + CHE_OUT_LU.titleTransConfirm + "</a>";
				}
				if(record.get('status') === 'CONFIRM' && record.get('tran_type') === 'TRANIN'){
					if(record.get('device_type') == 'STB'&& record.get('device_type') == 'MODEM'){
						result += "&nbsp;&nbsp;<a href='#' title='" + CHE_OUT_LU.labelTransIn + "' onclick=Ext.getCmp('transferGridId').retransfer("+record.get('device_done_code')+")>" + CHE_OUT_LU.labelTransIn + "</a>";
					}
				}
				var isHistory = record.get('is_history');
				if(isHistory == 'F'){
						result += "&nbsp;&nbsp;<a href='#' title='"+DEV_COMMON_LU.labelTransHistory+"' onclick=Ext.getCmp('transferGridId').editHisTransfer("+value+",'T')>"+COMMON_LU.labelHistory+"</a>";
				}else{
					result += "&nbsp;&nbsp;<a href='#' title='"+DEV_COMMON_LU.labelRestoreTrans+"' onclick=Ext.getCmp('transferGridId').editHisTransfer("+value+",'F')>"+COMMON_LU.restore+"</a>";
				}
				if(record.get('device_type') == 'STB' && record.get('device_type') == 'MODEM'){
					result += "&nbsp;&nbsp;<a href='#' title='"+COMMON_LU.downLoadDetail+"' onclick=Ext.getCmp('transferGridId').downloadExcel("+value+")>"+COMMON_LU.downLoad+"</a>";
				}
				result += "&nbsp;&nbsp;<a href='#' title='"+CHE_OUT_LU.labelPrint+"' onclick=Ext.getCmp('transferGridId').print("+value+")>"+CHE_OUT_LU.labelPrint+"</a>";
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
		
		var twoTbar = new Ext.Toolbar({
			items : [{text:CHE_OUT_LU.labelFileTrans,iconCls:'icon-excel',scope:this,handler:this.fileTransfer,tooltip:MSG_LU.tipDevCheckFileFormat},'-',
				{text:CHE_OUT_LU.labelManualTrans,iconCls:'icon-hand',scope:this,handler:this.handTransfer},'-',
				{text:CHE_OUT_LU.labelBatchNumTrans,iconCls:'icon-batch-number',scope:this,handler:this.batchNumTransfer},
				{text:CHE_OUT_LU.labelMateralTrans,iconCls:'icon-hand',scope:this,handler:this.materalTransfer}]
		});
		TransferGrid.superclass.constructor.call(this,{
			id:'transferGridId',
			title:DEV_COMMON_LU.titleTransInfo,
			region:'center',
			autoExpandColumn:'checkout_remark_id',
			border:false,
			ds:this.transferGridStore,
			columns:columns,
			sm:sm,
			tbar:['-',COMMON_LU.inputKeyWork ,
				new Ext.ux.form.SearchField({  
	                store: this.transferGridStore,
	                width: 120,
	                hasSearch : true,
	                emptyText: DEV_COMMON_LU.tipSupportFuzzyQuery
	            }),'-',
	            new Ext.form.ComboBox({
					store : this.deviceModelStore,
					forceSelection:true,
					selectOnFocus:true,
					triggerAction:'all',
					boxMinWidth : 350,
					minListWidth : 350,
					mode:'local',
					emptyText:MSG_LU.selectModel,
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
	            			data:[[CHE_OUT_LU.transStatus.ALL,'ALL'],[CHE_OUT_LU.transStatus.NOW,'NOW'],[CHE_OUT_LU.transStatus.HISTORY,'HISTORY']]
	            		}),displayField:'transferTypeText',valueField:'transferType',
	            	emptyText:CHE_OUT_LU.tipTransStatus,width:80,
	            	listeners:{
	            		scope:this,select:this.queryTransferByType
	            	}
	            },'-',
	            {id:'checkout_startDate_id',xtype:'datefield',name:'checkout_startDate',format:'Y-m-d',
	            	emptyText:COMMON_LU.startDate,width:90,listeners:{
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
	            	width:90,format:'Y-m-d',emptyText:COMMON_LU.endDate,listeners:{
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
	            {xtype:'button',text:COMMON_LU.query,iconCls:'icon-query',scope:this,handler:this.doDateQuery},'-'
				
				
			],
			bbar : new Ext.PagingToolbar({
						store : this.transferGridStore,
						pageSize : Constant.DEFAULT_PAGE_SIZE
					})
			,listeners:{
				scope:this,
				rowdblclick:this.doDblclick,
				'render' : function() {
					twoTbar.render(this.tbar);
				}
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
		var text = MSG_LU.confirm2HisTrans;
		if(isHistory == 'F'){
			text = MSG_LU.confirm2NowTrans;
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
	},
	materalTransfer:function(){
		var materalWin = Ext.getCmp('transferMateralWinId');
		if(!materalWin){
			materalWin = new TransferMateralWin();
		}
		materalWin.show();
	},
	print: function(deviceDoneCode){
		if(PrintTools.isIE){
			
			Confirm('确定打印吗?',this,function(){
				Ext.Ajax.request({
					url:root+'/resource/Device!queryTransferdevicePrintInfo.action',
					params:{deviceDoneCode:deviceDoneCode},
					scope:this,
					success:function(res){
						var data = Ext.decode(res.responseText);
						if(data){
							var printMothod = function(deviceData){
								var content = PrintTools.getContent( data["content"] , deviceData );
								PrintTools.webPrint(content);
							}
							var deviceList = data['data']['deviceListInfo']; 
							if(deviceList.length <=4){
								printMothod(data['data']);
							}else{
								var deviceDataList = [], list = [], deviceInfo = {};
								var xmlContent = data['content'];
								deviceInfo['transferDevice'] = data['data']['transferDevice'];
								
								for(var i=0,len=deviceList.length;i<len;i++){
									list.push(deviceList[i]);
									if(list.length == 4){
										deviceDataList.push(list);
										list = [];
									}
								}
								if(list.length > 0){
									deviceDataList.push(list);
								}
								var timeInvokeMothod = function(deviceListInfo, i){
									deviceInfo['deviceListInfo'] = deviceListInfo;
									var obj = {};
									Ext.apply(obj, deviceInfo);
									setTimeout(function(){printMothod(obj);},5000*i);
								}
								for(var i=0,len=deviceDataList.length;i<len;i++){
									timeInvokeMothod(deviceDataList[i], i);
								}
								
							}
						}
					}
				});
			});
		}else{
			Alert('请使用IE浏览器打印!');
		}
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
			labelWidth: 120,
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[
				transferNo,
				{fieldLabel:DEV_COMMON_LU.labelDeviceType,xtype:'paramcombo',typeAhead:false,paramName:'DEVICE_TYPE',
					hiddenName:'deviceType',allowBlank:false,defaultValue:'STB',id:'deviceTypeItemId'					
				},
				depot,statusCmp,
				{
	                xtype: 'displayfield',
	                width : 400,
	                value:"<font style='font-size:14px;color:red'>"+CHECK_COMMON.filesFormatFour+"</font>"
				},
				{id:'checkOutFileId',fieldLabel:DEV_COMMON_LU.labelDevFile,name:'files',xtype:'textfield',inputType:'file',allowBlank:false,anchor:'95%'},
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
			title:CHE_OUT_LU.labelFileTrans,
			closeAction:'hide',
			maximizable:false,
			width: 580,
			height: 340,
			layout: 'fit',
			border: false,
			items:[this.transferFileForm],
			buttonAlign:'right',
			buttons:[{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
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
			var flag = checkTxtXlsFileType(file);
			if(flag === false)return;
//			var flag = checkFileType(file);
//			if(!flag)return;
			
			var values = this.transferFileForm.getForm().getValues();
			var msg = Show();
			this.transferFileForm.getForm().submit({
				url:'resource/Device!saveTransferFile.action?fileType='+flag,
//				waitTitle:COMMON_LU.tipTxt,
//				waitMsg:COMMON_LU.waitForUpload,
				scope:this,
				success:function(form,action){
					msg.hide();
					var data = action.result;
					if(data.success == true){
						if(data.msg){//错误信息
							Alert(data.msg);
						}else{
							Alert(MSG_LU.fileUploadSuccess,function(){
								this.hide();
								Ext.getCmp('transferGridId').getStore().reload();
							},this);
						}
					}
				},  
				failure : function(form, action) {  
					Alert(MSG_LU.fileUploadFailure);  
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
			region:'south',
			height:100,
			bodyStyle:'padding-top:10px',	
			layout:'column',
			anchor: '100%',
			defaults:{
				baseCls: 'x-plain',
				columnWidth:0.5,
				layout: 'form',
				labelWidth: 130
			},
			items:[{
				columnWidth:0.45,
				items:[transferNo,depot]
			},{
				columnWidth:0.55,
				items:[statusCmp,outRemark]
			}]
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
			title:CHE_OUT_LU.labelManualTrans,
			closeAction:'hide',
			maximizable:false,
			width: 840,
			height: 500,
			border: false,
			layout:'border',
			items:[this.handForm,this.transferDeviceGrid],
			buttonAlign:'right',
			buttons:[{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
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
					Alert(MSG_LU.warnHasSaveCode);
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
			Alert(MSG_LU.pleaseInputCorrectDevInfo);
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
				Alert(COMMON_LU.addSuccess,function(){
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
				'<td class="label" width=25%>' +
				CHE_OUT_LU.labelOldTransStbNum +
				':</td>' +
				'<td class="input_bold" width=25%>&nbsp;{[values.old.STB || 0]}</td>'+
				'<td class="label" width=25%>' +
				CHE_OUT_LU.labelNewTransStbNum +
				':</td>' +
				'<td class="input_bold" width=25%>&nbsp;{[values.now.STB || 0]}</td>'+
			'</tr>'+
			'<tr height=24>'+
			'<td class="label">' +
			CHE_OUT_LU.labelOldTransCardNum +
			':</td>' +
				'<td class="input">&nbsp;{[values.old.CARD || 0]}</td>'+
				'<td class="label">' +
				CHE_OUT_LU.labelNewTransCardNum +
				':</td>' +
				'<td class="input">&nbsp;{[values.now.CARD || 0]}</td>'+
			'</tr>' +
			'<tr height=24>'+
				'<td class="label">' +
				CHE_OUT_LU.labelOldTransModemNum +
				':</td>' +
				'<td class="input">&nbsp;{[values.old.MODEM || 0]}</td>'+
				'<td class="label">' +
				CHE_OUT_LU.labelNewTransModemNum +
				':</td>' +
				'<td class="input">&nbsp;{[values.now.MODEM || 0]}</td>'+
			'</tr>' +
			'</tpl>' +
		'</table>';

		this.tpl = new Ext.XTemplate( this.transferInfoTemplate );
		Ext.apply(this.handForm,{height:145,border:false,bodyStyle: "background:#F9F9F9;padding-top: 4px;"})
		ReTransferHandWin.superclass.constructor.call(this,{
			id : 'retransferHandWinId',
			title:CHE_OUT_LU.labelTransIn,
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
			buttons:[{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
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
						(!data.now.STB && !data.now.CARD && !data.now.MODEM) ){
					Alert(MSG_LU.notSuitableDev4Trans);
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
				Alert(COMMON_LU.msg.actionSuccess,function(){
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
			region:'south',
			height:100,
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			layout:'column',
			anchor: '100%',
			defaults:{
				baseCls: 'x-plain',
				columnWidth:0.5,
				layout: 'form',
				labelWidth: 130
			},
			items:[{
				columnWidth:0.4,
				items:[transferNo,depot]
			},{
				columnWidth:0.6,
				items:[statusCmp,outRemark]
			}]
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
			emptyText: DEV_COMMON_LU.labelDevBatchNum,
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
   			{header:DEV_COMMON_LU.labelDevCode2,dataIndex:'device_code',width:150},//,editor:deviceCodeComp
   			{header:DEV_COMMON_LU.labelDeviceType,dataIndex:'device_type_text',width:90},
   			{header:DEV_COMMON_LU.labelDeviceModel,dataIndex:'device_model_text',width:200},
   			{header:DEV_COMMON_LU.labelPairCardCode,dataIndex:'pair_device_code',width:150}
//   			{header:'modem_mac',dataIndex:'modem_mac',width:75,hidden:true},
//   			{header:DEV_COMMON_LU.labelPairCardType,dataIndex:'pair_device_model_text',width:90},
//   			{header:DEV_COMMON_LU.labelPairModemType,dataIndex:'pair_device_modem_model_text',width:90},
//   			{header:DEV_COMMON_LU.labelPairModemCode,dataIndex:'pair_device_modem_code',width:120}
   		];
		
		BatchNumTransferDeviceGrid.superclass.constructor.call(this, {
			region: 'center',
//			title:DEV_COMMON_LU.titleDeviceInfo,
			store: this.queryDeviceGridStore,
			columns: columns,
			maskDisabled: true,
			loadMask: {
				store: this.queryDeviceGridStore
			},
			sm: sm,
			tbar: [this.queryField ,'-',{
				iconCls: 'icon-query',
				text: COMMON_LU.query,
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
			Alert(MSG_LU.pleaseInputBatchNum);
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
			title:CHE_OUT_LU.labelBatchNumTrans,
			closeAction:'hide',
			maximizable:false,
			width: 800,
			height: 500,
			border: false,
			layout:'border',
			items:[this.batchNumForm, this.queryDeviceGrid],
			buttonAlign:'right',
			buttons:[{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
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
			Alert(MSG_LU.pleaseSelectDev2Trans);
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
				Alert(COMMON_LU.msg.actionSuccess,function(){
					this.queryDeviceGrid.getStore().removeAll();
					Ext.getCmp('transferGridId').getStore().reload();
				},this);
			}
		});
	}
});


var MateralTransferDeviceGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	materalStore:null,
	remoteData:null,
	constructor:function(parent){
		this.parent = parent;
		materalThat = this;
		this.materalStore = new Ext.data.JsonStore({
			url:'resource/Device!queryMateralTransferDeviceByDepotId.action',
			fields:['device_model','device_model_text','total_num','num','device_id']
		});	
		
		var cm = new Ext.grid.ColumnModel([
				{id:'device_model_text_id',header:DEV_COMMON_LU.labelDeviceModel,dataIndex:'device_model_text',width:300
				},
				{header:DEV_COMMON_LU.labelTotalStoreNum,dataIndex:'total_num',width:100,renderer:App.qtipValue},
				{id:'num_id',header:DEV_COMMON_LU.labelNum,dataIndex:'num',width:100,
					scope:this
					,editor: new Ext.form.NumberField({
						allowDecimals:false,//不允许输入小数 
		    			allowNegative:false,
		    			minValue:0//enableKeyEvents: true,
					})
				},
				{header:DEV_COMMON_LU.labelDevCode,dataIndex:'device_id',hidden:true}
				
			]
		);
		cm.isCellEditable = this.cellEditable;
		MateralTransferDeviceGrid.superclass.constructor.call(this,{
//			title:DEV_COMMON_LU.labelMateralInfo,
			region:'center',
			id:'MateralTransferDeviceGridId',
			ds:this.materalStore,
			clicksToEdit:1,
			cm:cm,
			sm:new Ext.grid.RowSelectionModel({})
//			tbar:[
//				'-',
//				{text:COMMON_LU.addNewOne,iconCls:'icon-add',handler:this.doAdd,scope:this},'-'
//			]
		});
	},//是否可编辑
	cellEditable:function(colIndex,rowIndex){
		var record = materalThat.getStore().getAt(rowIndex);//当前编辑行对应record
		if(colIndex == this.getIndexById('num_id')){
			if(Ext.isEmpty(record.get('device_model_text'))){
				return false;
			}
		}
		return Ext.grid.ColumnModel.prototype.isCellEditable.call(this, colIndex, rowIndex);
	},
	initComponent:function(){
		MateralTransferDeviceGrid.superclass.initComponent.call(this);
	},
	initEvents:function(){
		MateralTransferDeviceGrid.superclass.initEvents.call(this);
		this.on('afterrender',function(){
			this.swapViews();
		},this,{delay:10});
		
		this.on("afteredit",this.afterEdit,this);
		this.on("beforeedit",this.beforeedit,this);
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
	beforeedit:function(obj){
	
	},
	afterEdit:function(obj){
		var record = obj.record;
		var fieldName = obj.field;//编辑的column对应的dataIndex
		var value = obj.value;
		if(fieldName == 'num'){
			if(value >record.get('total_num')){
				record.set('num','');
				Confirm(MSG_LU.tipOutOfStock,this,function(){
					materalThat.startEditing(obj.row,obj.column);
				});
			}
		}
	},
	doAdd:function(){
		var count = this.getStore().getCount();
		var recordType = this.getStore().recordType;
		var record = new recordType({
			device_id:'',device_model:'',
			device_model_text:'',total_num:'',num:''
		});
		this.stopEditing();
		this.getStore().add(record);
		this.startEditing(count,0);
		this.getSelectionModel().selectRow(count);
	}
});

//器材调拨
TransferMateralWin = Ext.extend(Ext.Window,{
	materalForm:null,
	queryDeviceGrid:null,
	constructor:function(){
		this.materalForm = new TransferHandForm();
		this.queryDeviceGrid = new MateralTransferDeviceGrid();
		TransferMateralWin.superclass.constructor.call(this,{
			id : 'transferMateralWinId',
			title:CHE_OUT_LU.labelMateralTrans,
			closeAction:'hide',
			maximizable:false,
			width: 650,
			height: 500,
			border: false,
			layout:'border',
			items:[this.materalForm, this.queryDeviceGrid],
			buttonAlign:'right',
			buttons:[{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.materalForm.getForm().reset();
					this.queryDeviceGrid.getStore().removeAll();
				}
			}
		});
	},
	show:function(){
		TransferMateralWin.superclass.show.call(this);
		this.queryDeviceGrid.materalStore.load();
		
	},
	doSave:function(){
		var form = this.materalForm.getForm();
		if(!form.isValid())return;
		
		var formValues = form.getValues();
		this.queryDeviceGrid.stopEditing();
		
		var store = this.queryDeviceGrid.getStore();
		
		var arr = [];//只传递device_id到后台
		store.each(function(record){
			if(!Ext.isEmpty(record.get('num'))  && record.get('num')>0){
				var obj = {};
				obj['device_id'] = record.get('device_id');
				obj['device_type'] = record.get('device_type');
				obj['device_model'] = record.get('device_model');
				obj['total_num'] = record.get('num');
				arr.push(obj);
			}
		});
		
		if(arr.length === 0){
			Alert(MSG_LU.pleaseInputCorrectDevInfo);
			return;
		}
		var obj={};
		Ext.apply(obj,formValues);
		obj['deviceDtoList'] = Ext.encode(arr);
	
		var msg = Show();
		Ext.Ajax.request({
			url:'resource/Device!saveMateralTransfer.action',
			params:obj,
			scope:this,
			success:function(res,opt){
				msg.hide();
				msg = null;
				Alert(COMMON_LU.addSuccess,function(){
					this.hide();
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
			labelWidth: 120,
			fileUpload: true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain',
				xtype:'textfield'
			},
			items:[
				{id:'transferNoId',fieldLabel:DEV_COMMON_LU.labelTransNo,name:'transferNo',readOnly:true},
				{id:'transfer_device_done_code_id',name:'deviceDoneCode',xtype:'hidden'},
				{fieldLabel:COMMON_LU.effect,hiddenName:'status',xtype:'combo',allowBlank:false,
					store:new Ext.data.ArrayStore({
						fields:['statusDis','statusValue'],
						data:[[COMMON_LU.confirm,'CONFIRM'],[COMMON_LU.cancelBtn,'INVALID']]
					}),displayField:'statusDis',valueField:'statusValue'
				},
				{fieldLabel:CHE_OUT_LU.labelConfirmInfo,name:'confirmInfo',xtype:'textarea',height:60,anchor:'90%'}
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
			title:CHE_OUT_LU.titleTransConfirm,
			closeAction:'hide',
			maximizable:false,
			width: 300,
			height: 200,
			layout:'fit',
			items:[this.transferConfirmForm],
			buttonAlign:'right',
			buttons:[{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
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
				Alert(MSG_LU.transSuccess,function(){
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
			title:DEV_COMMON_LU.titleModifyOrderNum,
			closeAction:'hide',
			border:false,
			maximizable:false,
			width: 330,
			height: 250,
			items:[{id:'transferNoFormId',xtype:'form',border:false,
				bodyStyle:'padding-top:10px',labelWidth:120,items:[
					{xtype:'hidden',name:'deviceDoneCode'},
					{xtype:'textfield',fieldLabel:DEV_COMMON_LU.labelNewOrderNo,name:'transferNo',width:200,vtype:'alphanum',allowBlank:false},
					{fieldLabel:lsys('common.remarkTxt'),name:'remark',maxLength:128,xtype:'textarea',width : 210,height : 140}//128个汉字
				]
			}],
			buttonAlign:'right',
			buttons:[
				{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
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
			{header:DEV_COMMON_LU.labelDeviceModel,dataIndex:'device_model_text',width:200,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelDevCode,dataIndex:'device_code',width:150,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelDevStatus,dataIndex:'device_status_text',width:120,renderer:App.qtipValue},
			{header:DEV_COMMON_LU.labelIsNewStb,dataIndex:'is_new_stb_text',width:120,renderer:App.qtipValue}

		];
		this.grid = new Ext.grid.GridPanel({
			border:false,
			store:this.dsStore,
			columns:columns,
			bbar:new Ext.PagingToolbar({store:this.dsStore,pageSize:Constant.DEFAULT_PAGE_SIZE})
		});
		TransferDetailWin.superclass.constructor.call(this,{
			id:'deviceDetailInfoWinId',
			title:COMMON_LU.detailInfo,
			closeAction:'close',
			maximizable:false,
			width:850,
			height:400,
			layout:'fit',
			items:[this.grid],
			buttons:[{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
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
			title:CHE_OUT_LU._title,
			closable: true,
			border : false ,
			baseCls: "x-plain",
			layout:'fit',
			items:[transferGrid/*,checkOutDeviceGrid*/]
		});
	}
});


