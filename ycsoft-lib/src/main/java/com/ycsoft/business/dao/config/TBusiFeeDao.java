/**
 * TBusiFeeDao.java	2010/02/25
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.business.dto.config.TBusiFeeDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TBusiFeeDao -> T_BUSI_FEE table's operator
 */
@Component
public class TBusiFeeDao extends BaseEntityDao<TBusiFee> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1993163331518019442L;

	/**
	 * default empty constructor
	 */
	public TBusiFeeDao() {}

	public int updateFeeStatus(String feeId, String status) throws Exception {
		String sql ="update t_busi_fee set status=? where fee_id=?";
		return executeUpdate(sql, status, feeId);
	}

	/**
	 * 根据用户类型查询一次性费用信息
	 * @param feeType
	 * @return
	 * @throws Exception
	 */
	public List<TBusiFee> queryBusiFeeByFeeType(String feeType) throws Exception {
		String sql = "select * from t_busi_fee where status=? and fee_type=?";
		return createQuery(TBusiFee.class, sql, StatusConstants.ACTIVE, feeType).list();
	}
	/**
	 * @Description:根据费用类型查询，返回费用配置信息并分页
	 * @param status 状态
	* @param start
	* @param limit
	* @param feeType
	* @return
	* @throws Exception
	* @return Pager<TBusiFee>
	 */
	public List<TBusiFeeDto> queryFee(String query, String countyId, String status)throws Exception{

		String cond = "";
		if(StringHelper.isNotEmpty(query)){
			cond = " and fee_name like '%"+query+"%'";
		}
		
		String statusCond = "";//状态条件
		if(StringHelper.isNotEmpty(status)){
			statusCond = " and status ='"+status+"'";
		} 
		
		//非设备费，营业费
		String sql = "select t.*,'' busi_code,'' busi_name,'' buy_mode,'' buy_mode_name"
		+ " from t_busi_fee t where  t.fee_type not in (?,?)" + cond + statusCond
		+ " union all"
		//服务费
	    + " select distinct t.*,c.busi_code,c.busi_name,'' buy_mode,'' buy_mode_name"
	    + " from t_busi_fee t,t_busi_code c,t_busi_code_fee f"
	    + " where t.fee_id=f.fee_id(+) and f.busi_code=c.busi_code(+)"
	    + " and  t.fee_type=? and c.busi_fee(+)=?" + cond + statusCond
	    + " union all"
	    // 设备费且折旧
	    + " select distinct t.*,'' busi_code,'' busi_name,'' buy_mode,'' buy_mode_name"
	    + " from t_busi_fee t where t.fee_type=? and t.device_fee_type=?" + cond + statusCond
	    + " union all"
	    // 设备费且销售
	    + " select distinct t.*,'' busi_code,'' busi_name,d.buy_mode,d.buy_mode_name"
	    + " from t_busi_fee t,t_device_buy_mode_fee b,t_device_buy_mode d"
	    + " where t.fee_id=b.fee_id(+) and b.buy_mode=d.buy_mode(+)"
	    + " and t.fee_type=? and d.buy_type(+)=? and t.device_fee_type=?" + cond + statusCond;
		
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql = "select distinct t.* from (" + sql + ") t,s_county s,s_county sc"
					+ " where s.area_id=sc.county_id(+)"
					+ " and (t.county_id=s.county_id or t.county_id=sc.county_id or t.county_id=?)"
					+ " and s.county_id=?";
			return this.createQuery(TBusiFeeDto.class, sql, 
					SystemConstants.FEE_TYPE_DEVICE, SystemConstants.FEE_TYPE_BUSI,
					SystemConstants.FEE_TYPE_BUSI, SystemConstants.BOOLEAN_TRUE,
					SystemConstants.FEE_TYPE_DEVICE, SystemConstants.DEVICE_FEE_TYPE_ZJ,
					SystemConstants.FEE_TYPE_DEVICE,SystemConstants.BUY_TYPE_BUSI,SystemConstants.DEVICE_FEE_TYPE_XS,
					SystemConstants.COUNTY_ALL, countyId
				).list();
		}
		return this.createQuery(TBusiFeeDto.class, sql, 
				SystemConstants.FEE_TYPE_DEVICE, SystemConstants.FEE_TYPE_BUSI,
				SystemConstants.FEE_TYPE_BUSI, SystemConstants.BOOLEAN_TRUE,
				SystemConstants.FEE_TYPE_DEVICE, SystemConstants.DEVICE_FEE_TYPE_ZJ,
				SystemConstants.FEE_TYPE_DEVICE,SystemConstants.BUY_TYPE_BUSI,SystemConstants.DEVICE_FEE_TYPE_XS
			).list();
	}
	
	/**
	 * 根据费用类型查询业务费用
	 * @param feeType
	 * @return
	 * @throws Exception
	 */
	public List<TBusiFee> queryFeeByFeeType(String feeType) throws Exception {
		return findList(" SELECT * FROM t_busi_fee WHERE fee_type=?",
				feeType);
	}

	/**
	 * 查找设备折旧费id
	 * @return
	 * @throws Exception
	 */
	public String queryZjFeeId() throws Exception{
		return this.findUnique("select fee_id from t_busi_fee where device_fee_type=?", SystemConstants.DEVICE_FEE_TYPE_ZJ);
	}

	/**
	 * 查询非营业费
	 * @return
	 * @throws JDBCException 
	 */
	public List<TBusiFee> queryUnBusiFee() throws JDBCException {
		String sql = "select * from t_busi_fee  t where t.fee_type!='DEVICE'";
		return createQuery(TBusiFee.class, sql).list();
	}
}
