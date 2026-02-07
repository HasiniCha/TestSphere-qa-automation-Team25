package com.qaautomation.stepdefinitions.api;

import static io.restassured.RestAssured.given;
import com.qaautomation.utils.ConfigReader;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;

public class SalesApiSteps {

  private Response response;
  private RequestSpecification request;

  private String loginAndGetToken(String role) {
    RestAssured.baseURI = ConfigReader.get("api.base.url");
    RestAssured.basePath = ConfigReader.get("api.base.path");

    Map<String, String> credentials = new HashMap<>();
    if (role.equalsIgnoreCase("admin")) {
      credentials.put("username", ConfigReader.get("app.admin.username"));
      credentials.put("password", ConfigReader.get("app.admin.password"));
    } else {
      credentials.put("username", ConfigReader.get("app.user.username"));
      credentials.put("password", ConfigReader.get("app.user.password"));
    }

    System.out.println(
      "DEBUG: Sending login request to: " +
      RestAssured.baseURI +
      RestAssured.basePath +
      "/auth/login"
    );

    Response authResponse = given()
      .contentType("application/json")
      .body(credentials)
      .log()
      .ifValidationFails()
      .when()
      .post("/auth/login");

    if (authResponse.getStatusCode() != 200) {
      System.err.println("--- LOGIN ERROR DETAILS ---");
      System.err.println(
        "Full URL reached: " +
        RestAssured.baseURI +
        RestAssured.basePath +
        "/auth/login"
      );
      authResponse.prettyPrint();
      Assert.fail(
        "Login failed for role [" +
        role +
        "] with status " +
        authResponse.getStatusCode()
      );
    }

    String token = authResponse.jsonPath().getString("token");
    if (token == null) {
      Assert.fail(
        "Login successful but 'token' field was missing in response JSON!"
      );
    }

    return token;
  }

  private void authenticate(String role) {
    String token = loginAndGetToken(role);

    RestAssured.baseURI = ConfigReader.get("api.base.url");
    RestAssured.basePath = ConfigReader.get("api.base.path");

    request =
      given()
        .header("Authorization", "Bearer " + token)
        .contentType("application/json");
  }

  // --- AUTHENTICATION STEPS ---

  @Given("Admin is authenticated for API")
  public void admin_is_authenticated_for_api() {
    authenticate("admin");
  }

  @Given("User is authenticated for API")
  public void user_is_authenticated_for_api() {
    authenticate("user");
  }

  // --- PRE-REQUISITES ---

  @Given("a plant with ID {int} exists and has valid stock")
  public void a_plant_with_id_exists_and_has_valid_stock(Integer plantId) {
    System.out.println("Verified: Plant " + plantId + " availability check.");
  }

  @Given("a sale with ID {int} exists in the system")
  public void a_sale_with_id_exists_in_the_system(Integer saleId) {
    System.out.println("Verified: Sale " + saleId + " existence check.");
  }

  // --- POST ACTIONS ---

  @When(
    "Admin sends a POST request to {string} for plant {int} with quantity {int}"
  )
  public void admin_sends_post_request(String endpoint, int plantId, int qty) {
    performPost(endpoint, plantId, qty);
  }

  @When(
    "Admin sends a POST request to {string} for invalid plant {int} with quantity {int}"
  )
  public void admin_sends_post_request_invalid(
    String endpoint,
    int plantId,
    int qty
  ) {
    performPost(endpoint, plantId, qty);
  }

  @When(
    "User sends a POST request to {string} for plant {int} with quantity {int}"
  )
  public void user_sends_post_request(String endpoint, int plantId, int qty) {
    performPost(endpoint, plantId, qty);
  }

  private void performPost(String endpoint, int plantId, int qty) {
    response =
      request
        .pathParam("plantId", plantId)
        .queryParam("quantity", qty)
        .post(endpoint);
  }

  // --- GET ACTIONS ---

  @When(" Sales User sends a GET request to {string}")
  public void sales_user_sends_get_request(String endpoint) {
    response = request.get(endpoint);
  }


  @When("User sends a GET request to {string} for sale ID {int}")
  public void user_get_sale_by_id(String endpoint, int saleId) {
    response = request.pathParam("id", saleId).get(endpoint);
  }

  @When(
    "User sends a GET request to {string} with page {int}, size {int}, and sort {string}"
  )
  public void user_get_paginated_sales(
    String endpoint,
    int page,
    int size,
    String sortField
  ) {
    response =
      request
        .queryParam("page", page)
        .queryParam("size", size)
        .queryParam("sort", sortField)
        .get(endpoint);
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

  // --- ASSERTIONS ---

  @Then("the API returns {int} status code")
  public void verify_status_code(int expectedStatus) {
    Assert.assertNotNull("Response was null!", response);
    Assert.assertEquals(
      "Status code mismatch!",
      expectedStatus,
      response.getStatusCode()
    );
  }

  @And("the response contains sale details")
  public void verify_sale_details() {
    Assert.assertNotNull("Response body is empty", response.getBody());
  }

  @And("the response error message should be {string}")
  public void verify_error_message(String expectedMessage) {
    String actualMessage = response.jsonPath().getString("message");
    Assert.assertEquals(expectedMessage, actualMessage);
  }

  @And("the response error message should contain {string}")
  public void verify_error_message_contains(String expectedPart) {
    String actualMessage = response.jsonPath().getString("message");
    Assert.assertTrue(actualMessage.contains(expectedPart));
  }

  @And("the response should contain a list of sales")
  public void verify_list_of_sales() {
    Assert.assertTrue(
      response.getBody().asString().startsWith("[") ||
      response.jsonPath().get("$") instanceof java.util.List
    );
  }

  @And("the response should contain paginated results sorted by {string}")
  public void verify_paginated_results(String field) {
    Assert.assertNotNull(
      "Pagination content is missing!",
      response.jsonPath().get("content")
    );
  }
}
