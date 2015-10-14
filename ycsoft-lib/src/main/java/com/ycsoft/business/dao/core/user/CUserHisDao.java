/**
 * CUserHisDao.java	2010/06/07
 */

package com.ycsoft.business.dao.core.user;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserHis;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;




/**
 * CUserHisDao -> C_USER_HIS table's operator
 */
@Component
public class CUserHisDao extends BaseEntityDao<CUserHis> {

	/**
	 *
	 */
	private static final long serialVersionUID = 8988497175198403603L;

	/**
	 * default empty constructor
	 */
	public CUserHisDao() {}

	public void updateCheckFlag(String cardId) throws Exception{
		String sql ="update c_user_his set need_check=? where card_id=?";
		
		this.executeUpdate(sql, SystemConstants.BOOLEAN_FALSE,cardId);
		
	}
	public List<CUser> queryAllUserHisByUserIds(String[] userIds) throws JDBCException{
		String sql = "SELECT * FROM c_user_his where "+getSqlGenerator().setWhereInArray("user_id",userIds)+"";
		return createQuery(CUser.class, sql).list();
	}
}
