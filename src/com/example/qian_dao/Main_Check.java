package com.example.qian_dao;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Main_Check extends Activity {
private ImageView refresh;
private	 TextView show;
private TextView showwelcome;
private   String list="当前可签到活动如下:\n";
private  int success=0,acnum;
private ProgressBar progressDialog4 = null; 
private MActivity[] ma;
private User user;
private ListView aList;
WifiManager wifiManager;
/**
 * 返回主线程处理信息
 */
private  Handler handler2=new Handler(){
	@Override
	 public void handleMessage(Message msg){
		progressDialog4.setVisibility(View.INVISIBLE);
		refresh.setVisibility(View.VISIBLE);
		String ms="网络错误!";
		switch(success){
		case 1:
			ms= "签到成功！";
			 getCheckList();
			break;
		case 2:
			ms="未处于指定wifi范围！";
			 break;
		case 3:
			ms="签到手机错误！";
			 break;
		case 0:
			ms="服务器错误！";
			 break;
		case 4:
			ms="网络错误!";
			 break;
		}
		progressDialog4.setVisibility(View.INVISIBLE);
		 CustomToast.showToast(Main_Check.this,ms, 2000);
		 super.handleMessage(msg);
	}
};
/**
 * 获取刷新LIST的数据
 */
private List< Map<String,Object>> getData2(){
	  List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();  
    Map<String, Object> map; 
for(int i=0;i<acnum;i++){
	map = new HashMap<String, Object>();
	 map.put("text",ma[i].getActivity_name());
	 if(ma[i].getActivity_type()==2)
		 map.put("image",R.drawable.imtp2);
	 else if(ma[i].getCheck()==1)
    	 map.put("image",R.drawable.cked);
    else if(ma[i].getCheck()==0)
    	 map.put("image",R.drawable.uncked);
    data.add(map);
    }
    return data;
}
/**
 * 返回主线程刷新UI
 */
private  Handler handler=new Handler(){
	@Override
	 public void handleMessage(Message msg){
		 System.out.println("进来了");
		 progressDialog4.setVisibility(View.INVISIBLE);
			refresh.setVisibility(View.VISIBLE);
			if(user.isManager())
 showwelcome.setText("你好:管理员");
			else
showwelcome.setText("你好:"+user.getName());			
 System.out.println("返回值为"+success);
		show.setText("当前可签到活动如下:");
	     if(success==0)
		     {show.setText("无任何活动！");acnum=0;}
		SimpleAdapter adapter2 = new SimpleAdapter(Main_Check.this, getData2(), R.layout.listconext, new String[] { "text",  "image" }, new int[] { R.id.text, R.id.image}); 
	  //添加并且显示  
	  aList.setOnItemClickListener(new OnItemClickListener() {  
		    
          @Override  
          public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,  
                 long arg3) { 
        	  if(ma[arg2].getActivity_type()==2){
        			Intent intent = new Intent(Main_Check.this,
        					ToTPActivity.class);
        			intent.putExtra("remark",ma[arg2].getRemark());
        			intent.putExtra("activity_id",ma[arg2].getActivity_id()); 
        			intent.putExtra("wifi_mac",ma[arg2].getMac());
        			intent.putExtra("sid",ma[arg2].getSid());
        			startActivity(intent);
        			return;
        	  }
        	  TextView tv=new  TextView(Main_Check.this);
        	  tv.setText("名称："+ma[arg2].getActivity_name()+"\n日期从"+
        			     ma[arg2].getDate_start()+"到"+ma[arg2].getDate_end()+"\n"+
        			     "签到时间："+
        			     ma[arg2].getTime_start()+"到"+
        			     ma[arg2].getTime_end()+"\n"+
        			     "\n备注："+
        			     ma[arg2].getRemark()+"\n"
        			  );
          	OnClickListener click=new OnClickListener(){                                           //监听弹窗的确定按钮
    			@Override
    			   public void onClick(DialogInterface dialog, int which) {
                     //判断wifi处于何种状态
    				SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("HH:mm:ss");
    				Date date=new Date(); 
    				String d=sDateFormat.format(date); 
    			   System.out.println(d);
    			   System.out.println(d);
    			   System.out.println(ma[arg2].getTime_start()+compareTime(d,ma[arg2].getTime_start()));
    			   System.out.println(ma[arg2].getTime_end()+compareTime(ma[arg2].getTime_end(),d));
                      if(!(compareTime(d,ma[arg2].getTime_start())&&compareTime(ma[arg2].getTime_end(),d)))
                      { CustomToast.showToast(Main_Check.this,"不在签到时间,下次记得准时哦O(∩_∩)O~", 3000);
                       return;
                      }
                      
                    	  
					switch(wifiManager.getWifiState()){
					case WifiManager.WIFI_STATE_ENABLED:
						showwelcome.setText("已获取wifi,正在签到");	
						break;
					case WifiManager.WIFI_STATE_DISABLED:
						 CustomToast.showToast(Main_Check.this,"请打开wifi", 2000);
						return;
					case WifiManager.WIFI_STATE_ENABLING:
						 CustomToast.showToast(Main_Check.this,"wifi还未准备好，请稍后点击刷新", 2000);
						return;
					}
    	        	  progressDialog4.setVisibility(View.VISIBLE);
    	        		refresh.setVisibility(View.INVISIBLE);
    				 new Thread( new Runnable() {
    					 @Override
						public void run() {
    						String mac=null;
    						 if( test(ma[arg2].getMac()))
    							{mac=ma[arg2].getMac();System.out.println("在列表中找到对应Mac~");}
    						 else
    							 {mac=user.getWifi_mac();System.out.println("未找到对应mac~"+mac);}
    						 String urlPath =getString(R.string.urlPath)+"do_check";
    					    	JSONParser jsonParser =new JSONParser();
    					    	System.out.println("活动id"+ma[arg2].getActivity_id()+""+
    					    			            "用户id"+user.getId()+
    					    			            "本地mac"+user.getmacaddress(Main_Check.this)+
    					    			            "wifimac"+user.getWifi_mac()
    					    			);
    					    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    					    	System.out.println("活动ID为："+ma[arg2].getActivity_id());
    					    			params.add(new BasicNameValuePair("activity_id",ma[arg2].getActivity_id()+""));
    					    			params.add(new BasicNameValuePair("user_id",user.getId()+""));
    					    			params.add(new BasicNameValuePair("local_mac",user.getmacaddress(Main_Check.this)));
    					    			params.add(new BasicNameValuePair("wifi_mac",mac));
    					    			params.add(new BasicNameValuePair("sid",ma[arg2].getSid()+""));
    					    			params.add(new BasicNameValuePair("check","1"));
    					    			try {
    										JSONObject json=jsonParser.getJSONFromUrl(urlPath,params,false);
    										if(json!=null){   										
    										  success=json.getInt("success");
    										}else 
    										success=4;  										
    						             //发送用户名和密码前往服务器配对
    									} catch (JSONException e) {
    										// TODO Auto-generated catch block
    										e.printStackTrace();
    									} catch (IOException e) {
    										// TODO Auto-generated catch block
    										e.printStackTrace();
    									}
    					    			Message m=handler2.obtainMessage();
    					    			handler2.sendMessage(m);
    					 }        
    					 }).start(); 
    				// TODO Auto-generated method stub
    			}
    		};
    		if(ma[arg2].getCheck()==0){
      		new AlertDialog.Builder(Main_Check.this)  
    		.setTitle("活动信息:")  
    		.setIcon(android.R.drawable.ic_dialog_info)  
    		.setView(tv) 
    		.setPositiveButton("签到", click)  
    	    .setNegativeButton("取消", null)  
    		.show();}
    		else
    		{      		new AlertDialog.Builder(Main_Check.this)  
    		.setTitle("活动信息:")  
    		.setIcon(android.R.drawable.ic_dialog_info)  
    		.setView(tv) 
    	    .setNegativeButton("确定", null)  
    		.show();
    			
    		}
          }  
     });
      aList.setAdapter(adapter2);
		 super.handleMessage(msg);
	 }
	 };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main__check);
		 show= (TextView)findViewById(R.id.textView_localmac);
		 showwelcome= (TextView)findViewById(R.id.textview_welcome);
		  progressDialog4=(ProgressBar)findViewById(R.id.progressBar4);
		  refresh=(ImageView)findViewById(R.id.image);
		  aList= (ListView) findViewById(R.id.listView1);
		  wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);		    
		    Session session = Session.getSession();
		    user = (User)session.get("Info");
		  getCheckList();
	}

	//获取无线路由器mac地址
	public  String getwifimac(){
	    WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo =wifiManager.getConnectionInfo();
	    return wifiinfo.getBSSID();
		}
	//获取本地mac地址
	public String getmacaddress() {
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo =wifiManager.getConnectionInfo();
		return wifiinfo.getMacAddress();
	}
	/**
	 * 获取WIFI列表
	 */
	public boolean  test(String mac){
		wifiManager.startScan();
		 List<ScanResult> wl = wifiManager.getScanResults();
		 if(wl==null)
			 return false;
		 for(int i=0;i<wl.size();i++)
		 {
			 System.out.println(wl.get(i).SSID);
			if(wl.get(i).BSSID.equals(mac))
			return true;
		 }
		return false;
	}
	/**
	 * 从本地获取签到列表
	 */
	public void getCheckList(){
        //传送
        progressDialog4.setVisibility(View.VISIBLE);
    	refresh.setVisibility(View.INVISIBLE);
		if(!wifiManager.isWifiEnabled())
		{wifiManager.setWifiEnabled(true);
		showwelcome.setText("正在打开wifi~");
		}
		else
		showwelcome.setText("正在获取签到列表~");
    	sentAndGet();
	}
	/**
	 * 发送获取LIST
	 */
	 private void sentAndGet(){
	   	 new Thread( new Runnable() {   
			 @Override
			public void run() {  
	        String id=user.getId()+"";
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	       params.add(new BasicNameValuePair("id",id+""));
	    	       System.out.println("id为："+id);
	    			try {
						JSONArray json=JSONParser.getJson(getString(R.string.urlPath)+"check",params);
						if(json!=null){
						try {
						    ma=new MActivity[json.length()];
							acnum=json.length();
							for(int i=0;i<acnum;i++){
							JSONObject j=json.getJSONObject(i);
							 ma[i]=new MActivity(j);
							ma[i].getActivity_name();
							System.out.println(ma[i].getActivity_name());
							list=list+ma[i].getActivity_name()+"\n";
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						success=1;}
						else{
							success=0;
							}
					} catch (IOException e) {
						success=3;
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			Message m=handler.obtainMessage();
	    			handler.sendMessage(m);
	 }        
	 }).start(); 
	 }
	/**
	 * 设置刷新点击事件
	 */
	public void check_in(View view){
		getCheckList();
	}
	public boolean compareTime(String t1,String t2){
		if(t1.length()!=8||t2.length()!=8)
			return false;
		
		if(t1.charAt(0)>t2.charAt(0))
			return true;
		if(t1.charAt(0)<t2.charAt(0))
			return false;
		
		if(t1.charAt(1)>t2.charAt(1))
			return true;
		if(t1.charAt(1)<t2.charAt(1))
			return false;
		
		if(t1.charAt(3)>t2.charAt(3))
			return true;
		if(t1.charAt(3)<t2.charAt(3))
			return false;
		
		if(t1.charAt(4)>t2.charAt(4))
			return true;
		if(t1.charAt(4)<t2.charAt(4))
			return false;
		
		if(t1.charAt(6)>t2.charAt(6))
			return true;
		if(t1.charAt(6)<t2.charAt(6))
			return false;
		
		if(t1.charAt(7)>t2.charAt(7))
			return true;
		if(t1.charAt(7)<t2.charAt(7))
			return false;
		return false;
	}

}
