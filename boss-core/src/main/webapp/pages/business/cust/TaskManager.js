/**工单管理*/

/**
 * 修改工单
 */
TaskManager_update = function(){
	
	var rs = Ext.getCmp('taskManagerPanelId').grid.getSelectionModel().getSelected();
	if(!rs){
		Alert("请选择一条需要修改的工单");
	}
	
	var oldDate = rs.get("books_time");
	var tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);

	this.bugCauseCombo = new Ext.ux.ParamCombo({
		fieldLabel:'故障类型',
		xtype:'paramcombo',
		allowBlank:false,
		hiddenName:'bug_cause',
		anchor: '95%',
		paramName:'TASK_BUG_CAUSE'
	});
	
	var form = new Ext.form.FormPanel({
		labelWidth: 90,
		bodyStyle: 'padding-top: 10px;',
		items: [{
				xtype:'textfield',
				name: 'task_cust_name',
				value: rs.get("task_cust_name"),
				fieldLabel: '联系人'
			},{
				xtype:'textfield',
				name: 'tel',
				value: rs.get("tel"),
				fieldLabel: '联系电话'
			},{
				xtype : 'xdatetime',
				fieldLabel : '预约时间',
				width : 160,
				name:'books_time',
				minText : '不能选择当日之前',
				timeWidth : 60,
				timeFormat : 'H:i',
				value: rs.get("books_time"),			
				timeConfig : {
					increment : 60,
					editable:true,
					altFormats : 'H:i|H:i:s',
					minValue:'04:00',  
					value: Ext.util.Format.date(oldDate, 'H:i'),
					maxValue:'21:00' 
				},
				dateFormat : 'Y-m-d'
				,dateConfig : {
					value: Ext.util.Format.date(oldDate, 'Y-m-d'),
					altFormats : 'Y-m-d|Y-n-d'
				}
			},{
				xtype:'textarea',
				name: 'remark',
				height:40,
				value: rs.get("remark"),
				fieldLabel: '备注'
			}]
	});
	//初始化下拉框的数据 
	App.form.initComboData([this.bugCauseCombo],function(){
		if(rs.get("task_type") == 'WX'){
			form.add(this.bugCauseCombo);
			this.bugCauseCombo.setValue(rs.get("bug_cause"));
			form.doLayout();			
		}
	},this);
	var win = new Ext.Window({
		width: 320,
		height: 360,
		title: '变更',
		border: false,
		closeAction:'close',
		layout: 'fit',
		items: form,
		buttons: [{
			text: '保存',
			scope: this,
			iconCls : 'icon-save',
			handler: function(){
				var url = Constant.ROOT_PATH + "/core/x/Task!modifyTask.action";
				var o = {
					task_id : rs.get("work_id"), 
					booksTime: form.getForm().getValues()["books_time"],
					taskCustName: form.getForm().getValues()["task_cust_name"], 
					tel: form.getForm().getValues()["tel"], 
					remark: form.getForm().getValues()["remark"],
					bugCause : form.getForm().getValues()["bug_cause"]
				};
				App.sendRequest( url, o, function(res,opt){
					rs.set("books_time", o.booksTime);
					rs.set("task_cust_name", o.taskCustName);
					rs.set("tel", o.tel);
					rs.set("remark", o.remark);	
					if(o.bugCause != null){
						rs.set("bug_cause",o.bugCause);
						var index = 0;
						index = this.bugCauseCombo.getStore().find('item_value',o.bugCause);
						rs.set("bug_cause_text",this.bugCauseCombo.getStore().getAt(index).get('item_name'));
					}
					win.close();
				});
			}
		},{
			text: '取消',
			handler: function(){
				win.hide();
			}
		}]
	});
	win.show();
}


/**
 * 用于显示多个客户的信息，及选择要办理业务的客户
 */
