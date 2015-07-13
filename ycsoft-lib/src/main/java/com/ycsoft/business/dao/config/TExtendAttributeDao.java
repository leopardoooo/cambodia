/**
 * TExtendAttributeDao.java	2010/03/08
 */

package com.ycsoft.business.dao.config;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TExtendAttribute;
import com.ycsoft.business.dto.config.ExtAttrFormDto;
import com.ycsoft.business.dto.config.ExtendAttributeDto;
import com.ycsoft.business.dto.config.ExtendTableAttributeDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.config.Table;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.helper.StringHelper;
import com.ycsoft.sysmanager.dto.config.ExtendTableDto;

/**
 * TExtendAttributeDao -> T_EXTEND_ATTRIBUTE table's operator
 */
@Component
public class TExtendAttributeDao extends BaseEntityDao<TExtendAttribute> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1555501349045731173L;

	/**
	 * default empty constructor
	 */
	public TExtendAttributeDao() {
	}

	public List<ExtendTableAttributeDto> findExtTableArrtible(String groupId, String tablename)
			throws JDBCException {
		String sql = "select * "
					+" from t_extend  e,"
					+"      t_extend_attribute ea"
					+" where e.extend_id = ea.extend_id"
					+"  and UPPER(e.extend_table) = UPPER(?)"
					+" {0} and ea.is_show = ? "
					+" order by ea.attribute_order";
		if(groupId == null){
			sql  = StringHelper.formatIgnoreType( sql , " and ea.group_id is null ");
		}else{
			sql  = StringHelper.formatIgnoreType( sql , " and (ea.group_id='"+ groupId +"' or ea.group_id is null)");
		}
		List<ExtendTableAttributeDto> list = createQuery(ExtendTableAttributeDto.class, sql,
				tablename,SystemConstants.BOOLEAN_TRUE).list();
		return list;
	}

	public Map<String, Object> findExtTable(String tableName, String primaryKey_value,
			String primaryKey) throws JDBCException {
		Table t = new Table();
		t.setTableName(tableName);
		t.setPrimaryKey(primaryKey);
		String sql = getSqlGenerator().getEntityByKey(t);
		return findToMap(sql, primaryKey_value);
	}

	/**
	 * 判断在扩展表中是否已经存在记录
	 * @param form 扩展信息
	 */
	public boolean existExtTable(ExtAttrFormDto form)throws JDBCException{
		String tableName = form.getExtendTable();
		String pkColumn = form.getPkColumn();
		String pkValue = form.getPkValue();
		String sql = StringHelper.formatIgnoreType(" SELECT count(*) FROM {0} t WHERE t.{0} = ?",tableName,pkColumn);
		return  count(sql, pkValue) > 0 ? true : false ;

	}

	/**
	 * 保存扩展信息
	 * @throws JDBCException
	 */
	public int insterToExtTable(String tableName, Map params)
			throws JDBCException {
		return executeUpdate(getSqlGenerator().getSave(tableName, params));
	}

	/**
	 * 更新扩展信息
	 */
	public int updateToExtTable(ExtAttrFormDto form) throws JDBCException {
		Table t = new Table();
		t.setTableName(form.getExtendTable());
		t.setPrimaryKey(form.getPkColumn());
		String sql = getSqlGenerator().getUpdate(t, (Map)form.getExtAttrs(),
				form.getPkValue());
		return executeUpdate(sql);
	}

	/**
	 * 删除扩展信息记录
	 * @throws JDBCException
	 */
	public int deleteToExtTable(String tabName,String pkColumn, Serializable [] pkValues) throws JDBCException {
		Table t = new Table();
		t.setTableName( tabName);
		t.setPrimaryKey( pkColumn);
		String sql = getSqlGenerator().getDelete(t);
		return executeUpdate(sql,pkValues);
	}

	/**
	 * 查询扩展类型为BUSI的扩展属性
	 * @return
	 */
	public List<ExtendAttributeDto> queryBusiExtAttr()throws Exception {
		String sql = "SELECT t2.attribute_id,t2.default_value, "
			       +" t2.attribute_name,"
			       +" t2.is_null,"
			       +" t2.input_type,"
			       +" t2.param_name,t.busi_code"
			       +" FROM t_extend t, t_extend_attribute t2"
			       +" WHERE t.extend_id = t2.extend_id"
			       +" and t.extend_type = ? and t2.is_show = ? "
			       +" order by t2.attribute_order";
		return createQuery(ExtendAttributeDto.class,sql, SystemConstants.EXT_ATTR_TYPE_BUSI,SystemConstants.BOOLEAN_TRUE ).list();
	}

	/**
	 * @param busiCode
	 * @return
	 */
	public List<TExtendAttribute> queryBusiExtAttr(String busiCode) throws JDBCException {
		String sql = "SELECT t2.attribute_id,t2.default_value, "
			       +" t2.attribute_name,"
			       +" t2.is_null,"
			       +" t2.input_type,"
			       +" t2.param_name"
			       +" FROM t_extend t, t_extend_attribute t2"
			       +" WHERE t.extend_id = t2.extend_id"
			       +" and t.extend_type = ?"
			       +" and t.busi_code = ? and t2.is_show = ? "
			       +" order by t2.attribute_order";
		return createQuery(sql, SystemConstants.EXT_ATTR_TYPE_BUSI, busiCode,SystemConstants.BOOLEAN_TRUE ).list();
	}



	/**
	 * 根据扩展ID，查询扩展属性
	 * @param extensionId
	 * @param groupId
	 * @return
	 * @throws JDBCException
	 */
	public List<ExtendTableDto> queryExtensionAttr(String extensionId,String groupId) throws JDBCException{
		String sql = "select tea.*,teg.group_name,te.extend_table ,s.item_desc param_name_text"
			+ " from t_extend_attribute tea,t_extend_group teg ,t_extend te,s_item_define s "
			+ " where tea.extend_id = ? and tea.extend_id=teg.extend_id(+) and tea.group_id = teg.group_id(+) "
			+ " and te.extend_id = tea.extend_id  and tea.param_name = s.item_key(+)";
		if(StringUtils.isNotEmpty(groupId)){
			sql = sql + " and tea.group_id = '" + groupId + "'";
		}
		List<ExtendTableDto> list = createQuery(ExtendTableDto.class, sql, extensionId).list();
		return list;
	}

	/**
	 * Description: 查询扩展表的字段信息
	 * @param tablename
	 * @return
	 * @throws JDBCException
	 */
	public List<ExtendTableDto> findCustExtendAttr(String tablename,String groupId) throws JDBCException{
		String sql = "select tea.*,teg.group_name,te.extend_table "
			+ " from t_extend_attribute tea,t_extend_group teg ,t_extend te "
			+ " where te.extend_table = ? and tea.extend_id=teg.extend_id(+) and tea.group_id = teg.group_id(+) "
			+ " and te.extend_id = tea.extend_id ";
		if(StringUtils.isNotEmpty(groupId)){
			sql = sql + " and tea.group_id = " + groupId;
		}
		List<ExtendTableDto> list = createQuery(ExtendTableDto.class, sql, tablename).list();
		return list;
	}

	/**
	 * Description: 查询扩展业务的字段信息
	 * @param busiCode 业务code
	 * @return
	 * @throws JDBCException
	 */
	public List<ExtendTableDto> findBusiExtendAttr(String busiCode,String groupId) throws JDBCException{
		String sql = "select tea.*,teg.group_name,te.busi_code "
			+ " from t_extend_attribute tea,t_extend_group teg ,t_extend te "
			+ " where te.busi_code = ? and tea.extend_id=teg.extend_id(+) and tea.group_id = teg.group_id(+) "
			+ " and te.extend_id = tea.extend_id ";
		if(StringUtils.isNotEmpty(groupId)){
			sql = sql + " and tea.group_id = " + groupId;
		}
		List<ExtendTableDto> list = createQuery(ExtendTableDto.class, sql, busiCode).list();;
		return list;
	}

	/**
	 * 根据扩展表attribute_id删除该扩展表的字段定义信息
	 * @param attribute_id
	 * @throws JDBCException
	 */
	public void deleteAttribute(String attribute_id) throws JDBCException{
		String sql = "delete from t_extend_attribute where attribute_id = ?";
		executeUpdate(sql, attribute_id);
	}

	/**
	 * 查询表的列
	 * @param table_name
	 * @return
	 * @throws JDBCException
	 */
	public List<TExtendAttribute> queryColumns(String table_name) throws JDBCException{
		String sql = "select t.column_name col_name,comments col_name_text from user_col_comments t where t.table_name = ?";
		List<TExtendAttribute> list = createQuery(TExtendAttribute.class, sql, table_name).list();
		return list;
	}

}
