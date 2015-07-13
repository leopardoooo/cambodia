/**
 * TAdjustDataDao.java	2012/08/13
 */
 
package com.ycsoft.business.dao.config; 

import java.util.Date;
import org.springframework.stereotype.Component;
import com.ycsoft.beans.config.TAdjustData;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * TAdjustDataDao -> T_ADJUST_DATA table's operator
 */
@Component
public class TAdjustDataDao extends BaseEntityDao<TAdjustData> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8998717128928823096L;

	/**
	 * default empty constructor
	 */
	public TAdjustDataDao() {}
	
	public void updateAdjustDate(String optrId, Date date) throws JDBCException {
		String sql = "update T_ADJUST_DATA set adjust_date=to_date(to_char(?,'yyyy-MM-dd'),'yyyy-MM-dd') where optr_id=?";
		this.executeUpdate(sql, date, optrId);
	}

}
