/**
 * RepKeyConDao.java	2010/06/21
 */
 
package com.ycsoft.report.dao.keycon; 

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepKeyCon;
import com.ycsoft.report.commons.tree.RepTreeBuilder;
import com.ycsoft.report.dto.RepKeyDto;


/**
 * RepKeyConDao -> REP_KEY_CON table's operator
 */
@Component
public class RepKeyConDao extends BaseEntityDao<RepKeyCon> {
	
	/**
	 * default empty constructor
	 */
	public RepKeyConDao() {}
	
	public List<RepKeyCon> findAllByOrder() throws JDBCException{
		
		return this.findList("select * from rep_key_con t order by htmlorder,t.key");
	}
	/**
	 * 获取自定义的数据权限
	 * @return
	 * @throws JDBCException
	 */
	public List<String> findroledatarighttypes()throws JDBCException{
		String sql="select r1.key from s_role t1,rep_key_con r1 " +
				"where t1.data_right_type=r1.key and t1.rule_id is not null ";
		List<RepKeyCon> list= this.findList(sql);
		List<String> datalist=new ArrayList<String>();
		for(RepKeyCon con:list)
			datalist.add(con.getKey());
		return datalist;
	}
	/**
	 * 
	 * @param key
	 * @param repkeys
	 * @return
	 * @throws JDBCException
	 */
	public List<RepKeyCon> findTree() throws JDBCException{
		
//		String sql=" select t.*,level from rep_key_con t   start with fkey is null connect by prior key= fkey ";
//		return this.findList(sql);
		String sql="select * from rep_key_con t order by t.htmlorder";
		List<RepKeyCon> list=this.findList(sql);
		for(RepKeyCon o:list){
			if(o.getFkey()!=null&&o.getFkey().trim().equals(""))
				o.setFkey(null);
		}
		return RepTreeBuilder.orderByTree(list, null);
	}

}
