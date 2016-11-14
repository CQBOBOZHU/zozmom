package com.zozmom.model;

public class ProductModel extends BaseModel {
	int id;
	int productId;
	int productIndex;
	int productPrice;// 商品总价格
	int  issueId;// 商品期号ID
	String productName;//商品名
	String productTitle;// 商品title
	String showImage;// 商品图片
	int allCount;// 商品总次数
	int buyCount;// 商品已经购买的次数
	int productHot;
	int productNew;
	int beginTime;
	int productArea;// 百元专区 十元专区
	int status;//判断商品是否下架
	
	
	
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//用在个人中心的几个字段
	String dreamNum;
	int luckyUserId;
	String luckyUserName;
	long joinTime;
	int allBuyCount;
	int totalCount;
	String showImg;
	
	
	


	public String getShowImg() {
		return showImg;
	}

	public void setShowImg(String showImg) {
		this.showImg = showImg;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getLuckyUserId() {
		return luckyUserId;
	}

	public void setLuckyUserId(int luckyUserId) {
		this.luckyUserId = luckyUserId;
	}

	public String getLuckyUserName() {
		return luckyUserName;
	}

	public void setLuckyUserName(String luckyUserName) {
		this.luckyUserName = luckyUserName;
	}

	public long getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(long joinTime) {
		this.joinTime = joinTime;
	}

	public int getAllBuyCount() {
		return allBuyCount;
	}

	public void setAllBuyCount(int allBuyCount) {
		this.allBuyCount = allBuyCount;
	}

	public String getDreamNum() {
		return dreamNum;
	}

	public void setDreamNum(String dreamNum) {
		this.dreamNum = dreamNum;
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

	public int getIssueId() {
		return issueId;
	}

	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductHot() {
		return productHot;
	}

	public void setProductHot(int productHot) {
		this.productHot = productHot;
	}

	public int getProductNew() {
		return productNew;
	}

	public void setProductNew(int productNew) {
		this.productNew = productNew;
	}

	public int getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getShowImage() {
		return showImage;
	}

	public void setShowImage(String showImage) {
		this.showImage = showImage;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public int getAllCount() {
		return allCount;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

}
