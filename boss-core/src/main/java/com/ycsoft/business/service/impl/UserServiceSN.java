package com.ycsoft.business.service.impl;

import static com.ycsoft.commons.constants.SystemConstants.ACCT_TYPE_SPEC;
import static com.ycsoft.commons.constants.SystemConstants.USER_TYPE_BAND;
import static com.ycsoft.commons.constants.SystemConstants.USER_TYPE_DTT;
import static com.ycsoft.commons.constants.SystemConstants.USER_TYPE_OTT;
import static com.ycsoft.commons.constants.SystemConstants.USER_TYPE_OTT_MOBILE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.config.TDeviceChangeReason;
import com.ycsoft.beans.core.acct.CAcct;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustDevice;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.fee.CFeeAcct;
import com.ycsoft.beans.core.fee.CFeeDevice;
import com.ycsoft.beans.core.job.JUserStop;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.beans.core.prod.CProdOrderFeeOut;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.prod.CancelUserDto;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.prod.PSpkgOpenbusifee;
import com.ycsoft.beans.prod.PSpkgOpenuser;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.task.SnTaskComponent;
import com.ycsoft.business.dao.core.fee.CFeeDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.prod.CProdOrderFeeDao;
import com.ycsoft.business.dao.core.prod.CProdOrderFeeOutDao;
import com.ycsoft.business.dao.task.WTaskBaseInfoDao;
import com.ycsoft.business.dao.task.WTaskUserDao;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.business.dto.core.fee.FeeInfoDto;
import com.ycsoft.business.dto.core.prod.DisctFeeDto;
import com.ycsoft.business.dto.core.prod.PromotionDto;
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
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;

@Service
public class UserServiceSN extends BaseBusiService implements IUserService {

	@Autowired
	private CProdOrderDao cProdOrderDao;
	@Autowired
	private SnTaskComponent snTaskComponent;
	@Autowired
	private WTaskBaseInfoDao wTaskBaseInfoDao;
	@Autowired
	private WTaskUserDao wTaskUserDao;
	@Autowired
	private CProdOrderFeeDao cProdOrderFeeDao;
	@Autowired
	private CFeeDao cFeeDao;
	@Autowired
	private CProdOrderFeeOutDao cProdOrderFeeOutDao;
	
	public void createUser(CUser user, String deviceCode, String deviceType, String deviceModel, String deviceBuyMode,
			FeeInfoDto deviceFee) throws Exception {
		CCust cust = getBusiParam().getCust();
		doneCodeComponent.lockCust(cust.getCust_id());
		this.validAccount(user.getLogin_name());
		this.canOpenUser(cust);
		
		Integer doneCode = doneCodeComponent.gDoneCode();
		getBusiParam().resetUser();
		if(user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
			user = openSingle(cust, user, doneCode, deviceCode, deviceType, deviceModel, deviceBuyMode, deviceFee);
			getBusiParam().addUser(user);//设置拦截器所需要的参数
		}else{
			//用户开户多设备开户
			List<DeviceDto> list= deviceComponent.querySaleableDeviceArea(deviceCode,user.getUser_type());
			
			for(DeviceDto device:list){
				if(list.size()>1){
					user.setLogin_name(null);
				}
				user = openSingle(cust, user, doneCode, device.getDevice_code(), device.getDevice_type(), device.getDevice_model(), deviceBuyMode, deviceFee);
				getBusiParam().addUser(user);//设置拦截器所需要的参数
			}
		}
		/**
		String userType = user.getUser_type();
		//若没有设备号，新增工单
		DTT不开工单
		if ((userType.equals(USER_TYPE_BAND) && StringHelper.isEmpty(user.getModem_mac()))
				|| (userType.equals(USER_TYPE_OTT) && StringHelper.isEmpty(user.getStb_id()))) {
			List<CUser> userList = new ArrayList<CUser>();
			user.setDevice_model(deviceModel);
			userList.add(user);
			snTaskComponent.createOpenTask(doneCode, cust, userList, getBusiParam().getWorkBillAsignType());
		}**/
		saveAllPublic(doneCode, getBusiParam());

	}
	
	public String generateUserName(String custId, String userType) {
		String userName = "";
		try {
			CCust cust = custComponent.queryCustById(custId);
			int num = getUserCount(cust, userType);
			if (userType.equals(USER_TYPE_BAND)) {
				String domainName = custComponent.getDomainByAddr(cust.getAddr_id());
				userName = cust.getCust_no() + "0" + num + "@" + domainName;
			} else {
				userName = cust.getCust_no() + "" + num;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userName;
	}
	
	private void canOpenUser(CCust cust) throws Exception {
		if(cust.getStatus().equals(StatusConstants.PREOPEN)){
			throw new ServicesException(ErrorCode.CustStatusIsNotOpenUser);
		}
	}
	
	@Override
	public void createUserBatch(List<UserInfo> userList, String stopType, String isHand) throws Exception {
		CCust cust = getBusiParam().getCust();
		this.canOpenUser(cust);
		doneCodeComponent.lockCust(cust.getCust_id());
		Integer doneCode = doneCodeComponent.gDoneCode();
		List<CUser> users = new ArrayList<CUser>();
		
		if(SystemConstants.BOOLEAN_TRUE.equals(isHand)){
			// 获取客户信息
			for (UserInfo userInfo:userList){
				for (int i=0;i<userInfo.getUser_count();i++){
					CUser user = new CUser();
					user.setUser_type(userInfo.getUser_type());
					String deviceType;
					if (user.getUser_type().equals(USER_TYPE_BAND)){
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
					
					CUser newUser = this.openSingle(cust, user, doneCode, null, deviceType, deviceModel, deviceBuyMode, deviceFee);
					users.add(newUser);
				}
			}
		}else{
			List<PSpkgOpenuser> spkgUserList = this.querySpkgUser(cust.getCust_id(),cust.getSpkg_sn());
			List<PSpkgOpenbusifee> spkgBusiFeeList = this.querySpkgOpenFee(cust.getSpkg_sn());
			if(spkgUserList.size() > 0){
				for(PSpkgOpenuser openUser : spkgUserList){
					CUser user = new CUser();
					user.setUser_type(openUser.getUser_type());
					String deviceType;
					if (user.getUser_type().equals(USER_TYPE_BAND)){
						deviceType = SystemConstants.DEVICE_TYPE_MODEM;
					} else {
						deviceType = SystemConstants.DEVICE_TYPE_STB;
					}
					
					String deviceModel = openUser.getDevice_model();
					String deviceBuyMode = openUser.getBuy_type();
					FeeInfoDto deviceFee = new FeeInfoDto();
					deviceFee.setFee_id(openUser.getFee_id());
					deviceFee.setFee(openUser.getFee());
					
					user.setStop_type(stopType);
					
					int openNum = openUser.getOpen_num();
					for(int i=0;i<openNum;i++){
						CUser newUser = this.openSingle(cust, user, doneCode, null, deviceType, deviceModel, deviceBuyMode, deviceFee);
						deviceFee.setFee(0);
						users.add(newUser);
					}
					
				}
			}
			if(spkgBusiFeeList.size() > 0){
				for(PSpkgOpenbusifee busiFee : spkgBusiFeeList){
					if(busiFee.getFee() > 0){
						feeComponent.saveBusiFee(cust.getCust_id(), cust != null ? cust.getAddr_id() : null, busiFee.getFee_id(), 1, 
								SystemConstants.PAY_TYPE_UNPAY, busiFee.getFee(), doneCode, doneCode, getBusiParam().getBusiCode(), null, "");
						doneCodeComponent.saveDoneCodeUnPay(cust.getCust_id(), doneCode,this.getOptr().getOptr_id());
					}
				}
			}
			userComponent.updateOpenUserDoneCode(cust.getSpkg_sn(), doneCode);
		}
		snTaskComponent.createOpenTask(doneCode, cust, users, getBusiParam().getWorkBillAsignType());
		
		getBusiParam().setSelectedUsers(users);
		snTaskComponent.saveTaskCreateBusiExt(cust.getCust_id(), doneCode, getBusiParam());
		saveAllPublic(doneCode, getBusiParam());
	}

	private int getUserCount(CCust cust, String userType) throws Exception {
		int num = userComponent.queryMaxNumByLoginName(cust.getCust_id(), cust.getCust_no(), userType);
		if(userType.equals(USER_TYPE_BAND)){
			num += SystemConstants.USER_TYPE_BAND_NUM;
		}else if(userType.equals(USER_TYPE_OTT) || userType.equals(USER_TYPE_OTT_MOBILE)){
			if(num == 0){
				num += SystemConstants.USER_TYPE_OTT_NUM;
			}else{
				num += 1;
			}
		}
		return num;
	}
	
	private CUser openSingle(CCust cust, CUser cuser, Integer doneCode, String deviceCode, String deviceType,
			String deviceModel, String deviceBuyMode, FeeInfoDto deviceFee) throws Exception, JDBCException {
		String custId = cust.getCust_id();
		
		String user_id = userComponent.gUserId();
		// 创建账户信息
		String acctId = acctComponent.createAcct(custId, user_id, ACCT_TYPE_SPEC, null);
		// 创建用户信息
		CUser user = (CUser)cuser.clone();
		user.setUser_id(user_id);
		user.setAcct_id(acctId);
		user.setCust_id(custId);
		user.setStr10(deviceBuyMode);//用户开始时设备购买方式
		user.setDevice_model(deviceModel);//工单需要记录设备型号
		user.setStr3(deviceModel);
		DeviceDto device = null;
		if (StringHelper.isNotEmpty(deviceCode)){
			device = deviceComponent.queryDeviceByDeviceCode(deviceCode);
			setUserDeviceInfo(user, device);
		} else {
			device = new DeviceDto();
			device.setDevice_type(deviceType);
			device.setDevice_model(deviceModel);
		}
		
		//验证band账号名
		if(StringHelper.isNotEmpty(user.getLogin_name()))
			this.validAccount(user.getLogin_name());
		if(StringHelper.isEmpty(user.getPassword()))
			user.setPassword(cust.getPassword());
		
		//设置OTT用户终端类型和默认用户名
		String userType = user.getUser_type();
		if (userType.equals(USER_TYPE_OTT)) {
			if (cust.getCust_type().equals(SystemConstants.CUST_TYPE_NONRESIDENT)){
				user.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_ZZD);
			} else {
				List<CUser> userList = userComponent.queryUserByCustId(cust.getCust_id());
				user.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_ZZD);
				if (userList.size() > 0){
					Map<String, List<CUser>> map = CollectionHelper.converToMap(userList, "user_type");
					List<CUser> ottList = map.get(USER_TYPE_OTT);
					if(ottList == null || (ottList.size() > 0 && ottList.size() % 3 == 0)){//二主一副
						user.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_ZZD);
					}else{
						user.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_FZD);
					}
				}
			}
		}
		
