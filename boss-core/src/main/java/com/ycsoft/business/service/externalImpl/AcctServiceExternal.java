/**
 *
 */
package com.ycsoft.business.service.externalImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CAcctBank;
import com.ycsoft.beans.core.acct.CAcctPreFee;
import com.ycsoft.beans.core.bank.CBankAgree;
import com.ycsoft.beans.core.bank.CBankPay;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.acct.AcctDto;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.acct.BankReturnDto;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.service.impl.AcctService;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.daos.core.JDBCException;

/**
 * @author liujiaqi
 */
@Service
public class AcctServiceExternal extends ParentService implements
		IAcctServiceExternal {
	public void cancelVodPreFee(BusiParameter p, String transId, String userId)
			throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		acctService.cancelVodPreFee(transId, userId);
	}
	
	public void cancelVodPreFee(BusiParameter p,CAcctPreFee preFee) throws Exception{
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		acctService.cancelVodPreFee(preFee);
	}

	public int saveAcctUnfreeze(BusiParameter p,
			CAcctAcctitemInactive unfreezeJob) throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		return acctService.saveAcctUnfreeze(unfreezeJob);
	}

	public void saveDelAcctItem(BusiParameter p, String acctId,
			String acctItemId,Integer doneCode) throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		acctService.saveDelAcctItem(acctId, acctItemId,doneCode);
	}

	public void test(BusiParameter p, String id) {
		System.out.println(Thread.currentThread() + "+" + id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#queryAcctItemByAcctitemId(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String)
	 */
	public CAcctAcctitem queryAcctItemByAcctitemId(BusiParameter p,
			String acctId, String acctItemId) throws JDBCException {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		return acctService.queryAcctItemByAcctitemId(acctId, acctItemId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#queryVodPreFees(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String)
	 */
	public List<CAcctPreFee> queryVodPreFees(BusiParameter p, String cardId)
			throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		return acctService.queryVodPreFees(cardId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#savePrePay(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.util.List)
	 */
	public Integer savePrePay(BusiParameter p, List<PayDto> payList)
			throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		acctService.savePrePay(payList);
		return p.getDoneCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#saveSinglePay(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String[], java.lang.Integer[],
	 *      com.ycsoft.beans.core.bank.CBankPay)
	 */
	public Integer saveSinglePay(BusiParameter p, String acctId,
			String[] acctitemId, Integer[] fee, CBankPay bankPay)
			throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		acctService.saveSinglePay(acctId, acctitemId, fee, bankPay);
		return p.getDoneCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#saveTrans(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, int)
	 */
	public void saveTrans(BusiParameter p, String sourceAcctId,
			String sourceAcctItemId, String orderAcctId,
			String orderAcctItemId, int fee) throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		acctService.saveTrans(sourceAcctId, sourceAcctItemId, orderAcctId,
				orderAcctItemId, fee);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#vodPreFee(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, java.util.Date,
	 *      java.lang.Integer, java.lang.String)
	 */
	public String vodPreFee(BusiParameter p, String transId, String userId,
			String prodId, String progId, String progName, Date requestTime,
			Integer price, String detailParams) throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		return acctService.vodPreFee(transId, userId, prodId, progId, progName,
				requestTime, price, detailParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#queryAcctByCustId(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String)
	 */
	public List<AcctDto> queryAcctByCustId(BusiParameter p, String custId)
			throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		return acctService.queryAcctByCustId(custId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#queryBankPay(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String)
	 */
	public CBankPay queryBankPay(BusiParameter p, String banklogid)
			throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		return acctService.queryBankPay(banklogid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#querySignBank(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String)
	 */
	public CAcctBank querySignBank(BusiParameter p, String bankPayType)
			throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		return acctService.querySignBank(bankPayType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#saveRemoveSignBank(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String)
	 */
	public void saveRemoveSignBank(BusiParameter p, String bankPayType,Date time) throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		acctService.saveRemoveSignBank(bankPayType,time);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#saveSignBank(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.Object, com.ycsoft.beans.core.bank.CBankAgree)
	 */
	public void saveSignBank(BusiParameter p, CBankAgree bankAgree)
			throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		acctService.saveSignBank(bankAgree);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#saveBankPay(com.ycsoft.business.commons.pojo.BusiParameter, com.ycsoft.beans.core.bank.CBankPay)
	 */
	public void saveBankPay(BusiParameter p, CBankPay bankPay) throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		acctService.saveBankPay(bankPay);		
	}
	
	/**
	 * 支付平台网账目充值
	 * @param payList
	 * @param bankPay
	 * @throws Exception
	 */
	public void saveBankProdPay(BusiParameter p,List<PayDto> payList,CBankPay bankPay) throws Exception{
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		acctService.saveBankProdPay(payList, bankPay);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#saveAdjustSpecAcctPay(com.ycsoft.business.commons.pojo.BusiParameter, java.util.List, java.lang.Integer)
	 */
	public void saveAdjustSpecAcctPay(BusiParameter p,
			List<CAcctAcctitemChange> changeList, Integer doneCode) throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		acctService.saveAdjustSpecAcctPay(changeList, doneCode);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#queryAcctitemToCallCenter(com.ycsoft.business.commons.pojo.BusiParameter, java.util.Map)
	 */
	public List<AcctitemDto> queryAcctitemToCallCenter(BusiParameter p,
			Map<String, Object> params) throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		return acctService.queryAcctitemToCallCenter(params);
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IAcctServiceExternal#queryAcctitemToCallCenter(com.ycsoft.business.commons.pojo.BusiParameter, java.util.Map)
	 */
	public int saveBankPk(BusiParameter p, String bankTransSn,String acctId,String custId,String acctitemId,Date beginDate,Date endDate,int fee, String userId,String prodSn) throws Exception{
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		return acctService.saveBankPk(bankTransSn, acctId,custId, acctitemId, beginDate, endDate, fee,userId,prodSn);
	}

	public void runBankReturn(BusiParameter p,BankReturnDto r) throws Exception {
		AcctService acctService = (AcctService) getBean(AcctService.class, p);
		
	
		String errorinfo=null;
		if("1301".equals(r.getBusi_type())&& "0000".equals(r.getIs_success())){
			//扣费成功处理缴费
			try{
				LoggerHelper.info(this.getClass(), "银行缴费交易流水【"
						+ r.getBank_trans_sn() + "|客户号"
						+ r.getCust_id() + "】金额: " + r.getFee()+"|"+r.getStart_date()+r.getEnd_date());
				//有事物控制
				acctService.saveBankPk(r.getBank_trans_sn(), r
						.getAcct_id(),r.getCust_id(), r.getAcctitem_id(), r
						.getStart_date(), r.getEnd_date(), r.getFee(),
						r.getUser_id(), r.getProd_sn());
			}catch(Throwable e){
				LoggerHelper.error(this.getClass(), "银行缴费交易流水:"+ r.getBank_trans_sn() , e);
				errorinfo=e.getMessage();
				if(errorinfo==null){
					errorinfo="失败";
				}
			}
		}
		//有事物控制
		//如果上面代码执行成功，下面这段代码执行失败发生底层错误，怎么办？
		//解决办法：把1301充值执行成功的修改回盘记录标记的功能分离到acctService.saveBankPk中执行，这样这段代码发生底层错误页没关系
		acctService.runBankReturn(r,errorinfo);
	}
}
