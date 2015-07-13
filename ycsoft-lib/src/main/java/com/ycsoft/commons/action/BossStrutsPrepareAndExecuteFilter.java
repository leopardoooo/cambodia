package com.ycsoft.commons.action;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

/**
 * @author liujiaqi
 * 增加过滤的url功能，数据在web.xml中配置，路径用’,’分割
 * 增加如下代码
 * 		<init-param>
			<param-name>excludeUri</param-name>
			<param-value>/urla/,/urlb/</param-value>
		</init-param>
 * 
 */
public class BossStrutsPrepareAndExecuteFilter extends
		StrutsPrepareAndExecuteFilter {

	private String[] excludeUri;

	public void init(FilterConfig filterConfig) throws ServletException {
		excludeUri = filterConfig.getInitParameter("excludeUri").split(",");
		super.init(filterConfig);
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;

		final String uri = request.getRequestURI();
		final String contextPath = request.getContextPath();

		for (int i = 0; i < excludeUri.length; i++) {
			if (uri.indexOf(excludeUri[i], contextPath.length()) > -1) {
				chain.doFilter(req, res);
				return;
			}
		}
		super.doFilter(req, res, chain);
	}

}
