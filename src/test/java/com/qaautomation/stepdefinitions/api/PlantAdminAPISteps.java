package com.qaautomation.stepdefinitions;

import static io.restassured.RestAssured.given;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import com.qaautomation.utils.ConfigReader; 
import java.util.HashMap;
import java.util.Map;

public class PlantAdminAPISteps {

    private Response response;
    private RequestSpecification request;
    private int sharedPlantId; 

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
            Assert.fail("Auth failed for " + role + ". Status: " + authResponse.getStatusCode());
        }

        return authResponse.jsonPath().getString("token");
    }

    private void authenticate(String role) {
        String token = loginAndGetToken(role);
        request = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);
    }

    @Given("Admin is authenticated via API for Plant")
    public void admin_is_authenticated_via_api() {
        authenticate("admin");
    }

    // --- PRE-REQUISITES ---

    @Given("A valid sub-category exists with ID {int}")
    public void a_valid_sub_category_exists_with_id(Integer id) {
        System.out.println("Using Sub-Category ID: " + id);
    }

    @Given("A parent category exists with ID {int}")
    public void a_parent_category_exists_with_id(Integer id) {
        System.out.println("Using Parent Category ID: " + id);
    }

    @Given("A plant exists with name {string}")
    public void a_plant_exists_with_name(String baseName) {
        String uniqueName = baseName + " " + System.currentTimeMillis(); 
        String jsonBody = "{\"name\": \"" + uniqueName + "\", \"price\": 10.0, \"quantity\": 10}";
        
        Response createResp = request
                .body(jsonBody)
                .post("/api/plants/category/2"); 
        
        if(createResp.getStatusCode() != 200 && createResp.getStatusCode() != 201) {
            System.out.println("Setup Failed! Server Response: " + createResp.getBody().asString());
            throw new RuntimeException("Setup Failed! Status: " + createResp.getStatusCode());
        }

        sharedPlantId = createResp.jsonPath().getInt("id");
        System.out.println("Created Unique Plant: " + uniqueName + " (ID: " + sharedPlantId + ")");
    }

    @Given("A plant exists with ID {int}")
    public void a_plant_exists_with_id(Integer id) {
        response = request.get("/api/plants/" + id);
        
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Test setup failed: Plant with ID " + id + " was not found in the database.");
        }
        System.out.println("Verified existence of Plant ID: " + id);
    }

    // --- POST ACTIONS ---

    @When("I send a POST request to {string} with the following data:")
    public void i_send_a_post_request_to_with_data(String endpoint, DataTable dataTable) {
        Map<String, Object> typedData = convertDataTableToMap(dataTable);
        response = request
                .body(typedData)
                .post(endpoint);
        response.prettyPrint(); 
    }

    // --- PUT ACTIONS ---

    @When("I send a PUT request to update the plant with:")
    public void i_send_a_put_request_to_update_the_plant_with(DataTable dataTable) {
        Map<String, Object> typedData = convertDataTableToMap(dataTable);
        response = request
                .body(typedData)
                .put("/api/plants/" + sharedPlantId);
        response.prettyPrint();
    }

    @When("I send a PUT request to {string} with the following data:")
    public void i_send_a_put_request_to_with_data(String endpoint, DataTable dataTable) {
        Map<String, Object> typedData = convertDataTableToMap(dataTable);
        response = request
                .body(typedData)
                .put(endpoint);
        response.prettyPrint();
    }

    // --- ASSERTIONS ---

    @Then("The API response status code should be {int}")
    public void verify_status_code(Integer expectedStatus) {
        Assert.assertNotNull("Response is null", response);
        Assert.assertEquals("Status Code Mismatch!", (int)expectedStatus, response.getStatusCode());
    }

    @Then("The API response status code should be {int} or {int}")
    public void verify_status_code_or(Integer code1, Integer code2) {
        int actual = response.getStatusCode();
        Assert.assertTrue("Status " + actual + " not in " + code1 + "/" + code2, 
                          actual == code1 || actual == code2);
    }

    @Then("The response body should contain a generated {string}")
    public void verify_generated_field(String field) {
        String value = response.jsonPath().getString(field);
        Assert.assertNotNull("Field '" + field + "' is missing/null", value);
    }

    @Then("The response body should contain {string} with value {string}")
    public void verify_body_field_value(String field, String expectedValue) {
        String actualValue = response.jsonPath().getString(field);
        Assert.assertEquals("Field value mismatch", expectedValue, actualValue);
    }

    @Then("The response body should contain {string} with value {int}")
    public void verify_body_field_int_value(String field, Integer expectedValue) {
        int actualValue = response.jsonPath().getInt(field);
        Assert.assertEquals("ID mismatch!", (int)expectedValue, actualValue);
    }

    @Then("The response body should contain {string} with text {string}")
    public void verify_body_contains_text(String field, String partialText) {
        String actualValue = response.jsonPath().getString(field);
        if(actualValue == null) actualValue = ""; 
        Assert.assertTrue("Expected '" + partialText + "' inside '" + actualValue + "'", 
                          actualValue.contains(partialText));
    }

    // --- HELPER METHODS ---
    
    private Map<String, Object> convertDataTableToMap(DataTable dataTable) {
        Map<String, String> rawData = dataTable.asMap(String.class, String.class);
        Map<String, Object> typedData = new HashMap<>();

        for (Map.Entry<String, String> entry : rawData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.equals("price")) {
                typedData.put(key, Double.parseDouble(value));
            } else if (key.equals("quantity") || key.equals("id")) {
                typedData.put(key, Integer.parseInt(value));
            } else {
                typedData.put(key, value);
            }
        }
        return typedData;
    }
}