var feeHtmlArr = [
'<div id="printBillDivId">',
'<table id="printBillTabId" width="100%" border="0" cellspacing="0" cellpadding="0">',
  '<tr>',
  	'<td colspan="4"><p align="center">账单</div></td>',
  '</tr>',
  '<tr>',
  	'<td colspan="4">&nbsp;</td>',
  '</tr>',
  '<tr>' ,
  	'<td width="22%"><p align="left">&nbsp;&nbsp;&nbsp;&nbsp;客户受理编号：</p></td>',
  	'<td>','{[values.cust_no]}','</td>',
  	'<td width="22%"><p align="left">&nbsp;&nbsp;&nbsp;&nbsp;账单统计区间：</p></td>',
  	'<td>{[values.firstDateOfMonth]} ~ {[values.lastDateOfMonth]}</td>',
  '</tr>',
  '<tr>' ,
  	'<td><p align="left">&nbsp;&nbsp;&nbsp;&nbsp;客户姓名：</p></td>',
  	'<td>{[values.cust_name]}</td>',
  	
  	'<td><p align="left">&nbsp;&nbsp;&nbsp;&nbsp;账单打印日期：</p></td>',
  	'<td>{[values.now]}</td>',
  	
  '</tr>',
  '<tr>' ,
  	'<td><p align="left">&nbsp;&nbsp;&nbsp;&nbsp;客户地址：</p></td>',
  	'<td colspan="3">{[values.address]}</td>',
  '</tr>',
//'</table>',
  '<tr><td colspan="4">',
		'<table id="printBillTabId2" style="border-right:0;border-bottom:0" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#C5A2F3">',
		  	'<tr>' ,
		  		'<td colspan="3"><div align="center">费用信息</div></td>',
		  	'</tr>',
		  	'<tr>' ,
			  	'<td width="40%"><p align="left">&nbsp;用户</p></td>' ,
			  	'<td width="40%"><p align="left">&nbsp;项目名称</p></td>' ,
			  	'<td><p align="left">&nbsp;金额(元)</p></td>' ,
		  	'</tr>',
		  	
		  	'<tpl for="values.billList">',
		  		'<tr>' ,
		  			'<td ',
			  			'<tpl if="Ext.isEmpty(values.user_id)">',
			  				'style="border-top:0;',
			  				'<tpl if="xindex != xcount">',
			  					'border-bottom:0;',
			  				'</tpl>',
			  				'">&nbsp;',
			  			'</tpl>',
			  			'<tpl if="!Ext.isEmpty(values.user_id)">',
			  				'style="',
			  				'<tpl if="xindex != 1">',
			  					'border-top:1px solid;',
			  				'</tpl>',
			  				'<tpl if="xindex != xcount">',
			  					'border-bottom:0;',
			  				'</tpl>',
			  				'><div align="center" valign="middle">&nbsp;{user_name}&nbsp;</div>',
			  			'</tpl>',
		  			'</td>',
		  			'<td>&nbsp;{acctitem_name}&nbsp;</td>' ,
		  			'<td>&nbsp;{[Ext.util.Format.formatFee(values.final_bill_fee)]}&nbsp;</td>' ,
		  		'</tr>',
		  	'</tpl>',
		  	
		  	'<tr>' ,
		  		'<td colspan="2" align="right">费用合计&nbsp;</td>' ,
		  		'<td>&nbsp;{[Ext.util.Format.formatFee(values.sum_bill_fee)]}</td>' ,
		  	'</tr>',
	  	'</table>',
'</td></tr>',
'<tr><td colspan="4">',
  	'<table  id="printBillTabId3" style="border-right:0;border-top:0;border-bottom:0" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#C5A2F3">',
  	'<tr>' ,
  		'<td colspan="3"><div align="center">账户信息</div></td>' ,
  	'</tr>',
//  	'<tr>' ,
//  		'<td width="40%"><div align="center">费用名称</div></td>' ,
//  		'<td width="40%"><div align="center">金额(元)</div></td>' ,
//  		'<td><div align="center">备注</div></td>' ,
//  	'</tr>',
  	'<tr>' ,
	  	'<td width="40%" nowrap="nowrap" valign="bottom"><p align="left">&nbsp;本期初结余 </p></td>',
	    '<td width="40%">&nbsp;{[Ext.util.Format.formatFee(values.month_begin_fee)]}</td>',
	    '<td>其他</td>',
    '</tr>',
  	'<tr>' ,
	  	'<td width="21%" nowrap="nowrap" valign="bottom"><p align="left">&nbsp;赠送 </p></td>',
	    '<td>&nbsp;{[Ext.util.Format.formatFee(values.zs_fee)]}</td><td>&nbsp;</td>' ,
    '</tr>',
  	'<tr>' ,
  		'<td width="21%" nowrap="nowrap" valign="bottom"><p align="left">&nbsp;退款 </p></td>',
    	'<td>&nbsp;{[Ext.util.Format.formatFee(values.tk_fee)]}</td><td>&nbsp;</td>' ,
    '</tr>',
    
//    '<tr>' ,
//  		'<td width="21%" nowrap="nowrap" valign="bottom"><p align="left">&nbsp;转账 </p></td>',
//    	'<td>&nbsp;{[Ext.util.Format.formatFee(values.zz_fee)]}</td><td>&nbsp;</td>' ,
//    '</tr>',
//    '<tr>' ,
//  		'<td width="21%" nowrap="nowrap" valign="bottom"><p align="left">&nbsp;调账 </p></td>',
//    	'<td>&nbsp;{[Ext.util.Format.formatFee(values.tz_fee)]}</td><td>&nbsp;</td>' ,
//    '</tr>',
    
  	'<tr>' ,
	  	'<td width="21%" nowrap="nowrap" valign="bottom"><p align="left">&nbsp;本期缴费 </p></td>',
	    '<td>&nbsp;{[Ext.util.Format.formatFee(values.xj_fee)]}</td><td>&nbsp;</td>' ,
    '</tr>',
//    '<tr>' ,
//	  	'<td width="21%" nowrap="nowrap" valign="bottom"><p align="left">&nbsp;本期其他方式缴费 </p></td>',
//	    '<td>&nbsp;{[Ext.util.Format.formatFee(values.other_fee)]}</td><td>&nbsp;</td>' ,
//    '</tr>',
    
  	'<tr>' ,
	  	'<td width="21%" nowrap="nowrap" valign="bottom"><p align="left">&nbsp;银行代扣 </p></td>',
	    '<td>&nbsp;{[Ext.util.Format.formatFee(values.bk_fee)]}</td><td>&nbsp;</td>' ,
	'</tr>',
  	'<tr>' ,
	  	'<td width="21%" nowrap="nowrap" valign="bottom"><p align="left">&nbsp;本期费用合计 </p></td>',
	    '<td>&nbsp;{[Ext.util.Format.formatFee(values.prod_bill_fee)]}</td>',
	    '<td>{[Ext.util.Format.formatFee(values.other_fee)]}</td>' ,
    '</tr>',
  	'<tr>' ,
	  	'<td width="21%" nowrap="nowrap" valign="bottom"><p align="left">&nbsp;本期末结余 </p></td>',
	    '<td>&nbsp;{[Ext.util.Format.formatFee(values.month_end_fee)]}</td><td>&nbsp;</td>' ,
    '</tr>',
'</table>',
'</td></tr></table>',
'</div>'
];

