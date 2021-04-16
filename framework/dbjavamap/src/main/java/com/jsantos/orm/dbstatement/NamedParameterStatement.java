package com.jsantos.orm.dbstatement;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



/**
 * This class wraps around a {@link PreparedStatement} and allows the programmer to set parameters by name instead of by
 * index. This eliminates any confusion as to which parameter index represents what. This also means that rearranging
 * the SQL statement or adding a parameter doesn't involve renumbering your indices. Code such as this:
 * 
 * 
 * Connection con=getConnection(); String query="select * from my_table where name=? or address=?"; PreparedStatement
 * p=con.prepareStatement(query); p.setString(1, "bob"); p.setString(2, "123 terrace ct"); ResultSet
 * rs=p.executeQuery();
 * 
 * can be replaced with:
 * 
 * Connection con=getConnection(); String query="select * from my_table where name=:name or address=:address";
 * NamedParameterStatement p=new NamedParameterStatement(con, query); p.setString("name", "bob"); p.setString("address",
 * "123 terrace ct"); ResultSet rs=p.executeQuery();
 * 
 * Sourced from JavaWorld Article @ http://www.javaworld.com/javaworld/jw-04-2007/jw-04-jdbc.html
 * 
 * @author adam_crume
 */

public class NamedParameterStatement implements AutoCloseable {
	/** The statement this object is wrapping. */
	private final PreparedStatement statement;

	/** Maps parameter names to arrays of ints which are the parameter indices. */
	private Map<String, int[]> indexMap;

	/**
	 * Creates a NamedParameterStatement. Wraps a call to c.{@link Connection#prepareStatement(java.lang.String)
	 * prepareStatement}.
	 * 
	 * @param connection
	 *            the database connection
	 * @param query
	 *            the parameterized query
	 * @throws SQLException
	 *             if the statement could not be created
	 */
	public NamedParameterStatement(Connection connection, String query) throws SQLException {
		String parsedQuery = parse(query);
		statement = connection.prepareStatement(parsedQuery);
	}

	public NamedParameterStatement(Connection connection, String query, int typeStatement) throws SQLException {
		String parsedQuery = parse(query);
		statement = connection.prepareStatement(parsedQuery,typeStatement);
	}
	
	public NamedParameterStatement(Connection connection, String query, int typeStatement, int concurrency) throws SQLException {
		String parsedQuery = parse(query);
		statement = connection.prepareStatement(parsedQuery,typeStatement, concurrency);
	}
	
