/**
 *
 */
package com.ycsoft.business.service.impl;

import static com.ycsoft.commons.constants.BusiCodeConstants.CUST_OPEN;
import static com.ycsoft.commons.constants.BusiCodeConstants.DEVICE_BUY;
import static com.ycsoft.commons.constants.BusiCodeConstants.DEVICE_CHANGE;
import static com.ycsoft.commons.constants.BusiCodeConstants.PROD_PACKAGE_ORDER;
import static com.ycsoft.commons.constants.BusiCodeConstants.USER_OPEN;
import static com.ycsoft.commons.constants.BusiCodeConstants.USER_OPEN_BATCH;
import static com.ycsoft.commons.constants.BusiCodeConstants.USER_PROMOTION;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TBusiCode;
import com.ycsoft.beans.core.acct.CAcct;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemTrans;
import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.fee.CFeeDevice;
import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.beans.core.job.JProdNextTariffHis;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.business.component.config.BusiConfigComponent;
import com.ycsoft.business.component.core.UserPromComponent;
import com.ycsoft.business.dto.core.cust.DoneCodeExtAttrDto;
import com.ycsoft.business.dto.core.cust.DoneInfoDto;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.business.dto.core.fee.QueryFeeInfo;
import com.ycsoft.business.service.IDoneCodeService;
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
import com.ycsoft.daos.core.Pager;

/**
 * @author YC-SOFT
 *
 */
@Service
public class DoneCodeService extends BaseBusiService implements IDoneCodeService {
	private UserPromComponent userPromComponent;
	private BusiConfigComponent busiConfigComponent;
	
