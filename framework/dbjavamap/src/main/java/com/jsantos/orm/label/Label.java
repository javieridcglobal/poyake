package com.jsantos.orm.label;

import java.util.Locale;

public class Label {
	String type;
	Locale locale;
	String text;

	public Label(String type, String lang, String text) {
		this.type = type;
		this.locale = fromLangString(lang);
		this.text = text;
	}
	
	public Label(String type, String lang) {
		this.type = type;
		this.locale = fromLangString(lang);
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setLang(String lang) {
		this.locale = fromLangString(lang);
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	Locale fromLangString(String langString) {
		return new Locale(langString.substring(0,2));
	}
	
}
