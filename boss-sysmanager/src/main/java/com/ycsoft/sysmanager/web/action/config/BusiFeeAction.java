package com.ycsoft.sysmanager.web.action.config;


import java.lang.reflect.Type;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TAcctFeeType;
import com.ycsoft.beans.config.TBusiDoc;
import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.business.dto.config.TBusiFeeDto;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.sysmanager.component.config.BusiFeeComponent;

public class BusiFeeAction extends BaseAction {
	/**
	 * @Description:
	 * @date Jul 14, 2010 10:05:46 AM
	 */
	private static final long serialVersionUID = -192617200299200908L;
	private BusiFeeComponent busiFeeComponent;
	private TBusiFee busiFee ;
	private TBusiFeeDto busiFeeDto;
	private TBusiDoc busiDoc;
	private String feetype;
	private String feeId;
	private String busiCode;

	private String acctItemId;
	private String prodIds;
	private Integer start;
	private Integer limit;
	private String query;
	private String doc_type;
	private String status;

	private String feeTypeListStr;
	
	public String queryBusiCodeByBusiType() throws Exception {
		getRoot().setRecords(busiFeeComponent.queryBusiCodeByBusiType());
		return JSON_RECORDS;
	}


	//保存、修改TBusiDoc(业务单据配置)
	public String updateBusiDoc() throws Exception{
		busiFeeComponent.updateBusiDoc(busiDoc);
		return JSON_SUCCESS;
	}

	//所有专项公用账目查询
	public String queryAllVewAcctitem() throws Exception{
		getRoot().setPage(busiFeeComponent.queryAllVewAcctitem(start,limit,query));
		return JSON_PAGE;
	}


	/**
	* @Description:查询费用信息
	* @return
	* @throws Exception
	* @return String
	 */
	public String queryBusiFee() throws Exception{
		status = null !=status ? status.trim() : "";
		getRoot().setRecords(busiFeeComponent.queryFee(query,status ));
		return JSON_RECORDS;
	}

	public String queryBusiDeviceBuyMode() throws Exception {
		getRoot().setRecords(busiFeeComponent.queryBusiDeviceBuyMode());
		return JSON_RECORDS;
	}
	
	/**
	* @Description: 保存或修改费用配置
	* @return
	* @throws Exception
	* @return String
	 */
	public String saveBusiFee() throws Exception{

		//异动
		TBusiFeeDto oldBean=busiFeeComponent.queryTBusiFeeDto(busiFeeDto.getFee_id());
		
		getRoot().setSuccess(busiFeeComponent.saveFee(busiFeeDto));
		
		TBusiFeeDto newBean=busiFeeComponent.queryTBusiFeeDto(busiFeeDto.getFee_id());
		busiFeeComponent.saveChangeTBusiFeeDto(oldBean, newBean);
		
		return JSON;
	}

	/**
	* @Description:禁用启用费用配置
	* @return
	* @throws Exception
	* @return String
	 */
	public String updateFeeStatus() throws Exception{
		//异动
		TBusiFeeDto oldBean=busiFeeComponent.queryTBusiFeeDto(feeId);
		
		getRoot().setSuccess(busiFeeComponent.updateFeeStatus(feeId,status));
		
		TBusiFeeDto newBean=busiFeeComponent.queryTBusiFeeDto(feeId);
		busiFeeComponent.saveChangeTBusiFeeDto(oldBean, newBean);
		
		return JSON;
	}

	/**
	 * 查询所有费用类型数据
	 * @return
	 * @throws Exception
	 */
	public String queryAllFeeType() throws Exception{
		getRoot().setRecords(busiFeeComponent.queryAllFeeType());
		return JSON_RECORDS;
	}

	public String updateFeeType() throws Exception{
		List<TAcctFeeType> feeTypeList = null;
		if(StringHelper.isNotEmpty(feeTypeListStr)){
			Type type = new TypeToken<List<TAcctFeeType>>(){}.getType();
			Gson gson = new Gson();
			feeTypeList = gson.fromJson(feeTypeListStr, type);
		}
		busiFeeComponent.updateFeeType(feeTypeList);
		return JSON;
	}

	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}
	public void setBusiFeeComponent(BusiFeeComponent busiFeeComponent) {
		this.busiFeeComponent = busiFeeComponent;
	}
	public void setBusiFee(TBusiFee busiFee) {
		this.busiFee = busiFee;
	}
	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public BusiFeeComponent getBusiFeeComponent() {
		return busiFeeComponent;
	}

	public TBusiFee getBusiFee() {
		return busiFee;
	}

	public String getFeetype() {
		return feetype;
	}

	public String getFeeId() {
		return feeId;
	}

	@Override
	public void setStart(Integer start) {
		this.start = start;
	}

	@Override
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}

	public TBusiDoc getBusiDoc() {
		return busiDoc;
	}

	public void setBusiDoc(TBusiDoc busiDoc) {
		this.busiDoc = busiDoc;
	}

	public String getFeeTypeListStr() {
		return feeTypeListStr;
	}

	public void setFeeTypeListStr(String feeTypeListStr) {
		this.feeTypeListStr = feeTypeListStr;
	}

	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}

	public TBusiFeeDto getBusiFeeDto() {
		return busiFeeDto;
	}

	public void setBusiFeeDto(TBusiFeeDto busiFeeDto) {
		this.busiFeeDto = busiFeeDto;
	}


	public void setStatus(String status) {
		this.status = status;
	}
}
