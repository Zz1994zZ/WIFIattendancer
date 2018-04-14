package com.example.qian_dao;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser{

	public boolean suc_respone;
	/**
	 * 从服务器获取JSON 返回JSONObject对象 用于登录注册签到
	 * @param url
	 * @param params
	 * @param issent
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public JSONObject getJSONFromUrl (String url,List <NameValuePair> params,boolean issent) throws JSONException, IOException {
			//进行 HTTP request Post 操作
		    suc_respone=false;
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
			//进行 Response 处理
			HttpResponse httpResponse1 = new DefaultHttpClient().execute(httpPost);
			String retSrc = EntityUtils.toString(httpResponse1.getEntity(),"UTF-8");
			System.out.println("返回的字符串为"+retSrc);
			if(!retSrc.equals("")&&retSrc!=null){
			while(retSrc.charAt(0)!='{'&&retSrc.length()>0){
				retSrc=retSrc.substring(1);
			}		
			//转换为 JSON 对象
			JSONObject jObj=null;
			try {
			jObj = new JSONObject(retSrc.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("在JSONParser中生成json失败了~");
				e.printStackTrace();
				return null;
			}
			int  success=jObj.getInt("success");//获取是否返回成功
			System.out.println("在函数中返回为"+success);
			suc_respone= success==1 ?  true:false;//返回成功则为true
			return jObj;
			}
   return null;
			
	        }
	/**
	 * 从服务器获取JSON数组 返回JSONObject数组对象 用于获取签到列表
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONArray getJson(String url,List <NameValuePair> params) throws ClientProtocolException, IOException{
		JSONArray ja=null;
		//		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse httpResponse1 = new DefaultHttpClient().execute(httpPost);
		String retSrc = EntityUtils.toString(httpResponse1.getEntity(),"UTF-8");
		System.out.println("返回的字符串为"+retSrc);
		if(!retSrc.equals("")&&retSrc!=null){
			try {
				while(retSrc.length()>0&&retSrc.charAt(0)!='['&&retSrc.length()>0){
					retSrc=retSrc.substring(1);
				}		
				ja=new JSONArray(retSrc);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("生成json数组失败~");
				ja=null;
				e.printStackTrace();
			}
		}
		return ja;

	}
}
