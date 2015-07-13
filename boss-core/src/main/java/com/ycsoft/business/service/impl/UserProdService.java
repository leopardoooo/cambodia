/**
 *
 */
package com.ycsoft.business.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.core.acct.CAcct;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CAcctBank;
import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserAtv;
import com.ycsoft.beans.core.user.CUserBroadband;
import com.ycsoft.beans.core.user.CUserDtv;
import com.ycsoft.beans.core.user.CUserStb;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.beans.prod.PProdUserRes;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.dto.core.acct.AcctAcctitemActiveDto;
import com.ycsoft.business.dto.core.acct.AcctAcctitemChangeDto;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.prod.CProdBacthDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.PProdDto;
import com.ycsoft.business.dto.core.prod.ProdDictDto;
import com.ycsoft.business.dto.core.prod.ProdListDto;
import com.ycsoft.business.dto.core.prod.ProdResDto;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.dto.core.prod.ResGroupDto;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.dto.core.user.UserProdRscDto;
import com.ycsoft.business.service.IUserProdService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.store.TemplateConfig.Template;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 * @author YC-SOFT
 *
 */
@Service
public class UserProdService extends BaseBusiService implements IUserProdService {
	private ExpressionUtil expressionUtil;

	public void saveOrder(String prodId, String tariffId, String feeDate,
				List<UserProdRscDto> dynamicRscList, String expDate, Date preOpenTime,String isBankPay) throws ServicesException {
		try{
			Integer doneCode = doneCodeComponent.gDoneCode();
			saveOrderBaseList(getBusiParam().getSelectedUsers(),doneCode,prodId,tariffId,feeDate,expDate,dynamicRscList,SystemConstants.PROD_ORDER_TYPE_ORDER,preOpenTime,isBankPay);
			List<ProdListDto> prodList = new ArrayList<ProdListDto>();
			ProdListDto dto = new ProdListDto();
			dto.setProdId(prodId);
			dto.setTariffId(tariffId);
			dto.setIsBankPay(isBankPay);
			prodList.add(dto);
			saveOrderProdDoneInfo(doneCode, prodList);
			saveAllPublic(doneCode,getBusiParam());
		}catch (Exception e) {
			throw new ServicesException(e);
		}
	}
	
	/**
	 * 提供给外部借口使用,没有预开通日期这个参数.
	 * @param prodId
	 * @param tariffId
	 * @param feeDate
	 * @param dynamicRscList
	 * @param expDate
	 */
	public void saveOrder(String prodId, String tariffId, String feeDate,
			List<UserProdRscDto> dynamicRscList, String expDate) throws Exception {
		Date preOpenTime = null;
		String isBankPay = SystemConstants.BOOLEAN_FALSE;
		saveOrder(prodId, tariffId, feeDate, dynamicRscList, expDate,preOpenTime,isBankPay);
	}

	public void saveOrderList(String pordLists) throws Exception {
		List<ProdListDto> prodList = null;
		if(StringHelper.isNotEmpty(pordLists)){
			Type type = new TypeToken<List<ProdListDto>>(){}.getType();
			prodList = JsonHelper.gson.fromJson( pordLists , type);
		}
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		List<ProdListDto> pldList = new ArrayList<ProdListDto>();
		
		for(ProdListDto dto:prodList){
			List<UserProdRscDto> dyResList = null;
			if(StringHelper.isNotEmpty(dto.getDynamicRscList())){
				Type type = new TypeToken<List<UserProdRscDto>>(){}.getType();
				Gson gson = new Gson();
				dyResList = gson.fromJson(dto.getDynamicRscList(), type);
			}
			ProdListDto pld = new ProdListDto();
			pld.setProdId(dto.getProdId());
			pld.setTariffId(dto.getTariffId());
			pld.setIsBankPay(dto.getIsBankPay());
			pldList.add(pld);
			saveOrderBaseList(getBusiParam().getSelectedUsers(),doneCode, dto.getProdId(), dto.getTariffId(), dto
					.getFeeDate(), dto.getExpDate(), dyResList,SystemConstants.PROD_ORDER_TYPE_ORDER,dto.getPreOpenTime(),dto.getIsBankPay());
		}
		saveOrderProdDoneInfo(doneCode, pldList);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void saveBatchOrder(List<String> userIdList, String prodId,
			String tariffId, String feeDate,
			List<UserProdRscDto> dynamicRscList, String expDate) throws Exception {
		BusiParameter parameter = getBusiParam();
		//操作员所在县市是否必须订购基本包
		String needBaseProd = userProdComponent.queryTemplateConfig(Template.NEED_BASE_PROD.toString());
		for(String userId : userIdList){
			UserDto user = userComponent.queryUserById(userId);
			if(user == null){
				throw new ServiceException("用户不存在,用户ID: " + userId);
			}
			
			//用户是否订购基本包
			if(SystemConstants.BOOLEAN_TRUE.equals(needBaseProd)){
				List<CProdDto> orderProdList = userProdComponent.queryByUserId(userId);
				if( orderProdList.size() == 0){
					throw new ServiceException("用户没订购基本包,用户ID: " + userId);
				}
			}
			
			expressionUtil.setCuser(user);
			//产品已订购
			CProd cprod = expressionUtil.orderPord(prodId);
			if(cprod != null){
				throw new ServiceException("产品 "+cprod.getProd_name()+" 已订购,用户ID: " + userId);
			}
			
			PProd prod = prodComponent.queryById(prodId);
			if(prod.getServ_id().equals(SystemConstants.PROD_SERV_ID_ITV)){
				if (!user.getUser_type().equals(SystemConstants.USER_TYPE_DTV)
						|| user.getServ_type().equals(
								SystemConstants.DTV_SERV_TYPE_SINGLE)) {
					throw new ServiceException("产品服务编号和用户类型不匹配,产品为数字双向产品,用户类型为"+user.getUser_type_text()+",用户ID: " + userId);
				}
			}else{
				if(!prod.getServ_id().equals(user.getUser_type())){
					throw new ServiceException("产品服务编号和用户类型不匹配,用户ID: " + userId);
				}
			}
			
			CCust cust = custComponent.queryCustById(user.getCust_id());
			expressionUtil.setCcust(cust);
			
			ProdTariffDto tariff = userProdComponent.queryTariffByTariffIds(new String[]{tariffId}).get(0);
			//资费不适用当前用户
			if (!expressionUtil.parseBoolean(tariff.getRule_id_text())) {
				throw new ServiceException("资费规则不适用,用户ID: " + userId);
			}
			
			
			List<CUser> userList = new ArrayList<CUser>();
			userList.add(user);
			
			CustFullInfoDto custFullInfo = new CustFullInfoDto();
			custFullInfo.setCust(cust);
			parameter.setCustFullInfo(custFullInfo);
			
			if(user.getUser_type().equals(SystemConstants.USER_TYPE_DTV)){
				List<CUserDtv> userDtvList = new ArrayList<CUserDtv>();
				CUserDtv userDtv = new CUserDtv();
				BeanUtils.copyProperties(user, userDtv);
				userDtvList.add(userDtv);
				parameter.setSelectedDtvs(userDtvList);
			}else if(user.getUser_type().equals(SystemConstants.USER_TYPE_ATV)){
				List<CUserAtv> userAtvList = new ArrayList<CUserAtv>();
				CUserAtv userAtv = new CUserAtv();
				BeanUtils.copyProperties(user, userAtv);
				userAtvList.add(userAtv);
				parameter.setSelectedAtvs(userAtvList);
			}else if(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
				List<CUserBroadband> userBandList = new ArrayList<CUserBroadband>();
				CUserBroadband userBand = new CUserBroadband();
				BeanUtils.copyProperties(user, userBand);
				userBandList.add(userBand);
				parameter.setSelectedBands(userBandList);
			}
			
			Integer doneCode = doneCodeComponent.gDoneCode();
			saveOrderBaseList(userList, doneCode, prodId, tariffId, feeDate,
					expDate, dynamicRscList, SystemConstants.PROD_ORDER_TYPE_ORDER,null,SystemConstants.BOOLEAN_FALSE);
			
			saveAllPublic(doneCode,parameter);
		}
		
	}
	
	/**
	 * 电视营业厅订购
	 * @param prodId
	 * @param tariffId
	 * @param feeDate
	 * @param fee
	 */
	public void saveTVOrder(String prodId, String tariffId, String feeDate,
			int fee) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		List<CUser> userList = getBusiParam().getSelectedUsers();
		
		//保存订购
		saveOrderBaseList(userList,doneCode,prodId,tariffId,feeDate,null,null,SystemConstants.PROD_ORDER_TYPE_TVORDER,null,SystemConstants.BOOLEAN_FALSE);
		
		CProd prod = userProdComponent.queryByProdId(userList.get(0).getUser_id(), prodId);
		if(prod != null){
			//获取公用账目信息
			CAcct acct = acctComponent.queryCustAcctByCustId(prod.getCust_id());
			//转账
			acctTrans(prod.getCust_id(), doneCode, busiCode,acct.getAcct_id(), SystemConstants.ACCTITEM_PUBLIC_ID,
					prod.getAcct_id(), prodId, fee);
			//创建销帐任务
			jobComponent.createCustWriteOffJob(doneCode, prod.getCust_id(),SystemConstants.BOOLEAN_TRUE);
			jobComponent.createAcctModeCalJob(doneCode, prod.getCust_id());
			prod = userProdComponent.queryByProdSn(prod.getProd_sn());
			//修改失效日期
			userProdComponent.updateExpDate(doneCode, prod.getProd_sn(),DateHelper.dateToStr(prod.getInvalid_date()));
			
			//保存订购信息
			userProdComponent.saveOrderInfo(prod.getUser_id(),prodId,tariffId,fee);
		}
		
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void saveOrderBaseList(List<CUser> userList,Integer doneCode, String prodId,
			String tariffId, String feeDate, String expDate,
			List<UserProdRscDto> dynamicRscList,String orderType, Date preOpenTime,String isBankPay) throws Exception {
		//获取客户用户信息
		CCust cust = getBusiParam().getCust();
		//List<CUser> userList = getBusiParam().getSelectedUsers();
		//获取业务流水
		String busiCode = getBusiParam().getBusiCode();
		
		//根据产品和资费id获取产品和资费的基本信息
		PProd prod = prodComponent.queryProdById(prodId);
		PProdTariff tariff = prodComponent.queryTariffById(tariffId);
		List<PProdUserRes> userResList = prodComponent.queryUserResByCountyId();
		//判断产品是否按到期日停机
		String stopByInvlaidDate = prodComponent.stopByInvaliddate(prod, tariff);
		//计算免费天数
		int freeAmount = DateHelper.getDifferDays(DateHelper.getDate("-"), feeDate);
		//判断产品是套餐还是基本产品,如果是套餐先保存套餐信息，再将套餐包含的子产品
		List<PPackageProd> packageProdList = null;
		if (prod.isPkg()){
			packageProdList = prodComponent.queryPackageProd(prodId,tariffId);
		}
		
		//模拟转数转的钱是否需要转回订购的产品对应的账目的标志位
		boolean needChange = false;
		List<AcctAcctitemChangeDto> acctitemChangeList = null;//模拟转数转到公用账目的异动记录
		List<CUserDtv> dtvList = getBusiParam().getSelectedDtvs();
		
		
		List<CProdDto> cProdList = userProdComponent
				.queryByUserIds(CollectionHelper.converValueToArray(dtvList,
						"user_id"));
		
		if(null != cProdList && cProdList.size() == 0){
			acctitemChangeList = acctComponent.queryAtvToDtvAcctitemChange(cust.getCust_id());
			if(null != acctitemChangeList){
				needChange = true;
			}
		}
		
		List<CAcctAcctitem> acctItemList = new ArrayList<CAcctAcctitem>();
		List<String> custIdList = new ArrayList<String>();
		//如果产品是基本产品，则直接保存产品信息
		//保存产品时需要判断产品是否有对应的动态资源，如果有则保存产品对应的动态资源
		String sn = null;
		for (CUser user:userList){
			if (prod.isPkg()){
				sn = userProdComponent.addPackage(doneCode,cust.getCust_id(), user.getAcct_id(), user.getUser_id()
						, prodId,prod.getProd_type(),orderType , feeDate, expDate,
						user.getStop_type(),tariff,packageProdList,dynamicRscList,stopByInvlaidDate,prod.getIs_base(),preOpenTime,isBankPay);

			} else {
				sn = userProdComponent.addProd(doneCode,cust.getCust_id(), user.getAcct_id(), user.getUser_id(),
						null,null, prodId,prod.getProd_type(), orderType, feeDate, expDate,
						user.getStop_type(),tariff,dynamicRscList,stopByInvlaidDate,prod.getIs_base(),preOpenTime,isBankPay);
			}
			expressionUtil.setCcust(cust);
			expressionUtil.setCuser(user);
			for (PProdUserRes userRes:userResList){
				if (userRes.getProd_id().equals(prod.getProd_id())){
					if (StringHelper.isEmpty(userRes.getRule_id_text()) 
							|| expressionUtil.parseBoolean(userRes.getRule_id_text())){
						String[] res = userRes.getRes_id().split(",");
						for (String resId:res){
							userProdComponent.addUserProdres(sn, resId);
						}
					}
				}
			}
			
//			if (freeAmount>0){
//				userProdComponent.addProdRscAcct(doneCode, sn, SystemConstants.BILLING_TYPE_DAY,freeAmount);
//			}
			
			/*
			if(needChange){//需要从公用账目转到新订购产品创建的账目
				acctTrans(cust.getCust_id(), doneCode, busiCode, acctitemChange.getAcct_id(), acctitemChange.getAcctitem_id(), acctitem.getAcct_id(), acctitem.getAcctitem_id(), acctitemChange.getChange_fee());
				needChange = false;
			}*/
			
			//为新订购的产品创建账目
			CAcctAcctitem acctitem = acctComponent.createAcctItem(user.getAcct_id(), prod.getProd_id());
			if(needChange){
				for(AcctAcctitemChangeDto acctitemChange : acctitemChangeList){
					if(acctitemChange.getUser_id().equals(user.getUser_id())){
						acctTrans(cust.getCust_id(), doneCode, busiCode,
								acctitemChange.getAcct_id(), acctitemChange
										.getAcctitem_id(), acctitem
										.getAcct_id(), acctitem
										.getAcctitem_id(), acctitemChange
										.getChange_fee());
					}
				}
			}
			
			//数字电视订购基本包时，查询是否有回退订购，账目异动作废金额小于零
			if (prod.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)
					&& prod.getServ_id().equals(SystemConstants.USER_TYPE_DTV)) {
				AcctAcctitemChangeDto changeDto = acctComponent
						.queryOrderZFAcctitemChange(user.getUser_id(), cust.getCust_id());
				if(null != changeDto){
					int changeFee = changeDto.getChange_fee()*-1;
					int refundBalance = 0;
					int transBalance = 0;
					if(prod.getRefund().equals(SystemConstants.BOOLEAN_TRUE)){
						refundBalance = changeFee;
					}
					if(prod.getTrans().equals(SystemConstants.BOOLEAN_TRUE)){
						transBalance = changeFee;
					}
					acctComponent.updateActiveBanlance(acctitem.getAcct_id(),
							acctitem.getAcctitem_id(), changeFee, 0, refundBalance,
							transBalance);
				}
			}
			
//  modify after compare


			if(null != preOpenTime){//如果预开通不为空，则产品状态设置为 预开通 ，不发授权开通指令（数字和宽带），并生成预开通JOB等待执行
				//生成预开通JOB等待执行
				jobComponent.createPreAuthCmdJob(doneCode,sn,preOpenTime,user.getArea_id(),user.getCounty_id());
			}else{
				if(busiCode.equals(BusiCodeConstants.BATCH_PROD_ORDER)){
					//批量订购产品 授权等级为20
					jobComponent.createBusiCmdJob(doneCode,
							BusiCmdConstants.ACCTIVATE_PROD, cust.getCust_id(),
							user.getUser_id(), user.getStb_id(), user.getCard_id(),
							user.getModem_mac(), sn, prod.getProd_id(), null,
							SystemConstants.PRIORITY_DSSQ);
				}else{
					jobComponent.createBusiCmdJob(doneCode,
							BusiCmdConstants.ACCTIVATE_PROD, cust.getCust_id(),
							user.getUser_id(), user.getStb_id(), user.getCard_id(),
							user.getModem_mac(), sn, prod.getProd_id());
				}
			}

			acctItemList.add(acctitem);
			
			String custId = user.getCust_id();
			if(StringHelper.isNotEmpty(custId) && !custIdList.contains(custId)){
				custIdList.add(custId);
			}
			
		}
		if(needChange)needChange = false;
		jobComponent.createCreditCalJob(doneCode, cust.getCust_id(), acctItemList,SystemConstants.BOOLEAN_FALSE);
		
		if(custIdList.size() > 0){
			String[] custIds = custIdList.toArray(new String[custIdList.size()]);
			jobComponent.createAcctModeCalJobByCustIds(doneCode, custIds);
			jobComponent.createInvalidCalJobByCustIds(doneCode, custIds);
		}
	}


