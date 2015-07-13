package com.ycsoft.report.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepTotal;
@Component
public class RepTotalDao extends BaseEntityDao<RepTotal> {
	public RepTotalDao(){
		
	}
	/**
	 * 保存字段合计定义
	 * @param repTotal
	 * @throws JDBCException
	 */
	public void saveRepTotal(RepTotal repTotal) throws JDBCException{
		String column_list=repTotal.getRep_column();
		if(column_list==null||"".equals(column_list))
			return;
		for(String column:column_list.split(",")){
			if(column!=null&&!"".equals(column)){
				repTotal.setRep_column(column.trim());
				this.save(repTotal);
			}
		}
	}
	
	public void delete(String rep_id) throws JDBCException{
		this.executeUpdate("delete from rep_total where rep_id=?", rep_id);
	}
	/**
	 * 统计项字段数组
	 * @param rep_id
	 * @return
	 * @throws JDBCException
	 */
	public String[] findTotals(String rep_id) throws JDBCException {
		
		List<RepTotal> list=this.createQuery("select * from rep_total where rep_id=?", rep_id).list();
		if(list!=null&&list.size()>0){
			String[] totals=new String[list.size()];
			for(int i=0;i<list.size();i++)
				totals[i]=list.get(i).getRep_column();
			return totals;			
		}
		return null;
	}
	
	/**
	 * 统计项字段字符串','分隔
	 * @param rep_id
	 * @return
	 * @throws JDBCException
	 */
	public String findTotalList(String rep_id) throws JDBCException {
		
		List<RepTotal> list=this.createQuery("select * from rep_total where rep_id=?", rep_id).list();
		
		if(list!=null&&list.size()>0){
			String totals="";
			for(int i=0;i<list.size();i++)
				totals=totals+","+list.get(i).getRep_column();
			return totals.substring(1);			
		}
		return null;
	}
}
