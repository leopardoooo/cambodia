/**
*银行转账打印
*/

BankPrintPanel = Ext.extend( PrintPanel , {
 	
 	//构造函数
 	constructor: function(){
 		Ext.apply(this,{
 			paytype:'银行卡扣',
			printGridColumns:[{ header: '流水号', dataIndex: 'done_code'},
				{ header: '单据名称', dataIndex: 'doc_name', width: 120},
				{ header: '操作员名称', dataIndex: 'optr_name', width: 120},
				{ header: '创建时间', dataIndex: 'done_date', width: 110}]
 		});
 		BankPrintPanel.superclass.constructor.call(this,{
 			layout: 'fit',
 			border: false,
 			items: this.printGrid,
 			buttons:[
 				{
 				text: '打印',
 				scope: this,
 				width: 60,
 				iconCls:'print',
 				handler: function(){
 					this.actionProcesser("doPrint");
 				}
 			},{
				text: '关闭',
				scope: this,
				iconCls:'icon-comments_remove',
				width: 60,
				handler: this.doClose
			}]
 		});
 		
 	}
 });