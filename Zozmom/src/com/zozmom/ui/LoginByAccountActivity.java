

package com.zozmom.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import com.chasedream.zhumeng.R;
import com.umeng.analytics.MobclickAgent;
import com.zozmom.constants.Constant;
import com.zozmom.manager.AccountManager;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.fragment.MeFragment;
import com.zozmom.util.DeviceInfo;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.MD5Utils;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.UpgradeUtil;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
/**
 * 用账户密码登录
 * @author Administrator
 *
 */
@ContentView(R.layout.login_activity)
public class LoginByAccountActivity extends BaseTaskActivity<Object>{
	@ViewInject(R.id.editName)
	EditText username;
	@ViewInject(R.id.editPwd)
	EditText password;
	Intent intent;
	public final static int resultCode=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		String phone=getIntent().getStringExtra("phone");
		if(phone!=null){
			username.setText(phone);
		}
	}
	
	public void exit(View view){
		finish();
	}
	/**
	 * 忘掉密码
	 * @param view
	 */
	public void forgetpwd(View view){
		intent = new Intent(this, LoginActivity.class);
		intent.putExtra("code", 3);
		String phone=username.getText().toString();
		intent.putExtra("phone", phone);
		startActivityForResult(intent, 3);
	}
	String phone;
	/**
	 * 账户密码登录
	 * @param view
	 */
	public void login(View view) {
		String name = username.getText().toString();
		String pwd = password.getText().toString();
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, getRString(R.string.network_hint));
			return;
		}
		if (name == null || name.trim().length() == 0) {
			ToastUtil.show(this, "账户不能为空");
			return;
		}
		if (pwd == null || name.trim().length() == 0) {
			ToastUtil.show(this, "密码不能为空"); 
			return;
		}
		pwd = MD5Utils.epPwd(pwd);
		Map<String, Object> map = new HashMap<String, Object>();
		phone=name;
		String equipmentId=UpgradeUtil.getChannelId(this);
		Log.v(TAG, equipmentId);
		map.put("equipmentId", equipmentId);
		map.put("phone", name);
		map.put("password", pwd);
		Log.v("this", "pwd:" + pwd);
		XHttp.Post(Constant.LOGIN+"pwd", map, new CommonCallback<JSONObject>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				RequestErrorUtil.judge(arg0, LoginByAccountActivity.this);
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(JSONObject arg0) {
				Log.v("this", "JSONArray " + arg0.toString());
				try {
					UserInfoModel model = (UserInfoModel) JsonUtil.JsonToModel(arg0,
							UserInfoModel.class);
					AccountManager.getInstance().setLogginUserInfo(model);
//					TalkingDataAppCpa.onLogin(phone);
					MobclickAgent.onProfileSignIn(phone);
					if(MeFragment.meFragment!=null){
						MeFragment.meFragment.changgeView(model, true);
					}
					setResult(resultCode);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		super.onActivityResult(arg0, arg1, data);
		if (arg1 == 1 || arg1 == 3) {
			String phone = data.getStringExtra("phone");
			if (username != null) {
				username.setText(phone);
			}
		} else if (arg1 == 2) {
			setResult(resultCode);
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
