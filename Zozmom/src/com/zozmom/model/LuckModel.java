package com.zozmom.model;

/**
 * 商品详情 的参与记录
 * @author Administrator
 * 
 */
public class LuckModel extends BaseModel{
	int id;
	String userName;
	long joinTime;
	int buyCount;
	String dreamNum;
	int userId;
	int randomNum;
	String headUrl;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public int getRandomNum() {
		return randomNum;
	}

	public void setRandomNum(int randomNum) {
		this.randomNum = randomNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(long joinTime) {
		this.joinTime = joinTime;
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	public String getDreamNum() {
		return dreamNum;
	}

	public void setDreamNum(String dreamNum) {
		this.dreamNum = dreamNum;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
