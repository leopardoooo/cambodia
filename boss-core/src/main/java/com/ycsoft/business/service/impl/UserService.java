package com.ycsoft.business.service.impl;

import static com.ycsoft.commons.constants.SystemConstants.ACCTITEM_TJ;
import static com.ycsoft.commons.constants.SystemConstants.ACCT_CHANGE_PROMOTION_CANCEL;
import static com.ycsoft.commons.constants.SystemConstants.ACCT_FEETYPE_PRESENT;
import static com.ycsoft.commons.constants.SystemConstants.BILLING_TYPE_MONTH;
import static com.ycsoft.commons.constants.SystemConstants.BILL_COME_FROM_MUCH;
import static com.ycsoft.commons.constants.SystemConstants.BOOLEAN_FALSE;
import static com.ycsoft.commons.constants.SystemConstants.BOOLEAN_TRUE;
import static com.ycsoft.commons.constants.SystemConstants.COUNTY_9005;
import static com.ycsoft.commons.constants.SystemConstants.DEVICE_TYPE_MODEM;
import static com.ycsoft.commons.constants.SystemConstants.FEE_DISCT_PROM;
import static com.ycsoft.commons.constants.SystemConstants.PRESENT_TYPE_FEE;
import static com.ycsoft.commons.constants.SystemConstants.PRESENT_TYPE_TIME;
import static com.ycsoft.commons.constants.SystemConstants.PROD_ORDER_TYPE_PRESENT;
import static com.ycsoft.commons.constants.SystemConstants.PROD_TYPE_BASE;
import static com.ycsoft.commons.constants.SystemConstants.REFRESH_TYPE_TERMINAL;
import static com.ycsoft.commons.constants.SystemConstants.USER_TYPE_BAND;
import static com.ycsoft.commons.helper.LoggerHelper.debug;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;

import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TDeviceChangeReason;
import com.ycsoft.beans.config.TOpenTemp;
import com.ycsoft.beans.core.acct.CAcct;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CAcctBank;
import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.beans.core.common.ExtCDoneCode;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustDevice;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.job.JUserStop;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.prod.CancelUserDto;
import com.ycsoft.beans.core.promotion.CPromotion;
import com.ycsoft.beans.core.promotion.CPromotionAcct;
import com.ycsoft.beans.core.promotion.CPromotionHis;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserAtv;
import com.ycsoft.beans.core.user.CUserBroadband;
import com.ycsoft.beans.core.user.CUserDtv;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PProdUserRes;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.beans.prod.PSpkgOpenbusifee;
import com.ycsoft.beans.prod.PSpkgOpenuser;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.config.BusiConfigComponent;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.component.core.UserPromComponent;
import com.ycsoft.business.component.resource.DeviceComponent;
import com.ycsoft.business.component.resource.PromComponent;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.fee.FeeBusiFormDto;
import com.ycsoft.business.dto.core.fee.FeeInfoDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.DisctFeeDto;
import com.ycsoft.business.dto.core.prod.ProdResDto;
import com.ycsoft.business.dto.core.prod.PromotionDto;
import com.ycsoft.business.dto.core.prod.ResGroupDto;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.dto.core.user.UserInfo;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.business.service.IUserService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CnToSpell;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;


public class UserService extends BaseBusiService implements IUserService {
	private PromComponent promComponent;
	private UserPromComponent userPromComponent;
	protected BusiConfigComponent busiConfigComponent;
	private ExpressionUtil expressionUtil;
	protected DeviceComponent deviceComponent;
	
	

	@Override
	public void createUserBatch(List<UserInfo> userList, String stopType, String isHand) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void createUser(CUser user, String deviceId, String deviceType, String deviceModel, String deviceBuyMode,
			FeeInfoDto deviceFee) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void createUser(CUser user,String deviceBuyMode,FeeInfoDto deviceFee) throws Exception {
		//获取客户信息
		CCust cust = getBusiParam().getCust();
		String  custId = cust.getCust_id();
		String stbId = user.getStb_id();
		String cardId = user.getCard_id();
		String modemMac = user.getModem_mac();		
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String user_id = userComponent.gUserId();
		//创建账户信息
		//String acctId = acctComponent.createAcct(custId,user_id,  ACCT_TYPE_SPEC, null);
		//创建用户信息
		user.setUser_id(user_id);
		user.setAcct_id(acctComponent.gAcctId());
		user.setCust_id(custId);		
		userComponent.createUser(user);
		
		//处理客户设备
		if (StringHelper.isNotEmpty(cardId)){
			//非居民开户用户多，多个操作员同时开户，同个客户下会出现同个设备开出多个用户来
			CCustDevice custDevice = custComponent.queryCustDeviceByCodeAndCustId(custId, cardId);
			if(custDevice != null && custDevice.getStatus().equals(StatusConstants.USE))
				throw new ServicesException("该设备正在开户中,卡号： "+cardId); 
			updateDevice(doneCode,cardId, custId,StatusConstants.USE);
		}
		
		if (StringHelper.isNotEmpty(stbId)){
			CCustDevice custDevice = custComponent.queryCustDeviceByCodeAndCustId(custId, stbId);
			if(custDevice != null && custDevice.getStatus().equals(StatusConstants.USE))
				throw new ServicesException("该设备正在开户中,机顶盒号： "+stbId);
			updateDevice(doneCode,stbId, custId,StatusConstants.USE);
		}
		
		updateDevice(doneCode,modemMac, custId,StatusConstants.USE);
		
		//修改客户状态为正常状态
		if (cust.getStatus().equals(StatusConstants.PREOPEN)){
			custComponent.updateCustStatus(doneCode,custId,StatusConstants.PREOPEN,StatusConstants.ACTIVE);
		}
		//生成'创建用户'JOB
		createUserJob(user, custId, doneCode);
		
		getBusiParam().setBusiConfirmParam("user", user);
//		if (user instanceof CUserDtv ){
//			//TODO 如果是双向用户自动订购按次点播节目
//			if(DTV_SERV_TYPE_DOUBLE.equals(((CUserDtv) user).getServ_type())){
//				if(BOOLEAN_TRUE.equals(userComponent.queryTemplateConfig(TemplateConfigDto.Config.AUTO_ORDER_VOD.toString()))){
//					orderVodProd(user,doneCode);
//				}
//			}
//		}
//		更新巡检标志		
//		this.updateUserCheckFlag(user.getCard_id());
		
//		// 保存打印信息
//		if (user instanceof CUserAtv) {//模拟电视
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("terminal_type", MemoryDict.getDictName(DictKey.TERMINAL_TYPE, ((CUserAtv) user).getTerminal_type()));
//			map.put("user_type", user.getUser_type_text());
//			doneCodeComponent.saveDoneCodeInfo(doneCode, custId,user.getUser_id(), map);
//		}else if (user instanceof CUserDtv) {// 数字电视
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("terminal_type", MemoryDict.getDictName(DictKey.TERMINAL_TYPE, ((CUserDtv) user).getTerminal_type()));
//			map.put("user_type", user.getUser_type_text());
//			map.put("card_id", user.getCard_id());
//			map.put("stb_id", user.getStb_id());
//			doneCodeComponent.saveDoneCodeInfo(doneCode, custId,user.getUser_id(), map);
//		}else if (user instanceof CUserBroadband) {// 宽带
//			// 保存打印信息
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("user_type", user.getUser_type_text());
//			map.put("modem_mac", user.getModem_mac());
//			map.put("login_name", ((UserDto) user).getLogin_name());
//			map.put("login_password", ((UserDto) user).getLogin_password());
//			doneCodeComponent.saveDoneCodeInfo(doneCode, custId, user.getUser_id(),map);
//		}
		
		//设置拦截器所需要的参数
		getBusiParam().resetUser();
		getBusiParam().addUser(user);
//		saveAllPublic(doneCode,getBusiParam(),busiInfo);
		saveAllPublic(doneCode,getBusiParam());
	}

	public void editUser(List<CUserPropChange> propChangeList) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		userComponent.editUser(doneCode, getBusiParam().getSelectedUserIds().get(0), propChangeList);
		//生成计算用户信用度的JOB
		jobComponent.createCreditCalJob(doneCode, getBusiParam().getCust().getCust_id(), null,BOOLEAN_TRUE);

		Map<String,CUserPropChange> map = CollectionHelper.converToMapSingle(propChangeList, "column_name");
		//如果包含用户密码修改
		if(map.containsKey("password")){
			String  custId = getBusiParam().getCust().getCust_id();
			CUser user = getBusiParam().getSelectedUsers().get(0);
			CUser userDto = queryUserById(user.getUser_id());
			CUserPropChange change = map.get("password");
			userDto.setPassword(change.getOld_value());
			userDto.setNewPassword(change.getNew_value());
			//发送用户修改指令
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.CHANGE_USER, custId,
					userDto.getUser_id(), userDto.getStb_id(), userDto.getCard_id(), userDto.getModem_mac(), null, null,JsonHelper.fromObject(userDto));
			
			//支付密码同步
			jobComponent.saveDataSyncJob(BusiCmdConstants.PSWD_SYNC, JsonHelper.fromObject(userDto), "C_USER");
		}
