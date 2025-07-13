package tests;

import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import Utility.ApiUtils;
import Utility.ExtentManager;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class DynamicallyApi3{
	
	public static ExtentReports extent = ExtentManager.createInstance();
    public static ExtentTest test;
    

    @BeforeMethod
    public void setup(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
    }

    @AfterSuite
    public void tearDown() {
        extent.flush();
    }

    @Test(priority = 1)
    public void getUserName_Positive() {
        Response response = ApiUtils.reqSpec()
            .when()
            .get("/api/users?page=2");

        test.info("GET Request executed");
        test.info("Response: " + response.asPrettyString());

        response.then()
            .statusCode(200)
            .body("data.size()", greaterThan(0))
            .log().body();

        test.pass("Valid user data retrieved");
    }


	@Test(priority = 5)
	public void getUserName_Negative() {
		Response response = ApiUtils.reqSpec()
	        .when()
	        .get("/api/users?page=999"); // Invalid page
	        test.info("GET Request executed");
	        test.info("Response: " + response.asPrettyString());
	        
	        response.then()
	        .statusCode(200) // Still returns 200 with empty data
	        .body("data.size()", equalTo(0))
	        .log().body();
	        
	        test.pass("Valid user data retrieved");
	}
	
	@Test(priority = 2)
	public void createUser_Positive() {
		Response response = ApiUtils.reqSpec()
	        .body(ApiUtils.createUserPayload("morpheus", "leader"))
	        .when()
	        .post("/api/users");
	    test.info("post Request executed");
        test.info("Response: " + response.asPrettyString());
        response.then()
	        .statusCode(201)
	        .body("name", equalTo("morpheus"))
	        .body("job", equalTo("leader"))
	        .log().body();
        test.pass("Valid user data retrieved with create user");
	}

	@Test(priority = 6)
	public void createUser_Negative_EmptyPayload() {
		Response response =ApiUtils.reqSpec()
	        .body("{}") // Missing required fields
	        .when()
	        .post("/api/users");
	    test.info("Post Request executed");
        test.info("Response: " + response.asPrettyString());
	        response.then()
	        .statusCode(201) // Still returns 201 with default values in ReqRes
	        .log().body();
	    test.pass("Valid createUser_Negative_EmptyPayload");
	}
	
	@Test(priority = 3)
	public void updateUser_Positive() {
		Response response = ApiUtils.reqSpec()
	        .body(ApiUtils.createUserPayload("Vinoth", "zion resident"))
	        .when()
	        .put("/api/users/2");
		    test.info("Put Request executed");
	        test.info("Response: " + response.asPrettyString());
	        response.then()
	        .statusCode(200)
	        .body("name", equalTo("Vinoth"))
	        .body("job", equalTo("zion resident"))
	        .log().body();
	        test.pass("Valid update User_Positive");
	}

	@Test(priority = 7)
	public void updateUser_Negative_InvalidUser() {
		Response response = ApiUtils.reqSpec()
	        .body(ApiUtils.createUserPayload("Neo", "One"))
	        .when()
	        .put("/api/users/9999");
		test.info("Put Request executed");
        test.info("Response: " + response.asPrettyString());// Non-existent user
	        response.then()
	        .statusCode(200) // ReqRes allows updates even on non-existent user
	        .log().body();
	    test.pass("Valid update User_Negative_InvalidUser");
	}
	
	@Test(priority = 4)
	public void deleteUser_Positive() {
		Response response = ApiUtils.reqSpec()
	        .when()
	        .delete("/api/users/2");
		test.info("delete Request executed");
        test.info("Response: " + response.asPrettyString());// Non-existent user
		response.then()
	        .statusCode(204)
	        .log().all();
		   test.pass("Valid user data retrieved with delete user details");
	}

	@Test(priority = 8)
	public void deleteUser_Negative_InvalidUser() {
		Response response = ApiUtils.reqSpec()
	        .when()
	        .delete("/api/users/9999"); // Non-existent user
		test.info("delete Request executed");
        test.info("Response: " + response.asPrettyString());// Non-existent user
	        response.then()
	        .statusCode(204) // ReqRes still returns 204
	        .log().all();
	        test.pass("Valid delete user with invalid user");
	}
	
	@Test(priority = 9)
	public void getUser_InvalidParam() {
		Response response = ApiUtils.reqSpec()
	        .when()
	        .get("/api/users?page=abc"); // Non-numeric page value
	    test.info("get Request executed");
        test.info("Response: " + response.asPrettyString());// Non-existent user
	        response.then()
	        .statusCode(200); // Expecting client-side error or fallback behavior
	        test.pass("Valid user data retrieved with invalid param");
	}

	@Test(priority = 10)
	public void createUser_MalformedJson() {
		Response response = ApiUtils.reqSpec()
	        .body("{ name: morpheus, job: leader") // Missing closing braces
	        .when()
	        .post("/api/users");
		 test.info("post Request executed");
	        test.info("Response: " + response.asPrettyString());
	        response.then()
	        .statusCode(400); // Possible parsing failure
	        test.pass("Valid create user with malformed json");
	}

	
	@Test(priority = 11)
	public void updateUser_EmptyBody() {
		Response response = ApiUtils.reqSpec()
	        .body("{}") // Empty payload
	        .when()
	        .put("/api/users/2");
		test.info("post Request executed");
        test.info("Response: " + response.asPrettyString());
	        response.then()
	        .statusCode(200)
	        .body("updatedAt", notNullValue()); // But may still succeed in ReqRes
	        test.pass("Valid udpated user with empty body");
	}

	@Test(priority = 12)
	public void deleteUser_InvalidMethod() {
		Response response = ApiUtils.reqSpec()
	        .when()
	        .put("/api/users/9999"); // Using PUT on a delete scenario
		test.info("delete Request executed");
        test.info("Response: " + response.asPrettyString());
	        response.then()
	        .statusCode(200); // May succeed but test for misuse
	        test.pass("Valid delete user with invalid Method");
	}

	
	@Test(priority = 13)
	public void createUser_MissingApiKey() {
		Response response = ApiUtils.reqSpec()
	        .baseUri("https://reqres.in")
	        .header("Content-Type", "application/json")
	        .body(ApiUtils.createUserPayload("Agent Smith", "villain"))
	    .when()
	        .post("/api/users");
		test.info("post Request executed");
        test.info("Response: " + response.asPrettyString());
	    response.then()
	        .statusCode(201); // If endpoint enforces x-api-key
	    test.pass("Valid create user with missing API key");
	}



}
