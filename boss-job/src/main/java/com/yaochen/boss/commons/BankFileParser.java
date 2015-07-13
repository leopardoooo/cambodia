package com.yaochen.boss.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.ycsoft.beans.core.bank.CBankAgree;
import com.ycsoft.beans.core.bank.CBankReturn;
import com.ycsoft.commons.helper.DateHelper;


/**
 * 银行回扣文件解析类
 * @author Tom
 */
public class BankFileParser {

	private final String SPLIT_CHAR = "\\|";
	
	public File[] listBankFiles(String local,String preName){
		File file = new File(local);
		File[] listFiles = file.listFiles(new DefaultFilenameFilter(preName));
		Arrays.sort(listFiles);
		return listFiles;
	}
	public File[] list25BankFiles(String local){
		return listBankFiles(local, BankFileUtil.HISTORY_25_SUFFIX);
	}
	
	public File[] list26BankFiles(String local){
		return listBankFiles(local, BankFileUtil.HISTORY_26_SUFFIX);
	}
	
	public File[] list08BankFiles(String local){
		return listBankFiles(local, BankFileUtil.HISTORY_08_SUFFIX);
	}	
	
	public List<CBankAgree> readAndParser26(File bankFile)throws IOException{
		List<CBankAgree> bankList = new ArrayList<CBankAgree>();
		FileInputStream fis = new FileInputStream(bankFile);
		List<String> lines = IOUtils.readLines(fis, "GBK");
		
		for (String line : lines) {
			if(StringUtils.isEmpty(line)){
				continue;
			}
			
			bankList.add(newCBankAgree(line,bankFile.getName()));
		}
		
		fis.close();
		return bankList;
	}
	
	public List<CBankReturn> readAndParser25(File bankFile)throws IOException{
		List<CBankReturn> bankList = new ArrayList<CBankReturn>();
		FileInputStream fis = new FileInputStream(bankFile);
		List<String> lines = IOUtils.readLines(fis, "GBK");
		
		for (String line : lines) {
			if(StringUtils.isEmpty(line)){
				continue;
			}
			String[] params = line.split(SPLIT_CHAR);
			bankList.add(newCBankReturn(params));
		}
		
		fis.close();
		return bankList;
	}
	
	/**
	 * 将文件的参数按规定的下标获取参数并构造<code>CBankAgree</code>
	 * 
	 * @param params
	 * @return
	 */
	private CBankAgree newCBankAgree(String line,String fileName){
		String[] params = line.split(SPLIT_CHAR);
		CBankAgree cba = new CBankAgree();
		cba.setB_filename(fileName);
		cba.setB_ognb(params[0]);
		cba.setB_feid(params[1]);
		cba.setB_asnb(params[2]);
		cba.setB_name(params[3]);
		cba.setB_bkno(params[4]);
		cba.setB_acno(params[5]);
		cba.setB_sqno(params[6]);
		cba.setB_state(params[8]);
		cba.setB_wkdt(params[9]);
		cba.setFile_detail(line);
		return cba;
	}
	
	private CBankReturn newCBankReturn(String[] params){
		CBankReturn cbr = new CBankReturn();
		cbr.setCompany_code(params[0]);
		cbr.setBusi_type(params[1]);
		cbr.setCust_no(params[2]);
		cbr.setQc(params[3]);
		cbr.setNeed_fee(Integer.parseInt(params[4]));
		cbr.setBank_code(params[5]);
		cbr.setBank_account(params[6]);
		cbr.setXjfg(params[7]);
		cbr.setBank_trans_sn(params[8]);
		cbr.setTrans_sn(params[9]);
		cbr.setReal_fee(Integer.parseInt(params[10]));
		try {
			cbr.setTrans_time(DateHelper.parseDate(params[11], "yyyyMMdd"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		cbr.setIs_success(params[13]);
		return cbr;
	}
	
	/**
	 * 检查数据的完整性
	 * @param params
	 * @return
	 */
	private boolean validParams( String[] params){
		
		// TODO  do valid
		return false;
	}
	
	
	class DefaultFilenameFilter implements FilenameFilter{

		String startWith;
		
		public DefaultFilenameFilter(String startWith){
			this.startWith = startWith;
		}
		
		public boolean accept(File dir, String name) {
			return name.startsWith(startWith);
		}
		
	}
}
