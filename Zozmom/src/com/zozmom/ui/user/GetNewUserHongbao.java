package com.zozmom.ui.user;

import java.util.HashMap;
import java.util.Map;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Util;
/**
 * 获取新手红包
 * @author Administrator
 *
 */
public class GetNewUserHongbao extends BaseTaskActivity<String> {
	EditText editText;
	Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getnewuser_hongbao);
		TextView titleView=(TextView) findViewById(R.id.status_bar_title);
		titleView.setText("新手红包");
		btn=(Button) findViewById(R.id.newuser_gethongbao);
		editText=(EditText) findViewById(R.id.newuser_phone);
		btn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.newuser_gethongbao:
				String phone=editText.getText().toString();
				getHongbao(phone);
				break;
		}
	}
	
	/**
	 * 请求
	 * 
	 * @param phone
	 */
	public void getHongbao(String phone) {
		if (phone == null || phone.length() == 0) {
			ToastUtil.show(this, "手机不能为空");
			return;
		}
		if (!Util.isMobile(phone)) {
			ToastUtil.show(this, "请输入正确的手机号");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		map.put("activityId",2);
		map.put("equipmentId", "活动默认注册");
		XHttp.Post(Constant.GET_NEWUSER_HONGBAO, map, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.v("this", arg0.toString());
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(String arg0) {
				Log.v("this", arg0.toString()+"");
				if(arg0.equals("0")){
					ToastUtil.show(m_activity, "您已经领取过");
				}else{
					ToastUtil.show(m_activity, "领取成功");
				}
			}
		});
	}
	

}
