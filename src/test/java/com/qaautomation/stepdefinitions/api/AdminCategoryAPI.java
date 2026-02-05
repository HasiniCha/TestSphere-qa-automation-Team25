package com.qaautomation.stepdefinitions.api;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Assert;

import com.qaautomation.utils.ConfigReader;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class AdminCategoryAPI {

    private Response response;
    private String baseUrl = "http://localhost:3000";
    private String token;

    // ================= AUTH =================
    @Given("Admin is authenticated via API")
    public void admin_is_authenticated_via_api() {

        response = given()
                .contentType("application/json")
                .body("{\"username\":\"" + ConfigReader.get("app.admin.username") +
                        "\",\"password\":\"" + ConfigReader.get("app.admin.password") + "\"}")
                .when()
                .post(baseUrl + "/api/auth/login");

        System.out.println(">>> Login Status: " + response.getStatusCode());
        System.out.println(">>> Login Response: " + response.getBody().asString());

        token = response.jsonPath().getString("token");
        Assert.assertNotNull("JWT token is null!", token);

        System.out.println(">>> JWT token received");
    }

    // ================= GET =================
    @When("Admin sends GET request to {string}")
    public void admin_sends_get_request_to(String endpoint) {

        response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + endpoint);

        System.out.println(">>> GET " + endpoint);
        System.out.println(">>> Status: " + response.getStatusCode());
        System.out.println(">>> Body: " + response.getBody().asString());
    }

    // ================= PUT UPDATE =================
    @When("Admin sends PUT request to {string} with valid category data")
    public void admin_sends_put_request_with_valid_category_data(String endpoint) {

        // ✅ Make sure this name matches your feature file expectation
        String requestBody = "{\n" +
                "  \"name\": \"uiuiuo\",\n" +  // <-- change to "uiuiui" if you want to match API behavior
                "  \"parentId\": null\n" +
                "}";

        response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .put(baseUrl + endpoint);

        System.out.println(">>> PUT " + endpoint);
        System.out.println(">>> Status: " + response.getStatusCode());
        System.out.println(">>> Body: " + response.getBody().asString());
    }

    // ================= COMMON ASSERTIONS =================
    @Then("Response status code should be {int}")
    public void response_status_code_should_be(int expectedStatus) {
        Assert.assertEquals(expectedStatus, response.getStatusCode());
        System.out.println(">>> Status code verified: " + expectedStatus);
    }

    @Then("Response body should contain updated category name {string}")
    public void response_body_should_contain_updated_category_name(String expectedName) {
        String actualName = response.jsonPath().getString("name");
        Assert.assertEquals("Category name was not updated correctly!", expectedName, actualName);
        System.out.println(">>> Category updated successfully: " + actualName);
    }

    // ================= NEW STEP: Sub-categories array =================
    // Add this method to fix "Response should contain sub-categories array" error
    @Then("Response should contain sub-categories array")
    public void response_should_contain_sub_categories_array() {

        String responseBody = response.getBody().asString();

        Assert.assertTrue("Response body is empty!", responseBody.length() > 0);
        Assert.assertTrue("Response is not an array!", responseBody.trim().startsWith("["));

        // Optional: check each item has 'subCategories'
        List<Map<String, Object>> categories = response.jsonPath().getList("");
        for (Map<String, Object> cat : categories) {
            Assert.assertTrue("Missing 'subCategories' in response", cat.containsKey("subCategories"));
        }

        System.out.println(">>> Sub-categories array verified");
    }

    // ================= NEW STEP: Non-existent category error =================
    // Add this method to fix "Response body should contain error message for non-existent category" error
    @Then("Response body should contain error message for non-existent category")
    public void response_body_should_contain_error_message_for_non_existent_category() {
        String responseBody = response.getBody().asString();
        System.out.println(">>> Error Response Body: " + responseBody);

        Assert.assertTrue(
            "Expected error message not found in response!",
            responseBody.toLowerCase().contains("not found")
        );

        System.out.println(">>> Verified error message for non-existent category");
    }

    // ================= GET PAGINATION =================
    @Then("Response body should contain paginated categories")
    public void response_body_should_contain_paginated_categories() {

        response.then().body("content", notNullValue());
        response.then().body("content.size()", greaterThanOrEqualTo(0));

        System.out.println(">>> Pagination content verified");
    }

    // ================= GET CATEGORIES LIST =================
    @Then("Response body should contain categories list")
    public void response_body_should_contain_categories_list() {

        response.then().body("content", notNullValue());
        response.then().body("content.size()", greaterThanOrEqualTo(0));

        System.out.println(">>> Categories list retrieved successfully");
    }
    // ================= POST CREATE MAIN CATEGORY =================
@When("Admin sends POST request to {string} with valid main category data")
public void admin_sends_post_request_with_valid_main_category_data(String endpoint) {

    // Define request body for a new main category
    String requestBody = "{\n" +
            "  \"name\": \"Option\",\n" +
            "  \"parentId\": null,\n" +   // parent = null indicates main category
            "  \"subCategories\": []\n" +
            "}";

    response = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
            .body(requestBody)
            .when()
            .post(baseUrl + endpoint);

    System.out.println(">>> POST " + endpoint);
    System.out.println(">>> Status: " + response.getStatusCode());
    System.out.println(">>> Body: " + response.getBody().asString());
}

// ================= ASSERT CREATED CATEGORY =================
@Then("Response body should contain the newly created main category name {string}")
public void response_body_should_contain_newly_created_main_category_name(String expectedName) {
    String actualName = response.jsonPath().getString("name");
    Assert.assertEquals("Main category name mismatch!", expectedName, actualName);
    System.out.println(">>> Main category created successfully: " + actualName);
}

}
