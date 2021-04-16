package com.jsantos.common.fieldMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsantos.orm.dbstatement.DetachedRecord;
import com.jsantos.orm.mt.MTDataType;
import com.jsantos.orm.mt.MTField;

public interface IFieldMapper {

	public default   MTField forField() {return null;};
	public default   MTDataType forModelType() {return null;};
	
	public default boolean insertOrUpdate(MTField mtField,DetachedRecord detachedRecord) {
		return false;
	}
	public default Object loadValue(ResultSet rs, MTField field) throws SQLException {
		return rs.getObject(field.getName());
	}
	
	public default Object unloadValue(MTField field,DetachedRecord detachedRecord) {
		return detachedRecord.get(field);
	}
}
