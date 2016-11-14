package com.zozmom.ui.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.xutils.x;
import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zozmom.constants.Constant;
import com.zozmom.model.AddressModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

public class AddressManagerActivity extends BaseTaskActivity<JSONArray> {
	TextView titleView;
	ListView address_listview;
	ListAdapter adapter;
	Button addbtn;
	UserInfoModel infoModel;
	List<AddressModel> list = new ArrayList<AddressModel>();
	PullToRefreshScrollView refreshScrollView;
	// Button addview;// 添加地址
	Button statAddBtn;// 标题栏上的添加
	TextView addTextView;
	ImageView addicon;
	private Map<Integer, Boolean> map = new HashMap<>();
	private Button chooseedBtn;

	public static final int resultCode = 1;

	private int id;// 幸运记录
	private int addressId;// 用户地址Id
	// private int chooseIndex;
	private int isChooseSubmitAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_manager_activity);
		id = getIntent().getIntExtra("id", -1);
		isChooseSubmitAddress = id;
		infoModel = getLogginUserinfo();
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.scrollview);
		refreshScrollView.setMode(Mode.PULL_FROM_START);
		titleView = (TextView) findViewById(R.id.status_bar_title);
		address_listview = (ListView) findViewById(R.id.address_listview);
		titleView.setText(getRString(R.string.address_management));
		statAddBtn = (Button) findViewById(R.id.status_bar_addbtn);
		statAddBtn.setVisibility(View.VISIBLE);
		// addview = (Button) findViewById(R.id.add_ad_view);
		statAddBtn.setText("添加");
		addTextView = (TextView) findViewById(R.id.address_text);
		addicon = (ImageView) findViewById(R.id.address_addicon);
		if (id != -1) {
			addicon.setVisibility(View.GONE);
			addTextView.setText("提交");
			// address_listview.setOnItemClickListener(this);
		}
		refreshScrollView.setOnRefreshListener(listener);
		requestAddress();
	}

	/**
	 * 查地址
	 */
	public void requestAddress() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, getRString(R.string.network_hint));
			refreshScrollView.onRefreshComplete();
			return;
		}
		RequestParams params = new RequestParams(Constant.SELECT_ADDRESS);
		long time = System.currentTimeMillis();
		params.addHeader("token-param", time + "," + infoModel.getToken() + ","
				+ infoModel.getUserId());
		x.http().get(params, new CommonCallback<JSONArray>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				refreshScrollView.onRefreshComplete();
				RequestErrorUtil.judge(arg0, m_activity);
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(JSONArray arg0) {
				Log.v(TAG, arg0.toString());
				refreshScrollView.onRefreshComplete();
				list = JsonUtil.JsonToModel(arg0, AddressModel.class);
				adapter = new ListAdapter(m_activity, list);
				address_listview.setAdapter(adapter);
			}
		});
	}

	public void notifyChange(int removeid) {
		list.remove(removeid);
		adapter.setmDatas(list);
		adapter.notifyDataSetChanged();
	}

	// boolean isrefresh=true;//true表示上啦 false表示下拉
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// super.onItemClick(parent, view, position, id);
	// chooseIndex = position;
	// showSureDialog();
	// }

	OnRefreshListener2<ScrollView> listener = new OnRefreshListener2<ScrollView>() {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ScrollView> refreshView) {
			// isrefresh=true;
			requestAddress();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

		}
	};
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (id == -1) {
			finish();
			return false;
		} else {
			showNotSubmitAdressDialog();
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
			case R.id.add_ad_view :
				if (id == -1) {
					intent = new Intent(this, AddaddressActivity.class);
					startActivityForResult(intent, 0);
				} else {
					// 提交
					onPostAddress();
				}
				break;
			case R.id.status_bar_Exit :
				if (id == -1) {
					finish();
				} else {
					showNotSubmitAdressDialog();
				}
				break;
			case R.id.status_bar_addbtn :
				intent = new Intent(this, AddaddressActivity.class);
				startActivityForResult(intent, 0);
				break;
		}
	}

	Drawable leftCse;
	Drawable leftunCse;
	class ListAdapter extends BaseTaskAdaper<AddressModel> {

		public ListAdapter(Context context, List<AddressModel> mDatas) {
			super(context, mDatas);
			extracted(mDatas);
		}

		@Override
		public void setmDatas(List<AddressModel> mDatas) {
			// TODO Auto-generated method stub
			super.setmDatas(mDatas);
			extracted(mDatas);
		}

		private void extracted(List<AddressModel> mDatas) {
			map = new HashMap<>();
			if (chooseedBtn != null) {
				CheckBtn(false);
				chooseedBtn = null;
			}
			for (int i = 0; i < mDatas.size(); i++) {
				map.put(i, false);
			}
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater
						.inflate(R.layout.addresslist_item, null);
				holder.nameview = (TextView) convertView
						.findViewById(R.id.address_item_nameview);
				holder.messview = (TextView) convertView
						.findViewById(R.id.address_item_messageview);
				holder.phoneview = (TextView) convertView
						.findViewById(R.id.address_item_phoneview);
				holder.updataBtn = (Button) convertView
						.findViewById(R.id.updata_btn);
				holder.deleteBtn = (Button) convertView
						.findViewById(R.id.addressdelete_btn);
				holder.chooseBtn = (Button) convertView
						.findViewById(R.id.choose_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (isChooseSubmitAddress == -1) {
				holder.chooseBtn.setVisibility(View.GONE);
			} else {
				holder.chooseBtn.setVisibility(View.VISIBLE);
				leftCse = getRDrawble(R.drawable.ricle_yes);
				leftCse.setBounds(0, 0, leftCse.getMinimumWidth(),
						leftCse.getMinimumHeight());
				leftunCse = getRDrawble(R.drawable.ricle_no);
				leftunCse.setBounds(0, 0, leftunCse.getMinimumWidth(),
						leftunCse.getMinimumHeight());
			}

			AddressModel model = mDatas.get(position);
			String name = model.getConsignee();
			String province = model.getProvince();
			String city = model.getCity();
			String district = model.getDistrict();
			String address = model.getAddress();
			String phone = model.getPhone();
			holder.nameview.setText(name);
			holder.messview.setText(province + city + district + address);
			holder.phoneview.setText(phone);
			holder.updataBtn.setTag(position);
			holder.deleteBtn.setTag(position);
			holder.chooseBtn.setTag(position);
			if (map.get(position)) {
				// chooseedBtn=holder.chooseBtn;
				CheckBtn(true);
			} else {

			}
			holder.updataBtn.setOnClickListener(clickListener);
			holder.deleteBtn.setOnClickListener(clickListener);
			holder.chooseBtn.setOnClickListener(clickListener);
			return convertView;
		}

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = (int) v.getTag();
				// AddressModel model=list.get(position);
				switch (v.getId()) {
					case R.id.updata_btn :
						Intent intent = new Intent(m_activity,
								AddaddressActivity.class);
						intent.putExtra("data", list.get(position));
						startActivityForResult(intent, 0);
						break;
					case R.id.addressdelete_btn :
						exitordelete(position);
						break;
					case R.id.choose_btn :
						if (chooseedBtn == null) {
							map.put(position, true);
							chooseedBtn = (Button) v;
							CheckBtn(true);
							chooseedBtn.setTag(position);
						} else {
							if (map.get(position)) {
								CheckBtn(false);
								chooseedBtn = null;
								map.put(position, false);
							} else {
								int a = (int) chooseedBtn.getTag();
								CheckBtn(false);
								map.put(a, false);
								chooseedBtn = (Button) v;
								CheckBtn(true);
								map.put(position, true);
								chooseedBtn.setTag(position);
							}
						}

						break;
				}
			}
		};

		@SuppressLint("ResourceAsColor")
		public void CheckBtn(boolean bl) {
			if (bl) {
				chooseedBtn.setTextColor(R.color.background);
				chooseedBtn.setCompoundDrawables(leftCse, null, null, null);
			} else {
				chooseedBtn.setTextColor(R.color.good_message_textcolors);
				chooseedBtn.setCompoundDrawables(leftunCse, null, null, null);
			}
		}

		class ViewHolder {
			TextView nameview;
			TextView messview;
			TextView phoneview;
			Button updataBtn;
			Button deleteBtn;
			Button chooseBtn;

		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == AddaddressActivity.resultSaveCode) {
			requestAddress();
		} else if (arg1 == AddaddressActivity.resultUpdaCode) {
			requestAddress();
		}
	}
	AlertDialog tilealertDialog;

	public void exitordelete(int position) {
		try {
			if (tilealertDialog != null) {
				if (tilealertDialog.isShowing()) {
					tilealertDialog.dismiss();
				}
				tilealertDialog = null;
			}
			AlertDialog.Builder builder = new Builder(this);
			tilealertDialog = builder.create();
			tilealertDialog.show();
			Window window = tilealertDialog.getWindow();
			window.setContentView(R.layout.exit_activity);
			TextView titleview = (TextView) window
					.findViewById(R.id.exit_dialog_title);
			titleview.setText("确定删除该地址吗?");
			Button startCa = (Button) window.findViewById(R.id.exitBtn0);
			Button startM = (Button) window.findViewById(R.id.exitBtn1);
			startCa.setTag(position);
			startCa.setOnClickListener(onClist);
			startM.setOnClickListener(onClist);
			tilealertDialog.setCanceledOnTouchOutside(true);
		} catch (Exception e) {

		}
	}

	OnClickListener onClist = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.exitBtn0 :
					int position = (int) v.getTag();
					deleteAddress(position);
					break;
				case R.id.exitBtn1 :
					tilealertDialog.dismiss();
					break;
			}
		}
	};

	/**
	 * 删除地址
	 */
	private void deleteAddress(final int position) {
		AddressModel adModel = list.get(position);
		int addid = adModel.getId();
		RequestParams params = new RequestParams(Constant.DELETE_ADDRESS
				+ addid);
		long time = System.currentTimeMillis();
		params.addHeader("token-param", time + "," + infoModel.getToken() + ","
				+ infoModel.getUserId());
		x.http().get(params, new CommonCallback<String>() {

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
					tilealertDialog.dismiss();
					ToastUtil.show(m_activity, "删除成功");
					notifyChange(position);
				} else {
					ToastUtil.show(m_activity, "删除失败");
				}
			}
		});
	}
	AlertDialog notSubmitDialog;
	/**
	 * 提示用户还没有提交收货地址
	 */
	public void showNotSubmitAdressDialog() {
		AlertDialog.Builder builder = new Builder(this);
		notSubmitDialog = builder.create();
		notSubmitDialog.show();
		Window window = notSubmitDialog.getWindow();
		window.setContentView(R.layout.not_submit_dialog);
		Button startCa = (Button) window.findViewById(R.id.exitBtn0);
		Button startM = (Button) window.findViewById(R.id.exitBtn1);
		startCa.setOnClickListener(onClist1);
		startM.setOnClickListener(onClist1);

	}

	OnClickListener onClist1 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.exitBtn0 :
					finish();
					break;
				case R.id.exitBtn1 :
					notSubmitDialog.dismiss();
					break;
			}
		}
	};

	// AlertDialog sureDialog;
	/**
	 * 确认选择
	 */
	// public void showSureDialog() {
	// try {
	// if (sureDialog != null) {
	// sureDialog.show();
	// return;
	// }
	// AlertDialog.Builder builder = new Builder(this);
	// sureDialog = builder.create();
	// sureDialog.show();
	// Window window = sureDialog.getWindow();
	// window.setContentView(R.layout.exit_activity);
	// TextView titleview = (TextView) window
	// .findViewById(R.id.exit_dialog_title);
	// titleview.setText("确定地址?");
	// Button startCa = (Button) window.findViewById(R.id.exitBtn0);
	// Button startM = (Button) window.findViewById(R.id.exitBtn1);
	// startCa.setOnClickListener(onClist1);
	// startM.setOnClickListener(onClist1);
	// sureDialog.setCanceledOnTouchOutside(true);
	// } catch (Exception e) {
	// Log.e(TAG, e.toString());
	// }
	// }
	// OnClickListener onClist1 = new OnClickListener() {

	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.exitBtn0 :
	// onPostAddress();
	// break;
	// case R.id.exitBtn1 :
	// sureDialog.dismiss();
	// break;
	// }
	// }
	// };

	private void onPostAddress() {

		if (chooseedBtn == null) {
			ToastUtil.show(this, "请选择地址");
			return;
		}
		int tag = (int) chooseedBtn.getTag();

		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, getRString(R.string.network_er));
			// sureDialog.dismiss();
			return;
		}
		// AddressModel model = list.get(chooseIndex);
		AddressModel model = list.get(tag);
		addressId = model.getId();
		RequestParams params = new RequestParams(Constant.CHOOSE_ADDRESS);
		long time = System.currentTimeMillis();
		params.addHeader("token-param", time + "," + infoModel.getToken() + ","
				+ infoModel.getUserId());
		params.addParameter("id", id);
		params.addParameter("addressId", addressId);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// sureDialog.dismiss();
				RequestErrorUtil.judge(arg0, m_activity);
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(String arg0) {
				// sureDialog.dismiss();
				if (arg0.equals("true")) {
					ToastUtil.show(m_activity, "提交成功");
					setResult(resultCode);
					finish();
				} else {
					ToastUtil.show(m_activity, "提交失败");
				}
			}
		});

	}

}
