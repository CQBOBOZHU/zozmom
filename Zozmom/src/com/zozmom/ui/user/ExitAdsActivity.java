package com.zozmom.ui.user;

import com.chasedream.zhumeng.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ExitAdsActivity extends Activity {
	TextView titleview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_activity);
		int id = getIntent().getIntExtra("data", 0);
		titleview = (TextView) findViewById(R.id.exit_dialog_title);
		if (id == 1) {
			titleview.setText("确定删除该地址吗?");
		} else {
			titleview.setText("是否放弃本次编辑");
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		return true;
	}

	public void exitbutton1(View v) {
		this.finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	public void exitbutton0(View v) {
		this.finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		AddaddressActivity.instance.finish();//
	}

}
