package com.ycsoft.business.dao.core.job;


import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.business.dto.core.prod.JCaCommandDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * JCaCommandDao -> J_CA_COMMAND table's operator
 */
@Component
public class JCaCommandDao extends BaseEntityDao<JCaCommand> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2518144229844541779L;

	/**
	 * default empty constructor
	 */
	public JCaCommandDao() {}

	/**
	 * 查询客户指令
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public Pager<JCaCommand> queryByCustId(String type ,String custId,Integer start,Integer limit) throws JDBCException{
		String sql = " select *"  
				+ " from ("
				+ " select transnum, job_id, cas_id, cas_type, user_id, cust_id, done_code, cmd_type, stb_id, card_id, prg_name, boss_res_id, control_id, auth_begin_date, auth_end_date, result_flag, error_info, area_id, is_sent, record_date, send_date, detail_params, priority, ret_date" 
				+ " from j_ca_command where cust_id=?"
				+ " union all"
			    + " select transnum, job_id, cas_id, cas_type, user_id, cust_id, done_code, cmd_type, stb_id, card_id, prg_name, boss_res_id, control_id, auth_begin_date, auth_end_date, result_flag, error_info, area_id, is_sent, record_date, send_date, detail_params, priority, ret_date" 
			    + " from j_ca_command_his where cust_id=?"
			    + " ) order by  transnum desc";
			return createQuery(sql, custId, custId).setStart(start).setLimit(limit).page();
	}

	public Pager<JCaCommand> queryCaByCardId(String[] cardIds,Integer start,Integer limit) throws JDBCException{
		String	sql = "select * from(  SELECT t.transnum,t.job_id,t.done_code,t.stb_id,t.card_id,case when  t.cmd_type = 'AddProduct' then trunc(t.auth_end_date) else null end auth_end_date," +
				" t.prg_name,t.result_flag,t.control_id,t.error_info,t.record_date,t.send_date, t.cmd_type ,c.optr_id "+
				" FROM j_ca_command t, c_done_code c " +
				" WHERE  c.done_code(+) = t.done_code " +
				" AND t.card_id in ("+sqlGenerator.in(cardIds)+") " +
				"  union all " +
				" SELECT t.transnum,t.job_id,t.done_code,t.stb_id,t.card_id,case when  t.cmd_type = 'AddProduct' then trunc(t.auth_end_date) else null end auth_end_date," +
				" t.prg_name,t.result_flag,t.control_id,t.error_info,t.record_date,t.send_date, t.cmd_type ,c.optr_id "+
				" FROM j_ca_command_his t,c_done_code c " +
				" WHERE c.done_code(+) = t.done_code " +
				" and  t.card_id in ("+sqlGenerator.in(cardIds)+") " +
				" ) ORDER BY transnum DESC ";
		return createQuery(sql).setStart(start).setLimit(limit).page();
	}
	/**
	 * 注释 shf 2012-08-24
	 * @param cardId
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException
	 */
