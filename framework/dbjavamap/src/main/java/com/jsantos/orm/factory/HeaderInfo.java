package com.jsantos.orm.factory;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.jsantos.orm.mt.MTField;

public class HeaderInfo {
	public static void logLabels(ArrayList<MTField> fields) {
		System.out.println(fields.stream().map(field->field.getLabel()).collect(Collectors.joining(", ")));
	}
}