		TDeviceBuyMode buyModeCfg = busiConfigComponent.queryBuyMode(deviceBuyMode);
		//处理设备和授权
		//批量开户不添加设备到c_cust_device
		if (!user.getUser_type().equals(USER_TYPE_OTT_MOBILE)){
			String ownership = SystemConstants.OWNERSHIP_GD;
			if (buyModeCfg!= null && buyModeCfg.getChange_ownship().equals(SystemConstants.BOOLEAN_TRUE))
				ownership = SystemConstants.OWNERSHIP_CUST;
			this.buyDevice(device, deviceBuyMode,ownership, deviceFee, getBusiParam().getBusiCode(), cust, doneCode);
			
			//非自购模式 设置协议截止日期，读取模板配置数据
			//去掉协议期
			/*if(buyModeCfg!= null && !buyModeCfg.getBuy_mode().equals(SystemConstants.BUSI_BUY_MODE_BUY) && StringHelper.isNotEmpty(deviceModel)){
				Integer months = Integer.parseInt( userComponent.queryTemplateConfig(TemplateConfigDto.Config.PROTOCOL_DATE_MONTHS.toString()) );
				user.setProtocol_date( DateHelper.addTypeDate(DateHelper.now(), "MONTH", months) );
			}*/
		}
		
		if(!user.getUser_type().equals(USER_TYPE_DTT) && StringHelper.isEmpty(user.getLogin_name())){
			user.setLogin_name( generateUserName(cust.getCust_id(), user.getUser_type()) );
		}
		
		userComponent.createUser(user);
		
		
		if (user.getStatus().equals(StatusConstants.ACTIVE) || user.getUser_type().equals(USER_TYPE_BAND)){
			createUserJob(user, custId, doneCode);
		}
		
