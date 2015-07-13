package com.ycsoft.report.web.commons;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet过滤器
 * 过滤web.xml中定义的Servlet,不被struts2处理
 * @author new
 *
 */
public class ReportServletFilter implements Filter  {

	private static final String includeServlets="includeServlets";
	private String[] servlets;

	public void init(FilterConfig config) throws ServletException {
		//web.xml中配置的servlet
		servlets= config.getInitParameter(includeServlets).split(",");
	}
	
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
	
		if(servlets!=null)
			for(String s:servlets){
				String tar=request.getRequestURI();
				if(tar.endsWith(s)){
					RequestDispatcher rd= request.getRequestDispatcher(s);
					//System.out.println("\n\nReportServletFilter:"+tar);
					rd.forward(req, res);
					return ;
				}
			}
		chain.doFilter(req, res);
	}

	
	public void destroy(){
		servlets=null;
	}

}
