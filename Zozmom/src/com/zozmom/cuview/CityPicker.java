package com.zozmom.cuview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chasedream.zhumeng.R;
import com.zozmom.cuview.ScrollerNumberPicker.OnSelectListener;
import com.zozmom.model.Cityinfo;
import com.zozmom.util.FileUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

;

/**
 * 城市Picker
 * 
 * @author LOVE
 * 
 */
public class CityPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	private ScrollerNumberPicker counyPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时索引 主要解决第一次重复触发的问题 */
	// 如:第一个选择了县,并且改变了值, 这时如果在选择市,即使不改变值,只要触发,县就会初始化,此处就是解决这个问题的
	private int tempProvinceIndex = -1;
	private int temCityIndex = -1;
	private int tempCounyIndex = -1;
	/** 上下文 */
	private Context context;
	/** 省的集合 */
	private ArrayList<String> province_list = new ArrayList<String>();
	/** 市的集合 */
	private HashMap<String, ArrayList<String>> city_map = new HashMap<String, ArrayList<String>>();
	// /** 县的集合 */
	private HashMap<String, ArrayList<String>> couny_map = new HashMap<String, ArrayList<String>>();
	/** 数据初始化的对象 */
	private JSONParser parser;
	/** 当前选中的省、市、县、及县代码的索引 */
	private String province_name = "重庆市";
	private String city_name = "重庆市";
	private String couny_name = "重庆";

	private String city_string;

	public CityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getaddressinfo();

	}

	public CityPicker(Context context) {
		super(context);
		this.context = context;
		getaddressinfo();

	}

	/** 获取城市信息 */
	private void getaddressinfo() {

		// 读取文件里的城市信息
		String area_str = FileUtil.readAssets(context, "area.json");
		// 解析城市信息

		JSONObject obj;
		try {
			obj = new JSONObject(area_str);
			Iterator it = obj.keys();
			while (it.hasNext()) {
				String province = (String) it.next();
				province_list.add(province);
				JSONArray array = obj.getJSONArray(province);
				JSONObject jsonObject = array.getJSONObject(0);
				Iterator ad = jsonObject.keys();
				ArrayList<String> Mcitylist = new ArrayList<String>();
				while (ad.hasNext()) {
					String cityname = (String) ad.next();
					JSONArray array1 = jsonObject.getJSONArray(cityname);
					ArrayList<String> countylist = new ArrayList<String>();
					for (int i = 0; i < array1.length(); i++) {
						countylist.add(array1.getString(i));
					}
					couny_map.put(cityname, countylist);
					Mcitylist.add(cityname);
				}
				city_map.put(province, Mcitylist);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	/**
	 * 进行map、 list的初始化
	 * 
	 * @author LOVE
	 * 
	 */
	public static class JSONParser {

		/**
		 * 获取县的ArrayList
		 * 
		 * @param JSONString
		 *            所有城市的json对象
		 * @param key
		 *            省的数组对象名称
		 * @return 所有省的ArrayList
		 */
		public ArrayList<String> getJSONParserResult(String JSONString,
				String key) {
			ArrayList<String> list = new ArrayList<String>();
			try {
				JSONObject dataJson = new JSONObject(JSONString);
				JSONArray data = dataJson.getJSONArray(key);
				for (int i = 0; i < data.length(); i++) {
					list.add(data.getString(i));
				}
				return list;

			} catch (JSONException e) {
				list.add("失败");
				return list;
			}
		}

		/**
		 * 获取市的ArrayList
		 * 
		 * @param JSONString
		 *            所有城市的json对象
		 * @param key
		 *            市的数组对象名称
		 * @return 所有市的map
		 */
		public HashMap<String, ArrayList<String>> getJSONParserResultArray(
				String JSONString, String key) {
			HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();

			try {
				JSONObject dataJson = new JSONObject(JSONString);
				// 解析第一层所有省数组
				JSONArray data = dataJson.getJSONArray(key);
				for (int i = 0; i < data.length(); i++) {
					// 这里的数据为['黑龙江', ['哈尔滨','齐齐哈尔','牡丹江','佳木斯']]
					JSONArray cityinfo = (JSONArray) data.get(i);
					// 下标1，也就是['哈尔滨','齐齐哈尔','牡丹江','佳木斯']
					JSONArray city = (JSONArray) cityinfo.get(1);
					// 生成list
					ArrayList<String> list = new ArrayList<String>();
					// 遍历下表为1的数组，加入list集合中
					for (int j = 0; j < city.length(); j++) {

						list.add(city.getString(j));
					}
					// 将集合加入map集合中键为下标0的元素
					hashMap.put(cityinfo.get(0) + "", list);

				}

				return hashMap;
			} catch (JSONException e) {
				return null;
			}
		}

		/**
		 * 获取县的ArrayList
		 * 
		 * @param JSONString
		 *            所有城市的json对象
		 * @param key
		 *            县的数组对象名称
		 * @return 所有县的map
		 */
		public HashMap<String, ArrayList<Cityinfo>> getJSONParserResultArray2(
				String JSONString, String key) {
			HashMap<String, ArrayList<Cityinfo>> hashMap = new HashMap<String, ArrayList<Cityinfo>>();
			try {
				JSONObject dataJson = new JSONObject(JSONString);
				// 解析第一层所有省数组
				JSONArray data = dataJson.getJSONArray(key);
				for (int i = 0; i < data.length(); i++) {
					// 这里的数据为['七台河', [['七台河', '101051002'], ['勃利',
					// '101051003']]],
					JSONArray cityinfo = (JSONArray) data.get(i);
					// 下标1，也就是[['七台河', '101051002'], ['勃利', '101051003']]
					JSONArray city = (JSONArray) cityinfo.get(1);
					// 生成list
					ArrayList<Cityinfo> list = new ArrayList<Cityinfo>();
					// 遍历下表为1的数组，加入list集合中
					for (int j = 0; j < city.length(); j++) {
						// 这里得到['七台河', '101051002']城市与代码的集合
						JSONArray city2 = (JSONArray) city.get(j);
						// 生成Cityinfo对象加入集合中
						list.add(new Cityinfo(city2.getString(0), city2
								.getString(1)));
					}
					// 将集合加入map集合中键为下标0的元素
					hashMap.put(cityinfo.get(0) + "", list);
				}
				return hashMap;
			} catch (JSONException e) {
				return null;
			}
		}

		/**
		 * 转换ArrayList < Cityinfo >为ArrayList < String >
		 * 
		 * @param oldlist
		 *            ArrayList < Cityinfo >
		 * @return ArrayList < String >
		 */
		public ArrayList<String> toArrayList(ArrayList<Cityinfo> oldlist) {
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < oldlist.size(); i++) {
				list.add(oldlist.get(i).getCity_name());
			}
			return list;

		}

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);

		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
		counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);

		String privon=province_list.get(29);
		ArrayList<String> citylist=city_map.get(privon);
		String city=citylist.get(0);
		provincePicker.setData(province_list);
		cityPicker.setData(citylist);
		counyPicker.setData(couny_map.get(city));

		provincePicker.setDefault(29);
		cityPicker.setDefault(0);
		counyPicker.setDefault(0);
		// 设置省控件的监听器
		provincePicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null) {
					return;
				}
				if (tempProvinceIndex != id) {
					String selectDay = cityPicker.getSelectedText();
					if (selectDay == null || selectDay.equals("")) {
						return;
					}
					String selectMonth = counyPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals("")) {
						return;
					}
					if (temCityIndex < 0) {
						temCityIndex = 0;
					}
					if (tempCounyIndex < 0) {
						tempCounyIndex = 0;
					}

					// 更改省的名称
					province_name = text;

					// 设置市的数据内容
					cityPicker.setData(city_map.get(text));
					cityPicker.setDefault(0);
					city_name = cityPicker.getSelectedText();
					// 设置县的数据内容
					counyPicker.setData(couny_map
							.get(cityPicker.getSelectedText()));
					counyPicker.setDefault(0);
					couny_name = counyPicker.getSelectedText();

				}
				tempProvinceIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
			}
		});
		// 设置市控件的监听器
		cityPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null) {
					return;
				}
				if (temCityIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals("")) {
						return;
					}
					String selectMonth = counyPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals("")) {
						return;
					}
					if (tempCounyIndex < 0) {
						tempCounyIndex = 0;
					}
					if (tempProvinceIndex < 0) {
						tempProvinceIndex = 0;
					}
					city_name = text;
					// 设置县的数据内容