//		//宽带修改密码
//		if(map.containsKey("login_password")){
//			String  custId = getBusiParam().getCust().getCust_id();
//			CUser user = getBusiParam().getSelectedUsers().get(0);
//			UserDto userDto = queryUserById(user.getUser_id());
//			CUserPropChange change = map.get("login_password");
//			userDto.setLogin_password(change.getOld_value());
//			userDto.setNewPassword(change.getNew_value());
//			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.BAND_EDIT_PWD, custId, user.getUser_id(), null, null, user.getModem_mac(), null, null, JsonHelper.fromObject(userDto));
//		}
		//宽带修改最大连接数
		if(map.containsKey("max_connection")){
			String  custId = getBusiParam().getCust().getCust_id();
			CUser user = getBusiParam().getSelectedUsers().get(0);
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.BAND_EDIT_CONNECT, custId, user.getUser_id(), null, null, user.getModem_mac(), null, null,  JsonHelper.fromObject(user));
		}
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * @param propChangeList
	 */
	public void editUserStatus(List<CUserPropChange> propChangeList) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		CUser user = getBusiParam().getSelectedUsers().get(0);
		userComponent.editUser(doneCode,user.getUser_id() , propChangeList);
		
		saveAllPublic(doneCode,getBusiParam());
	}


	
	/**
	 * 第二终端转副机
	 * @param propChangeList
	 * @param prodSn
	 * @throws Exception
	 */
	public void saveEzdtoFzd(List<CUserPropChange> propChangeList,String prodSn,String newTariffId) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		userComponent.editUser(doneCode, getBusiParam().getSelectedUserIds().get(0), propChangeList);
		//生成计算用户信用度的JOB
		jobComponent.createCreditCalJob(doneCode, getBusiParam().getCust().getCust_id(), null,BOOLEAN_TRUE);
		
		if(StringHelper.isNotEmpty(prodSn)){
			changeTariff(prodSn, newTariffId, DateHelper.formatNow(), null, true,false, doneCode);
		}
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void editStb(String stbId,String cardId) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		CUser user = getBusiParam().getSelectedUsers().get(0);
		String  custId = getBusiParam().getCust().getCust_id();
		
		//修改设备状态
		updateDevice(doneCode,stbId, custId, StatusConstants.USE);
		updateDevice(doneCode,cardId, custId, StatusConstants.USE);
		
		
		List<CUserPropChange> propChangeList = new ArrayList<CUserPropChange>();
		
		if(StringHelper.isNotEmpty(stbId)){
			CUserPropChange propChange = new CUserPropChange();
			propChange.setColumn_name("stb_id");
			propChange.setOld_value(user.getStb_id());
			propChange.setNew_value(stbId);
			propChangeList.add(propChange);
		}
		
		if(StringHelper.isNotEmpty(cardId)){
			CUserPropChange propChange = new CUserPropChange();
			propChange.setColumn_name("card_id");
			propChange.setOld_value("");
			propChange.setNew_value(cardId);
			propChangeList.add(propChange);
		}
		
		
		userComponent.editUser(doneCode, getBusiParam().getSelectedUserIds().get(0), propChangeList);
		
		user.setStb_id(stbId);
		user.setCard_id(cardId);
		
		//如果卡号为空，需发指令，修改到期日
		if(StringHelper.isNotEmpty(cardId)){
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_USER, custId,
					user.getUser_id(), user.getStb_id(),	user.getCard_id(),
									user.getModem_mac(), null, null,JsonHelper.fromObject(user));
			
			List<CProdDto> prodList = userProdComponent.queryByUserId(user.getUser_id());
			for (CProd prod:prodList){
				//生成激活产品任务
				if (isProdOpen(prod.getStatus())){
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, custId,
						user.getUser_id(),user.getStb_id(),	user.getCard_id(),
						user.getModem_mac(),  prod.getProd_sn(),prod.getProd_id());
				}
			
				//报开后更新到期日
				userProdComponent.updateInvalidDate(doneCode, prod.getProd_sn(), DateHelper.getDiffDays(prod.getStatus_date(), new Date()), 0, new CAcctAcctitem());
			}
			
		}
		
		
		//发送用户修改指令
		jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.CHANGE_USER, custId,
				user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), null, null,JsonHelper.fromObject(user));
		
		//生成计算用户信用度的JOB
		jobComponent.createCreditCalJob(doneCode, custId, null,BOOLEAN_TRUE);
		
		saveAllPublic(doneCode,getBusiParam());
	}

	public void saveAtvToDtv(CUser user,int curMonthFee,int payFee) throws Exception {
//		//获取业务流水
//		String  custId = getBusiParam().getCust().getCust_id();
//		Integer doneCode = doneCodeComponent.gDoneCode();
//		String busiCode = getBusiParam().getBusiCode();
//		CUser oldUser = getBusiParam().getSelectedUsers().get(0);
//		String oldAcctId = oldUser.getAcct_id();
//
//		//模拟产品
//		List<CProdDto> prodList = userProdComponent.queryByUserId(oldUser.getUser_id());
//		CProdDto prod = null;
//		if(null != prodList && prodList.size() > 0){
//			prod = prodList.get(0);
//			CAcct custAcct = acctComponent.queryCustAcctByCustId(custId);
//			//保存缴费信息
//			if (payFee>0){
//				PayDto pay = new PayDto();
//				pay.setUser_id(prod.getUser_id());
//				pay.setAcct_id(prod.getAcct_id());
//				pay.setAcctitem_id(prod.getProd_id());
//				pay.setFee(payFee);
//				pay.setTariff_id(prod.getTariff_id());
//				pay.setInvalid_date(DateHelper.dateToStr(prod.getInvalid_date()));
//				this.saveAcctPay(doneCode, pay);
//			}
//			
//			//修改当月账单，出帐金额为curMonthFee
//			if (billComponent.updateBill(prod.getProd_sn(), curMonthFee) ==0){
//				//没有当月账单
//				String billingCycle = DateHelper.format(new Date(), DateHelper.FORMAT_YM);
//				billComponent.createBill(prod, doneCode, 
//						billingCycle, curMonthFee, curMonthFee, BILL_COME_FROM_MANUAL);
//			}
//			//终止模拟产品
//			terminateProd(custId, oldUser, doneCode, busiCode, prod, "TRANS", custAcct.getAcct_id(), ACCTITEM_PUBLIC_ID);
//		}
//
//		//注销模拟用户
//		userComponent.removeUserWithHis(doneCode,oldUser);
//		//生成终止用户的业务指令
//		delUserJob(user, custId, doneCode);
////		acctComponent.removeAcctWithoutHis(oldUser.getAcct_id());
//		
//		String userId = userComponent.gUserId();
//		//创建用户账户
//		String acctId = acctComponent.createAcct(custId,userId,  ACCT_TYPE_SPEC, null);
//		//创建新用户
//		user.setUser_id(userId);
//		user.setCust_id(custId);
//		user.setAcct_id(acctId);
//		userComponent.createUser(user);
//		
//		//处理客户设备
//		if (StringHelper.isNotEmpty(user.getStb_id())){
//			updateDevice(doneCode,user.getStb_id(), custId,StatusConstants.USE);
//		}
//		if (StringHelper.isNotEmpty(user.getCard_id())){
//			updateDevice(doneCode,user.getCard_id(), custId,StatusConstants.USE);
//		}
//		if (StringHelper.isNotEmpty(user.getModem_mac())){
//			updateDevice(doneCode,user.getModem_mac(), custId,StatusConstants.USE);
//		}
//		getBusiParam().setBusiConfirmParam("user", user);
//		
////		if (prod!=null){
//		//生成销帐任务
//		int jobId = jobComponent.createCustWriteOffJob(doneCode, custId,BOOLEAN_TRUE);
//		jobComponent.terminateAcct(jobId, oldAcctId,null,doneCode);
////		}
//		//生成'创建用户'JOB
//		createUserJob(user,custId,doneCode);
//		
//		getBusiParam().getSelectedAtvs().clear();
//		getBusiParam().addUser(user);
//		
////		saveAllPublic(doneCode, getBusiParam(), busiInfo);
//		saveAllPublic(doneCode, getBusiParam());
	}
	
	public void saveCancelOpenInteractive() throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取操作的客户、用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		CUserDtv user = (CUserDtv) getBusiParam().getSelectedUsers().get(0);
		CUser userDto = queryUserById(user.getUser_id());
		
		userComponent.saveCancelOpenInteractive(user.getUser_id(), doneCode);
		
		jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.CANCEL_INTERACTIVE, custId,
				user.getUser_id(), user.getStb_id(), user.getCard_id(), "", null,null,JsonHelper.fromObject(userDto));
		
		String modemMac = user.getModem_mac();
		CCustDevice custDevice = custComponent.queryCustDeviceByCodeAndCustId(custId, modemMac);
		if(custDevice != null){	//单modem
			CUserBroadband band = userComponent.queryBandByDeviceId(modemMac);
			//单modem 双向、宽带共用一个modem
			if(band == null){
				custComponent.updateDeviceStatusByCode(custId, modemMac, StatusConstants.IDLE);
			}
		}
		
		if (StringHelper.isNotEmpty(modemMac)){
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_TERMINAL, custId,
					user.getUser_id(), null, null, modemMac, null, null);
		}
		
		saveAllPublic(doneCode, getBusiParam());
	}


	public void saveOpenInteractive(String netType, String modemMac,
			String password, String vodUserType,String remainReplacoverDate) throws Exception {
		/*
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取操作的客户、用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		CUserDtv user = (CUserDtv) getBusiParam().getSelectedUsers().get(0);
		//修改用户信息
		List<CUserPropChange> changeList = new ArrayList<CUserPropChange>();
		if(!netType.equals(user.getNet_type())){//user.getNet_type().equals(netType)
			CUserPropChange netTypeChange = new CUserPropChange();
			netTypeChange.setColumn_name("net_type");
			netTypeChange.setOld_value(user.getNet_type());
			netTypeChange.setNew_value(netType);
			changeList.add(netTypeChange);
		}
		getBusiParam().setBusiConfirmParam("net_type", netType);
		if (StringHelper.isNotEmpty(modemMac)){
			CUserPropChange modemChange =new CUserPropChange();
			modemChange.setColumn_name("modem_mac");
			modemChange.setOld_value(user.getModem_mac());
			modemChange.setNew_value(modemMac);
			changeList.add(modemChange);
		}
		
		if(StringHelper.isNotEmpty(password)){
			CUserPropChange passwordChange =new CUserPropChange();
			passwordChange.setColumn_name("password");
			passwordChange.setOld_value(user.getPassword());
			passwordChange.setNew_value(password);
			changeList.add(passwordChange);
		}
		
		if(StringHelper.isNotEmpty(vodUserType)){
			CUserPropChange vodUserTypeChange =new CUserPropChange();
			vodUserTypeChange.setColumn_name("str11");
			vodUserTypeChange.setOld_value(user.getStr11());
			vodUserTypeChange.setNew_value(vodUserType);
			changeList.add(vodUserTypeChange);
		}

		CUserPropChange servChange =new CUserPropChange();
		servChange.setColumn_name("serv_type");
		servChange.setOld_value(DTV_SERV_TYPE_SINGLE);
		servChange.setNew_value(DTV_SERV_TYPE_DOUBLE);
		changeList.add(servChange);

		userComponent.editUser(doneCode, user.getUser_id(), changeList);
		//处理客户设备
		updateDevice(doneCode,modemMac, custId,StatusConstants.USE);
		if(StringHelper.isNotEmpty(remainReplacoverDate) && SystemConstants.BOOLEAN_TRUE.equals(remainReplacoverDate)){
			CCustDevice stb = custComponent.queryCustDeviceByCodeAndCustId(user.getCust_id(), user.getStb_id());
			List<CCustDeviceChange> changes = new ArrayList<CCustDeviceChange>();
			CCustDeviceChange change = new CCustDeviceChange();
			change.setColumn_name("replacover_date");
			change.setDevice_id(stb.getDevice_id());
			Date oldDate = stb.getReplacover_date();
			String oldValue = DateHelper.format(oldDate, DateHelper.FORMAT_YMD);
			String newValue = null;
			Calendar newDate = Calendar.getInstance();
			if(oldDate !=null && oldDate.after(newDate.getTime())){
				newDate.setTime(oldDate);
			}
			newDate.add(Calendar.YEAR, 3);
			newValue = DateHelper.format(newDate.getTime(), DateHelper.FORMAT_YMD);
			change.setOld_value(oldValue);
			change.setNew_value(newValue);
			changes.add(change);
			this.custComponent.editCustDevice(doneCode, stb.getDevice_id(), changes);
		}
		
		//生成信用计算、修改用户信息、激活设备任务
		jobComponent.createCreditCalJob(doneCode, custId, null,BOOLEAN_TRUE);
		CUser userDto = queryUserById(user.getUser_id());
		jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.OPEN_INTERACTIVE, custId,
				user.getUser_id(), user.getStb_id(), user.getCard_id(), modemMac, null, null,JsonHelper.fromObject(userDto));
		
		//支付密码同步
		userDto.setPassword(password);
		jobComponent.saveDataSyncJob(BusiCmdConstants.PSWD_SYNC, JsonHelper.fromObject(userDto), "C_USER");
		
		if (StringHelper.isNotEmpty(modemMac)){
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_TERMINAL, custId,
					user.getUser_id(), user.getStb_id(), user.getCard_id(), modemMac, null, null);
		}
		
		//TODO 如果是双向用户自动订购按次点播节目
		if(DTV_SERV_TYPE_DOUBLE.equals(userDto.getServ_type())){
			if(BOOLEAN_TRUE.equals(userComponent.queryTemplateConfig(TemplateConfigDto.Config.AUTO_ORDER_VOD.toString()))){
				orderVodProd(user,doneCode);
			}
		}
		user.setNet_type(netType);
		user.setModem_mac(modemMac);
//		getBusiParam().setBusiConfirmParam("user", user);
		saveAllPublic(doneCode,getBusiParam());
		doneCodeComponent.saveDoneCodeInfo(doneCode, custId, null, getBusiParam().getBusiConfirmParamInfo());
		*/
	}

	/**
	 * @param user
	 */
	private void orderVodProd(CUser user,Integer doneCode) throws Exception {
		//根据产品和资费id获取产品和资费的基本信息
		PProdTariff tariff = prodComponent.queryVodProdTariff();
		if(null != tariff){
			PProd prod = prodComponent.queryProdById(tariff.getProd_id());
			
			CProd cprod = userProdComponent.queryByProdId(user.getUser_id(), prod.getProd_id());
			if(cprod == null){
				String sn = userProdComponent.addProd(doneCode,user.getCust_id(), user.getAcct_id(), user.getUser_id(),
						null,null, prod.getProd_id(),prod.getProd_type(), PROD_ORDER_TYPE_PRESENT, DateHelper.formatNow(),null,
						user.getStop_type(),tariff,null,"F",prod.getIs_base(),null,SystemConstants.BOOLEAN_FALSE);
				
				CAcctAcctitem acctitem = acctComponent.createAcctItem(user.getAcct_id(), prod.getProd_id());
				
				List<CAcctAcctitem> acctItemList = new ArrayList<CAcctAcctitem>();
				acctItemList.add(acctitem);
				
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD,
						user.getCust_id(), user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), sn,prod.getProd_id());
				
				jobComponent.createCreditCalJob(doneCode, user.getCust_id(), acctItemList,BOOLEAN_FALSE);
			}
		}
		
	}

	public void saveEditNetType(String netType,String modemMac) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		CCust cust = getBusiParam().getCust();
		CUser user = getBusiParam().getSelectedUsers().get(0);
		
		String custId = cust.getCust_id();
		
		//修改用户信息
		List<CUserPropChange> changeList = new ArrayList<CUserPropChange>();
		CUserPropChange netTypeChange = new CUserPropChange();
		netTypeChange.setColumn_name("net_type");
		netTypeChange.setOld_value(user.getNet_type());
		netTypeChange.setNew_value(netType);
		changeList.add(netTypeChange);
		
		if(StringHelper.isNotEmpty(modemMac)){
			CUserPropChange macChange = new CUserPropChange();
			macChange.setColumn_name("modem_mac");
			macChange.setOld_value("");
			macChange.setNew_value(modemMac);
			changeList.add(macChange);
		}else{
			CUserPropChange macChange = new CUserPropChange();
			macChange.setColumn_name("modem_mac");
			macChange.setOld_value(user.getModem_mac());
			macChange.setNew_value("");
			changeList.add(macChange);
		}
		//记录用户异动信息
		userComponent.editUser(doneCode, user.getUser_id(), changeList);
		
		//新接入方式需要modem
		if(StringHelper.isNotEmpty(modemMac)){
			//处理客户设备
			updateDevice(doneCode,modemMac, custId, StatusConstants.USE);
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_TERMINAL, custId,
					user.getUser_id(), user.getStb_id(), user.getCard_id(), modemMac, null, null);
		}else{
			//旧接入方式modem改空闲，待回收
			List<CUser> userList = userComponent.queryUserByDevice(DEVICE_TYPE_MODEM, user.getModem_mac());
			if(userList != null && userList.size() == 0){
				custComponent.updateDeviceStatusByCode(custId, user.getModem_mac(), StatusConstants.IDLE);
			}
		}
		
		saveAllPublic(doneCode, getBusiParam());
	}
	
	public void saveStop(String effectiveDate,int tjFee) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取操作的客户、用户信息
		CCust cust = getBusiParam().getCust();
		String busiCode = getBusiParam().getBusiCode();
		List<CUser> users = getBusiParam().getSelectedUsers();
