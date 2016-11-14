package com.zozmom.ui.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.ConsultingContent;
import com.sobot.chat.api.model.Information;
import com.sobot.chat.listener.MessageListener;
import com.sobot.chat.utils.ResourceUtils;
import com.sobot.chat.utils.ZhiChiConstant;
import com.zozmom.manager.AccountManager;
import com.zozmom.model.UserInfoModel;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.DemoActivity;
import com.zozmom.ui.BaseWebActivity.SampleWebViewClient;
import com.zozmom.util.DeviceInfo;

/**
 * 客服中心
 * 
 * @author Administrator
 * 
 */
//@ContentView(R.layout.scenter_activity)
public class ScenterBySdkActivity  extends BaseTaskActivity<JSONObject> {
	private String appkey = "3966113cba264761a8b688c64eed9a60";
	UserInfoModel infoModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		infoModel = AccountManager.getInstance().getLogginUserInfo();
		Information info = new Information();
		info.setAppKey(appkey);/* 必填 */
		info.setColor("#FF7E00");
		info.setShowSatisfaction(true);
		info.setUseVoice(false);
		if (infoModel != null) {
			info.setUid(infoModel.getUserId() + "");/* 必填，设置用户唯一标识 */
			info.setUname(infoModel.getUserName() + "");/* 用户昵称，选填 */
			info.setPhone(infoModel.getPhone() + "");/* 用户电话，选填 */
		} else {
			String uid = DeviceInfo.getMacAddress();
			if (uid != null) {
				info.setUid(uid);
			} else {
				uid = System.currentTimeMillis() + "";
				info.setUid(uid);
			}
		}
		info.setBackOrClose(true);// 设置 是否会话保持。默认true，进行会话保持显示返回。false，不进行会话保持显示关闭
		SobotApi.startSobotChat(ScenterBySdkActivity.this, info);
		finish();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
