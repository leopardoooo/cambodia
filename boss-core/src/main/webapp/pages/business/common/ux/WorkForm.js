/************************************
 * 施工单表单
 */
WorkForm = Ext.extend( BaseForm, {
	constructor: function(workData,docForm, allowTitle){
		//call superclass constructor
		WorkForm.superclass.constructor.call(this, {
			title: lbc("common.taskTitle") ,
			layout: 'form',
			labelAlign: 'top',
			bodyStyle: 'padding: 10px;',
			items: [{
				xtype: 'radiogroup',
	            fieldLabel: lbc("common.assignWay"),
	            id: 'radioAssignWay',
//	            vertical: true,
	            allowBlank: false,
	            columns: [120],
	            items: [{
	            	boxLabel: 'CFOCN',
	            	name: 'assignWay',
	            	inputValue: 'CFOCN'
	            },{
	            	boxLabel: 'SUPERNET',
	            	name: 'assignWay',
	            	inputValue: 'SUPERNET'
	            },{
	            	boxLabel: 'CFOCN+SUPERNET',
	            	name: 'assignWay',
	            	inputValue: 'CFOCN+SUPERNET'
	            }]
			}]
		});  
	},
	getValues:function(){
		var obj = {};
		var value = Ext.getCmp("radioAssignWay").getValue();
		if(value)
			obj["workBillAsignType"] = value.inputValue;
		return obj;
	}
});