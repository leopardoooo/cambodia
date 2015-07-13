/**
 * TBusiCodeFeeDao.java	2010/02/25
 */

package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiCodeFee;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * TBusiCodeFeeDao -> T_BUSI_CODE_FEE table's operator
 */
@Component
public class TBusiCodeFeeDao extends BaseEntityDao<TBusiCodeFee> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3858321005763850249L;

	/**
	 * default empty constructor
	 */
	public TBusiCodeFeeDao() {
	}

	/**
	 * 根据费用id删除记录
	 * @param feeId
	 * @throws Exception
	 */
	public void deleteByFeeId(String feeId) throws Exception {
		String sql = "delete from t_busi_code_fee t where t.fee_id=?";
		executeUpdate(sql, feeId);
	}

	/**
	 * 根据模板ID删除记录
	 *
	 * @param templateId
	 * @throws JDBCException
	 */
	public void deleteByTplId(String templateId) throws JDBCException {
		String sql = "delete from t_busi_code_fee t where t.template_id=?";
		executeUpdate(sql, templateId);
	}


	/**
	 * @Description:判断模板业务费用规则是否已经存在
	 * @param template_id
	 * @param busi_code
	 * @param fee_id
	 * @param value_id
	 * @return
	 * @throws Exception
	 * @return Object
	 */
	public Object isupdate(String template_id, String busi_code, String fee_id,
			String value_id) throws JDBCException {
		String sql = "  select * from t_busi_code_fee where  template_id = ? and busi_code = ? and fee_id = ? and value_id = ?  ";
		Object request = findUnique(sql, template_id, busi_code, fee_id,
				value_id) == null ? false : true;
		if (request.equals(true)) {
			request = "该模板业务费用组合已经存在";
		}
		return request;
	}

	public void updateTemplateBusi(String template_id, String busi_code,
			String fee_id, String value_id, String template_id_back,
			String busi_code_back, String fee_id_back, String value_id_back)
			throws JDBCException {
		String sql = " UPDATE t_busi_code_fee  SET   template_id = ? ,busi_code= ? , fee_id = ?,value_id = ?  WHERE template_id = ? and busi_code= ? and fee_id = ?  and value_id = ?    ";
		executeUpdate(sql, template_id, busi_code, fee_id, value_id,
				template_id_back, busi_code_back, fee_id_back, value_id_back);
	}

	public void logoffTF(String template_id, String busi_code, String fee_id,
			String value_id) throws JDBCException {
		String sql = " DELETE t_busi_code_fee   WHERE   template_id = ?  and busi_code = ? and fee_id = ? and value_id = ? ";
		executeUpdate(sql, template_id, busi_code, fee_id, value_id);
	}

}
