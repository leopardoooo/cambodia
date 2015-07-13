/**
 * CDoneCodeDetailDao.java	2010/03/16
 */

package com.ycsoft.business.dao.core.common;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CDoneCodeDetailDao -> C_DONE_CODE_DETAIL table's operator
 */
@Component
public class CDoneCodeDetailDao extends BaseEntityDao<CDoneCodeDetail> {

	/**
	 *
	 */
	private static final long serialVersionUID = -9033258331862792737L;

	/**
	 * default empty constructor
	 */
	public CDoneCodeDetailDao() {}

	public List<CDoneCodeDetail> queryDetail(Integer doneCode) throws Exception{
		String sql ="select * from C_DONE_CODE_DETAIL where done_code=?";
		return this.createQuery(sql, doneCode).list();
	}
}
