Ext.ns("App.func");

/**
 * 面板中行内操作列下的系列功能操作图标是否能显示
 * @param data 当前行record.data
 * @param busicode 功能操作对应的busicode,见s_resource表
 * @param gridpanel的id
 */
Ext.apply(App.func,{
	
	FilterOperteBtn : function(data,busicode,panelName){
		var flag = false;
		//如果是销户客户，全部隐藏操作按钮
		var custStatus = App.getApp().getData().custFullInfo.cust.status;
		if (custStatus == 'INVALID'){
				if(busicode === '1008'){return true;}
				else if(busicode === '1051' && data['is_busi_fee']==='Y' && data['status']!='INVALID'){//业务费用
					if(data['finance_status'] == 'CLOSE' || App.getData().optr.optr_id != data['optr_id'])
					{return false;}else{return true;}
				}else if(busicode === '1233'){//账目作废金额退款
					if(data['can_refund']!='T' || data['invalid_fee']<=0){
						return false;
					}
					return true;
				}else if(busicode == '1063'){//发票打印
					//机打票才能打印
					if(data['invoice_mode'] !='A' || data['finance_status']!='IDLE' ||data['status']=='INVALID')
						{return false;}else{return true;}
				}else if(busicode == '1061'){//发票修改
					//未结账发票才能修改
					if(data['finance_status']!='IDLE'||data['status']=='INVALID')
						{return false;}else{return true;}
				}else if(busicode == '1161'){//作废发票
					if(data['finance_status']!='IDLE')//首先判断发票结存状态,结存状态为空闲的发票才可作废.
						return false;
					//本人的发票才能作废.
					if(data['optr_id'] != App.getData().optr.optr_id  )
						{return false;}else{return true;}
				}else if(busicode == '1234'){//作废账单
					if(data['status'] != '1' )//已出账
						{return false;}else{return true;}
				}
				return false;
		}else if(custStatus == 'DATACLOSE'){//资料隔离
			return false;
		}
/**************************************用户信息面板开始*************************************************/
		if(panelName == 'U_USER'){
			//如果数字用户，且卡号为空，只允许补入设备
			/*if(data['user_type'] == 'DTV' && Ext.isEmpty(data['card_id'])){
				if(busicode != '1222'){
					return false;
				}else{
					return true;
				}
			}
			//状态必须正常 1323为续报停
			if(data['status'] == 'REQSTOP' && (busicode != '1323' && busicode != '1123')) return false;
			if( (data['status'] == 'WAITLOGOFF' || data['status'] == 'DORMANCY' || data['status'] == 'ATVCLOSE') && busicode!='1221' && busicode!='1030')return false;
			*/
			if(busicode === '1009'){//更换设备
				if( data['user_type'] == 'OTT_MOBILE' || ( (data['user_type'] =='OTT' || data['user_type'] =='DTT') && Ext.isEmpty(data['stb_id'])) || (data['user_type'] =='BAND' && Ext.isEmpty(data['modem_mac'])) )
					return false;
			}else if(busicode ==='1130' || busicode ==='1131'){ //加挂IP，补收IP费
				if(data['user_type'] != 'BAND'){
					return false;
				}
				if(busicode ==='1131'){
					if(Ext.isEmpty(data['str6']) || Ext.isEmpty(data['str5']) ){
						return false;
					}
				}
				
			}else if(busicode === '2262'){//销售设备
				if( data['user_type']=='OTT_MOBILE' || data['str10'] == 'BUY' || (((data['user_type']=='DTT' || data['user_type']=='OTT') && Ext.isEmpty(data['stb_id']))
					|| (data['user_type']=='BAND' && Ext.isEmpty(data['modem_mac']))) ){
					return false;
				}
			}else if(busicode === '1029'){//指令重发(数字电视)
				if(data['user_type'] != 'DTV')
					return false;
			}else if(busicode == '1033'){//临时授权(数字电视且主终端、宽带)
				if(data['user_type'] == 'ATV') return false;
				if(data['user_type'] == 'DTV' && data['terminal_type'] !='ZZD' && Ext.isEmpty(data['card_id']))
					return false;
				if(data['user_type'] == 'DTV' && data['terminal_type'] !='ZZD'  && data['str19'] != 'T'){//超额副机
					var store = App.getApp().main.infoPanel.getUserPanel().userGrid.getStore();
					var flag = false;
					store.each(function(record){
						//主机非正常状态,超额不能临时授权
						if(record.get('terminal_type') == 'ZZD' && record.get('status') != 'ACTIVE' && record.get('status') != 'OWESTOP' && record.get('status') != 'OUNSTOP'){
							flag = true;
							return false;
						}
					},store);
					if(flag)return false;
				} 
				
			}else if(busicode == '1071'){//排斥资源
				if(data['user_type'] != 'DTV' || Ext.isEmpty(data['card_id']))
					return false;
			}else if(busicode == '1048'){//模拟费补收
				if(data['user_type'] != 'DTV')
					return false;
			}else if(busicode == '1035'){//刷新指令
				if(data['user_type'] != 'DTV')
					return false;
			}else if(busicode == '1500'){//主机产品同步
				if(data['user_type'] != 'DTV' || data['terminal_type'] !='ZZD')
					return false;
			}else if(busicode == '1038'){//第二终端转副机
				if(data['terminal_type'] !='EZD'){//第二终端
					return false;
				}
				var openDate = Date.parseDate(data.open_time,'Y-m-d H:i:s');
				openDate.setYear(openDate.getYear() + 3);
				openDate = openDate.dateFormat('Y-m-d');
				var today = nowDate();
				today = today.dateFormat('Y-m-d');
				
				if(openDate > today){//满3年
					return false;
				}
			}else if(busicode == '1120'){//修改支付密码
				if(data['user_type']!='DTV' || data['serv_type'] != 'DOUBLE'){
					return false;
				}
			}else if(busicode == '1074'){//修改宽带密码
				if(data['user_type'] != 'BAND')
					return false;
			}else if(busicode == '2123'){//重置密码
				//只有user_type = BAND和OTT_MOBILE用户显示, 且用户状态 是 正常 和施工 
				if( (data['user_type'] == 'BAND' || data['user_type'] == 'OTT_MOBILE') && (data.status == 'ACTIVE' || data['status'] == 'INSTALL') ){
					return true;
				}else{
					return false;
				}
			}else if(busicode == '1075'){//修改最大连接数
				if(data['user_type'] != 'BAND')
					return false;
			}else if(busicode == '1079'){//修改最大用户数
				if(data['user_type'] != 'BAND')
					return false;
			}else if(busicode == '1076'){//清除绑定
				if(data['user_type'] != 'BAND')
					return false;
				var prodData = App.getApp().main.infoPanel.getUserPanel().prodGrid.prodMap[data['user_id']];
				if(!prodData || prodData.length == 0){
					return false;
				}
			}else if(busicode == '1077'){//强制下线
				if(data['user_type'] != 'BAND')
					return false;
			}else if(busicode == '1222'){//修改机顶盒
				if(data['user_type']!='DTV' || Ext.isEmpty(data['card_id']) || !Ext.isEmpty(data['stb_id'])){
					return false;
				}
			}else if(busicode == '1223'){//一体机转换
				if(data['user_type']!='DTV' || Ext.isEmpty(data['stb_id'])){
					return false;
				}
			}else if(busicode == '1129'){//取消双向
				if(data['user_type']!='DTV' || data['serv_type'] != 'DOUBLE'){
					return false;
				}
			}else if(busicode == '1155'){//双向,宽带开户指令重发
				if(data['user_type']!='BAND' && !(data['user_type']=='DTV' && data['serv_type'] == 'DOUBLE')){
					return false;
				}
			}else if(busicode == '1236'){//修改接入方式
				if((data['user_type']=='DTV' && data['serv_type'] != 'DOUBLE') || data['user_type']=='ATV'){
					return false;
				}
			}else if(busicode == '1064'){//促销
				if(data['user_type'] == 'ATV' && data['status'] == 'CUSTLINE'){
					return false;
				}
			}else if(busicode == '1133'){//模拟剪线
				if(data['user_type'] != 'ATV' || data['status'] == 'CUSTLINE'){
					return false;
				}
			}else if(busicode == '1134'){//模拟恢复
				if(data['user_type'] != 'ATV' || data['status'] != 'CUSTLINE'){
					return false;
				}
			}else if(busicode == '1323'){
//				if(Ext.isEmpty(data['stop_date'])){
				if(data['status'] != 'REQSTOP'){
					return false;
				}
			}else if(busicode == '1123'){
				if(Ext.isEmpty(data['card_id'])){
					return false;
				}
			}else if(busicode == '1221'){//恢复状态 休眠、关模隔离、待销户 
				if(data['status'] != 'DORMANCY' && data['status'] != 'ATVCLOSE' && data['status'] != 'WAITLOGOFF'){
					return false;
				}
			}else if(busicode === '1303'){//取消授权
				if (data['user_type'] != 'DTV' || Ext.isEmpty(data['card_id']))
					return false;
			}else if(busicode === '2002'){//机卡互换,宽带不给换
				if (data['user_type'] != 'DTV'){
					return false;
				}
				return true;
			}else if(busicode == '1193'){ // 免费终端只对副终端开放
				if (data["terminal_type"] != 'FZD'){
					return false;
				}
				return true;
			}else if(busicode == '1022'){//用户转户
				var devStore = App.main.infoPanel.custPanel.custDeviceGrid.custDeviceStore;
				//当前用户的设备产权是 广电的,不能执行
				var gdDev = false;
				for (var index = 0; index < devStore.getCount(); index++) {
					var rec = devStore.getAt(index);
					if(rec.get('device_code') == data.stb_id ){
						if(rec.get('ownership') == 'GD'){
							gdDev = true;
							break;
						}
					}
				}
				if(gdDev){
					return false;
				}
				if(data['status'] != 'ACTIVE' && data['status'] !='OUNSTOP'){
					return false;
				}else if(data['terminal_type'] == 'ZZD'){
					return false;
				}
				return true;
			}
		}
/**************************************用户信息面板结束************************************************/

/**************************************客户设备信息开始************************************************/
		/*else if(panelName == 'C_DEVICE'){
			//设备报停后没任何操作
			if(busicode == '1008'){//设备回收
				//设备使用中或者智能卡有配对的机，不允许回收
				if(data['status'] == 'USE' || data['pair_stb_device_id']||data['ownership']=='CUST')
					return false;
			}else if(busicode == '1011'){//取消挂失
				if(data['loss_reg'] == 'F')//挂失的才能取消挂失
					return false;
			}else if(busicode == '1014'){//挂失
				if(data['loss_reg'] =='T' || data['status']=='USE')//没有挂失、状态为报停或空闲的才能挂失
					return false;
			}else if(busicode == '1013'){
				if(data['ownership'] != 'GD' || App.getCust().status=='RELOCATE'||data['pair_stb_device_id'] !=null)
					return false;
			}else if(busicode == '1231'){
				if(data['pair_stb_device_id'] !=null)
					return false;
			}else if(busicode == '1201'){
				if(data['pair_stb_device_id'] !=null)
					return false;
			}else if(busicode == '1091'){//包换期购买
				if(data['device_type'] !='STB')
					return false;
			}
		}*/else if(panelName.indexOf('C_PROMFEE')>=0){//客户套餐信息
			if(busicode == '1687'){
				var create_time = Date.parseDate(data.create_time,'Y-m-d H:i:s');
				var now = Date.parseDate(nowDate().format('Y-m-d'),'Y-m-d');
				if(data.status != 'ACTIVE' || now.getTime() <= create_time.getTime() ){
					return false;
				}
			}
		}
/**************************************客户设备信息结束************************************************/		
		
/**************************************用户产品信息开始************************************************/
		else if(panelName.indexOf('U_PROD')>=0){//用户产品信息
			if(busicode == '131'){
				if( (data['is_pay'] == 'F' && (data['prod_type'] == 'CPKG' || data['prod_type'] == 'BASE') && data["package_id"] == null)
					|| (data['prod_type'] == 'SPKG' && data["package_id"] == null) ){
					return true;
				}else{
					return false;
				}
			}
			// 如果是套餐所有按钮在基本产品列表中都不显示
			if(data["package_id"]!=null || (data['status'] != 'ACTIVE' && data['status'] != 'INSTALL')){
				return false; 
			}
			
			if( (busicode == '1027' || busicode == '109' || busicode == '110' || busicode == '100') && data['is_pay'] == 'F' ){
				return false;
			}
			
			/*if(busicode == '1027'){//产品退订
				var userId = data['user_id'];
				var acctItemData = App.getAcctItemByProdId(data['prod_id'],userId);
				var flag = true;
				//欠费账户不能退订
				if(acctItemData){
					if(acctItemData.real_balance < 0 && acctItemData.order_balance != -acctItemData.real_balance)
						flag = false;
				}
				if(!flag) return false;
				
				if(data['is_base'] == 'T'){//基本包
					var store = App.getApp().main.infoPanel.getUserPanel().prodGrid.baseProdGrid.getStore();
					flag = false;
					store.each(function(record){
						//当前行的产品为基本包且有其他基本包，当前基本包产品才能退订
						if(data['prod_id']!=record.get('prod_id') 
							&& record.get('is_base') == 'T'){
							flag = true;
							return false;
						}
					},store);
					if(!flag)return false;
				}
			}else */
			if(busicode == '1028'){//资费变更
				var userId = data['user_id'];
				var acctItemData = App.getAcctItemByProdId(data['prod_id'],userId);
				if(acctItemData){
					if(data['billing_cycle']>1 && acctItemData['real_balance'] < 0)
						return false;
				}
				if(data['just_for_once'] == 'T' || App.getCust().status=='RELOCATE' || data['status'] == 'TMPPAUSE' || !Ext.isEmpty(data['next_tariff_name']))
					return false;
			}else if(busicode == '1081'){//修改到期日
				if(data['billing_cycle']<3)
					return false;
			}else if(busicode == '1036'){//修改到期日
				if(data['tariff_rent']==0 || data.status !='ACTIVE')
					return false;
			}else if(busicode == '1232'){//取消资费变更
				if( Ext.isEmpty(data['next_tariff_id']) )
					return false;
			}else if(busicode == '1034'){//协议缴费
				var userRecord = App.main.infoPanel.getUserPanel().userGrid.getSelectionModel().getSelected();
				if(userRecord && userRecord.get("str19") == 'T' && data["is_base"] == 'T' && data["tariff_rent"] == 0){
					return false;
				}
				if(data['is_zero_tariff'] == 'F' || data['is_invalid_tariff'] == 'T')//不是零资费产品,或资费失效，不能协议缴费
					return false;
			}else if(busicode == '1072'){//修改失效日期
				if(data['pkg'] == true || data['is_base'] == 'T' || data['next_bill_date'])//套餐，基本包，和非包月产品不能修改
					return false;
			}else if(busicode == '1324'){//产品暂停
				if(data['status'] == 'TMPPAUSE' || data['package_sn'])
					return false;
			}else if(busicode == '1325'){//产品恢复
				if(data['status'] != 'TMPPAUSE')
					return false;
			}else if(busicode == '1127'){//宽带升级
				if(data['serv_id'] != 'BAND'  || data['status'] == 'REQSTOP')
					return false;
			}else if(busicode == '1128'){	//更换动态资源
				
			}else if(busicode == '1983'){	//修改产品预开通日期
				if(Ext.isEmpty(data.pre_open_time) || data.status != 'PREAUTH'){
					return false;
				}
			}else if(busicode == '1984'){
				//非基本产品不行、0资费不行、点播的也不行
				if (data.tariff_rent == 0 || data.billing_cycle > 1
						|| data.is_base != 'T' || data.serv_id == 'ITV') {
					return false;
				}
			}else if(busicode == '1099'){//银行扣费变更
				var acctBank=App.getApp().data.custFullInfo.acctBank;
				if(data['is_base'] == 'T' || data['p_bank_pay'] != 'T' || acctBank.status != 'ACTIVE' ){//产品属性p_bank_pay = is_bank_pay
					if(acctBank.status != 'ACTIVE' && data['is_bank_pay']== 'T'){
						return true;
					}
					return false;
				}
			}
		}
/**************************************用户产品信息结束************************************************/
		else if(panelName.indexOf('P_ACCT')>=0 || panelName.indexOf('P_BUSI')>=0){
			//必须是本人,状态正常
			if (data['status'] =='INVALID'){
				return false;
			}
			//单位客户，显示的缴费信息，只是显示无法操作
			if(data['acct_type'] =='UNIT'){
				return false;
			}
			//模拟转数后，用户不存在时，仅"修改账务日期","修改出票方式"功能发开
			if( busicode !='1226' && busicode !='1225' && data['is_logoff'] == 'T'){
				return false;
			}
			if(busicode == '1051'){//冲正(预存费用和业务费用相同的限制)
				var date = Date.parseDate(data['create_time'],'Y-m-d H:i:s');
				date = date.add(Date.DAY,30);
				if(nowDate() > date || App.getData().optr.optr_id != data['optr_id']){
					return false;
				}
				
				if(data["real_pay"] < 0){
					return false;
				}
				
			}else if(busicode == '1054'){//银行冲正
				var cDate = Date.parseDate(data['create_time'],'Y-m-d H:i:s');
				var sDate = Date.parseDate('2013-10-01 00:00:00','Y-m-d H:i:s');
				if(data['pay_type'] == 'YHDK' && data['optr_id'] == '6' && cDate > sDate){
					return true;
				}
				return false;
			}else if(busicode == '1225'){//修改出票方式
				//不是当前操作员做的业务,不是现金支付,集团缴费业务都不可以修改.
				if(App.getData().optr.optr_id != data['optr_id']  || data['pay_type'] != 'XJ' || data['busi_code'] == '1039' ){//非当前操作员不给操作
					return false;
				}
				//xs开头的都是手工票,手工票都可以修改
				if(!Ext.isEmpty(data.invoice_id) && data.invoice_id.indexOf('xs')  ==0){
					return true;
				}
				//没有打印过的可以修改,出票方式和发票ID,CODE都为空,正行的,如果出票方式为空,发票代码号码bookid也应该都为空
				if( Ext.isEmpty(data.invoice_mode) ){// && Ext.isEmpty(data.invoice_id)
					return true;
				}
				//剩下的情况
				if ( data['finance_status'] != 'IDLE' )
					return false;
			}else if(busicode == '1501'){//手工发票号修改
				//不是定额发票
				if ( data['invoice_mode'] != 'M' || App.getData().optr.optr_id != data['optr_id']){
					return false;
				}
			}else if(busicode == '2345'){//定额发票调账
				//不是定额发票
				if ( data['invoice_mode'] != 'Q')
					return false;
			}else if(busicode == '1226'){//修改账务日期
				var finance_status = data['finance_status'];
				var dataRight = data['data_right'];
				var pay_type = data['pay_type'];
				//没此功能
				if(Ext.isEmpty(dataRight)){
					return false;
				};
				//功能修改需求见mantis 252
				if(dataRight == 'DEPT'){//营业厅
					if(Ext.isEmpty(finance_status)){
						return false;
					}
					if(App.getData().optr.dept_id == data['dept_id']){
						//没录发票，账务日期和系统默认账务不一致
						if(data['acct_date'] < App.baseAcctDate){
							return false;
						};
						//营业厅级只可修改未扎账的账务日期
						if(finance_status != 'IDLE'){
							return false;
						}
					}else{
						return false;
					}
				}else if(dataRight == 'COUNTY'){//分公司
					if(Ext.isEmpty(finance_status) && data['acct_date'] < App.baseAcctDate){
						return false;
					}
					//分公司级除已核销发票不能修改账务日期外，其他情况均可修改账务日期
					if(finance_status == 'CLOSE')
						return false;
				}
			}else if( busicode == '2224'){ //取消打印标记
				if (data['is_doc'] == 'T'){
					return false;
				}	
//				var date = Date.parseDate(data['create_time'].substring(0,10),'Y-m-d');
//				var cfgData = App.getApp().findCfgData('PRINT_DATE_CFG');
//				if(Ext.isEmpty(cfgData))
//					return false;
//				date = date.add(Date.DAY,parseInt(cfgData.config_value));
//				if(!Ext.isEmpty(data['invoice_id']) || nowDate().format('Y-m-d') > date.format('Y-m-d')){
//					return false;
//				}
				if(!Ext.isEmpty(data['invoice_id'])){
					return false;
				}
				if (data['is_doc'] == 'N'){
					return true;
				}else{
					return false;
				}					
			}else if(busicode == '2223'){ //打印标记
				if (data['is_doc'] == 'T' || data['status'] != 'PAY'){
					return false;
				}	
//				var date = Date.parseDate(data['create_time'].substring(0,10),'Y-m-d');
//				var cfgData = App.getApp().findCfgData('PRINT_DATE_CFG');
//				if(Ext.isEmpty(cfgData))
//					return false;
//				date = date.add(Date.DAY,parseInt(cfgData.config_value));
//				if(!Ext.isEmpty(data['invoice_id']) || nowDate().format('Y-m-d') > date.format('Y-m-d')){
//					return false;
//				}
				if(!Ext.isEmpty(data['invoice_id'])){
					return false;
				}
				
				if (data['is_doc'] == 'F'){
					return true;
				}else{
					return false;
				}
			}else if(busicode =='1151'){//修改业务员
				/*var date = Date.parseDate(data['create_time'].substring(0,10),'Y-m-d');
				var cfgData = App.getApp().findCfgData('ALLOW_DAY_MOD_OPTR');
				if(cfgData){
					date = date.add(Date.DAY,parseInt(cfgData.config_value));
				if(nowDate().format('Y-m-d') > date.format('Y-m-d'))
					return false;
				}*/
			}
		}else if(panelName === 'A_ITEM'){//账目信息面板
			//客户状态资料隔离，用户状态 休眠和关模隔离 都不能进行账目操作,待销户
			if(data['status'] == 'DATACLOSE'
						|| data['status'] == 'DORMANCY'
						|| data['status'] == 'ATVCLOSE' || data['status'] == 'WAITLOGOFF' ){
				return false;	
			}
			if( busicode == '1041'){//退款
				if(data['can_refund_balance'] == 0)//可退余额要大于0
					return false;
			}else if( busicode == '1042'){//调账
				if(data['allow_adjust'] !='T' || App.getCust().status=='RELOCATE')
					return false;
			}else if( busicode == '1086'){//调账可退
				if(data['allow_adjust'] !='T' || App.getCust().status=='RELOCATE' || data['owe_fee'] != 0)
					return false;
			}else if(busicode == '1088'){//小额减免
				if(data['active_balance']+data['order_balance']-data['owe_fee']-data['real_fee']<0){
					return true;
				}else{
					return false;
				}
			}else if( busicode == '1043'){//转账
				
				if(App.getCust()['county_id'] == '9005' && data['can_trans_balance'] > 0
					&& data['prod_id'] == '2728' && App.getCust().status != 'RELOCATE'){
					return true;
				}
				
				if (data['acctitem_type'] == 'PUBLIC'
						&& data['allow_tran'] == 'F'
						|| App.getCust().status == 'RELOCATE') {
					return false;
				}
				if(data['can_trans_balance'] == 0)//可转余额要大于0
					return false;
			}else if( busicode == '1073'){//欠费抹零
				if (data['billing_type'] != 'BY'
						|| data['owe_fee'] > ((data['tariff_rent'] / 30) * data['ownFeeNumber'])
						|| data['owe_fee'] <= 0)
					return false;
			}
		}else if(panelName === 'ACCTITEM_INACTIVE'){
			if(busicode == '2040'){
				if(data['balance'] <=0){
					return false;
				}
			}
		}else if(panelName === 'C_PACKAGE'){

		}else if(panelName === 'C_UNIT'){
		
		}else if(panelName === 'A_ACCT'){
		
		}else if(panelName ==='D_TASK'){
			if(busicode =='2261'){//工单作废
				if(data['task_status'] != 'CREATE' )
					return false;
			}
		}else if(panelName ==='P_FEE_PAY'){
			if(busicode =='2263'){//支付回退
//				if(nowDate().format('Y-m') != data['create_time'].substring(0,7)){
				if(Ext.util.Format.date(nowDate(),'Y-m-d') !== data['create_time'].substring(0,10)){
					return false;
				}
				if (data['optr_id'] != App.getData().optr.optr_id || data['is_valid'] == 'F'){
					return false;
				}
			}else if(busicode =='2264'){//隔月支付回退
//				if(nowDate().format('Y-m') == data['create_time'].substring(0,7)){
//					return false;
//				}
//				if (data['optr_id'] != App.getData().optr.optr_id || data['is_valid'] == 'F'){
//					return false;
//				}
			}
		}else if(panelName === 'D_BUSI'){
			if(busicode == '1163'){//重打业务单只能打当前操作员打印过的
				/*if (data['optr_id'] != App.getData().optr.optr_id){
					return false;
				}*/
			}
		}else if(panelName === 'D_INVOICE'){
			if (data['optr_id']!=App.getData().optr.optr_id){
				return false;
			}
			if(busicode == '1063'){//发票打印
				//机打票才能打印
				if(data['invoice_mode'] !='A' || data['finance_status']!='IDLE' || data['status']=='INVALID')
					return false;
			}if(busicode == '1161'){//发票作废
				if(data['finance_status']!='IDLE')
					return false;
			}else if(busicode == '1061'){//发票修改
				//未结账发票才能修改||data['status']=='INVALID'
				if(data.invoice_mode == 'M'){//TODO 改为手工票不给修改
					return false;
				}
				if(data['finance_status']!='IDLE' || data['status']=='INVALID')
					return false;
			}else if(busicode == '1163'){//收费清单换发票
				//限于潜江地区
				if (App.getCust()['county_id'] != '9005'
						|| data['finance_status'] != 'IDLE'
						|| data['doc_type'] !='6'
						|| data['status'] == 'INVALID' || data['fee_invoice_id']
						|| data['fee_invoice_status'] == 'ACTIVE')
					return false;
			}else if(busicode == '1164'){
				if(App.getCust()['county_id'] != '9005' || data['fee_invoice_status'] == 'INVALID')
					return false;
			}
		}else if(panelName == 'D_DONE'){
			if( busicode == '1032'){//业务回退
				if (data['status']=='INVALID' || data['cancel']=='F' || App.getData().optr.optr_id != data['optr_id'] 
					|| Ext.util.Format.date(nowDate(),'Y-m-d') !== data['done_date'].substring(0,10))
					return false;
			}else if( busicode == '1052'){//修改业务费用
				if(!Ext.isEmpty(data['fee_id']) && data['fee_id'] =='5051' && data['busi_code'] !='1052'){
					return true;
				}
				if (data['status']=='INVALID' || (data['busi_fee']=='F' && data['busi_code'] !='1007' 
				&& data['busi_code'] !='1008'  && data['busi_code'] !='1013' && data['busi_code'] !='1009'
				 && data['busi_code'] !='1108' && data['busi_code'] !='1109'
				) || App.getCust().status=='RELOCATE' || (Ext.isEmpty(data['real_pay']) && Ext.isEmpty(data['fee_id'])) )
					return false;
			}
		}else if(panelName == 'B_INVALID'){
			if(busicode == '1233'){
				if(data['can_refund']!='T' || data['invalid_fee']<=0){
					return false;
				}
			}
		}else if(panelName == 'B_BILL'){
			if(busicode == '1234'){
				if(data['come_from']=='4'){
					return false;
				}
				//套餐缴费的额外账单
				if(data['status']=='0'&&data['come_from']=='5')
					return true;
			    //多月产品未出帐,可以作废
//				if( (data['status'] != '1') && (data['status']!='0' || data['fee_flag']!='DY') )
				//0001062: 包月产品当月账单允许作废
				if( data['status'] != '1' && data['status']!='0')
					return false;
			}
		}else if(busicode == '1080'){//促销回退
			if(data['status'] != 'ACTIVE' || nowDate().format('Y-m') != data['create_time'].substring(0,7)){//当月促销
				return false;
			}
		}else if(busicode == '1180'){//更换促销节目
			var date = Date.parseDate(data['create_time'].substring(0,10),'Y-m-d');
			var cfgData = App.getApp().findCfgData('CHANGE_PROMOTION_PROD');
			if(Ext.isEmpty(cfgData))
				return false;
			date = date.add(Date.DAY,parseInt(cfgData.config_value));
			if(data['status'] != 'ACTIVE' ||data['is_necessary'] == 'T' || nowDate().format('Y-m-d') > date.format('Y-m-d')){//7天之内
				return false;
			}
		}else if(busicode == '1116'){//定额退款
			if(data['fee_type'] != 'DEZS'){
				return false;
			}
		}
		return true;
	}
});
