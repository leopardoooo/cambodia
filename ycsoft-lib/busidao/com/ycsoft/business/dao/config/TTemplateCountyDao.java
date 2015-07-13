/**
 * TTemplateCountyDao.java	2010/02/25
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TTemplateCounty;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TTemplateCountyDao -> T_TEMPLATE_COUNTY table's operator
 */
@Component
public class TTemplateCountyDao extends BaseEntityDao<TTemplateCounty> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7031708470459837603L;

	/**
	 * default empty constructor
	 */
	public TTemplateCountyDao() {}

	public List<TTemplateCounty> queryTemplateCounty(String template_id) throws JDBCException{
		return this.createQuery("select * from T_TEMPLATE_COUNTY where template_id=? ", template_id).list();
	}
	
	public List<TTemplateCounty> queryCountyByIds(String[] templateIds) throws JDBCException{
		return this.createQuery("select * from T_TEMPLATE_COUNTY where template_id in (" +sqlGenerator.in(templateIds)+")").list();
	}
	
	public String getTemplateIdByCounty(String county_id,String template_type) throws JDBCException{
		String sql = " select template_id from T_TEMPLATE_COUNTY where county_id=? and template_type=?";
		Object result = findUnique(sql, county_id,template_type);

		return result == null?"":result.toString();
	}
	/**
	* @Description: 新增模板与地市关系，进行验证
	* @author  wqy
	* @param template_id
	* @param county
	* @throws Exception
	* @return boolean
	*/
	public Object isIn(String template_id,String county) throws JDBCException{
		String sql = " select * from t_template a ,t_template_county b where a.template_type=b.template_type and a.template_id= ? and b.county_id= ?   ";
		Object request = findUnique( sql ,template_id,county ) == null ? false : true;
		 if( request.equals(true)){
			 request = "县市与模板类型组合已经存在";
		 }
		return request;
	}

	/**
	* @Description: 修改模板与地市关系，进行验证
	* @author  wqy
	* @param template_id
	* @param county
	* @param template_type
	* @throws Exception
	* @return boolean
	*/
	public Object isupdate(String template_id,String county,String template_type) throws JDBCException{
		String templatetype  = new String(findUnique( "  select template_type from t_template_county where  template_id = ? and county_id = ? ",template_id,county).toString());

		Object request = templatetype.equals(template_type) ? false : true;
		 if( request.equals(true)){
			 request = "县市与模板类型组合已经存在";
		 }
		return request;
	}
	/**
	* @Description: 根据模板编号获取模板类型号
	* @author  wqy
	* @param template_id
	* @throws Exception
	* @return String
	*/
	public String getTemplate_type(String template_id )throws Exception{
		return new String(findUnique( "  select template_type from t_template where  template_id = ? ",template_id).toString());
	}
	/**
	* @Description: 根据地市编号，模板类型，对模板编号进行更改
	* @author  wqy
	* @param county
	* @param template_type
	* @param template_id
	* @throws JDBCException
	* @return void
	*/
	public void updatetemplate(String county,String template_type,
			String template_id) throws JDBCException{
		String sql = " UPDATE T_TEMPLATE_COUNTY  SET   template_id = ?   WHERE template_type= ? and county_id = ? ";
		executeUpdate(sql, template_id, template_type,county);
	}

	public void logoffTC(String template_id,String template_type,
			String county_id) throws JDBCException{
		String sql = " DELETE T_TEMPLATE_COUNTY   WHERE template_id = ? and  template_type= ? and   county_id = ? ";
		executeUpdate(sql,template_id,template_type, county_id);
	}
}
