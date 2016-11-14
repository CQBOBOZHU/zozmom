package com.zozmom.model;

public class ActivityModel extends BaseModel{
//	[{"id":2,"activityId":"0","icon":"yCaJbGsJXAbdAd.png","masterTitle":"1","salveTitle":"2","sign":0,"status":0,"activityLine":"阿斯蒂芬"},
	int id;//id为0就不传token  id不为0就不传token
	String activityId;//活动id
	String icon;
	String masterTitle;//主标题
	String salveTitle;//副标题
	int sign;//为O就表示不显示hot图标  
	int status;
	String activityLine;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getMasterTitle() {
		return masterTitle;
	}
	public void setMasterTitle(String masterTitle) {
		this.masterTitle = masterTitle;
	}
	public String getSalveTitle() {
		return salveTitle;
	}
	public void setSalveTitle(String salveTitle) {
		this.salveTitle = salveTitle;
	}
	public int getSign() {
		return sign;
	}
	public void setSign(int sign) {
		this.sign = sign;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getActivityLine() {
		return activityLine;
	}
	public void setActivityLine(String activityLine) {
		this.activityLine = activityLine;
	}
	
	

}
