/**
 * JVodCommandDao.java	2010/11/18
 */
 
package com.ycsoft.business.dao.core.job; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JVodCommand;
import com.ycsoft.business.dto.core.prod.JVodCommandDto;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * JVodCommandDao -> J_VOD_COMMAND table's operator
 */
@Component
public class JVodCommandDao extends BaseEntityDao<JVodCommand> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2602038414565806180L;

	/**
	 * default empty constructor
	 */
	public JVodCommandDao() {}

	public Pager<JVodCommand> queryByCustId(String type,String custId,Integer start,Integer limit) throws JDBCException {
		String sql = "select v.transnum,v.cmd_type,v.send_time,v.done_code,v.error_info,u.card_id," +
				" CASE when v.error_info = 'null' AND v.is_success ='Y' then 'Y' " +
				" when v.error_info  is null AND v.is_success is null then 'F' else 'N' end is_success " +
				" from "+type+" v,(" +
					"select user_id,card_id from c_user where cust_id=?"+
					" union"+
					" select user_id,card_id from c_user_his where cust_id=?" +
				" ) u where v.user_id=u.user_id and v.cust_id= ? order by v.transnum desc";
		return createQuery(sql, custId, custId, custId).setStart(start).setLimit(limit).page();
	}
	public Pager<JVodCommandDto> queryByCardId(String cardId,Integer start,Integer limit) throws JDBCException {
		String sql = " select cu.card_id,jvc.cmd_type,jvc.is_send,jvc.create_time,jvc.send_time,jvc.is_success,jvc.error_info"+
					" from j_vod_command jvc , busi.c_user cu"+
					" where jvc.user_id = cu.user_id and cu.card_id=?"+
					"  union all"+
					" select cu.card_id,jvch.cmd_type,jvch.is_send,jvch.create_time,jvch.send_time,jvch.is_success,jvch.error_info"+
					" from j_vod_command_his jvch , busi.c_user cu"+
					" where jvch.user_id = cu.user_id and cu.card_id=?";
		return createQuery(JVodCommandDto.class,sql, cardId,cardId).setStart(start).setLimit(limit).page();
	}
}