	/**
	 * 根据流水号查询业务费用，按费用进行汇总
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public List<BusiFeeDto> queryEditFee(String custId,Integer doneCode,String busiCode) throws Exception{
		List<BusiFeeDto> feeList = new ArrayList<BusiFeeDto>();
		List<CFee>  sumFeeList = feeComponent.querySumFeeByDoneCode(custId,doneCode);
		Map<String,CFee> feeMap = CollectionHelper.converToMapSingle(sumFeeList, "fee_id","fee_std_id");
//		if (busiCode.equals(DEVICE_BUY) || busiCode.equals(DEVICE_SALE)
//				|| busiCode.equals(DEVICE_RECLAIM) || busiCode.equals(BusiCodeConstants.DEVICE_CHANGE )
//				|| busiCode.equals(BusiCodeConstants.DEVICE_BUY_PJ ) || busiCode.equals(BusiCodeConstants.DEVICE_BUY_PJ_BACTH)){
			
			List<BusiFeeDto> list = feeComponent.getDeviceFeeItems();
			List<CFeeDevice> deviceList = feeComponent.queryDeviceByDoneCode(doneCode);
			if (deviceList!=null ){
				for(CFeeDevice device : deviceList){
					CFee fee = feeMap.get(device.getFee_id()+"_"+device.getFee_std_id());
					if (StringHelper.isNotEmpty(device.getFee_std_id())){
						for (BusiFeeDto busiFee:list){
							if (busiFee.getFee_std_id().equals(device.getFee_std_id())){
								busiFee.setSum_fee(fee.getReal_pay());
								busiFee.setBuy_num(fee.getBuy_num());
								busiFee.setAddr_id(fee.getAddr_id());
								busiFee.setKeyname(SystemConstants.FEE_TYPE_DEVICE);
								busiFee.setDevice_model_text(device.getDevice_model_text());
								feeList.add(busiFee);
								break;
							}
						}
					} else {
						for (BusiFeeDto busiFee:list){
							if (busiFee.getFee_id().equals(device.getFee_id())){
								busiFee.setDevice_model(device.getDevice_model());
								busiFee.setDefault_value(0);
								busiFee.setSum_fee(fee.getReal_pay());
								busiFee.setBuy_num(fee.getBuy_num());
								busiFee.setAddr_id(fee.getAddr_id());
								busiFee.setKeyname(SystemConstants.FEE_TYPE_DEVICE);
								busiFee.setDevice_model_text(device.getDevice_model_text());
								feeList.add(busiFee);
								break;
							}
						}
					}
				}
			} 
//		}  else {
			List<BusiFeeDto> busiList = feeComponent.getBusiFeeItems();
			for (BusiFeeDto busiFee:busiList){
//				//IP费用
//				if(busiFee.getFee_id().equals(SystemConstants.USER_IP_FEE_ID)){
//					CFee fee = feeMap.get(busiFee.getFee_id());
//					if(fee != null){
//						busiFee.setSum_fee(fee.getReal_pay());
//						busiFee.setBuy_num(0);
//						busiFee.setAddr_id(fee.getAddr_id());
//						feeList.add(busiFee);
//					}
//				}else{
					if (busiFee.getBusi_code().equals(busiCode)){
						for (CFee fee:sumFeeList){
							if (fee.getFee_id().equals(busiFee.getFee_id())){
								busiFee.setSum_fee(fee.getReal_pay());
								busiFee.setBuy_num(0);
								busiFee.setAddr_id(fee.getAddr_id());
								break;
							}
						}
						feeList.add(busiFee);
					}					
//				}
			}
			
//		}
		
		return feeList;
			
	}

	
	public void cancelDoneCode(Integer doneCode) throws Exception {
		String custId =getBusiParam().getCust().getCust_id();
		CDoneCode cDoneCode = doneCodeComponent.queryByKey(doneCode);
		TBusiCode tBusiCode= busiConfigComponent.queryBusiCode(cDoneCode.getBusi_code());
		//判断是否可以取消
		if (tBusiCode.getCancel().equals(SystemConstants.BOOLEAN_FALSE)){
			throw new ServicesException(tBusiCode.getBusi_name()+"业务不能回退");
		} else if (!cDoneCode.getOptr_id().equals(cDoneCode.getOptr_id())){
			throw new ServicesException("不是本人办理的业务，不能回退");
		} else if(!DateHelper.dateToStr(cDoneCode.getDone_date()).equals(DateHelper.getDate("-"))) {
			throw new ServicesException("不是当天办理的业务，不能回退");
		} else if (cDoneCode.getStatus().equals(StatusConstants.INVALID)){
			throw new ServicesException("该业务已回退");
		} else if (doneCodeComponent.getNeedCancleCount(doneCode, custId)>0){
			throw new ServicesException("前面有业务需要先回退");
		}
		//根据业务编号调用相应的回退方法
		String busiCode = cDoneCode.getBusi_code();
		if (busiCode.equals(CUST_OPEN))
			cancelOpenCust(custId, doneCode);
//		else if (busiCode.equals(CUST_EDIT) || busiCode.equals(CUST_TRANS) 
//				|| busiCode.equals(CUST_CHANGE_ADDR) || busiCode.equals(CUST_RELOCATE))
//			cancelEditCust(custId, doneCode);
//		else if (busiCode.equals(USER_OPEN))
//			cancelOpenUser(custId, doneCode);
//		else if (busiCode.equals(USER_EDIT) || busiCode.equals(USER_EDIT_LEVEL))
//			cancelEditUser(custId, doneCode);
//		else if (busiCode.equals(USER_DTOI))
//			cancelOpenInteractive(custId, doneCode);
//		else if (busiCode.equals(USER_REQUIRE_STOP))
//			cancelRequireStop(custId, doneCode);
//		else if (busiCode.equals(PROD_PACKAGE_ORDER))
//			cancelOrder(custId, doneCode);
//		else if (busiCode.equals(PROD_CHANGE_TARIFF))
//			cancelEditTariff(custId, doneCode);
//		else if (busiCode.equals(USER_PROMOTION))
//			cancelPromotion(custId, doneCode);
//		else if (busiCode.equals(ACCT_TRANS))
//			cancelTransAcct(custId, doneCode);
////		else if (busiCode.equals(ACCT_ADJUST))
////			cancelAdjustAcct(custId, doneCode);
//		else if (busiCode.equals(DEVICE_BUY))
//			cancelBuyDevice(custId, doneCode);
//		else if (busiCode.equals(DEVICE_CHANGE))
//			cancelChangeDevice(custId, doneCode);
//		else if (busiCode.equals(EDIT_INVALID_DATE))
//			cancelEditInvalid(custId, doneCode);
		else
			throw new ServicesException(ErrorCode.BusiCodeCanNotCancel,busiCode);
		//冲正流水对应的费用
		List<CFee> feeList = feeComponent.queryByDoneCode(doneCode);
		for (CFee fee:feeList){
			if(fee.getFee_type().equals(SystemConstants.FEE_TYPE_BUSI)){
				cancelOtherFee(doneCode,busiCode,fee);
			}
		}
		//TODO  插入负费用，原先费用不用改
		Integer createDoneCode = doneCodeComponent.gDoneCode();
		getBusiParam().setRemark("doneCode="+doneCode);
//		getBusiParam().setFees(fees);
		getBusiParam().setBusiCode(BusiCodeConstants.BUSI_CANCEL);
		saveAllPublic(createDoneCode, getBusiParam());
			
			
//			if (fee.getStatus().equals(StatusConstants.PAY))
//				cancelFee(doneCode, cDoneCode.getBusi_code(), fee);
//			else {
//				if (fee.getFee_type().equals(SystemConstants.FEE_TYPE_ACCT)){
//					cancelFee(doneCode, cDoneCode.getBusi_code(), fee);
//				}
//				feeComponent.removeFee(fee.getFee_sn());
//			}
//		}
		//创建返销帐任务  jpz not need
//		if (feeList.size()>0){
//			jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_FALSE);
//			jobComponent.createAcctModeCalJob(doneCode, custId);
//			
//		}
		//TODO 作废工单   wait new task jpz
//		taskComponent.cancelTaskByDoneCode(doneCode);
		//更新流水状态
//		doneCodeComponent.updateStatus(doneCode,cDoneCode.getBusi_code());
	}

	public Pager<DoneCodeExtAttrDto> queryByCustId(String custId, QueryFeeInfo queryFeeInfo,
			Integer start,Integer limit) throws Exception {
		return doneCodeComponent.queryByCustId(custId, queryFeeInfo,start,limit);
	}

	//取消创建客户---客户开户
	private void cancelOpenCust(String custId,Integer doneCode) throws Exception {
		//删除客户
		custComponent.removeCustWithOutHis(custId);
		//删除账户
		CAcct acct = acctComponent.queryCustAcctByCustId(custId);
		acctComponent.removeAcctWithoutHis(acct.getAcct_id());
	}

	//取消修改客户资料----修改客户、过户、移机
	private void cancelEditCust(String custId,Integer doneCode) throws Exception {
		List<CCustPropChange> propChangeList = custComponent.queryPropChangeByDoneCode(custId, doneCode);
		List<CCustPropChange> newPropChangeList = new ArrayList<CCustPropChange>();
		for (CCustPropChange change:propChangeList){
			String temp = change.getOld_value() == null?"": change.getOld_value();
			change.setOld_value(change.getNew_value()== null?"":change.getNew_value());
			change.setNew_value(temp);
			newPropChangeList.add(change);
		}
		custComponent.editCust(doneCode, custId, newPropChangeList);
		//删除属性变动历史
		custComponent.removeCustPropChange(custId, doneCode);
	}

	//取消修改用户
	private void cancelEditUser(String custId,Integer doneCode) throws Exception {
		CDoneCodeDetail detail = doneCodeComponent.queryDetail(doneCode).get(0);
		List<CUserPropChange> propChangeList = userComponent.queryPropChangeByDoneCode(detail.getUser_id(), doneCode);
		List<CUserPropChange> newPropChangeList = new ArrayList<CUserPropChange>();
		for (CUserPropChange change:propChangeList){
			String temp = change.getOld_value();
			change.setOld_value(change.getNew_value());
			change.setNew_value(temp);
			newPropChangeList.add(change);
		}
		userComponent.editUser(doneCode, detail.getUser_id(), newPropChangeList);
		//删除属性变动历史
		userComponent.removeUserPropChange(detail.getUser_id(), doneCode);
	}

	//取消用户开户
	private void cancelOpenUser(String custId,Integer doneCode) throws Exception {
		//查找流水对应的用户
		CDoneCodeDetail detail = doneCodeComponent.queryDetail(doneCode).get(0);
		CCust cust = custComponent.queryCustById(custId);
		CUser user = userComponent.queryUserById(detail.getUser_id());
		//生成删除用户job
		delUserJob(user, custId, doneCode);
		//更新客户设备状态
		custComponent.updateDeviceStatusByCode(custId, user.getStb_id(), StatusConstants.IDLE);
		custComponent.updateDeviceStatusByCode(custId, user.getCard_id(), StatusConstants.IDLE);
		custComponent.updateDeviceStatusByCode(custId, user.getModem_mac(), StatusConstants.IDLE);
		//删除账户信息
		acctComponent.removeAcctWithoutHis(user.getAcct_id());
		//删除用户相关信息
		userComponent.removeUserWithoutHis(user.getUser_id());
		
		List<CUser> userList = userComponent.queryUserByCustId(user.getCust_id());
		if(userList.size() == 0){
			//修改客户状态为预开户
			if(cust.getStatus().equals(StatusConstants.ACTIVE))
				custComponent.updateCustStatus(doneCode,custId,StatusConstants.ACTIVE,StatusConstants.PREOPEN);
		}
	}

	//取消开通双向
	private void cancelOpenInteractive(String custId,Integer doneCode) throws Exception {
		//查找流水对应的用户
		CDoneCodeDetail detail = doneCodeComponent.queryDetail(doneCode).get(0);
		CUser userDto = queryUserById(detail.getUser_id());
		//取消修改用户属性
		cancelEditUser(userDto.getUser_id(), doneCode);
		jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.CANCEL_INTERACTIVE, custId,
				userDto.getUser_id(), userDto.getStb_id(), userDto.getCard_id(), "", null,null,JsonHelper.fromObject(userDto));
		//修改设备状态
		custComponent.updateDeviceStatusByCode(custId, userDto.getModem_mac(), StatusConstants.IDLE);
	}

	//取消报停
	private void cancelRequireStop(String custId,Integer doneCode) throws Exception {
		//根据流水号查找是否有预报停任务，
		//如果有，删除预报停任务
		//如果没有，则修改用户及设备以及产品的状态
		if (jobComponent.isPreStop(doneCode)){
			jobComponent.removePreStopByDoneCode(doneCode);
		} else {
			List<CDoneCodeDetail> detailList = doneCodeComponent.queryDetail(doneCode);
			List<CUser> userList = userComponent.queryUserByCustId(detailList.get(0).getCust_id());
			CUser user = null;
			for (CDoneCodeDetail detail:detailList){
				
				for (CUser cu:userList){
					if (cu.getUser_id().equals(detail.getUser_id())){
						user = userComponent.queryUserById(detail.getUser_id());
						break;
					}
				}
				//修改设备状态
				custComponent.updateDeviceStatusByCode(user.getCust_id(), user.getStb_id(), StatusConstants.USE);
				custComponent.updateDeviceStatusByCode(user.getCust_id(), user.getCard_id(), StatusConstants.USE);
				custComponent.updateDeviceStatusByCode(user.getCust_id(), user.getModem_mac(), StatusConstants.USE);

				//修改用户状态
				updateUserStatus(doneCode, user.getUser_id(), user.getStatus(), StatusConstants.ACTIVE);
				//生成激活用户JOB
				CUser userDto = queryUserById(user.getUser_id());
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_USER, custId,
						user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), null,null,JsonHelper.fromObject(userDto));
			}
			//更新产品状态
			List<CProdPropChange> changeList = userProdComponent.queryChangeByDoneCode(doneCode);
			for (CProdPropChange change:changeList){
				CProd prod = userProdComponent.queryByProdSn(change.getProd_sn());
				for (CUser cu:userList){
					if (cu.getUser_id().equals(prod.getUser_id())){
						user = userComponent.queryUserById(prod.getUser_id());
						break;
					}
				}
				userProdComponent.updateProdStatus(doneCode, change.getProd_sn(), change.getNew_value(), change.getOld_value());
				if (isProdOpen(change.getOld_value())){
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, 
							custId, user.getUser_id(), user.getStb_id(), user.getCard_id(),
							user.getModem_mac(), prod.getProd_sn(), prod.getProd_id());
				}
			}
		}
	}

	//取消产品订购
	private void cancelOrder(String custId,Integer doneCode) throws Exception {
		List<CProd> prodList = userProdComponent.queryProdByDoneCode(doneCode);
		for (CProd prod :prodList){
			CUser user = userComponent.queryUserById(prod.getUser_id());
			//生成钝化产品的指令
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD, custId,
					user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(),
					prod.getProd_sn(),prod.getProd_id());
			//删除产品
			userProdComponent.removeProdWithoutHis(doneCode, prod.getProd_sn());
			//删除产品对应的账目
			acctComponent.removeAcctItemWithoutHis(user.getCust_id(),user.getAcct_id(),prod.getProd_id(), doneCode,this.getBusiParam().getBusiCode());
		}
	}

	//取消修改资费
	private void cancelEditTariff(String custId,Integer doneCode) throws Exception {
		JProdNextTariff tariffJob = jobComponent.queryTariffJob(doneCode);
		if(tariffJob != null){
			JProdNextTariffHis tariffJobHis = new JProdNextTariffHis();
			BeanUtils.copyProperties(tariffJob, tariffJobHis);
			jobComponent.saveTariffJobHis(tariffJobHis);
			jobComponent.removeTariffJobByDoneCode(doneCode);
			userProdComponent.updateNextTariff(tariffJob.getProd_sn(), "");
		}
	}

	//取消促销
	private void cancelPromotion(String custId,Integer doneCode) throws Exception {
		//有问题
		/*String busiCode =  getBusiParam().getBusiCode();
		CPromotion promotion = userPromComponent.queryByDoneCode(doneCode);
		//修改费用的优惠信息为空
		feeComponent.cancelDisct(promotion.getPromotion_sn());
		//退还已经返还的优惠账目金额
		List<CAcctAcctitemInactive> itemList = acctComponent.queryInactiveByPromSn(promotion.getPromotion_sn());
		for (CAcctAcctitemInactive item:itemList){
			if(item.getUse_amount()>0){
				acctComponent.changeAcctItemBanlance(doneCode, busiCode, custId,
						item.getAcct_id(), item.getAcctitem_id(),
						SystemConstants.ACCT_CHANGE_PROMOTION, SystemConstants.ACCT_FEETYPE_PRESENT, item.getUse_amount()*-1);
			}
		}
		//删除优惠信息
		userPromComponent.removeBySn(promotion.getPromotion_sn());
		//删除冻结账户信息
		acctComponent.removeInactiveWithoutHis(promotion.getPromotion_sn());
		//删除赠送的产品信息
		cancelOrder(custId,doneCode);
		//生成返销帐任务
		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_FALSE);*/
	}
	
	private void cancelMuchProdTransAcct(Integer doneCode, String prodSn, int transAmount) throws Exception {
		CProd prod = userProdComponent.queryByProdSn(prodSn);
		if(prod != null){
			String acctId = prod.getAcct_id();
			String acctItemId = prod.getProd_id();
			CAcct acct = acctComponent.queryByAcctId(acctId);
			if (acct!=null && !acct.getAcct_type().equals(SystemConstants.ACCT_TYPE_PUBLIC)){
				CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(acctId, acctItemId);
				PProdTariff tariff = userProdComponent.queryProdTariffById(prod.getTariff_id());
				if(tariff != null && tariff.getBilling_cycle() > 1 && tariff.getRent() >0 ){
					//包多月的情况，如果有账单，则要取消账单
					List<BBill> muchbills=billComponent.queryMuchMonthProdBill(prod.getProd_sn(), doneCode, DateHelper.format(new Date(),
							DateHelper.FORMAT_YM), SystemConstants.BILL_COME_FROM_MUCH);
					if(muchbills!=null&&muchbills.size()>0){
						int owefee=0;
						int billfee=0;
						for(BBill bill:muchbills){
							owefee=owefee+bill.getOwe_fee();
							billfee=billfee+bill.getFinal_bill_fee();
							billComponent.cancelBill(bill.getBill_sn());
						}
						acctComponent.changeAcctItemOwefee(false, acctId, acctItemId, owefee*-1);
//						userProdComponent.updateInvalidDate(doneCode,  prod.getProd_sn(),0, billfee*-1, acctItem);
						userProdComponent.updateInvalidDate(doneCode, prod.getProd_sn(), 
								userProdComponent.getDate(userProdComponent.queryByProdSn(prod.getProd_sn()).getInvalid_date(), muchbills.size()*-1, 0));

					}
					
				}else{
					//包月情况
					userProdComponent.updateInvalidDate(doneCode,  prod.getProd_sn(),0, transAmount*-1, acctItem);
				}
			}
		}
	}

	//取消转账
	private void cancelTransAcct(String custId,Integer doneCode) throws Exception {
		String busiCode =  getBusiParam().getBusiCode();
		//取消转账
		List<CAcctAcctitemTrans> transList = acctComponent.queryTransByDoneCode(doneCode);
		for (CAcctAcctitemTrans trans:transList){
			String outAcctId = trans.getOut_acct_id();
			String outAcctItemId = trans.getOut_acctitem_id();
			String inAcctId = trans.getIn_acct_id();
			String inAcctItemId = trans.getIn_acctitem_id();
			int fee = trans.getAmount();
			
			acctComponent.changeAcctItemBanlance(doneCode, busiCode,trans.getCust_id(),
					outAcctId, outAcctItemId,
					SystemConstants.ACCT_CHANGE_TRANS,trans.getFee_type(),fee, null);

			acctComponent.changeAcctItemBanlance(doneCode, busiCode,trans.getCust_id(),
					inAcctId, inAcctItemId,
					SystemConstants.ACCT_CHANGE_TRANS,trans.getFee_type(),fee*-1, null);
			
			CProd outProd = userProdComponent.queryByAcctItem(outAcctId, outAcctItemId);
			if (outProd != null){
				CAcctAcctitem acctItemOut = acctComponent.queryAcctItemByAcctitemId(outAcctId, outAcctItemId);
//				acctComponent.updateInvalidDate(doneCode, outProd.getProd_sn(), 0, fee, acctItemOut);
				this.cancelMuchProdTransAcct(doneCode, outProd.getProd_sn(), fee);
			}
			
			CProd inProd = userProdComponent.queryByAcctItem(inAcctId, inAcctItemId);
			if (inProd != null){
				CAcctAcctitem acctItemIn = acctComponent.queryAcctItemByAcctitemId(inAcctId, inAcctItemId);
//				acctComponent.updateInvalidDate(doneCode, inProd.getProd_sn(), 0, fee*-1, acctItemIn);
				this.cancelMuchProdTransAcct(doneCode, inProd.getProd_sn(), fee*-1);
			}
		}
		
		//生成返销帐任务
		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_FALSE);
		jobComponent.createAcctModeCalJob(doneCode, custId);
		jobComponent.createInvalidCalJob(doneCode, custId);
		//删除转账记录
		acctComponent.removeTrans(doneCode);
		//删除账户异动
		acctComponent.removeChange(doneCode);
	}

	//取消调账