//		if (effectiveDate.equals(DateHelper.getDate("-"))){
//			//当天报停
//			for(CUser user:users){
//				//清除原有未执行的预报停
//				removeStopByUserId(user.getUser_id());
//				//修改客户设备状态
//				custComponent.updateDeviceStatusByCode(cust.getCust_id(), user.getStb_id(), StatusConstants.REQSTOP);
//				custComponent.updateDeviceStatusByCode(cust.getCust_id(), user.getCard_id(), StatusConstants.REQSTOP);
//				custComponent.updateDeviceStatusByCode(cust.getCust_id(), user.getModem_mac(), StatusConstants.REQSTOP);
//				CUser userDto = queryUserById(user.getUser_id());
//				if(userDto.getStatus().equals(StatusConstants.REQSTOP)){
//					throw new ServicesException("该用户已经报停!请重新查询该客户!");
//				}
//				//修改用户状态
//				updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.REQSTOP);
//				//生成钝化用户JOB
//				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_USER, cust.getCust_id(),
//						user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), null, null,JsonHelper.fromObject(userDto));
//				//修改用户产品状态为报停
//				List<CProdDto> prodList = userProdComponent.queryAllProdsByUserId(user.getUser_id());
//				for (CProdDto prod:prodList){
//					List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
//					changeList.add(new CProdPropChange("status",
//							prod.getStatus(),StatusConstants.REQSTOP));
//					changeList.add(new CProdPropChange("status_date",
//							DateHelper.dateToStr(prod.getStatus_date()),DateHelper.dateToStr(new Date())));
//					
//					userProdComponent.editProd(doneCode,prod.getProd_sn(),changeList);
//					
//					//生成钝化产品任务
//					if (isProdOpen(prod.getStatus())){
//						jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD, cust.getCust_id(),
//							user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), prod.getProd_sn(),prod.getProd_id());
//					}
//				}
////				busiInfo += "终端类型："+user.getUser_type_text()+" 设备号:"+user.getStb_id();
//			}
//		} else {
//			getBusiParam().setBusiCode(BusiCodeConstants.USER_PRE_REQUIRE_STOP);		
//			//预报停
//			for(CUser user:users){
//				//清除原有未执行的预报停
//				removeStopByUserId(user.getUser_id());
//				jobComponent.createUserStopJob(doneCode, user.getUser_id(), effectiveDate);
//			}
//		}
//		//保存停机费
//		saveTjFee(doneCode,busiCode, cust.getCust_id(), tjFee);
//		saveAllPublic(doneCode,getBusiParam());
//		
//		// 保存打印数据
//		int atvCount = 0;
//		String stopReason="";
//		CUser atvUserPrint = new CUser();
//		ExtCDoneCode[] extInfo =  getBusiParam().getBusiExtAttr() ;
//		if(extInfo != null){
//			for( ExtCDoneCode info :extInfo ){
//				if(info.getAttribute_id().equals("411")){
//					stopReason = info.getAttribute_value();
//				}
//			}
//		}
//		List<Object> udl = new ArrayList<Object>();
//		for (CUser user : users) {
//			Map<String,Object> map = new HashMap<String,Object>();
//			map.put("busiName", MemoryDict.getDictName(DictKey.BUSI_CODE, getBusiParam().getBusiCode()));
//			if (("ATV").equals(user.getUser_type())) {
//				atvCount ++;
//				atvUserPrint = user;
//			} else if (("DTV").equals(user.getUser_type())) {
//				
//				map.put("user_type", user.getUser_type());
//				CUserDtv dtv = (CUserDtv) user;
//				map.put("terminal_type", dtv.getTerminal_type_text());
//				map.put("card_id", user.getCard_id());
//				map.put("stb_id", user.getStb_id());
//				map.put("effective_date", effectiveDate);
//				map.put("stop_reason", stopReason);
//				map.put("ext_info", extInfo);
//				udl.add(map);
//			} else if(("BAND").equals(user.getUser_type())){
//				map.put("user_type", user.getUser_type());
//				CUserBroadband band = (CUserBroadband) user;
//				map.put("login_name", band.getLogin_name());
//				map.put("modem_mac", user.getModem_mac());
//				map.put("recycle", "Modem及配件已回收");
//				map.put("effective_date", effectiveDate);
//				map.put("stop_reason", stopReason);
//				map.put("ext_info", extInfo);
//				udl.add(map);
//			}
//		}
//		//保存模拟的打印信息
//		if(atvCount > 0){
//			Map<String,Object> map = new HashMap<String,Object>();
//			map.put("user_type", atvUserPrint.getUser_type());
//			map.put("terminal_type", ((CUserAtv) atvUserPrint).getTerminal_type_text());
//			map.put("user_count", getBusiParam().getSelectedUsers().size());
//			map.put("stop_count", atvCount);
//			map.put("effective_date", effectiveDate);
//			map.put("stop_reason", stopReason);
//			map.put("ext_info", extInfo);
//			udl.add(map);
//		}
//		getBusiParam().setBusiConfirmParam("users", udl);
//		doneCodeComponent.saveDoneCodeInfo(doneCode, cust.getCust_id(), null, getBusiParam().getBusiConfirmParamInfo());
	}
	
	//续报停
	public void editUserStop() throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		CUser user = getBusiParam().getSelectedUsers().get(0);
		
		List<CUserPropChange> changeList = new ArrayList<CUserPropChange>();
		CUserPropChange change = new CUserPropChange();
		change.setColumn_name("status_date");
		change.setOld_value(DateHelper.dateToStr(user.getStatus_date()));
		change.setNew_value(DateHelper.dateToStr(new Date()));
		changeList.add(change);
		
		userComponent.editUser(doneCode, user.getUser_id(), changeList);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void cancelStopUser() throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		List<CUser> users = getBusiParam().getSelectedUsers();
		String[] userall = CollectionHelper.converValueToArray(users, "user_id");
		jobComponent.cancelStopUser(userall);
		saveAllPublic(doneCode,getBusiParam());
		
		// 保存打印数据
		int atvCount = 0;
		String stopReason="";
		CUser atvUserPrint = new CUser();
		List<Object> udl = new ArrayList<Object>();
		
		for(CUser user:users){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("busiName", MemoryDict.getDictName(DictKey.BUSI_CODE, getBusiParam().getBusiCode()));
			if (("ATV").equals(user.getUser_type())) {
				atvCount ++;
				atvUserPrint = user;
			} else if (("DTV").equals(user.getUser_type())) {
				
				map.put("user_type", user.getUser_type());
				CUserDtv dtv = (CUserDtv) user;
				map.put("terminal_type", dtv.getTerminal_type_text());
				map.put("card_id", user.getCard_id());
				map.put("stb_id", user.getStb_id());
				udl.add(map);
			} else if(("BAND").equals(user.getUser_type())){
				map.put("user_type", user.getUser_type());
				CUserBroadband band = (CUserBroadband) user;
				map.put("login_name", band.getLogin_name());
				map.put("modem_mac", user.getModem_mac());
				map.put("recycle", "Modem及配件已回收");
				udl.add(map);
			}
		}
		
		//保存模拟的打印信息
		if(atvCount > 0){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("user_type", atvUserPrint.getUser_type());
			map.put("terminal_type", ((CUserAtv) atvUserPrint).getTerminal_type_text());
			map.put("user_count", getBusiParam().getSelectedUsers().size());
			map.put("stop_count", atvCount);
			map.put("stop_reason", stopReason);
			udl.add(map);
		}
		getBusiParam().setBusiConfirmParam("users", udl);
		doneCodeComponent.saveDoneCodeInfo(doneCode, getBusiParam().getCust().getCust_id(), null, getBusiParam().getBusiConfirmParamInfo());
	}
	
	public Object queryStopByUsers(String userLists) throws Exception{
		List<CUser> userList = null;
		String str = "";
		if(StringHelper.isNotEmpty(userLists)){
			Type type = new TypeToken<List<CUser>>(){}.getType();
			userList = JsonHelper.gson.fromJson( userLists , type);
		}
		for(CUser user :userList){
			List<JUserStop> list = jobComponent.queryStopByUserId(user.getUser_id());
			for(JUserStop dto : list){
				if(StringHelper.isNotEmpty(user.getStb_id())){
					str +="机顶盒号为:"+user.getStb_id()+"的用户预报停日期为【"+DateHelper.dateToStr(dto.getStop_date())+"】;";
				}else if(StringHelper.isNotEmpty(user.getModem_mac())){
					str +="MODEM号为:"+user.getModem_mac()+"的用户预报停日期为【"+DateHelper.dateToStr(dto.getStop_date())+"】;";
				}else{
					str +="用户预报停日期为【"+DateHelper.dateToStr(dto.getStop_date())+"】;";
				}
			}
		}
		if(StringHelper.isEmpty(str)){return null;}
		return str;
	}

	/**
	 * 清除预报停，并且使得操作流水失效
	 * @param userId
	 * @throws Exception
	 */
	public void removeStopByUserId(String userId) throws Exception{
		List<JUserStop> userList = jobComponent.queryStopByUserId(userId);
		if(userList.size()>0){
			//预报停需要修改费用，受理记录不失效
			/*for(JUserStop stop:userList){
				CDoneCode cDoneCode = doneCodeComponent.queryByKey(stop.getDone_code());
				//更新流水状态
				if (cDoneCode!=null)
					doneCodeComponent.updateStatus(cDoneCode.getDone_code(),cDoneCode.getBusi_code());
			}*/
			jobComponent.removeByUserId(userId);
		}
	}
	
	public void saveOpen(String stbId,String cardId,String modemMac,int tjFee) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取操作的客户、用户信息
		CCust cust = getBusiParam().getCust();
		String  custId = cust.getCust_id();
		String busiCode = getBusiParam().getBusiCode();
		List<CUser> users = getBusiParam().getSelectedUsers();
		
		//修改客户设备状态
		custComponent.updateDeviceStatusByCode(custId, stbId, StatusConstants.USE);
		custComponent.updateDeviceStatusByCode(custId, cardId, StatusConstants.USE);
		custComponent.updateDeviceStatusByCode(custId, modemMac, StatusConstants.USE);
		List<Object> udl = new ArrayList<Object>();
		int atvCount = 0;
		CUser atvUserPrint = new CUser();
		for(CUser user:users){
			//更新设备状态
			custComponent.updateDeviceStatusByCode(custId, user.getStb_id(), StatusConstants.USE);
			custComponent.updateDeviceStatusByCode(custId, user.getCard_id(), StatusConstants.USE);
			custComponent.updateDeviceStatusByCode(custId, user.getModem_mac(), StatusConstants.USE);

//			userComponent.updateDevice(doneCode,user,
//					StringHelper.isNotEmpty(user.getStb_id())?user.getStb_id():stbId,
//					StringHelper.isNotEmpty(user.getCard_id())?user.getCard_id():cardId,
//					StringHelper.isNotEmpty(user.getModem_mac())?user.getModem_mac():modemMac);
//			
			//修改用户状态
//			String userOldStatus = userComponent.queryUserLastStatus(user.getUser_id());
//			if(StringHelper.isNotEmpty(userOldStatus)){
//				updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), userOldStatus);
//			}else{
//				updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.ACTIVE);
//			}
			
			updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.ACTIVE);
			
			//生成激活用户JOB
			CUser userDto = queryUserById(user.getUser_id());
//			if(USER_TYPE_BAND.equals(userDto.getUser_type()) || StringHelper.isNotEmpty(userDto.getCard_id())){
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_USER, custId,
						user.getUser_id(), StringHelper.isNotEmpty(user.getStb_id())?user.getStb_id():stbId,
								StringHelper.isNotEmpty(user.getCard_id())?user.getCard_id():cardId,
										StringHelper.isNotEmpty(user.getModem_mac())?user.getModem_mac():modemMac, null, null,JsonHelper.fromObject(userDto));
//			}
			//修改用户产品状态为报停前的状态
			List<CProdDto> prodList = userProdComponent.queryAllProdsByUserId(user.getUser_id());
			for (CProdDto prod:prodList){
				String oldStatus = userProdComponent.queryLastStatus(prod.getProd_sn());
//				if (StringHelper.isEmpty(oldStatus)) {
//					if (prod.getInvalid_date().after(prod.getStatus_date()))
//						oldStatus = StatusConstants.ACTIVE;
//					else
//						oldStatus = StatusConstants.OWESTOP;
//				}else if(oldStatus.equals(StatusConstants.REQSTOP)){
//					oldStatus = StatusConstants.ACTIVE;
//				}
//				String oldStatus = StatusConstants.ACTIVE;
				userProdComponent.updateProdStatus(doneCode,prod.getProd_sn(),prod.getStatus(), oldStatus);
				//生成激活产品任务
//				if(StringHelper.isNotEmpty(userDto.getCard_id())){
					if (isProdOpen(oldStatus)){
						jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, cust.getCust_id(),
							user.getUser_id(), StringHelper.isNotEmpty(user.getStb_id())?user.getStb_id():stbId,
									StringHelper.isNotEmpty(user.getCard_id())?user.getCard_id():cardId,
											StringHelper.isNotEmpty(user.getModem_mac())?user.getModem_mac():modemMac, prod.getProd_sn(),prod.getProd_id());
					}
					
					Date invalidDate = userProdComponent.getInvalidDateByFeePro(prod.getProd_sn(), 0);
					//报开后更新到期日
					userProdComponent.updateInvalidDate(doneCode, prod.getProd_sn(), invalidDate);
//					userProdComponent.updateInvalidDate(doneCode, prod.getProd_sn(), DateHelper.getDiffDays(prod.getStatus_date(), new Date()), 0, new CAcctAcctitem());
//				}
			}
//			busiInfo += "终端类型："+user.getUser_type_text()+" 设备号:"+user.getStb_id();
			//更新用户产品的出帐日期
			//next_bill_date已作废
