package com.yaochen.boss.model;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.yaochen.boss.commons.CProdCycleDtoComparator;

/**
 * 计算产品到期日的数据bean
 * 能使用某个公用的所有产品集合
 */
public class CProdUpdate {
	
	List<CProdCycleDto> cprods;//一个客户的能使用某个公用账目的所有产品,按Invaliddatetonum从小到大的顺序
	Integer blanace;//公用账目余额
	Boolean cycle_sign;//是否包含周期性产品
	//排序比较器
	private static CProdCycleDtoComparator comparator=new CProdCycleDtoComparator();
	public Boolean getCycle_sign() {
		return cycle_sign;
	}
	public void setCycle_sign(Boolean cycle_sign) {
		this.cycle_sign = cycle_sign;
	}
	public CProdUpdate(){}
	
	/**
	 * 使用二叉树对应cprods按到期日数值排序
	 * @param cprods
	 * @return
	 */
	public List<CProdCycleDto> orderByInvalidNum(List<CProdCycleDto> cprods){
		TreeSet<CProdCycleDto> treeset=new TreeSet<CProdCycleDto>(comparator);
		for(CProdCycleDto cprod:cprods)
			treeset.add(cprod);
		cprods.clear();
		Iterator<CProdCycleDto> it= treeset.iterator();
		while(it.hasNext())
			cprods.add(it.next());
		return cprods;
	}
	/**
	 * 初始化
	 * cprods 一个客户的所有能使用公用产品列表
	 * cprods 已经按到期日大小排序，不需要使用二叉树排序
	 */
	public CProdUpdate(List<CProdCycleDto> cprods){
		for(CProdCycleDto cprod:cprods){
			if(blanace==null) blanace=cprod.getPublic_balance();
			if(cprod.getTariff_billing_cycle()!=null&&cprod.getTariff_billing_cycle()!=1){
				if(this.cycle_sign==null) this.cycle_sign=true;
				cprod.setBackup_invlaid_num(cprod.getInvalid_date_num());
				cprod.setInvalid_cycle_num(cprod.getTariff_billing_cycle()*(365.0/12));
				cprod.setTariff_cycle_rent_365(cprod.getTariff_rent_365()*cprod.getInvalid_cycle_num());
			}
		}
		//orderByInvalidNum(cprods);//cprods已经按到期日大小排序，不需要使用二叉树排序
		this.cprods=cprods;
		
	}

	public List<CProdCycleDto> getCprods() {
		return cprods;
	}
	public void setCprods(List<CProdCycleDto> cprods) {
		this.cprods = cprods;
	}
	public Integer getBlanace() {
		return blanace;
	}
	public void setBlanace(Integer blanace) {
		this.blanace = blanace;
	}

}