//	private void cancelAdjustAcct(String custId,Integer doneCode) throws Exception {
//		String busiCode =  getBusiParam().getBusiCode();
//		//查找调账记录
//		CAcctAcctitemAdjust adjust = acctComponent.queryAdjustByDoneCode(doneCode);
//		//修改账目余额
//		acctComponent.changeAcctItemBanlance(doneCode, busiCode, custId,
//				adjust.getAcct_id(), adjust.getAcctitem_id(),
//				SystemConstants.ACCT_CHANGE_ADJUST, SystemConstants.ACCT_FEETYPE_ADJUST, adjust.getAjust_fee());
//		//删除调账对应的账单
//		billComponent.deleteBill(adjust.getDone_code());
//		//生成返销帐任务
//		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_FALSE);
//		//删除调账记录
//		acctComponent.removeAdjust(doneCode);
//		//删除账户异动
//		acctComponent.removeChange(doneCode);
//	}

	//取消购买设备
	private void cancelBuyDevice(String custId,Integer doneCode) throws Exception {
		//查找购买的设备信息
		List<CFeeDevice> deviceList = feeComponent.queryDeviceByDoneCode(doneCode);
		//修改设备属性
		deviceComponent.recover(doneCode);
		//删除客户设备
		for (CFeeDevice device:deviceList){
			custComponent.removeDevice(custId, device.getDevice_id(),doneCode, SystemConstants.BOOLEAN_FALSE);
			if (StringHelper.isNotEmpty(device.getPair_card_id())){
				custComponent.removeDevice(custId, device.getPair_card_id(),doneCode, SystemConstants.BOOLEAN_FALSE);
			}
		}
		//删除设备信息异动
		deviceComponent.removeChange(doneCode);
	}
	/**
	 * 回退修改到期日
	 * @param custId
	 * @param doneCode
	 * @throws Exception
	 */
	private void cancelEditInvalid(String custId,Integer doneCode) throws Exception {
		List<CProdPropChange> changeList = userProdComponent.queryChangeByDoneCode(doneCode);
		if(changeList.size()>0){
			List<CProdPropChange> cList = new ArrayList<CProdPropChange>();
			for (CProdPropChange change:changeList){
				CProdPropChange propChange = new CProdPropChange();
				propChange.setColumn_name(change.getColumn_name());
				propChange.setOld_value(change.getNew_value());
				propChange.setNew_value(change.getOld_value());
				cList.add(propChange);
			}
			userProdComponent.editProd(doneCode, changeList.get(0).getProd_sn(), cList);

			CProd cProd = userProdComponent.queryByProdSn(changeList.get(0).getProd_sn());
			if (cProd != null){ 
				if(cProd.getInvalid_date().before(DateHelper.strToDate(DateHelper.formatNow()))){
					CUser user = userComponent.queryUserById(cProd.getUser_id());
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD, 
							custId, user.getUser_id(), user.getStb_id(), user.getCard_id(),
							user.getModem_mac(), cProd.getProd_sn(), cProd.getProd_id());
				}
				//删除属性变动历史
				userProdComponent.removePropChange(cProd.getProd_sn(), doneCode);
			}
		}
	}
	
	
	//取消更换设备
	private void cancelChangeDevice(String custId,Integer doneCode) throws Exception {
		/*//查找变更记录
		List<CCustDeviceChange> changeList = custComponent.queryDeviceChangeByDoneCode(doneCode);
		//调用变更接口
		String busiCode =  getBusiParam().getBusiCode();
		//boolean changeOwnership = changeList.get(0).getChange_ownship().equals(SystemConstants.BOOLEAN_TRUE)?true:false;
		String oldStbId="";
		String newStbId="";
		String oldCardId="";
		String newCardId="";
		String oldModemId="";
		String newModemId="";
		for (CCustDeviceChange change:changeList){
			if (change.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB)){
				oldStbId = change.getNew_device_code();
				newStbId = change.getOld_device_code();
			} else if (change.getDevice_type().equals(SystemConstants.DEVICE_TYPE_CARD)){
				oldCardId = change.getNew_device_code();
				newCardId = change.getOld_device_code();
			} else if (change.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM)){
				oldModemId = change.getNew_device_code();
				newModemId = change.getOld_device_code();
			}
		}

		if (changeList.get(0).getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM)){
			changeModem(oldModemId, newModemId,
					custId, doneCode,busiCode);
		} else {
			changeStbCard(oldStbId, oldCardId, newStbId, newCardId,  custId, doneCode, busiCode);
		}
		//删除客户设备变更历史
		custComponent.removeDeviceChange(doneCode);
		//删除设备信息异动
		deviceComponent.removeChange(doneCode);*/

	}

	public void setUserPromComponent(UserPromComponent userPromComponent) {
		this.userPromComponent = userPromComponent;
	}


	/**
	 * @param busiConfigComponent the busiConfigComponent to set
	 */
	public void setBusiConfigComponent(BusiConfigComponent busiConfigComponent) {
		this.busiConfigComponent = busiConfigComponent;
	}

	public Pager<DoneInfoDto> getGridDate(Integer doneCode, String custId, Integer start,
			Integer limit) throws Exception {
		CDoneCode cDoneCode = doneCodeComponent.queryByKey(doneCode);
		String busiCode = cDoneCode.getBusi_code();
		if(busiCode.equals(USER_OPEN) || busiCode.equals(USER_OPEN_BATCH)){
			//目前不支持分页，还未有批量开户
			List<CDoneCodeDetail> detailList = doneCodeComponent.queryDetail(doneCode);
			String[] users = CollectionHelper.converValueToArray(detailList, "user_id");
			List<CUser> userList = new ArrayList<CUser>();
			List<CUser> userUseList = userComponent.queryAllUserByUserIds(users);
			userList.addAll(userUseList);
			List<CUser> userHisList = userComponent.queryAllUserHisByUserIds(users);
			userList.addAll(userHisList);
			
			List<DoneInfoDto> infoList = new ArrayList<DoneInfoDto>();
			for(CUser user:userList){
				DoneInfoDto info = new DoneInfoDto();
				info.setCard_id(user.getCard_id());
				info.setModem_mac(user.getModem_mac());
				info.setStb_id(user.getStb_id());
				info.setUser_type(user.getUser_type());
				info.setUser_name(user.getUser_name());
				info.setStatus_text(user.getStatus_text());
				infoList.add(info);
			}
			return new Pager(infoList,infoList.size());
//			return doneCodeComponent.getUserOpenDate(doneCode,start,limit);
		}else if(busiCode.equals(PROD_PACKAGE_ORDER) || busiCode.equals(BusiCodeConstants.PROD_SINGLE_ORDER)
				|| busiCode.equals(BusiCodeConstants.PROD_CONTINUE) || busiCode.equals(BusiCodeConstants.PROD_UPGRADE)
				|| busiCode.equals(BusiCodeConstants.OTT_MOBILE_UPGRADE) || busiCode.equals(BusiCodeConstants.PROD_TERMINATE)
				|| busiCode.equals(BusiCodeConstants.PROD_HIGH_TERMINATE) || busiCode.equals(BusiCodeConstants.PROD_SUPER_TERMINATE)
				|| busiCode.equals(BusiCodeConstants.ACCT_PAY) || busiCode.equals(BusiCodeConstants.ACCT_REFUND)){
			return doneCodeComponent.getOrderProdDate(doneCode,custId,start,limit);
		}else if(busiCode.equals(DEVICE_CHANGE)){
			return doneCodeComponent.getDeviceChangeDate(doneCode,cDoneCode.getCounty_id(),start,limit);
		}else if(busiCode.equals(DEVICE_BUY)){
			return doneCodeComponent.getDeviceBuyDate(doneCode,cDoneCode.getCounty_id(),start,limit);
		}else if(busiCode.equals(USER_PROMOTION) || busiCode.equals(BusiCodeConstants.PROMOTION_AUTO)){
			return doneCodeComponent.getPromotionDate(doneCode,cDoneCode.getCounty_id(),start,limit);
		}else if(busiCode.equals(BusiCodeConstants.PROM_ACCT_PAY)){
			Pager<DoneInfoDto> donePager = doneCodeComponent.getPromFeeDate(doneCode,cDoneCode.getCounty_id(),start,limit);
			String[] users = CollectionHelper.converValueToArray(donePager.getRecords(), "user_id");
			List<CUser> userList = userComponent.queryAllUserByUserIds(users);
			for(DoneInfoDto dto:donePager.getRecords()){
				
				for(CUser user:userList){
					if(dto.getUser_id().equals(user.getUser_id())){
						String userName = "";
						if(StringHelper.isNotEmpty(user.getCard_id())){
							userName = user.getUser_type_text()+"-"+ user.getUser_name()+"("+user.getCard_id().substring( user.getCard_id().length()-4, user.getCard_id().length())+")";
						}else if(StringHelper.isNotEmpty(user.getUser_name())){
							userName = user.getUser_type_text()+"-"+user.getUser_name();
						}else{
							userName = user.getUser_type_text();
						}
						dto.setUser_name(userName);
					}
					
				}
			}
			return donePager;
		}else if(busiCode.equals(BusiCodeConstants.BAND_CHANG_PROD)){
			return doneCodeComponent.getBandUpgradeDate(doneCode, cDoneCode.getCounty_id(), start, limit);
		}
		return null;
	}
	
}
