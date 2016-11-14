package com.zozmom.model;

public class PayModel extends BaseModel {

	// int orderNum ;
	// String channel ;//支付渠道
	// String clientIp ;//客服端IP
	int dreamCoin;// 用户余额
	int price;// 总支付价
	long orderId;// 支付订单
	

	public int getDreamCoin() {
		return dreamCoin;
	}

	public void setDreamCoin(int dreamCoin) {
		this.dreamCoin = dreamCoin;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}



}
