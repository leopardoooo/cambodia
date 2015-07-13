/**
 * 套餐订购和修改公用js
 * @type 
 */

var cm = [
	{header:'客户名称',dataIndex:'cust_name'},
	{header:'用户类型',dataIndex:'user_type_text'},
	{header:'机顶盒号',dataIndex:'stb_id'},
	{header:'卡号',dataIndex:'card_id'},
	{header:'产品名称',dataIndex:'prod_name'}];
var fds = ['prod_sn','cust_name','user_name','stb_id','card_id','prod_name',"package_sn","package_id",'user_type','user_type_text','prod_id','max_prod_count'];

var blankRecord =  Ext.data.Record.create(fds);


var selectedProdGrid =new Ext.grid.GridPanel({
	title:'已选产品',
	store: new Ext.data.JsonStore({
				sortInfo: {field: 'stb_id', direction: 'ASC'},
				fields : fds
			}),
	columns:cm,
	border:false,
	enableDragDrop:true,
	ddGroup:'firstGridDDGroup',
	stripeRows: true,
	sm:new Ext.grid.RowSelectionModel(),
	viewConfig:{forceFit:true}
})

var prodStore =  new Ext.data.JsonStore({
	url:Constant.ROOT_PATH + "/core/x/Cust!queryProdForPkg.action",
	sortInfo: {field: 'stb_id', direction: 'ASC'},
	fields : fds
}); 

var selectableProdGrid =new Ext.grid.GridPanel({
	title:'可选产品',
	store: prodStore,
	columns:cm,
	border:false,
	enableDragDrop:true,
	ddGroup:'secondGridDDGroup',
	stripeRows: true,
	sm:new Ext.grid.RowSelectionModel(),
	viewConfig:{forceFit:true}
})
