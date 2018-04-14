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
import android.widget.RadioButton;
import android.widget.TextView;
public class ToTPActivity extends Activity {
	TextView welcome,tg;
	RadioButton c[]=new RadioButton[5];
	String remark,wifi_mac;
	int choose=0,activity_id,sid;
	User user;
	ProgressBar p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_tp);
		welcome=(TextView)findViewById(R.id.textView1);
		tg=(TextView)findViewById(R.id.textView2);
		c[0]=(RadioButton)findViewById(R.id.radioButton1);
		c[1]=(RadioButton)findViewById(R.id.radioButton2);
		c[2]=(RadioButton)findViewById(R.id.radioButton3);
		c[3]=(RadioButton)findViewById(R.id.radioButton4);
		c[4]=(RadioButton)findViewById(R.id.radioButton5);
		p=(ProgressBar)findViewById(R.id.progressBar1);
	    Session session = Session.getSession();
	    user = (User)session.get("Info");
		Intent intent = getIntent();
		remark = intent.getStringExtra("remark");
		activity_id = intent.getIntExtra("activity_id", -1);
		sid= intent.getIntExtra("sid", -1);
		wifi_mac = intent.getStringExtra("wifi_mac");
		setView();
	}
public void sure(View v){
	//网络操作
	new GetDatasTask().execute(null);
}
public void cancel(View v){
	//直接返回
	onBackPressed();
}
class GetDatasTask  extends AsyncTask<String,Void,Integer>{
	  @Override  
	    protected void onPreExecute() {
		  p.setVisibility(View.VISIBLE);
	    for(int i=0;i<5;i++){
	    	if(c[i].isChecked())
	    		choose=i+1;
	    }
	    System.out.println("choose="+choose);
	    }  
	@Override
	protected Integer doInBackground(String... a) {
		// TODO Auto-generated method stub
		int success=0;
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	        params.add(new BasicNameValuePair("activity_id",activity_id+""));
	    	        params.add(new BasicNameValuePair("sid",sid+""));	    	         
	    			params.add(new BasicNameValuePair("user_id",user.getId()+""));
	    			params.add(new BasicNameValuePair("local_mac",user.getLocal_mac()));
	    			params.add(new BasicNameValuePair("wifi_mac",wifi_mac));
	    			params.add(new BasicNameValuePair("check",choose+""));
	    			System.out.println("choose="+choose);
	    			try {
	    				JSONParser jsonParser =new JSONParser();
						JSONObject json=jsonParser.getJSONFromUrl(getString(R.string.urlPath)+"do_check",params,false);
						if(json!=null){
                             success=json.getInt("success");
                             }
						else{
							success=0;
	 						}
	                     }
					catch (IOException e) {
						success=0;
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		return success;
	}
	@Override
	  protected void onPostExecute(Integer result) {
		  p.setVisibility(View.INVISIBLE);
          if(result==1){
        	  CustomToast.showToast(ToTPActivity.this,"投票成功~！", 2000);
        		onBackPressed();
          }
          else
     		 CustomToast.showToast(ToTPActivity.this,"未知错误！", 2000);
	}

	
}
private void setView(){
	String t[]=remark.split("!-!");
	if(t.length<4){
		return;
	}
	tg.setText(t[1]);
	for(int i=2;i<t.length;i++){
		c[i-2].setVisibility(View.VISIBLE);
		c[i-2].setText(t[i]);
	}
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_t, menu);
		return true;
	}

}
