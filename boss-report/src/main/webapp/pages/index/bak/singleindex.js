/**
 * Author: hh
 * Date:2009.12.25  
 * Action:
 *   采用传统模式，top-left|right的布局方式，Ext布局管理器 。
 *   top  : image 
 *   left : resource tree ,采用异步的tree。而不是一次性加载all 
 * 	 right: main body。
 * version : 3.1 
 */


RepTabPanel = Ext.extend(Ext.TabPanel,{
	constructor : function(rep_id,rep_name){
		RepTabPanel.superclass.constructor.call(this,{
			activeTab: 0,
//			layout : 'fit',
			region : 'center',
			defaults : {
				layout : 'fit'
			},
			items : [{
					title : rep_name,
					id : rep_id+'MainPanel',
					closable: true,
					items : [new MainPanel(rep_id,rep_name)]
					}]
		})
	}
})

//最近更新报表grid
LogGrid = Ext.extend(Ext.grid.GridPanel,{
	LogStore: null,
	isReload: true,
	constructor: function(rep_id){
		this.LogStore = new Ext.data.JsonStore({
			root: 'records',
			url:Constant.ROOT_PATH + "/query/RepDesign!queryLog.action",
			baseParams :{rep_id:rep_id},
			fields: ["rep_id","rep_name","update_type","create_date","optr_login_name","remark"]
		});
		var cm = [
			{header:'报表名称',dataIndex:'rep_name',width:150},
			{header:'类型',dataIndex:'update_type'},
			{header:'更新时间',dataIndex:'create_date'},
			{header:'操作员',dataIndex:'optr_login_name',width:60},
			{header:'备注',dataIndex:'remark'}
		];
		LogGrid.superclass.constructor.call(this,{
			region: 'center',
			store:this.LogStore,
			columns:cm,
			bbar:  new Ext.PagingToolbar({store : this.LogStore})
		})
	},
	refresh:function(){
		this.LogStore.load();
	}
})

	
	
var siglerep_sign=true;
Ext.onReady(function(){
	//去掉文本框验证
	Ext.apply(Ext.form.TextField.prototype,{
		vtype:''
	});
	
	/*gridpanel单元格复制*/
	if  (!Ext.grid.GridView.prototype.templates) {    
	    Ext.grid.GridView.prototype.templates = {};
	}
	Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(    
	     '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,    
	     '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,    
	     '</td>'
	);

	App.page=new RepTabPanel('8111','营业收费日报(操作时间)') ;
	
	var win = new Ext.Window({
	titile:'我的报表',
	width:700,
	height:610,
	layout : 'fit',
	items:[App.page]
	});
	win.show(); 
	App.clearLoadImage();

});
