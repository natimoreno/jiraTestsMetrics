/**
 * Copyright (c) 2018. Natalia Moreno
 */

package com.github.testing.jiraTestsMetrics;

import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;


/**
 * 
 * @author nmoreno
 *
 */
public class Shipper extends Menu{
	
	
	/**
	 * Constructor
	 */
	public Shipper(){}
	
	
	/**
	 * Launch exporting test cases
	 * @throws BiffException
	 * @throws IOException
	 */
	public void startExportingTestCases() throws BiffException, IOException{
		
		authentique();
		
		SpreadsheetReader spreadsheetReader = new SpreadsheetReader();
		JsonBuilder jsonBuilder = new JsonBuilder();
		
		String project = textIO.newStringInputReader().read(" Project name in Jira: ");
		String key = textIO.newStringInputReader().read(" Key of project in Jira: ");
		String file = textIO.newStringInputReader().read(" Path and xls name: ");
		String amountImport = textIO.newStringInputReader().read(" Import up to (row number): ");
		int num = Integer.valueOf(amountImport);
		
		for (int j = 2; j < num; j++) {
			String json = jsonBuilder.makeJsonToTestIssue(project , key, spreadsheetReader.readXls(j, file));
			int totalStories = jsonBuilder.countStories(spreadsheetReader.readXls(j, file));
			List<String> stories = jsonBuilder.getStories(spreadsheetReader.readXls(j, file));
			jiraClient.createInJira(json, pwd, totalStories, stories);
		    System.out.println("********* Imported test case number: " + (j-1) + "/"+ (num-2) + " *********");    
		}
		textIO.dispose();
	}

}
