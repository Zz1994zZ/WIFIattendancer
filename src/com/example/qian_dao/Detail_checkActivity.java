package com.example.qian_dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.qian_dao.CheckInfoActivity.GetDatasTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class Detail_checkActivity extends Activity {
	private int activity_id,sid,depart_id,yes=0,no=0,all=0;
	private ListView list,list2;
	private int acnum=0;
	SimpleAdapter adapter,adapter2;
	TextView t;
	int success;
	User user[];
	ProgressBar p;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_check);
		t=(TextView)findViewById(R.id.textView1);
		list=(ListView)findViewById(R.id.listView1);
		list2=(ListView)findViewById(R.id.listView2);
		p=(ProgressBar)findViewById(R.id.progressBar1);
		Intent intent = getIntent();
		activity_id = intent.getIntExtra("activity_id", -1);
		sid=intent.getIntExtra("sid", -1);
		depart_id=intent.getIntExtra("depart_id", -1);
		System.out.println(activity_id+"~~"+sid+"~~"+depart_id);
		new GetDatasTask().execute(null);
	}
	 private OnItemClickListener clist=new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}  
			 
		 };
		private void refreshList(){
		    adapter = new SimpleAdapter(Detail_checkActivity.this, getData(), R.layout.listconext, new String[] { "text",  "image" }, new int[] { R.id.text, R.id.image});
		    list.setAdapter(adapter);
		    list.setOnItemClickListener(clist);
		    adapter2=new SimpleAdapter(Detail_checkActivity.this, getData2(), R.layout.listconext, new String[] { "text",  "image" }, new int[] { R.id.text, R.id.image});
		    list2.setAdapter(adapter2);
		    list2.setOnItemClickListener(clist);
		    t.setText("总人数："+all+"\n"+"已签到："+yes+"\n"+"未签到："+no);
		}
		private List< Map<String,Object>> getData(){
			  List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();  
		    Map<String, Object> map; 
		for(int i=0;i<acnum;i++){
		    if(user[i].getCheck()==1){
			map = new HashMap<String, Object>();
			 map.put("text",user[i].getName());
		    data.add(map);
		    }
		    }
		    return data;
		}
		private List< Map<String,Object>> getData2(){
			  List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();  
		    Map<String, Object> map; 
		for(int i=0;i<acnum;i++){
		    if(user[i].getCheck()==0){
			map = new HashMap<String, Object>();
			map.put("text",user[i].getName());
		    data.add(map);
		    }
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
			    	       System.out.println(activity_id+"!!"+sid+"~~!"+depart_id);
			    	       params.add(new BasicNameValuePair("activity_id",activity_id+""));
			    	       params.add(new BasicNameValuePair("sid",sid+""));                  
			    	       params.add(new BasicNameValuePair("depart_id",depart_id+""));		    	       
			    			try {
								JSONArray json=JSONParser.getJson(getString(R.string.urlPath)+"get_detail_check",params);
								if(json!=null){
								acnum=json.length();
								user=new User[acnum];
								for(int i=0;i<acnum;i++){
								JSONObject j=json.getJSONObject(i);
								yes=j.getInt("yes");
								no=j.getInt("no");
								all=j.getInt("all");
								user[i]=new User(Detail_checkActivity.this);
								User.copyUserInfo(j,user[i],100);
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
			    CustomToast.showToast(Detail_checkActivity.this, "网络错误，请重新尝试！", 3000);
			    onBackPressed();
				}
				refreshList();
			}

}
}
