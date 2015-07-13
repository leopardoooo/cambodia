<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String root = request.getContextPath(),
		rePrint = request.getParameter("reprint"),
			boss_res = com.ycsoft.commons.constants.Environment.ROOT_PATH_BOSS_LOGIN;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	<title>溧阳广电BOSS系统-业务受理单</title>
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/print.css" type="text/css" />
	<style media=print>
		#header{margin-top: 100px;}
    </style>
    <style>
    	#header{margin-top: 100px;}
    	.body{padding: 0px 10px;margin: 0px;}
    </style>
    <script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-base.js" ></script>
    <script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-all.js" ></script>
    <script type="text/javascript" src="<%=root %>/pages/business/pay/ServiceDocPrintFrame.js" ></script>
</head>
<body class="body">
</body>
<script type="text/javascript">
Ext.apply(String.prototype,{
	replaceAll:function(former,newStr){
	   var reg = new RegExp(former,"g");
	   return this.replace(reg,newStr);
	}
});
	var app = window.parent.parent.App;
	var custId = app.data.custFullInfo.cust["cust_id"];
	var doneCodes = [];//所有业务doneCode集合
	var rePrint = '<%=rePrint %>'; 
	var docSn = app.getData()['docSn'];
	var docSn2BePrint = null;
	
	if(rePrint == 'true'){
		var grid = Ext.getCmp('D_BUSI');
		var sel = grid.selModel.getSelections();
		App.getApp().docSn = [sel[0].get('doc_sn')];
	}
	
	//远程加载数据
	Ext.Ajax.request({
		url: '<%=root %>/core/x/Print!queryDoc.action',
		params: {cust_id: custId,docSn:docSn},
		success: function(res, ops){
			var data = Ext.decode(res.responseText);
			if(data.exception && data.exception.content){
				//Ext.Msg.alert(data.exception.title,data.exception.content);
				alert(data.exception.content);
				return;
			}
			//清空重打标识
			app.getData()['docSn'] = null;
			app.getApp().data['busiDocPrintDate'] = null;
			if(!Ext.isEmpty(data)){
				var codes = data['doneCodes'];
				if(!Ext.isEmpty(codes)){
					var arr = codes.split(',');
					for(var i=0,len=arr.length;i<len;i++){
						if(doneCodes.indexOf(arr[i]) == -1){
							doneCodes.push(arr[i]);
						}
					}
				}
				generatorPrintDoc(data);
			}
		}
	});
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// 打印配置
	var pageRows = 30, //一张单据能打印多少行数据
		split = false;   //是否打印分割线，如果配置了打印分割线，相应的pageRows需要调整
		
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    
    function processData(item){
		var list = item.prod_tariff_list;
		if(list && list.length && list.length>0){
			for(var index =0;index<list.length;index++){
				var info = list[index];
				var prod_descs = [];
				info.prod_desc = Ext.isEmpty(info.prod_desc)? '':info.prod_desc;
				info = splitProperty(info, 'prod_desc', 'prod_descs');
			}
		}
	}
	
	/**
		@param item 数据源
		@param propertyName 需要拆分的属性.必须为string.
		@param targetPropertyName 拆分后的属性名.拆分后为array .
	**/
	function splitProperty(item,propertyName,targetPropertyName){
		if(!Ext.isEmpty(item[propertyName])){
			var tempStr = item[propertyName].replaceAll('u003c','<');
			tempStr = tempStr.replaceAll('u003e','>');
			item[propertyName] = tempStr;
			var array = [];
			if(item[propertyName] && item[propertyName].length>0){
				var times = item[propertyName].length/64;
				for(var i_d_x =0;i_d_x<times;i_d_x++){
					array.push({str:item[propertyName].substring(i_d_x*64,(i_d_x+1)*64)});
				}
			}
			item[targetPropertyName] = array;
		}
		return item;
	}
    
	//生成打印业务单据
	function generatorPrintDoc(data){
		var subItems = "";
		for(var i = 0,len = data.items.length; i<len ; i++ ){
			//定义参数
			var item = data.items[i];
			//processData(item);
			var infoData = Ext.decode(item.info);//原来的
			var infoCopy = {};
			for(var key in infoData){
				infoCopy[key] = infoData[key];
			}
			
			//产品订购
			if(infoCopy.items && infoCopy.items.length>0){
				var _items = infoCopy.items;
				for(var id1 =0;id1<_items.length;id1++){
					var item_inner = _items[id1];
					processData(item_inner);
				}
			}
			//套餐缴费
			if(infoCopy.prod_tariff_list){
				for(var index = 0;index<infoCopy.prod_tariff_list.length;index++){
					var item_inner = infoCopy.prod_tariff_list[index];
					item_inner = splitProperty(item_inner, 'prod_desc', 'prod_descs');
				}
			}
			if(infoCopy.promotion_desc){
				infoCopy = splitProperty(infoCopy, "promotion_desc", 'promotion_descs');
			}
			
			//测试结束,中间代码抗议删除
			var itemValues = {
				point: (i + 1),
				item: item,
				//params: infoData
				params: infoCopy
			};
			subItems += DOC.transTpl(item.tpl, itemValues);
			if(split){
				subItems += DOC.br();
			}
		}
		docSn2BePrint = data.docSn;//未打印过的业务单,待打印的docsn
		//解析所有内容
		var custCopy = {};
		for ( var key in app.data.custFullInfo) {
			custCopy[key] = app.data.custFullInfo[key];
		}
		if(!app.data.custFullInfo.acctBank || app.data.custFullInfo.acctBank.status != 'ACTIVE'){
			custCopy.acctBank = {};
		}
		var body = DOC.transTpl(data.print.tpl, {
			docSn: docSn,
			id: data.id,
			done_date: data.today,
			subItems: subItems,
			custInfo: custCopy,
			fees: data.fees,
			feeTotal: data.feeTotal,
			optrDeptName: data.optrDeptName,
			loginName:data.loginName
		});
		document.body.innerHTML = body;
		//搜索行，如果行超过了，则换页
		var rows = Ext.query("DIV.row");
		var pageCount = parseInt(rows.length / pageRows);
		//如果被整除了，则当前总页数减一
		if(rows.length % pageRows === 0){ 
			pageCount -- ;
		}
		var html = null;
		for(var i = 0 ; i < pageCount; i++){
			if(html == null){
				html = DOC.page() + Ext.get('header').dom.outerHTML;
			}
			new Ext.Element(rows[(i + 1 ) * pageRows]).insertHtml("beforeBegin", html);
		}
		
		//更改页面上的分页信息
		var cpArr = Ext.query("SPAN.currentPage");
		var tpArr = Ext.query("SPAN.totalPage");
		for(var i = 0 ;i < cpArr.length ;i++){
			cpArr[i].innerHTML = (i+1);
		}
		for(var i=0; i< tpArr.length; i++){
			tpArr[i].innerHTML = tpArr.length;
		}
	}
</script>
</html>