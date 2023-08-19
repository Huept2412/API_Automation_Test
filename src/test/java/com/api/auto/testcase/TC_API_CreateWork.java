package com.api.auto.testcase;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.api.auto.utils.PropertiesFileUtils;

import groovyjarjarantlr4.v4.parse.ANTLRParser.id_return;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class TC_API_CreateWork {
	private String token;
	private Response response;
	private ResponseBody responseBody;
	private JsonPath jsonBody;
	// Chúng ta có thể tự tạo data
	private String nameWork = "Tester";
	private String experience = "2 years";
	private String education = "University";

	@BeforeClass
	public void init() {
		// Init data
		String baseUrl = PropertiesFileUtils.getProperty("baseURL");
		System.out.println("BaseUrl:" + baseUrl);
		String createWorkPath = PropertiesFileUtils.getProperty("createWorkPath");
		System.out.println("createWorkPath:" + createWorkPath);
		token = PropertiesFileUtils.getToken("token");
		System.out.println("token:" + token);

		RestAssured.baseURI = baseUrl;
		// make body
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("nameWork", nameWork);
		body.put("experience", experience);
		body.put("education", education);

		RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).header("token", token)
				.body(body);

		response = request.post(createWorkPath);
		responseBody = response.body();
		jsonBody = responseBody.jsonPath();

		System.out.println(" " + responseBody.asPrettyString());
	}

	@Test(priority = 0)
	public void TC01_Validate201Created() {
		int statusCode = response.getStatusCode();
		System.out.println("StatusCode:" + statusCode);
		Assert.assertEquals(statusCode, 201, "Status not success");
	}

	@Test(priority = 1)
	public void TC02_ValidateWorkId() {
		// kiểm chứng id
		String id = jsonBody.getString("id");
		Assert.assertTrue(id != null && !id.isEmpty());

	}

	@Test(priority = 2)
	public void TC03_ValidateNameOfWorkMatched() {
		// kiểm chứng tên công việc nhận được có giống lúc tạo
		assertTrue(responseBody.asString().contains("nameWork"), "nameWork field check Failed!");
		String actualnameWork = jsonBody.getString("nameWork");
		String expectnameWork = nameWork;
		assertEquals(actualnameWork, expectnameWork);
	}

	@Test(priority = 3)
	public void TC04_ValidateExperienceMatched() {
		// kiểm chứng kinh nghiệm nhận được có giống lúc tạo
		assertTrue(responseBody.asString().contains("experience"), "experience field check Failed!");
		String actualExperience = jsonBody.getString("experience");
		String expectExerience = experience;
		assertEquals(actualExperience, expectExerience);

	}

	@Test(priority = 4)
	public void TC05_ValidateEducationMatched() {
		// kiểm chứng học vấn nhận được có giống lúc tạo
		assertTrue(responseBody.asString().contains("education"), "education field check Failed!");
		String actualEducation = jsonBody.getString("education");
		String expectEducation = education;
		assertEquals(actualEducation, expectEducation);

	}
}
