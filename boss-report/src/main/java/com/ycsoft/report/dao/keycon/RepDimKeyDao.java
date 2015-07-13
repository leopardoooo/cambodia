/**
 * RepDimKeyDao.java	2010/07/16
 */
 
package com.ycsoft.report.dao.keycon; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepColumn;
import com.ycsoft.report.bean.RepDimKey;
import com.ycsoft.report.commons.ReportConstants;


/**
 * RepDimKeyDao -> REP_DIM_KEY table's operator
 */
@Component
public class RepDimKeyDao extends BaseEntityDao<RepDimKey> {
	
	/**
	 * default empty constructor
	 */
	public RepDimKeyDao() {}
	
	public List<RepDimKey> findAllByTreeOrder() throws JDBCException{
		return this.findList("select * from rep_dim_key start with fkey is null  connect by prior key= fkey");
	}

	/**
	 * 获取统计报表的粒度选择范围
	 * @param repcols
	 * @return
	 * @throws JDBCException
	 */
	public Map<String,List<RepDimKey>> getTotalDimMap(List<RepColumn> repcols) throws JDBCException{
		if(repcols==null) return null;
		Map<String,List<RepDimKey>> dimkeymap=new HashMap<String,List<RepDimKey>>();
		for(RepColumn col:repcols){
			if(ReportConstants.OLAP_TYPE_EXTEND.equals(col.getOlap_type())){
			String sql="select t.key,t.name from rep_dim_key t start with t.key='"+
				(col.getDim_key_select()==null||col.getDim_key_select().equals("")?col.getDim_key() :col.getDim_key_select()
						)+"' connect by prior t.fkey= t.key ";
			dimkeymap.put(col.getCol_index().toString(), this.findList(sql));
			}
		}
		return dimkeymap;
	}
}