package com.zozmom.cuview;


import com.chasedream.zhumeng.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 图片和文字的控件
 * 
 * @author Administrator
 * 
 */
public class ImageTextView extends RelativeLayout {
	TextView textView;
	ImageView imageView;

	public ImageTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.image_text, this);
		textView = (TextView) view.findViewById(R.id.textview);
		imageView = (ImageView) view.findViewById(R.id.imagview);
	}

	public void setText(String text) {
		textView.setText(text);
	}

	public void setImgage(Drawable drawble) {
		imageView.setImageDrawable(drawble);
	}

	public void select(Drawable drawble) {
		textView.setTextColor(getResources().getColor(R.color.background));
		imageView.setImageDrawable(drawble);
	}

	public void notselect(Drawable drawble) {
		textView.setTextColor(getResources().getColor(
				R.color.good_message_textcolors));
		imageView.setImageDrawable(drawble);
	}
}
