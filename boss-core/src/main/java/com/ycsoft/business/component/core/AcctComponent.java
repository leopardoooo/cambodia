/**
 *
 */
package com.ycsoft.business.component.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAcctFeeType;
import com.ycsoft.beans.config.TCountyAcct;
import com.ycsoft.beans.config.TCountyAcctChange;
import com.ycsoft.beans.config.TPublicAcctitem;
import com.ycsoft.beans.core.acct.CAcct;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemActive;
import com.ycsoft.beans.core.acct.CAcctAcctitemAdjust;
import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.beans.core.acct.CAcctAcctitemHis;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactiveHis;
import com.ycsoft.beans.core.acct.CAcctAcctitemInvalid;
import com.ycsoft.beans.core.acct.CAcctAcctitemMerge;
import com.ycsoft.beans.core.acct.CAcctAcctitemOrder;
import com.ycsoft.beans.core.acct.CAcctAcctitemThreshold;
import com.ycsoft.beans.core.acct.CAcctAcctitemThresholdProp;
import com.ycsoft.beans.core.acct.CAcctAcctitemTrans;
import com.ycsoft.beans.core.acct.CAcctBank;
import com.ycsoft.beans.core.acct.CAcctHis;
import com.ycsoft.beans.core.acct.CAcctPreFee;
import com.ycsoft.beans.core.acct.CGeneralAcct;
import com.ycsoft.beans.core.acct.CGeneralContract;
import com.ycsoft.beans.core.acct.CGeneralContractDetail;
import com.ycsoft.beans.core.acct.CGeneralContractHis;
import com.ycsoft.beans.core.acct.CGeneralContractPay;
import com.ycsoft.beans.core.acct.CGeneralCredential;
import com.ycsoft.beans.core.bank.CBankAgree;
import com.ycsoft.beans.core.bank.CBankPay;
import com.ycsoft.beans.core.bank.CBankRefundtodisk;
import com.ycsoft.beans.core.bank.CBankReturn;
import com.ycsoft.beans.core.bank.CBankReturnPayerror;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdHis;
import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.beans.core.prod.CProdOrderFeeOut;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.record.CBandUpgradeRecord;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.business.component.config.BusiConfigComponent;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.dao.config.TAdjustDataDao;
import com.ycsoft.business.dao.config.TCountyAcctChangeDao;
import com.ycsoft.business.dao.config.TCountyAcctDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemActiveDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemAdjustDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemChangeDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemHisDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemInactiveDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemInactiveHisDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemInvalidDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemMergeDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemOrderDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemThresholdDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemThresholdPropDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemTransDao;
import com.ycsoft.business.dao.core.acct.CAcctBankDao;
import com.ycsoft.business.dao.core.acct.CAcctDao;
import com.ycsoft.business.dao.core.acct.CAcctHisDao;
import com.ycsoft.business.dao.core.acct.CAcctPreFeeDao;
import com.ycsoft.business.dao.core.acct.CGeneralAcctDao;
import com.ycsoft.business.dao.core.acct.CGeneralContractDao;
import com.ycsoft.business.dao.core.acct.CGeneralContractDetailDao;
import com.ycsoft.business.dao.core.acct.CGeneralContractHisDao;
import com.ycsoft.business.dao.core.acct.CGeneralContractPayDao;
import com.ycsoft.business.dao.core.acct.CGeneralCredentialDao;
import com.ycsoft.business.dao.core.bank.CBankAgreeDao;
import com.ycsoft.business.dao.core.bank.CBankAgreeHisDao;
import com.ycsoft.business.dao.core.bank.CBankPayDao;
import com.ycsoft.business.dao.core.bank.CBankRefundtodiskDao;
import com.ycsoft.business.dao.core.bank.CBankReturnDao;
import com.ycsoft.business.dao.core.bank.CBankReturnPayerrorDao;
import com.ycsoft.business.dao.core.bill.BBillDao;
import com.ycsoft.business.dao.core.cust.CCustPropChangeDao;
import com.ycsoft.business.dao.core.prod.CProdHisDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.record.CBandUpgradeRecordDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.core.acct.AcctAcctitemActiveDto;
import com.ycsoft.business.dto.core.acct.AcctAcctitemChangeDto;
import com.ycsoft.business.dto.core.acct.AcctAcctitemInvalidDto;
import com.ycsoft.business.dto.core.acct.AcctAcctitemThresholdDto;
import com.ycsoft.business.dto.core.acct.AcctDto;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.acct.GeneralContractDto;
import com.ycsoft.business.dto.core.acct.QueryAcctitemThresholdDto;
import com.ycsoft.business.dto.core.acct.UnitPayDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
/**
 * @author YC-SOFT
 *
 */
@Component
public class AcctComponent  extends BusiConfigComponent {

	private CAcctDao cAcctDao ;
	private CAcctHisDao cAcctHisDao ;
	
	private CAcctAcctitemActiveDao cAcctAcctitemActiveDao;
	private CAcctAcctitemThresholdDao cAcctAcctitemThresholdDao;
	private CAcctAcctitemInactiveDao cAcctAcctitemInactiveDao;
	private CAcctAcctitemInactiveHisDao cAcctAcctitemInactiveHisDao;
	private CAcctAcctitemOrderDao cAcctAcctitemOrderDao;
	private CAcctAcctitemChangeDao cAcctAcctitemChangeDao;
	private CAcctAcctitemInvalidDao cAcctAcctitemInvalidDao;
	private CAcctBankDao cAcctBankDao;
	private CAcctAcctitemAdjustDao cAcctAcctitemAdjustDao;
	private CAcctAcctitemTransDao cAcctAcctitemTransDao;
	private CProdHisDao cProdHisDao ;
	private CBankAgreeDao cBankAgreeDao;
	private CBankAgreeHisDao cBankAgreeHisDao;
	private CBankPayDao cBankPayDao;
	private CGeneralAcctDao cGeneralAcctDao;
	private CGeneralContractDao cGeneralContractDao;
	private CGeneralContractDetailDao cGeneralContractDetailDao;
	private CGeneralContractPayDao cGeneralContractPayDao;
	private CGeneralContractHisDao cGeneralContractHisDao;
	private CGeneralCredentialDao cGeneralCredentialDao;
	//private ExpressionUtil expressionUtil;
	
	private CAcctAcctitemHisDao cAcctAcctitemHisDao;
	
	private CAcctPreFeeDao cAcctPreFeeDao;
	private CAcctAcctitemThresholdPropDao cAcctAcctitemThresholdPropDao;
	private CBandUpgradeRecordDao cBandUpgradeRecordDao;
	
	private TCountyAcctDao tCountyAcctDao;
	private TCountyAcctChangeDao tCountyAcctChangeDao;
	private TAdjustDataDao tAdjustDataDao;
	
	private BBillDao bBillDao;
	private CAcctAcctitemMergeDao cAcctAcctitemMergeDao;
	private CUserDao cUserDao;
	private CBankReturnDao cBankReturnDao;
	private CBankReturnPayerrorDao cBankReturnPayerrorDao;
	private CBankRefundtodiskDao cBankRefundtodiskDao;
	@Autowired
	private CCustPropChangeDao cCustProdChangeDao;
	@Autowired
	private BeanFactory beanFactory;
	/**
	 * @param custId    客户id
	 * @param acctType 账户类型
	 * @param payType
	 * @return acctId 账户编号
	 *
	 * 付费方式  可为空，如果为空，则系统默认为预付费
	 * 如果账户类型为公用账户，则创建公用账目（含专项公用账目)
	 */
	public String createAcct(String custId,String userId, String acctType,String payType) throws Exception{
		String acctId = gAcctId();
		if (StringHelper.isEmpty(payType))
			payType = SystemConstants.ACCT_PAY_TYPE_YFF;
		CAcct acct = new CAcct();
		acct.setAcct_id(acctId);
		acct.setUser_id(userId);
		acct.setCust_id(custId);
		acct.setAcct_type(acctType);
		acct.setPay_type(payType);
		acct.setArea_id(getOptr().getArea_id());
		acct.setCounty_id(getOptr().getCounty_id());
		cAcctDao.save(acct);

		if (acct.getAcct_type().equals(SystemConstants.ACCT_TYPE_PUBLIC)){
			List<TPublicAcctitem> publicAcctitemList = qureyAcctitem();
			for (TPublicAcctitem publicAcctitem : publicAcctitemList) {
				createAcctItem(acctId, publicAcctitem.getAcctitem_id());
			}
		}
		return acctId;
	}
	
	/**
	 * 删除账户信息
	 * @param acctId
	 * @throws Exception
	 */
	public void removeAcctWithoutHis(String acctId) throws Exception{
		cAcctDao.remove(acctId);
		cAcctAcctitemDao.removeByAcctId(acctId);
		cAcctAcctitemActiveDao.removeByAcctId(acctId);
		cAcctAcctitemThresholdDao.removeByAcctId(acctId);
		cAcctAcctitemInactiveDao.removeByAcctId(acctId);
		cAcctAcctitemOrderDao.removeByAcctId(acctId);
		cAcctAcctitemChangeDao.removeByAcctId(acctId);
		cAcctBankDao.removeByAcctId(acctId);
	}

	/**
	 * 删除账户信息 并记录历史
	 * @param acctId
	 * @throws Exception
	 */
	public void removeAcctWithHis(CAcct acct ,int doneCode,String busiCode) throws Exception{
		//记录账户历史
		CAcctHis acctHis = new CAcctHis();
		BeanUtils.copyProperties(acct, acctHis);
		acctHis.setDone_code(doneCode);
		cAcctHisDao.save(acctHis);

		//删除账户及相关信息
		String acctId = acct.getAcct_id();
		cAcctDao.remove(acctId);
		//查找账户对应的账目
		List<CAcctAcctitem> acctitemList = this.cAcctAcctitemDao.queryByAcctId(acctId);
		for (CAcctAcctitem acctItem :acctitemList){
			this.removeAcctItemWithoutHis(acct.getCust_id(), acctId, acctItem.getAcctitem_id(), doneCode, busiCode);
		}
		
	}
	
	/**
	 * 恢复公共账户,及相关账目信息.
	 * @param custId
	 * @param doneCode
	 * @param busiCode
	 * @throws Exception
	 */
	public void restorePublicAcctInfo(String custId,int doneCode,String busiCode) throws Exception{
		//记录账户历史
		CAcct acct = new CAcct();
		CAcctHis acctHis = new CAcctHis();
		acctHis.setCust_id(custId);
		List<CAcctHis> caccts = cAcctHisDao.findByEntity(acctHis);
		if(CollectionHelper.isNotEmpty(caccts)){
			for(CAcctHis his:caccts){
				if(his.getAcct_type().equals(SystemConstants.ACCT_TYPE_PUBLIC)){
					acctHis = his;
					break;
				}
			}
		}else{
			throw new ComponentException("返销户客户时候发生错误,未能获取的账户信息.");
		}
		
		BeanUtils.copyProperties(acctHis, acct);
		acct.setDone_code(doneCode);
		
		cAcctDao.save(acct);
		
		cAcctHisDao.removeByAcctId(acct.getAcct_id());
		
		List<CAcctAcctitemHis> itemsHis = cAcctAcctitemHisDao.queryAcctItemHis(acct.getAcct_id());
		if(CollectionHelper.isEmpty(itemsHis)){
			throw new ComponentException("返销户客户时候发生错误,未能获取的主账户的账目信息.");
		}
		List<CAcctAcctitem> items = new ArrayList<CAcctAcctitem>();
		
		for(CAcctAcctitemHis his:itemsHis){
			CAcctAcctitem item = new CAcctAcctitem();
			BeanUtils.copyProperties(his, item);
			item.setActive_balance(0);
			item.setOwe_fee(0);
			item.setReal_fee(0);
			item.setReal_bill(0);
			item.setOrder_balance(0);
			item.setReal_balance(0);
			item.setCan_trans_balance(0);
			item.setCan_refund_balance(0);
			item.setInactive_balance(0);
			items.add(item);
		}
		
		cAcctAcctitemDao.save(items.toArray(new CAcctAcctitem [items.size()]));
		
		cAcctAcctitemHisDao.removeByAcctId(acct.getAcct_id());
		
	}
	
	/**
	 * 创建新的账目
	 * @param acctId		账户编号
	 * @param acctItemId	账目编号
	 * @throws Exception
	 */
	public CAcctAcctitem createAcctItem(String acctId, String acctItemId)throws Exception{
		CAcctAcctitem acctitem = new CAcctAcctitem();
		acctitem.setAcct_id(acctId);
		acctitem.setAcctitem_id(acctItemId);
		acctitem.setArea_id(getOptr().getArea_id());
		acctitem.setCounty_id(getOptr().getCounty_id());
		cAcctAcctitemDao.save(acctitem);
		return acctitem;
	}
	
