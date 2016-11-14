package com.zozmom.cuview;


import com.chasedream.zhumeng.R;
import com.chasedream.zhumeng.R.color;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 图片和文字的控件 文字在上 图在下
 * 
 * @author Administrator
 * 
 */
public class TextImageView extends RelativeLayout {
	TextView textView;
	ImageView imageView;

	public TextImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TextImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.imge_text_bm, this);
		textView = (TextView) findViewById(R.id.textview);
		imageView = (ImageView) findViewById(R.id.imageview);
		this.setClickable(true);
		this.setFocusable(true);
	}

	public void setT(String text, int color) {
		textView.setText(text);
		textView.setTextColor(color);
	}

	public void setImgage(Drawable drawble) {
		imageView.setImageDrawable(drawble);
	}

}
