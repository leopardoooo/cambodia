/**
 * SRoleDataDao.java	2010/03/07
 */

package com.ycsoft.business.dao.system;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SRoleData;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * SRoleDataDao -> S_ROLE_DATA_ table's operator
 */
@Component
public class SRoleDataDao extends BaseEntityDao<SRoleData> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4891472316097439123L;

	/**
	 * default empty constructor
	 */
	public SRoleDataDao() {}

}
