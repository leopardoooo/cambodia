package com.ycsoft.business.dao.core.job;


import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.core.job.JCaCommandOsdInvalid;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.helper.BeanHelper;


/**
 * JCaCommandDao -> J_CA_COMMAND table's operator
 */
@Component
public class JCaCommandOsdInvalidDao extends BaseEntityDao<JCaCommandOsdInvalid> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2518144229844541779L;

	/**
	 * default empty constructor
	 */
	public JCaCommandOsdInvalidDao() {}

	public void copyInsert(JCaCommand cmd) throws Exception{
		JCaCommandOsdInvalid inv = new JCaCommandOsdInvalid();
		
		Map desc = BeanHelper.describe(cmd);
		Set fields = desc.keySet();
		for(Object field:fields){
			String key = field.toString();
			Object rawValue = desc.get(key);
			if(rawValue == null){
				continue;
			}
			String value = rawValue.toString();
			if(StringHelper.isEmpty(value)){
				continue;
			}
			if("ret_date,record_date,send_date,create_time".indexOf(key) >= 0){
				value = value.substring(0,19);
				BeanHelper.copyProperty(inv, key, DateHelper.parseDate(value, DateHelper.FORMAT_TIME));
			}else if("priority,transnum,done_code,job_id".indexOf(key)>=0){
				BeanHelper.copyProperty(inv, key, Integer.parseInt(value));
			}else if(key.equals("detail_params")){
				BeanHelper.copyProperty(inv, key, value.replaceAll("'", "''"));
			}else{
				BeanHelper.copyProperty(inv, key, value);
			}
		}
		
		save(inv);
	}

}
