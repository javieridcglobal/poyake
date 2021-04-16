package com.jsantos.common.util;

import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

import com.jsantos.orm.MainDb;
import com.jsantos.orm.mt.MTField;

public class ForeignKeyHelper {
	
	public static String getForeignKeyValue(MTField mtField, Integer foreignTablePk) throws SQLException{
		String retValue = null;
		
		if (null != mtField.getForeignKey())
			if (mtField.getForeignKey().getIsEnumeration())
				return mtField.getForeignKey().getEnumerationValue(foreignTablePk);
		
		if (null == foreignTablePk) return null;
		
		String sql = "select " + mtField.getForeignKey().getIdField().getName() + " from " + mtField.getForeignKey().getTableName() + " where " + mtField.getForeignKey().getPrimaryKey().getName() + " = ";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(MainDb.getMainDataSource());
		retValue = jdbcTemplate.queryForObject(sql, String.class);
		return retValue;
	}
}
