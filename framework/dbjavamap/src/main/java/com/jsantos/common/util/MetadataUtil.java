package com.jsantos.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map.Entry;

import com.jsantos.orm.dbstatement.DetachedQueryResult;
import com.jsantos.orm.dbstatement.DetachedRecord;
import com.jsantos.orm.mt.MTField;
import com.jsantos.orm.mt.MTTable;

public class MetadataUtil {

	public static File getMDD(MTTable table,String fileName) throws IOException, SQLException{
		
		MTTable mtTable=table;
		if(table.isView())mtTable=table.getPrimaryKey().getTable();
		
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		
		 DetachedQueryResult dgQuery = new DetachedQueryResult(mtTable);
		//ArrayList<String> header= new ArrayList<String>();
		 
		
		 ListValues<DetachedRecord> page = dgQuery.getPage(null).getRawData();
		 int i=1;	
		 for (DetachedRecord row : page) {
				
				out.write(line(row.getCopyValues(),",","'",";",i++));
				out.newLine();
			}
			out.close(); 
			
		return new File(fileName);
	}
	
	
	public static String line(MTMapValues<Object> values, String separator, String quotechar, String lineEnd,int pk) {
		String retValue="";
		
		for (Entry<MTField, Object> value : values.entrySet()) {
			if(retValue.isEmpty())
				retValue+=pk;
			else {
				retValue+=separator;
				if(value.getKey().isNoGuiInput())
					retValue+="DEFAULT";
				else {
					String sValue;
					if(null==value || null==value.getValue()) {
						sValue="NULL";
						retValue+=sValue;
					}
					else {
						retValue+=quotechar;
						sValue=value.getValue().toString();
						retValue+=sValue;
						retValue+=quotechar;
					}
				}
			}
		}
		retValue+=lineEnd;
		return retValue;
		
	}
	
	
	
}