//			userProdComponent.updateNextBillDate(user.getUser_id(), DateHelper.dateToStr(user.getStatus_date()));
			

			Map<String,Object> map = new HashMap<String,Object>();
			map.put("busiName", MemoryDict.getDictName(DictKey.BUSI_CODE, getBusiParam().getBusiCode()));
			if (("ATV").equals(user.getUser_type())) {
				atvCount ++;
				atvUserPrint = user;
			} else if (("DTV").equals(user.getUser_type())) {
				
				map.put("user_type", user.getUser_type());
				CUserDtv dtv = (CUserDtv) user;
				map.put("terminal_type", dtv.getTerminal_type_text());
				map.put("card_id", user.getCard_id());
				map.put("stb_id", user.getStb_id());
				udl.add(map);
			} else if(("BAND").equals(user.getUser_type())){
				map.put("user_type", user.getUser_type());
				CUserBroadband band = (CUserBroadband) user;
				map.put("login_name", band.getLogin_name());
				map.put("modem_mac", user.getModem_mac());
				map.put("recycle", "Modem及配件已回收");
				udl.add(map);
			}
			
		}
		
		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_TRUE);//销帐会自动查信控
		jobComponent.createCreditExecJob(doneCode, custId);
		//生成账务模式判断任务
		jobComponent.createAcctModeCalJob(doneCode, custId);
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		
		//保存停机费
		saveTjFee(doneCode,busiCode, custId, tjFee);

		this.updateUserCheckFlag(cardId);
		
		//保存模拟的打印信息
		if(atvCount > 0){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("user_type", atvUserPrint.getUser_type());
			map.put("terminal_type", ((CUserAtv) atvUserPrint).getTerminal_type_text());
			map.put("user_count", getBusiParam().getSelectedUsers().size());
			map.put("stop_count", atvCount);
			udl.add(map);
		}
		getBusiParam().setBusiConfirmParam("users", udl);
		doneCodeComponent.saveDoneCodeInfo(doneCode, cust.getCust_id(), null, getBusiParam().getBusiConfirmParamInfo());
		saveAllPublic(doneCode,getBusiParam());
	}

	/**
	 *	保存用户销户
	 */
	public void saveRemoveUser(String userId ,String banlanceDealType,String reclaim,Integer cancelFee, String transAcctId,
			String transAcctItemId) throws Exception {
		//获取客户用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		List<CUser> userList = getBusiParam().getSelectedUsers();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		//生成销帐任务
		int jobId = jobComponent.createCustWriteOffJob(doneCode, custId,BOOLEAN_TRUE);
		List<String> stbList =  CollectionHelper.converValueToList(userList, "stb_id");
		List<String> cardList =  CollectionHelper.converValueToList(userList, "card_id");
		List<String> modemList =  CollectionHelper.converValueToList(userList, "modem_mac");
		List<String> devoceList = new ArrayList<String>();
		devoceList.addAll(stbList);
		devoceList.addAll(cardList);
		devoceList.addAll(modemList);
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
		List<Map<String, Object>> userInfos = new ArrayList<Map<String,Object>>();
		
		//处理用户销户
		for (CUser user:userList){
			Map<String, Object> info = new HashMap<String, Object>();
			info.put("user_type", user.getUser_type_text());
			info.put("user", userMap.get(user.getUser_id()));
			
			//销户后保存原来的设备购买方式
			user.setStb_buy(map.get(user.getStb_id()) != null?map.get(user.getStb_id()).getBuy_mode():null);
			user.setCard_buy(map.get(user.getCard_id()) != null?map.get(user.getCard_id()).getBuy_mode():null);
			user.setModem_buy(map.get(user.getModem_mac()) != null?map.get(user.getModem_mac()).getBuy_mode():null);
			//终止用户的产品
			List<CProdDto> prodList = userProdComponent.queryByUserId(user.getUser_id());
			info.put("prods", prodList );
			for (CProd prod:prodList){
				terminateProd(custId,user, doneCode, busiCode, prod, banlanceDealType, transAcctId, transAcctItemId);
			}
			//终止用户对应的账户
			jobComponent.terminateAcct(jobId, user.getAcct_id(), null,doneCode);
			//更新用户对应的设备为空闲
			custComponent.updateDeviceStatusByCode(custId, user.getStb_id(), StatusConstants.IDLE);
			custComponent.updateDeviceStatusByCode(custId, user.getCard_id(), StatusConstants.IDLE);
			
			if(StringHelper.isNotEmpty(user.getModem_mac())){
				List<CUser> userDevice = userComponent.queryUserByDevice(DEVICE_TYPE_MODEM, user.getModem_mac());
				if(userDevice != null && userDevice.size() == 1){
					custComponent.updateDeviceStatusByCode(custId, user.getModem_mac(), StatusConstants.IDLE);
				}
			}
			
			//生成终止用户的业务指令
			delUserJob(user, custId, doneCode);
			//记录用户到历史表
			userComponent.removeUserWithHis(doneCode, user);
			userInfos.add(info);
		}
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("users", userInfos);
		doneCodeComponent.saveDoneCodeInfo(doneCode, custId, null, info);
		saveAllPublic(doneCode,getBusiParam());
	}


	public void saveResendCa() throws Exception {
		//获取客户用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		List<CUser> userList = getBusiParam().getSelectedUsers();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		for (CUser user:userList){
			//重发加授权指令
			List<CProdDto> prodList = userProdComponent.queryAllProdsByUserId(user.getUser_id());
			for (CProd prod:prodList){
				//正常状态的产品,重复指令
				if (isProdOpen(prod.getStatus())){
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD,
							custId, user.getUser_id(), user.getStb_id(),
							user.getCard_id(), user.getModem_mac(), prod.getProd_sn(),prod.getProd_id());
				}else{
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD,
							custId, user.getUser_id(), user.getStb_id(),
							user.getCard_id(), user.getModem_mac(), prod.getProd_sn(),prod.getProd_id());
				}
			}
		}

		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 重发开户指令
	 */
	public void saveResendUserCmd() throws Exception {
		//获取客户用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		List<CUser> userList = getBusiParam().getSelectedUsers();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		for (CUser user:userList){
			//宽带
			if(USER_TYPE_BAND.equals(user.getUser_type())){
				List<CProdDto> prodList = userProdComponent.queryByUserId(user.getUser_id());
				if(null != prodList && prodList.size() > 0){
					//宽带必须订购产品后，才能开户，且已发送开户后不再发送开户，必须先删除原来的开户指令
					jobComponent.deleteUserBand(user.getUser_id(),BusiCmdConstants.CREAT_USER);
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD,
							user.getCust_id(), user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), prodList.get(0).getProd_sn(),prodList.get(0).getProd_id());
				}
			}else{
				CUser userDto = queryUserById(user.getUser_id());
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.CREAT_USER, custId,
						user.getUser_id(), user.getStb_id(), user.getCard_id(), userDto.getModem_mac(), null, null,JsonHelper.fromObject(userDto));
			}
			
		}

		saveAllPublic(doneCode,getBusiParam());
	}
	
	
	public void saveRefreshCa(String refreshType) throws Exception {
		//获取客户用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		List<CUser> userList = getBusiParam().getSelectedUsers();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		for (CUser user:userList){
			if (REFRESH_TYPE_TERMINAL.equals(refreshType)){
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_TERMINAL,
						custId, user.getUser_id(), user.getStb_id(),
						user.getCard_id(), user.getModem_mac(),null,null);
			} else {
				//重发加授权指令
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.REFRESH_TERMINAL,
						custId, user.getUser_id(), user.getStb_id(),
						user.getCard_id(), user.getModem_mac(),null,null);
			}
				
		}

		saveAllPublic(doneCode,getBusiParam());
	}

	public void saveClearBind() throws Exception {
		//获取客户用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		CUser user = getBusiParam().getSelectedUsers().get(0);
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.BAND_CLEAR_BIND, custId, user.getUser_id(), null, null, user.getModem_mac(), null, null, null);
		saveAllPublic(doneCode,getBusiParam());
	}

	public void saveEditConnect(int maxConn) throws Exception {
		//获取客户用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		CUser user = getBusiParam().getSelectedUsers().get(0);
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.BAND_EDIT_CONNECT, custId, user.getUser_id(), null, null, user.getModem_mac(), null, null, "max_connect:''"+maxConn+"''");
		saveAllPublic(doneCode,getBusiParam());
		
	}

	/**
	 * 修改宽带密码
	 */
	public void saveEditPwd(String newPwd) throws Exception {
		//获取客户用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		CUser user = getBusiParam().getSelectedUsers().get(0);
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//生成计算用户信用度的JOB
		jobComponent.createCreditCalJob(doneCode, custId, null,BOOLEAN_TRUE);
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
		
		saveAllPublic(doneCode,getBusiParam());
		
	}

	public void saveOffLine() throws Exception {
		//获取客户用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		CUser user = getBusiParam().getSelectedUsers().get(0);
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.BAND_OFFLINE, custId, user.getUser_id(), null, null, user.getModem_mac(), null, null, null);
		saveAllPublic(doneCode,getBusiParam());
	}
	/**
	 * lxr:当促销资费和产品资费不一致时，且产品是包月产品时，原资费生效日期使用促销金额/促销资费 计算得到,非包月则取赠送时长
	 */
	private String addPromotion(Integer doneCode, int times, String promotionId,
			List<DisctFeeDto> feeList, List<PPromotionAcct> acctList,int changeDays,String changeCreateSn) throws Exception {
		//获取客户用户信息
		CUser user= getBusiParam().getSelectedUsers().get(0);
		CCust cust = getBusiParam().getCust();
		List<PProdUserRes> userResList = prodComponent.queryUserResByCountyId();
		if(acctList == null || acctList.size()==0){
			List<PPromotionAcct> promotionAcctList = promComponent.queryPromotionAcct(promotionId);
			if(promotionAcctList != null && promotionAcctList.size() > 0){
				throw new ServicesException("促销产品赠送数据异常，请重新选择");
			}
		}
		//判断更换促销是否跨月
		boolean monthchange=false;
		if(changeDays<0){
			String monthsone= DateHelper.getFirstDateInCurrentMonth();//当月1号
			CPromotionHis cph = userPromComponent.queryPromotionHis(changeCreateSn);
			if(cph == null){
				throw new ComponentException("数据异常,请联系管理员");
			}
			Date change_create_date=cph.getCreate_time();//原始促销日期
			if(change_create_date.before(DateHelper.strToDate(monthsone))){
				monthchange=true;
			}
		}
		
		//保存促销信息
		String promSn = userPromComponent.addPromotion(changeCreateSn,cust.getCust_id(), user.getUser_id(), user.getAcct_id(), promotionId, doneCode,times,acctList);
		//判断是否有费用优惠，如果有，修改费用优惠信息
		if (feeList != null && feeList.size()>0){
			for (DisctFeeDto fee:feeList){
				feeComponent.saveDisctFee(fee.getFee_sn(), FEE_DISCT_PROM, null, promSn, fee.getDisct_fee());
			}
		}
		
		for (PPromotionAcct acct:acctList){
			//如果配置的重复次数比允许的小，取配置重复次数
			int repeatTimes = times;
			if(acct.getRepetition_times()*10 < repeatTimes){
				repeatTimes = acct.getRepetition_times()*10 ;
			}
			
			if (PRESENT_TYPE_FEE.equals(acct.getPresent_type()))
				acct.setFee((int)(acct.getFee()*repeatTimes/10.0));
			else
				acct.setPresent_month(acct.getPresent_month()*repeatTimes);
		}
	
		//判断是否有账户优惠，如果有需要修改账户余额
		if (acctList != null){
			
			//公用账户
			CAcct acct = acctComponent.queryCustAcctByCustId(cust.getCust_id());
			
			for (PPromotionAcct promAcct: acctList ){
				String prodSn="";
				CAcctAcctitem acctItem = null;
				Date beginDate =  new Date();
				//判断促销账目是否公用账目，根据促销账目提取客户账户下的公用账目信息（acct.getAcct_id() 是 客户账目ID）
				acctItem = acctComponent.queryAcctItemByAcctitemId(acct.getAcct_id(),promAcct.getAcctitem_id());
				
				//是公用账目
				if(acctItem != null){
					//增加冻结余额
					CAcctAcctitemInactive inactiveItem =new CAcctAcctitemInactive();
					inactiveItem.setPromotion_sn(promSn);
					inactiveItem.setCust_id(cust.getCust_id());
					inactiveItem.setAcct_id(acctItem.getAcct_id());
					inactiveItem.setAcctitem_id(promAcct.getAcctitem_id());
					inactiveItem.setCycle(promAcct.getCycle());
					inactiveItem.setActive_amount(promAcct.getActive_amount());
					inactiveItem.setInit_amount(promAcct.getFee());
					inactiveItem.setBalance(promAcct.getFee());
					inactiveItem.setCounty_id(user.getCounty_id());
					inactiveItem.setArea_id(user.getArea_id());
					inactiveItem.setDone_code(doneCode);
					acctComponent.addAcctItemInactive(inactiveItem);
				}else{//不是公用账目

					PProdTariff newTariff = prodComponent.queryTariffById(promAcct.getTariff_id());
					//判断用户账目是否存在
					acctItem = acctComponent.queryAcctItemByAcctitemId(promAcct.getAcct_id(),promAcct.getAcctitem_id());
					boolean oldprodsign=true;
					if (acctItem == null){
						oldprodsign=false;
						//用户还没有订购此产品
						PProd prod = prodComponent.queryProdById(promAcct.getAcctitem_id());
						if (prod!=null){
							String stopByInvalidDate = prodComponent.stopByInvaliddate(prod, newTariff);
							if (newTariff == null)
								throw new ServicesException("资费"+promAcct.getTariff_id()+"不存在");
							
							CAcctBank acctBank = getBusiParam().getCustFullInfo().getAcctBank();
							String isBankPay = SystemConstants.BOOLEAN_FALSE;
							//有银行账户且产品可以使用银行扣款
							if(acctBank != null && StringHelper.isNotEmpty(acctBank.getBank_account())){
								if(prod.getIs_bank_pay().equals(SystemConstants.BOOLEAN_TRUE) 
										&& acctBank.getStatus().equalsIgnoreCase(StatusConstants.ACTIVE)){
									isBankPay = SystemConstants.BOOLEAN_TRUE;
								}
							}
							if (prod.getProd_type().equals(PROD_TYPE_BASE)){
								prodSn = userProdComponent.addProd(doneCode,cust.getCust_id(), user.getAcct_id(), user.getUser_id(),null,null,
									prod.getProd_id(), prod.getProd_type(), PROD_ORDER_TYPE_PRESENT,
									DateHelper.getDate("-"),null,user.getStop_type(), newTariff,  null,stopByInvalidDate,prod.getIs_base(),null,isBankPay);
							} else {
//								List<PPackageProd> childList = prodComponent.queryPackageProd(prod.getProd_id());
								List<PPackageProd> childList = prodComponent.queryPackageProd(prod.getProd_id(),newTariff.getTariff_id());
								prodSn = userProdComponent.addPackage(doneCode,cust.getCust_id(), user.getAcct_id(), user.getUser_id(), prod.getProd_id(), prod.getProd_type(), PROD_ORDER_TYPE_PRESENT
										,DateHelper.getDate("-"),null,  user.getStop_type(),newTariff, childList, null,stopByInvalidDate,prod.getIs_base(),null,isBankPay);
							}
							expressionUtil.setCcust(cust);
							expressionUtil.setCuser(user);
							for (PProdUserRes userRes:userResList){
								if (userRes.getProd_id().equals(prod.getProd_id())){
									if (StringHelper.isEmpty(userRes.getRule_id_text()) 
											|| expressionUtil.parseBoolean(userRes.getRule_id_text())){
										String[] res = userRes.getRes_id().split(",");
										for (String resId:res){
											userProdComponent.addUserProdres(prodSn, resId);
										}
									}
								}
							}
							jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, cust.getCust_id(),
									user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), prodSn,prod.getProd_id());
							//创建产品账目
							acctComponent.createAcctItem(user.getAcct_id(), prod.getProd_id());
							
							acctItem = acctComponent.queryAcctItemByAcctitemId(user.getAcct_id(),promAcct.getAcctitem_id());
						} 
					}
						
					CProd userProd = userProdComponent.queryByAcctItem(promAcct.getAcct_id(),promAcct.getAcctitem_id());
					beginDate = userProd.getInvalid_date();
					prodSn = userProd.getProd_sn();
					
					if (!userProd.getTariff_id().equals(promAcct.getTariff_id())){
						//用户已经订购了该产品情况下促销资费和已存在产品资费不一致，则换算成已存在产品资费促销
						//按实际资费换算赠送金额
						PProdTariff oldTariff = prodComponent.queryTariffById(userProd.getTariff_id());		
						//提取促销赠送的月数
						float present_months=0;
						if (PRESENT_TYPE_FEE.equals(promAcct.getPresent_type())){
							present_months=promAcct.getFee()*newTariff.getBilling_cycle()*1.0f/newTariff.getRent();
						}else{
							present_months=promAcct.getPresent_month()/10.0f;
						}
						if(oldTariff.getRent()==0){
							//0资费情况
							promAcct.setPresent_type(PRESENT_TYPE_TIME);
							promAcct.setPresent_month(Math.round(present_months*10));
						}else {
							//非0资费，按赠送月数换算赠送金额
							promAcct.setPresent_type(PRESENT_TYPE_FEE);
							promAcct.setFee( Math.round(present_months*oldTariff.getRent()*1.0F/oldTariff.getBilling_cycle()));
							promAcct.setPresent_month(Math.round(present_months*10));
							promAcct.setActive_amount(oldTariff.getRent());
							promAcct.setCycle(oldTariff.getBilling_cycle());
						}
						//把促销资费更好成实际资费
						newTariff=oldTariff;
					}else{
						promAcct.setPresent_month(Math.round(promAcct.getPresent_month()*10));
					}
											
					if (PRESENT_TYPE_FEE.equals(promAcct.getPresent_type())){
						if(promAcct.getFee() > 0){
							//增加冻结余额
							CAcctAcctitemInactive inactiveItem =new CAcctAcctitemInactive();
							inactiveItem.setPromotion_sn(promSn);
							inactiveItem.setCust_id(cust.getCust_id());
							inactiveItem.setAcct_id(promAcct.getAcct_id());
							inactiveItem.setAcctitem_id(promAcct.getAcctitem_id());
							inactiveItem.setCycle(promAcct.getCycle());
							if(newTariff.getBilling_cycle() == 1){
								inactiveItem.setActive_amount(promAcct.getActive_amount());
							}else if(newTariff.getBilling_cycle() > 1){	//包多月一次性激活完
								inactiveItem.setActive_amount(!oldprodsign&&changeDays<0&&newTariff.getBilling_cycle()==1? promAcct.getFee()+changeDays*newTariff.getRent()/30:promAcct.getFee());
							}
							
							inactiveItem.setInit_amount(!oldprodsign&&changeDays<0&&newTariff.getBilling_cycle()==1? promAcct.getFee()+changeDays*newTariff.getRent()/30:promAcct.getFee());
							inactiveItem.setBalance(!oldprodsign&&changeDays<0&&newTariff.getBilling_cycle()==1? promAcct.getFee()+changeDays*newTariff.getRent()/30:promAcct.getFee());
							inactiveItem.setCounty_id(user.getCounty_id());
							inactiveItem.setArea_id(user.getArea_id());
							inactiveItem.setDone_code(doneCode);
							if(monthchange){
								//跨月更换促销时，赠送金额一次性解冻
								inactiveItem.setActive_amount(inactiveItem.getInit_amount());
							}
							acctComponent.addAcctItemInactive(inactiveItem);
							//修改产品到期日
							if(newTariff.getBilling_cycle()==1){//包月情况
								userProdComponent.updateInvalidDate(doneCode, prodSn, 0,changeDays<0&&newTariff.getBilling_cycle()==1? promAcct.getFee()+changeDays*newTariff.getRent()/30:promAcct.getFee(), acctItem);
							}else{//包多月情况
								int allbill_fee=promAcct.getFee(), months = 0;
								if(promAcct.getPresent_month()==null||promAcct.getPresent_month()==0){
									promAcct.setPresent_month(Math.round(promAcct.getFee()*newTariff.getBilling_cycle()*10.0f/newTariff.getRent()));
									months = promAcct.getPresent_month()/10;
								}else{
									int m = promAcct.getPresent_month()/10;
									months = (int)(m*times/10.0);
								}
							
								int bill_rent=promAcct.getFee()/months;
																
								int first_bill_month=bill_rent+allbill_fee-bill_rent*months;
								int bill_month_index=0,bill_fee;
								while(allbill_fee>0){			
									bill_fee=(bill_month_index==0?first_bill_month:bill_rent);
									if(bill_fee>=allbill_fee){
										bill_fee=allbill_fee;
										allbill_fee=0;
									}else{
										allbill_fee=allbill_fee-bill_fee;
									}
									//按到期日来判断开始账单的账期
									String billingCycle = DateHelper.format(
											DateHelper.getNextMonthByNum(userProd.getInvalid_date().after(new Date())?userProd.getInvalid_date():new Date(),bill_month_index)
											, DateHelper.FORMAT_YM);
									billComponent.saveMuchMonthBill(userProd, doneCode, billingCycle, bill_fee, bill_fee);
									bill_month_index++;
								}
								//账户欠费金额修改和直接修改到期日
								acctComponent.changeAcctItemOwefee(false, userProd.getAcct_id(), userProd.getProd_id(), promAcct.getFee());	
								if (beginDate.before(new Date())){
									beginDate =DateHelper.today();
								}
								userProdComponent.updateInvalidDate(doneCode, prodSn, userProdComponent.getDate(beginDate,months,(promAcct.getPresent_month()%10) *3+changeDays));
							}
						}
					} else {
						if (beginDate.before(new Date()))
							beginDate = new Date();
						if (newTariff.getRent() ==0 && newTariff.getBilling_type().equals(BILLING_TYPE_MONTH) ){
							//新资费为零资费
							//更新产品到期日
							if (StringHelper.isNotEmpty(prodSn)){
								int m = promAcct.getPresent_month()/100;
								int d = (promAcct.getPresent_month()%10) *3;
								userProdComponent.updateInvalidDate(doneCode, prodSn, userProdComponent.getDate(beginDate,m,d+changeDays ));
							}
						} else {
							
						}
						//创建销帐任务
						jobComponent.createCustWriteOffJob(doneCode, cust.getCust_id(),BOOLEAN_TRUE);
					}
				}
				
				
			}
			
		}
		PromotionDto basic = promComponent.queryPromotionSimpleInfoByKey(promotionId);
		PromotionDto param = queryPromInfoById(cust.getCust_id(), user.getUser_id(), promotionId);
		param.setTheme_name(basic.getTheme_name());
		String promotion_desc = basic.getPromotion_desc();
		if(StringHelper.isEmpty(promotion_desc)){
			promotion_desc = "";
		}
		promotion_desc = promotion_desc.replaceAll("\r", "").replaceAll("\n", "</br>").replaceAll("\"", "");
		param.setPromotion_desc(promotion_desc);
		getBusiParam().setBusiConfirmParam("promotion", param);
		

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("busiName", MemoryDict.getDictName(DictKey.BUSI_CODE, getBusiParam().getBusiCode()));
		map.put("promotion_desc", promotion_desc);
		map.put("promotion_name", basic.getPromotion_name());
		map.put("user", user);
		doneCodeComponent.saveDoneCodeInfo(doneCode, cust.getCust_id(),user.getUser_id(), map);
		return promSn;
		
	}
	
	public void savePromotion(int times,String promotionId, List<DisctFeeDto> feeList,List<PPromotionAcct> acctList)
			throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		addPromotion(doneCode, times, promotionId, feeList, acctList,0,null);
		String custId = getBusiParam().getCust().getCust_id();
		
		//生成账务模式判断
		jobComponent.createAcctModeCalJob(doneCode, custId);
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void saveChangePromotion(int times, String promotionSn,
			String promotionId, List<PPromotionAcct> newAcctList) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		//先前促销产品已赠送一部分冻结金额，现变更促销后，加上这部分赠送冻结金额
		CPromotion promotion = userPromComponent.queryBySn(promotionSn);

		//原始促销日期
		Date change_create_date=promotion.getCreate_time();
		if(!promotion.getPromotion_sn().equals(promotion.getPromotion_create_sn())
				&&StringHelper.isNotEmpty(promotion.getPromotion_create_sn())){
			change_create_date=userPromComponent.queryPromotionHis(promotion.getPromotion_create_sn()).getCreate_time();
		}
		//提取变更天数
		int changeDays=DateHelper.getDiffDays(DateHelper.today(),DateHelper.strToDate( DateHelper.dateToStr(change_create_date)));
		//取消促销
		cancelPromotion(doneCode, BusiCodeConstants.CHANGE_PROMOTION, promotionSn);
		//新增促销
		String newPromotionSn = addPromotion(doneCode, times, promotionId, null, newAcctList,changeDays,promotion.getPromotion_create_sn());
		
		//将原冻结已赠送金额返还
		String custId = getBusiParam().getCust().getCust_id();
		
		jobComponent.createCustWriteOffJob(doneCode, custId, BOOLEAN_TRUE);
		jobComponent.createAcctModeCalJob(doneCode, custId);
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		
		saveAllPublic(doneCode,getBusiParam());
	}
	/**
	 * lxr:当产品是促销订购时，回退删除日租、作废实时账单、删除资费变更job；促销回退时，恢复原始资费
	 */
	private void cancelPromotion(Integer doneCode,String busiCode,String promotionSn) throws Exception {
		//查找促销账户信息
		CPromotion promotion = userPromComponent.queryBySn(promotionSn);
		List<CPromotionAcct> acctPromList = userPromComponent.queryAcctBySn(promotionSn);
		List<CAcctAcctitemInactive> inactiveList = acctComponent.queryInactiveByPromSn(promotionSn);
		CUser user = userComponent.queryUserById(promotion.getUser_id());
		//遍历账户，判断产品是否为新订购产品，如果是，则直接删除相信的信息，如果不是则恢复产品信息到促销前状态
		CAcctAcctitemInactive acctItemInactive = new  CAcctAcctitemInactive();
		for (CPromotionAcct promAcct :acctPromList){
			//查找账目对应的产品
			CProd prod = userProdComponent.queryByAcctItem(promAcct.getAcct_id(), promAcct.getAcctitem_id());
			CAcctAcctitem acctItem= acctComponent.queryAcctItemByAcctitemId(promAcct.getAcct_id(), promAcct.getAcctitem_id());
			for (CAcctAcctitemInactive inactive:inactiveList){
				if (inactive.getAcctitem_id().equals(promAcct.getAcctitem_id())){
					acctItemInactive = inactive;
					break;
				}
			}
			if (prod !=null){
				//原来的判断，当天的促销，账目金额都是解冻金额，且订购方式为赠送
//				if (DateHelper.dateToStr(prod.getOrder_date()).equals(DateHelper.formatNow()) && 
//						acctItem.getActive_balance().compareTo(acctItemInactive.getUse_amount())==0 && 
//						PROD_ORDER_TYPE_PRESENT.equals(prod.getOrder_type())){
				//现改成:促销订购的产品，且订购方式仍为赠送（缴费后会变成订购）
				if (promotion.getDone_code().equals(prod.getDone_code()) && PROD_ORDER_TYPE_PRESENT.equals(prod.getOrder_type())){
					//当天新订购产品,且帐目余额全部为解冻资金，直接删除产品以及账目
					//生成钝化产品的指令
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD, user.getCust_id(),
							user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(),
							prod.getProd_sn(),prod.getProd_id());
					//删除产品
					userProdComponent.removeProdWithHis(doneCode, prod);
					//删除产品对应的账目
					acctComponent.removeAcctItemWithoutHis(user.getCust_id(),user.getAcct_id(),prod.getProd_id(), doneCode,this.getBusiParam().getBusiCode());
					//删除实时账单和日租
					billComponent.cancelBill(prod.getProd_sn(),DateHelper.nowYearMonth());
					billComponent.deleteRentfee(prod.getProd_sn(),prod.getCounty_id());
					//删除资费job
					jobComponent.deleteNewProdTariffJob(prod.getProd_sn());
					
				} else {
					PProdTariff Tariff = prodComponent.queryTariffById(prod.getTariff_id());
					if (Tariff!=null&&Tariff.getRent()>0&&promAcct.getFee()>0){
						int initAmount = 0;		//冻结初始金额
						if(acctItemInactive != null){
							//参加促销的产品退订了再订购，然后回退促销时，账目冻结明细不存在了
							acctItemInactive = acctComponent.queryByPromotionSn(
									promotionSn,acctItemInactive.getAcct_id(),acctItemInactive.getAcctitem_id());
							
							if(acctItemInactive != null){
								initAmount = acctItemInactive.getInit_amount();
								if (acctItemInactive.getUse_amount()>0){
									//已经有返回
									acctComponent.changeAcctItemBanlance(doneCode, busiCode, promotion.getCust_id(),
											promAcct.getAcct_id(), promAcct.getAcctitem_id(),
											ACCT_CHANGE_PROMOTION_CANCEL, ACCT_FEETYPE_PRESENT, acctItemInactive.getUse_amount()*-1, null);
								}
								acctComponent.removeInactiveWithHis(acctItemInactive, doneCode);
							}
						}
						//修改用户产品的到期日
						if(Tariff.getBilling_cycle()>1){
							//包多月情况处理:删除账单，修改到期日
							List<BBill> muchbills=billComponent.queryMuchMonthProdBill(prod.getProd_sn(),promotion.getDone_code()
									, DateHelper.format(new Date(),	DateHelper.FORMAT_YM), BILL_COME_FROM_MUCH);
							if(muchbills!=null&&muchbills.size()>0){
								int owefee=0;
								int billfee=0;
								for(BBill bill:muchbills){
									owefee=owefee+bill.getOwe_fee();
									billfee=billfee+bill.getFinal_bill_fee();
									billComponent.cancelBill(bill.getBill_sn());
								}
								acctComponent.changeAcctItemOwefee(false, prod.getAcct_id(), prod.getProd_id(), owefee*-1);
							}
							int months=Math.round(initAmount*Tariff.getBilling_cycle()*10.0F/Tariff.getRent());
							int m = months/10;
							int d = (months%10) *3;
							userProdComponent.updateInvalidDate(doneCode,  prod.getProd_sn(), userProdComponent.getDate(prod.getInvalid_date(),m*-1,d*-1));
						}else{
							userProdComponent.updateInvalidDate(doneCode,  prod.getProd_sn(),0, initAmount*-1, acctItem);
						}
					} else {
						if(promAcct.getMonths() != null)
							userProdComponent.updateInvalidDate(doneCode, prod.getProd_sn(), userProdComponent.getDate(prod.getInvalid_date(), promAcct.getMonths()*-1, 0));
					}
				}
			}else{
				//公用账目
				if (promAcct.getAcctitem_id().equals(acctItemInactive.getAcctitem_id()) && promAcct.getFee()>0){
					if (acctItemInactive.getUse_amount()>0){
						//已经有返回
						acctComponent.changeAcctItemBanlance(doneCode, busiCode, promotion.getCust_id(),
								acctItemInactive.getAcct_id(), acctItemInactive.getAcctitem_id(),
								ACCT_CHANGE_PROMOTION_CANCEL, ACCT_FEETYPE_PRESENT, acctItemInactive.getUse_amount()*-1, null);
					}
					acctComponent.removeInactiveWithHis(acctItemInactive, doneCode);
				}
			}
		}
		
		//删除促销
		userPromComponent.removeBySn(doneCode,promotionSn);
		//调用返销帐接口
		jobComponent.createCustWriteOffJob(doneCode, promotion.getCust_id(), BOOLEAN_FALSE);
		//生成销账任务
		jobComponent.createCustWriteOffJob(doneCode, promotion.getCust_id(), BOOLEAN_TRUE);
	}
	
	public void saveCancelPromotion(String promotionSn) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		cancelPromotion(doneCode, busiCode, promotionSn);
		saveDoneCode(doneCode, busiCode, getBusiParam().getCust().getCust_id());
		
		String custId = getBusiParam().getCust().getCust_id();
		//生成计算账目模式判断任务
		jobComponent.createAcctModeCalJob(doneCode, custId);
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
	}
	
	//模拟剪线
	public void saveAtvCustLine() throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取操作的客户、用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		CUserAtv user = (CUserAtv) getBusiParam().getSelectedUsers().get(0);
		CUser userDto = queryUserById(user.getUser_id());
		
		String userId = user.getUser_id();
		
		//修改用户状态
		updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.CUSTLINE);
		
		jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_USER,
				custId, user.getUser_id(), null, null, null, null, null, JsonHelper.fromObject(userDto));
		
		
		List<CProdDto> prodList = userProdComponent.queryByUserId(userId);
		for(CProdDto prod : prodList){
			List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
			changeList.add(new CProdPropChange("status",
					prod.getStatus(),StatusConstants.CUSTLINE));
			changeList.add(new CProdPropChange("status_date",
					DateHelper.dateToStr(prod.getStatus_date()),DateHelper.dateToStr(new Date())));
			
			userProdComponent.editProd(doneCode,prod.getProd_sn(),changeList);
			
			//生成钝化产品任务
			if (isProdOpen(prod.getStatus())){
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD, custId,
					user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), prod.getProd_sn(),prod.getProd_id());
			}
		}
		
