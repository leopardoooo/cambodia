/**
 *
 */
package com.ycsoft.business.dto.system;

import com.ycsoft.beans.system.SSubSystem;

/**
 * @author liujiaqi
 *
 */
public class LoginDto extends SSubSystem {
	/**
	 *
	 */
	private static final long serialVersionUID = 9041148892466996009L;
	private String tokenId;
	private String login_name;
	private String password;

	/**
	 * @return the tokenId
	 */
	public String getTokenId() {
		return tokenId;
	}

	/**
	 * @param tokenId
	 *            the tokenId to set
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	/**
	 * @return the login_name
	 */
	public String getLogin_name() {
		return login_name;
	}

	/**
	 * @param login_name the login_name to set
	 */
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
