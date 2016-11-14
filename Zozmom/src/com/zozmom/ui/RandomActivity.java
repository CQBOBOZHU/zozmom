package com.zozmom.ui;

import com.chasedream.zhumeng.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class RandomActivity extends Activity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.random_activity);
		TextView titleview=(TextView) findViewById(R.id.status_bar_title);
		titleview.setText("用户随机数");
	}
	
	public void onClick(View view){
		finish();
	}
}
