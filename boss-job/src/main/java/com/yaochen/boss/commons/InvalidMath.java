package com.yaochen.boss.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.yaochen.boss.model.CProdUpdate;
import com.yaochen.boss.model.CProdCycleDto;

/**
 * 使用公用后的产品到期日计算算法
 */
public class InvalidMath {

	private static  ComparatorCProd comparator=new ComparatorCProd();
	/**
	 * 按周期产品计算到期日
	 * 按周期产品的出账日期判断非周期产品能否看到这个日期
	 * 计算公式存在问题，未排除不需要继续计算的产品
	 * @param cu
	 * @throws Exception 
	 */
	public static void doMathCycle(CProdUpdate cu) throws Exception{
		
		double maxinvalidnum=doMath(cu);
		if(cu.getCycle_sign()==null||!cu.getCycle_sign()) return;
		List<CProdCycleDto> updateList=cu.getCprods();//按Invaliddatetonum从小到大的顺序
		List<CProdCycleDto> cycleList = new ArrayList<CProdCycleDto>();//周期性产品列表
		List<CProdCycleDto> normalList = new ArrayList<CProdCycleDto>();//普通包月产品列表
		
		//按非包月规则设置反推的最小周期到期日
		double backinvalidnum=0.0;
		
		for(CProdCycleDto cprod:updateList){
			if(cprod.getTariff_billing_cycle()!=1){	
				double tempinvalidnum= cprod.getBackup_invlaid_num()+
					(int)((maxinvalidnum-cprod.getBackup_invlaid_num())/cprod.getInvalid_cycle_num())*cprod.getInvalid_cycle_num();
				if(tempinvalidnum<backinvalidnum||backinvalidnum==0.0)
					backinvalidnum=tempinvalidnum;
				cprod.setInvalid_date_num(tempinvalidnum);
				cycleList.add(cprod);
			}else
				normalList.add(cprod);
		}
		//设置包月的产品的到期日为backinvalidnum
		double minbalance=0.0;//周期产品下个周期需要最小金额
		for(CProdCycleDto cprod:updateList){
			if(cprod.getTariff_billing_cycle()==1)
				cprod.setInvalid_date_num(backinvalidnum);
			else if(cprod.getInvalid_date_num()==backinvalidnum){
				if(cprod.getTariff_cycle_rent_365()<minbalance||minbalance==0.0) 
					minbalance=cprod.getTariff_cycle_rent_365();
			}
		}
		//反推剩余公用金额
		double publicbalance=getBalanceByInvalidNum(updateList, maxinvalidnum);
		//剩余的公用金额>最小的周期产品需要的金额
		while(publicbalance>=minbalance){
			
			//周期产品一次性扣费
			
			for(CProdCycleDto cprod:cycleList){
				if(cprod.getInvalid_date_num()==backinvalidnum
						&&publicbalance>=cprod.getTariff_cycle_rent_365()){
					cprod.setInvalid_date_num(cprod.getInvalid_date_num()+cprod.getInvalid_cycle_num());
					publicbalance=publicbalance-cprod.getTariff_cycle_rent_365();
				}
				
			}
			//并取下一个周期日
			double tempbackinvalidnum=backinvalidnum;
			for(CProdCycleDto cprod:cycleList){
			if(cprod.getInvalid_date_num()<backinvalidnum||tempbackinvalidnum==backinvalidnum)
				backinvalidnum=cprod.getInvalid_date_num();
			}
			
			//非周期产品计费到 下个周期日
			if(backinvalidnum!=tempbackinvalidnum){
				double ba=getBalanceByInvalidNum(normalList, backinvalidnum);
				if(publicbalance>=ba){
					publicbalance=publicbalance-ba;
					for( CProdCycleDto cprod:normalList)
						cprod.setInvalid_date_num(backinvalidnum);
				}else break;
			}
			//取下个周期的最小金额
			minbalance=0.0;
			for(CProdCycleDto cprod:cycleList){
				if(cprod.getInvalid_date_num()==backinvalidnum){
					if(cprod.getTariff_cycle_rent_365()<minbalance||minbalance==0.0) 
						minbalance=cprod.getTariff_cycle_rent_365();
				}
			}
		}
		backinvalidnum=getMaxInvalidNum(normalList, publicbalance);
		for(CProdCycleDto cprod:normalList)
			cprod.setInvalid_date_num(backinvalidnum);
	}
	/**
	 * 按普通产品计算到期日
	 * @param cu
	 * @return
	 * @throws Exception 
	 */
	public static double doMath(CProdUpdate cu) throws Exception{
		List<CProdCycleDto> updateList= cu.getCprods();//产品集合 按Invaliddatetonum从小到大的顺序
		Collections.sort(updateList,comparator);
		List<CProdCycleDto> tempnoupdateList = new ArrayList<CProdCycleDto>();//临时产品集合  到期日大于理论均化最大到期日的产品集合或过了失效日期的产品
		try{
			int expcnt=0;//在updateList中的有失效日期的产品数
			//过滤失效日期产品 按原始到期日
			for(int i=updateList.size()-1;i>=0;i--){
				CProdCycleDto cprod=updateList.get(i);
				if(cprod.getExp_date_num()!=null){
					
					if(cprod.getInvalid_date_num()>=cprod.getExp_date_num()){
						updateList.remove(i);
						tempnoupdateList.add(cprod);
					}else{
						expcnt=expcnt+1;
					}
				}
			}
			
			double publicbalance=cu.getBlanace();
			//获得理论均化的最大到期日
			double maxinvalidnum=getMaxInvalidNum(updateList, publicbalance);
			
			//过滤失效日期产品按理论均化的最大到期日
			boolean exp_check_sign=true;//判断是否继续判断失效日期
			while(expcnt>0&&exp_check_sign){
				exp_check_sign=false;
				for(int i=updateList.size()-1;i>=0;i--){
					CProdCycleDto cprod=updateList.get(i);
					if(cprod.getExp_date_num()!=null&&maxinvalidnum>cprod.getExp_date_num()){
						expcnt=expcnt-1;
						updateList.remove(i);
						tempnoupdateList.add(cprod);
						//扣除改产品到失效日期需要的金额
						publicbalance=publicbalance-cprod.getTariff_rent_365()*(cprod.getExp_date_num()-cprod.getInvalid_date_num());
						cprod.setInvalid_date_num(cprod.getExp_date_num());
						exp_check_sign=true;//有失效日期的产品过滤掉了，继续判断其他有失效日期产品
						//有失效日期的产品过滤掉了,重新计算理论均化到期日
						maxinvalidnum=getMaxInvalidNum(updateList, publicbalance);
					}
				}
			}
			
			//如果存在大于理论均化最大到期日的产品，则重新计算理论均化最大到期日
			while(updateList.size()>0&&updateList.get(updateList.size()-1).getInvalid_date_num()>maxinvalidnum){
				
				//循环判断到期日超过理论到期日的产品,和过了失效时间的产品
				for(int i=updateList.size()-1;i>=0;i--){
					if(updateList.get(i).getInvalid_date_num()>maxinvalidnum){
						tempnoupdateList.add(updateList.get(i));
						updateList.remove(i);
					}else break;
				}
				//去掉大于理论均化最大到期日产品后，重新计算理论均化到期日
				maxinvalidnum=getMaxInvalidNum(updateList, publicbalance);
			}
						
			for(CProdCycleDto cprod:updateList)
				cprod.setInvalid_date_num(maxinvalidnum);
			if(tempnoupdateList!=null)
				for(CProdCycleDto cprod:tempnoupdateList)
					updateList.add(cprod);
			return maxinvalidnum;
		}catch(Exception e){
			String prod_sn=updateList.size()>0?updateList.get(0).getProd_sn():
				(tempnoupdateList.size()>0?tempnoupdateList.get(0).getProd_sn():"无");
			throw new Exception("PROD_SN="+prod_sn+"的客户到期日计算错误",e);
		}
	}
	
