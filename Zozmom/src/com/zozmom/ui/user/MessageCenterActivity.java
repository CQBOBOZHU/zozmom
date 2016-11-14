package com.zozmom.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.adapter.BaseTaskAdaper;

/**
 * 消息中心
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.message_center_activity)
public class MessageCenterActivity extends BaseTaskActivity<String> {
	@ViewInject(R.id.message_center_listview)
	PullToRefreshListView listView;
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	@ViewInject(R.id.status_bar_Exit)
	Button exitBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		titleView.setText("消息中心");
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("1");
		list.add("1");
		list.add("1");
		listView.setAdapter(new ListAdapter(this, list));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;

			default :
				break;
		}
	}

	class ListAdapter extends BaseTaskAdaper<String> {

		public ListAdapter(Context context, List<String> mDatas) {
			super(context, mDatas);
		}

		@SuppressLint({ "ViewHolder", "InflateParams" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(
					R.layout.message_center_listview_item, null);
			return convertView;
		}

	}
}