TaskCustSelectWin = Ext.extend( Ext.Window , {
	//客户信息的store
	custStore: null,
	custGrid: null,
	parent: null,
	constructor: function (store,parent){
		this.custStore =  store;
		this.parent = parent;
		var cm = [
			{header: '受理编号', dataIndex: 'cust_no'},
			{header: '客户名称', dataIndex: 'cust_name'},
			{header: '客户地址', dataIndex: 'addr_id_text', width: 240,
				renderer: function(value,md,record){
					value = record.get('address');
					return value;
				}},
			{header: '网络类型', dataIndex: 'net_type_text',width: 80},
			{header: '客户状态', dataIndex: 'status_text',width: 80},
			{header: '客户类型', dataIndex: 'cust_type_text',width: 80},
			{header: '客户级别', dataIndex: 'cust_level_text',width: 80},
			{header: '黑名单', dataIndex: 'is_black_text',width: 60},
			{id: 'autoCol', header: '开户时间', dataIndex: 'open_time'}
		];
		
		//实例化cust grid panel
		this.custGrid = new Ext.grid.GridPanel({
			stripeRows: true, 
	        store: this.custStore,
	        columns: cm,
	        bbar: new Ext.PagingToolbar({
	        	pageSize: 10,
				store: this.custStore
			})
	    })
		TaskCustSelectWin.superclass.constructor.call(this,{
			title: '选择客户',
			maximizable : false,
			width: 520,
			height: 400,
			layout: 'fit',
			border: false,
			closeAction : 'close',
			items: this.custGrid
		});
	},
	//注册事件
	initEvents: function(){
		this.custGrid.on("rowclick", function(grid ,index, e){
//			Confirm("确定要选择该客户吗?", this ,function(){
			var record = grid.getStore().getAt(index);
			Ext.getCmp('taskCustInfoPanel').remoteRefresh(record.get('cust_id'));
			this.close();
//			});
		}, this);
		TaskCustSelectWin.superclass.initEvents.call(this);
	}
});

CustInfo = Ext.extend( Ext.Panel , {
	id : 'taskCustInfoPanel',
	tpl: null,
	infoCustId:null,
	custData: {
 		// 客户信息 @type Object
 		cust: {},
 		// 联系人信息 @type Object
 		linkman: {},
 		//积分信息
 		bonuspoint: {},
 		//签约信息
 		acctBank : {}
 	}, 
	constructor: function(){
		this.tpl = App.getApp().main.infoPanel.getCustPanel().custInfoPanel.tpl;
		this.tpl.compile();
		CustInfo.superclass.constructor.call(this, {
			border: false,
			autoScroll:true,
			bodyStyle : Constant.TAB_STYLE,
			html: this.tpl.applyTemplate(this.custData)
		});
	},
	refresh: function(){
		if(this.custData.cust.cust_id == App.getData().custFullInfo.cust.cust_id){
			Alert("您查找的客户没变，请选择其他客户");
			return;
		}
		this.tpl.overwrite( this.body, this.custData);
	},
	remoteRefresh:function(custId){
		if(custId){
			Ext.Ajax.request({
				scope : this,
				url:  root + '/commons/x/QueryCust!searchCustById.action',
				params: { 
					"custId" : custId
				},
				success: function(res,ops){
					var data = Ext.decode(res.responseText);
					
					if(data){
						this.infoCustId = custId;
						this.custData = data;
						var linkman="", mobile = "";
						linkman = data.cust.cust_name;
						mobile = data.linkman.mobile;
						if(!mobile || mobile == ""){
							mobile = data.linkman.tel;
						}
						if(!Ext.isEmpty(linkman)){
							Ext.getCmp('taskCustNameId').setValue(linkman);
						}
						if(!Ext.isEmpty(mobile)){
							Ext.getCmp('telId').setValue(mobile);
						}
						
					}else{
						this.reset();
					}
					
					this.refresh();
	  			 }
			});
		}else{
			this.reset();
			this.refresh();
		}
		
	},
	reset : function(){
		this.infoCustId = null;
		this.custData.cust = {};
		this.custData.linkman = {};
		this.custData.acctBank = {};
	}
});


