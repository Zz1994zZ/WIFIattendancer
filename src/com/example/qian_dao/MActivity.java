package com.example.qian_dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 活动数据 包含 name    int activity_id;
    int depart_id;
    String activity_name;
    String create_time;
    String time_start;
    String time_end;
    String date_start;
    String date_end;
    String remark;
    String mac;
    int check;
 * @author Administrator
 *
 */
public class MActivity {
   private int activity_id;
   private int depart_id;
   private String activity_name;
   private String create_time;
   private String time_start;
   private String time_end;
   private String date_start;
   private String date_end;
   private String remark;
   private String mac;
   private int check=0;
   private int activity_type;
   private int sid;
   public MActivity(JSONObject json) throws JSONException{
	   activity_id=json.getInt("activity_id");
	     depart_id=json.getInt("depart_id");
	     activity_name=json.getString("activity_name");
	     create_time=json.getString("create_time");
	     time_start=json.getString("time_start");
	     time_end=json.getString("time_end");
	     date_start=json.getString("date_start");
	     date_end=json.getString("date_end");
	     remark=json.getString("remark"); 
	     mac=json.getString("wifi_mac");
	     activity_type=json.getInt("activity_type");
		check=json.getInt("check");
		sid=json.getInt("sid");
   }
   public MActivity(JSONObject json,int type) throws JSONException{
	   activity_id=json.getInt("activity_id");
	     depart_id=json.getInt("depart_id");
	     activity_name=json.getString("activity_name");
	     create_time=json.getString("create_time");
	     time_start=json.getString("time_start");
	     time_end=json.getString("time_end");
	     date_start=json.getString("date_start");
	     date_end=json.getString("date_end");
	     remark=json.getString("remark"); 
	     activity_type=json.getInt("activity_type");
   }
   public int getSid(){
	   return sid;
   }
   public int getActivity_type(){
	   return activity_type;
   }
   public int getActivity_id(){
	   return activity_id;
   }
   public int getDepart_id(){
	   return depart_id;
   }
   public String getActivity_name(){
	   return activity_name;
   }
   public String getCreate_time(){
	   return create_time;
   }
   public String getTime_start(){
	   return time_start;
   }
   public String getTime_end(){
	   return time_end;
   }
   public String getDate_start(){
	   return date_start;
   }
   public String getDate_end(){
	   return date_end;
   }
   public String getRemark(){
	   return remark;
   }
   public String getMac(){
	   return mac;
   }
   public int getCheck(){
	   return check;
   }
}