		return user;
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
		List<TDeviceChangeReason> list = userComponent.queryDeviceChangeReason();
		for(TDeviceChangeReason reason : list){
			reason.setReason_text( MemoryDict.getTransData(reason.getReason_text()) );
		}
		return list;
	}
	
	/**
	 * 用户更换设备
	 */
	public void saveChangeDevice(String userId, String deviceCode, String reasonType, String deviceBuyMode, FeeInfoDto deviceFee) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		CCust cust = getBusiParam().getCust();
		String busiCode = getBusiParam().getBusiCode();
		doneCodeComponent.lockCust(cust.getCust_id());
		CUser oldUser = userComponent.queryUserById(userId);
		CUser user = userComponent.queryUserById(userId);
		
		String oldDeviceCode = user.getStb_id();
		if (user.getUser_type().equals(USER_TYPE_BAND))
			oldDeviceCode = user.getModem_mac();
		DeviceDto oldDevice = deviceComponent.queryDeviceByDeviceCode(oldDeviceCode);
		DeviceDto device = deviceComponent.queryDeviceByDeviceCode(deviceCode);
		deviceComponent.checkChangeDevice(oldDevice, device);
		
		CCustDevice oldCustDevice = custComponent.queryCustDeviceByDeviceCode(oldDeviceCode);
		
		/*更换设备，新设备产权和购买方式与原设备一致，只是根据购买原因收费而已*/
		
		//修改用户设备信息
		setUserDeviceInfo(user, device);
		userComponent.updateDevice(doneCode, user);
		
		
		TDeviceChangeReason changeReason = userComponent.queryChangeReasonByType(reasonType);
		
		if(changeReason.getIs_charge().equals(SystemConstants.BOOLEAN_TRUE)){
			this.buyDevice(device, oldCustDevice.getBuy_mode(), oldDevice.getOwnership(), deviceFee, busiCode, cust, doneCode);
		}else{
			custComponent.addDevice(doneCode, cust.getCust_id(),
					device.getDevice_id(), device.getDevice_type(), device.getDevice_code(), 
					device.getPairCard() ==null?null:device.getPairCard().getDevice_id(),
					device.getPairCard() ==null?null:device.getPairCard().getCard_id(), 
					null, null,oldCustDevice.getBuy_mode());
			if (device != null){
				//更新设备仓库状态
				deviceComponent.updateDeviceDepotStatus(doneCode, busiCode, device.getDevice_id(),
						device.getDepot_status(), StatusConstants.USE, user.getStr10(), true);
				if (!device.getOwnership().equals(oldDevice.getOwnership())){
					deviceComponent.updateDeviceOwnership(doneCode, busiCode, device.getDevice_id(),device.getOwnership(),oldDevice.getOwnership(),user.getStr10(),true);
				}
				//更新设备为旧设备
				if (SystemConstants.BOOLEAN_TRUE.equals(device.getUsed()))
					deviceComponent.updateDeviceUsed(doneCode, busiCode, device.getDevice_id(), SystemConstants.BOOLEAN_TRUE, SystemConstants.BOOLEAN_FALSE,true);
			}
		}
		
		CCustDevice newCustDevice = custComponent.queryCustDeviceByDeviceCode(deviceCode);
		//保存更换原因
		if(newCustDevice != null)
			custComponent.saveChangeDevice(newCustDevice, changeReason.getReason_type());
		
		if(oldCustDevice != null)
			custComponent.saveChangeDevice(oldCustDevice, changeReason.getReason_type());
		
		if(changeReason.getIs_reclaim().equals(SystemConstants.BOOLEAN_TRUE)){
			deviceComponent.saveDeviceToReclaim(doneCode,  busiCode, oldDevice, cust.getCust_id(), reasonType);
		}
		if(changeReason.getIs_lost().equals(SystemConstants.BOOLEAN_TRUE)){
			deviceComponent.saveLossDevice(doneCode, busiCode, oldDeviceCode);
		}
		
		//处理授权
		if (user.getUser_type().equals(USER_TYPE_OTT)){
		    //OTT 发变更用户信息
			authComponent.sendAuth(user, null, BusiCmdConstants.CHANGE_USER, doneCode);
		} else if (user.getUser_type().equals(USER_TYPE_DTT)){
			//DTT新设备发开户指令
			authComponent.sendAuth(user, null, BusiCmdConstants.CREAT_USER, doneCode);
			//新设备发产品授权
			List<CProdOrder> prodList = orderComponent.queryOrderProdByUserId(user.getUser_id());
			authComponent.sendAuth(user, prodList, BusiCmdConstants.ACCTIVATE_PROD, doneCode);
	
			//设备回收，发设备销毁指令
			if(changeReason.getIs_reclaim().equals(SystemConstants.BOOLEAN_TRUE)){
				authComponent.sendAuth(oldUser, null, BusiCmdConstants.DEL_USER, doneCode);
			}else{
				//设备不回收，旧设备发产品减指令
				authComponent.sendAuth(oldUser, prodList, BusiCmdConstants.PASSVATE_PROD, doneCode);
			}
			
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


	private void logoffUser(Integer doneCode, String busiCode, CCust cust, String userId, 
			Integer cancelFee, Integer refundFee) throws Exception {
		String custId = cust.getCust_id();
		CUser user = userComponent.queryUserById(userId);
		if(user == null){
			throw new ServicesException(ErrorCode.CustDataException);
		}
		if(!user.getCust_id().equals(custId)){
			throw new ServicesException(ErrorCode.CustDataException);
		}
		if(user.getStatus().equals(StatusConstants.UNTUCK) || user.getStatus().equals(StatusConstants.REQSTOP) 
				|| user.getStatus().equals(StatusConstants.INSTALL) ){
			throw new ServicesException(ErrorCode.UserStatusNotOff);
		}
		if(SystemConstants.BUSI_BUY_MODE_PRESENT.equals(user.getStr10()) && 
			(StringHelper.isNotEmpty(user.getStb_id())||StringHelper.isNotEmpty(user.getCard_id()) 
					|| StringHelper.isNotEmpty(user.getModem_mac()))){
			throw new ServicesException(ErrorCode.UserStatusNotOff);
		}

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
		//销户后保存原来的设备购买方式
		user.setStb_buy(map.get(user.getStb_id()) != null?map.get(user.getStb_id()).getBuy_mode():null);
		user.setCard_buy(map.get(user.getCard_id()) != null?map.get(user.getCard_id()).getBuy_mode():null);
		user.setModem_buy(map.get(user.getModem_mac()) != null?map.get(user.getModem_mac()).getBuy_mode():null);
		
		//是否高级权限
		boolean isHigh=orderComponent.isHighCancel(busiCode);
		List<CProdOrderFee> orderFees=new ArrayList<>();
		//检查数据
		List<CProdOrderDto> cancelList=checkCancelProdOrderParm(orderFees,isHigh, userId, cancelFee,refundFee);
		List<CProdOrderFeeOut> outList=orderComponent.getOrderFeeOutFromOrderFee(orderFees);
		if(refundFee<0){
			//记录未支付业务
			doneCodeComponent.saveDoneCodeUnPay(custId, doneCode, this.getOptr().getOptr_id());
			//保存缴费信息
			feeComponent.saveCancelFee(cancelList,outList, cust, doneCode, this.getBusiParam().getBusiCode());
		}
		if(cancelFee-refundFee!=0){
			//余额转回公用账目
			acctComponent.saveCancelFeeToAcct(outList, custId, doneCode, this.getBusiParam().getBusiCode());
		}
		//记录费用转出记录
		orderComponent.saveOrderFeeOut(outList, doneCode);
		
		List<CProdOrder> cancelResultList=new ArrayList<>();
		
		for(CProdOrderDto dto:cancelList){
			//执行退订 返回被退订的用户订单清单
			cancelResultList.addAll(orderComponent.saveCancelProdOrder(dto, doneCode));
		}
		
		authComponent.sendAuth(user, cancelResultList, BusiCmdConstants.PASSVATE_PROD, doneCode);
		authComponent.sendAuth(user, cancelResultList, BusiCmdConstants.DEL_USER, doneCode);
		//记录用户到历史表
		userComponent.removeUserWithHis(doneCode, user);
	}

	@Override
	public void saveRemoveUser(String userId, Integer cancelFee,Integer refundFee) throws Exception {
		//获取客户用户信息
		CCust cust = getBusiParam().getCust();
		String  custId = cust.getCust_id();
		doneCodeComponent.lockCust(custId);
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		logoffUser(doneCode, getBusiParam().getBusiCode(), getBusiParam().getCust(), userId, cancelFee, refundFee);
		
		saveAllPublic(doneCode,getBusiParam());
	}

	
	
	private List<CProdOrderDto> checkCancelProdOrderParm(List<CProdOrderFee> orderFees,boolean isHigh,String userId,Integer cancelFee,Integer refundFee) throws Exception{
		
		if(StringHelper.isEmpty(userId)||cancelFee==null){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}

		List<CProdOrderDto> orderList = cProdOrderDao.queryProdOrderDtoByUserId(userId);
		
		orderFees.addAll(orderComponent.getLogoffOrderFee(orderList, isHigh));
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
	public void untuckUsers() throws Exception{
		CCust cust = getBusiParam().getCust();
		doneCodeComponent.lockCust(cust.getCust_id());
		List<String> userIdList = getBusiParam().getSelectedUserIds();
		String[] userIds = userIdList.toArray(new String[userIdList.size()]);
		Integer doneCode = doneCodeComponent.gDoneCode();
		List<CUser> users = userComponent.queryAllUserByUserIds(userIds);
		if (users == null || users.size() == 0 || users.get(0) == null)
			throw new ServicesException("请选择用户");
		checkUntruckUsers(userIds);
		
		List<CProdOrderDto> orderList = cProdOrderDao.queryCustEffOrderDto(cust.getCust_id());
		boolean isCustPkgStop = false;
		for(CUser user : users){
			//清除原有未执行的预报停
			removeStopByUserId(user.getUser_id());
			//修改用户状态
			updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.UNTUCK);
			
			//修改用户订单状态为报停状态
			
			for (CProdOrderDto order:orderList){
				if (StringHelper.isNotEmpty(order.getUser_id()) && order.getUser_id().equals(user.getUser_id())){
					stopProd(doneCode, order, StatusConstants.UNTUCK);
					if (StringHelper.isNotEmpty(order.getPackage_sn())){
						isCustPkgStop = true;
					}
				}
			}
			
			//生成钝化用户JOB
			authComponent.sendAuth(user, null, BusiCmdConstants.PASSVATE_USER, doneCode);
			//产品减授权
			List<CProdOrder> prodList = orderComponent.queryOrderProdByUserId(user.getUser_id());
			if(prodList.size() > 0){
				authComponent.sendAuth(user, prodList, BusiCmdConstants.PASSVATE_PROD, doneCode);
			}
			
		}
		
		if (isCustPkgStop){
			//修改套餐状态
			for (CProdOrderDto order:orderList){
				if (!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
					stopProd(doneCode, order, StatusConstants.UNTUCK);
				}
			}
		}
		
		snTaskComponent.createWriteOffTask(doneCode, cust, users, getBusiParam().getWorkBillAsignType());
		snTaskComponent.saveTaskCreateBusiExt(cust.getCust_id(), doneCode, getBusiParam());
		saveAllPublic(doneCode,getBusiParam());
	}
	
	private void checkUntruckUsers(String[] userIds) throws Exception{
		//获取操作的客户、用户信息
		CCust cust = getBusiParam().getCust();
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
		
		boolean hasPkgUser = false; //销户的用户有归属于客户套餐的
		boolean hasBand = false; //有宽带用户
		int count=0;
		for (CUser user:users){
			if (!user.getStatus().equals(StatusConstants.ACTIVE)){
				throw new ServicesException("用户["+user.getUser_id()+"]不是正常状态，不能拆机!");
				
			}
			if (packageUserIdS.get(user.getUser_id()) != null){
				hasPkgUser =true;
				count ++;
			}
			
			if (user.getUser_type().equals(USER_TYPE_BAND)){
				hasBand = true;
			}
		
		}
		
		if (hasPkgUser && (count<packageUserIdS.size())){
			throw new ServicesException("归属套餐的用户必须同时拆机");
			
		}
		
		if(hasBand && cust.getCust_type().equals(SystemConstants.CUST_TYPE_RESIDENT)){
			List<CUser> allUusers = userComponent.queryUserByCustId(cust.getCust_id());
			List<String> selectUser = Arrays.asList(userIds);
			for(CUser user : allUusers){
				if (!user.getStatus().equals(StatusConstants.REQSTOP)){
					if(!selectUser.contains(user.getUser_id())){
						throw new ServicesException("宽带用户拆机,其他用户都需要拆机");
					}
				}
			}
		}
	}

	
	@Override
	public void checkStopUser(String[] userIds) throws Exception{
		//获取操作的客户、用户信息
		List<CUser> users = userComponent.queryAllUserByUserIds(userIds);
		CCust cust = custComponent.queryCustById(users.get(0).getCust_id());
		if (users == null || users.size() == 0 || users.get(0) == null)
			throw new ServicesException(ErrorCode.UserIsNotExists);
		
		//查找客户名下所有有效的产品
		Map<String,String> packageUserIdS = new HashMap<String,String>();
		List<CProdOrder> orderList = cProdOrderDao.queryCustEffOrder(users.get(0).getCust_id());
		for (CProdOrder order:orderList){
			if (StringHelper.isNotEmpty(order.getPackage_sn()) && StringHelper.isNotEmpty(order.getUser_id())){
				packageUserIdS.put(order.getUser_id(),order.getPackage_sn());
			}
		}
		
		boolean hasPkgUser = false; //报停的用户有归属于客户套餐的
		boolean hasBand = false; //有宽带用户
		int count=0;
		Map<Integer, CUser> map = new HashMap<Integer, CUser>();
		for (CUser user:users){
			if (!user.getStatus().equals(StatusConstants.ACTIVE)){
				throw new ServicesException(ErrorCode.UserStatusIsNotActiveNotStop,user.getUser_id());
				
			}
			if (packageUserIdS.get(user.getUser_id()) != null){
				hasPkgUser =true;
				count ++;
			}
			
			if (user.getUser_type().equals(USER_TYPE_BAND)){
				hasBand = true;
			}
		
		}
		
		if (hasPkgUser && (count<packageUserIdS.size())){
			throw new ServicesException(ErrorCode.PackageUserMustToOperation);
			
		}
		
		if(hasBand && cust.getCust_type().equals(SystemConstants.CUST_TYPE_RESIDENT)){
			List<CUser> allUusers = userComponent.queryUserByCustId(cust.getCust_id());
			List<String> selectUser = Arrays.asList(userIds);
			for(CUser user : allUusers){
				if (!user.getStatus().equals(StatusConstants.REQSTOP) && user.getUser_type().equals(USER_TYPE_OTT)
						&& !selectUser.contains(user.getUser_id())){
					throw new ServicesException(ErrorCode.BandUserStopOhterOttUserMustStop);
				}
			}
		}
		
	}
	
	
	public void checkOpenUser(String[] userIds) throws Exception{
		//获取操作的客户、用户信息
		List<CUser> users = userComponent.queryAllUserByUserIds(userIds);
		CCust cust = custComponent.queryCustById(users.get(0).getCust_id());
		if (users == null || users.size() == 0 || users.get(0) == null)
			throw new ServicesException(ErrorCode.UserIsNotExists);
		
		//查找客户名下所有有效的产品
		Map<String,String> packageUserIdS = new HashMap<String,String>();
		List<CProdOrder> orderList = cProdOrderDao.queryCustEffOrder(users.get(0).getCust_id());
		for (CProdOrder order:orderList){
			if (StringHelper.isNotEmpty(order.getPackage_sn()) && StringHelper.isNotEmpty(order.getUser_id())){
				packageUserIdS.put(order.getUser_id(),order.getPackage_sn());
			}
		}
		
		boolean hasPkgUser = false; //报停的用户有归属于客户套餐的
		boolean hasOtt = false; //有OTT用户
		int count=0;
		Map<Integer, CUser> map = new HashMap<Integer, CUser>();
		for (CUser user:users){
			if (!user.getStatus().equals(StatusConstants.REQSTOP)){
				throw new ServicesException(ErrorCode.UserStatusIsNotReqstopNotOpen,user.getUser_id());
				
			}
			if (packageUserIdS.get(user.getUser_id()) != null){
				hasPkgUser =true;
				count ++;
			}
			
			if (user.getUser_type().equals(USER_TYPE_OTT)){
				hasOtt = true;
			}
		}
		
		if (hasPkgUser && (count<packageUserIdS.size())){
			throw new ServicesException(ErrorCode.PackageUserMustToOperation);
			
		}
		
		if(hasOtt && cust.getCust_type().equals(SystemConstants.CUST_TYPE_RESIDENT)){
			List<CUser> allUusers = userComponent.queryUserByCustId(cust.getCust_id());
			List<String> selectUser = Arrays.asList(userIds);
			for(CUser user : allUusers){
				if (user.getStatus().equals(StatusConstants.REQSTOP) && user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
					if(!selectUser.contains(user.getUser_id())){
						throw new ServicesException(ErrorCode.OttUserOpenOhterBandUserMustOpen);
					}
				}
			}
		}
		
	}
	
	
	public void checkCancelStop(String[] userIds,String custId) throws Exception{
		//获取操作的客户、用户信息
		List<CUser> users = userComponent.queryAllUserByUserIds(userIds);
		CCust cust = custComponent.queryCustById(users.get(0).getCust_id());
		if (users == null || users.size() == 0 || users.get(0) == null)
			throw new ServicesException(ErrorCode.UserIsNotExists);
		
		List<JUserStop> cancelUsers = jobComponent.queryStopByCustId(custId);
		if(cancelUsers.size()==0){
			throw new ServicesException(ErrorCode.ForecastStopUserIsNotExists);
		}
		List<String> cancelUser = CollectionHelper.converValueToList(cancelUsers, "user_id");
		//查找客户名下所有有效的产品
		Map<String,String> packageUserIdS = new HashMap<String,String>();
		List<CProdOrder> orderList = cProdOrderDao.queryCustEffOrder(users.get(0).getCust_id());
		for (CProdOrder order:orderList){
			if (StringHelper.isNotEmpty(order.getPackage_sn()) && StringHelper.isNotEmpty(order.getUser_id())
					&& cancelUser.contains(order.getUser_id())){
				packageUserIdS.put(order.getUser_id(),order.getPackage_sn());
			}
		}
		
		boolean hasPkgUser = false; //报停的用户有归属于客户套餐的
		boolean hasOtt = false; //有OTT用户
		int count=0;
		for (CUser user:users){
			if (packageUserIdS.get(user.getUser_id()) != null){
				hasPkgUser =true;
				count ++;
			}
			if (user.getUser_type().equals(USER_TYPE_OTT)){
				hasOtt = true;
			}
		}
		
		if (hasPkgUser && (count<packageUserIdS.size())){
			throw new ServicesException(ErrorCode.PackageUserMustToOperation);
		}
		
		List<CUser> cancelUsersList = userComponent.queryAllUserByUserIds(cancelUser.toArray(new String[cancelUser.size()]));
		if(hasOtt && cust.getCust_type().equals(SystemConstants.CUST_TYPE_RESIDENT)){
			List<String> selectUser = Arrays.asList(userIds);
			for(CUser user : cancelUsersList){
				if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
					if(!selectUser.contains(user.getUser_id())){
						throw new ServicesException(ErrorCode.OttUserCancelForecastStopBandUserAlso);
					}
				}
				
			}
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
				
				//修改用户订单状态为报停状态
				
				for (CProdOrderDto order:orderList){
					if (StringHelper.isNotEmpty(order.getUser_id()) && order.getUser_id().equals(user.getUser_id())){
						stopProd(doneCode, order, StatusConstants.REQSTOP);
						if (StringHelper.isNotEmpty(order.getPackage_sn())){
							isCustPkgStop = true;
						}
					}
				}
				//生成钝化用户JOB
				authComponent.sendAuth(user, null, BusiCmdConstants.PASSVATE_USER, doneCode);
				//产品减授权
				List<CProdOrder> prodList = orderComponent.queryOrderProdByUserId(user.getUser_id());
				if(prodList.size() > 0)
					authComponent.sendAuth(user, prodList, BusiCmdConstants.PASSVATE_PROD, doneCode);
				
			}
			
			if (isCustPkgStop){
				//修改套餐状态
				for (CProdOrderDto order:orderList){
					if (!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
						stopProd(doneCode, order, StatusConstants.REQSTOP);
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
		if(users.size()>0){
			checkCancelStop(userall,getBusiParam().getCust().getCust_id());
		}
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
		if(users.size()>0){
			checkOpenUser(CollectionHelper.converValueToArray(users, "user_id"));
		}
		
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
			//产品的到期日可能变化了，需要重发加授权
			List<CProdOrder> prodList = orderComponent.queryOrderProdByUserId(user.getUser_id());
			authComponent.sendAuth(user, prodList, BusiCmdConstants.ACCTIVATE_PROD, doneCode);
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



	/**
	 * 指令重发
	 */
	@Override
	public void saveResendCa() throws Exception {
		List<CUser> userList = getBusiParam().getSelectedUsers();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		for (CUser user:userList){
			//重发加授权指令
			List<CProdOrder> prodList = orderComponent.queryOrderProdByUserId(user.getUser_id());
			authComponent.sendAuth(user, prodList, BusiCmdConstants.ACCTIVATE_PROD, doneCode);
		}
		saveAllPublic(doneCode,getBusiParam());
	}




	@Override
	public void saveRefreshCa(String refreshType) throws Exception {
		List<CUser> userList = getBusiParam().getSelectedUsers();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		for (CUser user:userList){
			//用户刷新
			authComponent.sendAuth(user, null, BusiCmdConstants.REFRESH_TERMINAL, doneCode);
			//产品指令重发
			List<CProdOrder> prodList = orderComponent.queryOrderProdByUserId(user.getUser_id());
			authComponent.sendAuth(user, prodList, BusiCmdConstants.ACCTIVATE_PROD, doneCode);
		}

		saveAllPublic(doneCode,getBusiParam());
	}


	public void saveBatchUpdateUserName(List<CUser> userList, String custId) throws Exception {

		if(CollectionHelper.isEmpty(userList)){
			return ;
		}
		CCust cust=custComponent.queryCustById(custId);
		if(cust==null){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		Integer doneCode = this.doneCodeComponent.gDoneCode();
		
		BusiParameter busiParam = getBusiParam();
		CustFullInfoDto custFullInfo=new CustFullInfoDto();
		custFullInfo.setCust(cust);
		busiParam.setCustFullInfo(custFullInfo);
		for (CUser user : userList) {
			userComponent.updateUserNameByDeviceCode(user,custId);
		}
		
		
		busiParam.setBusiCode(BusiCodeConstants.BATCH_MOD_USER_NAME);//其他参数不需要
		saveAllPublic(doneCode,busiParam);
	}


	@Override
	public void saveEditPwd(String newLoginName, String newPwd) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		CUser selectedUser = getBusiParam().getSelectedUsers().get(0);
		CUser user = queryUserById(selectedUser.getUser_id());
		List<CUserPropChange> propChangeList = new ArrayList<CUserPropChange>();

		CUserPropChange propChange = new CUserPropChange();
		propChange.setColumn_name("password");
		propChange.setOld_value(user.getPassword());
		propChange.setNew_value(newPwd);
		propChangeList.add(propChange);
		
		user.setPassword(newPwd);
		
		if(!user.getLogin_name().equals(newLoginName)){
			this.validAccount(newLoginName);
			propChange = new CUserPropChange();
			propChange.setColumn_name("login_name");
			propChange.setOld_value(user.getLogin_name());
			propChange.setNew_value(newLoginName);
			propChangeList.add(propChange);
			
			user.setLogin_name(newLoginName);
			
			List<CProdOrder> orderList = orderComponent.queryNotExpAllOrderByUser(user.getUser_id());
			if(user.getUser_type().equals(USER_TYPE_BAND)){
				authComponent.sendAuth(user, null, BusiCmdConstants.REFRESH_TERMINAL, doneCode);
			}else{
				authComponent.sendAuth(selectedUser, orderList, BusiCmdConstants.DEL_USER, doneCode);
				authComponent.sendAuth(user, orderList, BusiCmdConstants.CREAT_USER, doneCode);
			}
			authComponent.sendAuth(user, orderList, BusiCmdConstants.ACCTIVATE_PROD, doneCode);
		}else{
			authComponent.sendAuth(user, null, BusiCmdConstants.CHANGE_USER, doneCode);
		}
		
		userComponent.editUser(doneCode, user.getUser_id(), propChangeList);
		
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
		return userComponent.getByDeptId(deptId);
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

	public void batchLogoffUser(List<CancelUserDto> cancelUserList) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		CCust cust = getBusiParam().getCust();
		String busiCode = getBusiParam().getBusiCode();
		for(CancelUserDto cancelUser : cancelUserList){
			String userId = cancelUser.getUser_id();
			CUser user = userComponent.queryUserById(userId);
			if(user == null){
				throw new ServicesException(ErrorCode.CustDataException);
			}
			
			logoffUser(doneCode, busiCode, cust, userId, cancelUser.getActive_fee(), cancelUser.getActive_fee());
		}
		saveAllPublic(doneCode,getBusiParam());
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
	
	@Override
	public List<PSpkgOpenuser> querySpkgUser(String custId,String spkgSn) throws Exception {
		
		custComponent.checkCustSpkgSnConfirm(custId,spkgSn);	
		return userComponent.querySpkgUser(spkgSn);
	}
	
	@Override
	public List<PSpkgOpenbusifee> querySpkgOpenFee(String spkgSn) throws Exception {
		return userComponent.querySpkgOpenFee(spkgSn);
	}
	
	private void stopProd(Integer doneCode, CProdOrderDto order, String status) throws Exception {
		List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
		changeList.add(new CProdPropChange("status",
				order.getStatus(), status));
		changeList.add(new CProdPropChange("status_date",
				DateHelper.dateToStr(order.getStatus_date()),DateHelper.dateToStr(new Date())));
		
		orderComponent.editProd(doneCode,order.getOrder_sn(),changeList);
	}
	
	
	
	
	/**验证用户能不能报停
	 * 1、协议期用户不能报亭
	 * 2、OTT主机没有报停的情况下，副机不能报停
	 * 3、如果用户名下有未到期的归属客户套餐的产品，套餐下用户必须同时报停
	 */
	
	
	
	
	/**
	 * 固定的ip外挂费用信息
	 * @return
	 * @throws Exception
	 */
	public BusiFeeDto queryUserIpFee()throws Exception{
		return feeComponent.getBusiFee(SystemConstants.USER_IP_FEE_ID);
	}
	
	
	/**
	 * 新增外挂IP
	 * @param user
	 * @throws Exception
	 */
	public void saveAddIpUser(CUser user) throws Exception{
		CCust cust = getBusiParam().getCust();
		doneCodeComponent.lockCust(cust.getCust_id());
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		// 获取客户信息
		List<CUserPropChange> propChangeList = new ArrayList<CUserPropChange>();
		CUser oldUser = userComponent.queryUserById(user.getUser_id());
		
		if(StringHelper.isNotEmpty(user.getStr4())){
			CUserPropChange propChange = new CUserPropChange();
			propChange.setColumn_name("str4");
			propChange.setOld_value(oldUser.getStr4());
			propChange.setNew_value(user.getStr4());
			propChangeList.add(propChange);
		}
		
		if(StringHelper.isNotEmpty(user.getStr5())){
			CUserPropChange propChange = new CUserPropChange();
			propChange.setColumn_name("str5");
			propChange.setOld_value(oldUser.getStr5());
			propChange.setNew_value(user.getStr5());
			propChangeList.add(propChange);
		}
		
		if(StringHelper.isNotEmpty(user.getStr6())){
			CUserPropChange propChange = new CUserPropChange();
			propChange.setColumn_name("str6");
			propChange.setOld_value(oldUser.getStr6());
			propChange.setNew_value(user.getStr6());
			propChangeList.add(propChange);
		}
		
		userComponent.editUser(doneCode, getBusiParam().getSelectedUserIds().get(0), propChangeList);
			
		// 设置拦截器所需要的参数
		getBusiParam().resetUser();
		CUser newUser = userComponent.queryUserById(user.getUser_id());
		getBusiParam().addUser(newUser);
		saveAllPublic(doneCode, getBusiParam());
	}
	
	public CProdOrderDto queryBandLastOrder(String userId)throws Exception{
		return orderComponent.getBandLastOrder(userId);
	}
	
	/**
	 * 补收IP费用所需要的信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String , Object> queryIpFeeLoad(String userId)throws Exception{
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("CProdOrder", queryBandLastOrder(userId)); //最后订单
		map.put("tBusiFee", queryUserIpFee());//IP费 配置信息
		return map;
	}

	/**
	 * 保存IP费用
	 * @throws Exception
	 */
	public void savePayIpFee() throws Exception{
		CCust cust = getBusiParam().getCust();
		doneCodeComponent.lockCust(cust.getCust_id());
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		saveAllPublic(doneCode, getBusiParam());
	}

	@Override
	public void cancelInstallTask(String taskId) throws Exception {
		WTaskBaseInfo task = wTaskBaseInfoDao.findByKey(taskId);
		List<WTaskBaseInfo> taskList = wTaskBaseInfoDao.queryTaskByDoneCode(task.getDone_code());
		
		List<String> userIdList = new ArrayList<String> ();
		List<WTaskUser> allUserList = new ArrayList<>();
		for(WTaskBaseInfo tb:taskList){
			List<WTaskUser> userList = wTaskUserDao.queryByTaskId(tb.getTask_id());
			for(WTaskUser user:userList){
				userIdList.add(user.getUser_id());
				allUserList.add(user);
			}
		}
		
		String[] userIds = userIdList.toArray(new String[userIdList.size()]);
		int busiDoneCode = task.getDone_code();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		boolean hasUnpay = false;
		//find unpay busi fee
		List<CFee> feeList= feeComponent.queryByBusiDoneCode(busiDoneCode);
		for (CFee fee:feeList){
			if (fee.getStatus().equals(StatusConstants.UNPAY)){
				feeComponent.saveCancelFeeUnPay(fee, doneCode);
				if (fee.getBusi_code().equals(BusiCodeConstants.FEE_EDIT)){
					//作废业务
					doneCodeComponent.cancelDoneCode(fee.getCreate_done_code());
				} 
				
			} 
		}
		// find pay busi fee
		feeList = feeComponent.querySumFeeByDoneCode(task.getCust_id(), busiDoneCode);
		for (CFee fee:feeList){
			if (fee.getReal_pay()>0 && fee.getFee_type().equals(SystemConstants.FEE_TYPE_BUSI)){
				feeComponent.saveBusiFee(task.getCust_id(), fee.getAddr_id(), fee.getFee_id(), 
						1, SystemConstants.PAY_TYPE_UNPAY, fee.getReal_pay()*-1, doneCode, busiDoneCode, 
						BusiCodeConstants.TASK_CANCEL,null, null);
				hasUnpay = true;
			}
		}
		
		// find pay device fee
		List<CFeeDevice> deviceFeeList = feeComponent.queryDeviceByDoneCode(busiDoneCode);
		for (CFeeDevice fee:deviceFeeList){
			if (fee.getReal_pay()>0 && fee.getStatus().equals(StatusConstants.PAY)){
				feeComponent.saveDeviceFee(task.getCust_id(), fee.getAddr_id(), fee.getFee_id(), null, 
						SystemConstants.PAY_TYPE_UNPAY, fee.getDevice_type(), fee.getDevice_id(),
						fee.getDevice_code(), null, null, null, null, fee.getDevice_model(), fee.getReal_pay()*-1,
						doneCode, busiDoneCode, BusiCodeConstants.TASK_CANCEL, -1);
				hasUnpay = true;
			}
		}
		
		//取消工单用户相关的产品订单
		if(cancelInstallTaskOrder(task,userIds,doneCode)){
			hasUnpay=true;
		}
		
		if (hasUnpay){
			doneCodeComponent.saveDoneCodeUnPay(task.getCust_id(),doneCode , getOptr().getOptr_id());
		}
		//update device status to idle,write off user
		//设置流水中user_id
		BusiParameter parameter = getBusiParam();
		List<CUser> userList = userComponent.queryUserByTaskId(taskId);
		if(userList.size() > 0){
			parameter.setSelectedUsers(userList);
		}
		for (WTaskUser user:allUserList){
			if (StringHelper.isNotEmpty(user.getDevice_id())){
				DeviceDto device = deviceComponent.queryDeviceByDeviceCode(user.getDevice_id());
				deviceComponent.updateDeviceDepotStatus(doneCode, BusiCodeConstants.TASK_CANCEL, device.getDevice_id(), 
					StatusConstants.USE, StatusConstants.ACTIVE,null, true);
			}
			
			// send write off cmd
			CUser cuser = userComponent.queryUserById(user.getUser_id());
			if(cuser == null)
				continue;
			authComponent.sendAuth(cuser, null, BusiCmdConstants.DEL_USER, doneCode);
			
			//write off user
			userComponent.removeUserWithHis(doneCode, cuser);
		}
		
		//cancel task
		for (WTaskBaseInfo tb:taskList){
			snTaskComponent.cancelTask(doneCode, tb.getTask_id());
		}
		
		parameter.setBusiCode(BusiCodeConstants.TASK_CANCEL);
		snTaskComponent.saveTaskCreateBusiExt(task.getTask_id(), task.getDone_code(), this.getBusiParam());
		saveAllPublic(doneCode, getBusiParam());
		
	}
	/**
	 * 取消工单相关的订单
	 * 订单提取跟工单相关的订单，a类：所有单产品订单（退订） 、 b类：在工单创建之后订购的套餐（套餐退订）、 c类：在工单创建之前订购的套餐的相关用户子产品(子产品移除)
	 * 提取a和b订单的未支付费用做取消操作，并恢复被覆盖产品
	 * a,b如果订单费用已支付，则操作退款，退所有钱（可退的现金退款，不可退的退账户）
	 * c类只做移除子产品。
	 * @throws Exception 
	 */
	private boolean cancelInstallTaskOrder(WTaskBaseInfo task,String[] userIds,Integer doneCode) throws Exception{
		boolean hasUnpay=false;
		List<CProdOrder> changeOrderList=new ArrayList<>();
		
		//List<CFeeAcct> acctFeeList = cFeeDao.queryUserUnPayOrderFee(task.getCust_id(), userIds);
		//提取a类和b类的未支付费用记录 
		List<CFeeAcct> acctFeeList = cFeeDao.queryTaskUserUnPayCFeeAcct(task.getCust_id(),userIds,task.getDone_code());
		for(CFeeAcct feeAcct:acctFeeList){
			//先取消订单修改
			if(feeAcct.getBusi_code().equals(BusiCodeConstants.ORDER_EDIT)){
				//回退订单修改的费用
				List<CProdOrderFeeOut> outList=cProdOrderFeeOutDao.queryByDoneCodeTransFee(feeAcct.getCreate_done_code());
				orderComponent.saveOrderFeeOutToBack(outList,doneCode);
				//作废缴费信息
				feeComponent.saveCancelFeeUnPay(feeAcct, doneCode);
				//作废业务
				doneCodeComponent.cancelDoneCode(feeAcct.getCreate_done_code());
			}
		}
		for(CFeeAcct feeAcct:acctFeeList){
			if(feeAcct.getReal_pay()>0&&
					(feeAcct.getBusi_code().equals(BusiCodeConstants.PROD_PACKAGE_ORDER)
					||feeAcct.getBusi_code().equals(BusiCodeConstants.PROD_UPGRADE)
					||feeAcct.getBusi_code().equals(BusiCodeConstants.PROD_CONTINUE)
					||feeAcct.getBusi_code().equals(BusiCodeConstants.PROD_SINGLE_ORDER))){
				//套餐订购，续订，升级,单用户订购存在覆盖转移支付的情况，恢复被覆盖的订单
				CProdOrder order=cProdOrderDao.findByKey(feeAcct.getProd_sn());
				changeOrderList.addAll(orderComponent.recoverTransCancelOrder(order.getDone_code(),order.getCust_id(),doneCode));
				cProdOrderFeeDao.deleteOrderFeeByOrderSn(feeAcct.getProd_sn());
				//移除订单到历史表
				changeOrderList.addAll(orderComponent.saveCancelProdOrder(order, doneCode));
				//作废缴费信息
				feeComponent.saveCancelFeeUnPay(feeAcct, doneCode);
				//作废业务
				doneCodeComponent.cancelDoneCode(feeAcct.getCreate_done_code());
			}
		}
		//处理已支付的订单退款
		List<CProdOrderFee> orderFeeList = cProdOrderFeeDao.queryTaskPayedOrderFeeByUser(task.getCust_id(), userIds, task.getDone_code());
		
		for (CProdOrderFee orderFee:orderFeeList){
			if (orderFee.getInput_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE) && orderFee.getFee()>0){
				hasUnpay = true;
				CProdOrder order = cProdOrderDao.findByKey(orderFee.getOrder_sn());
				CFeeAcct fee = cFeeDao.queryAcctFeeByOrderSn(orderFee.getOrder_sn());
				PayDto pay=new PayDto();
				pay.setCust_id(task.getCust_id());
				pay.setUser_id(order.getUser_id());
				pay.setAcct_id(fee.getAcct_id());
				pay.setAcctitem_id(fee.getAcctitem_id());
				pay.setFee(orderFee.getFee()*-1);
				pay.setPresent_fee(0);
				
				pay.setProd_sn(orderFee.getOrder_sn());
				pay.setInvalid_date(DateHelper.dateToStr(DateHelper.today().before(order.getEff_date())? order.getEff_date():DateHelper.today()));
				pay.setBegin_date(DateHelper.dateToStr(order.getExp_date()));
				CFeeAcct refundFee = feeComponent.saveAcctFee(task.getCust_id(), fee.getArea_id(), pay, doneCode, BusiCodeConstants.TASK_CANCEL, StatusConstants.UNPAY);
				orderFee.setOutput_sn(refundFee.getFee_sn());
				orderFee.setOutput_fee(orderFee.getFee());
				orderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_CFEE);
			} else if (orderFee.getInput_type().equals(SystemConstants.ORDER_FEE_TYPE_ACCT) && orderFee.getFee()>0){
				//账户
				CAcct acct=acctComponent.queryCustAcctByCustId(task.getCust_id());
				//按订单转账到公用账目中
				String acctItemId=SystemConstants.ACCTITEM_PUBLIC_ID;
				String acctId=acct.getAcct_id();
				String acct_change_sn=acctComponent.saveAcctAddFee(task.getCust_id(), acct.getAcct_id(), acctItemId,
						SystemConstants.ACCT_CHANGE_TRANS, orderFee.getFee(), orderFee.getFee_type(), 
						 BusiCodeConstants.TASK_CANCEL, doneCode,orderFee.getRemark()).getAcct_change_sn();
				
				orderFee.setOutput_sn(acct_change_sn);
				orderFee.setOutput_fee(orderFee.getFee());
				orderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_ACCT);
				//cProdOrderFeeDao.update(orderFee);
			}
		}
		//已支付的费用转出记录
		orderComponent.saveOrderFeeOut(orderComponent.getOrderFeeOutFromOrderFee(orderFeeList), doneCode);
		//退订相关所有订单
		List<CProdOrder> taskCancelOrders=cProdOrderDao.queryTaskCancelOrder(task.getCust_id(), userIds, task.getDone_code());
		Set<String> hasDetlSet=new HashSet<String>();//已退订的产品
		for(CProdOrder order:taskCancelOrders){
			//先退订单产品和套餐产品
			if(StringHelper.isEmpty(order.getPackage_sn())){
				changeOrderList.addAll(orderComponent.saveCancelProdOrder(order, doneCode));
				hasDetlSet.add(order.getOrder_sn());
			}
		}
		for(CProdOrder order:taskCancelOrders){
			//再退订套餐子产品，且对应套餐没有被退订
			if(StringHelper.isNotEmpty(order.getPackage_sn())&&!hasDetlSet.contains(order.getPackage_sn())){
				changeOrderList.addAll(orderComponent.saveCancelProdOrder(order, doneCode));
			}
		}
		//移动剩余订单接续
		Map<String,CUser> userMap=userComponent.queryUserMap(task.getCust_id());
		orderComponent.moveOrderByCancelOrder(changeOrderList, userMap, doneCode);
		//处理授权
		this.authProdNoPackage(changeOrderList, userMap, doneCode);
		return hasUnpay;
	}

	public void saveSaleDevice(String userId, String deviceModel, String deviceBuyMode, FeeInfoDto deviceFee) throws Exception{
		//获取客户用户信息
		CCust cust = getBusiParam().getCust();
		String  custId = cust.getCust_id();
		doneCodeComponent.lockCust(custId);
		String busiCode = getBusiParam().getBusiCode();
		CUser user = userComponent.queryUserById(userId);
		if(!user.getCust_id().equals(custId)){
			throw new ServicesException(ErrorCode.CustDataException);
		}
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		//查询设备
		String deviceCode = null;
		if(StringHelper.isNotEmpty(user.getStb_id())){
			deviceCode = user.getStb_id();
		}else if(StringHelper.isNotEmpty(user.getModem_mac())){
			deviceCode = user.getModem_mac();
		}
		DeviceDto device = deviceComponent.queryDeviceByDeviceCode(deviceCode);
		if(device == null || !device.getDevice_model().equals(deviceModel)){
			throw new ServicesException(ErrorCode.DeviceNotExists);
		}
		
		TDeviceBuyMode buyModeCfg = busiConfigComponent.queryBuyMode(deviceBuyMode);
		if(buyModeCfg!= null && device.getOwnership().equals(SystemConstants.OWNERSHIP_GD)
					&& buyModeCfg.getChange_ownship().equals(SystemConstants.BOOLEAN_TRUE)){
			String newOwnerShip = SystemConstants.OWNERSHIP_CUST;
			deviceComponent.updateDeviceOwnership(doneCode, busiCode, device.getDevice_id(),device.getOwnership(),newOwnerShip,deviceBuyMode,true);
		}
		//保存设备费用
		if (deviceFee != null && deviceFee.getFee_id()!= null && deviceFee.getFee()>0){
			String payType = SystemConstants.PAY_TYPE_UNPAY;
			if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
				payType = this.getBusiParam().getPay().getPay_type();
			doneCodeComponent.saveDoneCodeUnPay(cust.getCust_id(), doneCode, getBusiParam().getOptr().getOptr_id());

			feeComponent.saveDeviceFee( cust.getCust_id(), cust.getAddr_id(),deviceFee.getFee_id(),deviceFee.getFee_std_id(), 
					payType,device.getDevice_type(), device.getDevice_id(), device.getDevice_code(),
					null,null,null,null,device.getDevice_model(),deviceFee.getFee(), doneCode,doneCode, busiCode, 1);
		}
		
		CCustDevice custDevice = custComponent.queryCustDeviceByDeviceId(device.getDevice_id());
		deviceComponent.updateBuyMode(doneCode, busiCode, device.getDevice_id(),custDevice.getBuy_mode(),deviceBuyMode);
		custComponent.updateDeviceBuyMode(doneCode, cust.getCust_id(), device.getDevice_id(), deviceBuyMode);
		
		List<CUserPropChange> propChangeList = new ArrayList<CUserPropChange>();
		propChangeList.add(new CUserPropChange("str10", user.getStr10(), deviceBuyMode));
		userComponent.editUser(doneCode, userId, propChangeList);
		
		saveAllPublic(doneCode, getBusiParam());
		
	}

	public void saveDeviceReclaim() throws Exception{
		
		CCust cust = getBusiParam().getCust();
		doneCodeComponent.lockCust(cust.getCust_id());
		List<CUser> users = getBusiParam().getSelectedUsers();
		Integer doneCode = doneCodeComponent.gDoneCode();
		for(CUser user : users){
			CUser cuser = userComponent.queryUserById(user.getUser_id());
			//更新设备状态和仓库
			String deviceId = user.getStb_id();
			if(cuser.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
				deviceId = user.getModem_mac();
			}
			DeviceDto device = deviceComponent.queryDeviceByDeviceCode(deviceId);
			deviceComponent.updateDeviceDepotId(doneCode, BusiCodeConstants.DEVICE_USER_RECLAIM, device.getDevice_id(), 
					device.getDepot_id(), getOptr().getDept_id(),cuser.getStr10(), true);
			deviceComponent.updateDeviceDepotStatus(doneCode, BusiCodeConstants.DEVICE_USER_RECLAIM, device.getDevice_id(),
					device.getDepot_status(), StatusConstants.IDLE,cuser.getStr10(), true);
			deviceComponent.updateDeviceOwnership(doneCode, BusiCodeConstants.DEVICE_USER_RECLAIM, device.getDevice_id(), 
					device.getOwnership(), SystemConstants.OWNERSHIP_GD, cuser.getStr10(), true);
			//删除客户设备
			custComponent.removeDevice(cust.getCust_id(), device.getDevice_id(), doneCode, SystemConstants.BOOLEAN_FALSE);
			//更新用户设备信息为空
			
			List<CUserPropChange> propChangeList = new ArrayList<CUserPropChange>();
			if(StringHelper.isNotEmpty(cuser.getStb_id()))
				propChangeList.add(new CUserPropChange("stb_id", cuser.getStb_id(), ""));
			if(StringHelper.isNotEmpty(cuser.getCard_id()))
				propChangeList.add(new CUserPropChange("card_id", cuser.getCard_id(), ""));
			if(StringHelper.isNotEmpty(cuser.getModem_mac()))
				propChangeList.add(new CUserPropChange("modem_mac", cuser.getModem_mac(), ""));
//			propChangeList.add(new CUserPropChange("status", cuser.getStatus(), StatusConstants.UNTUCKEND));
//			propChangeList.add(new CUserPropChange("status_date", DateHelper.dateToStr(cuser.getStatus_date()),DateHelper.dateToStr(new Date())));
			userComponent.editUser(doneCode, user.getUser_id(), propChangeList);
			authComponent.sendAuth(cuser, null, BusiCmdConstants.DEL_USER, doneCode);
		}
		saveAllPublic(doneCode, getBusiParam());

		
	}

	
}
