package com.jsantos.orm.factory.internationalization;

import java.util.Locale;

import com.jsantos.orm.mt.MTField;
import com.jsantos.orm.mt.MTTable;

public interface ILabelProvider {
	
	public  String  get(Object label, Locale locale) ;
	

	public  String  get(MTField mtField, Locale locale);
	
	public boolean isImplemented();
	
	public  String getScreenSearchSql(MTTable mTtable);
	
}
