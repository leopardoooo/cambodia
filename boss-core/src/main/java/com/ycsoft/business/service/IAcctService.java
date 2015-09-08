package com.ycsoft.business.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemAdjust;
import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CAcctAcctitemOrder;
import com.ycsoft.beans.core.acct.CAcctAcctitemThreshold;
import com.ycsoft.beans.core.acct.CAcctAcctitemThresholdProp;
import com.ycsoft.beans.core.acct.CAcctBank;
import com.ycsoft.beans.core.acct.CAcctPreFee;
import com.ycsoft.beans.core.acct.CGeneralAcct;
import com.ycsoft.beans.core.acct.CGeneralContract;
import com.ycsoft.beans.core.acct.CGeneralContractPay;
import com.ycsoft.beans.core.acct.CGeneralCredential;
import com.ycsoft.beans.core.bank.CBankAgree;
import com.ycsoft.beans.core.bank.CBankPay;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.prod.PPromFee;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.acct.AcctAcctitemActiveDto;
import com.ycsoft.business.dto.core.acct.AcctAcctitemInvalidDto;
import com.ycsoft.business.dto.core.acct.AcctAcctitemThresholdDto;
import com.ycsoft.business.dto.core.acct.AcctDto;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.acct.GeneralContractDto;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.acct.QueryAcctitemThresholdDto;
import com.ycsoft.business.dto.core.acct.UnitPayDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.prod.PromFeeProdDto;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 * @author liujiaqi
 *
 */
public interface IAcctService extends IBaseService{

	/**
	 * 保存预存费信息，不支付
	 * @param payList
	 * @throws Exception
	 */
	public void savePrePay(List<PayDto> payList) throws Exception;

	/**
	 * 保存预存费信息，直接支付
	 * 
	 * @throws Exception
	 */
	public void saveSinglePay(String acct_id,String[] acctitemId, Integer fee[],CBankPay bankPay) throws Exception;
	
	/**
	 * 支付平台网账目充值
	 * @param payList
	 * @param bankPay
	 * @throws Exception
	 */
	public void saveBankProdPay(List<PayDto> payList,CBankPay bankPay) throws Exception;
	
	/**
	 * 银行批扣缴费
	 * @param bankTransSn
	 * @param acctId
	 * @param acctitemId
	 * @param fee
	 * @throws Exception
	 */
	public int saveBankPk(String bankTransSn,String acctId,String custId,String acctitemId,Date beginDate,Date endDate,int fee, String userId,String prodSn) throws Exception;
	/**
	 * 零资费产品缴费
	 * @param prodSn
	 * @param fee
	 * @param months
	 * @param beginDate
	 * @param invalidDate
	 * @throws Exception
	 */
	public void savePay(String prodSn,int fee,int months,Date beginDate,Date invalidDate) throws Exception;
	
	/**
	 * 数据电视缴纳模拟收视费
	 * 适用于数字电视用户停机后，缴纳模拟收视费
	 * @param userId
	 * @param prodId
	 * @param tariffId 
	 * @param fee
	 * @param beginDate
	 * @param invalidDate
	 * @throws Exception
	 */
	public void savePay(String prodId,String tariffId,int fee,Date beginDate,Date invalidDate) throws Exception;

	/**
	 * 退款
	 * @param acctId		账户
	 * @param acctItemId	账目
	 * @param prodSn 
	 * @param userId 
	 * @param fee			转账金额
	 * @param invalidDate   产品到期日
	 * @throws Exception
	 */
	public void saveRefund(String acctId,String acctItemId,String prodSn,String userId,int fee,String invalidDate,String promFeeSn) throws Exception;

	/**
	 * 转账
	 * @param sourceAcctId		转出账户
	 * @param sourceAcctItemId  转出目
	 * @param orderAcctId		转入账户
	 * @param orderAcctItemId	转入账目
	 * @param fee				转账金额
	 */
	public void saveTrans(String sourceAcctId,String sourceAcctItemId,String orderAcctId,String orderAcctItemId,int fee) throws Exception;

