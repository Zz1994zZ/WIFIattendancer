package com.example.qian_dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyInfo extends Activity {
User user;
Button post,cancel;
ListView list;
Session session;
int success=3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		post=(Button)findViewById(R.id.button2);
		cancel=(Button)findViewById(R.id.button3);
		list=(ListView) findViewById(R.id.listView1);
	     session = Session.getSession();
	     user = (User)session.get("Info");
	    refreshList();
	}
	private List< Map<String,Object>> getData(){
		  List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();  
	    Map<String, Object> map; 
 //name
		map = new HashMap<String, Object>();
		 map.put("text1","姓名");
		 if(!user.isManager())
	     map.put("text2",user.getName());
		 else
	     map.put("text2","管理员"+user.getDepart_id());	 
		 data.add(map);
//depart_name	 
		map = new HashMap<String, Object>();
		 map.put("text1","单位");
	     map.put("text2",user.getDepart_name());
	    data.add(map);
//email	  	    
		map = new HashMap<String, Object>();
		 map.put("text1","E-mail");
	     map.put("text2",user.getEmail());
	    data.add(map);
//remark	    
		map = new HashMap<String, Object>();
		 map.put("text1","备注");
	     map.put("text2",user.getRemark());
	    data.add(map);
//mac	    
		map = new HashMap<String, Object>();
		 map.put("text1","MAC");
	    	if(user.getLocal_mac().equals(user.getmacaddress(MyInfo.this)))
	    		 map.put("text2",user.getmacaddress(MyInfo.this)+" 已绑定");
	    	else
	    		 map.put("text2",user.getmacaddress(MyInfo.this)+" 未绑定");
	    data.add(map);
	    
	    return data;
	}
    private void refreshList(){
    	//////////////////////
		SimpleAdapter adapter2 = new SimpleAdapter(MyInfo.this, getData(), R.layout.info_list, new String[] { "text1",  "text2" }, new int[] { R.id.textView1, R.id.textView2}); 
		  //添加并且显示  
		  list.setOnItemClickListener(new OnItemClickListener() {  	    
	          @Override  
	          public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,  
	                 long arg3) { 
	        	  if(user.isManager())
	        		  return;
	        	  setTanChuang(arg2+1);
	          }	      
	     });
	      list.setAdapter(adapter2);
    	
    	return;
    	///////////////////////
  }
    private void setTanChuang(final int type){
    	String sign="~~";
    	final EditText ev=new EditText(this);

    	OnClickListener click=new OnClickListener(){                                           //监听弹窗的确定按钮
			@Override
			   public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
		    	switch(type){
		    	case 1:
		    		user.setName(ev.getText().toString());
		    		break;
		    	case 2:
		    		user.setDepart_name(ev.getText().toString());
		    		break;
		    	case 3:
		    	    user.setEmail(ev.getText().toString());
		            break;
		    	case 4:
		    	    user.setRemark(ev.getText().toString());
		            break;
		    	}
				 new Thread( new Runnable() {
					 @Override
					public void run() {                       
						 String urlPath =getString(R.string.urlPath)+"update_user_info";
					    	JSONParser jsonParser =new JSONParser();		
					    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					    			params.add(new BasicNameValuePair("name",user.getName()));
					    			params.add(new BasicNameValuePair("email",user.getEmail()));
					    			params.add(new BasicNameValuePair("id",user.getId()+""));
					    			try {
										JSONObject json=jsonParser.getJSONFromUrl(urlPath,params,false);
										if(json!=null){
										  success=json.getInt("success");									
										}else 
										success=3;
						             //发送用户名和密码前往服务器配对
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										success=3;
										e.printStackTrace();
									} catch (IOException e) {
										success=3;
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
					 }        
					 }).start(); 
				//传送数据库后返回
				session.cleanUpSession();
				session.put("Info", user);
               //刷新列表
				refreshList();
			}
		};
    	switch(type){
    	case 1:
    		if(user.isManager())
    			return;
    		sign="新名字：";
    		break;
    	/*case 2:
    		if(!user.isManager())
    			return;
    		sign="新部门名称：";
    		break;*/
    	case 3:
    	   sign="新邮箱地址：";
            break;
    	/*case 4:
    		sign="新备注:";
            break;
    	case 5:
    		return;*/
    	default:
    		return;
    	}
  		new AlertDialog.Builder(this)  
		.setTitle(sign)  
		.setIcon(android.R.drawable.ic_dialog_info)  
		.setView(ev) 
		.setPositiveButton("确定", click)  
	    .setNegativeButton("取消", null)  
		.show();
    }
    public void binding(View v){
    	
    }
    public void post(View v){
    	    post.setVisibility(View.INVISIBLE);
    	    cancel.setVisibility(View.INVISIBLE);
        	CustomToast.showToast(MyInfo.this,"提交成功，重新登录后生效！",3000,0);
    }    
    public void cancel(View v){
    	  post.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
            goBack();
            refreshList();
    }
    public void goBack(){
    }
    
    
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_info, menu);
		return true;
	}*/

}
