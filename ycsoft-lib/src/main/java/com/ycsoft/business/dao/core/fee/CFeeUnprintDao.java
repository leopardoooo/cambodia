package com.ycsoft.business.dao.core.fee;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.common.CDoneCodeUnpay;
import com.ycsoft.beans.core.fee.CFeeUnprint;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
@Component
public class CFeeUnprintDao extends BaseEntityDao<CFeeUnprint>  {

	public List<CFeeUnprint> queryByOptr(String optrId) throws JDBCException{
		String sql="select * from c_fee_unprint where optr_id=? ";
		return this.createQuery(sql, optrId).list();
	}
	/**
	 * 插入未打印的费用项目
	 * @param unPay
	 * @throws JDBCException
	 */
	public void insertByUnPayDoneCode(CDoneCodeUnpay unPay) throws JDBCException{
		String sql="insert into busi.c_fee_unprint(fee_sn,cust_id,optr_id,create_done_code) "
				+" select cf.fee_sn,cf.cust_id,cf.optr_id,cf.create_done_code "
				+" from c_fee cf "
				+" where create_done_code=? and cust_id=? and  optr_id=? and status=? ";
		this.executeUpdate(sql, unPay.getDone_code(),unPay.getCust_id(),unPay.getOptr_id(),StatusConstants.UNPAY);
	}
	/**
	 * 删除已打印的费用项目
	 * @param docitemSns
	 * @throws JDBCException
	 */
	public void deleteUnPrintByDocItem(String[] docitemSns) throws JDBCException{
		String sql=" delete from c_fee_unprint un where un.fee_sn in (select t.fee_sn from c_doc_fee t where t.docitem_sn=?) ";
		this.executeBatch(sql, docitemSns);
	}
	
}
