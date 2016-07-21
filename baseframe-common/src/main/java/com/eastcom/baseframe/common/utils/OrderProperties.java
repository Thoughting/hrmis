package com.eastcom.baseframe.common.utils;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class OrderProperties extends Properties {

	private Vector _names;

	public OrderProperties() {
		super();
		_names = new Vector();
	}

	public Enumeration propertyNames() {
		return _names.elements();
	}

	public Object put(Object key, Object value) {
		if (_names.contains(key)) {
			_names.remove(key);
		}

		_names.add(key);

		return super.put(key, value);
	}

	public Object remove(Object key) {
		_names.remove(key);

		return super.remove(key);
	}

}
