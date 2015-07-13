/**
 *
 */
package com.ycsoft.business.component.core;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.bill.BBillWriteoff;
import com.ycsoft.beans.core.bill.BillDto;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.core.bill.BBillDao;
import com.ycsoft.business.dao.core.bill.BBillWriteoffDao;
import com.ycsoft.business.dao.core.bill.BRentfeeDao;
import com.ycsoft.business.dto.core.fee.QueryFeeInfo;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 *
 * @author pyb
 *
 * Nov 3, 2010
 *
 */
@Component
public class BillComponent extends BaseBusiComponent{
	
	private BBillWriteoffDao bBillWriteoffDao;
	private BRentfeeDao bRentfeeDao;

	public List<BBill> queryPromFeeBillByCreateDonecode(String custId,int doneCode,String billingCycle,String comeFrom)throws Exception{
		return bBillDao.queryPromFeeBill(custId, doneCode, billingCycle,comeFrom);
	}
	//包多月产品按账目查账单
	public List<BBill> queryMuchMonthProdBill(String prodSn,int doneCode,String billingCycle,String comeFrom) throws Exception{
		return bBillDao.queryMuchProdBill(prodSn, doneCode, billingCycle, comeFrom, this.getOptr().getCounty_id());
	}
	
	
	public Integer queryPromFeeBillOwefeeSum(String custId,int doneCode,String prodSn)throws Exception{
		return bBillDao.queryPromFeeOweFeeSumByProdSn(custId, doneCode,prodSn);
	}
	/**
	 * 套餐缴费额外账单插入
	 * @param prod
	 * @param doneCode
	 * @param billingCycle
	 * @param billFee
	 * @param oweFee
	 * @param comeFrom
	 * @return
	 * @throws Exception
	 */
	public void createPromFeeBill(CProd prod,int doneCode,String billingCycle,int billFee,int oweFee) throws Exception{
		String billSn = bBillDao.findSequence().toString();
		BBill bill = new BBill();
		BeanUtils.copyProperties(prod, bill);
		bill.setAcctitem_id(prod.getProd_id());
		bill.setBill_sn(billSn);
		bill.setBilling_cycle_id(billingCycle);
		bill.setStatus("1");
		bill.setCome_from("5");//套餐缴费的额外账单设置成5
		bill.setBill_done_code(""+doneCode);
		bill.setFee_flag("ZC");
		bill.setBill_type("1");
		bill.setBill_date(new Date());
		bill.setEff_date(new Date());
		bill.setRent_month_fee(billFee);
		bill.setUse_fee(0);
		bill.setFinal_month_fee(billFee);
		bill.setFinal_use_fee(0);
		bill.setFinal_bill_fee(billFee);
		bill.setOwe_fee(oweFee);
		bBillDao.save(bill);
		createRecordChange("B_BILL", "ADD", billSn);
	}
	/**
	 * 创建账单
	 * @param prod              产品信息
	 * @param doneCode			流水号
	 * @param billingCycle		帐期
	 * @param billFee			出帐金额
	 * @param comeFrom			来源
	 * @throws Exception
	 */
	
	
	public BBill createBill(CProd prod,int doneCode,String billingCycle,int billFee,int oweFee,String comeFrom) throws Exception{
		String billSn = bBillDao.findSequence().toString();
		BBill bill = new BBill();
		BeanUtils.copyProperties(prod, bill);
		bill.setAcctitem_id(prod.getProd_id());
		bill.setBill_sn(billSn);
		bill.setBilling_cycle_id(billingCycle);
		bill.setStatus("1");
		bill.setCome_from(comeFrom);
		bill.setBill_done_code(""+doneCode);
		bill.setFee_flag("ZC");
		bill.setBill_type("1");
		bill.setBill_date(new Date());
		bill.setRent_month_fee(billFee);
		bill.setUse_fee(0);
		bill.setFinal_month_fee(billFee);
		bill.setFinal_use_fee(0);
		bill.setFinal_bill_fee(billFee);
		bill.setOwe_fee(oweFee);
		bBillDao.save(bill);
		createRecordChange("B_BILL", "ADD", billSn);
		return bill;
	}
	
