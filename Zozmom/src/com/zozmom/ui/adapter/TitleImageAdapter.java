package com.zozmom.ui.adapter;


import com.zozmom.ui.fragment.TitleImageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * 首页图片滚动适配
 * 
 * @author Administrator
 * 
 */
public class TitleImageAdapter extends FragmentPagerAdapter {

	public TitleImageAdapter(FragmentManager fm) {
		super(fm);
	}

	public TitleImageAdapter(FragmentManager fm, int[] image) {
		super(fm);
		this.image = image;
	}
	
	public int[] getImage() {
		return image;
	}

	public void setImage(int[] image) {
		this.image = image;
	}

	int[] image;

	@Override
	public Fragment getItem(int arg0) {
		TitleImageFragment title = new TitleImageFragment();
		title.setImageId(image[arg0]);
		return title;
	}

	@Override
	public int getCount() {
		return image.length;

	}
	
	@Override    
	public int getItemPosition(Object object) {    
	    return POSITION_NONE;    
	}  

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		TitleImageFragment tifrgment = (TitleImageFragment) super
				.instantiateItem(container, position);
		int title = image[position];
		tifrgment.setImageId(title);
		return tifrgment;
	}
}
