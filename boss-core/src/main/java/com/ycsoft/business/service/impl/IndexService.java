package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SResource;
import com.ycsoft.business.commons.abstracts.BaseService;
import com.ycsoft.business.component.system.IndexComponent;
import com.ycsoft.business.dto.system.MenuButtonDto;
import com.ycsoft.business.dto.system.OptrDto;
import com.ycsoft.business.service.IIndexService;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.system.SBulletinDto;
import com.ycsoft.sysmanager.dto.system.SRoleDto;

/**
 * <p>
 * 	首页控制服务层
 * </p>
 * @author hh
 * @date Dec 29, 2009 3:52:34 PM
 */
@Service
public class IndexService extends BaseService implements IIndexService{

	private IndexComponent indexComponent ;
	
	public List<OptrDto> queryBusiOptr() throws Exception {
		return indexComponent.queryBusiOptr();
	}

	/**
	 * 获取营业子系统功能资源
	 */
	public List<MenuButtonDto> findResource() throws Exception {
		List<SResource>  lst = indexComponent.findResource();
		List<MenuButtonDto> target = new ArrayList<MenuButtonDto>();

		for (SResource sr : lst) {
			target.add(new MenuButtonDto(sr));
		}
		return target;
	}
	/**
	 * 验证登陆
	 */
	public SOptr checkLogin(String loginName, String password) throws Exception {
		return indexComponent.checkOptrExists(loginName, password);
	}

	public Pager<SBulletinDto> queryBulletinByOptrId(Integer start,Integer limit) throws Exception {
		return indexComponent.queryBulletinByOptrId(start,limit, getOptr());
	}
	
	public SBulletinDto queryUnCheckByOptrId(String optrId) throws Exception {
		return indexComponent.queryUnCheckByOptrId(optrId);
	}
	
	public void checkBulletin(String bulletinId,String optrId) throws Exception {
		indexComponent.checkBulletin(bulletinId,optrId);
	}

	public boolean updateOptrData(String optrId,String password,String subSystemId) throws Exception{
		return indexComponent.updateOptrData(optrId,password,subSystemId); 
	}

	public List<SRoleDto> querySubSystemByOptrId(String optrId) throws Exception{
		return indexComponent.querySubSystemByOptrId(optrId); 
	}

	public void setIndexComponent(IndexComponent indexComponent) {
		this.indexComponent = indexComponent;
	}
}
