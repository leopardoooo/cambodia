package com.yaochen.boss.dao;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiCmdSupplier;
import com.ycsoft.beans.config.TServer;
import com.ycsoft.beans.config.TServerRes;
import com.ycsoft.beans.config.TStbFilled;
import com.ycsoft.beans.device.RCardModel;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdCountyRes;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.system.SArea;
import com.ycsoft.business.dto.core.prod.PPromotionDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.prod.ProdDto;

@Component
public class CfgDao extends BaseEntityDao<PProd> {
	/**
	 *
	 */
	private static final long serialVersionUID = 2965076443020972478L;

	/**
	 * default empty constructor
	 */
	public CfgDao() {}

	/**
	 * 查找产品配置信息
	 */
	public List<ProdDto> queryProd() throws Exception{
		String sql = "select * from p_prod";
		return this.createQuery(ProdDto.class,sql).list();
	}

	public List<String> queryProdRes(String prodId) throws Exception{
		String sql = "select res_id from p_prod_static_res where prod_id=?";
		return findUniques(sql, prodId);
	}

	public List<PProdCountyRes> queryProdResCounty(String prodId) throws Exception{
		String sql = "select * from p_prod_county_res where prod_id=? order by county_id ";
		return this.createQuery(PProdCountyRes.class, sql, prodId).list();
	}
	
	public List<String> queryProdResCounty(String prodId,String countyId) throws Exception{
		String sql = "select res_id from p_prod_county_res where prod_id=? and county_id=? ";
		return this.findUniques( sql, prodId,countyId);
	}


	public int queryProdDynamicResCount(String prodId) throws Exception{
		String sql = "select count(1) from p_prod_dyn_res where prod_id=?";
		return Integer.parseInt(findUnique(sql, prodId).toString());
	}
	/**
	 * 查找卡型号配置信息
	 */
	public List<RCardModel> queryCardModel()throws Exception{
		String sql ="select * from r_card_model";
		return this.createQuery(RCardModel.class,sql).list();
	}
	/**
	 * 读取服务器配置
	 */
	public List<TServer> queryServer() throws Exception{
		String sql ="select * from t_server";
		return this.createQuery(TServer.class,sql).list();
	}
	
	/**
	 * 读取宽带服务器配置
	 */
	public TServer queryBandServer(String countyId) throws Exception{
		String sql ="select a.* "+
					"  from t_server a, t_server_supplier b, t_server_county c "+
					" where a.supplier_id = b.supplier_id "+
					"   and a.server_id = c.server_id "+
					"   and b.server_type = 'BAND' "+
					"   and c.county_id = ?";
		List<TServer> serverList = this.createQuery(TServer.class,sql,countyId).list();
		if (serverList != null && serverList.size()>0)
			return serverList.get(0);
		else 
			return null;
	}

	public List<String> queryServerCounty(String serverId)throws Exception{
		String sql ="select county_id from t_server_county where server_id=? order by county_id";
		return this.findUniques(sql,serverId);
	}

	public List<TServerRes> queryServerRes()throws Exception{
		String sql ="select * from t_server_res order by boss_res_id";
		return this.createQuery(TServerRes.class,sql).list();
	}

	/**
	 * 读取BOSS指令和服务器指令配置
	 */
	public List<TBusiCmdSupplier> queryCmdSupplier() throws Exception{
		String sql ="select * from t_busi_cmd_supplier order by cmd_id,supplier_id,idx";
		return this.createQuery(TBusiCmdSupplier.class,sql).list();
	}

	/**
	 * 查找县市预授权配置信息
	 */

	public List<TStbFilled> queryStbFilledCfg(String countyId) throws Exception{
		String sql = "SELECT tsf.* FROM T_STB_FILLED TSF, t_template tt, t_template_county ttc "+
					 " where tt.template_id = ttc.template_id "+
					 "    and tsf.template_id = tt.template_id "+
					 "    and tt.template_type = 'STB_FILLED' "+
					 "    and ttc.county_id = ? ";
		return this.createQuery(TStbFilled.class,sql, countyId).list();
	}

	/**
	 * 获取地区信息
	 */
	public List<SArea> queryArea() throws Exception{
		String sql = "select * from s_area";
		return this.createQuery(SArea.class,sql).list();
	}

	/**
	 * 查找可以参加的促销
	 * @return
	 * @throws Exception
	 */

	public List<PPromotionDto> queryPromotion() throws Exception{
		String sql ="select p.promotion_id, promotion_name, theme_id, protocol_id, rule_id, auto_exec, eff_date, exp_date, total_acct_fee, priority, repetition_times,pc.county_id" +
				" from p_promotion p,p_promotion_county pc" +
				" where eff_date<sysdate and (exp_date is null or exp_date>sysdate)" +
				" and pc.promotion_id = p.promotion_id " +
				" order by theme_id,PRIORITY ";
		return this.createQuery(PPromotionDto.class, sql).list();
	}

	/**
	 * 查找促销赠送的产品信息
	 * @param promotionId
	 * @return
	 * @throws Exception
	 */
	public List<PPromotionAcct> queryPromotionAcct(String promotionId) throws Exception{
		String sql ="select * from p_promotion_acct " +
				" where promotion_id = ?";
		return this.createQuery(PPromotionAcct.class, sql, promotionId).list();
	}

	/**
	 * 查找在产品表和资费表中不存在的资费
	 */
	public int queryIllegalProd() throws Exception{
		String sql = "select count(1) from p_promotion_acct a,p_promotion b " +
					" where  a.promotion_id = b.promotion_id " +
					" and b.auto_exec='"+SystemConstants.BOOLEAN_TRUE + "' and (tariff_id is  null" +
					" or tariff_id not in (select tariff_id from p_prod_tariff))";
		return Integer.parseInt(this.findUnique(sql));
	}

	public String queryBandService(String prodId, String serverId) throws Exception{
		String sql =StringHelper.append("select b.external_res_id ",
					"  from p_prod_static_res a, t_server_res b ",
					" where a.res_id = b.boss_res_id ",
					"   and a.prod_id = ? " ,
					"   and b.server_id =? ",
					" union select b.external_res_id ",
					" from p_prod_county_res a, t_server_res b ",
					" where a.res_id = b.boss_res_id and a.prod_id = ? and b.server_id =?");
		return this.findUnique(sql, prodId,serverId, prodId,serverId);
	}
}
