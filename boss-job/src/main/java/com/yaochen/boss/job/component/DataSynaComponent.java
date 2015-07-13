package com.yaochen.boss.job.component;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.yaochen.boss.dao.DataSynaDao;
import com.ycsoft.beans.config.TAcctFeeType;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemActive;
import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.job.JCustWriteoff;
import com.ycsoft.business.dao.config.TAcctFeeTypeDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemActiveDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemChangeDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemDao;
import com.ycsoft.business.dao.core.bill.BBillDao;
import com.ycsoft.business.dao.core.job.JCustWriteoffDao;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.daos.core.JDBCException;

@Component
public class DataSynaComponent {
	private DataSynaDao dataSynaDao;
	private BBillDao bBillDao;
	private CAcctAcctitemActiveDao cAcctAcctitemActiveDao;
	private TAcctFeeTypeDao tAcctFeeTypeDao;
	private CAcctAcctitemDao cAcctAcctitemDao;
	private CAcctAcctitemChangeDao cAcctAcctitemChangeDao;
	private JCustWriteoffDao jCustWriteoffDao;
	

	/**
	 * 同步账单数据（南京双向数据库CZLY）
	 * @throws Exception 
	 */
	public void dataSynaCzly() throws Exception {
		System.out.println("------------------");
		String areaId="5000";
		String countyId="5001";
		dataSynaDao.synaTable();
		compData(areaId, countyId);
	}


	private void compData(String areaId, String countyId)
			throws JDBCException, Exception {
		Integer doneCode = Integer.valueOf(dataSynaDao.findSequence("seq_done_code").toString());
		List<Map<String, Object>> allacctitem = dataSynaDao
				.queryAllCzlyAcctItem();
		System.out.println("find bill count is "+allacctitem.size());
		for (Map<String, Object> acctitem : allacctitem) {
			String custId = acctitem.get("cust_id").toString();
			String boss_acct_id = acctitem.get("boss_acct_id").toString();
			String boss_acctitem_id = acctitem.get("boss_acctitem_id").toString();
			String billingCycleId = acctitem.get("billing_cycle_id").toString();
			String czlyBillSn = acctitem.get("src_bill_id").toString();
			String bossBillSn = acctitem.get("boss_bill_sn") == null ? ""
					: acctitem.get("boss_bill_sn").toString();
			Integer original_amount = Integer.valueOf(acctitem.get("original_amount").toString());// 出账金额
			Date billDate ;
			try {
				billDate = DateHelper.parseDate(acctitem
						.get("state_date").toString(), "yyyy-MM-dd HH:mm:ss");
			} catch (Exception e) {
				billDate = new Date();
			}
			// 查找对应账单
			
			Integer oweFee = 0;
			if (bossBillSn.isEmpty()) {
				//添加新账单
				String billSn = dataSynaDao.findSequence("seq_bill_sn").toString();
				System.out.println("New billsn="+billSn);
				String tariff_id = dataSynaDao.queryTariffId(boss_acct_id,boss_acctitem_id);
				BBill bill = createBill(billingCycleId, areaId, countyId, doneCode,
						billDate, custId, boss_acct_id, boss_acctitem_id,
						original_amount, billSn, tariff_id);
				bBillDao.save(bill);
				dataSynaDao.saveCzlyPkBillSn(billSn, czlyBillSn);
				oweFee = original_amount;
			}else{
				BBill bill = dataSynaDao.queryBill(bossBillSn);
				if (original_amount>bill.getFinal_bill_fee()){
					//更新账单金额，只更新比原来大的
					oweFee = original_amount-bill.getFinal_bill_fee();
					bBillDao.updateBillFee(bill.getBill_sn(),oweFee);
				}
			}
			if (oweFee>0) {
				//更新欠费
				dataSynaDao.updateOwnFee(boss_acct_id, boss_acctitem_id, oweFee);
				
				// 插入销账任务
				JCustWriteoff writeOffJob = new JCustWriteoff();
				writeOffJob.setJob_id(Integer.parseInt(jCustWriteoffDao.findSequence(SequenceConstants.SEQ_JOB_ID).toString()));
				writeOffJob.setDone_code(doneCode);
				writeOffJob.setCust_id(custId);
				writeOffJob.setArea_id(areaId);
				writeOffJob.setCounty_id(countyId);
				writeOffJob.setWriteoff(SystemConstants.BOOLEAN_TRUE);
				jCustWriteoffDao.save(writeOffJob);
			}

		}
	}


