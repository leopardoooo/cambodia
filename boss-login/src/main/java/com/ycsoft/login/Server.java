package com.ycsoft.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Server extends Thread{
	private int port = 8000;
	private ServerSocket serverSocket;


	private BufferedReader in;
	private java.io.DataOutputStream writer;
	
	public void run() {
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				socket = serverSocket.accept(); // 从连接请求队列中取出一个连接
				SocketServerThread thead = new SocketServerThread(socket);
				thead.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) throws Exception {
		Server server = new Server();
//		Thread.sleep(60000 * 10);
		// 睡眠10分钟
		server.start();
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
}