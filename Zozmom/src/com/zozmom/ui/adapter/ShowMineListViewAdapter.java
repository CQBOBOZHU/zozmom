package com.zozmom.ui.adapter;

import java.util.List;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuGridView;
import com.zozmom.model.ShowModel;
import com.zozmom.pic.PhotoLoader;
import com.zozmom.ui.GoodMessageActivity;
import com.zozmom.ui.SubmitShowUpdataActivity;
import com.zozmom.ui.user.OtherUserCenterActivity;
import com.zozmom.ui.user.UserShowActivity;
import com.zozmom.util.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 我的晒单
 * 
 * @author Administrator
 * 
 */
public class ShowMineListViewAdapter extends BaseTaskAdaper<ShowModel> {
	PhotoLoader loader;

	public ShowMineListViewAdapter(Context context, List<ShowModel> mDatas) {
		super(context, mDatas);
		loader = new PhotoLoader(mContext);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.open_list_item, null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.openlist_userface_image);
			holder.userNameText = (TextView) convertView
					.findViewById(R.id.openlist_userName_text);
			holder.showTimeText = (TextView) convertView
					.findViewById(R.id.openlist_time_text);
			holder.showContentText = (TextView) convertView
					.findViewById(R.id.openlist_content_text);
			holder.iwantBtn = (Button) convertView
					.findViewById(R.id.show_iwantbtn);
			holder.stutaImageView = (ImageView) convertView
					.findViewById(R.id.user_show_status_iamgeview);
			holder.goodTextView=(TextView) convertView.findViewById(R.id.good_name);
			holder.issueIdView=(TextView) convertView.findViewById(R.id.good_number);
			holder.showImage1 = (ImageView) convertView
					.findViewById(R.id.openlist_image1);
			holder.showImage2 = (ImageView) convertView
					.findViewById(R.id.openlist_image2);
			holder.showImage3 = (ImageView) convertView
					.findViewById(R.id.openlist_image3);
			holder.showImage4 = (ImageView) convertView
					.findViewById(R.id.openlist_image4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imageView.setTag(position);
		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int tag = (int) v.getTag();
				ShowModel showModel = mDatas.get(tag);
				int userId = showModel.getUserId();
				Intent intent = new Intent(mContext,
						OtherUserCenterActivity.class);
				intent.putExtra("userId", userId);
				mContext.startActivity(intent);
			}
		});
		ShowModel model = mDatas.get(position);
		String userface = model.getHeadUrl();
		Log.v("this", "this" + Constant.FACE_URL + userface + Constant.FACE_KEY);
		if (userface.contains("http://")) {
			loadImage(userface, holder.imageView);
		} else
			loadImage(Constant.FACE_URL + userface + Constant.FACE_KEY,
					holder.imageView);
		holder.userNameText.setText(model.getUserName());
		String content = model.getComment();
		content = content.replace("<p>", "").replace("</p>", "");
		holder.showContentText.setText(content);
		long time = model.getCreateTime();
		String title=model.getProductTitle();
		String gdname=model.getProductName();
		String issueId=model.getIssueId();
		String data = Util.GetDataTime1(time);
		int status = model.getStatus();
		int productStatus=model.getProductStatus();
		holder.goodTextView.setText(gdname+title);
		holder.issueIdView.setText("期号: "+issueId);
		holder.stutaImageView.setVisibility(View.VISIBLE);
		if(productStatus==0){
			holder.iwantBtn.setVisibility(View.GONE);
		}else{
			holder.iwantBtn.setVisibility(View.VISIBLE);
		}
		if (status == 0) {
			holder.stutaImageView.setImageResource(R.drawable.show_ing);
			holder.iwantBtn.setText("再买一次");
		} else if (status == 1) {
			holder.stutaImageView.setImageResource(R.drawable.show_pass);
			holder.iwantBtn.setText("再买一次");
		} else if (status == 2) {
			holder.stutaImageView.setImageResource(R.drawable.show_failed);
			holder.iwantBtn.setText("重新提交");
		}

		holder.showTimeText.setText(data);
		String shareimageString = model.getShareImg();
		String[] images = shareimageString.split(",");
		if (images.length == 1) {
			String image = images[0];
			String url1 = Constant.SHARE_URL + image + Constant.SHARE_KEY;
			loadImage(url1, holder.showImage1);
		} else if (images.length == 2) {
			String image = images[0];
			String image1 = images[1];
			String url1 = Constant.SHARE_URL + image + Constant.SHARE_KEY;
			String url2 = Constant.SHARE_URL + image1 + Constant.SHARE_KEY;
			holder.showImage2.setVisibility(View.VISIBLE);
			holder.showImage3.setVisibility(View.GONE);
			holder.showImage4.setVisibility(View.GONE);
			loadImage(url1, holder.showImage1);
			loadImage(url2, holder.showImage2);
		} else if (images.length == 3) {
			String image = images[0];
			String image1 = images[1];
			String image2 = images[2];
			String url1 = Constant.SHARE_URL + image + Constant.SHARE_KEY;
			String url2 = Constant.SHARE_URL + image1 + Constant.SHARE_KEY;
			String url3 = Constant.SHARE_URL + image2 + Constant.SHARE_KEY;
			loadImage(url1, holder.showImage1);
			loadImage(url2, holder.showImage2);
			loadImage(url3, holder.showImage3);
			holder.showImage2.setVisibility(View.VISIBLE);
			holder.showImage3.setVisibility(View.VISIBLE);
			holder.showImage4.setVisibility(View.GONE);
		} else if (images.length > 4 || images.length == 4) {
			String image = images[0];
			String image1 = images[1];
			String image2 = images[2];
			String image3 = images[3];
			String url1 = Constant.SHARE_URL + image + Constant.SHARE_KEY;
			String url2 = Constant.SHARE_URL + image1 + Constant.SHARE_KEY;
			String url3 = Constant.SHARE_URL + image2 + Constant.SHARE_KEY;
			String url4 = Constant.SHARE_URL + image3 + Constant.SHARE_KEY;
			loadImage(url1, holder.showImage1);
			loadImage(url2, holder.showImage2);
			loadImage(url3, holder.showImage3);
			loadImage(url4, holder.showImage4);
			holder.showImage2.setVisibility(View.VISIBLE);
			holder.showImage3.setVisibility(View.VISIBLE);
			holder.showImage4.setVisibility(View.VISIBLE);
		}
		holder.iwantBtn.setTag(position);
		holder.iwantBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int tag = (int) v.getTag();
				ShowModel showModel = mDatas.get(tag);
				int status = showModel.getStatus();
				Intent intent;
				if (status == 2) {
					intent = new Intent(mContext,
							SubmitShowUpdataActivity.class);
					intent.putExtra("showmodel", showModel);
					((UserShowActivity) mContext).startActivityForResult(
							intent, 1);
				} else {
					int productId = Integer.parseInt(showModel.getProductId());
					intent = new Intent(mContext, GoodMessageActivity.class);
					intent.putExtra("productId", productId);
					mContext.startActivity(intent);
				}
			}
		});
		return convertView;
	}
	class ViewHolder {
		ImageView imageView,stutaImageView,showImage1, showImage2, showImage3, showImage4;
		TextView userNameText,showTimeText,goodTextView,issueIdView,showContentText;
		Button iwantBtn;
	}


}
