package com.example.qian_dao;
/**
 * ����ϵͳuser ����name,mac,success����ʾ�Ƿ�ǩ����,ip
 * @author Administrator
 *
 */
public class local_user {
 private String name;
 private String mac;
 private Boolean success=false;
 private String ip;
 public local_user(String name,String mac,String ip){
	 this.name=name;
	 this.mac=mac;
	 this.ip=ip;
 }
 public  void  setIp(String IP){
	 ip=IP;
 }
 public String getName(){
	 return name;
 }
 public String getMac(){
	 return mac;
 }
 public String getIp(){
	 return ip;
 }
 public void Check(){
	 this.success=true;
 }
 public void unCheck(){
	 this.success=false;
 }
 public boolean isChecked(){
	 return success;
 }
}
