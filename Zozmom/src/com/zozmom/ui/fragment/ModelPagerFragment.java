package com.zozmom.ui.fragment;

import com.chasedream.zhumeng.R;
import com.zozmom.model.SplashModel;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ModelPagerFragment extends BaseTaskFragment {
	SplashModel model;

	public SplashModel getModel() {
		return model;
	}

	public void setModel(SplashModel model) {
		this.model = model;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.activity_titleimage, container, false);
		ImageView imageView = (ImageView) view.findViewById(R.id.image1);
		loadImage(model.getHeadurl(), imageView);
		return view;
	}
}
