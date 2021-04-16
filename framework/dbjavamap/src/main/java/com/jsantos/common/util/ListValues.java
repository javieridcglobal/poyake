package com.jsantos.common.util;

import java.util.ArrayList;
import java.util.Collection;

public class ListValues<E> extends ArrayList<E>{

	boolean notRepeted=false;
	boolean notNull=false;
	
	private static final long serialVersionUID = 1L;

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if(null==c)return false;
		for (E e : c) {
			add(e);
		}
		return true;
	}

	@Override
	public boolean add(E e) {
		//if(null==e)return false;
		if(notRepeted && this.contains(e))return false;
		//if(notNull )return false;
		return super.add(e);
	}

	
	
	public ListValues<E> addAllValues(Collection<? extends E> c) {
		if(null!=c)
			for (E e : c) {
				add(e);
			}
		return this;
	}

	
	public ListValues<E> addValue(E e) {
		add(e);
		return this;
	}

	
	public ListValues<E> addValue(Collection<? extends E> c) {
		addAllValues(c);
		return this;
	}

	public boolean isNotRepeted() {
		return notRepeted;
	}

	public ListValues<E> setNotRepeted(boolean notRepeted) {
		this.notRepeted = notRepeted;
		return this;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public ListValues<E> setNotNull(boolean notNull) {
		this.notNull = notNull;
		return this;
	}
}
