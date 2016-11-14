package com.zozmom.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.chasedream.zhumeng.R;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.Information;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zozmom.constants.Constant;
import com.zozmom.dialog.CustomProgressDialog;
import com.zozmom.gee.Geetest;
import com.zozmom.gee.GtDialog;
import com.zozmom.gee.GtDialog.GtListener;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.OkHttpUtil;
import com.zozmom.net.XHttp;
import com.zozmom.net.oss.PutObjectSamples;
import com.zozmom.util.DeviceInfo;
import com.zozmom.util.FileUtil;
import com.zozmom.util.ImageUtils;
import com.zozmom.util.LogUtil;
import com.zozmom.util.MD5Utils;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.UpgradeUtil;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DemoActivity extends BaseTaskActivity<Object> implements Callback {
	UMShareAPI mShareAPI;
	ProgressBar bar;
	ProgressBar progressBar;
	ImageView downImageView;
//	Handler handler=new Handler();
	String murl="http://10.1.24.131:8080/yyzm-app/activity/verify/playActivity/12";
	UserInfoModel infoModel;
	
	
	 private Context context = DemoActivity.this;
	    //因为可能用户当时所处在低速高延迟网络，所以异步请求可能在后台用时很久才获取到验证的数据。可以自己设计状态指示器, demo仅作演示。
//	    private ProgressDialog progressDialog;
//	    private CustomProgressDialog progressDialog;
	    private GtAppDlgTask mGtAppDlgTask;

	    // 创建验证码网络管理器实例
	    private Geetest captcha = new Geetest(

	            // 设置获取id，challenge，success的URL，需替换成自己的服务器URL
	            Constant.gee_check1,

	            // 设置二次验证的URL，需替换成自己的服务器URL
	            Constant.gee_check
	    );

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.demoactivity);
	        
	        
	        
	        

	        findViewById(R.id.yanzhengma).setOnClickListener(
	                new OnClickListener() {

	                    @Override
	                    public void onClick(View v) {

	                        GtAppDlgTask gtAppDlgTask = new GtAppDlgTask();
	                        mGtAppDlgTask = gtAppDlgTask;
	                        mGtAppDlgTask.execute();
	                        showProgressDialog();
	                        m_progressDialog.setOnCancelListener(new CustomProgressDialog.OnCancelListener() {
	                            @Override
	                            public void onCancel(DialogInterface dialog) {
	                                toastMsg("user cancel progress dialog");
	                                stopProgressDialog();
	                                mGtAppDlgTask.cancel(true);
	                                captcha.cancelReadConnection();
	                            }
	                        });
	                    }
	                });

	        captcha.setTimeout(5000);

	        captcha.setGeetestListener(new Geetest.GeetestListener() {
	            @Override
	            public void readContentTimeout() {
	                mGtAppDlgTask.cancel(true);
	                //TODO 获取验证参数超时
	                stopProgressDialog();
//	                Looper.prepare();
	                LogUtil.v("read content time out");
//	                Looper.loop();
	                ToastUtil.show(m_activity, "获取验证超时,请重新获取");
	            }

	            @Override
	            public void submitPostDataTimeout() {
	                //TODO 提交二次验证超时
	                ToastUtil.show(m_activity, "验证超时");
	            }
	        });
	    }

	    class GtAppDlgTask extends AsyncTask<Void, Void, Boolean> {

	        @Override
	        protected Boolean doInBackground(Void... params) {

	            return captcha.checkServer();
	        }

	        @Override
	        protected void onPostExecute(Boolean result) {
	            if (result) {
	                // 根据captcha.getSuccess()的返回值 自动推送正常或者离线验证
	                openGtTest(context, captcha.getGt(), captcha.getChallenge(), captcha.getSuccess());
	            } else {
	                // TODO 从API_1获得极验服务宕机或不可用通知, 使用备用验证或静态验证
	                // 静态验证依旧调用上面的openGtTest(_, _, _), 服务器会根据getSuccess()的返回值, 自动切换
	            	  ToastUtil.show(m_activity, "获取失败,请重新获取");
	            	  
	                LogUtil.v("Geetest Server is Down.");
	                // 执行此处网站主的备用验证码方案
	            }
	        }
	    }

	    public void openGtTest(Context ctx, String id, String challenge, boolean success) {

	        GtDialog dialog = new GtDialog(ctx, id, challenge, success);
	        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                //TODO 取消验证
	            	LogUtil.v("user close the geetest.");
	            }
	        });

	        dialog.setGtListener(new GtListener() {

	            @Override
	            public void gtResult(boolean success, String result) {

	                if (success) {
	                    LogUtil.v(result);
	                    try {
	                        JSONObject res_json = new JSONObject(result);
	                        Map<String, String> params = new HashMap<String, String>();
	                        params.put("geetest_challenge", res_json.getString("geetest_challenge"));
	                        params.put("geetest_validate", res_json.getString("geetest_validate"));
	                        params.put("geetest_seccode", res_json.getString("geetest_seccode"));
	                        params.put("phone", "15803006150");

	                        String response = captcha.submitPostData(params, "utf-8");

	                        //TODO 验证通过, 获取二次验证响应, 根据响应判断验证是否通过完整验证
	                        toastMsg("server captcha :" + response);

	                    } catch (Exception e) {

	                        e.printStackTrace();
	                    }

	                } else {
	                    //TODO 验证失败
	                    toastMsg("client captcha failed:" + result);
	                }
	            }

	            @Override
	            public void gtCallClose() {

	                toastMsg("close geetest windows");
	            }

	            @Override
	            public void gtCallReady(Boolean status) {

//	                progressDialog.dismiss();
	            	stopProgressDialog();

	                if (status) {
	                    //TODO 验证加载完成
	                    toastMsg("geetest finish load");
	                }else {
	                    //TODO 验证加载超时,未准备完成
	                    toastMsg("there's a network jam");
	                }
	            }

	        });

	    }

	    private void toastMsg(String msg) {

	        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
	        Log.v("zozmom", msg);

	    }

	
