package com.example.qian_dao;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LocalWork extends Activity {
 EditText ev;
 TextView textshow,zhuangtai;
 String IP="";
 Boolean isServeing=false,setIp=false,showb=false;
 SocketServer server=null;
 Handler createC;
 ImageButton check,zc,ms,ch,menu;
 int count;
 View layout;
 local_user[] lug; 
 int luNum=0;
 ListView lt; 
 AlertDialog a;
 ScrollView scrollView;
 /*
  * 处理接受到的信息
  */
	public Handler my_hanler=new Handler(){
        @Override
		public   void  handleMessage(Message   msg){             //处理通信线程传递的消息
            super.handleMessage(msg);
            String  text   =  msg.getData().getString("text");     //获取Message中的内容
            String ip=null,mac=null,m=null;
            int type=1;
           	System.out.println("got jsonString"+text);
            JSONObject j=null;
            try {
            	if(text!=null){
               	System.out.println("生成ing....~");
                j= new JSONObject(text);
            	System.out.println("接受并生成json成功了~");
				ip=j.getString("ip");
				mac=j.getString("mac");
				m=j.getString("msg");
				type=j.getInt("type");
            	}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            JSONArray ja;
           if(textshow.getLineCount()>=20)
                textshow.setText("新一页记录：");
            ja=readSaveData();
            String name=null;
            if(getLu(mac)==-1) name=null;
            else
            name=lug[getLu(mac)].getName();
            switch(type){
            case 1:    
            	if(name==null)
            		textshow.setText(textshow.getText()+"\nnew:来自陌生人的消息：\n"+m);
            		else            		
                    textshow.setText(textshow.getText()+"\n来自"+name+"的消息：\n"+m);                    //添加内容到消息记录中
                    System.out.println("完成刷新UI");
            	break;
            case 2:
                if(name==null){
                textshow.setText(textshow.getText()+"\n"+m+":未注册用户签到！");     
                sendM("未注册",20,ip);
                }
                else  //if(m.equals(name))
                { 
                	if( lug[getLu(mac)].isChecked()) return;
                textshow.setText(textshow.getText()+"\n"+m+"：已签到！"+m); 
                count++;
                zhuangtai.setText("状态\n签到中  \n  "+count+"/"+luNum);
                 lug[getLu(mac)].Check();
                 lug[getLu(mac)].setIp(ip);
                 refreshList(); 
                 sendM("签到成功",21,ip);
                } 
                //else     textshow.setText(textshow.getText()+"\n"+name+"换名为"+m+"签到！"); 
                System.out.println("完成刷新UI");
            	break;
            case 3:
            	System.out.println("进去了~");
                if(ja!=null){
                	if(name==null){
                    addsave(ja,j);
                	writeLug();
                	refreshList();
                    textshow.setText(textshow.getText()+"\n"+m+"：已注册到手机："+mac);} 
                	else  textshow.setText(textshow.getText()+"\n"+name+"重复注册为："+m);
                }else{
                	addsave(new JSONArray(),j);
                	writeLug();
                	refreshList();
                	textshow.setText(textshow.getText()+"\n"+m+"：已注册到手机："+mac);
                	System.out.println("初始化存储数据！");
                }
                System.out.println("完成刷新UI");
            	break;
            case 21:
            	textshow.setText(textshow.getText()+"\n签到成功~！"); 
            	CustomToast.showToast(LocalWork.this,"签到成功！\n你是好学生哦O(∩_∩)O~",3000,0);
            	break;
            case 20:
            	textshow.setText(textshow.getText()+"\n签到失败-未注册到该目标~！"); 
          		CustomToast.showToast(LocalWork.this,"失败！\n未注册到目标",2000);
            	break;
            }
            createC.post(new Runnable() {  
            	    @Override  
            	   public void run() {  
            	        scrollView.fullScroll(View.FOCUS_DOWN);  
            	    }  
            	});  

   }
};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_work);
		textshow=(TextView)findViewById(R.id.message);
		zhuangtai=(TextView)findViewById(R.id.show);
		check=(ImageButton)findViewById(R.id.ImageButton01);
		zc=(ImageButton)findViewById(R.id.ImageButton02);
		ms=(ImageButton)findViewById(R.id.ImageButton03);
		ch=(ImageButton)findViewById(R.id.imageButton5);
		menu=(ImageButton)findViewById(R.id.imageButton4);
		scrollView=(ScrollView)findViewById(R.id.scrollView1);
	   System.out.println("秒~1");
		check.setOnClickListener(BLitener); 
		zc.setOnClickListener(BLitener);
		ms.setOnClickListener(BLitener);
		ch.setOnClickListener(BLitener);
		 System.out.println("~~~2");
        //
		 LayoutInflater inflater = getLayoutInflater();
		 layout = inflater.inflate(R.layout.dialog,(ViewGroup) findViewById(R.id.dialog));
		 lt=(ListView)layout.findViewById(R.id.dialog);	
		 setLt();
		 a=new AlertDialog.Builder(LocalWork.this).setTitle("本地名单").setView(layout)
				 .create();
		//
		connectWifi(); 
		createC=new Handler();
		if(server==null)
		server=new SocketServer(3000);
		reSetIp();
		lug=new local_user [50];
		writeLug();
		refreshList();
		startRecive();
	}
	/*
	 * 初始化LIST
	 */
	private void setLt(){
		lt.setOnItemClickListener(new OnItemClickListener() {  			    
	          @Override  
	          public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,  
	                 long arg3) { 
	        		if(!lug[arg2].isChecked())return;
	  	    	OnClickListener click=new OnClickListener(){                                           //监听弹窗的确定按钮
					@Override
					   public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub					
					sendM(ev.getText().toString(),1, lug[arg2].getIp());
					}
				};
				 ev=new EditText(LocalWork.this);
		      		new AlertDialog.Builder(LocalWork.this)  
		    		.setTitle("发送消息：")  
		    		//.setIcon(android.R.drawable.ic_dialog_info)  
		    		.setView(ev) 
		    		.setPositiveButton("确定", click)  
		    	    .setNegativeButton("取消", null)  
		    		.show();
	          }
	});
	}
	/*
	 * 返回主线程弹出提示
	 */
	Thread create=new Thread(new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			CustomToast.showToast(LocalWork.this,"网络配置失败，请连接正确网络后重试重新程序再试！",2000);
			if(!setIp)
			reSetIp();
		}	
	});
	/*
	 * 返回主线程弹出提示2
	 */
	Thread create2=new Thread(new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub		
			CustomToast.showToast(LocalWork.this,"服务器响应超时！",2000);
		}	
	});
	/*
	 * 设置我要签到点击事件
	 */
	public void onClick(View v){
		if(!showb)
		{
			showb=true;
			check.setVisibility(View.VISIBLE);
			zc.setVisibility(View.VISIBLE);
			ms.setVisibility(View.VISIBLE);
			ch.setVisibility(View.VISIBLE);
			menu.setVisibility(View.INVISIBLE);
		}
	}
	/*
	 * 设置显示列表点击事件
	 */
	public void showListOut(View v){
		a.show();
	}
	/*
	 * 设置停止接受消息点击事件
	 */
	public void stoplisten(View v){
		if(isServeing){
	    	//点击事件设置
	    	OnClickListener click=new OnClickListener(){                                           //监听弹窗的确定按钮
				@Override
				   public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					isServeing=false;
					CustomToast.showToast(LocalWork.this,"已关闭信息接收",2000);
					zhuangtai.setText("状态\n\n   打酱油中···");
				}
			};
      		new AlertDialog.Builder(this)  
    		.setTitle("注意")  
    		.setIcon(android.R.drawable.ic_dialog_info)  
    		.setMessage("正在接收消息中，"+"\n确认中止？") 
    		.setPositiveButton("确定", click)  
    	    .setNegativeButton("取消", null)  
    		.show();
			return;
		}
	}
	/*
	 * 开始接受消息方法
	 */
	public void startRecive(){
		if(isServeing)
			return;
		if(server==null)
		{
			isServeing=false;
			CustomToast.showToast(LocalWork.this,"重新申请端口！",2000);
			server=new SocketServer(3000);
		    return;
		}
		isServeing=true;		
		zhuangtai.setText("状态\n\n   接收消息···");
		new Thread(new Runnable(){
             @Override
			public  void  run(){
           	  while(isServeing){
           	   	  try {      	 	
  					server.StartListen();
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				} 
           		  if(server.s!=null){
           		  Message message    =  new Message();    //新建消息类
                     Bundle bundle  = new Bundle();
                     bundle.putString("text",server.msg);
                     message.setData(bundle);                  //设置消息类包含的内容
                     my_hanler.sendMessage(message);                   //发送消息，用于ＵＩ更新
           		  }
           	  }
             }}).start();
	}
	/*
	 * 设置开始接受消息点击事件
	 */
	public void startClick(View v){
		if(isServeing){
			CustomToast.showToast(LocalWork.this,"正在接收信息",2000);
			//zhuangtai.setText("状态\n\n   打酱油中···");
			return;
		}
		
		if(server==null)
		{
			isServeing=false;
			CustomToast.showToast(LocalWork.this,"重新申请端口！",2000);
			server=new SocketServer(3000);
		    return;
		}
		isServeing=true;
		CustomToast.showToast(LocalWork.this,"开始接受消息",2000);
		zhuangtai.setText("状态\n\n   接收消息···");
		new Thread(new Runnable(){
             @Override
			public  void  run(){
           	  while(isServeing){
           	   	  try {      	 	
  					server.StartListen();
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				} 
           		  if(server.s!=null){
           		  Message message    =  new Message();    //新建消息类
                     Bundle bundle  = new Bundle();
                     bundle.putString("text",server.msg);
                     message.setData(bundle);                  //设置消息类包含的内容
                     my_hanler.sendMessage(message);                   //发送消息，用于ＵＩ更新
           		  }
           	  }
             }}).start();
 a.show();
	}
	/*
	 * 获取mac地址
	 */
	public String getmacaddress() {
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo =wifiManager.getConnectionInfo();
		return wifiinfo.getMacAddress();
	}
	/*
	 * 获取本地IP
	 */
	public String getIp(){
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo =wifiManager.getConnectionInfo();
		return String.valueOf(Formatter.formatIpAddress(wifiinfo.getIpAddress()));
	}
	/*
	 * 保存json数组
	 */
	public boolean addsave(JSONArray j,JSONObject newj){
		 j.put(newj);
		 SharedPreferences sp=getSharedPreferences("mrs",MODE_PRIVATE);
		 Editor editor=sp.edit();
		 editor.putString("JSON", j.toString());
		 editor.commit();
		return true;
	}
	/**
	 *清空签到列表
	 */
	public void clearStore(){
		 SharedPreferences sp=getSharedPreferences("mrs",MODE_PRIVATE);
		 Editor editor=sp.edit();
		 editor.clear();
		 editor.commit();
	}
	/*
	 * 读取JSON数组
	 */
	public JSONArray readSaveData(){
		JSONArray j=null;
		SharedPreferences sp=getSharedPreferences("mrs",MODE_PRIVATE);
		try {
			if(sp.getString("JSON", null)==null)return null;
			j=new JSONArray(sp.getString("JSON", null).toString());
			System.out.println("返回的j"+j.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("生成ja失败！");
			e.printStackTrace();
			return null;
		}
		return j;
	}
	/*
	 * 得到存储中相应MAC对应的用户姓名
	 */
	public String getName(String mac,JSONArray j){
		if(j==null)
			return null;
		int i=0;String name=null;
		try {
			System.out.println("正在验证mac"+mac);
			String jmac;
			while(true)
			{   
				if(i>=j.length())break;
				jmac=j.getJSONObject(i).getString("mac").toString();
				if(jmac.equals(mac))	name= j.getJSONObject(i).getString("msg");
			    i++;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}
	/*
	 * 重新设置目标IP（设置为默认连接的WIFI发射设备IP）
	 */
	public void reSetIp(){
		IP="";
		String lsIP=getIp();
		for(int i=0,j=0;j<lsIP.length();j++){
			if(lsIP.charAt(j)=='.') i++;
				IP+=lsIP.charAt(j);
	        if(i==3) break;
		}
		IP+="1";
		System.out.println("获得服务器IP："+IP);
	}
	/*
	 * 连接WIFI
	 */
	public void connectWifi(){
		WifiAdmin mWifiAdmin=new WifiAdmin(this);
		mWifiAdmin.openWifi();
	}
	/*
	 * 设置返回按键(non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	 @Override
	    public void onBackPressed() {
		    	//点击事件设置
		    	OnClickListener click=new OnClickListener(){                                           //监听弹窗的确定按钮
					@Override
					   public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						isServeing=false;	
						   try {
							   if(server.s!=null)
							server.s.close();
							   if(server.ss!=null)
							server.ss.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 back();
					}
				};
	      		new AlertDialog.Builder(this)  
	    		.setTitle("注意")  
	    		.setIcon(android.R.drawable.ic_dialog_info)  
	    		.setMessage("正在接收消息中，"+"\n确认退出？") 
	    		.setPositiveButton("确定", click)  
	    	    .setNegativeButton("取消", null)  
	    		.show();
	    }
	 private void back(){
		 super.onBackPressed();
	 }
	 /*
	  * 发送消息的弹窗
	  */
	    public void setTanchuang(final int type)                                                 //弹窗内容设置
	    {
	    	String sign=null;
	    	switch(type)
	    	{
	    		case 0:
	    			sign="新IP：";
	    			break;
	    		case 2:
	    		    sendM("check",type,IP);	
	    		    return;
	    		case 3:
	    			 sign="姓名：";
		    		 break;
	    		case 1:
	    			 sign="消息：";
		    		 break;
	    	}
	    	//点击事件设置
	    	OnClickListener click=new OnClickListener(){                                           //监听弹窗的确定按钮
				@Override
				   public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(type==0){//修改目标IP
					String yip=IP;
					IP=ev.getText().toString();
					CustomToast.showToast(LocalWork.this,"目标IP已改为："+IP+"\n原IP为"+yip,2000);
					 textshow.setText(textshow.getText()+"\n目标IP已改为："+IP+"\n原IP为"+yip);
			            createC.post(new Runnable() {  
		            	    @Override  
		            	   public void run() {  
		            	        scrollView.fullScroll(View.FOCUS_DOWN);  
		            	    }  
		            	});  
					setIp=true;
					}
					else//签到与注册与发送消息
					sendM(ev.getText().toString(),type,IP);	
				}
			};
	    	 ev=new EditText(this);
	    	//
      		new AlertDialog.Builder(this)  
    		.setTitle(sign)  
    		.setIcon(android.R.drawable.ic_dialog_info)  
    		.setView(ev) 
    		.setPositiveButton("确定", click)  
    	    .setNegativeButton("取消", null)  
    		.show();
    		if(showb){
    			check.setVisibility(View.INVISIBLE);
    			zc.setVisibility(View.INVISIBLE);
    			ms.setVisibility(View.INVISIBLE);
    			ch.setVisibility(View.INVISIBLE);
    			showb=false;
    			menu.setVisibility(View.VISIBLE);
    			}
	    }
	    /*
	     * 发送消息的方法
	     */
	    private void sendM(final String msg,final int type,final String ip){
			  new Thread(new Runnable(){
	              @Override
				public  void  run(){
	            	  SocketClient client;
	            	  client= new SocketClient(ip,3000);
	            	  if(client.client!=null){
	            	  String js=new JsonMaker(getIp(),getmacaddress(),msg,type).getJString();
	                  System.out.println("生成的json字符串为:"+js);  
	            	  if(client.sendMsg(js))//editview.getText().toString()
	            	  { 
	                      createC.post(new Runnable() {  
	                  	    @Override  
	                  	   public void run() {  
	  	            		  System.out.println("发送成功！");
                              if(type==1)
		            		  textshow.setText(textshow.getText()+"\n你说:\n"+msg); 
		            		  else
		            		  textshow.setText(textshow.getText()+"\n发送成功！"); 
	                  	        scrollView.fullScroll(View.FOCUS_DOWN);  
	                  	    }  
	                  	});  
	            	  }
	            	  else
	            		  {System.out.println("服务器未响应！"); createC.post(create2);}
	            	  }
	            	  else 
	            	  { System.out.println("网络配置失败，请连接正确网络后重试重新程序再试！");createC.post(create);}
	            	  }
	              }).start();
	    }
	    /*
	     * 四个功能按钮的点击事件
	     */
		public android.view.View.OnClickListener BLitener=new android.view.View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final ImageButton btn=(ImageButton)v;
				switch(btn.getId()){
				case R.id.ImageButton01:
					setTanchuang(0);
					break;
				case R.id.ImageButton02:
					startRecive();
					setTanchuang(2);
				    break;
				case R.id.ImageButton03:
					setTanchuang(3);
					break;
				case R.id.imageButton5:
					setTanchuang(1);
					break;
				}		
			}
		};
		/*
		 * 将本地存储的用户信息写入数组
		 */
		private void writeLug(){
			
			  JSONArray ja=readSaveData();
			  if(ja==null)
				  {luNum=0;return;}
		      for(int i=luNum;i<ja.length();i++){
			        try {
			        	String n=ja.getJSONObject(i).getString("msg");
			        	String m=ja.getJSONObject(i).getString("mac");
			        	lug[i]=new local_user(n, m, null);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}}
		      luNum=ja.length();
		}
		/*
		 * 从数组中得到相应MAC对应用户姓名
		 */
		private int getLu(String mac){
		 for(int i=0;i<luNum;i++){
			 if(lug[i].getMac().equals(mac))
				 return i;
		 }
			return -1;
		}
		/*
		 * 从LU数组获取刷新LIST的数据
		 */
	    private List< Map<String,Object>> getData(){
	    	  List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();  
	          Map<String, Object> map;  
      for(int i=0;i<luNum;i++){
    	  map = new HashMap<String, Object>();
    	  map.put("text",lug[i].getName());
	        if(lug[i].isChecked())
	        	  map.put("image",R.drawable.ischeck);
			else
				  map.put("image",null);
	        data.add(map); 
      }
	        return data;
	    }
	    /*
	     * 刷新LIST的方法
	     */
	    private void refreshList(){
	    	  System.out.println("人数！："+luNum);
			SimpleAdapter adapter = new SimpleAdapter(LocalWork.this, getData(), R.layout.listconext, new String[] { "text",  "image" }, new int[] { R.id.text, R.id.image});  
			lt.setAdapter(adapter);
	    }
		 @Override
			public boolean onCreateOptionsMenu(Menu menu) {                                        //设置菜单
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.local_work, menu);
				return true;
			}
			@Override                                                                              //设置菜单点击效果
			public boolean onOptionsItemSelected(MenuItem item) {
			    switch(item.getItemId()) {
			    case R.id.action_settings:
			    	//点击事件设置
			    	OnClickListener click=new OnClickListener(){                                           //监听弹窗的确定按钮
						@Override
						   public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
					    	System.out.println("~~");	    	
					    	clearStore();
					    	 writeLug();
					    	 refreshList();
					    	 CustomToast.showToast(LocalWork.this,"存储清除成功！",3000,0);
						}
					};
		      		new AlertDialog.Builder(this)  
		    		.setTitle("确定？")  
		    		.setIcon(android.R.drawable.ic_dialog_info)  
		    		.setMessage("将清除所有注册的用户信息") 
		    		.setPositiveButton("确定", click)  
		    	    .setNegativeButton("取消", null)  
		    		.show();
			    	break;
			    }
			    return true;
			}
}

