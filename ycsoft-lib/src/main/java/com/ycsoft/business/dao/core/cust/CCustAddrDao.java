package com.ycsoft.business.dao.core.cust;

import java.util.List;

/**
 * CCustAddrDao.java	2010/10/13
 */




import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCustAddr;
import com.ycsoft.beans.core.cust.CCustAddrNote;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * CCustAddrDao -> C_CUST_ADDR table's operator
 */
@Component
public class CCustAddrDao extends BaseEntityDao<CCustAddr> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7590084152908325875L;

	/**
	 * default empty constructor
	 */
	public CCustAddrDao() {}
	
	public Pager<CCustAddrNote> queryNoteCust(String addrId, Integer start, Integer limit) throws JDBCException{
		String sql=StringHelper.append(
				" select note,cust_name,cust_no, ",
				" case when user_cnt=0 then 'NOUSER' ",
				"      when user_cnt=install_cnt then 'INSTALL' ",
				"      when user_cnt=reqstop_cnt then 'REQSTOP' ",
				"      when order_cnt=0 then 'NOORDER' ",
				" when order_cnt=order_stop_cnt then 'FORSTOP' ",
				" else 'ACTIVE' end note_status_type ",
				" from ( ",
				" select  a.note,c.cust_name,c.cust_no, ",
				" count(distinct cu.user_id) user_cnt, ",
				" count(distinct case when cu.status='INSTALL' then cu.user_id end) install_cnt, ",
				" count(distinct case when cu.status='REQSTOP' then cu.user_id end) reqstop_cnt, ",
				" count(distinct cp.order_sn) order_cnt, ",
				" count(distinct case when cp.status in ('FORSTOP','LINKSTOP','OWESTOP') then cp.order_sn end) order_stop_cnt ",
				" from c_cust c  ",
				" join c_cust_addr a on a.cust_id=c.cust_id  ",
				" left join c_user cu on cu.cust_id=c.cust_id ",
				" left join c_prod_order cp on cu.user_id=cp.user_id ",
				" where c.addr_id=? ",
				" group by  a.note,c.cust_name,c.cust_no ",
				" ) order by note "	);
		return this.createQuery(CCustAddrNote.class, sql, addrId).setStart(start).setLimit(limit).page();
	}

}
