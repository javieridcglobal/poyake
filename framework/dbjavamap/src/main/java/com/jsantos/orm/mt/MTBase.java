package com.jsantos.orm.mt;

import java.util.LinkedHashMap;

public abstract class MTBase {
	protected static LinkedHashMap<String, MTTable> tables = new LinkedHashMap<String, MTTable>();
	protected static LinkedHashMap<String, MTEnumeration> enums = new LinkedHashMap<String, MTEnumeration>();

	public static MTTable getTable(String tableName) {
		//it need to be fixed correctly
		String tableNamewithoutSchema=tableName.contains(".")?tableName.substring(tableName.lastIndexOf(".")+1):tableName;
		return tables.get(tableNamewithoutSchema.toUpperCase());
	}

	public static LinkedHashMap<String, MTTable> getTables() {
		return tables;
	}

	public static LinkedHashMap<String, MTEnumeration> getEnums() {
		return enums;
	}

	public static MTEnumeration getEnum(String enumName) {
		return enums.get(enumName.toUpperCase());
	}

	public static MTField getMTField(String fullyQualifiedFieldName){
			if (null != fullyQualifiedFieldName){
				if (!fullyQualifiedFieldName.contains("."))
					throw new RuntimeException("The field name " + fullyQualifiedFieldName + " is not fully qualified. doesn't contain a dot");

				int lastDotPosition = fullyQualifiedFieldName.lastIndexOf('.');

				MTTable table = MTBase.getTable(fullyQualifiedFieldName.substring(0, lastDotPosition));
				if (null == table)
					throw new RuntimeException("Table " + fullyQualifiedFieldName.substring(0, lastDotPosition) + " doesn't exist!!");
				MTField field = table.getField(fullyQualifiedFieldName.substring(lastDotPosition + 1, fullyQualifiedFieldName.length()));
				if (null == field)
					throw new RuntimeException("Field " + fullyQualifiedFieldName.substring(lastDotPosition + 1, fullyQualifiedFieldName.length()) + " doesn't exist!!");
				return field;
			}
		throw new RuntimeException("Can't find fully qualified field name: " + fullyQualifiedFieldName );
	}

}