	/**
	 * 创建账单
	 * @param prod              产品信息
	 * @param doneCode			流水号
	 * @param billingCycle		帐期
	 * @param billFee			出帐金额
	 * @param comeFrom			来源
	 * @throws Exception
	 */
	
	
	public BBill createVodBill(CProd prod,int doneCode,String billingCycle,int billFee,int oweFee,String comeFrom) throws Exception{
		String billSn = bBillDao.findSequence().toString();
		BBill bill = new BBill();
		BeanUtils.copyProperties(prod, bill);
		bill.setAcctitem_id(prod.getProd_id());
		bill.setBill_sn(billSn);
		bill.setBilling_cycle_id(billingCycle);
		bill.setStatus("0");
		bill.setCome_from(comeFrom);
		bill.setBill_done_code(""+doneCode);
		bill.setFee_flag("ZC");
		bill.setBill_date(new Date());
		bill.setRent_month_fee(billFee);
		bill.setUse_fee(0);
		bill.setFinal_month_fee(billFee);
		bill.setFinal_use_fee(0);
		bill.setFinal_bill_fee(billFee);
		bill.setOwe_fee(oweFee);
		bBillDao.save(bill);
		createRecordChange("B_BILL", "ADD", billSn);
		return bill;
	}
	/**
	 * 出帐
	 */
	public BBill confirmBill(String prodSn,int doneCode) throws Exception{
		BBill bill = bBillDao.confirmBill(prodSn, doneCode);
		if (bill != null)
			createRecordChange("B_BILL", "ADD", bill.getBill_sn());
		return bill;
	}
	
	/**
	 * 作废账单
	 * @param billSn
	 * @throws Exception
	 */
	public void cancelBill(String billSn) throws Exception{
		this.bBillDao.updateStatus(billSn,"4");
	}
	
	public void cancelBillByDoneCode(Integer doneCode, String status, String billMonth, String comeFrom) throws Exception {
		this.bBillDao.updateStatusByDoneCode(doneCode, status, billMonth, comeFrom);
	}
	
	public void updateFeeStatus(String billSn) throws Exception{
		this.bBillDao.updateFeeStatus(billSn,"ZF");
	}
	
	public void updateBillInfo(String oldProdSn, String newProdSn, String newTariffId, 
			String newAcctId, String newProdId) throws Exception {
		this.bBillDao.updateBillInfo(oldProdSn, newProdSn, newTariffId, newAcctId, newProdId, getOptr().getCounty_id());
	}
	
	/**
	 * 作废账单
	 * @param billSn
	 * @throws Exception
	 */
	public void cancelBill(String prodSn,String billCycleId) throws Exception{
		this.bBillDao.cancelBill(prodSn,billCycleId,"4");
	}
	
	/**
	 * 作废包多月或套餐账单
	 * @param prodSn
	 * @param billCycleId
	 * @throws Exception
	 */
	public void cancelMuchBill(String prodSn,String billCycleId) throws Exception{
		this.bBillDao.cancalMuchBill(prodSn,billCycleId,"4");
	}
	
	/**
	 * 作废普通帐单
	 * @param prodSn
	 * @param billCycleId
	 * @throws Exception
	 */
	public void cancelTerminateBill(String prodSn,String billCycleId) throws Exception{
		this.bBillDao.cancelTerminateBill(prodSn,billCycleId,"4");
	}
	
	public List<BBill> queryMuchBill(String prodSn, String billCycleId, String comeFrom) throws Exception {
		return bBillDao.queryMuchBill(prodSn, billCycleId, comeFrom);
	}
	
	public List<BBill> queryOweFeeBill(String prodSn) throws Exception {
		return bBillDao.queryOweFeeBill(prodSn);
	}
	
	/**
	 * 作废账期之后的所有账单
	 * @param billSn
	 * @throws Exception
	 */
	public void cancelOweActiveBill(String prodSn,String billCycleId) throws Exception{
		this.bBillDao.cancelOweActiveBill(prodSn,billCycleId,"4");
	}
	
	/**
	 * 删除月租
	 * @param prodSn
	 * @throws Exception
	 */
	public void deleteRentfee(String billSn,String prodSn,String acctId, String acctItemId) throws Exception{
		this.bRentfeeDao.deleteRentfee(prodSn, acctId, acctItemId);
		createRecordChange("B_BILL", "MOD", billSn);
	}
	
	/**
	 * 删除月租
	 * @param prodSn
	 * @throws Exception
	 */
	public void deleteRentfee(String prodSn,String countyId) throws Exception{
		this.bRentfeeDao.deleteRentfee(prodSn,countyId);
	}
	
