/**
 * 差异管理
 * @class
 * @extends Ext.grid.GridPanel
 */
var COMMON_LU = lsys('common');
var DEV_COMMON_LU = lsys('DeviceCommon');
var DIFF_LU = lsys('DifferenceManage');
var MSG_LU = lsys('msgBox');

var DifferenceGrid = Ext.extend(Ext.grid.GridPanel,{
	differenceGridStore: null,
	constructor:function(){
		this.differenceGridStore = new Ext.data.JsonStore({
			url:'resource/Device!queryDeviceDiffence.action',
			totalProperty:'totalProperty',
			root:'records',
			fields:['device_id','device_code','pair_device_model','pair_device_code','depot_id','remark',
				'depot_id_text','device_type_text','device_type','device_model','device_model_text','diffence_type',
				'diffence_type_text','create_time','pair_device_modem_code','pair_device_modem_model','pair_device_modem_model_text']
		});
		this.differenceGridStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
		var sm = new Ext.grid.CheckboxSelectionModel({});     
		var columns = [
			sm,
			{header:DEV_COMMON_LU.labelDevCode,dataIndex:'device_code',width:180},
			{header:DEV_COMMON_LU.labelDeviceType,dataIndex:'device_type_text',width:75},
			{header:DEV_COMMON_LU.labelDeviceModel,dataIndex:'device_model_text',width:120,renderer:App.qtipValue},
			{header:DIFF_LU.labelDiffType,dataIndex:'diffence_type_text',width:75},
			{header:COMMON_LU.depotText,dataIndex:'depot_id_text',width:120},
			{header:DIFF_LU.labelDiffTime,dataIndex:'create_time',width:120},
//			{header:DEV_COMMON_LU.labelVitualModemModel,dataIndex:'pair_device_modem_model_text',width:100},
			{header:'MAC',dataIndex:'pair_device_modem_code',width:120},
//			{header:DEV_COMMON_LU.labelPairCardType,dataIndex:'pair_device_model',width:100},
			{header:'卡号',dataIndex:'pair_device_code',width:120},
			{header:'备注',dataIndex:'remark',width:120,renderer:App.qtipValue}
		];
		
		DifferenceGrid.superclass.constructor.call(this,{
			id:'differenceGridId',
			region:'center',
			border:false,
			ds:this.differenceGridStore,
			columns:columns,
			sm:sm,
			bbar:new Ext.PagingToolbar({store:this.differenceGridStore,pageSize:Constant.DEFAULT_PAGE_SIZE}),
			tbar:[
				'-',COMMON_LU.inputKeyWork,
				new Ext.ux.form.SearchField({  
	                store: this.differenceGridStore,
	                width: 200,
	                hasSearch : true,
	                emptyText: MSG_LU.supportDevCodeQuery
	            }),'-',{fieldLabel:DEV_COMMON_LU.labelDepot,id:'diff_depot_id',hiddenName:'depotId',xtype:'combo',allowBlank:false,
							store:new Ext.data.JsonStore({
								url:'resource/Device!queryAllDept.action',
								autoLoad:true,
								fields:['dept_id','dept_name']
							}),displayField:'dept_name',valueField:'dept_id',triggerAction:'all',mode:'local',width :150,minListWidth :250
							,listeners : {
								scope:this,
								select:function(combo,record){
									this.differenceGridStore.baseParams['depotId'] = combo.getValue();
									this.differenceGridStore.load({params:{start:0,limit:Constant.DEFAULT_PAGE_SIZE}});
								}
							}
					},
	            '-',
				{text:DIFF_LU.labelManualDiff,iconCls:'icon-add',scope:this,handler:this.addDifference},'-',
				{text:DIFF_LU.labelFileDiff,iconCls:'icon-add',scope:this,tooltip:MSG_LU.tipDevFileDiffInfo ,handler:this.addFileDifference},'-',
//				{text:DIFF_LU.labelConfirmDiff,iconCls:'icon-confirm',scope:this,handler:function(){this.callProcess('confirmDifference',['UNCHECK','NODIFF'],MSG_LU.tipDevHasNoDiff)}},'-',
				{text:DIFF_LU.labelCancelDiff,iconCls:'icon-cancel',scope:this,handler:function(){this.callProcess('cancelDifference',['DIFF'],MSG_LU.tipDevNotDiffCantCancel)}},'-'
			]
		});
	},
	callProcess:function(methodName,diffenTypeArr,info){
		var records = this.getSelectionModel().getSelections();
		if(records && records.length>0){
			for(var i=0,len=records.length;i<len;i++){
				if(diffenTypeArr.indexOf(records[i].get('diffence_type'))  == -1){
					Alert(info);
					return;
				}
			}
			Confirm(MSG_LU.confirmDoAction,this,function(){
				var deviceIds='';
				Ext.each(records,function(r){
					deviceIds = deviceIds.concat(r.get('device_id'),',');
				});
				deviceIds = deviceIds.substring(0,deviceIds.length-1);
				
				this[methodName].call(this,deviceIds);
			});
			
		}else{
			Alert(MSG_LU.tipSelectOneRow);
		}
	},
	addDifference:function(deviceIds){
		var win = Ext.getCmp('addDifferenceWinId');
		if(!win){
			win = new AddDifferenceWin(this);
		}
		win.show();
	},
	addFileDifference:function(deviceIds){
		var win = Ext.getCmp('fileDifferenceWinId');
		
		if(!win){
			win = new AddFileDifferenceWin(this);
		}
		win.show();
	},
	confirmDifference:function(deviceIds){
		Ext.Ajax.request({
			url:'resource/Device!checkDeviceDiffence.action',
			params:{deviceIds:deviceIds},
			scope:this,
			success:function(res,opt){
				Alert(MSG_LU.tipConfirmDiffSuccess,function(){
					this.getStore().reload();
				},this);
			}
		});
	},
	cancelDifference:function(deviceIds){
		Ext.Ajax.request({
			url:'resource/Device!cancelDeviceDiffence.action',
			params:{deviceIds:deviceIds},
			scope:this,
			success:function(res,opt){
				Alert(MSG_LU.tipCalcenDiffSuccess,function(){
					this.getStore().reload();
				},this);
			}
		});
	}
});

