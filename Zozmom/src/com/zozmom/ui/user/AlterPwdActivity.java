package com.zozmom.ui.user;

import java.util.HashMap;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.manager.AccountManager;
import com.zozmom.model.UserInfoModel;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.LoginActivity;
import com.zozmom.util.MD5Utils;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 修改密码
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.alter_pwd_activity)
public class AlterPwdActivity extends BaseTaskActivity<Boolean> {
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	@ViewInject(R.id.before_pwd_edit)
	EditText oldpwdEdit;
	@ViewInject(R.id.alter_newpwd_edit)
	EditText newpwdEdit;
	@ViewInject(R.id.alter_oncepwd_edit)
	EditText againpwdEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		titleView.setText("修改密码");
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.status_bar_Exit:
			 finish();
			break;
		case R.id.alter_forgetpwd_btn:
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra("code", 3);
			startActivity(intent);
			finish();
			break;
		case R.id.alter_save_btn:
			AlterPwd();
			break;

		default:
			break;
		}
	}

	String oldpwd;
	String newpwd;
	String againpwd;

	public void init() {
		oldpwd = oldpwdEdit.getText().toString().trim();
		newpwd = newpwdEdit.getText().toString().trim();
		againpwd = againpwdEdit.getText().toString().trim();
	}

	UserInfoModel userModel;

	public void AlterPwd() {
		init();
		if (oldpwd == null || newpwd == null || againpwd == null) {
			ToastUtil.show(this, "密码不能为空");
			return;
		}
		if (!newpwd.equals(againpwd)) {
			ToastUtil.show(this, "两次密码不同");
			return;
		}
		if (!Util.isPwd(againpwd)) {
			ToastUtil.show(this, "密码格式不对");
			return;
		}
		userModel = AccountManager.getInstance().getCurrentUserInfo();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userModel.getUserId());
		Log.v("this", "userId:" + userModel.getUserId());
		map.put("oldPassword", MD5Utils.epPwd(oldpwd));
		Log.v("this",
				"oldpwd:" + oldpwd + "  oldPassword :" + MD5Utils.epPwd(oldpwd));
		map.put("newPassword", MD5Utils.epPwd(againpwd));
		Log.v("this",
				"againpwd :" + againpwd + "  newPassword :"
						+ MD5Utils.epPwd(againpwd));
		requstPostByToken(Constant.ALTER_PWD_BYPWD,userModel, map);
	}

	@Override
	public void onError(Throwable arg0, boolean arg1) {
		// RequestErrorUtil.judge(arg0, m_activity);
		showFailDialog();
	}

	@Override
	public void onSuccess(Boolean arg0) {
		super.onSuccess(arg0);
		if (arg0) {
			showSucessDialog();
		} else
			showFailDialog();
	}

	AlertDialog alertDialog;

	public void showSucessDialog() {
		AlertDialog.Builder builder = new Builder(this);
		alertDialog = builder.create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.alter_pwd_susucess);
		Button startCa = (Button) window.findViewById(R.id.alter_pwd_sucessbtn);
		startCa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlterPwdActivity.this.finish();
			}
		});
		alertDialog.setCanceledOnTouchOutside(true);
	}

	public void showFailDialog() {
		AlertDialog.Builder builder = new Builder(this);
		alertDialog = builder.create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.alter_pwd_faile);
		Button startCa = (Button) window.findViewById(R.id.alter_fail_btn);
		startCa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AlterPwdActivity.this,
						LoginActivity.class);
				intent.putExtra("code", 3);
				startActivity(intent);
				finish();
			}
		});
		alertDialog.setCanceledOnTouchOutside(true);
	}
}
