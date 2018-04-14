package com.example.qian_dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 用于输入IP,MAC,MSG,TYPE创建出JSONObject对象用于本地系统传送消息
 * @author Administrator
 *
 */
public class JsonMaker {
	JSONObject json;
    public  JsonMaker(String ip,String mac,String msg,int type){
    	try {
    		json=new JSONObject();
			json.put("ip", ip);
			json.put("mac", mac);
			json.put("msg", msg);
			json.put("type", type);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("生成json失败了~");
			e.printStackTrace();
		}
    	System.out.println("生成json成功！");
    }
    public String getJString(){
    	System.out.println(json.toString());
    	return json.toString();
    }
}
