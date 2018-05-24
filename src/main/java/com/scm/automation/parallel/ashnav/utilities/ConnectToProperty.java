package com.scm.automation.parallel.ashnav.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConnectToProperty {

	private Properties property;
	private InputStream fis;
	
	public ConnectToProperty(String propertyFileName) throws IOException {
		property = new Properties();
		loadPropertyFile(propertyFileName);
	}
	
	private void loadPropertyFile(String propertyFileName) throws IOException {
		String fileName = propertyFileName+".properties";
		fis = getClass().getClassLoader().getResourceAsStream(fileName);
		if(fis == null) {
			fis = new FileInputStream(new File(fileName));
		}
		property.load(fis);
		fis.close();
	}

	public String getPropertyValue(String propertyName) {
		String value  = property.getProperty(propertyName);
		return value;
	}
}
