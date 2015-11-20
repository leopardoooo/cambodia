<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.ycsoft.commons.constants.Environment"/>
<jsp:directive.page import="com.ycsoft.commons.helper.DateHelper"/>
<jsp:directive.page import="java.util.Date"/>
<%@page import="com.ycsoft.commons.action.SessionListener"%>
<% String root = request.getContextPath(),
	 	   boss_res = Environment.ROOT_PATH_BOSS_LOGIN ; 
   String nowDate = DateHelper.format(new Date(),"yyyy/MM/dd HH:mm:ss");
   String basePath = request.getLocalAddr()+":"+request.getServerPort();
   String lang = session.getAttribute(Environment.USER_IN_SESSION_LANG).toString();
   String version = "2";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>SupetNet BOSS 1.0</title>
    <script type="text/javascript">
    	var loginPath = '<%=boss_res %>';
    </script>
    
    <link rel="stylesheet" href="<%=boss_res %>/components/ext3/resources/css/ext-all.css" type="text/css" />
   <!-- <link rel="stylesheet" href="<%=boss_res %>/components/ext3/resources/css/xtheme-gray.css" type="text/css" />  -->
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/index-core.css" type="text/css" />
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/boss-all.css" type="text/css" />
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/LockingGridView.css" type="text/css" />
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/fileuploadfield.css" type="text/css" />
<!-- 	<link rel="stylesheet" href="<%=boss_res %>/resources/css/icon.css" type="text/css" /> -->
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/icon.css" type="text/css" />
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/msg-tip.css" type="text/css" />
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/treegrid.css" type="text/css" />
	<link rel="stylesheet" href="<%=boss_res %>/resources/css/treegrideditor.css" type="text/css" />
  </head>
  <body style="overflow: hide;">
  	<div id="loading">
  	 <div>
  	  <img src="<%=boss_res %>/resources/images/loading.gif" width="51" height="31" 
		   style="margin-right:8px;float:left;vertical-align:top;" />
			 BOSS System <br />
	  <span class="loading-msg">loading...</span>
	 </div>
	</div>
	<div id="header" style="visibility: hidden;">
		<div class="site-nav-search">
			<div class="logo">
				<img src="<%=boss_res %>/resources/images/logo.jpg"/>
			</div>
			<div action="?" method="post" name="search" id="search">
				 <div class="search-panel">
					<span class="search-input-box"> 
						<input id='type' type='hidden' name='type'/>
						<input id="q" type="text"  style="color:#bbbbbb;font-weight:bold"  value=''
							 onkeydown="if(event.keyCode == 13){App.search.searchCustUnPrint();} "
							 onblur="searchonblur(this);"
							 onfocus="searchonfocus(this);" name="q" />
					</span> 
					<span class="search-btn">
						<button id='btnSearch_id' onclick="if (Ext.getDom('q').value!=tiptext)App.search.searchCustUnPrint(false)"></button> 
					</span>
					<span class="search-btn" style="margin-left: -9px">
						<button id='btnSearch_id2' onclick="App.search.searchCustUnPrint(true)"></button> 
					</span>
				</div>
				<ul id="TabList" class="search-tablist">
					<li  class="first selected"> <a href="#" boss:name="cust" onclick='changeSearchType(true)' id="_search0"></a> </li>
					<li> <a href="#" boss:name="device_id" onclick='changeSearchType(false)' id="_search1"></a> </li>
					<li> <a href="#" boss:name="addr_name" onclick='changeSearchType(false)' id="_search2"></a> </li>
					<li> <a href="#" boss:name="tel" onclick='changeSearchType(false)' id="_search3" ></a> </li>
					<li> <a href="#" boss:name="mutipleSearch" id="_search4" ></a> </li>
				</ul>
			</div>
			
			<div id='tool' class="toolpanel" style="display: block;">
			</div>

		</div>

	</div>
		<%--  导入ExtJS库相关文件 --%> 
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-base.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-all.js" ></script>
	
	<script type="text/javascript" src="<%=boss_res%>/i18n/<%=lang %>/ext-lang.js"></script>
	<script type="text/javascript" src="<%=boss_res%>/i18n/<%=lang %>/resouces-lang.js"></script>
	<script type="text/javascript" src="<%=boss_res%>/i18n/<%=lang %>/boss-core-lang.js"></script>
	<script type="text/javascript" src="<%=boss_res%>/i18n/langUtils.js"></script>
	
