package com.example.qian_dao;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainChooseActivity extends Activity {
	private User user;
	private ImageButton manager,network;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_choose);
		manager=(ImageButton)findViewById(R.id.ImageButton02);
		network=(ImageButton)findViewById(R.id.ImageButton03);
	    Session session = Session.getSession();
	    user = (User)session.get("Info");
	    if(user.isManager())
	    	network.setVisibility(View.INVISIBLE);
	    else
	    	manager.setVisibility(View.INVISIBLE);
	}
	public void localwork(View v){
		Intent intent= new Intent();
		intent.setClass(MainChooseActivity.this,LocalWork.class);
    	startActivity(intent);
	}
	public void info(View v){
		Intent intent= new Intent();
		intent.setClass(MainChooseActivity.this,MyInfo.class);
    	startActivity(intent);
	}
	public void relogin(View v){
		Intent intent= new Intent();
		intent.setClass(MainChooseActivity.this,Login.class);
    	startActivity(intent);
	}
	public void network(View v){
		Intent intent= new Intent();
		intent.setClass(MainChooseActivity.this,Main_Check.class);
    	startActivity(intent);
	}
	public void superTool(View v){
		Intent intent= new Intent();
		intent.setClass(MainChooseActivity.this,ManagerActivity.class);
    	startActivity(intent);
	}

	 @Override
	    public void onBackPressed() {
			   Intent intent=new Intent();
		    	intent.setAction(Intent.ACTION_MAIN);
	        	intent.addCategory(Intent.CATEGORY_HOME);
	        	startActivity(intent);   
	    }
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {                                        //设置菜单
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main__check, menu);
			return true;
		}
		@Override                                                                              //设置菜单点击效果
		public boolean onOptionsItemSelected(MenuItem item) {
			Intent intent= new Intent();
		    switch(item.getItemId()) {
		    case R.id.item1:	
	        	intent.setClass(MainChooseActivity.this,Login.class);
	        	startActivity(intent);   
		break;
		    case R.id.item2:
		    	Intent startMain = new Intent(Intent.ACTION_MAIN);
	            startMain.addCategory(Intent.CATEGORY_HOME);
	            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(startMain);
	            System.exit(0);
		break;
		    }
		    return true;
		}

}
