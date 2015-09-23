/**
 * 
 */
package test.com.ycsoft.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.DateHelper;

import test.com.ycsoft.simple.bean.A;
import test.com.ycsoft.simple.bean.A1;

/**
 * 
 * @author hh
 */
public class TestMain {
	
	/**
	 * @param args
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
//		calInvalidDate();
		System.out.println(DateHelper.isToday(DateHelper.strToDate("2015-09-23")));
	}
	
	private void printName(A a){
		A1 a1 = (A1)a;
		System.out.println(a1.getName());
		
	}
	
	private static void calInvalidDate(){
		int addMonths=0;
		int addDays =0;
		int changeBalance=15000;
		int balance=333;
		int oweFee=0;
		String servId="DTV";
		int rent=2500;
		int realFee=333;
		Date invalidDate= DateHelper.strToDate("2011-01-04");
		if (changeBalance!=0){
			if (!servId.equals(SystemConstants.PROD_SERV_ID_ATV)){
				changeBalance = changeBalance - (balance>0?0:oweFee);
				if (invalidDate.before(new Date())){
					changeBalance = changeBalance + balance - realFee;
					if (rent*1.0/30*(DateHelper.getCurrDAY()-1) == realFee) {
						//invalidDate = DateHelper.strToDate(DateHelper.getFirstDateInCurrentMonth());
					} else {
						invalidDate = new Date();
						changeBalance = changeBalance + balance - realFee;
					}
				}
			}
			
			//计费方式包月
			if (SystemConstants.BILLING_TYPE_MONTH.equals(SystemConstants.BILLING_TYPE_MONTH)){
				
				if (2==1){
					addMonths = ((changeBalance+balance)/rent - balance/rent) *rent;
				} else {
					addMonths = changeBalance/rent;
					addDays += (changeBalance - rent*addMonths)/(rent/30);
				}
			}
		}
		System.out.println(DateHelper.dateToStr(invalidDate)+"-"+addMonths+"-"+addDays);
	}

}