	/**
	 * 查询账目项资金明细
	 * @param acctId
	 * @param acctItemId
	 * @param feeType
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public CAcctAcctitemActive queryAdjustAcctitemActive(String acctId,
			String acctItemId, String feeType, String countyId) throws Exception{
		return cAcctAcctitemActiveDao.queryAcctItemActive(acctId, acctItemId, feeType, countyId);
	}


	/**
	 * 账户扣款
	 * @param acct_id
	 * @param acctitem_id
	 * @param fee
	 * @param onlyRefoud 是否值使用可退资金扣款
	 * @return
	 * @throws Exception 
	 */
	public List<CAcctAcctitemChange> saveAcctDebitFee(String custId,String acctId,String acctItemId,
			String changeType,Integer fee,String busiCode,Integer doneCode,boolean onlyRefoud,String debitRemark) throws Exception{
		List<CAcctAcctitemChange> changeList=new ArrayList<>();
		if(fee==null||fee>=0){
			throw new ComponentException(ErrorCode.AcctDebitFeeIsPositive);
		}
		CAcctAcctitem acctItem=cAcctAcctitemDao.queryAcctItem(acctId, acctItemId);
		if(acctItem==null){
			throw new ServicesException(ErrorCode.AcctItemNotExists);
		}
		if(fee*-1>acctItem.getActive_balance()){
			throw new ServicesException(ErrorCode.AcctFeeNotEnough);
		}
		cAcctAcctitemDao.updateActiveBanlance(acctId, acctItemId, fee,0,0,0, getOptr().getCounty_id());
		
		int checkFee=0;//检查明细和主记录是否金额一致
		List<AcctAcctitemActiveDto> actives=cAcctAcctitemActiveDao.queryByAcctitemId(acctId, acctItemId, this.getOptr().getCounty_id());
		for(AcctAcctitemActiveDto active:actives){
			if(active.getBalance()<0){
				throw new ComponentException(ErrorCode.AcctBalanceError);
			}
			checkFee=checkFee+active.getBalance();	
		}
		//检查账户余额和资金明细是否一致
		if(checkFee!=acctItem.getActive_balance()){
			//重新查询账目余额
			 if(checkFee!=cAcctAcctitemDao.queryAcctItem(acctId, acctItemId).getActive_balance()-fee){
				 throw new ComponentException(ErrorCode.AcctItemAndActiveFeeDisagree);
			 }
		}
		
		int debitTotalFee=fee*-1;
		for(AcctAcctitemActiveDto active:actives){
			
			if(active.getBalance()==0){
				continue;
			}
			checkFee=checkFee+active.getBalance();		
			if(onlyRefoud&&!active.getCan_refund().equals(SystemConstants.BOOLEAN_TRUE)){
				continue;//只用可退资金扣款
			}

			int debitFee=0;
			if(active.getBalance()>=debitTotalFee){
				debitFee=debitTotalFee;
			}else{
				debitFee=active.getBalance();
			}
			
			String feeType=active.getFee_type();
			cAcctAcctitemActiveDao.updateBanlance(acctId, acctItemId,
					feeType, fee, getOptr().getCounty_id());
			CAcctAcctitemChange change=saveAcctitemNewChange(doneCode, busiCode, custId, acctId,
									acctItemId, changeType, feeType, debitFee*-1, active.getBalance(), null,debitRemark);
			changeList.add(change);
			
			debitTotalFee=debitTotalFee-debitFee;
			if(debitTotalFee==0) break;
		}
		
		//资金明细余额不足判断
		if(debitTotalFee>0){
			if(onlyRefoud){
				throw new ComponentException(ErrorCode.AcctCanRefoudFeeNotEnough);
			}else{
				throw new ComponentException(ErrorCode.AcctFeeNotEnough);
			}
		}

		return changeList;
	}
	/**
	 * 账户增加金额
	 * @return
	 * @throws Exception 
	 */
	public CAcctAcctitemChange saveAcctAddFee(String custId,String acctId,String acctItemId,
			String changeType,Integer fee,String feeType,String busiCode,Integer doneCode,String addRemark) throws Exception{
		
		if(fee==null||fee<=0){
			throw new ComponentException(ErrorCode.AcctAddFeeIsNotPositive);
		}
		CAcctAcctitem acctItem=cAcctAcctitemDao.queryAcctItem(acctId, acctItemId);
		if(acctItem==null){
			throw new ServicesException(ErrorCode.AcctItemNotExists);
		}
		cAcctAcctitemDao.updateActiveBanlance(acctId, acctItemId, fee,0,0,0, getOptr().getCounty_id());
		CAcctAcctitemActive activeItem = cAcctAcctitemActiveDao.queryAcctItemActive(acctId, acctItemId, feeType, getOptr().getCounty_id());
		int preFee=0;
		if(activeItem==null){
			activeItem = new CAcctAcctitemActive();
			activeItem.setAcct_id(acctId);
			activeItem.setAcctitem_id(acctItemId);
			activeItem.setBalance(fee);
			activeItem.setFee_type(feeType);
			activeItem.setArea_id(getOptr().getArea_id());
			activeItem.setCounty_id(getOptr().getCounty_id());
			cAcctAcctitemActiveDao.save(activeItem);		
		}else{
			preFee=activeItem.getBalance();
			cAcctAcctitemActiveDao.updateBanlance(acctId, acctItemId,
					feeType, fee, getOptr().getCounty_id());
		}
		return saveAcctitemNewChange(doneCode, busiCode, custId, acctId,
				acctItemId, changeType,feeType, fee, preFee, null,addRemark);
	}
	
	
	/**
	 * 退款转账到公用账目中
	 * @param orderFees
	 * @param cust_id
	 * @param doneCode
	 * @param busi_code
	 * @throws Exception
	 */
	public void saveCancelFeeToAcct(List<CProdOrderFeeOut> outList,String cust_id,Integer doneCode,String busi_code) throws Exception{
		//账户
		CAcct acct=queryCustAcctByCustId(cust_id);
		//按订单转账到公用账目中
		String acctItemId=SystemConstants.ACCTITEM_PUBLIC_ID;
		String acctId=acct.getAcct_id();
		
		for(CProdOrderFeeOut out:outList){
			if(out.getOutput_type().equals(SystemConstants.ORDER_FEE_TYPE_ACCT)
					&&out.getOutput_fee()>0){
				Integer fee=out.getOutput_fee();
				String feeType=out.getFee_type();
				String changeType=SystemConstants.ACCT_CHANGE_TRANS;
				String acct_change_sn=
						saveAcctAddFee(cust_id, acctId, acctItemId, changeType, fee, feeType, busi_code, doneCode,out.getRemark())
										.getAcct_change_sn();
				out.setOutput_sn(acct_change_sn);
			}
		}
	}

	/**
	 * 修改账目余额
	 * 记录资金异动
	 * 对没有活动资金的创建活动资金
	 * @param doneCode
	 * @param busiCode
	 * @param custId
	 * @param acctId
	 * @param acctItemId
	 * @param changeType
	 * @param feeType
	 * @param fee
	 * @throws Exception
	 */
	public void changeAcctItemBanlance(Integer doneCode, String busiCode,
			String custId, String acctId, String acctItemId, String changeType,
			String feeType, int fee, Integer InactiveDoneCode) throws Exception {
		CAcctAcctitemActive activeItem = cAcctAcctitemActiveDao.queryAcctItemActive(acctId, acctItemId, feeType, getOptr().getCounty_id());
		//修改账目余额
		TAcctFeeType acctFeeType = queryAcctFeeType(feeType);
		int transFee = acctFeeType.getCan_trans().equals(SystemConstants.BOOLEAN_TRUE)?fee:0;
		int refundFee = acctFeeType.getCan_refund().equals(SystemConstants.BOOLEAN_TRUE)?fee:0;
		int inactiveFee=0;
		if (changeType.equals(SystemConstants.ACCT_CHANGE_UNFREEZE))
			inactiveFee = fee*-1;
		cAcctAcctitemDao.updateActiveBanlance(acctId, acctItemId, fee,inactiveFee,refundFee,transFee, getOptr().getCounty_id());

		//修改活动资金余额
		int  preFee = 0;
		if (activeItem == null){
			activeItem = new CAcctAcctitemActive();
			activeItem.setAcct_id(acctId);
			activeItem.setAcctitem_id(acctItemId);
			activeItem.setBalance(fee);
			activeItem.setFee_type(feeType);
			activeItem.setArea_id(getOptr().getArea_id());
			activeItem.setCounty_id(getOptr().getCounty_id());
			cAcctAcctitemActiveDao.save(activeItem);
			
			saveAcctitemChange(doneCode, busiCode, custId, acctId,
					acctItemId, changeType, feeType, fee, preFee, InactiveDoneCode);
		}else{
			preFee = activeItem.getBalance();
			
			//退款
			if(busiCode.equals(BusiCodeConstants.ACCT_REFUND)){
				//退款 如果有调账可退资金类型，优先扣除
				CAcctAcctitemActive cActive = cAcctAcctitemActiveDao.queryActiveByFeetype(acctId, acctItemId,
								SystemConstants.ACCT_FEETYPE_ADJUST_KT,getOptr().getCounty_id());
				if(cActive != null){
					int balance = cActive.getBalance().intValue();
					if(balance < fee*-1){		// 调账可退金额小于 需要退款金额
						if(balance > 0){
							//优先扣除完 调账可退 金额
							cAcctAcctitemActiveDao.updateBanlance(acctId, acctItemId,
									SystemConstants.ACCT_FEETYPE_ADJUST_KT, balance*-1, getOptr().getCounty_id());
							saveAcctitemChange(doneCode, busiCode, custId, acctId,
									acctItemId, changeType,
									SystemConstants.ACCT_FEETYPE_ADJUST_KT, balance*-1,
									balance, InactiveDoneCode);
							
							//扣除完 调账可退 金额，再扣除现金
							cAcctAcctitemActiveDao.updateBanlance(acctId, acctItemId,
									feeType, balance + fee, getOptr().getCounty_id());
							saveAcctitemChange(doneCode, busiCode, custId, acctId,
									acctItemId, changeType, feeType, balance + fee, preFee,
									InactiveDoneCode);
						}else{	//余额为零时
							cAcctAcctitemActiveDao.updateBanlance(acctId, acctItemId,
									feeType, fee, getOptr().getCounty_id());
							saveAcctitemChange(doneCode, busiCode, custId, acctId,
									acctItemId, changeType, feeType, fee, preFee, InactiveDoneCode);
						}
					}else{
						//调账可退 金额 大于需要退款的金额
						cAcctAcctitemActiveDao.updateBanlance(acctId, acctItemId,
								SystemConstants.ACCT_FEETYPE_ADJUST_KT, fee, getOptr().getCounty_id());
						saveAcctitemChange(doneCode, busiCode, custId, acctId,
								acctItemId, changeType,
								SystemConstants.ACCT_FEETYPE_ADJUST_KT, fee,
								balance, InactiveDoneCode);
					}
					
				}else{
					cAcctAcctitemActiveDao.updateBanlance(acctId, acctItemId,
							feeType, fee, getOptr().getCounty_id());
					saveAcctitemChange(doneCode, busiCode, custId, acctId,
							acctItemId, changeType, feeType, fee, preFee, InactiveDoneCode);
				}
			}else{
				cAcctAcctitemActiveDao.updateBanlance(acctId, acctItemId,
						feeType, fee, getOptr().getCounty_id());
				saveAcctitemChange(doneCode, busiCode, custId, acctId,
						acctItemId, changeType, feeType, fee, preFee, InactiveDoneCode);
			}
		}
	}
	
	public CAcctAcctitemChange saveAcctitemNewChange(Integer doneCode, String busiCode,
			String custId, String acctId, String acctItemId, String changeType,
			String feeType, int fee, int preFee, Integer inactiveDoneCode,String remark) throws Exception{
		// 增加资金异动
				CAcctAcctitemChange change = new CAcctAcctitemChange();
				change.setDone_code(doneCode);
				change.setBusi_code(busiCode);
				change.setCust_id(custId);
				change.setAcct_id(acctId);
				change.setAcctitem_id(acctItemId);
				change.setChange_type(changeType);
				change.setFee_type(feeType);
				change.setFee(fee + preFee);
				change.setPre_fee(preFee);
				change.setChange_fee(fee);
				change.setBilling_cycle_id(DateHelper.nowYearMonth());
				change.setArea_id(getOptr().getArea_id());
				change.setCounty_id(getOptr().getCounty_id());
				change.setInactive_done_code(inactiveDoneCode);
				change.setAcct_change_sn(cAcctAcctitemChangeDao.findSequence().toString());
				change.setCometype(remark);
				
				cAcctAcctitemChangeDao.save(change);
				return change;
	}
	
	public CAcctAcctitemChange saveAcctitemChange(Integer doneCode, String busiCode,
			String custId, String acctId, String acctItemId, String changeType,
			String feeType, int fee, int preFee, Integer inactiveDoneCode) throws Exception {
		return saveAcctitemNewChange(doneCode, busiCode, custId, acctId, acctItemId, changeType, feeType, fee, preFee, inactiveDoneCode,null);
	}
	
	
	public void updateInactiveBanlance(String sn, String acctId, String acctItemId, int fee) throws Exception {
		cAcctAcctitemInactiveDao.updateBanlance(sn, acctId, acctItemId,fee);
	}
	
	/**
	 * 更新冻结资金
	 * @param unfreezeJob
	 * @param acctId
	 * @param acctItemId
	 * @param sn
	 * @param fee
	 * @param cycle
	 */
	public void changeAcctitemInactive(int balance, String acctId,
			String acctItemId, String sn, int fee, int cycle) throws Exception{
		cAcctAcctitemInactiveDao.updateBanlance(sn, acctId, acctItemId,fee);
		if (balance>fee){
			cAcctAcctitemInactiveDao.updateUnfeezeDate(sn,acctId,acctItemId, cycle);
		} else {
			//查找另外的资金解冻记录
			CAcctAcctitemInactive acctitemInactive = cAcctAcctitemInactiveDao.queryNextUnfreezeRecord(acctId, acctItemId);
			if (acctitemInactive != null){
				cAcctAcctitemInactiveDao.updateUnfeezeDate(
						StringHelper.isEmpty(acctitemInactive.getFee_sn())?acctitemInactive.getPromotion_sn():acctitemInactive.getFee_sn(),
								acctId,acctItemId,cycle);
			}
		}
	}
	
	
	/**
	 * 增加账目欠费金额并清除实时费用
	 * @param acct_id
	 * @param prod_id
	 * @param oweFee
	 */
	public void changeAcctItemOwefee(boolean clearRealFee,String acctId, String acctItemId, int oweFee) throws Exception{
		this.cAcctAcctitemDao.changeOwefee(clearRealFee,acctId, acctItemId, oweFee,getOptr().getCounty_id());
		
	}
	
	public CAcctAcctitemInactive queryInactiveAcctitem(String acctId, String acctItemId) throws Exception {
		return cAcctAcctitemInactiveDao.queryInactiveAcctitem(acctId,acctItemId,getOptr().getCounty_id());
	}

	/**
	 * 增加冻结资金
	 * @param inactiveItem
	 * @throws Exception
	 */
	public void addAcctItemInactive(CAcctAcctitemInactive inactiveItem) throws Exception{
		//查找最后生成的冻结资金记录
		CAcctAcctitemInactive lastInactiveItem = this.queryInactiveAcctitem(
				inactiveItem.getAcct_id(), inactiveItem.getAcctitem_id());
		if (lastInactiveItem == null || lastInactiveItem.getNext_active_time() == null){
			inactiveItem.setNext_active_time(DateHelper.now());
		} else if (lastInactiveItem.getBalance() > 0){
			if (lastInactiveItem.getNext_active_time().getTime()>new Date().getTime()){
				inactiveItem.setNext_active_time(DateHelper.now());
			} else {
				inactiveItem.setNext_active_time(lastInactiveItem.getNext_active_time());
			}
		}else{
			inactiveItem.setNext_active_time(DateHelper.now());
		}
		setBaseInfo(inactiveItem);
		cAcctAcctitemInactiveDao.save(inactiveItem);
		cAcctAcctitemDao.updateActiveBanlance(inactiveItem.getAcct_id(), inactiveItem.getAcctitem_id()
				, 0, inactiveItem.getInit_amount(), 0, 0, getOptr().getCounty_id());
	}

