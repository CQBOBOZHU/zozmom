package com.zozmom.ui.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.ComeTrueModel;
import com.zozmom.model.ProductModel;
import com.zozmom.ui.GoodMessageActivity;
import com.zozmom.ui.OpeningGoodMessageActivity;
import com.zozmom.ui.adapter.ComeTrueListAdapter;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

/**
 * 实现的梦想
 * 
 * @author Administrator
 */
@ContentView(R.layout.userctrue_activity)
public class UserCtrueActivity extends UserManagerBaseActivity<JSONArray> {
	@ViewInject(R.id.userdream_pull_listview)
	PullToRefreshListView plistview;
	@ViewInject(R.id.status_bar)
	View barview;
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	int userId;
	List<ComeTrueModel> mlist = new ArrayList<ComeTrueModel>();
	ComeTrueListAdapter listAdapter;
	boolean isFresh = true;// true表示下拉刷新 false表示上拉刷新
	public static final int maxSize = 10;
	private int pageNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		titleView.setText(getRString(R.string.cometrue_dream));
		toDoSomeThing();
	}

	public void initView() {
		super.initView();
		userId = infoModel.getUserId();
		plistview.setMode(Mode.PULL_FROM_START);
		plistview.setOnRefreshListener(refresh);
		plistview.setOnItemClickListener(listener);
		requestPost();
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ComeTrueModel model = mlist.get(position - 1);
			int productId = model.getProductId();
			int issueId = model.getIssueId();
			Intent intent;
			intent = new Intent(UserCtrueActivity.this,
					OpeningGoodMessageActivity.class);
			intent.putExtra("productId", productId);
			intent.putExtra("issueId", issueId);
			startActivity(intent);
		}
	};

	public void requestPost() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, getRString(R.string.network_er));
			plistview.postDelayed(new Runnable() {
				@Override
				public void run() {
					plistview.onRefreshComplete();
				}
			}, 300);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();

		if (!isFresh) {
			int size = mlist.size();
			if (size == 0) {
				isFresh = true;
			} else {
				if (size < maxSize * pageNum) {
					plistview.postDelayed(new Runnable() {
						@Override
						public void run() {
							ToastUtil.show(m_activity, "无更多商品");
							plistview.onRefreshComplete();
						}
					}, 300);
					return;
				} else {
					map.put("latestId", mlist.get(size - 1).getId());
				}
			}
		}
		requstPost(Constant.LUCK_DREAM + userId, map);
	}

	OnRefreshListener2<ListView> refresh = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh = true;
			requestPost();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh = false;
			requestPost();
		}
	};

	@Override
	public void onSuccess(JSONArray arg0) {
		super.onSuccess(arg0);
		Log.v("this", arg0.toString());
		plistview.onRefreshComplete();
		List<ComeTrueModel> models = JsonUtil.JsonToModel(arg0,
				ComeTrueModel.class);
		if (models != null && models.size() > 0) {
			if (isFresh) {
				pageNum = 1;
				mlist = models;
				if (mlist.size() >= 10) {
					plistview.setMode(Mode.BOTH);
				}
				listAdapter = new ComeTrueListAdapter(m_activity, mlist,
						infoModel);
				plistview.setAdapter(listAdapter);
			} else {
				pageNum++;
				mlist.addAll(models);
				listAdapter.setmDatas(mlist);
				listAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == AddressManagerActivity.resultCode) {
			isFresh = true;
			requestPost();
		}
	}

	@Override
	public void onError(Throwable arg0, boolean arg1) {
		super.onError(arg0, arg1);
		plistview.onRefreshComplete();
		RequestErrorUtil.judge(arg0, m_activity);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
		}
	}

}
