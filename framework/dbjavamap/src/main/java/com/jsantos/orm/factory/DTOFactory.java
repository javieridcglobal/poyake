package com.jsantos.orm.factory;

import java.util.HashMap;

import com.jsantos.common.util.MapValues;
import com.jsantos.orm.dbstatement.DetachedRecord;
import com.jsantos.orm.mt.MTTable;

public class DTOFactory {
	static final HashMap<MTTable, Class> dtoRegistry = new HashMap<>();
	
	public static DetachedRecord get(MTTable table) {
		try {
			if (null == table) {
				throw new RuntimeException("Trying to get a DTO for a null table");
			}if (null == dtoRegistry.get(table)) {
				logInfo();
				throw new RuntimeException("No DTO registered for table " + table);
			}
			DetachedRecord dr=(DetachedRecord) dtoRegistry.get(table).newInstance();
			return dr;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static DetachedRecord get(MTTable table, Object pk) {
		if(null==pk)
			return get(table);
		try {
			DetachedRecord dr=(DetachedRecord) dtoRegistry.get(table).newInstance();
			dr.set(table.getMainFk(), pk);
			dr.setWhereExpression(table.getMainFk() + " = :"+table.getMainFk());
			dr.setParams(new MapValues<Object>().add(table.getMainFk().toString(), pk));
			dr.load();
			if (null != table.getExtendsTable())
				dr.setExtendedDetachedRecord(new DetachedRecord(table.getExtendsTable(), pk));
		    return dr;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static HashMap<MTTable, Class> getDtoregistry() {
		return dtoRegistry;
	}
	
	public static void logInfo() {
		System.out.println("DTOFactory: ==========================================");
		for (MTTable mtTable: dtoRegistry.keySet())
			System.out.println("\t" + dtoRegistry.get(mtTable).getName() + " ---> "+ mtTable.getFullTableName() );
		System.out.println("------------------------------------------------------");
		System.out.println("");
	}
			
	
}
