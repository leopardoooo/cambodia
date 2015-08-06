package com.ycsoft.business.dao.core.common;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.common.CDoneCodeUnpay;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class CDoneCodeUnpayDao extends BaseEntityDao<CDoneCodeUnpay> {
	
	/**
	 * 查询一个客户未支付的业务信息
	 * @param cust_id
	 * @return
	 * @throws JDBCException 
	 */
	public List<CDoneCodeUnpay> queryUnPay(String cust_id) throws JDBCException{
		String sql="select * from c_done_code_unpay where cust_id=? ";
		return this.createQuery(sql, cust_id).list();
	}
	/**
	 * 查询非当前营业员的未支付业务
	 * @param cust_id
	 * @param optr_id
	 * @return
	 * @throws JDBCException 
	 */
	public List<CDoneCodeUnpay> queryUnPayOtherLock(String cust_id,String optr_id) throws JDBCException{
		String sql="select * from c_done_code_unpay where cust_id=? and optr_id<>?";
		return this.createQuery(sql, cust_id,optr_id).list();
	}
	
	/**
	 * 保存为支付业务信息
	 * @param cust_id
	 * @param done_code
	 * @throws JDBCException
	 */
	public void saveUnpay(String cust_id,Integer done_code,String optr_id) throws JDBCException{
		CDoneCodeUnpay unpay=new CDoneCodeUnpay();
		unpay.setDone_code(done_code);
		unpay.setCust_id(cust_id);
		unpay.setDone_date(new Date());
		this.save(unpay);
	}
}
