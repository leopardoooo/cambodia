package com.ycsoft.business.dao.prod;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PResgroup;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.prod.ResGroupDto;


	@Component
	public class PResgroupDao extends BaseEntityDao<PResgroup> {
		/**
		 * @Description:
		 * @date Jul 22, 2010 11:13:54 AM
		 */
		private static final long serialVersionUID = 5457269864435917018L;


		public PResgroupDao() {}
		public List<ResGroupDto> queryGroupRes() throws JDBCException {
			String sql = "select * from p_resgroup  ";
			return this.createQuery(ResGroupDto.class, sql).list();
		}
		public List<ResGroupDto> queryGroupResByServId(String ServId,String countyId) throws JDBCException {
			String sql = StringHelper.append("select s.*,(select count(1) from p_resgroup_res p where p.group_id = s.group_id",
					"  group by p.group_id) max_count  from p_resgroup s where s.serv_id=?");
			if(StringHelper.isNotEmpty(countyId)){
				sql = StringHelper.append(sql," and (s.county_id='",countyId,"' or s.county_id='",SystemConstants.COUNTY_ALL,"')");
			}
			return this.createQuery(ResGroupDto.class, sql,ServId).list();
		}

		public int checkGroupRes(String groupId)throws JDBCException{
			String sql = "select count(*) from p_resgroup t ,p_resgroup_res t1 where t.group_id=t1.group_id and t.group_id= ? ";
			return  count(sql, groupId);
			//			return  count(sql, groupId) > 0 ? true : false ;

		}

		public Pager<ResGroupDto> query( String keyword ,String countyId,Integer start,Integer limit)throws Exception{
			String cond = "";
			String sql = " select t1.*, case when (exists (SELECT 1 FROM P_PROD_DYN_RES D where d.group_id=t1.group_id)) then 'F' ELSE 'T' END ALLOW_UPDATE from p_resgroup t1 where 1=1 ";
				if(StringUtils.isNotEmpty(keyword)){
					cond += " and (t1.group_name like '%"+keyword+"%') ";
			    }
			    if(!SystemConstants.COUNTY_ALL.equals(countyId)){
			       cond +=" and  (t1.county_id='"+countyId+"' or t1.county_id='"+SystemConstants.COUNTY_ALL+"')";
				 }
			    sql=sql+cond;
			return createQuery(ResGroupDto.class,sql ).setStart(start).setLimit(limit).page();
		}
		
		/**
		 * 查询动态资源组是否还被产品使用
		 * @param groupId
		 * @return
		 * @throws JDBCException
		 */
		public ResGroupDto queryResGroupById(String groupId) throws JDBCException{
			String sql = StringHelper.append("select distinct t1.prod_name,t3.group_name from p_prod t1,p_prod_dyn_res t2,p_resgroup t3",
					" where t1.prod_id=t2.prod_id and t2.group_id=t3.group_id and t3.group_id=?");
			return createQuery(ResGroupDto.class,sql, groupId).first();
		}
		
}
