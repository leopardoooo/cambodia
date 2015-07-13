package com.ycsoft.report.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepGroup;
@Component
public class RepGroupDao extends BaseEntityDao<RepGroup> {
	public RepGroupDao(){
	}
	
	public String findColumnName(String rep_id) throws JDBCException{
		RepGroup rg=this.findByKey(rep_id);
		if(rg!=null)
			return rg.getRep_column();
		return null;
	}
	
	public void saveGroup(RepGroup repGroup) throws JDBCException{
		if(StringHelper.isNotEmpty(repGroup.getRep_column()))
			this.save(repGroup);
	}
}
