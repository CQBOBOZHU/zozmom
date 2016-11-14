package com.zozmom.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.zozmom.constants.Constant;
import com.zozmom.manager.AccountManager;
import com.zozmom.model.HongbaoModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.ui.fragment.HongbaoFragment.ListAdapter;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UsedHongbaoFragment extends BaseTaskFragment {
	UserInfoModel infoModel;
	PullToRefreshListView listView;
	List<HongbaoModel> mlist = new ArrayList<HongbaoModel>();
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.list_fragment, null);
		infoModel = AccountManager.getInstance().getLogginUserInfo();
		listView = (PullToRefreshListView) view.findViewById(R.id.list_listview);
		listView.setOnRefreshListener(listener);
		return view;
	}
	boolean isFresh=true;
	
	OnRefreshListener<ListView> listener=new OnRefreshListener<ListView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			isFresh=false;
			getHongbao();
		}
	};
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && mlist.size() == 0) {
			getHongbao();
		}
	}

	public void getHongbao() {
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			if (isFresh) {
				ToastUtil.show(m_activity, "网络错误");
			} else {
				listView.postDelayed(new Runnable() {
					@Override
					public void run() {
						listView.onRefreshComplete();
						ToastUtil.show(m_activity, "网络错误");
					}
				}, 300);
			}
			return;
		}
		XHttp.PostByToken(Constant.userBonusList + 1, infoModel, null,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v("this", "onError" + arg0.toString());
						listView.onRefreshComplete();
						RequestErrorUtil.judge(arg0, m_activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONArray arg0) {
						Log.v("this", "onSuccess " + arg0.toString());
						listView.onRefreshComplete();
						if (arg0.toString().trim().equals("[]")) {
							ToastUtil.show(m_activity, "没有数据");
						} else {
							mlist = JsonUtil.JsonToModel(arg0,
									HongbaoModel.class);
							listView.setAdapter(new ListAdapter(m_activity,
									mlist));
						}
					}
				});
	}
	class ListAdapter extends BaseTaskAdaper<HongbaoModel> {

		public ListAdapter(Context context, List<HongbaoModel> mDatas) {
			super(context, mDatas);
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
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
				holderView.priceView = (TextView) convertView
						.findViewById(R.id.hongb_priceview);
				convertView.setTag(holderView);
			} else {
				holderView = (HolderView) convertView.getTag();
			}
			HongbaoModel hongbaoModel = mDatas.get(position);
			String title = hongbaoModel.getTitle();
			String endTime = hongbaoModel.getEndTime();
			int a = hongbaoModel.getDiscountPrice();
			int status = hongbaoModel.getStatus();
			holderView.textView.setText(title);
			holderView.textView1.setText("有效时间:" + Util.GetDataTime(endTime));
			holderView.button.setBackground(getResources().getDrawable(
					R.drawable.btn_background_gray1));
			holderView.priceView.setText(a + "元");
			holderView.button.setTextColor(getResources().getColor(
					R.color.good_message_textcolors));
			if (status == 1) {
				holderView.button.setText("已使用");
			} else {
				holderView.button.setText("已过期");
			}
			return convertView;
		}

		class HolderView {
			TextView textView;
			TextView textView1;
			TextView priceView;
			Button button;
		}
	}
}
