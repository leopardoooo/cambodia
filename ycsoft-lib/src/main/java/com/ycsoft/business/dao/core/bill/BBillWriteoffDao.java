/**
 * BBillWriteoffDao.java	2011/04/11
 */
 
package com.ycsoft.business.dao.core.bill; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bill.BBillWriteoff;
import com.ycsoft.business.dto.print.BBillWriteoffDto;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * BBillWriteoffDao -> B_BILL_WRITEOFF table's operator
 */
@Component
public class BBillWriteoffDao extends BaseEntityDao<BBillWriteoff> {
	
	/**
	 * default empty constructor
	 */
	public BBillWriteoffDao() {}
	
	public void save(BBillWriteoff writeOff) throws Exception{
		String sql ="insert into b_bill_writeoff "+
              "  (done_code, writeoff_sn, bill_sn, "+
              "   cust_id, acct_id, acctitem_id, "+
              "   fee_type, writeoff_date, fee, "+
              "   area_id, county_id, status,  "+
              "   cancel_done_code, writeoff_type, "+
              "   busi_code, years, bill_acctitem_id,bill_tariff_id,addr_id) "+
              "   values "+
              "  (?, ?, ?, ?, ?, ?, ?, sysdate, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'yyyymm'),?,?,?)";
		this.executeUpdate(sql, writeOff.getDone_code(),writeOff.getWriteoff_sn(),writeOff.getBill_sn(),
				writeOff.getCust_id(),writeOff.getAcct_id(),writeOff.getAcctitem_id(),
				writeOff.getFee_type(),writeOff.getFee(),
				writeOff.getArea_id(),writeOff.getCounty_id(),writeOff.getStatus(),
				"",writeOff.getWriteoff_type(),writeOff.getBusi_code(),writeOff.getBill_acctitem_id(),writeOff.getBill_tariff_id(),writeOff.getAddr_id());
	}
	
	public void updateStatus(String writeOffSn, String status,Integer doneCode) throws Exception{
		String sql="update b_bill_writeoff set status=?,cancel_done_code=? where writeoff_sn=?";
		this.executeUpdate(sql, status,doneCode,writeOffSn);
		
	}

	public List<BBillWriteoff> queryByBill(String billSn)throws Exception {
		String sql ="select * from b_bill_writeoff where bill_sn=?";
		return this.createQuery(sql, billSn).list();
	} 
	
	public void deleteWriteOff(int billDoneCode)throws Exception {
		String sql = "delete b_bill_writeoff where bill_sn=(select bill_sn from b_bill where bill_done_code=?)";
		this.executeUpdate(sql, billDoneCode);
	}
	
	public List<BBillWriteoffDto> queryWriteoffInfo(Integer doneCode) throws JDBCException {
		String sql= " select sum(bbt.fee) fee ,bb.serv_id, min(bb.billing_cycle_id) min_cycle_id , max(bb.billing_cycle_id) max_cycle_id "+
			" from bill.b_bill_writeoff bbt , busi.c_fee cf ,bill.b_bill bb"+
			" where  bbt.done_code = cf.create_done_code "+
			" and cf.create_done_code=? "+
			" and bbt.acctitem_id=cf.acctitem_id "+
			" and bbt.bill_sn = bb.bill_sn group by bb.serv_id ";
		return this.createQuery(BBillWriteoffDto.class, sql,doneCode).list();
	}

}
