/**
 * Copyright (c) 2018. Natalia Moreno
 */

package com.github.testing.jiraTestsMetrics;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.testing.jiraTestsMetrics.SpreadsheetReader.ReportData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * 
 * @author nmoreno
 * 
 */
public class JsonBuilder {
	
	Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	
	private String linesBreak = "\n\n\n";
	private String lineBreak = "\n";
	
	/**
	 * Values of priorities 
	 * @author nmoreno
	 *
	 */
	public enum Priority {
		ALTA 	 ("Critical","2"),
		MEDIA 	 ("Major", "3"),
		BAJA 	 ("Baja", "7"),
		NULA 	 ("Not Prioritized","6"),
		OBSOLETO ("Not Prioritized","6");
		
		private Priority(String name, String value) {
			this.name = name;
			this.value = value;
		}
		private String name;
		private String value;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	
	/**
	 * Constructor
	 */
	public JsonBuilder() {
		super();
	}
	
	
	/**
	 * Make Json for Test issue of Jira
	 * @param projec
	 * @param key
	 * @param data
	 */
	public String makeJsonToTestIssue(String projec, String key, Map<ReportData, String> data){
		
		JsonObject project =  new JsonObject();
		project.addProperty("key", key);
		project.addProperty("name", projec);
		
		JsonObject issuetype =  new JsonObject();
		issuetype.addProperty("name", "Prueba");
		issuetype.addProperty("subtask", false);
		
		JsonObject priority =  new JsonObject();
		priority.addProperty("name", Priority.valueOf(data.get(ReportData.PRIORITY).toUpperCase()).getName());
		priority.addProperty("id", Priority.valueOf(data.get(ReportData.PRIORITY).toUpperCase()).getValue());
		
		JsonArray labels = new JsonArray();
		labels.add(data.get(ReportData.TYPE));
		labels.add(data.get(ReportData.LEVEL));
		labels.add(data.get(ReportData.AUTOMATION_LEVEL));
		
		JsonObject component =  new JsonObject();
		component.addProperty("name", data.get(ReportData.COMPONENT));
		JsonArray components = new JsonArray();
		components.add(component);
		
		JsonObject assignee =  new JsonObject();
		assignee.addProperty("name", data.get(ReportData.AUTHOR));
		assignee.addProperty("key", data.get(ReportData.AUTHOR));
		assignee.addProperty("active", true);
		assignee.addProperty("timeZone", "America/Argentina/Buenos_Aires");
		
		JsonObject reporter =  new JsonObject();
		reporter.addProperty("name", data.get(ReportData.AUTHOR));
		reporter.addProperty("key", data.get(ReportData.AUTHOR));
		reporter.addProperty("active", true);
		reporter.addProperty("timeZone", "America/Argentina/Buenos_Aires");
		
		JsonObject fields =  new JsonObject();
		fields.add("project", project);
		fields.add("issuetype", issuetype);
		fields.add("priority", priority);
		fields.add("labels", labels);
		fields.add("components", components);
		fields.addProperty("summary", data.get(ReportData.NARRATIVE));
		fields.addProperty("description", 
				linesBreak + data.get(ReportData.NAME) 
				+ linesBreak + " *Pre condicion:* " 
				+ lineBreak + data.get(ReportData.PRE_CONDITIONS) 
				+ linesBreak + " *Pasos:* "
				+ lineBreak + data.get(ReportData.STEPS) 
				+ linesBreak + " *Resultado esperado:* "
				+ lineBreak + data.get(ReportData.EXPECTED_RESULT) 
				+ linesBreak + " *Comentarios:* " 
				+ lineBreak + data.get(ReportData.COMMENTS));
//		fields.add("reporter", reporter);
//		fields.add("assignee", assignee);
		
		JsonObject parent =  new JsonObject();
		parent.add("fields", fields);
		return gson.toJson(parent).toString();
	}
	
	
	/**
	 * Json to update issue in Jira
	 * @return json
	 */
	public String makeJsonToUpdateIssue(Map<ReportData, String> data){
		
		JsonObject priority =  new JsonObject();
		priority.addProperty("name", Priority.valueOf(data.get(ReportData.PRIORITY).toUpperCase()).getName());
		priority.addProperty("id", Priority.valueOf(data.get(ReportData.PRIORITY).toUpperCase()).getValue());
		
		JsonObject fields =  new JsonObject();
		fields.add("priority", priority);
		fields.addProperty("summary", data.get(ReportData.NARRATIVE));
		fields.addProperty("description", linesBreak + " Pre-Condition: " + lineBreak 
				+ data.get(ReportData.PRE_CONDITIONS) 
				+ linesBreak + " Steps: "+ lineBreak + data.get(ReportData.STEPS) 
				+ linesBreak + " Expected Result: "+ lineBreak + data.get(ReportData.EXPECTED_RESULT));
		
		JsonObject parent =  new JsonObject();
		parent.add("fields", fields);
		return gson.toJson(parent).toString();
	}
	
	
	/**
	 * Json to link issues
	 * @param issueId
	 * @param data
	 * @return
	 */
	public String makeJsonToIssueLink(String newIssue, String oldIssue){
			
		JsonObject type =  new JsonObject();
		type.addProperty("name", "Relates");
			
		JsonObject inwardIssue =  new JsonObject();
		inwardIssue.addProperty("key", newIssue);
			
		JsonObject outwardIssue =  new JsonObject();
		outwardIssue.addProperty("key", oldIssue);
			
		JsonObject parent =  new JsonObject();
		parent.add("type", type);
		parent.add("inwardIssue", inwardIssue);
		parent.add("outwardIssue", outwardIssue);

		return gson.toJson(parent).toString();
	}
	
	
	/**
	 * Get issue Id from json response
	 */
	public String getIssueId(String jsonString){
		JsonParser parser = new JsonParser();
		JsonObject json = new JsonObject();
		json = (JsonObject) parser.parse(jsonString);
		return json.get("key").toString().replaceAll("\"", "");
	}
	
	
	/**
	 * Total of stories for test case
	 * @param data
	 * @return
	 */
	public int countStories(Map<ReportData, String> data){
		String[] count = data.get(ReportData.STORY).split(",");
		return count.length;
	}
	
	/**
	 * List of Issue Id 
	 * @return
	 */
	public List<String> getStories(Map<ReportData, String> data){
		String[] stories = data.get(ReportData.STORY).split(",");
		return Arrays.asList(stories);
	}
	
	/**
	 * 
	 * @param projectName project name
	 * @param key project key
	 * @param type testing activity
	 * @param parentIssue parent issue
	 * @param componentName component name
	 * @param summary summary
	 * @return
	 */
	public String makeJsonToSubTaskIssue(String projectName, String key, String type, String parentIssue, String componentName, String summary){
		
		JsonObject project =  new JsonObject();
		project.addProperty("key", key);
		project.addProperty("name", projectName);
		
		JsonObject issuetype =  new JsonObject();
		issuetype.addProperty("name", type);
		
		JsonObject parent =  new JsonObject();
		parent.addProperty("key", parentIssue);
		
		JsonObject component =  new JsonObject();
		component.addProperty("name", componentName);
		JsonArray components = new JsonArray();
		components.add(component);
		
		JsonObject fields =  new JsonObject();
		fields.add("project", project);
		fields.add("issuetype", issuetype);
		fields.add("parent", parent);
		fields.add("components", components);
		fields.addProperty("summary", summary);
		
		JsonObject gparent =  new JsonObject();
		gparent.add("fields", fields);
		return gson.toJson(gparent).toString();
	}
	
}