	/**
	 * 恢复账单
	 * @param prodSn
	 * @param billCycleId
	 * @throws Exception
	 */
	public void recoverBill(String prodSn,String billCycleId)throws Exception{
		this.bBillDao.recoverBill(prodSn,billCycleId,"0");
	}
	
	public List<BBill> queryOweBillByProdSn(String prodSn) throws Exception {
		return bBillDao.queryOweBillByProdSn(prodSn);
	}
	
	public List<BBill> queryPromOweFeeBill(String prodSn) throws Exception {
		return bBillDao.queryPromOweFeeBill(prodSn);
	}
	
	//手工创建销账记录
	public void createWriteOff(BBill bill,String addrId) throws Exception{
		String sn = this.bBillWriteoffDao.findSequence().toString();
		BBillWriteoff writeOff = new BBillWriteoff();
		BeanUtils.copyProperties(bill, writeOff);
		writeOff.setWriteoff_sn(sn);
		writeOff.setFee_type(SystemConstants.ACCT_FEETYPE_CASH);
		writeOff.setFee(bill.getFinal_bill_fee());
		writeOff.setStatus("ZC");
		writeOff.setWriteoff_type("XZ");
		writeOff.setBill_acctitem_id(bill.getAcctitem_id());
		writeOff.setBill_tariff_id(bill.getTariff_id());
		writeOff.setAddr_id(addrId);
		this.bBillWriteoffDao.save(writeOff);
		
	}
	
	public void cancelWriteOff(BBillWriteoff writeOff,Integer doneCode) throws Exception{
		//更新原销账记录的状态和取消流水号
		this.bBillWriteoffDao.updateStatus(writeOff.getWriteoff_sn(),"ZF",doneCode);
		//插入反销账记录
		String sn = this.bBillWriteoffDao.findSequence().toString();
		writeOff.setWriteoff_sn(sn);
		writeOff.setDone_code(doneCode);
		writeOff.setWriteoff_type("FXZ");
		writeOff.setFee(writeOff.getFee()*-1);
		this.bBillWriteoffDao.save(writeOff);
		
	}
	
	/**
	 * @param billSn
	 * @return
	 */
	public List<BBillWriteoff> queryWriteOffByBill(String billSn)  throws Exception{
		// TODO Auto-generated method stub
		return this.bBillWriteoffDao.queryByBill(billSn);
	}
	
	/**
	 * 根据客户和月份查询客户月账单
	 * @param custId
	 * @param month 格式 YYYYMM
	 * @return
	 * @throws Exception
	 */
	public List<BillDto> queryBillByCustId(String custId,String month) throws Exception{
		return bBillDao.queryBillByCustId(custId, month);
	}
	
	/**
	 * 根据客户和月份查询客户欠费月账单
	 * @param custId
	 * @param month 格式 YYYYMM
	 * @return
	 * @throws Exception
	 */
	public Pager<BillDto> queryCustBill(String custId, QueryFeeInfo queryFeeInfo, Integer start, Integer limit) throws Exception{
		return bBillDao.queryCustBill(custId, queryFeeInfo, start, limit);
	}
	
	public List<BillDto> queryCustOweBill(String custId)throws Exception{
		return bBillDao.queryCustOweBill(custId);
	}
	
	/**
	 * 查找产品最后一个月的账单
	 * @param prodSn
	 * @return
	 * @throws JDBCException
	 */
	public BBill queryLatsBillByProdSn(String prodSn) throws JDBCException {
		return bBillDao.queryLatsBillByProdSn(prodSn);
	}
	

	
	public BBill queryBillBySn(String billSn) throws Exception{
		return bBillDao.findByKey(billSn);
	}
	
	public int updateBill(String prodSn,int billFee) throws Exception{
		return bBillDao.updateBill(prodSn, billFee);
	}
	
	public void updateMuchBill(String prodSn, int fee, String billingCycleId) throws Exception {
		bBillDao.updateMuchBill(prodSn, fee, billingCycleId, getOptr().getCounty_id());
	}


	public void deleteBill(int doneCode) throws Exception{
		bBillWriteoffDao.deleteWriteOff(doneCode);
		bBillDao.deleteBill(doneCode);
	}

	public void setBBillWriteoffDao(BBillWriteoffDao billWriteoffDao) {
		bBillWriteoffDao = billWriteoffDao;
	}
	public void setBRentfeeDao(BRentfeeDao rentfeeDao) {
		bRentfeeDao = rentfeeDao;
	}

}
