package com.zozmom.util;

import com.zozmom.ui.BaseActivity;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCount extends CountDownTimer {

	Button view;
	Button v1;
	Button v2;
	Button v3;
	Button v4;
	Context context;

	public TimeCount(long millisInFuture, long countDownInterval, Button view) {
		super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		this.view = view;
	}

	public TimeCount(long millisInFuture, long countDownInterval, Button v1,
			Button v2, Button v3, Button v4, Context context) {
		super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
		this.context = context;
	}

	@Override
	public void onFinish() {// 计时完毕时触发
		if (view != null) {
			view.setText("重新验证");
			view.setClickable(true);
		}
		if (v1 != null && v2 != null) {
			((BaseActivity<?>) context).finish();
		}

	}

	long startmin = 0;

	@Override
	public void onTick(long millisUntilFinished) {// 计时过程显示
		if (view != null) {
			view.setClickable(false);
			view.setText(millisUntilFinished / 1000 + "秒");
		}
		if (v1 != null && v2 != null && v3 != null && v4 != null) {
			long time = millisUntilFinished / 1000;// 30:00 29:59
			long min = time / 60;// 分钟
			long s = time % 60;// 秒
			String min1 = Long.toString(min);
			String s1 = Long.toString(s);// 秒
			String str1 = "0";
			String str2 = "0";
			if (startmin != min) {
				if (min1.length() == 2) {
					str1 = min1.substring(0, 1);
					str2 = min1.substring(1, 2);
				} else {
					str2 = "0";
					str2 = min1;
				}
				v1.setText(str1);
				v2.setText(str2);
				startmin = min;
			}
			String str3 = "0";
			String str4 = "0";
			if (s1.length() == 2) {
				str3 = s1.substring(0, 1);
				str4 = s1.substring(1, 2);
			} else {
				str3 = "0";
				str4 = s1;
			}
			v3.setText(str3);
			v4.setText(str4);
		}
	}

}
