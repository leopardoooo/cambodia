/**
 * TTemplateDao.java	2010/02/25
 */

package com.ycsoft.business.dao.config;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiCodeFee;
import com.ycsoft.beans.config.TBusiDoc;
import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.beans.config.TInvoicePrintitem;
import com.ycsoft.beans.config.TProdStatusRent;
import com.ycsoft.beans.config.TTemplate;
import com.ycsoft.beans.config.TUpdateCfg;
import com.ycsoft.business.dto.config.TemplateDto;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.tree.TreeDto;

/**
 * TTemplateDao -> T_TEMPLATE table's operator
 */
@Component
public class TTemplateDao extends BaseEntityDao<TTemplate> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6407490346683954022L;

	/**
	 * default empty constructor
	 */
	public TTemplateDao() {
	}

	public List<TemplateDto> findAllTemplate() throws JDBCException {
		String sql = "select * from t_template";

		List<TemplateDto> list = createQuery(TemplateDto.class, sql).list();
		return list;
	}

	/**
	 * @return
	 */
	public Map<String, List<TBusiDoc>> findAllDocTemplateToMap()
			throws Exception {
		String sql = "select d.*,b.template_id from t_busi_code_doc b,t_busi_doc d where b.doc_type=d.doc_type";
		return CollectionHelper.converToMap(
				createQuery(TBusiDoc.class, sql).list(), "template_id");
	}

	/**
	 * @return
	 */
	public Map<String, List<TBusiCodeFee>> findAllFeeTemplateToMap()
			throws Exception {
		String sql = "select * from t_busi_code_fee";
		return CollectionHelper.converToMap(
				createQuery(TBusiCodeFee.class, sql).list(), "template_id");
	}

	/**
	 * @return
	 */
	public Map<String, List<TInvoicePrintitem>> findAllPrintItmeTemplateToMap()
			throws Exception {
		String sql = "select * from t_invoice_printitem";
		return CollectionHelper.converToMap(createQuery(
				TInvoicePrintitem.class, sql).list(), "template_id");
	}

	/**
	 *
	 */
	public List<TConfigTemplate> queryAllConfig() throws JDBCException {
		String sql = "SELECT c.county_id,ct.*,t.template_name,t.template_type "
				+ " FROM t_config_template ct,t_template t,t_template_county c "
				+ " WHERE ct.template_id=t.template_id AND c.template_id=t.template_id";
		return createQuery(TConfigTemplate.class, sql).list();
	}

	/**
	 *
	 */
	public List<TUpdateCfg> queryAllUpdateProp() throws JDBCException {
		String sql = "SELECT c.county_id,ct.*,t.template_name,t.template_type "
				+ " FROM t_update_cfg ct,t_template t,t_template_county c "
				+ " WHERE ct.template_id=t.template_id AND c.template_id=t.template_id";
		return createQuery(TUpdateCfg.class, sql).list();

	}
	/**
	 * 查询模板配置菜单树
	 * @param string 
	 * @param string 
	 * @return
	 * @throws JDBCException
	 */
	public List<TreeDto> queryTemplateTree(String dataRight) throws JDBCException{
		String sql = "select to_char(rownum) id, '0' pid,t.type_name text ,t.template_type || '_type'  attr from t_template_type t"
			+ " where (t.template_type !='TERMINAL_AMOUNT' and t.template_type !='PRINT') union all "
			+ " select distinct t.template_id || '_' || ty.id id, ty.id pid ,t.template_name text,t.template_id|| '_' ||t.optr_id attr "
			+ " from t_template t ,t_template_county tc," 
			+ " (select to_char(rownum) id,template_type from t_template_type where (template_type !='TERMINAL_AMOUNT' and template_type !='PRINT')) ty "
			+ " where t.template_type = ty.template_type and tc.template_id(+)=t.template_id and "+dataRight;
		return this.createQuery(TreeDto.class, sql).list();
	}
	
	/**
	 * 查询费用模板配置菜单树
	 * @param string 
	 * @param string 
	 * @return
	 * @throws JDBCException
	 */
	public List<TreeDto> queryFeeTemplateTree(String countyId,String dataRight, String optrId) throws JDBCException{
		String sql = "select to_char(rownum) id, '0' pid,t.type_name text ,t.template_type || '_type'  attr" +
				" from t_template_type t"
			+ " where (t.template_type !='TERMINAL_AMOUNT' and t.template_type !='PRINT')" +
					" union all "
			+ " select distinct t.template_id || '_' || ty.id id, ty.id pid ,t.template_name text,t.template_id|| '_' ||t.optr_id attr "
			+ " from t_template t ,t_template_county tc," 
			+ " (select to_char(rownum) id,template_type from t_template_type where (template_type !='TERMINAL_AMOUNT' and template_type !='PRINT')) ty "
			+ " where t.template_type = ty.template_type and tc.template_id(+)=t.template_id";
		if(!SystemConstants.COUNTY_ALL.equals(countyId)){
			sql = StringHelper.append(sql," and (",dataRight," or t.optr_id='",optrId,"')");
		}
		return this.createQuery(TreeDto.class, sql).list();
	}

	/**
	 * 查询计费模板数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TProdStatusRent> queryBillTpls(String templateId) throws JDBCException{
		String sql = "select t1.*,t2.status_desc from t_prod_status_rent t1 ,t_status t2 "
			+ " where t1.template_id=? and t1.status_id=t2.status_id ";
		return createQuery(TProdStatusRent.class, sql, templateId).list();
	}

//	/**
//	 * 查询配置种类
//	 * @return
//	 * @throws JDBCException
//	 */
//	public List<TTemplateDto> queryConfigs() throws JDBCException{
//		String sql = "select * from t_config";
//		return createQuery(TTemplateDto.class, sql).list();
//	}


	/**
	* @param countyId 
	 * @Description: 根据type 查询模板
	* @param type
	* @return
	* @throws Exception
	* @return List<TTemplate>
	*/
	public List<TTemplate> queryTplsByType(String templateType, String countyId)throws Exception {
		String sql = " select distinct t.* from t_template t ,t_template_county tc  where t.template_id=tc.template_id(+) and t.template_type = ?";
		if(!SystemConstants.COUNTY_ALL.equals(countyId)){
			sql = StringHelper.append(sql," and tc.county_id='",countyId,"'");
		}
		return createQuery(TTemplate.class, sql, templateType).list();
	}

	/**
	 * 获取费用SEQ编号
	 */
	public String getTemplateID() throws Exception{
		return findSequence(SequenceConstants.SEQ_TEMPLATE_ID).toString();
	}
	/**
	 * 程序需要校验模板应当为1个
	 * @param county_id
	 * @param template_type
	 * @return
	 * @throws Exception
	 */
	public String getTemplateId(String county_id, String template_type)
			throws Exception {
		final String sql = " SELECT T.Template_Id "
				+ " FROM T_TEMPLATE T, T_TEMPLATE_COUNTY T1"
				+ " WHERE T.TEMPLATE_ID = T1.TEMPLATE_ID"
				+ "  AND T1.COUNTY_ID = ? " + "  AND T.TEMPLATE_TYPE = ?";
		return findUnique(sql, county_id, template_type);
	}

}
