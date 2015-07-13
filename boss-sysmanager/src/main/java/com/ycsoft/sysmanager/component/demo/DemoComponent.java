package com.ycsoft.sysmanager.component.demo;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.system.SOptrDao;
import com.ycsoft.business.dao.system.SResourceDao;
import com.ycsoft.business.dao.system.SRoleDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.daos.core.Pager;

/**
 * 测试组件
 *
 * @author hh
 * @data Mar 24, 2010 9:32:53 AM
 */
@Component
public class DemoComponent extends BaseComponent {
	private SResourceDao sResourceDao;
	private SRoleDao sRoleDao;
	private SOptrDao sOptrDao ;

	/**
	 * 查询操作员的信息
	 * @param start
	 * @param limit
	 * @param keyword 关键字
	 * @return
	 */
	public Pager<SOptr> queryOptrs(Integer start, Integer limit, String keyword)throws Exception {
		SOptr op = new SOptr();
		if( !"".equals( keyword)  ){
			op.setLogin_name( keyword );
			op.setOptr_name( keyword );
		}
		return sOptrDao.findByEntity(start, limit, op );
	}

	/**
	 * 注销操作员
	 * @param optr_id
	 */
	public void logoffOptr(String optr_id) {

		// 修改操作员的状态
	}

	public SResourceDao getSResourceDao() {
		return sResourceDao;
	}
	public void setSResourceDao(SResourceDao resourceDao) {
		sResourceDao = resourceDao;
	}
	public SRoleDao getSRoleDao() {
		return sRoleDao;
	}
	public void setSRoleDao(SRoleDao roleDao) {
		sRoleDao = roleDao;
	}

	public SOptrDao getSOptrDao() {
		return sOptrDao;
	}

	public void setSOptrDao(SOptrDao optrDao) {
		sOptrDao = optrDao;
	}

}
