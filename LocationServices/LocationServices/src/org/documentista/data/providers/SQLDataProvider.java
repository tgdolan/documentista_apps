package org.documentista.data.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SQLDataProvider {
	
	//private String dataSourceName = "jdbc/DinerDB";
	
	public Connection getConnection(String dataSourceName) throws NamingException, SQLException {
		
		Context context = new InitialContext();
		DataSource dataSource = (DataSource)context.lookup("java:/comp/env/" + dataSourceName);
		return dataSource.getConnection();
	}
	
	public ResultSet getData(Connection connection, String sqlStatement, List<String> parms) throws SQLException {
	
		PreparedStatement statement = connection.prepareStatement(sqlStatement);
		if (parms != null) {
			for (int i=0; i < parms.size(); i++) {
				statement.setObject(i+1, parms.get(i));			
			}		
		}
				
		return statement.executeQuery();
		
	}
	
	public boolean updateData(Connection connection, String sqlStatement, List<String> parms) throws SQLException {
		boolean insertOK = false;
		
		PreparedStatement statement = connection.prepareStatement(sqlStatement);
		if (parms != null) {
			for (int i=0; i < parms.size(); i++) {
				statement.setObject(i+1, parms.get(i));			
			}		
		}
		//if statement runs OK, we're always going to get a "row modified" count of at least 1
		insertOK = statement.executeUpdate() > 0 ? true : false;
		
		return insertOK;
	
	}
	
	public List<HashMap<String, Object>> processData(ResultSet resultSet) throws SQLException {
		List<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
		ResultSetMetaData metaData = resultSet.getMetaData();
		List<String> columnNames = getColumnNames(metaData);
		
		while (resultSet.next()) {
			HashMap<String, Object> row = new HashMap<String,Object>();
			int i =1;
			for (String columnName : columnNames) {
				int columnType = metaData.getColumnType(i);
				switch (columnType) {
				
					case 5:			//int
						row.put(columnName, resultSet.getInt(columnName));
						break;
						
					case 8:			//double
						row.put(columnName, resultSet.getDouble(columnName));
						break;
					case 12:			//varchar
						row.put(columnName, resultSet.getString(columnName));
						break;
	
					default:
					row.put(columnName, resultSet.getObject(columnName));
				}
				i++;
			}
			
			results.add(row);
		}
		
		return results;
	}

	private List<String> getColumnNames(ResultSetMetaData metaData) throws SQLException {
		
		ArrayList<String> columnNames = new ArrayList<String>();
		for (int i = 0; i < metaData.getColumnCount(); i++) {
			columnNames.add(metaData.getColumnName(i));
		}
		
		return columnNames;
	}

}
