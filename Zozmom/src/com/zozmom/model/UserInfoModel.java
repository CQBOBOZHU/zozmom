package com.zozmom.model;

import java.util.Observer;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import android.database.Observable;

@Table(name = "zozmom_usermodel")
public class UserInfoModel extends BaseModel {
	@Column(name = "userName")
	String userName;// 昵称
	@Column(name = "password")
	String password;// 密码
	@Column(name = "headUrl")
	String headUrl;// 头像url
	@Column(name = "phone")
	String phone;// 手机号
	@Column(name = "userId", isId = true, autoGen = false)
	int userId;// 用户id
	@Column(name = "authKey")
	String authKey;// 微信认证key
	@Column(name = "inviteCode")
	String inviteCode;// 邀请码
	@Column(name = "createTime")
	String createTime;// token创建时间.
	@Column(name = "token")
	String token;
	int balance;// 逐梦币
	int commission;// 佣金
	@Column(name="inviteId")
	int inviteId;
	
	public int getInviteId() {
		return inviteId;
	}

	public void setInviteId(int inviteId) {
		this.inviteId = inviteId;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getCommission() {
		return commission;
	}

	public void setCommission(int commission) {
		this.commission = commission;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
