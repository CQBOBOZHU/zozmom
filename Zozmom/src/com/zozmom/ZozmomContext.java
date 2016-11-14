package com.zozmom;

import com.zozmom.util.DeviceInfo;
import com.zozmom.util.UpgradeUtil;

import android.content.Context;

public class ZozmomContext {
	public static Context appContext;
//	public static String channelId;
	public static String deviceId;
	private static ZozmomContext m_zozmom;
	public static String resolution;
	public static int versionCode;
	public static String versionName;

	private ZozmomContext(Context paramContext) {
		appContext = paramContext;
		deviceId = DeviceInfo.getImei();
//		channelId = UpgradeUtil.getChannelId(paramContext);
		versionName = UpgradeUtil.getVersionName(paramContext);
		versionCode = UpgradeUtil.getVersionCode(paramContext);
		resolution = paramContext.getResources().getDisplayMetrics().widthPixels
				+ "x"
				+ paramContext.getResources().getDisplayMetrics().heightPixels;
		return;
	}

	public static final ZozmomContext getInstance(Context paramContext) {
		try {
			if (m_zozmom == null) {
				m_zozmom = new ZozmomContext(paramContext);
			}
			return m_zozmom;
		} finally {
		}
	}
}
