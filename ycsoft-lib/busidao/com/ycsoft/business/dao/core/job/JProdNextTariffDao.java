/**
 * JProdNextTariffDao.java	2010/06/08
 */

package com.ycsoft.business.dao.core.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * JProdNextTariffDao -> J_PROD_NEXT_TARIFF table's operator
 */
@Component
public class JProdNextTariffDao extends BaseEntityDao<JProdNextTariff> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7724974272184590007L;

	/**
	 * default empty constructor
	 */
	public JProdNextTariffDao() {}
	
	public JProdNextTariff queryByProdSn(String prodSn, String tariffId, String countyId) throws JDBCException {
		String sql = "select * from j_prod_next_tariff where prod_sn=? and tariff_id=? and county_id=?";
		return this.createQuery(sql, prodSn, tariffId, countyId).first();
	}
	
	public void removeByProdSn(String prodSn, String tariffId, String countyId) throws JDBCException {
		String sql = "delete j_prod_next_tariff where prod_sn=? and tariff_id=? and county_id=?";
		executeUpdate(sql, prodSn, tariffId, countyId);
	}
	
	public void removeByProdSn(String prodSn, String countyId) throws JDBCException {
		String sql = "delete j_prod_next_tariff where prod_sn=? and county_id=?";
		executeUpdate(sql, prodSn, countyId);
	}

	/**
	 * @param doneCode
	 */
	public void removeByDoneCode(Integer doneCode) throws Exception{
		String sql = "delete J_PROD_NEXT_TARIFF where done_code =?";
		executeUpdate(sql, doneCode);
	}

	/**
	 * @param doneCode
	 * @param county_id
	 * @return
	 */
	public JProdNextTariff queryByDoneCode(Integer doneCode, String countyId) throws Exception{
		String sql = " select * from J_PROD_NEXT_TARIFF " +
		" where done_code=? and county_id=? order by job_id desc";
		return createQuery(sql, doneCode,countyId).first();
	}
	
	/**
	 * 取相同donecode,prodsn的资费变更任务的最后一个变更任务
	 * @param doneCode
	 * @param county_id
	 * @return
	 */
	public JProdNextTariff queryByDoneCodeProdSn(Integer doneCode,String prodSn, String countyId) throws Exception{
		String sql = " select * from J_PROD_NEXT_TARIFF " +
		" where done_code=? and prod_sn=? and county_id=? order by eff_date desc";
		return createQuery(sql, doneCode,prodSn,countyId).first();
	}

	public List<JProdNextTariff> queryNextByProdSn(String prodSn, String countyId) throws JDBCException {
		String sql = "select * from j_prod_next_tariff where prod_sn=? and  county_id=?";
		return this.createQuery(sql, prodSn, countyId).list();
	}
}
