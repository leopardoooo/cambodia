package com.yaochen.boss.job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yaochen.boss.commons.BankFileParser;
import com.yaochen.boss.commons.BankFileUtil;
import com.yaochen.boss.commons.FtpUtilBuilder;
import com.yaochen.boss.job.component.BusiComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.bank.CBankAgree;
import com.ycsoft.daos.core.JDBCException;

/**
 * <p> 银行签约任务处理</p>
 * <p>从FTP服务器获取银行签约记录, 并解析远程调用core保存签约信息</p>
 * 
 * @author Tom
 */
@Service
public class BankAgreeJob implements Job2{
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private BusiComponent busiComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		try {
			bankAgree();
		} catch (Exception e) {
			logger.error("银行签约任务处理出错", e);
		}
	}
	
	

	private void bankAgree() throws JDBCException {
		List<CBankAgree> bankAgreeList = busiComponent.quertyUnProcBankAgree();
		logger.info("银行签约处理:"+bankAgreeList.size());
		for (int i = 0; i < bankAgreeList.size(); i++) {
			CBankAgree cba = bankAgreeList.get(i);
			try{
				// 银行签约或换卡
				if("01".indexOf(cba.getB_state()) >= 0){ 
					if (busiComponent.saveSignBank(cba)){
						busiComponent.successBankAgree(cba.getAgree_id());
					}
				// 取消银行扣款
				}else if("2".equals(cba.getB_state())){
					if( busiComponent.saveRemoveSignBank(cba)){
						busiComponent.successBankAgree(cba.getAgree_id());
					}
				}
			}catch (Exception e) {
				try {
					busiComponent.failBankAgree(cba.getAgree_id(),e.getMessage());
				} catch (JDBCException e1) {
					logger.info("银行签约错误："+e1.getMessage());
				}
				logger.info("银行签约失败"+e.getMessage());
			}
		}
		
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

}