var DifferenceQueryDeviceGrid = Ext.extend(QueryDeviceGrid,{
	constructor:function(){
		DifferenceQueryDeviceGrid.superclass.constructor.call(this);
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
		if(newValue && newValue!==oldValue)
			Ext.Ajax.request({
				url:'resource/Device!queryDeviceDiffecnceInfo.action',
				params:{
					deviceCode:newValue,
					depotId:Ext.getCmp('diff_depot_id').getValue()
				},
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
				}
			});
	}
});
 
/**
 * 文件差异
 */
var DiffFileForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(){
		DiffFileForm.superclass.constructor.call(this,{
			border:false,
			region:"center",
			labelWidth: 90,
			layout : 'column',
			fileUpload: true,
			trackResetOnLoad:true,
			bodyStyle:'padding-top:10px',
			defaults:{
				baseCls:'x-plain'
			},
			items:[ {columnWidth:1,layout:'form',
					items:[{
			                xtype: 'displayfield',
			                width : 400,
			                value:"<font style='font-size:13px;color:red'>"+CHECK_COMMON.filesFormatFour+"</font>"
						},
						{id:'differenceManageFielId',fieldLabel:DEV_COMMON_LU.labelDevFile,name:'files',xtype:'textfield',inputType:'file',allowBlank:false,anchor:'80%',emptyText:''},
						 {fieldLabel:COMMON_LU.remarkTxt,name:'remark',xtype:'textarea',anchor:'80%',height:50},
						{id:'depotId',name:'depotId',xtype:'hidden'}
						 
				]}
			]
		});
	},
	initComponent:function(){
		DiffFileForm.superclass.initComponent.call(this);
	}
});

/**
 * 调拨确认Window
 * @class
 * @extends Ext.Window
 */