	public void saveOrderCustPkg(String prodId, String tariffId, String feeDate,String stopType,String[] prodSns) throws Exception{
		//获取客户用户信息
		CCust cust = getBusiParam().getCust();
		CAcct acct = acctComponent.queryCustAcctByCustId(cust.getCust_id());
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		String custId = getBusiParam().getCust().getCust_id();
		//根据产品和资费id获取产品和资费的基本信息
		PProd prod = prodComponent.queryProdById(prodId);
		PProdTariff tariff = prodComponent.queryTariffById(tariffId);
		//计算免费天数
//		int freeAmount = DateHelper.getDifferDays(DateHelper.getDate("-"), feeDate);
		String stopByInvlaidDate = prodComponent.stopByInvaliddate(prod, tariff);
		String sn = userProdComponent.addProd(doneCode,cust.getCust_id(), acct.getAcct_id(), null,
					null,null, prodId,prod.getProd_type(), SystemConstants.PROD_ORDER_TYPE_ORDER, feeDate, null,stopType,
					tariff,null,stopByInvlaidDate,prod.getIs_base(),null,SystemConstants.BOOLEAN_FALSE);
//
//		if (freeAmount>0){
//			userProdComponent.addProdRscAcct(doneCode, sn, SystemConstants.BILLING_TYPE_DAY,freeAmount);
//		}
		//为新订购的产品创建账目
		acctComponent.createAcctItem(acct.getAcct_id(), prod.getProd_id());
		//更新用户产品信息
		for (String prodSn:prodSns){
			addProdToPkg(doneCode, prod.getProd_id(), sn, prodSn);
			
			CProd cprod = userProdComponent.queryByProdSn(prodSn);
			CUser user = userComponent.queryUserById(cprod.getUser_id());
			recoverUserStatus(user, doneCode);
			
			//单产品属于客户套餐时，到期日为客户套餐到期日加本身账目余额的到期日
//			userProdComponent.updateInvalidDate(doneCode, prodSn, DateHelper.strToDate(feeDate));
			
//			//将产品对应的账目余额转入套餐账目
//			CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(cProd.getAcct_id(), cProd.getProd_id());
//			acctTrans(cust.getCust_id(), doneCode, busiCode,cProd.getAcct_id(), cProd.getProd_id(), acct.getAcct_id(), prod.getProd_id(), acctItem.getCan_trans_balance());
//			//删除用户产品对应的账目
//			acctComponent.removeAcctItemWithoutHis(cProd.getAcct_id(), cProd.getProd_id());
		}
		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_TRUE);
		jobComponent.createAcctModeCalJob(doneCode, custId);
		jobComponent.createInvalidCalJob(doneCode, custId);
		saveAllPublic(doneCode,getBusiParam());
	}

	private String terminate(Integer doneCode, String[] prodSns,
			String banlanceDealType, String transAcctId, String transAcctItemId)
			throws Exception {
		//获取客户用户信息
		String custId = getBusiParam().getCust().getCust_id();
		CUser user = getBusiParam().getSelectedUsers().get(0);
		
		//获取业务流水
		String busiCode = getBusiParam().getBusiCode();
		String busiInfo = "";
		//生成销帐任务
		int jobId = jobComponent.createCustWriteOffJob(doneCode, custId,SystemConstants.BOOLEAN_TRUE);
		//终止产品
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (String prodSn:prodSns){
			CProd prod = userProdComponent.queryByProdSn(prodSn);
			PProd p = userProdComponent.queryByProdId(prod.getProd_id());
			prod.setProd_name(p.getProd_name());
			List<ProdTariffDto> tariffs = userProdComponent.queryTariffByTariffIds(new String[]{prod.getTariff_id()});
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("prod_name", p.getProd_name());
			if(CollectionHelper.isNotEmpty(tariffs)){
				map.put("tariff_name", tariffs.get(0).getTariff_name());
			}
			list.add(map);
			terminateProd(custId,user, doneCode, busiCode, prod, banlanceDealType, transAcctId, transAcctItemId);
			//保存需要终止的账目
			jobComponent.terminateAcct(jobId, prod.getAcct_id(), prod.getProd_id(),doneCode);
		}
		getBusiParam().getBusiConfirmParamInfo().put("prod_list", list);
		getBusiParam().getBusiConfirmParamInfo().put("user", user);
		doneCodeComponent.saveDoneCodeInfo(doneCode, custId, user.getUser_id(), getBusiParam().getBusiConfirmParamInfo());
		return busiInfo;
	}
	
	/**
	 * 终止产品
	 * @param prodSns
	 * @param banlanceDealType
	 * @throws Exception
	 * 20120401 by wang 退订时候，如何是套餐缴费的话，记录套餐信息
	 */
	public void saveTerminate(String[] prodSns,String banlanceDealType,String transAcctId,String transAcctItemId,String promFeeSn) throws Exception{
		Integer doneCode = doneCodeComponent.gDoneCode();
		if(StringHelper.isNotEmpty(promFeeSn)){
			CAcctAcctitem acctItem = new CAcctAcctitem();
			//获取账目信息
			if(StringHelper.isNotEmpty(transAcctId) && StringHelper.isNotEmpty(transAcctItemId)){
				acctItem = acctComponent.queryAcctItemByAcctitemId(transAcctId,transAcctItemId);
			}
			userProdComponent.savePromProdRefund(promFeeSn, prodSns, doneCode,acctItem.getCan_refund_balance());
		}
		terminate(doneCode, prodSns, banlanceDealType, transAcctId, transAcctItemId);
		jobComponent.createAcctModeCalJob(doneCode, getBusiParam().getCust().getCust_id());
		//账务模式，重新计算到期日
		jobComponent.createInvalidCalJob(doneCode, getBusiParam().getCust().getCust_id());
//		saveAllPublic(doneCode,getBusiParam(),busiInfo);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	
	public void changeBandProd(String prodId, String tariffId, String feeDate,
			String expDate, String oldProdSn, int presentFee) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String custId = getBusiParam().getCust().getCust_id();
		
		CProd oldProd = userProdComponent.queryByProdSn(oldProdSn);		//原产品
		CUser user = userComponent.queryUserById(oldProd.getUser_id());	//前台用户记录有时未选中 UserPanel
		List<CUserBroadband> userBandList = new ArrayList<CUserBroadband>();
		CUserBroadband userBand = new CUserBroadband();
		BeanUtils.copyProperties(user, userBand);
		userBandList.add(userBand);
		getBusiParam().setSelectedBands(userBandList);
		
		Map<String, Object> bu = new HashMap<String, Object>();
		bu.put("net_type", userBand.getNet_type_text());
		bu.put("login_name", userBand.getLogin_name());
		bu.put("modem_mac", userBand.getModem_mac());
		bu.put("bind_type", userBand.getBind_type_text());
		
		getBusiParam().setBusiConfirmParam("bandUser", bu);
		/*原资费相关信息*/
		String oldProdId = oldProd.getProd_id();
		List<AcctAcctitemActiveDto> oldAcctItemActiveList = acctComponent.queryActiveById(user.getAcct_id(), oldProdId);
		CAcctAcctitem oldAcctItem = acctComponent.queryAcctItemByAcctitemId(user.getAcct_id(), oldProdId);
		PProdTariff oldTariff = userProdComponent.queryProdTariffById(oldProd.getTariff_id());
		List<CAcctAcctitemInactive> oldInactiveList = acctComponent.queryInactive(user.getAcct_id(), oldProdId);
		PProd oldP = userProdComponent.queryByProdId(oldProdId);
		Map<String, String> oldMap = new HashMap<String, String>();
		oldMap.put("prod_name", oldP.getProd_name());
		oldMap.put("tariff_name", oldTariff.getTariff_name());
		getBusiParam().setBusiConfirmParam("old_prod", oldMap);
		
		
		//订购新产品
		saveOrderBaseList(getBusiParam().getSelectedUsers(),doneCode,prodId,tariffId,feeDate,expDate,null,SystemConstants.PROD_ORDER_TYPE_ORDER,null,SystemConstants.BOOLEAN_FALSE);
		
		/*新资费相关信息*/
		CAcctAcctitem newAcctItem = acctComponent.queryAcctItemByAcctitemId(user.getAcct_id(), prodId);
		CProd newProd = userProdComponent.queryByAcctItem(user.getAcct_id(), prodId);
		PProdTariff newTariff = userProdComponent.queryProdTariffById(tariffId);
		Map<String, Object> newMap = new HashMap<String, Object>();
		PProd newP = userProdComponent.queryByProdId(prodId);
		newMap.put("prod_name", newP.getProd_name());
		newMap.put("tariff_name", newTariff.getTariff_name());
		String prod_desc = newP.getProd_desc();
		prod_desc = StringHelper.isEmpty(prod_desc) ? "":prod_desc;
		prod_desc = prod_desc.replaceAll("\n", "</br>").replaceAll("\r", "").replaceAll("\"", "") ;
		int times = ( prod_desc.length() / 64 ) +  ( prod_desc.length() % 64 > 0 ? 1: 0 );
		List<String> prod_descs = new ArrayList<String>();
		for(int index =0;index<times;index++){
			int start = index * 64;
			int limit = start + 64;
			limit = limit > prod_desc.length() ? prod_desc.length() : limit; 
			String sub = prod_desc.substring(start,limit );
			prod_descs.add(sub);
		}
		newMap.put("prod_desc", prod_desc);
		newMap.put("prod_descs", prod_descs);
		getBusiParam().setBusiConfirmParam("new_prod", newMap);
		doneCodeComponent.saveDoneCodeInfo(doneCode, custId, user.getUser_id(), getBusiParam().getBusiConfirmParamInfo());
		//非现金余额赠送			现金余额
		int activeUnCashBalance = 0, activeCashBalance = 0, transFee = 0, cashNoTransFee = 0, realBill = 0, inactiveFee = 0;
		
		for(AcctAcctitemActiveDto active : oldAcctItemActiveList){
			if(active.getIs_cash().equals(SystemConstants.BOOLEAN_FALSE)){
				activeUnCashBalance += active.getBalance().intValue();
			}else{
				activeCashBalance += active.getBalance().intValue();
			}
			
			//不是现金金额且能转账的，需要扣除，因为退订时已经转账过去了
			if(active.getIs_cash().equals(SystemConstants.BOOLEAN_FALSE)
					&& active.getCan_trans().equals(SystemConstants.BOOLEAN_TRUE)){
				transFee += active.getBalance().intValue();
			}
			
			//现金且不能转账的，赠送进新账目中
			if(active.getIs_cash().equals(SystemConstants.BOOLEAN_TRUE)
					&& active.getCan_trans().equals(SystemConstants.BOOLEAN_FALSE)){
				cashNoTransFee += active.getBalance().intValue();
			}
		}
		realBill = oldAcctItem.getReal_bill().intValue();	//原产品本月费用
		
//		if(activeCashBalance > 0 && activeUnCashBalance == 0){	//只有现金余额
//			//有现金余额，原账目本月费用已除去，其余金额已作可转余额转到新账目中
//			realBill = 0;
//		}
		
		for(CAcctAcctitemInactive inactive : oldInactiveList){
			inactiveFee += inactive.getBalance().intValue();
		}
		
		//presentFee 根据资费不同，赠送金额
		int activeBalance = activeUnCashBalance + inactiveFee - transFee + cashNoTransFee;
		if(realBill>0){
			if(activeCashBalance>realBill)
				activeCashBalance-=realBill;
			else{
				activeCashBalance=0;
				activeBalance-=realBill;
			}
			if(activeBalance<0)
				throw new ComponentException("请先付清原欠费金额！");
		}
		activeBalance+=presentFee;
		if(activeBalance > 0){
			acctComponent.changeAcctItemBanlance(doneCode, BusiCodeConstants.BAND_CHANG_PROD, newProd.getCust_id(), 
					newProd.getAcct_id(), newProd.getProd_id(), SystemConstants.ACCT_CHANGE_TRANS, SystemConstants.ACCT_FEETYPE_PRESENT, 
					activeBalance, null);
			
		}else{
			//创建欠费账单
			String billingCycle = DateHelper.format(new Date(), DateHelper.FORMAT_YM);
			billComponent.createBill(newProd, doneCode, billingCycle,
					activeBalance * -1, activeBalance * -1, SystemConstants.BILL_COME_FROM_AUTO);
			acctComponent.changeAcctItemOwefee(false, user.getAcct_id(), prodId, activeBalance * -1);
		}
		//原现金转入新账户
		if(activeCashBalance>0)
			acctComponent.changeAcctItemBanlance(doneCode, BusiCodeConstants.BAND_CHANG_PROD,
					 newProd.getCust_id(), newProd.getAcct_id(), newProd.getProd_id(),
				SystemConstants.ACCT_CHANGE_PAY , SystemConstants.ACCT_FEETYPE_CASH, activeCashBalance, null);
		
		//作废原账目欠费账单
		if(oldAcctItem.getReal_balance().intValue() < 0 
				&& oldAcctItem.getOrder_balance().intValue() < oldAcctItem.getReal_balance().intValue() * -1){
			List<BBill> oweBillList = billComponent.queryOweBillByProdSn(oldProdSn);
			for(BBill bill : oweBillList){
				billComponent.cancelBill(bill.getBill_sn());
			}
		}
		/*if(oldTariff.getBilling_cycle()>1 && newTariff.getBilling_cycle()>1){
				billComponent.updateBillInfo(oldProdSn, newProd.getProd_sn(), newProd.getTariff_id(), 
						newProd.getAcct_id(), newProd.getProd_id());
				newAcctItem = acctComponent.queryAcctItemByAcctitemId(newProd.getAcct_id(), newProd.getProd_id());
				int balance = newAcctItem.getActive_balance() + oldAcctItem.getActive_balance();
				int oweFee = balance > newTariff.getRent() ? newTariff.getRent() : balance;
				acctComponent.changeAcctItemOwefee(true, newProd.getAcct_id(), newProd.getProd_id(), oweFee);
		}*/
		
		//退订原产品,将余额转账到新产品中
		terminate(doneCode, new String[] { oldProdSn },
				SystemConstants.ACCT_BALANCE_TRANS, newAcctItem.getAcct_id(),
				newAcctItem.getAcctitem_id());
		
		if(newTariff.getBilling_cycle() > 1){
			//修改新资费产品到期日和原资费产品到期日一样
			userProdComponent.updateInvalidDate(doneCode, newProd.getProd_sn(), oldProd.getInvalid_date());
			newAcctItem = acctComponent.queryAcctItemByAcctitemId(newProd.getAcct_id(), newProd.getProd_id());
			//作废原有账单
			billComponent.cancelBill(oldProd.getProd_sn(), DateHelper.nowYearMonth());
//			if(oldTariff.getBilling_cycle() == 1){
				//出账剩下还未出账的所有余额
				int balance = newAcctItem.getActive_balance() - newAcctItem.getOwe_fee();
				
				if(balance>0){
					int monthCycles=balance/newTariff.getRent();
					if(monthCycles>0){
						throw new ComponentException("数据异常，请与管理员联系");
					}
					int monthFee = newTariff.getRent()/newTariff.getBilling_cycle();
					//余额小于月租，将金额累加到当前未出帐账单上
					if(balance < monthFee && oldTariff.getBilling_cycle() == 1){
						billComponent.updateMuchBill(newProd.getProd_sn(), balance, DateHelper.nowYearMonth());
						acctComponent.changeAcctItemOwefee(true, newAcctItem.getAcct_id(), newAcctItem.getAcctitem_id(), balance);
					}else{
						int oweFee = balance;
						Date invalidDate = newProd.getInvalid_date();
						monthFee=(int)(newTariff.getRent()*1.0/newTariff.getBilling_cycle());
						int monthIndex = 0;
						while(balance>0){
							int billFee = monthFee;
							//按到期日来判断开始账单的账期
							String billingCycle = DateHelper.format(DateHelper.getNextMonthByNum(invalidDate,monthIndex), DateHelper.FORMAT_YM);
							if(monthFee >= balance){
								billFee = balance;
								balance = 0;
							}else{
								balance = balance - billFee;
							}
							billComponent.createBill(newProd, doneCode, billingCycle, billFee, billFee, SystemConstants.BILL_COME_FROM_MUCH);
							monthIndex++;
						}
						acctComponent.changeAcctItemOwefee(true, newAcctItem.getAcct_id(), newAcctItem.getAcctitem_id(), oweFee);
					}
				}
		}
		userProdComponent.updateInvalidDate(doneCode,  newProd.getProd_sn(),0, activeUnCashBalance+activeCashBalance, null);
		
		acctComponent.saveBandUpgradeInfo(doneCode,
				BusiCodeConstants.BAND_CHANG_PROD, custId, user.getUser_id(),
				newAcctItem.getAcct_id(), oldProd.getProd_id(), prodId, oldProd.getTariff_id(), 
				tariffId, oldAcctItem, oldAcctItemActiveList, inactiveFee+presentFee);
		
		jobComponent.createCreditCalJob(doneCode, custId, null, SystemConstants.BOOLEAN_TRUE);
		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_TRUE);
		jobComponent.createCreditExecJob(doneCode, custId);
		jobComponent.createInvalidCalJob(doneCode, custId);
		jobComponent.createAcctModeCalJob(doneCode, custId);
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void changeProdDynRes(String prodSn,List<UserProdRscDto> dyResList) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String  custId = getBusiParam().getCust().getCust_id();
		
		CProd prod = userProdComponent.queryByProdSn(prodSn);
		UserDto user = userComponent.queryUserById(prod.getUser_id());
		
		//用户下除旧产品外的所有资源
		List<String> oldResIds = userProdComponent.queryResByUserId(user.getUser_id(), prod.getProd_id());
		List<String> oldProdResIds = userProdComponent.queryUserProdRes(prodSn);		//旧产品资源
		//旧产品资源在用户下其他产品不存在，则发减授权,否则不发减授权
		for(String oldProdResId : oldProdResIds){
			boolean flag = true;
			for(String oldResId : oldResIds){
				if(oldProdResId.equals(oldResId)){
					flag = false;
					break;
				}
			}
			if(flag){
				//减授权
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD,
						custId, user.getUser_id(), user.getStb_id(), user.getCard_id(),
						user.getModem_mac(), prod.getProd_sn(), prod.getProd_id(),
						"RES_ID:''"+oldProdResId+"''");
			}
		}
		
		//更换资源
		userProdComponent.changeProdDynRes(prodSn, dyResList);
		
		//加授权
		jobComponent.createBusiCmdJob(doneCode,BusiCmdConstants.ACCTIVATE_PROD, 
				custId, user.getUser_id(), user.getStb_id(), user.getCard_id(), 
				user.getModem_mac(), prod.getProd_sn(), prod.getProd_id(), 
				JsonHelper.fromObject(user));
		
		jobComponent.createCreditCalJob(doneCode, custId, null, SystemConstants.BOOLEAN_TRUE);
		
		if(getBusiParam().getSelectedUserIds().size() == 0){
			if(user.getUser_type().equals(SystemConstants.USER_TYPE_ATV)){
				List<CUserAtv> userList = new ArrayList<CUserAtv>();
				CUserAtv atv = new CUserAtv();
				BeanUtils.copyProperties(user, atv);
				userList.add(atv);
				getBusiParam().setSelectedAtvs(userList);
			}else if(user.getUser_type().equals(SystemConstants.USER_TYPE_DTV)){
				List<CUserDtv> userList = new ArrayList<CUserDtv>();
				CUserDtv dtv = new CUserDtv();
				BeanUtils.copyProperties(user, dtv);
				userList.add(dtv);
				getBusiParam().setSelectedDtvs(userList);
			}else if(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
				List<CUserBroadband> userList = new ArrayList<CUserBroadband>();
				CUserBroadband band = new CUserBroadband();
				BeanUtils.copyProperties(user, band);
				userList.add(band);
				getBusiParam().setSelectedBands(userList);
			}
		}
		saveAllPublic(doneCode, getBusiParam());
	}
	
	
	public void saveBatchCancel(List<String> userIdList, String prodId) throws Exception {
		BusiParameter parameter = getBusiParam();
		for(String userId : userIdList){
			UserDto user = userComponent.queryUserById(userId);
			if(user == null){
				throw new ServiceException("用户不存在,用户ID: " + userId);
			}
			
			expressionUtil.setCuser(user);
			CProd prod = expressionUtil.orderPord(prodId);
			if(prod == null){
				throw new ServiceException("未订购该产品,用户ID: " + userId);
			}
			
			//如果是基本包，且用户下无其他基本包，则不能退订
			if(prod.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)){
				boolean flag = true;
				List<CProdDto> prodList = userProdComponent.queryByUserId(userId);
				for(CProdDto prodDto : prodList){
					if (!prodId.equals(prodDto.getProd_id())
							&& prodDto.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)) {
						flag = false;
						break;
					}
				}
				if(flag){
					throw new ServiceException("该产品为基本包,用户下已无基本包,无法退订,用户ID: " + userId);
				}
			}
			
			AcctitemDto acctitem = acctComponent.queryAcctItemByUserId(user.getUser_id(), prodId);
			if (acctitem.getReal_balance().intValue() < 0
					&& acctitem.getOrder_balance().intValue() != acctitem.getReal_balance().intValue() * -1) {
				throw new ServiceException("用户账目欠费,无法退订,用户ID: " + userId);
			}
			
			Integer doneCode = doneCodeComponent.gDoneCode();
			
			CustFullInfoDto custFullInfo = new CustFullInfoDto();
			custFullInfo.setCust(custComponent.queryCustById(user.getCust_id()));
			parameter.setCustFullInfo(custFullInfo);
			
			String userType = user.getUser_type();
			if(userType.equals(SystemConstants.USER_TYPE_DTV)){
				List<CUserDtv> userDtvList = new ArrayList<CUserDtv>();
				CUserDtv userDtv = new CUserDtv();
				BeanUtils.copyProperties(user, userDtv);
				userDtvList.add(userDtv);
				parameter.setSelectedDtvs(userDtvList);
			}else if(userType.equals(SystemConstants.USER_TYPE_ATV)){
				List<CUserAtv> userAtvList = new ArrayList<CUserAtv>();
				CUserAtv userAtv = new CUserAtv();
				BeanUtils.copyProperties(user, userAtv);
				userAtvList.add(userAtv);
				parameter.setSelectedAtvs(userAtvList);
			}else if(userType.equals(SystemConstants.USER_TYPE_BAND)){
				List<CUserBroadband> userBandList = new ArrayList<CUserBroadband>();
				CUserBroadband userBand = new CUserBroadband();
				BeanUtils.copyProperties(user, userBand);
				userBandList.add(userBand);
				parameter.setSelectedBands(userBandList);
			}
			
			//批量退订，账目余额默认作废掉
			String busiInfo = terminate(doneCode, new String[] { prod.getProd_sn() }, 
					SystemConstants.ACCT_BALANCE_EXPIRE, "", "");
			//账务模式，重新计算到期日
			jobComponent.createInvalidCalJob(doneCode, user.getCust_id());
