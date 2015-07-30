package com.ycsoft.business.service.impl;

import static com.ycsoft.commons.constants.SystemConstants.ACCT_TYPE_SPEC;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dto.core.fee.FeeInfoDto;
import com.ycsoft.business.dto.core.prod.DisctFeeDto;
import com.ycsoft.business.dto.core.prod.PromotionDto;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.business.service.IUserService;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
@Service
public class UserServiceSN extends BaseBusiService implements IUserService {
	
	public void createUser(CUser user, String deviceId, String deviceType, String deviceModel, String deviceBuyMode,
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
		DeviceDto device = null;
		if (StringHelper.isNotEmpty(deviceId)){
			device = deviceComponent.queryDeviceByDeviceId(deviceId);
			if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
				user.setModem_mac(device.getDevice_code());
			}
			if (user.getUser_type().equals(SystemConstants.USER_TYPE_OTT)){
				user.setStb_id(device.getDevice_code());
			}
			if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTT)){
				user.setStb_id(device.getDevice_code());
				user.setCard_id(device.getPairCard().getCard_id());
			}
		} else {
			device = new DeviceDto();
			device.setDevice_type(deviceType);
			device.setDevice_model(deviceModel);
		}
		
		//设置用户终端类型
		if (user.getUser_type().equals(SystemConstants.USER_TYPE_OTT) || 
				user.getUser_type().equals(SystemConstants.USER_TYPE_DTT)) {
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
		//处理购买设备
		if (!user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE))
			this.buyDevice(device, deviceBuyMode, deviceFee, getBusiParam().getBusiCode(), cust, doneCode);
		// 生成'创建用户'JOB
		createUserJob(user, custId, doneCode);
		getBusiParam().setBusiConfirmParam("user", user);
		// 设置拦截器所需要的参数
		getBusiParam().resetUser();
		getBusiParam().addUser(user);
		saveAllPublic(doneCode, getBusiParam());

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
	public void saveStop(String effectiveDat, int tjFee) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void cancelStopUser() throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void saveOpen(String stbId, String cardId, String modemMac, int tjFee) throws Exception {
		// TODO Auto-generated method stub
		
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




	private void buyDevice(DeviceDto device,String buyMode,FeeInfoDto fee, String busiCode,CCust cust,Integer doneCode) throws Exception {
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
			TDeviceBuyMode deviceBuyMode = busiConfigComponent.queryBuyMode(buyMode);
			if (SystemConstants.BOOLEAN_TRUE.equals(deviceBuyMode.getChange_ownship())){
				deviceComponent.updateDeviceOwnership(doneCode, busiCode, device.getDevice_id(),device.getOwnership(),SystemConstants.OWNERSHIP_CUST,true);
			}
			//更新设备为旧设备
			if (SystemConstants.BOOLEAN_TRUE.equals(device.getUsed()))
				deviceComponent.updateDeviceUsed(doneCode, busiCode, device.getDevice_id(), SystemConstants.BOOLEAN_TRUE, SystemConstants.BOOLEAN_FALSE,true);
		}
	}

}
