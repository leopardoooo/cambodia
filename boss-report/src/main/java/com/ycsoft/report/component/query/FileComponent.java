package com.ycsoft.report.component.query;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SResource;
import com.ycsoft.business.dao.system.SResourceDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.FileHelper;
import com.ycsoft.commons.helper.ZipHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.dao.config.RepDefineDao;
import com.ycsoft.report.dao.config.RepOptrExportDao;
import com.ycsoft.report.query.QueryManage;

/**
 * 文件管理组件 生成文件,上传文件,下载文件
 */
@Component
public class FileComponent extends BaseComponent {

	private RepDefineDao repDefineDao;
	private QueryManage queryManage;
	private RepOptrExportDao repOptrExportDao;
	private SResourceDao sResourceDao;
	
	public SResourceDao getSResourceDao() {
		return sResourceDao;
	}
	public void setSResourceDao(SResourceDao resourceDao) {
		sResourceDao = resourceDao;
	}
	
	public String saveQueryFileUpload(String fileNames, File[] files)throws Exception {
//		String[] fileNameArr = fileNames.split(",");
		String quiee_raq="";
		//数据检测quiee_raq
		
//		for(String s:fileNameArr){
			if(fileNames.endsWith("_arg.raq")){
				String temp_file=fileNames.substring(0,fileNames.length()-"_arg.raq".length());
				if(!quiee_raq.equals("")&&!quiee_raq.equals(temp_file))
					throw new Exception(fileNames+" and "+quiee_raq+" is not equals.");
				quiee_raq=temp_file;
			}else if (fileNames.endsWith(".raq")){
				String temp_file=fileNames.substring(0, fileNames.length()-".raq".length());
				if(!quiee_raq.equals("")&&!quiee_raq.equals(temp_file))
					throw new Exception(fileNames+" and "+quiee_raq+" is not equals.");
				quiee_raq=temp_file;
			}else throw new Exception(fileNames+" is not quiee_raq file.");

		String newFile=ReportConstants.CONTEXT_REAL_PATH+ File.separator + "reportFiles"+File.separator;
		for(File f:files){
			FileHelper.copyFile(f, newFile+fileNames);
		}
		return quiee_raq;
	} 
	/**
	 * 获得html导出文件名
	 * @param rep_id
	 * @return
	 * @throws ReportException
	 */
	public String queryHtmlExpFileName(String rep_id) throws ReportException{
		try {
			SResource vo=sResourceDao.findByKey(rep_id);
			if(vo==null)
				return rep_id;
			else
				return vo.getRes_name();
			//return sResourceDao.findByKey(rep_Id).getRes_name();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
		
	}
	/**
	 * 查询报表导出列配置
	 * @param optr_id
	 * @param query_id
	 * @return
	 * @throws Exception
	 */
	public Integer[] queryExportConfig(String optr_id,String query_id) throws Exception{
		
		return repOptrExportDao.queryExportConfig(queryManage.get(query_id).getRepId(), optr_id);
	}
	/**
	 * 报表查询结果导出
	 * @param query_id
	 * @param column_index_list
	 * @return
	 * @throws Exception
	 */
	public String downloadExp(String query_id,Integer... column_index_list) throws Exception{
		return queryManage.get(query_id).export(column_index_list);
	}

	/**
	 * 保存快逸报表模板
	 * @param rep_id
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public String saveQuieeRaq(String fileNames, File[] files)throws Exception {
//		String[] fileNameArr = fileNames.split(",");
		String quiee_raq="";
		//数据检测quiee_raq
		
//		for(String s:fileNameArr){
			if(fileNames.endsWith("_arg.raq")){
				String temp_file=fileNames.substring(0,fileNames.length()-"_arg.raq".length());
				if(!quiee_raq.equals("")&&!quiee_raq.equals(temp_file))
					throw new Exception(fileNames+" and "+quiee_raq+" is not equals.");
				quiee_raq=temp_file;
			}else if (fileNames.endsWith(".raq")){
				String temp_file=fileNames.substring(0, fileNames.length()-".raq".length());
				if(!quiee_raq.equals("")&&!quiee_raq.equals(temp_file))
					throw new Exception(fileNames+" and "+quiee_raq+" is not equals.");
				quiee_raq=temp_file;
			}else throw new Exception(fileNames+" is not quiee_raq file.");
//		}
		
//		for(File f:files){
//			if(f.getName().endsWith("_arg.raq")){
//				String temp_file=f.getName().substring(0,f.getName().length()-"_arg.raq".length());
//				if(!quiee_raq.equals("")&&!quiee_raq.equals(temp_file))
//					throw new Exception(f.getName()+" and "+quiee_raq+" is not equals.");
//				quiee_raq=temp_file;
//			}else if (f.getName().endsWith(".raq")){
//				String temp_file=f.getName().substring(0, f.getName().length()-".raq".length());
//				if(!quiee_raq.equals("")&&!quiee_raq.equals(temp_file))
//					throw new Exception(f.getName()+" and "+quiee_raq+" is not equals.");
//				quiee_raq=temp_file;
//			}else throw new Exception(f.getName()+" is not quiee_raq file.");
//		}
		
//		repDefineDao.updateQuieeRaq(rep_id, quiee_raq);
		String newFile=ReportConstants.CONTEXT_REAL_PATH+ File.separator + "reportFiles"+File.separator;
		for(File f:files){
			FileHelper.copyFile(f, newFile+fileNames);
		}
		return quiee_raq;
	}

	/**
	 * 下载快逸报表配置模板
	 * 
	 * @param quiee_raq
	 * @param out
	 * @throws Exception
	 */
	public void downloadQuieeRaq(String quiee_raq, OutputStream out)
			throws Exception {
		File reportFiles = new File(ReportConstants.CONTEXT_REAL_PATH+File.separator + "reportFiles");

		List<String> fileFullPathList = new ArrayList<String>();
		for (File f : reportFiles.listFiles()) {
			if (f.getName().equals(quiee_raq + ".raq")
					|| f.getName().equals(quiee_raq + "_arg.raq"))
				fileFullPathList.add(f.getAbsolutePath());
		}
		if (fileFullPathList.size() == 0)
			throw new Exception(quiee_raq + ".raq is not exists.");
		ZipHelper.zip(fileFullPathList, out);
	}

	public RepDefineDao getRepDefineDao() {
		return repDefineDao;
	}

	public void setRepDefineDao(RepDefineDao repDefineDao) {
		this.repDefineDao = repDefineDao;
	}

	public QueryManage getQueryManage() {
		return queryManage;
	}

	public void setQueryManage(QueryManage queryManage) {
		this.queryManage = queryManage;
	}

	public void setRepOptrExportDao(RepOptrExportDao repOptrExportDao) {
		this.repOptrExportDao = repOptrExportDao;
	}

}
