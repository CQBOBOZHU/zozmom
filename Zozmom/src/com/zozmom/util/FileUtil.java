package com.zozmom.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ProgressBar;

public class FileUtil {
	public static String getRandomPath() {
		return new SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
				.format(new Date());
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("SdCardPath")
	public static String getRootStorePath(Context context) {
		String fullpath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			if (Build.VERSION.SDK_INT >= 16) {// Android 4.4以后版本路径问题
				fullpath = "/sdcard" + File.separator;
			} else {
				File sdcardDir = Environment.getExternalStorageDirectory();
				fullpath = sdcardDir.getPath() + File.separator;
			}
		} else {
			fullpath = context.getFilesDir().getPath() + File.separator;
		}
		File check = new File(fullpath);
		if (!check.exists()) {
			check.mkdirs();
		}
		return fullpath;
	}

	/**
	 * 获取指定文件夹储存路径
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public static String getStorePath(Context context, String path) {
		File storePath = new File(getRootStorePath(context) + File.separator
				+ path);
		if (!storePath.exists()) {
			storePath.mkdirs();
		}
		return storePath.getAbsolutePath();
	}

	/**
	 * 拷贝资源文件
	 * 
	 * @param source
	 * @param des
	 * @param overwrite
	 * @return
	 */
	public static boolean extractResource(InputStream source, String file,
			ProgressBar bar, boolean overwrite) {
		if (source == null || file == null) {
			return false;
		}

		File f = new File(file);
		if (f.exists()) {
			if (!overwrite) {
				return true;
			}
		} else {
			try {
				f.createNewFile();
			} catch (IOException e) {
				return false;
			}
		}

		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		try {
			input = new BufferedInputStream(source);
			output = new BufferedOutputStream(new FileOutputStream(file));
			byte[] buffer = new byte[5120];
			int count = 0;
			int i = 0;
			while ((count = input.read(buffer)) > 0) {
				output.write(buffer, 0, count);
				i += count;
				bar.setProgress(i);
			}
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (input != null)
					input.close();
				if (output != null)
					output.close();
			} catch (IOException localIOException) {
				return false;
			}
		}
	}

	/**
	 * 移动文件
	 * 
	 * @param src
	 * @param desFolder
	 */
	public static void move(String src, String desFolder) {
		File oldFile = new File(src);
		File fnewpath = new File(desFolder);
		if (!fnewpath.exists())
			fnewpath.mkdirs();
		File fnew = new File(desFolder + oldFile.getName());
		boolean moved = oldFile.renameTo(fnew);
		if (!moved) {
		}
	}

	/**
	 * 拷贝文件
	 * 
	 * @param source
	 * @param dest
	 */
	public static boolean copy(String source, String dest) {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} catch (IOException e) {
			return false;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				inputChannel.close();
				outputChannel.close();
			} catch (IOException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	public static void deleteSubFiles(String dir) {
		File file = new File(dir);
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
		}
	}

	/**
	 * 删除指定文件
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}

	public static void writeTextFile(String fileName, String content) {
		if (fileName == null) {
			return;
		}

		if (content != null) {
			try {
				FileWriter output = new FileWriter(fileName);
				output.write(content);
				output.flush();
				output.close();
			} catch (IOException e) {
			}
		}
	}

	public static String readTextFile(String fileName) {
		StringBuilder sb = new StringBuilder();
		File file = new File(fileName);
		if (file.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					sb.append(tempString + "\n");
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
					}
				}
			}
		}

		return sb.toString();
	}

	/**
	 * 获取文件的大小
	 * 
	 * @param file
	 * @return
	 */
	public static long getFileSize(String file) {
		long size = 0;
		try {
			File f = new File(file);
			size = f.length();
		} catch (Exception e) {
		}
		return size;
	}
	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 * @return
	 */
	public static long getTotalFile(File file) {
		if (file.isFile())
			return file.length();
		final File[] children = file.listFiles();
		long total = 0;
		if (children != null)
			for (File child : children) {
				System.out.println(child.getPath());
				total += getTotalFile(child);
			}
		return total;
	}

	/**
	 * 判断是否有指定类型的文件
	 * 
	 * @param path
	 * @param ext
	 * @return
	 */
	public static boolean hasExtFiles(String path, String ext) {
		List<String> list = FileUtil.getFilePathByExt(path, ext);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据后缀名获取文件列�?
	 * 
	 * @param filePath
	 * @param ext
	 * @return
	 */
	public static List<String> getFilePathByExt(String filePath, String ext) {
		List<String> results = new ArrayList<String>();
		File[] files = new File(filePath).listFiles();
		if (files == null || files.length == 0) {
			return results;
		}
		for (File file : files) {
			String fileName = file.getName();
			if (fileName.endsWith(ext)) {
				results.add(file.getAbsolutePath());
			}
		}
		return results;
	}

	public static List<String> getFileNameByExt(String filePath, String ext) {
		List<String> results = new ArrayList<String>();
		File[] files = new File(filePath).listFiles();
		if (files == null) {
			return results;
		}
		for (File file : files) {
			String fileName = file.getName();
			if (fileName.endsWith(ext)) {
				results.add(file.getName());
			}
		}
		return results;
	}

	/**
	 * 删除指定后缀名的文件
	 * 
	 * @param path
	 * @param ext
	 */
	public static void deleteFileByExt(String path, String ext) {
		List<String> list = FileUtil.getFilePathByExt(path, ext);
		if (list != null && list.size() > 0) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				deleteFile(new File(list.get(i)));
			}
		}
	}

	/**
	 * 获取文件夹列�?
	 * 
	 * @param filePath
	 * @return
	 */
	public static List<String> getDirs(String filePath) {
		List<String> dirs = new ArrayList<String>();
		File[] files = new File(filePath).listFiles();
		if (files == null) {
			return dirs;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				dirs.add(file.getAbsolutePath());
			}
		}
		return dirs;
	}

	/**
	 * 从uri获取真实文件路径
	 * 
	 * @param context
	 * @param contentUri
	 * @return
	 */
	public static String getFilePathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = {MediaStore.Images.Media.DATA};
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * 重新验证数据库数据：暂时测试
	 * 
	 * @param context
	 */
	public static void reloadDb(Context context) {
		// List<String> dirs = getDirs(getRootStorePath(context));
		// DbUtils dbUtils = DbUtils.create(context);
		// // dbUtils.configDebug(true);
		// try {
		// dbUtils.deleteAll(Upload.class);
		// int size = dirs.size();
		// for (int i = 0; i < size; i++) {
		// Upload upload = new Upload();
		// upload.setTitle("bug_" + i);
		// upload.setFilePath(dirs.get(i));
		// upload.setResult(Constant.RESULT_FAILED);
		// upload.setUsername(Loginer.instance(context).getUsername());
		// upload.setProcessing(false);
		// upload.setCreateDate(new Date());
		// dbUtils.save(upload);
		// }
		// } catch (DbException e) {
		// LogUtils.e(Log.getStackTraceString(e));
		// }
	}

	/**
	 * 读取文本数据
	 * 
	 * @param context
	 *            程序上下文
	 * @param fileName
	 *            文件名
	 * @return String, 读取到的文本内容，失败返回null
	 */
	public static String readAssets(Context context, String fileName) {
		InputStream is = null;
		String content = null;
		try {
			is = context.getAssets().open(fileName);
			if (is != null) {

				byte[] buffer = new byte[1024];
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				while (true) {
					int readLength = is.read(buffer);
					if (readLength == -1)
						break;
					arrayOutputStream.write(buffer, 0, readLength);
				}
				is.close();
				arrayOutputStream.close();
				content = new String(arrayOutputStream.toByteArray());

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			content = null;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return content;
	}
}
