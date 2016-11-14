package com.zozmom.manager;

import com.zozmom.db.UserDao;
import com.zozmom.model.UserInfoModel;

public class AccountManager {

	private static AccountManager m_instance;
	private UserInfoModel m_curUser;

	public UserInfoModel getCurrentUserInfo() {
		if (m_curUser == null) {
			m_curUser = UserDao.findFirst(UserInfoModel.class);
		}
		return m_curUser;
	}

	public void setLogginUserInfo(UserInfoModel model) {
		m_curUser = model;
		UserDao.saveOrUpdate(m_curUser);
	}

	public void updatUser(UserInfoModel model) {
		m_curUser = model;
		UserDao.saveOrUpdate(m_curUser);
	}

	public UserInfoModel getLogginUserInfo() {
		if (m_curUser == null) {
			return getCurrentUserInfo();
		}
		return m_curUser;
	}

	public void clearUserInfo() {
		m_curUser = null;
	}

	public static AccountManager getInstance() {
		if (m_instance == null) {
			m_instance = new AccountManager();
		}
		return m_instance;
	}

	/**
	 * 注销
	 */
	public void logout() {
		if (m_curUser != null) {
			UserDao.delete(m_curUser);
		}
		m_curUser = null;
	}

	public void reset() {
		m_curUser = null;
	}

	public void setlocalUser() {
		if (m_curUser == null) {
			if (m_curUser == null) {
				// m_curUser = UserDao.findFirst(UserModel.class);
			}
		}
	}

}
