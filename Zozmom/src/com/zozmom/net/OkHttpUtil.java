package com.zozmom.net;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import com.zozmom.model.UserInfoModel;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtil {

	static OkHttpClient client = new OkHttpClient();
	static final long DEFAULT_CONNET_TIMEOUT = 5000;
	static final long DEFAULT_READ_TIMEOUT = 5000;
	static final long DEFAULT_WRITE_TIMEOUT = 5000;

	private OkHttpUtil() {
		client = new OkHttpClient.Builder()
				.connectTimeout(DEFAULT_CONNET_TIMEOUT, TimeUnit.MILLISECONDS)
				.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
				.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
				.build();
	}
	/**
	 * Get同步
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static Response Get(String url) throws Exception {
		Request request = new Request.Builder().url(url).build();
		return client.newCall(request).execute();
	}

	//
	/**
	 * Post同步
	 * 
	 * @param json
	 * @return
	 */
	public String Post(String json) {
		// RequestBody body=RequestBody.create(JSON, json);
		FormBody body = new FormBody.Builder().add("", "").build();
		Request request = new Request.Builder().post(body).url("").build();
		try {
			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				return response.body().toString();
			} else {
				return "";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * Get异步
	 * @param url
	 * @param map
	 * @param tag
	 * @param callback
	 */
	public static void AsyGet(String url, Map<String, Object> map, Object tag,
			Callback callback) {
		if (null != map) {
			int i = 0;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String value = entry.getKey() + "="
						+ entry.getValue().toString();
				if (i == 0) {
					url = url + "?" + value;
				} else {
					url = url + "&" + value;
				}
				i++;
			}
		}
		Request request = new Request.Builder().url(url).tag(tag).build();

		client.newCall(request).enqueue(callback);
	}
	/**
	 *  Post异步
	 * @param url
	 * @param map
	 * @param tag
	 * @param callback
	 */
	public static void AsyPost(String url, Map<String, Object> map, Object tag,
			Callback callback) {
		Builder builder = new FormBody.Builder();
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				builder.add(entry.getKey(), entry.getValue().toString());
			}
		}
		FormBody body = builder.build();
		Request request = new Request.Builder().url(url).post(body).tag(tag)
				.build();
		client.newCall(request).enqueue(callback);
	}

	/**
	 * 
	 * Post异步 带token
	 *
	 * @param url 
	 * @param infoModel
	 * @param map 
	 * @param tag  标签
	 * @param callback
	 */
	public static void AsyPostByToken(String url, UserInfoModel infoModel,
			Map<String, Object> map, Object tag, Callback callback) {
		Builder builder = new FormBody.Builder();
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				builder.add(entry.getKey(), entry.getValue().toString());
			}
		}
		FormBody body = builder.build();
		long time = System.currentTimeMillis();
		Request request = new Request.Builder()
				.addHeader(
						"token-param",
						time + "," + infoModel.getToken() + ","
								+ infoModel.getUserId()).url(url).post(body)
				.tag(tag).build();
		client.newCall(request).enqueue(callback);

	}

	/**
	 * 下载 (可以用get方法)
	 * 
	 * @param url
	 * @param callback
	 */
	public static void downLoadFile(String url, Callback callback) {
		Request request = new Request.Builder().url(url).build();
		client.newCall(request).enqueue(callback);
	}
	
	public static void cancleByTag(Object tag){
	}
}
