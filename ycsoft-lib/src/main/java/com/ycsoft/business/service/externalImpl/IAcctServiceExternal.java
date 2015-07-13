/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.ycsoft.daos.core.JDBCException;

/**
 * @author liujiaqi
 * 
 */
public interface IAcctServiceExternal {
	public void cancelVodPreFee(BusiParameter p, String transId, String userId)
			throws Exception;
	
	public void cancelVodPreFee(BusiParameter p,CAcctPreFee preFee) throws Exception;

	public int saveAcctUnfreeze(BusiParameter p,
			CAcctAcctitemInactive unfreezeJob) throws Exception;

	public void saveDelAcctItem(BusiParameter p, String acctId,
			String acctItemId,Integer doneCode) throws Exception;

	public void test(BusiParameter p, String id) throws Exception;

	public Integer saveSinglePay(BusiParameter p, String acctId,
			String[] acctItemIds, Integer[] fees, CBankPay bankPay)
			throws Exception;

	public Integer savePrePay(BusiParameter p, List<PayDto> payList)
			throws Exception;

	public CAcctAcctitem queryAcctItemByAcctitemId(BusiParameter p,
			String acctId, String acctItemId) throws Exception;

	public void saveTrans(BusiParameter p, String sourceAcctId,
			String sourceAcctItemId, String orderAcctId,
			String orderAcctItemId, int fee) throws Exception;

	public String vodPreFee(BusiParameter p, String transId, String userId,
			String prodId, String progId, String progName, Date requestTime,
			Integer price, String detailParams) throws Exception;

	public List<CAcctPreFee> queryVodPreFees(BusiParameter p, String deviceId)
			throws Exception;

	public List<AcctDto> queryAcctByCustId(BusiParameter p, String custId)
			throws Exception;

	public CBankPay queryBankPay(BusiParameter p, String banklogid)
			throws Exception;

	public CAcctBank querySignBank(BusiParameter p,
			String bankPayType) throws Exception;

	public void saveSignBank(BusiParameter p, CBankAgree bankAgree) throws Exception;

	public void saveRemoveSignBank(BusiParameter p, String bankPayType,Date time) throws Exception;

	public void saveBankPay(BusiParameter p, CBankPay bankPay)throws Exception;
	
	/**
	 * 支付平台网账目充值
	 * @param payList
	 * @param bankPay
	 * @throws Exception
	 */
	public void saveBankProdPay(BusiParameter p,List<PayDto> payList,CBankPay bankPay) throws Exception;

	/**
	 * 补入非公用账目充值的账目异动数据
	 * @param p
	 * @param changeList
	 * @param doneCode
	 */
	public void saveAdjustSpecAcctPay(BusiParameter p,
			List<CAcctAcctitemChange> changeList, Integer doneCode) throws Exception;
	
	public List<AcctitemDto> queryAcctitemToCallCenter(BusiParameter p, Map<String,Object> params) throws Exception;

	
	public int saveBankPk(BusiParameter p, String bankTransSn, String acctId,String custId,
			String acctitemId, Date beginDate,Date endDate, int fee, String userId,String prodSn) throws Exception;

	/**
	 * 触发银行回盘处理程序
	 */
	public void runBankReturn(BusiParameter p,BankReturnDto r) throws Exception;
}
