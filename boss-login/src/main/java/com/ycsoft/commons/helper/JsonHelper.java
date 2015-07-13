package com.ycsoft.commons.helper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 对JSON数据格式的字符串进行处理。
 * 依赖于Gson框架
 *
 * @author hh
 * @date Mar 10, 2010 8:19:43 PM
 */
public class JsonHelper {

	public static Gson gson = new Gson();
	public static JsonParser parser = new JsonParser();

	/**
	 * 将一个对象重新转换为Json串
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String fromObject(Object o)throws Exception{
		return gson.toJson( o );
	}


	/**
	 * 将Json转换为一个<code>java.util.Map</code>
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> toMap(String json)throws Exception{
		Type t = new TypeToken<Map<String ,String>>(){}.getType();
		Map<String ,String> r = gson.fromJson(json, t);
		return r ;
	}

	/**
	 * 将JSON字符串转换为一个JavaBean对象
	 * @param json 字符串
	 * @param cls JavaBean对象的Class
	 */
	public static <T> T toObject(String json ,Class<T> cls)throws Exception{
		return gson.fromJson(json, cls);
	}

	/**
	 * 将对象转换为List集合
	 * @param json 需要处理的字符串
	 * @param cls 集合中每一项的Class类型
	 * @throws Exception
	 */
	public static <T> List<T> toList(String json, Class<T> t)throws Exception{
		Type type = new TypeToken<List<T>>(){}.getType();
		List<T> rs = gson.fromJson(json, type);
		return rs;
	}

}
