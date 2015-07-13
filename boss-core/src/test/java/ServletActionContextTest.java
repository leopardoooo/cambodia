
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import com.opensymphony.xwork2.ActionContext;


/**
 * Unit test for ServletActionContext. Based loosly on Jason's ActionContextTest.
 * My first attempt at unit testing. Please hack away as needed.
 *
 */
@SuppressWarnings("unchecked")
public class ServletActionContextTest extends TestCase implements StrutsStatics {

    ActionContext actionContext;
    ServletActionContext servletActionContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockServletContext servletContext;

    @Before 
    public void setUp() {
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
    @Test
    public void testContextParams() {
        assertEquals(ServletActionContext.getRequest(), request);
        assertEquals(ServletActionContext.getResponse(), response);
        assertEquals(ServletActionContext.getServletContext(), servletContext);
    }
    @Test
    public void testGetContext() {
        ActionContext threadContext = ServletActionContext.getContext();
        assertEquals(actionContext, threadContext);
    }
}