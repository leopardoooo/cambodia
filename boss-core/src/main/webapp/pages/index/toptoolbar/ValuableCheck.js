/**
 * 【充值卡退卡】
 */			
var ValuableCheckForm = Ext.extend(BaseForm,{
	url : root + '/commons/x/QueryDevice!removeValuableCard.action',
	cardStore:null,
	fee:null,
	valuableCheckGrid:null,
	constructor:function(){
		ValuableCheckThat = this;
		this.cardStore = new Ext.data.JsonStore({
				root : 'records' ,
				fields: ['valuable_id','amount','cust_name']
		}); 
		this.cardStore.add(App.getApp().main.valuableCardGrid.dataStore);
			
		this.cardStore.each(function(record){
			ValuableCheckThat.fee += record.get('amount')/100;
			App.getApp().main.valuableCardGrid.custName = record.get('cust_name');
		})
				
		var cm = [
			{header:'充值卡',dataIndex:'valuable_id',width:100},
			{header:'金额',dataIndex:'amount',	width:100,renderer:Ext.util.Format.formatFee},
			{header:'客户名称',dataIndex:'cust_name',width:100}
		]				
		this.valuableCheckGrid = new Ext.grid.GridPanel({
				store:this.cardStore,
				border : false,
				columns: cm
		});
		ValuableCheckForm.superclass.constructor.call(this,{
			border : false,
			layout : 'border',
			id:'valuableCheckId',
			items:[{region : 'center',
					layout : 'fit',
					items:[this.valuableCheckGrid]
			}]
		})
	},getValues:function(){
		var  all = {};datas = [];data=null;
		var store = Ext.getCmp('valuableCheckId').valuableCheckGrid.getStore();
		if(store.getCount() ==0){Alert("无数据!"); return ;}
		store.each(function(record){
				datas.push(record.get('valuable_id'));
			},this);
			data=datas.join(",");
			all['valuable_id'] = data;			
		return all;
	},	
	success: function(form, res){
		App.getApp().main.valuableCardGrid.getStore().load();
	},getFee:function(){
		return -this.fee;
	}
})

Ext.onReady(function(){
	var panel = new ValuableCheckForm();
	var box = TemplateFactory.gTemplate(panel);
});