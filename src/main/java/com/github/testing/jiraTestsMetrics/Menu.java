/**
 * Copyright (c) 2018. Natalia Moreno
 */

package com.github.testing.jiraTestsMetrics;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

/**
 * 
 * @author nmoreno
 *
 */
public class Menu {


	TextIO textIO = TextIoFactory.getTextIO();
	
	String pwd = "";	
	
	public static JiraClient jiraClient = new JiraClient();
	
	/**
	 * Options
	 * @return option
	 */
	public void Options(){
		System.out.println("´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´"); 
	    System.out.println("´´´´´´´´´´´´´´ Jira Client ´´´´´´´´´´´´´´");   
	    System.out.println("´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´´"); 
	    System.out.println("");
	    System.out.println("1. Import test cases from a xls file ");
	    System.out.println("2. Generate automation testing report");
	    System.out.println("0. Exit ");
	    System.out.println("   Write one option and press Enter: ");
	}
	
	
	/**
	 * Authentique
	 */
	public void authentique(){
		
		try {
        	pwd = textIO.newStringInputReader().read("Jira User: ");
    		pwd += ":" + textIO.newStringInputReader().withInputMasking(true).read("Jira Password: ");
    		pwd = Encoder.encode(pwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
