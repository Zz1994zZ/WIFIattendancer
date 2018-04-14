package com.example.qian_dao;

public class CheckInfo {
    private int activity_id;
    private int sid;
    private int depart_id;
    private String date;
    public CheckInfo(int ai,int si,int di,String date){
    	activity_id=ai;
    	sid=si;
    	depart_id=di;
    	this.date=date;
    }
    public void showMe(){
    	System.out.println(activity_id+"\n"+sid+"\n"+depart_id+"\n"+date);
    }
    public int getActivity_id(){
    	return activity_id;
    }
    public int getSid(){
    	return sid;
    }
    public int getDepart_id(){
    	return depart_id;
    }
    public String getDate(){
    	return date;
    }
}
