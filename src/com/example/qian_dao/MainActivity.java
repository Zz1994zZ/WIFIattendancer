package com.example.qian_dao;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {
  private User mUser;
  private Boolean isrememberuser;
  private Boolean isautologin;
  private int success=4;
  private ProgressBar progressDialog3 = null; 
  private  Handler handler=new Handler(){
		 @SuppressLint("ShowToast")
		@Override
		 public void handleMessage(Message msg){
			 System.out.println("������");
			 progressDialog3.setVisibility(View.INVISIBLE);
	 if(success==1||success==10){
	        Session session = Session.getSession();
	        session.put("Info", mUser);
         Intent intent= new Intent();
     	intent.setClass(MainActivity.this,MainChooseActivity.class);
     	startActivity(intent);
     	}
     else //�����û������ڣ�ǰ����¼activity
		{
     	switch(success){
         case 0://�û���������
			    Toast.makeText(MainActivity.this, "���û��������ڣ�", 1000).show();
		    	break;
		 case 2://���ֻ�mac��ע��
			 Toast.makeText(MainActivity.this, "�������", 1000).show();
			    break;
		 case 3://����������
			  Toast.makeText(MainActivity.this, "���ݿ���˵�����~�㶮�� ���˻���û�취%>_<%", 1000).show();
			    break;
		 case 4:
			 Toast.makeText(MainActivity.this, "���������һ����~", 1000).show();
			 break;
     	}
     	System.out.println("�û���֤ʧ��ֱ��ǰ����¼");
    	Intent intent= new Intent();
    	intent.setClass(MainActivity.this,Login.class);
    	startActivity(intent);
		}
			 super.handleMessage(msg);
		 }
		 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("������� ");
		mUser=new User(this);
		//�ж��Ƿ�Ӵ���ر�����ļ���
        SharedPreferences sp=getSharedPreferences("mrsoft",MODE_PRIVATE);
        mUser.setUser_name(sp.getString("username",null)) ;
        mUser.setPassword(sp.getString("password", null)) ;
        isrememberuser=sp.getBoolean("isrememberuser",false);
        isautologin=sp.getBoolean("isautologin",false);
        progressDialog3=(ProgressBar)findViewById(R.id.progressBar3);
        if(mUser.getUser_name()!=null&&mUser.getPassword()!=null&&isautologin&&isrememberuser)//�жϱ�����û������Ƿ����~�Ƿ��趨�Զ���¼
        {
        	System.out.println("�����û�����ֱ�ӵ�¼");
        	progressDialog3.setVisibility(View.VISIBLE);
        	//���ر����û����ڲ������Զ���¼��ֱ��ת����ǩ��activity
        	//*******************��֤�˻�����~~********************//
        	 new Thread( new Runnable() {   
    			 @Override
				public void run() { 
     	   String urlPath = getString(R.string.urlPath)+"Login";
     	  System.out.println(urlPath);
     	  mUser.showMe();
	    	JSONParser jsonParser =new JSONParser();
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    			params.add(new BasicNameValuePair("user_name",mUser.getUser_name()));
	    			params.add(new BasicNameValuePair("password",mUser.getPassword()));
	    			params.add(new BasicNameValuePair("local_mac",mUser.getLocal_mac()));
	    			try {
						JSONObject json=jsonParser.getJSONFromUrl(urlPath,params,false);
						if(json!=null){
						success=json.getInt("success");
						  User.copyUserInfo(json,mUser,success);}
						else
							success=3;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			Message m=handler.obtainMessage();
	    			handler.sendMessage(m);
	    	        System.out.println("���������룡�̣����꣡����");
	 }        
	 }).start(); 
        	//��֤�ɹ�*****************************************//
        			}
        else
        {
        	System.out.println("�����û�������ֱ��ǰ����¼");
        	Intent intent= new Intent();
        	intent.setClass(MainActivity.this,Login.class);
        	startActivity(intent);
        }
        System.out.println("����������");
	}
}

