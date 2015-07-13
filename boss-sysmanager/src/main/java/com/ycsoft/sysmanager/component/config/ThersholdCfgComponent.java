package com.ycsoft.sysmanager.component.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.bill.BThersholdCfg;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.bill.BThersholdCfgDao;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.component.system.BaseSystemComponent;

@Component
public class ThersholdCfgComponent extends BaseSystemComponent {
	private BThersholdCfgDao bThersholdCfgDao;

	public Pager<BThersholdCfg> query(SOptr cfg, Integer start,
			Integer limit) throws Exception{
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		if(!cfg.getCounty_id().equals(SystemConstants.COUNTY_ALL)){
			params.put("county_id", cfg.getCounty_id());
		}
		params.put("prod_type", "T");//只查询基本及产品的
//		if(StringHelper.isNotEmpty(cfg.getTask_code())){params.put("task_code", cfg.getTask_code());}
		
		return bThersholdCfgDao.findByMap(start, limit, params);
	}

	public void saveOrUpdate(BThersholdCfg... cfgs) throws Exception{
		for(BThersholdCfg cfg:cfgs){
			Object[] params = new Object[5];
			params[0] = cfg.getNew_threshold_day();
			params[1] = cfg.getProd_type();
			params[2] = cfg.getTask_code();
			params[3] = cfg.getCounty_id();
			params[4] = cfg.getArea_id();
			bThersholdCfgDao.executeUpdate("update B_THRESHOLD_CFG set new_threshold_day = ?  where prod_type = ? and task_code = ? and county_id = ? and area_id = ? ", params);
		}
	}

	public void setBThersholdCfgDao(BThersholdCfgDao bThersholdCfgDao) {
		this.bThersholdCfgDao = bThersholdCfgDao;
	}
}
