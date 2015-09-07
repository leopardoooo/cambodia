package com.ycsoft.business.service.impl;

import static com.ycsoft.commons.constants.SystemConstants.ACCT_TYPE_SPEC;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.config.TDeviceChangeReason;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustDevice;
import com.ycsoft.beans.core.job.JUserStop;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.core.user.FillUSerDeviceDto;
import com.ycsoft.beans.device.RDeviceFee;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.core.OrderComponent;
import com.ycsoft.business.component.task.SnTaskComponent;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.core.fee.FeeInfoDto;
import com.ycsoft.business.dto.core.prod.DisctFeeDto;
import com.ycsoft.business.dto.core.prod.PromotionDto;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.dto.core.user.UserInfo;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.business.service.IUserService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
@Service
public class UserServiceSN extends BaseBusiService implements IUserService {
	@Autowired
	private OrderComponent orderComponent;
	@Autowired
	private CProdOrderDao cProdOrderDao;
	@Autowired
	private SnTaskComponent snTaskComponent;
	
	public void createUser(CUser user, String deviceCode, String deviceType, String deviceModel, String deviceBuyMode,
			FeeInfoDto deviceFee) throws Exception {
		CCust cust = getBusiParam().getCust();
		this.validAccount(user.getLogin_name());
		this.canOpenUser(cust);
		doneCodeComponent.lockCust(cust.getCust_id());
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		// 获取客户信息
		
		int num = getUserCount(cust, user.getUser_type());
		openSingle(cust, user, doneCode, deviceCode, deviceType, deviceModel, deviceBuyMode, deviceFee, num);
		
		// 设置拦截器所需要的参数
		getBusiParam().resetUser();
		getBusiParam().addUser(user);
		saveAllPublic(doneCode, getBusiParam());

	}
	
	private void canOpenUser(CCust cust) throws Exception {
		if(cust.getStatus().equals(StatusConstants.PREOPEN)){
			throw new ServicesException(ErrorCode.CustStatusIsNotOpenUser);
		}
	}
	
