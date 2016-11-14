package com.zozmom.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.chasedream.zhumeng.R;
import com.zozmom.cuview.CuListView;
import com.zozmom.model.BaseModel;
import com.zozmom.util.ToastUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GoodsListAdapter extends BaseAdapter {
	List<String> mlist;
	LayoutInflater mInflater;
	Context mContext;
	HolderView holderView;

	public GoodsListAdapter(List<String> mlist, Context mContext) {
		this.mlist = mlist;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			holderView = new HolderView();
			convertView = mInflater.inflate(R.layout.good_listitem, null);
			convertView.setTag(holderView);
			holderView.layout = (RelativeLayout) convertView
					.findViewById(R.id.relayout);
			holderView.listView = (ListView) convertView
					.findViewById(R.id.good_item_listview);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		holderView.layout.setTag(position);
		holderView.listView.setTag(position);
		holderView.layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.getTag();
				ToastUtil.show(mContext, v.getTag() + "");
				holderView.listView.setVisibility(View.VISIBLE);
				init();
				if (list.size() != 0) {
					holderView.listView.measure(40 * list.size(),
							50 * list.size());
				}
				listAdapter adapter = new listAdapter();
				holderView.listView.setAdapter(adapter);
			}
		});
		return convertView;
	}

	class HolderView {
		ListView listView;
		RelativeLayout layout;
	}

	List<String> list = new ArrayList<String>();

	public void init() {
		for (int i = 0; i < 10; i++) {
			list.add((String) ("这是第" + i + ""));
		}
	}

	class listAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = mInflater.inflate(R.layout.list_item, null);
			TextView textView = (TextView) convertView
					.findViewById(R.id.item_id);
			textView.setText(list.get(position) + "");
			Log.v("this", "convertView " + position);
			return convertView;
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
