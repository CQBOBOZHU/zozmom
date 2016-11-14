package com.zozmom.ui.fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.HttpException;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zozmom.constants.Constant;
import com.zozmom.manager.AccountManager;
import com.zozmom.manager.ShareUrlManager;
import com.zozmom.model.ShareUrlModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseActivity;
import com.zozmom.ui.BaseWebActivity;
import com.zozmom.ui.LoginActivity;
import com.zozmom.ui.pay.RechargeActivity;
import com.zozmom.ui.user.FindWebActivity;
import com.zozmom.ui.user.GetNewUserHongbao;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;
import com.chasedream.zhumeng.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 发现fragment
 * 
 * @author Administrator
 * 
 */
public class FindFragment extends BaseTaskFragment {
	// Android6.0使用 Math.floor 代替 FloatMath.floor 即可；
	private static final String url = "http://weixin.zozmom.com/hd/luckyCircle.html?type=app";//大转盘
	private static final String boxurl="http://weixin.zozmom.com/hd/box.html?type=app";//开箱子
	private static final String dreamurl="http://weixin.zozmom.com/hd/dream.html";//全民逐梦
	View findview;
	View boxview;
	View dreamView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.find_fragment, container, false);
		TextView title = (TextView) view.findViewById(R.id.status_bar_title);
		title.setText(getResources().getString(R.string.all_goods));
		ImageButton exitbtn = (ImageButton) view
				.findViewById(R.id.status_bar_Exit);
		exitbtn.setVisibility(View.GONE);
		findview=view.findViewById(R.id.findviewlay);
		boxview=view.findViewById(R.id.boxuserlay);
		dreamView=view.findViewById(R.id.findviewlay_dream);
		
		boxview.setOnClickListener(Listener);
		findview.setOnClickListener(Listener);
		dreamView.setOnClickListener(Listener);
		return view;
	}

	UserInfoModel userinfo;
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		userinfo = AccountManager.getInstance().getLogginUserInfo();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("this", "onResume");
		userinfo=AccountManager.getInstance().getCurrentUserInfo();
	}

	public void request() {
		if(userinfo==null){
			ToastUtil.show(m_activity, "亲~先登录哦");
			Intent intent=new Intent(m_activity,LoginActivity.class);
			m_activity.startActivityForResult(intent, 1);
			return;
		}
		if(!NetWorkUtil.isNetworkAvailable(m_activity)){
			ToastUtil.show(m_activity, "网络错误");
			return ;
		}
		XHttp.PostByToken(Constant.ownPrivateInfo, userinfo, null,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						if (arg0 instanceof HttpException) { // 网络错误
							HttpException arg = (HttpException) arg0;
							String result = arg.getResult();
							JSONObject object;
							try {
								object = new JSONObject(result);
								String msg = object.getString("msg");
								String code = object.getString("code");
								if (code.equals("DFT.0007")
										|| code.equals("DFT.0005")) {
									Intent intent = new Intent(m_activity,
											LoginActivity.class);
									((BaseActivity<?>) m_activity)
											.startActivityForResult(intent, 1);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						String murl=url + "&token=" + userinfo.getToken()
								+ "&userId=" + userinfo.getUserId();
						Intent intent=new Intent(m_activity,FindWebActivity.class);
						intent.putExtra("weburl", murl);
						intent.putExtra("title", "幸运大转盘");
						m_activity.startActivity(intent);
					}
				});
	}


	OnClickListener Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
				case R.id.findviewlay:
					request();
					break;
				case R.id.boxuserlay:
					requestBox();
					break;
				case R.id.findviewlay_dream:
					openDream();
					break;
			}

		}
	};
	
	public void requestBox() {
		if(userinfo==null){
			ToastUtil.show(m_activity, "亲~先登录哦");
			Intent intent=new Intent(m_activity,LoginActivity.class);
			m_activity.startActivityForResult(intent, 1);
			return;
		}
		if(!NetWorkUtil.isNetworkAvailable(m_activity)){
			ToastUtil.show(m_activity, "网络错误");
			return ;
		}
		XHttp.PostByToken(Constant.ownPrivateInfo, userinfo, null,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						if (arg0 instanceof HttpException) { // 网络错误
							HttpException arg = (HttpException) arg0;
							String result = arg.getResult();
							JSONObject object;
							try {
								object = new JSONObject(result);
								String msg = object.getString("msg");
								String code = object.getString("code");
								if (code.equals("DFT.0007")
										|| code.equals("DFT.0005")) {
									Intent intent = new Intent(m_activity,
											LoginActivity.class);
									((BaseActivity<?>) m_activity)
											.startActivityForResult(intent, 1);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						String murl=boxurl + "&token=" + userinfo.getToken()
								+ "&userId=" + userinfo.getUserId();
						Intent intent=new Intent(m_activity,FindWebActivity.class);
						intent.putExtra("weburl", murl);
						intent.putExtra("title", "疯狂开宝箱");
						m_activity.startActivity(intent);
					}
				});
	}
	
	public void openDream(){
		Intent intent=new Intent(m_activity,FindWebActivity.class);
		intent.putExtra("weburl", dreamurl);
		intent.putExtra("title", "全民圆梦");
		m_activity.startActivity(intent);
	}

}
