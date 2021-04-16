package com.jsantos.common.registry;

import java.util.Locale;

import com.jsantos.orm.dbstatement.DetachedRecord;
import com.jsantos.orm.mt.MTDataType;
import com.jsantos.orm.mt.MTField;

public interface IFieldRenderer {
	
	public default  MTField forField() {return null;};
	public default  MTDataType forModelDataType() {return null;}
	public String render(Object value,MTField mtField, DetachedRecord values, Locale locale);
}
