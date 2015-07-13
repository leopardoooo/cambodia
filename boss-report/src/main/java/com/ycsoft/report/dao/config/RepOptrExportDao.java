package com.ycsoft.report.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepOptrExport;

@Component
public class RepOptrExportDao extends BaseEntityDao<RepOptrExport> {
	public RepOptrExportDao(){}
	
	/**
	 * 获取一个操作员一个报表的导出配置
	 * @param rep_id
	 * @param optr_id
	 * @return
	 * @throws JDBCException
	 */
	public Integer[] queryExportConfig(String rep_id,String optr_id) throws JDBCException{
		String sql="select * from rep_optr_export where rep_id=? and optr_id=? ";
		List<RepOptrExport> list=this.findList(sql, rep_id,optr_id);
		Integer cols[]=new Integer[list.size()];
		for(int i=0;i<list.size();i++){
			cols[i]=list.get(i).getCol_idx();
		}
		return cols;
	}
	
	public void deletebyoptridrepid(String rep_id,String optr_id) throws JDBCException{
		String sql="delete from rep_optr_export where rep_id=? and optr_id=? ";
		this.executeUpdate(sql, rep_id,optr_id);
	}
}
