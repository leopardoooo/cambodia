package com.ycsoft.sysmanager.web.action.resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.ycsoft.beans.core.job.JBusiCmd;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.helper.FileHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.sysmanager.component.resource.JobComponent;
import com.ycsoft.sysmanager.dto.resource.DeviceDto;

@Controller
public class JobAction extends BaseAction {
	/**
	 *
	 */
	private static final long serialVersionUID = -1227977321692753554L;

	private JobComponent jobComponent;
	private JBusiCmd busiCmd;
	
	private String cardId;
	private String stbId;
	private String[] cardIds;
	private String server_id;
	private String supplier_id;
	private String detail_params;
	private Date start_date;
	private Date end_date;
	private Date send_time;
	private Date end_time;
	private Integer send_num;
	private Integer time_num;
	private Integer send_for;
	private String query;
	private Integer done_code;
	private String ca_type;
	private String jobId;
	private File files;
	private InputStream excelStream;

	public void setDone_code(Integer doneCode) {
		done_code = doneCode;
	}

	public String queryServerByCountyId() throws Exception {
		getRoot().setRecords(jobComponent.queryServerByCountyId(optr.getCounty_id()));
		return JSON_RECORDS;
		
	}
	
	public String saveSendAllCmd() throws Exception {
		jobComponent.saveSendAllCmd(start_date,end_date,send_time,end_time,send_num,time_num,send_for,server_id,
				supplier_id,detail_params,ca_type,optr);
		return JSON;
	}
	public String querySendAllCmd()throws Exception{ 
		getRoot().setPage( jobComponent.querySendAllCmd(start, limit,query,optr));
		return JSON_PAGE;  
	}
	
	public String querySendAllCmdProp() throws Exception {
		getRoot().setPage(jobComponent.querySendAllCmdProp(start, limit, query, optr));
		return JSON_PAGE;
	}
	public String querySendAllCmdHis() throws Exception {
		getRoot().setPage(jobComponent.querySendAllCmdHis(start, limit, query, optr));
		return JSON_PAGE;
	}
	
	public String deleteSendAllCmd() throws Exception{
		getRoot().setSuccess(jobComponent.deleteSendAllCmd(done_code));
	return JSON;
	}
	
	public String queryCurrentDateLog() throws Exception {
		getRoot().setRecords(jobComponent.queryCurrentDateLog(cardId));
		return JSON_RECORDS;
	}
	
	public String queryCurrDateCommand() throws Exception {
		getRoot().setRecords(jobComponent.queryCurrDateCommand(jobId, cardId));
		return JSON_RECORDS;
	}
	
	/**
	 * 创建单机灌装命令
	 * @return
	 * @throws Exception
	 */
	public String createCmdStbFilled() throws Exception {
		jobComponent.createCmdStbFilled(cardId,stbId, optr);
		return JSON;
	}
	
	/**
	 * 机卡解绑
	 * @return
	 * @throws Exception
	 */
	public String cancelStbCardFilled() throws Exception {
		jobComponent.cancelStbCardFilled(cardId,stbId, optr);
		return JSON;
	}
	
	/** 
	 * 查询Ca指令
	 * @return
	 * @throws Exception
	 */
	public String queryCaCommand() throws Exception{
		getRoot().setPage(jobComponent.queryCaCommandByCardId(cardIds,start,limit));
		return JSON_PAGE;
	}
	public String createBusiCmd() throws Exception {
		jobComponent.createBusiCmd(busiCmd, optr);
		return JSON;
	}
	
	public String createBusiCmdFile() throws Exception {
		List<String> cardList = new ArrayList<String>();
		String msg = "";
		List<JBusiCmd> list =  new ArrayList<JBusiCmd>();
		String type = request.getParameter("fileType");
		if(files != null){
			cardList = FileHelper.fileToArrayByType(files,type);
		}
		try{
			if(cardList.size() > 500){
				throw new Exception("请一次性录入小于500条数据");
			}
			list = jobComponent.createBusiCmdFile(cardList,busiCmd, optr);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		if(StringHelper.isNotEmpty(msg)){
			return retrunNone(msg);
		}
		
		boolean skey = false;
		boolean ckey = false;
		String ssrc = "";
		String csrc = "";
		int t=0;
		int f=0;
		int n=0;
		for(JBusiCmd jbc : list){
			if(StringHelper.isNotEmpty(jbc.getSupplier_id())){
				skey = true;
				if(t<3){
					ssrc += jbc.getSupplier_id()+",";
				}
				t++;
				if(t==3){
					ssrc +="<br/> ";
					t=0;
				}
			}
			if(StringHelper.isNotEmpty(jbc.getCounty_id())){
				ckey = true;
				if(f<3){
					csrc += jbc.getCounty_id()+",";
				}
				f++;
				if(f==3){
					csrc +="<br/> ";
					f=0;
				}
			}
			n++;
			if(n ==20){
				break;
			}
		}
		if(skey){
			msg +="CA类型错误: <br/>"+StringHelper.delEndChar(ssrc,1)+";";
		}
		if(ckey){
			msg +="县市错误：<br/>"+StringHelper.delEndChar(csrc,1)+";";
		}
		
		return retrunNone(msg);
	}
	
	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public void setDetail_params(String detail_params) {
		this.detail_params = detail_params;
	}

	public void setServer_id(String server_id) {
		this.server_id = server_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public JBusiCmd getBusiCmd() {
		return busiCmd;
	}

	public void setBusiCmd(JBusiCmd busiCmd) {
		this.busiCmd = busiCmd;
	}

	public void setStart_date(Date startDate) {
		start_date = startDate;
	}

	public void setEnd_date(Date endDate) {
		end_date = endDate;
	}

	public void setSend_time(Date sendTime) {
		send_time = sendTime;
	}

	public void setEnd_time(Date endTime) {
		end_time = endTime;
	}

	public void setSend_num(Integer sendNum) {
		send_num = sendNum;
	}

	public void setTime_num(Integer timeNum) {
		time_num = timeNum;
	}

	public void setSend_for(Integer sendFor) {
		send_for = sendFor;
	}
	public void setQuery(String query) {
		this.query = query;
	}

	public void setCa_type(String caType) {
		ca_type = caType;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public File getFiles() {
		return files;
	}

	public void setFiles(File files) {
		this.files = files;
	}

	public void setCardIds(String[] cardIds) {
		this.cardIds = cardIds;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public void setStbId(String stbId) {
		this.stbId = stbId;
	}
	
}
