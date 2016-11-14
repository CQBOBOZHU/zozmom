package com.zozmom.ui.adapter;

import java.lang.reflect.Field;
import java.util.List;

import com.zozmom.ui.fragment.BaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {
//public class ImagePagerAdapter extends FragmentPagerAdapter {
	List<BaseFragment> list;
	FragmentManager fm;

	public ImagePagerAdapter(FragmentManager fm, List<BaseFragment> list) {
		super(fm);
		this.fm = fm;
		this.list = list;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
//		super.destroyItem(container, position, object);
	}
}