var AddDifferenceWin = Ext.extend(Ext.Window,{
	parent:null,
	grid :null,
 	constructor:function(p){
 		this.parent = p ,
		this.grid = new DifferenceQueryDeviceGrid();
 		AddDifferenceWin.superclass.constructor.call(this,{
			id : 'addDifferenceWinId',
			title:DIFF_LU.titleManualAddDiff,
			closeAction:'hide',
			maximizable:false,
			width: 800,
			height: 350,
			layout:'border',
			border: false,
			items:[{region:'center',border: false,layout:'fit',items:[this.grid]},
				{
					region: 'south',
					layout:'form',
					height:80,
					bodyStyle:'padding-top:10px',
					border: false,
					labelWidth: 80,
					labelAlign:'right', 
					defaults : {
						baseCls: 'x-plain',
						border : false
					},
					items:[{fieldLabel:COMMON_LU.remarkTxt,name:'remark',id:'differenceRemarkId',xtype:'textarea',anchor:'80%',height:50}]
				}],
			buttonAlign:'right',
			buttons:[{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					this.grid.getStore().removeAll();
				}
			}
		});
	},
	doSave:function(){
		var store = this.grid.getStore();
		var deviceIds='';
		store.each(function(record){
			if(!Ext.isEmpty(record.get('device_id')))
				deviceIds = deviceIds.concat(record.get('device_id'),',')
		});
		if(deviceIds.length === 0){
			Alert(MSG_LU.pleaseInputCorrectDevInfo);
			return;
		}
		deviceIds = deviceIds.substring(0,deviceIds.length-1);
		
		var msg = Show();
		Ext.Ajax.request({
			url:'resource/Device!addDeviceDiffence.action',
			params:{deviceIds:deviceIds,remark:Ext.getCmp('differenceRemarkId').getValue()},
			scope:this,
			success:function(res,opt){
				msg.hide();
				msg = null;
				Alert(COMMON_LU.addSuccess,function(){
					this.hide();
					Ext.getCmp('differenceGridId').getStore().reload();
				},this);
			}
		});
	}
});


/**
 * 文件差异
 * @class
 * @extends Ext.Window
 */
var AddFileDifferenceWin = Ext.extend(Ext.Window,{
	fileForm :null,
 	constructor:function(){
		this.fileForm = new DiffFileForm();
 		AddFileDifferenceWin.superclass.constructor.call(this,{
			id : 'fileDifferenceWinId',
			title:DIFF_LU.titleFileAddDiff,
			closeAction:'hide',
			maximizable:false,
			width: 650,
			height: 350,
			layout:'border',
			items:[this.fileForm],
			buttonAlign:'right',
			buttons:[{text:COMMON_LU.saveBtn,iconCls:'icon-save',scope:this,handler:this.doSave},
				{text:COMMON_LU.cancel,iconCls:'icon-close',scope:this,handler:function(){
					this.hide();
				}}],
			listeners:{
				scope:this,
				hide:function(){
					
 				}
			}
		});
	},
	doSave:function(){
		
		 if(this.fileForm.getForm().isValid()){
			var file = Ext.getCmp('differenceManageFielId').getValue();
			var flag = checkTxtXlsFileType(file);
			if(flag === false)return;
			
 			Ext.getCmp('depotId').setValue(Ext.getCmp('diff_depot_id').getValue());
 			var msg = Show();
			this.fileForm.getForm().submit({
				url:'resource/Device!addFileDeviceDiffence.action?fileType='+flag,
//				waitTitle:'提示',
//				waitMsg:'正在上传中,请稍后...',
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
							},this);
						}
					}
				},  
				failure : function(form, action) {  
					alert(MSG_LU.fileUploadFailure);  
				}
			});
		}
	}
});







DifferenceManage = Ext.extend(Ext.Panel,{
	constructor:function(){
		var differenceGrid = new DifferenceGrid();
		queryDeviceGrid = new QueryDeviceGrid();
		DifferenceManage.superclass.constructor.call(this,{
			id:'DifferenceManage',
			title:DIFF_LU._title,
			closable: true,
			border : false ,
			layout:'border',
			baseCls: "x-plain",
			items:[differenceGrid]
		});
	}
});


