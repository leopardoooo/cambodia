package test.ycsoft.business.service.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import test.ycsoft.testcomm.JunitSpringBase;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("unchecked")
public class TestBusiFeeService extends  JunitSpringBase implements StrutsStatics {
    ActionContext actionContext;
    ServletActionContext servletActionContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockServletContext servletContext;
    
	@Autowired

	@Before
	public void setUp() throws Exception {
	    Map extraContext = new HashMap();

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        servletContext = new MockServletContext();

        extraContext.put(HTTP_REQUEST, request);
        extraContext.put(HTTP_RESPONSE, response);
        extraContext.put(SERVLET_CONTEXT, servletContext);

        actionContext = new ActionContext(extraContext);
        ServletActionContext.setContext(actionContext);
	}

	@After
	public void tearDown() throws Exception {
	}


}
