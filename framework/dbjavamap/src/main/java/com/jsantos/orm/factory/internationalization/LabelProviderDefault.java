package com.jsantos.orm.factory.internationalization;

import java.util.Locale;

import com.jsantos.orm.mt.MTField;
import com.jsantos.orm.mt.MTTable;

public class LabelProviderDefault implements ILabelProvider{

	@Override
	public String get(Object label, Locale locale) {
		return label.toString();
	}

	@Override
	public String get(MTField mtField, Locale locale) {
		return mtField.getLabel();
	}

	@Override
	public String getScreenSearchSql(MTTable mTtable) {
		return null;
	}

	@Override
	public boolean isImplemented() {
		return false;
	}
}
