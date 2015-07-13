/**
 * TTerminalAmountDao.java	2010/10/19
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TTerminalAmount;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TTerminalAmountDao -> T_TERMINAL_AMOUNT table's operator
 */
@Component
public class TTerminalAmountDao extends BaseEntityDao<TTerminalAmount> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2892790020226245555L;

	/**
	 * default empty constructor
	 */
	public TTerminalAmountDao() {}

	public List<TTerminalAmount> queryByTemplateId(String templateId)
			throws JDBCException {
		String sql = "SELECT * FROM t_terminal_amount t WHERE t.template_id=?";
		return createQuery(sql, templateId).list();
	}

}
