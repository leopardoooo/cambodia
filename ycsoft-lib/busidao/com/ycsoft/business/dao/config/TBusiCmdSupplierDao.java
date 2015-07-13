/**
 * TBusiCmdSupplierDao	2011/08/22
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiCmdSupplier;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * TBusiCmdSupplierDao -> T_BUSI_CMD_SUPPLIER table's operator
 */
@Component
public class TBusiCmdSupplierDao extends BaseEntityDao<TBusiCmdSupplier> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2758318283929819082L;

	/**
	 * default empty constructor
	 */
	public TBusiCmdSupplierDao() {
	}

	public List<TBusiCmdSupplier> querySendTypeByType(String type,String countyId) throws JDBCException {
		if(StringHelper.isNotEmpty(type)){
			return createQuery(
				"select * from t_busi_cmd_supplier t where t.supplier_id= ? and t.idx='1' ",type).list();
		}else{
			String sql = "select distinct bcs.* from t_busi_cmd_supplier bcs,t_server s,t_server_county sc"
				+ " where bcs.supplier_id=s.supplier_id and s.server_id=sc.server_id "
				+ " and bcs.idx='1' and sc.county_id=?";
			return createQuery(sql, countyId).list();
		}
	}
}
