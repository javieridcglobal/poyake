package com.jsantos.orm.mt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

import com.jsantos.orm.label.Label;

public abstract class MTEnumeration {
	public abstract LinkedHashMap<Integer, String> getHashmap();
	public abstract LinkedHashMap<String, Integer> getShortCodes();
	public String getValue(Integer key){return getHashmap().get(key);}
	public Set<Integer> getKeys(){return getHashmap().keySet();}
	public Collection<String> getValues(){return getHashmap().values();}
	public Integer getKeyForValue(String value){
		for (Integer o:getHashmap().keySet())
			if (getHashmap().get(o).equals(value))
				return o;
		return null;
	}
	public String getShortCode(Integer key) {
		for (String shortCode:getShortCodes().keySet())
			if (key == getShortCodes().get(shortCode))
				return shortCode;
		return null;
	};
	
	
	public abstract LinkedHashMap<Integer, ArrayList<Label>> getLabels();
	
}
