package com.zozmom.ui.user;

import org.xutils.x;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CityPicker;
import com.zozmom.cuview.CityPicker.OnSelectingListener;
import com.zozmom.model.AddressModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 添加地址
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.add_address_activity)
public class AddaddressActivity extends BaseTaskActivity<Object> {
	@ViewInject(R.id.add_address_provincetextview)
	TextView provinceView;
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	@ViewInject(R.id.add_address_usernameedit)
	EditText nameedit;
	@ViewInject(R.id.add_address_userphoneedit)
	EditText phoneedit;
	@ViewInject(R.id.add_address_xiangqingedit)
	EditText xiangxiaddress;
	@ViewInject(R.id.exit_delete_btn)
	Button exitordeleted;
	@ViewInject(R.id.layout_citylay)
	View citylay;
	public static AddaddressActivity instance = null;
	UserInfoModel infoModel;
	static final int resultSaveCode = 1;
//	static final int resultDeteCode = 2;
	static final int resultUpdaCode = 3;

	private String consignee;
	private String phone;
	private String province;
	private String city;
	private String district;
	private String address;
	private int addid;

	int type = 1;// 0 修改 1新增加

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		instance = this;
		AddressModel model = (AddressModel) getIntent().getSerializableExtra(
				"data");
		infoModel = getLogginUserinfo();
		if (model != null) {
			type = 0;
			addid = model.getId();
			consignee = model.getConsignee();
			phone = model.getPhone();
			province = model.getProvince();
			city = model.getCity();
			district = model.getDistrict();
			address = model.getAddress();
			nameedit.setText(consignee);
			phoneedit.setText(phone);
			provinceView.setText(province + "-" + city + "-" + district);
			xiangxiaddress.setText(address);
			titleView.setText("地址详情");//
			exitordeleted.setText("取消");
		} else {
			type = 1;
			exitordeleted.setText("取消");
		}
		nameedit.addTextChangedListener(new MaxLengthWatcher(8, nameedit, this));
		xiangxiaddress.addTextChangedListener(new MaxLengthWatcher(50,
				xiangxiaddress, this));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				exitordelete(v);
				break;
			case R.id.province_lay :
				showCityDailog();
				break;
		}
	}

	public void saveAddress(View view) {
		if (type == 1) {
			save();
		} else {
			updateAddress();
		}
	}

	/**
	 * 判断输入是否规范
	 * 
	 * @return
	 */
	public boolean instance() {
		consignee = nameedit.getText().toString();
		phone = phoneedit.getText().toString();
		address = xiangxiaddress.getText().toString();
		if (consignee == null || consignee.trim().length() == 0) {
			ToastUtil.show(this, "收货人不能为空");
			return false;
		}
		if (phone == null || !Util.isMobile(phone)) {
			ToastUtil.show(this, "手机号码错误");
			return false;
		}
		if (province == null || province.length() == 0) {
			ToastUtil.show(this, "选择地址");
			return false;
		}
		if (address == null || address.trim().length() == 0) {
			ToastUtil.show(this, "详细地址不能为空");
			return false;
		}
		return true;
	}

	private void save() {
		if (!instance()) {
			return;
		}
		RequestParams params = new RequestParams(Constant.ADD_ADDRESS);
		long time = System.currentTimeMillis();
		params.addHeader("token-param", time + "," + infoModel.getToken() + ","
				+ infoModel.getUserId());
		params.addParameter("consignee", consignee);
		params.addParameter("phone", phone);
		params.addParameter("province", province);
		params.addParameter("city", city);
		params.addParameter("district", district);
		params.addParameter("address", address);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				RequestErrorUtil.judge(arg0, m_activity);
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(String arg0) {
				if (arg0.equals("true")) {
					ToastUtil.show(m_activity, "保存成功");
					setResult(resultSaveCode);
					finish();
				}else{
					ToastUtil.show(m_activity, "保存失败");
				}
			}
		});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					v.clearFocus();
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = {0, 0};
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	AlertDialog dialog;

	public void showCityDailog() {
		AlertDialog.Builder alertDialog = new Builder(this);
		dialog = alertDialog.create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setBackgroundDrawableResource(R.drawable.btn_background_gray);
		window.setContentView(R.layout.address_city_dialog);
		CityPicker cityPicker = (CityPicker) window
				.findViewById(R.id.citypicker);
		cityPicker.setOnSelectingListener(new OnSelectingListener() {

			@Override
			public void selected(boolean selected, String province_name,
					String city_name, String count_name) {
				province = province_name;
				city = city_name;
				district = count_name;
				provinceView.setText(province_name + "-" + city_name + "-"
						+ count_name);
			}
		});
		dialog.setCanceledOnTouchOutside(true);
	}

	AlertDialog exitdialog;

	public void exitordelete(View view) {
		if (exitdialog != null) {
			exitdialog.show();
			return;
		}
		AlertDialog.Builder builder = new Builder(this);
		exitdialog = builder.create();
		exitdialog.show();
		Window window = exitdialog.getWindow();
		window.setContentView(R.layout.eixtaddress);
		TextView titleview = (TextView) window
				.findViewById(R.id.exit_dialog_title);
		titleview.setText("是否放弃本次编辑");
		Button startCa = (Button) window.findViewById(R.id.exitBtn0);
		Button startM = (Button) window.findViewById(R.id.exitBtn1);
		startCa.setOnClickListener(listener1);
		startM.setOnClickListener(listener1);
		exitdialog.setCanceledOnTouchOutside(true);
	}

	OnClickListener listener1 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.exitBtn0 :
//					exitdialog.dismiss();
					finish();
					break;
				case R.id.exitBtn1 :
					exitdialog.dismiss();
					break;
			}
		}
	};

	/**
	 * 修改地址
	 */
	private void updateAddress() {
		if (!instance()) {
			return;
		}
		RequestParams params = new RequestParams(Constant.UPDATE_ADDRESS);
		long time = System.currentTimeMillis();
		params.addHeader("token-param", time + "," + infoModel.getToken() + ","
				+ infoModel.getUserId());
		params.addParameter("consignee", consignee);
		params.addParameter("phone", phone);
		params.addParameter("province", province);
		params.addParameter("city", city);
		params.addParameter("district", district);
		params.addParameter("address", address);
		params.addParameter("id", addid);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				RequestErrorUtil.judge(arg0, m_activity);
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(String arg0) {
				if (arg0.equals("true")) {
					ToastUtil.show(m_activity, "更改成功");
					setResult(resultUpdaCode);
					finish();
				}else{
					ToastUtil.show(m_activity, "更改失败");
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitordelete(null);
			return false;
		}
		return true;
	}
}
