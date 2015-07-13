/**
 * TBusiCodeDao.java	2010/02/25
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiCode;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TBusiCodeDao -> T_BUSI_CODE table's operator
 */
@Component
public class TBusiCodeDao extends BaseEntityDao<TBusiCode> {

	/**
	 *
	 */
	private static final long serialVersionUID = 9206442426616499351L;

	/**
	 * default empty constructor
	 */
	public TBusiCodeDao() {}

	/**
	 * 根据业务类型查询业务编号定义信息
	 * @param busiFee
	 * @return
	 * @throws Exception
	 */
	public List<TBusiCode> queryBusiCodeByBusiFee(String busiFee) throws Exception {
		String sql = "select * from t_busi_code t where t.busi_fee=?";
		return this.createQuery(sql, busiFee).list();
	}
	
	/**
	 * 查询跟信息修改相关的业务
	 * @return
	 * @throws Exception
	 */
	public List<TBusiCode> queryBusiForUpdate() throws Exception{
		String sql = "select * from t_busi_code t where t.update_info = ?";
		return createQuery(TBusiCode.class, sql, SystemConstants.BOOLEAN_TRUE).list();
	}
}
