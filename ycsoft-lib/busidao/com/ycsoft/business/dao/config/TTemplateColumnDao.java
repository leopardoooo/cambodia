/**
 * TTemplateColumnDao.java	2013/01/14
 */
 
package com.ycsoft.business.dao.config; 

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiFeeStd;
import com.ycsoft.beans.config.TTemplateColumn;
import com.ycsoft.business.dto.config.TemplateFeeDto;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;


/**
 * TTemplateColumnDao -> T_TEMPLATE_COLUMN table's operator
 */
@Component
public class TTemplateColumnDao extends BaseEntityDao<TTemplateColumn> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9207106105901804426L;

	/**
	 * default empty constructor
	 */
	public TTemplateColumnDao() {}
	
	public void deleteColumn(String feeStdId, String optrId) throws Exception {
		String sql = "delete t_template_column_optr where column_id in (" +
			" select column_id from t_template_column t where t.optr_id=?" +
			" and t.column_id in (select column_id from t_template_fee_std where fee_std_id=?)" +
			")";
		int num = this.executeUpdate(sql, optrId, feeStdId);
		
		if(num >= 0 || num == -2){
			sql = "delete t_template_column t where t.optr_id=?" +
					" and t.column_id in (select column_id from t_template_fee_std where fee_std_id=?)";
			num = this.executeUpdate(sql, optrId, feeStdId);
			
			if(num >= 0 || num == -2){
				sql = "delete t_template_fee_std where fee_std_id=?";
				this.executeUpdate(sql, feeStdId);
			}
		}
	}
	
	public void updateColumn(TBusiFeeStd feeStd, String optrId) throws Exception {
		
		// String sql = "update (select /*+bypass_ujvc+*/ c.* from t_template_column c,t_template_fee_std fs" +
		//		" where c.column_id=fs.column_id and fs.fee_std_id=? and c.column_name=? and c.optr_id=?" +
		//		" ) t set t.min_value=?, t.max_value=?, t.default_value=?";
		String sql="update t_template_column c set c.min_value = ?, c.max_value = ?, c.default_value = ? "+
				" where c.column_id in (select fs.column_id from t_template_fee_std fs where fs.fee_std_id=?) "+
				" and c.column_name=? and c.optr_id=?";
		List<Object[]> objs = new ArrayList<Object[]>();
		List<Object> list = new ArrayList<Object>();
		/**
		list.add(feeStd.getFee_std_id());
		list.add("min_value");
		list.add(optrId);
		list.add(feeStd.getMin_value());
		list.add(feeStd.getMax_value());
		list.add(feeStd.getMin_value());
		**/		
		
		list.add(feeStd.getMin_value());
		list.add(feeStd.getMax_value());
		list.add(feeStd.getMin_value());
		list.add(feeStd.getFee_std_id());
		list.add("min_value");
		list.add(optrId);
		
		
		objs.add(list.toArray(new Object[list.size()]));
		
		list = new ArrayList<Object>();
		list.add(feeStd.getMin_value());
		list.add(feeStd.getMax_value());
		list.add(feeStd.getMax_value());
		list.add(feeStd.getFee_std_id());
		list.add("max_value");
		list.add(optrId);
		
		objs.add(list.toArray(new Object[list.size()]));
		
		list = new ArrayList<Object>();
		list.add(null);
		list.add(null);
		list.add(feeStd.getDefault_value());
		list.add(feeStd.getFee_std_id());
		list.add("default_value");
		list.add(optrId);
		
		objs.add(list.toArray(new Object[list.size()]));
		
		this.executeBatch(sql, objs);
		
	}
	
	public List<TTemplateColumn> queryColumnByOptrId(String optrId, String templateId,
			String templateType) throws Exception {
		String sql = "select c.*,fs.fee_std_id" +
				" from T_TEMPLATE_COLUMN c,T_TEMPLATE_COLUMN_OPTR co,T_TEMPLATE_FEE_STD fs" +
				" where c.column_id=co.column_id and c.column_id=fs.column_id" +
				" and c.template_id=? and co.optr_id=?" ;
		return this.createQuery(sql, templateId, optrId).list();
	}

	public List<TemplateFeeDto> queryFeeTemplateColumn(String templateId, String key) throws Exception {
		String sql = "select tc.*,tfs.fee_std_id,t.template_name,bf.fee_id,bf.fee_name," +
			"bfs.min_value fee_min_value,bfs.default_value fee_default_value,bfs.max_value fee_max_value," +
			"tt.device_buy_mode, tt.device_type" +
			" from t_template_column tc,t_template t,t_template_fee_std tfs," +
			" t_busi_fee bf,t_busi_fee_std bfs," +
			" (select distinct bfd.fee_std_id,bfd.device_buy_mode,bfd.device_type" +
			" from  t_busi_fee_device bfd ) tt" +
			" where tc.template_id=t.template_id and tc.column_id=tfs.column_id" +
			" and tfs.fee_std_id=bfs.fee_std_id and bfs.fee_id=bf.fee_id" +
			" and bfs.fee_std_id=tt.fee_std_id(+) and t.template_type='FEE'" +
			" and t.template_id=?";
		if(StringHelper.isNotEmpty(key)){
			sql += " and bf.fee_name like '%"+key+"%'";
		}
		sql += " order by tc.column_id,bf.fee_id";
		return this.createQuery(TemplateFeeDto.class, sql, templateId).list();
	}
}
