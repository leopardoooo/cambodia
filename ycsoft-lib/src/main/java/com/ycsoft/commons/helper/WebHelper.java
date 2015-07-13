package com.ycsoft.commons.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WEB辅助类，包括Servlet 四大作用域的处理
 *
 * @author hh
 * @date Dec 3, 2009 1:27:02 PM
 * @see javax.servlet.http.HttpServletRequest
 */
public class WebHelper {

	private WebHelper(){
	}


	/**
	 * 通过名称，获取客户端的Cookie
	 * @param request
	 */
	public static Cookie getCookie( HttpServletRequest request , String key ){
		Cookie[] cookies = request.getCookies();
		if( null == cookies){
			return null ;
		}
		for (Cookie ck : cookies) {
			if ( ck.getName().equals( key ) ){
				return ck ;
			}
		}
		return null ;
	}

	/**
	 * 通过请求与名称，获取客户端的Cookie value值
	 *
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getCookieValue( HttpServletRequest request , String key ){
		Cookie ck = getCookie(request, key);
		if(null != ck){
			return ck.getValue() ;
		}
		return null ;
	}

	/**
	 * 添加cookie , 默认保存一年
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void addCookieAndOneYear(HttpServletResponse response , String name , String value){
		addCookie(response, name, value , 365 * 24 * 3600 );
	}
	/**
	 * 添加cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param ex 期限
	 */
	public static void addCookie(HttpServletResponse response , String name , String value, int ex){
		Cookie ck = new Cookie( name ,value);
		ck.setMaxAge( ex );
		response.addCookie( ck );

	}

}
