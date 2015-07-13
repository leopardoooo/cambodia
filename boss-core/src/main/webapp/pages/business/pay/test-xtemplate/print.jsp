<%@ page language="java" pageEncoding="UTF-8"%>
<% 
	String r = request.getContextPath(),
		   boss_res = com.ycsoft.commons.constants.Environment.ROOT_PATH_BOSS_LOGIN; 
%>
<html>
  <head>
  	<link rel="stylesheet" href="<%=boss_res %>/components/ext3/resources/css/ext-all.css" type="text/css" />
 	<style type="text/css">
 		body{}
 		tr th,body,textarea{
 			font-size: 12px;
 		}
 		table, table tr td ,table tr th{
 			border: 1 solid #D2D2D2;
 		}
 		table tr{ height: 25px;}
 		table tr th{padding-left: 10px; background-color: #DFDFDF}
 		textarea{
 			border: 0px;
 			overflow: auto;
 		},
 		tfoot{background-color: #F6F6F6;}
 		tfoot tr{height: 35;}
 		button{ padding-top: 3px; width: 120px; cursor: hand}
 		#dataArea, #tplArea{
 			background-color: #E3FDE6;
 		}
 		#tplArea{
 		}
 		#dataArea{
 			color: #9A9A32
 		}
 		#content{
 			background-color: yellow;
 		}
 	</style>
  </head>
  <body>
  	<OBJECT classid="clsid:C80C0611-AE1F-45EC-BE4E-625366C3752F"
		  codebase="<%=boss_res %>/components/cab/PrintCtrl.cab#version=1,0,0,0" name="PrintCtrl"
		  width="0" height="0">
	</OBJECT>
	
	<br />
	<br />
	<br />
	<br />
	<center>
		<table width = "80%">
			<tr> <th>编辑模板</th> <th>定义数据</th> </tr>
			<tr>
				<td>
					<textarea rows="10" cols="55" id="tplArea" >
<?xml version="1.0" encoding="GBK" ?> 
<printinfo>
<defaultsetting>
  <width>2500</width>
  <height>933</height>
  <font>10</font>
</defaultsetting>
<items>
  <printitem><x>940</x><y>150</y><data>{[values.cust.busi_name]}</data></printitem>
  <printitem><x>340</x><y>150</y><data>{[values.cust.cust_name || '']}</data></printitem>
  <printitem><x>210</x><y>106</y><data>{[values.date.year || '']}</data></printitem>
  <printitem><x>330</x><y>106</y><data>{[values.date.month || '']}</data></printitem>
  <printitem><x>385</x><y>106</y><data>{[values.date.day || '']}</data></printitem>	 
  <printitem><x>350</x><y>220</y><data>{[this.chinese(values.total)]}</data></printitem>
  <printitem><x>950</x><y>220</y><data>{[fm.convertToYuan(values.total)]}</data></printitem>
  <tpl for="items">
   <printitem><x>270</x><y>{[285 + xindex* 50]}</y><data>{printitem_name}</data></printitem>
   <printitem><x>1150</x><y>{[285+ xindex* 50]}</y><data>{[fm.convertToYuan(values.amount)]}</data></printitem>
  </tpl> 
  <printitem><x>950</x><y>483</y><data>{[values.optr.optr_name]}</data></printitem>
</items>
</printinfo></textarea>
				</td>
				<td>
					<textarea rows="10" cols="55" id="dataArea">
{total: 11560,
 cust: {busi_name: '010132132332',cust_name: '黄辉'},
 optr: {optr_name: '徐海平'},
 date: {year: 2010, month: 4, day: 19},
 items: [
  {printitem_name: '打印项一', amount: 3330},
  {printitem_name: '打印项二',amount: 6660 }
 ]
}</textarea>
				</td>
			</tr>
			<tr>
				<th colspan="2"> 模板替换后的内容 </th>
			</tr>
			<tr>
				<td colspan="2">
					<textarea readOnly="true" cols="113" rows="16" id='content'></textarea>
				</td>
			</tr>
			<tfoot>
			<tr align="center">
				<td colspan="2">
					<button onclick="doWrite();" >输出内容</button>
					<button onclick="doSave();" >保存内容</button>
					<button onclick="doPrint();"  >打印内容</button>
				</td>
			</tr>
		</table>
	</center>
	
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-base.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-all.js" ></script>
	<script type="text/javascript">
		var root = '<%=r %>';
	</script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Constant.js"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Override.js" charset="UTF-8"></script>
  	<script type="text/javascript" src="./Print.js"></script>
  	<script type="text/javascript">
  		function doPrint(){
		 	PrintCtrl.XMLDoc = content.value;
			PrintCtrl.Print();
  		} 
  		function doWrite(){
  			var tpl = tplArea.innerText;
  			var data = dataArea.innerText;
  			try{
  				data = Ext.decode( data );
  			}catch(e){
  				alert( "定义的数据有问题：" + e );
  			}
  			var contentText;
  			try{
  				contentText = PrintTools.getContent( tpl , data )
  			}catch(e){
  				alert("applyTemplate is error : " + e.message);
  			}
  			if(contentText){
  				content.innerText = contentText;
  			}
  		}
  		
  		disableTab = function( e ,ele){
  			if(e.getKey() == e.TAB){
  				e.stopEvent();
  			}
  		}
  		Ext.get('tplArea').on("keydown", disableTab );
  		Ext.get('dataArea').on("keydown", disableTab );
  	</script>
  </body>
</html>