	public void addAdjust(Integer doneCode, String acctId, String acctItemId,
			int fee, String feeType, String reason, String remark)
			throws Exception {
		//保存调账记录
		CAcctAcctitemAdjust adjust = new CAcctAcctitemAdjust();
		adjust.setDone_code(doneCode);
		adjust.setAcct_id(acctId);
		adjust.setAcctitem_id(acctItemId);
		adjust.setAjust_fee(fee);
		adjust.setRemark(remark);
		adjust.setFee_type(feeType);
		adjust.setReason(reason);
		setBaseInfo(adjust);

		cAcctAcctitemAdjustDao.save(adjust);

	}

	/**
	 * 小额减免的天数
	 * @return
	 * @throws Exception
	 */
	public String getAdjustDay()throws Exception {
		return queryTemplateConfig(TemplateConfigDto.Config.BASE_EASY_ADJUST_DAYS.toString());
	}
	
	public List<CAcctAcctitemAdjust> queryAdjustFee(String acctId, String acctItemId,String feeType) throws Exception{
		return cAcctAcctitemAdjustDao.queryAdjustFee(acctId, acctItemId, feeType);
	}
	
	/**
	 * 转账
	 * @param outAcctId
	 * @param custId
	 * @param doneCode
	 * @param busiCode
	 * @param outAcctItemId
	 * @param inAcctId
	 * @param inAcctItemId
	 * @param fee
	 * @throws JDBCException
	 * @throws Exception
	 */
	public void trans(String  custId, Integer doneCode,
			String busiCode,String outAcctId,  String outAcctItemId, String inAcctId,
			String inAcctItemId, int fee) throws JDBCException, Exception {
		// 获取转出账目的资金明细
		List<AcctAcctitemActiveDto> activeItemList = queryActiveById(
				outAcctId, outAcctItemId);
		// 保存转账
		CAcctAcctitemTrans acctTrans = new CAcctAcctitemTrans();
		acctTrans.setCust_id(custId);
		acctTrans.setDone_code(doneCode);
		acctTrans.setOut_acct_id(outAcctId);
		acctTrans.setOut_acctitem_id(outAcctItemId);
		acctTrans.setIn_acct_id(inAcctId);
		acctTrans.setIn_acctitem_id(inAcctItemId);
		setBaseInfo(acctTrans);
		for (AcctAcctitemActiveDto activeItem : activeItemList) {
			if (activeItem.getCan_trans().equals(SystemConstants.BOOLEAN_TRUE)) {
				int trans = activeItem.getBalance() < fee ? activeItem
						.getBalance() : fee;
				changeAcctItemBanlance(doneCode, busiCode, custId,
						outAcctId, outAcctItemId,
						SystemConstants.ACCT_CHANGE_TRANS, activeItem
								.getFee_type(), trans* -1, null);
				changeAcctItemBanlance(doneCode, busiCode, custId, inAcctId,
						inAcctItemId, SystemConstants.ACCT_CHANGE_TRANS,
						activeItem.getFee_type(), trans, null );
				fee = fee - trans;
				acctTrans.setFee_type(activeItem.getFee_type());
				acctTrans.setAmount(trans);
				cAcctAcctitemTransDao.save(acctTrans);
				if (fee <= 0)
					break;
			}
		}
	}

	/**
	 * 取消转账
	 * @param doneCode
	 * @param busiCode
	 * @throws Exception
	 */
	public void cancelTrans(Integer doneCode,String busiCode) throws Exception{
		

	}
	/**
	 * 退款
	 * @param custId
	 * @param doneCode
	 * @param busiCode
	 * @param acctId
	 * @param acctItemId
	 * @param fee
	 * @throws JDBCException
	 * @throws Exception
	 */
	public void refund(String custId, Integer doneCode, String busiCode,
			String acctId, String acctItemId, int fee) throws JDBCException,
			Exception {
		//获取退款账目的资金明细
		List<AcctAcctitemActiveDto> activeItemList = queryActiveById(acctId, acctItemId);
		//保存退款
		for (AcctAcctitemActiveDto activeItem : activeItemList) {
			if (activeItem.getCan_refund().equals(SystemConstants.BOOLEAN_TRUE)) {
				int refund = activeItem.getBalance() < fee ? activeItem
						.getBalance() : fee;
				changeAcctItemBanlance(doneCode, busiCode, custId, acctId,
						acctItemId, SystemConstants.ACCT_CHANGE_REFUND,
						activeItem.getFee_type(), refund * -1, null);
				fee = fee - refund;
				if (fee <= 0)
					break;
			}
		}
	}
	/**
	 * 修改账目的停开机的临时阈值
	 * @param acct_id
	 * @param acctitem_id
	 * @param tempThreshold
	 */
	public void updateTempThreshold(String acctId, String acctitemId,
			int tempThreshold) throws JDBCException{
		cAcctAcctitemThresholdDao.updateTempThreshold(acctId, acctitemId, tempThreshold);

	}

	public void clearTempThreshold(String custId) throws Exception{
		cAcctAcctitemThresholdDao.clearTempThreshold(custId, getOptr().getCounty_id());
	}

	public void editAdjustReason(Integer doneCode,String reason) throws Exception {
		cAcctAcctitemAdjustDao.updateReason(doneCode, reason);
	}

	//生成账户编号
	/**
	 * 查询账户的所有账目信息
	 * @param acctId
	 * @return
	 */
	public List<CAcctAcctitem> queryAcctItemByAcctId(String acctId) throws JDBCException {
		return cAcctAcctitemDao.queryByAcctId(acctId);
	}

	public AcctitemDto queryAcctItemDtoByAcctitemId(String acctId,String acctItemId) throws JDBCException{
		return cAcctAcctitemDao.queryAcctItemDto(acctId, acctItemId);
	}
	
	
	public CAcctAcctitem queryAcctItemByAcctitemId(String acctId,String acctItemId) throws JDBCException {
		CAcct acct = this.queryByAcctId(acctId);
		CAcctAcctitem acctItem = cAcctAcctitemDao.queryByAcctItemId(acctId,acctItemId);
		if (acct!=null && !acct.getAcct_type().equals(SystemConstants.ACCT_TYPE_PUBLIC)){
			try{
				String dataRight = this.queryDataRightCon(getOptr(), DataRight.PROD_TERMINATE.toString());
				List<CProd> refundProdList = cProdDao.queryAllowRefundProd(dataRight);
				Boolean refund=false;
				for (CProd prod :refundProdList){
					if (acctItem.getAcctitem_id().equals(prod.getProd_id())){
						refund=true;
						break;
					}
				}
				
				if (refund==true)
					acctItem.setCan_refund_balance(acctItem.getCan_refund_balance_norule());//如果有退款的特殊权限，设置可退金额为原始的可退金额（没有考虑产品的退款属性）
			}catch(Exception e){
			}
		}
				
		return acctItem;
	}
	
