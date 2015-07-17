package com.ycsoft.login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ycsoft.commons.helper.JsonHelper;



public class SocketServerThread  extends Thread{
	
	
	private BufferedReader in;
	private BufferedWriter writer;
	private Socket socket = null;
	public SocketServerThread(Socket socket){
		this.socket = socket;
	}
	
	public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
				String recStr = in.readLine();
				String result ="";
				if (recStr != null&&recStr.length()>0) {
					System.out.println(socket.getInetAddress()+recStr);
					String c = recStr.substring(0,1);
					recStr = recStr.substring(1);
					if (c.equals("g")){
						//切换系统
						String[] p = recStr.split(",");
						if (p.length == 3) {
							OnlineUser ouser = (OnlineUser)OnlineUser.map.get(p[0]);
							if (ouser!=null){
								String sessionid = (String) ouser.getServers().get(socket.getInetAddress()+p[2]);
								if (sessionid == null ){
									ouser.getServers().put(socket.getInetAddress()+p[2],p[1]);
								}
								result = JsonHelper.fromObject(ouser.getOptr());
							}
						}
					}else if(c.equals("c")){
						//session失效
						String[] p = recStr.split(",");
						if (p.length == 2) {
							OnlineUser ouser = OnlineUser.map.get(p[0]);
							if (ouser!=null){
								ouser.getServers().remove(socket.getInetAddress()+p[1]);
								if(ouser.getServers().size()==0)
									OnlineUser.map.remove(p[0]);
							}
						}
					}else if(c.equals("o")){
						//安全退出
						OnlineUser.map.remove(recStr);
					}else if(c.equals("d")){
						//删除指定服务器上的用户数据
						String[] p = recStr.split(",");
						Iterator<String> onlineSesids = OnlineUser.map.keySet().iterator();
						List<String> delsesid = new ArrayList<String>();
						while(onlineSesids.hasNext()){
							String onlineSesid = onlineSesids.next();
							OnlineUser onlineUser = OnlineUser.map.get(onlineSesid);
							Iterator<String> serverNames = onlineUser.getServers().keySet().iterator();
							int i =0;
							while(serverNames.hasNext()){
								String resname = serverNames.next();
								if(resname!=null && resname.indexOf(socket.getInetAddress().toString())>-1){
									onlineUser.getServers().remove(resname);
									continue;
								}
								i++;
					       }
							if (i==0){
								delsesid.add(onlineSesid);
							}
						}
						for (String sesid :delsesid){
							OnlineUser.map.remove(sesid);
						}
						
						OnlineUser.map.remove(p[0]);
						
					}else if(c.equals("a")){
						// 增加操作内容
						String[] p = recStr.split(",");
						OnlineUser ouser = OnlineUser.map.get(p[0]);
						if (ouser != null && p.length > 1) {
							ouser.addResourceList(p[1]);
						}
					}
				}
				writer.write(result);
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					writer.close();
					in.close();
				} catch (IOException e) {
				}
			}
	}

}

