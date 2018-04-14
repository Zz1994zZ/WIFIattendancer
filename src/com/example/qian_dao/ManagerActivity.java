package com.example.qian_dao;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ManagerActivity extends Activity {
	private final static int REQUEST_CODE=1;
	private WifiManager wifiManager;
	private String name;
	private ListView list;
	private int acnum;
	SimpleAdapter adapter;
	private User user;
	MActivity ma[];
	int success;
	ProgressBar p;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		p=(ProgressBar)findViewById(R.id.progressBar1);
        list=(ListView)findViewById(R.id.listView1);
	    Session session = Session.getSession();
	    user = (User)session.get("Info");  
	    refresh(null);
		//setWifiApEnabled(true);
	}
	 private OnItemClickListener clist=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(ManagerActivity.this,
					CheckInfoActivity.class);
			intent.putExtra("activity_id", ma[arg2].getActivity_id());
			intent.putExtra("depart_id", ma[arg2].getDepart_id());
			intent.putExtra("activity_name", ma[arg2].getActivity_name());
			intent.putExtra("activity_type", ma[arg2].getActivity_type());
			intent.putExtra("remark", ma[arg2].getRemark());
			startActivity(intent);
		}  
		 
	 };
	private void refreshList(){
	    adapter = new SimpleAdapter(ManagerActivity.this, getData(), R.layout.listconext, new String[] { "text",  "image" }, new int[] { R.id.text, R.id.image});
	    list.setAdapter(adapter);
	    list.setOnItemClickListener(clist);
	}
	private List< Map<String,Object>> getData(){
		  List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();  
	    Map<String, Object> map; 
	for(int i=0;i<acnum;i++){
		map = new HashMap<String, Object>();
		 map.put("text",ma[i].getActivity_name());
	    if(ma[i].getActivity_type()==1)
	    	 map.put("image",R.drawable.isacheck);
	    else 
	    	 map.put("image",R.drawable.isatoupiao);
	    data.add(map);
	    }
	    return data;
	}
	public void refresh(View v){
		new GetDatasTask().execute(null);
	}
	class GetDatasTask  extends AsyncTask<String,Void,Integer>{
		  @Override  
		    protected void onPreExecute() {  
		       p.setVisibility(View.VISIBLE);
		    }  
		@Override
		protected Integer doInBackground(String... a) {
			// TODO Auto-generated method stub
		    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		    	user.showMe();
		    	       params.add(new BasicNameValuePair("depart_id",user.getDepart_id()+""));
		    			try {
							JSONArray json=JSONParser.getJson(getString(R.string.urlPath)+"get_activity",params);
							if(json!=null){
							acnum=json.length();
						    ma=new MActivity[acnum];
							for(int i=0;i<acnum;i++){
								JSONObject j=json.getJSONObject(i);
								 ma[i]=new MActivity(j,i);
								ma[i].getActivity_name();
								System.out.println(ma[i].getActivity_name());
							}
							success=1;}
							else{
								success=0;
								}
						} catch (IOException e) {
							success=0;
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							success=0;
							e.printStackTrace();
						}
			return success;
		}
		@Override
		  protected void onPostExecute(Integer result) {
			p.setVisibility(View.INVISIBLE);
			if(result==0)
			{	acnum=0;
		    CustomToast.showToast(ManagerActivity.this, "网络错误，请重新尝试！", 3000);}
			refreshList();
		}

		
	}
	public boolean setWifiApEnabled(boolean enabled) {
		if (enabled) { // disable WiFi in any case
		//wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
		wifiManager.setWifiEnabled(false);
		}
		try {
		//热点的配置类
		WifiConfiguration apConfig = new WifiConfiguration();
		//配置热点的名称(可以在名字后面加点随机数什么的)
		apConfig.SSID = name;
		//配置热点的密码
		apConfig.preSharedKey="wifi";
		     //通过反射调用设置热点
		Method method = wifiManager.getClass().getMethod(
		"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
		//返回热点打开状态
		return (Boolean) method.invoke(wifiManager, apConfig, enabled);
		} catch (Exception e) {
		return false;
		}
		}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manager, menu);
		return true;
	}
	public void creatActivity(View v){
		Intent intent=new Intent();
		intent.setClass(ManagerActivity.this,CreateNewActivity.class);
		startActivityForResult(intent,REQUEST_CODE);
	}
	/**
	 * 处理注册返回值
	 */
	@Override
	protected void onActivityResult(int requestCode ,int resultCode , Intent data)           //注册的返回方法
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE){
		      name=(data.getStringExtra("name"));
		      setWifiApEnabled(true);
		      CustomToast.showToast(ManagerActivity.this,"已开启SSID："+name+"的热点~",3000);
		}
	}
	 @Override
	    public void onBackPressed() {
		 this.finish();
			     Intent intent= new Intent();
  	intent.setClass(ManagerActivity.this,MainChooseActivity.class);
  	startActivity(intent);
	    }

}
