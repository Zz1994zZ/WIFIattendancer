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
	 * �ӷ�������ȡJSON ����JSONObject���� ���ڵ�¼ע��ǩ��
	 * @param url
	 * @param params
	 * @param issent
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public JSONObject getJSONFromUrl (String url,List <NameValuePair> params,boolean issent) throws JSONException, IOException {
			//���� HTTP request Post ����
		    suc_respone=false;
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
			//���� Response ����
			HttpResponse httpResponse1 = new DefaultHttpClient().execute(httpPost);
			String retSrc = EntityUtils.toString(httpResponse1.getEntity(),"UTF-8");
			System.out.println("���ص��ַ���Ϊ"+retSrc);
			if(!retSrc.equals("")&&retSrc!=null){
			while(retSrc.charAt(0)!='{'&&retSrc.length()>0){
				retSrc=retSrc.substring(1);
			}		
			//ת��Ϊ JSON ����
			JSONObject jObj=null;
			try {
			jObj = new JSONObject(retSrc.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("��JSONParser������jsonʧ����~");
				e.printStackTrace();
				return null;
			}
			int  success=jObj.getInt("success");//��ȡ�Ƿ񷵻سɹ�
			System.out.println("�ں����з���Ϊ"+success);
			suc_respone= success==1 ?  true:false;//���سɹ���Ϊtrue
			return jObj;
			}
   return null;
			
	        }
	/**
	 * �ӷ�������ȡJSON���� ����JSONObject������� ���ڻ�ȡǩ���б�
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
		System.out.println("���ص��ַ���Ϊ"+retSrc);
		if(!retSrc.equals("")&&retSrc!=null){
			try {
				while(retSrc.length()>0&&retSrc.charAt(0)!='['&&retSrc.length()>0){
					retSrc=retSrc.substring(1);
				}		
				ja=new JSONArray(retSrc);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("����json����ʧ��~");
				ja=null;
				e.printStackTrace();
			}
		}
		return ja;

	}
}
