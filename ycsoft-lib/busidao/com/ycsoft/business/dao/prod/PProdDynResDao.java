package com.ycsoft.business.dao.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdDynRes;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.prod.ResGroupDto;

@Component

public class PProdDynResDao extends BaseEntityDao<PProdDynRes> {

	/**
	 * @Description:
	 * @date Jul 26, 2010 3:30:57 PM
	 */
	private static final long serialVersionUID = 5124097721015359109L;

	public PProdDynResDao() {}

	public List<ResGroupDto> queryDynByProdId(String prodId) throws JDBCException {
		String sql = "select * from p_prod_dyn_res t1,p_resgroup t2 where t1.group_id =t2.group_id  and t1.prod_id = ?  ";
		return this.createQuery(ResGroupDto.class, sql,prodId).list();
	}
	public void deleteByAll (List<Object[]> list) throws Exception {
		String	sql = "delete p_prod_dyn_res where group_id = ? and prod_id = ? and res_number = ? ";
		executeBatch(sql, list);
	}
}
