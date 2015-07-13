/**
 * CBandUpgradeRecordDao.java	2012/12/19
 */
 
package com.ycsoft.business.dao.record; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.record.CBandUpgradeRecord;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CBandUpgradeRecordDao -> C_BAND_UPGRADE_RECORD table's operator
 */
@Component
public class CBandUpgradeRecordDao extends BaseEntityDao<CBandUpgradeRecord> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2276708739081642903L;

	/**
	 * default empty constructor
	 */
	public CBandUpgradeRecordDao() {}
	
	public List<CBandUpgradeRecord> queryUpgradeRecordBydoneCode(Integer doneCode) throws Exception {
		String sql = "select t.*,p1.prod_name old_prod_name,p2.prod_name new_prod_name,pt1.tariff_name old_tariff_name,pt2.tariff_name new_taiff_name" +
				" from C_BAND_UPGRADE_RECORD t,p_prod p1,p_prod p2,p_prod_tariff pt1,p_prod_tariff pt2" +
				" where t.old_prod_id=p1.prod_id and t.new_prod_id=p2.prod_id" +
				" and t.old_tariff_id=pt1.tariff_id and t.new_tariff_id=pt2.tariff_id" +
				" and t.done_code=?";
		return this.createQuery(sql, doneCode).list();
	}

}