//	@SuppressLint("NewApi")
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
		// PlatformConfig.setWeixin("wxef020bf683e76488",
		// "1b1443c19b1c4f9ea8088a4e287caf1e");
//		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);
//		infoModel=getLogginUserinfo();
//		setContentView(R.layout.demoactivity);
//		progressBar=(ProgressBar) findViewById(R.id.progressbar);
//		downImageView=(ImageView) findViewById(R.id.downloadimage);
		// UserInfoModel infoModel=getLogginUserinfo();
		// Map<String, Object> map=new HashMap<String, Object>();
		// map.put("lotteryId", "1");
		// XHttp.PostByToken(Constant.userLogisticsInfo, infoModel,map, new
		// CommonCallback<String>() {
		//
		// @Override
		// public void onCancelled(CancelledException arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onError(Throwable arg0, boolean arg1) {
		// // TODO Auto-generated method stub
		// Log.v("this","onError"+ arg0.toString());
		// }
		//
		// @Override
		// public void onFinished() {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onSuccess(String arg0) {
		// // TODO Auto-generated method stub
		// Log.v("this","onSuccess"+ arg0.toString());
		// }
		// });
		// Intent intent=new Intent(this,IndexActivity.class);
		// PendingIntent pendIntent=PendingIntent.getActivity(this, 0, intent,
		// 0);
		// Builder builder=new Builder(this);
		// builder.setContentTitle("测试").setContentText("望眼欲穿")
		// .setContentIntent(pendIntent)
		// .setOngoing(true)//用来表示一个正在进行的通知,通常用来表示一个后台任务
		// .setTicker("谦让玩耍")
		// .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知的优先级
		// .setSmallIcon(R.drawable.ic_logo);
		// .setContentIntent(get)

		// bar = (ProgressBar) findViewById(R.id.progressBar);
		// mShareAPI = UMShareAPI.get(this);
		// initView();