	/**
	 * 查询客户的所有账目信息
	 */
	public List<AcctitemDto> queryAcctItemEsayByCustId(String custId)throws Exception{
		return  cAcctAcctitemDao.queryByCustId(custId,getOptr().getCounty_id());
	}
	/**
	 * 查询客户的所有账目信息
	 * @param custId
	 * @throws Exception
	 * @throws JDBCException
	 */
	public  List<AcctitemDto> queryAcctItemByCustId(String custId) throws JDBCException, Exception {
		
		List<AcctitemDto> acctitems = cAcctAcctitemDao.queryByCustId(custId,getOptr().getCounty_id());
		Map<String, CProdDto> cprods = CollectionHelper.converToMapSingle(cProdDao.queryProdByCustId(custId, getOptr().getCounty_id()), "acct_id","prod_id");
		Map<String, CProd> refundProdMap =new HashMap<String, CProd>();
		List<String> adjustList = new ArrayList<String>();
		
		try{
			String dataRight = this.queryDataRightCon(getOptr(), DataRight.PROD_TERMINATE.toString());
			List<CProd> refundProdList = cProdDao.queryAllowRefundProd(dataRight);
			refundProdMap = CollectionHelper.converToMapSingle(refundProdList,"prod_id");
		}catch(Exception e){
		}
		
		try{
			String dataRight = this.queryDataRightCon(getOptr(), DataRight.ADJUST.toString());
			adjustList = cAcctAcctitemDao.queryCanAdjust(dataRight);
		}catch(Exception e){
		}
		
		
		String specAcctitemFlag = queryTemplateConfig(TemplateConfigDto.Config.SPEC_ACCTITEM_FLAG
				.toString());
		String pubAcctitemTranFlag = queryTemplateConfig(TemplateConfigDto.Config.PUBLIC_ACCTITEM_TRAN_FLAG
				.toString());
		String ofl = queryTemplateConfig(TemplateConfigDto.Config.OWE_FEE_NUMBER.toString());
		String band_ofl = queryTemplateConfig(TemplateConfigDto.Config.BAND_OWE_FEE_NUMBER.toString());
		int ownFeeNumber = 1, bandOwnFeeNumber = 1;
		if(StringHelper.isNotEmpty(ofl))
			ownFeeNumber = Integer.parseInt(ofl);
		if(StringHelper.isNotEmpty(band_ofl))
			bandOwnFeeNumber = Integer.parseInt(band_ofl);
		
		for (AcctitemDto acctitem :acctitems){
			if (acctitem.getAcctitem_type() == null) {
				String prodId = acctitem.getAcctitem_id();
				CProdDto cprod = cprods.get(acctitem.getAcct_id()+"_"+prodId);
				if (cprod != null){
					acctitem.setAcctitem_name(cprod.getProd_name());
					acctitem.setProd_id(prodId);
					acctitem.setProd_name(cprod.getProd_name());
					acctitem.setProd_sn(cprod.getProd_sn());
					acctitem.setProd_status(cprod.getStatus());
					acctitem.setTariff_id(cprod.getTariff_id());
					acctitem.setTariff_name(cprod.getTariff_name());
					acctitem.setTariff_rent(cprod.getTariff_rent());
					acctitem.setNext_tariff_name(cprod.getNext_tariff_name());
					String nextTariffId = cprod.getNext_tariff_id();
					acctitem.setNext_tariff_id(nextTariffId);
					acctitem.setInvalid_date(cprod.getInvalid_date());
					acctitem.setIs_base(cprod.getIs_base());
					acctitem.setAllow_pay(specAcctitemFlag);
					
					if(cprod.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)){
						acctitem.setOwnFeeNumber(bandOwnFeeNumber);
					}else{
						acctitem.setOwnFeeNumber(ownFeeNumber);
					}
					acctitem.setIs_zero_tariff(cprod.getIs_zero_tariff());
					acctitem.setBilling_type(cprod.getBilling_type());
					acctitem.setBilling_cycle(cprod.getBilling_cycle());

					//产品本身不允许缴费
					boolean prodNotAllowPay = cprod.getAllow_pay().equals(SystemConstants.BOOLEAN_FALSE);
					//无效资费且没有未生效资费
					boolean invalidTariffAndNoNextTariff = SystemConstants.BOOLEAN_TRUE.equals(cprod.getIs_invalid_tariff()) && StringHelper.isEmpty(nextTariffId);
					if(prodNotAllowPay || invalidTariffAndNoNextTariff){
						acctitem.setAllow_pay(SystemConstants.BOOLEAN_FALSE);
					}else if (cprod.getIs_zero_tariff().equals(SystemConstants.BOOLEAN_TRUE) ){
						//0资费的时候,分两种情况,没有未生效资费,不允许缴费,如果有未生效资费,且不是0资费,则可以缴费
						PProdTariff nextTariff = pProdTariffDao.findByKey(nextTariffId);
						if(nextTariff != null && nextTariff.getRent() > 0){
							acctitem.setAllow_pay(SystemConstants.BOOLEAN_TRUE);
						}else{
							acctitem.setAllow_pay(SystemConstants.BOOLEAN_FALSE);
						}
					}
						
					//潜江地区平移基本包可转到基本节目包1中
					if (cprod.getCounty_id().equals(SystemConstants.COUNTY_9005)
							&& cprod.getProd_id().equals("2728")){
					}else{
						if (SystemConstants.BOOLEAN_FALSE.equals(cprod.getTrans()))
							acctitem.setCan_trans_balance(0);
					}
					if (refundProdMap.get(prodId) == null){//没有退款的特权
						if (SystemConstants.BOOLEAN_FALSE.equals(cprod.getRefund()))
							acctitem.setCan_refund_balance(0);
					}
				}
			}else if(acctitem.getAcctitem_type().equals(SystemConstants.ACCT_TYPE_PUBLIC)){
				acctitem.setAllow_tran(pubAcctitemTranFlag);
			}
			
			for (String acctItemId:adjustList){
				if (acctitem.getAcctitem_id().equals(acctItemId)){
					acctitem.setAllow_adjust(SystemConstants.BOOLEAN_TRUE);
					break;
				}
			}
			
		}
		return acctitems;
	}
	
	public List<AcctitemDto> queryAcctitemToCallCenter(Map<String,Object> params) throws Exception{
		return cAcctAcctitemDao.queryAcctitemToCallCenter(params, getOptr().getCounty_id());
	}
	
	/**
	 * 查询用户的所有账目信息
	 * @param userId
	 * @return
	 * @throws JDBCException
	 * @throws Exception
	 */
	public List<AcctitemDto> queryAcctItemByUserId(String userId) throws Exception {
		return cAcctAcctitemDao.queryByUserId(userId, getOptr().getCounty_id());
	}
	
	/**
	 * 根据用户编号，账目编号查询账目
	 * @param userId
	 * @param acctitemId
	 * @return
	 * @throws Exception
	 */
	public AcctitemDto queryAcctItemByUserId(String userId,String acctitemId) throws Exception{
		AcctitemDto acctitem = null;
		List<AcctitemDto> acctitemList = this.queryAcctItemByUserId(userId);
		for(AcctitemDto item : acctitemList){
			if(acctitemId.equals(item.getAcctitem_id())){
				acctitem = item;
			}
		}
		return acctitem;
	}
	
	/**
	 * 查询用户的所有账目信息和公用账目信息
	 * @param userId
	 * @return
	 * @throws JDBCException
	 * @throws Exception
	 */
	public List<AcctitemDto> queryAcctAndAcctItemByUserId(String custId ,String userId) throws Exception {
		return cAcctAcctitemDao.queryAcctAndAcctItemByUserId(custId, userId,
				getOptr().getCounty_id());
	}
	
	/**
	 * @param custId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public CAcct queryUserAcct(String custId, String userId) throws Exception {
		return cAcctDao.queryUserAcct(custId, userId, getOptr().getCounty_id());
	}
	
	/**
	 * 查询账目下余额明细
	 * @param acctitemId
	 * @return
	 * @throws JDBCException
	 * @see com.ycsoft.business.dao.core.acct.CAcctAcctitemActiveDao#queryByAcctitemId(java.lang.String)
	 */
	public List<AcctAcctitemActiveDto> queryActiveById(String acctId,String acctitemId)
			throws Exception {
		return cAcctAcctitemActiveDao.queryByAcctitemId(acctId,acctitemId,getOptr().getCounty_id());
	}
	
	/**
	 * 记录宽带升级异动
	 * @param doneCode
	 * @param busiCode
	 * @param custId
	 * @param userId
	 * @param acctId
	 * @param oldProdId
	 * @param newProdId
	 * @param oldTariffId
	 * @param newTariffId
	 * @param cashFee
	 * @param oldAcctItemActiveList
	 * @param newPresentFee
	 * @param oweFee				原升级账目欠费金额
	 * @param oweInactiveFee		欠费还有未解冻金额
	 * @throws Exception
	 */
	public void saveBandUpgradeInfo(Integer doneCode, String busiCode,
			String custId, String userId, String acctId, String oldProdId,
			String newProdId, String oldTariffId, String newTariffId,
			CAcctAcctitem oldAcctItem, List<AcctAcctitemActiveDto> activeList,
			int tariffPresentFee) throws Exception {
		List<CBandUpgradeRecord> list = new ArrayList<CBandUpgradeRecord>();
		CBandUpgradeRecord record = new CBandUpgradeRecord();
		setBaseInfo(record);
		record.setDone_code(doneCode);
		record.setBusi_code(busiCode);
		record.setCust_id(custId);
		record.setUser_id(userId);
		record.setAcct_id(acctId);
		record.setOld_tariff_id(oldTariffId);
		record.setNew_tariff_id(newTariffId);
		record.setOld_prod_id(oldProdId);
		record.setNew_prod_id(newProdId);
		
		int realBill = oldAcctItem.getReal_bill();
		int writeoffFee = realBill;		//原账目需要销账的金额
		
		List<AcctAcctitemActiveDto> oldCashList = new ArrayList<AcctAcctitemActiveDto>();
		List<AcctAcctitemActiveDto> oldUnCashList = new ArrayList<AcctAcctitemActiveDto>();
		int cash = 0, unCash = 0;
		for(AcctAcctitemActiveDto activeDto : activeList){
			if(activeDto.getIs_cash().equals(SystemConstants.BOOLEAN_TRUE)){
				oldCashList.add(activeDto);
				cash += activeDto.getBalance();
			}else{
				oldUnCashList.add(activeDto);
				unCash += activeDto.getBalance();
			}
		}
		
		Collections.sort(oldCashList, new Comparator<AcctAcctitemActiveDto>(){
			public int compare(AcctAcctitemActiveDto o1, AcctAcctitemActiveDto o2) {
				return o1.getPriority().intValue() - o2.getPriority().intValue();
			}
		});
		
		Collections.sort(oldUnCashList, new Comparator<AcctAcctitemActiveDto>(){
			public int compare(AcctAcctitemActiveDto o1, AcctAcctitemActiveDto o2) {
				if(o2.getFee_type().equals(SystemConstants.ACCT_FEETYPE_PRESENT)){
					return 1;
				}else{
					return o1.getPriority().intValue() - o2.getPriority().intValue();
				}
			}
		});
		
		//只有现金时，本月费用从现金冲销账
		if(cash > 0 && unCash == 0){
			for(AcctAcctitemActiveDto dto : activeList){
				if(writeoffFee == 0)	break;
				int b = dto.getBalance().intValue();
				if(dto.getIs_cash().equals(SystemConstants.BOOLEAN_TRUE)){
					if(b >= writeoffFee){
						dto.setBalance(b - writeoffFee);
						
						CBandUpgradeRecord writeoffRecord = new CBandUpgradeRecord();
						BeanUtils.copyProperties(record, writeoffRecord);
						writeoffRecord.setFee_type(dto.getFee_type());
						this.addWriteoffRecord(writeoffRecord, oldProdId, oldTariffId, list, b, writeoffFee);
						
						writeoffFee = 0;
					}else{
						dto.setBalance(0);
						
						CBandUpgradeRecord writeoffRecord = new CBandUpgradeRecord();
						BeanUtils.copyProperties(record, writeoffRecord);
						writeoffRecord.setFee_type(dto.getFee_type());
						this.addWriteoffRecord(writeoffRecord, oldProdId, oldTariffId, list, b, b);
						
						writeoffFee = writeoffFee - b;
					}
				}
			}
			writeoffFee = 0;
		}
		
		writeoffFee = this.addActiveBandRecord(doneCode, busiCode, custId, userId, acctId,
				oldProdId, oldTariffId, newProdId, newTariffId, writeoffFee,
				oldUnCashList, list, true);
		
		
		if(tariffPresentFee != 0){
			
			if(writeoffFee > 0){
				if(tariffPresentFee >= writeoffFee){
					CBandUpgradeRecord writeoffRecord = new CBandUpgradeRecord();
					BeanUtils.copyProperties(record, writeoffRecord);
					writeoffRecord.setFee_type(SystemConstants.ACCT_FEETYPE_PRESENT);
					this.addWriteoffRecord(writeoffRecord, oldProdId, oldTariffId, list, writeoffFee, writeoffFee);
					
					if(tariffPresentFee - writeoffFee > 0){
						
						CBandUpgradeRecord presentRecord = new CBandUpgradeRecord();
						BeanUtils.copyProperties(record, presentRecord);
						this.mergePresentRecord(list, tariffPresentFee - writeoffFee, presentRecord);
					}
					writeoffFee = 0;
				}else{
					CBandUpgradeRecord writeoffRecord = new CBandUpgradeRecord();
					BeanUtils.copyProperties(record, writeoffRecord);
					writeoffRecord.setFee_type(SystemConstants.ACCT_FEETYPE_PRESENT);
					this.addWriteoffRecord(writeoffRecord, oldProdId, oldTariffId, list, tariffPresentFee, tariffPresentFee);
					
					writeoffFee = writeoffFee - tariffPresentFee;
				}
			}else{
				CBandUpgradeRecord presentRecord = new CBandUpgradeRecord();
				BeanUtils.copyProperties(record, presentRecord);
				this.mergePresentRecord(list, tariffPresentFee, presentRecord);
			}
		}
		
		
		writeoffFee = this.addActiveBandRecord(doneCode, busiCode, custId, userId, acctId,
				oldProdId, oldTariffId, newProdId, newTariffId, writeoffFee,
				oldCashList, list, true);
		
		if(writeoffFee > 0){
			CBandUpgradeRecord oweRecord = new CBandUpgradeRecord();
			BeanUtils.copyProperties(record, oweRecord);
			oweRecord.setFee_type(SystemConstants.ACCT_FEETYPE_OWEFEE);
			oweRecord.setChange_type(SystemConstants.ACCT_CHANGE_TRANS);
			oweRecord.setPre_fee(0);
			oweRecord.setChange_fee(writeoffFee * -1);
			oweRecord.setFee(writeoffFee * -1);
			list.add(oweRecord);
		}else{
		}
		
		if(list.size() > 0){
			cBandUpgradeRecordDao.save(list.toArray(new CBandUpgradeRecord[list.size()]));
		}
	}
	
	private void addWriteoffRecord(CBandUpgradeRecord writeoffRecord, String oldProdId,
			String oldTariffId, List<CBandUpgradeRecord> list, int preWriteoffFee, int writeoffFee) {
		if(writeoffFee > 0){
			writeoffRecord.setOld_prod_id(oldProdId);
			writeoffRecord.setNew_prod_id(oldProdId);
			writeoffRecord.setOld_tariff_id(oldTariffId);
			writeoffRecord.setNew_tariff_id(oldTariffId);
			writeoffRecord.setChange_type(SystemConstants.ACCT_CHANGE_WRITEOFF);
			writeoffRecord.setPre_fee(preWriteoffFee);
			writeoffRecord.setChange_fee(writeoffFee * -1);
			writeoffRecord.setFee(preWriteoffFee - writeoffFee);
			list.add(writeoffRecord);
		}
	}
	
	private void mergePresentRecord(List<CBandUpgradeRecord> list, int fee,
			CBandUpgradeRecord presentRecord) {
		boolean flag = false;
		for(CBandUpgradeRecord bandRecord : list){
			if(bandRecord.getFee_type().equals(SystemConstants.ACCT_FEETYPE_PRESENT)
					&& bandRecord.getChange_type().equals(SystemConstants.ACCT_CHANGE_TRANS)){
				bandRecord.setChange_fee(bandRecord.getChange_fee() + fee);
				bandRecord.setFee(bandRecord.getFee() + fee);
				flag = true;
				break;
			}
		}
		
		if(!flag){
			presentRecord.setFee_type(SystemConstants.ACCT_FEETYPE_PRESENT);
			presentRecord.setChange_type(SystemConstants.ACCT_CHANGE_TRANS);
			presentRecord.setPre_fee(0);
			presentRecord.setChange_fee(fee);
			presentRecord.setFee(fee);
			list.add(presentRecord);
		}
	}
	
	private int addActiveBandRecord(Integer doneCode, String busiCode,
			String custId, String userId, String acctId, String oldProdId,
			String oldTariffId, String newProdId, String newTariffId,
			int writeoffFee, List<AcctAcctitemActiveDto> activeList,
			List<CBandUpgradeRecord> list, boolean isTran) throws Exception {
		CBandUpgradeRecord record = new CBandUpgradeRecord();
		setBaseInfo(record);
		record.setDone_code(doneCode);
		record.setBusi_code(busiCode);
		record.setCust_id(custId);
		record.setUser_id(userId);
		record.setAcct_id(acctId);
		record.setOld_prod_id(oldProdId);
		record.setNew_prod_id(newProdId);
		record.setOld_tariff_id(oldTariffId);
		record.setNew_tariff_id(newTariffId);
		record.setFee_type(SystemConstants.ACCT_FEETYPE_CASH);
		record.setChange_type(SystemConstants.ACCT_CHANGE_WRITEOFF);
		
		for(AcctAcctitemActiveDto activeDto : activeList){
			int balance = activeDto.getBalance();
			if(balance == 0) continue;
			String feeType = activeDto.getFee_type();
			if(writeoffFee > 0){
				CBandUpgradeRecord cashRecord = new CBandUpgradeRecord();
				BeanUtils.copyProperties(record, cashRecord);
				cashRecord.setOld_prod_id(oldProdId);
				cashRecord.setNew_prod_id(oldProdId);
				cashRecord.setOld_tariff_id(oldTariffId);
				cashRecord.setNew_tariff_id(oldTariffId);
				cashRecord.setFee_type(feeType);
				cashRecord.setChange_type(SystemConstants.ACCT_CHANGE_WRITEOFF);
				list.add(cashRecord);
				
				//有现金，先记录原账目销账异动
				//现金金额大于本月费用，原账目销掉本月费用，剩余现金金额转账到新账目
				if(balance >= writeoffFee){
					cashRecord.setPre_fee(balance);
					cashRecord.setChange_fee(writeoffFee * -1);
					cashRecord.setFee(balance - writeoffFee);
					
					if(balance - writeoffFee > 0){
						if(isTran){
							CBandUpgradeRecord cashTransRecord = new CBandUpgradeRecord();
							BeanUtils.copyProperties(record, cashTransRecord);
							cashTransRecord.setFee_type(feeType);
							cashTransRecord.setChange_type(SystemConstants.ACCT_CHANGE_TRANS);
							cashTransRecord.setPre_fee(0);
							cashTransRecord.setChange_fee(balance - writeoffFee);
							cashTransRecord.setFee(balance - writeoffFee);
							list.add(cashTransRecord);
						}
					}
					writeoffFee = 0;
				}else{
					cashRecord.setPre_fee(balance);
					cashRecord.setChange_fee(balance * -1);
					cashRecord.setFee(0);
					
					//现金小于本月费用，原账目销掉现金金额，剩余由非现金销账
					writeoffFee = writeoffFee - balance;	//剩余未销掉的本月费用
				}
				
			}else{
				if(isTran){
					CBandUpgradeRecord cashTransRecord = new CBandUpgradeRecord();
					BeanUtils.copyProperties(record, cashTransRecord);
					cashTransRecord.setFee_type(feeType);
					cashTransRecord.setChange_type(SystemConstants.ACCT_CHANGE_TRANS);
					cashTransRecord.setPre_fee(0);
					cashTransRecord.setChange_fee(balance);
					cashTransRecord.setFee(balance);
					list.add(cashTransRecord);
				}
				writeoffFee = 0;
			}
		}
		return writeoffFee;
	}
	
	/**
	 * @param doneCode
	 * @param busiCode
	 * @param oldAcctId
	 * @param newCustId
	 * @param newUserId
	 * @return
	 * @throws Exception
	 */
	public String updateAcct(Integer doneCode, String busiCode, String oldAcctId, 
			String newCustId, String newUserId) throws Exception {
		String newAcctId = null;
		CAcct oldAcct = cAcctDao.findByKey(oldAcctId);
		String acctType = oldAcct.getAcct_type();
		String oldCustId = oldAcct.getCust_id();
		
		List<CAcctAcctitem> oldAcctItemList = this.queryAcctItemByAcctId(oldAcctId);
		if(acctType.equals(SystemConstants.ACCT_TYPE_PUBLIC)){
			CAcct newAcct = this.queryCustAcctByCustId(newCustId);
			newAcctId = newAcct.getAcct_id();
			List<CAcctAcctitem> newAcctItemList = this.queryAcctItemByAcctId(newAcct.getAcct_id());
			Map<String, CAcctAcctitem> newAcctItemMap = CollectionHelper.converToMapSingle(newAcctItemList, "acctitem_id");
			for(CAcctAcctitem oldAcctitem : oldAcctItemList){
				//合并公共账目余额
				if(oldAcctitem.getActive_balance() > 0){
					CAcctAcctitem newAcctitem = newAcctItemMap.get(oldAcctitem.getAcctitem_id());
					
					this.saveAcctitemMergeChange(doneCode, busiCode, oldCustId, oldAcctitem.getAcct_id(), 
							oldAcctitem.getAcctitem_id(), newCustId, newAcctitem.getAcct_id(), newAcctitem.getAcctitem_id());
				}
				
				cAcctDao.updateByAcctItemId(doneCode, oldAcctitem.getAcct_id(), oldAcctitem.getAcctitem_id(), newAcctId, newCustId);
				this.saveAcctitemHis(doneCode, oldAcctitem, newAcctId, newCustId);
			}
		}else{
			//新建专用账户
			newAcctId = this.createAcct(newCustId, newUserId, SystemConstants.ACCT_TYPE_SPEC, null);
			cUserDao.updateAcctId(newUserId, newAcctId);
			for(CAcctAcctitem oldAcctitem : oldAcctItemList){
				
				//新建专用账目
				CAcctAcctitem newAcctitem = new CAcctAcctitem();
				BeanUtils.copyProperties(oldAcctitem, newAcctitem);
				newAcctitem.setAcct_id(newAcctId);
				
				this.saveAcctitemMergeChange(doneCode, busiCode, oldCustId, oldAcctitem.getAcct_id(), 
						oldAcctitem.getAcctitem_id(), newCustId, newAcctitem.getAcct_id(), 
						newAcctitem.getAcctitem_id());
				
				cAcctDao.updateByAcctItemId(doneCode, oldAcctitem.getAcct_id(), oldAcctitem.getAcctitem_id(), newAcctId, newCustId);
				//先更新账目相关表数据，再创建账目表
				cAcctAcctitemDao.save(newAcctitem);
				this.saveAcctitemHis(doneCode, oldAcctitem, newAcctId, newCustId);
				
			}
		}
		
		//修改已出账欠费账单，未出账账单 和日租
		bBillDao.updateBillByAcctId(oldAcctId, newCustId, newUserId, newAcctId);
		
		CAcctHis oldAcctHis = new CAcctHis();
		BeanUtils.copyProperties(oldAcct, oldAcctHis);
		oldAcctHis.setDone_code(doneCode);
		cAcctHisDao.save(oldAcctHis);
		
		cAcctDao.remove(oldAcctId);
		return newAcctId;
	}
	
	private void saveAcctitemMergeChange(Integer doneCode, String busiCode, String fromCustId, String fromAcctId, String fromAcctitemId,
			String toCustId, String toAcctId, String toAcctitemId) throws Exception {
		
		List<AcctAcctitemActiveDto> oldActiveList = this.queryActiveById(fromAcctId, fromAcctitemId);
		for(AcctAcctitemActiveDto active : oldActiveList){
			if(active.getBalance() > 0){
				CAcctAcctitemMerge merge = new CAcctAcctitemMerge();
				merge.setDone_code(doneCode);
				merge.setFrom_cust_id(fromCustId);
				merge.setFrom_acct_id(fromAcctId);
				merge.setFrom_acctitem_id(fromAcctitemId);
				merge.setTo_cust_id(toCustId);
				merge.setTo_acct_id(toAcctId);
				merge.setTo_acctitem_id(toAcctitemId);
				merge.setFee_type(active.getFee_type());
				merge.setBalance(active.getBalance());
				cAcctAcctitemMergeDao.save(merge);
				
				this.saveAcctitemChange(doneCode, busiCode, fromCustId, fromAcctId, fromAcctitemId, 
						SystemConstants.ACCT_CHANGE_TRANS, active.getFee_type(), active.getBalance(), active.getBalance(), null);
				this.saveAcctitemChange(doneCode, busiCode, toCustId, toAcctId, toAcctitemId, 
						SystemConstants.ACCT_CHANGE_INIT, active.getFee_type(), active.getBalance(), 0, null);
			}
		}
	}
	
	private void saveAcctitemHis(Integer doneCode, CAcctAcctitem oldAcctitem, 
			String newAcctId, String newCustId) throws Exception {
		
		CAcctAcctitemHis oldAcctitemHis = new CAcctAcctitemHis();
		BeanUtils.copyProperties(oldAcctitem, oldAcctitemHis);
		oldAcctitemHis.setDone_code(doneCode);
		cAcctAcctitemHisDao.save(oldAcctitemHis);
		
		cAcctAcctitemDao.removeByAcctItemId(oldAcctitem.getAcct_id(), oldAcctitem.getAcctitem_id());
	}

	/**
	 * 查找激活资金类型
	 * @param acctId
	 * @param acctItemId
	 * @param feeType
	 * @return
	 * @throws Exception
	 */
	public CAcctAcctitemActive queryActiveByFeetype(String acctId,String acctItemId,String feeType) throws Exception{
		return cAcctAcctitemActiveDao.queryActiveByFeetype(acctId, acctItemId, feeType, getOptr().getCounty_id());
	}


	public int querySumFeetype(String acctId,String acctItemId,String feeType) throws Exception{
		return cAcctAcctitemActiveDao.querySumFeetype(acctId, acctItemId, feeType, getOptr().getCounty_id());
	}

	public List<CAcctAcctitemActive> queryActiveMinusByCustId(String custId) throws Exception{
		return cAcctAcctitemActiveDao.queryMinusByCustId(custId, getOptr().getCounty_id());
	}
	/**
	 * 查询账目下阈值明细
	 * @param acctitemId
	 * @return
	 * @throws JDBCException
	 * @see com.ycsoft.business.dao.core.acct.CAcctAcctitemThresholdDao#queryByAcctitemId(java.lang.String)
	 */
	public List<CAcctAcctitemThreshold> queryThreshold(String acctId,String acctitemId)
			throws JDBCException {
		return cAcctAcctitemThresholdDao.queryByAcctitemId(acctId,acctitemId);
	}

	/**
	 * 查询账目下冻结信息
	 * @param acctitemId
	 * @return
	 * @throws JDBCException
	 * @see com.ycsoft.business.dao.core.acct.CAcctAcctitemInactiveDao#queryByAcctitemId(java.lang.String)
	 */
	public List<CAcctAcctitemInactive> queryInactive(String acctId,String acctitemId)
			throws JDBCException {
		return cAcctAcctitemInactiveDao.queryByAcctitemId(acctId,acctitemId);
	}
	
	/**
	 * 返回账目调账信息
	 * @param acctId
	 * @param acctitemId
	 * @return
	 * @throws JDBCException
	 */
	public List<CAcctAcctitemAdjust> queryAdjust(String acctId,String acctitemId)throws JDBCException {
		return cAcctAcctitemAdjustDao.queryByAcctitemId(acctId,acctitemId);
	}
	/**
	 * 根据feesn获取赠送记录
	 * @param feeSn
	 * @return
	 * @throws Exception
	 */
	public CAcctAcctitemInactive queryInactiveByFeesn(String feeSn) throws Exception{
		return cAcctAcctitemInactiveDao.queryByFeeSn(feeSn);
	}
	
	public CAcctAcctitemInactive queryByPromotionSn(String promotionSn,String acctId,String acctitemId) throws Exception{
		return cAcctAcctitemInactiveDao.queryByPromotionSn(promotionSn, acctId, acctitemId);
	}
	
	/**
	 * 根据feesn获取赠送记录
	 * @param feeSn
	 * @return
	 * @throws Exception
	 */
	public CAcctAcctitemInactive queryPromInactiveByFeesn(String feeSn,String acctId,String acctItemId) throws Exception{
		return cAcctAcctitemInactiveDao.queryPromByFeeSn(feeSn,acctId,acctItemId);
	}
	
	/**
	 * @param promotion_sn
	 * @return
	 */
	public List<CAcctAcctitemInactive> queryInactiveByPromSn(String promotionSn)throws Exception {
		return cAcctAcctitemInactiveDao.queryByPromSn(promotionSn);
	}

	/**
	 * 查询账目下冻结明细
	 * @param acctitemId
	 * @return
	 * @throws JDBCException
	 * @see com.ycsoft.business.dao.core.acct.CAcctAcctitemOrderDao#queryByAcctitemId(java.lang.String)
	 */
	public List<CAcctAcctitemOrder> queryOrder(String acctId,String acctitemId)
			throws JDBCException {
		return cAcctAcctitemOrderDao.queryByAcctitemId(acctId,acctitemId);
	}

	/**
	 * @param cust_id
	 * @param doneCode
	 * @return
	 */
	public List<CAcctAcctitemInactive> queryPromAcctItemInactive(String cust_id,Integer doneCode, boolean fromHistory) throws Exception{
		return cAcctAcctitemInactiveDao.queryByPromDoneCode(doneCode,cust_id, fromHistory);
	}
	
	/**
	 * 查询客户的所有账户信息
	 * @param custId
	 * @return
	 */
	public List<AcctDto> queryAcctsByCustId(CCust cust) throws Exception {
		List<AcctitemDto> acctItems = queryAcctItemByCustId(cust.getCust_id());
		Map<String, List<AcctitemDto>> acctitemMap = CollectionHelper.converToMap(acctItems,"acct_id");
		
		List<CAcct> accts = cAcctDao.queryAcctByCustId(cust.getCust_id(),cust.getCounty_id());
		
		List<AcctDto> result = new ArrayList<AcctDto>();
		for (CAcct c : accts) {
			AcctDto acct = new AcctDto();
			BeanUtils.copyProperties(c,acct);
			//设置账目信息
			acct.setAcctitems(acctitemMap.get(c.getAcct_id()));
			fillAcctAllowPay(acct,cust);
			

			result.add(acct);
		}
		
		return result;
	}
	/**
	 * 查询账户信息
	 * @param cust
	 * @return
	 * @throws Exception
	 */
	public List<AcctDto> queryAcctsEsayByCustId(CCust cust) throws Exception {

		CAcct c=queryCustAcctByCustId(cust.getCust_id());
		Map<String, List<AcctitemDto>> acctitemMap = CollectionHelper.converToMap(queryAcctItemEsayByCustId(cust.getCust_id()),"acct_id");
		
		AcctDto acct = new AcctDto();
		BeanUtils.copyProperties(c,acct);
		acct.setAcctitems(acctitemMap.get(c.getAcct_id()));
		fillAcctAllowPay(acct,cust);
		
		List<AcctDto> result = new ArrayList<AcctDto>();
		result.add(acct);

		return result;
	}
	
	
	/**
	 * 查询基本包的账户信息
	 * @param custIds
	 * @return
	 */
	public List<UnitPayDto> queryBaseProdAcctItems(String[] custIds,String prodId) throws JDBCException {
		return cAcctAcctitemDao.queryBaseProdAcctItems(custIds,prodId);
	}
	
	/**
	 * 单位缴费，查询可以缴费的产品
	 * @param custIds
	 * @return
	 */
	public List<CProd> querySelectableProds(String[] custIds) throws Exception {
		return cAcctAcctitemDao.querySelectableProds(custIds);
	}
	
	public CAcct queryByAcctId(String acctId) throws JDBCException{
		return cAcctDao.findByKey(acctId);
	}

	private void fillAcctAllowPay(AcctDto acct,CCust cust) throws Exception {
		String public_acctitem_flag = queryTemplateConfig(TemplateConfigDto.Config.PUBLIC_ACCTITEM_FLAG
				.toString());
		String spec_pulic_acctitem_flag = queryTemplateConfig(TemplateConfigDto.Config.SPEC_PULIC_ACCTITEM_FLAG
				.toString());
		String ruleId = queryTemplateConfig(TemplateConfigDto.Config.SPEC_ACCTITEM_RULE
				.toString());
		String spec_public_ruleId = queryTemplateConfig(TemplateConfigDto.Config.SPEC_PULIC_ACCTITEM_RULE
				.toString());
		if (acct.getAcctitems() != null){
			for (AcctitemDto acctitem : acct.getAcctitems()) {
				if (StringHelper.isNotEmpty(acctitem.getAcctitem_type())){
					if (acctitem.getAcctitem_type().equals(
							SystemConstants.ACCT_TYPE_PUBLIC)) {
						// 设置公用账目
						acctitem.setAllow_pay(public_acctitem_flag);
					} else if (acctitem.getAcctitem_type().equals(
							SystemConstants.ACCT_TYPE_SPEC)) {
						// 设置专项公用账目
						if(spec_pulic_acctitem_flag.equals(SystemConstants.BOOLEAN_TRUE)){
							acctitem.setAllow_pay(canAcctItemPay(spec_public_ruleId,
									acctitem.getAcctitem_id(),cust)?SystemConstants.BOOLEAN_TRUE:SystemConstants.BOOLEAN_FALSE);
						}else{
							acctitem.setAllow_pay(spec_pulic_acctitem_flag);
						}
					} else if (acctitem.getAcctitem_type().equals(
							SystemConstants.ACCT_TYPE_SPECFEE)) {
						// 设置特殊账目，不允许
						acctitem.setAllow_pay(SystemConstants.BOOLEAN_FALSE);
					}
				}else{
					//专用账目
					if (SystemConstants.BOOLEAN_TRUE.equals(acctitem.getAllow_pay()))
						acctitem.setAllow_pay(
							canAcctItemPay(ruleId,
									acctitem.getProd_id(),cust)?SystemConstants.BOOLEAN_TRUE:SystemConstants.BOOLEAN_FALSE);
				}
			}
		}
	}
	
	private boolean canAcctItemPay(String ruleId,String prodId,CCust cust){
		if (StringHelper.isEmpty(ruleId) || cust.getCust_type().equals(SystemConstants.CUST_TYPE_NONRESIDENT))
			return true;
		else {
			Map<String,Object> varMap = new HashMap<String,Object>();
			varMap.put("prod_id", prodId);
			ExpressionUtil expressionUtil=new ExpressionUtil(beanFactory);
			return  expressionUtil.parseBoolean(ruleId, varMap);
		}
	}
	
	public CAcct queryCustAcctByCustId(String custId) throws Exception {
		CAcct acct = cAcctDao.findCustAcctByCustId(custId,getOptr().getCounty_id());
//		if (acct == null)
//			throw new ComponentException(ErrorCode.AcctPublicNotExists);
		return acct;
	}
	/**
	 * @param doneCode
	 * @return
	 */
	public CAcctAcctitemAdjust queryAdjustByDoneCode(Integer doneCode)throws Exception  {
		return cAcctAcctitemAdjustDao.queryByDoneCode(doneCode).get(0);
	}

	public List<CAcctAcctitemTrans> queryTransByDoneCode(Integer doneCode)throws Exception  {
		return cAcctAcctitemTransDao.queryByDoneCode(doneCode);
	}

	/**
	 * 获取往月欠费
	 */
	public CAcctAcctitem queryOweFee(String custId) throws JDBCException, ComponentException {
		CAcctAcctitem acct = cAcctAcctitemDao.getOweFee(custId,SystemConstants.ACCTITEM_TJ);
		return acct;
	}

	/**
	 * 查询账目下异动明细
	 * @param acctitemId
	 * @return
	 * @throws JDBCException
	 * @see com.ycsoft.business.dao.core.acct.CAcctAcctitemChangeDao#queryByAcctitemId(java.lang.String)
	 */
	public Pager<CAcctAcctitemChange> queryAcctitemChange(String acctId,String acctitemId, Integer start, Integer limit)
			throws JDBCException {
		return cAcctAcctitemChangeDao.queryByAcctitemId(acctId,acctitemId,start,limit);
	}
	
	public List<CAcctAcctitemChange> queryAcctitemChangeByBusiInfo(String acctId,String acctitemId,String busiCode,Integer doneCode)throws Exception {
		return cAcctAcctitemChangeDao.queryByBusiInfo(acctId, acctitemId, busiCode, doneCode);
	}
	
	/**
	 * 查找模转数转到公用账目的记录
	 * @param custId
	 * @return
	 */
	public List<AcctAcctitemChangeDto> queryAtvToDtvAcctitemChange(String custId) throws Exception{
		return cAcctAcctitemChangeDao.queryAtvToDtvAcctitemChange(custId,getOptr().getCounty_id());
	}
	
	/**
	 * 查询订购回退的作废记录
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public AcctAcctitemChangeDto queryOrderZFAcctitemChange(String userId,
			String custId) throws Exception {
		return cAcctAcctitemChangeDao.queryOrderZFAcctitemChange(userId, custId, getOptr().getCounty_id());
	}
	
	public void updateActiveBanlance(String acctId, String acctItemId, int fee,
			int inactiveFee, int refundFee, int transFee) throws Exception {
		cAcctAcctitemDao.updateActiveBanlance(acctId, acctItemId, fee,
				refundFee, transFee, getOptr().getCounty_id());
	}
	public void updateRealFee(String acctId, String acctItemId, int fee) throws Exception {
		cAcctAcctitemDao.updateRealFee(acctId, acctItemId, fee);
	}

	public int querySumWriteOff(String acctId,String acctitemId,String feeType) throws Exception{
		return cAcctAcctitemChangeDao.querySumWriteOff(acctId,acctitemId,feeType);
	}
	
	/**
	 * 查询分公司账户
	 * @return
	 * @throws Exception
	 */
	public List<CGeneralAcct> queryCompanyAcct() throws Exception{
		return cGeneralAcctDao.queryCompanyAcct();
	}
	
	/**
	 * 生成通用账户编号
	 * @return
	 * @throws Exception
	 */
	private String gGeneralAcctId() throws Exception{
		return cGeneralAcctDao.findSequence().toString();
	}
	
	/**
	 * 生成合同编号
	 * @return
	 * @throws Exception
	 */
	private Integer gGeneralContractId() throws Exception{
		return Integer.parseInt(cGeneralContractDao.findSequence().toString());
	}
	
	/**
	 * 生成账户编号
	 * @return
	 * @throws Exception
	 */
	public String gAcctId() throws Exception{
		return cAcctDao.findSequence().toString();
	}
	/**
	 * 保存银行签约信息
	 * @param acctId 账户Id
	 * @param signTime 签约日期
	 * @param cBankAgree  CBankAgree 对象
	 * @throws JDBCException
	 */
	public void saveSignBank(Integer doneCode,String acctId,CCust cust,CBankAgree cBankAgree) throws Exception {
		CAcctBank cAcctBank = new CAcctBank();
		cAcctBank.setAcct_id(acctId);
		cAcctBank.setAccount_name(cBankAgree.getB_name());
		cAcctBank.setBank_account(cBankAgree.getB_acno());
		cAcctBank.setBank_code(cBankAgree.getB_bkno());
		cAcctBank.setSign_time(DateHelper.parseDate(cBankAgree.getB_wkdt(), DateHelper.FORMAT_YMD_STR));
		cAcctBank.setArea_id(getOptr().getArea_id());
		cAcctBank.setCounty_id(getOptr().getCounty_id());
		cAcctBank.setStatus(StatusConstants.ACTIVE);
		cAcctBank.setBank_pay_type(SystemConstants.PAY_TYPE_BANK_DEDU);
		cAcctBank.setCust_id(cust.getCust_id());
		if (!cust.getCust_class().equals(SystemConstants.CUST_CLASS_YBKH)) {
			//非一般客户，签约后先停止卡扣
			cAcctBank.setStatus(StatusConstants.STOP);
		}
		
		//record change 
		CAcctBank oldAcctBank = cAcctBankDao.querySignBankByAcctId(acctId, cAcctBank.getBank_pay_type());
		if (oldAcctBank == null)
			oldAcctBank = new CAcctBank();
		if (StatusConstants.STOP.equals(oldAcctBank.getStatus())){
			//暂停卡扣的客户，重新签约，继续暂停卡扣
			cAcctBank.setStatus(StatusConstants.STOP);
		}
		recordBankChange(doneCode, cust.getCust_id(), oldAcctBank.getBank_code(),
				cAcctBank.getBank_code(), oldAcctBank.getBank_account(), cAcctBank.getBank_account(),oldAcctBank.getStatus(),cAcctBank.getStatus());
		
		//先删除现有银行签约信息
		cAcctBankDao.removeByAcctIdPayType(acctId, cAcctBank.getBank_pay_type());
		//保存最新的银行签约信息
		cAcctBankDao.save(cAcctBank);
	}
	
	private void recordBankChange(int doneCode, String custId,
			String oldBankCode, String newBankCode, String oldBankAccount,
			String newBankAccount,String oldStatus,String newStatus) throws Exception {
		List<CCustPropChange> cpcList = new ArrayList<CCustPropChange>();
		
		if(!newBankCode.equals(oldBankCode)){
			CCustPropChange propChange = new CCustPropChange();
			propChange.setDone_code(doneCode);
			propChange.setCust_id(custId);
			propChange.setColumn_name("bank_code");
			propChange.setParam_name(DictKey.BANK_CODE.name());
			propChange.setOld_value(oldBankCode);
			propChange.setNew_value(newBankCode);
			propChange.setCounty_id(getOptr().getCounty_id());
			propChange.setArea_id(getOptr().getArea_id());
			
			cpcList.add(propChange);
		}
		
		if(!newBankAccount.equals(oldBankAccount)){
			CCustPropChange propChange2 = new CCustPropChange();
			propChange2.setDone_code(doneCode);
			propChange2.setCust_id(custId);
			propChange2.setColumn_name("bank_account");
			propChange2.setOld_value(oldBankAccount);
			propChange2.setNew_value(newBankAccount);
			propChange2.setCounty_id(getOptr().getCounty_id());
			propChange2.setArea_id(getOptr().getArea_id());
			
			cpcList.add(propChange2);
		}
		
		if(!newStatus.equals(oldStatus)){
			CCustPropChange propChange2 = new CCustPropChange();
			propChange2.setDone_code(doneCode);
			propChange2.setCust_id(custId);
			propChange2.setColumn_name("bank_status");
			propChange2.setOld_value(oldStatus);
			propChange2.setNew_value(newStatus);
			propChange2.setCounty_id(getOptr().getCounty_id());
			propChange2.setArea_id(getOptr().getArea_id());
			
			cpcList.add(propChange2);
		}		
		
		if(cpcList.size() >0){
			cCustProdChangeDao.save(cpcList.toArray(new CCustPropChange[]{}));
		}
	}
	
	/**
	 * 删除银行签约信息
	 * @param doneCode
	 * @param acctId 账户id
	 * @param custId
	 * @param bankPayType 银行支付类型
	 * @param optionType
	 * @throws Exception
	 */
	public void removeSignBank(Integer doneCode, String acctId, String custId,
			String bankPayType, Date time) throws Exception {
		// record change for remove bank sign infomation
		CAcctBank oldAcctBank = cAcctBankDao.querySignBankByAcctId(acctId,
				bankPayType);
		// 原客户未签约
		if (oldAcctBank != null) {
			recordBankChange(doneCode, custId, oldAcctBank
					.getBank_code(), "", oldAcctBank.getBank_account(), "",oldAcctBank.getStatus(),"");
			cAcctBankDao.updateByAcctId(acctId, bankPayType, "2", time);
		}
	}
	
	/**
	 * 查询银行签约信息
	 * @param acctId
	 * @return
	 * @throws JDBCException
	 */
	public CAcctBank querySignBankByAcctId(String acctId,String bankPayType) throws JDBCException{
		return cAcctBankDao.querySignBankByAcctId(acctId,bankPayType);
	}

	public CBankPay queryBankPay(String banklogid) throws Exception{
		return cBankPayDao.queryBankPayByLogId(banklogid);
	}
	public void saveBankPay(CBankPay cBankPay) throws JDBCException{
		cBankPayDao.save(cBankPay);
	}

	/**
	 * @param doneCode
	 */
	public void removeAdjust(Integer doneCode) throws Exception{
		cAcctAcctitemAdjustDao.removeByDoneCode(doneCode);

	}

	/**
	 * @param doneCode
	 */
	public void removeTrans(Integer doneCode)  throws Exception{
		cAcctAcctitemTransDao.removeByDoneCode(doneCode);

	}
	/**
	 * @param doneCode
	 */
	public void removeChange(Integer doneCode)  throws Exception{
		cAcctAcctitemChangeDao.removeByDoneCode(doneCode);

	}
	/**
	 * @param acct_id
	 * @param prod_id
	 */
	public void removeAcctItemWithoutHis(String custId,String acctId, String acctItemId, Integer doneCode,String busiCode)  throws Exception{
		CAcctAcctitem acctitem = cAcctAcctitemDao.queryByAcctItemId(acctId, acctItemId);
		if (acctitem == null) return ;
		CAcctAcctitemHis acctitemHis = new CAcctAcctitemHis();
		BeanUtils.copyProperties(acctitem, acctitemHis);
		acctitemHis.setDone_code(doneCode);
		cAcctAcctitemHisDao.save(acctitemHis);
		
		//查找账目的资金明细
		List<AcctAcctitemActiveDto> acctitemActiveList = cAcctAcctitemActiveDao.queryByAcctitemId(acctId, acctItemId, acctitem.getCounty_id());
		//生成作废记录并记录账目资金异动
		
		for (AcctAcctitemActiveDto acctitemActive:acctitemActiveList){
			if (acctitemActive.getBalance()>0){
				createAcctitemInvalid(custId, acctId, acctItemId, doneCode,
						busiCode, acctitemActive.getFee_type(),acctitemActive.getBalance());
			}
			
		}
		
		cAcctAcctitemDao.removeByAcctItemId(acctId,acctItemId);
		cAcctAcctitemActiveDao.removeByAcctItemId(acctId,acctItemId);
		//删除冻结资金记录，并记录历史
		cAcctAcctitemInactiveDao.removeByAcctItemIdWithHis(acctId,acctItemId,doneCode);
		cAcctAcctitemThresholdDao.removeByAcctItemId(acctId,acctItemId);
		//cAcctAcctitemChangeDao.removeByAcctItemId(acctId,acctItemId);
	}

	public void createAcctitemInvalid(String custId, String acctId,
			String acctItemId, Integer doneCode, String busiCode,
			String feeType,int invalidFee) throws Exception,
			BeansException {
		String billingCycle = DateHelper.format(new Date(), DateHelper.FORMAT_YM);
		CAcctAcctitemChange change = new CAcctAcctitemChange();
		change.setCust_id(custId);
		change.setDone_code(doneCode);
		change.setBusi_code(busiCode);
		change.setAcct_id(acctId);
		change.setAcctitem_id(acctItemId);
		change.setArea_id(getOptr().getArea_id());
		change.setCounty_id(getOptr().getCounty_id());
		change.setOptr_id(getOptr().getOptr_id());
		change.setDept_id(getOptr().getDept_id());
		change.setBilling_cycle_id(billingCycle);
		change.setFee_type(feeType);
		change.setPre_fee(invalidFee);
		change.setChange_fee(invalidFee*-1);
		change.setFee(0);
		change.setChange_type(SystemConstants.ACCT_CHANGE_INVALID);
		if (invalidFee>0)
			this.cAcctAcctitemChangeDao.save(change);
		
		CAcctAcctitemInvalid acctitemInvalid = new CAcctAcctitemInvalid();
		BeanUtils.copyProperties(change,acctitemInvalid);
		acctitemInvalid.setInvalid_type(SystemConstants.ACCT_CHANGE_INVALID);
		acctitemInvalid.setInvalid_fee(invalidFee);
		acctitemInvalid.setIs_active(1);
		String prodSn = null;
		String tariffId = null;
		
		List<CProdHis> prodList = cProdHisDao.queryByAcctItem(acctId, acctItemId, getOptr().getCounty_id());
		if (prodList != null && prodList.size()>0){
			prodSn = prodList.get(0).getProd_sn();
			tariffId = prodList.get(0).getTariff_id();
		} else {
			
			List<CProd> prodList1 = cProdDao.queryByAcctItem(acctId, acctItemId, getOptr().getCounty_id());
			if (prodList1 != null && prodList1.size()>0){
				prodSn = prodList1.get(0).getProd_sn();
				tariffId = prodList1.get(0).getTariff_id();
			}
		}
		acctitemInvalid.setProd_sn(prodSn);
		acctitemInvalid.setTariff_id(tariffId);
		
		this.cAcctAcctitemInvalidDao.save(acctitemInvalid);
	}
	
	/**
	 * @param generalAcctList
	 */
	public void updateGeneralAcct(List<CGeneralAcct> generalAcctList) throws JDBCException {
		cGeneralAcctDao.update(generalAcctList.toArray(new CGeneralAcct[generalAcctList.size()]));
	}
	
	/**
	 * @return
	 */
	public List<SDept> queryCompanyWithOutAcct()  throws JDBCException {
		return cGeneralAcctDao.queryCompanyWithOutAcct();
	}


	/**
	 * 保存通用账户
	 * @param generalAcct
	 */
	public void saveGeneralAcct(CGeneralAcct generalAcct) throws Exception {
		generalAcct.setG_acct_id(gGeneralAcctId());
		cGeneralAcctDao.save(generalAcct);
	}
	
	/**
	 * 根据编号查询
	 * @param contractId
	 * @return
	 * @throws Exception
	 */
	public CGeneralAcct queryGeneralAcctById(String gAcctId) throws Exception{
		return cGeneralAcctDao.findByKey(gAcctId);
	}

	/**
	 * 保存预收款或工程款合同
	 * @param generalContract
	 */
	public void saveGeneralContract(CGeneralContract generalContract) throws Exception {
		if(generalContract.getContract_id() != null){
			cGeneralCredentialDao.deleteByContractId(generalContract.getContract_id());
		}
		
		
		generalContract.setContract_id(gGeneralContractId());
		generalContract.setCounty_id(getOptr().getCounty_id());
		generalContract.setOptr_id(getOptr().getOptr_id());
		cGeneralContractDao.save(generalContract);
	}
	
	/**
	 * 保存合同凭据明细
	 * @param contractDetail
	 * @throws JDBCException
	 */
	public void saveGeneralContractDetail(CGeneralContractDetail contractDetail) throws JDBCException{
		cGeneralContractDetailDao.save(contractDetail);
	}
	
	/**
	 * 保存合同凭据
	 * @param generalCredentialList
	 */
	public void saveGeneralCredentialList(
			List<CGeneralCredential> generalCredentialList) throws JDBCException {
		cGeneralCredentialDao.save(generalCredentialList.toArray(new CGeneralCredential[generalCredentialList.size()]));
	}
	
	/**
	 * 修改凭据
	 * @param lastGeneralCredential
	 */
	public void updateGeneralCredential(CGeneralCredential lastGeneralCredential) throws JDBCException {
		cGeneralCredentialDao.update(lastGeneralCredential);
	}
	
	/**
	 * 验证合同号是否重复
	 * @return
	 * @throws Exception
	 */
	public CGeneralContract queryByContractNo(String contractNo) throws Exception{
		return cGeneralContractDao.queryByContractNo(contractNo);
	}

	/**
	 * 查询合同凭据信息
	 * @param contractId
	 * @param start
	 * @param limit
	 * @return
	 */
	public Pager<CGeneralCredential> queryCredential(String contractId,
			Integer start, Integer limit) throws JDBCException {
		return cGeneralCredentialDao.queryCredential(contractId,start,limit);
	}
	
	/**
	 * @param contractId
	 * @param start
	 * @param limit
	 * @return
	 */
	public Pager<CGeneralContractPay> queryPayInfo(String contractId,
			Integer start, Integer limit) throws JDBCException {
		return cGeneralContractPayDao.queryPayInfo(contractId,start,limit);
	}


	
	/**
	 * 根据地区查询预收款或工程款
	 * @return
	 */
	public Pager<GeneralContractDto> queryGeneralContracts(Integer start, Integer limit, String query) throws Exception{
		return cGeneralContractDao.queryGeneralContracts(start,limit,query,getOptr().getCounty_id());
	}

	/**
	 * 根据编号查询
	 * @param contractId
	 * @return
	 * @throws Exception
	 */
	public CGeneralContract queryGeneralContractById(String contractId) throws Exception{
		return cGeneralContractDao.findByKey(contractId);
	}
	
	/**
	 * 保存合同付款信息
	 * @param doneCode
	 * @param contractId
	 * @param fee
	 */
	public void saveGeneralContractPay(Integer doneCode, String contractId,
			int fee) throws Exception {
		CGeneralContractPay pay = new CGeneralContractPay();
		pay.setContract_id(Integer.valueOf(contractId));
		pay.setDone_code(doneCode);
		pay.setFee(fee);
		pay.setOptr_id(getOptr().getOptr_id());
		cGeneralContractPayDao.save(pay);
	}

	
	/**
	 * 根据编号查询
	 * @param contractId
	 * @return
	 * @throws Exception
	 */
	public CGeneralContractDetail queryGeneralContractDetailById(String contractId) throws Exception{
		return cGeneralContractDetailDao.findByKey(contractId);
	}
	
	/**
	 * 更新预收款或工程款
	 * @param generalContract
	 * @throws JDBCException
	 */
	public void updateGeneralContract(CGeneralContract generalContract) throws JDBCException{
		cGeneralContractDao.update(generalContract);
	}
	
	/**
	 * 删除合同，保存历史
	 * @param generalContract
	 * @param doneCode
	 */
	public void removeContractWithHis(CGeneralContract generalContract,Integer doneCode) throws JDBCException{
		CGeneralContractHis contractHis = new CGeneralContractHis();
		BeanUtils.copyProperties(generalContract, contractHis);
		contractHis.setDone_code(doneCode);
		
		cGeneralContractHisDao.save(contractHis);
		cGeneralContractDao.remove(generalContract.getContract_id());
		removeGeneralCredentialByContractId(generalContract.getContract_id());
	}
	
	public void removeGeneralCredentialByContractId(Integer contractId) throws JDBCException{
		cGeneralCredentialDao.deleteByContractId(contractId);
	}
	
	/**
	 * 根据合同号和凭据号查找凭据
	 * @param contractId
	 * @param credentialNo
	 * @return
	 */
	public CGeneralCredential queryCredentialById(
			String credentialNo) throws Exception{
		return cGeneralCredentialDao.queryCredentialById(credentialNo,getOptr().getCounty_id());
	}
	
	/**
	 * 查找凭据号最大的凭据
	 * @param contractId
	 * @param credentialNo
	 * @return
	 */
	public CGeneralCredential queryLastCredential(
			String contractId) throws Exception{
		return cGeneralCredentialDao.queryLastCredential(contractId);
	}
	
	

	public void removeInactiveWithHis(CAcctAcctitemInactive acctInactive,Integer doneCode ) throws Exception {
		cAcctAcctitemDao.updateActiveBanlance(acctInactive.getAcct_id(), acctInactive.getAcctitem_id()
				, 0, acctInactive.getBalance()*-1, 0, 0, getOptr().getCounty_id());

//		cAcctAcctitemInactiveDao.removeByAcctItemIdWithHis(acctInactive.getAcct_id(), acctInactive.getAcctitem_id(), doneCode);
		CAcctAcctitemInactiveHis acctInactiveHis = new CAcctAcctitemInactiveHis();
		BeanUtils.copyProperties(acctInactive, acctInactiveHis);
		acctInactiveHis.setUse_amount(0);
		acctInactiveHis.setBalance(acctInactiveHis.getInit_amount());
		acctInactiveHis.setDone_code(doneCode);
		acctInactiveHis.setCreate_done_code(acctInactive.getDone_code());

		cAcctAcctitemInactiveHisDao.save(acctInactiveHis);

		cAcctAcctitemInactiveDao.removeBySn(acctInactive.getFee_sn(),acctInactive.getPromotion_sn(),acctInactive.getAcct_id(), acctInactive.getAcctitem_id());
	}
	
	public void clearInactiveAmount(Integer doneCode, String sn,String acctId, String acctItemId,int fee) throws Exception {
		CAcctAcctitemInactive acctInactive = this.queryInactiveAcctitem(acctId, acctItemId);
		if(acctInactive == null || acctInactive.getBalance() <= 0)
			throw new ServicesException("冻结金额应大于0");
		
		cAcctAcctitemDao.updateActiveBanlance(acctInactive.getAcct_id(), acctInactive.getAcctitem_id()
				, 0, fee*-1, 0, 0, getOptr().getCounty_id());
		if(fee == acctInactive.getBalance()){
			CAcctAcctitemInactiveHis acctInactiveHis = new CAcctAcctitemInactiveHis();
			BeanUtils.copyProperties(acctInactive, acctInactiveHis);
			acctInactiveHis.setDone_code(doneCode);
			acctInactiveHis.setCreate_done_code(acctInactive.getDone_code());
			cAcctAcctitemInactiveHisDao.save(acctInactiveHis);
			
			cAcctAcctitemInactiveDao.removeBySn(sn,acctInactive.getPromotion_sn(),acctInactive.getAcct_id(), acctInactive.getAcctitem_id());
		}else if(fee < acctInactive.getBalance()){
			cAcctAcctitemInactiveDao.updateBanlance(sn, acctId, acctItemId,fee);
		}else{
			throw new ComponentException("清除的冻结金额不能大于原有冻结金额");
		}
	}

	
	/**
	 * 通过用户编号和产品编号查询预扣费记录
	 * @param userId
	 * @param prodId
	 * @return
	 * @throws JDBCException
	 */
	public List<CAcctPreFee> queryByUserIdandProdId(String userId,String prodId) throws JDBCException{
		return cAcctPreFeeDao.queryByUserIdandProdId(userId, prodId);
	}
	
	
	/**
	 * 查询账目记录
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<AcctAcctitemInvalidDto> queryAcctitemInvalidByCustId(String custId) throws Exception{
		return cAcctAcctitemInvalidDao.queryAcctitemInvalidByCustId(custId);
	}
	
	/**
	 * 更新账目作废记录
	 * @param acctId
	 * @param acctItemId
	 * @param feeType
	 * @param fee
	 * @throws Exception
	 */
	public void updateInvalidFee(String acctId,String acctItemId,String feeType,int fee) throws Exception{
		cAcctAcctitemInvalidDao.updateInvalidFee(acctId, acctItemId, feeType, fee);

	}
	
	
	/**
	 * 根据交易流水查找预扣费记录
	 * @param transId
	 * @param userId
	 * @return
	 */
	public CAcctPreFee queryByTransId(String transId, String userId) throws JDBCException{
		return cAcctPreFeeDao.queryByTransId(transId, userId); 
	}
	
	/**
	 * 根据用户编号，产品编号，影片编号查询已处理的预扣费记录
	 * @param userId
	 * @param prodId
	 * @param progId
	 * @return
	 * @throws JDBCException
	 */
	public CAcctPreFee queryByProgId(String userId,String prodId,String progId)throws JDBCException{
		return cAcctPreFeeDao.queryByProgId(userId,prodId,progId); 
	}
	
	/**
	 * 取消预扣费
	 * @param transId
	 * @param userId
	 */
	public void updateAcctPreFee(CAcctPreFee acctPreFee) throws JDBCException{
		cAcctPreFeeDao.update(acctPreFee);
	}
	/**
	 * 保存预收款
	 * @param acctPreFee
	 * @throws JDBCException
	 */
	public void saveCAcctPreFee(CAcctPreFee acctPreFee) throws JDBCException{
		cAcctPreFeeDao.save(acctPreFee);
	}
	
	/**
	 * 查询点播消费记录
	 * @param cardId
	 * @return
	 * @throws Exception
	 */
	public List<CAcctPreFee> queryVodPreFees(String cardId) throws Exception{
		return cAcctPreFeeDao.queryVodPreFees(cardId);
	}
	
	public int queryXJBalanceByCustId(String custId) throws Exception {
		return cAcctAcctitemActiveDao.queryXJBalanceByCustId(custId, getOptr().getCounty_id());
	}
	/**
	 * 查询账目下预约和被预约记录
	 * @param acctId
	 * @param acctitemId
	 * @return
	 * @throws JDBCException
	 */
	public List<CAcctAcctitemOrder> queryAllAcctitemOrder(String acctId,String acctItemId)
			throws JDBCException {
		return cAcctAcctitemOrderDao.queryAllByAcctitemId(acctId, acctItemId);
	}
	
	public List<CAcctAcctitemThresholdProp> queryAcctitemThresholdProp(
			String acctId, String acctItemId) throws Exception {
		return cAcctAcctitemThresholdPropDao.queryAcctitemThresholdProp(acctId, acctItemId);
	}
	
	public void saveThreasholdPropChange(CAcctAcctitemThreshold threshold,Integer doneCode,String columnName) throws Exception {
		CAcctAcctitemThresholdProp prop = new CAcctAcctitemThresholdProp();
		prop.setDone_code(doneCode);
		BeanUtils.copyProperties(threshold, prop);
		prop.setColumn_name(columnName);
		prop.setOld_value("");
//		prop.setNew_value(new_value);
		setBaseInfo(prop);
		cAcctAcctitemThresholdPropDao.save(prop);
	}
	
	public void updateThreshold(List<CAcctAcctitemThresholdProp> thresholdPropList,Integer doneCode)
		throws Exception {
		for (CAcctAcctitemThresholdProp change:thresholdPropList){
			CAcctAcctitemThreshold threshold = new CAcctAcctitemThreshold();
			threshold.setAcct_id(change.getAcct_id());
			threshold.setAcctitem_id(change.getAcctitem_id());
			threshold.setTask_code(change.getTask_code());
			threshold.setTemp_threshold(Integer.parseInt(change.getNew_value()));
			setBaseInfo(threshold);
			cAcctAcctitemThresholdDao.updateThreshold(threshold);
			change.setDone_code(doneCode);
			setBaseInfo(change);
		}
		cAcctAcctitemThresholdPropDao.save(thresholdPropList
				.toArray(new CAcctAcctitemThresholdProp[thresholdPropList.size()]));
	}
	
	public List<AcctAcctitemThresholdDto> queryThresholdByAcctId(String[] acctIds) throws Exception {
		return cAcctAcctitemThresholdDao.queryThresholdByAcctId(acctIds, getOptr().getCounty_id());
	}
	
	public List<AcctAcctitemThresholdDto> queryThresholdByCustId(
			QueryAcctitemThresholdDto dto, String custId, String[] acctIds)
			throws Exception {
		return cAcctAcctitemThresholdDao.queryThresholdByCustId(dto, custId,
				acctIds, getOptr().getCounty_id());
	}
	
	/**
	 * @param changeList
	 * @param doneCode
	 */
	public void saveAdjustSpecAcctPay(List<CAcctAcctitemChange> changeList,
			Integer doneCode) throws Exception {
		List<CAcctAcctitemChange> newChangeList = new ArrayList<CAcctAcctitemChange>();
		List<CAcctAcctitemTrans> transList = new ArrayList<CAcctAcctitemTrans>();
		
		//删除流水对应非公用账目异动
		cAcctAcctitemChangeDao.removeSpecChangeByDoneCode(doneCode);
		
		String custId = changeList.get(0).getCust_id();
		
		//公用账目资金明细
		CAcctAcctitemActive publicAcctitemActive = cAcctAcctitemDao.queryPublicByCustId(custId,
				changeList.get(0).getFee_type());
		
		if(null == publicAcctitemActive){
			publicAcctitemActive = new CAcctAcctitemActive();
			CAcctAcctitem ca = cAcctAcctitemDao.queryPublicByCustId(custId);
			BeanUtils.copyProperties(ca, publicAcctitemActive);
			publicAcctitemActive.setFee_type(changeList.get(0).getFee_type());
			publicAcctitemActive.setBalance(0);
		}
		
		for(CAcctAcctitemChange change : changeList){
			//缴费公用账目异动
			CAcctAcctitemChange publicPay = new CAcctAcctitemChange();
			BeanUtils.copyProperties(change, publicPay);
			publicPay.setAcct_id(publicAcctitemActive.getAcct_id());
			publicPay.setAcctitem_id(publicAcctitemActive.getAcctitem_id());
			publicPay.setPre_fee(publicAcctitemActive.getBalance());
			publicPay.setFee(publicAcctitemActive.getBalance() + publicPay.getChange_fee());
			newChangeList.add(publicPay);
			
			//公用账目转出异动
			CAcctAcctitemChange publicTrans = new CAcctAcctitemChange();
			BeanUtils.copyProperties(publicPay, publicTrans);
			publicTrans.setPre_fee(publicPay.getFee());
			publicTrans.setChange_fee(-publicTrans.getChange_fee());
			publicTrans.setFee(publicAcctitemActive.getBalance());
			publicTrans.setChange_type(SystemConstants.ACCT_CHANGE_TRANS);
			newChangeList.add(publicTrans);
			
			//非公用账目转入异动
			change.setChange_type(SystemConstants.ACCT_CHANGE_TRANS);
			newChangeList.add(change);
			
			// 保存转账
			CAcctAcctitemTrans acctTrans = new CAcctAcctitemTrans();
			acctTrans.setCust_id(custId);
			acctTrans.setFee_type(change.getFee_type());
			acctTrans.setAmount(publicTrans.getChange_fee()*-1);
			acctTrans.setDone_code(doneCode);
			acctTrans.setOut_acct_id(publicTrans.getAcct_id());
			acctTrans.setOut_acctitem_id(publicTrans.getAcctitem_id());
			acctTrans.setIn_acct_id(change.getAcct_id());
			acctTrans.setIn_acctitem_id(change.getAcctitem_id());
			setBaseInfo(acctTrans);
			transList.add(acctTrans);
		}
		//保存异动
		cAcctAcctitemChangeDao.save(newChangeList.toArray(new CAcctAcctitemChange[newChangeList.size()]));
		//保存转账
		cAcctAcctitemTransDao.save(transList.toArray(new CAcctAcctitemTrans[transList.size()]));
	}
	
	/**
	 * @param custColony
	 * @param countyId
	 * @return
	 */
	public TCountyAcct queryAcctConfig(String custColony, String countyId) throws JDBCException {
		return tCountyAcctDao.queryAcctConfig(countyId, custColony);
	}
	
	public void updateCoutyAcct(TCountyAcct countyAcct,Integer doneCode,Integer fee) throws Exception{
		countyAcct.setBalance(countyAcct.getBalance() - fee);
		updateCountyAcct(countyAcct);
		TCountyAcctChange change = new TCountyAcctChange(countyAcct.getT_acct_id(),fee,getOptr().getOptr_id(),doneCode);
		saveCountyAcctChange(change);
	}
	
	/**
	 * @param countyAcct
	 */
	public void updateCountyAcct(TCountyAcct countyAcct) throws JDBCException {
		tCountyAcctDao.update(countyAcct);
	}

	/**
	 * @param change
	 */
	public void saveCountyAcctChange(TCountyAcctChange change) throws JDBCException {
		tCountyAcctChangeDao.save(change);
	}
	
	/**
	 * @param doneCode
	 */
	public void deleteCountyAcctChange(Integer doneCode) throws JDBCException {
		tCountyAcctChangeDao.deleteCountyAcctChange(doneCode);
	}

	/**
	 * @param doneCode
	 */
	public TCountyAcctChange queryChangeByDoneCode(Integer doneCode) throws JDBCException {
		return tCountyAcctChangeDao.queryChangeByDoneCode(doneCode);
	}

	/**
	 * @param tAcctId
	 * @return
	 */
	public TCountyAcct queryAcctConfig(String tAcctId) throws JDBCException {
		return tCountyAcctDao.findByKey(tAcctId);
	}
	
	public String queryPrintItemId(String acctItemId) throws JDBCException{
		return cAcctAcctitemDao.queryPrintItemId(acctItemId);
	}
	
	public List<Object[]> queryUnRefundByOptr() throws Exception {
		return cAcctAcctitemChangeDao.queryUnRefundByOptr(getOptr().getOptr_id(), getOptr().getCounty_id());
	}
	public String queryUnRefundMaxDoneCode(String custId,Integer doneCode) throws Exception {
		return cAcctAcctitemChangeDao.queryUnRefundMaxDoneCode(custId, doneCode);
	}
	
	/**
	 * @param acctBankDao the cAcctBankDao to set
	 */
	public void setCAcctBankDao(CAcctBankDao acctBankDao) {
		cAcctBankDao = acctBankDao;
	}
	public void setCAcctDao(CAcctDao acctDao) {
		cAcctDao = acctDao;
	}

	public void setCAcctAcctitemActiveDao(
			CAcctAcctitemActiveDao acctAcctitemActiveDao) {
		cAcctAcctitemActiveDao = acctAcctitemActiveDao;
	}
	public void setCAcctAcctitemChangeDao(
			CAcctAcctitemChangeDao acctAcctitemChangeDao) {
		cAcctAcctitemChangeDao = acctAcctitemChangeDao;
	}

	public void setCAcctAcctitemInactiveDao(
			CAcctAcctitemInactiveDao acctAcctitemInactiveDao) {
		cAcctAcctitemInactiveDao = acctAcctitemInactiveDao;
	}
	public void setCAcctAcctitemOrderDao(CAcctAcctitemOrderDao acctAcctitemOrderDao) {
		cAcctAcctitemOrderDao = acctAcctitemOrderDao;
	}

	public void setCAcctAcctitemAdjustDao(
			CAcctAcctitemAdjustDao acctAcctitemAdjustDao) {
		cAcctAcctitemAdjustDao = acctAcctitemAdjustDao;
	}

	public void setCAcctAcctitemTransDao(CAcctAcctitemTransDao acctAcctitemTransDao) {
		cAcctAcctitemTransDao = acctAcctitemTransDao;
	}

	public void setCAcctAcctitemThresholdDao(
			CAcctAcctitemThresholdDao acctAcctitemThresholdDao) {
		cAcctAcctitemThresholdDao = acctAcctitemThresholdDao;
	}

	public void setCAcctAcctitemInactiveHisDao(
			CAcctAcctitemInactiveHisDao acctAcctitemInactiveHisDao) {
		cAcctAcctitemInactiveHisDao = acctAcctitemInactiveHisDao;
	}

	public void setCAcctHisDao(CAcctHisDao acctHisDao) {
		cAcctHisDao = acctHisDao;
	}

	public void setCBankAgreeDao(CBankAgreeDao bankAgreeDao) {
		cBankAgreeDao = bankAgreeDao;
	}

	public void setCBankAgreeHisDao(CBankAgreeHisDao bankAgreeHisDao) {
		cBankAgreeHisDao = bankAgreeHisDao;
	}
	public void setCBankPayDao(CBankPayDao bankPayDao) {
		cBankPayDao = bankPayDao;
	}

	public void setCGeneralAcctDao(CGeneralAcctDao generalAcctDao) {
		cGeneralAcctDao = generalAcctDao;
	}

	public void setCGeneralContractDao(CGeneralContractDao generalContractDao) {
		cGeneralContractDao = generalContractDao;
	}

	public void setCGeneralCredentialDao(CGeneralCredentialDao generalCredentialDao) {
		cGeneralCredentialDao = generalCredentialDao;
	}

	public void setCAcctAcctitemHisDao(CAcctAcctitemHisDao acctAcctitemHisDao) {
		cAcctAcctitemHisDao = acctAcctitemHisDao;
	}

	public void setCAcctPreFeeDao(CAcctPreFeeDao acctPreFeeDao) {
		cAcctPreFeeDao = acctPreFeeDao;
	}

	public void setCAcctAcctitemInvalidDao(
			CAcctAcctitemInvalidDao acctAcctitemInvalidDao) {
		cAcctAcctitemInvalidDao = acctAcctitemInvalidDao;
	}

	public void setCGeneralContractHisDao(
			CGeneralContractHisDao generalContractHisDao) {
		cGeneralContractHisDao = generalContractHisDao;
	}


	public void setCAcctAcctitemThresholdPropDao(
			CAcctAcctitemThresholdPropDao acctAcctitemThresholdPropDao) {
		cAcctAcctitemThresholdPropDao = acctAcctitemThresholdPropDao;
	}

	public void setTCountyAcctDao(TCountyAcctDao tCountyAcctDao) {
		this.tCountyAcctDao = tCountyAcctDao;
	}

	public void setTCountyAcctChangeDao(TCountyAcctChangeDao tCountyAcctChangeDao) {
		this.tCountyAcctChangeDao = tCountyAcctChangeDao;
	}

	public CGeneralContractDetailDao getcGeneralContractDetailDao() {
		return cGeneralContractDetailDao;
	}

	public void setCGeneralContractDetailDao(
			CGeneralContractDetailDao generalContractDetailDao) {
		this.cGeneralContractDetailDao = generalContractDetailDao;
	}

	public void setCGeneralContractPayDao(
			CGeneralContractPayDao generalContractPayDao) {
		this.cGeneralContractPayDao = generalContractPayDao;
	}

	public void setTAdjustDataDao(TAdjustDataDao adjustDataDao) {
		tAdjustDataDao = adjustDataDao;
	}

	public void setCBandUpgradeRecordDao(CBandUpgradeRecordDao bandUpgradeRecordDao) {
		cBandUpgradeRecordDao = bandUpgradeRecordDao;
	}

	public void setCProdHisDao(CProdHisDao prodHisDao) {
		cProdHisDao = prodHisDao;
	}

	public void setBBillDao(BBillDao billDao) {
		bBillDao = billDao;
	}

	public void setCAcctAcctitemMergeDao(CAcctAcctitemMergeDao acctAcctitemMergeDao) {
		cAcctAcctitemMergeDao = acctAcctitemMergeDao;
	}

	public void setCUserDao(CUserDao userDao) {
		cUserDao = userDao;
	}

	/**
	 * 查询客户十分欠费,0不欠费，大于0有欠费信息.
	 * @param custId
	 * @return
	 */
	public String queryWhetherCustOwnfee(String custId) throws Exception{
		return this.cAcctDao.queryWhetherCustOwnfee(custId);
	}
	
	public String queryWhetherUserOwnfee(String userId) throws Exception{
		return this.cAcctDao.queryWhetherUserOwnfee(userId);
	}

	public void updateBankReturn(CBankReturn cbr) throws JDBCException {
		cBankReturnDao.update(cbr);
	}

	public void saveBankReturnPayerror(CBankReturnPayerror crp) throws JDBCException {
		cBankReturnPayerrorDao.save(crp);
	}

	/**
	 * @param bankReturnDao the cBankReturnDao to set
	 */
	public void setCBankReturnDao(CBankReturnDao bankReturnDao) {
		cBankReturnDao = bankReturnDao;
	}

	/**
	 * @param bankReturnPayerrorDao the cBankReturnPayerrorDao to set
	 */
	public void setCBankReturnPayerrorDao(
			CBankReturnPayerrorDao bankReturnPayerrorDao) {
		cBankReturnPayerrorDao = bankReturnPayerrorDao;
	}

	/**
	 * @param dest
	 */
	public void updateBankReturnPayerror(CBankReturnPayerror dest) throws JDBCException {
		cBankReturnPayerrorDao.update(dest);		
	}

	/**
	 * @param trans_sn
	 * @return
	 */
	public CBankRefundtodisk findBankRefundtodisk(String trans_sn) throws JDBCException {
		return cBankRefundtodiskDao.findByKey(trans_sn);
	}

	/**
	 * @param bankRefundtodiskDao the cBankRefundtodiskDao to set
	 */
	public void setCBankRefundtodiskDao(CBankRefundtodiskDao bankRefundtodiskDao) {
		cBankRefundtodiskDao = bankRefundtodiskDao;
	}

	
	/**
	 * 作废赠送，加入作废表
	 * @param acctitem
	 * @param custId
	 * @param doneCode
	 * @param busiCode
	 * @param feeType
	 * @param invalidFee
	 * @throws Exception
	 * @throws BeansException
	 */
	public void createAcctitemInvalid(CAcctAcctitem acctitem,String custId, Integer doneCode, String busiCode,
			String feeType,int invalidFee) throws Exception,BeansException {	
		
		CAcctAcctitemInvalid acctitemInvalid = new CAcctAcctitemInvalid();
		BeanUtils.copyProperties(acctitem,acctitemInvalid);
		acctitemInvalid.setInvalid_type(SystemConstants.ACCT_CHANGE_INVALID);
		acctitemInvalid.setCust_id(custId);
		acctitemInvalid.setDone_code(doneCode);
		acctitemInvalid.setBusi_code(busiCode);
		acctitemInvalid.setInvalid_fee(invalidFee);
		acctitemInvalid.setFee_type(feeType);
		acctitemInvalid.setIs_active(1);
		String prodSn = null;
		String tariffId = null;
		
		List<CProdHis> prodList = cProdHisDao.queryByAcctItem(acctitem.getAcct_id(), acctitem.getAcctitem_id(), getOptr().getCounty_id());
		if (prodList != null && prodList.size()>0){
			prodSn = prodList.get(0).getProd_sn();
			tariffId = prodList.get(0).getTariff_id();
		} else {
			
			List<CProd> prodList1 = cProdDao.queryByAcctItem(acctitem.getAcct_id(), acctitem.getAcctitem_id(), getOptr().getCounty_id());
			if (prodList1 != null && prodList1.size()>0){
				prodSn = prodList1.get(0).getProd_sn();
				tariffId = prodList1.get(0).getTariff_id();
			}
		}
		acctitemInvalid.setProd_sn(prodSn);
		acctitemInvalid.setTariff_id(tariffId);
		
		this.cAcctAcctitemInvalidDao.save(acctitemInvalid);
	}

	
	
}