	/**
	 * Parses a query with named parameters. The parameter-index mappings are put into the map, and the parsed query is
	 * returned. DO NOT CALL FROM CLIENT CODE. This method is non-private so JUnit code can test it.
	 * 
	 * @param query
	 *            query to parse
	 * @param paramMap
	 *            map to hold parameter-index mappings
	 * @return the parsed query
	 */
	final String parse(String query) {
		// I was originally using regular expressions, but they didn't work well for ignoring
		// parameter-like strings inside quotes.
		int length = query.length();
		StringBuffer parsedQuery = new StringBuffer(length);
		boolean inSingleQuote = false;
		boolean inDoubleQuote = false;
		int index = 1;
		HashMap<String, List<Integer>> indexes = new HashMap<String, List<Integer>>(10);

		for (int i = 0; i < length; i++) {
			char c = query.charAt(i);
			if (inSingleQuote) {
				if (c == '\'') {
					inSingleQuote = false;
				}
			} else if (inDoubleQuote) {
				if (c == '"') {
					inDoubleQuote = false;
				}
			} else {
				if (c == '\'') {
					inSingleQuote = true;
				} else if (c == '"') {
					inDoubleQuote = true;
				} else if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(query.charAt(i + 1))) {
					int j = i + 2;
					while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
						j++;
					}
					String name = query.substring(i + 1, j);
					c = '?'; // replace the parameter with a question mark
					i += name.length(); // skip past the end if the parameter

					List<Integer> indexList = indexes.get(name);
					if (indexList == null) {
						indexList = new LinkedList<Integer>();
						indexes.put(name, indexList);
					}
					indexList.add(Integer.valueOf(index));

					index++;
				}
			}
			parsedQuery.append(c);
		}

		indexMap = new HashMap<String, int[]>(indexes.size());
		// replace the lists of Integer objects with arrays of ints
		for (Map.Entry<String, List<Integer>> entry : indexes.entrySet()) {
			List<Integer> list = entry.getValue();
			int[] intIndexes = new int[list.size()];
			int i = 0;
			for (Integer x : list) {
				intIndexes[i++] = x.intValue();
			}
			indexMap.put(entry.getKey(), intIndexes);
		}

		return parsedQuery.toString();
	}

	/**
	 * Returns the indexes for a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * @return parameter indexes
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 */
	private int[] getIndexes(String name) {
		int[] indexes = indexMap.get(name);
		if (indexes == null) {
			return  null;
			//throw new IllegalArgumentException("Parameter not found: " + name);
		}
		return indexes;
	}

	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws SQLException
	 *             if an error occurred
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 * @see PreparedStatement#setObject(int, java.lang.Object)
	 */

	
	public void setParameters(Hashtable<String,Object>parameters) throws SQLException{
		
		for(String name:parameters.keySet()){

			if(null!=getIndexes(name)){
				Object value=parameters.get(name);
				if(value instanceof Integer ) 				setInt(name,(Integer)value);
				else if(value instanceof String ) 			setString(name,(String)value);
				else if(value instanceof Array ) 			setArray(name,(Array)value);
				else if(value instanceof  BigDecimal) 		setBigDecimal(name,(BigDecimal)value);
				else if(value instanceof Blob ) 			setBlob(name,(Blob)value);
				else if(value instanceof  Boolean) 			setBoolean(name,(Boolean)value);
				else if(value instanceof  Clob) 			setClob(name,(Clob)value);
				else if(value instanceof  Double) 			setDouble(name,(Double)value);
				else if(value instanceof  Date) 			setDate(name,(Date)value);
				else if(value instanceof  java.util.Date) 	setDate(name,(java.util.Date)value);
				else if(value instanceof  Float) 			setFloat(name,(Float)value);
				else if(value instanceof  Timestamp) 		setTimestamp(name,(Timestamp)value);
				else if(value instanceof  Time) 			setTime(name,(Time)value);
				else if(value instanceof  Long) 			setLong(name,(Long)value);
				else setObject(name,value);
			}
		} 
		
		
	}
	
	public void setTime(String name, Time value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			statement.setTime(indexes[i], value);
		}
		
	}

	public void setFloat(String name, Float value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			statement.setFloat(indexes[i], value);
		}
		
	}

	public void setDate(String name, Date value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			if (null == value)
				statement.setNull(indexes[i], Types.DATE);
			else
				statement.setDate(indexes[i], value);
		}
	}

	public void setDate(String name, java.util.Date value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			if (null == value)
				statement.setNull(indexes[i], Types.DATE);
			else
				statement.setDate(indexes[i], new Date(value.getTime()));
		}
		
	}

	
	
	public void setDouble(String name, Double value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			statement.setDouble(indexes[i], value);
		}
		
	}

	public void setClob(String name, Clob value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			statement.setClob(indexes[i], value);
		}
		
	}

	public void setBoolean(String name, Boolean value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			statement.setBoolean(indexes[i], value);
		}
		
	}

	public void setBlob(String name, Blob value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			statement.setBlob(indexes[i], value);
		}
		
	}

	public void setBigDecimal(String name, BigDecimal value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			if (null == value)
				statement.setNull(indexes[i], java.sql.Types.NUMERIC);
			else
				statement.setBigDecimal(indexes[i], value);
		}
		
	}

	public void setArray(String name, Array value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			statement.setArray(indexes[i], value);
		}
		
	}

	public void setObject(String name, Object value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			statement.setObject(indexes[i], value);
		}
	}
	

	public void setBytes(String name, Object value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			statement.setBytes(indexes[i], (byte[]) value);
		}
	}

	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws SQLException
	 *             if an error occurred
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 * @see PreparedStatement#setString(int, java.lang.String)
	 */
	public void setString(String name, String value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			if(null==value) 
				statement.setNull(indexes[i], java.sql.Types.VARCHAR);
			else if(value.length()>4000)
				statement.setCharacterStream(indexes[i], new StringReader(value), value.length());
			else 	
				statement.setString(indexes[i], value);
		}
	}

	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws SQLException
	 *             if an error occurred
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 * @see PreparedStatement#setInt(int, int)
	 */
	public void setInt(String name, Integer value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			if(null==value)
				statement.setNull(indexes[i], java.sql.Types.INTEGER);
			else	
				statement.setInt(indexes[i], value);
		}
	}

	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws SQLException
	 *             if an error occurred
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 * @see PreparedStatement#setInt(int, int)
	 */
	public void setLong(String name, long value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			statement.setLong(indexes[i], value);
		}
	}

	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws SQLException
	 *             if an error occurred
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 * @see PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp(String name, Timestamp value) throws SQLException {
		int[] indexes = getIndexes(name);
		if(null!=indexes)
		for (int i = 0; i < indexes.length; i++) {
			statement.setTimestamp(indexes[i], value);
		}
	}

	/**
	 * Returns the underlying statement.
	 * 
	 * @return the statement
	 */
	public PreparedStatement getStatement() {
		return statement;
	}

	/**
	 * Executes the statement.
	 * 
	 * @return true if the first result is a {@link ResultSet}
	 * @throws SQLException
	 *             if an error occurred
	 * @see PreparedStatement#execute()
	 */
	public boolean execute() throws SQLException {
		return statement.execute();
	}

	/**
	 * Executes the statement, which must be a query.
	 * 
	 * @return the query results
	 * @throws SQLException
	 *             if an error occurred
	 * @see PreparedStatement#executeQuery()
	 */
	public ResultSet executeQuery() throws SQLException {
		return statement.executeQuery();
	}

	/**
	 * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE statement; or an SQL statement that returns
	 * nothing, such as a DDL statement.
	 * 
	 * @return number of rows affected
	 * @throws SQLException
	 *             if an error occurred
	 * @see PreparedStatement#executeUpdate()
	 */
	public int executeUpdate() throws SQLException {
		return statement.executeUpdate();
	}

	/**
	 * Closes the statement.
	 * 
	 * @throws SQLException
	 *             if an error occurred
	 * @see Statement#close()
	 */
	public void close() throws SQLException {
		statement.close();
	}

	/**
	 * Adds the current set of parameters as a batch entry.
	 * 
	 * @throws SQLException
	 *             if something went wrong
	 */
	public void addBatch() throws SQLException {
		statement.addBatch();
	}

	/**
	 * Executes all of the batched statements.
	 * 
	 * See {@link Statement#executeBatch()} for details.
	 * 
	 * @return update counts for each statement
	 * @throws SQLException
	 *             if something went wrong
	 */
	public int[] executeBatch() throws SQLException {
		return statement.executeBatch();
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return statement.getGeneratedKeys();
	}
}