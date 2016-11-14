package com.zozmom.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.http.RequestParams;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuViewPager;
import com.zozmom.manager.AccountManager;
import com.zozmom.manager.ActiviyManager;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.adapter.ImagePagerAdapter;
import com.zozmom.ui.fragment.BaseFragment;
import com.zozmom.ui.fragment.IndexFragment;
import com.zozmom.ui.fragment.MeFragment;
import com.zozmom.ui.fragment.NewFindFragment;
import com.zozmom.ui.fragment.ShowFragment;
import com.zozmom.ui.fragment.TheNewFragment;
import com.zozmom.ui.pay.RechargeActivity;
import com.zozmom.ui.user.AccountActivity;
import com.zozmom.ui.user.AddressManagerActivity;
import com.zozmom.ui.user.CommissionActivity;
import com.zozmom.ui.user.InputCodeActivity;
import com.zozmom.ui.user.McActivity;
import com.zozmom.ui.user.ScenterActivity;
import com.zozmom.ui.user.ScenterBySdkActivity;
import com.zozmom.ui.user.SetupActivity;
import com.zozmom.ui.user.UserCtrueActivity;
import com.zozmom.ui.user.UserDreamActivity;
import com.zozmom.ui.user.UserHongbaoActivity;
import com.zozmom.ui.user.UserManagementActivity;
import com.zozmom.ui.user.UserShowActivity;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.TimeCount;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.UpdateInfoUtil3;
import com.zozmom.util.UpgradeUtil;
import com.zozmom.util.Util;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class IndexActivity extends BaseTaskActivity<Object> {

	IndexFragment indexfragment;
	TheNewFragment thenewFragment;
//	FindFragment findFragment;
	NewFindFragment findFragment;
	ShowFragment openFragment;
	MeFragment mydreamFragment;
	UserInfoModel userinfo;
	Drawable ddr;
	Drawable ddr_sd;
	Drawable alldr;
	Drawable alldr_sd;
	Drawable newdr;
	Drawable newdr_sd;
	Drawable shopdr;
	Drawable shopdr_sd;
	Drawable myddr;
	Drawable myddr_sd;
	Button newcheckBtn;
	Button oldcheckBtn;
	int newcheckindex;
	int oldcheckindex;
	int starttime;
	UpdateInfoUtil3 infoUtil3;
	public static IndexActivity indexActivity;
	TimeCount time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActiviyManager.instance().removeAll();
		super.onCreate(savedInstanceState);
		indexActivity = this;
		infoUtil3 = new UpdateInfoUtil3(m_activity);
		starttime = getIntent().getIntExtra("starttime", -1);
		userinfo = AccountManager.getInstance().getLogginUserInfo();
		setContentView(R.layout.index_activity);
		init();
		requestVersion();
	}

	public boolean isItemOne() {
		// viewPager.
		if (oldcheckindex == 1)
			return true;
		else
			return false;
	}

	AlertDialog hongbaoAlertDialog;
	Button requestKeyBtn;
	/**
	 * 领取新手红包
	 */
	@SuppressLint("InflateParams")
	public void showGetFirstHongbao(int opentime) {
		if (hongbaoAlertDialog != null && opentime == 0) {
			hongbaoAlertDialog.show();
			return;
		}
		if(userinfo==null){
			Intent intent=new Intent(this,LoginActivity.class);
			startActivityForResult(intent, 1);
			return;
		}
		AlertDialog.Builder builder = new Builder(this);
		hongbaoAlertDialog = builder.create();
		View view = LayoutInflater.from(this).inflate(R.layout.newuser_hongbao,
				null);
		hongbaoAlertDialog.setView(new EditText(this));//让后面的editview开始获取不了焦点
		hongbaoAlertDialog.show();
		Window window = hongbaoAlertDialog.getWindow();
		window.setContentView(view);
		final EditText editText = (EditText) view
				.findViewById(R.id.newuser_phone);
		final EditText codeeditText = (EditText) view
				.findViewById(R.id.input_codeedit);
		View inputlay = view.findViewById(R.id.input_view);
		Button button = (Button) view.findViewById(R.id.newuser_gethongbao);
		ImageView exit = (ImageView) view.findViewById(R.id.newhongbao_exit);
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hongbaoAlertDialog.dismiss();
			}
		});
		requestKeyBtn = (Button) view.findViewById(R.id.get_codebtn);
		time = new TimeCount(60000, 1000, requestKeyBtn);
		if (opentime > 0) {
			requestKeyBtn.setVisibility(View.GONE);
			codeeditText.setVisibility(View.GONE);
			hongbaoAlertDialog.setCanceledOnTouchOutside(true);
			if (userinfo != null) {
				// editText.setVisibility(View.GONE);
				inputlay.setVisibility(View.GONE);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String phone = userinfo.getPhone();
						GetHongbaoRequest(phone);
					}
				});
				return;
			} else {
				inputlay.setVisibility(View.VISIBLE);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String phone = editText.getText().toString();
						if (phone == null || !Util.isMobile(phone)) {
							ToastUtil.show(m_activity, "请输入正确手机号");
							return;
						}
						GetHongbaoRequest(phone);
					}
				});
				return;
			}
		}
		requestKeyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phone = editText.getText().toString();
				sendKey(phone);
			}
		});
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone = editText.getText().toString();
				String code = codeeditText.getText().toString();
				getHongbao(phone, code);
			}
		});
		hongbaoAlertDialog.setCanceledOnTouchOutside(false);
		// window.setContentView(view);
		// hongbaoAlertDialog.show();
	}
	/**
	 * 请求
	 * 
	 * @param phone
	 */
	public void getHongbao(String phone, String code) {

		if (phone == null || phone.length() == 0) {
			ToastUtil.show(this, "手机不能为空");
			return;
		}
		if (!Util.isMobile(phone)) {
			ToastUtil.show(this, "请输入正确的手机号");
			return;
		}
		if (code == null || code.length() == 0) {
			ToastUtil.show(this, "验证码不能为空");
			return;
		}
		if (code.length() != 4) {
			ToastUtil.show(this, "验证码错误");
			return;
		}
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络错误");
			return;
		}
		phoneLogin(phone, code);
		// GetHongbaoRequest(phone);
	}

	/**
	 * 手机登录
	 */
	public void phoneLogin(final String phone, String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		map.put("vcode", code);
		String mac = UpgradeUtil.getChannelId(this);;
		Log.v(TAG, mac);
		map.put("equipmentId", mac);
		XHttp.Post(Constant.LOGIN + "code", map,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						RequestErrorUtil.judge(arg0, IndexActivity.this);
						// ToastUtil.show(IndexActivity.this, "phoneLogin领取失败");
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						Log.v("this", "onSuccess" + arg0.toString());
						try {
							userinfo = (UserInfoModel) JsonUtil.JsonToModel(
									arg0, UserInfoModel.class);
							AccountManager.getInstance().setLogginUserInfo(
									userinfo);
							if(MeFragment.meFragment!=null){
								MeFragment.meFragment.changgeView(userinfo, true);
							}
							GetHongbaoRequest(phone);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

	}

	private void GetHongbaoRequest(String phone) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		map.put("activityId", 2);
		// String equipmentId = UpgradeUtil.getChannelId(this);
		map.put("equipmentId", "zozmom");
		XHttp.Post(Constant.GET_NEWUSER_HONGBAO, map,
				new CommonCallback<String>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v("this", arg0.toString());
						hongbaoAlertDialog.dismiss();
						ToastUtil.show(m_activity, "领取失败");
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(String arg0) {
						Log.v("this", arg0 + "");
						hongbaoAlertDialog.dismiss();
						if (arg0.equals("0")) {
							ToastUtil.show(m_activity, "您已经领取过");
						} else {
							ToastUtil.show(m_activity, "领取成功,快去逐梦吧");
						}

					}
				});
	}

	Button dreambtn;
	Button thenewbtn;
	Button allbtn;
	Button shopping_carbtn;
	Button my_dreambtn;
	CuViewPager viewPager;
	List<BaseFragment> frlist = new ArrayList<BaseFragment>();
	List<Button> buttons = new ArrayList<Button>();
	public void init() {
		viewPager = (CuViewPager) findViewById(R.id.viewpager);
		initFragment();
		dreambtn = (Button) findViewById(R.id.dreambtn);
		thenewbtn = (Button) findViewById(R.id.the_newbtn);
		allbtn = (Button) findViewById(R.id.all_btn);
		shopping_carbtn = (Button) findViewById(R.id.shopping_carbtn);
		my_dreambtn = (Button) findViewById(R.id.my_dreambtn);
		buttons.add(dreambtn);
		buttons.add(thenewbtn);
		buttons.add(allbtn);
		buttons.add(shopping_carbtn);
		buttons.add(my_dreambtn);
		setLeftdrawable();
		oldcheckBtn = dreambtn;
		oldcheckindex = 1;
	}

	public void initFragment() {
		indexfragment = new IndexFragment();
//		findFragment = new FindFragment();
		findFragment = new NewFindFragment();
		thenewFragment = new TheNewFragment();
		openFragment = new ShowFragment();
		mydreamFragment = new MeFragment();
		frlist.add(indexfragment);
		frlist.add(thenewFragment);
		frlist.add(findFragment);
		frlist.add(openFragment);
		frlist.add(mydreamFragment);
		ImagePagerAdapter adapter = new ImagePagerAdapter(
				getSupportFragmentManager(), frlist);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(2);
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				newcheckindex = position + 1;
				newcheckBtn = buttons.get(position);
				ButtonChang(position + 1);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	/**
	 * 选择
	 */
	public void choseBtn() {

	}

	public void changeOldBtn(int id) {
		switch (id) {
			case 1 :
				dreambtn.setCompoundDrawables(null, ddr, null, null);
				break;
			case 2 :
				thenewbtn.setCompoundDrawables(null, newdr, null, null);
				break;
			case 3 :
				allbtn.setCompoundDrawables(null, alldr, null, null);
				break;
			case 4 :
				shopping_carbtn.setCompoundDrawables(null, shopdr, null, null);
				break;
			case 5 :
				my_dreambtn.setCompoundDrawables(null, myddr, null, null);
				break;
		}
	}

	public void ButtonChang(int arg0) {
		switch (arg0) {
			case 1 :
				setBtnTextColor(dreambtn, allbtn, thenewbtn, shopping_carbtn,
						my_dreambtn);
				dreambtn.setCompoundDrawables(null, ddr_sd, null, null);
				break;
			case 2 :
				setBtnTextColor(thenewbtn, dreambtn, allbtn, shopping_carbtn,
						my_dreambtn);
				thenewbtn.setCompoundDrawables(null, newdr_sd, null, null);
				break;
			case 3 :
				setBtnTextColor(allbtn, dreambtn, thenewbtn, shopping_carbtn,
						my_dreambtn);
				allbtn.setCompoundDrawables(null, alldr_sd, null, null);
				break;

			case 4 :
				setBtnTextColor(shopping_carbtn, dreambtn, allbtn, thenewbtn,
						my_dreambtn);
				shopping_carbtn.setCompoundDrawables(null, shopdr_sd, null,
						null);
				break;
			case 5 :
				my_dreambtn.setCompoundDrawables(null, myddr_sd, null, null);
				setBtnTextColor(my_dreambtn, dreambtn, allbtn, thenewbtn,
						shopping_carbtn);
				break;
		}
		changeOldBtn(oldcheckindex);
		oldcheckBtn = newcheckBtn;
		oldcheckindex = newcheckindex;
	}

	public void setBtnTextColor(Button btn1, Button btn2, Button btn3,
			Button btn4, Button btn5) {
		btn1.setTextColor(getRColor(R.color.background));
		btn2.setTextColor(getRColor(R.color.grid_text_color));
		btn3.setTextColor(getRColor(R.color.grid_text_color));
		btn4.setTextColor(getRColor(R.color.grid_text_color));
		btn5.setTextColor(getRColor(R.color.grid_text_color));
	}

	public void setLeftdrawable() {
		ddr = getRDrawble(R.drawable.dream);
		ddr.setBounds(0, 0, ddr.getMinimumWidth(), ddr.getMinimumHeight());

		alldr = getRDrawble(R.drawable.all);
		alldr.setBounds(0, 0, alldr.getMinimumWidth(), alldr.getMinimumHeight());

		newdr = getRDrawble(R.drawable.the_new);
		newdr.setBounds(0, 0, newdr.getMinimumWidth(), newdr.getMinimumHeight());

		shopdr = getRDrawble(R.drawable.show);
		shopdr.setBounds(0, 0, shopdr.getMinimumWidth(),
				shopdr.getMinimumHeight());

		myddr = getRDrawble(R.drawable.my_dream);
		myddr.setBounds(0, 0, myddr.getMinimumWidth(), myddr.getMinimumHeight());

		ddr_sd = getRDrawble(R.drawable.dream_selected);
		ddr_sd.setBounds(0, 0, ddr_sd.getMinimumWidth(),
				ddr_sd.getMinimumHeight());

		alldr_sd = getRDrawble(R.drawable.all_selected);
		alldr_sd.setBounds(0, 0, alldr_sd.getMinimumWidth(),
				alldr_sd.getMinimumHeight());

		newdr_sd = getRDrawble(R.drawable.the_new_selected);
		newdr_sd.setBounds(0, 0, newdr_sd.getMinimumWidth(),
				newdr_sd.getMinimumHeight());

		shopdr_sd = getRDrawble(R.drawable.show_selected);
		shopdr_sd.setBounds(0, 0, shopdr_sd.getMinimumWidth(),
				shopdr_sd.getMinimumHeight());

		myddr_sd = getRDrawble(R.drawable.my_dream_selected);
		myddr_sd.setBounds(0, 0, myddr_sd.getMinimumWidth(),
				myddr_sd.getMinimumHeight());
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.dreambtn :
				newcheckBtn = dreambtn;
				newcheckindex = 1;
				viewPager.setCurrentItem(0);
				break;
			case R.id.the_newbtn :
				newcheckBtn = thenewbtn;
				newcheckindex = 2;
				viewPager.setCurrentItem(1);
				break;
			case R.id.all_btn :
				newcheckindex = 3;
				newcheckBtn = allbtn;
				viewPager.setCurrentItem(2);
				break;
			case R.id.shopping_carbtn :
				newcheckBtn = shopping_carbtn;
				newcheckindex = 4;
				viewPager.setCurrentItem(3);
				break;
			case R.id.my_dreambtn :
				newcheckindex = 5;
				newcheckBtn = my_dreambtn;
				viewPager.setCurrentItem(4);
				break;
		}
	}

	long m_backPressTime = 0;

	@Override
	protected void onResume() {
		super.onResume();
		userinfo = getLogginUserinfo();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			if ((System.currentTimeMillis() - m_backPressTime) > 3000) {
				ToastUtil.show(this, R.string.press_back_to_exit);
				m_backPressTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
				return true;
			}
		}
		return false;
	}

	public void onDetach() {
		try {
			Field child = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			child.setAccessible(true);
			child.set(this, null);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == LoginActivity.resultCode) {
			userinfo = getLogginUserinfo();
		}
	}

	public void startActivity(Class<?> cl) {
		Intent intent = new Intent(this, cl);
		startActivity(intent);
	}

	public boolean JudgeUserinfo() {
		if (userinfo == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, 1);
			// startActivity(LoginActivity.class);
			return true;
		}
		return false;
	}

	/**
	 * 跳转到我的逐梦记录
	 * 
	 * @param view
	 */
	public void dreamList(View view) {
		if (JudgeUserinfo()) {
			return;
		}
		startActivity(UserDreamActivity.class);
	}

	/**
	 * 启动实现的梦想
	 */
	public void userCtrue(View view) {
		if (JudgeUserinfo()) {
			return;
		}
		startActivity(UserCtrueActivity.class);
	}

	/**
	 * 启动个人信息
	 */
	public void userMg(View view) {
		startActivity(UserManagementActivity.class);
	}

	/**
	 * 启动登录界面
	 */
	public void login(View view) {
		startActivity(LoginActivity.class);
	}

	/**
	 * 启动用户晒单页面
	 */
	public void userShow(View view) {
		if (JudgeUserinfo()) {
			return;
		}
		startActivity(UserShowActivity.class);
	}

	/**
	 * 启动用户红包页面
	 */
	public void hongBao(View view) {
		if (JudgeUserinfo()) {
			return;
		}
		startActivity(UserHongbaoActivity.class);
	}

	/**
	 * 分享赚钱页面
	 */
	public void shareForMoney(View view) {
		if (JudgeUserinfo()) {
			return;
		}
		Intent intent = new Intent(this, BaseWebActivity.class);
		intent.putExtra("title", "赚红包");
		String token = userinfo.getToken();
		int userId = userinfo.getUserId();
		String inviteCode = userinfo.getInviteCode();
		String url = Constant.weixin + "#hongbao?type=app&token=" + token
				+ "&userId=" + userId + "&inviteCode=" + inviteCode;
		intent.putExtra("weburl", url);
		startActivity(intent);
	}

	/**
	 * 输入邀请码页面
	 */
	public void inputCode(View view) {
		if (JudgeUserinfo()) {
			return;
		}
		startActivity(InputCodeActivity.class);
	}

	/**
	 * 账户明细页面
	 */
	public void uAccount(View view) {
		if (JudgeUserinfo()) {
			return;
		}
		startActivity(AccountActivity.class);
	}

	/**
	 * 消息中心页面
	 */
	public void uMc(View view) {
		if (JudgeUserinfo()) {
			return;
		}
		startActivity(McActivity.class);
	}

	/**
	 * 客服中心页面
	 */
	public void sCenter(View view) {
		startActivity(ScenterActivity.class);
	}

	/**
	 * 充值页面
	 */
	public void recharge(View view) {
		startActivity(RechargeActivity.class);
	}

	/**
	 * 设置页面
	 */
	public void setup(View view) {
		startActivity(SetupActivity.class);
	}

	/**
	 * 商品分类
	 */
	public void gdGdCls(View view) {
		startActivity(GdClsActivity.class);
	}

	/**
	 * 赚红包
	 */
	public void hongBaoCls(View view) {
		if (JudgeUserinfo()) {
			return;
		}
		Intent intent = new Intent(this, BaseWebActivity.class);
		intent.putExtra("title", "赚红包");
		String token = userinfo.getToken();
		int userId = userinfo.getUserId();
		String inviteCode = userinfo.getInviteCode();
		String url = Constant.weixin + "#hongbao?type=app&token=" + token
				+ "&userId=" + userId + "&inviteCode=" + inviteCode;
		intent.putExtra("weburl", url);
		startActivity(intent);
	}
	
	public void kefu(View view){
		Intent intent=new Intent(this,ScenterBySdkActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 我的逐梦币
	 */
	public void myDreamG(View view) {
		if (JudgeUserinfo()) {
			return;
		}
		startActivity(AccountActivity.class);
	}
	
	public void address(View view){
		if (JudgeUserinfo()) {
			return;
		}
		startActivity(AddressManagerActivity.class);
	}
	/**
	 * 跳转到我的佣金界面
	 * 
	 * @param view
	 */
	public void openYJ(View view) {
		if (JudgeUserinfo()) {
			return;
		}
		Intent intent = new Intent(this, CommissionActivity.class);
		String text = ((Button) view).getText().toString();
		String coin = text.replace("佣金:", "").replace("元", "");
		intent.putExtra("commcoin", coin);
		startActivity(intent);
	}

	/**
	 * 用来获取当前版本和服务器版本是否一直
	 */
	public void requestVersion() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			return;
		}
		int vcode = UpgradeUtil.getVersionCode(this);
		oldversionName = UpgradeUtil.getVersionName(this);
		XHttp.Get(Constant.UP_VERSION + vcode, null,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v(TAG, "onError" + arg0.toString());
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						Log.v(TAG, "onSuccess" + arg0.toString());
						if (arg0.toString().contains("code")
								&& arg0.toString().contains("HOM.0001")) {
							return;
						} else {
							try {
								// {"vid":2,"versionNum":"V1.0.1","desc":"新增商品搜索功能","link":"http://www.hao123.com/"}
								String content = arg0.getString("desc");
								updataUrl = arg0.getString("link");
								String newversion = arg0
										.getString("versionNum");
								showUpdateDialog(content, newversion);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});

	}

	private ProgressDialog progressDialog;

	String description = "";// 更新的内容
	String newversionName;// 最新版本的版本号
	String oldversionName;// 旧版本号
	String updataUrl;// 更新版本的url;
	AlertDialog upDialog;
	View sureView;
	Button sureBtn;
	ProgressBar bar;
	TextView barhint;

	@SuppressLint("InflateParams")
	private void showUpdateDialog(String content, String newversion) {
		if (hongbaoAlertDialog != null && hongbaoAlertDialog.isShowing()) {
			return;
		}
		if (upDialog != null) {
			upDialog.show();
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		upDialog = builder.create();
		upDialog.show();
		View view = LayoutInflater.from(this)
				.inflate(R.layout.up_version, null);
		upDialog.getWindow().setContentView(view);
		TextView contentview = (TextView) view
				.findViewById(R.id.up_content_view);
		TextView titleView = (TextView) view.findViewById(R.id.up_title_view);
		titleView.setText(newversion);
		contentview.setText(content);
		sureBtn = (Button) view.findViewById(R.id.up_sure_btn);
		bar = (ProgressBar) view.findViewById(R.id.download_progressbar);
		barhint = (TextView) view.findViewById(R.id.download_hintview);
		sureBtn.setOnClickListener(islistener);

		upDialog.setCanceledOnTouchOutside(true);
	}

	OnClickListener islistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.up_sure_btn :
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
							upDialog.cancel();
							ToastUtil.show(m_activity, "网络错误");
							return;
						}
						sureBtn.setVisibility(View.GONE);
						infoUtil3.downLoadFile(updataUrl, upDialog, handler1,
								bar, barhint);
					} else {
						ToastUtil.show(m_activity, "SD卡不可用，请插入SD卡");
					}
					break;

				default :
					break;
			}
		}
	};

	// UpgradeUtil upgradeUtil;
	//
	// void downFile(final String url) {
	// progressDialog = new ProgressDialog(this); // 进度条，在下载的时候实时更新进度，提高用户友好度
	// progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	// progressDialog.setTitle("正在下载...");
	// progressDialog.setMessage("请稍候...");
	// progressDialog.setProgress(0);
	// progressDialog.show();
	// upgradeUtil.downLoadFile(url, progressDialog, handler1);
	// }
	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
		};
	};

	public void toChang(View view) {
		viewPager.setCurrentItem(2);
	}
	/**
	 * 更改viewpager
	 */
	public void changeCunrrentItem() {
		if (viewPager != null) {
			viewPager.setCurrentItem(2);
		}
	}

	public void openDialog(View view) {
		showGetFirstHongbao(1);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent.getIntExtra("data", -1) == 10) {
			if (viewPager != null) {
				viewPager.setCurrentItem(0);
			}
		}
		super.onNewIntent(intent);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public void sendKey(String phone) {
		if (phone == null || !Util.isMobile(phone)) {
			ToastUtil.show(this, "请输入正确手机号");
			return;
		}
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络错误");
			return;
		}

		requestKeyBtn.setClickable(false);
		RequestParams params = new RequestParams(Constant.REGISTER_CHECK);
		params.addParameter("phone", phone);
		params.addParameter("type", "0");
		post0(params);

	}

	public void post0(RequestParams params) {
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				Log.v("this", "onCancelled" + arg0.getMessage());
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				ToastUtil.show(IndexActivity.this, "获取失败");
				requestKeyBtn.setClickable(true);
				// RequestErrorUtil.judge(arg0, LoginActivity.this);
				// USR.0005:验证码错误、USR.0006:手机格式不正确、USR.0007:设备ID不能为空、USR.0008:IP地址不正确
			}

			@Override
			public void onFinished() {
				Log.v("this", "onFinished");
			}

			@Override
			public void onSuccess(String arg0) {
				Log.v("this", "onSuccess" + arg0.toString());
				time.start();
			}
		});
	}
}
