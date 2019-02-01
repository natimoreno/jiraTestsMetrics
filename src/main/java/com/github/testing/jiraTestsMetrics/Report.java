/**
 * Copyright (c) 2018. Natalia Moreno
 */

package com.github.testing.jiraTestsMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 
 * @author nmoreno
 *
 */
public class Report extends Menu{
	
	
	/**
	 * Constructor
	 */
	public Report(){}
	
	
	/**
	 * Start Automation Report
	 * @throws Exception
	 */
	public void startAutomationReport() throws Exception{
			
		authentique();
		String tmp = textIO.newStringInputReader().read(" Component/s: ");
		textIO.dispose();
		
		SpreadsheetReader spreadsheetReader = new SpreadsheetReader();
		spreadsheetReader.createReport();
		List<String> components = new ArrayList<String>();
		List<String> levels = new ArrayList<String>();
		List<String> types = new ArrayList<String>();
		
		components = Arrays.asList(tmp.split(","));
		
		for (String string : components) {
			levels.clear();
			levels.addAll(jiraClient.countAutomationLevel(string.trim(), pwd));
			spreadsheetReader.writeAutomationLevel(levels);
		}
		
		spreadsheetReader.writeTypeHeader();
		
		for (String string : components) {
			types.clear();
			types.addAll(jiraClient.countTestType(string.trim(), pwd));
			spreadsheetReader.writeTestTypes(types);
		}
		
		spreadsheetReader.closeReport();
	}

}
