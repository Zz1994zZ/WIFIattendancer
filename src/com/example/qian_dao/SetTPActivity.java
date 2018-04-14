package com.example.qian_dao;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SetTPActivity extends Activity {
EditText tp,c1,c2,c3,c4,c5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_tp);
		tp=(EditText)findViewById(R.id.EditText08);
		c1=(EditText)findViewById(R.id.EditText07);
		c2=(EditText)findViewById(R.id.EditText05);
		c3=(EditText)findViewById(R.id.EditText04);
		c4=(EditText)findViewById(R.id.EditText03);
		c5=(EditText)findViewById(R.id.EditText02);
	}
	/**
	 * 完成
	 * @param v
	 */
   public void finish(View v){
	   String tpc="",tg[]=new String [6];
     int num=0;
	    tg[0]=tp.getText().toString();
		tg[1]=c1.getText().toString();
		tg[2]=c2.getText().toString();
		tg[3]=c3.getText().toString();
		tg[4]=c4.getText().toString();
		tg[5]=c5.getText().toString();
		if(tg[0].equals("")){
			 CustomToast.showToast(SetTPActivity.this, "请输入投票内容！", 3000);
		    return;	 
		}
		tpc=tg[0];
        for(int i=1;i<6;i++){
        	if(!tg[i].equals(""))
        	{tpc=tpc+"!-!"+tg[i];num++;}
        }
        if(num<2){
			 CustomToast.showToast(SetTPActivity.this, "请至少输入两个选项内容！", 3000);
			    return;	 
        }
        tpc=num+"!-!"+tpc;
        System.out.println(tpc);
	   Intent intent=new Intent(); //设置返回账户密码到登录界面
		intent.putExtra("tp",tpc);     		
		setResult(Activity.RESULT_OK,intent);
	    finish();
   }
   /**
    * 重置
    * @param v
    */
   public void reset(View v){
	   tp.setText("");
	   c1.setText("");
	   c2.setText("");
	   c3.setText("");
	   c4.setText("");
	   c5.setText(""); 
   }
   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_t, menu);
		return true;
	}
	 @Override
	    public void onBackPressed() {
		 this.finish();
	 Intent intent= new Intent();
  	intent.setClass(SetTPActivity.this,CreateNewActivity.class);
  	startActivity(intent);
	    }

}
