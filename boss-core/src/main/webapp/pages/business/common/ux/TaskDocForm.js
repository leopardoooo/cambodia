

/**
 * 工单明细类型Record定义
 */
TaskDetailType = Ext.data.Record.create([
	{name: 'detail_type_id' },	
	{name: 'detail_type_name' },	
	{name: 'task_type_id' },	
	{name: 'terminal_cal_type' },	
	{name: 'can_add_manual' },	
	{name: 'is_system_cust' },	
	{name: 'rule_id' },	
	{name: 'remark' }
]);

/**
 * 业务单据Record定义
 */
BusiDoc = Ext.data.Record.create([
	{name: 'doc_type'},
	{name: 'doc_name'}
]);

/************************************
 * 施工单及业务单据表单
 */
TaskDocForm = Ext.extend( BaseForm, {

	taskData: null,
	docData: null,
	bgColor: '#F5F5F5',
	
	constructor: function(taskData, docData){
		
		this.taskData = taskData || [] ;
		this.docData = docData || [];
	
		TaskDocForm.superclass.constructor.call(this, {
			title: '业务单据',
			layout: 'column',
			bodyStyle: "background:"+ this.bgColor +";padding-left: 5px;padding-top: 3px;",
			defaults:{
				border: false,
				defaults:{
					bodyStyle: "background:"+ this.bgColor +";border:none"
				}
			},
			items: [{
				columnWidth: .5
			},{
				columnWidth: .5
			}]
		});  
	},
	getValues:function(){
		var obj = {};
		if(Ext.getCmp('taskIds')){
			var taskIds = Ext.getCmp('taskIds').getValue();
			var arr1 = [];
			for(var i=0;i<taskIds.length;i++){
				arr1.push(taskIds[i].inputValue);
			}
			obj['taskIds'] = arr1;
		}
		
		if(Ext.getCmp('docTypes')){
			var docTypes = Ext.getCmp('docTypes').getValue();
			var arr1 = [];
			for(var i=0;i<docTypes.length;i++){
				arr1.push(docTypes[i].inputValue);
			}
			obj['docTypes'] = arr1;
		}
		
		return obj;
	},
	doInit: function(){
		//施工单生成
		var taskItems = [],taskGroup;
		for(var i=0; i< this.taskData.length; i++){
			taskItems[i] = this.gTaskItem( new TaskDetailType( this.taskData[i] ));
		}
		taskGroup = this.gGroup('taskIds', taskItems);
		// 业务单据生成

		var docItems = [], docGroup;
		for (var i = 0; i < this.docData.length; i++) {
			docItems[i] = this.gDocItem(new BusiDoc(this.docData[i]));;
		}
		docGroup = this.gGroup('docTypes', docItems);
		this.items.get(0).add(taskGroup);
		this.items.get(1).add(docGroup);
		this.doLayout();
	},
	gGroup: function( id,items){
		var o = {};
		if(items.length == 0){
			//o["height"] = BOTTOM_HEIGHT;
			//o["html"] = "<p><b>没有符合的数据!</b></p>";
		}else{
			o["items"] = [{
				id : id,
				xtype: 'checkboxgroup',
            	columns: 1,
            	items: items
			}]
		}
		return o;
	},
	gTaskItem: function( r ){
		return {
			boxLabel: r.get('detail_type_name'), 
			name: 'taskIds', 
			inputValue: r.get('detail_type_id'),
			checked: true
		};
	},
	gDocItem: function( r ){
		return {
			boxLabel: r.get('doc_name'), 
			name: 'docTypes', 
			inputValue: r.get('doc_type'),
			checked: true
		};
	}
});