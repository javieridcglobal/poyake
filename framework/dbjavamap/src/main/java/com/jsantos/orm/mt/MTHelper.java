package com.jsantos.orm.mt;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.jsantos.common.util.ListValues;
import com.jsantos.common.util.MTMapValues;
import com.jsantos.common.util.MapValues;
import com.jsantos.common.util.PostingDate;
import com.jsantos.orm.dbstatement.DetachedRecord;
import com.jsantos.orm.factory.DTOFactory;


public class MTHelper {

	public static List<MTField> getDescriptionFields(MTTable table){
		List<MTField> retValue= new ArrayList<MTField>();
		for (MTField field:table.getFields()) {
			if (field.getStereoTypes().contains("DESCRIPTION")) {
				//if(null==field.getForeignKey() || field.getForeignKey().isPkTable())
				    retValue.add(field);
				//else {
				//	MTTable realTable=field.getForeignKey().getRealFKTOTable();
				//	retValue.addAll(getDescriptionFields(realTable));
				//}
			}
		}
		return retValue;
	}
	
	public static List<MTTable> getTables(List<MTField> fields){
		List<MTTable> tables= new ArrayList<MTTable>();
		for (MTField mtField : fields) {
			tables.add(mtField.getTable());
		}
		return tables;
	}
	
	public static List<MapValues<Object>> getValues(List<MTMapValues<Object>> values) {
		List<MapValues<Object>> retValues= new ArrayList<MapValues<Object>>();
		values.forEach((key)-> retValues.add(getValues(key)));
		return retValues;
	}
	
	public static List<MapValues<Object>> getValuesDR(List<DetachedRecord> values) {
		List<MapValues<Object>> retValues= new ArrayList<MapValues<Object>>();
		values.forEach((key)-> retValues.add(getValues(key.getCopyValues())));
		return retValues;
	}
	
	public static MapValues<Object> getValues(MTMapValues<Object> values) {
		
		MapValues<Object> retValue= new MapValues<Object>();
		
		for (Entry<MTField, Object> item : values.entrySet()) {
			retValue.put(item.getKey().getName(), item.getValue());
		}
		return retValue;
	}
	

	
	public static List<MTMapValues<Object>> getValues(List<MapValues<Object>> values, String tableName) {
		List<MTMapValues<Object>> retValues= new ArrayList<MTMapValues<Object>>();
		values.forEach((key)-> retValues.add(getValues(key,tableName)));
		return retValues;
	}
	
	public static MTMapValues<Object> getValues(MapValues<Object> values, String tableName) {
		
		MTMapValues<Object> retValue= new MTMapValues<Object>();
		MTTable mTTable = MTBase.getTable(tableName);
		
		for (Entry<String, Object> item : values.entrySet()) {
			if(null!=mTTable.getField(item.getKey()))
				retValue.put(mTTable.getField(item.getKey()), item.getValue());
		}
		return retValue;
	}
	
	public static Date getDefaultDate(MTField field) {
		if(null==field.getDefaultValue())return null;
		String defaultValue=field.getDefaultValue();
		if(defaultValue.equals("config.getPostingDate()"))
		  return PostingDate.get();
		if(defaultValue.equals("'01-Jan-2099'"))
			return new Date(2099,1,1);
		return null;
	}
	
	public static DetachedRecord getTableFromView(DetachedRecord view) {
		if(!view.getTable().isView()) return view;
		MTTable table=view.getTable().getPrimaryKey().getSameAs().getTable();
		
		return getTableFromView(view, table);
	}
	
	public static DetachedRecord getTableFromView(DetachedRecord view,MTTable table) {

		DetachedRecord dto=DTOFactory.get(table);
		dto.getOriginalValues().getValues().putAll(view.getOriginalValues().getValues());
		
		
		for (MTField tablefield : table.getFields()) {
			for (MTField viewfield : view.getFields()) {
				MTField sameas=viewfield.getSameAs();
				if(null!=sameas  && tablefield.equals(sameas))
					dto.getOriginalValues().set(tablefield, view.getOriginalValues().get(viewfield));
			}
		}
		for (MTField tablefield : table.getFields()) {
			for (MTField viewfield : view.getFields()) {
				MTField sameas=viewfield.getSameAs();
				if(null!=sameas  && tablefield.equals(sameas))
					dto.set(tablefield, view.get(viewfield));
			}
		}
		return dto;	
	}

     public static  ListValues<DetachedRecord> convertMapToDetachRecord(List<Object> values,MTTable table){
		
    	 ListValues<DetachedRecord> retValue=new ListValues<DetachedRecord>();
			for (Object sf : values) {
				if(sf instanceof DetachedRecord) retValue.add((DetachedRecord) sf);
				else{
					MTMapValues<Object> mtm=MTHelper.getValues(new MapValues<Object>().add((LinkedHashMap)sf),table.getTableName());
					retValue.add( DTOFactory.get(table).setValues(mtm));
					}
				}
    	 return retValue;
     }
}
