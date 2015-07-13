

import org.apache.struts2.StrutsStatics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.ycsoft.testcomm.JunitSpringBase;

import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.daos.core.JDBCException;

@SuppressWarnings("unchecked")
public class CopyOfTestExpUnit extends  JunitSpringBase implements StrutsStatics {
	
	@Autowired
	private CUserDao cUserDao;
	@Autowired
	private ExpressionUtil expressionUtil;
	
	@Test
	public void test1() throws JDBCException{
		
		org.junit.Assert.assertTrue(expressionUtil.parseBoolean(""));
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
	 * @param expressionUtil the expressionUtil to set
	 */
	public void setExpressionUtil(ExpressionUtil expressionUtil) {
		this.expressionUtil = expressionUtil;
	}






}
