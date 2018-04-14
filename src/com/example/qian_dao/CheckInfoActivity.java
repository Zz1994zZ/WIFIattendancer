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

import com.example.qian_dao.ManagerActivity.GetDatasTask;

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

public class CheckInfoActivity extends Activity {
	private int activity_id,depart_id,activity_type;
	private String activity_name,remark;
	private ListView list;
	private int acnum=0;
	SimpleAdapter adapter;
	CheckInfo ci[];
	int success=0;
	ProgressBar p;
	TextView show;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_info);
		list=(ListView)findViewById(R.id.listView1);
		p=(ProgressBar)findViewById(R.id.progressBar1);
		show=(TextView)findViewById(R.id.textView1);
		Intent intent = getIntent();
		activity_id = intent.getIntExtra("activity_id", -1);
		depart_id   = intent.getIntExtra("depart_id", -1);
		activity_type   = intent.getIntExtra("activity_type", -1);
		activity_name=intent.getStringExtra("activity_name");
		remark=intent.getStringExtra("remark");
		show.setText(activity_name);
		System.out.println(activity_name+"~"+activity_id+"~~"+depart_id+remark);
		new GetDatasTask().execute(null);
	}
	 private OnItemClickListener clist=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent ;
			if(activity_type==1)
			intent= new Intent(CheckInfoActivity.this,
					Detail_checkActivity.class);
			else
			intent= new Intent(CheckInfoActivity.this,
						ShowTPActivity.class);
			intent.putExtra("activity_id",ci[arg2].getActivity_id());
			intent.putExtra("sid", ci[arg2].getSid());
			intent.putExtra("depart_id", ci[arg2].getDepart_id());
			intent.putExtra("remark",remark);
			startActivity(intent);
		}  
		 
	 };
	private void refreshList(){

	    adapter = new SimpleAdapter(CheckInfoActivity.this, getData(), R.layout.listconext, new String[] { "text",  "image" }, new int[] { R.id.text, R.id.image});
	    list.setAdapter(adapter);
	    list.setOnItemClickListener(clist);
	}
	private List< Map<String,Object>> getData(){
		  List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();  
	    Map<String, Object> map; 
	for(int i=0;i<acnum;i++){
		map = new HashMap<String, Object>();
		 map.put("text",ci[i].getDate());
	     map.put("image",R.drawable.info);
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
		    	       params.add(new BasicNameValuePair("activity_id",activity_id+""));	    	      	      
		    			try {
							JSONArray json=JSONParser.getJson(getString(R.string.urlPath)+"get_check",params);
							if(json!=null){
							acnum=json.length();
							ci=new CheckInfo [acnum];
							for(int i=0;i<acnum;i++){
							JSONObject j=json.getJSONObject(i);
							ci[i]=new CheckInfo(j.getInt("activity_id"),j.getInt("sid"),depart_id,j.getString("date"));
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
		    CustomToast.showToast(CheckInfoActivity.this, "ÍøÂç´íÎó£¬ÇëÖØÐÂ³¢ÊÔ£¡", 3000);
		    onBackPressed();
			}
			refreshList();
		}

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check_info, menu);
		return true;
	}

}
