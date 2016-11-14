//package com.zozmom.ui.pay;
//
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.chasedream.zhumeng.R;
//import com.zozmom.ui.BaseTaskActivity;
//
///**
// * 放弃支付页面
// * 
// * @author Administrator
// * 
// */
//public class GiveupActivity extends BaseTaskActivity<String> {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.give_up_activity);
//	}
//
//	public void giveup(View view) {
//		PayActivity.instance.finish();
//		overridePendingTransition(android.R.anim.fade_in,
//				android.R.anim.fade_out);
//		finish();
//		overridePendingTransition(android.R.anim.fade_in,
//				android.R.anim.fade_out);
//	}
//
//	public void notgiveup(View view) {
//		finish();
//		overridePendingTransition(android.R.anim.fade_in,
//				android.R.anim.fade_out);
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		finish();
//		overridePendingTransition(android.R.anim.fade_in,
//				android.R.anim.fade_out);
//		return true;
//	}
//}