//	}
	String downurl="http://zozmom.oss-cn-hangzhou.aliyuncs.com/zozmom.apk";
	
//	public void download(View view){
//		
//	}
	
	
	public void getversion(View view){
//		Constant.UP_VERSION + vcode,/
		 OkHttpUtil.AsyGet(Constant.UP_VERSION+6,
				 null,"GETVERSION", this);
	}
	String imageviewurl="http://img4.imgtn.bdimg.com/it/u=16705507,1328875785&fm=21&gp=0.jpg";
	public void downloadimage(View view){
		OkHttpUtil.downLoadFile(imageviewurl, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				InputStream is = null;
              try
              {
                  is = response.body().byteStream();
                  final Bitmap    bitmap = BitmapFactory.decodeStream(is);
                  handler.post(new Runnable()
                  {
                      @Override
                      public void run()
                      {
                    	  downImageView.setImageBitmap(bitmap);
                      }
                  });
              } catch (Exception e)
              {
                  setErrorResId(downImageView, R.drawable.pic_loading);

              } finally
              {
                  if (is != null) try
                  {
                      is.close();
                  } catch (IOException e)
                  {
                      e.printStackTrace();
                  }
              }
			}
			
			 /*
			   * 得到图片字节流 数组大小
			   * */
			  public  byte[] readStream(InputStream inStream) throws Exception{	  
			    ByteArrayOutputStream outStream = new ByteArrayOutputStream();	  
			    byte[] buffer = new byte[1024];	  
			    int len = 0;	  
			    while( (len=inStream.read(buffer)) != -1){	  
			      outStream.write(buffer, 0, len);	  
			    }	  
			    outStream.close();	  
			    inStream.close();	  
			    return outStream.toByteArray();	  
			  }
			
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				
			}
		});
	}
	  private void setErrorResId(final ImageView view, final int errorResId)
	    {
	       handler.post(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                view.setImageResource(errorResId);
	            }
	        });
	    }
	
	public void post(View view) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("productId", "116");
