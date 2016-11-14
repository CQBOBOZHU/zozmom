package com.zozmom.model;

import java.util.List;
/**
 * 晒单
 * @author Administrator
 *
 */
public class ShowModel extends BaseModel {
	
	int id;
	long createTime;
	int status;
	String productTitle;
	int userId;
	String userName;
	String issueId;
	String headUrl;
	String shareImg;//分享的图片地址
	String comment;//内容
	String productName;
	String productId;
	int buyCount; 
	String luckyNumber;
	long announcedTime;
	int productStatus;
	
	
	
	public int getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(int productStatus) {
		this.productStatus = productStatus;
	}
	public long getAnnouncedTime() {
		return announcedTime;
	}
	public void setAnnouncedTime(long announcedTime) {
		this.announcedTime = announcedTime;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public String getLuckyNumber() {
		return luckyNumber;
	}
	public void setLuckyNumber(String luckyNumber) {
		this.luckyNumber = luckyNumber;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	public String getShareImg() {
		return shareImg;
	}
	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
}
