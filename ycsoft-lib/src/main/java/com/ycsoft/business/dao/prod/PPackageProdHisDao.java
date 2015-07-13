package com.ycsoft.business.dao.prod;

/**
 * PPackageProdHisDao.java	2011/09/26
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPackageProdHis;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;



/**
 * PPackageProdHisDao -> P_PACKAGE_PROD_HIS table's operator
 */
@Component
public class PPackageProdHisDao extends BaseEntityDao<PPackageProdHis> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2939053732791613614L;

	/**
	 * default empty constructor
	 */
	public PPackageProdHisDao() {}

	public List<PPackageProdHis> queryByPackId(String packageId) throws JDBCException{
		String sql = StringHelper.append("select t2.prod_name,t3.tariff_name package_tariff_name,t4.tariff_name ,t.* ",
				" from p_package_prod_his t,p_prod t2,p_prod_tariff t3,p_prod_tariff t4 where ",
				" t.prod_id=t2.prod_id and t.package_tariff_id=t3.tariff_id and t.tariff_id=t4.tariff_id(+) ",
				" and t.package_id=? order by t.type, t.done_date desc" );
		return createQuery(sql, packageId).list();
	}
}
