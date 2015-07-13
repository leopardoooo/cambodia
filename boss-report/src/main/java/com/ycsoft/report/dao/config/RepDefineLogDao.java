/**
 * RepDefineDetailDao.java	2010/08/13
 */

package com.ycsoft.report.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.report.bean.RepDefineLog;

/**
 * RepDefineDetailDao -> REP_DEFINE_DETAIL table's operator
 */
@Component
public class RepDefineLogDao extends BaseEntityDao<RepDefineLog> {

	/**
	 * default empty constructor
	 */
	public RepDefineLogDao() {
	}


	/**
	 * 查询该操作员具有权限的日志
	 * 
	 * @param optr_id
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException
	 */
	public Pager<RepDefineLog> queryLog(String optr_id,String rep_id, Integer start,
			Integer limit) throws JDBCException {
		if(StringHelper.isEmpty(rep_id)){
		String sql = StringHelper
				.append(
						"select log.rep_id,log.rep_name,log.optr_login_name,log.create_date,",
						" decode(log.update_type,'UPDATE','更新','创建') update_type,log.remark",
						" from (select res.res_id",
						" from  s_optr_role r,rep_define d,s_role_resource r_res,s_resource res",
						"  where  r.role_id=r_res.role_id and r_res.res_id=res.res_id and res.res_id=d.rep_id",
						"  and    r.optr_id=? ",
						"  union ",
						"  select res.res_id from  rep_define d,s_optr_resource o,s_resource res ",
						"  where  res.res_id=o.res_id and res.res_id=d.rep_id ",
						" and o.optr_id=? ",
						") a,rep_define_log log",
						"  where a.res_id=log.rep_id",
						"  order by log.create_date desc");
				return createQuery(sql,optr_id,optr_id).setStart(start).setLimit(limit).page();
		}else{
			String sql = StringHelper
			.append(
					"select log.rep_id,log.rep_name,log.optr_login_name,log.create_date,",
					" decode(log.update_type,'UPDATE','更新','创建') update_type,log.remark",
					" from (select res.res_id",
					" from  s_optr_role r,rep_define d,s_role_resource r_res,s_resource res",
					"  where  r.role_id=r_res.role_id and r_res.res_id=res.res_id and res.res_id=d.rep_id",
					"  and    r.optr_id=? and d.rep_id=?",
					"  union ",
					"  select res.res_id from  rep_define d,s_optr_resource o,s_resource res ",
					"  where  res.res_id=o.res_id and res.res_id=d.rep_id  and o.optr_id=? and d.rep_id=?) a,rep_define_log log",
					"  where a.res_id=log.rep_id",
					"  order by log.create_date desc");
			return createQuery(sql,optr_id,rep_id,optr_id,rep_id).setStart(start).setLimit(limit).page();
		}
	}
}
