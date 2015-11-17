<!-- 
	Author：danjp
	Date  : 2010/09/07
	Action: 系统管理配置模块
 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<% 
	String r = request.getContextPath(); 
%>
	<!-- 公用打印项 -->
	<script type="text/javascript" src="<%=r %>/pages/commons/PrintItemPanel.js" charset="UTF-8" ></script>
	<script type="text/javascript" src="<%=r %>/pages/commons/ExtAttrFactory.js" charset="UTF-8" ></script>
	<script type="text/javascript" src="<%=r %>/pages/commons/CountyChoosePanel.js" charset="UTF-8"></script>
	<!-- 扩展属性 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/extension/Extension.js" charset="UTF-8" ></script>
    <script type="text/javascript" src="<%=r %>/pages/sysmanager/config/extension/CreateGroup.js" charset="UTF-8" ></script>
	<!-- 模板配置 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/template/TemplateMng.js"></script>
	<!-- 规则配置 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/rule/Rule.js"></script>
	<!-- 机构管理 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/dept/DeptView.js"></script>
	<!-- 操作记录 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/log/LogView.js"></script>
	<!-- 系统异动记录 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/SysChange.js"></script>
	<!-- 延停天数阈值 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/ThersholdCfg.js"></script>
	<!-- 操作员管理 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/optr/OptrManage.js"></script>
	<!-- 菜单管理 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/resource/MenuManage.js"></script>
	<!-- 角色管理 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/role/RoleManage.js"></script>
	<!-- 地区管理 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/addr/AddressView.js"></script>
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/addr/DistrictView.js"></script>
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/addr/ChangeAddress.js"></script>
	<!-- 促销配置 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/promotion/Promotion.js"></script>
	<!-- 促销配置 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/promfee/promfee.js"></script>
	<!-- 产品管理 -->
	<script type="text/javascript" src="<%=r %>/pages/prod/Prod.js" charset="UTF-8" ></script>
  	<script type="text/javascript" src="<%=r %>/pages/prod/ProdItem.js" charset="UTF-8" ></script>
  	<script type="text/javascript" src="<%=r %>/pages/prod/Tariff.js" charset="UTF-8" ></script>
  	<script type="text/javascript" src="<%=r %>/pages/prod/Disct.js" charset="UTF-8" ></script>
  	<script type="text/javascript" src="<%=r %>/pages/prod/ProdDetalis.js" charset="UTF-8" ></script>
  	<!-- 费用项管理 -->
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/cfgfee/BusiFee.js" charset="UTF-8" ></script>
  	<!-- 系统参数配置 -->
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/sysconfig/itemvalue/ItemValue.js" charset="UTF-8" ></script>
  	<!-- 财务配置 -->
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/finance/Finance.js" charset="UTF-8" ></script>
	<!-- 批量管理 -->
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/send/StopTask.js" charset="UTF-8" ></script>
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/send/MessageTask.js" charset="UTF-8" ></script>
  	<!-- 动态资源组管理 -->
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/res/ResGroup.js" charset="UTF-8" ></script>
  	<!-- 公告管理 -->
  	<!-- 引入 百度的 html editor -->
  	<script charset="utf-8" src="<%=r %>/editor/kindeditor-min.js"></script>
  	<script charset="utf-8" src="<%=r %>/editor/lang/zh_CN.js"></script>
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/bulletin/Bulletin.js" charset="UTF-8" ></script>
  	<!-- 通授权管理 -->
  	<script type="text/javascript" src="<%=r %>/pages/cmd/SendAllCmd.js" charset="UTF-8" ></script>
  	
  	<!-- 生成代金券 -->
  	<script type="text/javascript" src="<%=r %>/pages/resource/voucher/GenerateVoucher.js" charset="UTF-8" ></script>
  	<!-- 领用代金券 -->
  	<script type="text/javascript" src="<%=r %>/pages/resource/voucher/ProcureVoucher.js" charset="UTF-8" ></script>
  	<!-- 代金券查询 -->
  	<script type="text/javascript" src="<%=r %>/pages/resource/voucher/QueryVoucher.js" charset="UTF-8" ></script>
  	<script type="text/javascript" src="<%=r %>/pages/resource/voucher/VoucherTypeEdit.js" charset="UTF-8" ></script>
  	<!-- 资源管理 -->
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/res/ResView.js" charset="UTF-8" ></script>
  	<!-- 任务配置 -->
  	<script type="text/javascript" src="<%=r %>/pages/resource/task/TaskMng.js" charset="UTF-8" ></script>
	<!-- 客户数量管理 -->
  	<script type="text/javascript" src="<%=r %>/pages/resource/CustColonyCfg.js" charset="UTF-8" ></script>
  	<!-- 产品目录管理 -->
	<script type="text/javascript" src="<%=r %>/pages/prod/ProdDict.js"></script>
  	
	 <!-- 定额账户配置 -->
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/acctConfig/acctConfig.js" charset="UTF-8" ></script>
  	 <!-- 非居民审批单 -->
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/approval/NonresCustApproval.js" charset="UTF-8" ></script>
  	<!-- 字段参数配置 -->
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/sysconfig/tabDefine/TabDefine.js" charset="UTF-8" ></script>
  	<!-- 在线用户 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/OnlineUser.js"></script>
	<!-- 服务器配置 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/ServerCfg.js"></script>
	
	<!-- 汇率配置 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/ExchangeRateManage.js"></script>
	
	<!-- OSD监控 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/OsdPhraseCfg.js"></script>
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/OsdMonitor.js"></script>
	<!-- 模板权限配置 -->
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/TemplateRoleView.js"></script>
  	<!-- 停机数配置 -->
  	<script type="text/javascript" src="<%=r %>/pages/sysmanager/config/stopcount/StopCount.js"></script>
  	
  	<!-- 省定义配置 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/ProvinceConfig.js"></script>
	<!-- 代理商配置 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/AgentConfig.js"></script>
	<!-- 翻译配置 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/TranslationConfig.js"></script>
	<!-- 协议用户配置 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/AgreementUserConfig.js"></script>
	<!-- OTT资源配置 -->
	<script type="text/javascript" src="<%=r %>/pages/sysmanager/system/OttResConfig.js"></script>