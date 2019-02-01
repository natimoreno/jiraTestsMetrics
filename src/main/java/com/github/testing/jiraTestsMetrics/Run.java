/**
 * Copyright (c) 2018. Natalia Moreno
 */

package com.github.testing.jiraTestsMetrics;

import java.util.Scanner;


/**
 * 
 * @author nmoreno
 *
 */
public class Run {
	
	
	public static void main(String[] args) throws Exception{
		
		Menu menu = new Menu();
		menu.Options();
		
		Scanner scanner = new Scanner(System.in);
		int option = scanner.nextInt();
		System.out.println(option);
			
		switch (option) {
			case 1:
				new Shipper().startExportingTestCases();
				break;
				
			case 2:
				new Report().startAutomationReport();
				break;
				
			case 0:
				break;
				
			default:
				break;
			}
			
		scanner.close();
	}
	
}
