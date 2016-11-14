package com.zozmom.model;

public class OpenedModel extends BaseModel{
	
	private static final long serialVersionUID = 1L;
	private String location;
	private int id;
	private int userId;
	private int productId;
	private int productIndex;
	private long announcedTime;
	private long buyTime;
	private String phone;
	private String productName;
	private int productPrice;
	private String productTitle;
	private String productImg;
	private String luckyNumber;
	private int userBuyCount;
	private String userFace;
	private String userName;
	private int productArea;
	private int status;
	
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getProductArea() {
		return productArea;
	}
	public void setProductArea(int productArea) {
		this.productArea = productArea;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
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
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductIndex() {
		return productIndex;
	}
	public void setProductIndex(int productIndex) {
		this.productIndex = productIndex;
	}
	public long getAnnouncedTime() {
		return announcedTime;
	}
	public void setAnnouncedTime(long announcedTime) {
		this.announcedTime = announcedTime;
	}
	public long getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(long buyTime) {
		this.buyTime = buyTime;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
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
	public String getLuckyNumber() {
		return luckyNumber;
	}
	public void setLuckyNumber(String luckyNumber) {
		this.luckyNumber = luckyNumber;
	}
	public int getUserBuyCount() {
		return userBuyCount;
	}
	public void setUserBuyCount(int userBuyCount) {
		this.userBuyCount = userBuyCount;
	}
	public String getUserFace() {
		return userFace;
	}
	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
