package com.zozmom.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.xutils.x;
import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zozmom.constants.Constant;
import com.zozmom.model.AddressModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

public class ChooseAdressActivity extends BaseTaskActivity<JSONArray> {
	TextView titleView;
	PullToRefreshListView listView;
	UserInfoModel infoModel;
	List<AddressModel> list = new ArrayList<AddressModel>();
//	private int id;// 幸运记录
//	private int addressId;// 用户地址Id
//	private int chooseIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_address_activity);
//		id = getIntent().getIntExtra("id", 0);
		infoModel = getLogginUserinfo();
		titleView = (TextView) findViewById(R.id.status_bar_title);
		listView = (PullToRefreshListView) findViewById(R.id.choose_address_listview);
		listView.setMode(Mode.PULL_FROM_START);
		titleView.setText("选择地址");
		listView.setOnRefreshListener(listener);
		listView.setOnItemClickListener(itemlistener);
		requestAdress();
	}

	OnItemClickListener itemlistener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			chooseIndex = position;
//			showSureDialog();
		}
	};

	OnRefreshListener2<ListView> listener = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			requestAdress();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
		}
	}
	ListAdapter adapter;
	public void requestAdress() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, getRString(R.string.network_hint));
			listView.onRefreshComplete();
			return;
		}
		RequestParams params = new RequestParams(Constant.SELECT_ADDRESS);
		long time = System.currentTimeMillis();
		params.addHeader("token-param", time + "," + infoModel.getToken()+","+infoModel.getUserId());
		x.http().get(params, new CommonCallback<JSONArray>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				listView.onRefreshComplete();
				RequestErrorUtil.judge(arg0, m_activity);
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(JSONArray arg0) {
				Log.v(TAG, arg0.toString());
				listView.onRefreshComplete();
				list = JsonUtil.JsonToModel(arg0, AddressModel.class);
				adapter = new ListAdapter(m_activity, list);
				listView.setAdapter(adapter);
			}
		});
	}

	class ListAdapter extends BaseTaskAdaper<AddressModel> {

		public ListAdapter(Context context, List<AddressModel> mDatas) {
			super(context, mDatas);
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.choose_address_listitem, null);
				holder.nameview = (TextView) convertView
						.findViewById(R.id.address_item_nameview);
				holder.messview = (TextView) convertView
						.findViewById(R.id.address_item_messageview);
				holder.phoneview = (TextView) convertView
						.findViewById(R.id.address_item_phoneview);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
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
			return convertView;
		}

		class ViewHolder {
			TextView nameview;
			TextView messview;
			TextView phoneview;
		}
	}
}
