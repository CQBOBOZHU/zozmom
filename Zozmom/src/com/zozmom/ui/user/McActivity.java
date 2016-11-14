package com.zozmom.ui.user;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.chasedream.zhumeng.R;
import com.zozmom.ui.BaseTaskActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 消息中心
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.mc_activity)
public class McActivity extends BaseTaskActivity<Object> {
	@ViewInject(R.id.status_bar_title)
	TextView titleview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		titleview.setText("消息中心");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.status_bar_Exit:
			finish();
			break;
		case R.id.huodong_lay:
			intent = new Intent(this, MessageCenterActivity.class);
			startActivity(intent);
			break;
		case R.id.zhongjiang_lay:
			intent = new Intent(this, UserHongbaoActivity.class);
			startActivity(intent);
			break;
		case R.id.wuliu_lay:
			break;
		case R.id.sys_lay:
			intent = new Intent(this, MessageSysActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
