package com.jsantos.orm.mt;

import java.util.ArrayList;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsantos.orm.label.Label;
import com.jsantos.orm.label.LabelSelector;

public class MTField extends AbstractField{
	
	MTDataType dataType;
	private Integer length = null;
	private Integer scale = null;
	private Boolean nullable = false;
	private Boolean autoincrement = false;
	private Boolean primaryKey = false;
	@JsonIgnore
	private MTTable foreignKey = null;
	@JsonIgnore
	private MTTable table = null;
	private String sequence = null;
	private ArrayList<Label> labels = new ArrayList<>();
	private ArrayList<String> stereoTypes = new ArrayList<>();
	private String defaultValue = null;
	@JsonIgnore
	private MTField sameAs = null;
	@JsonIgnore
	private MTTable multiRefTo = null;
	private Boolean isTransient = false;
	
	public MTField(String name) {
		super(name);
	}
	
	public MTField(MTField mtField, String name) {
		super(name);
		this.setSameAs(mtField);
		this.setDefaultValue(mtField.getDefaultValue());
		this.setForeignKey(mtField.getForeignKey());
		this.setLength(mtField.getLength());
		this.setNullable(mtField.isNullable());
		this.setScale(mtField.getScale());
		this.setSequence(mtField.getSequence());
		this.setStereoTypes(mtField.getStereoTypes());
		this.setTable(mtField.getTable());
		this.setDataType(mtField.getDataType());
	}
	
	public String getFullyQualifiedName() {
		return getTable().getTableName() + "." + getName();
	}
	
	public String toString() {
		return name;
	}
	
	public String getLabel(Locale locale, String type) {
		return LabelSelector.inst().getLabel(locale, type, this);
	}

	public String getLabel(String type) {
		return LabelSelector.inst().getLabel(type, this);
	}

	public String getLabel() {
		return LabelSelector.inst().getLabel(this);
	}

	
	public boolean isFileGroup(){
		if(null != sameAs)
			return sameAs.isFileGroup();
		if (null == getForeignKey()) return false;
		return "FILEGROUP".equalsIgnoreCase(getForeignKey().toString().toUpperCase());
	}
	


	public boolean isNullable() {
		if(null != sameAs)
			return sameAs.isNullable();
		return nullable;
	}

	public boolean isPrimaryKey() {
		//if(null != sameAs)
		//	return sameAs.isPrimaryKey();
		return primaryKey;
	}

	public boolean isAutoincrement() {
		if(null != sameAs)
			return sameAs.isAutoincrement();
		return autoincrement;
	}

	public MTTable getForeignKey() {
		if(null != sameAs)
			return sameAs.getForeignKey();
		return foreignKey;
	}
	
	public String getForeignKeyName() {
		if(null != sameAs)
			return sameAs.getForeignKeyName();
		if(null!=foreignKey)
			return foreignKey.getTableName();
		return null;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public void setAutoincrement(Boolean autoincrement) {
		this.autoincrement = autoincrement;
	}

	public void setPrimaryKey(Boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setForeignKey(MTTable foreignKey) {
		this.foreignKey = foreignKey;
	}

	public MTTable getTable() {
		return table;
	}
	
	public String getTableName() {
		return table.getTableName();
	}

	public void setTable(MTTable table) {
		this.table = table;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer precission) {
		this.length = precission;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}


	public ArrayList<Label> getLabels() {
		return labels;
	}

	public ArrayList<String> getStereoTypes() {
		return stereoTypes;
	}

	public void setStereoTypes(ArrayList<String> stereoTypes) {
		this.stereoTypes = stereoTypes;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	@JsonIgnore
	public MTField getSameAs() {
		return sameAs;
	}
	@JsonIgnore
	public MTField getRealField() {
		if(null!=getSameAs())
			return getSameAs();
		return this;
	}
	
	
	
	public String getSameAsName() {
		if(null!=sameAs)
			return sameAs.getTableName();
		return null;
	}

	public void setSameAs(MTField sameAs) {
		this.sameAs = sameAs;
	}

	public boolean isEnumeration() {
		return (null != getForeignKey() && getForeignKey().getPatterns().contains("Enumeration"));
	}

	public boolean isNoGuiInput() {
		return (getStereoTypes().contains("NOGUIINPUT"));
	}
	
	
	public boolean isDescription() {
		return (getStereoTypes().contains("DESCRIPTION"));
	}
	
	public boolean isLink() {
		return (getStereoTypes().contains("LINK"));
	}
	
	
	public MTDataType getDataType() {
		return dataType;
	}

	public void setDataType(MTDataType dataType) {
		this.dataType = dataType;
	}

	public MTTable getMultiRefTo() {
		return multiRefTo;
	}

	public void setMultiRefTo(MTTable multiRefTo) {
		this.multiRefTo = multiRefTo;
	}

	public Boolean isTransient() {
		return isTransient;
	}

	public void setTransient(Boolean isTransient) {
		this.isTransient = isTransient;
	}
	public boolean isMainFK() {
		return (getStereoTypes().contains("AUTOHISTORYMAINFK"));
	}
	
}
