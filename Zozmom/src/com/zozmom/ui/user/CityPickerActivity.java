package com.zozmom.ui.user;

import com.chasedream.zhumeng.R;
import com.zozmom.cuview.CityPicker;
import com.zozmom.cuview.CityPicker.OnSelectingListener;
import com.zozmom.ui.BaseTaskActivity;

import android.os.Bundle;
import android.view.MotionEvent;

public class CityPickerActivity extends BaseTaskActivity<Object>{
	CityPicker cityPicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_activity);
		cityPicker = (CityPicker) findViewById(R.id.citypicker1);
		cityPicker.setOnSelectingListener(new OnSelectingListener() {
			
			@Override
			public void selected(boolean selected, String province_name,
					String city_name, String couny_name) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		return true;
	}
}
