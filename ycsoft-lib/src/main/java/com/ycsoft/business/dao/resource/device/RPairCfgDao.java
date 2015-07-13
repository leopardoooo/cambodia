package com.ycsoft.business.dao.resource.device;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RCardModel;
import com.ycsoft.beans.device.RPairCfg;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * RPairCfgDao -> R_PAIR_CFG table's operator
 */
@Component
public class RPairCfgDao extends BaseEntityDao<RPairCfg> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2534568102210863312L;

	/**
	 * default empty constructor
	 */
	public RPairCfgDao() {}
	
	public void removeOld(String stbModel) throws JDBCException {
		String sql = "delete from R_PAIR_CFG where stb_model = ?";
		executeUpdate(sql, stbModel);
	}
	
	/**
	 * 查询已经做过机卡配对配置的卡型号.
	 * @param stbModel
	 * @return
	 * @throws JDBCException
	 */
	public List<RCardModel> queryStbCardPaired(String stbModel) throws JDBCException {
		String sql = "select c.* from r_card_model c ,r_pair_cfg rpc where rpc.card_model = c.device_model"
			+ " and rpc.stb_model = ? ";
		
		return createQuery(RCardModel.class, sql, stbModel).list();
	}

	/**
	 * 查询可以做配对配置的卡型号.
	 * @param stbModel
	 * @return
	 * @throws JDBCException
	 */
	public List<RCardModel> queryIdelCardModel(String stbModel) throws JDBCException {
		String sql = "SELECT c.* FROM r_card_model c ,t_server_supplier t,r_device_supplier sup " +
				" WHERE c.ca_type = t.supplier_id AND c.supplier_id=sup.supplier_id " +
				" and c.device_model not in (select p.card_model from r_pair_cfg p where p.stb_model = ? )";
		return createQuery(RCardModel.class, sql, stbModel).list();
	}
	
	public boolean isPair(String stbModel,String cardModel) throws Exception {
		String sql = "select count(*) from r_pair_cfg where stb_model=? and card_model=?";
		return count(sql, stbModel, cardModel) > 0;
	}

}
