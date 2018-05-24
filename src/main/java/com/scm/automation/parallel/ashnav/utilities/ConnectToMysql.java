package com.scm.automation.parallel.ashnav.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectToMysql {

	private static Logger logger = LogManager.getLogger(ConnectToMysql.class.getName());

	private Connection connection 	= null;
	private String MYSQLHost 		= null;
	private String MYSQLUser 		= null;
	private String MYSQLPassword 	= null;
	String url 						= null;
	private int MYSQLPort 			= 0;

	public ConnectToMysql(String MYSQLHost, String MYSQLUser, String MYSQLPassword, String LPort) {
		this.MYSQLHost 			= MYSQLHost;
		this.MYSQLUser 			= MYSQLUser;
		this.MYSQLPassword 		= MYSQLPassword;
		this.MYSQLPort 			= Integer.parseInt(LPort);
	}

	public int getMysqlPort() {
		return MYSQLPort;
	}

	public void createMYSQLConnection() throws SQLException {
		url 				  = "jdbc:mysql://"+MYSQLHost+":"+MYSQLPort;
		Properties properties = new Properties();
		properties.setProperty("user", MYSQLUser);
		properties.setProperty("password", MYSQLPassword);
		properties.setProperty("useSSL", "false");
		properties.setProperty("autoReconnect", "true");
		properties.setProperty("enableQueryTimeouts", "false");
		connection = DriverManager.getConnection(url, properties);
		logger.info("JDBC Connection to "+url+" established? "+ isConnected());
	}

	public Boolean isConnected() throws SQLException {
		Boolean connected = false;
		if(connection != null) {
			if(!connection.isClosed()) {
				connected = true;
			}
		}
		return connected;
	}

	public void destroyMySqlConnection() throws SQLException {
		if(connection != null)
			connection.close();
		logger.info("JDBC Connection to "+url+" destroyed? {}", !isConnected());
	}

	public List<Map<String,String>> executeSqlQuery(String sqlQuery) throws SQLException {

		List<Map<String,String>> resultSetListofMap = new ArrayList<>();
		
		Statement currentStatement = null;
		logger.info("SQL Query: "+sqlQuery);
		currentStatement = connection.createStatement();
		ResultSet resultSet = currentStatement.executeQuery(sqlQuery);
		int columnCount = resultSet.getMetaData().getColumnCount();
		while (resultSet.next()) {
			Map<String,String> rowMap = new HashMap<>();
			for (int i = 1; i <= columnCount; i++) {
				rowMap.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
			}
			resultSetListofMap.add(rowMap);
		}
		logger.info(resultSetListofMap.toString());
		return resultSetListofMap;
	}

		
}


