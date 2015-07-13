package com.ycsoft.business.dao.prod;

/**
 * PPromFeeProdDao.java	2012/06/28
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PPromFeeProd;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.business.dto.core.prod.PromFeeProdDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.prod.ResGroupDto;


/**
 * PPromFeeProdDao -> P_PROM_FEE_PROD table's operator
 */
@Component
public class PPromFeeProdDao extends BaseEntityDao<PPromFeeProd> {
	
	/**
	 * default empty constructor
	 */
	public PPromFeeProdDao() {}

	public List<PromFeeProdDto> queryPromFeeProds(String promFeeId) throws JDBCException {
		String sql = StringHelper.append("select * from ( select t.prom_fee_id,t2.is_base, t.user_no, t.prod_id, t.tariff_id, t.real_pay/100 real_pay, t.months, t.force_select," +
				"t2.prod_name,t3.tariff_name,(t3.rent * t.months / t3.billing_cycle)/100 should_pay ,t3.rent,t.bind_prod_id,(select pp1.prod_name from p_prod pp1 where pp1.prod_id=t.bind_prod_id) bind_prod_name" +
				" ,t2.serv_id from p_prom_fee_prod t,p_prod t2,p_prod_tariff t3 ",
			"where t.prod_id=t2.prod_id and t.tariff_id=t3.tariff_id(+) and t.prom_fee_id=?  ",
			" union all ",
			" select t.prom_fee_id,'F' is_base,t.user_no,t.prod_id,t.tariff_id, t.real_pay/100 real_pay, t.months, t.force_select, ",
			" '宽带自动匹配' prod_name,'宽带自动匹配' tariff_name,0 should_pay,0 rent,null,null ,'BAND' ",
			" from p_prom_fee_prod t where t.prom_fee_id=? and t.prod_id='BAND' ) order by force_select DESC");
			 return createQuery(PromFeeProdDto.class,sql, promFeeId,promFeeId).list();
	}

	public void deleteByUserNo(String promFeeId, String userNo) throws JDBCException {
		String sql = " delete from p_prom_fee_prod p where p.prom_fee_id= ? and p.user_no=?";
		this.executeUpdate(sql, promFeeId,userNo);
	}

	public List<PProdTariff> queryAllTariff(String promFeeId, String prodId) throws JDBCException {
		String sql = StringHelper.append("select DISTINCT t.tariff_id,t.tariff_name || '('||(nvl(tr.rule_name,'无规则'))||')' tariff_name,t.rent ",
				" from p_prod_tariff t,p_prod_tariff_county pt,t_rule_define tr ,P_PROM_FEE_COUNTY PF where ",
				" t.rule_id=tr.rule_id(+)   AND PT.COUNTY_ID=PF.COUNTY_ID  and t.tariff_id=pt.tariff_id",
				"  and t.status = ? and PF.PROM_FEE_ID=? and t.prod_id=?");
		return createQuery(PProdTariff.class, sql,StatusConstants.ACTIVE, promFeeId,prodId).list();
	}

	public List<PProd> queryAllTariff(String promFeeId) throws JDBCException {
		String sql = "select DISTINCT p.prod_id,p.prod_name from p_prod p,p_prod_county pc,p_prom_fee_county pfc " +
			" where p.prod_id=pc.prod_id and pc.county_id=pfc.county_id and pfc.prom_fee_id=? and p.status = ?  and p.prod_type<>'CPKG'   ";
//		and nvl(p.serv_id,'NULL')<>'BAND'
		return createQuery(PProd.class, sql, promFeeId,StatusConstants.ACTIVE).list();
	}

	public List<ResGroupDto> queryGroupByProdIds(String[] ProdIds) throws Exception {
		String sql = "select pd.*,pr.group_name from p_prod_dyn_res pd,p_resgroup pr where  pd.group_id = pr.group_id and pd.prod_id in("
				+ getSqlGenerator().in(ProdIds) + ")";
		return this.createQuery(ResGroupDto.class, sql).list();
	}
	
	public List<PRes> queryResByGroupId(String[] resGroupId) throws Exception {
		String sql = "select pr.*,prgr.group_id from p_res pr,p_resgroup_res prgr "
				+ " where pr.res_id= prgr.res_id and prgr.group_id  in("
				+ getSqlGenerator().in(resGroupId) + ")";
		return this.createQuery(PRes.class, sql).list();
	}
	
}
