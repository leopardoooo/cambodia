/**
 * CDocFeeDao.java	2010/04/09
 */

package com.ycsoft.business.dao.core.print;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.print.CDocFee;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CDocFeeDao -> C_DOC_FEE table's operator
 */
@Component
public class CDocFeeDao extends BaseEntityDao<CDocFee> {

	/**
	 *
	 */
	private static final long serialVersionUID = 9191924861218601989L;

	/**
	 * default empty constructor
	 */
	public CDocFeeDao() {}
	
	public int queryByDocSn(String docSn) throws Exception {
		String sql = "select count(distinct f.user_id) from c_fee f,c_doc_fee df"
			+" where f.fee_sn=df.fee_sn"
			+" and df.doc_sn=?"
			+" union all "
			+" select count(distinct f.user_id) from c_fee f,c_doc_fee df,c_prom_fee ff,p_prom_fee pf"
			+" where f.create_done_code=ff.done_code"
			+" and ff.prom_fee_sn = df.fee_sn and ff.prom_fee_id=pf.prom_fee_id"
			+" and df.doc_sn=?";
		return Integer.parseInt(findUnique(sql, docSn, docSn));
	}

}
