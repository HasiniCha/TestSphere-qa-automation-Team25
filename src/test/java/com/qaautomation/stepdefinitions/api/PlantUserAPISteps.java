package com.qaautomation.stepdefinitions.api;

import static io.restassured.RestAssured.given;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import com.qaautomation.utils.ConfigReader; 
import java.util.HashMap; 
import java.util.List;
import java.util.Map; 

public class PlantUserAPISteps {

    private Response response;
    private RequestSpecification request;

    // --- DYNAMIC AUTHENTICATION LOGIC ---
    
    private String loginAndGetToken(String role) {
        RestAssured.baseURI = ConfigReader.get("api.base.url");

        Map<String, String> credentials = new HashMap<>();
        if (role.equalsIgnoreCase("admin")) {
            credentials.put("username", ConfigReader.get("app.admin.username"));
            credentials.put("password", ConfigReader.get("app.admin.password"));
        } else {
            credentials.put("username", ConfigReader.get("app.user.username"));
            credentials.put("password", ConfigReader.get("app.user.password"));
        }

        Response authResponse = given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/api/auth/login");

        if (authResponse.getStatusCode() != 200) {
            authResponse.prettyPrint();
            Assert.fail("User Auth failed. Status: " + authResponse.getStatusCode());
        }

        return authResponse.jsonPath().getString("token");
    }

    private void authenticate(String role) {
        String token = loginAndGetToken(role);
        request = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);
    }

    @Given("User is authenticated via API")
    public void user_is_authenticated_via_api() {
        authenticate("user");
    }

    // --- PRE-REQUISITES ---
    @Given("A valid category exists with ID {int}")
    public void a_valid_category_exists_with_id(Integer id) {
        // Just a context step, actual verification happens in the GET call
        System.out.println("Testing retrieval for Category ID: " + id);
    }

    // --- GET ACTIONS ---
    @When("User sends a GET request to {string}")
    public void user_sends_get_request(String endpoint) {
        response = request.get(endpoint);
        response.prettyPrint();
    }

    @When("User sends a GET request to {string} with page {int} and size {int}")
    public void user_sends_paged_request(String endpoint, int page, int size) {
        response = request
                .queryParam("page", page)
                .queryParam("size", size)
                .get(endpoint);
        response.prettyPrint();
    }

    @When("User sends a GET request to search {string} for name {string}")
    public void user_sends_search_request(String endpoint, String nameQuery) {
        response = request
                .queryParam("name", nameQuery)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .get(endpoint);
        response.prettyPrint();
    }

    // --- ASSERTIONS ---
    @Then("The response should contain a list of plants")
    public void verify_list_of_plants() {
        // Checks if the response body is an Array (starts with [ )
        String responseBody = response.getBody().asString().trim();
        Assert.assertTrue("Response is not a list/array!", responseBody.startsWith("["));
        
        List<Object> plants = response.jsonPath().getList("$");
        System.out.println("Number of plants found: " + plants.size());
    }

    @Then("The response body should contain {string}")
    public void verify_body_has_field(String field) {
        Object value = response.jsonPath().get(field);
        Assert.assertNotNull("Field '" + field + "' is missing in response!", value);
    }

    @Then("The response list {string} should have size less than or equal to {int}")
    public void verify_list_size(String listField, int maxSize) {
        List<Object> list = response.jsonPath().getList(listField);
        Assert.assertNotNull(listField + " is null", list);
        Assert.assertTrue("List size " + list.size() + " exceeds max " + maxSize, list.size() <= maxSize);
    }

   
    @Then("The User receives status code {int}")
    public void verify_user_status_code(Integer expectedStatus) {
        Assert.assertNotNull("Response is null! Did the request fail?", response);
        Assert.assertEquals("Status Code Mismatch!", (int)expectedStatus, response.getStatusCode());
    }

    // Add this inside the class
    @Then("The User response should contain {string} with text {string}")
    public void verify_user_response_text(String field, String expectedText) {
        Assert.assertNotNull("Response is null!", response);
        String actualValue = response.jsonPath().getString(field);
        
        if(actualValue == null) actualValue = "";
        
        Assert.assertTrue("Expected '" + expectedText + "' but found '" + actualValue + "'", 
                          actualValue.toLowerCase().contains(expectedText.toLowerCase()));
    }

    @Then("The User response body should contain {string} with text {string}")
    public void verify_user_response_body_text(String field, String expectedText) {
        Assert.assertNotNull("User Response is null!", response);
        String actualValue = response.jsonPath().getString(field);
        
        if(actualValue == null) actualValue = "";
        
        Assert.assertTrue("Expected text '" + expectedText + "' not found in: " + actualValue, 
                          actualValue.toLowerCase().contains(expectedText.toLowerCase()));
    }
}