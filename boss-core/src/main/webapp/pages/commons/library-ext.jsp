<!-- 
	Author：hh
	Date  : 2009/12/25
	Action: 使用Ext库所需要导入的文件。使用时只需要包含该文件即可。
 -->
 
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.page import="com.ycsoft.commons.helper.DateHelper"/>
<% 
	String r = request.getContextPath(),
	 	   boss_res = com.ycsoft.commons.constants.Environment.ROOT_PATH_BOSS_LOGIN;
%>
<script type="text/javascript">
	function nowDate(){
		return parent.nowDate();
	}
</script>
<link rel="stylesheet" href="<%=boss_res %>/components/ext3/resources/css/ext-all.css" type="text/css" />
<link rel="stylesheet" href="<%=boss_res %>/resources/css/boss-all.css" type="text/css" />
<link rel="stylesheet" href="<%=boss_res %>/resources/css/icon.css" type="text/css" />
<link rel="stylesheet" href="<%=boss_res %>/resources/css/LockingGridView.css" type="text/css" />
<link rel="stylesheet" href="<%=boss_res %>/resources/css/Spinner.css" type="text/css" />
<link rel="stylesheet" href="<%=boss_res %>/resources/css/msg-tip.css" type="text/css" />

<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-base.js" ></script>
<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-all.js" ></script>
<script type="text/javascript" src="<%=boss_res %>/components/ext3/ext-lang-zh_CN.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/components/jquery/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="<%=boss_res %>/components/jquery/ext-async-adapter.js"></script>
<script type="text/javascript">
	var root = '<%=r %>';
</script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/DebugWindow.js"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/Constant.js"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/Override.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/ext-helper.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/Vtypes.js" ></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/busi-helper.js" ></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/Grid.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/SearchField.js"></script>
<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/BufferView.js"></script>
<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/LockingGridView.js"></script>
<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/ColumnLockBufferView.js"></script>
<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/Spinner.js"></script>
<script type="text/javascript" src="<%=boss_res %>/components/ext3/ux/SpinnerField.js"></script>

<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FilterTreePanel.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/QueryFilterTreePanel.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/TreeComboBox.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/ParamComboBox.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/MoneyColumn.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/MoneyField.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FilterGrid.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/FilterGridTbar.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/DistrictTreeCombo.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=r %>/pages/business/user/UserBaseInfo.js"></script>
<script type="text/javascript" src="<%=r %>/pages/business/user/UserInfoPanel.js"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/MonthPickerPlugin.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=boss_res %>/pages/commons/ux/lovcombo.js" charset="UTF-8"></script>
<body>
	 <div id='loading' >
		<img src="<%=boss_res %>/resources/images/loading.gif" width="32" height="32" 
			style="margin-right:4px;" align='middle' />
		<span style='font: normal 12px arial,tahoma,sans-serif;font-weight:bold'>正在加载...</span>
	</div>
</body>
