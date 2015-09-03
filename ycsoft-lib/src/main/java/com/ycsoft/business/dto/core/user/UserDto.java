/**
 *
 */
package com.ycsoft.business.dto.core.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

/**
 * @author liujiaqi
 *
 */
public class UserDto extends CUser {
	/**
	 *
	 */
	private static final long serialVersionUID = 5137001993928060887L;
	private String terminal_type ;
	private String serv_type ;
	private String password;

	private String terminal_type_text;
	private String serv_type_text;
	private List<CProd> prods = new ArrayList<CProd>();

	private String check_type;
	private String login_name;
	private String login_password;
	private String bind_type;
	private Integer max_connection;
	private Integer max_user_num;

	private String check_type_text;
	private String bind_type_text;
	
	private Integer user_count;//模拟用户终端数量
	
	private String rejectRes;//用户排斥的资源
	
	private String stbModel;
	private String cardModel;
	
	private String newPassword;
	private Date stop_date;
	
	private String device_model;
	private String device_model_text;
	private String buy_model;
	private String buy_model_text;

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getBuy_model() {
		return buy_model;
	}
	
	public void setDevice_model_text(String device_model_text) {
		this.device_model_text = device_model_text;
	}

	public void setBuy_model(String buy_model) {
		this.buy_model = buy_model;
	}
	
	public String getDevice_model_text() {
		return device_model_text;
	}

	public void setBuy_model_text(String buy_model_text) {
		this.buy_model_text = buy_model_text;
	}
	
	public String getBuy_model_text() {
		return buy_model_text;
	}

	/**
	 * @return the prods
	 */
	public List<CProd> getProds() {
		return prods;
	}

	public Integer getMax_user_num() {
		return max_user_num;
	}

	public void setMax_user_num(Integer maxUserNum) {
		max_user_num = maxUserNum;
	}

	/**
	 * @param prods
	 *            the prods to set
	 */
	public void setProds(List<CProd> prods) {
		this.prods = prods;
	}

	public String getTerminal_type() {
		return terminal_type;
	}

	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
		this.terminal_type_text = MemoryDict.getDictName(DictKey.TERMINAL_TYPE, terminal_type);
	}

	public String getServ_type() {
		return serv_type;
	}

	public void setServ_type(String serv_type) {
		this.serv_type = serv_type;
	}

	public String getTerminal_type_text() {
		return terminal_type_text;
	}

	public void setTerminal_type_text(String terminal_type_text) {
		this.terminal_type_text = terminal_type_text;
	}

	public String getServ_type_text() {
		return MemoryDict.getDictName(getUser_type()+"_SERV_TYPE", serv_type);
	}

	// login_name getter and setter
	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	// password getter and setter
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// bind_type getter and setter
	public String getBind_type() {
		return bind_type;
	}

	public void setBind_type(String bind_type) {
		this.bind_type = bind_type;
		bind_type_text = MemoryDict.getDictName(DictKey.BAND_BIND_TYPE, bind_type);
	}

	// max_connection getter and setter
	public Integer getMax_connection() {
		if (max_connection == null)
			return 1;
		return max_connection;
	}

	public void setMax_connection(Integer max_connection) {
		this.max_connection = max_connection;
	}

	/**
	 * @return the check_type
	 */
	public String getCheck_type() {
		return check_type;
	}

	/**
	 * @param check_type
	 *            the check_type to set
	 */
	public void setCheck_type(String check_type) {
		this.check_type = check_type;
		check_type_text = MemoryDict.getDictName(DictKey.BAND_CHECK_TYPE, check_type);
	}

	public String getCheck_type_text() {
		return check_type_text;
	}

	public String getBind_type_text() {
		return bind_type_text;
	}

	public String getRejectRes() {
		return rejectRes;
	}

	public void setRejectRes(String rejectRes) {
		this.rejectRes = rejectRes;
	}

	public Integer getUser_count() {
		return user_count;
	}

	public void setUser_count(Integer user_count) {
		this.user_count = user_count;
	}

	public String getStbModel() {
		return stbModel;
	}

	public void setStbModel(String stbModel) {
		this.stbModel = stbModel;
	}

	public String getCardModel() {
		return cardModel;
	}

	public void setCardModel(String cardModel) {
		this.cardModel = cardModel;
	}


	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getLogin_password() {
		return login_password;
	}

	public void setLogin_password(String loginPassword) {
		login_password = loginPassword;
	}

	public Date getStop_date() {
		return stop_date;
	}

	public void setStop_date(Date stopDate) {
		stop_date = stopDate;
	}

}
