package com.zozmom.ui.adapter;

import java.util.List;

import com.chasedream.zhumeng.R;
import com.zozmom.model.DeGoodModel;
import com.zozmom.model.LuckModel;
import com.zozmom.ui.fragment.FindFragment;
import com.zozmom.util.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AlistViewAdapter extends BaseTaskAdaper<LuckModel> {

	public AlistViewAdapter(Context context, List<LuckModel> mDatas) {
		super(context, mDatas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listview_a_item, null);
			holderView.timeView = (TextView) convertView
					.findViewById(R.id.time_text);
			holderView.nameView = (TextView) convertView
					.findViewById(R.id.name_text);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}

		if (position == 0) {
			holderView.timeView.setText("逐梦时间");
			holderView.nameView.setText("用户昵称");
		} else {
			LuckModel model = mDatas.get(position);
			long time = model.getJoinTime();
			String username = model.getUserName();
			String data = Util.GetDataTime2(time);
			String temp=data.replace(" ","").substring("yyyy/MM/dd".length()).replace(":", "");
			holderView.timeView.setText(data +"="+temp);
			holderView.nameView.setText(username);
		}
		return convertView;
	}

	class HolderView {
		TextView timeView;
		TextView nameView;
	}
}
