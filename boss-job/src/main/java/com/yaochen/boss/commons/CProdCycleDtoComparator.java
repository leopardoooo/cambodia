package com.yaochen.boss.commons;

import java.io.Serializable;
import java.util.Comparator;

import com.yaochen.boss.model.CProdCycleDto;

/**
 * CProdCycleDto比较器,用于排序
 */
public class CProdCycleDtoComparator implements Comparator<CProdCycleDto>, Serializable {

	private static final long serialVersionUID = -655838680883636960L;

	public int compare(CProdCycleDto c1, CProdCycleDto c2) {

		int result=c1.getInvalid_date_num()>c2.getInvalid_date_num()?1:(c1.getInvalid_date_num()==c2.getInvalid_date_num()?0:-1);
		if(result==0)
			result=c1.getProd_sn().compareTo(c2.getProd_sn());
		return 0;
	}

}