//		map.put("issueId", 235);
//		String equipmentId=UpgradeUtil.getChannelId(this);
//		Log.v(TAG, equipmentId);
//		map.put("equipmentId", equipmentId);
//		map.put("phone", "15803006150");
//		String pwd = MD5Utils.epPwd("123456");
//		map.put("password", pwd);
//		String tag="LOGIN";
//		OkHttpUtil.AsyPost(Constant.LOGIN+"pwd", map,tag, this);
//		infoModel=getLogginUserinfo();
//		
//		for (int i = 0; i < 20; i++) {
//			new Thread(){
//				public void run() {
//					extracted();
//				}
//			}.start();
//		}
		XHttp.Get(Constant.REGISTER_CHECK, null, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				Log.v("zozmom", arg0);
			}
		});
	}
	private void extracted() {
		XHttp.PostByToken(murl, infoModel, null, new CommonCallback<JSONObject>() {
			
			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				LogUtil.v(arg0.toString());
			}
			
			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				LogUtil.v(arg0.toString());
			}
		});
	}

	public void get(View view) {
//		 Map<String, Object> map=new HashMap<String, Object>();
//		 map.put("currentPage", 20+ "");
//		 OkHttpUtil.AsyGet(Constant.SELECTE_PRODUCT_BYTYPE_HOT,
//				 map,"GETHOT", this);
		
		Intent intent=new Intent(this,LoginActivity.class);
		startActivityForResult(intent, 0);
//		Request request = new Request.Builder()
//				.url("https://api.zozmom.com/product/productList/hot?currentPage=1")
//				.header("getMethd", "gethot").build();
//		OkHttpClient client = new OkHttpClient();
//		client.newCall(request).enqueue(new Callback() {
//			@Override
//			public void onFailure(Call call, IOException e) {
//				e.printStackTrace();
//			}
//
//			@Override
//			public void onResponse(Call call, Response response)
//					throws IOException {
//				if (!response.isSuccessful())
//					throw new IOException("Unexpected code " + response);
//
//				Headers responseHeaders = response.headers();
//				for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//					System.out.println(responseHeaders.name(i) + ": "
//							+ responseHeaders.value(i));
//				}
//
//				System.out.println(response.body().string());
//			}
//		});

	}

	private static final int NOTIFICATION_FLAG = 1;

	@SuppressLint("NewApi")
	public void notificationMethod(View view) {
		// 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		switch (view.getId()) {
		// 默认通知
			case R.id.btn1 :
				// 创建一个PendingIntent，和Intent类似，不同的是由于不是马上调用，需要在下拉状态条出发的activity，所以采用的是PendingIntent,即点击Notification跳转启动到哪个Activity
				PendingIntent pendingIntent = PendingIntent.getActivity(this,
						0, new Intent(this, DemoActivity.class), 0);
				// 下面需兼容Android 2.x版本是的处理方式
				// Notification notify1 = new Notification(R.drawable.message,
				// "TickerText:" + "您有新短消息，请注意查收！", System.currentTimeMillis());
				Notification notify1 = new Notification();
				notify1.icon = R.drawable.message;
				notify1.tickerText = "TickerText:您有新短消息，请注意查收！";
				notify1.when = System.currentTimeMillis();
				notify1.setLatestEventInfo(this, "Notification Title",
						"This is the notification message", pendingIntent);
				notify1.number = 1;
				notify1.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
				// 通过通知管理器来发起通知。如果id不同，则每click，在statu那里增加一个提示
				manager.notify(NOTIFICATION_FLAG, notify1);
				break;
			// 默认通知 API11及之后可用
			case R.id.btn2 :
				PendingIntent pendingIntent2 = PendingIntent.getActivity(this,
						0, new Intent(this, DemoActivity.class), 0);
				// 通过Notification.Builder来创建通知，注意API Level
				// API11之后才支持
				Notification notify2 = new Notification.Builder(this)
						.setSmallIcon(R.drawable.message) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
															// icon)
						.setTicker("TickerText:" + "您有新短消息，请注意查收！")// 设置在status
																	// bar上显示的提示文字
						.setContentTitle("Notification Title")// 设置在下拉status
																// bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
						.setContentText("This is the notification message")// TextView中显示的详细内容
						.setContentIntent(pendingIntent2) // 关联PendingIntent
						.setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
						.getNotification(); // 需要注意build()是在API level
				// 16及之后增加的，在API11中可以使用getNotificatin()来代替
				notify2.flags |= Notification.FLAG_AUTO_CANCEL;
				manager.notify(NOTIFICATION_FLAG, notify2);
				break;
			// 默认通知 API16及之后可用
			case R.id.btn3 :
				PendingIntent pendingIntent3 = PendingIntent.getActivity(this,
						0, new Intent(this, DemoActivity.class), 0);
				// 通过Notification.Builder来创建通知，注意API Level
				// API16之后才支持
				Notification notify3 = new Notification.Builder(this)
						.setSmallIcon(R.drawable.message)
						.setTicker("TickerText:" + "您有新短消息，请注意查收！")
						.setContentTitle("Notification Title")
						.setContentText("This is the notification message")
						.setContentIntent(pendingIntent3).setNumber(1).build(); // 需要注意build()是在API
																				// level16及之后增加的，API11可以使用getNotificatin()来替代
				notify3.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
				manager.notify(NOTIFICATION_FLAG, notify3);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
				break;
			// 自定义通知
			case R.id.btn4 :
				// Notification myNotify = new Notification(R.drawable.message,
				// "自定义通知：您有新短信息了，请注意查收！", System.currentTimeMillis());
				Notification myNotify = new Notification();
				myNotify.icon = R.drawable.message;
				myNotify.tickerText = "TickerText:您有新短消息，请注意查收！";
				myNotify.when = System.currentTimeMillis();
				myNotify.flags = Notification.FLAG_NO_CLEAR;// 不能够自动清除
				RemoteViews rv = new RemoteViews(getPackageName(),
						R.layout.notifacation);
				rv.setTextViewText(R.id.noti_content, "hello wrold!");
				rv.setImageViewResource(R.id.noti_icon, R.drawable.app_icon);
				rv.setTextViewText(R.id.noti_title, "just a demo");
				myNotify.contentView = rv;
				Intent intent = new Intent(Intent.ACTION_MAIN);
				PendingIntent contentIntent = PendingIntent.getActivity(this,
						1, intent, 1);
				myNotify.contentIntent = contentIntent;
				manager.notify(NOTIFICATION_FLAG, myNotify);
				break;
			case R.id.btn5 :
				// 清除id为NOTIFICATION_FLAG的通知
				manager.cancel(NOTIFICATION_FLAG);
				// 清除所有的通知
				// manager.cancelAll();
				break;
			default :
				break;
		}
	}

	public void initNoti() {
		// Notification notification=Bu
	}

	boolean isload = true;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			File file = new File("mnt/sdcard/zozmom.apk");
			if (file.exists()) {
				long leng = file.length();
				Log.v("this", "leng" + leng);
				bar.setProgress((int) (leng * 100 / maxlen));
			}
		}
	};
	long maxlen = 10607839;

	public void download(View view) {
		final String file="mnt/sdcard/QQmusic.apk";
		OkHttpUtil.downLoadFile(downurl, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				if (arg1.message().toLowerCase().equals("ok")) {
					InputStream inputStream = arg1.body().byteStream();
					long max=arg1.body().contentLength();
					progressBar.setMax((int)max);
					boolean bl=FileUtil.extractResource(inputStream, file,progressBar, true);
					if(bl){
						Log.v("this", "下载成功");
					}
				}
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}
		});

	}
	// 05-16 16:09:55.313: V/this(23160): onSuccess mnt/sdcard/QQmusic.apk

	private void extracted1(String url) {
		final File file = new File("mnt/sdcard/zozmom.apk");
		if (file.exists()) {
			file.delete();
		}
		// new Thread() {
		// public void run() {
		// try {
		// while (isload) {
		// Message msg=new Message();
		// handler.sendMessage(msg);
		// sleep(100);
		// }
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }.start();
		showProgressDialog();
		// RandomAccessFile raf = new RandomAccessFile(getFileName(url), "rw");
		// raf.setLength(length);//
		XHttp.DownLoadFile(url, "mnt/sdcard/zozmom.apk",
				new CommonCallback<File>() {

					@Override
					public void onCancelled(CancelledException arg0) {
						// TODO Auto-generated method stub
						Log.v("this", "onCancelled" + arg0.toString());
					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						// TODO Auto-generated method stub
						Log.v("this", "onError" + arg0.toString());
						ToastUtil.show(DemoActivity.this, "下载失败");
						stopProgressDialog();
					}

					@Override
					public void onFinished() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(File arg0) {
						// TODO Auto-generated method stub
						stopProgressDialog();
						isload = false;
						// Log.v("this", "onSuccess" + arg0.toString());
						openFiles(arg0);
					}
				});
	}

	private void extracted(String url) {
		// URL u1;
		// try {
		// u1 = new URL(url);
		// HttpURLConnection conn = (HttpURLConnection) u1.openConnection();
		// conn.setRequestMethod("GET");
		// conn.setConnectTimeout(5000);
		// int code = conn.getResponseCode();
		// if (code == 200) {
		// maxlen = conn.getConnectTimeout();
		// Log.v("this", "int maxlen:" + maxlen);
		extracted1(url);
		// }
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private void openFiles(File file) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	public void imageload(View view) {
		// requestKey();
		// final String file1 =
		// "/sdcard/photo/temp_cutted_20160509120735224.jpg";
		// if(!((new File(file1)).exists())){
		// ToastUtil.show(this, "文件不存在");
		// return;
		// }
		new Thread() {
			public void run() {
				String ip = DeviceInfo.GetNetIp();
				Log.v("this", "ip " + ip);
			}
		}.start();

	}

	String url = "http://120.27.152.196:8888/yyzm-manage/uploadAuth/";
	OSS oss;

	public void requestKey() {

		XHttp.Get(url, null, new CommonCallback<JSONObject>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {

			}

			@Override
			public void onFinished() {

			}

			@SuppressWarnings("unused")
			@Override
			public void onSuccess(JSONObject arg0) {
				try {
					String accessid = arg0.getString("accessid");
					// String policy = arg0.getString("policy");
					String signature = arg0.getString("signature");
					String dir = arg0.getString("dir");
					// String host = arg0.getString("host");
					// String expire = arg0.getString("expire");
					String file1 = "/sdcard/photo/temp_cutted_20160509120735224.jpg";
					File file = new File("");
					if (file == null) {
						Log.v("this", "file is null");
					}

					OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
							accessid, signature);

					ClientConfiguration conf = new ClientConfiguration();
					conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
					conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
					conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
					conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
					OSSLog.enableLog();
					String endpoint = "oss-cn-hangzhou.aliyuncs.com";
					oss = new OSSClient(m_activity, endpoint,
							credentialProvider, conf);
					new PutObjectSamples(oss, "zozmom", dir, file1)
							.putObjectFromLocalFile();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void initView() {
		List<String> list = new ArrayList<String>();
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		Map<Integer, List<String>> allchildemap = new HashMap<Integer, List<String>>();
		list.add("11");
		map.put(0, list);
		list.add("11");
		map.put(1, list);
		list.add("11");
		map.put(2, list);
		list.add("11");
		map.put(3, list);
		list.add("11");
		map.put(4, list);
		allchildemap.put(0, getList(1));
		allchildemap.put(1, getList(2));
		allchildemap.put(2, getList(3));
		allchildemap.put(3, getList(4));
		allchildemap.put(4, getList(5));
		ExpandableListView listview = (ExpandableListView) findViewById(R.id.CuExpandableListview);

		// NewGoodExAdapter adapter = new NewGoodExAdapter(list, this,
		// allchildemap, listview);
		// listview.setAdapter(adapter);
	}

	private List<String> getList(int item) {
		// TODO Auto-generated method stub
		List<String> chidlist = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			chidlist.add(item + "11111" + i);
		}
		return chidlist;
	}

	public void request(View view) {
		XHttp.Get(Constant.SCREEN, null, new CommonCallback<JSONArray>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				RequestErrorUtil.judge(arg0, DemoActivity.this);
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(JSONArray arg0) {
				// TODO Auto-generated method stub
				Log.v("this", arg0.toString());
			}
		});
	}

	public void request1(View view) {
		XHttp.Get(Constant.BANNER, null, new CommonCallback<JSONArray>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				RequestErrorUtil.judge(arg0, DemoActivity.this);
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(JSONArray arg0) {
				// TODO Auto-generated method stub
				Log.v("this", arg0.toString());
			}
		});
	}

	public void loginQQ(View view) {
		init(SHARE_MEDIA.QQ);
	}

	public void loginWeixin(View view) {
		init(SHARE_MEDIA.WEIXIN);
		// wechatShare(0);
	}

	public void shareQQ(View view) {
		// init(SHARE_MEDIA.WEIXIN_CIRCLE);
		share();
	}

	public void shareWeixin(View view) {
		// wechatShare(1);
		share();
	}

	public void shareQzone(View view) {
		share();
	}

	public void shareCircle(View view) {
		share();
	}

	public void share() {
		final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,};
		UMImage image = new UMImage(this, BitmapFactory.decodeResource(
				getResources(), R.drawable.app_icon));
		new ShareAction(this).setDisplayList(displaylist)

		.withText("一元购买iphone 6s").withTitle("逐梦科技圆你梦想")
				.withTargetUrl("http://www.baidu.com")
				.setListenerList(new UMShareListener() {
					@Override
					public void onResult(SHARE_MEDIA platform) {
						Toast.makeText(DemoActivity.this, platform + " 分享成功啦",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SHARE_MEDIA platform, Throwable t) {
						Toast.makeText(DemoActivity.this, platform + " 分享失败啦",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(DemoActivity.this, platform + " 分享取消了",
								Toast.LENGTH_SHORT).show();
					}
				}).share();
	}

	public void init(SHARE_MEDIA media) {
		// SHARE_MEDIA platform = media;
		mShareAPI = UMShareAPI.get(this);
		mShareAPI.doOauthVerify(this, media, umAuthListener);

	}

	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action,
				Map<String, String> data) {
			Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText(getApplicationContext(), "授权返回", Toast.LENGTH_SHORT)
					.show();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		Log.v("this", "------");
//		mShareAPI.onActivityResult(requestCode, resultCode, data);
		infoModel=getLogginUserinfo();
	}

	// private ShareBoardlistener shareBoardlistener = new ShareBoardlistener()
	// {
	//
	// @Override
	// public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
	// if (share_media == null) {
	// if (snsPlatform.mKeyword.equals("11")) {
	// Toast.makeText(DemoActivity.this, "add button success",
	// Toast.LENGTH_LONG).show();
	// }
	//
	// } else {
	// new ShareAction(DemoActivity.this).setPlatform(share_media)
	// .setCallback(umShareListener).withText("多平台分享").share();
	// }
	// }
	// };

	// ImageView imageView;
	// WebView webview;
	// CityPicker cityPicker;
	// TextView textView ;
	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.demoactivity);
	// Button button = (Button) findViewById(R.id.demobtn);
	// textView = (TextView) findViewById(R.id.demotext);
	// imageView = (ImageView) findViewById(R.id.demoImage);
	// cityPicker = (CityPicker) findViewById(R.id.citypicker);
	//
	// cityPicker.setOnSelectingListener(new OnSelectingListener() {
	// @Override
	// public void selected(boolean selected, String province_name,
	// String city_name, String city_code) {
	// textView.setText(cityPicker.getCity_string() + "   " + selected
	// + "  " + province_name + "  " + city_name + " "
	// + city_code);
	// }
	// });
	// }

	// int i = 0;
	// List<UserPer> list;

	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// super.onClick(v);
	// switch (v.getId()) {
	// case R.id.demobtn1:
	// save("王尼玛" + i);
	// i++;
	// break;
	// case R.id.demobtn:
	// list = DatabaseDao.findAll(UserPer.class);
	// if (list == null || list.size() == 0) {
	// Log.v("this", "没有数据");
	// } else {
	// for (UserPer per : list) {
	// Log.v("this", "name :" + per.getName() + "ID" + per.getId());
	// }
	// }
	// break;
	// case R.id.demobtn2:
	// if (list != null) {
	// DatabaseDao.deleteAll(list);
	// }
	// break;
	// case R.id.demobtn3:
	// UserPer per = new UserPer();
	// per.setId(i);
	// break;
	// case R.id.demobtn4:
	// UserPer per1 = new UserPer();
	// per1.setId(10);
	// per1.setName("你大跌");
	// DatabaseDao.update(per1);
	// break;
	// case R.id.demobtn5:
	// UserPer perid=(UserPer) DatabaseDao.find(UserPer.class, "useId", "12");
	// if(perid!=null){
	// System.out.println(perid.getName());
	// Log.v("this", "perid NAME:"+perid.getName());
	// }else{
	// Log.v("this", "perid NAME:"+"null");
	// }
	// break;
	// case R.id.demobtn6:
	// DatabaseDao.addColumn(UserPer.class, "useId");
	// break;
	// case R.id.dpbtn:
	// // DensityUtil.dip2px(this,100);
	// final float scale = this.getResources().getDisplayMetrics().density;
	// ToastUtil.show(this,"密度:"+scale+" "+DensityUtil.dip2px(this,100) +"px");
	// break;
	// case R.id.pxbtn:
	// // DensityUtil.px2dip(this,630);
	// ToastUtil.show(this,DensityUtil.px2dip(this,630) +"dp");
	// break;
	// }
	// }

	// public void save(String text) {
	// UserPer person1 = new UserPer();
	// person1.setName(text);
	// person1.setAge("23");
	// person1.setUseId(26+"");
	// DatabaseDao.saveOrUpdate(person1);
	// }

