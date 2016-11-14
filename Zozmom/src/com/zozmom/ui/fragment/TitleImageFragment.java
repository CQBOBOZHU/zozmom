package com.zozmom.ui.fragment;

import java.lang.reflect.Field;

import com.chasedream.zhumeng.R;
import com.zozmom.ui.IndexActivity;
import com.zozmom.ui.user.FindWebActivity;
import com.zozmom.util.ToastUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 滚动图fragement
 * 
 * @author Administrator
 * 
 */
public class TitleImageFragment extends BaseTaskFragment {
	int ImageId;
	private static final String dreamurl="http://weixin.zozmom.com/hd/dream.html";
	public int getImageId() {
		return ImageId;
	}

	public void setImageId(int imageId) {
		ImageId = imageId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.activity_titleimage, container, false);
		ImageView imageView = (ImageView) view.findViewById(R.id.image1);
//		Bitmap bitmap=BitmapFactory.decodeStream(getResources().openRawResource(ImageId));
//		imageView.setImageBitmap(bitmap);
		imageView.setImageResource(ImageId);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				ToastUtil.show(getActivity(), "图片ID :" + ImageId);
					IndexActivity.indexActivity.changeCunrrentItem();
			}
		});
		return view;
	}

}
