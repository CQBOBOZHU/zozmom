package com.zozmom.ui;

import org.xutils.x;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.constants.Constants;
import com.zozmom.net.XHttp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 商品分类
 * 
 * @author Administrator
 * 
 */
public class GdClsActivity extends BaseTaskActivity<Object> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gdcls_activity);
		TextView textView = (TextView) findViewById(R.id.status_bar_title);
		textView.setText(getRString(R.string.good_facation));
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.gd_laytwo :
				tak(R.string.good_two, Constants.TID_ONE);
				break;
			case R.id.gd_laythree :
				tak(R.string.good_three, Constants.TID_EIGHT);
				break;
			case R.id.gd_layfour :
				tak(R.string.good_four, Constants.TID_NINE);
				break;
			case R.id.gd_layfive :
				tak(R.string.good_five, Constants.TID_TWO);
				break;
			case R.id.gd_laysix :
				tak(R.string.good_six, Constants.TID_THREE);
				break;
			case R.id.gd_layseven :
				tak(R.string.good_seven, Constants.TID_FOUR);
				break;
			case R.id.gd_layeight :
				tak(R.string.good_eight, Constants.TID_FIVE);
				break;
			case R.id.gd_laynine :
				tak(R.string.good_nine, Constants.TID_SEVEN);
				break;
			case R.id.gd_layten :
				tak(R.string.good_ten, Constants.TID_SIX);
				break;
			default :
				break;
		}
	}
	public void tak(int id, int type) {
		Intent intent = new Intent(this, CmmCftionActivity.class);
		intent.putExtra("type", getRString(id));
		intent.putExtra("producttype", type);
		startActivity(intent);
	}
	/**
	 * 十元区
	 * @param view
	 */
	public void tenDis(View view){
		Intent intent = new Intent(this, CmmDisActivity.class);
		intent.putExtra("type", "十元专区");
		intent.putExtra("producttype", 10);
		startActivity(intent);
	}
	
	/**
	 * 百元区
	 * @param view
	 */
	public void hundredDis(View view){
		Intent intent = new Intent(this, CmmDisActivity.class);
		intent.putExtra("type","百元专区");
		intent.putExtra("producttype", 100);
		startActivity(intent);
	}
}