//	public Pager<JCaCommandDto> queryByCardId(String cardId, Integer start,
//			Integer limit) throws JDBCException {
//		return createQuery(
//				JCaCommandDto.class,
//				"SELECT t.*,'终端' terminal_type_text FROM j_ca_command t where t.card_id=?",
//				cardId).setStart(start).setLimit(limit).page();
//	}
	public Pager<JCaCommandDto> queryByCardId(String cardId, Integer start,
			Integer limit) throws JDBCException {
		return createQuery(
				JCaCommandDto.class,
				"SELECT t.*,'终端' terminal_type_text FROM j_ca_command t where t.card_id=? union all "+
				"SELECT t.*,'终端' terminal_type_text FROM j_ca_command_day t where t.card_id=? union all "+
				"SELECT t.*,'终端' terminal_type_text FROM j_ca_command_out t where t.card_id=?",
				cardId,cardId,cardId).setStart(start).setLimit(limit).page();
 
	}
	public void saveCancelCaAuth(CUser user,Integer doneCode) throws Exception{
		String sql="insert into j_ca_command (TRANSNUM, JOB_ID, CAS_ID, CAS_TYPE, USER_ID, CUST_ID, "+
			       " DONE_CODE, CMD_TYPE, STB_ID, CARD_ID,PRG_NAME, BOSS_RES_ID,CONTROL_ID,"+
			       " AUTH_BEGIN_DATE, AUTH_END_DATE, RESULT_FLAG, ERROR_INFO, AREA_ID, IS_SENT, RECORD_DATE ,PRIORITY)"+				
			       " select seq_ca_transnum.nextval , ?,ts.server_id,ts.supplier_id,'"+user.getUser_id()+"',"+
				   " '"+user.getCust_id()+"','"+doneCode+"','CancelProduct','"+user.getStb_id()+"','"+user.getCard_id()+"',tsr.res_name,tsr.boss_res_id,tsr.external_res_id,"+
				   " to_char(sysdate,'yyyymmddhh24miss'),to_char(sysdate,'yyyymmddhh24miss'),'','','"+user.getArea_id()+"','N',sysdate,'10'"+
				   " from t_server_county tsc , t_server_res tsr , t_server ts  , t_server_supplier tss"+
				   " where tsc.server_id = tsr.server_id and ts.server_id = tsr.server_id and tsc.county_id='"+user.getCounty_id()+"' "+
				   " and ts.supplier_id = tss.supplier_id and tss.server_type='CA' ";
		this.executeUpdate(sql, doneCode);
	}
	
	public List<JCaCommand> queryCurrDateCommand(String jobId, String cardId)
			throws JDBCException {
		String sqlStr = "t.TRANSNUM,t.JOB_ID,t.CAS_ID,t.CAS_TYPE,t.USER_ID,t.CUST_ID,t.DONE_CODE,t.CMD_TYPE,t.STB_ID,t.CARD_ID,t.PRG_NAME,t.BOSS_RES_ID  " +
				",t.CONTROL_ID,t.AUTH_BEGIN_DATE,t.AUTH_END_DATE,decode(t.is_sent,'Y','已发_','S','调度待发','')||t.result_flag RESULT_FLAG,t.ERROR_INFO,t.AREA_ID,IS_SENT,t.RECORD_DATE,SEND_DATE,RET_DATE " +
				",t.DETAIL_PARAMS,t.PRIORITY ,s.supplier_cmd_name cmd_type_text ";
		String sql = "select "+sqlStr+",s.supplier_cmd_name cmd_type_text from j_ca_command t,(select max(supplier_cmd_name) supplier_cmd_name,supplier_id,supplier_cmd_id from  t_busi_cmd_supplier  group by supplier_id,supplier_cmd_id) s"
				+ " where t.cas_type=s.supplier_id and s.supplier_cmd_id=t.cmd_type"
				+ " and t.job_id=? and t.card_id=? "
				+ " union"
				+ " select "+sqlStr+",s.supplier_cmd_name cmd_type_text from j_ca_command_day t,(select max(supplier_cmd_name) supplier_cmd_name,supplier_id,supplier_cmd_id from  t_busi_cmd_supplier  group by supplier_id,supplier_cmd_id) s"
				+ " where t.cas_type=s.supplier_id and s.supplier_cmd_id=t.cmd_type"
				+ " and t.job_id=? and t.card_id=? "
				+ " union"
				+ " select "+sqlStr+",s.supplier_cmd_name cmd_type_text from j_ca_command_his t,(select max(supplier_cmd_name) supplier_cmd_name,supplier_id,supplier_cmd_id from  t_busi_cmd_supplier  group by supplier_id,supplier_cmd_id) s"
				+ " where t.cas_type=s.supplier_id and s.supplier_cmd_id=t.cmd_type"
				+ " and t.job_id=? and t.card_id=? "
				+ " union"
				+ " select "+sqlStr+",s.supplier_cmd_name cmd_type_text from j_ca_command_out t,(select max(supplier_cmd_name) supplier_cmd_name,supplier_id,supplier_cmd_id from  t_busi_cmd_supplier  group by supplier_id,supplier_cmd_id) s"
				+ " where t.cas_type=s.supplier_id and s.supplier_cmd_id=t.cmd_type"
				+ " and t.job_id=? and t.card_id=? ";
		return this.createQuery(sql, jobId, cardId, jobId, cardId,jobId, cardId, jobId,
				cardId).list();
	}

	/**
	 * 查询当天未发送的OSD.
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<JCaCommand> queryQueuedOsd(Integer start, Integer limit) throws Exception{
		String sql = "select t.* from j_ca_command t where t.cmd_type = 'SendOsd' order by t.RECORD_DATE ";
		return createQuery(sql).setStart(start).setLimit(limit).page();
	}
	
	/**
	 * 查询当天已发送的OSD.
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<JCaCommand> querySended(Integer start, Integer limit) throws Exception{
		String sql = "select t.* from j_ca_command_day t where t.cmd_type = 'SendOsd' order by t.RECORD_DATE";
		return createQuery(sql).setStart(start).setLimit(limit).page();
	}

	public void invalidOsd(JCaCommand cmd) throws Exception{
//		String sql = "update j_ca_command set is_sent = 'I' where transnum = ? ";
		String sql = "delete from j_ca_command where transnum = ? ";
		executeUpdate(sql, cmd.getTransnum());
	}
}