<script type="text/javascript">
	var regourl = '<%=SessionListener.getSsoLoginUrl(request) %>';
	var bossBasePath = '<%=basePath%>';
    
    var root = '<%=root %>',token_id ='${param.tokenId }';
    var issearchtip = true;
    var tiptext = langUtils.bc("home.searchTip");
    
	function searchonblur(athis){
		if(issearchtip){
			if (athis.value == ''){
				tiptext = langUtils.bc("home.searchTip");
				athis.value = tiptext;
				athis.style.color='#bbbbbb';
			}
		}
	}
	
	function searchonfocus(athis){
		if(issearchtip){
			if (athis.value == tiptext ){
				athis.value='';
				athis.style.color='#660000';
			}else{
				athis.select();
			}
		}
	}
	
	function changeSearchType(type){
		var athis = document.getElementById("q");
		if (type){
			if (athis.value == tiptext ){
				athis.style.color='#660000';
			}else if (athis.value == ''){
				athis.value = tiptext;
				athis.style.color='#bbbbbb';
			}
		}else{
			if (athis.value == tiptext ){	
				athis.value = '';	
				athis.style.color='#660000';
			}
		}
		issearchtip=type;
	}
	
	// i18n初始化
	langUtils.bc("home.searchBtns.[0]", "btnSearch_id");
	langUtils.bc("home.searchBtns.[1]", "btnSearch_id2");
	
	langUtils.bc("home.searchTabs.[0]", "_search0");
	langUtils.bc("home.searchTabs.[1]", "_search1");
	langUtils.bc("home.searchTabs.[2]", "_search2");
	langUtils.bc("home.searchTabs.[3]", "_search3");
	langUtils.bc("home.searchTabs.[4]", "_search4");

    </script>
	<script type="text/javascript" src="<%=boss_res %>/components/jquery/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="<%=boss_res %>/components/jquery/ext-async-adapter.js"></script>
	
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/RowExpander.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/SearchField.js"></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/BufferView.js"></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/LockingGridView.js"></script>
	<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/ColumnLockBufferView.js"></script>
	
	<%--  导入系统相关通用文件 --%>
  	<script type="text/javascript" src="<%=boss_res %>/pages/commons/DebugWindow.js"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Constant.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Override.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ext-helper.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Vtypes.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/busi-helper.js" ></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Grid.js"  charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/Gpanel.js"  charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FilterTreePanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/QueryFilterTreePanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/lovcombo.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/TreeComboBox.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/ParamComboBox.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FilterGrid.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FilterGridTbar.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FilterWindow.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/PagingMemoryProxy.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FileUploadField.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/PrintTools.js"></script>
	
		<!-- TreeGrid -->
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridSorter.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridColumnResizer.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridNodeUI.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridLoader.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridColumns.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGrid.js"></script>
  	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeNodeChecked.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditor.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorEventModel.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorNode.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorNodeUI.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorNodeReader.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorNodeWriter.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorLoader.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorDragZone.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/treegrid/TreeGridEditorSelectionModel.js"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/DataView-more.js"></script>
 
	
	<%--  top部分，包含最顶端的菜单及其刷新、搜索栏、公告 --%>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/TopToolbar.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/topsearch/topCfg.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/topsearch/top.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/MobileBillWin.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/BatchPayFeeWin.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/BatchEditAcctDateWin.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/FeeUnitpreWin.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/QueryInvoice.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/QueryDevice.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/business/cust/AddrCustSelect.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/OpenOTTMobile.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/QueryVoucher.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/ValuableCardWin.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/UpdateAddress.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/Bulletin.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/EditCustStatus.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/EditUserStatus.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/BatchLogoffCust.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/BatchLogoffUser.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/PinyinProdCombo.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/BatchOrderProd.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/BatchCancelProd.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/UserCountCheck.js"></script>
   	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/BandOnlineUserWin.js"></script>
   	<script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/BandUserFailedLogWin.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/RefreshTool.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/SendCaCard.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/AddressViewWin.js"></script>
    <script type="text/javascript" src="<%=root %>/pages/index/toptoolbar/OsdSendViewWin.js"></script>
	
	<%--  right部分，包含资源菜单 --%>
	<script type="text/javascript" src="<%=root %>/pages/commons/CoreConstant.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/commons/CommonTab.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/right/MenuHandler.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/right/right.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/right/BigWindow.js"></script>
	
	<%--  center部分,包含客户信息相关信息、用户相关信息面板、账户面板 --%>
	<script type="text/javascript" src="<%=root %>/pages/index/center/BaseInfoPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/center/CustPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/center/UnitPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/center/UserPanel.js" charset="UTF-8"></script>

	<script type="text/javascript" src="<%=root %>/pages/index/center/PayfeePanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/center/DocPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/center/DoneCodePanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/business/cust/TaskBaseForm.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/center/CommandInfoPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/MonthPickerPlugin.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/center/BillPanel.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=root %>/pages/index/center/center.js?version=<%=version %>" charset="UTF-8" ></script>
  
  	<%--  组装首页  --%> 
  	<script type="text/javascript" src="<%=root %>/pages/business/common/ExtAttrFactory.js"></script>
  	<script type="text/javascript" src="<%=root %>/pages/commons/App.func.js" charset="UTF-8"></script>
    <script type="text/javascript" src="<%=boss_res %>/pages/commons/App.data.js" charset="UTF-8"></script>
    
    <script type="text/javascript" src="<%=root %>/pages/index/TipsWindow.js"></script>
    
  	<script type="text/javascript" src="<%=root %>/pages/index/index.js"></script>
  	
  	<script type="text/javascript">
  	
  	
  	
  	//web服务器时间
   	var serverDate = new Date('<%=nowDate%>');
   	serverDate.setSeconds(serverDate.getSeconds()+2);
	function nowDate(){
		return serverDate.clone();
	}
	/* function runDate(){
		serverDate.setSeconds(serverDate.getSeconds()+1);
		var txt = Ext.getCmp('nowServerDateViewer');
		var dateStr = serverDate.format('Y-m-d H:i:s');
		if(!txt){
			txt = {id:'nowServerDateViewer',text:dateStr};
			if(App.tool){
				App.tool.insert(1,txt);
				App.tool.doLayout();
			}
		}else{
			txt.setText(dateStr);
		}
	}
	setInterval("runDate()",1000); */
  	</script>
  </body>
</html>
