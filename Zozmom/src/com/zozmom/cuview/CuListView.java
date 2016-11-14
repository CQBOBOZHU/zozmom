package com.zozmom.cuview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义listview 防止与scrollview冲突
 * 
 * @author 飞越
 * 
 */
public class CuListView extends ListView {
	public CuListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CuListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CuListView(Context context, AttributeSet attrs, int arg2) {
		super(context, attrs, arg2);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
