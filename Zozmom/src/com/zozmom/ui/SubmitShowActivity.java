//package com.zozmom.ui;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.regex.PatternSyntaxException;
//
//import org.json.JSONObject;
//import org.xutils.x;
//import org.xutils.http.RequestParams;
//import org.xutils.view.annotation.ContentView;
//import org.xutils.view.annotation.ViewInject;
//
//import com.alibaba.sdk.android.oss.ClientException;
//import com.alibaba.sdk.android.oss.OSSClient;
//import com.alibaba.sdk.android.oss.ServiceException;
//import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
//import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
//import com.alibaba.sdk.android.oss.model.PutObjectRequest;
//import com.alibaba.sdk.android.oss.model.PutObjectResult;
//import com.chasedream.zhumeng.R;
//import com.zozmom.constants.Constant;
//import com.zozmom.cuview.CuGridView;
//import com.zozmom.model.ShowModel;
//import com.zozmom.model.UserInfoModel;
//import com.zozmom.net.oss.OSSClicentUtil;
//import com.zozmom.net.oss.OSSConstant;
//import com.zozmom.ui.adapter.BaseTaskAdaper;
//import com.zozmom.util.RequestErrorUtil;
//import com.zozmom.util.ToastUtil;
//import com.zozmom.util.Tools;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AbsListView;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.ImageView.ScaleType;
//
///**
// * 提交晒单 And 重新提交晒单
// * 
// * @author Administrator
// * 
// */
//@ContentView(R.layout.submit_show_activity)
//public class SubmitShowActivity extends BaseTaskActivity<JSONObject> {
//	@ViewInject(R.id.status_bar_title)
//	TextView titleview;
//	@ViewInject(R.id.submit_show_edittext)
//	EditText editText;
//	@ViewInject(R.id.submit_show_imageview1)
//	ImageView imageView1;
//	@ViewInject(R.id.submit_show_imageview2)
//	ImageView imageView2;
//	@ViewInject(R.id.submit_show_imageview3)
//	ImageView imageView3;
//	@ViewInject(R.id.submit_show_imageview4)
//	ImageView imageView4;
//	@ViewInject(R.id.submit_show_gridview)
//	CuGridView gridView;
//	@ViewInject(R.id.max_textview)
//	TextView maxHintView;
//	List<String> selectpictrue = new ArrayList<String>();
//	GridViewAdapter adapter;
//	int width;
//	int height;
//	UserInfoModel infoModel;
//	private int userId;
//	private String comment;
//	private int lotteryId;
//	ShowModel showModel;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		x.view().inject(this);
//		infoModel = getLogginUserinfo();
//		userId = infoModel.getUserId();
//		showModel = (ShowModel) getIntent().getSerializableExtra("showmodel");
//		lotteryId = getIntent().getIntExtra("id", 0);
//		DisplayMetrics metric = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metric);
//		width = metric.widthPixels; // 屏幕宽度（像素）
//		height = metric.heightPixels; // 屏幕高度（像素）
//		selectpictrue.add("theone");
//		titleview.setText("提交晒单");
//		// String sAgeFormat=getRString(R.string.viewpager_indicator);
//		// String text=String.format(sAgeFormat, "0","100");
//		maxHintView.setText(getString(R.string.viewpager_indicator, 0, 100));
//		editText.addTextChangedListener(watcher);
//		adapter = new GridViewAdapter(this, selectpictrue);
//		gridView.setAdapter(adapter);
//		gridView.setOnItemClickListener(itemClickListener);
//	}
//	public static String stringFilter(String str) throws PatternSyntaxException {
//		// 只允许字母、数字和汉字
//		String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
//		Pattern p = Pattern.compile(regEx);
//		Matcher m = p.matcher(str);
//		return m.replaceAll("").trim();
//	}
//	TextWatcher watcher = new TextWatcher() {
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before,
//				int count) {
//			String editable = editText.getText().toString();
//			String str = stringFilter(editable.toString());
//			if (!editable.equals(str)) {
//				editText.setText(str);
//				// 设置新的光标所在位置
//				editText.setSelection(str.length());
//			}
//		}
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//		}
//
//		@Override
//		public void afterTextChanged(Editable s) {
//			maxHintView.setText(getString(R.string.viewpager_indicator, s
//					.toString().length(), 100));
//			// String str = stringFilter(s.toString());
//		}
//	};
//
//
//	OnItemClickListener itemClickListener = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			if (position == 0) { // 点击图片位置为+ 0对应0张图片
//				if (selectpictrue != null && selectpictrue.size() >= 5) {
//					ToastUtil.show(m_activity, "最多提交4张");
//					return;
//				}
//				Intent intent = new Intent(SubmitShowActivity.this,
//						SelectPictureActivity.class);
//				startActivityForResult(intent, 0);
//			}
//			if (position > 0) {
//				dialog(position);
//			}
//		}
//	};
//
//	/**
//	 * Dialog对话框提示用户删除操作 position为删除图片位置
//	 */
//	protected void dialog(final int position) {
//		AlertDialog.Builder builder = new Builder(this);
//		builder.setMessage("是否删除该图片");
//		builder.setTitle("提示");
//
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				selectpictrue.remove(position);
//				adapter.setmDatas(selectpictrue);
//				adapter.notifyDataSetChanged();
//			}
//		});
//		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}
//
//	@Override
//	public void onClick(View v) {
//		super.onClick(v);
//		switch (v.getId()) {
//			case R.id.status_bar_Exit :
//				finish();
//				break;
//			case R.id.submit_show_photo_btn :
//				// Intent intent = new Intent(this,
//				// SelectPictureActivity.class);
//				// startActivity(intent);
//				if (posttime < 4) {
//					startCamera(false);
//				} else {
//					ToastUtil.show(this, "最多4张");
//				}
//				break;
//			default :
//				break;
//		}
//	}
//	// PhotoSelector m_photoSelector = null;
//	// @SuppressWarnings("unchecked")
//	@Override
//	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//		super.onActivityResult(arg0, arg1, arg2);
//		// List<String> list = new ArrayList<String>();
//		// list = (ArrayList<String>) arg2
//		// .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
//		// selectpictrue.addAll(list);
//		// adapter.setmDatas(selectpictrue);
//		// adapter.notifyDataSetChanged();
//		// for (String string : selectpictrue) {
//		// Log.v("this", " string" + string);
//		// }
//		// m_photoSelector=new PhotoSelector(m_activity);
//		// if(arg1==PhotoConstant.FROM_CAMERA){
//		String temp = m_photoSelector.getTempPhotoPath();
//		if (temp != null && temp.length() > 0) {
//			Log.v("this", "temp " + temp);
//			SubmitPic(temp);
//		}
//	}
//
//	class GridViewAdapter extends BaseTaskAdaper<String> {
//		LayoutParams params = new AbsListView.LayoutParams(width / 5, width / 5);
//
//		public GridViewAdapter(Context context, List<String> mDatas) {
//			super(context, mDatas);
//		}
//
//		@SuppressLint("InflateParams")
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			holderView holderview;
//			if (convertView == null) {
//				holderview = new holderView();
//				convertView = LayoutInflater.from(mContext).inflate(
//						R.layout.submit_griditem, null);
//				holderview.imageView = (ImageView) convertView
//						.findViewById(R.id.album_image);
//				convertView.setTag(holderview);
//			} else {
//				holderview = (holderView) convertView.getTag();
//			}
//			holderview.imageView.setScaleType(ScaleType.CENTER_CROP);
//			holderview.imageView.setLayoutParams(params);
//
//			if (position == 0) {
//				Bitmap addbmp = BitmapFactory.decodeResource(getResources(),
//						R.drawable.photo);
//				holderview.imageView.setImageBitmap(addbmp);
//			} else {
//				loadimage(holderview.imageView,
//						"file://" + mDatas.get(position));
//			}
//			return convertView;
//		}
//		class holderView {
//			ImageView imageView;
//		}
//	}
//
//	public void SubmitPic() {
//		for (String str : selectpictrue) {
//			if (str.equals("theone")) {
//				return;
//			}
//			SubmitPic(str);
//		}
//	}
//	String shareurl;
//	int posttime = 0;
//	public void SubmitPic(final String file) {
//		showProgressDialog();
//		File photo = new File(file);
//		final String testObject = Tools.getRandomString() + ".png";// 随机名字
//		OSSClient oss = OSSClicentUtil.instanceFaceOss(this);
//		PutObjectRequest put = new PutObjectRequest(
//				OSSConstant.share_bucketName, userId + "/" + testObject,
//				photo.getPath());
//		shareurl = userId + "/" + testObject;
//		@SuppressWarnings({"rawtypes", "unused"})
//		OSSAsyncTask task = oss.asyncPutObject(put,
//				new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//
//					@Override
//					public void onFailure(PutObjectRequest arg0,
//							ClientException arg1, ServiceException arg2) {
//
//					}
//
//					@Override
//					public void onSuccess(PutObjectRequest arg0,
//							PutObjectResult arg1) {
//						Log.v("this", "onSuccess:" + shareurl);
//						stopProgressDialog();
//						posttime++;
//						if (temp.length() == 0) {
//							temp = shareurl;
//						} else {
//							temp = temp + "," + shareurl;
//						}
//						switch (posttime) {
//							case 1 :
//								loadimage(imageView1, file);
//								break;
//							case 2 :
//								loadimage(imageView2, file);
//								break;
//							case 3 :
//								loadimage(imageView3, file);
//								break;
//							case 4 :
//								loadimage(imageView4, file);
//								break;
//
//							default :
//								break;
//						}
//						// request();
//					}
//				});
//	}
//	String temp = "";
//	/**
//	 * 提交晒单
//	 */
//	public void request() {
//
//		RequestParams params = new RequestParams(Constant.SUBMIT_SHOW);
//		long time = System.currentTimeMillis();
//		params.addHeader("token-param", time + "," + infoModel.getToken() + ","
//				+ infoModel.getUserId());
//		params.addParameter("comment", comment);
//		params.addParameter("shareImg", temp);
//		params.addParameter("lotteryId", lotteryId);
//		x.http().post(params, new CommonCallback<Boolean>() {
//
//			@Override
//			public void onCancelled(CancelledException arg0) {
//
//			}
//
//			@Override
//			public void onError(Throwable arg0, boolean arg1) {
//				RequestErrorUtil.judge(arg0, m_activity);
//			}
//
//			@Override
//			public void onFinished() {
//
//			}
//
//			@Override
//			public void onSuccess(Boolean arg0) {
//				Log.v("this", arg0 + "");
//				if (arg0) {
//					ToastUtil.show(m_activity, "上传成功");
//				}
//			}
//		});
//
//	}
//	/**
//	 * 取消
//	 * 
//	 * @param view
//	 */
//	public void onCancel(View view) {
//		finish();
//	}
//	/**
//	 * 保存
//	 * 
//	 * @param view
//	 */
//	public void onSave(View view) {
//		comment = editText.getText().toString();
//		if (comment == null || comment.trim().length() == 0) {
//			ToastUtil.show(this, "内容不能为空");
//			return;
//		}
//		if (posttime < 1) {
//			ToastUtil.show(this, "至少晒一张图");
//			return;
//		}
//		request();
//	}
//}
