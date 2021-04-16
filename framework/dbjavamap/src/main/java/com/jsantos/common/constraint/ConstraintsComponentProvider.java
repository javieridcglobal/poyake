package com.jsantos.common.constraint;

import java.util.LinkedHashMap;
import java.util.Map;

import com.jsantos.common.util.ListValues;
import com.jsantos.orm.mt.MTDataType;
import com.jsantos.orm.mt.MTField;

public class ConstraintsComponentProvider {
	
	static final Map<MTField, ListValues<IConstraintsBuilder>> byMTField = new LinkedHashMap<>();
	static final Map<MTDataType, ListValues<IConstraintsBuilder>> byModelDataType = new LinkedHashMap<>();
	
	
	
	public static ListValues<IConstraintsBuilder> getConstraintsComponent(MTField mtField){
		ListValues<IConstraintsBuilder> constraints= new ListValues<>();

		if (byMTField.containsKey(mtField))
			constraints.addAll(byMTField.get(mtField));
		if (byMTField.containsKey(mtField.getSameAs()))
			constraints.addAll(byMTField.get(mtField.getSameAs()));
		if (byModelDataType.containsKey(mtField.getDataType()))
			constraints.addAll(byModelDataType.get(mtField.getDataType()));
		

		return constraints;
	}
	
	public static Map<MTField, ListValues<IConstraintsBuilder>> getBymtfield() {
		return byMTField;
	}

	public static Map<MTDataType, ListValues<IConstraintsBuilder>> getByModelDataType() {
		return byModelDataType;
	}
	

	public static void logBindings() {
		for (MTDataType modelDataType:byModelDataType.keySet())
			for (IConstraintsBuilder iConstraintsBuilder : byModelDataType.get(modelDataType)) 
				System.out.println("Field Component Provider: " + modelDataType + " -> " + iConstraintsBuilder.getClass().getSimpleName());
		for (MTField field:byMTField.keySet())
			for (IConstraintsBuilder iConstraintsBuilder : byMTField.get(field)) 
			System.out.println("Field Component Provider: " + field.getTable() + "."+ field.getName() + " -> " + iConstraintsBuilder.getClass().getSimpleName());
		
	}
}
