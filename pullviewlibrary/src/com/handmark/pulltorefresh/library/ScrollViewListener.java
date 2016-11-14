package com.handmark.pulltorefresh.library;

import android.widget.ScrollView;

public interface ScrollViewListener {

	void onScrollChanged(ScrollView scrollView, int x, int y,
			int oldx, int oldy);

}