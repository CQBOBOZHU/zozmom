package com.zozmom.net;

import java.lang.reflect.Type;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import com.zozmom.model.UserInfoModel;

import android.util.Log;

public class XHttp {

	/**
	 * 发送get请求
	 * 
	 * @param <T>
	 */
	public static <T> Cancelable Get(String url, Map<String, String> map,
			CommonCallback<T> callback) {
		RequestParams params = new RequestParams(url);
		if (null != map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				params.addQueryStringParameter(entry.getKey(), entry.getValue());
			}
		}
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}

	/**
	 * 发送post请求
	 * 
	 * @param <T>
	 */
	public static <T> Cancelable Post(String url, Map<String, Object> map,
			CommonCallback<T> callback) {
		Log.v("this", "url" + url);
		RequestParams params = new RequestParams(url);
		// params.setHeader("", "")
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		Cancelable cancelable = x.http().post(params, callback);
		return cancelable;
	}
	/**
	 * 发送post请求
	 * 
	 * @param <T>
	 */
	public static <T> Cancelable PostByToken(String url,
			UserInfoModel infoModel, Map<String, Object> map,
			CommonCallback<T> callback) {
		Log.v("this", "url" + url);
		RequestParams params = new RequestParams(url);
		long time = System.currentTimeMillis();
		params.addHeader("token-param", time + "," + infoModel.getToken() + ","
				+ infoModel.getUserId());
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		Cancelable cancelable = x.http().post(params, callback);
		return cancelable;
	}

	/**
	 * 上传文件
	 * 
	 * @param <T>
	 */
	public static <T> Cancelable UpLoadFile(String url,
			Map<String, Object> map, CommonCallback<T> callback) {
		RequestParams params = new RequestParams(url);
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		params.setMultipart(true);
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}

	/**
	 * 下载文件
	 * 
	 * @param <T>
	 */
	public static <T> Cancelable DownLoadFile(String url, String filepath,
			CommonCallback<T> callback) {
		RequestParams params = new RequestParams(url);
		// 设置断点续传
		params.setAutoResume(true);
		params.setSaveFilePath(filepath);
		// params.setRedirectHandler(redirectHandler);
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}

}
