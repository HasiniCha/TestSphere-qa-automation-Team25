package com.qaautomation.stepdefinitions.api;

import org.junit.Assert;

import com.qaautomation.utils.ConfigReader;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class UserCategoryAPI {

    private Response response;
    private String baseUrl = "http://localhost:8080";
    private String token;

    // ================= AUTH =================

    @Given("User is authenticated via API")
    public void user_is_authenticated_via_api() {

        response = given()
                .contentType("application/json")
                .body("{\"username\":\"" + ConfigReader.get("app.user.username") +
                        "\",\"password\":\"" + ConfigReader.get("app.user.password") + "\"}")
                .when()
                .post(baseUrl + "/api/auth/login");

        System.out.println(">>> Login Status: " + response.getStatusCode());
        System.out.println(">>> Login Response: " + response.getBody().asString());

        token = response.jsonPath().getString("token");
        Assert.assertNotNull("JWT token is null!", token);

        System.out.println(">>> User JWT token received");
    }

    // ================= GET SUB-CATEGORIES =================

    @When("User sends GET request to {string}")
    public void user_sends_get_request_to(String endpoint) {

        response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + endpoint);

        System.out.println(">>> GET " + endpoint);
        System.out.println(">>> Status: " + response.getStatusCode());
        System.out.println(">>> Body: " + response.getBody().asString());
    }

    // ================= COMMON ASSERTIONS =================

    @Then("User response status code should be {int}")
    public void user_response_status_code_should_be(int expectedStatus) {
        Assert.assertEquals(expectedStatus, response.getStatusCode());
        System.out.println(">>> User response status code verified: " + expectedStatus);
    }

    @Then("User response should contain sub-categories array")
    public void user_response_should_contain_sub_categories_array() {
        String responseBody = response.getBody().asString();
        Assert.assertTrue("Response body is empty!", responseBody.length() > 0);
        Assert.assertTrue("Response is not an array!", responseBody.trim().startsWith("["));
        System.out.println(">>> User sub-categories array verified");
    }
    
// ================= GET SPECIFIC CATEGORY - NEW ASSERTIONS =================

@Then("User response body should contain category with ID {int}")
public void user_response_body_should_contain_category_with_id(int expectedId) {
    int actualId = response.jsonPath().getInt("id");
    Assert.assertEquals("Returned category ID does not match!", expectedId, actualId);
    System.out.println(">>> User retrieved category with correct ID: " + actualId);
}

@Then("User response body should contain category details")
public void user_response_body_should_contain_category_details() {
    String responseBody = response.getBody().asString();
    Assert.assertNotNull("Response body is null!", responseBody);
    
    // Check if response contains expected fields
    Assert.assertNotNull("Category name is missing!", response.jsonPath().getString("name"));
    
    System.out.println(">>> User response contains category details:");
    System.out.println(">>> - ID: " + response.jsonPath().getString("id"));
    System.out.println(">>> - Name: " + response.jsonPath().getString("name"));
}
// ================= ERROR HANDLING ASSERTIONS =================

@Then("User response body should contain error details")
public void user_response_body_should_contain_error_details() {
    String responseBody = response.getBody().asString();
    Assert.assertNotNull("Response body is null!", responseBody);
    Assert.assertTrue("Response body is empty!", responseBody.length() > 0);
    
    System.out.println(">>> User response contains error details");
    System.out.println(">>> Error Response: " + responseBody);
}

@Then("User error message should indicate category not found")
public void user_error_message_should_indicate_category_not_found() {
    String responseBody = response.getBody().asString().toLowerCase();
    
    // Check if response contains "not found" or "category" or error-related keywords
    boolean containsNotFound = responseBody.contains("not found") || 
                               responseBody.contains("notfound") ||
                               responseBody.contains("does not exist") ||
                               responseBody.contains("not exist");
    
    Assert.assertTrue("Error message does not indicate category not found!", containsNotFound);
    System.out.println(">>> User error message verified: Category not found");
}
// ================= PUT REQUEST =================

@When("User sends PUT request to {string} with body:")
public void user_sends_put_request_to_with_body(String endpoint, String requestBody) {
    response = given()
            .header("Authorization", "Bearer " + token)
            .contentType("application/json")
            .body(requestBody)
            .when()
            .put(baseUrl + endpoint);

    System.out.println(">>> PUT " + endpoint);
    System.out.println(">>> Request Body: " + requestBody);
    System.out.println(">>> Status: " + response.getStatusCode());
    System.out.println(">>> Response Body: " + response.getBody().asString());
}

// ================= ACCESS DENIED ASSERTION =================

@Then("User error message should indicate access denied")
public void user_error_message_should_indicate_access_denied() {
    String responseBody = response.getBody().asString().toLowerCase();
    
    // Check for common access denied messages
    boolean isAccessDenied = responseBody.contains("forbidden") || 
                            responseBody.contains("access denied") ||
                            responseBody.contains("unauthorized") ||
                            responseBody.contains("permission") ||
                            responseBody.contains("not allowed") ||
                            responseBody.contains("no access");
    
    Assert.assertTrue("Error message does not indicate access denied! Got: " + responseBody, 
                     isAccessDenied);
    System.out.println(">>> User error message verified: Access denied");
}
// ================= CATEGORY SUMMARY ASSERTIONS =================

@Then("User response should contain mainCategories count")
public void user_response_should_contain_main_categories_count() {
    Integer mainCategoriesCount = response.jsonPath().getInt("mainCategories");
    Assert.assertNotNull("mainCategories count is missing!", mainCategoriesCount);
    Assert.assertTrue("mainCategories count should be >= 0", mainCategoriesCount >= 0);
    
    System.out.println(">>> mainCategories count verified: " + mainCategoriesCount);
}

@Then("User response should contain subCategories count")
public void user_response_should_contain_sub_categories_count() {
    Integer subCategoriesCount = response.jsonPath().getInt("subCategories");
    Assert.assertNotNull("subCategories count is missing!", subCategoriesCount);
    Assert.assertTrue("subCategories count should be >= 0", subCategoriesCount >= 0);
    
    System.out.println(">>> subCategories count verified: " + subCategoriesCount);
}
}
