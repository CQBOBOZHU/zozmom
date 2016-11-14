package com.zozmom.model;


/**
 * 正在揭晓的商品
 * 
 * @author Administrator
 * 
 */
public class CountdownGoodsModel extends BaseModel {
	String lotteryId;
	String lotteryProductId;
	String lotteryProductTitle;
	String lotteryProductPrice;
	String lotteryProductPeriod;
	String lotteryProductImg;
	String lotteryProductEndDate;

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getLotteryProductId() {
		return lotteryProductId;
	}

	public void setLotteryProductId(String lotteryProductId) {
		this.lotteryProductId = lotteryProductId;
	}

	public String getLotteryProductTitle() {
		return lotteryProductTitle;
	}

	public void setLotteryProductTitle(String lotteryProductTitle) {
		this.lotteryProductTitle = lotteryProductTitle;
	}

	public String getLotteryProductPrice() {
		return lotteryProductPrice;
	}

	public void setLotteryProductPrice(String lotteryProductPrice) {
		this.lotteryProductPrice = lotteryProductPrice;
	}

	public String getLotteryProductPeriod() {
		return lotteryProductPeriod;
	}

	public void setLotteryProductPeriod(String lotteryProductPeriod) {
		this.lotteryProductPeriod = lotteryProductPeriod;
	}

	public String getLotteryProductImg() {
		return lotteryProductImg;
	}

	public void setLotteryProductImg(String lotteryProductImg) {
		this.lotteryProductImg = lotteryProductImg;
	}

	public String getLotteryProductEndDate() {
		return lotteryProductEndDate;
	}

	public void setLotteryProductEndDate(String lotteryProductEndDate) {
		this.lotteryProductEndDate = lotteryProductEndDate;
	}
}
