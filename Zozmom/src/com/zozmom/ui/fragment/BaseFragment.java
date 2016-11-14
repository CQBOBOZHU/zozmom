package com.zozmom.ui.fragment;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.xutils.common.Callback.CommonCallback;

import com.chasedream.zhumeng.R;
import com.zozmom.net.XHttp;
import com.zozmom.pic.IPhotoSelectResultListener;
import com.zozmom.pic.IPhotoUploadResultListener;
import com.zozmom.ui.BaseActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class BaseFragment extends Fragment implements OnClickListener,
		IPhotoUploadResultListener, IPhotoSelectResultListener,
		CommonCallback<JSONArray> {
	protected int m_layoutID = 0;
	protected BaseActivity<?> m_activity;
	protected View m_rootView;
	protected int m_titleID;
	private View m_reload;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return m_rootView;
	}

	protected void setTitle(int id) {
		m_titleID = id;
	}

	protected View findViewById(int id) {
		View res = null;
		if (id > 0) {
			if (m_rootView != null) {
				res = m_rootView.findViewById(id);
			}
			if (res == null) {
				res = m_activity.findViewById(id);
			}
		}
		return res;
	}

	@Override
	public void onAttach(Context activity) {
		super.onAttach(activity);

		m_activity = (BaseActivity<?>) activity;
	}

	protected void setContentView(int id) {
		m_layoutID = id;
	}

	@Override
	public void onClick(View arg0) {
	}

	@Override
	public void onPhotoSelected(String photo, boolean isHead) {
	}

	@Override
	public void onPicUploaded(List<String> uploadedPics) {
	}

	public void reload() {
	}

	/**
	 * 没有数据 或者网络
	 */
	public void showReload() {
		if (m_reload == null) {
			m_reload = this.findViewById(R.id.layReload);
			View btn = this.findViewById(R.id.btnReload);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					reload();
				}
			});
		}
		if (m_reload != null) {
			m_reload.setVisibility(View.VISIBLE);
		}
	}

	public void hideReload() {
		if (m_reload != null) {
			m_reload.setVisibility(View.GONE);
		}
	}

	public void reQuest(String url, Map<String, String> map) {
		m_activity.showProgressDialog();
		XHttp.Get(url, map, this);
	}

	@Override
	public void onCancelled(CancelledException arg0) {
		// TODO Auto-generated method stub
		m_activity.stopProgressDialog();
	}

	@Override
	public void onError(Throwable arg0, boolean arg1) {
		// TODO Auto-generated method stub
		m_activity.stopProgressDialog();
	}

	@Override
	public void onFinished() {
		// TODO Auto-generated method stub
		m_activity.stopProgressDialog();
	}

	@Override
	public void onSuccess(JSONArray arg0) {
		// TODO Auto-generated method stub
		m_activity.stopProgressDialog();
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		m_activity=null;
	}
}
