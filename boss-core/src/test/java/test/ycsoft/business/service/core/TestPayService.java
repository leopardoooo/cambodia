package test.ycsoft.business.service.core;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.service.impl.PayService;
import com.ycsoft.commons.helper.JsonHelper;

import test.ycsoft.testcomm.JunitSpringBase;

public class TestPayService extends JunitSpringBase{
	@Autowired
	private PayService payService;
	
	
	
	@Test
	@Rollback(true) //true表示事物最终执行回滚，false表示事物最终执行提交
	public void testPay() throws Exception{
		
		
		System.out.println(payService.queryExchage());
		String cust_id="11078537";
		System.out.println("SUM"+ payService.queryUnPaySum(cust_id));
		System.out.println(JsonHelper.fromObject(payService.queryUnPayDetail(cust_id)));
		
		//测试支付
		SOptr soptr=new SOptr();
		soptr.setOptr_id("test");
		soptr.setDept_id("21");
		soptr.setCounty_id("5001");
		soptr.setArea_id("5000");
		BusiParameter parm=new BusiParameter();
		payService.setParam(parm);
		parm.setOptr(soptr);
		CCust cust=new CCust();
		cust.setCust_id(cust_id);
		CustFullInfoDto custinfo=new CustFullInfoDto();
		custinfo.setCust(cust);
		parm.setCustFullInfo(custinfo);
		
		parm.setBusiCode("1");

		CFeePayDto pay=new CFeePayDto();
		parm.setPay(pay);
		
		pay.setAcct_date(new Date());
		pay.setPay_type("XJ");
		pay.setInvoice_mode("A");
		pay.setPayer("大大");
		
		pay.setExchange(4000);
		pay.setUsd(34900);
		pay.setKhr(400000);
		
		pay.setCust_id(cust_id);
		 
		
		payService.savePay(pay);
		
		
		
	}

}
