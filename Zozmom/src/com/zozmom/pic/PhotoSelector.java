package com.zozmom.pic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.zozmom.constants.Constants;
import com.zozmom.util.FileUtil;
import com.zozmom.util.ImageUtil;
import com.zozmom.util.Util;

public class PhotoSelector {
	private static final int IMG_HEAD = 0;
	private static final int IMG_NORMAL = 1;

	protected Uri fileUri;
	protected Activity m_activity;
	protected static String m_photoPath = null;
	protected int m_photoType;

	private static String m_lastPhoto;
	private static String m_lastCropPhoto;

	public PhotoSelector(Activity act) {
		m_activity = act;
		if (m_photoPath == null) {
			m_photoPath = FileUtil.getStorePath(act, Constants.PHOTO_PATH);
		}
	}

	public boolean isCurHeadPhoto() {
		return m_photoType == IMG_HEAD;
	}

	public static void deleteTempPhotos() {
		if (m_photoPath != null) {
			File f = new File(m_photoPath);
			if (f.isDirectory()) {
				File[] files = f.listFiles();
				for (File cf : files) {
					cf.delete();
				}
			}
		}
	}

	public boolean cropImageUri(Uri uri, int outputX, int outputY) {
		boolean res = false;
		if (m_activity != null && uri != null) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", outputX);
			intent.putExtra("outputY", outputY);
			intent.putExtra("scale", true);
			intent.putExtra("scaleUpIfNeeded", true);

			File f = new File(createCuttedTempPhoto());
			f.delete();

			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

			intent.putExtra("return-data", false);
			intent.putExtra("outputFormat",
					Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true); // no face detection

			try {
				m_activity.startActivityForResult(intent,
						PhotoConstant.FROM_CUT);
				res = true;
			} catch (ActivityNotFoundException anfe) {
			}
		}
		return res;
	}

	public void startCamera(boolean isHead) {
		if (m_activity != null) {
			try {
				if (isHead) {
					m_photoType = IMG_HEAD;
				} else {
					m_photoType = IMG_NORMAL;
				}
				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				String fileName = createTempPhoto();
				File f = new File(fileName);
				if (f.exists()) {
					f.delete();
				}
				Uri imageUri = Uri.fromFile(f);
				cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 1);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				cameraIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
				m_activity.startActivityForResult(cameraIntent,
						PhotoConstant.FROM_CAMERA);
			} catch (Exception e) {
			}
		}
	}

	public void startLocalPhoto(boolean isHead) {
		if (m_activity != null) {
			try {
				if (isHead) {
					m_photoType = IMG_HEAD;
				} else {
					m_photoType = IMG_NORMAL;
				}

				String fileName = createTempPhoto();
				File f = new File(fileName);
				if (f.exists()) {
					f.delete();
				}

				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
				m_activity.startActivityForResult(intent,
						PhotoConstant.FROM_LOCAL);
			} catch (Exception e) {

				Intent contentIntent = new Intent();
				contentIntent.setAction(Intent.ACTION_GET_CONTENT);
				contentIntent.setType("zozmom/*");
				contentIntent.addCategory(Intent.CATEGORY_OPENABLE);

				Intent intent = Intent.createChooser(contentIntent, "选择照片");
				m_activity.startActivityForResult(intent,
						PhotoConstant.REQUEST_CODE_IMAGE_FAIL);
			}
		}
	}

	private String createTempPhoto() {
		m_lastPhoto = m_photoPath + "/temp_" + Util.getTime() + "."
				+ Constants.PHOTO_EXT;
		return m_lastPhoto;
	}

	public String getTempPhotoPath() {
		return m_lastPhoto;
	}

	private String createCuttedTempPhoto() {
		m_lastCropPhoto = m_photoPath + "/temp_cutted_" + Util.getTime() + "."
				+ Constants.PHOTO_EXT;
		return m_lastCropPhoto;
	}

	public String getTempCuttedPhotoPath() {
		return m_lastCropPhoto;
	}

	public String saveSelectedPhoto(Uri uri, boolean isHeadPhoto) {
		String res = null;
		if (m_activity != null && uri != null) {
			ContentResolver resolver = m_activity.getContentResolver();
			try {
				InputStream stream = resolver.openInputStream(uri);
				Bitmap localBit = BitmapFactory.decodeStream(stream);
				if (localBit != null) {
					String fileName = m_photoPath + "/"
							+ FileUtil.getRandomPath() + "."
							+ Constants.PHOTO_EXT;
					ImageUtil.save(localBit, fileName);
					res = fileName;
				}
			} catch (FileNotFoundException e) {
			}
		}

		return res;
	}
}
