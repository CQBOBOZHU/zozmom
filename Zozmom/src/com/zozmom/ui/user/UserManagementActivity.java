package com.zozmom.ui.user;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CircleImageView;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.oss.OSSClicentUtil;
import com.zozmom.net.oss.OSSConstant;
import com.zozmom.pic.PhotoConstant;
import com.zozmom.pic.PhotoSelector;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.LoginActivity;
import com.zozmom.ui.fragment.MeFragment;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Tools;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * 个人资料
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.use_management_activity)
public class UserManagementActivity extends BaseTaskActivity<String> {
	@ViewInject(R.id.usermanage__face_imageview)
	CircleImageView faceView;
	@ViewInject(R.id.usermanage_idtextview)
	TextView idTextView;
	@ViewInject(R.id.usermanager_ivcode)
	TextView ivcodeView;
	@ViewInject(R.id.usermanager_nameview)
	TextView usernameView;
	@ViewInject(R.id.usermanager_phonetextview)
	TextView phoneView;
	UserInfoModel userModel;
	private static int requestCode = 0;
	int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		userModel = getLogginUserinfo();
		userId = userModel.getUserId();
		init();
	}

	public void init() {
		Log.v("this", "userModel headUrl:" + userModel.getHeadUrl());
		String url = userModel.getHeadUrl();
		if (url == null || url.length() == 0) {
			faceView.setImageResource(R.drawable.deimage);
		}
		loadimage(faceView, Constant.FACE_URL + url + Constant.FACE_KEY);
		idTextView.setText(userModel.getUserId() + "");
		ivcodeView.setText(userModel.getInviteCode());
		usernameView.setText(userModel.getUserName());
		phoneView.setText(userModel.getPhone());
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent data;
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.alter_usernameview :
				data = new Intent(this, AlterNameActivity.class);
				startActivityForResult(data, requestCode);
				break;
			case R.id.alterpwd_view :
				data = new Intent(this, LoginActivity.class);
				data.putExtra("code", 3);
				data.putExtra("from", "user");
				startActivity(data);
				break;
			case R.id.address_view :
				data = new Intent(this, AddressManagerActivity.class);
				startActivity(data);
				break;
			case R.id.usermanage__face_imageview :
				showdailog();
				break;
			default :
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getLogginUserinfo() == null) {
			finish();
		}
	}

	@Override
	public void onSuccess(String arg0) {
		super.onSuccess(arg0);
		ToastUtil.show(this, "修改成功");
		if (arg0.equals("true")) {
			userModel.setHeadUrl(urlface);
			updataUserinfo(userModel);
			if(MeFragment.meFragment!=null){
				MeFragment.meFragment.changgeView(userModel, true);
			}
		} else {
			ToastUtil.show(this, "修改失败");
		}
	}

	public void onError(Throwable arg0, boolean arg1) {
		super.onError(arg0, arg1);
		Log.v(TAG, arg0.toString());
		ToastUtil.show(this, "修改失败");
		RequestErrorUtil.judge(arg0, m_activity);

	}

	AlertDialog alertDialog;

	/**
	 * 修改头像的dialog
	 */
	public void showdailog() {
		// View view = LayoutInflater.from(this).inflate(
		// R.layout.alter_head_dailog, null);
		AlertDialog.Builder builder = new Builder(this);
		alertDialog = builder.create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.alter_head_dailog);
		Button startCa = (Button) window.findViewById(R.id.start_btn1);
		Button startM = (Button) window.findViewById(R.id.start_btn2);
		startCa.setOnClickListener(listener);
		startM.setOnClickListener(listener);
		alertDialog.setCanceledOnTouchOutside(true);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.start_btn1 :
					alertDialog.dismiss();
					startLocalPhoto(true);
					break;
				case R.id.start_btn2 :
					alertDialog.dismiss();
					startCamera(true);
					break;
			}
		}
	};
	String urlface = "";

	public void onPhotoSelected(final String photo, boolean isHead) {
		File file = new File(photo);
		if (!file.exists()) {
			ToastUtil.show(m_activity, "文件不存在");
			return;
		}
		Log.v("this", file.getPath());
		final String testObject = Tools.getRandomString() + ".png";// 随机名字
		OSSClient oss = OSSClicentUtil.instanceFaceOss(this);
		PutObjectRequest put = new PutObjectRequest(
				OSSConstant.face_bucketName, userId + "/" + testObject,
				file.getPath());
		urlface = userId + "/" + testObject;
		@SuppressWarnings("unused")
		OSSAsyncTask<?> task = oss.asyncPutObject(put,
				new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

					@Override
					public void onFailure(PutObjectRequest arg0,
							ClientException arg1, ServiceException arg2) {

					}

					@Override
					public void onSuccess(PutObjectRequest arg0,
							PutObjectResult arg1) {
						loadimage(faceView, photo);
						String imagefile = userId + "/" + testObject;
						requestAlterUserFacce(imagefile);
					}

				});
	}

	/**
	 * 修改用户头像
	 */
	public void requestAlterUserFacce(String faceurl) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("headUrl", faceurl);
		map.put("userId", userId);
		requstPostByToken(Constant.UPDATA_USER, userModel, map);
	}

	PhotoSelector m_photoSelector = null;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == UserManagementActivity.requestCode
				&& resultCode == AlterNameActivity.resultCode) {
			userModel = getLogginUserinfo();
			usernameView.setText(userModel.getUserName());
		}
		if (m_photoSelector == null) {
			m_photoSelector = new PhotoSelector(m_activity);
		}

		switch (requestCode) {
			case PhotoConstant.FROM_CAMERA : {
				String temp = m_photoSelector.getTempPhotoPath();
				if (temp != null) {
					File f = new File(temp);
					ToastUtil.show(this, "拍照成功");
					if (!m_photoSelector
							.cropImageUri(Uri.fromFile(f), 800, 800)) {
						super.onActivityResult(requestCode, resultCode, data);
					}
				} else {
					ToastUtil.show(this, "拍照失败, 请重试");
				}
				break;
			}
			case PhotoConstant.FROM_CUT : {
				String newFile = m_photoSelector.getTempCuttedPhotoPath();
				if (newFile != null) {
					File f = new File(newFile);
					Log.v("this", "pic check true");
					if (f.exists()) {
						onPhotoSelected(newFile, true);
					} else {
						ToastUtil.show(this, "选择照片失败, 请重新选择");
					}
				} else {
					ToastUtil.show(this, "选择照片失败, 请重新选择");
				}
				super.onActivityResult(requestCode, resultCode, data);
				break;
			}
			case PhotoConstant.FROM_LOCAL : {
				if (data != null) {
					Uri uri = data.getData();
					String newPhoto = m_photoSelector.saveSelectedPhoto(uri,
							false);
					if (newPhoto != null) {
						File f = new File(newPhoto);
						if (f.exists()) {
							m_photoSelector.cropImageUri(Uri.fromFile(f), 800,
									800);
						} else {
							ToastUtil.show(this, "选择照片失败, 请重新选择");
						}
					} else {
						ToastUtil.show(this, "选择照片失败, 请重新选择");
					}
				}
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
