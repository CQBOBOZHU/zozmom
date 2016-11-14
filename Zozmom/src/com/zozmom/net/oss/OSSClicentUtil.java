package com.zozmom.net.oss;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

public class OSSClicentUtil {
	static OSSClient faceoss;
	static OSSClient shareoss;
	static ClientConfiguration conf;
	static OSSCredentialProvider credentialProvider;

	public static OSSClient instanceFaceOss(Context mContext) {
		if (shareoss != null) {
			return shareoss;
		}
		if (credentialProvider == null) {
			initCreaProvider();
		}
		if (conf == null) {
			initClientConf();
		}
		shareoss = new OSSClient(mContext, OSSConstant.SHARE_ENDPOINT,
				credentialProvider, conf);
		return shareoss;
	}

	public static void initCreaProvider() {
		credentialProvider = new OSSPlainTextAKSKCredentialProvider(
				OSSConstant.AccessKeyID, OSSConstant.AccessKeySecret);
	}

	public static OSSClient instanceShareOss(Context mContext) {
		if (faceoss != null) {
			return faceoss;
		}
		if (credentialProvider == null) {
			initCreaProvider();
		}
		if (conf == null) {
			initClientConf();
		}
		faceoss = new OSSClient(mContext, OSSConstant.FACE_ENDPOINT,
				credentialProvider, conf);
		return faceoss;
	}

	public static void initClientConf() {
		conf = new ClientConfiguration();
		conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
		conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
		conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
		conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
		OSSLog.enableLog();
	}

}
