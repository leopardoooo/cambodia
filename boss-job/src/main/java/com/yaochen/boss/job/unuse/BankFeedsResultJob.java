package com.yaochen.boss.job.unuse;

import java.io.File;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yaochen.boss.commons.BankConstants;
import com.yaochen.boss.commons.BankFileParser;
import com.yaochen.boss.commons.BankFileUtil;
import com.yaochen.boss.commons.FtpUtilBuilder;
import com.yaochen.boss.job.component.BusiComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.bank.CBankGotodisk;
import com.ycsoft.beans.core.bank.CBankRefundtodisk;
import com.ycsoft.beans.core.bank.CBankReturn;
import com.ycsoft.beans.core.bank.CBankReturnPayerror;
import com.ycsoft.daos.core.DataHandler;

/**
 * 银行回盘任务处理
 * 
 * @author Tom
 */
@Service
public class BankFeedsResultJob implements Job2 {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private BusiComponent busiComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		logger.info("银行回盘任务处理..");
		try{
			busiComponent.runBankReturn();
		}catch(Exception e){
			logger.error("银行回盘任务处理出错", e);
		}
	}
	
	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}
}
