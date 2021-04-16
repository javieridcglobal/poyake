package com.jsantos.orm.dbstatement;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jsantos.common.fieldMapper.FieldMapperComponentProvider;
import com.jsantos.common.fieldMapper.IFieldMapper;
import com.jsantos.orm.mt.MTField;

public class DBValueMapper {
	public static final Null NULL = new Null();

	public static Object loadValue(ResultSet rs, MTField field) throws SQLException {
		
		return loadValueByJdbcDefault(rs,field);
	}

	private static Object loadValueByJdbcDefault(ResultSet rs, MTField field) throws SQLException {
		if (null == rs.getObject(field.getName())){
			return NULL;
		}
		IFieldMapper fmc=FieldMapperComponentProvider.getMapper(field);
		if(null!=fmc)
			return fmc.loadValue(rs, field);
			//return fmc.loadvalue(field, rs.getObject(field.getName()));
		return rs.getObject(field.getName());
	}

	public static Object unloadValue(MTField field,DetachedRecord detachedRecord) {
		IFieldMapper fmc=FieldMapperComponentProvider.getMapper(field);
		if(null!=fmc)
			return fmc.unloadValue(field,detachedRecord);
		return detachedRecord.get(field);
	}

	
	
	
    public static class Null{
        Null() {
            super();
        }
        
        public String toString() {
        	return "";
        }
    }

}
