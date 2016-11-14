//package com.zozmom.pic;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.widget.Toast;
//
//public class UploadPhotoTask extends AsyncTask<List<String>, Void, String> {
//	private Context m_context;
//	private List<IPhotoUploadResultListener> m_resultListener;
//	private static final boolean DEBUG = false;
//
//	public UploadPhotoTask(Context context) {
//		m_context = context;
//	}
//
//	public Context getContext() {
//		return m_context;
//	}
//
//	public void upload(String pic) {
//		if (pic != null) {
//			List<String> to = new ArrayList<String>();
//			to.add(pic);
//			upload(to);
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public void upload(List<String> pics) {
//		this.execute(pics);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see android.os.AsyncTask#doInBackground(Params[])
//	 */
//	@Override
//	protected String doInBackground(List<String>... picsToUpload) {
//		String string = null;
//
//		try {
//
//			if (picsToUpload == null || picsToUpload.length == 0) {
//				return string;
//			}
//
//			// 上传文件到对应的bucket中去。
//			List<String> uploaded = new ArrayList<String>();
//			for (String pic : picsToUpload[0]) {
//				// 远端无需上传
//				if (pic.toLowerCase(Locale.getDefault()).startsWith("http://")) {
//					uploaded.add(pic);
//				} else {
//					File f = new File(pic);
//					if (f.exists()) {
//						uploaded.add(string);
//					}
//				}
//			}
//			dispatchUploadResult(uploaded);
//
//		} catch (Exception e) {
//		}
//
//		return string;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
//	 */
//	@Override
//	protected void onPostExecute(String result) {
//		super.onPostExecute(result);
//		if (DEBUG) {
//			if (result != null) {
//				Toast.makeText(getContext(), "upload成功", Toast.LENGTH_LONG)
//						.show();
//			} else {
//				Toast.makeText(getContext(), "upload失败", Toast.LENGTH_LONG)
//						.show();
//			}
//		}
//	}
//
//	protected void dispatchUploadResult(List<String> uploadedPics) {
//		if (m_resultListener != null && m_resultListener.size() > 0) {
//			for (IPhotoUploadResultListener listener : m_resultListener) {
//				try {
//					listener.onPicUploaded(uploadedPics);
//				} catch (Exception ex) {
//				}
//			}
//		}
//	}
//
////	public <T> void addUploadResultListener(BaseActivity<T> baseActivity) {
////		if (baseActivity != null) {
////			if (m_resultListener == null) {
////				m_resultListener = new ArrayList<IPhotoUploadResultListener>();
////			}
////			if (!m_resultListener.contains(baseActivity)) {
////				m_resultListener.add(baseActivity);
////			}
////		}
////	}
//
////	public void removeUploadResultListener(IPhotoUploadResultListener listener) {
////		if (listener != null && m_resultListener != null) {
////			m_resultListener.remove(listener);
////		}
////	}
//}