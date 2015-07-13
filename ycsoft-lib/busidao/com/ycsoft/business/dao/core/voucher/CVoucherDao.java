package com.ycsoft.business.dao.core.voucher;

/**
 * CVoucherDao.java	2011/01/25
 */
 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.util.Key;
import com.ycsoft.beans.core.voucher.CVoucher;
import com.ycsoft.business.dto.core.voucher.VoucherDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;



/**
 * CVoucherDao -> C_VOUCHER table's operator
 */
@Component
public class CVoucherDao extends BaseEntityDao<CVoucher> {
	
	/**
	 * default empty constructor
	 */
	public CVoucherDao() {}
	
	public void updateVoucherStatus(String voucherId) throws JDBCException {
		String sql = "update c_voucher set status='INVALID' where voucher_id=?";
		this.executeUpdate(sql, voucherId);
	}
	
	public void updateVoucherProcureStatus(String [] voucherIds,String countyId) throws JDBCException {
		
		String sql = "update c_voucher set is_procured='F' where for_county_id=? and " +getSqlGenerator().setWhereInArray("voucher_id",voucherIds);
		this.executeUpdate(sql, countyId);
		
		
		List<Object[]> list = createSQLQuery("select t.* from c_voucher_done_voucherid t where "  +getSqlGenerator().setWhereInArray("voucher_id",voucherIds) ).list();
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(Object[] obj: list){
			Object doneCode = obj[0];
			Object voucherId = obj[1];
			if(doneCode !=null && voucherId !=null){
				Integer counts = map.get(doneCode.toString());
				counts = null == counts ? 0 : counts;
				map.put(doneCode.toString(), counts +1);
			}
		}
		
		for(String doneCode : map.keySet()){
			String updateCount = "update c_voucher_procure set count = (count - ? ) where voucher_done_code = ?";
			executeUpdate(updateCount, map.get(doneCode),doneCode);
		}
		
		this.executeUpdate(" delete from c_voucher_done_voucherid where "  +getSqlGenerator().setWhereInArray("voucher_id",voucherIds) );
	}
	
	public void updateVoucherCounty(String[] voucherIds, String countyId) throws JDBCException {
		String sql = "update c_voucher set for_county_id=? where "+getSqlGenerator().setWhereInArray("voucher_id",voucherIds);
		this.executeUpdate(sql, countyId);
	}
	
	public List<CVoucher> queryVoucher(Long startVoucherId, Long endVoucherId, String countyId) throws JDBCException {
		String sql = "select * from c_voucher t where t.for_county_id=? and to_number(t.voucher_id) between ? and ?";
		return this.createQuery(sql, countyId, startVoucherId, endVoucherId).list();
	}
	
