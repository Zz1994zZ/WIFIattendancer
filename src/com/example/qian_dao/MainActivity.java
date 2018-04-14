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
			 System.out.println("进来了");
			 progressDialog3.setVisibility(View.INVISIBLE);
	 if(success==1||success==10){
	        Session session = Session.getSession();
	        session.put("Info", mUser);
         Intent intent= new Intent();
     	intent.setClass(MainActivity.this,MainChooseActivity.class);
     	startActivity(intent);
     	}
     else //本地用户不存在，前往登录activity
		{
     	switch(success){
         case 0://用户名不存在
			    Toast.makeText(MainActivity.this, "该用户名不存在！", 1000).show();
		    	break;
		 case 2://此手机mac已注册
			 Toast.makeText(MainActivity.this, "密码错误！", 1000).show();
			    break;
		 case 3://服务器崩了
			  Toast.makeText(MainActivity.this, "数据库出了点问题~你懂得 便宜货嘛没办法%>_<%", 1000).show();
			    break;
		 case 4:
			 Toast.makeText(MainActivity.this, "网络错误检查一下撒~", 1000).show();
			 break;
     	}
     	System.out.println("用户验证失败直接前往登录");
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
		System.out.println("进入程序 ");
		mUser=new User(this);
		//判断是否哟本地保存的文件；
        SharedPreferences sp=getSharedPreferences("mrsoft",MODE_PRIVATE);
        mUser.setUser_name(sp.getString("username",null)) ;
        mUser.setPassword(sp.getString("password", null)) ;
        isrememberuser=sp.getBoolean("isrememberuser",false);
        isautologin=sp.getBoolean("isautologin",false);
        progressDialog3=(ProgressBar)findViewById(R.id.progressBar3);
        if(mUser.getUser_name()!=null&&mUser.getPassword()!=null&&isautologin&&isrememberuser)//判断保存的用户密码是否存在~是否设定自动登录
        {
        	System.out.println("本地用户存在直接登录");
        	progressDialog3.setVisibility(View.VISIBLE);
        	//本地保存用户存在并设置自动登录，直接转跳到签到activity
        	//*******************验证账户密码~~********************//
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
	    	        System.out.println("！进！！入！程！序！完！！毕");
	 }        
	 }).start(); 
        	//验证成功*****************************************//
        			}
        else
        {
        	System.out.println("本地用户不存在直接前往登录");
        	Intent intent= new Intent();
        	intent.setClass(MainActivity.this,Login.class);
        	startActivity(intent);
        }
        System.out.println("进入程序完毕");
	}
}

