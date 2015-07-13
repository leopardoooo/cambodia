package com.ycsoft.commons.helper;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketHelp {

	public static String sendData(String ip, int port, String data)
			throws IOException {
		StringBuffer result = new StringBuffer(1024 * 10);
		Socket clientSocket = null;
		DataInputStream in = null;
		PrintWriter out = null;
		try {
			clientSocket = new Socket(ip, port);
			out = new PrintWriter(clientSocket.getOutputStream());
			in = new DataInputStream(clientSocket.getInputStream());
			out.println(data);
			out.flush();
			byte[] bt = new byte[1024];
			int len = 0;
			while ((len = in.read(bt)) != -1) {
				result.append(new String(bt, 0, len));
			}
		} finally {
			try {
				in.close();
				out.close();
				clientSocket.close();
			} catch (Exception e) {
			}
		}
		return new String(result.toString().getBytes(), "GBK");
	}
}
