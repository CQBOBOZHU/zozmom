package com.zozmom.model;

public class OrderProductModel extends ProductModel {

	int buytimes;// 想要购买的次数
	int randomCode;// 随机号
	public int getBuytimes() {
		return buytimes;
	}
	public void setBuytimes(int buytimes) {
		this.buytimes = buytimes;
	}
	public int getRandomCode() {
		return randomCode;
	}
	public void setRandomCode(int randomCode) {
		this.randomCode = randomCode;
	}

}