	/**
	 * 调账
	 * @param acctId		账户
	 * @param acctItemId	账目
	 * @param fee			调账金额
	 * @throws Exception
	 */
	public void saveAdjust(String acctId,String acctItemId,String prodSn,int fee,String feeType,String reason) throws Exception;
	
	/**
	 * 欠费抹零
	 * @param acctId
	 * @param acctItemId
	 * @param prodSn
	 * @param fee
	 * @param feeType
	 * @param remark
	 * @throws Exception
	 */
	public void saveAcctFree(String acctId,String acctItemId,String prodSn,int fee,String feeType) throws Exception;
	
	/**
	 * 资金解冻
	 * @param unfreezeJob
	 * @return 
	 * @throws Exception
	 */
	public int saveAcctUnfreeze(CAcctAcctitemInactive unfreezeJob)throws Exception;
	
	/**
	 * 查询客户的所有账户信息
	 * @param custId
	 * @return
	 */
	public List<AcctDto> queryAcctByCustId(String custId)throws Exception;
	/**
	 * 查询客户公用账目
	 * @return
	 * @throws Exception
	 */
	public List<AcctitemDto>  queryPublicAcctItemByCustId(String custId) throws Exception;

	/**
	 * 查询账目下余额明细,不包括0余额
	 * @param acctitemId
	 * @return
	 */
	public List<AcctAcctitemActiveDto> queryActive(
			String acctId,String acctitemId) throws Exception;

	/**
	 * 查询账目下异动明细
	 * @param acctitemId
	 * @return
	 */
	public Pager<CAcctAcctitemChange> queryAcctitemChange(
			String acctId,String acctitemId, Integer start, Integer limit)throws Exception;

	/**
	 * 查询账目下阈值明细
	 * @param acctitemId
	 * @return
	 */
	public List<CAcctAcctitemThreshold> queryAcctitemThreshold(
			String acctId,String acctitemId)throws Exception;

	/**
	 * 修改临时阀值
	 * @param thresholdList
	 * @throws JDBCException
	 */
	public void updateThreshold(List<CAcctAcctitemThresholdProp> thresholdList) throws Exception;
	
	/**
	 * 查询账目阀值信息
	 * @param acctIds
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<AcctAcctitemThresholdDto> queryThresholdByAcctId(String custId, String[] acctIds) throws Exception;
	
	/**
	 * 查询账目阀值信息
	 * @param custId
	 * @param acctIds
	 * @return
	 * @throws JDBCException
	 */
	public List<AcctAcctitemThresholdDto> queryThresholdByCustId(
			QueryAcctitemThresholdDto dto, String custId, String[] acctIds)
			throws Exception;

	/**
	 * 查询账目下返还明细
	 * @param acctitemId
	 * @return
	 */
	public List<CAcctAcctitemInactive> queryAcctitemInactive(
			String acctId,String acctitemId)throws Exception;

	/**
	 * 查询账目调账信息
	 * @param acctId
	 * @param acctItemId
	 * @return
	 * @throws JDBCException 
	 */
	public List<CAcctAcctitemAdjust> queryAcctitemAdjust(String acctId, String acctItemId) throws JDBCException;
	
	/**
	 * 查询账目下冻结明细
	 * @param acctitemId
	 * @return
	 */
	public List<CAcctAcctitemOrder> queryAcctitemOrder(
			String acctId,String acctitemId)throws Exception;

	/**
	 * 保存银行签约信息
	 * @param cBankAgree 签约信息
	 * @throws Exception
	 */
	public void saveSignBank(CBankAgree cBankAgree) throws Exception;


	/**
	 * 删除银行签约信息
	 * @param custId 客户Id
	 * @throws Exception
	 */
	public void saveRemoveSignBank(String bankPayType,Date time) throws Exception;

	/**
	 * 查询银行签约信息
	 * @return
	 * @throws Exception
	 */
	public CAcctBank querySignBank(String bankPayType) throws Exception;
	
	public CBankPay queryBankPay(String banklogid) throws Exception;
	
	public void saveBankPay(CBankPay cBankPay) throws JDBCException;

	/**
	 * 查询产品的账户信息
	 * @param custIds
	 * @return
	 * @throws Exception
	 */
	public List<UnitPayDto> queryBaseProdAcctItems(String[] custIds,String prodId) throws Exception;

