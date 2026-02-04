package com.qaautomation.stepdefinitions;

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

    @Given("User is authenticated for API")
    public void user_is_authenticated_for_api() {
        authenticate(ConfigReader.get("api.user.token"));
    }

    private void authenticate(String token) {
        RestAssured.baseURI = ConfigReader.get("api.base.url");
        RestAssured.basePath = ConfigReader.get("api.base.path");
        request = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json");
    }

    // --- POST ACTIONS ---

    @When("Admin sends a POST request to {string} for plant {int} with quantity {int}")
    public void admin_sends_post_request(String endpoint, int plantId, int qty) {
        performPost(endpoint, plantId, qty);
    }

    @When("User sends a POST request to {string} for plant {int} with quantity {int}")
    public void user_sends_post_request(String endpoint, int plantId, int qty) {
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
        response = request.pathParam("id", id).delete(endpoint);
    }

    @When("User sends a DELETE request to {string} for sale ID {int}")
    public void user_delete_request(String endpoint, int id) {
        response = request.pathParam("id", id).delete(endpoint);
    }

    // --- GET ACTIONS ---

    @When("User sends a GET request to {string}")
    public void user_sends_get_request(String endpoint) {
        response = request.get(endpoint);
    }

    @When("User sends a GET request to {string} for sale ID {int}")
    public void user_get_sale_by_id(String endpoint, int saleId) {
        response = request.pathParam("id", saleId).get(endpoint);
    }

    @When("User sends a GET request to {string} with page {int}, size {int}, and sort {string}")
    public void user_get_paginated_sales(String endpoint, int page, int size, String sortField) {
        response = request
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sortField)
                .get(endpoint);
    }

    // --- ASSERTIONS ---

    @Then("the API returns {int} status code")
    public void verify_status_code(int expectedStatus) {
        Assert.assertNotNull("Response is null!", response);
        Assert.assertEquals("Status code mismatch!", expectedStatus, response.getStatusCode());
    }

    @And("the response error message should be {string}")
    public void verify_error_message(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @And("the response should contain a list of sales")
    public void verify_list() {
        Assert.assertNotNull(response.getBody());
    }

    @And("the response should contain paginated results sorted by {string}")
    public void verify_paginated(String field) {
        Assert.assertNotNull(response.jsonPath().get("content"));
    }
}