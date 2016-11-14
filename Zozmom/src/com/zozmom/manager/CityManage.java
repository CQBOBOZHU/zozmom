//package com.zozmom.manager;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class CityManage {
//	/** 省的集合 */
//	 ArrayList<String> province_list = new ArrayList<String>();
//	/** 市的集合 */
//	 HashMap<String, ArrayList<String>> city_map = new HashMap<String, ArrayList<String>>();
//	/** 县的集合 */
//	 HashMap<String, ArrayList<String>> couny_map = new HashMap<String, ArrayList<String>>();
//	/** 数据初始化的对象 */
//	
//	static CityManage ciyManage;
//	public static CityManage instance(){
//		if(ciyManage==null){
//			ciyManage=new CityManage();
//		}
//		return ciyManage;
//	}
//	
//
//	public ArrayList<String> getProvince_list() {
//		return province_list;
//	}
//
//	public void setProvince_list(ArrayList<String> province_list) {
//		this.province_list = province_list;
//	}
//
//	public HashMap<String, ArrayList<String>> getCity_map() {
//		return city_map;
//	}
//
//	public void setCity_map(HashMap<String, ArrayList<String>> city_map) {
//		this.city_map = city_map;
//	}
//
//	public HashMap<String, ArrayList<String>> getCouny_map() {
//		return couny_map;
//	}
//
//	public void setCouny_map(HashMap<String, ArrayList<String>> couny_map) {
//		this.couny_map = couny_map;
//	}
//
//}