	private BBill createBill(String billingCycleId, String areaId,
			String countyId, Integer doneCode, Date billDate,
			String custId, String boss_acct_id, String boss_acctitem_id,
			Integer original_amount, String billSn, String tariff_id) {
		BBill bill = new BBill();
		bill.setProd_id(boss_acctitem_id);
		bill.setAcctitem_id(boss_acctitem_id);
		bill.setTariff_id(tariff_id);
		bill.setCust_id(custId);
		bill.setAcct_id(boss_acct_id);
		bill.setBill_sn(billSn);
		bill.setBilling_cycle_id(billingCycleId);
		bill.setStatus("1");
		bill.setCome_from("7");
		bill.setBill_done_code(""+doneCode);
		bill.setFee_flag("ZC");
		bill.setBill_type("2");
		bill.setBill_date(billDate);
		bill.setFinal_bill_fee(original_amount);
		bill.setOwe_fee(original_amount);
		bill.setArea_id(areaId);
		bill.setCounty_id(countyId);
		return bill;
	}

	
	/**
	 * 计算最大允许扣款数
	 * 查找余额，判断是否大于实际欠费值，如余额不足，则返回余额
	 * @param boss_acct_id
	 * @param boss_acctitem_id
	 * @param amount 实际欠费值
	 * @return
	 * @throws JDBCException
	 */
	private Integer allowCutAmount(String boss_acct_id,
			String boss_acctitem_id, Integer amount) throws JDBCException {
		CAcctAcctitem acctAcctitem = cAcctAcctitemDao.queryAcctItem(boss_acct_id, boss_acctitem_id);
		if (acctAcctitem == null)
			amount = 0;
		else if (amount>acctAcctitem.getActive_balance())
			amount = acctAcctitem.getActive_balance();
		return amount;
	}

	/**
	 * 保存扣费，扣除余额，记录异动
	 * @param boss_acct_id
	 * @param boss_acctitem_id
	 * @throws Exception 
	 */
	private void saveCutAmount(Integer doneCode, String busiCode,String custId,String acctId, String acctItemId,Integer cutAmount ) throws Exception {
		
		Map<String,TAcctFeeType> feeTypeMap =CollectionHelper.converToMapSingle(tAcctFeeTypeDao.findAll(),"fee_type");
		List<CAcctAcctitemActive> acctActives = dataSynaDao.queryAcctActive(acctId,acctItemId);
		int transFee = 0;
		int refundFee = 0;
		String countyId="";
		Integer allamount = cutAmount;
		for (CAcctAcctitemActive acctActive:acctActives){
			TAcctFeeType feeType = feeTypeMap.get(acctActive.getFee_type());
			Integer fee = 0;
			countyId = acctActive.getCounty_id();
			if (acctActive.getBalance() >= allamount){
				fee = allamount;
				allamount = 0;
			}else{
				fee = acctActive.getBalance();
				allamount -= acctActive.getBalance();
			}
			transFee += feeType.getCan_trans().equals(SystemConstants.BOOLEAN_TRUE)?fee:0;
			refundFee += feeType.getCan_refund().equals(SystemConstants.BOOLEAN_TRUE)?fee:0;
			saveAcctitemChange(doneCode, busiCode, custId, acctId, acctItemId, SystemConstants.ACCT_CHANGE_WRITEOFF,
					feeType.getFee_type(), -fee, acctActive.getBalance(),acctActive.getArea_id(),countyId);
			cAcctAcctitemActiveDao.updateBanlance(acctId, acctItemId, acctActive.getFee_type(),-fee,countyId );
			
			if (allamount ==0)
				break;
		}
		cAcctAcctitemDao.updateActiveBanlance(acctId, acctItemId,allamount-cutAmount,0,-refundFee,-transFee, countyId);
		
		
	}

	/**
	 * @param dataSynaDao
	 *            the dataSynaDao to set
	 */
	public void setDataSynaDao(DataSynaDao dataSynaDao) {
		this.dataSynaDao = dataSynaDao;
	}
	private void saveAcctitemChange(Integer doneCode, String busiCode,
			String custId, String acctId, String acctItemId, String changeType,
			String feeType, int fee, int preFee, String areaId,String countyId) throws Exception {
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
		change.setArea_id(areaId);
		change.setCounty_id(countyId);

		cAcctAcctitemChangeDao.save(change);
	}

	/**
	 * @param billDao the bBillDao to set
	 */
	public void setBBillDao(BBillDao billDao) {
		bBillDao = billDao;
	}

	/**
	 * @param acctAcctitemActiveDao the cAcctAcctitemActiveDao to set
	 */
	public void setCAcctAcctitemActiveDao(
			CAcctAcctitemActiveDao acctAcctitemActiveDao) {
		cAcctAcctitemActiveDao = acctAcctitemActiveDao;
	}

	/**
	 * @param acctFeeTypeDao the tAcctFeeTypeDao to set
	 */
	public void setTAcctFeeTypeDao(TAcctFeeTypeDao acctFeeTypeDao) {
		tAcctFeeTypeDao = acctFeeTypeDao;
	}

	/**
	 * @param acctAcctitemDao the cAcctAcctitemDao to set
	 */
	public void setCAcctAcctitemDao(CAcctAcctitemDao acctAcctitemDao) {
		cAcctAcctitemDao = acctAcctitemDao;
	}

	/**
	 * @param acctAcctitemChangeDao the cAcctAcctitemChangeDao to set
	 */
	public void setCAcctAcctitemChangeDao(
			CAcctAcctitemChangeDao acctAcctitemChangeDao) {
		cAcctAcctitemChangeDao = acctAcctitemChangeDao;
	}


	/**
	 * @param custWriteoffDao the jCustWriteoffDao to set
	 */
	public void setJCustWriteoffDao(JCustWriteoffDao custWriteoffDao) {
		jCustWriteoffDao = custWriteoffDao;
	}
}