	@Override
	public void createUserBatch(List<UserInfo> userList, String stopType) throws Exception {
		CCust cust = getBusiParam().getCust();
		this.canOpenUser(cust);
		doneCodeComponent.lockCust(cust.getCust_id());
		Integer doneCode = doneCodeComponent.gDoneCode();
		// 获取客户信息
		for (UserInfo userInfo:userList){
			for (int i=0;i<userInfo.getUser_count();i++){
				CUser user = new CUser();
				user.setUser_type(userInfo.getUser_type());
				String deviceType;
				if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
					deviceType = SystemConstants.DEVICE_TYPE_MODEM;
				} else {
					deviceType = SystemConstants.DEVICE_TYPE_STB;
				}
				
				String deviceModel = userInfo.getDevice_model();
				String deviceBuyMode = userInfo.getDevice_buy_mode();
				FeeInfoDto deviceFee = new FeeInfoDto();
				deviceFee.setFee_id(userInfo.getFee_id());
				deviceFee.setFee(userInfo.getFee());
				
				user.setStop_type(stopType);
				
				int num = getUserCount(cust, user.getUser_type());
				this.openSingle(cust, user, doneCode, null, deviceType, deviceModel, deviceBuyMode, deviceFee, num);
				
			}
		}
		saveAllPublic(doneCode, getBusiParam());
	}

	private int getUserCount(CCust cust, String userType) throws Exception {
		int num = userComponent.queryMaxNumByLoginName(cust.getCust_id(), cust.getCust_no(), userType);
		if(userType.equals(SystemConstants.USER_TYPE_BAND)){
			num += SystemConstants.USER_TYPE_BAND_NUM;
		}else if(userType.equals(SystemConstants.USER_TYPE_OTT)){
			if(num == 0){
				num += SystemConstants.USER_TYPE_OTT_NUM;
			}else{
				num += 1;
			}
		}
		return num;
	}

	private void openSingle(CCust cust, CUser user, Integer doneCode, String deviceCode, String deviceType,
			String deviceModel, String deviceBuyMode, FeeInfoDto deviceFee, int num) throws Exception, JDBCException {
		String custId = cust.getCust_id();
		
		String user_id = userComponent.gUserId();
		// 创建账户信息
		String acctId = acctComponent.createAcct(custId, user_id, ACCT_TYPE_SPEC, null);
		// 创建用户信息
		user.setUser_id(user_id);
		user.setAcct_id(acctId);
		user.setCust_id(custId);
		user.setStr10(deviceBuyMode);//用户开始时设备购买方式
		DeviceDto device = null;
		if (StringHelper.isNotEmpty(deviceCode)){
			device = deviceComponent.queryDeviceByDeviceCode(deviceCode);
			setUserDeviceInfo(user, device);
		} else {
			device = new DeviceDto();
			device.setDevice_type(deviceType);
			device.setDevice_model(deviceModel);
		}
		
		//设置OTT用户终端类型和默认用户名
		String userType = user.getUser_type();
		if (userType.equals(SystemConstants.USER_TYPE_OTT)) {
			if (cust.getCust_type().equals(SystemConstants.CUST_TYPE_NONRESIDENT)){
				user.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_ZZD);
			} else {
				List<CUser> userList = userComponent.queryUserByCustId(cust.getCust_id());
				if (userList != null){
					for (CUser cu:userList){
						if(cu.getUser_type().equals(user.getUser_type())){
							user.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_FZD);
							break;
						}
					}
				}
				
				if (StringHelper.isEmpty(user.getTerminal_type())){
					user.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_ZZD);
				}
			}
			
			/*if (StringHelper.isEmpty(user.getLogin_name()))
				user.setLogin_name("supertv"+user.getUser_id());*/
			user.setLogin_name(cust.getCust_no()+""+num);
		}else if(userType.equals(SystemConstants.USER_TYPE_BAND)){
			String domainName = custComponent.gCustNoByAddr(cust.getAddr_id(), null);
			user.setLogin_name(cust.getCust_no()+"0"+num+"@"+domainName);
			user.setPassword(cust.getPassword());
		}
		
		TDeviceBuyMode buyModeCfg = busiConfigComponent.queryBuyMode(deviceBuyMode);
		//处理设备和授权
		if (!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
			String ownership = SystemConstants.OWNERSHIP_GD;
			if (buyModeCfg!= null && buyModeCfg.getChange_ownship().equals(SystemConstants.BOOLEAN_TRUE))
				ownership = SystemConstants.OWNERSHIP_CUST;
			this.buyDevice(device, deviceBuyMode,ownership, deviceFee, getBusiParam().getBusiCode(), cust, doneCode);
			
			//非自购模式 设置协议截止日期，读取模板配置数据
			if(buyModeCfg!= null && !buyModeCfg.getBuy_mode().equals(SystemConstants.BUSI_BUY_MODE_BUY) && StringHelper.isNotEmpty(deviceModel)){
				Integer months = Integer.parseInt( userComponent.queryTemplateConfig(TemplateConfigDto.Config.PROTOCOL_DATE_MONTHS.toString()) );
				user.setProtocol_date( DateHelper.addTypeDate(DateHelper.now(), "MONTH", months) );
			}
//			if(buyModeCfg != null){
//				user.setStr9(buyModeCfg.getBuy_mode());
//			}
		}
		
		userComponent.createUser(user);
		
		
		if (user.getStatus().equals(StatusConstants.ACTIVE)){
			createUserJob(user, custId, doneCode);
		}
	}
	
	@Override
	public void saveFillDevice(List<FillUSerDeviceDto> deviceList) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		CCust cust = null;
		for (FillUSerDeviceDto userDevice:deviceList){
			CUser user = userComponent.queryUserById(userDevice.getUser_id());
			DeviceDto device = deviceComponent.queryDeviceByDeviceCode(userDevice.getDevice_id());
			setUserDeviceInfo(user, device);
			userComponent.updateDevice(doneCode, user);
			//发送授权
			this.createUserJob(user, user.getCust_id(), doneCode);
			authComponent.sendAuth(user, null, BusiCmdConstants.REFRESH_TERMINAL, doneCode);
			//处理设备
			TDeviceBuyMode buyModeCfg = busiConfigComponent.queryBuyMode(user.getStr10());
			String ownership = SystemConstants.OWNERSHIP_GD;
			if (buyModeCfg.getChange_ownship().equals(SystemConstants.BOOLEAN_TRUE))
				ownership = SystemConstants.OWNERSHIP_CUST;
			if (cust == null)
				cust = custComponent.queryCustById(user.getCust_id());
			this.buyDevice(device, user.getStr10(), ownership, null, getBusiParam().getBusiCode(), cust, doneCode);
		}
	}

	/**
	 * 验证ott_mobile账户是否唯一
	 * @param name
	 * @throws Exception
	 */
	public void validAccount(String name) throws Exception {
		boolean flag = userComponent.validAccount(name);
		if(flag){
			throw new ServicesException(ErrorCode.UserLoginNameIsExists);
		}
	}
	
	public List<TDeviceChangeReason> queryDeviceChangeReason() throws Exception {
		return userComponent.queryDeviceChangeReason();
	}
	
	/**
	 * 用户更换设备
	 */
