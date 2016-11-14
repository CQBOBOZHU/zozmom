package com.zozmom.ui.adapter;

import java.util.List;

import com.chasedream.zhumeng.R;
import com.zozmom.model.DeGoodModel;
import com.zozmom.model.LuckModel;
import com.zozmom.ui.adapter.AlistViewAdapter.HolderView;
import com.zozmom.util.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BlistViewAdapter extends BaseTaskAdaper<LuckModel> {

	public BlistViewAdapter(Context context, List<LuckModel> mDatas) {
		super(context, mDatas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listview_b_item, null);
			holderView.timeView = (TextView) convertView
					.findViewById(R.id.time_text);
			holderView.nameView = (TextView) convertView
					.findViewById(R.id.name_text);
			convertView.setTag(holderView);
			holderView.lunckNumView=(TextView) convertView.findViewById(R.id.lunckNumber_text);
		} else {
			holderView = (HolderView) convertView.getTag();
		}

		if (position == 0) {
			holderView.timeView.setText("逐梦时间");
			holderView.nameView.setText("用户昵称");
			holderView.lunckNumView.setText("幸运数字");
		} else {
			LuckModel model = mDatas.get(position);
			long time = model.getJoinTime();
			String username = model.getUserName();
			int luckname = model.getRandomNum();
			String data=Util.GetDataTime2(time);
			holderView.timeView.setText(data);
			holderView.nameView.setText(username);
			holderView.lunckNumView.setText(String.valueOf(luckname));
		}
		return convertView;
	}

	class HolderView {
		TextView timeView;
		TextView nameView;
		TextView lunckNumView;
	}
}
