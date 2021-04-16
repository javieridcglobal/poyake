package com.jsantos.orm.mt;

public class AbstractField {
	protected final String name;
	
	public AbstractField(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

}
