package com.ycsoft.business.component.core;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.commons.helper.DateHelper;
/**
 * 柬埔寨计费组件
 * @author new
 *
 */
@Component
public class BillingComponent extends BaseBusiComponent{

	/**
	 * 按指定日期重算rent_fee(覆盖订购和报停时)
	 */
	public int recalculateRentFee(CProdOrder order,Date cancelDate){
		if(order.getRent_fee()==0||order.getOrder_fee()==0||!cancelDate.after(order.getLast_bill_date())){
			//1.未计费的订购记录
			//2.免费的订购记录
			//3.当期账期未消费的订购记录
			return 0;
		}
		//退订日刚好是下次账期
		if(cancelDate.equals(order.getNext_bill_date())){
			return order.getRent_fee();
		}else{
			//按天算应该计费多少钱
			int rent_days=DateHelper.getDiffDays(order.getLast_bill_date(),cancelDate);
			// 计费金额= 计费天数*按天资费(按天资费= 订单金额/订单月数/30天 )
			int rent_fee=  Math.round((rent_days*order.getOrder_fee())/(order.getOrder_months()*30.0f));
			if(rent_fee>order.getRent_fee()){
				rent_fee=order.getRent_fee();
			}
			return rent_fee;
		}
	}
}
