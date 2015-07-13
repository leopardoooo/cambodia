/**
 * TBusiCodeDocDao.java	2010/03/08
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiCodeDoc;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * TBusiCodeDocDao -> T_BUSI_CODE_DOC table's operator
 */
@Component
public class TBusiCodeDocDao extends BaseEntityDao<TBusiCodeDoc> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6886580441425485419L;

	/**
	 * default empty constructor
	 */
	public TBusiCodeDocDao() {
	}

	/**
	 * 根据模板ID查询业务单据配置
	 *
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TBusiCodeDoc> queryDocTpls(String templateId)
			throws JDBCException {
		String sql = " select  t1.*,t2.busi_name,t3.doc_name,t4.template_name from t_busi_code_doc  t1 ,t_busi_code t2, t_busi_doc t3 ,t_template t4 "
				+ " where t1.template_id = ? and t1.busi_code=t2.busi_code and t1.doc_type=t3.doc_type and  t4.template_id=t1.template_id  ";
		return createQuery(TBusiCodeDoc.class, sql, templateId).list();
	}

	/**
	 * 根据模板ID删除记录
	 *
	 * @param templateId
	 * @throws JDBCException
	 */
	public void deleteByTplId(String templateId) throws JDBCException {
		String sql = "delete from t_busi_code_doc t where t.template_id=?";
		executeUpdate(sql, templateId);
	}

//	public Pager<TTemplateDto> query(Integer start, Integer limit,
//			String keyword) throws Exception {
//		String sql = " select  t1.*,t2.busi_name,t3.doc_name,t4.template_name from t_busi_code_doc  t1 ,t_busi_code t2, t_busi_doc t3 ,t_template t4 "
//				+ " where t1.busi_code=t2.busi_code and t1.doc_type=t3.doc_type and  t4.template_id=t1.template_id  ";
//		if (!keyword.equals("")) {
//			sql = sql + " and (t1.template_id = ? or t2.busi_code = ? )  ";
//			return createQuery(TTemplateDto.class, sql, keyword, keyword)
//					.setStart(start).setLimit(limit).page();
//		} else {
//			return createQuery(TTemplateDto.class, sql).setStart(start)
//					.setLimit(limit).page();
//		}
//	}

	public Object isupdate(String template_id, String busi_code, String doc_type)
			throws Exception {
		String sql = "  select * from t_busi_code_doc where  template_id = ? and  busi_code = ? and doc_type = ? ";
		Object request = findUnique(sql, template_id, busi_code, doc_type) == null ? false
				: true;
		if (request.equals(true)) {
			request = "该模板业务单据组合已经存在";
		}
		return request;

	}

	public void updateTemplateBusi(String template_id, String busi_code,
			String doc_type, String template_id_back, String busi_code_back,
			String doc_type_back) throws JDBCException {
		String sql = " UPDATE t_busi_code_doc  SET   template_id = ? ,busi_code= ? , doc_type = ?  WHERE template_id = ? and busi_code= ? and doc_type = ?    ";
		executeUpdate(sql, template_id, busi_code, doc_type, template_id_back,
				busi_code_back, doc_type_back);
	}

	public void logoffTD(String template_id, String busi_code, String doc_type)
			throws JDBCException {
		String sql = " DELETE t_busi_code_doc   WHERE   template_id = ?  and busi_code = ? and doc_type = ? ";
		executeUpdate(sql, template_id, busi_code, doc_type);
	}

}
