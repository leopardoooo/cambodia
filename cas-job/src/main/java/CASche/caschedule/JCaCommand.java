/**
 * JCaCommand.java	2010/09/22
 */

package CASche.caschedule;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;



/**
 * JCaCommand -> J_CA_COMMAND mapping
 */
public class JCaCommand  implements Serializable {

	// JCaCommand all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2766430818806493825L;
	private Long transnum;
	private Long job_id;
	private String cas_id;
	private String cas_type;
	private String user_id;
	private String cust_id;
	private Long   done_code;
	private String cmd_type;
	private String stb_id;
	private String card_id;
	private String prg_name;
	private String boss_res_id;
	private String control_id;
	private String auth_begin_date;
	private String auth_end_date;
	private String area_id;
	private String result_flag;
	private String error_info;
	private String is_sent;
	private Timestamp record_date;
	private Timestamp send_date;
	private String detail_params;
	private Integer priority;
	private Timestamp ret_date;
	
	public Timestamp getRet_date() {
		return ret_date;
	}
	public void setRet_date(Timestamp ret_date) {
		this.ret_date = ret_date;
	}
	public JCaCommand esaycopyBean(){
		JCaCommand cmd=new JCaCommand();
		cmd.transnum=transnum.longValue();
		cmd.job_id=job_id.longValue();
		cmd.cas_id=cas_id;
		cmd.cas_type=cas_type;
		cmd.user_id=user_id;
		cmd.cust_id=cust_id;
		cmd.done_code=done_code.longValue();
		cmd.cmd_type=cmd_type;
		cmd.stb_id=stb_id;
		cmd.card_id=card_id;
		
		cmd.auth_begin_date=auth_begin_date;
		cmd.auth_end_date=auth_end_date;
		cmd.area_id=area_id;
		
		cmd.is_sent=is_sent;
		cmd.record_date=record_date;
		cmd.priority=priority.intValue();
		return cmd;
	}
	public JCaCommand copyBean(){
		JCaCommand cmd=this.esaycopyBean();
		cmd.prg_name=prg_name;
		cmd.boss_res_id=boss_res_id;
		cmd.control_id=control_id;
		cmd.detail_params=detail_params;
		return cmd;
	}
	public Long getTransnum() {
		return transnum;
	}
	public void setTransnum(Long transnum) {
		this.transnum = transnum;
	}
	public Long getJob_id() {
		return job_id;
	}
	public void setJob_id(Long job_id) {
		this.job_id = job_id;
	}
	public String getCas_id() {
		return cas_id;
	}
	public void setCas_id(String cas_id) {
		this.cas_id = cas_id;
	}
	public String getCas_type() {
		return cas_type;
	}
	public void setCas_type(String cas_type) {
		this.cas_type = cas_type;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public Long getDone_code() {
		return done_code;
	}
	public void setDone_code(Long done_code) {
		this.done_code = done_code;
	}
	public String getCmd_type() {
		return cmd_type;
	}
	public void setCmd_type(String cmd_type) {
		this.cmd_type = cmd_type;
	}
	public String getStb_id() {
		return stb_id;
	}
	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getPrg_name() {
		return prg_name;
	}
	public void setPrg_name(String prg_name) {
		this.prg_name = prg_name;
	}
	public String getBoss_res_id() {
		return boss_res_id;
	}
	public void setBoss_res_id(String boss_res_id) {
		this.boss_res_id = boss_res_id;
	}
	public String getControl_id() {
		return control_id;
	}
	public void setControl_id(String control_id) {
		this.control_id = control_id;
	}
	public String getAuth_begin_date() {
		return auth_begin_date;
	}
	public void setAuth_begin_date(String auth_begin_date) {
		this.auth_begin_date = auth_begin_date;
	}
	public String getAuth_end_date() {
		return auth_end_date;
	}
	public void setAuth_end_date(String auth_end_date) {
		this.auth_end_date = auth_end_date;
	}
	public String getResult_flag() {
		return result_flag;
	}
	public void setResult_flag(String result_flag) {
		this.result_flag = result_flag;
	}
	public String getError_info() {
		return error_info;
	}
	public void setError_info(String error_info) {
		this.error_info = error_info;
	}
	public String getIs_sent() {
		return is_sent;
	}
	public void setIs_sent(String is_sent) {
		this.is_sent = is_sent;
	}
	public Timestamp getRecord_date() {
		return record_date;
	}
	public void setRecord_date(Timestamp record_date) {
		this.record_date = record_date;
	}
	public Timestamp getSend_date() {
		return send_date;
	}
	public void setSend_date(Timestamp send_date) {
		this.send_date = send_date;
	}
	public String getDetail_params() {
		return detail_params;
	}
	public void setDetail_params(String detail_params) {
		this.detail_params = detail_params;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

}