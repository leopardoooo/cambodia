/**
 * CCust.java	2010/02/25
 */

package com.ycsoft.beans.core.cust;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CCust -> C_CUST mapping
 */
@POJO(tn = "C_CUST", sn = "SEQ_CUST_ID", pk = "CUST_ID")
public class CCust extends BusiBase implements Serializable {

	// CCust all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6773990683472724374L;
	private String cust_id;
	private String cust_name;
	private String cust_no;
	private String addr_id;
	private String address;
	private String status;
	private String password;
	private String cust_type;
	private String cust_level;
	private String net_type;
	private String add_net_type;
	private String add_net_type_text;
	private String is_black;
	private Date open_time;
	private String cust_class_date;
	private String cust_class;
	private String cust_colony;
	private Integer cust_count;
	private String remark;
	private String old_cust_no;
	private String optr_id;
	private String str1 ;
	private String str2 ;
	private String str3 ;
	private String str4 ;
	private String str5 ;
	private String str6 ;
	private String str7 ;
	private String str8 ;
	private String str9 ;
	private String str10 ;
	private String app_code;

	private String t1 ;
	private String t2 ;
	private String t3 ;
	private String t4 ;
	private String t5 ;
	private String note ;

	private String addr_id_text;
	private String status_text;
	private String cust_type_text;
	private String cust_level_text;
	private String net_type_text;
	private String is_black_text;
	private String cust_class_text;
	private String cust_colony_text;
	
	private String unit_id;
	private String unit_name;
	
	private String mn_cust_id;
	private String mn_cust_name;
	private Integer real_cust_count;//模拟大客户的实际数量
	
	private String community;
	private String district;
	
	private String community_id;
	private String district_id;
	
	private Integer user_count;
	private Integer user_count_dtv;

	private String old_address;
	private String addr_pid;
	
	private String busi_optr_id;//小区客服人员
	private String busi_optr_name;
	private String serv_optr_id;//运维人员
	private String serv_optr_name;//运维人员
	
	private String is_bank = "F";
	
	private String spkg_sn;
	private String spkg_text;
	
	private String develop_optr_name;	//发展人
	private String login_name;
	private String str6_text;
	

	public String getStr6_text() {
		return str6_text;
	}

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getDevelop_optr_name() {
		return develop_optr_name;
	}

	public String getSpkg_sn() {
		return spkg_sn;
	}

	public void setSpkg_sn(String spkg_sn) {
		this.spkg_sn = spkg_sn;
	}

	public String getSpkg_text() {
		return spkg_text;
	}

	public void setSpkg_text(String spkg_text) {
		this.spkg_text = spkg_text;
	}

	public String getIs_bank() {
		return is_bank;
	}

	public void setIs_bank(String is_bank) {
		this.is_bank = is_bank;
	}

	/**
	 * @return the community
	 */
	public String getCommunity() {
		return community;
	}

	/**
	 * @param community the community to set
	 */
	public void setCommunity(String community) {
		this.community = community;
	}

	
	public String getCust_class_date() {
		return cust_class_date;
	}

	public void setCust_class_date(String cust_class_date) {
		this.cust_class_date = cust_class_date;
	}
	
	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * default empty constructor
	 */
	public CCust() {
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// cust_name getter and setter
	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	/**
	 * @return the cust_no
	 */
	public String getCust_no() {
		return cust_no;
	}

	/**
	 * @param cust_no
	 *            the cust_no to set
	 */
	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}

