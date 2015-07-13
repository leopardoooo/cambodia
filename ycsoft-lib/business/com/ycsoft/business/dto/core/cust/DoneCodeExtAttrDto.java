package com.ycsoft.business.dto.core.cust;

import java.util.ArrayList;
import java.util.List;

public class DoneCodeExtAttrDto extends DoneCodeDto{

	/**
	 *
	 */
	private static final long serialVersionUID = -7272783868511840810L;
	private List<ExtAttributeDto> extAttrs = new ArrayList<ExtAttributeDto>();

	public List<ExtAttributeDto> getExtAttrs() {
		return extAttrs;
	}

	public void setExtAttrs(List<ExtAttributeDto> extAttrs) {
		this.extAttrs = extAttrs;
	}

}
