package com.example.qian_dao;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class Registration extends Activity {
	String username,password,email,localmac,wifimac,name;
	private int success2=5;
	private EditText showpassword1;
	private EditText showusername;
	private EditText showpassword2;
	private EditText showemail,showname;
	private ProgressBar progressDialog = null; 
	private Button post;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		 showusername=  (EditText)findViewById(R.id.editText2);
		 showpassword1= (EditText)findViewById(R.id.editText1);
		 showpassword2= (EditText)findViewById(R.id.EditText01);
		 showemail=     (EditText)findViewById(R.id.editText3);
		 progressDialog=(ProgressBar)findViewById(R.id.progressBar2);
		 post= (Button)findViewById(R.id.button1);
		 showname=(EditText)findViewById(R.id.editText4);
	}
	private  Handler handler=new Handler(){
		 @SuppressLint("ShowToast")
		@Override
		 public void handleMessage(Message msg){
			 System.out.println("进来了"+success2);
			  progressDialog.setVisibility(View.INVISIBLE);
			  post.setClickable(true);
	 switch (success2){
	    case 1://成功
		    setTanchuang("周壮公司");//弹出公司名字的对话框
		break;
		case 0://用户名存在
			CustomToast.showToast(Registration.this, "你所连接wifi没在服务器注册~请联系我们！", 3000);
			break;
		case 2://手机已注册
			CustomToast.showToast(Registration.this, "用户名已存在或该手机已注册！", 3000);
			break;
		case 3://服务器崩了
			CustomToast.showToast(Registration.this, "哎 我们脆弱的数据库可能罢工了~", 3000);
			break;
		case 4://次wifimac没在服务器注册
			CustomToast.showToast(Registration.this, "你所连接wifi没在服务器注册~请联系我们！", 3000);
			break;
		case 5:
			CustomToast.showToast(Registration.this, "网络连接失败！请检查！", 3000);
			break;
		 }	 	 
			 super.handleMessage(msg);
		 }
		 };


	public void sureclick(View view){

		 System.out.println(showusername.getText());
		 System.out.println(showpassword1.getText());
		 System.out.println(showpassword2.getText());
		 System.out.println(showemail.getText());
		 System.out.println(showname.getText());
		 if(!((showpassword1.getText().toString()).equals((showpassword2.getText().toString()))))
		 {		
			 CustomToast.showToast(Registration.this,"两次输入密码不相同！", 3000);
			 showpassword1.setText("");
			 showpassword2.setText("");
		 }
		 else{
	    progressDialog.setVisibility(View.VISIBLE);
	    post.setClickable(false);
		username=showusername.getText().toString();
		password=showpassword1.getText().toString();
		email=showemail.getText().toString();
		localmac=getmacaddress();
		wifimac=getwifimac();
		name=showname.getText().toString();
		//***************上传上列数据到服务器****************//   
		 new Thread( new Runnable() {   
			 @Override
			public void run() { 
		   String urlPath =getString(R.string.urlPath)+"register";
		   System.out.println("已发送！0+"+urlPath);
		    	JSONParser jsonParser =new JSONParser();
		    	System.out.println("已发送！1");
		    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(); 
		    			params.add(new BasicNameValuePair("user_name",username));
		    			params.add(new BasicNameValuePair("name",name));
		    			params.add(new BasicNameValuePair("password",password));
		    			params.add(new BasicNameValuePair("email",email));
		    			params.add(new BasicNameValuePair("local_mac",Registration.this.getmacaddress()));
		    			params.add(new BasicNameValuePair("wifi_mac",Registration.this.getwifimac()));
		    			try {
		    				JSONObject json=jsonParser.getJSONFromUrl(urlPath,params,false);
		    				System.out.println("已发送！");
		    				if(json!=null)
							  success2=json.getInt("success");
							   System.out.println("html return is"+success2);
						} catch (JSONException e) {
							success2=5;
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							success2=5;
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			Message m=handler.obtainMessage();
		    			handler.sendMessage(m);
		 }     
		 }).start(); 
		 }
	}
	public void resetclick(View view){
		//重置所有文本内容
		System.out.println("进入重置");
		 System.out.println("实例化成功！");
		 showusername.setText("");
		 showpassword1.setText("");
		 showpassword2.setText("");
		 showemail.setText("");
		 showname.setText("");
		 System.out.println("重置完成");
	}
    public void setTanchuang(String cpn)
    {
    	String sign="注册成功！";
          //点击事件设置
    	OnClickListener click=new OnClickListener(){
			@Override
			   public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub				
					Intent intent=new Intent(); //设置返回账户密码到登录界面
					intent.putExtra("username", username);  
					intent.putExtra("password", password);
					setResult(Activity.RESULT_OK,intent);
				    finish();	
			}
		};
		//
		new AlertDialog.Builder(this)  
		.setTitle(sign)  
		.setIcon(android.R.drawable.ic_dialog_info)  
		.setPositiveButton("确定", click)  
		.show();
    }
	 @Override
	    public void onBackPressed() {
		 this.finish();
			     Intent intent= new Intent();
        	intent.setClass(Registration.this,Login.class);
        	startActivity(intent);
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

}
