package com.ycsoft.business.service;

import java.util.List;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.system.MenuButtonDto;
import com.ycsoft.business.dto.system.OptrDto;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.system.SBulletinDto;
import com.ycsoft.sysmanager.dto.system.SRoleDto;

/**
 * 首页服务接口定义
 * 
 * @author hh
 * @date Jan 6, 2010 3:13:07 PM
 */
public interface IIndexService extends IBaseService {

	/**
	 * @deprecated
	 * 验证登陆
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public SOptr checkLogin(String loginName, String password) throws Exception;

	/**
	 * 获取营业子系统功能资源
	 * 
	 * @return
	 */
	public List<MenuButtonDto> findResource() throws Exception;

	/**
	 * 更新操作员密码，默认登录系统
	 * @param optrId
	 * @param password
	 * @param subSystemId
	 * @return
	 * @throws Exception
	 */
	public boolean updateOptrData(String optrId, String password,
			String subSystemId) throws Exception;

	/**
	 * 查询操作员的权限
	 * @param optrId
	 * @return
	 * @throws Exception
	 */
	public List<SRoleDto> querySubSystemByOptrId(String optrId) throws Exception;

	/**
	 * 查询操作员的公告信息
	 * @param start
	 * @param limit
	 * @param optrId
	 * @return
	 * @throws Exception
	 */
	public Pager<SBulletinDto> queryBulletinByOptrId(Integer start,Integer limit)
			throws Exception;

	/**
	 * 查询未查看的公告
	 * @param optrId
	 * @return
	 * @throws Exception
	 */
	public SBulletinDto queryUnCheckByOptrId(String optrId)
			throws Exception;

	/**
	 * 确认查看公告
	 * @param bulletinId
	 * @param optrId
	 * @throws Exception
	 */
	public void checkBulletin(String bulletinId,String optrId) throws Exception;
	
	/**
	 * 查询县市的所有操作员信息
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<OptrDto> queryBusiOptr() throws Exception;

}
