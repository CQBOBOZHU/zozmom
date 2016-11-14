package com.zozmom.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.common.util.LogUtil;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import com.zozmom.Myapplication;
import com.zozmom.ZozmomContext;
import com.zozmom.model.BaseModel;

import android.database.Cursor;

/**
 * 对本地数据库的操作
 * 
 * @author Administrator
 * 
 */
public class DatabaseDao {
	private static DbManager db = null;

	public static void save(Object obj) {
		if (obj != null) {
			try {
				init();
				db.save(obj);
			} catch (DbException e) {
			}
		}
	}

	public static void update(Object obj) {
		if (obj != null) {
			try {
				init();
				db.update(obj);
			} catch (DbException e) {
				LogUtil.e("save " + obj + " error", e);
			}
		}
	}

	public static void delete(Object obj) {
		if (obj != null) {
			try {
				db.delete(obj);
			} catch (DbException e) {
				LogUtil.e("delete " + obj + " error", e);
			}
		}
	}

	public static <T> T findFirst(Class cl) {
		if (cl != null) {
			try {
				init();
				return (T) db.selector(cl).findFirst();
			} catch (DbException e) {
				LogUtil.e("findFirst " + cl + " error", e);
			}
		}
		return null;
	}

	public static <T> List<T> findAll(Class<T> cl) {
		if (cl != null) {
			init();
			try {
				return db.findAll(cl);
			} catch (DbException e) {
				LogUtil.e("findAll " + cl.getName() + " error", e);
			}
		}
		return null;
	}

	public static void deleteByType(Class entityType, String TaskType) {

		init();
		try {
			db.deleteById(entityType,
					WhereBuilder.b("taskType", "==", TaskType));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static <T> void deleteAll(List<T> list) {
		init();
		try {
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					db.delete(list.get(i));
				}
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void init() {
		if (db == null) {
			db = x.getDb(((Myapplication) ZozmomContext.appContext
					.getApplicationContext()).getdaoConfig());
		}
	}

	/**
	 * 更改本地数据库
	 * 
	 * @param cls
	 */
	private static void upgrade(Class cls) {

	}

	/**
	 * 如果一个对象主键为null则会新增该对象,成功之后【会】对user的主键进行赋值绑定,否则根据主键去查找更新
	 * 
	 * @param cls
	 */
	public static void saveOrUpdate(Object cls) {
		init();
		try {
			db.saveOrUpdate(cls);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 保存成功之后【会】对user的主键进行赋值绑定,并返回保存是否成功
	 * 
	 * @param cls
	 * @return
	 */
	public static boolean saveBinding(Object obj) {
		init();
		try {
			return db.saveBindingId(obj);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static Object find(Class cls, String key, String value) {
		init();
		// db.selector(cls).where("name", "=", "骆驼").findFirst();
		try {
			return db.selector(cls).where(key, "=", value).findFirst();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// addColumn(Class<> entityType,String column) 增加列

	public static void addColumn(Class cls, String column) {
		init();
		try {
			db.addColumn(cls, column);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
