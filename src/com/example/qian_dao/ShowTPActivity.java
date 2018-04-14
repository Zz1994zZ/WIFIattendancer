package com.example.qian_dao;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ShowTPActivity extends Activity {
	private int activity_id,sid,depart_id,yes=0,no=0,all=0,num=0;
	TextView t;
	ProgressBar p;
	User user[];
	private int acnum=0;
	String remark;
    TextView x1[]=new TextView[3],x2[]=new TextView[3],x3[]=new TextView[3],x4[]=new TextView[3],x5[]=new TextView[3];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_tp);
		t=(TextView)findViewById(R.id.textView1);
		p=(ProgressBar)findViewById(R.id.progressBar1);
		x1[0]=(TextView)findViewById(R.id.textView2);
		x1[1]=(TextView)findViewById(R.id.textView3);
		x1[2]=(TextView)findViewById(R.id.textView4);
		
		x2[0]=(TextView)findViewById(R.id.TextView01);
		x2[1]=(TextView)findViewById(R.id.TextView03);
		x2[2]=(TextView)findViewById(R.id.TextView02);
		
		x3[0]=(TextView)findViewById(R.id.TextView04);
		x3[1]=(TextView)findViewById(R.id.TextView06);
		x3[2]=(TextView)findViewById(R.id.TextView05);
		
		x4[0]=(TextView)findViewById(R.id.TextView07);
		x4[1]=(TextView)findViewById(R.id.TextView09);
		x4[2]=(TextView)findViewById(R.id.TextView08);
		
		x5[0]=(TextView)findViewById(R.id.TextView10);
		x5[1]=(TextView)findViewById(R.id.TextView12);
		x5[2]=(TextView)findViewById(R.id.TextView11);
		
		Intent intent = getIntent();
		activity_id = intent.getIntExtra("activity_id", -1);
		sid=intent.getIntExtra("sid", -1);
		depart_id=intent.getIntExtra("depart_id", -1);
		remark=intent.getStringExtra("remark");
		System.out.println(activity_id+"~~"+sid+"~~"+depart_id+"~~"+remark);
		handleR();
		new GetDatasTask().execute(null);
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
		    	       params.add(new BasicNameValuePair("sid",sid+""));                  
		    	       params.add(new BasicNameValuePair("depart_id",depart_id+""));
		    	       System.out.println(activity_id+"!!"+sid+"~~!"+depart_id);
		    	       int success=0;
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
							user[i]=new User(ShowTPActivity.this);
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
			int a[]={0,0,0,0,0};
			float b[]={0,0,0,0,0};
			String c[]={"","","","",""};
		for(int i=0;i<acnum;i++){
			if(user[i].getCheck()-1>=0)
			a[user[i].getCheck()-1]++;
		}
		for(int i=0;i<num;i++){
			b[i]=(float)a[i]/acnum;
			switch(i){
			case 0:
				x1[2].setText((int)(b[i]*100)+"%");
				break;
			case 1:
				x2[2].setText((int)(b[i]*100)+"%");
				break;
			case 2:
				x3[2].setText((int)(b[i]*100)+"%");
				break;
			case 3:
				x4[2].setText((int)(b[i]*100)+"%");
				break;
			case 4:
				x5[2].setText((int)(b[i]*100)+"%");
				break;
			}
			for(int j=0;j<(int)(b[i]*10);j++)
			c[i]+="哈";
		}
		
		x1[1].setText(c[0]);
		x2[1].setText(c[1]);
		x3[1].setText(c[2]);
		x4[1].setText(c[3]);
		x5[1].setText(c[4]);
		t.setText("总人数："+all+"\n");
		for(int i=0;i<num;i++){
			t.setText(t.getText()+"选项"+(i+1)+"："+a[i]+"人\n");
		}
			//t.setText("总人数："+all+"\n"+"已签到："+yes+"\n"+"未签到："+no);
			p.setVisibility(View.INVISIBLE);
		}
		

}
	private void handleR(){
		String x[]=remark.split("!-!");
		if(!(x.length>=3))
			return;
		t.setText(x[1]);
		num=x.length-2;
		switch(num){
		case 5:
			for(int i=0;i<3;i++)
				x5[i].setVisibility(View.VISIBLE);
			x5[0].setText(x[6]);
		case 4:
			for(int i=0;i<3;i++)
				x4[i].setVisibility(View.VISIBLE);
			x4[0].setText(x[5]);
		case 3:
			for(int i=0;i<3;i++)
				x3[i].setVisibility(View.VISIBLE);
			x3[0].setText(x[4]);
		case 2:
			for(int i=0;i<3;i++)
				x2[i].setVisibility(View.VISIBLE);
			x2[0].setText(x[3]);
		case 1:
			for(int i=0;i<3;i++)
				x1[i].setVisibility(View.VISIBLE);
			x1[0].setText(x[2]);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_t, menu);
		return true;
	}

}
