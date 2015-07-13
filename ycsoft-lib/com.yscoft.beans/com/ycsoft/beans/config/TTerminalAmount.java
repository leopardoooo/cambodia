/**
 * TTerminalAmount.java	2010/10/19
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TTerminalAmount -> T_TERMINAL_AMOUNT mapping
 */
@POJO(
	tn="T_TERMINAL_AMOUNT",
	sn="",
	pk="")
public class TTerminalAmount implements Serializable {

	// TTerminalAmount all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -8142026538220820625L;
	private String template_id ;
	private String cust_type ;
	private String user_type ;
	private String terminal_type ;
	private Integer amount ;

	/**
	 * default empty constructor
	 */
	public TTerminalAmount() {}


	// template_id getter and setter
	public String getTemplate_id(){
		return template_id ;
	}

	public void setTemplate_id(String template_id){
		this.template_id = template_id ;
	}

	// cust_type getter and setter
	public String getCust_type(){
		return cust_type ;
	}

	public void setCust_type(String cust_type){
		this.cust_type = cust_type ;
	}

	// user_type getter and setter
	public String getUser_type(){
		return user_type ;
	}

	public void setUser_type(String user_type){
		this.user_type = user_type ;
	}

	// terminal_type getter and setter
	public String getTerminal_type(){
		return terminal_type ;
	}

	public void setTerminal_type(String terminal_type){
		this.terminal_type = terminal_type ;
	}

	// amount getter and setter
	public Integer getAmount(){
		return amount ;
	}

	public void setAmount(Integer amount){
		this.amount = amount ;
	}

}