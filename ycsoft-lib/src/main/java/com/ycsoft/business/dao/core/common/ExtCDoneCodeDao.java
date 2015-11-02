/**
 * ExtCDoneCodeDao.java	2010/05/24
 */

package com.ycsoft.business.dao.core.common;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.common.ExtCDoneCode;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * ExtCDoneCodeDao -> EXT_C_DONE_CODE table's operator
 */
@Component
public class ExtCDoneCodeDao extends BaseEntityDao<ExtCDoneCode> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6448862157571375760L;

	/**
	 * default empty constructor
	 */
	public ExtCDoneCodeDao() {}
	
	public List<ExtCDoneCode> queryExtOperateObjByDoneCode(Integer doneCode) throws JDBCException{
		String sql="select * from ext_c_done_code t where t.done_code=? and t.attribute_id=? ";
	    return this.createQuery(sql, doneCode,SystemConstants.ExtOperateObj).list();
	}

}
