/**
 * 修改客户等级.
 * @class TransferForm
 * @extends Ext.FormPanel
 */
EditCustLevelForm = Ext.extend( BaseForm , {
	doInit: function(){
		EditCustLevelForm.superclass.doInit.call(this);
		App.form.initComboData(this.findByType('paramcombo'),function(){
			var level = App.getCust().cust_level;
			if(Ext.isEmpty(level) || Ext.isEmpty(level.trim())){
				return;
			}
			var index = this.findByType('paramcombo')[0].store.find('item_value',level);
			if(index > -1){
				this.findByType('paramcombo')[0].store.removeAt(index);
			}
		},this);
	},
	url:  Constant.ROOT_PATH + "/core/x/Cust!editCustLevel.action",
	formerCustLevel:null,
	constructor: function(){
		
		this.formerCustLevel = App.getCust().cust_level_text;
		
		EditCustLevelForm.superclass.constructor.call(this, {
			border:false,
			bodyStyle:'padding-top:10px',
			labelWidth:100,
			items:[{
				xtype:'displayfield',fieldLabel:'原客户级别',
				value:this.formerCustLevel
			},{
				xtype : 'paramcombo',
				fieldLabel:'新客户级别',
				allowBlank : false,
				paramName:'CUST_LEVEL',
				hiddenName:'cust_level',
				width:150,
				listeners: {
					scope: this,
					select: function(combo,record,index){
						
					}
				}
			}]
		});
	},
	success:function(){
		var panel = App.getApp().main.infoPanel;
		panel.getCustPanel().custInfoPanel.remoteRefresh();
		panel.getCustPanel().custDetailTab.propChangeGrid.remoteRefresh();
		panel.getUserPanel().prodGrid.remoteRefresh();
		App.getApp().refreshPanel(App.getApp().getData().currentResource.busicode);
	}
});


/**
 * 入口
 */
Ext.onReady(function(){
	var ecf = new EditCustLevelForm();
	var box = TemplateFactory.gTemplate(ecf);
});
