/**
 * SAreaDao.java	2010/03/07
 */

package com.ycsoft.business.dao.core.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JSignal;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class JSignalDao extends BaseEntityDao<JSignal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2718615219676584449L;

	/**
	 * default empty constructor
	 */
	public JSignalDao() {
	}

	public String loadLastId() throws JDBCException {
		String id = findUnique("select MAX(signal_id) from j_signal");
		if (StringHelper.isEmpty(id)) {
			id = "0";
		}
		return id;
	}

	public void addSignal(String type, String content) throws JDBCException {
		JSignal s = new JSignal();
		s.setSignal_content(content);
		s.setSignal_type(type);
		save(s);
	}

	/**
	 * 查找大于指定id的加载数据
	 * 
	 * @param loadLastId
	 * @return
	 * @throws JDBCException
	 */
	public List<JSignal> signalDatas(String loadLastId) throws JDBCException {
		return findList(
				"SELECT signal_type,signal_content,MAX(signal_id) signal_id"
						+ " FROM j_signal t WHERE signal_id>? GROUP BY signal_type,signal_content"
						+ " ORDER BY signal_id", loadLastId);
	}
}
