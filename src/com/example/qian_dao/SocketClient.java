package com.example.qian_dao;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
	Socket client;
	 public SocketClient(String ip, int port){
		  try {
			client = new Socket();   
			InetSocketAddress isa = new InetSocketAddress(ip, port);     
			client.connect(isa, 500);
			 System.out.println("申请cilent成功！");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			 System.out.println("申请cilent失败1！");
			 client=null;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 System.out.println("申请cilent失败2！");
			 client=null;
			e.printStackTrace();
		}
	 }
	 public  boolean sendMsg(String msg){
		 if(msg==null)return false;
		 OutputStream os;
		try {
			System.out.println("开始流");
			os = client.getOutputStream();
		    os.write(msg.getBytes("utf-8"));
		    System.out.println("传送中");
		    os.close();
		    client.close();
		    System.out.println("完成");
			 return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("发送失败了~");
			e.printStackTrace();
			return false;
		}
	
	 }

}
