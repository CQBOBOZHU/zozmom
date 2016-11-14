package com.zozmom.db;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.ex.DbException;

import android.content.Context;

import com.zozmom.Myapplication;
import com.zozmom.constants.Constant;
import com.zozmom.model.UserInfoModel;

public class UserDao extends DatabaseDao {
	UserInfoModel model;
	Context context;

	public UserInfoModel getModel() {
		if (model == null) {
			getDbUser();
		}
		return model;
	}

	public void setModel(UserInfoModel model) {
		this.model = model;

	}

	public UserInfoModel getDbUser() {
		return findFirst(UserInfoModel.class);
	}

	public UserInfoModel findUserByUserId(String userid) {
		return (UserInfoModel) find(UserInfoModel.class, "userId", "");
	}
}