//		String busiInfo = "终端类型："+user.getUser_type_text()+" 设备号:"+user.getStb_id();
		getBusiParam().setBusiConfirmParam("user", user);
		
//		saveAllPublic(doneCode,getBusiParam(),busiInfo);
		saveAllPublic(doneCode,getBusiParam());
	}

	//模拟剪线 恢复
	public void saveAtvActive() throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取操作的客户、用户信息
		String  custId = getBusiParam().getCust().getCust_id();
		CUserAtv user = (CUserAtv) getBusiParam().getSelectedUsers().get(0);
		String userId = user.getUser_id();
		
		//修改用户状态
		updateUserStatus(doneCode, userId, user.getStatus(), StatusConstants.ACTIVE);
		
		List<CProdDto> prodList = userProdComponent.queryByUserId(userId);
		for (CProd prod:prodList){
			String oldStatus = userProdComponent.queryLastStatus(prod.getProd_sn());
			if (StringHelper.isEmpty(oldStatus)) {
				if (prod.getInvalid_date().after(prod.getStatus_date()))
					oldStatus = StatusConstants.ACTIVE;
				else
					oldStatus = StatusConstants.OWESTOP;
			}else if(oldStatus.equals(StatusConstants.REQSTOP)){
				oldStatus = StatusConstants.ACTIVE;
			}/*else if(oldStatus.equals(StatusConstants.CUSTLINE)){
				oldStatus = StatusConstants.ACTIVE;
			}*/
			userProdComponent.updateProdStatus(doneCode,prod.getProd_sn(),prod.getStatus(), oldStatus);
			//生成激活产品任务
			if (isProdOpen(oldStatus)){
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, custId,
					userId, null, null, null, prod.getProd_sn(),prod.getProd_id());
			}
		
			//报开后更新到期日
			userProdComponent.updateInvalidDate(doneCode, prod.getProd_sn(), DateHelper.getDiffDays(prod.getStatus_date(), new Date()), 0, new CAcctAcctitem());
			
		}
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		//生成信用计算、修改用户信息、激活设备任务
		jobComponent.createCreditCalJob(doneCode, custId, null,BOOLEAN_TRUE);
		//更新用户产品的出帐日期
		//next_bill_date已作废
