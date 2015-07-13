/**
 * PDictProdDao.java	2010/07/06
 */

package com.ycsoft.business.dao.prod;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PDictProd;
import com.ycsoft.business.dto.core.prod.ProdDictDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.prod.PProdDictDto;


/**
 * PDictProdDao -> P_DICT_PROD table's operator
 */
@Component
public class PDictProdDao extends BaseEntityDao<PDictProd> {

	/**
	 *
	 */
	private static final long serialVersionUID = -217946045345700252L;

	/**
	 * default empty constructor
	 */
	public PDictProdDao() {}

	public List<ProdDictDto> queryAll(String userType,String areaId) throws Exception{
		List<ProdDictDto>  prodList = null;
		String sql = " select * from ( select 'N'||pd.node_pid node_pid, 'N'||pd.node_id node_id, pd.node_name,'F' is_leaf  " +
				" from p_prod_dict pd" +
				" union all " +
				" select 'N'||d.node_id node_pid , d.prod_id node_id,p.prod_name node_name,'T' is_leaf " +
				" from p_dict_prod d,p_prod p where  p.prod_id=d.prod_id and (for_area_id=:ALL or for_area_id =:areaId)  and user_type=:userType and status=:status " +
				" )" +
				" start with node_pid = 'N-1' connect by prior node_id = node_pid order by level";

		Map<String, Serializable> paramer = new HashMap<String, Serializable>();
		paramer.put("ALL", SystemConstants.AREA_ALL);
		paramer.put("areaId", areaId);
		paramer.put("userType", userType);
		paramer.put("status", StatusConstants.ACTIVE);

		prodList = this.createNameQuery(ProdDictDto.class ,sql,paramer).list();
		return prodList;
	}

	/**
	 * @param prodIds
	 * @param area_id
	 * @return
	 */
	public List<ProdDictDto> queryProdDict(String[] prodIds, String areaId,String countyId) throws JDBCException {
		List<ProdDictDto>  prodList = null;
		String sql = " select * from ( select 'N'||pd.node_id node_id,'N'||pd.node_pid node_pid,  pd.node_name,'F' is_leaf  " +
				" from p_prod_dict pd  ,p_prod_dict_county pdc where pdc.node_id=pd.node_id and pdc.county_id =:countyId " +
				" union all " +
				" select distinct p.prod_id node_id,'N'||NVL(D.NODE_ID,'-1') node_pid , p.prod_name node_name,'T' is_leaf " +
				" from p_dict_prod d,p_prod p where  p.prod_id=d.prod_id(+) and (for_area_id=:ALL or for_area_id =:areaId)  " +
				" and p.prod_id in ("+getSqlGenerator().in(prodIds)+") and status=:status " +
				" )" +
				" start with node_pid = 'N-1' connect by prior node_id = node_pid order by level";

		Map<String, Serializable> paramer = new HashMap<String, Serializable>();
		paramer.put("countyId", countyId);
		paramer.put("ALL", SystemConstants.AREA_ALL);
		paramer.put("areaId", areaId);
		paramer.put("status", StatusConstants.ACTIVE);

		prodList = this.createNameQuery(ProdDictDto.class ,sql,paramer).list();
		return prodList;
	}
	public List<PDictProd> queryProdDict(String nodeId) throws JDBCException {
		String sql = " select d.*,p.prod_name from P_DICT_PROD d,p_prod p where node_id = ? and p.prod_id= d.prod_id ";
		return createQuery(PDictProd.class ,sql,nodeId).list();
	}
	public List<PProdDictDto> getNodeAll() throws Exception{
		String sql = "  select *  from  p_prod_dict t start with t.node_pid = '-1'   connect by prior t.node_id = t.node_pid order by level ";
		return createQuery(PProdDictDto.class,sql).list();
	}

	public List<PProdDictDto> getProdNodeByProdId(String prodId) throws Exception{
		String sql = " select t1.* from p_prod_dict t1 ,p_dict_prod t2 where t2.node_id=t1.node_id and  t2.prod_id= ? ";
		return createQuery(PProdDictDto.class,sql,prodId).list();
	}

	public void addDictProd (String [] nodeId, String prodId) throws Exception {
		String	sql = "insert into p_dict_prod(node_id, prod_id) values (?, '"+prodId+"')";
		 executeBatch(sql, nodeId);
	}
	public void deleteDictProd (String prodId) throws Exception {
		String	sql = "delete p_dict_prod where prod_id = ? ";
		executeUpdate(sql, prodId);
	}
}
