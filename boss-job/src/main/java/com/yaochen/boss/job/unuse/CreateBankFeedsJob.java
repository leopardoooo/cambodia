package com.yaochen.boss.job.unuse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yaochen.boss.commons.BankFileUtil;
import com.yaochen.boss.commons.FtpUtil;
import com.yaochen.boss.commons.FtpUtilBuilder;
import com.yaochen.boss.job.component.BusiComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.yaochen.myquartz.Task;
import com.ycsoft.beans.core.bank.CBankGotodisk;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.DataHandler;

/**
 * 创建银行扣费文件
 * 任务支持 银行扣款(GO)，银行退款两种模式(REFUND)，
 * 为了程序的可读性，强行配置JobType，不使用Spring自动扫描机制
 * 
 * @author Tom
 */
public class CreateBankFeedsJob implements Job2{
	private final static Logger logger = LoggerFactory.getLogger(CreateBankFeedsJob.class);
	
	private BusiComponent busiComponent;
	private FtpUtilBuilder ftpBuilder;
	private String jobType ;
	
	public CreateBankFeedsJob(){
	}
	
	enum JobType{
		REFUND, //退款
		GO //扣款
	}
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		final String fileNo = BankFileUtil.gBankFeeDeductionFileName();
		
		final String filePath = ftpBuilder.getLocalTempPath() + File.separator + fileNo;
		logger.info("创建银行"+ this.jobType +"文件任务处理 ..");
		try {
			DataHandler<CBankGotodisk> myHandler = new DataHandler<CBankGotodisk>() {
				@Override
				public void fetchRows(List<CBankGotodisk> results, int fetchCount)
						throws Exception {
					new CreateBankFeedsTask(results, filePath).execute(null, null);
				}
			};
			// 扣款
			if(this.jobType.equals(JobType.GO.name())){ 
				busiComponent.saveBankGotodisk(fileNo);
				busiComponent.queryBankGotodisk(fileNo, myHandler);
			}else{ // 退款
				busiComponent.saveBankRefundtodisk(fileNo);
				busiComponent.queryBankRefundtoDisk(fileNo, myHandler);
			}
			File file = new File(filePath);
			if(file.exists()){
				FtpUtil ftp = ftpBuilder.buildFtpUtil();
				ftp.login();
				ftp.uploadFile(filePath, ftpBuilder.getRemoteUploadPath());
				ftp.logout();
				//移除到历史目录
				BankFileUtil.moveTo08History(file, ftpBuilder.getLocalHistoryPath());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}
	
	
	public void setFtpBuilder(FtpUtilBuilder ftpBuilder) {
		this.ftpBuilder = ftpBuilder;
	}
	
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}


	static class CreateBankFeedsTask implements Task{

		private List<CBankGotodisk> results;
		private String bankFile;
		
		public CreateBankFeedsTask(List<CBankGotodisk> results, String bankFile){
			this.results = results;
			this.bankFile = bankFile;
		}
		
		@Override
		public void execute(Job2 parentJob, Job2ExecutionContext context)
				throws JobExecutionException {
			Collection<String> lines = new ArrayList<String>();
			List<String> transSn = new ArrayList<String>();
			for (CBankGotodisk disk : this.results) {
				lines.add(parserLine(disk));
				transSn.add(disk.getTrans_sn());
			}
			FileOutputStream fos = null;
			try {
				File file = new File(bankFile);
				if(!file.exists()){
					file.createNewFile();
					logger.info("创建文件" + bankFile);
				}
				fos = new FileOutputStream(file, true);
				IOUtils.writeLines(lines, IOUtils.LINE_SEPARATOR, fos, "GBK");
			} catch (Exception e) {
				logger.error("追加文件内容出错", e);
			}finally{
				try {
					if(null != fos){
						fos.close();
					}
				} catch (IOException e) {
				}
			}
		}
		
		private String parserLine(CBankGotodisk disk){
			StringBuffer sb = new StringBuffer();
			sb.append("D301|");
			sb.append(disk.getBusi_type());
			sb.append("|");
			sb.append(disk.getCust_no());
			sb.append("|");
			sb.append(DateHelper.format(disk.getCreate_time(), "yyyyMMdd"));
			sb.append("|");
			sb.append(substr(disk.getCust_name(), 20));
			sb.append("||"); // 用户地址
			sb.append(DateHelper.format(disk.getStart_date(), "yyyyMMdd"));
			sb.append("|");
			sb.append(DateHelper.format(disk.getEnd_date(), "yyyyMMdd"));
			sb.append("|");
			sb.append(DateHelper.format(disk.getCreate_time(), "yyyyMMdd"));
			sb.append("|");
			sb.append(disk.getTrans_sn());
			sb.append("|");
			//sb.append(StringHelper.isEmpty(disk.getBank_trans_sn()) ? "" : disk.getBank_trans_sn());
			sb.append("|");
			sb.append(disk.getFee());
			sb.append("|0|");// 滞纳金
			sb.append(disk.getBank_fee_name());
			sb.append("|");
			sb.append(disk.getBank_account());
			sb.append("|");
			sb.append(disk.getBank_code());
			sb.append("|0|0|0|0|0|0|0|0|0|0");
			return sb.toString();
		}
		
		private String substr(String src, int length){
			if(StringUtils.isEmpty(src)){
				return "";
			}
			
			if(src.length() > length){
				return src.substring(0, length);
			}else{
				return src;
			}
		}
	}
	
}

