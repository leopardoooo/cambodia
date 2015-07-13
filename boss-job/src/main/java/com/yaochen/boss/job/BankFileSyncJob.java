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
import com.yaochen.boss.commons.FtpUtil;
import com.yaochen.boss.commons.FtpUtilBuilder;
import com.yaochen.boss.job.component.BusiComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.bank.CBankAgree;
import com.ycsoft.beans.core.bank.CBankReturn;

/**
 * 定时从FTP下载银行回扣文件
 * 
 * @author Tom
 */
@Service
public class BankFileSyncJob implements Job2 {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private BusiComponent busiComponent;
	private FtpUtilBuilder ftpBuilder;

	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		logger.info("定时同步FTP文件[" + ftpBuilder.getRemoteBankPath() + "]");

		FtpUtil ftp = ftpBuilder.buildFtpUtil();
		ftp.login();
		downLoadfile(ftp);
		ftp.logout();

		file26ToDd();
		file25ToDb();

		moveOtherFileToHistory();
	}

	private void moveOtherFileToHistory() {
		BankFileUtil.moveToHistory(ftpBuilder.getLocalDownloadPath(),ftpBuilder.getLocalHistoryPath(),BankFileUtil.HISTORY_11_SUFFIX);
		BankFileUtil.moveToHistory(ftpBuilder.getLocalDownloadPath(),ftpBuilder.getLocalHistoryPath(),BankFileUtil.HISTORY_14_SUFFIX);
		BankFileUtil.moveToHistory(ftpBuilder.getLocalDownloadPath(),ftpBuilder.getLocalHistoryPath(),BankFileUtil.HISTORY_87_SUFFIX);
	}

	private void downLoadfile(FtpUtil ftp) {
		String localPath = ftpBuilder.getLocalDownloadPath();
		String remotePath = ftpBuilder.getRemoteBankPath();
		ftp.downLoadDirectory(localPath, remotePath, true);

	}

	private void file25ToDb() {
		BankFileParser bankParser = new BankFileParser();
		File[] bankFiles = bankParser.list25BankFiles(ftpBuilder
				.getLocalDownloadPath());
		try {
			for (File bankfile : bankFiles) {
				List<CBankReturn> bankList;
				bankList = bankParser.readAndParser25(bankfile);
				busiComponent.saveBankReturn(bankList);
				// 移除到历史
				 BankFileUtil.moveTo25History(bankfile,
				 ftpBuilder.getLocalHistoryPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void file26ToDd() {
		BankFileParser bankParser = new BankFileParser();
		File[] bankFiles = bankParser.list26BankFiles(ftpBuilder
				.getLocalDownloadPath());
		if (bankFiles == null)
			return;

		List<List<CBankAgree>> fileList = new ArrayList<List<CBankAgree>>();
		for (File bankfile : bankFiles) {
			System.out.println(bankfile.getName());
			try {
				List<CBankAgree> bankList = bankParser
						.readAndParser26(bankfile);
				// 保存文件信息到数据库
				busiComponent.savebankagree(bankList);
				fileList.add(bankList);

				// 移除到历史
				 BankFileUtil.moveTo26History(bankfile,
				 ftpBuilder.getLocalHistoryPath());
			} catch (Exception e) {
				BankFileUtil.moveToErrorHistory(bankfile, ftpBuilder
						.getLocalHistoryPath());
				logger.info(bankfile.getName() + " 已移除到错误目录");
			}
		}
	}

	public void setFtpBuilder(FtpUtilBuilder ftpBuilder) {
		this.ftpBuilder = ftpBuilder;
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}
}
