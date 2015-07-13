package com.ycsoft.sysmanager.web.action.config;

import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.sysmanager.component.config.BusiFeeComponent;

public class BusiFeeStandardAction extends BaseAction {
	/**
	 * @Description:
	 * @date Jul 15, 2010 11:43:13 AM
	 */
	private static final long serialVersionUID = -147972913880232669L;
	private BusiFeeComponent busiFeeComponent;
	private BusiFeeDto busiFeeDto ;
	private String feetype;


	public String query() throws Exception{
		getRoot().setPage(busiFeeComponent.queryFeeValue(start , limit , feetype ));
		return JSON_PAGE;
	}

	public String save() throws Exception{
		//	getRoot().setSimpleObj(busiFeeComponent.saveUdateFeeValue(busiFeeDto));
		return JSON_SIMPLEOBJ;
	}

	/**
	* @Description:删除费用配置
	* @return
	* @throws Exception
	* @return String
	 */
	public String delete() throws Exception{
		//	getRoot().setSimpleObj(busiFeeComponent.deleteFeeValue(busiFeeDto));
		return JSON_SIMPLEOBJ;
	}



	public String getFeetype() {
		return feetype;
	}


	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}
	public BusiFeeComponent getBusiFeeComponent() {
		return busiFeeComponent;
	}


	public void setBusiFeeComponent(BusiFeeComponent busiFeeComponent) {
		this.busiFeeComponent = busiFeeComponent;
	}



	public BusiFeeDto getBusiFeeDto() {
		return busiFeeDto;
	}



	public void setBusiFeeDto(BusiFeeDto busiFeeDto) {
		this.busiFeeDto = busiFeeDto;
	}

}
