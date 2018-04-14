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
	 * �������ͶƱ
	 * @param v
	 */
	public void setTP(View v){
		System.out.println("~~settp");
		Intent intent=new Intent();
		intent.setClass(CreateNewActivity.this,SetTPActivity.class);
		startActivityForResult(intent,REQUEST_CODE);
	}
	/**
	 * ����ע�᷵��ֵ
	 */
	@Override
	protected void onActivityResult(int requestCode ,int resultCode , Intent data)           //ע��ķ��ط���
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE){
			tpcn=(data.getStringExtra("tp"));setReady=true;
		      }
		if(tpcn!=null){
			settp.setText("��������ͶƱ");
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
	 * ������ֵ
	 */
	  private  Handler handler=new Handler(){
			@Override
			 public void handleMessage(Message msg){
				progressBar1.setVisibility(View.INVISIBLE);
         if(success==1){
 		    CustomToast.showToast(CreateNewActivity.this, "�����ɹ���", 3000);
     		Intent intent=new Intent(); //���÷����˻����뵽��¼����
    		intent.putExtra("name",name.getText().toString());     		
    		intent.putExtra("remark",remark.getText().toString()); 
    		setResult(Activity.RESULT_OK,intent);
    	    finish();
         }
         else
         {
  		    CustomToast.showToast(CreateNewActivity.this, "����ʧ�ܣ�", 3000);
         }
			
				 super.handleMessage(msg);
			 }
			 };
	/**
	 * ���ع�����������ȵ�
	 * @param v
	 */
	public void Apstart(View v){
		activity_name=name.getText().toString();
		activity_remark=remark.getText().toString();
		if(name.getText().toString().equals(""))
    	{CustomToast.showToast(CreateNewActivity.this,"����Ʋ���Ϊ�գ�",3000);
    	return;
    	}
		else if(start_date.getText().toString().equals("")){
			CustomToast.showToast(CreateNewActivity.this,"��ʼ���ڲ���Ϊ�գ�",3000);
		 return;
		}
		else if(end_date.getText().toString().equals("")){
			CustomToast.showToast(CreateNewActivity.this,"�������ڲ���Ϊ�գ�",3000);
			return;
		}
		else if(start_time.getText().toString().equals("")){
			CustomToast.showToast(CreateNewActivity.this,"��ʼʱ�䲻��Ϊ�գ�",3000);
			return;
		}
		else if(end_time.getText().toString().equals("")){
			CustomToast.showToast(CreateNewActivity.this,"��ֹʱ�䲻��Ϊ�գ�",3000);
			return;
		}
		if(isTP&&(tpcn==null||tpcn.split("!-!").length<4))
		{
			CustomToast.showToast(CreateNewActivity.this,"ͶƱ�����������������ã�",3000);
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
//����
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
    	        System.out.println("���������룡�̣����꣡����");
 }        
 }).start(); 
	}
	/**
	 * ��������
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
		                builder.setTitle("ѡȡ��ʼ����"); 
		                builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() { 
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
            builder.setTitle("ѡȡ��������"); 
            builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() { 
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
            builder.setTitle("ѡȡ��ʼʱ��"); 
            builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() { 
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
            builder.setTitle("ѡȡ��ֹʱ��"); 
            builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() { 
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
