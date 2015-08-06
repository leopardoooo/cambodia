package com.ycsoft.business.service.impl;

import static com.ycsoft.commons.constants.SystemConstants.ACCT_TYPE_SPEC;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.job.JUserStop;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.core.user.FillUSerDeviceDto;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.component.core.OrderComponent;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.prod.CProdPropChangeDao;
import com.ycsoft.business.dto.core.fee.FeeInfoDto;
import com.ycsoft.business.dto.core.prod.DisctFeeDto;
import com.ycsoft.business.dto.core.prod.PromotionDto;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.business.service.IUserService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
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
	private CProdPropChangeDao cProdPropChangeDao;
	
	public void createUser(CUser user, String deviceCode, String deviceType, String deviceModel, String deviceBuyMode,
			FeeInfoDto deviceFee) throws Exception {
		// 获取客户信息
		CCust cust = getBusiParam().getCust();
		String custId = cust.getCust_id();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
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
		
		//设置用户终端类型
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_OTT)) {
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
		}
		
		userComponent.createUser(user);

		// 修改客户状态为正常状态
		if (cust.getStatus().equals(StatusConstants.PREOPEN)) {
			custComponent.updateCustStatus(doneCode, custId, StatusConstants.PREOPEN, StatusConstants.ACTIVE);
		}
		//处理设备和授权
		if (!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
			TDeviceBuyMode buyModeCfg = busiConfigComponent.queryBuyMode(deviceBuyMode);
			String ownership = SystemConstants.OWNERSHIP_GD;
			if (buyModeCfg.getChange_ownship().equals(SystemConstants.BOOLEAN_TRUE))
				ownership = SystemConstants.OWNERSHIP_CUST;
			this.buyDevice(device, deviceBuyMode,ownership, deviceFee, getBusiParam().getBusiCode(), cust, doneCode);
			
		}
		
		
		if (user.getStatus().equals(StatusConstants.ACTIVE)){
			createUserJob(user, custId, doneCode);
		}
		
		// 设置拦截器所需要的参数
		getBusiParam().resetUser();
		getBusiParam().addUser(user);
		saveAllPublic(doneCode, getBusiParam());

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
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.REFRESH_TERMINAL, user.getCust_id(), user.getUser_id(),
					null, null, null, null, null, null);
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
	 * 用户更换设备
	 */
	@Override
	public void saveChangeDevice(String userId, String deviceCode, String devcieBuyMode, 
			FeeInfoDto deviceFee,boolean reclaim)
			throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		CUser oldUser = userComponent.queryUserById(userId);
		CUser user = userComponent.queryUserById(userId);
		CCust cust = custComponent.queryCustById(user.getCust_id());
		
		String oldDeviceCode = user.getStb_id();
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND))
			oldDeviceCode = user.getModem_mac();
		DeviceDto oldDevice = deviceComponent.queryDeviceByDeviceCode(oldDeviceCode);
		DeviceDto device = deviceComponent.queryDeviceByDeviceCode(deviceCode);
		deviceComponent.checkChangeDevice(oldDevice, device);
		
		//修改用户设备信息
		setUserDeviceInfo(user, device);
		userComponent.updateDevice(doneCode, user);
		//处理授权
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_OTT)){
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.CHANGE_USER,
					cust.getCust_id(), userId, null, null,
					null, null, null, null);
		} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTT)){
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_TERMINAL,
					user.getCust_id(), userId, oldUser.getStb_id(), oldUser.getCard_id(), oldUser.getModem_mac(), 
					null, null, null);
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.REFRESH_TERMINAL,
					cust.getCust_id(), userId, null, null,
					null, null, null, null);
		}
		//处理设备购买和回收
		this.buyDevice(device, devcieBuyMode, oldDevice.getOwnership(), deviceFee, getBusiParam().getBusiCode(),
				cust, doneCode);
		if (reclaim){
			deviceComponent.saveDeviceReclaim(doneCode,  getBusiParam().getBusiCode(),
					oldDevice.getDevice_id(), cust.getCust_id(), "change device");
		}
	}



	

	@Override
	public void createUser(CUser user, String deviceBuyMode, FeeInfoDto deviceFee) throws Exception {
		// TODO Auto-generated method stub
		
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
	public void saveRemoveUser(String banlanceDealType, String transAcctId, String transAcctItemId) throws Exception {
		// TODO Auto-generated method stub
		
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
		if (users == null || users.get(0) == null)
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
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取操作的客户、用户信息
		CCust cust = getBusiParam().getCust();
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
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_USER, cust.getCust_id(),
						user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), null, null,JsonHelper.fromObject(user));
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
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取操作的客户、用户信息
		CCust cust = getBusiParam().getCust();
		List<CUser> users = getBusiParam().getSelectedUsers();
		List<CProdOrderDto> orderList = cProdOrderDao.queryCustEffOrderDto(cust.getCust_id());
		boolean isCustPkgOpen = false;
		for(CUser user:users){
			updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.ACTIVE);
			//生成钝化用户JOB
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_USER, cust.getCust_id(),
					user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), null, null,JsonHelper.fromObject(user));
			//修改订单状态为正常状态，并更新到期日
			for (CProdOrderDto order:orderList){
				if (StringHelper.isNotEmpty(order.getUser_id()) && order.getUser_id().equals(user.getUser_id())){
					openProd(doneCode, order);
					
					if (StringHelper.isNotEmpty(order.getPackage_sn())){
						isCustPkgOpen = true;
					}
				}
			}			
		}
		
		if (isCustPkgOpen){
			//修改套餐状态
			for (CProdOrderDto order:orderList){
				if (!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
					openProd(doneCode, order);
				}
			}
		}
		
	}


	




	@Override
	public void saveResendCa() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveRefreshCa(String refreshType) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveEditPwd(String newPwd) throws Exception {
		// TODO Auto-generated method stub
		
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




	private void buyDevice(DeviceDto device,String buyMode,String ownership,FeeInfoDto fee, 
			String busiCode,CCust cust,Integer doneCode) throws Exception {
		//增加客户设备
		custComponent.addDevice(doneCode, cust.getCust_id(),
				device.getDevice_id(), device.getDevice_type(), device.getDevice_model(), 
				device.getPairCard() ==null?null:device.getPairCard().getDevice_id(),
				device.getPairCard() ==null?null:device.getPairCard().getCard_id(), 
				null, null,buyMode);
		//保存设备费用
		if (fee != null && fee.getFee_id()!= null){
			String payType = SystemConstants.PAY_TYPE_CASH;
			if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
				payType = this.getBusiParam().getPay().getPay_type();
			feeComponent.saveDeviceFee( cust.getCust_id(), cust.getAddr_id(),fee.getFee_id(),fee.getFee_std_id(), 
					payType,device.getDevice_type(), device.getDevice_id(), device.getDevice_code(),
					null,
					null,
					null,
					null,
					device.getDevice_model(),
					fee.getFee(), doneCode,doneCode, busiCode, 1);	
			
		}
		
		if (StringHelper.isNotEmpty(device.getDevice_id())){
			//更新设备仓库状态
			deviceComponent.updateDeviceDepotStatus(doneCode, busiCode, device.getDevice_id(),
					device.getDepot_status(), StatusConstants.USE,true);
			//更新设备产权
			if (SystemConstants.OWNERSHIP_CUST.equals(ownership)){
				deviceComponent.updateDeviceOwnership(doneCode, busiCode, device.getDevice_id(),device.getOwnership(),SystemConstants.OWNERSHIP_CUST,true);
			}
			//更新设备为旧设备
			if (SystemConstants.BOOLEAN_TRUE.equals(device.getUsed()))
				deviceComponent.updateDeviceUsed(doneCode, busiCode, device.getDevice_id(), SystemConstants.BOOLEAN_TRUE, SystemConstants.BOOLEAN_FALSE,true);
		}
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
	
	private void openProd(Integer doneCode, CProdOrderDto order) throws Exception, ServicesException {
		CProdPropChange statusChange = cProdPropChangeDao.queryLastStatus(order.getOrder_sn(), order.getCounty_id());
		if (statusChange == null)
			throw new ServicesException("找不到产品报停记录，请联系管理员");
		//计算报停天数
		int stopDays = DateHelper.getDiffDays(statusChange.getChange_time(), new Date());
		
		List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
		changeList.add(new CProdPropChange("status",
				order.getStatus(),statusChange.getNew_value()));
		changeList.add(new CProdPropChange("status_date",
				DateHelper.dateToStr(order.getStatus_date()),DateHelper.dateToStr(new Date())));
		changeList.add(new CProdPropChange("exp_date",
				DateHelper.dateToStr(order.getExp_date()),DateHelper.dateToStr(DateHelper.addDate(order.getExp_date(), stopDays))));
		
		orderComponent.editProd(doneCode,order.getOrder_sn(),changeList);
	}
	
	private void setUserDeviceInfo(CUser user, DeviceDto device) throws Exception{
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
			if (!device.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM))
				throw new ServicesException("设备类型不正确");
			user.setModem_mac(device.getDevice_code());
		}
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_OTT)){
			if (!device.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB) 
					|| StringHelper.isNotEmpty(device.getPairCard().getCard_id()))
				throw new ServicesException("设备类型不正确");
			user.setStb_id(device.getDevice_code());
			user.setModem_mac(device.getDevice_mac());
		}
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTT)){
			if ( StringHelper.isEmpty(device.getPairCard().getCard_id()))
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
