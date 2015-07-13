package com.ycsoft.business.dao.prod;

/**
 * PPromFeeCountyDao.java	2012/07/13
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromFeeCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * PPromFeeCountyDao -> P_PROM_FEE_COUNTY table's operator
 */
@Component
public class PPromFeeCountyDao extends BaseEntityDao<PPromFeeCounty> {
	
	/**
	 * default empty constructor
	 */
	public PPromFeeCountyDao() {}

	public void deleteById(String promFeeId) throws JDBCException {
		String sql = "delete p_prom_fee_county where prom_fee_id=?";
		this.executeUpdate(sql, promFeeId);
	}

	public List<TreeDto> querybyPromFeeId(String promFeeId) throws JDBCException {
		String sql = " select county_id id from P_PROm_fee_COUNTY where prom_fee_id = ? ";
		return createQuery(TreeDto.class,sql,promFeeId).list();
	}

}
