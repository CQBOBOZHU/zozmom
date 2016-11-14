package com.zozmom.ui.user;

import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.view.View;

import com.chasedream.zhumeng.R;
import com.zozmom.model.UserInfoModel;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.LoginActivity;

public class UserManagerBaseActivity<T> extends BaseTaskActivity<T> {
	private int requestCode = 7;
	UserInfoModel infoModel;
	@ViewInject(R.id.notloginview)
	View notloginView;

	public void toDoSomeThing() {
		infoModel = getLogginUserinfo();
		if (null != infoModel) {
			initView();
		} else
			notloginView.setVisibility(View.VISIBLE);
	}

	public void initView() {
		infoModel = getLogginUserinfo();
	}

	public void login(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == requestCode && arg1 == LoginActivity.resultCode) {
			notloginView.setVisibility(View.GONE);
			initView();
		}
	}
}
