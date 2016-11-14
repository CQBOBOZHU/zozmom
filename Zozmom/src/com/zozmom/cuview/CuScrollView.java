package com.zozmom.cuview;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class CuScrollView extends PullToRefreshScrollView {

	public CuScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}

	public CuScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CuScrollView(Context context) {
		super(context);
	}

	private ScrollViewListener scrollViewListener = null;

	public void setOnScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	public void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this.getRefreshableView(), x, y, oldx, oldy);
		}
	}
}
