package com.ycsoft.sysmanager.web.action.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TProvince;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.sysmanager.component.config.ConfigComponent;

@Controller
public class ConfigAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4463745557033215523L;
	
	@Autowired
	private ConfigComponent configComponent;
	
	public String queryProvince() throws Exception {
		getRoot().setRecords(configComponent.queryProvince());
		return JSON_RECORDS;
	}
	
	public String saveProvince() throws Exception {
		String str = request.getParameter("provinces");
		List<TProvince> provinceList = new Gson().fromJson(str, new TypeToken<List<TProvince>>(){}.getType());
		configComponent.saveProvince(provinceList);
		return JSON_SUCCESS;
	}
	
}