//		userProdComponent.updateNextBillDate(user.getUser_id(), DateHelper.dateToStr(user.getStatus_date()));
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/*
	 * 查询用户可以选择的促销
	 */
	public List<PromotionDto> querySelectableProm() throws Exception {
		String userId = getBusiParam().getSelectedUserIds().get(0);
		String custId = getBusiParam().getCust().getCust_id();
		List<PromotionDto> promotions = promComponent.queryManualPromotion(userId);
		List<CFee> feeList = feeComponent.queryUserFee(custId,userId);
		List<AcctitemDto> balanceList = acctComponent.queryAcctAndAcctItemByUserId(custId,userId);
		List<CProdDto> prodList = userProdComponent.queryByUserId(userId);

		expressionUtil.setAllValue(getBusiParam(), feeList, balanceList,prodList);
		expressionUtil.setCuserStb(userComponent.queryUserStbByUserId(userId));
		for (int i = promotions.size() - 1; i >= 0; i--) {
			PromotionDto promotionDto = promotions.get(i);
			Integer times = expressionUtil.parsePromotion(promotionDto
					.getRule_str(), promotionDto.getRepetition_times());
			debug(getClass(), "手动促销"+promotionDto.getPromotion_name()+promotionDto
					.getRule_str()+"结果="+times);
			if (times ==0) {
				// 一次促销都不满足
				promotions.remove(i);
			}
			promotionDto.setRepetition_times(times);
		}
		return promotions;
	}

	/**
	 * 根据促销ID查询促销详细信息
	 *
	 * @param promotionId
	 *            促销ID
	 * @return
	 * @throws Exception
	 */
	public PromotionDto queryPromInfoById(String custId,String userId,String promotionId) throws Exception{
		PromotionDto promotionDto = promComponent.queryById(promotionId);
		List<PPromotionAcct> acctItemList = promotionDto.getAcctList();
		for(PPromotionAcct acctItem : acctItemList){
			if(StringHelper.isEmpty(acctItem.getTariff_id())){
				AcctitemDto acctitemDto = null;
				List<AcctitemDto> acctitemDtolist = acctComponent.queryAcctAndAcctItemByUserId(custId, userId);
				for (AcctitemDto dto : acctitemDtolist) {
					if(dto.getAcctitem_id().equals(acctItem.getAcctitem_id())){
						acctitemDto = dto;
					}
				}
				if(null == acctitemDto){
					throw new ServicesException("参加该促销，需先订购产品"+acctItem.getAcctitem_name());
				}
			}
			
			List<ProdResDto> resList = prodComponent.queryProdRes(acctItem.getAcctitem_id());
			if(resList != null && resList.size() > 0){
				for(ProdResDto prod : resList){
					List<ResGroupDto> rgdList = prod.getDynamicResList();
					CProd cProd = userProdComponent.queryByProdId(userId, acctItem.getAcctitem_id());
					if(rgdList.size() > 0 && cProd == null){
						throw new ServicesException("参加该促销，需先订购产品"+prod.getProd_name());
					}
				}
			}
		}
		promotionDto.setUserFeeList(feeComponent.queryPromotionFee(custId, userId, promotionId));
		return promotionDto;
	}

	private void updateDevice(Integer doneCode,String deviceCode, String CustId,String status)
		throws JDBCException, Exception {
		if (StringHelper.isNotEmpty(deviceCode)){
			DeviceDto device = deviceComponent.queryDeviceByDeviceCode(deviceCode);
			custComponent.transDevice(doneCode,CustId, device);
			custComponent.updateDeviceStatus(CustId, device.getDevice_id(), status);
		}
	}

	/**
	 * 批量临时授权.
	 * @throws Exception
	 */
	public void saveOpenTempBatch() throws Exception {
		try{
			Integer doneCode = doneCodeComponent.gDoneCode();
			String  custId = getBusiParam().getCust().getCust_id();
			List<CUser> users = getBusiParam().getSelectedUsers();
			for(CUser user:users){
				saveOpenTempSingle(doneCode,custId,user);
			}
			saveAllPublic(doneCode,getBusiParam());
		}catch (Exception e) {
			throw new ServicesException(e);
		}
	}
	
	public void saveOpenTemp() throws Exception {
		try{
			Integer doneCode = doneCodeComponent.gDoneCode();
			String  custId = getBusiParam().getCust().getCust_id();
			CUser user= getBusiParam().getSelectedUsers().get(0);
			saveOpenTempSingle(doneCode,custId,user);
			saveAllPublic(doneCode,getBusiParam());
		}catch (Exception e) {
			throw new ServicesException(e);
		}
	}
	/**
	 * 用户临时授权
	 * 20140416 by wqy 0001234: 临时授权BUG；月包：改阈值，通过信控改状态为临时开通；年包：根据配置天数，延长到期日，并前台改状态为临时开通,发加授权 
	 * @param userId
	 * @throws Exception
	 */
	private void saveOpenTempSingle(Integer doneCode,String  custId,CUser user) throws Exception {
		//获取业务流水
		//String busiCode = getBusiParam().getBusiCode();
		//获取临时授权的配置信息
		TOpenTemp openTempCfg = busiConfigComponent.queryOpenTempCfg(user.getUser_type());
		if (openTempCfg == null)
			throw new ServicesException("该地区没有配置 "+ user.getUser_type_text() +" 临时授权");
		//获取用户最近一个周期内的临时授权次数，判断是否可以临时授权
		int openTempTimes = doneCodeComponent.queryOpenTempTimes(user.getUser_id(), openTempCfg.getCycle());
		if (openTempTimes >= openTempCfg.getTimes())
			throw new ServicesException("该用户在"+openTempCfg.getCycle()+"个月内已经有过"+openTempTimes+"次临时授权,不能再做临时授权");
		//查找用户基本包
		List<CProdDto> prodList = userProdComponent.queryUserDtvBaseProd(user.getUser_id());
		if (prodList == null || prodList.size() == 0)
			throw new ServicesException("该用户没有 "+user.getUser_type_text()+" 基本包，不能临时授权");
		
		List<UserDto> allUsersOfCust = userComponent.queryUser(custId);//当前客户所有的用户
		UserDto zzd = null;
		for (UserDto userDto : allUsersOfCust) {//allUsersOfCust 如果为空就进不来这里了...
			if(userDto.getTerminal_type().equals(SystemConstants.USER_TERMINAL_TYPE_ZZD)){
				zzd = userDto;
				break;
			}
		}
		
		CProdDto prod = new CProdDto();
		Date date = null; 
		//busicode : 1033  从机临时开通时须判断主机状态(主机符合临时开通的状态)。
		//检查主终端的条件
		if(zzd != null && !zzd.getUser_id().equals(user.getUser_id()) ){
			prod = new CProdDto();
			List<CProdDto> zzdBaseProdList = userProdComponent.queryUserDtvBaseProd(zzd.getUser_id());
			if(CollectionHelper.isEmpty(zzdBaseProdList)){
				throw new ServicesException("该客户没有 "+user.getUser_type_text()+" 基本包，不能临时授权");
			}
			for(CProdDto dto:zzdBaseProdList){
				if(!dto.getStatus().equals(StatusConstants.OWESTOP)){
					throw new ServicesException("该客户主终端基本包【"+dto.getProd_name()+"】的状态不是欠费停，不能临时授权");
				}
			}
		}
		
		for(CProdDto dto:prodList){
			if(!dto.getStatus().equals(StatusConstants.OWESTOP)){
				throw new ServicesException("该用户基本包【"+dto.getProd_name()+"】的状态不是欠费停，不能临时授权");
			}
			Date statusDate = DateHelper.strToDate(DateHelper.dateToStr(dto.getStatus_date()));
			if(date == null || statusDate.compareTo(date)>0){
				date = statusDate;
				prod = dto;
			}else if(statusDate.compareTo(date)==0 && dto.getBilling_cycle()==1){
				//同一天的优先月包
				date = statusDate;
				prod = dto;
			}
		}
		
		//获取产品的资费信息
		PProdTariff tariff = userProdComponent.queryProdTariffById(prod.getTariff_id());
		//包多月或零资费基本包，修改到期日，发送加授权，改状态为临时开通
		if(prod.getStop_by_invalid_date().equals(BOOLEAN_TRUE)){
			
			if(prod.getBilling_cycle()>1 || tariff.getRent() == 0){
				Date invalidDate = DateHelper.addDate(new Date(),openTempCfg.getDays());
				//修改到期日,并修改产品状态为正常
				userProdComponent.updateInvalidDateStatus(doneCode, prod.getProd_sn(), invalidDate,BusiCodeConstants.USER_OPEN_TEMP);
				jobComponent.createCreditExecJob(doneCode, user.getCust_id());
			}else{
				//包月产品临时授权按阈值处理，修改停机类型为账务模式
				List<CProdPropChange> propChangeList = new ArrayList<CProdPropChange>();
				CProdPropChange prop = new CProdPropChange();
				prop.setProd_sn(prod.getProd_sn());
				prop.setDone_code(doneCode);
				prop.setColumn_name("stop_by_invalid_date");
				prop.setOld_value(BOOLEAN_TRUE);
				prop.setNew_value(BOOLEAN_FALSE);
				prop.setArea_id(prod.getArea_id());
				prop.setCounty_id(prod.getCounty_id());
				propChangeList.add(prop);
				userProdComponent.editProd(doneCode, prod.getProd_sn(), propChangeList);
				updateThread(doneCode, custId, prod.getAcct_id(), prod.getProd_id(), tariff.getRent(), openTempCfg.getDays());
			}
		}else{
			updateThread(doneCode, custId, prod.getAcct_id(), prod.getProd_id(), tariff.getRent(), openTempCfg.getDays());
		}
	}
	
	private void updateThread(Integer doneCode, String custId, String acctId, String acctItemdId, int rent, int days) throws Exception {
		//查找产品对应的账目信息
		CAcctAcctitem acctitem = acctComponent.queryAcctItemByAcctitemId(acctId, acctItemdId);
		int ownFee =  acctitem.getOwe_fee() + acctitem.getReal_fee() - acctitem.getActive_balance() - acctitem.getOrder_balance();
		int thresholdFee =rent/30*days;
		int tempThreshold = ownFee>0?ownFee+thresholdFee:thresholdFee;
		//更新账目的临时阈值
		acctComponent.updateTempThreshold(acctitem.getAcct_id(),acctitem.getAcctitem_id(),tempThreshold);
		//生成销帐任务
		jobComponent.createCustWriteOffJob(doneCode, custId,BOOLEAN_TRUE);
	}

	private void saveTjFee(int doneCode,String busiCode,String custId,int tjFee) throws Exception{
		if (tjFee>0){
			//查找客户公用账户
			CAcct acct = acctComponent.queryCustAcctByCustId(custId);
			//保存缴费记录
			PayDto pay = new PayDto();
			pay.setAcct_id(acct.getAcct_id());
			pay.setAcctitem_id(ACCTITEM_TJ);
			pay.setFee(tjFee);
			this.saveAcctPay(doneCode, pay);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IUserService#queryValidRes(java.lang.String)
	 */
	public List<UserRes> queryValidRes(String userId) throws Exception {
		return queryValidRes(userId.split(","));
	}
	
	/**
	 * 保存用户排斥的资源
	 * @param userId
	 * @param custId
	 * @param resIds
	 * @throws Exception
	 */
	public void saveRejectRes(String userId,String custId,String resIds) throws Exception{
		CUser user= getBusiParam().getSelectedUsers().get(0);
		//查找原有的排斥资源
		List<PRes> oldRejRes= userProdComponent.queryRejectRes(user.getUser_id(), user.getCust_id());
		String[] resIdsArr = null;
		Integer doneCode = doneCodeComponent.gDoneCode();
		userComponent.saveRejectRes(userId, custId, resIds);
		if(StringHelper.isNotEmpty(resIds)){
			resIdsArr = resIds.split(",");
		}
		//生成排斥资源的任务
		String addRejRes="";
		String delRejRes="";
		for (PRes res:oldRejRes){
			boolean exits = false;
			if(resIdsArr!=null){
				for (String resId:resIdsArr){
					if (res.getRes_id().equals(resId)){
						exits = true;
						break;
					}
				}
			}
			if (!exits)
				delRejRes +=","+res.getRes_id();
		}
		if(resIdsArr!=null){
			for (String resId:resIdsArr){
				boolean exits = false;
				for (PRes res:oldRejRes){
					if (res.getRes_id().equals(resId)){
						exits = true;
						break;
					}
				}
				
				if (!exits)
					addRejRes +=","+resId;
			}
		}
		if (StringHelper.isNotEmpty(addRejRes)){
			addRejRes = addRejRes.substring(1);
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ADD_REJECT_RES, 
					user.getCust_id(), user.getUser_id(), user.getStb_id(), user.getCard_id(),
					user.getModem_mac(),null, null,"RES_ID:''"+addRejRes+"''");
		}
		
		if (StringHelper.isNotEmpty(delRejRes)){
			delRejRes = delRejRes.substring(1);
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.DEL_REJECT_RES, 
					user.getCust_id(), user.getUser_id(), user.getStb_id(), user.getCard_id(),
					user.getModem_mac(),null, null,"RES_ID:''"+delRejRes+"''");
		}
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 部门下的是所有操作员
	 * @param deptId
	 * @return
	 * @throws JDBCException
	 */
	public List<SOptr> getByDeptId(String deptId) throws JDBCException{
		return userComponent.getByDeptId(deptId);
	}
	
	public TBusiFee queryZlFeeById() throws Exception {
		return busiConfigComponent.queryZlFeeById(); 
	}
	
	/**
	 * 保存租赁费用
	 * @param feeId
	 * @param amount
	 * @return
	 * @throws Exception
	 */

	public void saveLeaseFee(String feeId, String amount) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		List<FeeBusiFormDto> feeslist = new ArrayList<FeeBusiFormDto>();
		FeeBusiFormDto fees = new FeeBusiFormDto();
		fees.setFee_id(feeId);
		fees.setReal_pay(Integer.parseInt(amount));
		fees.setCount(0);
		feeslist.add(fees);
		getBusiParam().setFees(feeslist);
		
		saveAllPublic(doneCode,getBusiParam());
		
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IUserService#checkLoginName(java.lang.String)
	 */
	public void checkLoginName(String loginName) throws Exception {
		if(StringHelper.isNotEmpty(loginName)){
			if(getOptr().getCounty_id().equals(COUNTY_9005)){
				Pattern p = Pattern.compile("^gd[0-9]{4,5}$");
				Matcher matcher = p.matcher(loginName);
				if(!matcher.find()){
					throw new ServicesException("潜江宽带登录账号以gd开头,后面接4或5位数字");
				}
			}
			CUser user = userComponent.queryUserByLoginName(loginName);
			if(null != user){
				throw new ServicesException("宽带登录账号不能重复");
			}
		}
	}
	
	//充值卡充值
	@Deprecated
	public void saveRechargeCard(String icCard,String rechargeCard) throws Exception {
//		java.net.URL endpointURL = new java.net.URL(RECHARFE_CARD_URL);
//		javax.xml.rpc.Service service = new org.apache.axis.client.Service();
//		RechargeServiceHttpBindingStub drcom = new RechargeServiceHttpBindingStub(endpointURL, service);
//		
//		RechargeReq req = new RechargeReq();
//		req.setUserName(CARD_USER_NAME);
//		req.setPassword(CARD_RECHARGE_CARD);
//		req.setIcCard(icCard);
//		req.setRechargeCard(rechargeCard);
		
//		return drcom.recharge(req);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IUserService#saveToSingleCard(java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	public void saveToSingleCard(String newCardId, String str4, String str5,
			boolean reclaim, String deviceStatus) throws Exception {
		/*
		Integer doneCode = doneCodeComponent.gDoneCode();
		CCust cust = getBusiParam().getCust();
		CUser user = getBusiParam().getSelectedUsers().get(0);
		String busiCode = getBusiParam().getBusiCode();
		
		//取消原来的双向,变成单向
		CUser userDto = queryUserById(user.getUser_id());
		if(DTV_SERV_TYPE_DOUBLE.equals(userDto.getServ_type())){
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.CANCEL_INTERACTIVE, user.getCust_id(),
					user.getUser_id(), user.getStb_id(), user.getCard_id(), "", null,null,JsonHelper.fromObject(userDto));

			String modemMac = user.getModem_mac();
			CCustDevice custDevice = custComponent.queryCustDeviceByCodeAndCustId(user.getCust_id(), modemMac);
			if(custDevice != null){	//单modem
				CUserBroadband band = userComponent.queryBandByDeviceId(modemMac);
				//单modem 双向、宽带共用一个modem
				if(band == null){
					custComponent.updateDeviceStatusByCode(user.getCust_id(), modemMac, StatusConstants.IDLE);
				}
			}
		}
		
		//更换设备
		changeStbCard(true,user.getStb_id(), user.getCard_id(), null, null, newCardId, null,
				 user.getCust_id(), doneCode,busiCode);
		
		//回收设备
		if(reclaim){
			DeviceDto device = null;
			//回收机顶盒
			device = deviceComponent.queryDeviceByDeviceCode(user.getStb_id());
			reclaimDevice(device.getDevice_id(), deviceStatus,SystemConstants.RECLAIM_REASON_XHTH, 0, cust, doneCode, busiCode);
			
			//回收卡
			if(!newCardId.equals(user.getCard_id())){
				device = deviceComponent.queryDeviceByDeviceCode(user.getCard_id());
				reclaimDevice(device.getDevice_id(), deviceStatus,SystemConstants.RECLAIM_REASON_XHTH, 0, cust, doneCode, busiCode);
			}
			
		}
		
		
		//修改用户属性
		List<CUserPropChange> propChangeList = new ArrayList<CUserPropChange>();
		if(StringHelper.isNotEmpty(str4) && !str4.equals(user.getStr4())){
			CUserPropChange change = new CUserPropChange();
			change.setUser_id(user.getUser_id());
			change.setColumn_name("str4");
			change.setOld_value(user.getStr4());
			change.setNew_value(str4);
			propChangeList.add(change);
		}
		
		if(StringHelper.isNotEmpty(str5) && !str5.equals(user.getStr5())){
			CUserPropChange change = new CUserPropChange();
			change.setUser_id(user.getUser_id());
			change.setColumn_name("str5");
			change.setOld_value(user.getStr5());
			change.setNew_value(str5);
			propChangeList.add(change);
		}
		
		//取消双向
		if(DTV_SERV_TYPE_DOUBLE.equals(userDto.getServ_type())){
			if (StringHelper.isNotEmpty(user.getNet_type())){
				CUserPropChange change = new CUserPropChange();
				change.setUser_id(user.getUser_id());
				change.setColumn_name("net_type");
				change.setOld_value(user.getNet_type());
				change.setNew_value("");
				propChangeList.add(change);
			}
			//双向用户类型
			if (StringHelper.isNotEmpty(user.getStr11())){
				CUserPropChange change = new CUserPropChange();
				change.setUser_id(user.getUser_id());
				change.setColumn_name("str11");
				change.setOld_value(user.getStr11());
				change.setNew_value("");
				propChangeList.add(change);
			}
			if (StringHelper.isNotEmpty(userDto.getPassword())){
				CUserPropChange change = new CUserPropChange();
				change.setUser_id(user.getUser_id());
				change.setColumn_name("password");
				change.setOld_value(userDto.getPassword());
				change.setNew_value("");
				propChangeList.add(change);
			}
			if (userDto.getServ_type().equals(DTV_SERV_TYPE_DOUBLE)){
				CUserPropChange change = new CUserPropChange();
				change.setUser_id(user.getUser_id());
				change.setColumn_name("serv_type");
				change.setOld_value(userDto.getServ_type());
				change.setNew_value(DTV_SERV_TYPE_SINGLE);
				propChangeList.add(change);
			}
		}
		
		userComponent.editUser(doneCode, user.getUser_id(), propChangeList);
		
		saveAllPublic(doneCode,getBusiParam());
		*/
	}
	
	public void updateUserStatus(List<String> userIds,String userStatus) throws Exception {
		List<CUser> userList = userComponent.queryUserByUserIds(userIds);
		if(userList == null || userList.size()==0)
			throw new Exception("未查询到用户，请确定用户ID是否正确");
		List<CUser> users = new ArrayList<CUser>();
//		if(userStatus.equals(StatusConstants.ATVCLOSE)){//关模隔离 只针对模拟用户
//			for(CUser user : userList){
//				if(user.getUser_type().equals(USER_TYPE_ATV)){
//					users.add(user);
//				}
//			}
//			if(users.size() == 0){
//				throw new Exception("关模隔离只针对模拟用户，请重新确认用户ID");
//			}
//		}else{
		users = userList;
//		}
		BusiParameter bp = getBusiParam();
		List<CUserPropChange> upcList = new ArrayList<CUserPropChange>();
		List<CDoneCode> dcList = new ArrayList<CDoneCode>();
		List<CDoneCodeDetail> dcdList = new ArrayList<CDoneCodeDetail>();
		for(CUser user : users){
			String status = user.getStatus();
			if(StatusConstants.REQSTOP.equals(status)){
				throw new ServicesException("报停用户不能操作");
			}
			if(!status.equals(StatusConstants.DORMANCY) && !status.equals(StatusConstants.ATVCLOSE)){
				Integer doneCode = doneCodeComponent.gDoneCode();
				String countyId = user.getCounty_id();
				String areaId = user.getArea_id();
				String userId = user.getUser_id();
				
				CUserPropChange upc = new CUserPropChange();
				upc.setUser_id(userId);
				upc.setColumn_name("status");
				upc.setOld_value(user.getStatus());
				upc.setNew_value(userStatus);
				upc.setCounty_id(countyId);
				upc.setArea_id(areaId);
				upc.setDone_code(doneCode);
				upcList.add(upc);
				
				Date date = new Date();
				upc = new CUserPropChange();
				upc.setUser_id(userId);
				upc.setColumn_name("status_date");
				upc.setOld_value(DateHelper.dateToStr(user.getStatus_date()));
				upc.setNew_value(DateHelper.dateToStr(date));
				upc.setCounty_id(countyId);
				upc.setArea_id(areaId);
				upc.setDone_code(doneCode);
				upcList.add(upc);
				
				user.setStatus(userStatus);
				user.setStatus_date(date);
				
				CDoneCode cDoneCode = new CDoneCode();
				cDoneCode.setDone_code(doneCode);
				cDoneCode.setBusi_code(bp.getBusiCode());
				cDoneCode.setStatus(StatusConstants.ACTIVE);
				cDoneCode.setCounty_id(countyId);
				cDoneCode.setArea_id(areaId);
				cDoneCode.setDept_id(getOptr().getDept_id());
				cDoneCode.setOptr_id(getOptr().getOptr_id());
				dcList.add(cDoneCode);
				
				CDoneCodeDetail detail = new CDoneCodeDetail();
				detail.setDone_code(doneCode);
				detail.setCust_id(user.getCust_id());
				detail.setUser_id(userId);
				detail.setArea_id(areaId);
				detail.setCounty_id(countyId);
				dcdList.add(detail);
			}
		}
		userComponent.updateUserStatus(users, upcList,dcList,dcdList);
	}
	
	public void renewUser(String userId) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		CUser user = queryUserById(userId);
		
		//恢复用户状态，取最近状态异动：如果最近状态异动为报停，则新状态为报停，否则均为正常
		userComponent.renewUser(doneCode, userId);
		
		//直接修改用户状态为正常
//		updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.ACTIVE);
		
		//生成激活用户JOB
		jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_USER, user.getCust_id(),
				user.getUser_id(), user.getStb_id(),user.getCard_id(),user.getModem_mac(), null, null,JsonHelper.fromObject(user));
		
		//修改隔离用户产品状态为正常
		List<CProdDto> prodList = userProdComponent.queryByUserId(user.getUser_id());
		for (CProdDto prod:prodList){
			//修改信控不处理的状态，报停、不计算、暂停不处理
			if(StatusConstants.ISOLATED.equals(prod.getStatus())){
//				String oldStatus = userProdComponent.queryLastStatus(prod.getProd_sn());
//				if (StringHelper.isEmpty(oldStatus)) {
//					if (prod.getInvalid_date().after(prod.getStatus_date()))
//						oldStatus = StatusConstants.ACTIVE;
//					else
//						oldStatus = StatusConstants.OWESTOP;
//				}
				
				String oldStatus = StatusConstants.ACTIVE;
				
				userProdComponent.updateProdStatus(doneCode,prod.getProd_sn(),prod.getStatus(), oldStatus);
				
				/*List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
				changeList.add(new CProdPropChange("status",
						prod.getStatus(),StatusConstants.ACTIVE));
				changeList.add(new CProdPropChange("status_date",
						DateHelper.dateToStr(prod.getStatus_date()),DateHelper.dateToStr(new Date())));
				
				userProdComponent.editProd(doneCode,prod.getProd_sn(),changeList);*/
				
				if (isProdOpen(oldStatus)){
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, user.getCust_id(),
						user.getUser_id(), user.getStb_id(),user.getCard_id(),user.getModem_mac(), prod.getProd_sn(),prod.getProd_id());
				}
			
				//更新到期日
				userProdComponent.updateInvalidDate(doneCode, prod.getProd_sn(), DateHelper.getDiffDays(prod.getStatus_date(), new Date()), 0, new CAcctAcctitem());
			}
		}
		
		jobComponent.createCreditExecJob(doneCode, user.getCust_id());
		jobComponent.createAcctModeCalJob(doneCode, user.getCust_id());
		saveAllPublic(doneCode, getBusiParam());
	}
	
	public void setPromComponent(PromComponent promComponent) {
		this.promComponent = promComponent;
	}

	public void setUserPromComponent(UserPromComponent userPromComponent) {
		this.userPromComponent = userPromComponent;
	}

	/**
	 * @param expressionUtil the expressionUtil to set
	 */
	public void setExpressionUtil(ExpressionUtil expressionUtil) {
		this.expressionUtil = expressionUtil;
	}

	/**
	 * @param busiConfigComponent the busiConfigComponent to set
	 */
	public void setBusiConfigComponent(BusiConfigComponent busiConfigComponent) {
		this.busiConfigComponent = busiConfigComponent;
	}

	public String createLoginName(String loginName, String countyId) {
		String name = "";
		while(true){
			if ("0501".equals(countyId)){
				Random random = new Random(System.currentTimeMillis());
				name = "yc"+new CnToSpell().getPinYinHeadChar(loginName)+(Math.abs(random.nextInt())%900+100);
			}else if ("9004".equals(countyId)){
				Random random = new Random(System.currentTimeMillis());
				name = "xt"+new CnToSpell().getPinYinHeadChar(loginName)+(Math.abs(random.nextInt())%900+100)+"@xttv";
			}else if (COUNTY_9005.equals(countyId)){
				name = "gd" + RandomStringUtils.randomNumeric(5);	//"gd" + 5位数字
			}else if ("9006".equals(countyId)){
				name = "tm" + new CnToSpell().getPinYinHeadChar(loginName) + RandomStringUtils.randomNumeric(3);
			}
			if(StringHelper.isEmpty(name))	break;
			try {
				this.checkLoginName(name);
				break;
			} catch (Exception e) {
				
			}
		}
		return name;
	}
	/* (non-Javadoc)取消授权
	 * @see com.ycsoft.business.service.IUserService#CancelAuth()
	 */
	public void saveCancelCaAuth() throws Exception {
		//获取客户用户信息
		List<CUser> userList = getBusiParam().getSelectedUsers();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//保存取消授权
		jobComponent.saveCancelCaAuth(userList.get(0),doneCode);

		saveAllPublic(doneCode,getBusiParam());
		
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IUserService#userInvalid()
	 */
	public void saveUserInvalid() throws Exception {
		//获取客户用户信息
		CCust cust = getBusiParam().getCust();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//保存到期日期计算任务
		jobComponent.saveCustInvalidCal(doneCode,cust.getCust_id());
		saveAllPublic(doneCode,getBusiParam());
		
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IUserService#batchLogoffUser(java.util.List)
	 */
	public void batchLogoffUser(List<String> userIdList,String isReclaimDevice,String deviceStatus,String remark) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		
		if(null != userIdList && userIdList.size() == 0){
			throw new ServicesException("表格数据为空");
		}
		List<String> userIds = new ArrayList<String>();
		for(String userId : userIdList){
			if(!userIds.contains(userId)){
				userIds.add(userId);
			}
		}
		
		userComponent.batchLogoffUser(doneCode,remark,userIds,isReclaimDevice,deviceStatus);
		
		saveDoneCode(doneCode, busiCode, null,remark);
		
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IUserService#editFreeUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date)
	 */
	public void editFreeUser(String userId, String prodSn, String newTariffId,String type,Date tariffStartDate )throws Exception {
		/*
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String  custId = getBusiParam().getCust().getCust_id();
		CUser user = userComponent.queryUserById(userId);
		String newValue = "";
		if(type.equals("OUT")){
			newValue = "F";
		}else{
			newValue = "T";
		}
		List<CUserPropChange> changeList = new ArrayList<CUserPropChange>();
		CUserPropChange change =new CUserPropChange();
		change.setColumn_name("str19");
		change.setNew_value(newValue);
		changeList.add(change);
		
		userComponent.editUser(doneCode, userId, changeList);
		//如果原来就有prodSn,变更基本包资费资费
		if(StringHelper.isNotEmpty(prodSn)){
			changeTariff(prodSn, newTariffId, DateHelper.format(tariffStartDate, "yyyy-MM-dd"), null, true, true, doneCode);
			
			PProdTariff newTariff = prodComponent.queryTariffById(newTariffId);
			PProd prod = prodComponent.queryProdByProdSn(prodSn);
			getBusiParam().setBusiConfirmParam("prod", prod);
			getBusiParam().setBusiConfirmParam("new_tariff", newTariff);
			//生成计算到期日任务
			jobComponent.createInvalidCalJob(doneCode, custId);
		}
		saveAllPublic(doneCode, getBusiParam());
		*/
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.IUserService#transferUsers(java.lang.String)
	 */
	public void transferUsers(String toCustId) throws Exception {
//	    Integer doneCode = doneCodeComponent.gDoneCode();
//	    String busiCode = this.getBusiParam().getBusiCode();
//	    List<CUser>  cuserList = this.getBusiParam().getSelectedUsers();
//	    CUser cuser = cuserList.get(0);
//	    String userId = cuser.getUser_id();
//	    CCust oldCust = this.getBusiParam().getCust();
//	    if(StringHelper.isEmpty(toCustId)){
//	    	throw new ServicesException("转户失败:目标客户不存在!");
//	    }
//	    CCust newCust  = custComponent.queryCustById(toCustId);
//	    String custId = oldCust.getCust_id();
//	    List<CProdDoneInfo> prodDoneInfoList = new ArrayList<CProdDoneInfo>(); 
// 
//	    if(!oldCust.getCust_type().equals(SystemConstants.CUST_TYPE_RESIDENT) || !newCust.getCust_type().equals(SystemConstants.CUST_TYPE_RESIDENT)){
//	    	throw new ServicesException("转户失败:原客户与目标客户必须都是居民客户!");
//	    }
//	    
//		List<UserDto>  userList = userComponent.queryUser(toCustId);
//
//		if(!newCust.getStatus().equals(StatusConstants.PREOPEN)&&!newCust.getStatus().equals(StatusConstants.ACTIVE)){
//			 throw new ServicesException("转户失败:目标客户非正常客户或者预开户客户!");
//		}
//		UserDto oldUser = userComponent.queryUserById(userId);
//		if(StringHelper.isNotEmpty(oldUser.getCard_id())){
//			RDevice device = deviceComponent.findByDeviceCode(oldUser.getCard_id());
//			if(null != device && SystemConstants.OWNERSHIP_GD.equals(device.getOwnership())){
//				throw new ServicesException("数字用户转户失败:智能卡"+oldUser.getCard_id()+",属于广电！");
//			}
//		}
//		if(StringHelper.isNotEmpty(oldUser.getStb_id())){
//			RDevice device = deviceComponent.findByDeviceCode(oldUser.getStb_id());
//			if(null != device && SystemConstants.OWNERSHIP_GD.equals(device.getOwnership())){
//				throw new ServicesException("数字用户转户失败:机顶盒号"+oldUser.getStb_id()+",属于广电！");
//			}
//		}
//		if(oldUser.getUser_type().equals("BAND") && StringHelper.isNotEmpty(oldUser.getModem_mac())){
//			RDevice device = deviceComponent.findByDeviceCode(oldUser.getModem_mac());
//			if(null != device && SystemConstants.OWNERSHIP_GD.equals(device.getOwnership())){
//				throw new ServicesException("宽带用户转户失败:Modem号"+oldUser.getModem_mac()+",属于广电！");
//			}
//		}
//		
//		
//		String fromflag = acctComponent.queryWhetherUserOwnfee(userId);
//		String zZDflag = "0";
//		if(cuser.getUser_type().equals("DTV")){
//			List<CUser> fromUserList = userComponent.queryUserByCustId(cuser.getCust_id());
//			for(CUser user : fromUserList){
//				if(user.getUser_type().equals("DTV")){
//					UserDto fromUser = userComponent.queryUserById(user.getUser_id());
//					if(!userId.equals(fromUser.getUser_id()) && fromUser.getTerminal_type().equals(SystemConstants.USER_TERMINAL_TYPE_ZZD)) {
//						zZDflag = acctComponent.queryWhetherUserOwnfee(userId);
//					}
//				}
//			}
//		}
//		//主机和转出用户不能欠费
//		if(!"0".equals(fromflag) || !"0".equals(zZDflag)){
//			throw new ServicesException("数字用户转户失败:主机和转出用户不能欠费！");
//		}
//		
//		
//		//标准目标客户无数字用户
//		boolean toCustNoDtvFlag=true;
////	    标识宽带用户，必须有一个用户状态正常
//	    boolean normalUserFlag=false;
//	    Integer freeNum = 0; //免费终端
//	    for(UserDto user : userList){
//	      if(oldUser.getUser_type().equals("DTV")){
//	    	  if(user.getUser_type().equals("DTV")){
//	    		  toCustNoDtvFlag=false;
//		    	  if(oldUser.getTerminal_type().equals(SystemConstants.USER_TERMINAL_TYPE_ZZD) && user.getTerminal_type().equals(SystemConstants.USER_TERMINAL_TYPE_ZZD)){
//		    		  throw new ServicesException("数字用户转户失败:目标客户名下数字用户已经是主机，不能同是主机!");
//		    	  }
//		    	  if(oldUser.getTerminal_type().equals(SystemConstants.USER_TERMINAL_TYPE_FZD)){
//		    		  if(user.getTerminal_type().equals(SystemConstants.USER_TERMINAL_TYPE_ZZD)){
//		    			  if(!user.getStatus().equals(StatusConstants.ACTIVE) && !user.getStatus().equals(StatusConstants.OWENOTSTOP)){
//			    			  throw new ServicesException("数字用户转户失败:目标客户名下数字用户主机状态必须是正常或欠费未停!");
//			    		  }
//		    		  }else{
//		    			  if("T".equals(user.getStr19())){
//		    				  freeNum++;
//		    	    	  }
//		    		  }
//		    	  }
//		    	  normalUserFlag=true;
//		    	  
//		    	  if (user.getTerminal_type().equals(SystemConstants.USER_TERMINAL_TYPE_ZZD)){
//		    		  List<CProdDto>  prodList = userProdComponent.queryByUserId(user.getUser_id());
//		    		  for (CProdDto prod : prodList) {
//							if (!prod.getStatus().equals(StatusConstants.ACTIVE)&& !prod.getStatus().equals(StatusConstants.OWENOTSTOP)&& prod.getIs_base().equals("T")) {
//								throw new ServicesException(
//										"数字用户转户失败:转户时目标客户名下所有用户基本产品状态必须是正常或欠费未停!");
//							}
//						}
//		    	  }
//	    	  }
//	    	  
//	      }else if(oldUser.getUser_type().equals("BAND") || oldUser.getUser_type().equals("ATV")){
//	    	  //宽带转户时，目标客户而状态必须正常
//	    	  if(user.getStatus().equals(StatusConstants.ACTIVE)){
//	    		  normalUserFlag=true;
//	    	  }
//	      }
//	    }
////	    if("T".equals(oldUser.getStr19()) && freeNum>1){
////	    	throw new ServicesException("数字用户转户失败:目标客户名下已经有2个免费终端!");
////	    }
//	    
//	    if(oldUser.getUser_type().equals("BAND") && !normalUserFlag&&userList.size()>0){
//	    	 throw new ServicesException("宽带用户转户失败:目标客户名下用户状态不正常，转户时目标客户名下至少有一个用户状态正常!");
//	    }
//	    if(toCustNoDtvFlag&& oldUser.getUser_type().equals(SystemConstants.USER_TYPE_DTV) && !oldUser.getTerminal_type().equals(SystemConstants.USER_TERMINAL_TYPE_ZZD)){
//	    	 throw new ServicesException("数字用户转户失败:转户用户是非主机，目标客户名下无主机!");
//	    }
//	 
//	    List<CProdDto>  prodList = userProdComponent.queryByUserId(userId);
////	    3. 副机过户，目标客户主机有空余位置挂载。
//	    if (cuser instanceof CUserDtv || cuser instanceof CUserBroadband) {
//	    	if("DTV".equals(oldUser.getUser_type())){
//	    		List<UserDto>  oldUserList = userComponent.queryUser(oldUser.getCust_id());
//	    		//超额终端
//	    		UserDto outUser = new UserDto();
//	    		if("T".equals(oldUser.getStr19())){
//	    			//目标客户已经有2台免费终端，转户用户由免费转为超额
//	    			if(freeNum>1){
//		        		updateUserDyn(doneCode,userId,"str19",oldUser.getStr19(),"T".equals(oldUser.getStr19())?"F":"T");
//	    			}
//	    			for(UserDto dto:oldUserList){
//		    			if("DTV".equals(dto.getUser_type()) && dto.getTerminal_type().equals(SystemConstants.USER_TERMINAL_TYPE_FZD) && !"T".equals(dto.getStr19())){
//		    				BeanUtils.copyProperties(dto, outUser);
//		    				break;
//		    			}
//		    		}
//	    			//原客户其中一台超额终端变成免费终端
//	    			if(StringHelper.isNotEmpty(outUser.getUser_id())){
//	    				updateUserDyn(doneCode,outUser.getUser_id(),"str19",outUser.getStr19(),"T".equals(outUser.getStr19())?"F":"T");
//	    			}
//	    		}else{
//	    			//目标客户已经没满2台免费终端，转户用户由超额转为免费
//	    			if(freeNum<2){
//	    				updateUserDyn(doneCode,userId,"str19",oldUser.getStr19(),"F".equals(oldUser.getStr19())?"T":"F");
//	    			}
//	    		}
//	    		if(StringHelper.isNotEmpty(outUser.getUser_id())){
//	    			List<CProdDto>  outProdList = userProdComponent.queryByUserId(outUser.getUser_id());
//	    			for(CProdDto prod : outProdList){
//    					  List<ProdTariffDto> tariffList = prodComponent.queryTariffByProd(prod.getProd_id());
//    					  if("T".equals(prod.getIs_base())){
//    						  //删除资费job
//    						userProdComponent.removeNextByProdSn(prod.getProd_sn());
//      		        	  }
//    		        	  // tId "F":不能操作；"T":用原来的资费；"**"：变更成适合新客户条件的资费
//    		        	  String tId = userComponent.toUtilValue(outUser.getCust_id(), outUser.getUser_id(), prod.getTariff_id(),tariffList);
//    		        	  if(tId.equals("F")){
//    		        		  throw new ServicesException("转户失败:原客户的超额终端不能转为免费终端!"); 
//    		        	  }else if(!tId.equals("T")){
//    		        		  //如果原来就有prodSn,变更基本包资费资费
//    		        		  changeTariff(prod.getProd_sn(), tId, DateHelper.format(DateHelper.getNextMonth(new Date()), "yyyy-MM-dd"), null, true, true, doneCode);
////	    		        		  userProdComponent.updateProdTariff(doneCode, prod.getProd_sn(), tId);
//    		        	  }
//	    			}
//	    		}
//	    		
//	    	}
//	        for(CProdDto prod : prodList){
////		          4. 目标客户能否使用 被过户用户产品资费的权限。
//	        	  List<ProdTariffDto> tariffList = prodComponent.queryTariffByProd(prod.getProd_id());
//	        	  if("T".equals(prod.getIs_base())){
//	        		//删除资费job
//	        		  userProdComponent.removeNextByProdSn(prod.getProd_sn());
//	        	  }
//	        	  if(tariffList.size() == 0){
//	        		  throw new ServicesException("转户失败:请检查本操作员是否具有使用["+prod.getProd_name()+"]资费的权限或者该资费是否适用本地区!");
//	        	  }
//	        	  // tId "F":不能操作；"T":用原来的资费；"**"：变更成适合新客户条件的资费
//	        	  String tId = userComponent.toUtilValue(toCustId, cuser.getUser_id(), prod.getTariff_id(),tariffList);
//	        	  if(tId.equals("F")){
//	        		  throw new ServicesException("转户失败:目标客户不能使用原产品:"+prod.getProd_name()+"的资费:"+prod.getTariff_name()+"!"); 
//	        	  }else if(!tId.equals("T")){
//	        		  //如果原来就有prodSn,变更基本包资费资费
//	        		  changeTariff(prod.getProd_sn(), tId, DateHelper.format(DateHelper.getNextMonth(new Date()), "yyyy-MM-dd"), null, true, true, doneCode);
////	        		  userProdComponent.updateProdTariff(doneCode, prod.getProd_sn(), tId);
//	        	  }
//	        }
//	    }
//	    //修改状体为正常
//		if (newCust.getStatus().equals(StatusConstants.PREOPEN)){
//			custComponent.updateCustStatus(doneCode,toCustId,StatusConstants.PREOPEN,StatusConstants.ACTIVE);
//		}
//		
//	    //更新产品账单状态为出帐
//	    for(CProdDto prod : prodList){
//	    	CProdDoneInfo prodDoneInfo = new CProdDoneInfo();
//	    	prodDoneInfo.setProd_name(prod.getProd_name());
//	    	prodDoneInfo.setTariff_name(prod.getTariff_name());
//	    	prodDoneInfoList.add(prodDoneInfo);
//		    billComponent.confirmBill(prod.getProd_sn(), doneCode);
//	    }
//	  //将账目的实时费用更新为欠费和同时清空实时费用
//	    List<AcctitemDto>  userAcctItemList =  acctComponent.queryAcctItemByUserId(userId);
//	    for(AcctitemDto acctitem : userAcctItemList){
//		    acctComponent.changeAcctItemOwefee(true, acctitem.getAcct_id(), acctitem.getAcctitem_id(),acctitem.getReal_bill());
//	    }
//	    //销账	    
//		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_TRUE);//销帐会自动查信控
//		
//		jobComponent.createCreditExecJob(doneCode, custId);
//		//生成账务模式判断任务
//		jobComponent.createAcctModeCalJob(doneCode, custId);
//		//生成计算到期日任务
//		jobComponent.createInvalidCalJob(doneCode, custId);
//	    
//		//信控任务
//		jobComponent.createCreditExecJob(doneCode, custId);
//		
//		getBusiParam().getTempVar().put(SystemConstants.EXTEND_ATTR_KEY_NEWADDR, newCust.getAddress());
//		String deviceId = StringHelper.isEmpty(oldUser.getStb_id())?(StringHelper.isEmpty(oldUser.getModem_mac())?"":oldUser.getModem_mac()):oldUser.getStb_id();
//	    if(StringHelper.isNotEmpty(deviceId)){
//	    	deviceId = ",设备号:"+deviceId;
//	    }
//		//1.  生成施工单， 3.  杂费收取
//		String info = "新受理编号:"+newCust.getCust_no()+",新客户名称:"+newCust.getCust_name()+deviceId;
//		getBusiParam().setRemark(StringHelper.isEmpty(getBusiParam().getRemark())?info:getBusiParam().getRemark()+"("+info+")");
//	    saveAllPublic(doneCode,getBusiParam());
//	    // 保存打印信息
//	    Map<String, Object> map = new HashMap<String, Object>();
//
//	    //进行用户转户
//	    userComponent.changeCust(userId, toCustId, doneCode, busiCode);
//	    
//	    //生成计算到期日任务
//		jobComponent.createInvalidCalJob(doneCode, toCustId);
//		
//	    map.put("old_cust_name",oldCust.getCust_name() );
//	    map.put("new_cust_name",newCust.getCust_name() );
//	    map.put("old_cust_no", oldCust.getCust_no());
//	    map.put("new_cust_no", newCust.getCust_no());
////	    map.put("old_address", oldCust.getAddress());
////	    map.put("new_address", newCust.getAddress());
//	    map.put("user_type", cuser.getUser_type_text());
//	    map.put("modem_mac", cuser.getModem_mac());
//		// 数字电视
//		  if (cuser instanceof CUserDtv) {
// 			map.put("terminal_type", MemoryDict.getDictName(DictKey.TERMINAL_TYPE, ((CUserDtv) cuser).getTerminal_type()));
//			map.put("card_id", cuser.getCard_id());
//			map.put("stb_id", cuser.getStb_id());
// 		}
//		// 宽带
//		else if (cuser instanceof CUserBroadband) {
//			map.put("login_name", ((UserDto) cuser).getLogin_name());
//			map.put("login_password", ((UserDto) cuser).getLogin_password());
//		}  
////		  map.put("prodList", prodDoneInfoList);
//		  doneCodeComponent.saveDoneCodeInfo(doneCode, custId,null, map);
//		  Integer toDoneCode = doneCodeComponent.gDoneCode();
//		  saveDoneCode(toDoneCode, busiCode, toCustId,"原受理编号:"+oldCust.getCust_no()+",原客户流水号:"+doneCode);
	}

	@Override
	public void checkStopUser(String[] userIds) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveRemoveUser(String userId, Integer cancelFee, Integer refundFee) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validAccount(String name) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveChangeDevice(String userId, String deviceCode, String changeReason, String deviceBuyMode, FeeInfoDto deviceFee) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TDeviceChangeReason> queryDeviceChangeReason() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PSpkgOpenuser> querySpkgUser(String custId,String spkgSn) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PSpkgOpenbusifee> querySpkgOpenFee(String custId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void cancelInstallTask(String taskId) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void untuckUsers() throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void saveEditPwd(String loginName, String newPwd) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void batchLogoffUser(List<CancelUserDto> cancelUserList) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