	/**
	 * 单位缴费，查询可以缴费的产品
	 * @param custIds
	 * @return
	 * @throws Exception 
	 */
	public List<CProd> querySelectableProds(String[] custIds) throws Exception;
	
	/**
	 * 批量修改到期日
	 * @param payList
	 * @param newExpDate
	 * @throws Exception
	 */
	public void saveBatchEditExpDate(List<PayDto> payList,String newExpDate) throws Exception;
	
	public CAcctAcctitem queryAcctItemByAcctitemId(String acctId,String acctItemId) throws JDBCException;
	
	
	/**
	 * 查询分公司账户
	 * @return
	 * @throws Exception
	 */
	public List<CGeneralAcct> queryCompanyAcct() throws Exception ;

	/**
	 * 修改调账原因
	 * @param doneCode
	 * @param remark
	 * @throws Exception
	 */
	public void editAdjustReason(Integer doneCode, String reason) throws Exception;
	
	/**
	 * 编辑分公司账户
	 * @param generalAcctList
	 * @throws JDBCException 
	 * @throws Exception 
	 */
	public void editCompanyAcct(List<CGeneralAcct> generalAcctList) throws JDBCException, Exception;

	/**
	 * 查询没有分公司账户的分公司
	 * @return
	 * @throws Exception
	 */
	public List<SDept> queryCompanyWithOutAcct() throws Exception;

	/**
	 * 保存分公司账户
	 * @param generalAcct
	 * @throws Exception 
	 */
	public void saveGeneralAcct(CGeneralAcct generalAcct) throws Exception;

	/**
	 * 保存预收款或工程款
	 * @param generalContract
	 * @param credentialAmount 
	 * @param credentialEndNo 
	 * @param credentialStartNo 
	 * @param presentAmount 
	 * @throws Exception 
	 */
	public void saveGeneralContract(CGeneralContract generalContract, CFeePayDto pay,SOptr optr, Integer credentialStartNo, Integer credentialEndNo, Integer credentialAmount, Integer presentAmount) throws Exception;

	/**
	 * 查询预收款或工程款
	 * @param query 
	 * @param limit 
	 * @param start 
	 * @return
	 * @throws Exception
	 */
	public Pager<GeneralContractDto> queryGeneralContracts(Integer start, Integer limit, String query) throws Exception;

	/**
	 * 修改预收款或工程款合同金额
	 * @param contractId
	 * @param acctId
	 * @param newNominalAmount
	 * @throws Exception
	 */
	public void editGeneralContract(CGeneralContract generalContract) throws Exception;
	
	/**
	 * 作废合同
	 * @param contractId
	 * @throws Exception
	 */
	public void saveRemoveContract(String contractId) throws Exception;

	/**
	 * 添加合同凭据
	 * @param contractId
	 * @param credentialStartNo
	 * @param credentialEndNo
	 * @param credentialAmount
	 * @throws Exception 
	 */
	public void addCredential(String contractId, Integer credentialStartNo,
			Integer credentialEndNo, Integer credentialAmount) throws Exception;

	/**
	 * 查询合同凭据信息
	 * @param contractId
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException 
	 */
	public Pager<CGeneralCredential> queryCredential(String contractId, Integer start,
			Integer limit) throws JDBCException;
	
	/**
	 * 查询合同付款明细
	 * @param contractId
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException 
	 */
	public Pager<CGeneralContractPay> queryPayInfo(String contractId, Integer start,
			Integer limit) throws JDBCException;
	
	/**
	 * VOD预扣费处理
	 * 结果返回价格和需要充值的金额，分号隔开
	 * @param transId VOD交易流水号
	 * @param userId  用户编号
	 * @param prodId  产品编号
	 * @param progId  节目编号
	 * @param progName  节目名称
	 * @param requestTime 请求扣费时间
	 * @param price    原始价格
	 * @param detailParams  详细参数
	 * @return  大于等于0表示本次扣费金额  小于0代表本次需要需要充值的金额
	 * @throws Exception 
	 */
	public String vodPreFee(String transId,String userId,String prodId,String progId,String progName,Date requestTime,int price,String detailParams) throws Exception;
	
