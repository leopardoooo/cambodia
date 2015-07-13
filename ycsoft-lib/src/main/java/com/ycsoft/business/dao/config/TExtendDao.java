/**
 * TExtendDao.java	2010/03/08
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TExtend;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * TExtendDao -> T_EXTEND table's operator
 */
@Component
public class TExtendDao extends BaseEntityDao<TExtend> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5024918873001949447L;

	/**
	 * default empty constructor
	 */
	public TExtendDao() {}

	/**
	 * 查询扩展属性菜单树
	 * @return
	 * @throws JDBCException
	 */
	public List<TreeDto> queryExtensionTree() throws JDBCException{
		String sql = "select * from (select nvl(extend_id,b.busi_code) id,'-1' pid,nvl(e.extend_name,b.busi_name)  text , b.busi_code attr "
				+ " from t_extend  e,t_busi_code b "
				+ " where e.busi_code(+)=b.busi_code "
				+ " union all "
				+ " select e.extend_id  id,'-2' pid ,e.extend_name text,e.extend_table attr from t_extend  e where e.busi_code is null "
				+ " union all "
//				+ " select g.extend_id||'_'||g.group_id id,g.extend_id pid,g.group_name text,'T' attr from t_extend e,t_extend_group g "
//				+ " where e.extend_id=g.extend_id "
//				+ " union all "
				+ " select '-1' id ,'0' pid ,'业务扩展' text,'BUSI' url from dual "
				+ " union all "
				+ " select '-2' id ,'0' pid ,'信息扩展' text,'TABLE' url from dual) "
				+ " start with pid = '0' connect by prior id = pid";
		return createQuery(TreeDto.class, sql).list();
	}

	/**
	 * 查找以”EXT“开头的表
	 * @return
	 * @throws JDBCException
	 */
	public List<TExtend> findExtTable() throws JDBCException{
		String sql = "select table_name extend_table from user_tables where table_name != 'EXT_C_DONE_CODE' and table_name like 'EXT%'";
		return this.createQuery( sql).list();
	}
	/**
	 * 根据扩展ID得到扩展对象
	 * @param extend_id 扩展ID
	 * @return
	 * @throws JDBCException
	 */
	public TExtend getByExtend_id(String extend_id) throws JDBCException{
		String SQL = "select * from t_extend where extend_id = ?";
		return super.findEntity(SQL, extend_id);
	}

	/**
	 * 根据扩展类型和对应的值找到扩展对象
	 * @param extendType 扩展类型
	 * @param extendValue 扩展类型对应的值
	 * @return
	 * @throws JDBCException
	 */
	public TExtend findByExtendName(String extendType,String extendValue) throws JDBCException{
		String sql = "";
		if(extendType.equals(SystemConstants.EXDT_ATTR_TYPE_TAB)){
			sql = "select * from t_extend te where te.extend_table=?";
		}else if(extendType.equals(SystemConstants.EXT_ATTR_TYPE_BUSI)){
			sql = "select * from t_extend te where te.busi_code=?";
		}
		return createQuery(TExtend.class, sql, extendValue).first();
	}

	/**
	 * //从user_tab_comments中获取表的描述
	 * @param extendTable 扩展表名
	 * @return
	 * @throws JDBCException
	 */
	public TExtend findExtendName(String extendTable) throws JDBCException{
		String sql = "select utc.comments extend_name from user_tab_comments utc where utc.table_name=?";
		return createQuery(TExtend.class, sql, extendTable).first();
	}
}
