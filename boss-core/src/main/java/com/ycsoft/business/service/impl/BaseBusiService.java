/**
 *
 */
package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TBusiConfirm;
import com.ycsoft.beans.config.TCountyAcct;
import com.ycsoft.beans.config.TCountyAcctChange;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CGeneralCredential;
import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.fee.CFeeAcct;
import com.ycsoft.beans.core.fee.CFeePay;
import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.promotion.CPromFeeProd;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.device.RCard;
import com.ycsoft.beans.device.RModem;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.task.TaskFillDevice;
import com.ycsoft.beans.task.WTaskUser;
import com.ycsoft.business.cache.PrintContentConfiguration;
import com.ycsoft.business.commons.abstracts.BaseService;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.config.BusiConfigComponent;
import com.ycsoft.business.component.config.ExtTableComponent;
import com.ycsoft.business.component.core.AcctComponent;
import com.ycsoft.business.component.core.AuthComponent;
import com.ycsoft.business.component.core.BillComponent;
import com.ycsoft.business.component.core.CustComponent;
import com.ycsoft.business.component.core.FeeComponent;
import com.ycsoft.business.component.core.JobComponent;
import com.ycsoft.business.component.core.OrderComponent;
import com.ycsoft.business.component.core.UserComponent;
import com.ycsoft.business.component.core.UserProdComponent;
import com.ycsoft.business.component.resource.DeviceComponent;
import com.ycsoft.business.component.resource.InvoiceComponent;
import com.ycsoft.business.component.resource.ProdComponent;
import com.ycsoft.business.component.task.TaskComponent;
import com.ycsoft.business.dto.core.acct.AcctAcctitemActiveDto;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.fee.FeeBusiFormDto;
import com.ycsoft.business.dto.core.fee.FeeInfoDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.ProdListDto;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.store.MemoryPrintData;
import com.ycsoft.daos.core.JDBCException;

/**
 * @author YC-SOFT
 *
 */
@Service
public class BaseBusiService extends BaseService {
	protected UserProdComponent userProdComponent;
	protected AcctComponent acctComponent;
	protected FeeComponent feeComponent;
	protected JobComponent jobComponent;
	protected UserComponent userComponent;
	protected CustComponent custComponent;
	protected DeviceComponent deviceComponent;
	protected ProdComponent prodComponent;
	protected BillComponent billComponent;
	protected InvoiceComponent invoiceComponent;
	protected ExtTableComponent extTableComponent;
	protected TaskComponent taskComponent;
	@Autowired
	protected OrderComponent orderComponent;
	protected BusiConfigComponent busiConfigComponent;
	@Autowired
	protected AuthComponent authComponent;
	
	/**
	 * 处理产品授权（不处理套餐，但是一个产品如果是套餐子产品则会被处理）
	 * 加减授权都会正确处理
	 * @param cancelList
	 * @throws Exception 
	 */
	public void authProdNoPackage(List<CProdOrder> cancelResultList,Map<String,CUser> userMap,Integer done_code) throws Exception{
	
		Map<CUser,List<CProdOrder>> pstProdMap=new HashMap<>();
		for(CProdOrder pstorder:cancelResultList){
			if(StringHelper.isNotEmpty(pstorder.getUser_id())){
				CUser user=userMap.get(pstorder.getUser_id());
			    if(user==null){
			    	user=userComponent.queryUserById(pstorder.getUser_id());
			    	userMap.put(pstorder.getUser_id(), user);
			    	if(user==null){
			    		throw new ServicesException(ErrorCode.OrderDateException,pstorder.getOrder_sn());
			    	}
			    }
			    List<CProdOrder> pstlist=pstProdMap.get(user);
			    if(pstlist==null){
			    	pstlist=new ArrayList<>();
			    	pstProdMap.put(user, pstlist);
			    }
			    pstlist.add(pstorder);
			}
		}
		for(CUser user:  pstProdMap.keySet()){
			authComponent.sendAuth(user, pstProdMap.get(user), BusiCmdConstants.ACCTIVATE_PROD, done_code);
		}
	}
	/**
	 * 初始化接口的业务参数
	 * @param busiCode
	 * @throws Exception 
	 */
	protected Integer initExternalBusiParam(String busiCode,String  custId) throws Exception{
		BusiParameter param=new BusiParameter();
		SOptr optr=new SOptr();
		optr.setOptr_id("0");
		optr.setDept_id("4501");
		optr.setLogin_name("admin");
		optr.setArea_id("4500");
		optr.setCounty_id("4501");
		CustFullInfoDto custFullInfo = new CustFullInfoDto();
		if(custId!=null){
			custFullInfo.setCust(custComponent.queryCustById(custId));
		}
		param.setOptr(optr);
		param.setBusiCode(busiCode);
		param.setCustFullInfo(custFullInfo);
		param.setService_channel(SystemConstants.SERVICE_CHANNEL_MOBILE);
		param.setDoneCode(doneCodeComponent.gDoneCode());
		this.setParam(param);
		return param.getDoneCode();
	}
	/**
	 * 保存订购产品受理单
	 * @param doneCode
	 * @param prodList
	 * @throws Exception
	 */
	public void saveOrderProdDoneInfo(Integer doneCode, List<ProdListDto> prodList) throws Exception {
		CCust cust = getBusiParam().getCust();
		List<CUser> userList = getBusiParam().getSelectedUsers();
		String custType = cust.getCust_type();
		String userType = userList.get(0).getUser_type();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("cust_type", custType);
		map.put("user_type", userType);
		
		List<Map<String,Object>> prodTariffList = new ArrayList<Map<String,Object>>();
		for(ProdListDto dto : prodList){
			Map<String,Object> prodTariffMap = new HashMap<String, Object>();
			String prodName = dto.getProdName();
			PProd prod = new PProd();
			if(StringHelper.isEmpty(prodName)){
				prod = prodComponent.queryProdById(dto.getProdId());
				prodName = prod.getProd_name();
			}
			prodTariffMap.put("prod_name", prodName);
			
			String tariffName = dto.getTariffName();
			if(StringHelper.isEmpty(tariffName)){
				PProdTariff _tariff = prodComponent.queryTariffById(dto.getTariffId());
				tariffName = _tariff.getTariff_name();
			}
			prodTariffMap.put("tariff_name", tariffName );
			String prod_remark = dto.getProd_remark();
			if(StringHelper.isEmpty(prod_remark)){
				prod_remark = prod.getProd_desc();
			}
			if(StringHelper.isEmpty(prod_remark)){
				prod_remark = "";
			}
			prodTariffMap.put("prod_desc", prod_remark.replaceAll("\n", "</br>").replaceAll("\r", "").replaceAll("\"", "") );
			prodTariffMap.put("isBankPay", MemoryDict.getDictName(DictKey.BOOLEAN, dto.getIsBankPay()));
			prodTariffList.add(prodTariffMap);
		}
		map.put("prod_tariff_list", prodTariffList);
		
		List<Map<String,Object>> stbCardList = new ArrayList<Map<String,Object>>();
		for(CUser user : userList){
			Map<String,Object> stbCardMap = new HashMap<String, Object>();
			stbCardMap.put("stb_id", user.getStb_id());
			stbCardMap.put("card_id", user.getCard_id());
			stbCardList.add(stbCardMap);
		}
		map.put("stb_card_list", stbCardList);
		map.put("terminal_count", userList.size());
		doneCodeComponent.saveDoneCodeInfo(doneCode, cust.getCust_id(),userList.get(0).getUser_id(), map);
	}
	
