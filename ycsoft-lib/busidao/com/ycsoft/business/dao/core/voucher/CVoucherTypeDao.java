package com.ycsoft.business.dao.core.voucher;

/**
 * CVoucherDao.java	2011/01/25
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.voucher.CVoucherType;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CVoucherDao -> C_VOUCHER table's operator
 */
@Component
public class CVoucherTypeDao extends BaseEntityDao<CVoucherType> {
	
	/**
	 * default empty constructor
	 */
	public CVoucherTypeDao() {}

	public List<CVoucherType> findAllTypes() throws Exception{
		String sql = "select t.*,r.rule_name from c_voucher_type t ,busi.t_rule_define r where r.rule_id(+) = t.rule_id ";
		return createQuery(sql).list();
	}

	public void removeAll() throws Exception{
		executeUpdate("delete from c_voucher_type");
	}
	
}
