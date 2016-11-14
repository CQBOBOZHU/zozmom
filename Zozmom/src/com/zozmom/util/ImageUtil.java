package com.zozmom.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;


public class ImageUtil {
	private static Options m_options;

	public static Bitmap getBitmap(String fileName, int resize) {
		Bitmap res = null;
		if (fileName != null) {
			File f = new File(fileName);
			if (f.exists()) {
				if (m_options == null) {
					m_options = new Options();
					m_options.inPurgeable = true;
					m_options.inPreferredConfig = Config.RGB_565;
				}
				m_options.inSampleSize = resize;
				try {
					res = BitmapFactory.decodeFile(fileName, m_options);
				} catch (OutOfMemoryError e) {
				}
				if (res == null) {
				}
			}
		}
		return res;
	}

	public static Bitmap getBitmap(String fileName) {
		return getBitmap(fileName, 1);
	}

	public static void save(Bitmap bitmap, String fileName) {
		if (bitmap != null && fileName != null) {
			File f = new File(fileName);
			try {
				if (!f.exists()) {
					if (!f.createNewFile()) {
					}
				}
				FileOutputStream fOut = new FileOutputStream(f);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 下载网络图片到本地
	 * 
	 * @param path
	 * @param imageName
	 * @return
	 * @throws Exception
	 */
	public static String downloadImage(String path, String imageName)
			throws Exception {
		String sdcardPath = android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/zozmom/image";//
		File file = new File(sdcardPath);
		if (!file.exists()) {
			file.mkdir();
		}
		File file2 = new File(sdcardPath + "/" + imageName + ".png");
		URL url = new URL(path);
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setReadTimeout(10000);
		connection.setDoInput(true);
		connection.setRequestProperty("Connection", "Keep-Alive");
		InputStream in = connection.getInputStream();
		BufferedInputStream bin = new BufferedInputStream(in);
		FileOutputStream out = new FileOutputStream(file2);
		BufferedOutputStream bout = new BufferedOutputStream(out);
		byte[] buff = new byte[1024 * 5];
		int len = 0;
		while ((len = bin.read(buff)) != -1) {
			bout.write(buff, 0, len);
		}
		if (bin != null) {
			bout.flush();
			bin.close();
			in.close();
			out.close();
			bout.close();
		}
		Log.v("this", "file：" + file2.toString());
		return file2.toString();
	}
}