	/**
	 * 计算统一到期日
	 * @return
	 */
	private static double getMaxInvalidNum(List<CProdCycleDto> updateList,double publicbalance){
		if(updateList.size()>0){
			double numerator=publicbalance;
			double denominator=0.0;
			for(CProdCycleDto cprod:updateList){
				numerator=numerator+cprod.getInvalid_date_num()*cprod.getTariff_rent_365();
				denominator=denominator+cprod.getTariff_rent_365();
			}
			return numerator/denominator;
		}
		return 0;
	}
	/**
	 * 指定一个统一到期日计算需要的公用账目是多少金额
	 * @param updateList
	 * @param InvalidNum
	 * @return
	 */
	private static double getBalanceByInvalidNum(List<CProdCycleDto> updateList,double invalidnum){
		if(updateList.size()==0) return 0;
		double balance=0;
		for(CProdCycleDto cprod:updateList)
			balance=balance+cprod.getTariff_rent_365()*(invalidnum-cprod.getInvalid_date_num());
		
		return  balance;
	}
	
	public static void main(String[] args){
		CProdCycleDto a=new CProdCycleDto();
		a.setProd_sn("1");
		a.setTariff_rent_365(30.0);
		a.setInvalid_date_num(0.0);
		a.setBackup_invlaid_num(0.0);
	    a.setExp_date_num(25.0);
		CProdCycleDto b=new CProdCycleDto();
		b.setProd_sn("2");
		b.setTariff_rent_365(20.0);
		b.setInvalid_date_num(30.0);
		b.setBackup_invlaid_num(30.0);
		CProdCycleDto c=new CProdCycleDto();
		c.setProd_sn("3");
		c.setTariff_rent_365(30.0);
		c.setInvalid_date_num(60.0);
		c.setBackup_invlaid_num(60.0);
		c.setExp_date_num(64.0);
		CProdCycleDto d=new CProdCycleDto();
		d.setProd_sn("4");
		d.setTariff_rent_365(70.0);
		d.setInvalid_date_num(65.0);
		d.setBackup_invlaid_num(65.0);
		CProdCycleDto e=new CProdCycleDto();
		e.setProd_sn("5");
		e.setTariff_rent_365(60.0);
		e.setInvalid_date_num(98.0);
		e.setBackup_invlaid_num(98.0);
		List<CProdCycleDto> cprods=new ArrayList<CProdCycleDto>();
		cprods.add(a);
		cprods.add(b);
		cprods.add(c);
		cprods.add(d);
		cprods.add(e);
		CProdUpdate cu=new CProdUpdate();
		cu.setCprods(cprods);
		cu.setBlanace(2300);
		System.out.println(InvalidMath.getMaxInvalidNum(cu.getCprods(), cu.getBlanace()));
		
		try {
			InvalidMath.doMath(cu);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		for(CProdCycleDto cp:cprods)
			System.out.println(cp.getProd_sn()+" "+cp.getExp_date_num()+" "+cp.getBackup_invlaid_num()+" "+cp.getInvalid_date_num());
		System.out.println(cu.getBlanace());
		double a1=0.0;
		for(CProdCycleDto cp:cprods)
			a1=a1+cp.getTariff_rent_365()*(cp.getInvalid_date_num()-cp.getBackup_invlaid_num());
		System.out.println(a1);
			
		
	}
}
class ComparatorCProd implements Comparator<CProdCycleDto>{
	public int compare(CProdCycleDto o1, CProdCycleDto o2) {
		if(o1.getInvalid_date_num()>o2.getInvalid_date_num())
			return 1;
		if(o1.getInvalid_date_num()<o2.getInvalid_date_num())
			return -1;
		return 0;
	}
 }