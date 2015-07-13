RefreshCmdForm = Ext.extend(BaseForm,{
	url : Constant.ROOT_PATH + "/core/x/User!RefreshCmd.action",
	constructor : function(){
		RefreshCmdForm.superclass.constructor.call(this,{
			layout:'form',
		    border : false,
		    bodyStyle : "background:#F9F9F9; padding: 10px;padding-top : 20px",
		    items:[{
	            xtype: 'radiogroup',
	            anchor :'100%',
	            id : 'cmdRadiogroup',
	            columns: 2,
	            boxMinHeight : 50,
	            items: [{boxLabel: '终端指令',value : 'TERMINAL', checked: true, name: 'radios'},{boxLabel: '产品指令',value : 'PROD', name: 'radios'}]
	        }]
			
		})
	},
	getValues : function(){
		var all = {};
		all['refreshType'] = Ext.getCmp('cmdRadiogroup').getValue().value;
		return all;
	},
	success : function(){
		App.getApp().main.infoPanel.getCommandInfoPanel().caGrid.remoteRefresh;
	}
})

Ext.onReady(function(){
	var form = new RefreshCmdForm();
	var box = TemplateFactory.gTemplate(form);
});