package com.eastcom.baseframe.common.config;

import com.eastcom.baseframe.common.utils.PropertiesLoader;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 全局配置类
 */
public class Global {

	private static Global config = new Global();
	
	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";
	
	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();

	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader propertiesLoader = new PropertiesLoader("global.properties");

	/**
	 * 获取当前实例对象
	 * @return
	 */
	public static Global getInstance() {
		return config;
	}

	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null) {
			value = propertiesLoader.getProperty(key);
			map.put(key, value);
		}
		return value;
	}

	public static String getConfig(String key, String defaultValue) {
		String value = map.get(key);
		if (value == null) {
			value = propertiesLoader.getProperty(key, defaultValue);
			map.put(key, value);
		}
		return value;
	}

	// ///////////////////////////////////////////////////////

	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}

}