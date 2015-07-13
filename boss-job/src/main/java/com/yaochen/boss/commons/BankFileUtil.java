package com.yaochen.boss.commons;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * 扣费文件名称生成器
 * 
 * @author Tom
 */
public class BankFileUtil {

	public final static String FEE_DEDUCTION_SUFFIX = "08D301";
	public final static String HISTORY_25_SUFFIX = "25D301";
	public final static String HISTORY_26_SUFFIX = "26D301";
	public final static String HISTORY_08_SUFFIX = "08D301";
	public final static String HISTORY_11_SUFFIX = "11D301";
	public final static String HISTORY_14_SUFFIX = "14D301";
	public final static String HISTORY_87_SUFFIX = "87D301";

	public static synchronized String gBankFeeDeductionFileName() {
		StringBuffer sb = new StringBuffer();
		sb.append(FEE_DEDUCTION_SUFFIX);
		sb.append(".");
		sb.append(System.currentTimeMillis());
		return sb.toString();
	}
	
	public static void moveTo25History(File src, String historyPath){
		moveToHistory(src, historyPath, HISTORY_25_SUFFIX);
	}
	
	public static void moveTo26History(File src, String historyPath){
		moveToHistory(src, historyPath, HISTORY_26_SUFFIX);
	}
	
	public static void moveTo08History(File src, String historyPath){
		moveToHistory(src, historyPath, HISTORY_08_SUFFIX);
	}
	
	public static void moveToErrorHistory(File src, String historyPath){
		moveToHistory(src, historyPath, "Error");
	}
	
	private static void moveToHistory(File src, String historyPath, String suffix){
		if(src.exists()){
			historyPath = (suffix == null ? historyPath : historyPath + File.separator + suffix);
			try {
				FileUtils.copyFileToDirectory(src, new File(historyPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			src.delete();
		}
	}

	public static void moveToHistory(String localTempPath,String localHistoryPath,
			String history) {
		BankFileParser bankParser = new BankFileParser();
		File[] bankFiles = bankParser.listBankFiles(localTempPath, history);
		for (File file : bankFiles) {
			if(file.isFile()){
				moveToHistory(file,localHistoryPath,history);
			}
		}
	}
}
