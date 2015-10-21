package com.ycsoft.business.dao.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SDataTranslation;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;

@Component
public class SDataTranslationDao extends BaseEntityDao<SDataTranslation> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8492457879663131272L;

	public SDataTranslationDao(){}
	
	public int countByDataCn(String id, String dataCn) throws Exception {
		String sql = "select count(1) from s_data_translation where data_cn=?";
		if(StringHelper.isNotEmpty(id)){
			sql += " and id<>'"+id+"'";
		}
		return this.count(sql, dataCn);
	}
	
	public List<SDataTranslation> queryDataTranslation(String query) throws Exception {
		String sql = "select * from s_data_translation where data_cn like ? or data_en like ?";
		return this.createQuery(sql, "%"+query+"%", "%"+query+"%").list();
	}
	
	public Pager<SDataTranslation> queryDataTranslation(String query, Integer start, Integer limit) throws Exception {
		String sql = "select * from s_data_translation where 1=1";
		if(StringHelper.isNotEmpty(query)){
			sql += " and (data_cn like '%"+query+"%' or data_en like '%"+query+"%')";
		}
		sql += " order by data_cn desc";
		return this.createQuery(sql).setStart(start).setLimit(limit).page();
	}

}
