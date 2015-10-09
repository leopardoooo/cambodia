/**
 *
 */
package com.ycsoft.business.dto.config;

import com.ycsoft.beans.task.WTaskBaseInfo;
import com.ycsoft.commons.helper.StringHelper;

/**
 * @author YC-SOFT
 *
 */
public class TaskBaseInfoDto extends WTaskBaseInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7637497351437559729L;
	/**
	 *
	 */
	private String address;
	private String team_type;

	
	public String getTeam_type() {
		return team_type;
	}


	public void setTeam_type(String team_type) {
		this.team_type = team_type;
	}


	public String getAddress() {
		if(StringHelper.isNotEmpty(getOld_addr())){
			address = "old:"+getOld_addr()+",new:"+getNew_addr();
		}else{
			address = getNew_addr();
		}
		return address;
	}
	
}
