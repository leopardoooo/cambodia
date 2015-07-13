package com.ycsoft.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ycsoft.business.dto.system.SsoDto;
import com.ycsoft.commons.helper.JsonHelper;

public class Online extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5573839823495749571L;

	/**
	 * Constructor of the object.
	 */
	public Online() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("text/json;charset=utf-8");
			PrintWriter out = response.getWriter();
			List<OnlineUser> onlineList = new ArrayList<OnlineUser>();
			String countyId = request.getParameter("countyId");

			Iterator<String> it = OnlineUser.map.keySet().iterator();
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				OnlineUser ou = OnlineUser.map.get(key);
				if (countyId.equals("4501")
						|| countyId.equals(ou.getOptr().getCounty_id()) || countyId.equals(ou.getOptr().getArea_id())) {
					if ((ou.getLastResTime() == null && new Date().getTime()
							- ou.getLoginTime().getTime() < 60 * 60 * 1000 * 48)
							|| (new Date().getTime()
									- ou.getLastResTime().getTime() < 60 * 60 * 1000 * 48))
						onlineList.add(ou);
				}
			}
			out.print(JsonHelper.fromObject(onlineList));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
