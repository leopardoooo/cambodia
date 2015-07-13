

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.struts2.StrutsStatics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.ycsoft.testcomm.JunitSpringBase;

import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.daos.core.JDBCException;

import exp.ExpUnit;

@SuppressWarnings("unchecked")
public class TestExpUnit extends  JunitSpringBase implements StrutsStatics {
	
	ExpUnit exp = new ExpUnit();
	
	@Autowired
	private CUserDao cUserDao;
	@Autowired
	private ExpressionUtil expressionUtil;
	
	@Test
	public void test1() throws JDBCException{
		CUser user = cUserDao.findByKey("4210");
		exp.setVariable(user);
		exp.setVariable("kckt","KCKT");
		
		

		String expStr = "stop_type==#kckt";
		Assert.assertEquals( exp.parseBoolean(expStr),true);
		
		expStr = "stop_type='abc' ";
		exp.getVariable(expStr);
		
		System.out.println(exp.getVariable("cust_id"));
		System.out.println(exp.getVariable("stop_type"));
		
	}

	@Test
	public void test2() throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("custId", "331538");
		System.out.print(expressionUtil.parseBoolean("490", params)); 
	}
	@Test
	public void test4() throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("custId", "100000280");
		System.out.print(expressionUtil.parseBoolean("406", params)); 
	}
	
	@Test
	public void test3() throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("feeSn", new String[]{"10001166"});
		System.out.print(expressionUtil.parseBoolean("426", params)); 
	}
	/**
	 * @param userDao the cUserDao to set
	 */
	public void setCUserDao(CUserDao userDao) {
		cUserDao = userDao;
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * @return the expressionComponent
	 */
	public ExpressionUtil getExpressionUtil() {
		return expressionUtil;
	}

	/**
	 * @param expressionComponent the expressionComponent to set
	 */
	public void setExpressionUtil(ExpressionUtil expressionUtil) {
		this.expressionUtil = expressionUtil;
	}


}
