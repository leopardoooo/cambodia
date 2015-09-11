package com.ycsoft.business.dao.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdStaticRes;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.prod.ProdCountyResDto;
@Component
public class PProdStaticResDao extends BaseEntityDao<PProdStaticRes> {

	/**
	 * @Description:
	 * @date Jul 26, 2010 3:32:25 PM
	 */
	private static final long serialVersionUID = 6418745555748578784L;

		public PProdStaticResDao() {}

		public List<ProdCountyResDto> queryStaticResByprodId(String prodId) throws JDBCException {
			String sql = " select  ? county_id,t2.res_name,t2.res_id from   p_prod_static_res t1,p_res t2 where t1.res_id = t2.res_id and t1.prod_id = ? " +
					"union all " +
					"select t1.county_id county_id,t2.res_name,t2.res_id from   P_PROD_COUNTY_RES t1,p_res t2 where t1.res_id = t2.res_id and t1.prod_id = ?  ";
			return this.createQuery(ProdCountyResDto.class, sql,SystemConstants.COUNTY_ALL,prodId,prodId).list();
		}
		public void deleteStatic (String prodId,List<String> list) throws Exception {
			String	sql = "delete p_prod_static_res where prod_id = '"+prodId+"' and res_id = ? ";
			executeBatch(sql,list.toArray());
		}
		
		public List<PProdStaticRes> queryBaseProdRes() throws JDBCException{
			String sql = "select r.res_id from p_prod t,p_prod_static_res r where t.is_base =? and t.prod_id=r.prod_id";
			return createQuery(sql,SystemConstants.BOOLEAN_TRUE).list();
		}
		
		public String[] queryResByProdIds(List<String> prodIds) throws JDBCException{
			String prodIdStr ="";
			for (String prodId:prodIds){
				prodIdStr = ",'"+prodId+"'";
			}
			
			prodIdStr = prodIdStr.substring(1);
			
			String sql = "select distinct b.external_res_id res_id from p_prod_static_res a,t_server_res b "
					+ " where a.res_id=b.boss_res_id and a.prod_id in ("+prodIdStr+")";
			
			List<PProdStaticRes> list = this.createQuery(sql).list();
			String[] resIds = new String[list.size()];
			int i=0;
			for(PProdStaticRes res:list){
				resIds[i] = res.getRes_id();
				i++;
			}
			
			return resIds;
		}
}
