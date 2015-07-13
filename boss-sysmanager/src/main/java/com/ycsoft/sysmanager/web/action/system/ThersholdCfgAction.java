package com.ycsoft.sysmanager.web.action.system;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.bill.BThersholdCfg;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.component.config.ThersholdCfgComponent;

@Controller
public class ThersholdCfgAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7872534744662246502L;

	private ThersholdCfgComponent thersholdCfgComponent;

	private BThersholdCfg cfg;// 阈值对象
	
	public String query() throws Exception{
		Pager<BThersholdCfg> page = thersholdCfgComponent.query(optr,start,limit);
		getRoot().setPage(page);
		return JSON_PAGE;
	}
	
	public String saveOrUpdate() throws Exception{
		String json = request.getParameter("cfgs");
		
		Type type = new TypeToken<List<BThersholdCfg>>(){}.getType();
		Gson gson = new Gson();
		
		List<BThersholdCfg> list = gson.fromJson(json, type);
		try{
			thersholdCfgComponent.saveOrUpdate(list.toArray(new BThersholdCfg[list.size()]));
		}catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
		return JSON_SUCCESS;
	}
	
	public BThersholdCfg getCfg() {
		return cfg;
	}

	public void setCfg(BThersholdCfg cfg) {
		this.cfg = cfg;
	}

	public void setThersholdCfgComponent(ThersholdCfgComponent thersholdCfgComponent) {
		this.thersholdCfgComponent = thersholdCfgComponent;
	}

}