//			saveAllPublic(doneCode,getBusiParam(),busiInfo);
			saveAllPublic(doneCode,getBusiParam());
		}
	}

	/**
	 * 终止套餐
	 * @param prodSns
	 * @param banlanceDealType
	 * @throws Exception
	 */
	public void saveTerminatePkg(String prodSn,String banlanceDealType,String transAcctId,String transAcctItemId) throws Exception{
		//获取客户用户信息
		String custId = getBusiParam().getCust().getCust_id();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		//生成销帐任务
		int jobId = jobComponent.createCustWriteOffJob(doneCode, custId,SystemConstants.BOOLEAN_TRUE);
		//更新套餐对应的套餐信息为空
		List<CProd> prodList = userProdComponent.queryByPkgSn(prodSn);
		for (CProd cp:prodList){
			removeProdFromPkg(doneCode, cp);
		}
		//终止套餐
		CProd prod = userProdComponent.queryByProdSn(prodSn);
		terminateProd(custId,null, doneCode, busiCode, prod, banlanceDealType, transAcctId, transAcctItemId);
		//保存需要终止的账目
		jobComponent.terminateAcct(jobId, prod.getAcct_id(), prod.getProd_id(),doneCode);
		jobComponent.createAcctModeCalJob(doneCode, custId);
		//客户到期日计算任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		//保存流水
//		saveAllPublic(doneCode,getBusiParam(),prod.getProd_name());
		getBusiParam().setBusiConfirmParam("prod_names", prod.getProd_name());
		getBusiParam().setBusiConfirmParam("prods", prod);
		
		saveAllPublic(doneCode,getBusiParam());
	}

	/**
	 * 修改产品
	 * @param propChangeList
	 * @throws Exception
	 */
	public void saveEditProd(String prodSn , List<CProdPropChange> propChangeList) throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		userProdComponent.editProd(doneCode, prodSn, propChangeList);
		
		//保存流水
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void saveEditCustPkg(String pkgSn,String stopType, String[] prodSns)
			throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//查找客户套餐信息
		CProd pkg = userProdComponent.queryByProdSn(pkgSn);
		if(StringHelper.isNotEmpty(stopType) && !stopType.equals(pkg.getStop_type())){
			CProdPropChange propChange = new CProdPropChange();
			propChange.setColumn_name("stop_type");
			propChange.setOld_value(pkg.getStop_type());
			propChange.setNew_value(stopType);

			List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
			changeList.add(propChange);
			userProdComponent.editProd(doneCode, pkgSn, changeList);
		}
		//查找套餐对应的用户产品
		List<CProd> prodList = userProdComponent.queryByPkgSn(pkgSn);
		//增加新增的用户产品
		boolean flag = false;
		//循环新的子产品prodSns，查找还未加入套餐的，
		for(String prodSn:prodSns){
			flag =false;
			for (CProd userProd:prodList){
				if (prodSn.equals(userProd.getProd_sn())){//产品已经在套餐中
					flag=true;
					break;
				}
			}
			
			//不存在套餐中时，
			if (!flag){
				this.addProdToPkg(doneCode, pkg.getProd_id(), pkg.getProd_sn(), prodSn);
				
				//子产品到期日在本身基础上加上套餐的免费日子
				int day = DateHelper.getDiffDays(new Date(), pkg.getInvalid_date());
				CProd prod = userProdComponent.queryByProdSn(prodSn);
				Date newInvalidDate = DateHelper.addDate(prod.getInvalid_date(), day);
				userProdComponent.updateInvalidDate(doneCode, prodSn, newInvalidDate);
				
				CUser user = userComponent.queryUserById(prod.getUser_id());
				recoverUserStatus(user, doneCode);
			}
				
		}
		//从套餐移出产品
		//循环已经存在的子产品，去除不属于prodSns的产品
		for (CProd userProd:prodList){
			flag =false;
			for(String prodSn:prodSns){
				if (prodSn.equals(userProd.getProd_sn())){//产品已经在套餐中
					flag=true;
					break;
				}
			}
			
			if (!flag)
				this.removeProdFromPkg(doneCode, userProd);
		}
		
		//生成销账任务
		jobComponent.createCustWriteOffJob(doneCode, pkg.getCust_id(), SystemConstants.BOOLEAN_TRUE);
		jobComponent.createAcctModeCalJob(doneCode, pkg.getCust_id());
		//客户到期日计算任务
		jobComponent.createInvalidCalJob(doneCode, pkg.getCust_id());
		
		//保存流水
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 变更银行扣费
	 * @param prodSn
	 * @throws Exception
	 */
	public void changeCprodBank(String prodSn) throws Exception {
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//获取产品信息
		CProd cProd = userProdComponent.queryByProdSn(prodSn);
		PProd prod = prodComponent.queryProdById(cProd.getProd_id());
		UserDto user = userComponent.queryUserById(cProd.getUser_id());
		
		CProdPropChange propChange = new CProdPropChange();
		propChange.setColumn_name("is_bank_pay");
		propChange.setOld_value(cProd.getIs_bank_pay());
		propChange.setNew_value("T".equals(cProd.getIs_bank_pay())?"F":"T");
		List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
		changeList.add(propChange);
		userProdComponent.editProd(doneCode, prodSn, changeList);
		//保存流水
		propChange.setOld_value_text(MemoryDict.getDictName(DictKey.BOOLEAN, propChange.getOld_value()));
		propChange.setNew_value_text(MemoryDict.getDictName(DictKey.BOOLEAN, propChange.getNew_value()));
		
		getBusiParam().setBusiConfirmParam("changeInfo", propChange);
		getBusiParam().setBusiConfirmParam("user", user);
		getBusiParam().setBusiConfirmParam("prod", prod);
		
		doneCodeComponent.saveDoneCodeInfo(doneCode, cProd.getCust_id() ,user.getUser_id(), getBusiParam().getBusiConfirmParamInfo() );
		
		List<CAcctAcctitem> acctItems = new ArrayList<CAcctAcctitem>();
		CAcctAcctitem item = new CAcctAcctitem();
		item.setAcct_id(cProd.getAcct_id());
		item.setAcctitem_id(cProd.getProd_id());
		acctItems.add(item);
		//生成计算信用度任务
		jobComponent.createCreditCalJob(doneCode, user.getCust_id(), acctItems ,SystemConstants.BOOLEAN_TRUE);
				
		
		saveAllPublic(doneCode, getBusiParam());
	}
	
	
	/**
	 * 将子产品从客户套餐走移走
	 * @param pkgSn
	 * @param prodSn
	 * @throws Exception
	 */
	public void saveEditCustPkg(String prodSn) throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//查找客户套餐信息
		CProd subProd = userProdComponent.queryByProdSn(prodSn);
		if(null != subProd){
			//移出套餐，并计算到期日
			this.removeProdFromPkg(doneCode, subProd);
			
			//修改产品状态
			if(!StatusConstants.ACTIVE.equals(subProd.getStatus())){
				List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
				changeList.add(new CProdPropChange("status",
						subProd.getStatus(),StatusConstants.ACTIVE));
				changeList.add(new CProdPropChange("status_date",
						DateHelper.dateToStr(subProd.getStatus_date()),DateHelper.dateToStr(new Date())));
				
				userProdComponent.editProd(doneCode,subProd.getProd_sn(),changeList);
				
				//生成激活产品任务
				if (!isProdOpen(subProd.getStatus())){
					CUser user = userComponent.queryUserById(subProd.getUser_id());
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, user.getCust_id(),
						user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), subProd.getProd_sn(),subProd.getProd_id());
				}
			}
		}
		
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, subProd.getCust_id());
		
		//保存流水
		saveAllPublic(doneCode,getBusiParam());
	}


	public void editInvalidDate(String prodSn,String invalidDate) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		CProd cProd = userProdComponent.queryByProdSn(prodSn);
		String stauts = cProd.getStatus();
		//修改到期日
		userProdComponent.editInvalidDate(doneCode, cProd, DateHelper.strToDate(invalidDate));
		
		//用户产品非正常状态，需发授权
		if (!isProdOpen(stauts)){
			CUser user = userComponent.queryUserById(cProd.getUser_id());
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, user.getCust_id(),
					user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), prodSn,cProd.getProd_id());
		}
		jobComponent.createInvalidCalJob(doneCode, cProd.getCust_id());
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void changeTariff(String prodSn, String newTariffId, String effDate,String expDate,boolean isUpdate)
			throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		changeTariff(prodSn, newTariffId, effDate, expDate,true,isUpdate, doneCode);
		//生成业务信息
