package com.zozmom.ui.adapter;

import java.util.List;

import com.zozmom.model.SplashModel;
import com.zozmom.ui.fragment.ModelPagerFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class ModelPagerAdapter extends FragmentPagerAdapter {

	List<SplashModel> mData;

	public ModelPagerAdapter(FragmentManager fm, List<SplashModel> mData) {
		super(fm);
		this.mData = mData;
	}

	@Override
	public Fragment getItem(int arg0) {
		ModelPagerFragment fragment = new ModelPagerFragment();
		fragment.setModel(mData.get(arg0));
		return fragment;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ModelPagerFragment tifrgment = (ModelPagerFragment) super
				.instantiateItem(container, position);
		SplashModel model = mData.get(position);
		tifrgment.setModel(model);
		return tifrgment;
	}
}
