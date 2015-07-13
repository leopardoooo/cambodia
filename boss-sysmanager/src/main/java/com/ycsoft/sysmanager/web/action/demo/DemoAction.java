package com.ycsoft.sysmanager.web.action.demo;

import org.springframework.stereotype.Controller;

import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.sysmanager.component.demo.DemoComponent;


/**
 * Demo演示
 *
 * @author hh
 * @data Mar 24, 2010 9:36:04 AM
 */
@Controller
public class DemoAction extends BaseAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -2125605256674501248L;
	private DemoComponent demoComponent;

	/**
	 * 根据条件查询操作员的信息，并进行分页
	 * @return
	 * @throws Exception
	 */
	public String queryOptrs()throws Exception{
		String keyword = request.getParameter("query").toString();
		getRoot().setPage( demoComponent.queryOptrs( start , limit , keyword ) );
		return JSON_PAGE;
	}

	/**
	 * 注销操作员
	 * @return
	 * @throws Exception
	 */
	public String logoff()throws Exception{
		String optr_id = request.getParameter("optr_id");
		demoComponent.logoffOptr( optr_id );
		return JSON ;
	}

	public DemoComponent getDemoComponent() {
		return demoComponent;
	}

	public void setDemoComponent(DemoComponent demoComponent) {
		this.demoComponent = demoComponent;
	}
}
