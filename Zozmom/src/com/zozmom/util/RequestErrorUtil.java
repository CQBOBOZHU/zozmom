package com.zozmom.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.HttpException;

import com.zozmom.manager.AccountManager;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.LoginActivity;
import com.zozmom.ui.fragment.MeFragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RequestErrorUtil {

	public static void judge(Throwable arg0, Context context) {
		if (arg0 instanceof HttpException) { // 网络错误
			HttpException arg = (HttpException) arg0;
			String result = arg.getResult();
			try {
				JSONObject object = new JSONObject(result);
				String msg = object.getString("msg");
				String code = object.getString("code");
				showError(msg, code, context);
				Log.v("zozmom", result);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("zozmom", result);
			}
		}
	}

	public static void showError(String msg, String code, Context context) {
		Intent intent;
		if (code.equals("USR.0001")) {
			ToastUtil.show(context, "手机已注册");
		} else if (code.equals("USR.0002")) {
			ToastUtil.show(context, "密码错误");
		} else if (code.equals("USR.0003")) {
			ToastUtil.show(context, "用户不存在");
		} else if (code.equals("USR.0004")) {
			ToastUtil.show(context, "用户已被冻结");
		} else if (code.equals("USR.0005")) {
			ToastUtil.show(context, "验证码错误");
		} else if (code.equals("USR.0006")) {
			ToastUtil.show(context, "手机格式不正确");
		} else if (code.equals("USR.0007")) {
			Log.e("requesterror", "设备ID为空");
		} else if (code.equals("USR.0008")) {
			Log.e("requesterror", "IP地址不正确");
		} else if (code.equals("USR.0009")) {

		} else if (code.equals("USR.0010")) {
			ToastUtil.show(context, "请求频繁,请稍后再试");
		} else if (code.equals("USR.0011")) {
			ToastUtil.show(context, "验证码失效");
		} else if (code.equals("USR.0012")) {
			ToastUtil.show(context, "密码错误");
		} else if (code.equals("PRD.0001")) {
			ToastUtil.show(context, "无更多商品");
		} else if (code.equals("PRD.0003")) {
			ToastUtil.show(context, "商品已下架");
		} else if (code.equals("ODR.0001")) {
			ToastUtil.show(context, "剩余次数不足");
		} else if (code.equals("ODR.0003")) {
			ToastUtil.show(context, "红包使用失败");
		} else if (code.equals("DFT.0006")) {

		} else if (code.equals("DFT.0001")) {

		} else if (code.equals("HOM.0001")) {
			ToastUtil.show(context, "当前为最新版本");
		} else if (code.equals("DFT.0003")) {
			ToastUtil.show(context, "账户在其他设备登录");
			intent = new Intent(context, LoginActivity.class);
			AccountManager.getInstance().logout();
			MeFragment.meFragment.isLogin(false);
			((BaseTaskActivity<?>) context).startActivityForResult(intent, 0);
			((BaseTaskActivity<?>) context).finish();
		} else if (code.equals("DFT.0005")) {
			ToastUtil.show(context, "账户在其他设备登录");
			intent = new Intent(context, LoginActivity.class);
			AccountManager.getInstance().logout();
			MeFragment.meFragment.isLogin(false);
			((BaseTaskActivity<?>) context).startActivityForResult(intent, 0);
			((BaseTaskActivity<?>) context).finish();
		} else if (code.equals("DFT.0007")) {
			ToastUtil.show(context, "账户在其他设备登录");
			intent = new Intent(context, LoginActivity.class);
			AccountManager.getInstance().logout();
			MeFragment.meFragment.isLogin(false);
			((BaseTaskActivity<?>) context).startActivityForResult(intent, 0);
			((BaseTaskActivity<?>) context).finish();
		} else if (code.equals("DFT.0009")) {
			ToastUtil.show(context, "暂停支付");
		} else {
			ToastUtil.show(context, msg);
		}

	}
}