	// address getter and setter

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, this.status);
	}

	// password getter and setter
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// cust_type getter and setter
	public String getCust_type() {
		return cust_type;
	}

	public void setCust_type(String cust_type) {
		this.cust_type = cust_type;
		cust_type_text = MemoryDict.getDictName(DictKey.CUST_TYPE, this.cust_type);
	}

	// cust_level getter and setter
	public String getCust_level() {
		return cust_level;
	}

	public void setCust_level(String cust_level) {
		this.cust_level = cust_level;
		cust_level_text = MemoryDict.getDictName(DictKey.CUST_LEVEL, this.cust_level);
	}

	// net_type getter and setter
	public String getNet_type() {
		return net_type;
	}

	public void setNet_type(String net_type) {
		this.net_type = net_type;
		net_type_text = MemoryDict.getDictName(DictKey.CUST_NET_TYPE, this.net_type);
	}


	// open_time getter and setter
	public Date getOpen_time() {
		return open_time;
	}

	public void setOpen_time(Date open_time) {
		this.open_time = open_time;
	}

	public String getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}

	public String getIs_black() {
		return is_black;
	}

	public void setIs_black(String is_black) {
		this.is_black = is_black;
		is_black_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.is_black);
	}

	public String getAddr_id_text() {
		return addr_id_text;
	}

	public void setAddr_id_text(String addr_id_text) {
		this.addr_id_text = addr_id_text;
	}

	public String getStatus_text() {
		return status_text;
	}

	public String getCust_type_text() {
		return cust_type_text;
	}

	public String getCust_level_text() {
		return cust_level_text;
	}

	public String getNet_type_text() {
		return net_type_text;
	}

	public String getIs_black_text() {
		return is_black_text;
	}

	public String getCust_class() {
		return cust_class;
	}

	public void setCust_class(String cust_class) {
		this.cust_class = cust_class;
		cust_class_text = MemoryDict.getDictName(DictKey.CUST_CLASS, cust_class);
	}

	public String getCust_colony() {
		return cust_colony;
	}

	public void setCust_colony(String cust_colony) {
		this.cust_colony = cust_colony;
		cust_colony_text = MemoryDict.getDictName(DictKey.CUST_COLONY, cust_colony);
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	// t1 getter and setter
	public String getT1(){
		return t1 ;
	}

	public void setT1(String t1){
		this.t1 = t1 ;
	}

	// t2 getter and setter
	public String getT2(){
		return t2 ;
	}

	public void setT2(String t2){
		this.t2 = t2 ;
	}

	// t3 getter and setter
	public String getT3(){
		return t3 ;
	}

	public void setT3(String t3){
		this.t3 = t3 ;
	}

	// t4 getter and setter
	public String getT4(){
		return t4 ;
	}

	public void setT4(String t4){
		this.t4 = t4 ;
	}

	// t5 getter and setter
	public String getT5(){
		return t5 ;
	}

	public void setT5(String t5){
		this.t5 = t5 ;
	}

	// note getter and setter
	public String getNote(){
		return note ;
	}

	public void setNote(String note){
		this.note = note ;
	}

	public String getCust_class_text() {
		return cust_class_text;
	}

	public String getCust_colony_text() {
		return cust_colony_text;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}

	public String getStr3() {
		return str3;
	}

	public void setStr3(String str3) {
		this.str3 = str3;
	}

	public String getStr4() {
		return str4;
	}

	public void setStr4(String str4) {
		this.str4 = str4;
	}

	public String getStr5() {
		return str5;
	}

	public void setStr5(String str5) {
		this.str5 = str5;
	}

	public String getStr6() {
		return str6;
	}

	public void setStr6(String str6) {
		this.str6 = str6;
		this.str6_text = MemoryDict.getDictName(DictKey.LANGUAGE_TYPE, str6);
	}

	public String getStr7() {
		return str7;
	}

	public void setStr7(String str7) {
		this.str7 = str7;
	}

	public String getStr8() {
		return str8;
	}

	public void setStr8(String str8) {
		this.str8 = str8;
	}

	public String getStr9() {
		return str9;
	}

	public void setStr9(String str9) {
		this.str9 = str9;
		this.develop_optr_name = MemoryDict.getDictName(DictKey.OPTR, str9);
	}

	public String getStr10() {
		return str10;
	}

	public void setStr10(String str10) {
		this.str10 = str10;
	}

	public String getOld_cust_no() {
		return old_cust_no;
	}

	public void setOld_cust_no(String old_cust_no) {
		this.old_cust_no = old_cust_no;
	}

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	/**
	 * @return the user_count
	 */
	public Integer getUser_count() {
		return user_count;
	}

	/**
	 * @param user_count the user_count to set
	 */
	public void setUser_count(Integer user_count) {
		this.user_count = user_count;
	}

	public String getOld_address() {
		return old_address;
	}

	public void setOld_address(String oldAddress) {
		old_address = oldAddress;
	}

	public Integer getCust_count() {
		return cust_count;
	}

	public void setCust_count(Integer cust_count) {
		this.cust_count = cust_count;
	}

	public String getMn_cust_id() {
		return mn_cust_id;
	}

	public void setMn_cust_id(String mn_cust_id) {
		this.mn_cust_id = mn_cust_id;
	}

	public String getMn_cust_name() {
		return mn_cust_name;
	}

	public void setMn_cust_name(String mn_cust_name) {
		this.mn_cust_name = mn_cust_name;
	}

	public Integer getReal_cust_count() {
		return real_cust_count;
	}

	public void setReal_cust_count(Integer real_cust_count) {
		this.real_cust_count = real_cust_count;
	}

	public String getCommunity_id() {
		return community_id;
	}

	public void setCommunity_id(String community_id) {
		this.community_id = community_id;
	}

	public String getDistrict_id() {
		return district_id;
	}

	public void setDistrict_id(String district_id) {
		this.district_id = district_id;
	}

	public String getApp_code() {
		return app_code;
	}

	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}

	/**
	 * @return the addr_pid
	 */
	public String getAddr_pid() {
		return addr_pid;
	}

	/**
	 * @param addr_pid the addr_pid to set
	 */
	public void setAddr_pid(String addr_pid) {
		this.addr_pid = addr_pid;
	}

	/**
	 * @return the optr_id
	 */
	public String getOptr_id() {
		return optr_id;
	}

	/**
	 * @param optr_id the optr_id to set
	 */
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}

	public Integer getUser_count_dtv() {
		return user_count_dtv;
	}

	public void setUser_count_dtv(Integer user_count_dtv) {
		this.user_count_dtv = user_count_dtv;
	}

	/**
	 * @return the add_net_type
	 */
	public String getAdd_net_type() {
		return add_net_type;
	}

	/**
	 * @param add_net_type the add_net_type to set
	 */
	public void setAdd_net_type(String add_net_type) {
		this.add_net_type = add_net_type;
		if(StringHelper.isNotEmpty(this.add_net_type)){
			String [] items = this.add_net_type.split(",");
			this.add_net_type_text = "";
			for(String item:items){
				this.add_net_type_text += MemoryDict.getDictName(DictKey.USER_TYPE, item) +",";
			}
			this.add_net_type_text = this.add_net_type_text.substring(0,this.add_net_type_text.length() -1 ); 
		}
	}

	/**
	 * @return the add_net_type_text
	 */
	public String getAdd_net_type_text() {
		return add_net_type_text;
	}

	/**
	 * @return the busi_optr_id
	 */
	public String getBusi_optr_id() {
		return busi_optr_id;
	}

	/**
	 * @param busi_optr_id the busi_optr_id to set
	 */
	public void setBusi_optr_id(String busi_optr_id) {
		this.busi_optr_id = busi_optr_id;
		this.busi_optr_name = MemoryDict.getDictName(DictKey.OPTR, busi_optr_id);
	}

	/**
	 * @return the busi_optr_name
	 */
	public String getBusi_optr_name() {
		return busi_optr_name;
	}

	/**
	 * @return the serv_optr_id
	 */
	public String getServ_optr_id() {
		return serv_optr_id;
	}

	/**
	 * @param serv_optr_id the serv_optr_id to set
	 */
	public void setServ_optr_id(String serv_optr_id) {
		this.serv_optr_id = serv_optr_id;
		this.serv_optr_name = MemoryDict.getDictName(DictKey.OPTR, serv_optr_id);
	}

	/**
	 * @return the serv_optr_name
	 */
	public String getServ_optr_name() {
		return serv_optr_name;
	}
	
	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}
}