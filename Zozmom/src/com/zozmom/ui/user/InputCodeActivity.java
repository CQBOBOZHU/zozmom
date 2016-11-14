package com.zozmom.ui.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

@ContentView(R.layout.input_code_activity)
public class InputCodeActivity extends UserManagerBaseActivity<Object> {
	@ViewInject(R.id.status_bar_title)
	TextView textView;
	@ViewInject(R.id.input_code_editview)
	EditText editText;
	@ViewInject(R.id.show_invited_lay)
	View view;
	@ViewInject(R.id.input_lay)
	View inputView;
	@ViewInject(R.id.invited_userName)
	TextView invitedNameView;
	@ViewInject(R.id.invited_userinvitedCode)
	TextView invitedCodeView;
	@ViewInject(R.id.input_imageview)
	ImageView invitedIconView;
	UserInfoModel infoModel;
	int inviteId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		textView.setText(getRString(R.string.input_ivcode));
		infoModel=getLogginUserinfo();
		inviteId=infoModel.getInviteId();
		if(inviteId==0){
			view.setVisibility(View.GONE);
			inputView.setVisibility(View.VISIBLE);
		}else{
			inputView.setVisibility(View.GONE);
			view.setVisibility(View.VISIBLE);
			requestInvitedUser(inviteId);
		}
	}
	
	public void requestInvitedUser(int inviteId){
		Map<String, String> map=new HashMap<String, String>();
		map.put("inviteId", inviteId+"");
		XHttp.Get(Constant.FIND_INVITEDUSER, map, new CommonCallback<JSONObject>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				RequestErrorUtil.judge(arg0, m_activity);
			}

			@Override
			public void onFinished() {
				
			}

			@Override
			public void onSuccess(JSONObject arg0) {
				try {
					UserInfoModel model=(UserInfoModel) JsonUtil.JsonToModel(arg0, UserInfoModel.class);
					init(model);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	int otherUid;
	public void init(UserInfoModel model){
		invitedNameView.setText(model.getUserName());
		invitedCodeView.setText(model.getInviteCode());
		otherUid=model.getUserId();
		String icon=model.getHeadUrl();
		loadimage(invitedIconView, Constant.FACE_URL+icon+Constant.FACE_KEY);
	}
	

	@Override
	public void initView() {
		super.initView();
	}

	public void toSure(View view) {
		String code=editText.getText().toString().trim();
//		if(code==null||code.trim().length()!=7){
//			ToastUtil.show(this, "邀请码错误");
//			return;
//		}
		XHttp.PostByToken(Constant.INPUT_CODE+code,infoModel, null, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				RequestErrorUtil.judge(arg0, m_activity);
			}

			@Override
			public void onFinished() {
				
			}

			@Override
			public void onSuccess(String arg0) {
				Log.v(TAG, "arg0 "+arg0.toString());
				if(arg0.equals("true")){
//					I67654
					ToastUtil.show(InputCodeActivity.this, "绑定成功");
				}else{
					ToastUtil.show(InputCodeActivity.this, "绑定失败");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
		}
	}
	
	public void Wth(View view){
		Intent intent=new Intent(this,OtherUserCenterActivity.class);
		intent.putExtra("userId", otherUid);
		startActivity(intent);
	}
}
