BatchTariffForm = Ext.extend( BaseForm , {
	url : Constant.ROOT_PATH+"/core/x/User!bacthChangeTariff.action",
	panel:null,
	data : [],//提交数据
	constructor: function(){
		this.panel = new BatchTariffPanel();
		BatchTariffForm.superclass.constructor.call(this,{
			autoScroll:true,
			layout:'border',
            border:false,
            bodyStyle: Constant.TAB_STYLE,
            items:[{
				region:'center',
				layout : 'fit',
				items:[this.panel]
			}]
		});
	},
	getValues: function(){
		var record  = this.panel.getValues();
		var all = {'pordLists':Ext.encode(record)};
		all['newTariffId'] = Ext.getCmp('newTariffNameId').getValue() ;
		return all;
	},
	doValid: function(){
		var records = this.panel.getSelectValues();
		if(Ext.isEmpty(Ext.getCmp('newTariffNameId').getValue())){
			var obj = {};
			obj['msg'] = '新资费不能为空';
			obj['isValid'] = false;
			return obj;
		}
		if(records.length==0){
			var obj = {};
			obj['msg'] = '未选中产品信息';
			obj['isValid'] = false;
			return obj;
		}
		var key = false;
		for(var i=0;i < records.length;i++){
			data = records[i].data;
			if(data['eff_date'] == null){
				key = true;
				break;
			}
		}
		if(key){
			var obj = {};
			obj['msg'] = '生效日期不能为空';
			obj['isValid'] = false;
			return obj;
		}
		return BatchTariffForm.superclass.doValid.call(this);
	},
	success : function(){
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});

Ext.onReady(function(){
	var batchTar = new BatchTariffForm();
	TemplateFactory.gTemplate(batchTar);
})