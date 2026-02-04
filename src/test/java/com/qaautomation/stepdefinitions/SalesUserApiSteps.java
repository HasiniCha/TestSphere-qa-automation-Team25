// package com.qaautomation.stepdefinitions;

// import static io.restassured.RestAssured.given;
// import com.qaautomation.utils.ConfigReader;
// import io.cucumber.java.en.*;
// import io.restassured.RestAssured;
// import io.restassured.response.Response;
// import io.restassured.specification.RequestSpecification;
// import org.junit.Assert;
// import java.util.HashMap;
// import java.util.Map;

// public class SalesUserApiSteps {

//     private Response response;
//     private RequestSpecification request;

//     @Given("User is authenticated for API")
//     public void user_is_authenticated_for_api() {
//         RestAssured.baseURI = ConfigReader.get("api.base.url");
//         RestAssured.basePath = ConfigReader.get("api.base.path");
        
//         // Fetching the USER token (distinct from Admin token)
//         String authToken = ConfigReader.get("api.user.token");

//         request = given()
//                 .header("Authorization", "Bearer " + authToken)
//                 .contentType("application/json");
//     }

//     @When("User sends a POST request to {string} for plant {int} with quantity {int}")
//     public void user_sends_post_request(String endpoint, int plantId, int qty) {
//         response = request
//                 .pathParam("plantId", plantId)
//                 .queryParam("quantity", qty)
//                 .post(endpoint);
//     }

//     @When("User sends a DELETE request to {string} for sale ID {int}")
//     public void user_sends_delete_request(String endpoint, int saleId) {
//         response = request.pathParam("id", saleId).delete(endpoint);
//     }

//     @When("User sends a GET request to {string}")
//     public void user_sends_get_request(String endpoint) {
//         response = request.get(endpoint);
//     }

//     @When("User sends a GET request to {string} for sale ID {int}")
//     public void user_get_sale_by_id(String endpoint, int saleId) {
//         response = request.pathParam("id", saleId).get(endpoint);
//     }

//     @When("User sends a GET request to {string} with page {int}, size {int}, and sort {string}")
//     public void user_get_paginated_sales(String endpoint, int page, int size, String sortField) {
//         // Query parameters for pagination and sorting
//         response = request
//                 .queryParam("page", page)
//                 .queryParam("size", size)
//                 .queryParam("sort", sortField)
//                 .get(endpoint);
//     }

//     @Then("the response should contain a list of sales")
//     public void verify_list_of_sales() {
//         Assert.assertTrue("Response is not a list", response.getBody().asString().startsWith("[") || response.jsonPath().get("content") != null);
//     }

//     @And("the response should contain paginated results sorted by {string}")
//     public void verify_paginated_results(String field) {
//         // Checking if 'content' exists in the paginated response
//         Assert.assertNotNull("Paginated content missing", response.jsonPath().get("content"));
//     }
// }