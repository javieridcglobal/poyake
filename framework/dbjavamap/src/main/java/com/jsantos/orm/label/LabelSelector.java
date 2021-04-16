package com.jsantos.orm.label;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.jsantos.orm.mt.MTEnumeration;
import com.jsantos.orm.mt.MTField;
import com.jsantos.orm.mt.MTTable;

public class LabelSelector {
	Locale defaultLocale = Locale.ENGLISH;
	private static LabelSelector inst = new LabelSelector();
	//MapValues<DetachedRecord> labeles= new MapValues<DetachedRecord>();
	
	
	private LabelSelector() {
	}
	
	public static LabelSelector inst() {
		return inst;
	}
	
	public String getLabel(Locale locale, String type, MTField field) {
		String label = findLabel(locale, type, field.getLabels());
		if (null == label && null !=field.getForeignKey() && field.getName().equals(field.getForeignKey().getPrimaryKey().getName()) )
			label = findLabel(locale, type, field.getForeignKey().getPrimaryKey().getLabels());
		if (null == label)
			label = StringUtils.capitalize(field.getName());
			
		return label;
	}

	public String getLabel(Locale locale, String type, MTTable table) {
		return getLabel(locale, type, table.getLabels(), table.getTableName());
	}

	public String getLabel(Locale locale, MTField field) {
		return getLabel(locale, null, field);
	}

	public String getLabel(String type, MTField field) {
		return getLabel(defaultLocale, type, field);
	}

	public String getLabel(String type, MTTable table) {
		return getLabel(defaultLocale, type, table.getLabels(), table.getTableName());
	}

	public String getLabel(MTField field) {
		return getLabel(defaultLocale, null, field);
	}

	public String getLabel(MTTable table) {
		return getLabel(defaultLocale, null, table.getLabels(), table.getTableName());
	}
	
	private String findLabel(Locale locale, String type, ArrayList<Label> labels) {
		for (Label label:labels) {
			if (null == type && locale.getLanguage().equals((label.getLocale().getLanguage())))
				return label.getText();
			if (locale.getLanguage().equals((label.getLocale().getLanguage())) && type.equals(label.getType()))
				return label.getText();
		}
		return null;
	}
	
	private String getLabel(Locale locale, String type, ArrayList<Label> labels, String name) {
		String label = findLabel(locale, type, labels);
		if (null != label)
			return label;
		return StringUtils.capitalize(name);
	}

	public void setDefaultLocale(Locale locale) {
		this.defaultLocale = locale;
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}
	
	public String getLabel(MTEnumeration enu, Locale locale, Integer value) {
		if (null == value)
			return null;
		if (null == locale)
			locale = getDefaultLocale();
		String label = findLabel(locale, null, enu.getLabels().get(value));
		if (null != label)
			return label;
		else
			return enu.getValue(value);
	}
	
	
	
}
