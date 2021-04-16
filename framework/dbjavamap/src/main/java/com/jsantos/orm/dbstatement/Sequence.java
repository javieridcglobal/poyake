package com.jsantos.orm.dbstatement;

import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;

import com.jsantos.orm.MainDb;
import com.jsantos.orm.mt.MTTable;

public class Sequence {
    public static Object nextForTable(MTTable table){
        if(table.getPrimaryKey().getDataType().getName().equals("UUID"))
            return UUID.randomUUID();
        return next(table.getPrimaryKey().getSequence());
    }

    private static Object next(String name){
    	String sql = " select NEXT VALUE FOR " + name;
    	if (MainDb.getDatabaseProductName().equals("PostgreSQL"))
    		sql = " select nextval('" + name + "') ";
        return new JdbcTemplate(MainDb.getMainDataSource()).queryForObject(sql, Integer.class);
    }
}
