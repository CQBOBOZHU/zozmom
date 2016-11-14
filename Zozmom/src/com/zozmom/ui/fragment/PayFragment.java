package com.zozmom.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.zozmom.constants.Constant;
import com.zozmom.model.ConsumeModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.ui.user.AccountActivity;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Util;
/**
 * 充值记录
 * @author Administrator
 *
 */
public class PayFragment extends BaseFragment {
	PullToRefreshListView listView;
	private static final int maxSize = 10;
	int pageNum = 1;
	boolean isFresh = true;// 下拉为true 上啦为false
	UserInfoModel infoModel;
	public PayFragment(UserInfoModel infoModel) {
		this.infoModel = infoModel;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.list_fragment, null);
		listView = (PullToRefreshListView) view
				.findViewById(R.id.list_listview);
		listView.setMode(Mode.PULL_FROM_START);
		listView.setOnRefreshListener(onRefreshListener2);
		getList();
		return view;
	}

	OnRefreshListener2<ListView> onRefreshListener2 = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh = true;
			getList();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh = false;
			getList();
		}
	};

	List<ConsumeModel> mlist = new ArrayList<ConsumeModel>();
	PayListAdapter payListAdapter;
	public void getList() {
		if (!NetWorkUtil.isNetworkAvailable(getActivity())) {
			listView.onRefreshComplete();
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isFresh) {
			int size = mlist.size();
			if (size < pageNum * maxSize) {
				listView.postDelayed(new Runnable() {
		            @Override
		            public void run() {
		            	ToastUtil.show(m_activity, "没有更多数据");
		            	listView.onRefreshComplete();
		            }
		        }, 200);
				return;
			} else {
				map.put("recordId", mlist.get(size - 1).getId());
			}
		}

		XHttp.PostByToken(Constant.GET_RECHARGE_RECORD, infoModel, map,
				new CommonCallback<JSONArray>() {

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
						Log.v("this", arg0.toString());
						listView.onRefreshComplete();
						List<ConsumeModel> mli = JsonUtil.JsonToModel(arg0,
								ConsumeModel.class);
						if (mli != null && mli.size() > 0) {
							if (isFresh) {
								mlist = mli;
								pageNum = 1;
								if (mlist.size() >= maxSize) {
									listView.setMode(Mode.BOTH);
								}
								payListAdapter = new PayListAdapter(m_activity,
										mlist);
								listView.setAdapter(payListAdapter);
							} else {
								pageNum++;
								mlist.addAll(mli);
								payListAdapter.setmDatas(mlist);
								payListAdapter.notifyDataSetChanged();
							}

						}
					}
				});
	}

	/**
	 * 充值adpter
	 * 
	 * @author Administrator
	 * 
	 */
	class PayListAdapter extends BaseTaskAdaper<ConsumeModel> {

		public PayListAdapter(Context context, List<ConsumeModel> mDatas) {
			super(context, mDatas);
		}

		@SuppressLint({"ViewHolder", "InflateParams"})
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater
					.inflate(R.layout.account_paylist_item, null);
			TextView timeView = (TextView) convertView
					.findViewById(R.id.account_recharge_timeview);
			TextView priceView = (TextView) convertView
					.findViewById(R.id.account_recharge_priceView);
			ConsumeModel model = mDatas.get(position);
			long createTime = model.getCreateTime();
			int coin = model.getAmount();
			timeView.setText(Util.GetDataTime(createTime));
			priceView.setText(coin + "元");
			return convertView;
		}

	}
}