TaskAddPanel = Ext.extend(Ext.FormPanel,{
	custStore : null,
	custWin : null,
	taskTypeStore:null,
	constructor: function(){
		this.custStore = new Ext.data.JsonStore({
			url: root + '/commons/x/QueryCust!searchCust.action',
			root: 'records',
			totalProperty: 'totalProperty',
			fields: Ext.data.Record.create([
				"cust_id",
				"cust_no",
				"cust_name",
				"addr_id_text",
				"address",
				"status",
				"cust_type",
				"status_text",
				"cust_type_text",
				"cust_level_text",
				"net_type_text",
				"is_black_text",
				"open_time",
				"county_id",
				"county_name",
				"area_id"
			])
		});
		this.custStore.on('load',this.doLoadResult,this);
		
		this.taskTypeStore = new Ext.data.JsonStore({
            url: root + "/core/x/Task!getTaskType.action",
            fields: ['detail_type_id', 'detail_type_name']
        });
        this.taskTypeStore.load();
		var type_store = new Ext.data.SimpleStore({
			fields:['value', 'text'],
			data: [
				['cust_no','受理编号'],
				['cust_name','客户名称'],
				['device_id','设备编号']
			]
		});
		
		TaskAddPanel.superclass.constructor.call(this, {
			trackResetOnLoad:true,
			border : false,
			labelWidth: 65,
			layout:'border',
			baseCls: 'x-plain',
			bodyStyle: Constant.TAB_STYLE,
			items: [{
				xtype:'panel',
				region:'east',
				title:'工单信息',
				width:250,
				bodyStyle: Constant.FORM_STYLE,
				layout : 'form',
				items:[{
					fieldLabel: '工单名称',
                    xtype: 'combo',
                    store: this.taskTypeStore,
                    emptyText: '请选择...',
                    mode: 'local',
                    editable: false,
                    id: 'ruleNameId',
                    valueField: 'detail_type_id',
                    displayField: 'detail_type_name',
                    hiddenName: 'detail_type_id',
					listeners:{
						scope:this,
						select : this.setBugCause
					}
					
//					fieldLabel:'工单名称',
//					xtype:'paramcombo',
//					allowBlank:false,
//					hiddenName:'task_type',
//					paramName:'TASK_DETAIL_TYPE',
//					defaultValue:'WX',
					
				},{
					fieldLabel:'故障类型',
					xtype:'paramcombo',
					allowBlank:false,
					anchor: '95%',
					hiddenName:'bug_cause',
					paramName:'TASK_BUG_CAUSE'
				},{
					xtype:'textfield',
					allowBlank:false,
					name: 'task_cust_name',
					id:'taskCustNameId',
					fieldLabel: '联系人'
				},{
					xtype:'textfield',
					name: 'tel',
					id:'telId',
					allowBlank:false,
					fieldLabel: '联系电话'
				},{
					xtype : 'xdatetime',
					fieldLabel : '预约时间',
					width : 160,
					allowBlank:false,
					name:'books_time',
					minText : '不能选择当日之前',
					timeWidth : 60,
					timeFormat : 'H:i',
					timeConfig : {
						increment : 60,
						editable:true,
						altFormats : 'H:i|H:i:s',
						minValue:'04:00',  
						maxValue:'21:00' 
					},
					dateFormat : 'Y-m-d'
					,dateConfig : {
						altFormats : 'Y-m-d|Y-n-d'
					}
				},{
					xtype:'textarea',
					name: 'remark',
					height:40,
					width : 140,
					fieldLabel: '备注'
				}]
			},{
				xtype : 'panel',
				region : 'center',	
				layout : 'fit',
				title:'客户信息',
				autoScroll:true,
				bodyStyle: "background:#F9F9F9",
				items : [new CustInfo()],
				tbar : [' ',
					new Ext.form.ComboBox({
						id : 'searchType',
						store : type_store,
						width:100,
						hiddenName : 'value',
						modal : 'local',
						valueField: 'value',
						displayField: 'text', 
						value : 'cust_no',
						forceSelection : true,
						allowBlank:false
				}),'-',
					{
						xtype : 'textfield',
						id : 'cust.cust_name',
						allowBlank:false,
						height : 20,
						width:150,
						name:'cust.cust_name',
						listeners :{
							scope : this,
							"specialkey":function(_this,_e){
								if(13 == _e.getKey()){ 
									this.searchCust(this);
								}
							}
						}
				},{
					text : ' 查找客户',
					xtype : 'button',
					height : 20,
					iconCls : "icon-query",
					scope : this,
					handler : this.searchCust
				}]
			}]
		});
	},
	initComponent:function(){
		TaskAddPanel.superclass.initComponent.call(this);
		App.form.initComboData(this.findByType('paramcombo'));
	},
	doLoadResult : function(_store, _rs, ops){
		switch (_rs.length){                                                                                                                                     
			case 0  : 	
						Alert("没有查询到符合条件的客户，请检查条件是否有误!",function(){
							Ext.getCmp('cust.cust_name').focus();
						});
					  	break ;
			case 1  :  
						Ext.getCmp('taskCustInfoPanel').remoteRefresh(_store.getAt(0).get('cust_id'));
					  	break ; 
			default : 
						if(!this.custWin)
							this.custWin = new TaskCustSelectWin( _store,this);
						this.custWin.show();
		}
	},
	searchCust : function(){
		if(!Ext.getCmp('cust.cust_name').getValue()){
			Alert("请输入查询值");
			return;
		}
		var searchType = Ext.getCmp('searchType').getValue();
		var ps = {
			"search_type": searchType,
			"search_value": Ext.getCmp('cust.cust_name').getValue()
		};
		this.custStore.baseParams = ps ;
		
		this.custStore.load({
			params: { start:0, limit: 10}
		});
	},
	setBugCause : function(v){	
		var form = this.getForm();
		var comp = form.findField('bug_cause');
		if(v.value=='WX'){
			comp.allowBlank=false;			
			comp.enable();
		}else{
			comp.allowBlank=true;
			comp.disable();
		}
	},	
	getValues: function(){
		var obj = {},works = [];
		var fvs = this.getForm().getValues();
		works.push(fvs["detail_type_id"])
		obj['taskIds'] = works;
		obj["task_books_time"] = Ext.isEmpty(fvs["books_time"])? null : fvs["books_time"];
		obj["task_cust_name"] = fvs["task_cust_name"];
		obj["task_mobile"] = fvs["tel"];
		obj["task_remark"] = fvs["remark"];
		obj["task_bug_cause"] = fvs["bug_cause"];
		return obj;
	},
	doValid: function(){
		var obj = {};
		if (this.getForm().isValid()){
			obj["isValid"] = true;
		}else{
			obj["isValid"] = false;
			obj["msg"] = "含有验证不通过的输入项";
			return obj;
		}
		
		if(Ext.getCmp('taskCustInfoPanel').infoCustId == null){
			obj["isValid"] = false;
			obj["msg"] = "请指定新建工单的客户";
			return obj;
		}
		
		var books_time = this.getForm().getValues()["books_time"];
		if(books_time== null||books_time==""){
			obj["isValid"] = false;
			obj["msg"] = "请输入预约时间";
			return obj;
		}
		

		return obj;
	}
});


