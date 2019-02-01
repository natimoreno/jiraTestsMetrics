/**
 * Copyright (c) 2018. Natalia Moreno
 */

package com.github.testing.jiraTestsMetrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * 
 * @author nmoreno
 *
 */
public class JiraClient {
	
	
	private static String  base_url = "/rest/api/2";
	
	private static String urlToCreateTest = base_url + "/issue/";
	
	private static String urlToLinkIssues = base_url + "/issueLink";
	
//	private static String issue_url = base_url + "/issue/%s"; 
//	
//	private static String filter_url = base_url + "/filter/%s";
	
	private static String issue_wp_jql_url = base_url + "/search?jql=issue in linkedIssues(%s) "
			+ "AND issuetype in (Story, Bug, Improvement, \"New Feature\")"
			+ "AND issuetype not in (Task, \"Technical task\", Test, Documentation)";
	
	private static String automation_level_url = base_url + "/search?jql=component = \"%s\" AND issuetype = TestCase AND labels in (%s, %s)";
	
	private static String test_types_url = base_url + "/search?jql=component = \"%s\" AND issuetype = TestCase AND labels in (%s) AND labels not in (MANUAL, Manual, manual)";
	
	private static Logger log = Logger.getLogger(JiraClient.class);
	
	private RestClient restClient = new RestClient();
	
	private JsonBuilder jsonBuider = new JsonBuilder();
	
	
	/**
	 * Return issues of work package
	 * @return
	 */
	public String searchWPIssue(String pwd, String workPkgId){
		
		return restClient.getRequestWithAuthorization(String.format(issue_wp_jql_url, workPkgId), pwd);
	}
	
	
	/**
	 * Create new jira issue
	 * @throws IOException
	 */
	public void createInJira(String json, String password, int amountStories, List<String> stories) throws IOException{
		try {
			
			String response = restClient.postRequestWithAuthorization(urlToCreateTest, json.replace("[\n]", ""), password);
			
			if (amountStories > 1) {
				for (String string : stories) {
					linkTestCase(response, string.trim(), password);
				}
			} else {
				linkTestCase(response, stories.get(0), password);
			}
		} catch (Exception e) {
			log.error("Check if the test case was registered in Jira with the link to the Story");
		}
	}

	
	/**
	 * Link issue with story in Jira
	 * @param response
	 * @param story
	 */
	public void linkTestCase(String response, String story, String password){
		String updateIssueJson = jsonBuider.makeJsonToIssueLink(jsonBuider.getIssueId(response), story);
		restClient.postRequestWithAuthorization(urlToLinkIssues, updateIssueJson, password);
		if (restClient.getStatusCode() != 201 ){
			log.warn("Check if " + story + " exit in Jira");
			log.info("Last test case stored was " + response);
		}
	}


	/**
	 * Return amount of automatics and manuals test cases
	 * @param component
	 * @param pwd
	 * @return
	 */
	public List<String> countAutomationLevel(String component, String pwd){
		
		String automatic = String.format(automation_level_url, component, "AUTOMATICO", "Automatico");
		String automaticResponse = restClient.getRequestWithAuthorization(automatic, pwd);
		String manuals = String.format(automation_level_url, component, "MANUAL", "Manual");
		String manualResponse = restClient.getRequestWithAuthorization(manuals, pwd);
		
		List<String> levels = new ArrayList<String>();
		levels.add(component);
		levels.add(JsonParser.getStringValue(automaticResponse, "total"));
		levels.add(JsonParser.getStringValue(manualResponse, "total"));
		return levels;
	}
	
	
	/**
	 * Return amount of test cases types
	 * @param component
	 * @param pwd
	 * @return
	 */
	public List<String> countTestType(String component, String pwd){
		
		String system = String.format(test_types_url, component, "Sistema");
		String systemResponse = restClient.getRequestWithAuthorization(system, pwd);
		String integration = String.format(test_types_url, component, "Integracion");
		String integrationResponse = restClient.getRequestWithAuthorization(integration, pwd);
		String manuals = String.format(automation_level_url, component, "MANUAL", "Manual");
		String manualResponse = restClient.getRequestWithAuthorization(manuals, pwd);
		
		List<String> types = new ArrayList<String>();
		types.add(component);
		types.add(JsonParser.getStringValue(systemResponse, "total"));
		types.add(JsonParser.getStringValue(integrationResponse, "total"));
		types.add(JsonParser.getStringValue(manualResponse, "total"));
		return types;
	}
	
}
