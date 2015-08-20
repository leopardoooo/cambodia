/************************************
 * 施工单表单
 */
WorkForm = Ext.extend( BaseForm, {
	constructor: function(workData,docForm, allowTitle){
		//call superclass constructor
		WorkForm.superclass.constructor.call(this, {
			title: '工单' ,
			layout: 'form',
			labelAlign: 'top',
			bodyStyle: 'padding: 10px;',
			items: [{
				xtype: 'radiogroup',
	            fieldLabel: '派单方式',
	            anchor: '100%',
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
		obj["workBillAsignType"] = Ext.getCmp("radioAssignWay").getValue().inputValue;
		return obj;
	}
});