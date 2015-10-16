/**
 * JVodCommandDao.java	2010/11/18
 */
 
package com.ycsoft.business.dao.core.job; 

import java.util.List;

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
		String sql = "select * from ("
				+ "select v.transnum,v.cmd_type,v.send_time,v.done_code,v.error_info,u.stb_id,u.card_id,u.modem_mac,v.is_success "
				+ " from J_VOD_COMMAND v,("
					+ " select user_id, stb_id, card_id, modem_mac from c_user where cust_id=?"
					+ " union"
					+ " select user_id, stb_id, card_id, modem_mac from c_user_his where cust_id=?"
				+ " ) u where v.user_id=u.user_id and v.cust_id= ?"
				+ " union all"
				+ " select v.transnum,v.cmd_type,v.send_time,v.done_code,v.error_info,u.stb_id,u.card_id,u.modem_mac,v.is_success " 
				+ " from J_VOD_COMMAND_his v,(" 
					+ " select user_id, stb_id, card_id, modem_mac from c_user where cust_id=?"
					+ " union"
					+ " select user_id, stb_id, card_id, modem_mac from c_user_his where cust_id=?" 
				+ " ) u where v.user_id=u.user_id and v.cust_id= ?"
				+ ") order by transnum desc";
		return createQuery(sql, custId, custId, custId, custId, custId, custId).setStart(start).setLimit(limit).page();
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
	
	public List<JVodCommand> queryCmd() throws JDBCException {
		String sql = "select * from (select * from j_vod_command where is_send='F' order by transnum) where  rownum<500 ";
		return this.createQuery(sql).list();
	}
	
	public int updateByCmd(Long transnum,String isSent,String isSuccess,String errorInfo,Integer returnCode) throws JDBCException{
		//cmd.setIs_send(SystemConstants.BOOLEAN_TRUE);
		if(isSent==null){
			isSent="F";
		}
		if(isSuccess==null){
			isSuccess="F";
		}
		if(errorInfo!=null&&errorInfo.length()>100){
			errorInfo=errorInfo.substring(0,100);
		}
		String sql="update j_vod_command set is_send=?,send_time=sysdate,is_success=?,error_info=?,return_code=? where transnum=? ";
		return this.executeUpdate(sql, isSent,isSuccess,errorInfo,returnCode,transnum);
	}
	
}
