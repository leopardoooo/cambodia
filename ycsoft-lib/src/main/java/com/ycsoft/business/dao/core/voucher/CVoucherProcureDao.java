/**
 * CVoucherProcureDao.java	2011/03/23
 */
 
package com.ycsoft.business.dao.core.voucher; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.voucher.CVoucherProcure;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;


/**
 * CVoucherProcureDao -> C_VOUCHER_PROCURE table's operator
 */
@Component
public class CVoucherProcureDao extends BaseEntityDao<CVoucherProcure> {
	
	/**
	 * default empty constructor
	 */
	public CVoucherProcureDao() {}
	
	public Pager<CVoucherProcure> queryVoucherProcure(String query, String countyId,
			Integer start, Integer limit) throws Exception {
		String sql = "select * from c_voucher_procure where county_id=?";
		if(StringHelper.isNotEmpty(query)){
			sql +=" and (procure_no like '%"+query+"%' or procure_dept like '%"+query+"%' or procurer like '%"+query+"%')";
		}
		sql +=" order by create_time desc";
		return this.createQuery(sql, countyId).setStart(start).setLimit(limit).page();
	}

}
