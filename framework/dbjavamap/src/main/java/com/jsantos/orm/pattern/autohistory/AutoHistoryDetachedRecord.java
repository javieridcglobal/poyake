package com.jsantos.orm.pattern.autohistory;

import java.sql.ResultSet;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsantos.common.util.MapValues;
import com.jsantos.common.util.PostingDate;
import com.jsantos.orm.MainDb;
import com.jsantos.orm.dbstatement.DetachedRecord;
import com.jsantos.orm.dbstatement.Sequence;
import com.jsantos.orm.mt.MTField;
import com.jsantos.orm.mt.MTTable;

public class AutoHistoryDetachedRecord extends DetachedRecord{
	MTField mainFk = null;
	boolean isMainFk=true;
	
	String whereMainFkClause=" and startDate<=config.getPostingDate() and endDate>config.getPostingDate() ";
	
	public AutoHistoryDetachedRecord(MTTable table) {
		super(table);
		if (!table.getPatterns().contains("AutoHistory"))
			throw new RecoverableDataAccessException("Table " + table.getFullTableName() + " doesn't implement AUTOHISTORY pattern");
		mainFk = AutohistoryHelper.getAutohistoryMainFk(table);
		if (null == mainFk)
			throw new RecoverableDataAccessException("Table " + table.getFullTableName() + " doesn't contain any field with AUTOHISTORYMAINFK Stereotype but it is marked as Autohistory");
	}

	public AutoHistoryDetachedRecord (MTTable table, boolean isMainFk, Object id) {
		super(table);
		this.isMainFk=isMainFk;
		mainFk = AutohistoryHelper.getAutohistoryMainFk(table);
		if (isMainFk) 
			whereExpression = mainFk.getName() + " = " + id + whereMainFkClause;
		else
			whereExpression = " revisionId = " + id;
		load();
	}
	
	
	public AutoHistoryDetachedRecord(MTTable table, int id) {
		this(table, true, id);
	}

	public AutoHistoryDetachedRecord(MTTable table, ResultSet rs) {
		super(table, rs);
	}

	public AutoHistoryDetachedRecord(MTTable table, String whereClause) {
		this(table);
		if (isMainFk)
			whereClause+= whereMainFkClause;
		this.setWhereExpression(whereClause + (isMainFk ?whereMainFkClause:""));
		load();
	}
	
	public AutoHistoryDetachedRecord(MTTable table, MapValues<Object> params) {
		this(table);
		find(params);
	}
	
	@Override
	public DetachedRecord find(MapValues<Object> params) {
		
			this.whereExpression = "(1=1) " +(isMainFk ?whereMainFkClause:" ") + params.keySet().stream()
					.map(element -> new String("and " + element + "=:" + element)).collect(Collectors.joining(" "));
			this.params = params;
			load();

			// inheritance
			if (null != table.getExtendsTable())
				extendedDetachedRecord = new DetachedRecord(table.getExtendsTable(), getPk());

		
		return this;
	}

	@Override
	public DetachedRecord insert() {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(MainDb.getMainDataSource());
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String sql = "update " + table.getFullTableName() + " set endDate=config.getPostingDate() where " + mainFk.getName() + "=" + get(mainFk) + " and endDate>config.getPostingDate()"; 
		jdbcTemplate.update(sql, namedParameters);

		// this will find the keys table for this autohistory pattern, insert a record and set the fk on this one. Then insert.
		if (null == get(mainFk)) 
			set(mainFk, new DetachedRecord(mainFk.getForeignKey()).insert().getPk());
		
		return super.insert();
	}

	@Override
	public void update() {
		if(!isUpdated())return;
		
		Date now = PostingDate.get();
		
		set(table.getPrimaryKey(), Sequence.nextForTable(table));
		for (MTField field:getFields()) {
			if (!field.getStereoTypes().contains("NOGUIINPUT"))
				updates.set(field, get(field));
		}

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(MainDb.getMainDataSource());
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("endDate", now);
		String sql = "update " + table.getFullTableName() + " set endDate=:endDate where " + mainFk.getName() + "=" + get(mainFk) + " and endDate>config.getPostingDate()"; 
		jdbcTemplate.update(sql, namedParameters);

		//setEndDate to now
		for (MTField field:getFields())
			if (field.getName().equalsIgnoreCase("startDate"))
				set(field, now);
		super.insert();
	}

	@Override
	public void delete() {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(MainDb.getMainDataSource());
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String sql = "update " + table.getFullTableName() + " set endDate=config.getPostingDate() where " + mainFk.getName() + "=" + get(mainFk) + " and endDate>config.getPostingDate()"; 
		jdbcTemplate.update(sql, namedParameters);
	}
	@JsonIgnore
	public MTField getMainFk() {
		return mainFk;
	}

	public void setMainFk(MTField mainFk) {
		this.mainFk = mainFk;
	}
	@JsonIgnore
	public boolean isMainFk() {
		return isMainFk;
	}

	public void setMainFk(boolean isMainFk) {
		this.isMainFk = isMainFk;
	}

	
}