TaskAddWin = Ext.extend(Ext.Window,{
	form:null,
	constructor:function(){
		this.form = new TaskAddPanel(); 
		TaskAddWin.superclass.constructor.call(this,{
			title : '新增工单',
			layout : 'fit',
			height : 400,
			width : 700,
			closeAction : 'close',
			items : [this.form],
			buttons : [{
				text : '保存',
				scope : this,
				iconCls : 'icon-save',
				handler : this.doSave
			}, {
				text : '关闭',
				scope : this,
				handler : function() {
					this.close();
				}
			}]
			
		})
	},
	doSave:function(){
		var result = this.form.doValid();
		//不是业务的表单（如杂费）返回的是true/false
		if(result && result != true){
			if(result.isValid == false){
				if(result.msg){
					Alert(result.msg);
				}else{
					Alert("含有验证不通过的输入项!");
				}
				return false;
			}
		}else if(result == false){
			Alert("含有验证不通过的输入项!");
			return false;
		}
		var task = this.form.getValues();
		
		mb = Show();
		var that = this;
		var all = {};
		//获取通用的参数
		var commons = App.getValues();
		Ext.apply(commons, task);
		commons["custFullInfo"] = Ext.getCmp('taskCustInfoPanel').custData;
		commons['busiCode'] = '1100';
		//设置提交参数
		all[CoreConstant.JSON_PARAMS] = Ext.encode(commons);
		//提交
		Ext.Ajax.request({
			scope: this,
			params: all,
			url: Constant.ROOT_PATH + "/core/x/Task!saveNewTask.action",
			success: function( res, ops){
				mb.hide();
				Alert("操作成功", function(){
					that.close();			
				});
			}
		});
	}
})


