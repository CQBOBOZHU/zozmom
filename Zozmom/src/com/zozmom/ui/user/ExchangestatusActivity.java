package com.zozmom.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.ui.BaseTaskActivity;
/**
 * 兑换成功与失败
 * 
 * @author Administrator
 * 
 */
public class ExchangestatusActivity extends BaseTaskActivity<String> {
	TextView titleView;
	int type;
	String result;
	int coin;
	ImageView imageView;
	TextView contentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_sucess_or_fail);
		type = getIntent().getIntExtra("type", 0);
		result = getIntent().getStringExtra("result");
		coin = getIntent().getIntExtra("coin", 0);
		titleView = (TextView) findViewById(R.id.status_bar_title);
		imageView = (ImageView) findViewById(R.id.comm_imageview);
		contentView = (TextView) findViewById(R.id.content_view);
		if (type == 0) {
			titleView.setText("兑换结果");
		} else {
			titleView.setText("提现结果");
		}
		exted();
	}

	public void exted() {
		if (result.equals("success")) {
			imageView.setImageResource(R.drawable.success);
			contentView.setText("你已经兑换" + coin + "个逐梦币");
		} else {
			imageView.setImageResource(R.drawable.fail);
		}
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
