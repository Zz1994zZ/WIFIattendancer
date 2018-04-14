package com.example.qian_dao;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
/**
 * 网络登录的用户类 包括
 *    String local_mac;
   String wifi_mac;
   String user_name;
   String password;
   String name;
   String email;
   String create_time;
   String login_ip;
   String remark;
   String depart_name;
   int success;
   int id;
 * @author Administrator
 *
 */
public class User {
  private String local_mac;
  private String wifi_mac;
  private String user_name;
  private String password;
  private String name;
  private String email;
  private String create_time;
  private String login_ip;
  private String remark;
  private String depart_name;
  private String last_login;
  private int success;
  private int id;
  private int depart_id;
  private int check=0;
  private int sex=1;
  private String qq;
  private String phone;
  private boolean isManager=false;
     public User(Activity ac){
    	 this.local_mac=getmacaddress(ac);
    	 this.wifi_mac=getwifimac(ac);
     }
     public void setUser_name(String user_name){
    	 this.user_name=user_name;
     }
     public String getUser_name(){
    	 return this.user_name;
     }
     public void setName(String name){
    	 this.name=name;
     }
     public String getName(){
    	 return this.name;
     }
     public void setPassword(String password){
    	 this.password=password;
     }
     public String getPassword(){
    	 return this.password;
     }
     public void setEmail(String email){
    	 this.email=email;
     }
     public String getEmail(){
    	 return this.email;
     }
     public void setCreate_time(String create_time){
    	 this.create_time=create_time;
     }
     public String get(){
    	 return this.create_time;
     }
     public void setlogin_ip(String login_ip){
	 this.login_ip=login_ip;
     }
     public String getLogin_ip(){
	 return this.login_ip;
     }
     public void setRemark(String remark){
	 this.remark=remark;
     }
     public String getRemark(){
	 return this.remark;
     }
     public void setDepart_name(String depart_name){
	 this.depart_name=depart_name;
     }
     public String getDepart_name(){
	 return this.depart_name;
     }
     public void setId(int id){
	 this.id=id;
     }
     public int getId(){
	 return this.id;
     }
     public String getLocal_mac(){
	 return this.local_mac;
     }
     public void  setLocal_mac(String local_mac){
	 this.local_mac=local_mac;
     }
     public String getWifi_mac(){
	 return this.wifi_mac;
     }
     public void setWifi_mac(String wifi_mac){
     this.wifi_mac=wifi_mac;	 
     }
     public void setLast_login(String time){
    	 last_login=time;
     }
     public String getLast_login(){
    	 return last_login;
     }
     private  void setCheck(int c){
    	 check=c;
     }
     public int getCheck(){
    	 return check;
     }
		public   String getwifimac(Activity ac){
		    WifiManager wifiManager = (WifiManager)ac.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiinfo =wifiManager.getConnectionInfo();
		    return wifiinfo.getBSSID();
			}
		//获取本地mac地址
		public  String getmacaddress(Activity ac) {
			WifiManager wifiManager = (WifiManager)ac.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiinfo =wifiManager.getConnectionInfo();
			return wifiinfo.getMacAddress();
		}
	 public void showMe(){
		 System.out.println(  
		 "\nlocal_mac="+local_mac+
		 "\nwifi_mac="+wifi_mac+
		 "\nuser_name="+user_name+
		 "\npassword="+password+
		 "\nname="+name+
		 "\nemail="+email+
		 "\ncreate_time="+create_time+
		 "\nlogin_ip="+login_ip+
		 "\nremark="+remark+
		 "\nname="+name+
		 "\nsuccess="+success+
		 "\nid="+id+
		 "\ndepart_id"+depart_id+
		 "\nisManager"+isManager
		 );
	 }
	 public void setDepart_id(int di){
		 depart_id=di;
	 }
	 public int getDepart_id(){
		 return depart_id;
	 }
	 public void setManager(){
		 isManager=true;
	 }
	 public boolean isManager(){
		 return isManager;
	 }
     public  static   void copyUserInfo(JSONObject json,User u,int type) throws JSONException{
    switch(type){
    case 1: 
    u.setId(json.getInt("id"));
      u.setName(json.getString("name"));
      u.setEmail(json.getString("email"));
      u.setCreate_time(json.getString("create_time"));
      u.setDepart_name(json.getString("depart_name"));
      u.setRemark(json.getString("remark"));
      u.setLocal_mac(json.getString("local_mac"));
      break;
    case 10:
      u.setCreate_time(json.getString("create_time"));
      u.setDepart_name(json.getString("depart_name"));
      u.setRemark(json.getString("remark"));
      u.setLocal_mac(json.getString("wifi_mac"));	
      u.setDepart_id(json.getInt("depart_id"));
      //u.setLast_login(json.getString("last_login"));
      u.setEmail(json.getString("email"));
      u.setManager();
     break;
    case 100:
        u.setId(json.getInt("id"));
        u.setName(json.getString("name"));
        u.setEmail(json.getString("email"));
        u.setCreate_time(json.getString("create_time"));
        u.setRemark(json.getString("remark"));
        u.setLocal_mac(json.getString("local_mac"));
        u.setCheck(json.getInt("check"));
        break;
    }
     }
}
