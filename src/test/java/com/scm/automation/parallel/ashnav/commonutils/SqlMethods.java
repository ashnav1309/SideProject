package com.scm.automation.parallel.ashnav.commonutils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jcraft.jsch.JSchException;
import com.scm.automation.parallel.ashnav.utilities.ConnectToMysql;
import com.scm.automation.parallel.ashnav.utilities.ConnectToProperty;
import com.scm.automation.parallel.ashnav.utilities.ConnectToServer;

public class SqlMethods {

	public static List<Map<String, String>> getResultAsListOfMap(String query) {

		ConnectToProperty databaseProperty;
		ConnectToServer connectToServer 			= null;
		ConnectToMysql connectToMysql 			    = null;
		List<Map<String,String>> resultSetListOfMap = new ArrayList<>();

		try {
			databaseProperty 					= new ConnectToProperty("database");
			Boolean sshRequired					= Boolean.parseBoolean(databaseProperty.getPropertyValue("sshRequired"));
			String environment					= databaseProperty.getPropertyValue("environment");
			String sshUser 						= databaseProperty.getPropertyValue("sshUser");
			String sshHostName 					= databaseProperty.getPropertyValue("sshHostName");
			String sshPassword 					= databaseProperty.getPropertyValue("sshPassword");
			String sshPort 						= databaseProperty.getPropertyValue("sshPort");
			String sshLPort 					= databaseProperty.getPropertyValue("sshLPort");
			String sshRhost 					= databaseProperty.getPropertyValue("sshRhost");
			String sshRPort 					= databaseProperty.getPropertyValue("sshRPort");
			String mysqlHostName 				= databaseProperty.getPropertyValue("mysqlHostName");
			String mysqlUserName 				= databaseProperty.getPropertyValue("mysqlUserName");
			String mysqlPassword 				= databaseProperty.getPropertyValue("mysqlPassword");
			String mysqlPort 					= databaseProperty.getPropertyValue("mysqlPort");

			if(sshRequired) {
				connectToServer = new ConnectToServer(environment, sshUser, sshHostName, sshPassword, sshPort, sshLPort, sshRhost, sshRPort);
				connectToServer.createSSHSession();
			}
			connectToMysql = new ConnectToMysql(mysqlHostName, mysqlUserName, mysqlPassword, mysqlPort);
			connectToMysql.createMYSQLConnection();
			if (connectToMysql.isConnected()) {
				resultSetListOfMap = connectToMysql.executeSqlQuery(query);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (JSchException e) {
			e.printStackTrace();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				connectToMysql.destroyMySqlConnection();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
			catch (NullPointerException e) {
				return resultSetListOfMap;
			}
			try {
				connectToServer.destroyLPort();
			} 
			catch (JSchException e) {
				e.printStackTrace();
			}
			catch (NullPointerException e) {
				return resultSetListOfMap;
			}
			connectToServer.destroySSHSession();
		}
		return resultSetListOfMap;
	}

	public static String getColumnValue(String query, String columnName, int rowth) {
		try {
		return getResultAsListOfMap(query).get(rowth).get(columnName);
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
}
