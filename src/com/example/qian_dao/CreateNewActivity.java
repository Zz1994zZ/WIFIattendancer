package com.example.qian_dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateNewActivity extends Activity {
	private final static int REQUEST_CODE=1;
	private EditText name,remark,start_date,end_date,start_time,end_time;
	private String activity_name,activity_remark,tpcn=null;
	private int success=0;
	private ProgressBar progressBar1;
	private User user;
	private RadioButton qd,tp;
	private Button settp;
	private TextView r;
	private boolean isTP=false,setReady=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new);
		name=(EditText)findViewById(R.id.EditText05);
		start_date=(EditText)findViewById(R.id.EditText06);
		end_date=(EditText)findViewById(R.id.EditText01);
		start_time=(EditText)findViewById(R.id.EditText04);
		end_time=(EditText)findViewById(R.id.EditText03);
		remark=(EditText)findViewById(R.id.EditText02);
		progressBar1=(ProgressBar)findViewById(R.id.progressBar1);
		settp=(Button)findViewById(R.id.button3);
		qd=(RadioButton)findViewById(R.id.checkBox2);
		tp=(RadioButton)findViewById(R.id.checkBox1);
		r=(TextView)findViewById(R.id.TextView02);
	    Session session = Session.getSession();
	    user = (User)session.get("Info");
	}
	/**
	 * 点击设置投票
	 * @param v
	 */
	public void setTP(View v){
		System.out.println("~~settp");
		Intent intent=new Intent();
		intent.setClass(CreateNewActivity.this,SetTPActivity.class);
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
			tpcn=(data.getStringExtra("tp"));setReady=true;
		      }
		if(tpcn!=null){
			settp.setText("重新设置投票");
		}
	}
	public void clickQD(View v){
		isTP=false;
		settp.setVisibility(View.INVISIBLE);
		remark.setVisibility(View.VISIBLE);
		r.setVisibility(View.VISIBLE);
	}
	public void clickTP(View v){
		isTP=true;
		settp.setVisibility(View.VISIBLE);
		remark.setVisibility(View.INVISIBLE);
		r.setVisibility(View.INVISIBLE);
	}
	/**
	 * 处理返回值
	 */
	  private  Handler handler=new Handler(){
			@Override
			 public void handleMessage(Message msg){
				progressBar1.setVisibility(View.INVISIBLE);
         if(success==1){
 		    CustomToast.showToast(CreateNewActivity.this, "创建成功！", 3000);
     		Intent intent=new Intent(); //设置返回账户密码到登录界面
    		intent.putExtra("name",name.getText().toString());     		
    		intent.putExtra("remark",remark.getText().toString()); 
    		setResult(Activity.RESULT_OK,intent);
    	    finish();
         }
         else
         {
  		    CustomToast.showToast(CreateNewActivity.this, "创建失败！", 3000);
         }
			
				 super.handleMessage(msg);
			 }
			 };
	/**
	 * 返回管理界面启动热点
	 * @param v
	 */
	public void Apstart(View v){
		activity_name=name.getText().toString();
		activity_remark=remark.getText().toString();
		if(name.getText().toString().equals(""))
    	{CustomToast.showToast(CreateNewActivity.this,"活动名称不可为空！",3000);
    	return;
    	}
		else if(start_date.getText().toString().equals("")){
			CustomToast.showToast(CreateNewActivity.this,"起始日期不可为空！",3000);
		 return;
		}
		else if(end_date.getText().toString().equals("")){
			CustomToast.showToast(CreateNewActivity.this,"结束日期不可为空！",3000);
			return;
		}
		else if(start_time.getText().toString().equals("")){
			CustomToast.showToast(CreateNewActivity.this,"起始时间不可为空！",3000);
			return;
		}
		else if(end_time.getText().toString().equals("")){
			CustomToast.showToast(CreateNewActivity.this,"中止时间不可为空！",3000);
			return;
		}
		if(isTP&&(tpcn==null||tpcn.split("!-!").length<4))
		{
			CustomToast.showToast(CreateNewActivity.this,"投票设置有误，请重新设置！",3000);
			return;
		}
		progressBar1.setVisibility(View.VISIBLE);
   	 new Thread( new Runnable() {   
			 @Override
			public void run() {
    	JSONParser jsonParser =new JSONParser();
    	  String urlPath = getString(R.string.urlPath)+"create_activity";
    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    			params.add(new BasicNameValuePair("activity_name",activity_name));
    			params.add(new BasicNameValuePair("date_start",start_date.getText().toString()));
    			params.add(new BasicNameValuePair("date_end",end_date.getText().toString()));
    			params.add(new BasicNameValuePair("time_start",start_time.getText().toString()));
    			params.add(new BasicNameValuePair("time_end",end_time.getText().toString()));
    			params.add(new BasicNameValuePair("depart_id",user.getDepart_id()+""));
//区别
    			params.add(new BasicNameValuePair("remark",isTP? tpcn:activity_remark));
    			params.add(new BasicNameValuePair("activity_type",isTP ? "2":"1"));
    			try {
					JSONObject json=jsonParser.getJSONFromUrl(urlPath,params,false);
					 success=json.getInt("success");
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
	}
	/**
	 * 重置输入
	 * @param v
	 */
	public void reSet(View v){
		name.setText("");
		start_date.setText("");
		end_date.setText("");
		start_time.setText("");
		end_time.setText("");
		remark.setText("");
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	public void  setStartDate(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		            View view = View.inflate(this, R.layout.date, null); 
		            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker2); 
		            //final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.timePicker1); 
		            builder.setView(view); 
		            Calendar cal = Calendar.getInstance(); 
		            cal.setTimeInMillis(System.currentTimeMillis()); 
		           datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null); 
		                builder.setTitle("选取起始日期"); 
		                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() { 
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) { 
		                        StringBuffer sb = new StringBuffer(); 
		                        sb.append(String.format("%d-%02d-%02d",  
		                                datePicker.getYear(),  
		                                datePicker.getMonth() + 1, 
		                                datePicker.getDayOfMonth())); 
		                        sb.append("  "); 
		                       // sb.append(timePicker.getCurrentHour()) 
		                       // .append(":").append(timePicker.getCurrentMinute()); 
		                        start_date.setText(sb); 
		                        end_date.requestFocus(); 
		                        dialog.cancel(); 
		                    } 
		                }); 
		                Dialog dialog = builder.create(); 
		                dialog.show(); 

	}
    public void  setEndDate(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
        View view = View.inflate(this, R.layout.date, null); 
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker2); 
        //final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.timePicker1); 
        builder.setView(view); 
        Calendar cal = Calendar.getInstance(); 
        cal.setTimeInMillis(System.currentTimeMillis()); 
       datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null); 
            builder.setTitle("选取结束日期"); 
            builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() { 
                @Override 
                public void onClick(DialogInterface dialog, int which) { 
                    StringBuffer sb = new StringBuffer(); 
                    sb.append(String.format("%d-%02d-%02d",  
                            datePicker.getYear(),  
                            datePicker.getMonth() + 1, 
                            datePicker.getDayOfMonth())); 
                    sb.append("  "); 
                   // sb.append(timePicker.getCurrentHour()) 
                   // .append(":").append(timePicker.getCurrentMinute()); 
                   end_date.setText(sb); 
                    start_time.requestFocus(); 
                    dialog.cancel(); 
                } 
            }); 
            Dialog dialog = builder.create(); 
            dialog.show(); 
	}
    public void  setStartTime(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
        View view = View.inflate(this, R.layout.time, null); 
        //final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker); 
        final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.timePicker1); 
        builder.setView(view); 
        Calendar cal = Calendar.getInstance(); 
        cal.setTimeInMillis(System.currentTimeMillis()); 
       // datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null); 
        timePicker.setIs24HourView(true); 
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY)); 
        timePicker.setCurrentMinute(Calendar.MINUTE); 
            builder.setTitle("选取起始时间"); 
            builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() { 
                @Override 
                public void onClick(DialogInterface dialog, int which) { 
                    StringBuffer sb = new StringBuffer(); 
                    int h=timePicker.getCurrentHour(),m=timePicker.getCurrentMinute();
                    String hour=h+"",minute=m+"";
                    if(h<10)
                    	hour="0"+h;
                    if(m<10)
                    	minute="0"+m;
                    sb.append(hour) 
                    .append(":").append(minute).append(":00"); 
                    start_time.setText(sb); 
                    end_time.requestFocus(); 
                    dialog.cancel(); 
                } 
            }); 
            Dialog dialog = builder.create(); 
            dialog.show(); 
		
	}
    public void  setEndTime(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
        View view = View.inflate(this, R.layout.time, null); 
        //final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker); 
        final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.timePicker1); 
        builder.setView(view); 
        Calendar cal = Calendar.getInstance(); 
        cal.setTimeInMillis(System.currentTimeMillis()); 
       // datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null); 
        timePicker.setIs24HourView(true); 
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY)); 
        timePicker.setCurrentMinute(Calendar.MINUTE); 
            builder.setTitle("选取中止时间"); 
            builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() { 
                @Override 
                public void onClick(DialogInterface dialog, int which) { 
                    StringBuffer sb = new StringBuffer(); 
                    int h=timePicker.getCurrentHour(),m=timePicker.getCurrentMinute();
                    String hour=h+"",minute=m+"";
                    if(h<10)
                    	hour="0"+h;
                    if(m<10)
                    	minute="0"+m;
                    sb.append(hour) 
                    .append(":").append(minute).append(":00"); 
                    end_time.setText(sb); 
                    remark.requestFocus(); 
                    dialog.cancel(); 
                } 
            }); 
            Dialog dialog = builder.create(); 
            dialog.show(); 
	
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new, menu);
		return true;
	}
	 @Override
	    public void onBackPressed() {
		 this.finish();
			     Intent intent= new Intent();
     	intent.setClass(CreateNewActivity.this,ManagerActivity.class);
     	startActivity(intent);
	    }


}
