package com.api.auto.testcase;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.api.auto.utils.PropertiesFileUtils;
import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class TC_API_Login {
	private String account;
	private String password;
	private Response response;
	private ResponseBody responseBody;
	private JsonPath jsonBody;

	@BeforeClass
	public void init() {
		String baseUrl = PropertiesFileUtils.getProperty("baseURL");
		System.out.println("URL: " + baseUrl);
		String loginPath = PropertiesFileUtils.getProperty("loginPath");
		System.out.println("loginPath: " + loginPath);
		account = PropertiesFileUtils.getProperty("account");
		password = PropertiesFileUtils.getProperty("password");
		System.out.println("account:" + account);
		System.out.println("password:" + password);

		RestAssured.baseURI = baseUrl;
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("account", account);
		body.put("password", password);

		RequestSpecification requestSpecification = RestAssured.given().contentType(ContentType.JSON).body(body);

		response = requestSpecification.post(loginPath);
		responseBody = response.body();
		System.out.println("responseBody=" + responseBody);
		jsonBody = responseBody.jsonPath();
		System.out.println("jsonBody=" + jsonBody);
		System.out.println("Response: " + responseBody.asPrettyString());
	}

	@Test(priority = 0)
	public void TC01_Validate200Ok() {
		// kiểm chứng status code
		int statusCode = response.getStatusCode();
		System.out.println("StatusCode:" + statusCode);
		assertEquals(statusCode, 200, "Status not success");

	}

	@Test(priority = 1)
	public void TC02_ValidateMessage() {
		// kiểm chứng response body có chứa trường message hay không
		// kiểm chứng trường message có = "Đăng nhập thành công
		assertTrue(responseBody.asString().contains("message"), "message field check Failed!");
		assertEquals(jsonBody.getString("message"), "Đăng nhập thành công");

	}

	@Test(priority = 2)
	public void TC03_ValidateToken() {
		// kiểm chứng response body có chứa trường token hay không
		String token = jsonBody.getString("token");
		assertTrue(token != null && !token.isEmpty());
		// lưu lại token

		PropertiesFileUtils.saveToken(token);
	}

	@Test(priority = 3)
	public void TC04_ValidateUserType() {
		// kiểm chứng response body có chứa thông tin user và trường type hay không
		// kiểm chứng trường type có phải là “UNGVIEN”

		assertTrue(responseBody.asString().contains("user"), "user field check Failed!");
		assertTrue(responseBody.asString().contains("type"), "type field check Failed!");
		String actualUserType = jsonBody.getString("user.type");
		assertNotNull(actualUserType, "Type field is missing from response body.");
		assertEquals(actualUserType, "UNGVIEN", "Unexpected type value in response body.");
	}

	@Test(priority = 4)
	public void TC05_ValidateAccount() {
		// kiểm chứng response chứa thông tin user và trường account hay không
		assertTrue(responseBody.asString().contains("user"), "user field check Failed!");
		assertTrue(responseBody.asString().contains("account"), "account field check Failed!");
		// Kiểm chứng trường account có khớp với account đăng nhập
		String actualUserAccount = jsonBody.getString("user.account");
		String expectUserAccount = account;
		assertEquals(actualUserAccount, expectUserAccount);
		// kiểm chứng response chứa thông tin user và trường password hay không
		assertTrue(responseBody.asString().contains("password"), "password field check Failed!");
		// Kiểm chứng trường password có khớp với password đăng nhập
		String actualUserPassword = jsonBody.getString("user.password");
		String expectUserPassword = password;
		assertEquals(actualUserPassword, expectUserPassword);
	}
}
