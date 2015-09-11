/**
 * 处理中工单
 */
var EndTaskGrid = Ext.extend(TaskBaseForm, {
	taskStore : null,
	acceptSm:new Ext.grid.CheckboxSelectionModel({}),
	constructor : function() {
		this.doInit();
		this.taskStore = new Ext.data.JsonStore({
				url:root+'/config/Task!queryWaitAcceptTask.action',
				fields:['work_id','cust_id','cust_no','task_type','tel','create_type','task_type_text',
				'task_status','task_status_text','books_optr','books_time','books_optr_text','task_cust_name','create_time',
				"assign_dept","assign_dept_text","installer_dept_text","installer_dept","installer_optr",
				"assign_optr","assign_optr_text","busi_name","installer_optr_text",
				"busi_code","install_addr","old_addr"
				,"remark"],
				root : 'records',
				totalProperty : 'totalProperty',
				autoDestroy : true
		});
		
    	// create the Grid
		EndTaskGrid.superclass.constructor.call(this, {
				id : 'EndTaskGridId',
				region : 'center',
				store: this.taskStore,
		        cm: new Ext.ux.grid.LockingColumnModel({
		        	columns:[
		        	this.acceptSm,
					//{header: '工单编号',		dataIndex : 'task_id', 				width: 100},
					{header: langUtils.sys('WorkTask.formWin.orderDate'), 		dataIndex: 'books_time', width: 140 },
					{header: langUtils.sys('WorkTask.formLabels.contactPerson'), 		dataIndex: 'task_cust_name', 				width: 80},
					{header: langUtils.sys('WorkTask.formLabels.contactPhone'), 	dataIndex : 'tel', 				width: 100},
					{header: langUtils.sys('WorkTask.formLabels.labelAddress'), 		dataIndex : 'install_addr', 			width: 200},
					{header: langUtils.sys('WorkTask.formLabels.workTaskType'),		dataIndex : 'task_type_text', 	width: 100, renderer: function(v, m ,rs){
						return "<span style='font-weight: bold;'>"+ v +"</span>";
					}},
					{header: langUtils.sys('WorkTask.formLabels.workTaskStatus'), 		dataIndex: 'task_status', width: 100, renderer: function(v, m ,rs){
						var text = rs.get("task_status_text");
						var color = "black";
						if(v == 'INIT'){
							color = "purple";
						}else if(v == 'INSTALL'){
							color = "orange";
						}else if(v == 'END'){
							color = "green";
						}else if(v == 'CANCEL'){
							color = "gray";
						}
						return "<span style='font-weight: bold;color: "+ color +";'>"+ text +"</span>";
					}},				
					{header : langUtils.sys('common.doActionBtn'), menuDisabled : true, locked: true, dataIndex:'xxx', width : 120,scope:this,
					 renderer:function(v,m,rs,rIndex,cIndex,store){
					 	var btns = this.doFilterBtns('EndTaskGridId',rs);
	            		return btns;
					}},
					{header: langUtils.sys('WorkTask.formLabels.createTime'), 		dataIndex: 'create_time', 		width: 140,renderer:Ext.util.Format.dateFormat},
					{header: langUtils.sys('WorkTask.formLabels.custBusiName'),	dataIndex : 'cust_no', 				width: 80},
					{header: langUtils.sys('WorkTask.formLabels.businessType'),		dataIndex : 'busi_name', 	width: 100, renderer: function(v, m ,rs){
						return "<span style='font-weight: bold;'>"+ v +"</span>";
					}},
					
					{header: langUtils.sys('WorkTask.formLabels.workDept'), dataIndex: 'assign_dept_text' , width: 100},
					{header: langUtils.sys('WorkTask.formLabels.createOptr'), dataIndex: 'books_optr_text' , width: 100},
					{header:langUtils.sys('WorkTask.formLabels.workRemark'),dataIndex:'remark',width:100,renderer:App.qtipValue}
		        ]}),
		        sm: this.acceptSm,
		        region: 'center',
		        stripeRows: true,
				bbar: new Ext.PagingToolbar({
			        store: this.taskStore, // grid and PagingToolbar using same store
			        pageSize: Constant.DEFAULT_PAGE_SIZE
			    }),
				tbar: [langUtils.sys('WorkTask.formLabels.createDate') + ':',this.createStartDateField,' ',this.createEndDateField,'-',
				this.custNoField,' ',this.newaddrField,' ',this.mobileField,' ','-',this.taskTeamCombo,'-',this.taskDetailTypeCombo,'-',{
					text: langUtils.sys('WorkTask.formWin.btnSearchWorkTask'),
					pressed: true,
					scope: this,
					width: 80,
					handler:  this.doSearchTask
				}]
				
			});
	},
	initComponent : function() {
		EndTaskGrid.superclass.initComponent.call(this);
		
		this.addButton(this.printBtn);
		var comboes = this.findByType("paramcombo");
		//初始化下拉框的数据 
		App.form.initComboData([this.taskDetailTypeCombo]);
	},
	doSearchTask: function(){
		this.doSearchBaseTask('EndTaskGridId','END');		
	},
	doPrintTask:function(){
		this.doPrintBaseTask('EndTaskGridId');
	}
});

EndTaskView = Ext.extend(Ext.Panel, {
			constructor : function() {
				endTaskGrid = new EndTaskGrid();
				EndTaskView.superclass.constructor.call(this, {
							id : 'EndTaskView',
							title : langUtils.sys('WorkTask.formWin.titleFinishedWorkTask'),
							closable : true,
							border : false,
							layout : 'border',
							baseCls : "x-plain",
							items : [endTaskGrid]
						});
			}
		});
