package test.ycsoft.business.service.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.PackageGroupUser;
import com.ycsoft.business.service.impl.OrderService;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.helper.DateHelper;

import test.ycsoft.testcomm.JunitSpringBase;

public class testOrderAction extends JunitSpringBase {
	
	@Autowired
	private OrderService orderService;

	@Test 
	public void test1() throws Exception{
		
		//OrderService orderService=this.applicationContext.getBean(OrderService.class);
		
		System.out.println("####AAAAAAAAAAAAAA"+orderService.queryOrderableProd(BusiCodeConstants.PROD_PACKAGE_ORDER,
				"11078637", null, null));
		
		//保存单产品
		/**cust_id=11078537
			user_id=1302200
			prod_id=604
			tariff_id=1494
				**/
		OrderProd order=new OrderProd();
		order.setCust_id("11078537");
		order.setUser_id("1302200");
		order.setProd_id("604");
		order.setTariff_id("1494");
		order.setOrder_months(1);
		order.setPay_fee(1);
		order.setTransfer_fee(0);
		order.setEff_date(DateHelper.today());
		order.setExp_date(DateHelper.getNextMonthByNum(DateHelper.today(), 1));
		
		SOptr soptr=new SOptr();
		soptr.setOptr_id("test");
		
		BusiParameter parm=new BusiParameter();
		parm.setOptr(soptr);
		orderService.setParam(parm);
		
		
		orderService.saveOrderProd(order, BusiCodeConstants.PROD_SINGLE_ORDER);
		
		//订购一个套餐
		order.setProd_id("101");
		order.setTariff_id("1000");
		
		List<PackageGroupUser> groupSe=new ArrayList<>();
		PackageGroupUser u=new PackageGroupUser();
		groupSe.add(u);
		u.setPackage_group_id("11");
		u.setUserSelectList(Arrays.asList("1302501"));
		
		u=new PackageGroupUser();
		groupSe.add(u);
		u.setPackage_group_id("12");
		u.setUserSelectList(Arrays.asList("1302300"));
		order.setGroupSelected(groupSe);
		
		orderService.saveOrderProd(order, BusiCodeConstants.PROD_PACKAGE_ORDER);
	}
	@Test
	public void test2() throws Exception{
		OrderProd order=new OrderProd();
		order.setCust_id("11078637");
		order.setUser_id("1302501");
		order.setProd_id("603");
		order.setTariff_id("1491");
		order.setOrder_months(1);
		order.setPay_fee(1);
		order.setTransfer_fee(0);
		order.setEff_date(DateHelper.today());
		order.setExp_date(DateHelper.getNextMonthByNum(DateHelper.today(), 1));
		
		orderService.queryTransferFee(order, BusiCodeConstants.PROD_SINGLE_ORDER);
		
	}
}
