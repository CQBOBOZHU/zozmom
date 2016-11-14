package com.zozmom.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zozmom.model.ShowPicModel;

import android.util.Log;

public class JsonUtil {
	/**
	 * 2级Json 解析 (晒单解析)
	 * 
	 * @param jsonObject
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static Object JsonToShowModel(JSONObject jsonObject, Class<?> c)
			throws Exception {
		Object object;
		object = Class.forName(c.getName()).newInstance();

		Class<?> obj = object.getClass();
		Field[] fields = null;
		fields = obj.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String proName = field.getName();
			Log.v("this", "proName  ==" + proName);
			if (jsonObject.toString().contains(proName)) {
				if (proName.equals("shareimageList")) {
					JSONArray pro1 = jsonObject.getJSONArray(proName);
					List<ShowPicModel> listmodel = new ArrayList<ShowPicModel>();
					for (int i = 0; i < pro1.length(); i++) {
						ShowPicModel model = (ShowPicModel) JsonToShowModel(
								(JSONObject) pro1.get(0), ShowPicModel.class);
						listmodel.add(model);
					}
					field.set(object, listmodel);
				} else {
					if (field.getType() == int.class) {
						field.set(object, jsonObject.getInt(proName));
					} else if (field.getType() == String.class) {
						field.set(object, jsonObject.getString(proName));
					} else if (field.getType() == double.class) {
						field.set(object, jsonObject.getDouble(proName));
					} else if (field.getType() == long.class) {
						field.set(object, jsonObject.getLong(proName));
					}
				}
			}
		}

		return object;
	}

	/**
	 * 一级解析
	 * 
	 * @param jsonObject
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static <T> Object JsonToModel(JSONObject jsonObject, Class<?> c)
			throws Exception {
		Object object;
		object = Class.forName(c.getName()).newInstance();

		Class<?> obj = object.getClass();
		Field[] fields = null;
		fields = obj.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String proName = field.getName();
			if (jsonObject.toString().contains("\"" + proName + "\"")) {
				if (field.getType() == int.class) {
					field.set(object, jsonObject.getInt(proName));
				} else if (field.getType() == String.class) {
					String temp = jsonObject.getString(proName);
					if (temp != null) {
						field.set(object, jsonObject.getString(proName));
					}
				} else if (field.getType() == double.class) {
					field.set(object, jsonObject.getDouble(proName));
				} else if (field.getType() == long.class) {
					field.set(object, jsonObject.getLong(proName));
				}
			}
		}
		return object;
	}

	// public void parse(String json,String key){
	// int start=json.indexOf(key);
	// int end=start+key.length();
	// String starttmp=json.substring(start-1, start);
	// String endtmp=json.substring(end, end+1);
	// if(){
	//
	// }
	// }

	public static <T> List<T> JsonToModel(JSONArray array, Class<?> c) {
		List<T> list = new ArrayList<T>();
		if (array == null || array.length() == 0) {
			return null;
		} else {
			for (int i = 0; i < array.length(); i++) {
				try {
					@SuppressWarnings("unchecked")
					T object = (T) JsonToModel(array.getJSONObject(i), c);
					list.add(object);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public static List<Object> JsonToShowModel(JSONArray array, Class<?> c) {
		List<Object> list = new ArrayList<Object>();
		if (array == null || array.length() == 0) {
			return null;
		} else {
			for (int i = 0; i < array.length(); i++) {
				try {
					Object object = JsonToShowModel(array.getJSONObject(i), c);
					list.add(object);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * 2级Json 解析 (晒单解析)
	 * 
	 * @param jsonObject
	 * @param c
	 * @param key是二级关键字
	 * @return
	 * @throws Exception
	 */
	public static <T> Object JsonToModel(JSONObject jsonObject, Class<?> c1,
			Class<?> c2, String key) throws Exception {
		Object object;
		object = Class.forName(c1.getName()).newInstance();

		Class<?> obj = object.getClass();
		Field[] fields = null;
		fields = obj.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String proName = field.getName();
			Log.v("this", "proName  ==" + proName);
			Object pro = null;
			if (key != null && proName.equals(key)) {
				JSONArray pro1 = jsonObject.getJSONArray(proName);
				List<Object> listmodel = new ArrayList<Object>();
				for (int i = 0; i < pro1.length(); i++) {
					Object ob = JsonToModel((JSONObject) pro1.get(0), c2, null,
							null);
					listmodel.add(ob);
				}
				field.set(object, listmodel);
			} else {
				pro = jsonObject.getString(proName);
				Log.v("this", "pro  ==" + pro);
				field.set(object, pro);
			}
		}

		return object;
	}

}
