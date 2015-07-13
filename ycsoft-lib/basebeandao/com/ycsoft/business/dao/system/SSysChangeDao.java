/**
 * SSubSystemDao.java	2010/09/07
 */

package com.ycsoft.business.dao.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.beans.system.SSysChangeContent;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * SSysChangeDao -> S_SYS_CHANGE table's operator
 */
@Component
public class SSysChangeDao extends BaseEntityDao<SSysChange> {

	/**
	 * default empty constructor
	 */
	public SSysChangeDao() {}
	

	public List<SItemvalue> queryChangeId(SysChangeType changeType, String idDesc) throws Exception{
		String sql = "select t.key  item_value, t.key_desc|| ' - ' || t.key as item_name from s_sys_change t " +
						" where t.change_type=? and T.KEY_DESC like '%" + idDesc + "%' order by t.create_time desc";
		
		List<SItemvalue> list = createQuery(SItemvalue.class,sql, changeType.toString()).list();
		List<SItemvalue> result = new ArrayList<SItemvalue>();
		Map<String, SItemvalue> map = new HashMap<String, SItemvalue>();
		for(SItemvalue value:list){
			String key = value.getItem_value();
			if(!map.containsKey(key)){
				map.put(key, value);
			}
		}
		result.addAll(map.values());
		return result;
	}

	/**
	 * 角色变更的时候,插入异动时候重新查询下操作员的姓名,防止内存的数据未更新.
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<OptrBase> getOptrRole(String role_id) throws Exception{
		String cond=" ";
		String sql="select optr.optr_name,t1.* from s_optr_role t1,s_optr optr where t1.optr_id = optr.optr_id ";
		if(StringHelper.isNotEmpty(role_id)){
			cond = " and t1.role_id = '"+role_id+"'";
		}
		sql = sql +cond;
		return createQuery(OptrBase.class, sql).list();
	}
	/**
	 * 
	 */
	@Override
	public int[] save(SSysChange...entitys) throws JDBCException {
		if(entitys==null||entitys.length==0)
			return null;
		String sql=" insert into s_sys_change_content(change_id,idx,content) values(?,?,?)";
		for(SSysChange o:entitys){
			if(o.getChange_id()==null||o.getChange_id().trim().equals("")){
				o.setChange_id(this.findSequence().toString());
			}
			int index=o.getContent().length()/2000;
			for (int i = 0; i <= index; i++) {
				if (i == index){
					this.executeUpdate(sql, o.getChange_id(),i,o.getContent().substring(i*2000));
				}else{ 
					this.executeUpdate(sql, o.getChange_id(),i,o.getContent().substring(i*2000,(i+1)*2000));
				}
			}
		}
		return super.save(entitys);
	}
	/**
	 * 
	 * @param endDate 
	 * @param beginDate 
	 * @param changeType
	 * @param id
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<SSysChange> queryChangeInfo(Date beginDate, Date endDate, SysChangeType changeType, String id, Integer start, Integer limit) throws Exception{
		//要求结束日期的也要查到  
		if(endDate !=null){
			endDate = DateHelper.addDate(endDate, 1);
		}
		List<Object> params = new ArrayList<Object>();
		String sql = "select t.* from S_SYS_CHANGE t where t.change_type = ? ";
		params.add(changeType.toString());
		if(StringHelper.isNotEmpty(id)){
			sql += " and  t.key = ? ";
			params.add(id);
		}
		
		if(null == beginDate && null!=endDate){//结束时间为空
			sql += " and t.create_time < ? ";
			params.add(endDate);
		}else if(null != beginDate && null==endDate){//开始时间为空
			sql += " and t.create_time > ? ";
			params.add(beginDate);
		}else if(null != beginDate && null!=endDate){//两者都不为空
			sql += " and t.create_time between ? and ? ";
			params.add(beginDate);
			params.add(endDate);
		}
		
		sql += "order by t.create_time desc ";
		
		Pager<SSysChange> page= createQuery(sql,params.toArray() ).setLimit(limit).setStart(start).page();
		StringBuilder buffer=new StringBuilder();
		for(SSysChange o: page.getRecords()){
			sql="select * from S_SYS_CHANGE_CONTENT where change_id=? order by idx";
			List<SSysChangeContent> contentDetail = this.createQuery(SSysChangeContent.class, sql, o.getChange_id()).list();
			for(SSysChangeContent con: contentDetail){
				buffer.append(con.getContent());
			}
			o.setContent(buffer.toString());
			buffer.delete(0, buffer.length());
		}
		return page;
	}

}
