package com.example.qian_dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * ��������IP,MAC,MSG,TYPE������JSONObject�������ڱ���ϵͳ������Ϣ
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
			System.out.println("����jsonʧ����~");
			e.printStackTrace();
		}
    	System.out.println("����json�ɹ���");
    }
    public String getJString(){
    	System.out.println(json.toString());
    	return json.toString();
    }
}
