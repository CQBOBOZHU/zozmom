package com.zozmom.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.ex.HttpException;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zozmom.constants.Constant;
import com.zozmom.manager.AccountManager;
import com.zozmom.model.ActivityModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseActivity;
import com.zozmom.ui.IndexActivity;
import com.zozmom.ui.LoginActivity;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.ui.user.FindWebActivity;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.LogUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 新的发现页面
 * 
 * @author Administrator
 * 
 */
public class NewFindFragment extends BaseTaskFragment {
	PullToRefreshListView listview;
	List<ActivityModel> mAs;
	UserInfoModel userinfo;
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			LogUtil.v("NewFindFragment:  onCreateView");
			rootView = LayoutInflater.from(getActivity()).inflate(
					R.layout.new_findfragment, null);
			TextView title = (TextView) rootView
					.findViewById(R.id.status_bar_title);
			title.setText(getResources().getString(R.string.all_goods));
			ImageButton exitbtn = (ImageButton) rootView
					.findViewById(R.id.status_bar_Exit);
			exitbtn.setVisibility(View.GONE);
			listview = (PullToRefreshListView) rootView
					.findViewById(R.id.new_findlistview);
			listview.setMode(Mode.PULL_FROM_START);
			listview.setOnRefreshListener(listener);
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (position == mAs.size()) {
//						IndexActivity.indexActivity.showGetFirstHongbao(1);
					} else {
						ActivityModel activityModel = mAs.get(position - 1);
						int mid = activityModel.getId();
						if (mid == 0) {
							String url = activityModel.getActivityLine();
							String murl = url + "?type=app";
							String title = activityModel.getMasterTitle();
							Intent intent = new Intent(m_activity,
									FindWebActivity.class);
							intent.putExtra("weburl", murl);
							intent.putExtra("title", title);
							m_activity.startActivity(intent);
						} else {
							requestBox(position - 1);
						}
					}
				}
			});
		}
		return rootView;
	}

	OnRefreshListener<ListView> listener = new OnRefreshListener<ListView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
				listview.postDelayed(new Runnable() {
					@Override
					public void run() {
						listview.onRefreshComplete();
						ToastUtil.show(m_activity, "网络错误");
					}
				}, 300);
				return;
			}
			requestActivity();
		}
	};

	public void requestBox(final int position) {
		if (userinfo == null) {
			ToastUtil.show(m_activity, "亲~先登录哦");
			Intent intent = new Intent(m_activity, LoginActivity.class);
			m_activity.startActivityForResult(intent, 1);
			return;
		}
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			ToastUtil.show(m_activity, "网络错误");
			return;
		}
		XHttp.PostByToken(Constant.ownPrivateInfo, userinfo, null,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						if (arg0 instanceof HttpException) { // 网络错误
							HttpException arg = (HttpException) arg0;
							String result = arg.getResult();
							JSONObject object;
							try {
								object = new JSONObject(result);
								String msg = object.getString("msg");
								String code = object.getString("code");
								if (code.equals("DFT.0007")
										|| code.equals("DFT.0005")) {
									Intent intent = new Intent(m_activity,
											LoginActivity.class);
									((BaseActivity<?>) m_activity)
											.startActivityForResult(intent, 1);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						ActivityModel activityModel = mAs.get(position);
						String boxurl = activityModel.getActivityLine()
								+ "?type=app";
						String murl = boxurl + "&token=" + userinfo.getToken()
								+ "&userId=" + userinfo.getUserId();
						String title = activityModel.getMasterTitle();
						Intent intent = new Intent(m_activity,
								FindWebActivity.class);
						intent.putExtra("weburl", murl);
						intent.putExtra("title", title);
						m_activity.startActivity(intent);
					}
				});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		userinfo = AccountManager.getInstance().getCurrentUserInfo();
	}

	public void requestActivity() {
		XHttp.Get(Constant.ACTIVITY_LIST, null,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						listview.onRefreshComplete();
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONArray arg0) {
						listview.onRefreshComplete();
						List<ActivityModel> list = JsonUtil.JsonToModel(arg0,
								ActivityModel.class);
						if (list != null) {
							mAs = new ArrayList<ActivityModel>();
							ActivityModel model = new ActivityModel();
							model.setId(1);
							model.setSign(0);
							mAs.addAll(list);
							mAs.add(model);
							ListAdapter adapter = new ListAdapter(
									getActivity(), mAs);
							listview.setAdapter(adapter);
						}
					}
				});
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		LogUtil.v("NewFindFragment:  setUserVisibleHint"+isVisibleToUser);
		userinfo = AccountManager.getInstance().getLogginUserInfo();
		if (isVisibleToUser) {
			if (mAs == null || mAs.size() == 0) {
				requestActivity();
			}
		}
	}

	class ListAdapter extends BaseTaskAdaper<ActivityModel> {
		Drawable ddr;
		@SuppressWarnings("deprecation")
		public ListAdapter(Context context, List<ActivityModel> mDatas) {
			super(context, mDatas);
			ddr = getResources().getDrawable(R.drawable.hot);
			ddr.setBounds(0, 0, ddr.getMinimumWidth(), ddr.getMinimumHeight());
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.find_list_item, null);
				viewHolder.iconView = (ImageView) convertView
						.findViewById(R.id.newfind_imgview_dream);
				viewHolder.titleView = (TextView) convertView
						.findViewById(R.id.newfind_title);
				viewHolder.seTitleView = (TextView) convertView
						.findViewById(R.id.newfind_setitle);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (position == mDatas.size()-1) {
				convertView.setVisibility(View.GONE);
//				viewHolder.iconView.setImageResource(R.drawable.fu);
//				viewHolder.titleView.setText("新手红包");
//				viewHolder.seTitleView.setText("送你个红包，愿你梦想成真。");
			} else {
				ActivityModel model = mDatas.get(position);
				String icon = model.getIcon();
				String title = model.getMasterTitle();
				String sctitle = model.getSalveTitle();
				int sign = model.getSign();
				loadImage(Constant.PRODUCT_URL + icon + Constant.PRODUCT_KEY,
						viewHolder.iconView);
				viewHolder.titleView.setText(title);
				if (sign == 1) {
					viewHolder.titleView.setCompoundDrawables(null, null, ddr,
							null);
				} else {
					viewHolder.titleView.setCompoundDrawables(null, null, null,
							null);
				}
				viewHolder.seTitleView.setText(sctitle);
			}
			return convertView;
		}

		class ViewHolder {
			ImageView iconView;
			TextView titleView;
			TextView seTitleView;
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		LogUtil.v("NewFindFragment:  onDestroyView");
//		if (rootView != null) {
//			((ViewGroup) rootView.getParent()).removeView(rootView);
//		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LogUtil.v("NewFindFragment:  onDestroy");
		super.onDestroy();
	}
}