	/**
	 * 取消预扣费
	 * @param transId
	 * @param userId
	 * @throws Exception
	 */
	public void cancelVodPreFee(String transId,String userId) throws Exception;

	/**
	 * 查询非营业费
	 * @return
	 * @throws Exception
	 */
	public List<TBusiFee> queryUnBusiFee() throws Exception;
	
	/**
	 * 作废账单
	 * 1、修改账单的状态为作废
	 * 2、根据该账单的销账记录，修改销账记录对应的账目余额或者增加作废记录
	 * 2、删除该账单对应销账记录
	 * @param billSns
	 * @throws Exception
	 */
	public void saveCancelBill (String[] billSns) throws Exception;
	
	/**
	 * 对用户账目余额的作废记录进行退款
	 * @param acctId
	 * @param acctItemId
	 * @param feeType
	 * @param fee
	 * @throws Exception
	 */
	public void saveRefundInvlid(String acctId, String acctItemId, String feeType,int fee) throws Exception;
	
	/**
	 * 查询点播消费记录
	 * @param cardId
	 * @return
	 * @throws Exception
	 */
	public List<CAcctPreFee> queryVodPreFees(String cardId) throws Exception;

	
	/**查询账目作废记录
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<AcctAcctitemInvalidDto> queryAcctitemInvalidByCustId(String custId)throws Exception;
	/**
	 * 查询账目预约与被预约记录
	 * @param acctId
	 * @param acctItemId
	 * @return
	 */
	public List<CAcctAcctitemOrder> queryAllByAcctitemId(String acctId,String acctItemId) throws Exception;

	/**
	 * 查询账目阈值异动
	 * @param acctId
	 * @param acctItemId
	 * @return
	 * @throws Exception
	 */
	public List<CAcctAcctitemThresholdProp> queryAcctitemThresholdProp(String acctId,String acctItemId) throws Exception;
	/**
	 * 非营业费退款
	 * @param generalContract
	 */
	public void saveRefundUnBusiFee(CGeneralContract generalContract) throws Exception;

	/**
	 * @param acctId
	 * @param acctItemId
	 * @param feeType
	 * @param fee
	 */
	public void dezsRefund(String acctId, String acctItemId, String feeType,
			int fee) throws Exception;

	/**
	 * @param contractId
	 * @param fee
	 */
	public void savePayUnBusiFee(String contractId, int fee) throws Exception;

	/**
	 * 取消套餐缴费.
	 * @param promFeeSn
	 * @throws Exception
	 */
	public void cancelPromFee(String promFeeSn, String reason) throws Exception;
	
	/**
	 * @param promFeeId
	 * @param promFee
	 * @param prodList
	 */
	public void savePromPayFee(String promFeeId, int promFee,
			List<PromFeeProdDto> prodList, String preOpenTime) throws Exception;
	
	/**
	 * 呼叫中心 公用账户、专用账户信息查询
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<AcctitemDto> queryAcctitemToCallCenter(Map<String,Object> params) throws Exception;

	/**
	 * 查询套餐缴费相关的 账目的冻结余额使用情况.
	 * @param doneCode
	 */
	public List<CAcctAcctitemInactive> queryPromAcctItemInactive(Integer doneCode, String custId, boolean fromHistory) throws Exception;
	
	public void clearInactiveAmount(String promFeeSn,String acctId, String acctItemId,int fee,int preFee ) throws Exception;
	/**
	 * 查找上月和当月的有效套餐缴费
	 * @param userId
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public List<PPromFee> queryIsPromFee(String userId,String prodId)throws Exception;

	/**
	 * 根据到期日计算批量缴费应缴费用.
	 * @param payFeesData
	 * @param invalidDate
	 * @return
	 */
	public List<PayDto> calcBatchPayFees(List<PayDto> payList, Date invalidDate,String custId) throws Exception;

	/**
	 * 作废赠送.
	 * @param acctId
	 * @param acctItemId
	 * @param prodSn
	 * @param fee
	 */
	public void cancelFree(String acctId, String acctItemId, String prodSn, int fee)throws Exception;
	
}