	/**
	 * 空闲、未失效且未领用的代金券
	 * @param startVoucherId
	 * @param endVoucherId
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<CVoucher> queryIdleVoucher(Long startVoucherId,
			Long endVoucherId, String countyId) throws JDBCException {
		String sql = "select * from c_voucher t where t.for_county_id=? and to_number(t.voucher_id) between ?"
				+ " and ? and t.status=? and to_char(t.invalid_time,'yyyy-mm-dd') >=to_char(sysdate,'yyyy-mm-dd') and t.is_procured=?";
		return this.createQuery(sql, countyId, startVoucherId, endVoucherId,
				StatusConstants.IDLE, SystemConstants.BOOLEAN_FALSE).list();
	}
	
	public Pager<CVoucher> queryProcureByDoneCode(Integer doneCode,
			Integer start, Integer limit) throws JDBCException {
		String sql = StringHelper.append("select t.* from c_voucher t where exists ",
				"(select * from c_voucher_done_voucherid c where t.voucher_id=c.voucher_id",
				" and c.voucher_done_code=?)"
		);
		return this.createQuery(sql, doneCode).setStart(start).setLimit(limit).page();
	}
	
	public Pager<CVoucher> queryVoucher(String query, Integer start,
			Integer limit, String countyId) throws JDBCException {
		String str = "";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			str = " and t.for_county_id='"+countyId+"'";
		}
		if(StringHelper.isNotEmpty(query)){
			str +=" and t.voucher_id like '%"+query+"%'";
		}
		String sql = "select * from c_voucher t where 1=1 "+str+" order by t.create_time desc";
		return this.createQuery(sql).setStart(start).setLimit(limit).page();
	}
	
	public CVoucher queryVoucherById(String voucherId, String countyId) throws JDBCException {
		String sql = "select c.* from c_voucher c where c.voucher_id=? and c.for_county_id=?";
		return this.createQuery(VoucherDto.class, sql, voucherId, countyId).first();
	}
	
	public Pager<VoucherDto> queryMulitVoucher(VoucherDto voucherDto, Integer start, Integer limit) throws JDBCException {
		String sql = "select cc.cust_name,t.*,p.procure_no,p.procure_dept,p.procurer from c_voucher t,"
			+" c_voucher_procure p,c_voucher_done_voucherid d,c_cust cc "
			+" where t.voucher_id=d.voucher_id(+) and d.voucher_done_code=p.voucher_done_code(+)"
			+" and t.for_county_id=? and cc.cust_id(+) = t.cust_id ";
		String startVoucherId = voucherDto.getStart_voucher_id();
		String endVoucherId = voucherDto.getEnd_voucher_id();
		String startCreateTime = voucherDto.getStart_create_time();
		String endCreateTime = voucherDto.getEnd_create_time();
		String startInvalidTime = voucherDto.getStart_invalid_time();
		String endInvalidTime = voucherDto.getEnd_invalid_time();
		String startUsedTime = voucherDto.getStart_used_time();
		String endUsedTime = voucherDto.getEnd_used_time();
		String status = voucherDto.getStatus();
		String isProcured = voucherDto.getIs_procured();
		Integer voucherValue = voucherDto.getVoucher_value();
		String voucher_type = voucherDto.getVoucher_type();
		
		if(StringHelper.isNotEmpty(startVoucherId)){
			sql += " and t.voucher_id>="+Long.parseLong(startVoucherId);
		}
		if(StringHelper.isNotEmpty(endVoucherId)){
			sql += " and t.voucher_id<="+Long.parseLong(endVoucherId);
		}
		if(StringHelper.isNotEmpty(voucher_type)){
			sql += " and t.voucher_type = '" + voucher_type + "' " ;
		}
		if(StringHelper.isNotEmpty(startCreateTime)){
			sql += " and to_char(t.create_time,'yyyy-mm-dd') >= '"+startCreateTime+"'";
		}
		if(StringHelper.isNotEmpty(endCreateTime)){
			sql += " and to_char(t.create_time,'yyyy-mm-dd') <= '"+endCreateTime+"'";
		}
		if(StringHelper.isNotEmpty(startInvalidTime)){
			sql += " and to_char(t.invalid_time,'yyyy-mm-dd') >= '"+startInvalidTime+"'";
		}
		if(StringHelper.isNotEmpty(endInvalidTime)){
			sql += " and to_char(t.invalid_time,'yyyy-mm-dd') <= '"+endInvalidTime+"'";
		}
		if(StringHelper.isNotEmpty(startUsedTime)){
			sql += " and to_char(t.used_time,'yyyy-mm-dd') >= '"+startUsedTime+"'";
		}
		if(StringHelper.isNotEmpty(endUsedTime)){
			sql += " and to_char(t.used_time,'yyyy-mm-dd') <= '"+endUsedTime+"'";
		}
		if(StringHelper.isNotEmpty(status)){
			sql += " and t.status in ("+sqlGenerator.in(status.split(","))+")";
		}
		if(StringHelper.isNotEmpty(isProcured)){
			sql += " and t.is_procured='"+isProcured+"'";
		}
		if(voucherValue != null){
			sql += " and t.voucher_value="+voucherValue;
		}
		return this.createQuery(VoucherDto.class, sql,
				voucherDto.getFor_county_id()).setStart(start).setLimit(limit)
				.page();
	}
	
	public String queryVoucherRule(String voucherType) throws JDBCException {
		String sql = "SELECT r.rule_str FROM c_voucher_type t,t_rule_define r where r.rule_id(+)=t.rule_id and t.voucher_type= ?";
		return this.findUnique(sql, voucherType);
	}
	
}
