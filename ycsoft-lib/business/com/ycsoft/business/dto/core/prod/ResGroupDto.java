/**
 *
 */
package com.ycsoft.business.dto.core.prod;

import java.util.List;

import com.ycsoft.beans.prod.PRes;
import com.ycsoft.beans.prod.PResgroup;

/**
 * @author YC-SOFT
 *
 */
public class ResGroupDto extends PResgroup {
	/**
	 *
	 */
	private static final long serialVersionUID = -5320627259366199473L;
	private int res_number;
	private List<PRes> resList;

	public int getRes_number() {
		return res_number;
	}

	public void setRes_number(int res_number) {
		this.res_number = res_number;
	}

	public List<PRes> getResList() {
		return resList;
	}

	public void setResList(List<PRes> resList) {
		this.resList = resList;
	}

}