var BillPrintPanel = Ext.extend(Ext.Panel,{
	tpl: null,
	printData: null,
	promStore:null,
	constructor:function(){
		this.tpl = new Ext.XTemplate( feeHtmlArr.join(''));
		
		 this.promStore =   new Ext.data.JsonStore({
				 			url:root+'/commons/x/QueryCust!queryPromFeeByCust.action',
							totalProperty:'totalProperty',
							root:'records',
							fields:['prom_fee_sn','prom_fee_name']
						})
		this.promStore.load({params:{custId: App.getData().custFullInfo.cust.cust_id,start:0,limit:12}});
		BillPrintPanel.superclass.constructor.call(this,{
			border:false,
			autoScroll:true,
			layout:'fit',
			items : [{
					xtype:'form',border:false,bodyStyle:'padding-top:5px',
					items:[
						{
							xtype:'displayfield',
							anchor:'100%',
							value:'查询历史账单，请输入年份(例如：2011) 进行查询'
						},
						{
						id:'billDateId',
						fieldLabel:'账期',
						xtype:'combo',
						store:new Ext.data.JsonStore({
							url:root+'/commons/x/QueryCust!queryAllBillingCycleCfg.action',
							totalProperty:'totalProperty',
							root:'records',
							params:{start:0,limit:12},
							fields:['billing_cycle_id']
						}),displayField:'billing_cycle_id',valueField:'billing_cycle_id',
						forceSelection:true,editable:true,anchor:'80%',
						typeAhead: true,mode:'remote',minChars:4,pageSize:12,
						listeners:{
							scope:this,
							select:this.doBillDateSelect
						}
					},{
							xtype:'displayfield',
							anchor:'100%',
							value:'查询套餐缴费账单'
						},
						{
							id:'promFeeId',
							fieldLabel:'套餐缴费',
							xtype:'combo',
							store:this.promStore,displayField:'prom_fee_name',valueField:'prom_fee_sn',
							forceSelection:true,editable:true,anchor:'80%',
							typeAhead: true,minChars:4,pageSize:12,
							listeners:{
								scope:this,
								select:this.doPromDateSelect
							}
					}]
				},{
					border : false,
					hidden:true,
					bodyStyle: "padding: 10px;padding-top: 4px;padding-bottom: 0px;",//background:#F9F9F9; 
					html : this.tpl.applyTemplate({
						cust_no:'',cust_name:'',address:'',
						firstDateOfMonth:'',lastDateOfMonth:'',
						zs_fee:0,		//赠送金额
						tk_fee:0,		//退款金额
						xj_fee:0,		//现金金额
						other_fee:0,	//其他金额
						zz_fee:0,		//转账金额
						tz_fee:0,		//调账金额
						bk_fee:0,		//银行代扣
						month_begin_fee:0,	//本月初结余
						month_end_fee:0,	//本月末结余
						month_sum_fee:0,	//本月费用合计
						billList:[{user_name:'',acctitem_name:'',final_bill_fee:''}]//费用信息
					})
				}],
			buttons:[
				{id:'printBillId',text:'打印',scope:this,disabled:true,handler: this.doPrint},
				{id:'printBackId',text:'返回',scope:this,disabled:true,handler: function(){window.location.reload();}},
				{text:'关闭',scope:this,handler: this.doClose}
			]
		});
	},
	doBillDateSelect:function(combo,record){
		var value = combo.getValue();
		var msg = Show();
		Ext.Ajax.request({
			url:root+'/commons/x/QueryCust!queryBillPrint.action',
			params:{
				custId:App.getData().custFullInfo.cust.cust_id,
				billingCycleId:value
			},
			scope:this,
			success:function(res,opt){
				msg.hide();msg = null;
				var data = Ext.decode(res.responseText);
				
				var p = this.printData = data;
				var cust = App.getData().custFullInfo.cust;
				p['cust_no'] = cust['cust_no'];
				p['cust_name'] = cust['cust_name'];
				p['address'] = cust['address'];
				p['now'] = Ext.util.Format.dateFormat(nowDate());
				
				var firstDate = value+'01';//月第一天日期
				//用户选择账期月份的第一天，最后一天
				p['firstDateOfMonth'] = Date.parseDate(firstDate,'Ymd').format('Y-m-d');
				//月最后一天的日期
				p['lastDateOfMonth'] = Date.parseDate(p['firstDateOfMonth'],'Y-m-d').getLastDateOfMonth().format('Y-m-d');
				
				var list = data['billList'] || [];
				var num = 0;
				Ext.each(list,function(r){
					num += r['final_bill_fee'];
				});
				p['sum_bill_fee'] = num;
				
				var len = list.length;
				for(var i=0;i<len;i++){
					var userId = list[i]['user_id'];
					if(userId !=''){
						for(var j=i+1;j<len;j++){
							var uId = list[j]['user_id'];
							if(userId == uId){
								list[j]['user_id']='';
							}
						}
					}
				}
				
				
				this.tpl.overwrite( this.items.itemAt(1).body, this.printData);
				/*var table = document.getElementById('feeInfoTabId');
				
				var len = list.length;
				var count = 1,sum = 0;
				for(var i=0;i<len;i++){
					var row = table.insertRow(); 
					
					var bill = list[i];
					var userId = bill['user_id'];
					
					if(sum == i){
						for(var j=count;j<len;j++){
							var uId = list[j]['user_id'];
							if(userId == uId){
								++count;
							}
						}
					}
					
					if(count>1){
						var td = row.insertCell();
						td.rowSpan=count;
						td.innerHTML = bill['user_name'];
						sum += count;
						count = 1;
					}else{
						if(i > sum){
							var td = row.insertCell(0);
							td.innerHTML = bill['user_name'];
						}
					}
					var td = row.insertCell();
					td.innerHTML = bill['acctitem_name'];
					
					td = row.insertCell();
					td.innerHTML = Ext.util.Format.formatFee(bill['final_bill_fee']);
					
				}
				
//				Ext.get('feeInfoTabId').innerHTML = html;
*/				
				
//				this.tpl.overwrite( this.items.itemAt(1).body, this.printData);
				this.items.itemAt(0).hide();//隐藏账期下拉框panel
				this.items.itemAt(1).show();//显示账单数据信息panel
				Ext.getCmp('printBillId').enable();
				Ext.getCmp('printBackId').enable();
				
				var bigWindow = App.getApp().menu.getBusiWin();//当前"账单打印"window
				
				var h = Ext.get('printBillDivId').dom.offsetHeight;
				if(h>window.parent.document.body.clientHeight)//高度最大不能超过600
					h=520;
				bigWindow.setHeight(h+50);
				bigWindow.center();//将重新设置高度的window居中
				
				
				Ext.each(document.getElementsByTagName("table"),function(ele){
					Ext.get(ele).set({style:'font-size:12px'});
				});
				Ext.get("printBillTabId").set({style:'font-size:12px'});
			}
		});
	},
	doPromDateSelect:function(combo,record){
		var value = combo.getValue();
		var msg = Show();
		Ext.Ajax.request({
			url:root+'/commons/x/QueryCust!queryPromPrint.action',
			params:{
				custId:App.getData().custFullInfo.cust.cust_id,
				promFeeSn:value
			},
			scope:this,
			success:function(res,opt){
				msg.hide();msg = null;
				var data = Ext.decode(res.responseText);
				
				var p = this.printData = data;
				var cust = App.getData().custFullInfo.cust;
				p['cust_no'] = cust['cust_no'];
				p['cust_name'] = cust['cust_name'];
				p['address'] = cust['address'];
				p['now'] = Ext.util.Format.dateFormat(nowDate());
				
				p['firstDateOfMonth'] = "";
				//月最后一天的日期
				p['lastDateOfMonth'] = "";
				
				var list = data['billList'] || [];
				var num = 0;
				Ext.each(list,function(r){
					num += r['final_bill_fee'];
					r['final_bill_fee'] = 0;
				});
				p['sum_bill_fee'] = num;
				p['prod_bill_fee'] = num;
				p['xj_fee'] = num;
				var len = list.length;
				for(var i=0;i<len;i++){
					var userId = list[i]['user_id'];
					if(userId !=''){
						for(var j=i+1;j<len;j++){
							var uId = list[j]['user_id'];
							if(userId == uId){
								list[j]['user_id']='';
							}
						}
					}
				}
				
				
				this.tpl.overwrite( this.items.itemAt(1).body, this.printData);
	
				this.items.itemAt(0).hide();//隐藏账期下拉框panel
				this.items.itemAt(1).show();//显示账单数据信息panel
				Ext.getCmp('printBillId').enable();
				Ext.getCmp('printBackId').enable();
				
				var bigWindow = App.getApp().menu.getBusiWin();//当前"账单打印"window
				
				var h = Ext.get('printBillDivId').dom.offsetHeight;
				if(h>window.parent.document.body.clientHeight)//高度最大不能超过600
					h=520;
				bigWindow.setHeight(h+50);
				bigWindow.center();//将重新设置高度的window居中
				
				
				Ext.each(document.getElementsByTagName("table"),function(ele){
					Ext.get(ele).set({style:'font-size:12px'});
				});
				Ext.get("printBillTabId").set({style:'font-size:12px'});
			}
		});
	},
	doPrint:function(){
		
		Ext.each(document.getElementsByTagName("table"),function(ele){
			Ext.get(ele).set({style:'font-size:20px'});
		});
		Ext.get("printBillTabId").set({style:'font-size:20px'});
		document.body.innerHTML = 
			Ext.get('printBillDivId').dom.innerHTML;
			
		
		window.print();
	},
	doClose:function(){
		App.getApp().menu.hideBusiWin();
	}
});

window.onbeforeprint = function(){
	PageSetup_Null();
}

window.onafterprint = function(){
	//打印完后恢复回去
	document.body.innerHTML = '';
	App.getApp().menu.hideBusiWin();

}

//设置页眉页脚为空   
function PageSetup_Null(){
    try{
    	//注册表路径
		var path="HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
        var RegWsh = new ActiveXObject("WScript.Shell");   
        RegWsh.RegWrite(path+"header","");
        RegWsh.RegWrite(path+"footer","");
    }catch(e){}
}


//Ext.onReady(function(){
//	var panel = new BillPrintPanel();
//	TemplateFactory.gViewport(panel);
//});