//	private IWXAPI wxApi;

	// 实例化
	/**
	 * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
	 * 
	 * @param flag
	 *            (0:分享到微信好友，1：分享到微信朋友圈)
	 */
//	private void wechatShare(int flag) {
//		wxApi = WXAPIFactory.createWXAPI(this, "wx21bfd4ded7f42a50");
//		wxApi.registerApp("wx21bfd4ded7f42a50");
//		WXWebpageObject webpage = new WXWebpageObject();
//		webpage.webpageUrl = "这里填写链接url";
//		WXMediaMessage msg = new WXMediaMessage(webpage);
//		msg.title = "这里填写标题";
//		msg.description = "这里填写内容";
//		// 这里替换一张自己工程里的图片资源
//		Bitmap thumb = BitmapFactory.decodeResource(getResources(),
//				R.drawable.app_icon);
//		msg.setThumbImage(thumb);
//
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = String.valueOf(System.currentTimeMillis());
//		req.message = msg;
//		req.scene = flag == 0
//				? SendMessageToWX.Req.WXSceneSession
//				: SendMessageToWX.Req.WXSceneTimeline;
//		wxApi.sendReq(req);
//	}
	// 在需要分享的地方添加代码：
	// wechatShare(0);//分享到微信好友
	// wechatShare(1);//分享到微信朋友圈

	@Override
	public void onFailure(Call arg0, IOException arg1) {
		// TODO Auto-generated method stub
		String tag=(String) arg0.request().tag();
		Log.v("this", tag);
		Log.v("this", "onFailure" + arg1.toString());
		Log.v("this", "onFailure" + arg0.toString());
	}

	@Override
	public void onResponse(Call arg0, Response response) throws IOException {
		// TODO Auto-generated method stub
			Headers responseHeaders = response.headers();
			String tag=(String) response.request().tag();
			Log.v("this", tag);
			extracted(response, responseHeaders);

	
	}

	private void extracted(Response response, Headers responseHeaders)
			throws IOException {
		int code=response.code();
		int hashCode=response.hashCode();
		String message=response.message();
		boolean b=response.isSuccessful();
		boolean c=response.isRedirect();
		Log.v("this", "onResponse" + code);
		Log.v("this", "onResponse" + hashCode);
		Log.v("this", "message" + message);
		Log.v("this", "b" + b);
		Log.v("this", "c" + c);
		
		for (int i = 0, size = responseHeaders.size(); i < size; i++) {
			System.out.println(responseHeaders.name(i) + ": "
					+ responseHeaders.value(i));
		}
		Log.v("this", "onResponse" + response.body().string());
		Log.v("this", "onResponse" + response.toString());
	}
	
	public void postByToken(View view){
		OkHttpUtil.AsyPostByToken(Constant.GET_RECHARGE_RECORD, infoModel, null, "getrecode", this);
	}
}
