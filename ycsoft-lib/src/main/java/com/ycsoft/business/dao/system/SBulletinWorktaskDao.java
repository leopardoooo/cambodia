/**
 * TBulletinDao.java	2010/11/26
 */
 
package com.ycsoft.business.dao.system; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SBulletinWorktask;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * SBulletinDao -> S_BULLETIN table's operator
 */
@Component
public class SBulletinWorktaskDao extends BaseEntityDao<SBulletinWorktask> {
	

	/**
	 * default empty constructor
	 */
	public SBulletinWorktaskDao() {}
	
}