//		String busiInfo = "";
		PProdTariff newTariff = prodComponent.queryTariffById(newTariffId);
		PProd prod = prodComponent.queryProdByProdSn(prodSn);
//		busiInfo +="产品 "+prod.getProd_name()+" 新资费 "+newTariff.getTariff_name()+effDate+"生效";
		getBusiParam().setBusiConfirmParam("prod", prod);
		getBusiParam().setBusiConfirmParam("new_tariff", newTariff);
		CProd cProd = userProdComponent.queryByProdSn(prodSn);
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, cProd.getCust_id());

//		saveAllPublic(doneCode,getBusiParam(),busiInfo);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void bacthChangeTariff(List<CProdBacthDto> pordList,String newTariffId,boolean isUpdate)
		throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		PProdTariff newTariff = prodComponent.queryTariffById(newTariffId);
		List<String> custIds = CollectionHelper.converValueToList(pordList, "cust_id");
		List<String> userIds = CollectionHelper.converValueToList(pordList, "user_id");
		for(CProdBacthDto dto:pordList){
			if(dto.getEff_date() == null){
				throw new ServicesException("智能卡【"+dto.getCard_id()+"】的生效日期不能为空!");
			}
			changeTariff(dto.getProd_sn(), newTariffId, DateHelper.dateToStr(dto.getEff_date()), DateHelper.dateToStr(dto.getExp_date()),true,isUpdate, doneCode);
		}
		//生成计算到期日任务
	    List<String> list = new ArrayList<String>();
	    for(String str:custIds){
	    	if(!list.contains(str)) {
                list.add(str);
            }
	    }  
	    jobComponent.createInvalidCalJobByCustIds(doneCode, list.toArray(new String[list.size()]));
	    
		String info ="产品 "+pordList.get(0).getProd_name()+" 新资费 "+newTariff.getTariff_name();
		for (int i =0 ;i < custIds.size(); i++) {
			doneCodeComponent.saveDoneCodeInfo(doneCode, custIds.get(i), userIds.get(i),info);
		}
		getBusiParam().setDoneCode(doneCode);
		doneCodeComponent.saveBatchDoneCode(doneCode, getBusiParam().getBusiCode(), getBusiParam().getRemark(), custIds, userIds);
	}
	
	public void removeByProdSn(String prodSn, String tariffId) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		CProd cprod = userProdComponent.queryByProdSn(prodSn);
		String currentTariffId = cprod.getTariff_id();
		//当前资费
		PProdTariff currentTariff = userProdComponent.queryProdTariffById(currentTariffId);
		
		//取消资费变更,next_tariff_id设置为空
		userProdComponent.removeByProdSn(prodSn, tariffId);
		
		//TODO 重新计算并更新到期日 -------------------------------------
		CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(cprod.getAcct_id(),cprod.getProd_id());
		userProdComponent.updateInvalidDateByTariff(doneCode,  prodSn, acctItem);
		
		//基本包0资费+30年
		PProd p = userProdComponent.queryByProdId(cprod.getProd_id());
		if(p.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)){//基本产品
			if(currentTariff.getRent() == 0){
				String newValue = null;
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.YEAR, 30);
				newValue = DateHelper.format(calendar.getTime(), DateHelper.FORMAT_YMD);
				userProdComponent.updateInvalidDate(doneCode, prodSn, DateHelper.strToDate(newValue));
			}else{
				userProdComponent.updateInvalidDate(doneCode, prodSn, cprod.getEff_date());
				CAcctAcctitem acctitem = acctComponent.queryAcctItemByAcctitemId(cprod.getAcct_id(), cprod.getProd_id());
				if(currentTariff.getBilling_cycle() > 1){
					userProdComponent.updateInvalidDate(doneCode, prodSn, 0, acctitem.getActive_balance() - acctitem.getOwe_fee(), acctitem);
				}else if(currentTariff.getBilling_cycle() == 1){
					userProdComponent.updateInvalidDate(doneCode, prodSn, 0, acctitem.getReal_balance(), acctitem);
				}
			}
		}else{//非基本产品
			if(currentTariff.getRent()>0){
				userProdComponent.updateInvalidDate(doneCode, prodSn, cprod.getEff_date());
				CAcctAcctitem acctitem = acctComponent.queryAcctItemByAcctitemId(cprod.getAcct_id(), cprod.getProd_id());
				if(currentTariff.getBilling_cycle() > 1){
					userProdComponent.updateInvalidDate(doneCode, prodSn, 0, acctitem.getActive_balance() - acctitem.getOwe_fee(), acctitem);
				}else if(currentTariff.getBilling_cycle() == 1){
					userProdComponent.updateInvalidDate(doneCode, prodSn, 0, acctitem.getReal_balance(), acctitem);
				}
			}
		}
		
		//TODO over
		
		if(cprod != null && cprod.getStatus().equals(StatusConstants.TMPPAUSE)){
			String oldStatus = userProdComponent.queryLastStatus(prodSn);
			if(StringHelper.isNotEmpty(oldStatus)){
				userProdComponent.updateProdStatus(doneCode, prodSn, StatusConstants.TMPPAUSE, oldStatus);
			}
			jobComponent.createCreditCalJob(doneCode, cprod.getCust_id(), null,SystemConstants.BOOLEAN_TRUE);
		}
		saveAllPublic(doneCode,getBusiParam());
	}

	
	public void saveProdSyn(String[] prodSns, String[] userIds)
			throws Exception {
		//获取客户用户信息
		String custId = getBusiParam().getCust().getCust_id();
		CUser user = getBusiParam().getSelectedUsers().get(0);
		
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		
		//客户名下所有产品
		List<CProdDto> userProdList = userProdComponent.queryProdByCustId(custId);
		Map<String,List<CProdDto>> prodMap = CollectionHelper.converToMap(userProdList, "prod_sn") ;
		
		//客户名下所有用户
		List<CUser> userList = userComponent.queryUserByCustId(custId);
		Map<String,List<CUser>> userMap = CollectionHelper.converToMap(userList, "user_id");
		
		//客户名下账目
		List<AcctitemDto> acctItemList = acctComponent.queryAcctItemByCustId(custId);
		Map<String,List<AcctitemDto>> acctItemMap = CollectionHelper.converToMap(acctItemList, "prod_sn");
		
		//被选择主机的基本包到期日
		Date baseInvalidDate = DateHelper.now();
		for (CProdDto mp:userProdList){
			if (mp.getUser_id().equals(user.getUser_id()) && mp.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)){
				baseInvalidDate = getInvalidDate(mp,acctItemMap.get(mp.getProd_sn()).get(0),null);
				break;
			}
		}
		boolean isWriteOff = false;
		for (String mProdSn:prodSns){
			CProdDto mp = prodMap.get(mProdSn).get(0);
			
			//比较主机基本包到期与产品的到期日，取较小值
			Date mpInvalidDate = getInvalidDate(mp,acctItemMap.get(mProdSn).get(0),baseInvalidDate);
			
			if (mpInvalidDate.after(DateHelper.now())){
				for (String ouId:userIds){
					//查找副机下的产品
					CProd op = this.getUserProd(userProdList, ouId, mp.getProd_id());
					
					//验证是否有动态资源的产品未订购
					List<ProdResDto> resList = prodComponent.queryProdRes(mp.getProd_id());
					if(resList != null && resList.size() > 0){
						for(ProdResDto prod : resList){
							List<ResGroupDto> rgdList = prod.getDynamicResList();
							if(rgdList.size() > 0 && op == null){
								throw new ServicesException("副终端需先订购产品"+mp.getProd_name());
							}
						}
					}
					
					String isNewProd = SystemConstants.BOOLEAN_FALSE;
					int fee=0;
					//副机产品到期日
					Date opInvalidDate = null;
					if (op == null){//如果副机下不存在该产品
						//给同步用户订购产品
						isNewProd = SystemConstants.BOOLEAN_TRUE;
						this.saveOrderBaseList(userMap.get(ouId), doneCode,
								mp.getProd_id(), mp.getTariff_id(),
								DateHelper.formatNow(), null, null,
								SystemConstants.PROD_ORDER_TYPE_PRESENT,
								mp.getPre_open_time(),mp.getIs_bank_pay());
						op = userProdComponent.queryByProdId(ouId, mp.getProd_id());
						opInvalidDate = DateHelper.now();
					} else {
						opInvalidDate = getInvalidDate(op,acctItemMap.get(op.getProd_sn()).get(0),null);
					}
					
					//主机产品到期日在副机产品之后
					if (mpInvalidDate.after(opInvalidDate)){
						PProdTariff tariff = userProdComponent.queryProdTariffById(op.getTariff_id());
						//零资费修改到期日，月包、包多月修改到期日且修改余额
						if (tariff.getRent() == 0){
							userProdComponent.updateInvalidDate(doneCode, op.getProd_sn(), mpInvalidDate);
						} else {
							//先计算月份数，再计算天数，月份数按每月资费计算，天数按资费/30
						    Calendar startCal = new GregorianCalendar();
					        Calendar dealCal = new GregorianCalendar();
					        startCal.setTime(opInvalidDate);
					        dealCal.setTime(mpInvalidDate);
					        int diffMonth = (dealCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)) * 12 + dealCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH) ;
					        int diffDate = dealCal.get(Calendar.DATE) - startCal.get(Calendar.DATE);
					        fee = diffMonth*tariff.getRent() + diffDate*tariff.getRent()/30;
							
							//fee = DateHelper.getDiffDays(opInvalidDate, mpInvalidDate)*tariff.getRent()/30;
							if (acctItemMap.get(op.getProd_sn()) != null){
								AcctitemDto item=  acctItemMap.get(op.getProd_sn()).get(0);
								int oweFee= item.getOwe_fee() + item.getReal_fee() - item.getActive_balance() + item.getInactive_balance()  ;
								if (oweFee>0)
									fee += oweFee;
							}
							
							CAcctAcctitemInactive inactiveItem = new CAcctAcctitemInactive();
							inactiveItem.setPromotion_sn(doneCode.toString());
							inactiveItem.setCust_id(op.getCust_id());
							inactiveItem.setAcct_id(op.getAcct_id());
							inactiveItem.setAcctitem_id(op.getProd_id());
							if (tariff != null){
								inactiveItem.setCycle(tariff.getBilling_cycle());
							}
							inactiveItem.setActive_amount(fee);
							inactiveItem.setInit_amount(fee);
							inactiveItem.setBalance(fee);
							inactiveItem.setDone_code(doneCode);
							acctComponent.addAcctItemInactive(inactiveItem);
							
							userProdComponent.updateInvalidDate(doneCode, op.getProd_sn(), mpInvalidDate);
						}
						userProdComponent.saveProdSyn(doneCode,custId, mp.getUser_id(), ouId, mp.getProd_id(), isNewProd, opInvalidDate, mpInvalidDate, fee);
					}
				}
				isWriteOff = true;
			}
		}
		//生成销账任务
		if (isWriteOff )
			jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_TRUE);
		jobComponent.createAcctModeCalJob(doneCode, custId);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 根据产品到期日，账目余额(暂未考虑),基准到期日计算新的到期日
	 * @param mp
	 * @param acctitemDto
	 * @return
	 */
	private Date getInvalidDate(CProd mp, AcctitemDto acctitemDto,Date baseInvalidDate) {
		Date invalidDate = null;
		if (baseInvalidDate != null)
			invalidDate = mp.getInvalid_date().after(baseInvalidDate)?baseInvalidDate:mp.getInvalid_date();
		else 
			invalidDate = mp.getInvalid_date();
		if (DateHelper.now().after(invalidDate))
			invalidDate = DateHelper.now();
		return invalidDate;
	}
	

	private CProd getUserProd(List<CProdDto> userProdList,String userId,String prodId){
		for (CProd prod:userProdList){
			if (prod.getUser_id().equals(userId) && prod.getProd_id().equals(prodId)){
				return prod;
			}
		}
		return null;
	}
	
	public List<ProdTariffDto> queryBatchTariffByTariffId(String[] tariffIds) throws Exception {
		return userProdComponent.queryTariffByTariffIds(tariffIds);
	}

	public List<ProdTariffDto> queryTariffByTariffIds(String[] tariffIds,
			String[] userIds,String custId) throws Exception {
		List<ProdTariffDto> result = new ArrayList<ProdTariffDto>();
		Map<String, ProdTariffDto> tariffs = CollectionHelper
				.converToMapSingle(userProdComponent
						.queryTariffByTariffIds(tariffIds), "tariff_id");
		Map<String, List<PProdTariffDisct>> map = CollectionHelper
				.converToMap(
						userProdComponent.queryDisctByTariffIds(tariffIds),
						"tariff_id");
		Map<String, CUser> users = CollectionHelper.converToMapSingle(
				userComponent.queryUserByCustId(custId), "user_id");
		expressionUtil.setCcust(custComponent.queryCustById(custId));
		
		//按照折扣大小排序,降序
		Comparator<PProdTariffDisct> comparator = new Comparator<PProdTariffDisct>() {
			public int compare(PProdTariffDisct o1, PProdTariffDisct o2) {
				if(o1.getDisct_rent() >o2.getDisct_rent()){
					return -1;
				}else if(o1.getDisct_rent() < o2.getDisct_rent()){
					return 1;
				}
				return 0;
			}
		};
		
		for (String element : tariffIds) {
			ProdTariffDto pt = tariffs.get(element);
			if (pt != null) {
				result.add(pt);
				List<PProdTariffDisct> discts = map.get(pt.getTariff_id());
				if (discts != null && discts.size() > 0) {
					for (int i = discts.size() - 1; i >= 0; i--) {
						PProdTariffDisct dis = discts.get(i);
						if (dis != null)
							for (String userId : userIds) {
								CUser user = users.get(userId);
								if (user != null) {
									expressionUtil.setCuser(user);
									if (!expressionUtil.parseBoolean(dis
											.getRule_id_text())) {
										discts.remove(i);
										break;
									}
								}
							}
					}
					Collections.sort(discts, comparator);
					pt.setDisctList(discts);
				}
			}
		}
		return result;
	}
	
	private List<PProd> queryOrderProd(String[] userIds, String userType,String servType)
		throws Exception {
		String servId = userType;
		if (userType.equals(SystemConstants.USER_TYPE_DTV)
				&& servType.equals(SystemConstants.DTV_SERV_TYPE_DOUBLE)) {
			servId = SystemConstants.USER_TYPE_DTV + ","+SystemConstants.PROD_SERV_ID_ITV;
		}
		return userProdComponent.queryCanOrderProds(userIds,servId);
	}
	
	private List<PProd> filterProd(List<PProd> prodList, List<CUser> selectedUsers) throws Exception {
		List<PProd> orderProds = new ArrayList<PProd>();
		CCust cust = custComponent.queryCustById(selectedUsers.get(0).getCust_id());
		expressionUtil.setCcust(cust);
		for(PProd prod : prodList){
			List<ProdTariffDto> tariffList = prodComponent.queryTariffByProd(prod.getProd_id());
			if(tariffList.size() > 0){
				boolean flag = false;
				for(ProdTariffDto tariff : tariffList){
					for (CUser user : selectedUsers) {
						expressionUtil.setCuser(user);
						if (expressionUtil.parseBoolean(tariff.getRule_id_text(),String.valueOf(selectedUsers.size()))) {
							flag = true;
							break;
						}
					}
					if(flag) break;
				}
				
				//如果没有一个适合的资费，不添加该产品
				if(flag){
					orderProds.add(prod);
				}
				
			}
		}
		return orderProds;
	}

	public List<ProdDictDto> queryCanOrderProd(String[] userIds, String userType,String servType)
			throws Exception {
		
		List<PProd> prods = this.queryOrderProd(userIds, userType, servType);
		
		List<PProd> orderProds = new ArrayList<PProd>();
		//过滤掉宽带产品，无资费的(规则限定资费出不来)
		if(userType.equals(SystemConstants.USER_TYPE_BAND)){
			List<CUser> selectedUsers = userComponent.queryAllUserByUserIds(userIds);
			orderProds = this.filterProd(prods, selectedUsers);
		}else{
			orderProds = prods;
		}
		
		List<ProdDictDto> prodList = prodComponent.queryProdTree(orderProds);

		return prodList;
	}
	
	public List<PProd> queryCanOrderBandProd(String userId) throws Exception {
		List<CUser> selectedUsers = userComponent.queryAllUserByUserIds(new String[]{userId});
		List<PProd> prods = this.queryOrderProd(new String[]{userId}, SystemConstants.USER_TYPE_BAND, "");
		List<PProd> orderProds = new ArrayList<PProd>();
		if(selectedUsers.get(0).getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
			orderProds = this.filterProd(prods, selectedUsers);
		}else{
			orderProds = prods;
		}
		return orderProds;
	}
	
	public List<ProdDictDto> queryBatchCanOrderProd(String userType)
		throws Exception {
		List<PProd> orderProds = userProdComponent.queryCanOrderUserProd(userType);
		List<ProdDictDto> prods = prodComponent.queryProdTree(orderProds);
		return prods;
	}
	
	public List<PProdDto> queryCanOrderProdToCallCenter(String[] userIds, String userType,String servType) throws Exception {
//		List<PProd> orderProds = this.queryOrderProd(userIds, userType, servType);
		List<PProd> orderProds = userProdComponent.queryCanOrderProds(userIds,userType);
		
		
		List<PProdDto> list = new ArrayList<PProdDto>();
		for(PProd prod : orderProds){
//			List<PProdDto> prodList = prodComponent.queryTariffByProdDisct(prod.getProd_id());
//			if(prodList != null && prodList.size() > 0){
//				for(PProdDto dto : prodList){
//					dto.setProd_id(prod.getProd_id());
//					dto.setProd_name(prod.getProd_name());
//				}
//				list.addAll(prodList);
//			}
			List<ProdTariffDto> prodTariffDtoList = this.queryProdTariff(userIds, prod.getProd_id(), "");
			for(ProdTariffDto tariff : prodTariffDtoList){
				PProdDto ppDto = new PProdDto();
				BeanUtils.copyProperties(prod, ppDto);
				ppDto.setTariff_id(tariff.getTariff_id());
				ppDto.setTariff_name(tariff.getTariff_name());
				ppDto.setBilling_cycle(tariff.getBilling_cycle());
				List<PProdTariffDisct>  disctList = tariff.getDisctList();
				ppDto.setRent(tariff.getRent());
				if(null!=disctList){
					PProdTariffDisct disct = disctList.get(0);
					ppDto.setDisct_id(disct.getDisct_id());
					ppDto.setDisct_name(disct.getDisct_name());
					ppDto.setFinal_rent(disct.getFinal_rent());
					ppDto.setDisct_rent(disct.getDisct_rent());
				}
				list.add(ppDto);
			}
		}
		return list;
	}

	public List<ProdDictDto> queryCanOrderPkg(String custId) throws Exception {
		List<PProd> orderprods = userProdComponent.queryCanOrderPkgs(custId);
		List<ProdDictDto> prods = prodComponent.queryProdTree(orderprods);

		return prods;
	}


	public List<ProdResDto> queryProdRes(String prodId) throws Exception {
		return prodComponent.queryProdRes(prodId);
	}

	
	public List<ProdTariffDto> queryBatchProdTariff(String prodId) throws Exception {
		List<ProdTariffDto> list = prodComponent.queryTariffByProd(prodId);
		for(ProdTariffDto dto:list){
			if(StringHelper.isNotEmpty(dto.getRule_id_text())){
				dto.setTariff_name(dto.getTariff_name()+"--"+dto.getRule_name());
			}
		}
		return list;
	}
	
	public void resumeCustClass()throws Exception {
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//保存客户信息
		CCust cust = this.getBusiParam().getCust();
		custComponent.updateCustClass(doneCode, cust, SystemConstants.CUST_CLASS_YBKH, "");
		
		CProd prod = this.userProdComponent.queryBaseProdByCustId(cust.getCust_id());
		
		// base prod
		if(prod != null){ 
			cust.setCust_class(SystemConstants.CUST_CLASS_YBKH);
			List<CUser> zzdUsers = this.userComponent.queryAllUserByUserIds(new String[]{prod.getUser_id()});
			
			this.getBusiParam().addUser(zzdUsers.get(0));
			
			List<ProdTariffDto> prodTariffDto = this.queryProdTariff(new String[]{prod.getUser_id()}, prod.getProd_id(), null);
			String tariffStartDate = DateHelper.format(new Date(), "yyyy-MM-dd");
			if(prodTariffDto.size() > 0){
				changeTariff(prod.getProd_sn(), prodTariffDto.get(0).getTariff_id(), tariffStartDate, "", true, true, doneCode);
			}else{
				throw new ServiceException("没有匹配的资费信息，资费修改失败!");
			}
		}
		
		saveAllPublic(doneCode, getBusiParam());
	}

	public List<ProdTariffDto> queryProdTariff(String[] userIds, String prodId,String tariffId)
			throws Exception {
		List<ProdTariffDto> tariffList = prodComponent
				.queryTariffByProd(prodId);
		
//		Map<String, List<PProdTariffDisct>> disctmap = CollectionHelper
//				.converToMap(prodComponent.queryTariffDisctByProd(prodId),
//						"tariff_id");
		
		PProdTariff oldTariff = prodComponent.queryTariffById(tariffId);
		CCust cust = getBusiParam().getCust();
		expressionUtil.setCcust(cust);
		List<CProdDto> prodList = userProdComponent.queryByCustId(cust.getCust_id());
		Map<String,List<CProdDto>> orderprods = CollectionHelper.converToMap(prodList, "user_id");
		Map<String, CUserStb> userStbMap = CollectionHelper.converToMapSingle(userComponent.queryUserStbByCustId(getBusiParam().getCust().getCust_id()), "user_id");
		List<CUser> selectedUsers = getBusiParam().getSelectedUsers();
		int size = tariffList.size();
		for (int i = size - 1; i >= 0; i--) {
			ProdTariffDto tariff = tariffList.get(i);
			
			if(null !=oldTariff && !oldTariff.getBilling_cycle().equals(tariff.getBilling_cycle())){
				//不同周期不允许修改
				tariffList.remove(i);
				continue;
			}else if (tariff.getTariff_id().equals(tariffId)){
				//剔除原资费,用户资费修改
				tariffList.remove(i);
				continue;
			}
//			 产品规则
			if(selectedUsers.size() > 0){
				for (CUser user : selectedUsers) {
					expressionUtil.setCuser(user);
					expressionUtil.setCuserStb(userStbMap.get(user.getUser_id()));
					expressionUtil.setOrderprods(orderprods.get(user.getUser_id()));
					if (!expressionUtil.parseBoolean(tariff.getRule_id_text(),String.valueOf(selectedUsers.size()))) {
						tariffList.remove(i);
						break;
					}
				}
			}else{
				//客户套餐
				if (!expressionUtil.parseBoolean(tariff.getRule_id_text(), "0")) {
					tariffList.remove(i);
				}
			}
		}
		//按照资费升序排序
		Comparator<PProdTariff> comparator = new Comparator<PProdTariff>() {
			public int compare(PProdTariff o1, PProdTariff o2) {
				if(o1.getRent() /o1.getBilling_cycle() > o2.getRent() /o2.getBilling_cycle()){
					return 1;
				}else if(o1.getRent() /o1.getBilling_cycle() < o2.getRent() /o2.getBilling_cycle()){
					return -1;
				}else {
					return 0;
				}
			}
		};
		Collections.sort(tariffList, comparator);
		return tariffList;
	}
	
	public List<ProdTariffDto> queryFreeTariff(String[] userIds, String type,String prodId,String tariffId)throws Exception {
		List<ProdTariffDto> tariffList = prodComponent.queryTariffByProd(prodId);
		PProdTariff oldTariff = prodComponent.queryTariffById(tariffId);
		expressionUtil.setCcust(getBusiParam().getCust());
		List<CUser> selectedUsers = getBusiParam().getSelectedUsers();
		int size = tariffList.size();
		for (int i = size - 1; i >= 0; i--) {
			ProdTariffDto tariff = tariffList.get(i);
			
			if(null !=oldTariff && !oldTariff.getBilling_cycle().equals(tariff.getBilling_cycle())){
				//不同周期不允许修改
				tariffList.remove(i);
				continue;
			}else if (tariff.getTariff_id().equals(tariffId)){
				//剔除原资费,用户资费修改
				tariffList.remove(i);
				continue;
			}
		//	 产品规则
			if(selectedUsers.size() > 0){
				for (CUser user : selectedUsers) {
					//user原先是免费的，改成超额；原先超额改免费
					if(type.equals("OUT")){
						user.setStr19("F");
					}else{
						user.setStr19("T");
					}
					expressionUtil.setCuser(user);
					if (!expressionUtil.parseBoolean(tariff.getRule_id_text(),String.valueOf(selectedUsers.size()))) {
						tariffList.remove(i);
						break;
					}
				}
			}else{
				//客户套餐
				if (!expressionUtil.parseBoolean(tariff.getRule_id_text(), "0")) {
					tariffList.remove(i);
				}
			}
		}
		//按照资费升序排序
		Comparator<PProdTariff> comparator = new Comparator<PProdTariff>() {
			public int compare(PProdTariff o1, PProdTariff o2) {
				if(o1.getRent() /o1.getBilling_cycle() > o2.getRent() /o2.getBilling_cycle()){
					return 1;
				}else if(o1.getRent() /o1.getBilling_cycle() < o2.getRent() /o2.getBilling_cycle()){
					return -1;
				}else {
					return 0;
				}
			}
		};
		Collections.sort(tariffList, comparator);
		return tariffList;
	}
	
	
	public Map<String,Object> queryEditProdTariff(String[] userIds, String prodId,String tariffId)
		throws Exception {
		String dataRight = "";
		try {
			dataRight = prodComponent.queryChangeTariffRole();
		} catch (Exception e) {
		}
		List<ProdTariffDto> tariffList = prodComponent.queryTariffByProd(prodId);

		expressionUtil.setCcust(getBusiParam().getCust());
		List<CUser> selectedUsers = getBusiParam().getSelectedUsers();
		for (int i = tariffList.size() - 1; i >= 0; i--) {
			ProdTariffDto tariff = tariffList.get(i);
			
			if (tariff.getTariff_id().equals(tariffId)){
				//剔除原资费,用户资费修改
				tariffList.remove(i);
				continue;
			}
			//产品规则
			if(selectedUsers.size() > 0){
				//用户产品 用户套餐
				for (CUser user : selectedUsers) {
					expressionUtil.setCuser(user);
					if (!expressionUtil.parseBoolean(tariff.getRule_id_text(),String.valueOf(selectedUsers.size()))) {
						tariffList.remove(i);
						break;
					}
				}
			}else{
				//客户套餐
				if (!expressionUtil.parseBoolean(tariff.getRule_id_text(), "0")) {
					tariffList.remove(i);
				}
			}
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("dataRight", dataRight);
		map.put("tariffList", tariffList);
		return map;
	}
	
	public List<ProdTariffDto> queryAllProdTariff(String[] userIds, String prodId,String tariffId)
			throws Exception {
		List<ProdTariffDto> tariffList = prodComponent.queryTariffByProd(prodId);

		PProdTariff oldTariff = prodComponent.queryTariffById(tariffId);
		List<CUser> userList =  userComponent.queryAllUserByUserIds(userIds);
		
		Map<String, List<CUser>> custmap = CollectionHelper.converToMap(userList,"cust_id");
		
		String[] custs = CollectionHelper.converValueToArray(userList, "cust_id");
		String[] custArr =  null;
	    List<String> list = new ArrayList<String>();
	        for(int i = 0; i < custs.length; i++) {
	            if(!list.contains(custs[i])) {
	                list.add(custs[i]);
	            }
	        }
	    custArr = list.toArray(new String[list.size()]);
	    List<CCust> custList = custComponent.queryCustBycustIds(custArr);
		for (CCust custdto:custList){
			expressionUtil.setCcust(custdto);
			List<CUser> selectedUsers = custmap.get(custdto.getCust_id());
			for (int i = tariffList.size() - 1; i >= 0; i--) {
				ProdTariffDto tariff = tariffList.get(i);
				
				if(null !=oldTariff && !oldTariff.getBilling_cycle().equals(tariff.getBilling_cycle())){
					//不同周期不允许修改
					tariffList.remove(i);
					continue;
				}else if (tariff.getTariff_id().equals(tariffId)){
					//剔除原资费,用户资费修改
					tariffList.remove(i);
					continue;
				}
			//	 产品规则
				if(selectedUsers.size() > 0){
					for (CUser dto : selectedUsers) {
						expressionUtil.setCuser(dto);
						if (!expressionUtil.parseBoolean(tariff.getRule_id_text(),String.valueOf(selectedUsers.size()))) {
							tariffList.remove(i);
							break;
						}
					}
				}else{
					if (!expressionUtil.parseBoolean(tariff.getRule_id_text(), "0")) {
						tariffList.remove(i);
					}
				}
			}
		}
		return tariffList;
	}
	
	public List<ProdTariffDto> queryTariffForEzdToFzd(String custId,String userId,String prodId,String tariffId)
		throws Exception {
		
		List<ProdTariffDto> tariffList = prodComponent.queryTariffByProd(prodId);
		
		CCust cust = custComponent.queryCustById(custId);
		expressionUtil.setCcust(cust);
		
		UserDto userDto = userComponent.queryUserById(userId);
		CUserDtv dtvUser = new CUserDtv();
		BeanUtils.copyProperties(userDto, dtvUser);
		
		dtvUser.setTerminal_type(SystemConstants.USER_TERMINAL_TYPE_FZD);
		expressionUtil.setCuser(dtvUser);
		
		for (int i = tariffList.size() - 1; i >= 0; i--) {
			ProdTariffDto tariff = tariffList.get(i);
			if (tariff.getTariff_id().equals(tariffId)){
				//剔除原资费,用户资费修改
				tariffList.remove(i);
				continue;
			}
			
			if (!expressionUtil.parseBoolean(tariff.getRule_id_text(),String.valueOf(1))) {
				tariffList.remove(i);
			}
		}
		return tariffList;
	}

	public List<PRes> queryProdResByUserProdSn(String prodSn) throws Exception {
		return userProdComponent.queryResByProdSn(prodSn);
	}


	public List<PProd> querySubProds(String prodId) throws Exception {
		return prodComponent.querSubProds(prodId);
	}
	
	private void addProdToPkg(Integer doneCode, String packageId, String packageSn,
			String prodSn) throws Exception {
		//获取产品信息
		CProd cProd = userProdComponent.queryByProdSn(prodSn);
		
		//如果子产品已停机
		if(!isProdOpen(cProd.getStatus())){
			userProdComponent.updateProdStatus(doneCode,prodSn,cProd.getStatus(), StatusConstants.ACTIVE);
			
			CUser user = userComponent.queryUserById(cProd.getUser_id());
			//生成激活产品指令
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, cProd.getCust_id(),
					user.getUser_id(), user.getStb_id(),user.getCard_id(),user.getModem_mac(), cProd.getProd_sn(),cProd.getProd_id());
		}
		
		PProdTariff tariff = prodComponent.queryTariffById(cProd.getTariff_id());
		
		//如果是单月的资费
		if(tariff.getBilling_cycle() == 1 && SystemConstants.BILLING_TYPE_MONTH.equals(tariff.getBilling_type())){
			//出帐
			BBill bill = this.billComponent.confirmBill(prodSn, doneCode);
			if (bill != null){
				acctComponent.changeAcctItemOwefee(true,cProd.getAcct_id(), cProd.getProd_id(), bill.getOwe_fee());
			}
		}else if(tariff.getBilling_cycle() > 1 && SystemConstants.BILLING_TYPE_MONTH.equals(tariff.getBilling_type())){
			//作废账单
			billComponent.cancelBill(prodSn,DateHelper.nowYearMonth());
		}
		
		//在用户产品历史表中增加记录
//		cProd.setBillinfo_eff_date(new Date());
//		userProdComponent.addHis(doneCode, cProd);
		//更新用户产品对应的套餐信息
		userProdComponent.updateProdPkg(prodSn, packageSn, packageId);
	}
	
	private void removeProdFromPkg(Integer doneCode, CProd cp) throws Exception {
		userProdComponent.updateProdPkg(cp.getProd_sn(), "", "");
		
		//处理包多月资费产品账单
		CProd prod = userProdComponent.queryByProdSn(cp.getProd_sn());
		PProdTariff ppt = userProdComponent.queryProdTariffById(prod.getTariff_id());
		//next_bill_date已作废
//		if(SystemConstants.BOOLEAN_TRUE.equals(prod.getStop_by_invalid_date()) && null != prod.getNext_bill_date()){
		if(SystemConstants.BOOLEAN_TRUE.equals(prod.getStop_by_invalid_date()) && ppt.getBilling_cycle()>1){
			
			List<BBill> oweBillList = billComponent.queryPromOweFeeBill(prod.getProd_sn());
			if(oweBillList.size() == 0){
				//更新到期日
				CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(cp.getAcct_id(), cp.getProd_id());
				userProdComponent.updateInvalidDateByTariff(doneCode, cp.getProd_sn(), acctItem);
			}
			
			//下次出账日期之前的账单还原，之后的作废
			//恢复账单
			billComponent.recoverBill(cp.getProd_sn(),DateHelper.nowYearMonth());
			
			//next_bill_date已作废
//			billComponent.cancelBill(cp.getProd_sn(),DateHelper.formatYM(prod.getNext_bill_date()));
			//作废账单
			billComponent.cancelBill(cp.getProd_sn(),DateHelper.formatYM(prod.getInvalid_date()));
				
		} else {
			//更新到期日
			CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(cp.getAcct_id(), cp.getProd_id());
			userProdComponent.updateInvalidDateByTariff(doneCode, cp.getProd_sn(), acctItem);
		}
	}




	public void resetUserProdRes() throws Exception {
		CCust cust = getBusiParam().getCust();
		List<CUser> userList = getBusiParam().getSelectedUsers();
		if(userList == null || userList.size()==0){
			userList = userComponent.queryUserByCustId(cust.getCust_id());
		}
		//查找产品用户资源配置
		List<PProdUserRes> userResList = prodComponent.queryUserResByCountyId();
		Integer doneCode = doneCodeComponent.gDoneCode();
		for (CUser user:userList){
			expressionUtil.setCcust(cust);
			expressionUtil.setCuser(user);
			//查找用户的所有产品
			List<CProdDto> prodList = userProdComponent.queryByUserId(user.getUser_id());
			for (CProdDto prod:prodList){
				List<String> curReslist = new ArrayList<String>();
				boolean hasUserRes = false;
				for (PProdUserRes userRes:userResList){
					if (userRes.getProd_id().equals(prod.getProd_id())){
						hasUserRes = true;
						if (StringHelper.isEmpty(userRes.getRule_id_text()) 
								|| expressionUtil.parseBoolean(userRes.getRule_id_text())){
							String[] res = userRes.getRes_id().split(",");
							for (String resId:res){
								curReslist.add(resId);
							}
						}
					}
				}
				
				if (hasUserRes){
					//查找修改前产品对应的用户资源
					List<String> prodResList =  userProdComponent.queryUserProdRes(prod.getProd_sn());
					String addRes="";
					String delRes="";
					for (String res1:prodResList){
						boolean exits = false;
						if(curReslist!=null){
							for (String resId:curReslist){
								if (res1.equals(resId)){
									exits = true;
									break;
								}
							}
						}
						if (!exits)
							delRes +=","+res1;
					}
					if(curReslist!=null){
						for (String resId:curReslist){
							boolean exits = false;
							for (String res1:prodResList){
								if (res1.equals(resId)){
									exits = true;
									break;
								}
							}
							
							if (!exits)
								addRes +=","+resId;
						}
					}
					if (StringHelper.isNotEmpty(addRes)){
						addRes = addRes.substring(1);
						jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, 
								user.getCust_id(), user.getUser_id(), user.getStb_id(), user.getCard_id(),
								user.getModem_mac(),prod.getProd_sn(), prod.getProd_id(),"RES_ID:''"+addRes+"''");
					}
					
					if (StringHelper.isNotEmpty(delRes)){
						delRes = delRes.substring(1);
						jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD, 
								user.getCust_id(), user.getUser_id(), user.getStb_id(), user.getCard_id(),
								user.getModem_mac(),prod.getProd_sn(), prod.getProd_id(),"RES_ID:''"+delRes+"''");
					}
					//删除用户产品资源
					userProdComponent.removeProdRes(prod.getProd_sn(), delRes.split(","));
					//增加用户产品资源
					String[] addResArr = addRes.split(",");
					for (String resId:addResArr){
						if (StringHelper.isNotEmpty(resId))
							userProdComponent.addUserProdres(prod.getProd_sn(), resId);
					}
					
				}
			}
		}
		
		
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IUserProdService#changeExpDate(java.lang.String, java.lang.String)
	 */
	public void changeExpDate(String prodSn, String expDate) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String custId = getBusiParam().getCust().getCust_id();
		userProdComponent.updateExpDate(doneCode, prodSn,expDate);
		
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	//产品暂停
	public void pauseProd(String prodSn,String userId) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		this.tempPauseProd(doneCode, prodSn, userId);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void tempPauseProd(Integer doneCode,String prodSn,String userId) throws Exception {
		String custId = getBusiParam().getCust().getCust_id();
		UserDto user = queryUserById(userId);
		CProd prod = userProdComponent.queryByProdSn(prodSn);
		
		if(prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			userProdComponent.updateProdStatus(doneCode, prodSn, prod.getStatus(), StatusConstants.TMPPAUSE);
			if(isProdOpen(prod.getStatus())){
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD, custId,
						user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), prod.getProd_sn(), prod.getProd_id());
			}
		}else{
			userProdComponent.updateProdStatus(doneCode, prod.getProd_sn(), prod.getStatus(), StatusConstants.TMPPAUSE);
			List<CProdDto> childProdList = userProdComponent.queryChildProdByPkgsn(prod.getProd_sn());
			for(CProdDto childProd : childProdList){
				userProdComponent.updateProdStatus(doneCode, childProd.getProd_sn(), childProd.getStatus(), StatusConstants.TMPPAUSE);
				if(isProdOpen(childProd.getStatus())){
					user = queryUserById(childProd.getUser_id());
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD, custId,
							user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), childProd.getProd_sn(), childProd.getProd_id());
				}
			}
		}
		
	}
	
	//产品恢复
	public void resumeProd(String prodSn,String userId) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String custId = getBusiParam().getCust().getCust_id();
		UserDto user = queryUserById(userId);
		
		CProd prod = userProdComponent.queryByProdSn(prodSn);
		
		String oldStatus = userProdComponent.queryLastStatus(prodSn);
		if (StringHelper.isEmpty(oldStatus)) {
			if (prod.getInvalid_date().after(prod.getStatus_date()))
				oldStatus = StatusConstants.ACTIVE;
			else
				oldStatus = StatusConstants.OWESTOP;
		}else if(oldStatus.equals(StatusConstants.REQSTOP)){
			oldStatus = StatusConstants.ACTIVE;
		}
		
		if(prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			userProdComponent.updateProdStatus(doneCode, prodSn, prod.getStatus(), oldStatus);
			if(!isProdOpen(prod.getStatus())){
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, custId,
						user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), prod.getProd_sn(), prod.getProd_id());
			}
		}else{
			userProdComponent.updateProdStatus(doneCode, prod.getProd_sn(), prod.getStatus(), oldStatus);
			List<CProdDto> childProdList = userProdComponent.queryChildProdByPkgsn(prod.getProd_sn());
			for(CProdDto childProd : childProdList){
				userProdComponent.updateProdStatus(doneCode, childProd.getProd_sn(), childProd.getStatus(), oldStatus);
				if(!isProdOpen(childProd.getStatus())){
					user = queryUserById(childProd.getUser_id());
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, custId,
							user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), childProd.getProd_sn(), childProd.getProd_id());
				}
			}
		}
		
		
		//包月非零资费、按到期日停机，修改为按账务模式停机
		PProdTariff pt = userProdComponent.queryProdTariffById(prod.getTariff_id());
		if(pt.getRent() > 0 && pt.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH) 
				&& pt.getBilling_cycle() == 1
				&& prod.getStop_by_invalid_date().equals(SystemConstants.BOOLEAN_TRUE)){
			List<CProdPropChange> propChangeList = new ArrayList<CProdPropChange>();
			CProdPropChange propChange = new CProdPropChange();
			propChange.setColumn_name("stop_by_invalid_date");
			propChange.setOld_value(SystemConstants.BOOLEAN_TRUE);
			propChange.setNew_value(SystemConstants.BOOLEAN_FALSE);
			propChangeList.add(propChange);
			userProdComponent.editProd(doneCode, prodSn, propChangeList);
		}	
		
		if(pt.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)
				&& (pt.getBilling_cycle()>1 || pt.getRent() ==0)){
			userProdComponent.updateInvalidDate(doneCode, prod.getProd_sn(), DateHelper.getDiffDays(prod.getStatus_date(), new Date()), 0, new CAcctAcctitem());
		}
		jobComponent.createCreditExecJob(doneCode, custId);
		jobComponent.createInvalidCalJob(doneCode, custId);
		jobComponent.createAcctModeCalJob(doneCode, custId);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * @param prodId
	 * @param countyId
	 * @return
	 */
	public List<PProdDto> queryProdByCounty(String prodId, String countyId) throws JDBCException {
		return userProdComponent.queryProdByCountyId(prodId, countyId);
	}


	
	/**
	 * 根据用户Id和产品Id查询已订购产品
	 */
	public CProd queryOrderdProdByUserId(String userId,String prodId) throws Exception{
		return userProdComponent.queryByProdId(userId, prodId);
	}
	
