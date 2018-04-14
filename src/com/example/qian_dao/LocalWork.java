package com.example.qian_dao;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LocalWork extends Activity {
 EditText ev;
 TextView textshow,zhuangtai;
 String IP="";
 Boolean isServeing=false,setIp=false,showb=false;
 SocketServer server=null;
 Handler createC;
 ImageButton check,zc,ms,ch,menu;
 int count;
 View layout;
 local_user[] lug; 
 int luNum=0;
 ListView lt; 
 AlertDialog a;
 ScrollView scrollView;
 /*
  * ������ܵ�����Ϣ
  */
	public Handler my_hanler=new Handler(){
        @Override
		public   void  handleMessage(Message   msg){             //����ͨ���̴߳��ݵ���Ϣ
            super.handleMessage(msg);
            String  text   =  msg.getData().getString("text");     //��ȡMessage�е�����
            String ip=null,mac=null,m=null;
            int type=1;
           	System.out.println("got jsonString"+text);
            JSONObject j=null;
            try {
            	if(text!=null){
               	System.out.println("����ing....~");
                j= new JSONObject(text);
            	System.out.println("���ܲ�����json�ɹ���~");
				ip=j.getString("ip");
				mac=j.getString("mac");
				m=j.getString("msg");
				type=j.getInt("type");
            	}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            JSONArray ja;
           if(textshow.getLineCount()>=20)
                textshow.setText("��һҳ��¼��");
            ja=readSaveData();
            String name=null;
            if(getLu(mac)==-1) name=null;
            else
            name=lug[getLu(mac)].getName();
            switch(type){
            case 1:    
            	if(name==null)
            		textshow.setText(textshow.getText()+"\nnew:����İ���˵���Ϣ��\n"+m);
            		else            		
                    textshow.setText(textshow.getText()+"\n����"+name+"����Ϣ��\n"+m);                    //������ݵ���Ϣ��¼��
                    System.out.println("���ˢ��UI");
            	break;
            case 2:
                if(name==null){
                textshow.setText(textshow.getText()+"\n"+m+":δע���û�ǩ����");     
                sendM("δע��",20,ip);
                }
                else  //if(m.equals(name))
                { 
                	if( lug[getLu(mac)].isChecked()) return;
                textshow.setText(textshow.getText()+"\n"+m+"����ǩ����"+m); 
                count++;
                zhuangtai.setText("״̬\nǩ����  \n  "+count+"/"+luNum);
                 lug[getLu(mac)].Check();
                 lug[getLu(mac)].setIp(ip);
                 refreshList(); 
                 sendM("ǩ���ɹ�",21,ip);
                } 
                //else     textshow.setText(textshow.getText()+"\n"+name+"����Ϊ"+m+"ǩ����"); 
                System.out.println("���ˢ��UI");
            	break;
            case 3:
            	System.out.println("��ȥ��~");
                if(ja!=null){
                	if(name==null){
                    addsave(ja,j);
                	writeLug();
                	refreshList();
                    textshow.setText(textshow.getText()+"\n"+m+"����ע�ᵽ�ֻ���"+mac);} 
                	else  textshow.setText(textshow.getText()+"\n"+name+"�ظ�ע��Ϊ��"+m);
                }else{
                	addsave(new JSONArray(),j);
                	writeLug();
                	refreshList();
                	textshow.setText(textshow.getText()+"\n"+m+"����ע�ᵽ�ֻ���"+mac);
                	System.out.println("��ʼ���洢���ݣ�");
                }
                System.out.println("���ˢ��UI");
            	break;
            case 21:
            	textshow.setText(textshow.getText()+"\nǩ���ɹ�~��"); 
            	CustomToast.showToast(LocalWork.this,"ǩ���ɹ���\n���Ǻ�ѧ��ŶO(��_��)O~",3000,0);
            	break;
            case 20:
            	textshow.setText(textshow.getText()+"\nǩ��ʧ��-δע�ᵽ��Ŀ��~��"); 
          		CustomToast.showToast(LocalWork.this,"ʧ�ܣ�\nδע�ᵽĿ��",2000);
            	break;
            }
            createC.post(new Runnable() {  
            	    @Override  
            	   public void run() {  
            	        scrollView.fullScroll(View.FOCUS_DOWN);  
            	    }  
            	});  

   }
};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_work);
		textshow=(TextView)findViewById(R.id.message);
		zhuangtai=(TextView)findViewById(R.id.show);
		check=(ImageButton)findViewById(R.id.ImageButton01);
		zc=(ImageButton)findViewById(R.id.ImageButton02);
		ms=(ImageButton)findViewById(R.id.ImageButton03);
		ch=(ImageButton)findViewById(R.id.imageButton5);
		menu=(ImageButton)findViewById(R.id.imageButton4);
		scrollView=(ScrollView)findViewById(R.id.scrollView1);
	   System.out.println("��~1");
		check.setOnClickListener(BLitener); 
		zc.setOnClickListener(BLitener);
		ms.setOnClickListener(BLitener);
		ch.setOnClickListener(BLitener);
		 System.out.println("~~~2");
        //
		 LayoutInflater inflater = getLayoutInflater();
		 layout = inflater.inflate(R.layout.dialog,(ViewGroup) findViewById(R.id.dialog));
		 lt=(ListView)layout.findViewById(R.id.dialog);	
		 setLt();
		 a=new AlertDialog.Builder(LocalWork.this).setTitle("��������").setView(layout)
				 .create();
		//
		connectWifi(); 
		createC=new Handler();
		if(server==null)
		server=new SocketServer(3000);
		reSetIp();
		lug=new local_user [50];
		writeLug();
		refreshList();
		startRecive();
	}
	/*
	 * ��ʼ��LIST
	 */
	private void setLt(){
		lt.setOnItemClickListener(new OnItemClickListener() {  			    
	          @Override  
	          public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,  
	                 long arg3) { 
	        		if(!lug[arg2].isChecked())return;
	  	    	OnClickListener click=new OnClickListener(){                                           //����������ȷ����ť
					@Override
					   public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub					
					sendM(ev.getText().toString(),1, lug[arg2].getIp());
					}
				};
				 ev=new EditText(LocalWork.this);
		      		new AlertDialog.Builder(LocalWork.this)  
		    		.setTitle("������Ϣ��")  
		    		//.setIcon(android.R.drawable.ic_dialog_info)  
		    		.setView(ev) 
		    		.setPositiveButton("ȷ��", click)  
		    	    .setNegativeButton("ȡ��", null)  
		    		.show();
	          }
	});
	}
	/*
	 * �������̵߳�����ʾ
	 */
	Thread create=new Thread(new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			CustomToast.showToast(LocalWork.this,"��������ʧ�ܣ���������ȷ������������³������ԣ�",2000);
			if(!setIp)
			reSetIp();
		}	
	});
	/*
	 * �������̵߳�����ʾ2
	 */
	Thread create2=new Thread(new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub		
			CustomToast.showToast(LocalWork.this,"��������Ӧ��ʱ��",2000);
		}	
	});
	/*
	 * ������Ҫǩ������¼�
	 */
	public void onClick(View v){
		if(!showb)
		{
			showb=true;
			check.setVisibility(View.VISIBLE);
			zc.setVisibility(View.VISIBLE);
			ms.setVisibility(View.VISIBLE);
			ch.setVisibility(View.VISIBLE);
			menu.setVisibility(View.INVISIBLE);
		}
	}
	/*
	 * ������ʾ�б����¼�
	 */
	public void showListOut(View v){
		a.show();
	}
	/*
	 * ����ֹͣ������Ϣ����¼�
	 */
	public void stoplisten(View v){
		if(isServeing){
	    	//����¼�����
	    	OnClickListener click=new OnClickListener(){                                           //����������ȷ����ť
				@Override
				   public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					isServeing=false;
					CustomToast.showToast(LocalWork.this,"�ѹر���Ϣ����",2000);
					zhuangtai.setText("״̬\n\n   �����С�����");
				}
			};
      		new AlertDialog.Builder(this)  
    		.setTitle("ע��")  
    		.setIcon(android.R.drawable.ic_dialog_info)  
    		.setMessage("���ڽ�����Ϣ�У�"+"\nȷ����ֹ��") 
    		.setPositiveButton("ȷ��", click)  
    	    .setNegativeButton("ȡ��", null)  
    		.show();
			return;
		}
	}
	/*
	 * ��ʼ������Ϣ����
	 */
	public void startRecive(){
		if(isServeing)
			return;
		if(server==null)
		{
			isServeing=false;
			CustomToast.showToast(LocalWork.this,"��������˿ڣ�",2000);
			server=new SocketServer(3000);
		    return;
		}
		isServeing=true;		
		zhuangtai.setText("״̬\n\n   ������Ϣ������");
		new Thread(new Runnable(){
             @Override
			public  void  run(){
           	  while(isServeing){
           	   	  try {      	 	
  					server.StartListen();
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				} 
           		  if(server.s!=null){
           		  Message message    =  new Message();    //�½���Ϣ��
                     Bundle bundle  = new Bundle();
                     bundle.putString("text",server.msg);
                     message.setData(bundle);                  //������Ϣ�����������
                     my_hanler.sendMessage(message);                   //������Ϣ�����ڣգɸ���
           		  }
           	  }
             }}).start();
	}
	/*
	 * ���ÿ�ʼ������Ϣ����¼�
	 */
	public void startClick(View v){
		if(isServeing){
			CustomToast.showToast(LocalWork.this,"���ڽ�����Ϣ",2000);
			//zhuangtai.setText("״̬\n\n   �����С�����");
			return;
		}
		
		if(server==null)
		{
			isServeing=false;
			CustomToast.showToast(LocalWork.this,"��������˿ڣ�",2000);
			server=new SocketServer(3000);
		    return;
		}
		isServeing=true;
		CustomToast.showToast(LocalWork.this,"��ʼ������Ϣ",2000);
		zhuangtai.setText("״̬\n\n   ������Ϣ������");
		new Thread(new Runnable(){
             @Override
			public  void  run(){
           	  while(isServeing){
           	   	  try {      	 	
  					server.StartListen();
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				} 
           		  if(server.s!=null){
           		  Message message    =  new Message();    //�½���Ϣ��
                     Bundle bundle  = new Bundle();
                     bundle.putString("text",server.msg);
                     message.setData(bundle);                  //������Ϣ�����������
                     my_hanler.sendMessage(message);                   //������Ϣ�����ڣգɸ���
           		  }
           	  }
             }}).start();
 a.show();
	}
	/*
	 * ��ȡmac��ַ
	 */
	public String getmacaddress() {
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo =wifiManager.getConnectionInfo();
		return wifiinfo.getMacAddress();
	}
	/*
	 * ��ȡ����IP
	 */
	public String getIp(){
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo =wifiManager.getConnectionInfo();
		return String.valueOf(Formatter.formatIpAddress(wifiinfo.getIpAddress()));
	}
	/*
	 * ����json����
	 */
	public boolean addsave(JSONArray j,JSONObject newj){
		 j.put(newj);
		 SharedPreferences sp=getSharedPreferences("mrs",MODE_PRIVATE);
		 Editor editor=sp.edit();
		 editor.putString("JSON", j.toString());
		 editor.commit();
		return true;
	}
	/**
	 *���ǩ���б�
	 */
	public void clearStore(){
		 SharedPreferences sp=getSharedPreferences("mrs",MODE_PRIVATE);
		 Editor editor=sp.edit();
		 editor.clear();
		 editor.commit();
	}
	/*
	 * ��ȡJSON����
	 */
	public JSONArray readSaveData(){
		JSONArray j=null;
		SharedPreferences sp=getSharedPreferences("mrs",MODE_PRIVATE);
		try {
			if(sp.getString("JSON", null)==null)return null;
			j=new JSONArray(sp.getString("JSON", null).toString());
			System.out.println("���ص�j"+j.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("����jaʧ�ܣ�");
			e.printStackTrace();
			return null;
		}
		return j;
	}
	/*
	 * �õ��洢����ӦMAC��Ӧ���û�����
	 */
	public String getName(String mac,JSONArray j){
		if(j==null)
			return null;
		int i=0;String name=null;
		try {
			System.out.println("������֤mac"+mac);
			String jmac;
			while(true)
			{   
				if(i>=j.length())break;
				jmac=j.getJSONObject(i).getString("mac").toString();
				if(jmac.equals(mac))	name= j.getJSONObject(i).getString("msg");
			    i++;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}
	/*
	 * ��������Ŀ��IP������ΪĬ�����ӵ�WIFI�����豸IP��
	 */
	public void reSetIp(){
		IP="";
		String lsIP=getIp();
		for(int i=0,j=0;j<lsIP.length();j++){
			if(lsIP.charAt(j)=='.') i++;
				IP+=lsIP.charAt(j);
	        if(i==3) break;
		}
		IP+="1";
		System.out.println("��÷�����IP��"+IP);
	}
	/*
	 * ����WIFI
	 */
	public void connectWifi(){
		WifiAdmin mWifiAdmin=new WifiAdmin(this);
		mWifiAdmin.openWifi();
	}
	/*
	 * ���÷��ذ���(non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	 @Override
	    public void onBackPressed() {
		    	//����¼�����
		    	OnClickListener click=new OnClickListener(){                                           //����������ȷ����ť
					@Override
					   public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						isServeing=false;	
						   try {
							   if(server.s!=null)
							server.s.close();
							   if(server.ss!=null)
							server.ss.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 back();
					}
				};
	      		new AlertDialog.Builder(this)  
	    		.setTitle("ע��")  
	    		.setIcon(android.R.drawable.ic_dialog_info)  
	    		.setMessage("���ڽ�����Ϣ�У�"+"\nȷ���˳���") 
	    		.setPositiveButton("ȷ��", click)  
	    	    .setNegativeButton("ȡ��", null)  
	    		.show();
	    }
	 private void back(){
		 super.onBackPressed();
	 }
	 /*
	  * ������Ϣ�ĵ���
	  */
	    public void setTanchuang(final int type)                                                 //������������
	    {
	    	String sign=null;
	    	switch(type)
	    	{
	    		case 0:
	    			sign="��IP��";
	    			break;
	    		case 2:
	    		    sendM("check",type,IP);	
	    		    return;
	    		case 3:
	    			 sign="������";
		    		 break;
	    		case 1:
	    			 sign="��Ϣ��";
		    		 break;
	    	}
	    	//����¼�����
	    	OnClickListener click=new OnClickListener(){                                           //����������ȷ����ť
				@Override
				   public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(type==0){//�޸�Ŀ��IP
					String yip=IP;
					IP=ev.getText().toString();
					CustomToast.showToast(LocalWork.this,"Ŀ��IP�Ѹ�Ϊ��"+IP+"\nԭIPΪ"+yip,2000);
					 textshow.setText(textshow.getText()+"\nĿ��IP�Ѹ�Ϊ��"+IP+"\nԭIPΪ"+yip);
			            createC.post(new Runnable() {  
		            	    @Override  
		            	   public void run() {  
		            	        scrollView.fullScroll(View.FOCUS_DOWN);  
		            	    }  
		            	});  
					setIp=true;
					}
					else//ǩ����ע���뷢����Ϣ
					sendM(ev.getText().toString(),type,IP);	
				}
			};
	    	 ev=new EditText(this);
	    	//
      		new AlertDialog.Builder(this)  
    		.setTitle(sign)  
    		.setIcon(android.R.drawable.ic_dialog_info)  
    		.setView(ev) 
    		.setPositiveButton("ȷ��", click)  
    	    .setNegativeButton("ȡ��", null)  
    		.show();
    		if(showb){
    			check.setVisibility(View.INVISIBLE);
    			zc.setVisibility(View.INVISIBLE);
    			ms.setVisibility(View.INVISIBLE);
    			ch.setVisibility(View.INVISIBLE);
    			showb=false;
    			menu.setVisibility(View.VISIBLE);
    			}
	    }
	    /*
	     * ������Ϣ�ķ���
	     */
	    private void sendM(final String msg,final int type,final String ip){
			  new Thread(new Runnable(){
	              @Override
				public  void  run(){
	            	  SocketClient client;
	            	  client= new SocketClient(ip,3000);
	            	  if(client.client!=null){
	            	  String js=new JsonMaker(getIp(),getmacaddress(),msg,type).getJString();
	                  System.out.println("���ɵ�json�ַ���Ϊ:"+js);  
	            	  if(client.sendMsg(js))//editview.getText().toString()
	            	  { 
	                      createC.post(new Runnable() {  
	                  	    @Override  
	                  	   public void run() {  
	  	            		  System.out.println("���ͳɹ���");
                              if(type==1)
		            		  textshow.setText(textshow.getText()+"\n��˵:\n"+msg); 
		            		  else
		            		  textshow.setText(textshow.getText()+"\n���ͳɹ���"); 
	                  	        scrollView.fullScroll(View.FOCUS_DOWN);  
	                  	    }  
	                  	});  
	            	  }
	            	  else
	            		  {System.out.println("������δ��Ӧ��"); createC.post(create2);}
	            	  }
	            	  else 
	            	  { System.out.println("��������ʧ�ܣ���������ȷ������������³������ԣ�");createC.post(create);}
	            	  }
	              }).start();
	    }
	    /*
	     * �ĸ����ܰ�ť�ĵ���¼�
	     */
		public android.view.View.OnClickListener BLitener=new android.view.View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final ImageButton btn=(ImageButton)v;
				switch(btn.getId()){
				case R.id.ImageButton01:
					setTanchuang(0);
					break;
				case R.id.ImageButton02:
					startRecive();
					setTanchuang(2);
				    break;
				case R.id.ImageButton03:
					setTanchuang(3);
					break;
				case R.id.imageButton5:
					setTanchuang(1);
					break;
				}		
			}
		};
		/*
		 * �����ش洢���û���Ϣд������
		 */
		private void writeLug(){
			
			  JSONArray ja=readSaveData();
			  if(ja==null)
				  {luNum=0;return;}
		      for(int i=luNum;i<ja.length();i++){
			        try {
			        	String n=ja.getJSONObject(i).getString("msg");
			        	String m=ja.getJSONObject(i).getString("mac");
			        	lug[i]=new local_user(n, m, null);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}}
		      luNum=ja.length();
		}
		/*
		 * �������еõ���ӦMAC��Ӧ�û�����
		 */
		private int getLu(String mac){
		 for(int i=0;i<luNum;i++){
			 if(lug[i].getMac().equals(mac))
				 return i;
		 }
			return -1;
		}
		/*
		 * ��LU�����ȡˢ��LIST������
		 */
	    private List< Map<String,Object>> getData(){
	    	  List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();  
	          Map<String, Object> map;  
      for(int i=0;i<luNum;i++){
    	  map = new HashMap<String, Object>();
    	  map.put("text",lug[i].getName());
	        if(lug[i].isChecked())
	        	  map.put("image",R.drawable.ischeck);
			else
				  map.put("image",null);
	        data.add(map); 
      }
	        return data;
	    }
	    /*
	     * ˢ��LIST�ķ���
	     */
	    private void refreshList(){
	    	  System.out.println("��������"+luNum);
			SimpleAdapter adapter = new SimpleAdapter(LocalWork.this, getData(), R.layout.listconext, new String[] { "text",  "image" }, new int[] { R.id.text, R.id.image});  
			lt.setAdapter(adapter);
	    }
		 @Override
			public boolean onCreateOptionsMenu(Menu menu) {                                        //���ò˵�
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.local_work, menu);
				return true;
			}
			@Override                                                                              //���ò˵����Ч��
			public boolean onOptionsItemSelected(MenuItem item) {
			    switch(item.getItemId()) {
			    case R.id.action_settings:
			    	//����¼�����
			    	OnClickListener click=new OnClickListener(){                                           //����������ȷ����ť
						@Override
						   public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
					    	System.out.println("~~");	    	
					    	clearStore();
					    	 writeLug();
					    	 refreshList();
					    	 CustomToast.showToast(LocalWork.this,"�洢����ɹ���",3000,0);
						}
					};
		      		new AlertDialog.Builder(this)  
		    		.setTitle("ȷ����")  
		    		.setIcon(android.R.drawable.ic_dialog_info)  
		    		.setMessage("���������ע����û���Ϣ") 
		    		.setPositiveButton("ȷ��", click)  
		    	    .setNegativeButton("ȡ��", null)  
		    		.show();
			    	break;
			    }
			    return true;
			}
}

