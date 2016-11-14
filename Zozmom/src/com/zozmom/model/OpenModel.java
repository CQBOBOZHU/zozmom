package com.zozmom.model;

/**
 * 揭晓的商品
 * 
 * @author Administrator
 * 
 */
public class OpenModel extends BaseModel {
	private static final long serialVersionUID = 1L;
	private long announcedTime;// 揭晓时间
	private int userBuyCount;// 购买的次数
	private int issueId;// 商品期号
	private int productId;// 商品id
	private int id;
	private String productImg;// 商品图片
	private String productTitle;// 商品title
	private String userName;// 获奖者名字
	private String luckyNumber;// 获奖幸运号
	private String productName;// 商品名
	private int productArea;
	
	

	public int getProductArea() {
		return productArea;
	}

	public void setProductArea(int productArea) {
		this.productArea = productArea;
	}

	public long getAnnouncedTime() {
		return announcedTime;
	}

	public void setAnnouncedTime(long announcedTime) {
		this.announcedTime = announcedTime;
	}

	public int getUserBuyCount() {
		return userBuyCount;
	}

	public void setUserBuyCount(int userBuyCount) {
		this.userBuyCount = userBuyCount;
	}

	public int getIssueId() {
		return issueId;
	}

	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

}