//					if(couny_map.size()==0){
//						ArrayList<String> list = new ArrayList<String>();
//						list.add(city_name);
//						couny_map.put(city_name,list);
//					}
					counyPicker.setData(couny_map.get(text));
					counyPicker.setDefault(0);

					couny_name = counyPicker.getSelectedText();

				}
				temCityIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {

			}
		});
		// 设置县控件的监听器
		counyPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {

				if (text.equals("") || text == null) {
					return;
				}
				if (tempCounyIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals("")) {
						return;
					}
					String selectMonth = cityPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals("")) {
						return;
					}
					if (temCityIndex < 0) {
						temCityIndex = 0;
					}
					if (tempProvinceIndex < 0) {
						tempProvinceIndex = 0;
					}

					// 改变县的名称
					couny_name = text;
					// 在县集合中得到城市的天气代码

				}
				tempCounyIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {

			}
		});
	}

	// 这是用来更新界面，和绑定监听器值的
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true, province_name,
							city_name, couny_name);
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 绑定监听器
	 * 
	 * @param onSelectingListener
	 *            控件的监听器接口
	 */
	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	/**
	 * 得到所选择的省的名称
	 * 
	 * @return 省的名称
	 */
	public String getprovince_name() {
		return province_name;
	}

	/**
	 * 得到所选择的市的名称
	 * 
	 * @return 市的名称
	 */
	public String getcity_name() {
		return city_name;
	}

	/**
	 * 得到所选择的县的名称
	 * 
	 * @return 省县的名称
	 */
	public String getcouny_name() {
		return couny_name;
	}


	/**
	 * 得到所选择的省--市--县
	 * 
	 * @return省--市--县
	 */
	public String getCity_string() {
		city_string = provincePicker.getSelectedText() + "--"
				+ cityPicker.getSelectedText();
		return city_string;
	}

	/**
	 * 监听器接口
	 * 
	 * @author LOVE
	 * 
	 */
	public interface OnSelectingListener {

		/**
		 * @param selected
		 *            是否选择该控件？？？
		 * @param province_name
		 *            省的名称
		 * @param city_name
		 *            市的名称
		 * @param couny_name
		 *            县的名称
		 * @param city_code
		 *            城市天气代码
		 */
		public void selected(boolean selected, String province_name,
				String city_name, String couny_name);
	}
}
