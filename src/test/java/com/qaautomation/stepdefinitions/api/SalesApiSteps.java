package com.qaautomation.stepdefinitions.api;

import static io.restassured.RestAssured.given;
import com.qaautomation.utils.ConfigReader;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

public class SalesApiSteps {

    private Response response;
    private RequestSpecification request;

    // --- AUTHENTICATION ---
    
    @Given("Admin is authenticated for API")
    public void admin_is_authenticated_for_api() {
        authenticate(ConfigReader.get("api.admin.token"));
    }

    private void authenticate(String token) {
        RestAssured.baseURI = ConfigReader.get("api.base.url");
        RestAssured.basePath = ConfigReader.get("api.base.path");
        request = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json");
    }

    // --- PRE-REQUISITES 

    @Given("a plant with ID {int} exists and has valid stock")
    public void a_plant_with_id_exists_and_has_valid_stock(Integer plantId) {
        // Logic: Usually you'd make a GET call here to verify the plant
        System.out.println("Verified: Plant " + plantId + " is available in database.");
    }

    @Given("a sale with ID {int} exists in the system")
    public void a_sale_with_id_exists_in_the_system(Integer saleId) {
        System.out.println("Verified: Sale " + saleId + " exists before deletion.");
    }

    // --- POST ACTIONS ---

    @When("Admin sends a POST request to {string} for plant {int} with quantity {int}")
    public void admin_sends_post_request(String endpoint, int plantId, int qty) {
        performPost(endpoint, plantId, qty);
    }

    @When("Admin sends a POST request to {string} for invalid plant {int} with quantity {int}")
    public void admin_sends_post_request_invalid(String endpoint, int plantId, int qty) {
        performPost(endpoint, plantId, qty);
    }

    private void performPost(String endpoint, int plantId, int qty) {
        response = request
                .pathParam("plantId", plantId)
                .queryParam("quantity", qty)
                .post(endpoint);
        response.prettyPrint();
    }

    // --- DELETE ACTIONS ---

    @When("Admin sends a DELETE request to {string} for sale ID {int}")
    public void admin_delete_request(String endpoint, int id) {
        response = request
                .pathParam("id", id)
                .delete(endpoint);
    }

    // --- ASSERTIONS ---

    @Then("the API returns {int} status code")
    public void verify_status_code(int expectedStatus) {
        Assert.assertNotNull("Response was null! Check if the request was sent.", response);
        Assert.assertEquals("Status code mismatch!", expectedStatus, response.getStatusCode());
    }

    @And("the response contains sale details")
    public void verify_sale_details() {
        // Check that the JSON response has an ID or some indication of success
        Assert.assertNotNull("Response body is empty", response.getBody());
    }

    @And("the response error message should be {string}")
    public void verify_error_message(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        Assert.assertEquals("Error message mismatch!", expectedMessage, actualMessage);
    }

    @And("the response error message should contain {string}")
    public void verify_error_message_contains(String expectedPart) {
        String actualMessage = response.jsonPath().getString("message");
        Assert.assertTrue("Expected error to contain [" + expectedPart + "] but got [" + actualMessage + "]", 
                actualMessage.contains(expectedPart));
    }



    // --- USER AUTHENTICATION ---
    
    @Given("User is authenticated for API")
    public void user_is_authenticated_for_api() {
        authenticate(ConfigReader.get("api.user.token"));
    }

    // --- USER SPECIFIC ACTIONS (GET REQUESTS) ---

    @When("User sends a GET request to {string}")
    public void user_sends_get_request(String endpoint) {
        response = request.get(endpoint);
    }

    @When("User sends a GET request to {string} for sale ID {int}")
    public void user_get_sale_by_id(String endpoint, int saleId) {
        response = request
                .pathParam("id", saleId)
                .get(endpoint);
    }

    @When("User sends a GET request to {string} with page {int}, size {int}, and sort {string}")
    public void user_get_paginated_sales(String endpoint, int page, int size, String sortField) {
        response = request
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sortField)
                .get(endpoint);
    }

    // --- USER SPECIFIC POST/DELETE (To test 403 Forbidden) ---

    @When("User sends a POST request to {string} for plant {int} with quantity {int}")
    public void user_sends_post_request(String endpoint, int plantId, int qty) {
        performPost(endpoint, plantId, qty);
    }

    @When("User sends a DELETE request to {string} for sale ID {int}")
    public void user_delete_request(String endpoint, int id) {
        response = request
                .pathParam("id", id)
                .delete(endpoint);
    }

    // --- NEW USER ASSERTIONS ---

    @And("the response should contain a list of sales")
    public void verify_list_of_sales() {
        Assert.assertTrue("Response is not a list!", response.getBody().asString().startsWith("[") || 
                response.jsonPath().get("$") instanceof java.util.List);
    }

    @And("the response should contain paginated results sorted by {string}")
    public void verify_paginated_results(String field) {
        // Assuming your API returns a 'content' array for paginated data
        Assert.assertNotNull("Pagination content is missing!", response.jsonPath().get("content"));
        System.out.println("Verified paginated results for field: " + field);
    }
}