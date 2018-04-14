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
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Login extends Activity {
private final static int REQUEST_CODE=1;
private EditText showusername;
private EditText showpassword;
private  CheckBox showisrememberuser;
private  CheckBox showisautologin;
private int success=3;
private String username;
private String password;
private String localmac;
private Button login;
private User mUser;
private ProgressBar progressDialog2 = null; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//ʵ�������
		showusername= (EditText)findViewById(R.id.editText1);
		showpassword= (EditText)findViewById(R.id.editText2);
		showisrememberuser=(CheckBox)findViewById(R.id.checkBox1);
	    showisautologin=(CheckBox)findViewById(R.id.CheckBox01);
	    progressDialog2=(ProgressBar)findViewById(R.id.progressBar2);
	    login=(Button)findViewById(R.id.button1);
	    mUser=new User(this);
		//�ж��Ƿ�Ӵ���ر�����ļ���
        SharedPreferences sp=getSharedPreferences("mrsoft",MODE_PRIVATE);
        if(sp==null)
         username =sp.getString("username",null);
         password =sp.getString("password", null);
        Boolean isrememberuser=sp.getBoolean("isrememberuser",false);
     
       if(isrememberuser)
        {
          showusername.setText(username);
          showpassword.setText(password);
          showisrememberuser.setChecked(true);
        }
        else
          {
            showusername.setText("");
            showpassword.setText("");
            showisrememberuser.setChecked(false);
          }    
	}
	/**
	 * ������ֵ
	 */
	private  Handler handler=new Handler(){
		@Override
		 public void handleMessage(Message msg){
			  progressDialog2.setVisibility(View.INVISIBLE);
			  login.setClickable(true);
	 SharedPreferences sp=getSharedPreferences("mrsoft",MODE_PRIVATE);
	 Editor editor=sp.edit();
	 switch (success){
	 case 1://��½�ɹ�
	/*	 if(showisrememberuser.isChecked()){
                editor.putString("username",username);
                editor.putString("password",password);
                editor.putBoolean("isrememberuser",true);
                if(showisautologin.isChecked())
                	editor.putBoolean("isautologin",true);
                else
                	editor.putBoolean("isautologin",false);
		 }
		 else{
			 editor.putBoolean("isrememberuser",false); 
			 editor.putString("username",null);
             editor.putString("password",null);
		 }
		 editor.commit();   //���ر����˻�����
	     setTanchuang();	//������½�ɹ���ʾ��
	 break;*/
	 case 10:
		 if(showisrememberuser.isChecked()){
             editor.putString("username",username);
             editor.putString("password",password);
             editor.putBoolean("isrememberuser",true);
             if(showisautologin.isChecked())
             	editor.putBoolean("isautologin",true);
             else
             	editor.putBoolean("isautologin",false);
		 }
		 else{
			 editor.putBoolean("isrememberuser",false); 
			 editor.putString("username",null);
          editor.putString("password",null);
		 }
		 editor.commit();   //���ر����˻�����
	     setTanchuang();	//������½�ɹ���ʾ��
	     break;
	 case 0://�û���������
		    CustomToast.showToast(Login.this, "���û��������ڣ�", 1000);
		    break;
	 case 2://�������
		 CustomToast.showToast(Login.this, "�������", 1000);
		 showpassword.setText("");
		    break;
	 case 3:
		 CustomToast.showToast(Login.this, "��������ʧ�ܣ����飡", 1000);
		    break;
	 } 	 
			 super.handleMessage(msg);
		 }
		 };
		 /**
		  * ����ע�����
		  * @param view
		  */
	public void registratclick(View view){                                //���ע��
		Intent intent=new Intent();
		intent.setClass(Login.this,Registration.class);
		startActivityForResult(intent,REQUEST_CODE);
	}
	/**
	 * ���뱾��ϵͳ
	 * @param view
	 */
	public void gotoLocal(View view){
		Intent intent=new Intent();
		intent.setClass(Login.this,LocalWork.class);
		startActivity(intent);
	}
	/**
	 * �����¼�¼�
	 * @param view
	 */
	public void loginClick(View view){                           //�����¼
		  username=showusername.getText().toString();
		  password=showpassword.getText().toString();
		  localmac=mUser.getmacaddress(Login.this);
		  progressDialog2.setVisibility(View.VISIBLE);
		  login.setClickable(false);
		 //**********δ���*************//
		 new Thread( new Runnable() {
			 @Override
			public void run() {                       
				 String urlPath =getString(R.string.urlPath)+"Login";
			    	JSONParser jsonParser =new JSONParser();		
			    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			    			params.add(new BasicNameValuePair("user_name",username));
			    			params.add(new BasicNameValuePair("password",password));
			    			params.add(new BasicNameValuePair("local_mac",localmac));
			    			try {
								JSONObject json=jsonParser.getJSONFromUrl(urlPath,params,false);
								if(json!=null){
								  success=json.getInt("success");
								  User.copyUserInfo(json,mUser,success);
								}else 
								success=3;
				             //�����û���������ǰ�����������
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    			 System.out.println("�û���Ϊ��"+username);
			    			 System.out.println("�û����룺"+password);
			    			Message m=handler.obtainMessage();
			    			handler.sendMessage(m);
			 }        
			 }).start(); 

		 //
	}
	/**
	 * ����ע�᷵��ֵ
	 */
	@Override
	protected void onActivityResult(int requestCode ,int resultCode , Intent data)           //ע��ķ��ط���
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE){
		      showusername.setText(data.getStringExtra("username"));
		      showpassword.setText(data.getStringExtra("password"));
		}
	}
	/**
	 * 	���õ�������
	 * @param type
	 */
    public void setTanchuang()                                                 //������������
    {
    	//����¼�����
    	OnClickListener click=new OnClickListener(){                                           //����������ȷ����ť
			@Override
			   public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Login.this,MainChooseActivity.class);
		        Session session = Session.getSession();
		        mUser.setUser_name(username);
		        mUser.setPassword(password);
		        session.put("Info", mUser);
				startActivity(intent);				
			}
		};
		new AlertDialog.Builder(this)  
		.setTitle("��½�ɹ���")  
		.setIcon(android.R.drawable.ic_dialog_info)  
		.setPositiveButton("ȷ��", click)  
		.show();
    }
	 @Override
	    public void onBackPressed() {
		   Intent intent=new Intent();
		    	intent.setAction(Intent.ACTION_MAIN);
	        	intent.addCategory(Intent.CATEGORY_HOME);
	        	startActivity(intent);   
	  }


}
