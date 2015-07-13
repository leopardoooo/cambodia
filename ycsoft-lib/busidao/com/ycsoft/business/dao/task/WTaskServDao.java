/**
 * WTaskServDao.java	2010/03/16
 */

package com.ycsoft.business.dao.task;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WTaskServ;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * WTaskServDao -> W_TASK_SERV table's operator
 */
@Component
public class WTaskServDao extends BaseEntityDao<WTaskServ> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4530530259017878299L;
	/**
	 * 查询服务类型
	 * @param taskId
	 * @return
	 * @throws JDBCException
	 */
	public List<WTaskServ> queryTaskServByTaskId(String taskId) throws JDBCException {
		String sql = "select * from busi.w_task_serv t where t.task_id=?";
		return createQuery(WTaskServ.class, sql,taskId).list();
	}
}
