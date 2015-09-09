/**
 * CProdPropChangeDao.java	2010/07/13
 */

package com.ycsoft.business.dao.core.prod;


import java.util.Date;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdStatusChange;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CProdPropChangeDao -> C_PROD_PROP_CHANGE table's operator
 */
@Component
public class CProdStatusChangeDao extends BaseEntityDao<CProdStatusChange> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4710329294236197489L;

	/**
	 * default empty constructor
	 */
	public CProdStatusChangeDao() {}
	
	public void saveStatusChange(Integer done_code,String order_sn,String status) throws JDBCException{
		CProdStatusChange _o=new CProdStatusChange();
		_o.setDone_code(done_code);
		_o.setOrder_sn(order_sn);
		_o.setStatus(status);
		_o.setStatus_date(new Date());
		this.save(_o);
	}
	
	public void deleteByDoneCode(Integer done_code) throws JDBCException{
		String sql="delete c_prod_status_change where done_code=? ";
		this.executeUpdate(sql, done_code);
	}
	
	/**
	 * 记录失效订单的到期停日期
	 * @param doneCode
	 * @throws JDBCException 
	 */
	public void saveOrderExpStatus(Integer doneCode) throws JDBCException{
		String sql="insert into c_prod_status_change(done_code,order_sn,status,status_date) "
				+" select ?,t.order_sn,?,sysdate "
				+" from c_prod_order t where t.status=? and t.exp_date<trunc(sysdate) ";
		this.executeUpdate(sql,doneCode,StatusConstants.FORSTOP,StatusConstants.ACTIVE);
	}
}
