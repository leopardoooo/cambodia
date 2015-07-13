package com.ycsoft.web.action.core;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserAtv;
import com.ycsoft.beans.core.user.CUserBroadband;
import com.ycsoft.beans.core.user.CUserDtv;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.business.dto.config.ChangeValueDto;
import com.ycsoft.business.dto.core.prod.CProdBacthDto;
import com.ycsoft.business.dto.core.prod.DisctFeeDto;
import com.ycsoft.business.dto.core.user.UserProdRscDto;
import com.ycsoft.business.service.IUserProdService;
import com.ycsoft.business.service.IUserService;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.FileHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

/**
 * 用户控制器
 *
 * @author liujiaqi
 *
 */
@Controller
public class UserAction extends BaseBusiAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 3927133051583505986L;

	private CUser user;
	private CUserDtv userDtv;
	private CUserAtv userAtv;
	private CUserBroadband userBroadband;

	private int payFee;
	private int curMonthFee;
	
	private String userChangeInfo;
	private String effectiveDate;
	private int specFee;

	private String dynamicRscList;
	private String feeDate;
	private Date preOpenTime;//预开通时间
	private String isBankPay;//银行扣费
	private String stopType;
	private String prodId;
	private String tariffId;

	private String prodSns;
	private String banlanceDealType;
	private String transAcctId;
	private String transAcctItemId;
	private int refundFeeValue;

	private String prodSn;
	private String pkgSn;
	private String newTariffId;
	private String effDate;
	private String expDate;
	private String invalidDate;

	private String promotionId;
	private String feeListJson;
	private String acctListJson;
	private int times;
	private String custId;
	private String userId;
	
	private String promotionSn;

	private IUserService userService;
	private IUserProdService userProdService;
	private String refreshType;

	private String pordLists;
	private String userLists;
	
	private String netType;
	private String modemMac;
	
	private String loginName;
	
	private String userStatus;
	private File file;
	private String userIds;

	private String promFeeSn;
	/**
	 * 用户开户
	 * @throws Exception
	 */
	public String createUser() throws Exception {
		if (userDtv!=null){
			if (StringHelper.isEmpty(userDtv.getPassword())
					&& userDtv.getServ_type().equals(
							SystemConstants.DTV_SERV_TYPE_DOUBLE)) {
				userDtv.setPassword(SystemConstants.DEFAULT_PAY_PASSWORD);
			}
			userService.createUser(userDtv);
			getRoot().setSimpleObj(userDtv.getUser_id());
		}else if (userAtv!=null){
			userService.createUser(userAtv);
			getRoot().setSimpleObj(userAtv.getUser_id());
		}else if (userBroadband!=null){
			userService.createUser(userBroadband);
			getRoot().setSimpleObj(userBroadband.getUser_id());
		}

		return JSON;
	}

	/**
	 * 用户销户
	 * @throws Exception
	 */
	public String logoffUser() throws Exception{
		userService.saveRemoveUser(banlanceDealType, transAcctId, transAcctItemId);
		return JSON_SUCCESS;
	}

	/**
	 * 模拟转数
	 * @throws Exception
	 */
	public String atvToDtv() throws Exception{
		if (StringHelper.isEmpty(userDtv.getPassword())
				&& userDtv.getServ_type().equals(
						SystemConstants.DTV_SERV_TYPE_DOUBLE)) {
			userDtv.setPassword(SystemConstants.DEFAULT_PAY_PASSWORD);
		}
		userService.saveAtvToDtv(userDtv,curMonthFee,payFee);
		return JSON_SUCCESS;
	}

	/**
	 * 开通双向
	 * @throws Exception
	 */
	public String openDuplex() throws Exception{
		String password = request.getParameter("password");
		String netType = request.getParameter("net_type");
		String modemMac = request.getParameter("modem_mac");
		if (StringHelper.isEmpty(password)) {
			password = SystemConstants.DEFAULT_PAY_PASSWORD;
		}
		String vodUserType = request.getParameter("str11");
		//是否保留远机顶盒的保修期还是延期三年
		String remainReplacoverDate = request.getParameter("remainReplacoverDate");
		userService.saveOpenInteractive(netType, modemMac,password,vodUserType,remainReplacoverDate);
		return JSON_SUCCESS;
	}

	/**
	 * 转户
	 * @throws Exception
	 */
	public String transferUsers() throws Exception{
		userService.transferUsers(custId);
		return JSON_SUCCESS;
	}
	/**
	 * 修改用户资料
	 * @throws Exception
	 */
	public String editUser() throws Exception{
		Type type = new TypeToken<List<ChangeValueDto>>(){}.getType();
		Gson gson = new Gson();
		List<ChangeValueDto> changeValueList = gson.fromJson(userChangeInfo, type);
		List<CUserPropChange> propChangeList = new ArrayList<CUserPropChange>();
		//如果有信息发生了修改
		if (changeValueList != null && changeValueList.size()>0){
			for (ChangeValueDto dto:changeValueList){
				CUserPropChange propChange = new CUserPropChange();
				propChange.setColumn_name(dto.getColumnName());
				propChange.setOld_value(dto.getOldValue());
				propChange.setNew_value(dto.getNewValue());
				propChangeList.add(propChange);
			}
		}
		userService.editUser(propChangeList);
		return JSON_SUCCESS;
	}
	
	/**
	 * 第二终端转副机
	 * @return
	 * @throws Exception
	 */
	public String ezdTofzd() throws Exception{
		Type type = new TypeToken<List<ChangeValueDto>>(){}.getType();
		Gson gson = new Gson();
		List<ChangeValueDto> changeValueList = gson.fromJson(userChangeInfo, type);
		List<CUserPropChange> propChangeList = new ArrayList<CUserPropChange>();
		//如果有信息发生了修改
		if (changeValueList != null && changeValueList.size()>0){
			for (ChangeValueDto dto:changeValueList){
				CUserPropChange propChange = new CUserPropChange();
				propChange.setColumn_name(dto.getColumnName());
				propChange.setOld_value(dto.getOldValue());
				propChange.setNew_value(dto.getNewValue());
				propChangeList.add(propChange);
			}
		}
		
		userService.saveEzdtoFzd(propChangeList, prodSn, newTariffId);
		
		return JSON_SUCCESS;
	}
	
	public String editStb() throws Exception {
		String stbId = request.getParameter("stb_id");
		String cardId = request.getParameter("card_id");
		userService.editStb(stbId,cardId);
		return JSON_SUCCESS;
	}
	
	public String saveOfflineCmd() throws Exception{
			userService.saveOffLine();
		return JSON_SUCCESS;
	}
	public String saveClearBind() throws Exception{
			userService.saveClearBind();
		return JSON_SUCCESS;
	}
	
	public String saveEditPwd() throws Exception{
		String pwd = request.getParameter("login_password");
		userService.saveEditPwd(pwd);
	return JSON_SUCCESS;
}
	
	/**
	 * 取消双向
	 * @return
	 * @throws Exception
	 */
	public String saveCancelOpenInteractive() throws Exception {
		userService.saveCancelOpenInteractive();
		return JSON;
	}
	
	/**
	 * 修改接入方式
	 * @return
	 * @throws Exception
	 */
	public String saveEditNetType() throws Exception {
		userService.saveEditNetType(netType, modemMac);
		return JSON;
	}
	
	/**
	 * 报停
	 * @throws Exception
	 */
	public String stopUser() throws Exception{
		userService.saveStop(effectiveDate,specFee);
		return JSON_SUCCESS;
	}
	
	/**
	 * 取消预报停
	 * @return
	 * @throws Exception
	 */
	public String cancelStopUser() throws Exception{
		userService.cancelStopUser();
		return JSON_SUCCESS;
	}
	
	//续报停
	public String editUserStop() throws Exception {
		userService.editUserStop();
		return JSON;
	}
	//重算到期日任务
	public String userInvalid() throws Exception{
		userService.saveUserInvalid();
		return JSON;
	}
	
	public String queryStopByUsers() throws Exception {
		getRoot().setSimpleObj(userService.queryStopByUsers(userLists));
		return JSON;
	}
	/**
	 * 报开
	 * @throws Exception
	 */
	public String openUser() throws Exception{
		String stbId = request.getParameter("stb_id");
		String cardId = request.getParameter("card_id");
		String modemMac = request.getParameter("modem_mac");
		userService.saveOpen(stbId,cardId,modemMac,specFee);
		return JSON_SUCCESS;
	}
	
	//模拟剪线
	public String saveAtvCustLine() throws Exception {
		userService.saveAtvCustLine();
		return JSON;
	}
	
	//模拟恢复
	public String saveAtvActive() throws Exception {
		userService.saveAtvActive();
		return JSON;
	}

	/**
	 * 订购产品
	 * @throws Exception
	 */
	public String orderProd() throws Exception{
		if(StringHelper.isNotEmpty(pordLists)){
			userProdService.saveOrderList(pordLists);
		}else{
			List<UserProdRscDto> dyResList = null;
			if(StringHelper.isNotEmpty(dynamicRscList)){
				Type type = new TypeToken<List<UserProdRscDto>>(){}.getType();
				Gson gson = new Gson();
				dyResList = gson.fromJson(dynamicRscList, type);
			}
			userProdService.saveOrder(prodId, tariffId, feeDate, dyResList, expDate,preOpenTime,isBankPay);
		}
		return JSON_SUCCESS;
	}
	
	public String changeBandProd() throws Exception {
		String oldProdSn = request.getParameter("oldProdSn");
		String present_fee = request.getParameter("present_fee");
		int presentFee = 0;
		if(StringHelper.isNotEmpty(present_fee))
			presentFee = Integer.parseInt(present_fee);
		userProdService.changeBandProd(prodId, tariffId, feeDate, expDate, oldProdSn, presentFee);
		return JSON_SUCCESS;
	}
	
	public String changeProdDynRes() throws Exception {
		List<UserProdRscDto> dyResList = null;
		if(StringHelper.isNotEmpty(dynamicRscList)){
			Type type = new TypeToken<List<UserProdRscDto>>(){}.getType();
			Gson gson = new Gson();
			dyResList = gson.fromJson(dynamicRscList, type);
		}
		userProdService.changeProdDynRes(prodSn, dyResList);
		return JSON_SUCCESS;
	}
	
	public String batchOrderProd() throws Exception {
		List<UserProdRscDto> dyResList = null;
		if(StringHelper.isNotEmpty(dynamicRscList)){
			Type type = new TypeToken<List<UserProdRscDto>>(){}.getType();
			Gson gson = new Gson();
			dyResList = gson.fromJson(dynamicRscList, type);
		}
		
		String msg = "";
		List<String> userIdList = new ArrayList<String>();
		try{
			if(file != null){
				userIdList = FileHelper.fileToArray(file);
			}
			if(userIdList.size() > 1000)
				throw new Exception("请一次性录入小于1000条数据");
			else if(userIdList.size() == 0){
				throw new Exception("文件数据为空");
			}
			userProdService.saveBatchOrder(userIdList, prodId, tariffId, feeDate, dyResList, expDate);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		
		return retrunNone(msg);
	}
	
		
	/**
	 * 订购客户套餐
	 * @return
	 * @throws Exception
	 */
	public String orderPkg() throws Exception{
		userProdService.saveOrderCustPkg(prodId, tariffId, feeDate,stopType, prodSns.split(","));
		return JSON_SUCCESS;
	}

	/**
	 * 修改套餐
	 * @return
	 * @throws Exception
	 */
	public String editPkg() throws Exception{
		userProdService.saveEditCustPkg(pkgSn,stopType, prodSns.split(","));
		return JSON;
	}
	/**
	 * 退订产品
	 * @throws Exception
	 * 20130401,by wang  产品退订，记录套餐信息
	 */
	public String cancelProd() throws Exception{
		if (getBusiParam().getSelectedUsers() != null && getBusiParam().getSelectedUsers().size()>0)
			userProdService.saveTerminate(prodSns.split(","), banlanceDealType, transAcctId, transAcctItemId,promFeeSn);
		else
			userProdService.saveTerminatePkg(prodSns, banlanceDealType, transAcctId, transAcctItemId);
		return JSON_SUCCESS;
	}
	
	public String batchCancelProd() throws Exception {
		String msg = "";
		List<String> userIdList = new ArrayList<String>();
		try{
			if(file != null){
				userIdList = FileHelper.fileToArray(file);
			}
			if(userIdList.size() > 1000)
				throw new Exception("请一次性录入小于1000条数据");
			else if(userIdList.size() == 0){
				throw new Exception("文件数据为空");
			}
			
			userProdService.saveBatchCancel(userIdList, prodId);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		
		return retrunNone(msg);
	}

	/**
	 * 设备变更
	 * @throws Exception
	 */
	public String changeDevice() throws Exception{
		return JSON_SUCCESS;
	}

	/**
	 * 资费变更
	 * @throws Exception
	 */
	public String changeTariff() throws Exception{
		userProdService.changeTariff(prodSn, newTariffId, effDate,expDate,false);
		return JSON_SUCCESS;
	}
	
	/**
	 * 变更到期日
	 * @return
	 * @throws Exception
	 */
	public String editInvalidDate() throws Exception{
		userProdService.editInvalidDate(prodSn, invalidDate);
		return JSON_SUCCESS;
	}
	
	/**
	 * 批量修改资费
	 * @return
	 * @throws Exception
	 */
	public String bacthChangeTariff() throws Exception{
		List<CProdBacthDto> pordList = new ArrayList<CProdBacthDto>();
		Type type = new TypeToken<List<CProdBacthDto>>(){}.getType();
		Gson gson = new Gson();
		pordList = gson.fromJson(pordLists, type);
		userProdService.bacthChangeTariff(pordList, newTariffId,false);
		return JSON_SUCCESS;
	}
	/**
	 * 失效日期变更
	 * @throws Exception
	 */
	public String changeExpDate() throws Exception{
		userProdService.changeExpDate(prodSn,expDate);
		return JSON_SUCCESS;
	}
	
	public String removeByProdSn() throws Exception {
		userProdService.removeByProdSn(prodSn, tariffId);
		return JSON_SUCCESS;
	}
	
	//产品暂停
	public String pauseProd() throws Exception {
		userProdService.pauseProd(prodSn,userId);
		return JSON;
	}
	
	//产品恢复
	public String resumeProd() throws Exception {
		userProdService.resumeProd(prodSn,userId);
		return JSON;
	}
	
	/**
	 * 指令重发
	 * @throws Exception
	 */
	public String ResendCmd() throws Exception{
		userService.saveResendCa();
		return JSON_SUCCESS;
	}
	
	/**
	 * 开户指令重发
	 * @throws Exception
	 */
	public String ResendVodCmd() throws Exception{
		userService.saveResendUserCmd();
		return JSON_SUCCESS;
	}
	
	
	/**
	 * 指令刷新
	 * @throws Exception
	 */
	public String RefreshCmd() throws Exception{
		userService.saveRefreshCa(refreshType);
		return JSON_SUCCESS;
	}

	/**
	 * 设备更换
	 * @return
	 */
	public String exchangeDevice(){
		return JSON_SUCCESS;
	}

	/**
	 * 查询可选择的促销
	 * @return
	 * @throws Exception
	 */
	public String querySelectableProm() throws Exception{
		getRoot().setRecords(userService.querySelectableProm());
		return JSON_RECORDS;
	}

	/**
	 * 根据促销ID查询促销详细信息
	 * @return
	 * @throws Exception
	 */
	public String queryPromInfo() throws Exception{
		getRoot().setSimpleObj(userService.queryPromInfoById(custId,userId,promotionId));
		return JSON;
	}

	/**
	 * 保存促销
	 * @return
	 * @throws Exception
	 */
	public String savePromotion() throws Exception{
		List<DisctFeeDto> feeList = null;
		if(StringHelper.isNotEmpty(feeListJson)){
			Type type = new TypeToken<List<DisctFeeDto>>(){}.getType();
			Gson gson = new Gson();
			feeList = gson.fromJson(feeListJson, type);
		}

		List<PPromotionAcct> acctList = null;
		if(StringHelper.isNotEmpty(acctListJson)){
			Type type = new TypeToken<List<PPromotionAcct>>(){}.getType();
			Gson gson = new Gson();
			acctList = gson.fromJson(acctListJson, type);
		}
		userService.savePromotion(times,promotionId, feeList,acctList);
		return JSON_SUCCESS;
	}
	
	public String saveChangePromotion() throws Exception {
		List<PPromotionAcct> acctList = null;
		if(StringHelper.isNotEmpty(acctListJson)){
			Type type = new TypeToken<List<PPromotionAcct>>(){}.getType();
			Gson gson = new Gson();
			acctList = gson.fromJson(acctListJson, type);
		}
		userService.saveChangePromotion(times, promotionSn, promotionId, acctList);
		return JSON_SUCCESS;
	}
	
	public String saveCancelPromotion() throws Exception{
		userService.saveCancelPromotion(promotionSn);
		return JSON_SUCCESS;
	}

	/**
	 * 保存租赁费用
	 * @return
	 * @throws Exception
	 */
	public String saveLeaseFee() throws Exception{
		String amount = request.getParameter("amount");
		String feeId = request.getParameter("fee_id");
		userService.saveLeaseFee(feeId,amount);
		return JSON_SUCCESS;
		
	}
	
	
	/**
	 * 用户临时授权
	 * @return
	 * @throws Exception
	 */
	public String saveOpenTemp() throws Exception {
		userService.saveOpenTemp();
		return JSON_SUCCESS;
	}
	
	/**
	 * 批量用户临时授权
	 * @return
	 * @throws Exception
	 */
	public String saveOpenTempBatch() throws Exception {
		userService.saveOpenTempBatch();
		return JSON_SUCCESS;
	}
	
	/**
	 * 用户排斥资源
	 * @return
	 * @throws Exception
	 */
	public String saveRejectRes() throws Exception {
		String resIds = request.getParameter("resIds");
		userService.saveRejectRes(userId, custId, resIds);
		return JSON_SUCCESS;
	}
	
	/**
	 * 查询用户有效资源
	 * @return
	 * @throws Exception
	 */
	public String queryValidRes() throws Exception{
		getRoot().setRecords(userService.queryValidRes(userId));
		return JSON_RECORDS;
	}
	
	/**
	 * 部门下的是所有操作员
	 * @param deptId
	 * @return
	 * @throws JDBCException
	 */
	public String getByDeptId() throws Exception {
		String deptId = request.getParameter("deptId");
		getRoot().setRecords(userService.getByDeptId(deptId));
		return JSON_RECORDS;
	}

	/**
	 * 查询租赁费用信息
	 * @return
	 * @throws Exception
	 */
	public String queryZlFeeById() throws Exception{
		getRoot().setSimpleObj(userService.queryZlFeeById());
		return JSON;
	}
	
	public String checkLoginName() throws Exception{
		userService.checkLoginName(loginName);
		return JSON;
	}
	
	public String createLoginName() throws Exception{
		getRoot().setSimpleObj(userService.createLoginName(loginName,optr.getCounty_id()));
		return JSON_SIMPLEOBJ;
	}
	
	public String toSingleCard() throws Exception{
		String newCardId = request.getParameter("newCardCode");
		String str4 = request.getParameter("str4");
		String str5 = request.getParameter("str5");
		boolean reclaim = SystemConstants.BOOLEAN_TRUE.equals(request.getParameter("reclaimDevice"))?true:false;
		String deviceStatus = request.getParameter("deviceStatus");
		userService.saveToSingleCard(newCardId,str4,str5,reclaim,deviceStatus);
		return JSON;
	}
	
	//充值卡充值
	public String rechargeCard() throws Exception {
		String icCard = request.getParameter("icCard");
		String rechargeCard = request.getParameter("rechargeCard");
		getRoot().setSimpleObj(userService.saveRechargeCard(icCard, rechargeCard));
		return JSON_SIMPLEOBJ;
	}
	
	public String updateUserStatus() throws Exception {
		String msg = "";
		List<String> list = new ArrayList<String>();
		List<String> userIdList = null;
		String[] ids = null;
		if(StringHelper.isNotEmpty(userIds)){
			ids = userIds.split(",");
		}
		try {
			if(file != null){
				userIdList = FileHelper.fileToArray(file);
				list.addAll(userIdList);
				if(ids != null){
					for(int i=0,len=ids.length;i<len;i++){
						if(!list.contains(ids[i])){
							list.add(ids[i]);
						}
					}
				}
			}else{
				if(ids != null){
					for(int i=0,len=ids.length;i<len;i++){
						if(!list.contains(ids[i]))
							list.add(ids[i]);
					}
				}
			}
			userService.updateUserStatus(list, userStatus);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		return retrunNone(msg);
	}
	
	/**
	 * 调用存储过程，批量销用户
	 * @return
	 * @throws Exception
	 */
	public String batchLogoffUser() throws Exception{
		String remark = request.getParameter("remark");
		String isReclaimDevice = request.getParameter("isReclaimDevice");
		String deviceStatus = request.getParameter("deviceStatus");
		String msg = "";
		List<String> userIdList = new ArrayList<String>();
		if(file != null){
			userIdList = FileHelper.fileToArray(file);
		}
		try{
			if(userIdList.size() > 2500)
				throw new Exception("请一次性录入小于2500条数据");
			userService.batchLogoffUser(userIdList,isReclaimDevice,deviceStatus,remark);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		
		return retrunNone(msg);
	}
	
	public String saveProdSyn() throws Exception{
		String[] prodSns = request.getParameterValues("prodSns");
		String[] userIds = request.getParameterValues("userIds");
		userProdService.saveProdSyn(prodSns, userIds);
		return JSON;
	}
	
	public String renewUser() throws Exception {
		userService.renewUser(userId);
		return JSON;
	}
	
	/**
	 * 取消授权
	 * @throws Exception
	 */
	public String cancelCaAuth() throws Exception{
		userService.saveCancelCaAuth();
		return JSON_SUCCESS;
	}
	
	/**
	 * 修改免费终端
	 * @return
	 * @throws Exception
	 */
	public String editFreeUser() throws Exception{
		String userId = request.getParameter("user_id");
		String prodSn = request.getParameter("prod_sn");
		String tariffId = request.getParameter("tariff_id");
		String type = request.getParameter("type");
		Date tariffStartDate = null;
		//如果该终端还没订购基本产品，prodSh
		if(StringHelper.isNotEmpty(request.getParameter("tariffStartDate"))){
			tariffStartDate = DateHelper.parseDate(request.getParameter("tariffStartDate"), "yyyy-MM-dd");
		}
		userService.editFreeUser(userId, prodSn, tariffId,type, tariffStartDate);
		return JSON;
	}
	
	/**
	 * 变更银行扣费
	 * @return
	 * @throws Exception
	 */
	public String changeCprodBank() throws Exception{
		userProdService.changeCprodBank(prodSn);
		return JSON;
	}
	
	/**
	 * @return the userService
	 */
	public IUserService getUserService() {
		return userService;
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}


	/**
	 * @return
	 */
	public IUserProdService getUserProdService() {
		return userProdService;
	}

	/**
	 * @param userProdService
	 */
	public void setUserProdService(IUserProdService userProdService) {
		this.userProdService = userProdService;
	}

	/**
	 * @return the user
	 */
	public CUser getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(CUser user) {
		this.user = user;
	}

	/**
	 * @param effectiveDate
	 */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @param userChangeInfo
	 */
	public void setUserChangeInfo(String userChangeInfo) {
		this.userChangeInfo = userChangeInfo;
	}

	public void setUserDtv(CUserDtv userDtv) {
		this.userDtv = userDtv;
	}

	public CUserDtv getUserDtv() {
		return userDtv;
	}

	/**
	 * @param userAtv the userAtv to set
	 */
	public void setUserAtv(CUserAtv userAtv) {
		this.userAtv = userAtv;
	}

	/**
	 * @param userBroadband the userBroadband to set
	 */
	public void setUserBroadband(CUserBroadband userBroadband) {
		this.userBroadband = userBroadband;
	}

	public CUserAtv getUserAtv() {
		return userAtv;
	}

	public CUserBroadband getUserBroadband() {
		return userBroadband;
	}

	public String getUserChangeInfo() {
		return userChangeInfo;
	}

	public String getDynamicRscList() {
		return dynamicRscList;
	}

	public void setDynamicRscList(String dynamicRscList) {
		this.dynamicRscList = dynamicRscList;
	}

	public String getFeeDate() {
		return feeDate;
	}

	public void setFeeDate(String feeDate) {
		this.feeDate = feeDate;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getTariffId() {
		return tariffId;
	}

	public void setTariffId(String tariffId) {
		this.tariffId = tariffId;
	}

	public void setProdSns(String prodSns) {
		this.prodSns = prodSns;
	}

	public void setBanlanceDealType(String banlanceDealType) {
		this.banlanceDealType = banlanceDealType;
	}

	public void setTransAcctId(String transAcctId) {
		this.transAcctId = transAcctId;
	}

	public void setTransAcctItemId(String transAcctItemId) {
		this.transAcctItemId = transAcctItemId;
	}

	public void setProdSn(String prodSn) {
		this.prodSn = prodSn;
	}

	public void setNewTariffId(String newTariffId) {
		this.newTariffId = newTariffId;
	}

	public void setEffDate(String effDate) {
		this.effDate = effDate;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public String getFeeListJson() {
		return feeListJson;
	}

	public void setFeeListJson(String feeListJson) {
		this.feeListJson = feeListJson;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setSpecFee(int specFee) {
		this.specFee = specFee;
	}

	public String getAcctListJson() {
		return acctListJson;
	}

	public void setAcctListJson(String acctListJson) {
		this.acctListJson = acctListJson;
	}

	public void setPkgSn(String pkgSn) {
		this.pkgSn = pkgSn;
	}

	public int getRefundFeeValue() {
		return refundFeeValue;
	}

	public void setRefundFeeValue(int refundFeeValue) {
		this.refundFeeValue = refundFeeValue;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public void setPayFee(int payFee) {
		this.payFee = payFee;
	}

	public void setCurMonthFee(int curMonthFee) {
		this.curMonthFee = curMonthFee;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public void setStopType(String stopType) {
		this.stopType = stopType;
	}

	public void setRefreshType(String refreshType) {
		this.refreshType = refreshType;
	}

	public void setPordLists(String pordLists) {
		this.pordLists = pordLists;
	}

	public void setUserLists(String userLists) {
		this.userLists = userLists;
	}

	public void setPromotionSn(String promotionSn) {
		this.promotionSn = promotionSn;
	}


	public void setNetType(String netType) {
		this.netType = netType;
	}

	public void setModemMac(String modemMac) {
		this.modemMac = modemMac;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setInvalidDate(String invalidDate) {
		this.invalidDate = invalidDate;
	}

	public Date getPreOpenTime() {
		return preOpenTime;
	}

	public void setPreOpenTime(Date preOpenTime) {
		this.preOpenTime = preOpenTime;
	}

	public void setPromFeeSn(String promFeeSn) {
		this.promFeeSn = promFeeSn;
	}

	public void setIsBankPay(String isBankPay) {
		this.isBankPay = isBankPay;
	}
	
	
}
