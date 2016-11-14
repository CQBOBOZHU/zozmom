package com.zozmom.model;

public class HongbaoModel extends BaseModel {
	
	String startTime;
	int usedPrice;//用户使用次红包的最低消费
	String title;
	String phone;
	String bonusId;// 红包ID
	int status;// 红包类型 0 可用 1已使用 2已过期
	int discountPrice;// 红包额度
	String endTime;
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public int getUsedPrice() {
		return usedPrice;
	}
	public void setUsedPrice(int usedPrice) {
		this.usedPrice = usedPrice;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBonusId() {
		return bonusId;
	}
	public void setBonusId(String bonusId) {
		this.bonusId = bonusId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(int discountPrice) {
		this.discountPrice = discountPrice;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
