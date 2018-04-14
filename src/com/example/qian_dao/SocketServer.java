package com.example.qian_dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	ServerSocket ss=null;
	String msg;
	 Socket s;
	public SocketServer(int port){
		try {
			ss = new ServerSocket(port);
            System.out.println("…Í«Î≥…π¶£°");
		} catch (IOException e) {
			// TODO Auto-generated catch block
            System.out.println("…Í«Î ß∞‹£°");
			e.printStackTrace();
		}
	}
	public  void StartListen() throws IOException{	     
					try {						
				     s = ss.accept();
		            BufferedReader br = new BufferedReader(new InputStreamReader( 
		            s.getInputStream()));
		            String line = br.readLine(); 
		            msg=line;
			        br.close();
			        s.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	                 //  }
			//	}
		//	}.start();
	  }
}