package com.zozmom.model;

public class ComeTrueModel extends BaseModel {
	private int id;
	private int status;
	private long announcedTime;
	private int userId;
	private int userBuyCount;
	private String productTitle;
	private String productImg;
	private int issueId;
	private String luckyNumber;
	private String productName;
	private int productId;
	private int allCount;
	private int productArea;
	
	
	
	public int getProductArea() {
		return productArea;
	}
	public void setProductArea(int productArea) {
		this.productArea = productArea;
	}
	public int getAllCount() {
		return allCount;
	}
	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getAnnouncedTime() {
		return announcedTime;
	}
	public void setAnnouncedTime(long announcedTime) {
		this.announcedTime = announcedTime;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUserBuyCount() {
		return userBuyCount;
	}
	public void setUserBuyCount(int userBuyCount) {
		this.userBuyCount = userBuyCount;
	}
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public String getProductImg() {
		return productImg;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}
	public int getIssueId() {
		return issueId;
	}
	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}
	public String getLuckyNumber() {
		return luckyNumber;
	}
	public void setLuckyNumber(String luckyNumber) {
		this.luckyNumber = luckyNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	
	
}
