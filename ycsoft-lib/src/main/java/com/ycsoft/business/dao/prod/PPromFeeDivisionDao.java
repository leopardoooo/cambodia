package com.ycsoft.business.dao.prod;

/**
 * PPromFeeDivisionDao.java	2012/08/06
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromFeeDivision;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * PPromFeeDivisionDao -> P_PROM_FEE_DIVISION table's operator
 */
@Component
public class PPromFeeDivisionDao extends BaseEntityDao<PPromFeeDivision> {
	
	/**
	 * default empty constructor
	 */
	public PPromFeeDivisionDao() {}

	public List<PPromFeeDivision> queryPromFeeDivision(String promFeeId) throws JDBCException {
		String sql = StringHelper.append("select b.*, case when b.prod_id='BAND' then '宽带自动匹配' else pp.prod_name end prod_name,pt.tariff_name,pd.percent_value ",
		"  from (select a2.*, a1.* from (select item_value type",
		" from s_itemvalue si where si.item_key = ?) a1,",
		" (select distinct pa.prom_fee_id,pa.user_no,pa.prod_id,pa.tariff_id,pa.real_pay ",
		" from p_prom_fee_prod pa where  pa.prom_fee_id=?) a2) b ,",
		" p_prom_fee_division pd,p_prod pp,p_prod_tariff pt",
		" where b.prod_id=pp.prod_id(+) and b.tariff_id=pt.tariff_id(+) and pd.prom_fee_id(+)=b.prom_fee_id",
		" and b.user_no=pd.user_no(+) and b.prod_id=pd.prod_id(+) and b.type = pd.type(+)");
		return createQuery(sql,DictKey.SEPARATE_TYPE.toString(),promFeeId).list();
	}

	/**
	 * 套餐产品发生变化时，根据promFeeId，删除分成不存在的产品和实际缴费金额为0 的分成
	 * @param promFeeId
	 * @throws JDBCException 
	 */
	public void changeDivisionRecrods(String promFeeId) throws JDBCException {
		String sql = StringHelper.append("delete from p_prom_fee_division p where (not exists (select 1 from p_prom_fee_prod pf ",
				" where pf.prom_fee_id=p.prom_fee_id and p.user_no=pf.user_no and p.prod_id=pf.prod_id) ",
				" or exists (select 1 from p_prom_fee_prod pf where pf.prom_fee_id=p.prom_fee_id ",
				" and p.user_no=pf.user_no and p.prod_id=pf.prod_id and pf.real_pay=0)) and p.prom_fee_id=?");
		this.executeUpdate(sql, promFeeId);
	}

	/**
	 * 根据id删除原有数据
	 * @param promFeeId
	 * @throws JDBCException
	 */
	public void deleteByPromFeeId(String promFeeId) throws JDBCException {
		String sql = "delete from p_prom_fee_division p where p.prom_fee_id=?";
		this.executeUpdate(sql, promFeeId);
	}

}
