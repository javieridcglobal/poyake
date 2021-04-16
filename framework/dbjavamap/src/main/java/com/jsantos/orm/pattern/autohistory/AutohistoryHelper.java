package com.jsantos.orm.pattern.autohistory;

import com.jsantos.orm.dbstatement.DetachedRecord;
import com.jsantos.orm.mt.MTField;
import com.jsantos.orm.mt.MTTable;

public class AutohistoryHelper {
	public static final String AUTOHISTORY = "Autohistory";
	public static final String AUTOHISTORYMAINFK = "AUTOHISTORYMAINFK";
	
	public static MTField getAutohistoryMainFk(MTTable table) {
		for (MTField field:table.getFields())
			if (field.getStereoTypes().contains(AUTOHISTORYMAINFK))
				return field;
		return null;
	}
	
	public static DetachedRecord getByMainFk(MTTable table, int mainFk) {
		String where = getAutohistoryMainFk(table).getName() + "=" + mainFk + " and startDate<config.getPostingDate() and endDate>config.getPostingDate()";
		return new DetachedRecord(table, where,null);
	}
}