/**
 * 工单管理
 */
TaskManagerPanel = Ext.extend( Ext.Panel ,{
	taskStore:null,
	grid: null,
	pageSize: 20,
	taskFinishWin: null,
	taskDetailsWin:null,
	iconCls: 'doc',
	title: '工单管理',
	constructor:function(item){
		this.initWidgets(item);		
  		TaskManagerPanel.superclass.constructor.call(this,{
			border: false,
			id: 'taskManagerPanelId',
			layout: 'border',
			items: this.grid,
			buttons: [{
				id:'add_btn_id',
				text: '新增工单',
				disabled:true,
				tooltip: '新增业务单,报修单',
				height: 35,
				width: 80,
				style: 'color: red;',
				scope: this,
				handler: this.addTask
			},{
				id:'ivalid_btn_id',
				text: '作废',
				disabled:true,
				tooltip: '作废选择的施工单',
				height: 35,
				width: 80,
				style: 'color: red;',
				scope: this,
				handler: this.cancelTask
			},{
				id:'send_btn_id',
				text: '提交呼叫',
				disabled:true,
				width: 80,
				tooltip: '工单传给呼叫',
				height: 35,
				scope: this,
				handler: this.assignTask
			}]
		});
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
	initEvents: function(){
		this.on("afterrender",function(){
			this.swapViews.call(this.grid);
		},this,{delay:10});
		
		TaskManagerPanel.superclass.initEvents.call(this);
	},
	initWidgets: function(item){
		this.taskStore = new Ext.data.JsonStore({
			url: root + '/core/x/Task!queryTasks.action' ,
			fields:['work_id','cust_id','cust_no','task_cust_name','tel','install_addr','task_type',
					'task_status','task_status_text','task_type_text','task_finish_type','books_time',{
						name: 'create_time',
						type: 'date',
						dateFormat: 'Y-m-d H:i:s'
					},{
						name: 'installer_time',
						type: 'date',
						dateFormat: 'Y-m-d H:i:s'
					},"books_optr","books_optr_text","busi_code","busi_name","bug_cause","bug_cause_text","remark"],
			root : 'records',
			totalProperty : 'totalProperty',
			autoDestroy : true
		});
		
		var tomorrow = new Date();
		tomorrow.setDate(tomorrow.getDate() + 1);
		var startDate = new Date();
		startDate.setDate(startDate.getDate() - 1);
//		startDate.setFullYear(startDate.getFullYear()-5);
		//create search field
		this.createStartDateField = new Ext.form.DateField({
				xtype: 'datefield',
				width: 90,
				format: 'Y-m-d',
				allowBlank: false,
				value: startDate
			});
		this.createEndDateField = new Ext.form.DateField({
				xtype: 'datefield',
				width: 90,
				format: 'Y-m-d',
				allowBlank: false,
				value: tomorrow
			});
		this.custNoField = new Ext.form.TextField({
				xtype: 'textfield',
				width: 90,
				emptyText: '受理编号'
			});
		this.mobileField = new Ext.form.TextField({
				xtype: 'textfield',
				width: 80,
				emptyText: '联系电话'
			});
		this.newaddrField = new Ext.form.TextField({
				xtype: 'textfield',
				width: 100,
				emptyText: '地址'
			});
		this.taskStatusCombo = new Ext.ux.ParamCombo({
				xtype: 'textfield',
				width: 70,
				emptyText: '工单状态',
				paramName:'TASK_STATUS',
				allowBlankItem: true
			});		
		
		this.taskDetailTypeCombo = new Ext.ux.ParamCombo({
				xtype: 'textfield',
				width: 80,
				emptyText: '工单类型',
				paramName:'TASK_DETAIL_TYPE',
				allowBlankItem: true
		});
		
		//初始化下拉框的数据 
		App.form.initComboData([this.taskStatusCombo,this.taskDetailTypeCombo]);
		
    	// create the Grid
    	var sm = new Ext.grid.CheckboxSelectionModel();
	    this.grid = new Ext.grid.GridPanel({
	        store: this.taskStore,
	        cm: new Ext.ux.grid.LockingColumnModel({
	        	columns:[
	        	sm,
				//{header: '工单编号',		dataIndex : 'work_id', 				width: 100},
				{header: '创建时间', 		dataIndex: 'create_time', 		width: 165, renderer: Ext.util.Format.dateRenderer("Y年m月d日H点i分s秒")},
				{header: '预约时间', 		dataIndex: 'books_time', width: 140},
				{header: '客户受理号',	dataIndex : 'cust_no', 				width: 80},
				{header: '联系人', 		dataIndex: 'task_cust_name', 				width: 80},
				{header: '联系电话', 	dataIndex : 'tel', 				width: 100},
				{header: '地址', 		dataIndex : 'install_addr', 			width: 200},
				{header: '业务名称',		dataIndex : 'busi_name', 	width: 100, renderer: function(v, m ,rs){
					return "<span style='font-weight: bold;'>"+ v +"</span>";
				}},
				
				{header: '工单类型',		dataIndex : 'task_type_text', 	width: 100, renderer: function(v, m ,rs){
					return "<span style='font-weight: bold;'>"+ v +"</span>";
				}},
				{header:'故障原因',dataIndex:'bug_cause_text',width:85},
				{header: '工单状态', 		dataIndex: 'task_status', width: 100, renderer: function(v, m ,rs){
					var text = rs.get("task_status_text");
					var color = "black";
					if(v == 'INIT'){
						color = "purple";
					}else if(v == 'CREATE'){
						color = "orange";
					}else if(v == 'COMPLETE'){
						color = "green";
					}else if(v == 'CANCEL'){
						color = "gray";
					}
					return "<span style='font-weight: bold;color: "+ color +";'>"+ text +"</span>";
				}},							
				{header : "操作", menuDisabled : true, locked: true, dataIndex:'xxx', width : 80,
				 renderer:function(v,m,rs,rIndex,cIndex,store){
				 	var optrHtml = "", status = rs.get("task_status");
				 	if(status == 'CREATE'){
						optrHtml = "<a href='#' style='color: blue;font-weight: bold;' id='TaskManager_update_btn' onclick='TaskManager_update();' title='修改工单信息'>修改</a> ";
					}
					return optrHtml;
				}},
				{header:'施工备注',dataIndex:'remark',width:100,renderer:App.qtipValue}
	        ]}),
	        view: new Ext.ux.grid.ColumnLockBufferView({
	        	getRowClass: function(record,index){
		            if(Ext.util.Format.date(record.get('books_time'), 'Y-m-d') < nowDate().format('Y-m-d') && record.get('task_status')=='CREATE' ){ 
		                return 'red-row';  
	                }
	                return '';  
		        }  
	        }),
	        sm: sm,
	        region: 'center',
	        stripeRows: true,
			bbar: new Ext.PagingToolbar({
		        store: this.taskStore, // grid and PagingToolbar using same store
		        pageSize: this.pageSize
		    }),
			tbar: ['创建时间:',this.createStartDateField,' ',this.createEndDateField,'-',
			this.custNoField,' ',this.newaddrField,' ',this.mobileField,' ',this.taskStatusCombo,'-','-',this.taskDetailTypeCombo,'-',{
				text: '搜索工单',
				pressed: true,
				scope: this,
				width: 80,
				handler: this.doSearchTask
			}]
	    });
	},
	createStartDateField: null,
	createEndDateField: null,
	custNoField: null,
	taskStatusCombo: null,
	mobileField: null,
	newaddrField: null,
	cancelTask: function(){
		var rs = this.getSelections();
		if(rs === false){
			return ;
		}
		var taskIds = [];
		for(var i = 0; i< rs.length ; i++){
			var ts = rs[i].get("task_status");
			if(ts == 'COMPLETE' || ts == 'CANCEL' || ts == 'INIT'){
				Alert("不允许作废!");
				return ;
			}
			taskIds.push(rs[i].get("work_id"));
		}
		Ext.MessageBox.prompt("备注","作废原因：",function(bu,txt){
			if(bu != 'cancel'){
	   			Confirm("确定要作废选中的工单吗?", this , function(){
					App.sendRequest(
						Constant.ROOT_PATH + "/core/x/Task!cancelTask.action",
						{task_ids : taskIds,cancelRemark:txt},
						function(res,opt){
							Alert('工单已作废!');
							for(var i = 0; i< rs.length ; i++){
								rs[i].set("task_status", "CANCEL");
								rs[i].set("task_status_text", "作废");
								rs[i].set("remark", txt);
							}
						});
				}); 
			}
		}); 
		
		
	},
	assignTask: function(){
		var rs = this.getSelections();
		if(rs === false){
			return ;
		}
		var taskIds = [];
		for(var i = 0; i< rs.length ; i++){
			if(rs[i].get("task_status") != 'CREATE'){
				Alert("只有新建的工单才能提交呼叫!");
				return ;
			}
			taskIds.push(rs[i].get("work_id"));
		}
		
		var that = this;
		Confirm("确定要提交呼叫吗?", this , function(){
			App.sendRequest(
				Constant.ROOT_PATH + "/core/x/Task!assignTask.action",
				{task_ids : taskIds},
				function(res,opt){
					Alert('提交成功!', function(){
						that.close();
					});
					that.doSearchTask();					
				});
		});
	},
	addTask:function(){
		var w  = new TaskAddWin();
		w.show();
		
	},
	getSelections: function(allowMoreRows){
		var rs = this.grid.getSelectionModel().getSelections();
		if(rs.length == 0){
			Alert("请选择需要操作的记录!");
			return false;
		}else if(rs.length > 1 && allowMoreRows === false){
			Alert("仅能选择一条记录进行操作!");
			return false;
		}
		return rs;
	},
	//search task from remote
	doSearchTask: function(){
		var o = {
			"taskCond.startTime": Ext.util.Format.date(this.createStartDateField.getValue(), "Y/m/d"),
			"taskCond.EndTime": Ext.util.Format.date(this.createEndDateField.getValue(), "Y/m/d"),
			"taskCond.custNo": this.custNoField.getValue(),
			"taskCond.status": this.taskStatusCombo.getValue(),
			"taskCond.mobile": this.mobileField.getValue(),
			"taskCond.newAddr": this.newaddrField.getValue(),
			"taskCond.taskDetailType":this.taskDetailTypeCombo.getValue()
		};
		this.taskStore.baseParams = o;
		this.taskStore.load({
			params: {
				"start": 0, 
				"limit": this.pageSize
			}
		});
	}
});		
