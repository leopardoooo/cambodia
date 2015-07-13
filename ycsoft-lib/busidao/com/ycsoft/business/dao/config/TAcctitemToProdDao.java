package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAcctitemToProd;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.config.VewAcctitemDto;



/**
 * TAcctitemToProdDao -> T_ACCTITEM_TO_PROD table's operator
 */
@Component
public class TAcctitemToProdDao extends BaseEntityDao<TAcctitemToProd> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8083901804305178299L;

	/**
	 * default empty constructor
	 */
	public TAcctitemToProdDao() {}

	public Pager<VewAcctitemDto> queryAllVewAcctitem(Integer start,Integer limit,String key) throws Exception{
		String sql = "select * from t_public_acctitem";
		if(StringHelper.isNotEmpty(key)){
			sql += " where (acctitem_id = '"+key+"' or acctitem_name like ('%"+key+"%') )";
		}
		return this.createQuery(VewAcctitemDto.class, sql).setStart(start).setLimit(limit).page();
	}

	/**
	 * 根据账目ID查询
	 * @param acctItemId
	 * @return
	 * @throws Exception
	 */
	public List<TAcctitemToProd> queryById(String acctItemId) throws Exception{
		String sql = "select t.*,p.prod_name from t_acctitem_to_prod t,p_prod p where t.prod_id=p.prod_id and t.acctitem_id=?";
		return createQuery(TAcctitemToProd.class, sql, acctItemId).list();
	}

	/**
	 * 根据账目ID删除
	 * @param acctItemId
	 * @throws Exception
	 */
	public void deleteById(String acctItemId) throws Exception{
		String sql = "delete from t_acctitem_to_prod t where t.acctitem_id=?";
		executeUpdate(sql, acctItemId);
	}
}
