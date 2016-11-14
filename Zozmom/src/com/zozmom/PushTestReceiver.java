package com.zozmom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chasedream.zhumeng.R;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.zozmom.cuview.BarrageView;
import com.zozmom.manager.AccountManager;
import com.zozmom.manager.ActiviyManager;
import com.zozmom.ui.IndexActivity;
import com.zozmom.ui.user.McActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Layout;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PushTestReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
		Log.v("this", bundle.getInt(PushConsts.CMD_ACTION) + "");
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {

			case PushConsts.GET_CLIENTID :
				String cid = bundle.getString("clientid");
				// CIDUtil.setTaskid(cid);
				// context.getResources().getDrawable(R.drawable.push);
				Log.v("this", "Got ClientID:" + cid);
				break;
			case PushConsts.GET_MSG_DATA :
				byte[] payload = bundle.getByteArray("payload");
				String taskid = bundle.getString("taskid");
				String messageid = bundle.getString("messageid");
				Log.v("this", "taskid:" + taskid);
				Log.v("this", "messageid:" + messageid);
				// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
				// boolean result =
				// PushManager.getInstance().sendFeedbackMessage(context,
				// taskid, messageid, 90001);
				// System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));
				if (payload != null) {
					String data = new String(payload);
					Log.v("this", "messageid:" + data);
					showBarrage(data);
					// show(text, context)
					// Intent intentTemp = new Intent(
					// context.getApplicationContext(), MainActivity.class);
					// intentTemp.putExtra("data", data);
					// intentTemp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					// | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					// context.getApplicationContext().startActivity(intentTemp);
				}
				break;
		}
	}

	public void showNotifacation(Context myContext) {
		// NotificationManager manager=System.get

		NotificationManager mNotificationManager = (NotificationManager) myContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		long when = System.currentTimeMillis();
		Notification notification = new Notification(R.id.app_logo,
				"zhe shi yige demo", when);
		/*
		 * Notification.FLAG_AUTO_CANCEL ，该标志表示当用户点击 Clear 之后，能够清除该通知
		 */
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		// notification.flags = Notification.FLAG_NO_CLEAR; //永久存在，不能删除

		notification.defaults = Notification.DEFAULT_SOUND; // 声音
		notification.defaults = Notification.DEFAULT_LIGHTS; // 灯光
		notification.defaults = Notification.DEFAULT_VIBRATE; // 震动

		/*
		 * 创建点击事件开启Activity对象
		 * 
		 * 如果要以该Intent启动一个Activity，一定要设置 Intent.FLAG_ACTIVITY_NEW_TASK 标记
		 * Intent.FLAG_ACTIVITY_CLEAR_TOP ：如果在当前Task中，有要启动的Activity，
		 * 那么把该Acitivity之前的所有Activity都关掉，并把此Activity置前以避免创建Activity的实例
		 */
		Intent notificationIntent = new Intent(myContext, IndexActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		/*
		 * 点击启动Activity 和startActivity()同含义 表示如果该描述的PendingIntent已存在，
		 * 则改变已存在的PendingIntent的Extra数据为新的PendingIntent的Extra数据。
		 */
		// PendingIntent contentIntent = PendingIntent.getActivity(myContext, 0,
		// notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		// notification.setLatestEventInfo(myContext, contentTitle, contentText,
		// contentIntent);

		/*
		 * 用mNotificationManager的notify方法通知用户生成标题栏消息通知
		 */
		mNotificationManager.notify(R.string.app_name, notification);
	}
	private Random random = new Random();

	private DisplayMetrics dm;

	int mWidth;
	int mHight;
	/**
	 * 弹幕
	 */
	public void showBarrage(final String text) {
		// final Activity activity = ActiviyManager.instance().getTopActivity();
		Activity activity = IndexActivity.indexActivity;
		if (activity == null) {
			return;
		}
		if (!IndexActivity.indexActivity.isItemOne())
			return;
		dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mWidth = dm.widthPixels;
		mHight = dm.heightPixels;
		// // 设置宽高全屏
		// final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
		// ViewGroup.LayoutParams.MATCH_PARENT,
		// ViewGroup.LayoutParams.MATCH_PARENT);
		// // String[] str=new String[50];
		// final Handler handler = new Handler();
		// Runnable createBarrageView = new Runnable() {
		// @Override
		// public void run() {
		// Log.e("this", "发送弹幕");
		// // 新建一条弹幕，并设置文字
		// BarrageView barrageView = new BarrageView(activity);
		// barrageView.setText(text); // 随机设置文字
		// // barrageView.setBackgroundResource(R.drawable.barree);
		// activity.addContentView(barrageView, lp);
		// }
		// };
		// handler.post(createBarrageView);
		show(text, activity);
	}
	// 两两弹幕之间的间隔时间
	public static final int DELAY_TIME = 500;

	public void showAnimate(String text) {
		final Activity activity = ActiviyManager.instance().getTopActivity();
		if (activity == null) {
			return;
		}
		TextView view = new TextView(activity);
		view.setText(text);
		view.setPadding(0, random.nextInt(mHight-100), 0, 0);
		view.setBackgroundResource(R.drawable.abr_three);
		AnimationSet animationSet = new AnimationSet(true);
		int width = view.getWidth();
		TranslateAnimation translateAnimation = new TranslateAnimation(width
				+ this.mWidth, -width, 0, 0);
		translateAnimation.setDuration(8000);
		animationSet.addAnimation(translateAnimation);
		view.startAnimation(animationSet);
	}
	AlertDialog ctdialog;
	/**
	 * 实现梦想
	 */
	public void showComeTrueDialog(Context context) {
		AlertDialog.Builder builder = new Builder(context);
		ctdialog = builder.create();
		Window window = ctdialog.getWindow();
		window.setContentView(R.layout.cometruedialog);
	}

	TextView view;
	View layout;
	public void show(String text, Context context) {
		view = new TextView(context);
		// String[] t=text.split("获得");
		if (text.contains("获得") && text.contains("商品") && text.contains("恭喜")
				&& text.contains("用户")) {
			int start1 = text.indexOf("恭喜") + "恭喜".length();
			int end1 = text.indexOf("用户");
			String username = text.substring(start1, end1);
			int start = text.indexOf("获得") + "获得".length();
			int end = text.indexOf("商品");
			String goodname;
			String html1;
			if (end > 25) {
				goodname = text.substring(start, 25);
				html1 = "<b><font color=\"#ffffff\">恭喜</b><b><font color=\"#00BFFF\">"
						+ username
						+ "</b><b><font color=\"#ffffff\">用户获得</b><b><font color=\"#FFF727\">"
						+ goodname
						+ "..."
						+ "</b><b><font color=\"#FFFFFF\"></b>";
			} else {
				goodname = text.substring(start, end);
				html1 = "<b><font color=\"#ffffff\">恭喜</b><b><font color=\"#00BFFF\">"
						+ username
						+ "</b><b><font color=\"#ffffff\">用户获得</b><b><font color=\"#FFF727\">"
						+ goodname + "</b><b><font color=\"#FFFFFF\">商品</b>";
			}
			view.setMaxLines(1);
			view.setText(Html.fromHtml(html1));
		} else {
			view.setMaxLines(1);
			view.setText(text);
		}
		view.setGravity(Gravity.CENTER);
		view.setBackgroundResource(R.drawable.abr_three);
		layout = ((Activity) context).findViewById(R.id.index_layout_re);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 100);
		params.setMargins(0, random.nextInt(mHight - 100), 0, 0);
		view.setLayoutParams(params);
		view.setVisibility(View.INVISIBLE);
		((ViewGroup) layout).addView(view);
		final AnimationSet animationSet = new AnimationSet(true);
		view.post(new Runnable() {
			@Override
			public void run() {
				int width = view.getWidth(); // height is ready
				Log.v("this", "width" + width);
				extracted(animationSet, width, view);
			}

		});
	}
	private void extracted(final AnimationSet animationSet, int width,
			final View mview) {
		TranslateAnimation translateAnimation = new TranslateAnimation(mWidth,
				-width, 0, 0);
		translateAnimation.setDuration(8000);
		animationSet.addAnimation(translateAnimation);
		mview.startAnimation(animationSet);
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mview.setVisibility(View.GONE);
			}
		});
	}
}
