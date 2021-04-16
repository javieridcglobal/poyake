package com.jsantos.common.registry;

import java.util.LinkedHashMap;

import com.jsantos.orm.mt.MTDataType;
import com.jsantos.orm.mt.MTField;

public class FieldRendererProvider {
	static final LinkedHashMap<MTField, IFieldRenderer> byMTField = new LinkedHashMap<>();
	static final LinkedHashMap<MTDataType, IFieldRenderer> byModelDataType = new LinkedHashMap<>();
	
	
	public static void initialize() {
		
	}
	
	
	public static IFieldRenderer getRenderer(MTField field) {
		if (byMTField.containsKey(field))
			return byMTField.get(field);
		if (byMTField.containsKey(field.getSameAs()))
			return byMTField.get(field.getSameAs());
		if (byModelDataType.containsKey(field.getDataType()))
			return byModelDataType.get(field.getDataType());
		return null;
		
	}

	

	public static LinkedHashMap<MTField, IFieldRenderer> getBymtfield() {
		return byMTField;
	}

	public static LinkedHashMap<MTDataType, IFieldRenderer> getByModelDataType() {
		return byModelDataType;
	}

	public static void logBindings() {
		System.out.println("Field Renderers: ===============================================");
		for (MTDataType modelDataType:byModelDataType.keySet())
			System.out.println("\t DataType: " + modelDataType.getName() + " -> " + byModelDataType.get(modelDataType).getClass().getSimpleName());
		for (MTField field:byMTField.keySet()) {
			System.out.print("\t Entity Field: " + field.getTable() + "."+ field.getName() + " -> "); 
			System.out.println(byMTField.get(field).getClass().getSimpleName());
		}
		System.out.println("-----------------------------------------------------");
		System.out.println("");
	}
	
}
