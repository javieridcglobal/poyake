package com.jsantos.orm.mt;

public class MTDataType {
	private String name;
	private String sqlNativeType;
	private String javaType;
	private MTDataType subTypeOf;
	private boolean hasPrecissionAndScale;
	private boolean hasLength;
	
	public MTDataType(String name, String sqlNativeType, String javaType, MTDataType subTypeOf, boolean hasPrecissionAndScale, boolean hasLength) {
		this.name = name;
		this.sqlNativeType = sqlNativeType;
		this.javaType = javaType;
		this.subTypeOf = subTypeOf;
		this.hasPrecissionAndScale = hasPrecissionAndScale;
		this.hasLength = hasLength;
	}
	
	public boolean isSubType() {
		return null != subTypeOf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSqlNativeType() {
		return sqlNativeType;
	}

	public void setSqlNativeType(String sqlNativeType) {
		this.sqlNativeType = sqlNativeType;
	}

	public MTDataType getSubTypeOf() {
		return subTypeOf;
	}

	public void setSubTypeOf(MTDataType subTypeOf) {
		this.subTypeOf = subTypeOf;
	}

	public boolean isHasPrecissionAndScale() {
		return hasPrecissionAndScale;
	}

	public void setHasPrecissionAndScale(boolean hasPrecissionAndScale) {
		this.hasPrecissionAndScale = hasPrecissionAndScale;
	}

	public boolean isHasLength() {
		return hasLength;
	}

	public void setHasLength(boolean hasLength) {
		this.hasLength = hasLength;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	
	
}
