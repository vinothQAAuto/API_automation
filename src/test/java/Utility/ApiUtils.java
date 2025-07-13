package Utility;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class ApiUtils {
	
    public static RequestSpecification reqSpec() {
        return 
        		RestAssured.given()
            .baseUri("https://reqres.in")
            .header("x-api-key", "reqres-free-v1")
            .header("Content-Type", "application/json");
    }

    public static String createUserPayload(String name, String job) {
        return "{\n" +
               "    \"name\": \"" + name + "\",\n" +
               "    \"job\": \"" + job + "\"\n" +
               "}";
    }


}
