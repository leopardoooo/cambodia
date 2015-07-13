package com.ycsoft.sysmanager.dto.prod;

import java.util.List;

import com.ycsoft.beans.prod.PProdDynRes;
import com.ycsoft.beans.prod.PResgroup;
import com.ycsoft.beans.prod.PResgroupRes;

public class PProdDynResDto extends PProdDynRes {

	/**
	 *
	 */
	private static final long serialVersionUID = 325511379434923737L;

	private List<PResgroupRes> groupResList;

	private PResgroup pResgroup;

	public PResgroup getPResgroup() {
		return pResgroup;
	}

	public void setPResgroup(PResgroup resgroup) {
		pResgroup = resgroup;
	}

	public List<PResgroupRes> getGroupResList() {
		return groupResList;
	}

	public void setGroupResList(List<PResgroupRes> groupResList) {
		this.groupResList = groupResList;
	}
}
