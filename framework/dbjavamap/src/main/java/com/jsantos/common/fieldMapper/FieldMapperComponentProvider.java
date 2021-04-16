package com.jsantos.common.fieldMapper;

import java.util.LinkedHashMap;

import com.jsantos.orm.mt.MTDataType;
import com.jsantos.orm.mt.MTField;

public class FieldMapperComponentProvider {
	static final LinkedHashMap<MTField, IFieldMapper> byMTField = new LinkedHashMap<>();
	static final LinkedHashMap<MTDataType, IFieldMapper> byModelDataType = new LinkedHashMap<>();

	
	
	
	public static IFieldMapper getMapper(MTField field) {
		if (byMTField.containsKey(field))
			return  byMTField.get(field);
		if (byModelDataType.containsKey(field.getDataType()))
			return byModelDataType.get(field.getDataType());
		return null;
		
	}
	
	
	public static LinkedHashMap<MTField, IFieldMapper> getBymtfield() {
		return byMTField;
	}

	public static LinkedHashMap<MTDataType, IFieldMapper> getByModelDataType() {
		return byModelDataType;
	}

	public static void logBindings() {
		System.out.println(" Field Mapper Component Provider: =================================================");
		for (MTDataType modelDataType:byModelDataType.keySet())
			System.out.println("\t DataType: " + modelDataType + " -> " + byModelDataType.get(modelDataType).getClass().getSimpleName());
		for (MTField field:byMTField.keySet())
			System.out.println("\t Entity Field: " + field.getTable() + "."+ field.getName() + " -> " + byMTField.get(field).getClass().getSimpleName());
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("");
	}
	
}