//	public void saveChangeDevice(String userId, String deviceCode, String devcieBuyMode, 
//			FeeInfoDto deviceFee, String changeReason, boolean reclaim) throws Exception {
	public void saveChangeDevice(String userId, String deviceCode, String reasonType) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		CCust cust = getBusiParam().getCust();
		doneCodeComponent.lockCust(cust.getCust_id());
		CUser oldUser = userComponent.queryUserById(userId);
		CUser user = userComponent.queryUserById(userId);
		
		String oldDeviceCode = user.getStb_id();
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND))
			oldDeviceCode = user.getModem_mac();
		DeviceDto oldDevice = deviceComponent.queryDeviceByDeviceCode(oldDeviceCode);
		DeviceDto device = deviceComponent.queryDeviceByDeviceCode(deviceCode);
		deviceComponent.checkChangeDevice(oldDevice, device);
		
		//修改用户设备信息
		setUserDeviceInfo(user, device);
		userComponent.updateDevice(doneCode, user);
		
		TDeviceChangeReason changeReason = userComponent.queryChangeReasonByType(reasonType);
		
		if(changeReason.getIs_charge().equals(SystemConstants.BOOLEAN_TRUE)){
			TDeviceBuyMode buyMode = busiConfigComponent.queryBuyMode(SystemConstants.BUSI_BUY_MODE_BUY);
			RDeviceFee deviceFee = deviceComponent.queryDeviceFee(device.getDevice_type(), device.getDevice_model(), buyMode.getBuy_mode()).get(0);
			FeeInfoDto feeInfo = new FeeInfoDto();
			BeanUtils.copyProperties(feeInfo, deviceFee);
			feeInfo.setFee(deviceFee.getFee_value());
			//处理设备购买和回收
			this.buyDevice(device, SystemConstants.BUSI_BUY_MODE_BUY, oldDevice.getOwnership(), feeInfo, getBusiParam().getBusiCode(), cust, doneCode);
		}
		
		if(changeReason.getIs_reclaim().equals(SystemConstants.BOOLEAN_TRUE)){
			deviceComponent.saveDeviceReclaim(doneCode,  getBusiParam().getBusiCode(),
					oldDevice.getDevice_id(), cust.getCust_id(), reasonType);
		}
		if(changeReason.getIs_lost().equals(SystemConstants.BOOLEAN_TRUE)){
			deviceComponent.saveLossDevice(doneCode, getBusiParam().getBusiCode(), oldDeviceCode);
		}
		
		//处理授权
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_OTT)){
			authComponent.sendAuth(user, null, BusiCmdConstants.CHANGE_USER, doneCode);
		} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTT)){
			//设备回收，发设备销毁指令
			if(changeReason.getIs_reclaim().equals(SystemConstants.BOOLEAN_TRUE)){
				authComponent.sendAuth(oldUser, null, BusiCmdConstants.PASSVATE_TERMINAL, doneCode);
			}else{
				//设备不回收，发产品减指令
				List<CProdOrder> orderList = orderComponent.queryOrderProdByUserId(userId);
				if(orderList.size() > 0)
					authComponent.sendAuth(oldUser, orderList, BusiCmdConstants.PASSVATE_PROD, doneCode);
			}
			authComponent.sendAuth(user, null, BusiCmdConstants.ACCTIVATE_TERMINAL, doneCode);
			authComponent.sendAuth(user, null, BusiCmdConstants.REFRESH_TERMINAL, doneCode);
		}
		// 设置拦截器所需要的参数
		getBusiParam().resetUser();
		getBusiParam().addUser(user);
		saveAllPublic(doneCode, getBusiParam());
	}

	@Override
	public void createUser(CUser user, String deviceBuyMode, FeeInfoDto deviceFee) throws Exception {
		// TODO Auto-generated method stub
		
	}


	/**
	 * 创建客户，并且自动创建一个用户
	 * 
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_passwd 用户密码
	 * @param user_name 用户名称
	 * @param user_rank 用户等级
	 * @param telephone 手机号码
	 * @param email 邮箱
	 * @throws Exception
	 */
	public void createOttMobileUser(String userId, String userPassword, String userName, String userRank, String telephone, String email)throws Exception{
		
	}


	@Override
	public void editUser(List<CUserPropChange> propChangeList) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveEzdtoFzd(List<CUserPropChange> propChangeList, String prodSn, String newTariffId) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void editStb(String stbId, String cardId) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveRemoveUser(String userId,String banlanceDealType,String reclaim,Integer cancelFee,Integer refundFee, String transAcctId, String transAcctItemId) throws Exception {
		// TODO Auto-generated method stub
		//获取客户用户信息
		CCust cust = getBusiParam().getCust();
		String  custId = cust.getCust_id();
		doneCodeComponent.lockCust(custId);
		
		List<CUser> userList = getBusiParam().getSelectedUsers();
		CUser user = null;
		for(CUser u : userList){
			if(userId.equals(u.getUser_id())){
				//user = u;
				user=userComponent.queryUserById(userId);
			}
		}
		if(user == null){
			throw new ServicesException(ErrorCode.CustDataException);
		}
		if(!user.getCust_id().equals(custId)){
			throw new ServicesException(ErrorCode.CustDataException);
		}
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		//生成销帐任务
		//int jobId = jobComponent.createCustWriteOffJob(doneCode, custId,BOOLEAN_TRUE);

		List<String> devoceList = new ArrayList<String>();
		if(user.getStb_id() != null){
			devoceList.add(user.getStb_id());
		}
		if(user.getCard_id()!=null){
			devoceList.add(user.getCard_id());
		}
		if(user.getModem_mac() != null){
			devoceList.add(user.getModem_mac());
		}
		
		List<CCustDevice> buyModeList = null;
		if(devoceList.size()>0){
			buyModeList = custComponent.findBuyModeById(custId, devoceList.toArray(new String[devoceList.size()]));
		}
		Map<String, CCustDevice> map = CollectionHelper.converToMapSingle(buyModeList, "device_code");
		Map<String, UserDto> userMap = new HashMap<String, UserDto>();
		
		if(CollectionHelper.isNotEmpty(userList)){
			List<UserDto> allUsers = userComponent.queryUser(custId);
			userMap = CollectionHelper.converToMapSingle(allUsers, "user_id");
		}
		
		//处理用户销户
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("user_type", user.getUser_type_text());
		info.put("user", userMap.get(userId));
		
		//销户后保存原来的设备购买方式
		user.setStb_buy(map.get(user.getStb_id()) != null?map.get(user.getStb_id()).getBuy_mode():null);
		user.setCard_buy(map.get(user.getCard_id()) != null?map.get(user.getCard_id()).getBuy_mode():null);
		user.setModem_buy(map.get(user.getModem_mac()) != null?map.get(user.getModem_mac()).getBuy_mode():null);
		
		
		//柬埔寨 用户销户 回收设备
		if(SystemConstants.BOOLEAN_TRUE.equals(reclaim)){
			DeviceDto stbDevice = null;
			//回收机顶盒
			if(StringHelper.isNotEmpty(user.getStb_id())){
				stbDevice = deviceComponent.queryDeviceByDeviceCode(user.getStb_id());
				reclaimDevice(stbDevice.getDevice_id(), null,SystemConstants.RECLAIM_REASON_XHTH, 0, cust, doneCode, busiCode);
			}
			if(StringHelper.isNotEmpty(user.getCard_id()) && (stbDevice != null && (stbDevice.getPairCard() == null  || !user.getCard_id().equals(stbDevice.getPairCard().getCard_id())))){
				DeviceDto device = deviceComponent.queryDeviceByDeviceCode(user.getCard_id());
				reclaimDevice(device.getDevice_id(), null,SystemConstants.RECLAIM_REASON_XHTH, 0, cust, doneCode, busiCode);
			}
			if(StringHelper.isNotEmpty(user.getModem_mac()) && (stbDevice != null && (stbDevice.getPairModem() == null  || !user.getModem_mac().equals(stbDevice.getPairModem().getModem_mac())))){
				DeviceDto device = deviceComponent.queryDeviceByDeviceCode(user.getModem_mac());
				reclaimDevice(device.getDevice_id(), null,SystemConstants.RECLAIM_REASON_XHTH, 0, cust, doneCode, busiCode);
			}
												
		}
		//是否高级权限
		boolean isHigh=orderComponent.isHighCancel(busiCode);
		List<CProdOrderFee> orderFees=new ArrayList<>();
		//检查数据
		List<CProdOrderDto> cancelList=checkCancelProdOrderParm(orderFees,isHigh, userId, cancelFee,refundFee);
		
		if(refundFee<0){
			//记录未支付业务
			doneCodeComponent.saveDoneCodeUnPay(custId, doneCode, this.getOptr().getOptr_id());
			//保存缴费信息
			feeComponent.saveCancelFee(cancelList,orderFees, this.getBusiParam().getCust(), doneCode, this.getBusiParam().getBusiCode());
		}
		if(cancelFee-refundFee!=0){
			//余额转回公用账目
			acctComponent.saveCancelFeeToAcct(orderFees, custId, doneCode, this.getBusiParam().getBusiCode());
		}
		
		List<CProdOrder> cancelResultList=new ArrayList<>();
		
		for(CProdOrderDto dto:cancelList){
			//执行退订 返回被退订的用户订单清单
			cancelResultList.addAll(orderComponent.saveCancelProdOrder(dto, doneCode));
		}
		
		//直接解除授权，不等支付（因为不能取消）
		if(cancelResultList.size()>0){
			authComponent.sendAuth(user, cancelResultList, BusiCmdConstants.PASSVATE_PROD, doneCode);
		}
		authComponent.sendAuth(user, cancelResultList, BusiCmdConstants.DEL_USER, doneCode);
		
		//记录用户到历史表
		userComponent.removeUserWithHis(doneCode, user);
		
		//doneCodeComponent.saveDoneCodeInfo(doneCode, custId, null, info);
		saveAllPublic(doneCode,getBusiParam());
	}

	
	
	private List<CProdOrderDto> checkCancelProdOrderParm(List<CProdOrderFee> orderFees,boolean isHigh,String userId,Integer cancelFee,Integer refundFee) throws Exception{
		
		if(StringHelper.isEmpty(userId)||cancelFee==null){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}

		List<CProdOrderDto> orderList = cProdOrderDao.queryProdOrderDtoByUserId(userId);
		
		orderFees=orderComponent.getLogoffOrderFee(orderList, isHigh);
		if(refundFee==0){
			for(CProdOrderDto order:orderList){
				if(order.getBalance_cfee()!=null&&order.getBalance_cfee()>0){
					order.setBalance_acct(order.getBalance_acct()+order.getBalance_cfee());
					order.setBalance_cfee(0);
				}
			}
		}
		int fee=0;
		int balance_cfee=0;
		for(CProdOrderFee orderFee:orderFees){
			fee=fee+orderFee.getOutput_fee();
			if(orderFee.getOutput_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE)){
				if(refundFee<0){
					balance_cfee=balance_cfee+orderFee.getOutput_fee();
				}else{
					//当实际退现金=0时，退现金明细改成转账到公用账目
					orderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_ACCT);
				}
			}
		}
		//金额核对
		if(cancelFee!=fee*-1){
			throw new ServicesException(ErrorCode.FeeDateException);
		}
		if(refundFee!=balance_cfee*-1){
			throw new ServicesException(ErrorCode.FeeDateException);
		}
		
		return orderList;
	}



	@Override
	public void saveOpenInteractive(String netType, String modemMac, String password, String vodUserType,
			String remainReplacoverDate) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveAtvToDtv(CUser user, int curMonthFee, int payFee) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void checkStopUser(String[] userIds) throws Exception{
		//获取操作的客户、用户信息
		//CCust cust = getBusiParam().getCust();
		List<CUser> users = userComponent.queryAllUserByUserIds(userIds);
		if (users == null || users.size() == 0 || users.get(0) == null)
			throw new ServicesException("请选择用户");
		
		//查找客户名下所有有效的产品
		Map<String,String> packageUserIdS = new HashMap<String,String>();
		List<CProdOrder> orderList = cProdOrderDao.queryCustEffOrder(users.get(0).getCust_id());
		for (CProdOrder order:orderList){
			if (StringHelper.isNotEmpty(order.getPackage_sn()) && StringHelper.isNotEmpty(order.getUser_id())){
				packageUserIdS.put(order.getUser_id(),order.getPackage_sn());
			}
		}
		
		boolean hasPkgUser = false; //报停的用户有归属于客户套餐的
		boolean hasZzd = false; //有OTT主终端用户
		boolean hasFzd = false; //有OTT副终端用户
		int count=0;
		for (CUser user:users){
			if (user.getProtocol_date() != null && user.getProtocol_date().after(new Date())){
				throw new ServicesException("用户["+user.getUser_id()+"]还在协议期内，不能报停!");
			} else if (!user.getStatus().equals(StatusConstants.ACTIVE)){
				throw new ServicesException("用户["+user.getUser_id()+"]不是正常状态，不能报停!");
			}
			if (packageUserIdS.get(user.getUser_id()) != null){
				hasPkgUser =true;
				count ++;
			}
			if (StringHelper.isNotEmpty(user.getTerminal_type())){
				if (user.getUser_type().equals(SystemConstants.USER_TYPE_OTT)){
					if(user.getTerminal_type().equals(SystemConstants.USER_TERMINAL_TYPE_ZZD)){
						hasZzd = true;
					} else {
						hasFzd = true;
					}
				}
			}
		}
		
		if (hasPkgUser && (count<packageUserIdS.size())){
			throw new ServicesException("归属套餐的用户必须同时报停");
		} else if (hasFzd && !hasZzd){
			throw new ServicesException("OTT副机报停,主机必须报停");
		}
		
	}


	@Override
	public void saveStop(String effectiveDate, int tjFee) throws Exception {
		CCust cust = getBusiParam().getCust();
		doneCodeComponent.lockCust(cust.getCust_id());
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取操作的客户、用户信息
		
		List<CUser> users = getBusiParam().getSelectedUsers();
		
		if (effectiveDate.equals(DateHelper.getDate("-"))){
			List<CProdOrderDto> orderList = cProdOrderDao.queryCustEffOrderDto(cust.getCust_id());
			//当天报停
			boolean isCustPkgStop = false;
			for(CUser user:users){
				//清除原有未执行的预报停
				removeStopByUserId(user.getUser_id());
				//修改用户状态
				updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.REQSTOP);
				//生成钝化用户JOB
				authComponent.sendAuth(user, null, BusiCmdConstants.PASSVATE_USER, doneCode);
				//修改用户订单状态为报停状态
				
				for (CProdOrderDto order:orderList){
					if (StringHelper.isNotEmpty(order.getUser_id()) && order.getUser_id().equals(user.getUser_id())){
						stopProd(doneCode, order);
						if (StringHelper.isNotEmpty(order.getPackage_sn())){
							isCustPkgStop = true;
						}
					}
				}
			}
			
			if (isCustPkgStop){
				//修改套餐状态
				for (CProdOrderDto order:orderList){
					if (!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
						stopProd(doneCode, order);
					}
				}
			}
		} else {
			getBusiParam().setBusiCode(BusiCodeConstants.USER_PRE_REQUIRE_STOP);		
			//预报停
			for(CUser user:users){
				//清除原有未执行的预报停
				removeStopByUserId(user.getUser_id());
				//TODO 预报停
				jobComponent.createUserStopJob(doneCode, user.getUser_id(), effectiveDate);
			}
		}
		saveAllPublic(doneCode,getBusiParam());
	}




	

	//取消预报听
	@Override
	public void cancelStopUser() throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		List<CUser> users = getBusiParam().getSelectedUsers();
		String[] userall = CollectionHelper.converValueToArray(users, "user_id");
		jobComponent.cancelStopUser(userall);
		saveAllPublic(doneCode,getBusiParam());
	}


	/**
	 * 报开，传入参数都没有用
	 */
	@Override
	public void saveOpen(String stbId, String cardId, String modemMac, int tjFee) throws Exception {
		CCust cust = getBusiParam().getCust();
		doneCodeComponent.lockCust(cust.getCust_id());
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取操作的客户、用户信息
		List<CUser> users = getBusiParam().getSelectedUsers();
		List<CProdOrderDto> orderList = cProdOrderDao.queryCustEffOrderDto(cust.getCust_id());
		boolean isCustPkgOpen = false;
		for(CUser user:users){
			updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.ACTIVE);
			//修改订单状态为正常状态，并更新到期日
			Date startDate = null;
			String curProdId = null;
			for (CProdOrderDto order:orderList){
				if (StringHelper.isNotEmpty(order.getUser_id()) && order.getUser_id().equals(user.getUser_id())){
					if (curProdId == null || !order.getProd_id().equals(curProdId))
						startDate = null;
					startDate = openProd(doneCode, order,startDate);
					if (StringHelper.isNotEmpty(order.getPackage_sn())){
						isCustPkgOpen = true;
					}
					
					curProdId = order.getProd_id();
				}
			}
			
			//发授权
			authComponent.sendAuth(user, null, BusiCmdConstants.ACCTIVATE_USER, doneCode);
		}
		
		if (isCustPkgOpen){
			//修改套餐状态
			Date startDate = null;
			for (CProdOrderDto order:orderList){
				if (!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
					startDate = openProd(doneCode, order,startDate);
				}
			}
		}
		saveAllPublic(doneCode,getBusiParam());
		
	}


	




	@Override
	public void saveResendCa() throws Exception {
		List<CUser> userList = getBusiParam().getSelectedUsers();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		for (CUser user:userList){
			//重发加授权指令
			List<CProdOrder> prodList = orderComponent.queryOrderProdByUserId(user.getUser_id());
			for (CProdOrder prod:prodList){
				//正常状态的产品,重复指令
				List<CProdOrder> orderList = new ArrayList<CProdOrder>();
				orderList.add(prod);
				String authCmdType = "";
				if (isProdOpen(prod.getStatus())){
					authCmdType = BusiCmdConstants.ACCTIVATE_PROD;
				}else{
					authCmdType = BusiCmdConstants.PASSVATE_PROD;
				}
				authComponent.sendAuth(user, orderList, authCmdType, doneCode);
			}
		}

		saveAllPublic(doneCode,getBusiParam());
	}




	@Override
	public void saveRefreshCa(String refreshType) throws Exception {
		List<CUser> userList = getBusiParam().getSelectedUsers();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		for (CUser user:userList){
			if (SystemConstants.REFRESH_TYPE_TERMINAL.equals(refreshType)){
				authComponent.sendAuth(user, null, BusiCmdConstants.ACCTIVATE_TERMINAL, doneCode);
			} else {
				authComponent.sendAuth(user, null, BusiCmdConstants.REFRESH_TERMINAL, doneCode);
			}
				
		}

		saveAllPublic(doneCode,getBusiParam());
	}


	public void saveBatchUpdateUserName(List<CUser> userList, String custId) throws Exception {

		if(CollectionHelper.isEmpty(userList)){
			return ;
		}
		Integer doneCode = this.doneCodeComponent.gDoneCode();
		
		BusiParameter busiParam = getBusiParam();
		for (CUser user : userList) {
			int result = userComponent.updateUserNameByDeviceCode(user,custId);
			if(result < 1){
				throw new ServicesException(ErrorCode.NoUserExistsOrBelong2CurrentCust);
			}
		}
		busiParam.setBusiCode(BusiCodeConstants.BATCH_MOD_USER_NAME);//其他参数不需要
		saveAllPublic(doneCode,busiParam);
	}


	@Override
	public void saveEditPwd(String newPwd) throws Exception {

		//获取客户用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		CUser user = getBusiParam().getSelectedUsers().get(0);
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//生成计算用户信用度的JOB
		jobComponent.createCreditCalJob(doneCode, custId, null,SystemConstants.BOOLEAN_TRUE);
		List<CUserPropChange> propChangeList = new ArrayList<CUserPropChange>();
		CUser userDto = queryUserById(user.getUser_id());

		userDto.setNewPassword(newPwd);
		jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.BAND_EDIT_PWD, custId, user.getUser_id(), null, null, user.getModem_mac(), null, null, JsonHelper.fromObject(userDto));
	
		
		CUserPropChange propChange = new CUserPropChange();
		propChange.setColumn_name("password");
		propChange.setOld_value(userDto.getPassword());
		propChange.setNew_value(newPwd);
		propChangeList.add(propChange);
		userComponent.editUser(doneCode, getBusiParam().getSelectedUserIds().get(0), propChangeList);
		
		authComponent.sendAuth(user, null, BusiCmdConstants.CHANGE_USER, doneCode);
		
		saveAllPublic(doneCode,getBusiParam());
	}




	@Override
	public void saveEditConnect(int maxConn) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveClearBind() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveOffLine() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void savePromotion(int times, String promotionId, List<DisctFeeDto> feeList, List<PPromotionAcct> acctList)
			throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveCancelPromotion(String promotionSn) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public List<PromotionDto> querySelectableProm() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public PromotionDto queryPromInfoById(String custId, String userId, String promotionId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void saveOpenTempBatch() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveOpenTemp() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveRejectRes(String userId, String custId, String resIds) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public List<UserRes> queryValidRes(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<SOptr> getByDeptId(String deptId) throws JDBCException {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public Object queryStopByUsers(String userLists) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void saveCancelOpenInteractive() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveEditNetType(String netType, String modemMac) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public TBusiFee queryZlFeeById() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void saveLeaseFee(String fee_Id, String amount) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveAtvCustLine() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveAtvActive() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void editUserStop() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void checkLoginName(String loginName) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveToSingleCard(String newCardId, String str4, String str5, boolean reclaim, String deviceStatus)
			throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveRechargeCard(String icCard, String rechargeCard) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public Object createLoginName(String loginName, String county_id) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void saveResendUserCmd() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void updateUserStatus(List<String> userIds, String userStatus) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void renewUser(String userId) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveChangePromotion(int times, String promotionSn, String promotionId, List<PPromotionAcct> acctList)
			throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveCancelCaAuth() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveUserInvalid() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void batchLogoffUser(List<String> userIdList, String isReclaimDevice, String deviceStatus, String remark)
			throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void editFreeUser(String userId, String prodId, String tariffId, String type, Date tariffStartDate)
			throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void transferUsers(String toCustId) throws Exception {
		// TODO Auto-generated method stub
		
	}




	
	/**
	 * 清除预报停，并且使得操作流水失效
	 * @param userId
	 * @throws Exception
	 */
	public void removeStopByUserId(String userId) throws Exception{
		List<JUserStop> userList = jobComponent.queryStopByUserId(userId);
		if(userList.size()>0){
			jobComponent.removeByUserId(userId);
		}
	}
	
	private void stopProd(Integer doneCode, CProdOrderDto order) throws Exception {
		List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
		changeList.add(new CProdPropChange("status",
				order.getStatus(),StatusConstants.REQSTOP));
		changeList.add(new CProdPropChange("status_date",
				DateHelper.dateToStr(order.getStatus_date()),DateHelper.dateToStr(new Date())));
		
		orderComponent.editProd(doneCode,order.getOrder_sn(),changeList);
	}
	
	private Date openProd(Integer doneCode, CProdOrderDto order,Date startDate) throws Exception, ServicesException {
		Date expDate = null;
		Date effDate = null;
		CProdPropChange statusChange =new CProdPropChange();
		statusChange.setChange_time(new Date(order.getStatus_date().getTime()));
		statusChange.setNew_value(StatusConstants.ACTIVE);
		if (statusChange == null)
			throw new ServicesException("找不到产品报停记录，请联系管理员");
		if (startDate == null) {
			//计算报停天数
			int stopDays = DateHelper.getDiffDays(statusChange.getChange_time(), DateHelper.today());
			expDate = DateHelper.addDate(order.getExp_date(), stopDays);
		} else {
			effDate =  DateHelper.addDate(startDate, 1);
			if(order.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)){
				//包月计费方式处理
				expDate = DateHelper.getNextMonthPreviousDay(effDate, order.getOrder_months().intValue());
			}else if(order.getBilling_type().equals(SystemConstants.BILLING_TYPE_DAY)){
				//包天计费方式处理
				int orderDays=Math.round(order.getOrder_months()*30);
				expDate=DateHelper.addDate(effDate, orderDays-1);
			}else{
				//其他计费方式处理
				int stopDays = DateHelper.getDiffDays(statusChange.getChange_time(), DateHelper.today());
				expDate = DateHelper.addDate(order.getExp_date(), stopDays);
			}
			
		}
		
		List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
		changeList.add(new CProdPropChange("status",
				order.getStatus(),statusChange.getNew_value()));
		changeList.add(new CProdPropChange("status_date",
				DateHelper.dateToStr(order.getStatus_date()),DateHelper.dateToStr(new Date())));
		changeList.add(new CProdPropChange("exp_date",DateHelper.dateToStr(order.getExp_date()),
				DateHelper.dateToStr(expDate)));
		
		
		if (effDate != null){
			changeList.add(new CProdPropChange("eff_date",DateHelper.dateToStr(order.getEff_date()),
					DateHelper.dateToStr(effDate)));
		}
		orderComponent.editProd(doneCode,order.getOrder_sn(),changeList);
		
		return expDate;
		
	}
	
	private void setUserDeviceInfo(CUser user, DeviceDto device) throws Exception{
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
			if (!device.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM))
				throw new ServicesException("设备类型不正确");
			user.setModem_mac(device.getDevice_code());
		}
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_OTT)){
			if (!device.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB) 
					|| (device.getPairCard() != null && StringHelper.isNotEmpty(device.getPairCard().getCard_id())))
				throw new ServicesException("设备类型不正确");
			user.setStb_id(device.getDevice_code());
			user.setModem_mac(device.getStbMac());
		}
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTT)){
			if ( device.getPairCard() == null || StringHelper.isEmpty(device.getPairCard().getCard_id()))
				throw new ServicesException("设备类型不正确");
			user.setStb_id(device.getDevice_code());
			user.setCard_id(device.getPairCard().getCard_id());
		}
	}
	/**验证用户能不能报停
	 * 1、协议期用户不能报亭
	 * 2、OTT主机没有报停的情况下，副机不能报停
	 * 3、如果用户名下有未到期的归属客户套餐的产品，套餐下用户必须同时报停
	 * @param users
	 */
	
}
