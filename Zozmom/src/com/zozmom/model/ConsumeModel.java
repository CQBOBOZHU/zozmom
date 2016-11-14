package com.zozmom.model;

public class ConsumeModel extends BaseModel {

	long payTime;// 时间
	int dreamCoin;// 逐梦币
	long createTime;
	int id;
	int userId;
	int amount;
	String purposeDesc;// 渠道描述
	String attrOne;
	int purpose;
	int source;
	
	
	public int getPurpose() {
		return purpose;
	}
	public void setPurpose(int purpose) {
		this.purpose = purpose;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getPayTime() {
		return payTime;
	}
	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}
	public int getDreamCoin() {
		return dreamCoin;
	}
	public void setDreamCoin(int dreamCoin) {
		this.dreamCoin = dreamCoin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getPurposeDesc() {
		return purposeDesc;
	}
	public void setPurposeDesc(String purposeDesc) {
		this.purposeDesc = purposeDesc;
	}
	public String getAttrOne() {
		return attrOne;
	}
	public void setAttrOne(String attrOne) {
		this.attrOne = attrOne;
	}

}
