package com.eastcom.baseframe.web.modules.sys.security;

import java.util.Enumeration;
import java.util.LinkedHashMap;

import com.eastcom.baseframe.common.utils.OrderProperties;
import com.eastcom.baseframe.common.utils.PropertiesUtil;

public class ShiroFilterChainDefinitionsMap extends LinkedHashMap<String, String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1493035045153694266L;

	public ShiroFilterChainDefinitionsMap() {
		initFilters();
	}

	@SuppressWarnings("rawtypes")
	private void initFilters() {
		OrderProperties p = PropertiesUtil.loadOrderdProperties("security/shiro.properties");
		if (!p.isEmpty()) {
			Enumeration e = p.propertyNames();
			while (e.hasMoreElements()) {
				String key = e.nextElement().toString();
				if (key.startsWith("/")) {
					this.put(key.toString(), p.getProperty(key.toString()));
				}
			}
		}
	}
}
