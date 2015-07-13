package com.ycsoft.login;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	public static void main(String args[]) throws Exception {

		Socket clientSocket = new Socket("127.0.0.1", 8000);
		java.io.DataInputStream in = new DataInputStream(clientSocket
				.getInputStream());
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
		out.println("abc");
		out.flush();
		System.out.println(in.readUTF());
		
		clientSocket.close();

	}
}
