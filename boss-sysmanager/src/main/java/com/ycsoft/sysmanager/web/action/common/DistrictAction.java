package com.ycsoft.sysmanager.web.action.common;

import org.springframework.stereotype.Controller;

import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.sysmanager.component.system.DistrictComponent;

/**
 *  实现区域、县市、营业厅树状的控制类
 *
 * @author hh
 * @data Mar 31, 2010 11:42:06 AM
 */
@Controller
public class DistrictAction extends BaseAction{

	/**
	 *
	 */
	private static final long serialVersionUID = -207077557297046060L;
	private DistrictComponent districtComponent;

	private String templateId;
	private String model;

	/**
	 * 查询操作员对应的区域分公司
	 * @return
	 * @throws Exception
	 */
	public String queryCounties() throws Exception{
		getRoot().setRecords(districtComponent.queryCounties(templateId,optr));
		return JSON_RECORDS;
	}
	/**
	 * 查询区域的列表
	 * @return
	 * @throws Exception
	 */
	public String queryDistrict()throws Exception{
		getRoot().setRecords( districtComponent.getDistricts(model,optr) );
		return JSON_RECORDS;
	}

	public String getDistrictTree()throws Exception{
		getRoot().setRecords( districtComponent.getDistrictTree(optr) );
		return JSON_RECORDS;
	}

	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public DistrictComponent getDistrictComponent() {
		return districtComponent;
	}
	public void setDistrictComponent(DistrictComponent districtComponent) {
		this.districtComponent = districtComponent;
	}
	public void setModel(String model) {
		this.model = model;
	}

}
