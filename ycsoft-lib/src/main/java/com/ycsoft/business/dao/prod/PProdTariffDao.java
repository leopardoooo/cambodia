/**
 * PProdTariffDao.java	2010/07/05
 */

package com.ycsoft.business.dao.prod;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.business.dto.core.prod.PProdDto;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * PProdTariffDao -> P_PROD_TARIFF table's operator
 */
@Component
public class PProdTariffDao extends BaseEntityDao<PProdTariff> {

	/**
	 *
	 */
	private static final long serialVersionUID = 926819077568754739L;
	/**
	 * default empty constructor
	 */
	public PProdTariffDao() {}

	/**
	 * 根据产品id获取产品对应的有效资费
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryProdTariff(String prodId,String countyId,String dataRight)  throws Exception{
		String sql = "select t.*,r.rule_str rule_id_text,r.rule_name from p_prod_tariff t,p_prod_tariff_county tc,t_rule_define r " +
				" where t.tariff_id=tc.tariff_id and tc.county_id =? " +
				" and t.prod_id=? and t.status=? and t.rule_id=r.rule_id(+) " +
				" and t.tariff_id in (select tariff_id from p_prod_tariff where "+dataRight+") order by t.tariff_type ,t.rent";
		List<ProdTariffDto> tariffList = this.createQuery(ProdTariffDto.class,sql,countyId,prodId,StatusConstants.ACTIVE).list();
		return tariffList;
	}
	
	/**
	 * 根据产品id获取产品对应的有效资费和折扣
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public List<PProdDto> queryProdTariffDisct(String prodId,String countyId,String dataRight)  throws Exception{
		String sql = "select t.*,td.disct_id,td.disct_name,td.final_rent,td.disct_rent" +
				" from p_prod_tariff t,p_prod_tariff_county tc,p_prod_tariff_disct td,p_prod_tariff_disct_county tdc " +
				" where t.tariff_id=tc.tariff_id and t.tariff_id=td.tariff_id(+) and td.disct_id=tdc.disct_id(+)" +
				" and t.prod_id=? and tc.county_id =? and tdc.county_id(+)=? and t.status=? and td.status(+)=?" +
				" and t.tariff_id in (select tariff_id from p_prod_tariff where "+dataRight+")";
		return this.createQuery(PProdDto.class, sql, prodId, countyId,
				countyId, StatusConstants.ACTIVE, StatusConstants.ACTIVE).list();
	}

	public List<PProdTariff> queryPTariffByIds(String[] tariffIds) throws Exception{
		List<PProdTariff> tariffList = new ArrayList<PProdTariff>();
		String sql = "";
		if(tariffIds.length>0){
			sql = "select t.*,r.rule_str rule_id_text from p_prod_tariff t,t_rule_define r where t.rule_id=r.rule_id(+) and("+getSqlGenerator().setWhereInArray("t.tariff_id",tariffIds)+") ";
			tariffList.addAll(this.createQuery(PProdTariff.class, sql).list());
		}
		return tariffList;
	}

	/**
	 * 根据产品id获取产品对应的有效资费
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryTariffByProd(String prodId)  throws Exception{
		String sql = " select   t.*  from p_prod_tariff  t where  t.prod_id = ? ";
		return this.createQuery(ProdTariffDto.class,sql, prodId).list();
	}
	
	/**
	 * 根据产品编号和应用地市查询资费
	 * @param prodId
	 * @param prodCountyIds
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryTariffByCounty(String prodId,String [] prodCountyIds)  throws Exception{
		String sql = " select   t.*  from p_prod_tariff  t where t.prod_id=?  and t.status = ? " +
				" and t.tariff_id in (select pc.tariff_id from p_prod_tariff_county  pc where ("+getSqlGenerator().setWhereInArray("pc.county_id",prodCountyIds)+") )" ;
		return this.createQuery(ProdTariffDto.class,sql, prodId,StatusConstants.ACTIVE).list();
	}
	
	/**
	 * 根据操作员地市权限查询对应的产品资费
	 * @param prodId
	 * @param dataRight
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryTariffByProdId(String prodId,String dataRight)  throws Exception{
		String sql = " select pt.* from P_PROD_TARIFF pt where pt.prod_id= ? ";
		if(!SystemConstants.DEFAULT_DATA_RIGHT.equals(dataRight)){
			sql = StringHelper.append(sql," and exists (select 1 from p_prod_tariff_county pc where pc.tariff_id = pt.tariff_id  and pc.",dataRight.trim()," )");
		}
		return this.createQuery(ProdTariffDto.class,sql, prodId).list();
	}
	
	/**
	 * 查询促销适用地区可以用的资费
	 * @param acctitemId
	 * @param promotionId
	 * @return
	 * @throws JDBCException
	 */
	public List<PProdTariff> findProdTariff(String acctitemId,String promotionId) throws JDBCException {
		String sql = StringHelper.append("select DISTINCT t.tariff_id,t.tariff_name || '('||(nvl(tr.rule_name,'无规则'))||')' tariff_name,t.rent",
				" from p_prod_tariff t,p_prod_tariff_county pt,t_rule_define tr where",
				" t.rule_id=tr.rule_id(+) and t.status = ? and t.prod_id=? and t.tariff_id=pt.tariff_id and pt.county_id in",
				" (select pp.county_id from p_promotion p,p_promotion_county pp where",
				" p.promotion_id=? and p.promotion_id=pp.promotion_id)");
		return createQuery(PProdTariff.class, sql,StatusConstants.ACTIVE, acctitemId,promotionId).list();
	}
	public void deleteTariffByTariffId(String tariffId) throws Exception {
		String sql ="update p_prod_tariff set status=? where tariff_id=?";
		executeUpdate(sql, StatusConstants.INVALID,tariffId);
	}
	
	/**
	 * 查找VOD按次点播产品资费
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public PProdTariff queryVodProdTariff(String countyId) throws JDBCException {
		String sql = "select p.* from p_prod_tariff p,p_prod_tariff_county pc where p.tariff_id=pc.tariff_id and  p.billing_type=?";
		return createQuery(PProdTariff.class,sql, SystemConstants.BILLING_TYPE_JC).first();
	}

	/**
	 * 查询资费信息
	 * @param acctId
	 * @param acctItemId
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public PProdTariff queryTariffByAcctId(String acctId, String acctItemId, String countyId) throws JDBCException {
		String sql = "select ppt.* from c_prod cp,p_prod_tariff ppt"
			+" where cp.tariff_id=ppt.tariff_id"
			+" and cp.acct_id=? and cp.prod_id=?"
			+" and cp.county_id=?";
		return this.createQuery(sql, acctId, acctItemId, countyId).first();
	}
}
