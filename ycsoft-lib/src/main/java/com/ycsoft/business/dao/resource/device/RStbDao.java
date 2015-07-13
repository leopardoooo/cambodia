/**
 * RStbDao.java	2010/06/24
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RStb;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * RStbDao -> R_STB table's operator
 */
@Component
public class RStbDao extends BaseEntityDao<RStb> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8224229640506660020L;

	/**
	 * default empty constructor
	 */
	public RStbDao() {}

	/**
	 * @param stbDeviceId
	 * @param cardId
	 */
	public void updatePairCard(String stbDeviceId, String cardId) throws JDBCException {
		String sql = "update r_stb s set s.pair_card_id=? where s.device_id=?";
		executeUpdate(sql,cardId, stbDeviceId);
	}
	
	public void updatePairModem(String stbDeviceId, String modemId) throws JDBCException {
		String sql = "update r_stb s set s.pair_modem_id=? where s.device_id=?";
		executeUpdate(sql,modemId, stbDeviceId);
	}

	/**
	 * 根据智能卡id ，查询配对的机顶盒
	 * @param stbDeviceId
	 * @return
	 */
	public RStb findPairStbByCardDeviceId(String cardDeviceId)
			throws JDBCException {
		String sql = "select s.*,'STB' device_type from r_stb s where s.pair_card_id=?";
		return createQuery(sql, cardDeviceId).first();

	}
	
	/**
	 * 根据智能卡id ，查询配对的机顶盒
	 * @param stbDeviceId
	 * @return
	 */
	public RStb findPairStbByModemDeviceId(String cardDeviceId)
			throws JDBCException {
		String sql = "select s.*,'STB' device_type from r_stb s where s.pair_modem_id=?";
		return createQuery(sql, cardDeviceId).first();

	}
	
	public RStb queryStbById(String stbId) throws JDBCException {
		String sql = StringHelper.append("select * from r_stb  where stb_id = ? ");
		return createQuery(RStb.class,sql, stbId).first();

	}
	
	public boolean isExistsStb(String stbId) throws Exception{
		String sql ="select count(stb_id) from r_stb where stb_id=?";
		return count(sql, stbId)>0;
	}
	
	public List<RStb> findPairCardByDeviceId(String[] deviceIds) throws JDBCException {
		String sql = "select s.* from r_stb s where "+getSqlGenerator().setWhereInArray("s.device_id",deviceIds)+" and s.pair_card_id is not null ";
		return createQuery(sql).list();
	
	}
	
	public List<RStb> findPairByDeviceId(String[] deviceIds,String[] paidCards,String type) throws JDBCException {
		String sql = "select s.* from r_stb s where "+getSqlGenerator().setWhereInArray("s.device_id",deviceIds);
		if(paidCards != null){
			if(type.equals(SystemConstants.DEVICE_TYPE_CARD)){
				sql+=" and s.pair_card_id not in ";
			}else{
				sql+=" and s.pair_modem_id not in ";
			}
			sql += "(select r.device_id from r_device r where "+getSqlGenerator().setWhereInArray("r.device_id",deviceIds)+")";
		}
		return createQuery(sql).list();
	}

}
