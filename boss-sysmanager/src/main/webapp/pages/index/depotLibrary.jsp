<!-- 
	Author：danjp
	Date  : 2010/09/07
	Action: 仓库配置模块
 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.page import="com.ycsoft.commons.constants.Environment"/>
<% 
	String root = request.getContextPath(),b_res = Environment.ROOT_PATH_BOSS_LOGIN; 
%>
	<!-- 扩展属性 -->
	<script type="text/javascript" src="<%=b_res %>/pages/commons/ux/PrintTools.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/DepotIndexManager.js"></script>
	<!-- <script type="text/javascript" src="<%=root %>/pages/sysmanager/config/custextend/custextend.js" charset="UTF-8" ></script> -->
	<script type="text/javascript" src="<%=root %>/pages/resource/CommonDeviceInfo.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/CheckIn.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/OrderManager.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/CheckOut.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/DifferenceManage.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/AppUseDevice.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/DeviceStatusManage.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/BackHouse.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/Deploy.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/StbFilled.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/StbCardFilled.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/QueryDevice.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/DeviceDetailInfo.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/DownloadDeviceInfo.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/ReclaimDevice.js"></script>
	<script type="text/javascript" src="<%=root %>/pages/resource/DeviceLoss.js"></script>
 	<script type="text/javascript" src="<%=root %>/pages/resource/CardSend.js"></script>