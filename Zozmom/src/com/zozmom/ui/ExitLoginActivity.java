//package com.zozmom.ui;
//
//import com.chasedream.zhumeng.R;
//import com.zozmom.ui.user.SetupActivity;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//
//public class ExitLoginActivity extends Activity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.exit_activity);
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		finish();
//		overridePendingTransition(android.R.anim.fade_in,
//				android.R.anim.fade_out);
//		return true;
//	}
//
//	public void exitbutton1(View v) {
//		this.finish();
//		overridePendingTransition(android.R.anim.fade_in,
//				android.R.anim.fade_out);
//	}
//
//	public void exitbutton0(View v) {
//		this.finish();
//		overridePendingTransition(android.R.anim.fade_in,
//				android.R.anim.fade_out);
//		SetupActivity.instance.lout();//
//	}
//
//}
