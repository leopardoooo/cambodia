/**
 * PProdDictDao.java	2012/03/06
 */
 
package com.ycsoft.business.dao.prod; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdDict;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.prod.PProdDictDto;



/**
 * PProdDictDao -> P_PROD_DICT table's operator
 */
@Component
public class PProdDictDao extends BaseEntityDao<PProdDict> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1105603258827908349L;

	/**
	 * default empty constructor
	 */
	public PProdDictDao() {}
	public List<PProdDictDto> queryProdDictByCountyId(String countyId) throws JDBCException{
		String sql = StringHelper.append("select level,t.* from (select pd.* from P_PROD_DICT pd,p_prod_dict_county pp " +
				"where pd.node_id=pp.node_id and pp.county_id= ? ) t start with t.node_pid = '-1'  " +
				"connect by prior t.node_id=t.node_pid order by level ");
		return createQuery(PProdDictDto.class, sql,countyId).list();
	}
	
	public List<PProdDictDto> queryProdDictAll() throws JDBCException{
		String sql = StringHelper.append("select level,t.* from P_PROD_DICT t start with t.node_pid = '-1'  connect by prior t.node_id=t.node_pid order by level");
		return createQuery(PProdDictDto.class, sql).list();
	}
	public void deleteProdDict (String nodeId) throws Exception {
		String	sql = "delete P_PROD_DICT where node_id = ? ";
		executeUpdate(sql, nodeId);
	}
	public List<PProdDict> queryDictByPid(String nodePid) throws JDBCException{
		String sql = StringHelper.append("select * from P_PROD_DICT where node_pid = ? ");
		return createQuery(PProdDict.class, sql,nodePid).list();
	}

}
