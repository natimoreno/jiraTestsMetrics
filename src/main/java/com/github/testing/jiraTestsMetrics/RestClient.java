/**
 * Copyright (c) 2018. Natalia Moreno
 */

package com.github.testing.jiraTestsMetrics;

import static com.jayway.restassured.RestAssured.given;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.jayway.restassured.response.Response;


/**
 * 
 * @author nmoreno
 * 
 * 	Extract from Shredder Service
 *
 */
public class RestClient {

	
	private Response response;
	
	private PropertiesReader prop;
	
	private String endpoint = "";
  
	private Integer port = 443;
	
	private String contentType = "application/json;charset=UTF-8";
	
	private String accept = "application/json";

	private static Logger log = Logger.getLogger(RestClient.class);

	
	/**
	 * Constructor
	 */
	public RestClient() {
		BasicConfigurator.configure();
		prop = new PropertiesReader();
		endpoint = prop.getPropertyValue("jira.endpoint");
		port = Integer.parseInt(prop.getPropertyValue("jira.port"));
	}


	/**
	 * Get Json request
	 * @param url web service
	 * @param idSession idSession encoded in 64
	 * @return String Json
	 */
	public String getRequestWithAuthorization(String url, String idSession) {
		printHeader("GET", url, null);
		response = given().header("Authorization", "Basic " + idSession)
				.baseUri(endpoint)
				.port(port)
				.accept(accept)
				.contentType(contentType)
				.when().get(url).thenReturn();
		String json = (response != null) ?  response.asString() : null;
		printFooter(json);
		return json;
	}	

	/**
     * Post Json request with authorization
     * @param url web service
     * @param json body request
     * @param idSession idSession encoded in 64
     * @return String Json
     */
    public String postRequestWithAuthorization(String url, String json, String idSession) {
    	printHeader("POST", url, json);
        if(accept != null){
            response = given()
                    .header("Authorization", "Basic " + idSession)
                    .baseUri(endpoint)
                    .port(port)
                    .contentType(contentType).accept(this.accept).
                    body(json).when().post(url).thenReturn();
        } else {
            response = given()
                    .header("Authorization", "Basic " + idSession)
                    .baseUri(endpoint)
                    .port(port)
                    .contentType(contentType).
                    body(json).when().post(url).thenReturn();
        }
        String result = (response != null) ?  response.asString() : null;
        printFooter(result);
        return result;
    }


	/**
	 * Print head banner
	 * @param type api rest
	 * @param url api rest
	 * @param body text
	 */
	private void printHeader(String type, String url, String body) {
		log.info("-------------------------------------------------------------------------------");
		log.info("Send " + type + " request:");
		log.info("ENDPOINT: " + endpoint + ":" + port);
		log.info("URL: " + url);
		if(body != null)
			log.info("BODY:" + body);
		log.info("-------------------------------------------------------------------------------");
	}

	/**
	 * Print footer banner
	 * @param json request
	 */
	private void printFooter(String json) {
		log.info("Response:");
		log.info("JSON: " + json);
		log.info("Status code: " + getStatusCode());
		log.info("-------------------------------------------------------------------------------");
	}

	/**
	 * Get Status code of the response
	 * @return status code
	 */
	public int getStatusCode() {
		return response.getStatusCode();
	}

}