	/**
	 * 终止一个产品
	 * @param custId
	 * @param doneCode
	 * @param busiCode
	 * @param jobId
	 * @param prod
	 * @param banlanceDealType
	 * @param transAcctId
	 * @param transAcctItemId
	 * @throws Exception
	 */
	protected void terminateProd(String custId,CUser user,Integer doneCode,String busiCode
			,CProd prod,String banlanceDealType,String transAcctId,String transAcctItemId) throws Exception{
		//如果资费是包多月或套餐缴费的产品，作废当月以后账单
		PProdTariff tariff = prodComponent.queryTariffById(prod.getTariff_id());
		if(null != tariff  && SystemConstants.BILLING_TYPE_MONTH.equals(tariff.getBilling_type())
				&& tariff.getRent() > 0){
			//作废欠费账单
			billComponent.cancelMuchBill(prod.getProd_sn(),DateHelper.nowYearMonth());
			billComponent.cancelTerminateBill(prod.getProd_sn(),DateHelper.nowYearMonth());
			
			CAcctAcctitem acctitem = acctComponent.queryAcctItemByAcctitemId(prod.getAcct_id(), prod.getProd_id());
			acctComponent.changeAcctItemOwefee(true, prod.getAcct_id(), prod.getProd_id(), acctitem.getOwe_fee()*-1);
		}
		
		
		//更新产品账单状态为出帐
		BBill bill = billComponent.confirmBill(prod.getProd_sn(), doneCode);
		if (bill !=null )
			acctComponent.changeAcctItemOwefee(true, prod.getAcct_id(), prod.getProd_id(), bill.getOwe_fee());
		terminateAcctItem(custId, doneCode, busiCode,prod.getUser_id(), prod.getAcct_id(),prod.getProd_id(), banlanceDealType,
				transAcctId, transAcctItemId);
		//删除该产品已经存在的资费任务
		jobComponent.deleteNewProdTariffJob(prod.getProd_sn());
		//判断产品状态，如果当前产品的状态为"开"，则生成钝化产品的任务
		if (StringHelper.isNotEmpty(prod.getUser_id())){
			if (isProdOpen(prod.getStatus())){
				if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD, custId,
							user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(),
							prod.getProd_sn(),prod.getProd_id());
				} else {
					List<CProd> prodList = userProdComponent.queryByPkgSn(prod.getProd_sn());
					for (CProd cp:prodList){
						CUser cu = userComponent.queryUserById(cp.getUser_id());
						jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD, custId,
								cu.getUser_id(), cu.getStb_id(), cu.getCard_id(), cu.getModem_mac(),
								cp.getProd_sn(),cp.getProd_id());
					}
				}
			}
		}
		
		//将产品记录到历史表
		userProdComponent.removeProdWithHis(doneCode, prod);
	}

	/**
	 * 终止账目
	 * @param custId
	 * @param doneCode
	 * @param busiCode
	 * @param userId
	 * @param acctId
	 * @param AcctITemId
	 * @param banlanceDealType
	 * @param transAcctId
	 * @param transAcctItemId
	 * @throws JDBCException
	 * @throws Exception
	 */
	protected void terminateAcctItem(String custId, Integer doneCode,
			String busiCode,String userId,String acctId,String AcctITemId, String banlanceDealType,
			String transAcctId, String transAcctItemId) throws JDBCException,
			Exception {
		CAcctAcctitem acctItem = new CAcctAcctitem();
		//获取账目信息
		if(StringHelper.isNotEmpty(acctId) && StringHelper.isNotEmpty(AcctITemId)){
			acctItem = acctComponent.queryAcctItemByAcctitemId(acctId,AcctITemId);
		}

		//转账或者退款
		if (banlanceDealType.equals(SystemConstants.ACCT_BALANCE_TRANS)){
//			//如果是模转数，减去资金中的调账金额
			if(BusiCodeConstants.USER_ATOD.equals(busiCode)){
				int transBalance = acctItem.getCan_trans_atod();//模拟转数时不需要考虑产品余额是否可转属性
//				//查询模拟账目调账金额
//				CAcctAcctitemActive acctItemActive = acctComponent.queryAdjustAcctitemActive(acctItem.getAcct_id(), acctItem.getAcctitem_id(), SystemConstants.ACCT_CHANGE_ADJUST, acctItem.getCounty_id());
//				if(null != acctItemActive){
//					transBalance = transBalance-acctItemActive.getBalance();
//				}
				if(transBalance > 0){
					acctTrans( custId, doneCode, busiCode, acctItem.getAcct_id(),
							acctItem.getAcctitem_id(), transAcctId, transAcctItemId, transBalance);
				}
			}else{
				acctTrans( custId, doneCode, busiCode, acctItem.getAcct_id(),
						acctItem.getAcctitem_id(), transAcctId, transAcctItemId, acctItem.getCan_trans_balance());
			}
		} else if(banlanceDealType.equals(SystemConstants.ACCT_BALANCE_REFUND)){
			PayDto pay = new PayDto();
			pay.setUser_id(userId);
			pay.setAcct_id(acctItem.getAcct_id());
			pay.setAcctitem_id(acctItem.getAcctitem_id());
			pay.setFee(acctItem.getCan_refund_balance()*-1);
			
			this.saveAcctPay(doneCode, pay);
		}
	}
	
	/**
	 * 回退套餐缴费
	 * @param doneCode
	 * @param busiCode
	 */
	protected void cancelPromPay(Integer doneCode,Integer feeDoneCode,String busiCode,String custId) throws Exception{
		List<CFee> feeList = feeComponent.queryByDoneCode(feeDoneCode);
		//冲正缴费记录
		for (CFee cFee : feeList) {
			cancelFee(doneCode, busiCode, cFee);
		}
		
		//取消实缴大于实际资费*缴费月数的额外账单
		String billingcycle= DateHelper.format(new Date(), DateHelper.FORMAT_YM);
		List<BBill> promFeeBills=billComponent.queryPromFeeBillByCreateDonecode(custId, feeDoneCode, billingcycle, SystemConstants.BILL_COME_FROM_PROM);
		if(promFeeBills!=null&&promFeeBills.size()>0){
			//Map<acct_id,map<acctitem_id,b_bill>>
			Map<String,Map<String,BBill>> acctitemMap=new HashMap<String,Map<String,BBill>>();
			for(BBill bill:billComponent.queryPromFeeBillByCreateDonecode(custId, feeDoneCode, billingcycle, SystemConstants.BILL_COME_FROM_PROM)){
				billComponent.cancelBill(bill.getBill_sn());
				if(!acctitemMap.containsKey(bill.getAcct_id())){
					acctitemMap.put(bill.getAcct_id(), new HashMap<String,BBill>());
				}
				Map<String,BBill> itemMap=acctitemMap.get(bill.getAcct_id());
				if(!itemMap.containsKey(bill.getAcctitem_id())){
					itemMap.put(bill.getAcctitem_id(), bill);
				}else{
					itemMap.get(bill.getAcctitem_id()).setOwe_fee(itemMap.get(bill.getAcctitem_id()).getOwe_fee()+bill.getOwe_fee());
				}
			}
			//更新账目欠费
			for(Map<String,BBill> itemMap:  acctitemMap.values()){
				for(BBill bill:itemMap.values()){
					acctComponent.changeAcctItemOwefee(false, bill.getAcct_id(), bill.getAcctitem_id(), bill.getOwe_fee()*-1);
				}
			}
			
		}
		
		//冲正解冻记录
		List<CAcctAcctitemInactive> inactiveList = feeComponent.queryInactiveByDoneCode(feeDoneCode);
		for(CAcctAcctitemInactive inactive : inactiveList){
			//查找账目对应的产品
			CProd prod = userProdComponent.queryByAcctItem(inactive.getAcct_id(), inactive.getAcctitem_id());
			CAcctAcctitem acctItem= acctComponent.queryAcctItemByAcctitemId(inactive.getAcct_id(), inactive.getAcctitem_id());
			
			if (inactive.getUse_amount()>0){
				//已经有返回
				acctComponent.changeAcctItemBanlance(doneCode, busiCode, inactive.getCust_id(),
						inactive.getAcct_id(), inactive.getAcctitem_id(),
						SystemConstants.ACCT_CHANGE_UNCFEE, SystemConstants.ACCT_FEETYPE_PRESENT, inactive.getUse_amount()*-1, null);
			}
			acctComponent.removeInactiveWithHis(inactive, doneCode);
			
			//修改用户产品的到期日
			userProdComponent.updateInvalidDate(doneCode,  prod.getProd_sn(),0, inactive.getInit_amount()*-1, acctItem);
		}
		
		//零资费产品和包多月产品修改到期日
		List<CProdPropChange> changeList = userProdComponent.queryPromPayChange(feeDoneCode, getOptr().getCounty_id());
		if(null != changeList && changeList.size() > 0){
			for(CProdPropChange change : changeList){
				//修改到期日
				userProdComponent.updateInvalidDate(doneCode, change.getProd_sn(), DateHelper.strToDate(change.getOld_value()));
			}
		}
		
		//套餐缴费记录移到历史
		feeComponent.savePromPayHis(doneCode,feeDoneCode);
		
		//信控任务
		jobComponent.createCreditExecJob(doneCode, custId);
	}
	
	//TODO 杂费取消
	protected void cancelOtherFee( Integer doneCode, String busiCode,CFee fee) throws Exception, JDBCException {
		//更新费用记录为冲正
		feeComponent.saveCancelFee(fee,doneCode);
		//重载操作员未打印的费用
		String optrId = getOptr().getOptr_id();
		List<String> feeSnList = feeComponent.queryUnPrintFeeByOptr(optrId);
		MemoryPrintData.reloadOptrFee(optrId, feeSnList);
	}
	

	/**
	 * 冲正
	 * @param doneCode
	 * @param busiCode
	 * @param fee
	 * @throws Exception
	 * @throws JDBCException
	 */
	protected void cancelFee( Integer doneCode, String busiCode,
			CFee fee) throws Exception, JDBCException {
		//如果是缴费,需要修改账户余额
		if (!fee.getStatus().equals(StatusConstants.INVALID)
				&& fee.getFee_type().equals(SystemConstants.FEE_TYPE_ACCT)
				&& !fee.getBusi_code().equals(BusiCodeConstants.ACCT_PAY_ATV)){
			CFeeAcct feeAcct = feeComponent.queryAcctFeeByFeeSn(fee.getFee_sn());
			//获取资金类型
			String acctFeeType = SystemConstants.ACCT_FEETYPE_CASH;
			if (StringHelper.isNotEmpty(fee.getPay_type()) )
				acctFeeType=this.acctComponent.getFeeType(feeAcct.getPay_type());
			//如果是现金充值，判断充值时是否有折扣
			if (SystemConstants.ACCT_FEETYPE_CASH.equals(acctFeeType) && StringHelper.isNotEmpty(feeAcct.getDisct_id())){
				PProdTariffDisct disct = userProdComponent.queryDistById(feeAcct.getDisct_id());
				String refund = disct.getRefund();
				String trans = disct.getTrans();
				//如折扣配置不可退，不可转，则acctFeeType为赠送
				if(SystemConstants.BOOLEAN_FALSE.equals(refund) && 
						SystemConstants.BOOLEAN_FALSE.equals(trans)){
					acctFeeType = SystemConstants.ACCT_FEETYPE_ZKXJYH;
				}
			}
			CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(fee.getAcct_id(), fee.getAcctitem_id());
			if (acctItem == null){
				throw new ServicesException("该笔费用对应的账目已经被删除，无法冲正");
			}
//			int feeTypeBalance = acctComponent.querySumFeetype(fee.getAcct_id(), fee.getAcctitem_id(), acctFeeType);
//			if (feeTypeBalance < fee.getReal_pay()){
				//查找该资金的累积销帐金额
//				int sumWriteOff = acctComponent.querySumWriteOff(fee.getAcct_id(), fee.getAcctitem_id(), acctFeeType);
//				if (feeTypeBalance + sumWriteOff<fee.getReal_pay()){
//					throw new ServicesException("该笔费用对应的账目做过退款或者转账操作，余额不足，不能冲正!");
//				}
//				throw new ServicesException("该笔费用对应的账目余额不足，不能冲正!");
//			}
			int changeFee = fee.getReal_pay();

			//查询账目信息
			acctComponent.changeAcctItemBanlance(doneCode, busiCode, fee.getCust_id(),
					fee.getAcct_id(), fee.getAcctitem_id(),
					SystemConstants.ACCT_CHANGE_UNCFEE, acctFeeType, fee.getReal_pay()*-1, null);
			//判断是否有赠送，如果取消赠送金额
			
			//查找feesn对应的赠送记录
			CAcctAcctitemInactive acctInactive = acctComponent.queryInactiveByFeesn(fee.getFee_sn());
			if (acctInactive != null){
				changeFee += acctInactive.getInit_amount();
				if (acctInactive.getUse_amount()>0){
					//已经有返还
					acctComponent.changeAcctItemBanlance(doneCode, busiCode, fee.getCust_id(),
							acctInactive.getAcct_id(), acctInactive.getAcctitem_id(),
							SystemConstants.ACCT_CHANGE_UNCFEE, SystemConstants.ACCT_FEETYPE_PRESENT, acctInactive.getUse_amount()*-1, null);
				}
				acctComponent.removeInactiveWithHis(acctInactive, doneCode);
			}
		
			//修改用户产品的到期日
			if(fee.getBusi_code().equals(BusiCodeConstants.ACCT_PAY_ZERO)){
				userProdComponent.updateInvalidDateByDoneCode(doneCode,fee.getCreate_done_code(), feeAcct.getProd_sn());
				
				//修改帐目欠费
				acctComponent.changeAcctItemOwefee(false, feeAcct.getAcct_id(), feeAcct.getAcctitem_id(), -fee.getReal_pay());
			}else{
				if(fee.getBusi_code().equals(BusiCodeConstants.PROM_ACCT_PAY)){//套餐缴费处理
					
					PProdTariff tariff = userProdComponent.queryProdTariffById(feeAcct.getTariff_id());
					if(tariff==null)throw new ServicesException("系统错误：c_fee_acct.tariff_id("+feeAcct.getTariff_id()+")找不到对应的资费信息.");
					if(tariff.getRent()>0&&tariff.getBilling_cycle()==1){//包月非0资费处理到期日，其他不处理
						changeFee=changeFee-billComponent.queryPromFeeBillOwefeeSum(fee.getCust_id(), fee.getCreate_done_code(), feeAcct.getProd_sn());
						userProdComponent.updateInvalidDate(doneCode,  feeAcct.getProd_sn(),0, changeFee*-1, acctItem);
					}
				}else{
					PProdTariff tariff = userProdComponent.queryProdTariffById(feeAcct.getTariff_id());
					if(tariff!=null&&tariff.getBilling_cycle()>1&&tariff.getRent()>0){
						//包多月的情况，如果有账单，则要取消账单
						List<BBill> muchbills=billComponent.queryMuchMonthProdBill(feeAcct.getProd_sn(), fee.getCreate_done_code(), DateHelper.format(new Date(),
								DateHelper.FORMAT_YM), SystemConstants.BILL_COME_FROM_MUCH);
						if(muchbills!=null&&muchbills.size()>0){
							int owefee=0;
							int billfee=0;
							for(BBill bill:muchbills){
								owefee=owefee+bill.getOwe_fee();
								billfee=billfee+bill.getFinal_bill_fee();
								billComponent.cancelBill(bill.getBill_sn());
							}
							acctComponent.changeAcctItemOwefee(false, feeAcct.getAcct_id(), feeAcct.getAcctitem_id(), owefee*-1);
							//userProdComponent.updateInvalidDate(doneCode,  feeAcct.getProd_sn(),0, billfee*-1, acctItem);
							userProdComponent.updateInvalidDate(doneCode, feeAcct.getProd_sn(), 
									userProdComponent.getDate(userProdComponent.queryByProdSn(feeAcct.getProd_sn()).getInvalid_date(), muchbills.size()*-1, 0));
						}
						
					}else{
						//包月情况
						userProdComponent.updateInvalidDate(doneCode,  feeAcct.getProd_sn(),0, changeFee*-1, acctItem);
					}
				}
				//生成计算到期日任务
				jobComponent.createInvalidCalJob(doneCode, fee.getCust_id());
			}
		}else if(!fee.getStatus().equals(StatusConstants.INVALID)
				&& fee.getFee_type().equals(SystemConstants.FEE_TYPE_PROMACCT)){//促销缴费
			//资金类型
			String acctFeeType = SystemConstants.ACCT_FEETYPE_CASH;
			
			
			List<CPromFeeProd> feeProdList = feeComponent.queryFeeProdByDoneCode(fee.getBusi_done_code());
			for(CPromFeeProd feeProd : feeProdList){
				CAcctAcctitem acctItem = acctComponent.queryAcctItemByUserId(feeProd.getUser_id(), feeProd.getProd_id());
				if (acctItem == null){
					PProd prod = prodComponent.queryProdById(feeProd.getProd_id());
					throw new ServicesException("促销缴费产品"+prod.getProd_name()+"已经被删除，无法冲正");
				}
				
				int feeTypeBalance = acctComponent.querySumFeetype(acctItem.getAcct_id(), acctItem.getAcctitem_id(), acctFeeType);
				if (feeTypeBalance < feeProd.getReal_pay()){
					throw new ServicesException("产品"+acctItem.getAcctitem_name()+"对应的账目余额不足，不能冲正!");
				}
				//记录账目的变动金额
				int changeFee = feeProd.getReal_pay();
				
				//修改账目余额
				acctComponent.changeAcctItemBanlance(doneCode, busiCode, fee.getCust_id(),
						acctItem.getAcct_id(), acctItem.getAcctitem_id(),
						SystemConstants.ACCT_CHANGE_UNCFEE, acctFeeType, feeProd.getReal_pay()*-1,null);
				
				//有赠送记录的
				if(feeProd.getShould_pay() > feeProd.getReal_pay()){
					CAcctAcctitemInactive acctInactive = acctComponent.queryPromInactiveByFeesn(fee.getFee_sn(), acctItem.getAcct_id(), acctItem.getAcctitem_id());
					if (acctInactive != null){
						changeFee += acctInactive.getInit_amount();
						if (acctInactive.getUse_amount()>0){
							//已经有返还
							acctComponent.changeAcctItemBanlance(doneCode, busiCode, fee.getCust_id(),
									acctInactive.getAcct_id(), acctInactive.getAcctitem_id(),
									SystemConstants.ACCT_CHANGE_UNCFEE, SystemConstants.ACCT_FEETYPE_PRESENT, acctInactive.getUse_amount()*-1,null);
						}
						acctComponent.removeInactiveWithHis(acctInactive, doneCode);
					}
				}
				
//				userProdComponent.updateInvalidDate(doneCode,  feeAcct.getProd_sn(),0, changeFee*-1, acctItem);
				
			}
			
			//生成计算到期日任务
			jobComponent.createInvalidCalJob(doneCode, fee.getCust_id());
			
		}
		
		//找出这张发票所有的费用
//		String invoiceMode = fee.getInvoice_mode();
//		List<CFee>feeList = new ArrayList<CFee>();
//		if(StringHelper.isNotEmpty(invoiceMode) && SystemConstants.INVOICE_MODE_AUTO.equals(invoiceMode)){
//			feeList = feeComponent.queryFeeByInvoice(fee.getInvoice_code(), fee.getInvoice_id(), fee.getCust_id());
//			
//		}
		
		//更新费用记录为冲正
		feeComponent.saveCancelFee(fee,doneCode);
		
		if (fee.getBusi_code().equals(BusiCodeConstants.ACCT_PAY_ATV)){
			//如果是补收模拟费或者协议缴费,则删除补收生成的账单和销账记录
			billComponent.deleteBill(fee.getBusi_done_code());
		}
		
		//如果是代金券
		if(SystemConstants.PAY_TYPE_DJQ.equals(fee.getPay_type())){
			feeComponent.updateVoucher(fee.getFee_sn());
		}else if(SystemConstants.PAY_TYPE_PRESENT.equals(fee.getPay_type())){//如果是赠送
			feeComponent.updateGeneralAcct(-fee.getReal_pay());
		}else if(fee.getPay_type().indexOf(SystemConstants.PAY_TYPE_UNITPRE) > -1){//预收款
			CFeePay feePay = feeComponent.queryCFeePaybyFeeSn(fee.getFee_sn());
			if(StringHelper.isNotEmpty(feePay.getReceipt_id())){
				feeComponent.updateCredential(-fee.getReal_pay(),feePay.getReceipt_id());
			}
		}else if(SystemConstants.PAY_TYPE_DEZS.equals(fee.getPay_type())){//定额赠送
			TCountyAcctChange change = acctComponent.queryChangeByDoneCode(fee.getCreate_done_code());
			if(null != change){
				TCountyAcct countyAcct = acctComponent.queryAcctConfig(change.getT_acct_id());
				countyAcct.setBalance(countyAcct.getBalance() + fee.getReal_pay());
				acctComponent.updateCountyAcct(countyAcct);
				acctComponent.deleteCountyAcctChange(fee.getCreate_done_code());
			}
		}
		
		//重载操作员未打印的费用
		String optrId = getOptr().getOptr_id();
		List<String> feeSnList = feeComponent.queryUnPrintFeeByOptr(optrId);
		MemoryPrintData.reloadOptrFee(optrId, feeSnList);
		
	}

	/**
	 * 判断产品状态是否为开
	 * @param status
	 * @return
	 */
	protected boolean isProdOpen(String status) throws Exception {
		/*if (status.equals(StatusConstants.ACTIVE) || status.equals(StatusConstants.OWENOTSTOP) 
				|| status.equals(StatusConstants.TMPOPEN) || status.equals(StatusConstants.LNKUSTOP) 
				|| status.equals(StatusConstants.PAUSE) )
			return true;
		else
			return false;*/
		return userProdComponent.isProdOpen(status);
	}
	
	/**
	 * 根据卡号更新用户历史表中的巡检标志
	 * @param cardId
	 */
	protected void updateUserCheckFlag(String cardId) throws Exception{
		if (StringHelper.isNotEmpty(cardId)){
			userComponent.updateUserCheckFlag(cardId);
		}
	}

	/**
	 * 新增用户时创建业务指令
	 * @param user
	 * @param cust
	 * @param doneCode
	 * @throws Exception
	 */
	protected void createUserJob(CUser user, String custId, Integer doneCode)
			throws Exception {
		authComponent.sendAuth(user, null, BusiCmdConstants.CREAT_USER, doneCode);
	}

	/**
	 * 删除用户是创建业务指令
	 * @param user
	 * @param cust
	 * @param doneCode
	 * @throws Exception
	 */
	protected void delUserJob(CUser user, String custId, Integer doneCode)
		throws Exception {
		CUser userDto = queryUserById(user.getUser_id());
		
		jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.DEL_USER,custId,
			user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), null,null,JsonHelper.fromObject(userDto));
		if (StringHelper.isNotEmpty(user.getCard_id())){
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_TERMINAL,custId,
			user.getUser_id(), user.getStb_id(), user.getCard_id(), null, null,null);
		}
		if (StringHelper.isNotEmpty(user.getModem_mac())){
			jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_TERMINAL,custId,
					user.getUser_id(), null,null, user.getModem_mac(), null,null);
		}
	}
	
	/**
	 * 返回userdto
	 * @param userId
	 * @return
	 * @throws JDBCException
	 */
	protected CUser queryUserById(String userId) throws JDBCException{
		return userComponent.queryUserById(userId);
	}

	/**
	 * 修改用户状态
	 * @param userId
	 * @param oldStatus
	 * @param newStatus
	 */
	protected void updateUserStatus(Integer doneCode,String userId,String oldStatus,String newStatus) throws Exception{
		CUser user = userComponent.queryUserById(userId);

		List<CUserPropChange> changeList = new ArrayList<CUserPropChange>();
		changeList.add(new CUserPropChange("status", oldStatus, newStatus));
		changeList.add(new CUserPropChange("status_date", DateHelper.dateToStr(user.getStatus_date()), DateHelper.dateToStr(new Date())));
		userComponent.editUser(doneCode, userId, changeList);
	}

	protected void updateUserDyn(Integer doneCode,String userId,String columnName,String oldStatus,String newStatus) throws Exception{
		CUserPropChange propChange = new CUserPropChange();
		propChange.setColumn_name(columnName);
		propChange.setOld_value(oldStatus);
		propChange.setNew_value(newStatus);

		List<CUserPropChange> changeList = new ArrayList<CUserPropChange>();
		changeList.add(propChange);
		userComponent.editUser(doneCode, userId, changeList);
	}

	/**
	 * 更换设备
	 * @param oldStbId
	 * @param oldCardId
	 * @param newStbId
	 * @param newCardId
	 * @param changeOwnership
	 * @param param
	 * @param cust
	 * @param doneCode
	 * @throws JDBCException
	 * @throws Exception
	 */
	protected String changeStbCard(boolean singleCard, String oldStbId,
			String oldCardId, String oldModemId, String newStbId,
			String newCardId, String newModemId, String custId,
			Integer doneCode, String busiCode) throws JDBCException, Exception {
		String busiInfo = "";
		oldStbId = oldStbId == null ? "" : oldStbId;
		oldCardId = oldCardId == null ? "" : oldCardId;
		oldModemId = oldModemId == null ? "" : oldModemId;

		newStbId = newStbId == null ? "" : newStbId;
		newCardId = newCardId == null ? "" : newCardId;
		newModemId = newModemId == null ? "" : newModemId;
		
		if (StringHelper.isEmpty(newStbId))
			newStbId = oldStbId;
		if (StringHelper.isEmpty(newCardId))
			newCardId = oldCardId;
		if (StringHelper.isEmpty(newModemId))
			newModemId = oldModemId;
		
		//如果是一体机
		if(singleCard){
			newStbId = "";
		}
		
		DeviceDto oldStb = null;
		DeviceDto oldCard = null;
		DeviceDto oldModem = null;
		
		DeviceDto newStb = null;
		DeviceDto newCard = null;
		DeviceDto newModem = null;
		
		boolean isChangeCard = false;
		boolean isChangeModem = false;

		if (oldStbId.equals(newStbId)){
			//只更换智能卡
			if(StringHelper.isNotEmpty(oldCardId) && !oldCardId.equals(newCardId)){
				
				oldCard = deviceComponent.queryDeviceByDeviceCode(oldCardId);
				newCard = deviceComponent.queryDeviceByDeviceCode(newCardId);
				
				oldStb = deviceComponent.queryDeviceByDeviceCode(oldStbId);
				if(oldStb != null){
					boolean flag = deviceComponent.isPair(oldStb.getDevice_model(), newCard.getDevice_model());
					if(!flag){
						throw new ComponentException("机顶盒型号和卡型号不能配对!");
					}
				}
				
				//将新设备转到客户名下
				custComponent.transDevice(doneCode,custId, newCard);
				//修改原card的客户设备状态为空闲,修改新的客户设备的状态为使用
				custComponent.updateDeviceStatus(custId, oldCard.getDevice_id(), StatusConstants.IDLE);
				custComponent.updateDeviceStatus(custId, newCard.getDevice_id(), StatusConstants.USE);
				if (StringHelper.isNotEmpty(oldStbId)){
					oldStb = deviceComponent.queryDeviceByDeviceCode(oldStbId);
					//判断原机卡是否是配对的，如果是修改原机顶盒的配对卡号为新卡号
					if (oldStb.getPairCard() != null && StringHelper.isNotEmpty(oldStb.getPairCard().getDevice_id())){
						deviceComponent.savePairStbChange(doneCode, busiCode, oldStb.getPairCard().getDevice_id(),oldStb.getDevice_id(),"",true);
						deviceComponent.updatePairCard(doneCode, busiCode, oldStb
								.getDevice_id(), oldStb.getPairCard().getDevice_id(),
								newCard.getDevice_id(),true);
						custComponent.updatePairCard(oldStb.getDevice_id(), newCard
								.getDevice_id(), newCard.getDevice_code());
					}
				}
			}
			if(!oldModemId.equals(newModemId)){
				isChangeModem = true;
				if(StringHelper.isNotEmpty(oldModemId))
					oldModem = deviceComponent.queryDeviceByDeviceCode(oldModemId);
				newModem = deviceComponent.queryDeviceByDeviceCode(newModemId);
				//将新设备转到客户名下
				custComponent.transDevice(doneCode,custId, newModem);
				//修改原MODEM的客户设备状态为空闲,修改新的客户设备的状态为使用
				if(StringHelper.isNotEmpty(oldModemId))
					custComponent.updateDeviceStatus(custId, oldModem.getDevice_id(), StatusConstants.IDLE);
				custComponent.updateDeviceStatus(custId, newModem.getDevice_id(), StatusConstants.USE);
				if (StringHelper.isNotEmpty(oldStbId)){
					oldStb = deviceComponent.queryDeviceByDeviceCode(oldStbId);
					//判断原机MODEM是否是配对的，如果是修改原机顶盒的配对MODEM号为新MODEM号
					if (oldStb.getPairModem() != null && StringHelper.isNotEmpty(oldStb.getPairModem().getDevice_id())){
						deviceComponent.updatePairModem(doneCode, busiCode, oldStb
							.getDevice_id(), oldStb.getPairModem().getDevice_id(),
							newModem.getDevice_id(),true);
						custComponent.updatePairModem(oldStb.getDevice_id(), newModem.getDevice_id(), newModemId);
					}
				}
			}
		} else if (oldCardId.equals(newCardId)){
			//只更换机顶盒
			oldStb = deviceComponent.queryDeviceByDeviceCode(oldStbId);
			newStb = deviceComponent.queryDeviceByDeviceCode(newStbId);
			
			oldCard = deviceComponent.queryDeviceByDeviceCode(oldCardId);
			if(oldCard != null && newStb != null){
				boolean flag = deviceComponent.isPair(newStb.getDevice_model(), oldCard.getDevice_model());
				if(!flag){
					throw new ComponentException("机顶盒型号和卡型号不能配对!");
				}
			}
			
			if(oldStb != null){
				//修改原card的客户设备状态为空闲,修改新的客户设备的状态为使用
				custComponent.updateDeviceStatus(custId, oldStb.getDevice_id(), StatusConstants.IDLE);
				//判断原机卡是否是配对的，如果是修改原机顶盒的配对卡号为空，修改新机顶盒的配对卡号为原卡号
				if (oldStb.getPairCard() != null && StringHelper.isNotEmpty(oldStb.getPairCard().getDevice_id())){
					deviceComponent.updatePairCard(doneCode,busiCode,oldStb.getDevice_id(),oldStb.getPairCard().getDevice_id(),"",true);
					deviceComponent.updatePairCard(doneCode,busiCode,newStb.getDevice_id(),"",oldStb.getPairCard().getDevice_id(),true);
					custComponent.updatePairCard(oldStb.getDevice_id(), "","");
					custComponent.updatePairCard(newStb.getDevice_id(), oldStb.getPairCard().getDevice_id(),oldStb.getPairCard().getCard_id());
				}
			}
			
			//将新设备转到客户名下
			if(newStb != null){
				custComponent.transDevice(doneCode,custId, newStb);
				custComponent.updateDeviceStatus(custId, newStb.getDevice_id(), StatusConstants.USE);
			}
			
			//单向数字 一体机可以更换为单机
			if(!oldStbId.equals(newStbId) 
					&& (oldStb.getPairModem() != null && oldStb.getPairModem().getIs_virtual().equals(SystemConstants.BOOLEAN_TRUE))
					&& newStb.getPairModem() == null){
				UserDto userDto = userComponent.queryUserByDeviceId(oldStbId);
				if(userDto.getServ_type().equals(SystemConstants.DTV_SERV_TYPE_SINGLE)){
					newModemId = "";
				}
			}
			
			if(!oldModemId.equals(newModemId) && !isChangeModem){
				//更换MODEM
				oldModem = deviceComponent.queryDeviceByDeviceCode(oldModemId);
				newModem = deviceComponent.queryDeviceByDeviceCode(newModemId);
				
				if(oldModem != null){
					custComponent.updateDeviceStatus(custId, oldModem.getDevice_id(), StatusConstants.IDLE);
					//判断原机MODEM是否是配对的，如果是修改原机顶盒的配对MODEM号为空，修改新机顶盒的配对MODEM号为原MODEM号
					if (oldStb.getPairModem() != null && StringHelper.isNotEmpty(oldStb.getPairModem().getDevice_id())){
					deviceComponent.updatePairModem(doneCode,busiCode,newStb.getDevice_id(),newModem.getDevice_id(),oldStb.getPairModem().getDevice_id(),true);
						RModem rmodem = newStb.getPairModem();
						String modemDevieId = rmodem == null ? "" : rmodem.getDevice_id();
						String modemDevieCoe = rmodem == null ? "" : rmodem.getModem_mac();
						custComponent.updatePairModem(newStb.getDevice_id(), modemDevieId,modemDevieCoe);
					}
				}
				
				if(newModem != null){
					//将新设备转到客户名下
					custComponent.transDevice(doneCode, custId, newModem);
					custComponent.updateDeviceStatus(custId, newModem.getDevice_id(), StatusConstants.USE);
				}
				
			}
			
		}else if(oldModemId.equals(newModemId)){
			//只更换机顶盒
			oldStb = deviceComponent.queryDeviceByDeviceCode(oldStbId);
			newStb = deviceComponent.queryDeviceByDeviceCode(newStbId);
			
			if(oldStb != null){
				//修改原机顶盒的客户设备状态为空闲,修改新的客户设备的状态为使用
				custComponent.updateDeviceStatus(custId, oldStb.getDevice_id(), StatusConstants.IDLE);
				//判断原机MODEM是否是配对的，如果是修改原机顶盒的配对MODEM号为空，修改新机顶盒的配对MODEM号为原MODEM号
				if (oldStb.getPairModem() != null && StringHelper.isNotEmpty(oldStb.getPairModem().getDevice_id())){
					deviceComponent.updatePairModem(doneCode,busiCode,oldStb.getDevice_id(),oldStb.getPairModem().getDevice_id(),"",true);
					deviceComponent.updatePairModem(doneCode,busiCode,newStb.getDevice_id(),"",oldStb.getPairModem().getDevice_id(),true);
					custComponent.updatePairModem(oldStb.getDevice_id(), "","");
					custComponent.updatePairModem(newStb.getDevice_id(), oldStb.getPairModem().getDevice_id(),oldStb.getPairModem().getModem_mac());
				}
			}
			
			if(newStb != null){
				//将新设备转到客户名下
				custComponent.transDevice(doneCode, custId, newStb);
				custComponent.updateDeviceStatus(custId, newStb.getDevice_id(), StatusConstants.USE);
			}
			
			if(!oldCardId.equals(newCardId) && !isChangeCard){
				//更换卡
				oldCard = deviceComponent.queryDeviceByDeviceCode(oldCardId);
				newCard = deviceComponent.queryDeviceByDeviceCode(newCardId);
				
				if(oldCard != null){
					//修改原卡的客户设备状态为空闲,修改新的客户设备的状态为使用
					custComponent.updateDeviceStatus(custId, oldCard.getDevice_id(), StatusConstants.IDLE);
					//判断原机卡是否是配对的，如果是修改原机顶盒的配对卡号为空，修改新机顶盒的配对卡号为原卡号
					if (oldStb.getPairCard() != null && StringHelper.isNotEmpty(oldStb.getPairCard().getDevice_id())){
//					deviceComponent.updatePairCard(doneCode,busiCode,oldStb.getDevice_id(),oldStb.getPairCard().getDevice_id(),newStb.getPairCard().getDevice_id());
						RCard rcard = newStb.getPairCard();
						if(rcard != null)
							custComponent.updatePairCard(newStb.getDevice_id(), rcard.getDevice_id(),rcard.getCard_id());
					}
				}
				
				if(newCard != null){
					//将新设备转到客户名下
					custComponent.transDevice(doneCode, custId, newCard);
					custComponent.updateDeviceStatus(custId, newCard.getDevice_id(), StatusConstants.USE);
				}
				
			}
			
		} else {
			//机卡猫同时更换
			if (StringHelper.isNotEmpty(oldStbId))
				oldStb = deviceComponent.queryDeviceByDeviceCode(oldStbId);
			
			newStb =deviceComponent.queryDeviceByDeviceCode(newStbId);
			oldCard = deviceComponent.queryDeviceByDeviceCode(oldCardId);
			newCard = deviceComponent.queryDeviceByDeviceCode(newCardId);
			
			if(StringHelper.isNotEmpty(oldModemId))
				oldModem = deviceComponent.queryDeviceByDeviceCode(oldModemId);
			newModem = deviceComponent.queryDeviceByDeviceCode(newModemId);
			
			boolean flag = deviceComponent.isPair(newStb.getDevice_model(), newCard.getDevice_model());
			if(!flag){
				throw new ComponentException("机顶盒型号和卡型号不能配对!");
			}
			
			//将新设备转到客户名下
			custComponent.transDevice(doneCode,custId, newStb);
			custComponent.transDevice(doneCode,custId, newCard);
			custComponent.transDevice(doneCode,custId, newModem);
			
			if(oldCard != null){
				//修改原机卡的客户设备状态为空闲,修改新的客户设备的状态为使用
				custComponent.updateDeviceStatus(custId, oldCard.getDevice_id(), StatusConstants.IDLE);
			}
			
			if(newCard != null){
				custComponent.updateDeviceStatus(custId, newCard.getDevice_id(), StatusConstants.USE);
			}
			
			if (oldStb != null){
				custComponent.updateDeviceStatus(custId, oldStb.getDevice_id(), StatusConstants.IDLE);
			}
			if(newStb !=null){
				custComponent.updateDeviceStatus(custId, newStb.getDevice_id(), StatusConstants.USE);
			}
			
			if (oldModem != null){
				custComponent.updateDeviceStatus(custId, oldModem.getDevice_id(), StatusConstants.IDLE);
			}
			if(newModem != null){
				custComponent.updateDeviceStatus(custId, newModem.getDevice_id(), StatusConstants.USE);
			}
		}
		//调用用户组件，更新用户设备编号
		//双向和宽带共用一个MODEM
		List<CUser> userList = null;
		if(StringHelper.isNotEmpty(oldModemId)){
			userList = userComponent.queryUserByDevice(SystemConstants.DEVICE_TYPE_MODEM, oldModemId);
		}else{
			userList = userComponent.queryUserByDevice(SystemConstants.DEVICE_TYPE_CARD, oldCardId);
		}
		if (userList != null && userList.size() > 0){
			for(CUser user : userList){
				String userType = user.getUser_type();
				//钝化用户产品
				List<CProdDto> prodList = userProdComponent.queryAllProdsByUserId(user.getUser_id());
				for (CProdDto prod:prodList){
					if (isProdOpen(prod.getStatus())){
						jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_PROD,
								custId, user.getUser_id(), user.getStb_id(),
								user.getCard_id(), user.getModem_mac(), prod.getProd_sn(),prod.getProd_id());
					}
				}
				//注销设备
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.PASSVATE_TERMINAL,
						custId, user.getUser_id(), user.getStb_id(),
						user.getCard_id(), user.getModem_mac(),null,null);
				
				if(userType.equals(SystemConstants.USER_TYPE_BAND)){
					//userComponent.updateDevice(doneCode, user, null, null, newModemId);
				}else{
					//userComponent.updateDevice(doneCode, user, newStbId, newCardId, newModemId);
				}
				
				String userStbId = StringHelper.isNotEmpty(newStbId)
						&& !userType.equals(SystemConstants.USER_TYPE_BAND) ? newStbId : user.getStb_id();
				String userCardId = StringHelper.isNotEmpty(newCardId)
						&& !userType.equals(SystemConstants.USER_TYPE_BAND) ? newCardId : user.getCard_id();
				String userModemId = StringHelper.isNotEmpty(newModemId) ? newModemId : user.getModem_mac();
				//激活设备
				jobComponent.createBusiCmdJob(doneCode,
						BusiCmdConstants.ACCTIVATE_TERMINAL, custId, user.getUser_id(), 
						userStbId, userCardId, userModemId, null, null);
				
				//激活产品
				for (CProdDto prod:prodList){
					//vod 更换设备不发激活指令
					if (isProdOpen(prod.getStatus()) 
							&& !prod.getServ_id().equals(SystemConstants.PROD_SERV_ID_ITV)){
						jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD,
								custId, user.getUser_id(), userStbId,
								userCardId, userModemId, prod.getProd_sn(),prod.getProd_id());
					}
				}
				if(!busiCode.equals(BusiCodeConstants.USER_SINGLE_CARD)){
					//修改用户指令
					CUser userDto = queryUserById(user.getUser_id());
					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.CHANGE_USER, custId,
							userDto.getUser_id(), userDto.getStb_id(), userDto.getCard_id(), userDto.getModem_mac(), null, null,JsonHelper.fromObject(userDto));
				}
			}
		}
		if(oldStb!=null && newStb!=null){
			getBusiParam().getBusiConfirmParamInfo().put("oldStb", oldStb);
			getBusiParam().getBusiConfirmParamInfo().put("newStb", newStb);
		}
		if(oldCard!=null && newCard!=null){
			getBusiParam().getBusiConfirmParamInfo().put("oldCard", oldCard);
			getBusiParam().getBusiConfirmParamInfo().put("newCard", newCard);
		}
		if(oldModem!=null && newModem!=null){
			getBusiParam().getBusiConfirmParamInfo().put("oldModem", oldModem);
			getBusiParam().getBusiConfirmParamInfo().put("newModem", newModem);
		}
		
		
		updateUserCheckFlag(newCardId);
		return busiInfo;
	}
	
	
	
	/**
	 * 回收设备
	 * @param deviceId
	 * @param deviceStatus
	 * @param fee
	 * @param cust
	 * @param doneCode
	 * @param busiCode
	 * @return
	 * @throws JDBCException
	 * @throws Exception
	 * @throws ComponentException
	 */
	protected String reclaimDevice(String deviceId, String deviceStatus, String reclaimReason,int fee,
			 CCust cust, Integer doneCode, String busiCode)
			throws JDBCException, Exception, ComponentException {
		//查询设备的基本信息
		DeviceDto device = deviceComponent.queryDeviceByDeviceId(deviceId);
		DeviceDto pairDevice = null, pairModemDevice = null;
		String busiInfo  = "设备编号："+device.getDevice_code();
		
		getBusiParam().setBusiConfirmParam("device", device);
		if (device.getPairCard() != null){
			pairDevice = deviceComponent.queryDeviceByDeviceId(device.getPairCard().getDevice_id());
			getBusiParam().setBusiConfirmParam("paired_card", pairDevice);
		}
		if (device.getPairModem() != null){
			pairModemDevice = deviceComponent.queryDeviceByDeviceId(device.getPairModem().getDevice_id());
			getBusiParam().setBusiConfirmParam("paired_modem", pairModemDevice);
		}
		
		String deviceType = device.getDevice_type();
		List<CUser> userList = userComponent.queryUserByDevice(device.getDevice_type(), device.getDevice_code());
		//如果还有不是报亭状态用户在使用这个设备，提示操作员数据
		if(userList!=null && userList.size() > 0){
			for(CUser cuser : userList){
				//柬埔寨 销户回收设备，过滤这个判断
				if(!BusiCodeConstants.USER_WRITE_OFF.equals(busiCode) && !BusiCodeConstants.USER_HIGH_WRITE_OFF.equals(busiCode) 
						&& !StatusConstants.REQSTOP.equals(cuser.getStatus())){
					throw new ServicesException("该设备还在被用户使用，不能回收");
				}
				String stb_id = cuser.getStb_id();
				String card_id = cuser.getCard_id();
				String modem_mac = cuser.getModem_mac();
				
				if (deviceType.equals(SystemConstants.DEVICE_TYPE_STB)){
					stb_id = null;
					if(pairDevice !=null){
						card_id = null;
					}
					if(pairModemDevice !=null){
						modem_mac = null;
					}
				}else if (deviceType.equals(SystemConstants.DEVICE_TYPE_CARD)){
					card_id = null;
				}else if (deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)){
					modem_mac = null;
				}
				//userComponent.updateDevice(doneCode, cuser, stb_id, card_id, modem_mac);
			}
		}
		//如果收取了折旧费，保存折旧费信息
		if (fee>0){
			//查找折旧费id
			String feeId = busiConfigComponent.queryZjFeeId();
			String payType = SystemConstants.PAY_TYPE_CASH;
			if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
				payType = this.getBusiParam().getPay().getPay_type();
			feeComponent.saveDeviceFee( cust.getCust_id(),cust.getAddr_id(), feeId,null,payType,device.getDevice_type(),
					deviceId, device.getDevice_code(),
					pairDevice==null?null:pairDevice.getDevice_id(),
					pairDevice==null?null:pairDevice.getDevice_code(),
					pairModemDevice==null?null:pairModemDevice.getDevice_id(),
					pairModemDevice==null?null:pairModemDevice.getDevice_code(),device.getDevice_model(),
					fee, doneCode,doneCode, busiCode, 1);
		}
		//待回收
		deviceComponent.saveDeviceReclaim(doneCode, busiCode, deviceId, cust.getCust_id(),reclaimReason);
		if (pairDevice != null && pairDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE)){
			deviceComponent.saveDeviceReclaim(doneCode, busiCode, device
					.getPairCard().getDevice_id(), cust.getCust_id(),reclaimReason);
		}
		if (pairModemDevice != null && pairModemDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE)){
			deviceComponent.saveDeviceReclaim(doneCode, busiCode, device
					.getPairModem().getDevice_id(), cust.getCust_id(),reclaimReason);
		}
		
		deviceComponent.saveDeviceUseRecords(doneCode, busiCode, device.getDevice_id(),
				device.getDevice_type(), device.getDevice_code(), cust.getCust_id(), cust.getCust_no());
		if (pairDevice != null){
			deviceComponent.saveDeviceUseRecords(doneCode, busiCode, pairDevice.getDevice_id(),
					pairDevice.getDevice_type(), pairDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
		}
		if (pairModemDevice != null){
			deviceComponent.saveDeviceUseRecords(doneCode, busiCode, pairModemDevice.getDevice_id(),
					pairModemDevice.getDevice_type(), pairModemDevice.getDevice_code(), cust.getCust_id(), cust.getCust_no());
		}
		
		return busiInfo;
	}
	
	public void saveRemoveDevice(Integer doneCode, String busiCode, CCust cust,
			String deviceId, String deviceStatus) throws Exception {
		//查询设备的基本信息
		DeviceDto device = deviceComponent.queryDeviceByDeviceId(deviceId);
		DeviceDto pairDevice = null, pairModemDevice = null;
		if (device.getPairCard() != null){
			pairDevice = deviceComponent.queryDeviceByDeviceId(device.getPairCard().getDevice_id());
		}
		if (device.getPairModem() != null){
			pairModemDevice = deviceComponent.queryDeviceByDeviceId(device.getPairModem().getDevice_id());
		}
		
		List<CUser> userList = userComponent.queryUserByDevice(device.getDevice_type(), device.getDevice_code());
		//如果还有用户在数据这个设备，提示操作员数据
		if(userList!=null && userList.size() > 0){
			throw new ServicesException("该设备还在被用户使用，不能回收");
		}
		
		//仓库设备回收，确认后，取消，再次确认时，c_cust_device已删除
		if(cust != null){
			//回收客户设备，并判断是否有配对的设备,有的话也一起收回
			custComponent.removeDevice(cust.getCust_id(), deviceId, doneCode, SystemConstants.BOOLEAN_TRUE);
			if (pairDevice != null && pairDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE)){
				custComponent.removeDevice(cust.getCust_id(), pairDevice.getDevice_id(),doneCode, SystemConstants.BOOLEAN_TRUE);
			}
			if (pairModemDevice != null && pairModemDevice.getIs_virtual().equals(SystemConstants.BOOLEAN_FALSE)){
				custComponent.removeDevice(cust.getCust_id(), pairModemDevice.getDevice_id(),doneCode, SystemConstants.BOOLEAN_TRUE);
			}
		}
		//判断回收后的设备状态和当前设备状态不一致，修改设备状态,并修改对应配对的设备
		if (!device.getDevice_status().equals(deviceStatus)){
			deviceComponent.updateDeviceStatus(doneCode, busiCode, deviceId,device.getDevice_status(), deviceStatus,true);
			if (pairDevice != null){
				deviceComponent.updateDeviceStatus(doneCode, busiCode,
						device.getPairCard().getDevice_id(), pairDevice.getDevice_status(),deviceStatus,false);
			}
			if (pairModemDevice != null){
				deviceComponent.updateDeviceStatus(doneCode, busiCode,
						pairModemDevice.getDevice_id(), pairModemDevice.getDevice_status(),deviceStatus,false);
			}
		}
		//修改设备仓库状态,并修改对应配对的设备
		deviceComponent.updateDeviceDepotStatus(doneCode, busiCode, deviceId,device.getDepot_status(), StatusConstants.IDLE,null,true);
		if (pairDevice != null){
			deviceComponent.updateDeviceDepotStatus(doneCode, busiCode,
					device.getPairCard().getDevice_id(),pairDevice.getDepot_status(), StatusConstants.IDLE,null,false);
		}
		if (pairModemDevice != null){
			deviceComponent.updateDeviceDepotStatus(doneCode, busiCode,
					pairModemDevice.getDevice_id(),pairModemDevice.getDepot_status(), StatusConstants.IDLE,null,false);
		}
		//修改设备的产权为广电
		deviceComponent.updateDeviceOwnership(doneCode, busiCode, deviceId, device.getOwnership(), SystemConstants.OWNERSHIP_GD,null,true);
		if (pairDevice != null){
			deviceComponent.updateDeviceOwnership(doneCode, busiCode, pairDevice.getDevice_id(), pairDevice.getOwnership(), SystemConstants.OWNERSHIP_GD,null,false);
		}
		if (pairModemDevice != null){
			deviceComponent.updateDeviceOwnership(doneCode, busiCode, pairModemDevice.getDevice_id(), pairModemDevice.getOwnership(), SystemConstants.OWNERSHIP_GD,null,false);
		}
		//设备回收新机变成旧机
		if(StringHelper.isEmpty(device.getIs_new_stb()) || SystemConstants.BOOLEAN_TRUE.equals(device.getIs_new_stb())){
			deviceComponent.updateDeviceIsNewStb(doneCode, busiCode, deviceId, device.getIs_new_stb(), SystemConstants.BOOLEAN_FALSE,true);
		}
		//修改设备库位
		deviceComponent.updateDeviceDepotId(doneCode, busiCode, deviceId, device.getDepot_id(), getOptr().getDept_id(),null,true);
		if (pairDevice != null){
			deviceComponent.updateDeviceDepotId(doneCode, busiCode, pairDevice.getDevice_id(), pairDevice.getDepot_id(), getOptr().getDept_id(),null,false);
		}
		if (pairModemDevice != null){
			deviceComponent.updateDeviceDepotId(doneCode, busiCode, pairModemDevice.getDevice_id(), pairModemDevice.getDepot_id(), getOptr().getDept_id(),null,false);
		}
	}
	
	/**
	 * 修改资费
	 * @param prodSn
	 * @param newTariffId
	 * @param effDate
	 * @param expDate
	 * @param delFlag 删除原来未生效资费
	 * @param isUpdate 是否更新到期日期
	 * @param doneCode
	 * @throws JDBCException
	 * @throws Exception
	 */
	protected void changeTariff(String prodSn, String newTariffId,
			String effDate, String expDate,boolean delFlag,boolean isUpdate, Integer doneCode)
			throws JDBCException, Exception {
		   //由 effDate.equals(DateHelper.formatNow()) 改为 !DateHelper.now().before(DateHelper.strToDate(effDate))
		   // 生效日期 等于 当前日期 ，改为 生效日期 小于等于 当前日期
		CProd prod = userProdComponent.queryByProdSn(prodSn);
		if (prod ==null){
			return;
		}
		//产品尚未到期
		if (!DateHelper.now().before(DateHelper.strToDate(effDate))){
			userProdComponent.updateProdTariff(doneCode, prodSn, newTariffId);
			//生成信用度计算任务
			List<CAcctAcctitem> acctItemList = new ArrayList<CAcctAcctitem>();
			CAcctAcctitem acctItem = new CAcctAcctitem();
			acctItem.setAcct_id(prod.getAcct_id());
			acctItem.setAcctitem_id(prod.getProd_id());
			acctItemList.add(acctItem);
			jobComponent.createCreditCalJob(doneCode, prod.getCust_id(), acctItemList,SystemConstants.BOOLEAN_TRUE);
			
			PProdTariff oldTariff = userProdComponent.queryProdTariffById(prod.getTariff_id());
			getBusiParam().setBusiConfirmParam("old_tariff", oldTariff);
			
			if(oldTariff.getBilling_cycle() > 1  && oldTariff.getRent()>0){
				List<BBill> billList = billComponent.queryMuchBill(prodSn, DateHelper.nowYearMonth(), SystemConstants.BILL_COME_FROM_MUCH);
				int oweFee = 0;
				for(BBill bill : billList){
					billComponent.cancelBill(bill.getBill_sn());
					oweFee += bill.getOwe_fee();
				}
				if(oweFee != 0){
					acctComponent.changeAcctItemOwefee(false, prod.getAcct_id(), prod.getProd_id(), oweFee*-1);
				}
			}else{
				if (DateHelper.getCurrDAY()>1){
					BBill bill = billComponent.confirmBill(prod.getProd_sn(), doneCode);
					if (bill !=null && bill.getOwe_fee()>0){
						acctComponent.changeAcctItemOwefee(true, prod.getAcct_id(), prod.getProd_id(), bill.getOwe_fee());
					}
				}
			}
			jobComponent.createCustWriteOffJob(doneCode, prod.getCust_id(), SystemConstants.BOOLEAN_TRUE);
			
			//基本包0资费+30年
			PProd p = userProdComponent.queryByProdId(prod.getProd_id());
			if(p.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)){//基本产品
				PProdTariff newTariff = userProdComponent.queryProdTariffById(newTariffId);
				if(newTariff.getRent() == 0){
					String newValue = null;
					Calendar calendar = Calendar.getInstance(); 
					calendar.setTime(DateHelper.strToDate(effDate));
					calendar.add(Calendar.YEAR, 30);
					newValue = DateHelper.format(calendar.getTime(), DateHelper.FORMAT_YMD);
					userProdComponent.updateInvalidDate(doneCode, prodSn, DateHelper.strToDate(newValue));
				}else{
					userProdComponent.updateInvalidDate(doneCode, prodSn, DateHelper.strToDate(effDate));
					CAcctAcctitem acctitem = acctComponent.queryAcctItemByAcctitemId(prod.getAcct_id(), prod.getProd_id());
					if(oldTariff.getBilling_cycle() > 1){
						userProdComponent.updateInvalidDate(doneCode, prodSn, 0, acctitem.getActive_balance() - acctitem.getOwe_fee(), acctitem);
					}else if(oldTariff.getBilling_cycle() == 1){
						userProdComponent.updateInvalidDate(doneCode, prodSn, 0, acctitem.getReal_balance(), acctitem);
					}
				}
			}else{//非基本产品
				if(oldTariff.getRent()>0){
					userProdComponent.updateInvalidDate(doneCode, prodSn, DateHelper.strToDate(effDate));
					CAcctAcctitem acctitem = acctComponent.queryAcctItemByAcctitemId(prod.getAcct_id(), prod.getProd_id());
					if(oldTariff.getBilling_cycle() > 1){
						userProdComponent.updateInvalidDate(doneCode, prodSn, 0, acctitem.getActive_balance() - acctitem.getOwe_fee(), acctitem);
					}else if(oldTariff.getBilling_cycle() == 1){
						userProdComponent.updateInvalidDate(doneCode, prodSn, 0, acctitem.getReal_balance(), acctitem);
					}
				}
			}
			
			
			
			//产品状态暂停修改为正常
			if(prod.getStatus().equals(StatusConstants.TMPPAUSE)){
				userProdComponent.updateProdStatus(doneCode, prod.getProd_sn(), StatusConstants.TMPPAUSE, StatusConstants.ACTIVE);
				CUser  user = userComponent.queryUserById(prod.getUser_id());
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, user.getCust_id(),
						user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), prod.getProd_sn(),prod.getProd_id());
				
			}
		} else {
			//产品已到期
			String oldTariffId = prod.getTariff_id();
//			JProdNextTariff tariff = jobComponent.queryNextTariffJob(prodSn,newTariffId,getOptr().getCounty_id());
//			if(tariff != null && effDate.equals(DateHelper.dateToStr(tariff.getEff_date()))){
			//相同资费不插资费变更
			if(!oldTariffId.equals(newTariffId)){
				//促销,资费不同
				//插入第二条未生效资费时，旧资费为第一条新资费
				JProdNextTariff nextTariff = jobComponent.queryTariffJob(doneCode);
				if(nextTariff != null && nextTariff.getProd_sn().equals(prodSn))
					oldTariffId = nextTariff.getTariff_id();
				jobComponent.createNewProdTariffJob(doneCode, prodSn, newTariffId, oldTariffId, effDate,delFlag);
				//修改用户产品的未生效资费
				if(StringHelper.isEmpty(prod.getNext_tariff_id()) || 
						delFlag){
					userProdComponent.updateNextTariff(prodSn, newTariffId);	
				}
				
				//修改公用账目使用类型
				changeNoneToAll(doneCode, prod);
			}
		}
		
		//判断是否修改产品到期日
		if (StringHelper.isNotEmpty(expDate)){
			userProdComponent.updateInvalidDate(doneCode, prodSn, DateHelper.strToDate(expDate));
		}else{
			if(isUpdate){
				//isUpdate是否需要修改到期日
				//套餐子产品不修改到期日
				if(StringHelper.isNotEmpty(prod.getPackage_sn())){
					CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(prod.getAcct_id(),prod.getProd_id());
					userProdComponent.updateInvalidDateByTariff(doneCode,  prodSn, acctItem);
				}
			}
		}
		//账务模式判断
		jobComponent.createAcctModeCalJob(doneCode, prod.getCust_id());
	}
	
	/**
	 * 修改产品公用账目使用类型，NONE TO ALL
	 * @param doneCode
	 * @param prod
	 * @throws Exception
	 */
	protected void changeNoneToAll(Integer doneCode,CProd prod) throws Exception{
		List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
		if(!SystemConstants.PROD_ORDER_TYPE_ORDER.equals(prod.getOrder_type())){
			//订购方式转为订购
			CProdPropChange change1 = new CProdPropChange("order_type",prod.getOrder_type(),SystemConstants.PROD_ORDER_TYPE_ORDER);
			changeList.add(change1);
		}
		
		if(SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE.equals(prod.getPublic_acctitem_type())){
			PProdTariff tariff = prodComponent.queryTariffById(prod.getTariff_id());
			//包单月的资费
			if(tariff.getBilling_cycle() == 1 && SystemConstants.BILLING_TYPE_MONTH.equals(tariff.getBilling_type())){
				//潜江可用公用账目类型转为专项公用，其余转为都可以用
				CProdPropChange change2 = null;
				if(SystemConstants.COUNTY_9005.equals(prod.getCounty_id())){
					change2 = new CProdPropChange("public_acctitem_type",prod.getPublic_acctitem_type(),SystemConstants.PUBLIC_ACCTITEM_TYPE_SPEC_ONLY);
				}else{
					change2 = new CProdPropChange("public_acctitem_type",prod.getPublic_acctitem_type(),SystemConstants.PUBLIC_ACCTITEM_TYPE_ALL);
				}
				
				changeList.add(change2);
			}
		}
		
		userProdComponent.editProd(doneCode, prod.getProd_sn(), changeList);
	}
	
	
	/**
	 * 保存账户缴费
	 * @param doneCode
	 * @param pay
	 * @throws Exception
	 */
	public void saveAcctPay(int doneCode,PayDto pay) throws Exception{
		String custId = pay.getCust_id();
		String addrId = getBusiParam().getCust().getAddr_id();
		if(StringHelper.isEmpty(custId)){
			custId = this.getBusiParam().getCust().getCust_id();
		}
		
		String payType = SystemConstants.PAY_TYPE_UNPAY;
		if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
			payType = this.getBusiParam().getPay().getPay_type();
		String busiCode = this.getBusiParam().getBusiCode();
		
		if(SystemConstants.PAY_TYPE_UNITPRE.equals(payType)){
			//如果是预收款支付的
			String receipId = this.getBusiParam().getPay().getReceipt_id();
			CGeneralCredential credential = acctComponent.queryCredentialById(receipId);
			if(null == credential){
				throw new ServicesException("凭据号有误，请重新输入");
			}else{
				int percent = credential.getPercent();
				int fee = pay.getFee();//缴费金额
				int present = fee * percent / 100;//预收款中赠送金额
				present = (present > 0) ? present : pay.getPresent_fee();
				int cash = 0;
				
				//合同款无赠送比例，直接现金缴费，赠送金额按解冻赠送
				//有，则减去赠送的，剩下的现金缴费
				if(percent == 0) cash = fee;
				else cash = fee - present;
				
				//先记录合同现金
				pay.setFee(cash);
				CFeeAcct acctFee = feeComponent.saveAcctFee(custId,addrId, pay, doneCode, busiCode, SystemConstants.PAY_TYPE_UNITPRE_CASH);
				this.changeAcctFee(doneCode, acctFee);
				
				if(percent > 0){
					//在记录合同赠送金额
					pay.setFee(present);
					pay.setPresent_fee(0);
					acctFee = feeComponent.saveAcctFee(custId,addrId, pay, doneCode, busiCode, SystemConstants.PAY_TYPE_UNITPRE_PRESENT);
					this.changeAcctFee(doneCode, acctFee);
				}
			}
		}else if(SystemConstants.PAY_TYPE_DEZS.equals(payType)){//定额赠送
			CCust cust = this.getBusiParam().getCust();
			TCountyAcct countyAcct = acctComponent.queryAcctConfig(cust.getCust_colony(),cust.getCounty_id());
			if(countyAcct == null){
				throw new ServicesException("未配置定额账户");
			}
			if(countyAcct.getBalance() < pay.getFee()){
				throw new ServicesException("定额账户余额不足");
			}
			
			//更新定额账户的使用
			acctComponent.updateCoutyAcct(countyAcct, doneCode, pay.getFee());
			
			CFeeAcct acctFee = feeComponent.saveAcctFee(custId,addrId, pay, doneCode, busiCode, payType);
			this.changeAcctFee(doneCode, acctFee);
		}else{
			CFeeAcct acctFee = feeComponent.saveAcctFee(custId,addrId, pay, doneCode, busiCode, payType);
//			pay.getFee()
			this.changeAcctFee(doneCode, acctFee);
		}
		
	}
	
	protected void changeAcctFee(int doneCode,
			CFeeAcct feeAcct) throws JDBCException, Exception {
		if (feeAcct == null)
			return;
		
		//查找变化前的账目信息
		CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(feeAcct.getAcct_id(), feeAcct.getAcctitem_id());
		if (acctItem == null)
			return ;
		
		int changeFee= feeAcct.getReal_pay();//本次余额的变化量，含冻结金额
		String acctFeeType=this.acctComponent.getFeeType(feeAcct.getPay_type());
		
		
		if (StringHelper.isNotEmpty(feeAcct.getDisct_id())){
			//享受折扣的缴费必须是现金
//			if(!acctFeeType.equals(SystemConstants.ACCT_FEETYPE_CASH)){
//				throw new ServicesException("必须现金缴费才能享受折扣");
//			}
			
			PProdTariffDisct disct = userProdComponent.queryDistById(feeAcct.getDisct_id());
			String refund = disct.getRefund();
			String trans = disct.getTrans();
			//如折扣配置不可退，不可转，则acctFeeType为赠送
			if(SystemConstants.BOOLEAN_FALSE.equals(refund) && 
					SystemConstants.BOOLEAN_FALSE.equals(trans)){
				acctFeeType = SystemConstants.ACCT_FEETYPE_ZKXJYH;
			}
		}
		
		//修改账户余额
		if (feeAcct.getShould_pay()>0){
			//缴费
			acctComponent.changeAcctItemBanlance(doneCode, feeAcct.getBusi_code(),
				feeAcct.getCust_id(), feeAcct.getAcct_id(), feeAcct.getAcctitem_id(),
				SystemConstants.ACCT_CHANGE_CFEE , acctFeeType, feeAcct.getReal_pay(), null);
//			//如果有免费期
//			if (feeAcct.getProd_free_days()>0){
//				userProdComponent.addProdRscAcct(doneCode, feeAcct.getProd_sn(), SystemConstants.PROD_FREE_TYPE_DAY, feeAcct.getProd_free_days());
//			}
			//判断是否打折，如果打折则增加冻结资金
			if (StringHelper.isNotEmpty(feeAcct.getDisct_id())){
				//根据资费id和折扣id获取资费和折扣信息
				CAcctAcctitemInactive inactiveItem =new CAcctAcctitemInactive();
				inactiveItem.setFee_sn(feeAcct.getFee_sn());
				inactiveItem.setCust_id(feeAcct.getCust_id());
				inactiveItem.setAcct_id(feeAcct.getAcct_id());
				inactiveItem.setAcctitem_id(feeAcct.getAcctitem_id());
				inactiveItem.setDone_code(doneCode);

				PProdTariff tariff = null;
				if(StringHelper.isNotEmpty(feeAcct.getTariff_id())){
					tariff = prodComponent.queryTariffById(feeAcct.getTariff_id());
					if (tariff != null){
						inactiveItem.setCycle(tariff.getBilling_cycle());
					}
				}
				PProdTariffDisct disct = null;
				if(StringHelper.isNotEmpty(feeAcct.getDisct_id())){
					disct = prodComponent.queryDisctById(feeAcct.getDisct_id());
					if (disct != null){
						inactiveItem.setActive_amount(disct.getDisct_rent());
						//int disctAmount = feeAcct.getReal_pay()/disct.getFinal_rent()*disct.getDisct_rent();
						//inactiveItem.setInit_amount(disctAmount);
						//inactiveItem.setBalance(disctAmount);
					}
				}
				acctComponent.addAcctItemInactive(inactiveItem);
				changeFee += inactiveItem.getBalance();
			} else if (feeAcct.getShould_pay()>feeAcct.getReal_pay()){
				int presentAmount = feeAcct.getShould_pay() - feeAcct.getReal_pay();
				//有赠送金额
				CAcctAcctitemInactive inactiveItem =new CAcctAcctitemInactive();
				inactiveItem.setFee_sn(feeAcct.getFee_sn());
				inactiveItem.setCust_id(feeAcct.getCust_id());
				inactiveItem.setAcct_id(feeAcct.getAcct_id());
				inactiveItem.setAcctitem_id(feeAcct.getAcctitem_id());
				inactiveItem.setCycle(1);
				inactiveItem.setInit_amount(presentAmount);
				inactiveItem.setBalance(presentAmount);
				inactiveItem.setActive_amount(presentAmount);
				inactiveItem.setDone_code(doneCode);
				acctComponent.addAcctItemInactive(inactiveItem);
				changeFee += inactiveItem.getBalance();
			}
		} else {
			//退款
			//查找资金明细
			List<AcctAcctitemActiveDto> activeList = acctComponent.queryActiveById(feeAcct.getAcct_id(), feeAcct.getAcctitem_id());
			//让调账可退记录排前面，先退调账可退
			Collections.sort(activeList, new Comparator<AcctAcctitemActiveDto>(){

				public int compare(AcctAcctitemActiveDto o1,
						AcctAcctitemActiveDto o2) {
					return o1.getFee_type().compareTo(o2.getFee_type());
				}
			});
			int needRefund = feeAcct.getReal_pay()*-1;
			
			//实际可退金额
			int realRefund = 0;
			for (AcctAcctitemActiveDto active:activeList){
				//判断是否可退资金
				if (active.getCan_refund().equals(SystemConstants.BOOLEAN_TRUE)){
					int refund = active.getBalance()>=needRefund?needRefund:active.getBalance();
					
					realRefund = realRefund + refund;
					
					acctComponent.changeAcctItemBanlance(doneCode, feeAcct.getBusi_code(),
							feeAcct.getCust_id(), feeAcct.getAcct_id(), feeAcct.getAcctitem_id(),
							SystemConstants.ACCT_CHANGE_REFUND , active.getFee_type(), refund*-1, null);
					needRefund = needRefund -refund;
					if (needRefund==0)
						break;
				}
			}
			
			if(needRefund > 0){
				throw new ServicesException("实际可退余额只剩"+realRefund/100+",请重新查询");
			}
		}
		//TODO 套餐缴费 包多月不更新到期日
		Date invalidDate =new Date();
		if(BusiCodeConstants.PROM_ACCT_PAY.equals(feeAcct.getBusi_code())){
			PProdTariff tariff = userProdComponent.queryProdTariffById(feeAcct.getTariff_id());
			if(tariff==null)throw new ServicesException("系统错误：c_fee_acct.tariff_id("+feeAcct.getTariff_id()+")找不到对应的资费信息.");
			if(tariff.getRent()>0&&tariff.getBilling_cycle()==1){
				invalidDate = userProdComponent.updateInvalidDate(doneCode,  feeAcct.getProd_sn(),0, changeFee, acctItem);
				//更新缴费的到期日
				feeComponent.updateInvalidDate(feeAcct.getFee_sn(),feeAcct.getProd_sn(),invalidDate);
			}
		}else{
			invalidDate = userProdComponent.updateInvalidDate(doneCode,  feeAcct.getProd_sn(),0, changeFee, acctItem);
//			Date invalidDateByFeePro = userProdComponent.getInvalidDateByFeePro(feeAcct.getProd_sn(), payFee);
			//更新缴费的到期日
			feeComponent.updateInvalidDate(feeAcct.getFee_sn(),feeAcct.getProd_sn(),invalidDate);
		}
		//如果是充值卡缴费，并且账目是产品账目，则更新产品的失效日期。
 		if(feeAcct.getPay_type().equals(SystemConstants.PAY_TYPE_CARD)){
			userProdComponent.updateExpDate(doneCode, feeAcct.getProd_sn(), DateHelper.format(invalidDate,DateHelper.FORMAT_YMD));
		}
		
 		
	}



	//处理转账
	public void acctTrans(String  custId, Integer doneCode,
			String busiCode,String outAcctId,  String outAcctItemId, String inAcctId,
			String inAcctItemId, int fee) throws JDBCException, Exception {
		if (fee==0)
			return;
		CAcctAcctitem acctItemOut = acctComponent.queryAcctItemByAcctitemId(outAcctId, outAcctItemId);
		CAcctAcctitem acctItemIn = acctComponent.queryAcctItemByAcctitemId(inAcctId, inAcctItemId);
		acctComponent.trans(custId, doneCode, busiCode, outAcctId, outAcctItemId, inAcctId, inAcctItemId, fee);
		//查找转出账目对应的产品
		CProd prodOut = userProdComponent.queryByAcctItem(outAcctId, outAcctItemId);
		CProd prodIn = userProdComponent.queryByAcctItem(inAcctId, inAcctItemId);
		//更新产品到期日
		if (prodOut != null)
			userProdComponent.updateInvalidDate(doneCode, prodOut.getProd_sn(), 0, fee*-1, acctItemOut);
		if (prodIn != null)
			userProdComponent.updateInvalidDate(doneCode, prodIn.getProd_sn(), 0, fee, acctItemIn);
	}

	/**
	 * 查询用户有效资源
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	public List<UserRes> queryValidRes(String[] userIds)throws Exception{
		return jobComponent.queryValidRes(userIds);
	}
	
	/**
	 * 处理业务确认单信息.
	 * @param busiCode
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	private String processBusiConfirmInfo(String busiCode,Integer doneCode) throws Exception{
		TBusiConfirm tbc = PrintContentConfiguration.getBusiConfirm(busiCode, getOptr().getCounty_id());
		if(tbc ==null){
			return null;
		}
		String infoTemplate = tbc.getInfo_template();//获取模版
		if(StringHelper.isEmpty(infoTemplate)){//全部在xml里配置的
			infoTemplate = JsonHelper.fromObject(getBusiParam().getBusiConfirmParamInfo());
			return infoTemplate;
		}
		String sysdateVar = "{sysdate}";//原来的部分业务如高级修改,客户开户,模版里只配置了时间
		while(infoTemplate.indexOf(sysdateVar)>=0){
			infoTemplate = infoTemplate.replace(sysdateVar, DateHelper.formatNow());
		}
		return infoTemplate;
	}
	
	/**
	 * 保存doneCode,同时根据busiCode和doneCode查询需要的信息保存doneCodeInfo信息.
	 * @param param
	 * @throws Exception
	 */
	private void saveDoneCode(BusiParameter param) throws Exception{
		String busiCode = param.getBusiCode();//业务代码
		Integer doneCode = param.getDoneCode();//流水号
		CCust cust = param.getCust();
		doneCodeComponent.saveDoneCode(doneCode, busiCode, param.getRemark(),
				cust.getCust_id(), param.getSelectedUserIds(),cust.getAddr_id(),param.getService_channel());
//		String info = processBusiConfirmInfo(busiCode, doneCode);
//		
//		if(StringHelper.isNotEmpty(info)){
//			doneCodeComponent.saveDoneCodeInfo(cust.getCust_id(), doneCode, info);
//		}
	}
	
	/**
	 * 保存业务流水，包括  扩展信息  业务工单 业务费用 缴费信息
	 * @param doneCode
	 * @param busiParam
	 */
	public void saveAllPublic(Integer doneCode, BusiParameter busiParam)
			throws Exception {
		busiParam.setDoneCode(doneCode);
		//String custId = null;
		CCust cust = busiParam.getCustFullInfo().getCust();
		SOptr optr = getOptr();
		if(null!= cust && StringHelper.isNotEmpty(cust.getCounty_id()) && !cust.getCounty_id().equals(optr.getCounty_id())){
			LoggerHelper.error(getClass(), "串数据：操作员"+optr.getOptr_name()+"，地区："+optr.getCounty_id()+",客户："+cust.getCust_name()+"，地区"+cust.getCounty_id());
			throw new ServicesException(ErrorCode.CustDataException);
		}
		saveDoneCode(busiParam);
		
		//保存业务费用信息
		if (busiParam.getFees() !=null){
			boolean hasUnpay=false;
			for (FeeBusiFormDto feeDto : busiParam.getFees()) {
				if(feeDto.getReal_pay() > 0){
					feeComponent.saveBusiFee(cust.getCust_id(),cust!=null?cust.getAddr_id():null, feeDto.getFee_id(), feeDto.getCount(),SystemConstants.PAY_TYPE_UNPAY,feeDto
							.getReal_pay(), busiParam.getDoneCode(),busiParam.getDoneCode(), busiParam.getBusiCode(),
							busiParam.getSelectedUsers(),feeDto.getDisct_info());
					hasUnpay=true;
				}
			}
			if(hasUnpay){
				//保存未支付业务
				doneCodeComponent.saveDoneCodeUnPay(cust.getCust_id(), doneCode,this.getOptr().getOptr_id());
			}
		}
		
		/**
		 * ext_c_done_code，记录一些操作对象 
		 * 取消费用（费用名称，创建流水号，金额）
		 * 工单相关 工单编号
		 * 订单修改  产品名称，订单编号
		 * 取消和打开打印标记   业务流水号，费用编号
		 * 打印收据  收据编号
		 * 作废收据  收据编号
		 * 修改收据  收据编号
		 */
		if(null != busiParam.getBusiExtAttr()){
			extTableComponent.saveBusiAttr(busiParam.getDoneCode(), busiParam.getBusiExtAttr());
		}
		/**
		if(cust != null){
			//获取业务流水
			custId = cust.getCust_id();
		}
		
		//保存扩展信息
		extTableComponent.saveOrUpdate(busiParam.getExtAttrForm());
		//保存业务扩展信息
		if(null != busiParam.getBusiExtAttr()){
			extTableComponent.saveBusiAttr(busiParam.getDoneCode(), busiParam.getBusiExtAttr());
		}
		
		//保存业务工单
		String newAddr= busiParam.getTempVar().get(SystemConstants.EXTEND_ATTR_KEY_NEWADDR)==null?
				null:busiParam.getTempVar().get(SystemConstants.EXTEND_ATTR_KEY_NEWADDR).toString();
		taskComponent.createTask(busiParam.getTaskIds(), busiParam.getDoneCode(), busiParam.getCustFullInfo(),
				busiParam.getSelectedUsers(), newAddr,busiParam.getTask_books_time(),busiParam.getTask_cust_name(),busiParam.getTask_mobile(),null,busiParam.getTask_remark(),optr);

		//保存业务单据
//		printComponent.saveDoc(param.getDoneCode(),param.getBusiCode(), param.getCust().getCust_id(),param.getDocTypes());
		**/

		/**原有支付
		CFeePayDto pay= this.getBusiParam().getPay();
		if(null != pay){
			//保存缴费信息
			feeComponent.savePayFee(pay, busiParam.getCust().getCust_id(),busiParam.getDoneCode());
			
			//if (pay.getInvoice_mode().equals(SystemConstants.INVOICE_MODE_AUTO))
			//	printComponent.saveDoc( feeComponent.queryAutoMergeFees(param.getDoneCode()),param.getCust().getCust_id(), param.getDoneCode(),param.getBusiCode());
			if (SystemConstants.INVOICE_MODE_MANUAL.equals(pay.getInvoice_mode())){
				feeComponent.saveManualInvoice(busiParam.getDoneCode(), pay
						.getInvoice_code(), pay.getInvoice_id(), pay
						.getInvoice_book_id());
				invoiceComponent.useInvoice(pay.getInvoice_code(),pay.getInvoice_id(), 
						SystemConstants.INVOICE_MODE_MANUAL, pay.getFee());
			}else if(SystemConstants.INVOICE_MODE_AUTO.equals(pay.getInvoice_mode())){//机打
				if (StringHelper.isNotEmpty(custId)) {
					List<CFee> feeList = feeComponent.queryByDoneCode(doneCode);
					for(CFee fee : feeList){
						MemoryPrintData.appendPrintData(optr.getOptr_id(), fee.getFee_sn());
					}
					
				}
			}else if(SystemConstants.INVOICE_MODE_QUOTA.equals(pay.getInvoice_mode())){
				feeComponent.saveQuatoInvoice(busiParam.getDoneCode());
			}
		}
		**/
	}
	
	/**
	 * 只保存业务流水 ，不包括包括 业务信息  扩展信息  业务工单 业务费用 缴费信息（不保存用户明细）
	 * @param doneCode
	 * @param busiCode
	 * @param custId
	 * @throws Exception
	 */
	public void saveDoneCode(Integer doneCode, String busiCode,String custId)
	throws Exception {
		doneCodeComponent.saveDoneCode(doneCode, busiCode, "",
			custId,null,null,null);
	}
	
	/**
	 * 只保存业务流水 ，不包括包括 业务信息  扩展信息  业务工单 业务费用 缴费信息（不保存用户明细）
	 * @param doneCode
	 * @param busiCode
	 * @param custId
	 * @throws Exception
	 */
	public void saveDoneCode(Integer doneCode, String busiCode,String custId,String remark)
	throws Exception {
		doneCodeComponent.saveDoneCode(doneCode, busiCode, remark,
			custId,null,null,null);
	}
	
	//恢复长期欠费的用户状态为正常
	public void recoverUserStatus(CUser user,Integer doneCode) throws Exception{
//	    //如果是长期欠费状态的用户 或 关模隔离的用户 ，将状态改成正常,待销户
//	    if(null != user && (StatusConstants.OWELONG.equals(user.getStatus()) ||  StatusConstants.ATVCLOSE.equals(user.getStatus()) || StatusConstants.WAITLOGOFF.equals(user.getStatus()) ) ){
//	    	String userId = user.getUser_id();
//	    	List<CUserPropChange> upcList= new ArrayList<CUserPropChange>();
//	    	CUserPropChange upc = new CUserPropChange();
//	    	upc.setUser_id(userId);
//	    	upc.setDone_code(doneCode);
//	    	upc.setColumn_name("status");
//	    	upc.setOld_value(user.getStatus());
//	    	upc.setNew_value(StatusConstants.ACTIVE);
//	    	upcList.add(upc);
//	      
//	    	userComponent.editUser(doneCode, userId, upcList);
//	      
//	    	//修改用户产品状态为正常
//	    	List<CProdDto> prodList = userProdComponent.queryByUserId(userId);
//	    	for (CProdDto cPprod:prodList){
//	          // 如果产品状态时隔离  或者 用户类型是模拟用户。
//	    		if(StatusConstants.ISOLATED.equals(cPprod.getStatus()) ||  SystemConstants.USER_TYPE_ATV.equals(user.getUser_type())){
//	    			List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
//	    			changeList.add(new CProdPropChange("status",
//	    					cPprod.getStatus(),StatusConstants.ACTIVE));
//    				changeList.add(new CProdPropChange("status_date",
//    						DateHelper.dateToStr(cPprod.getStatus_date()),DateHelper.dateToStr(new Date())));
//	            
//    				userProdComponent.editProd(doneCode,cPprod.getProd_sn(),changeList);
//	            
//    				//如果不是模拟用户则，生成激活产品任务
//    				if(!SystemConstants.USER_TYPE_ATV.equals(user.getUser_type())){
//    					jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, user.getCust_id(),
//    							user.getUser_id(), user.getStb_id(), user.getCard_id(), user.getModem_mac(), cPprod.getProd_sn(),cPprod.getProd_id());
//    				}
//	    		}
//	        
//	    	}
// 	    	//生成销账任务
//			jobComponent.createCustWriteOffJob(doneCode, user.getCust_id(), SystemConstants.BOOLEAN_FALSE);
//    	}
	}
	
	protected void buyDevice(DeviceDto device,String buyMode,String ownership,FeeInfoDto fee, 
			String busiCode,CCust cust,Integer doneCode) throws Exception {
		if(!BusiCodeConstants.USER_OPEN_BATCH.equals(busiCode)){
			//增加客户设备
			custComponent.addDevice(doneCode, cust.getCust_id(),
					device.getDevice_id(), device.getDevice_type(), device.getDevice_code(), 
					device.getPairCard() ==null?null:device.getPairCard().getDevice_id(),
					device.getPairCard() ==null?null:device.getPairCard().getCard_id(), 
					null, null,buyMode);
		}
		//保存设备费用
		if (fee != null && fee.getFee_id()!= null && fee.getFee()>0){
			String payType = SystemConstants.PAY_TYPE_UNPAY;
			if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
				payType = this.getBusiParam().getPay().getPay_type();
			doneCodeComponent.saveDoneCodeUnPay(cust.getCust_id(), doneCode, getBusiParam().getOptr().getOptr_id());
			
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
					device.getDepot_status(), StatusConstants.USE,buyMode,true);
			//更新设备产权
			if (!device.getOwnership().equals(ownership)){
				deviceComponent.updateDeviceOwnership(doneCode, busiCode, device.getDevice_id(),device.getOwnership(),ownership,buyMode,true);
			}
			//更新设备为旧设备
			if (SystemConstants.BOOLEAN_TRUE.equals(device.getUsed()))
				deviceComponent.updateDeviceUsed(doneCode, busiCode, device.getDevice_id(), SystemConstants.BOOLEAN_TRUE, SystemConstants.BOOLEAN_FALSE,true);
		}
	}
	
	protected Date openProd(Integer doneCode, CProdOrderDto order,Date startDate) throws Exception, ServicesException {
		Date expDate = null;
		Date effDate = null;
		CProdPropChange statusChange =new CProdPropChange();
		statusChange.setChange_time(new Date(order.getStatus_date().getTime()));
		statusChange.setNew_value(StatusConstants.ACTIVE);
		if (startDate == null) {
			//计算延期天数
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
		
		if (expDate != null && order.getExp_date().getTime() != expDate.getTime()){
			changeList.add(new CProdPropChange("exp_date",DateHelper.dateToStr(order.getExp_date()),
					DateHelper.dateToStr(expDate)));
		}
		
		
		if (effDate != null && order.getEff_date().getTime() != effDate.getTime()){
			changeList.add(new CProdPropChange("eff_date",DateHelper.dateToStr(order.getEff_date()),
					DateHelper.dateToStr(effDate)));
		}
		orderComponent.editProd(doneCode,order.getOrder_sn(),changeList);
		
		return expDate;
		
	}
	
	
	
	protected void setUserDeviceInfo(CUser user, DeviceDto device) throws Exception{
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
	
	
	public void setUserProdComponent(UserProdComponent userProdComponent) {
		this.userProdComponent = userProdComponent;
	}
	public void setAcctComponent(AcctComponent acctComponent) {
		this.acctComponent = acctComponent;
	}
	public void setFeeComponent(FeeComponent feeComponent) {
		this.feeComponent = feeComponent;
	}
	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}



	public void setUserComponent(UserComponent userComponent) {
		this.userComponent = userComponent;
	}

	public void setCustComponent(CustComponent custComponent) {
		this.custComponent = custComponent;
	}

	public void setDeviceComponent(DeviceComponent deviceComponent) {
		this.deviceComponent = deviceComponent;
	}

	public void setProdComponent(ProdComponent prodComponent) {
		this.prodComponent = prodComponent;
	}

	public void setBillComponent(BillComponent billComponent) {
		this.billComponent = billComponent;
	}

	/**
	 * @param invoiceComponent the invoiceComponent to set
	 */
	public void setInvoiceComponent(InvoiceComponent invoiceComponent) {
		this.invoiceComponent = invoiceComponent;
	}

	/**
	 * @param extTableComponent the extTableComponent to set
	 */
	public void setExtTableComponent(ExtTableComponent extTableComponent) {
		this.extTableComponent = extTableComponent;
	}

	/**
	 * @param taskComponent the taskComponent to set
	 */
	public void setTaskComponent(TaskComponent taskComponent) {
		this.taskComponent = taskComponent;
	}

	/**
	 * @param busiConfigComponent the busiConfigComponent to set
	 */
	public void setBusiConfigComponent(BusiConfigComponent busiConfigComponent) {
		this.busiConfigComponent = busiConfigComponent;
	}

}
