package com.jsantos.orm.DBUtil;

import org.apache.commons.lang3.StringUtils;

public class DbPageMSSUtil {
static public String table_select="table_select";
	
	static public String getSql(String dataTableName, String selectClause, String whereClause, String orderbyClause,String selectCustomItems, String asOfDate) {
		String sql = "";
		if (StringUtils.isNotBlank(selectClause))
			sql += selectClause;
		else
		sql += " select * from " + dataTableName + " baseTable ";
		
		if (StringUtils.isNotBlank(whereClause))
			sql +=  " where  (1=1) " + whereClause;
		if (StringUtils.isNotBlank(orderbyClause))
			sql += " order by " + orderbyClause;
		
		
		return sql;
	}

	static public String getSqlPageable(String dataTableName, int pageSize, int page, String selectClause, String whereClause, String orderbyClause,String selectCustomItems, String asOfDate) {

		String orderBy = "";
		if (null!=orderbyClause && orderbyClause.length() > 0) {
		orderBy = " order by " + orderbyClause;
		} else
		orderBy = " order by (select null) ";

		String asOfDateClause = "";
		if (null != asOfDate) {
		asOfDateClause = " for system_time as of '" + asOfDate + "' ";
		}

		String retSql = "with table_select as (" + selectClause + " baseTable" + ((null!=whereClause && whereClause.length() > 0) ? " where  (1=1) " + whereClause : "") + " " + asOfDateClause + " ) "
		+ " select  " + selectCustomItems + ",querytotalcount " + " from " + table_select + " " + "   cross join(select count(1) as querytotalcount from " + table_select + ") as tcount " + orderBy
		+ " offset " + (page  * pageSize) + " rows fetch next " + pageSize + " rows only";

		
		return retSql;
		}



	static public String getSqlPageableNoCount(String dataTableName, int pageSize, int page, String selectClause, String whereClause, String orderbyClause,String selectCustomItems, String asOfDate) {
		String orderBy = "";
		if (orderbyClause.length() > 0) {
		orderBy = " order by " + orderbyClause;
		} else
		orderBy = " order by (select null) ";

		String asOfDateClause = "";
		if (null != asOfDate) {
		asOfDateClause = " for system_time as of '" + asOfDate + "' ";
		}

		String retSql = "with table_select as (" + selectClause + " baseTable" + (whereClause.length() > 0 ? " where  (1=1) and " + whereClause : "") + " " + asOfDateClause + " ) "
		+ " select  " + selectCustomItems + ",null querytotalcount " + " from " + table_select + " " + " " + orderBy + " offset " + ((page - 1) * pageSize) + " rows fetch next " + pageSize
		+ " rows only";

		
		return retSql;
		}
	
	
	
	


}
