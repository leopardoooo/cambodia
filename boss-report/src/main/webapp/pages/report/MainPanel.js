/**
 * 查询分页
 * @class MainPanel
 * @extends Ext.Panel
 */
MainPanel = Ext.extend(Ext.Panel,{
	queryPanel:null,
	constructor : function(rep_id,rep_name){
		this.queryPanel = new QueryPanel(rep_id,rep_name);
		this.queryPanel.expand();
		MainPanel.superclass.constructor.call(this,{
			id :  rep_id+'panelId',
	   		baseCls: "x-plain",
	   		layout : 'fit',
	   		items : [this.queryPanel]
		})}
});