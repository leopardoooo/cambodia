package com.ycsoft.beans.prod;

import java.util.List;

public class PProdDto extends PProd {
	private List<PProdTariffDisct> tariffList;

	public List<PProdTariffDisct> getTariffList() {
		return tariffList;
	}

	public void setTariffList(List<PProdTariffDisct> tariffList) {
		this.tariffList = tariffList;
	}
	
	

}
