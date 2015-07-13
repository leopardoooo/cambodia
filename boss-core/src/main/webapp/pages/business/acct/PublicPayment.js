/**
 *公用账目缴费 
*/

PublicPaymentForm = Ext.extend(Ext.Panel,{
	dataArr:null,
	constructor:function(data){
		this.dataArr = data;
		PublicPaymentForm.superclass.constructor.call(this,{
			id:'publicPaymentFormId',
			title:'公用账目缴费',
			border:false,
			bodyStyle:'padding-top:10px',
			layout:'table',
			layoutConfig:{columns:2},
			labelWidth:200,
			defaults:{border:false,layout:'form'},
			items:[
				/*{items:[{id:'tyFeeId',fieldLabel:'金额',xtype:'numberfield',
					allowBlank:false,allowNegative:false,minValue:0.01}]},
				{bodyStyle:'padding-left:80px',items:[{xtype:'button',text:'统一缴费',
					scope:this,handler:this.doPayment}]}*/
			]
		});
	},
	initComponent:function(){
		PublicPaymentForm.superclass.initComponent.call(this);
		
		var arr = [];
		for(var i=0;i<this.dataArr.length;i++){
			var d = this.dataArr[i];
			var acct_id = d['acct_id'];
			var acctitem_id = d['acctitem_id'];
			var invalid_date = d['invalid_date'];
			var prod_sn = d['prod_sn'];
			var tariff_id = d['tariff_id'];
			var str = d['acctitem_name']+'(余额：'+Ext.util.Format.formatFee(d['real_balance'])+')';
			arr.push({items:[
					{name:acct_id+'_'+acctitem_id+'_'+invalid_date+'_'+prod_sn+'_'+tariff_id,xtype:'numberfield',
						allowNegative:false,fieldLabel:str}
					]});
		}
		this.add(arr);
	},
	getValues:function(){
//		if(!this.getForm().isValid())return;
		var comps = this.findByType('numberfield');
		var arr = [];
		Ext.each(comps,function(comp){
			if(comp.getValue()){
				var obj = {};
				var name = comp.name.split('_');
//				var acctId = name[0];
//				var acctItemId = name[1];
//				var invalid_date = name[2];
//				var prod_sn = name[3];
				var fee = comp.getValue();
				obj["fee"] = Ext.util.Format.formatToFen(fee);
				obj["acct_id"] = name[0];
				obj["acctitem_id"] = name[1];
				
				if( !Ext.isEmpty(name[2]) && name[2] != 'null' ){
					obj["invalid_date"] = name[2];
				}
				if( !Ext.isEmpty(name[3]) && name[3] != 'null' ){
					obj["prod_sn"] = name[3];
				}
				if( !Ext.isEmpty(name[4]) && name[4] != 'null' ){
					obj["tariff_id"] = name[4];
				}
				arr.push(obj);
			}
		},this);
		return arr;
	}
});