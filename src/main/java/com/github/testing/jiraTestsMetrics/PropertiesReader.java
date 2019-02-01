/**
 * Copyright (c) 2018. Natalia Moreno
 */

package com.github.testing.jiraTestsMetrics;

import java.io.InputStream;
import java.util.Properties;

import jline.internal.Log;

/**
 * 
 * @author nmoreno
 *
 */
public class PropertiesReader {
	
	Properties properties;
	
	InputStream inputStream;
	
	
	/**
	 * Constructor
	 * @return
	 */
	public PropertiesReader() {
		
		try {
			properties = new Properties();
			String fileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
					
			if (inputStream != null) {
				properties.load(inputStream);
			}
			
		} catch (Exception e) {
			Log.error("Error when configuration file was read: " + e);
		}
		
	}
	
	
	/**
	 * Get propuerty value
	 * @param key
	 * @return
	 */
	public String getPropertyValue(String key) {
		
		return properties.getProperty(key);
	}

}
