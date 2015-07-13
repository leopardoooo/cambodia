/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.List;
import java.util.Map;

import com.ycsoft.business.commons.pojo.BusiParameter;

/**
 * @author liujiaqi
 * 
 */
public interface ITaskServiceExternal {

	List<Map<String,Object>> queryPrintContent(BusiParameter p,String []  taskTypes,String[] cust_id, String [] task_id)  throws Exception;
	
}
