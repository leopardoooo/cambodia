/**
 * TCustColonyCfgDao.java	2012/04/24
 */
 
package com.ycsoft.business.dao.config; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TCustColonyCfg;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * TCustColonyCfgDao -> T_CUST_COLONY_CFG table's operator
 */
@Component
public class TCustColonyCfgDao extends BaseEntityDao<TCustColonyCfg> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3987347280864500418L;

	/**
	 * default empty constructor
	 */
	public TCustColonyCfgDao() {}
	public Pager<TCustColonyCfg> query(Integer start , Integer limit  ,List<SItemvalue> list,String query,String countyId)throws Exception{
		String sql = "select * from  t_cust_colony_cfg  where status = ? ";
		String itms = "";
		boolean key = false;
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql += " and county_id_for = '"+countyId+"' ";
		}
	    if(list.size()>0){
	    	itms += " and ( ";
	    	for(SItemvalue dto :list){
	    		if(dto.getItem_key().equals(DictKey.COUNTY.toString())){
	    			itms += "  county_id_for = '"+dto.getItem_value()+"' or ";
	    			key = true;
	    		}
	    		if(dto.getItem_key().equals(DictKey.CUST_COLONY.toString())){
	    			itms += "  cust_colony = '"+dto.getItem_value()+"' or ";
	    			key = true;
	    		}
	     		if(dto.getItem_key().equals(DictKey.CUST_CLASS.toString())){
	    			itms += "  cust_class = '"+dto.getItem_value()+"' or ";
	    			key = true;
	    		}
	    	}
	    	if(key){
	    		if(!"".equals(query)&& query != null){
	    			itms += " year_date like '%"+query+"%' or ";
	    		}
	    		itms = StringHelper.delEndChar( itms ,  3);
	    		itms +=" ) ";
	    	}else{
	    		if(!"".equals(query)&& query != null){
	    			itms = " and year_date like '%"+query+"%' ";
	    		}else{
	    			itms ="";
	    		}
	    	}
	    }
	    
	    sql=sql+itms;
		sql += " order by year_date desc";
		return createQuery(TCustColonyCfg.class,sql,StatusConstants.ACTIVE ).setLimit(limit).setStart(start).page();
	}
	public List<TCustColonyCfg> queryList(String yearDate,String custColony,String custClass,String countyId)throws Exception{
		String sqlsrc = "";
		if(StringHelper.isNotEmpty(custColony)){
			sqlsrc += "and cust_colony in ('"+custColony.replaceAll(",", "','")+"') ";
		}else{
			sqlsrc += "and cust_colony is null ";
		}
		if(StringHelper.isNotEmpty(custClass)){
			sqlsrc += "and cust_class in ('"+custClass.replaceAll(",", "','")+"') ";
		}else{
			sqlsrc += "and cust_class is null ";
		}
		String sql = "select * from  t_cust_colony_cfg  where year_date  in ('"+yearDate.replaceAll(",", "','")+"') " +sqlsrc+
				" and county_id_for in ('"+countyId.replaceAll(",", "','")+"') and status = ? ";
		return createQuery(TCustColonyCfg.class,sql,StatusConstants.ACTIVE ).list();
	}
	
	/**
	 * 根据客户群体和客户优惠类型查询对应的配置
	 * @param yearDate
	 * @param custColony
	 * @param custClass
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public TCustColonyCfg query(String yearDate,String custColony,String custClass,String countyId)throws Exception{
		String sqlsrc = "";
		if(StringHelper.isNotEmpty(custColony)){
			sqlsrc += "and cust_colony = '"+custColony+"' ";
		}else{
			sqlsrc += "and cust_colony is null ";
		}
		if(StringHelper.isNotEmpty(custClass)){
			sqlsrc += "and cust_class = '"+custClass+"' ";
		}else{
			sqlsrc += "and cust_class is null ";
		}
		String sql = "select * from  t_cust_colony_cfg  where year_date = ? " + sqlsrc +
				" and county_id_for = ? and status = ? ";
		return createQuery(TCustColonyCfg.class,sql,yearDate,countyId,StatusConstants.ACTIVE ).first();
	}
	
	/**
	 * 查询客户的客户群体和客户优惠类型查询已经到达使用限额的数据
	 * @param yearDate
	 * @param custColony
	 * @param custClass
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public TCustColonyCfg getCustCfg(String yearDate,String custColony,String custClass,String countyId)throws Exception{
		String sql = "select * from t_cust_colony_cfg  where use_num>=cust_num and cust_num>0 and year_date = ?  and " +
				"(( cust_colony = ? and  cust_class =? ) or ( cust_colony = ? and  cust_class is null )  or ( cust_colony is null and  cust_class =? ))" +
				" and county_id_for = ? and status = ? ";
		return createQuery(TCustColonyCfg.class,sql,yearDate,custColony,custClass,custColony,custClass,countyId,StatusConstants.ACTIVE ).first();
	}
	
	/**
	 * 根据客户群体客户优惠类型的变更，查询数量限制配置
	 * @param yearDate
	 * @param is_colony
	 * @param is_class
	 * @param custColony
	 * @param custClass
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<TCustColonyCfg> queryCfg(String yearDate,boolean is_colony,boolean is_class,String custColony,String custClass,String countyId)throws Exception{
		String sqlsrc = "";
		if(is_colony && is_class){
			sqlsrc += " and (( cust_colony = '"+custColony+"' and  cust_class ='"+custClass+"' ) or ( cust_colony = '"+custColony+"' and  cust_class is null )  or ( cust_colony is null and  cust_class ='"+custClass+"' )) ";
		}else if(is_colony){
			sqlsrc += " and (( cust_colony = '"+custColony+"' and  cust_class ='"+custClass+"' ) or ( cust_colony = '"+custColony+"' and  cust_class is null )) ";
		}else if(is_class){
			sqlsrc += "  and (( cust_colony = '"+custColony+"' and  cust_class ='"+custClass+"' ) or ( cust_colony is null and  cust_class = '"+custClass+"' ))";
		}
		
		String sql = "select * from  t_cust_colony_cfg  where year_date = ?  "+sqlsrc+" " +
				" and county_id_for = ? and status = ? ";
		return createQuery(TCustColonyCfg.class,sql,yearDate,countyId,StatusConstants.ACTIVE ).list();
	}
	
	public void delete(String yearDate,String custColony,String custClass,String countyId)throws Exception{
		String sqlsrc = "";
		if(StringHelper.isNotEmpty(custColony)){
			sqlsrc += "and cust_colony = '"+custColony+"' ";
		}else{
			sqlsrc += "and cust_colony is null ";
		}
		if(StringHelper.isNotEmpty(custClass)){
			sqlsrc += "and cust_class = '"+custClass+"' ";
		}else{
			sqlsrc += "and cust_class is null ";
		}
		String sql = "update t_cust_colony_cfg set status =?  where year_date = ? "+sqlsrc+" and county_id_for = ? ";
		executeUpdate(sql, StatusConstants.INVALID,yearDate,countyId);
	}
	
	/**
	 * 更新新的客户开户总数和用户开户总数
	 * @param yearDate
	 * @param custColony
	 * @param custClass
	 * @param countyId
	 * @param custNum
	 * @param userNum
	 * @throws Exception
	 */
	public void update(String yearDate,String custColony,String custClass,String countyId,Integer custNum,Integer userNum)throws Exception{
		String sqlsrc = "";
		if(StringHelper.isNotEmpty(custColony)){
			sqlsrc += "and cust_colony = '"+custColony+"' ";
		}else{
			sqlsrc += "and cust_colony is null ";
		}
		if(StringHelper.isNotEmpty(custClass)){
			sqlsrc += "and cust_class = '"+custClass+"' ";
		}else{
			sqlsrc += "and cust_class is null ";
		}
		String sql = "update t_cust_colony_cfg set cust_num =? ,user_num = ? where year_date = ? "+sqlsrc+" and county_id_for = ? ";
		executeUpdate(sql,custNum,userNum,yearDate,countyId);
	}
	
	/**
	 * 客户开户已使用量加1
	 * @param yearDate
	 * @param custColony
	 * @param custClass
	 * @param countyId
	 * @throws Exception
	 */
	public void add(String yearDate,boolean is_colony,boolean is_class,String custColony,String custClass,String countyId)throws Exception{
		String sqlsrc = "";
		if(is_colony && is_class){
			sqlsrc += " and (( cust_colony = '"+custColony+"' and  cust_class ='"+custClass+"' ) or ( cust_colony = '"+custColony+"' and  cust_class is null )  or ( cust_colony is null and  cust_class ='"+custClass+"' )) ";
		}else if(is_colony){
			sqlsrc += " and (( cust_colony = '"+custColony+"' and  cust_class ='"+custClass+"' ) or ( cust_colony = '"+custColony+"' and  cust_class is null )) ";
		}else if(is_class){
			sqlsrc += "  and (( cust_colony = '"+custColony+"' and  cust_class ='"+custClass+"' ) or ( cust_colony is null and  cust_class = '"+custClass+"' ))";
		}
		String sql = "update t_cust_colony_cfg set use_num = use_num+1  where status =?  and cust_num>0  and year_date = ? "+sqlsrc+" and county_id_for = ? ";
		executeUpdate(sql, StatusConstants.ACTIVE,yearDate,countyId);
	}
	
	/**
	 * 客户开户已使用量减1
	 * @param yearDate
	 * @param custColony
	 * @param custClass
	 * @param countyId
	 * @throws Exception
	 */
	public void removeNum(String yearDate,String custColony,String custClass,String countyId)throws Exception{
		String sqlsrc = "";
		if(StringHelper.isNotEmpty(custColony)){
			sqlsrc += "and cust_colony = '"+custColony+"' ";
		}else{
			sqlsrc += "and cust_colony is null ";
		}
		if(StringHelper.isNotEmpty(custClass)){
			sqlsrc += "and cust_class = '"+custClass+"' ";
		}else{
			sqlsrc += "and cust_class is null ";
		}
		String sql = "update t_cust_colony_cfg set use_num = use_num-1  where status =? and cust_num>0  and year_date = ? "+sqlsrc+"  and county_id_for = ? ";
		executeUpdate(sql, StatusConstants.ACTIVE,yearDate,countyId);
	}
	
	/**
	 * 客户属性查询已存在的用户数
	 * @param yearDate
	 * @param custColony
	 * @param custClass
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public int queryUserNum(String yearDate,String custColony,String custClass,String countyId) throws JDBCException {
		String sqlsrc = "";
		if(StringHelper.isNotEmpty(custColony)){
			sqlsrc += "and cc.cust_colony = '"+custColony+"' ";
		}
		if(StringHelper.isNotEmpty(custClass)){
			sqlsrc += "and cc.cust_class = '"+custClass+"' ";
		}
		String sql = "select count(1) from c_cust cc,c_user cu where cc.cust_id=cu.cust_id and cc.county_id=cu.county_id " +
				"and cc.county_id=? and cu.county_id=? "+sqlsrc+" and  Extract(year from cu.open_time) = ? ";
		return Integer.parseInt(findUnique(sql, countyId, countyId,yearDate));
	}
}
