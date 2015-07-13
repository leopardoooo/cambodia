/**
 *
 */
package com.ycsoft.business.dto.core.user;

import java.util.ArrayList;
import java.util.List;

import com.ycsoft.beans.prod.PRes;

/**
 * @author YC-SOFT
 *
 */
public class UserProdRscDto {
	private String prodId;
	private List<PRes> rscList = new ArrayList<PRes>();
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public List<PRes> getRscList() {
		return rscList;
	}
	public void setRscList(List<PRes> rscList) {
		this.rscList = rscList;
	}
}
