/**
 *
 */
package com.ycsoft.business.component.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JSignal;
import com.ycsoft.beans.system.SDataTranslation;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.business.dao.core.fee.CFeeDao;
import com.ycsoft.business.dao.core.job.JSignalDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.store.MemoryPrintData;
import com.ycsoft.commons.store.TemplateConfig;
import com.ycsoft.daos.core.JDBCException;

/**
 * @author liujiaqi
 * 
 */
@Component
public class MemoryComponent extends BaseComponent{
	private JSignalDao jSignalDao;
	private CFeeDao cFeeDao;

	/**
	 * 修改字典数据之后调用该方法,大概30秒更新.另外，仅能更新当前子系统的  MemoryDict 
	 * @param content
	 * @throws JDBCException
	 */
	public void addDictSignal(String content) throws JDBCException {
		jSignalDao.addSignal("M", content);
	}

	public void addTemplateSignal(String content) throws JDBCException {
		jSignalDao.addSignal("T", content);
	}
	
	public void addPrintSignal(String content) throws JDBCException {
		jSignalDao.addSignal("P", content);
	}

	public void setupMemoryDict(JSignal signal) throws Exception {
		if (StringHelper.isEmpty(signal.getSignal_content())) {
			// 装载所有数据
			MemoryDict.setupData(sItemvalueDao.findAllViewDict(), sDataTranslationDao.findAll());
		} else {
			List<SItemvalue> datas = sItemvalueDao.findViewDict(signal
					.getSignal_content());
			MemoryDict.appendData(datas);
			
			MemoryDict.appendTransData(sDataTranslationDao.queryDataTranslation(signal.getSignal_content()));
		}
	}

	public void setupMemoryTemplate() throws Exception {
		// 装载所有模板
		TemplateConfig.loadData();
	}
	
	public void setupMemoryPrintData() throws Exception {
		//MemoryPrintData.loadData(cFeeDao.queryUnPrintFee());
	}

	/**
	 * @param signalDao the jSignalDao to set
	 */
	public void setJSignalDao(JSignalDao signalDao) {
		jSignalDao = signalDao;
	}

	public void setCFeeDao(CFeeDao feeDao) {
		cFeeDao = feeDao;
	}


}
