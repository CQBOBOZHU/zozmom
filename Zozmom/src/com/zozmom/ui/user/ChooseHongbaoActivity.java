package com.zozmom.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.manager.AccountManager;
import com.zozmom.model.HongbaoModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Util;

public class ChooseHongbaoActivity extends BaseTaskActivity<String> {
	UserInfoModel infoModel;
	ListView listView;
	List<HongbaoModel> mlist = new ArrayList<HongbaoModel>();
	public static final int resultCode = 7;
	public static final int resultCode1 = 9;
	Button notshooseBtn;
	int count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosehongbao_activity);
		TextView titleView = (TextView) findViewById(R.id.status_bar_title);
		titleView.setText("选择红包");
		notshooseBtn = (Button) findViewById(R.id.status_bar_addbtn);
		notshooseBtn.setText("不使用");
		notshooseBtn.setVisibility(View.VISIBLE);
		infoModel = AccountManager.getInstance().getLogginUserInfo();
		count = getIntent().getIntExtra("count", 0);
		listView = (ListView) findViewById(R.id.list_listview);
		// listView.setOnItemClickListener(itemlistener);
		getHongbao();
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.status_bar_addbtn :
				setResult(resultCode1);
				finish();
				break;
		}

	}

	// OnItemClickListener itemlistener = new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// Intent data = new Intent();
	// HongbaoModel model = mlist.get(position);
	// data.putExtra("model", model);
	// setResult(resultCode, data);
	// finish();
	// }
	// };

	public void getHongbao() {
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			ToastUtil.show(m_activity, "网络错误");
			return;
		}
		XHttp.PostByToken(Constant.userBonusList + 0, infoModel, null,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v("this", "onError" + arg0.toString());
						RequestErrorUtil.judge(arg0, m_activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONArray arg0) {
						Log.v("this", "onSuccess " + arg0.toString());
						if (arg0.toString().trim().equals("[]")) {
							ToastUtil.show(m_activity, "没有可用红包");
						} else {
							mlist = JsonUtil.JsonToModel(arg0,
									HongbaoModel.class);
							// judeHongbao(mlist);
							listView.setAdapter(new ListAdapter(m_activity,
									mlist));
						}
					}
				});
	}
	// List<HongbaoModel> hongbaoData = new ArrayList<HongbaoModel>();
	/**
	 * 提取比购买的额度小的红包
	 * 
	 * @param mhongbaolist
	 */
	// protected void judeHongbao(List<HongbaoModel> mhongbaolist) {
	// for (HongbaoModel hongbaoModel : mhongbaolist) {
	// // int discountPrice = hongbaoModel.getDiscountPrice();
	// int usedPrice=hongbaoModel.getUsedPrice();
	// if (usedPrice <= count) {
	// hongbaoData.add(hongbaoModel);
	// }
	// }
	// if (hongbaoData.size() > 0) {
	// listView.setAdapter(new ListAdapter(m_activity, hongbaoData));
	// } else {
	// ToastUtil.show(this, "没有适合的红包");
	// }
	// }

	class ListAdapter extends BaseTaskAdaper<HongbaoModel> {

		public ListAdapter(Context context, List<HongbaoModel> mDatas) {
			super(context, mDatas);
		}

		@SuppressLint({"InflateParams", "NewApi"})
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holderView;
			if (convertView == null) {
				holderView = new HolderView();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.hongbao_item, null);
				holderView.textView = (TextView) convertView
						.findViewById(R.id.hongbao_item_textview);
				holderView.textView1 = (TextView) convertView
						.findViewById(R.id.hongbao_item_textview1);
				holderView.button = (Button) convertView
						.findViewById(R.id.hongbao_item_btn);
				holderView.discountPriceView = (TextView) convertView
						.findViewById(R.id.hongb_priceview);
				convertView.setTag(holderView);
			} else {
				holderView = (HolderView) convertView.getTag();
			}
			// holderView.button.setVisibility(View.GONE);
			HongbaoModel hongbaoModel = mDatas.get(position);
			int usedPrice = hongbaoModel.getUsedPrice();
			holderView.button.setTag(position);
			if (count < usedPrice) {
				holderView.button
						.setBackgroundResource(R.drawable.btn_background_d7);
				holderView.button.setFocusable(false);
				holderView.button.setEnabled(false);
				holderView.button.setClickable(false);
			} else {
				holderView.button
						.setBackgroundResource(R.drawable.btn_background_yellow);
				holderView.button.setFocusable(true);
				holderView.button.setClickable(true);
			}

			String endtime = hongbaoModel.getEndTime();
			int discountPrice = hongbaoModel.getDiscountPrice();
			String title = hongbaoModel.getTitle();
			holderView.textView.setText(title);
			holderView.discountPriceView.setText(discountPrice + "元");
			holderView.textView1.setText("有效时间至:" + Util.GetDataTime(endtime));
			holderView.button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int tag = (int) v.getTag();
					Intent data = new Intent();
					HongbaoModel model = mDatas.get(tag);
					data.putExtra("model", model);
					setResult(resultCode, data);
					finish();
				}
			});
			return convertView;
		}

		class HolderView {
			TextView textView;
			TextView textView1;
			TextView discountPriceView;
			Button button;
		}
	}
}
