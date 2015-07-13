package com.ycsoft.business.dao.config;

/**
 * TCountyAcctDao.java	2012/04/24
 */
 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TCountyAcct;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * TCountyAcctDao -> T_COUNTY_ACCT table's operator
 */
@Component
public class TCountyAcctDao extends BaseEntityDao<TCountyAcct> {
	
	/**
	 * default empty constructor
	 */
	public TCountyAcctDao() {}

	public Pager<TCountyAcct> queryAcctConfig(String countyId,String colony, Integer start, Integer limit) throws JDBCException {
		String sql = "select * from t_county_acct t where 1=1 ";
		if(StringHelper.isNotEmpty(countyId)){
			sql = StringHelper.append(sql," and t.county_id='",countyId,"'");
		}
		if(StringHelper.isNotEmpty(colony)){
			sql = StringHelper.append(sql," and t.cust_colony='",colony,"'");
		}
		sql = StringHelper.append(sql," order by t.create_time, t.config_year,t.county_id desc");
		return createQuery(sql).setStart(start).setLimit(limit).page();
	}

	public List<TCountyAcct> queryAcctConfigForAdd(String countyId,String colony) throws JDBCException {
		String sql = null;
		if(StringHelper.isNotEmpty(countyId)){
			sql = StringHelper.append("select distinct '", countyId,"' county_id, s.item_value CUST_COLONY, 0 initamount,0 balance ,",
					" to_char(sysdate,'yyyy') config_year from s_itemvalue s where s.item_key=? AND NOT EXISTS",
					" (SELECT 1 FROM T_COUNTY_ACCT T WHERE T.COUNTY_ID=? AND T.CUST_COLONY=S.ITEM_VALUE)");
			if(StringHelper.isNotEmpty(colony)){
				sql = StringHelper.append(sql, " and s.item_value='",colony,"'");
			}
			
			return createQuery(TCountyAcct.class,sql,DictKey.CUST_COLONY.toString(),countyId).list();
		}else{
			sql = StringHelper.append("select s.county_id, ? CUST_COLONY, 0 initamount,0 balance ,",
					" to_char(sysdate,'yyyy') config_year from s_county s where  NOT EXISTS ",
					" (SELECT 1 FROM T_COUNTY_ACCT T WHERE T.COUNTY_ID=s.county_id AND T.CUST_COLONY=?)");
			return createQuery(TCountyAcct.class,sql,colony,colony).list();
		}
	}

	/**
	 * 删除定额账户配置
	 * @param acctIds
	 * @param optrId
	 * @throws JDBCException
	 */
	public void deleteAcctConfig(String[] acctIds, String optrId) throws JDBCException {
		String sql = StringHelper.append("insert into t_county_acct_his ",
				" ( select t.*,sysdate done_date,'",optrId,"' done_optr from t_county_acct t where",
				getSqlGenerator().setWhereInArray("t_acct_id",acctIds),")");
		
		executeUpdate(sql);
		
		sql = StringHelper.append(" delete from t_county_acct where ",getSqlGenerator().setWhereInArray("t_acct_id",acctIds));
		executeUpdate(sql);
	}

	public TCountyAcct queryAcctConfig(String countyId, String custColony) throws JDBCException {
		String sql = "select * from t_county_acct t where t.county_id = ? and t.cust_colony=? and config_year=?";
		return createQuery(sql, countyId,custColony,DateHelper.getCurrYear()).first();
	}

}
