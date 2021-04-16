package com.jsantos.orm.mt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsantos.orm.label.Label;

public abstract class MTTable {
	public abstract ArrayList<MTField> getFields();
	protected String tableName;
	//@JsonIgnore
	protected List<MTField> primaryKeys = new ArrayList<MTField>();
	protected MTEnumeration enumeration = null;
	protected String schema = null;
	protected String entityType = null;
	protected String sql = null;
	protected ArrayList<Label> labels = new ArrayList<>();
	//@JsonIgnore
	protected MTTable extendsTable = null;
	protected ArrayList<String> patterns = new ArrayList<>();
	protected ArrayList<String> stereotypes = new ArrayList<>();
	
	public String getFullTableName() {
		if (StringUtils.isNotEmpty(schema)) return schema + "." + tableName;
		else return tableName;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String toString(){
		return tableName;
	}

	public MTField getIdField() {
		for (MTField field:getFields())
			if (field.getStereoTypes().contains("DESCRIPTION"))
				return field;
		return null;
	}
	
	public List<MTField> getPrimaryKeys(){
		return primaryKeys;
	}
	
	public MTField getPrimaryKey() {
		for (MTField field: getPrimaryKeys()) {
			if (! field.getName().equalsIgnoreCase("REV")) {
				return field;
			}
		}
		return null;
	}
	
	public Boolean getIsEnumeration() {
		return patterns.contains("Enumeration");
	}

	public String getEnumerationValue(Integer key){
		return enumeration.getValue(key);
	}

	public MTEnumeration getEnumeration(){
		return enumeration;
	}

	@Override
	public boolean equals(Object obj) {
		return obj.toString().equals(this.toString());
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getEntityType() {
		return entityType;
	}

	public String getSql() {
		if ("QUERY".equals(entityType))
			return sql;
		else
			return "select * from " + getFullTableName();
	}

	public ArrayList<Label> getLabels() {
		return labels;
	}

	public MTField getField(String fieldName) {
		for (MTField field:getFields())
			if (field.getName().equalsIgnoreCase(fieldName))
				return field;
		return null;
	}

	public MTTable getExtendsTable() {
		return extendsTable;
	}
	
	public String getExtendsTableName() {
		if(null!=extendsTable)
			return extendsTable.getTableName();
		return null;
	}

	public void setExtendsTable(MTTable extendsTable) {
		this.extendsTable = extendsTable;
	}

	public ArrayList<String> getPatterns() {
		return patterns;
	}
	
	public  boolean isPkTable() {
		if(getFields().size()==1 && getPrimaryKey() != null)
			return true;
		return false;
	}
	@JsonIgnore
	public  MTTable getRealFKTOTable() {
		if(isPkTable()) {
			//MTField pk=table.getPrimaryKey();
			for (MTTable item : MTBase.getTables().values()) {
				if(item.getPatterns().contains("AutoHistory")) {
					for (MTField element : item.getFields()) {
						if(element.getStereoTypes().contains("AUTOHISTORYMAINFK") && element.getForeignKey().equals(this))
							return item;
					}
				}
			}
		}
		return this;
	}
	
	public  String getRealFKTOTableName() {
		return getRealFKTOTable().getTableName();
	}
	
	
	public List<MTField> getDescriptionFields(){
		return MTHelper.getDescriptionFields(this);
	}

	public ArrayList<String> getStereotypes() {
		return stereotypes;
	}

	public void setStereotypes(ArrayList<String> stereotypes) {
		this.stereotypes = stereotypes;
	}
	
	public boolean isView() {
		return getEntityType().equals("VIEW");
		
	}
	public boolean isAutoHistory() {
			return getPatterns().contains("AutoHistory");
	}
	
	public boolean isLinktable() {
		return getStereotypes().contains("LINKTABLE");
}
	public boolean isRecorder() {
		return getStereotypes().contains("RECORDER");
}
	
	public MTField getMainFk() {
		if(getPatterns().contains("AutoHistory")) {
			for (MTField mtField : getFields()) {
				if(mtField.getStereoTypes().contains("AUTOHISTORYMAINFK"))
					return mtField;
			}
		}
		return getPrimaryKey();
	}
	
}
