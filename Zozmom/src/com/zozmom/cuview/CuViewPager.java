package com.zozmom.cuview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
/**
 * 阻止viewpage滑动
 * @author Administrator
 *
 */
public class CuViewPager extends ViewPager {

	public CuViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item, false);
	}
}