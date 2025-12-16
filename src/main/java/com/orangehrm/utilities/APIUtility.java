package com.orangehrm.utilities;

import javax.management.relation.RelationServiceNotRegisteredException;

import org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.StaticMetaMethodSiteNoUnwrapNoCoerce;
import org.codehaus.groovy.transform.tailrec.ReturnStatementToIterationConverter;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIUtility {

	// Method to send the GET Request
	public static Response sendGetRequest(String endPoint) {
		return RestAssured.get(endPoint);
	}

	// Method to send the POST Request
	public static Response sendPostRequest(String endPoint, String payLoad) {
		return RestAssured.given().header("Content-Type", "application/json").body(payLoad).post();
	}

	// Method to Validate Response Status Code
	public static boolean validateStatusCode(Response response, int statusCode) {
		return response.getStatusCode() == statusCode;
	}
	
	//Method to extract the value from JSON Response
	public static String getJsonValue(Response response, String value) {
		return response.jsonPath().getString(value);
	}
	
	

}
