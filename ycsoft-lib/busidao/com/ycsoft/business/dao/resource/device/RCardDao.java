/**
 * RCardDao.java	2010/06/24
 */

package com.ycsoft.business.dao.resource.device;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RCard;
import com.ycsoft.beans.device.RStb;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * RCardDao -> R_CARD table's operator
 */
@Component
public class RCardDao extends BaseEntityDao<RCard> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4303009976635007765L;

	/**
	 * default empty constructor
	 */
	public RCardDao() {}

	/**
	 * 根据机顶盒id ，查询配对的卡
	 * @param stbDeviceId
	 * @return
	 */
	public RCard findPairCardByStbDeviceId(String stbDeviceId)
			throws JDBCException {
		String sql = "select c.*,'CARD' device_type,r.is_virtual from r_card c,r_stb s,r_card_model r " +
				"where r.device_model = c.device_model and s.device_id=? and s.pair_card_id=c.device_id";
		return createQuery(sql, stbDeviceId).first();
	}
	/**
	 * 检查卡号是否存在
	 * @param cardId
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsCard(String cardId) throws Exception {
		String sql ="select count(card_id) from r_card where card_id=?";
		return count(sql, cardId)>0;
	}
	
	/**
	 * 根据卡号查卡信息
	 * @param cardId
	 * @return
	 * @throws JDBCException
	 */
	public RCard queryCardById(String cardId) throws JDBCException {
		String sql = StringHelper.append("select * from r_card  where card_id = ? ");
		return createQuery(RCard.class,sql, cardId).first();
	}
}