//	public Date getInvalidDateByFee(long fee,int balance,int oweFee,int realFee,int rent,String rentType,Date beginFeeDate)throws Exception{
//		return userProdComponent.getInvalidDateByFee(fee, balance, oweFee, realFee, rent, rentType, beginFeeDate);
//	}
//	
//	public long getFeeByInvalidDate(int balance,int oweFee,int realFee,int rent,String rentType,Date invaidDate,Date beginFeeDate) throws Exception{
//		return userProdComponent.getFeeByInvalidDate(balance, oweFee, realFee, rent, rentType, invaidDate, beginFeeDate);
//	}
	
	public Date getInvalidDateByFee(String prodSn, int payFee) throws Exception {
		return userProdComponent.getInvalidDateByFeePro(prodSn, payFee);
	}

	/**
	 * 重算到期日.`
	 * @param prodSn
	 * @return
	 */
	public Date reCalcInvalidDate(String prodSn) throws Exception{
		CProd prod = userProdComponent.queryByProdSn(prodSn);
		Date oldInvalidDate = prod.getInvalid_date();
		String oldStr = DateHelper.format(oldInvalidDate, DateHelper.FORMAT_YMD);
		oldInvalidDate = DateHelper.parseDate(oldStr, DateHelper.FORMAT_YMD);
		Date newInvalid = userProdComponent.getInvalidDateByFeePro(prodSn, 0);
		String newStr = DateHelper.format(newInvalid, DateHelper.FORMAT_YMD);
		newInvalid = DateHelper.parseDate(newStr, DateHelper.FORMAT_YMD);
		
		if(null == newInvalid){
			throw new ServicesException("已欠费，不能变更到期日！");
		}
		
		if(newInvalid.getTime() != oldInvalidDate.getTime()){
			int doneCOde = userProdComponent.getDoneCOde();
			List<CProdPropChange> propChangeList  = new ArrayList<CProdPropChange>();
			CProdPropChange change = new CProdPropChange();
			change.setOld_value(oldStr);
			change.setNew_value(newStr);
			change.setColumn_name("invalid_date");
			change.setProd_sn(prodSn);
			propChangeList.add(change);
			
			userProdComponent.editProd(doneCOde, prodSn, propChangeList);
			
			saveAllPublic(doneCOde, getBusiParam());
		}else{
			newInvalid = oldInvalidDate;
		}
		return newInvalid;
	}
	
	public long getFeeByInvalidDate(String prodSn, Date invalidDate)
			throws Exception {
		return userProdComponent.getFeeByInvalidDatePro(prodSn, invalidDate);
	}
	

	/**
	 * @param expressionUtil the expressionUtil to set
	 */
	public void setExpressionUtil(ExpressionUtil expressionUtil) {
		this.expressionUtil = expressionUtil;
	}


	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IUserProdService#saveOrder(java.lang.String, java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.String)
	 */
	public void saveOrder(String prodId, String tariffId, String feeDate,
			List<UserProdRscDto> dynamicRscList, String expDate,
			String publicAcctItemType) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		saveOrderBaseList(getBusiParam().getSelectedUsers(),doneCode,prodId,tariffId,feeDate,expDate,dynamicRscList,SystemConstants.PROD_ORDER_TYPE_ORDER,null,SystemConstants.BOOLEAN_FALSE);
		saveAllPublic(doneCode,getBusiParam());
	}

	public void updateProdPreOpenDate(String prodSn, String countyId,Date newPreOpenDate, Date feeDate) throws ServicesException{
		try{
			Integer doneCode = doneCodeComponent.gDoneCode();
			CProd cprod=userProdComponent.updateProdPreOpenDate(doneCode,prodSn,countyId,newPreOpenDate,feeDate);
			if(cprod!=null){
				jobComponent.createInvalidCalJob(doneCode, cprod.getCust_id());
			}
			saveAllPublic(doneCode,getBusiParam());
		}catch (Exception e) {
			throw new ServicesException(e);
		}
	}

	public void updatePublicAcctItemType(String prodSn, String countyId,String publicAcctitemType)throws ServicesException{
		if(StringHelper.isEmpty(publicAcctitemType)){
			throw new ServicesException("参数'公用账目适用类型不能为空!'");
		}
		try{
			Integer doneCode = doneCodeComponent.gDoneCode();
			userProdComponent.updatePublicAcctItemType(doneCode,prodSn,getBusiParam().getCust(),publicAcctitemType);
			jobComponent.createInvalidCalJob(doneCode, getBusiParam().getCust().getCust_id());
			jobComponent.createAcctModeCalJob(doneCode, getBusiParam().getCust().getCust_id());
			saveAllPublic(doneCode,getBusiParam());
		}catch (Exception e) {
			throw new ServicesException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IUserProdService#saveBusiCmdCard(java.lang.String)
	 */
	public void saveBusiCmdCard(String cardId) throws Exception {
		if(StringHelper.isEmpty(cardId)){
			throw new ServicesException("智能卡不能为空!");
		}
		if(deviceComponent.getIsCard(cardId)){
			Integer doneCode = doneCodeComponent.gDoneCode();
			getBusiParam().setBusiCode(BusiCodeConstants.CA_SINGLE_CARD);
			getBusiParam().setRemark(getBusiParam().getRemark()==null?"CARD:"+cardId:getBusiParam().getRemark()+"CARD:"+cardId);
			jobComponent.createBusiCmdCard(doneCode, cardId, getOptr());
			saveAllPublic(doneCode,getBusiParam());
		}
	}

	public Pager<JCaCommand> queryCaCommandByCardId(String cardId,Integer start, Integer limit) throws Exception {
		if(StringHelper.isEmpty(cardId)){
			throw new ServicesException("智能卡不能为空!");
		}
		return jobComponent.queryCaCommandByCardId(cardId.split(","),start,limit);
	}
}
