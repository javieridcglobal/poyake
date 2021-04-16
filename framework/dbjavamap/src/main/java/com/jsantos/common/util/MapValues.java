package com.jsantos.common.util;


import java.util.LinkedHashMap;

/**
 * @author raul ripoll
 */

public class MapValues<T> extends LinkedHashMap<String,T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public MapValues<T> add(String key,T value) {
		this.put(key, value);
		return this;
	}
	
	public MapValues<T> add(MapValues<T> values) {
		if(null!=values)
			this.putAll(values);
		return this;
	}
	
	public MapValues<T> add(LinkedHashMap<String,T> values) {
		if(null!=values)
			this.putAll(values);
		return this;
	}
	
}

