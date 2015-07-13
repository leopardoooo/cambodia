package com.ycsoft.login;

import java.io.IOException;

import javax.servlet.http.HttpServlet;

public class InitServlet extends HttpServlet {

	public void init() {
		String webpath = this.getServletContext().getRealPath("/");
		try {
			// 读取配置文件
			LoadPropertie.getInstance(webpath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Server server = new Server();
		try{
		int ssoPort=Integer.parseInt(LoadPropertie.getInstance()
				.getProperty("soo.port"));
		server.setPort(ssoPort);
		}catch (Exception e) {
		}
		
		server.start();

	}

}
