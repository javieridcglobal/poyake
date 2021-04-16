package com.jsantos.orm.dbstatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.jsantos.common.util.FieldValues;
import com.jsantos.common.util.ListValues;
import com.jsantos.common.util.MapValues;
import com.jsantos.orm.MainDb;
import com.jsantos.orm.factory.DTOFactory;
import com.jsantos.orm.mt.MTField;
import com.jsantos.orm.mt.MTTable;

/**
 * @author javier santos
 */

public class DBQuery {
	private ArrayList<MTField> fields = new ArrayList<>();
	private String selectSection;
	private String whereSection = null;
    
	private MapValues<Object> parameters= new MapValues<Object>();
	private String groupBySection = null;
	private String orderBySection = null;
	private MTTable mTTable;
    
	private String customSql;
	
    public DBQuery() {
    }

    public DBQuery(MTTable table) {
		super();
		this.mTTable = table;
		for (MTField field:table.getFields())
			fields.add(field);
		if ("VIEW".equals(table.getEntityType())) 
			setSelectSection("select * from " + table.getFullTableName());
		else if ("SQLQUERY".equals(table.getEntityType()))
			setSelectSection(table.getSql());
		//inheritance
		else if (null != table.getExtendsTable()) {
			buildExtendedTableSelectSection(table);
		}
		else {	
			setSelectSection("select * from " + table.getFullTableName());
		}
	}

	public DBQuery(MTTable table, String whereSection) {
		this(table);
		this.whereSection = whereSection;
	}
	
	
	public DBQuery(MTTable table, String whereSection,MapValues<Object> parameters) {
		this(table);
		this.whereSection = whereSection;
		this.parameters=parameters;
	}
	
	void buildExtendedTableSelectSection(MTTable baseTable) {
		String select = "select ";
		select += baseTable.getTableName() + ".*";
		MTTable table = baseTable;
		for (MTTable superTable=table.getExtendsTable(); superTable != null; superTable=superTable.getExtendsTable()) {
			for (MTField field:superTable.getFields()) {
				if (!field.isPrimaryKey() || (field.isPrimaryKey() && !field.getName().equalsIgnoreCase(table.getPrimaryKey().getName()))) {
					this.fields.add(field);
					select += "," + superTable.getTableName() + "." + field.getName();
				}
			}
			table = superTable;
		}
		select += " from ";
		table = baseTable;
		if (null != table.getSchema()) select += table.getSchema() + ".";
		select += table.getTableName();
		for (MTTable superTable=table.getExtendsTable(); superTable != null; superTable=superTable.getExtendsTable()) {
			select += " join ";
			if (null != superTable.getSchema()) selectSection += superTable.getSchema() + ".";
			select += superTable.getTableName() + " on ";
			select += superTable.getTableName() + "." + superTable.getPrimaryKey().getName();
			select += " = " + table.getTableName() + "." + table.getPrimaryKey().getName();
			table = superTable;
		}
		setSelectSection(select);
	}
	
	public DQResults<DetachedRecord> getPage(Integer pageNumber) throws SQLException {
		ListValues<DetachedRecord> results = new ListValues<>();
		String sql = "select * from (" + getSql() + " ) pc1 ";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(MainDb.getMainDataSource());
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				DetachedRecord dr=DTOFactory.get(getmTTable());
 				results.add(dr);
				for (MTField field:fields){
					dr.originalValues.set(field, DBValueMapper.loadValue(rs, field));
				}
			}
		});
		DQResults<DetachedRecord> dqResults = new DQResults<DetachedRecord>();
		dqResults.setRawData(results);
		return dqResults;
	}
	
	public Vector<FieldValues> getSqlResult() throws SQLException {
		Vector<FieldValues> results = new Vector<>();
		String sql= getSql();
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(MainDb.getMainDataSource());
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				FieldValues row = new FieldValues();
				results.add(row);
				for (MTField field:fields){
					row.set(field, DBValueMapper.loadValue(rs, field));
				}
			}
		});
		
		return results;
	}
	
	public ArrayList<MTField> getFields() {
		return fields;
	}

	protected String getSql(){
		if(null!=customSql) return customSql;
		
		StringBuilder sb = new StringBuilder(getSelectSection());
        if (!StringUtils.isEmpty(whereSection)) {
            sb.append(" where ").append(whereSection);
        }
        if (!StringUtils.isEmpty(groupBySection)) {
            sb.append(" ").append(groupBySection);
        }
        if (null != orderBySection) {
            sb.append(" order by ").append(orderBySection);
        } else {
           // sb.append(" order by ").append(fields.get(0).getName());
        }
        return sb.toString();
	}

	public String getSelectSection() {
		return selectSection;
	}

	public void setSelectSection(String selectSection) {
		this.selectSection = selectSection;
	}

	public String getWhereSection() {
		return whereSection;
	}

	public void setWhereSection(String whereSection) {
		this.whereSection = whereSection;
	}

	public String getGroupBySection() {
		return groupBySection;
	}

	public void setGroupBySection(String groupBySection) {
		this.groupBySection = groupBySection;
	}
	
	public void setFields(ArrayList<MTField> fields) {
		this.fields = fields;
	}
	public MapValues<Object> getParameters() {
		return parameters;
	}
	public void setParameters(MapValues<Object> parameters) {
		this.parameters = parameters;
	}
	public String getOrderBySection() {
		return orderBySection;
	}
	public void setOrderBySection(String orderBySection) {
		this.orderBySection = orderBySection;
	}
	public MTTable getmTTable() {
		return mTTable;
	}
	public void setmTTable(MTTable mTTable) {
		this.mTTable = mTTable;
	}
	
    public void execute(RowCallbackHandler handler){
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(MainDb.getMainDataSource());
 		jdbcTemplate.query(getSql(),new MapSqlParameterSource(),handler);
     }

	public String getCustomSql() {
		return customSql;
	}

	public void setCustomSql(String customSql) {
		this.customSql = customSql;
	}
}
