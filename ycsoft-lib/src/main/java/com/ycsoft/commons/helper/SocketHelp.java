package com.ycsoft.commons.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketHelp {

	public static String sendData(String ip, int port, String data)
			throws IOException {
		StringBuffer result = new StringBuffer(1024 * 10);
		Socket clientSocket = null;
		BufferedReader reader = null;
		PrintWriter out = null;
		try {
			clientSocket = new Socket(ip, port);
			out = new PrintWriter(clientSocket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"utf-8"));
			out.println(data);
			out.flush();
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
		} finally {
			try {
				reader.close();
				out.close();
				clientSocket.close();
			} catch (Exception e) {
			}
		}
		return result.toString();
	}
}
