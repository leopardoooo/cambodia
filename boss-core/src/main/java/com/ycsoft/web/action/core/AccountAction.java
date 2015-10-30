package com.ycsoft.web.action.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ycsoft.business.dto.core.cust.CustOTTMobile;
import com.ycsoft.business.dto.core.user.UserLoginPwd;
import com.ycsoft.business.service.IAccountService;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.pojo.Root;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

@Controller
public class AccountAction extends BaseBusiAction {

	@Autowired
	private IAccountService accountService;
	private CustOTTMobile custOtt;
	private InputStream txtStream;
	private String txtFileName;
	
	public String createOttMobile() throws Exception {
		List<UserLoginPwd> list = accountService.createOttMobile(custOtt);
		
		String path = ServletActionContext.getServletContext().getRealPath("/");
		this.txtFileName = custOtt.getCust_name_prefix()+"vip.txt";
		BufferedWriter bw = null;
		File file = null;
		Root root = getProxyRoot();
		try {
			file = new File(path+this.txtFileName);
			bw = new BufferedWriter(new FileWriter(file));
			bw.write("账号,密码");
			for(UserLoginPwd ulp : list){
				bw.newLine();
				bw.write(ulp.getLogin_name()+","+ulp.getPassword());
			}
			bw.flush();
			
			this.txtStream = new FileInputStream(file);
			root.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			root.setSimpleObj(e.getMessage());
			root.setSuccess(false);
		} finally {
			if(bw != null)bw.close();
			if(file.exists())file.delete();
		}
		
		return "txt";
	}
	
	public String queryOTTMobileFreeProd() throws Exception {
		getRoot().setRecords(accountService.queryOTTMobileFreeProd());
		return JSON_RECORDS;
	}
	
	public InputStream getTxtStream() {
		return txtStream;
	}

	public void setTxtStream(InputStream txtStream) {
		this.txtStream = txtStream;
	}
	
	public String getTxtFileName() {
		return txtFileName;
	}

	public void setTxtFileName(String txtFileName) {
		this.txtFileName = txtFileName;
	}
	
	public CustOTTMobile getCustOtt() {
		return custOtt;
	}

	public void setCustOtt(CustOTTMobile custOtt) {
		this.custOtt = custOtt;
	}
	
}
