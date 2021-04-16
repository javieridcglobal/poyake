package com.jsantos.common.util;

import java.util.Map.Entry;

import com.jsantos.orm.mt.MTField;


public class FieldValues extends MTMapValues<Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public void set(MTField field, Object o) {
		put(field, o);
	}
	
	
	public MTMapValues<Object> getValues() {
		return this;
	}

		
	public MapValues<Object> getPlainValues(){
		MapValues<Object> retValues= new MapValues<Object>();
		for (Entry<MTField, Object> item : entrySet()) {
			retValues.put(item.getKey().getName(), item.getValue());
		}
		return retValues;
	}
}
