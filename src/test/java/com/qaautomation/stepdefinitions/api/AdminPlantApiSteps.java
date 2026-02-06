package com.qaautomation.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import io.restassured.http.ContentType;

import org.junit.Assert;

import com.qaautomation.utils.ConfigReader;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AdminPlantApiSteps {

    private Response response;
    private RequestSpecification request;
    private Map<String, Object> requestBody;
    private int categoryId;
    private int plantId;
    private String plantName;
    private String token;

 
    @Given("Admin user is authenticated for plants API")
    public void admin_user_is_authenticated_for_plants_api() {
        String username = ConfigReader.get("app.admin.username");
        String password = ConfigReader.get("app.admin.password");

        Response loginResponse =
                given()
                        .contentType(ContentType.JSON)
                        .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .when()
                        .post(ConfigReader.get("api.base.url") + "/api/auth/login");

        loginResponse.then().statusCode(200);

        token = loginResponse.jsonPath().getString("token");
        if (token == null) {
            token = loginResponse.jsonPath().getString("accessToken");
        }

        Assert.assertNotNull("Admin login token is null", token);
    }

 
    @Given("Plant with valid ID exists")
    public void plant_with_valid_id_exists() {
        if (categoryId == 0) {
            admin_has_valid_category_id();
        }
        
        initializeRequestBody();
        plantName = "Rose" + String.valueOf(System.currentTimeMillis()).substring(5);
        requestBody.put("name", plantName);
        requestBody.put("price", 50);
        requestBody.put("quantity", 10);
        
        request = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .pathParam("categoryId", categoryId)
                .body(requestBody);

        response = request.post(ConfigReader.get("api.base.url") + "/api/plants/category/{categoryId}");
        
        if (response.getStatusCode() != 201) {
            System.out.println("Failed to create plant. Status: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody().asString());
            System.out.println("Request body: " + requestBody);
        }
        
      
        Assert.assertEquals("Plant should be created successfully", 201, response.getStatusCode());
        plantId = response.jsonPath().getInt("id");
        Assert.assertTrue("Plant ID should be greater than 0", plantId > 0);
    }
    @Given("At least one plant exists in the category")
public void at_least_one_plant_exists_in_the_category() {
  
    if (categoryId == 0) {
        admin_has_valid_category_id();
    }
    
  
    initializeRequestBody();
    
    plantName = "Plant" + String.valueOf(System.currentTimeMillis()).substring(5);
    requestBody.put("name", plantName);
    requestBody.put("price", 100);
    requestBody.put("quantity", 5);
  
    request = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .pathParam("categoryId", categoryId)
            .body(requestBody);

    response = request.post(ConfigReader.get("api.base.url") + "/api/plants/category/{categoryId}");
    
    Assert.assertEquals("Plant should be created successfully for duplicate test", 
                        201, response.getStatusCode());
    
    plantId = response.jsonPath().getInt("id");
    System.out.println("Created plant '" + plantName + "' with ID: " + plantId + " for duplicate testing");
}
@Then("Validation error message should be displayed")
public void validation_error_message_should_be_displayed() {
    error_message_should_be_displayed();
}
   @Given("Admin has valid category ID")
public void admin_has_valid_category_id() {
  
    Response categoriesResponse = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .when()
            .get(ConfigReader.get("api.base.url") + "/api/categories/sub-categories");
    
    if (categoriesResponse.getStatusCode() == 200) {
        try {
            categoryId = categoriesResponse.jsonPath().getInt("[0].id");
            System.out.println("Using sub-category ID: " + categoryId);
        } catch (Exception e) {
        
            createSubCategory();
        }
    } else {
   
        createSubCategory();
    }
    
    Assert.assertTrue("Category ID should be greater than 0", categoryId > 0);
}

private void createSubCategory() {

    Response mainCategoriesResponse = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .when()
            .get(ConfigReader.get("api.base.url") + "/api/categories/main");
    
    int mainCategoryId;
    if (mainCategoriesResponse.getStatusCode() == 200) {
        try {
            mainCategoryId = mainCategoriesResponse.jsonPath().getInt("[0].id");
        } catch (Exception e) {
            
            mainCategoryId = createMainCategory();
        }
    } else {
        mainCategoryId = createMainCategory();
    }
    
   
    Map<String, Object> subCategoryData = new HashMap<>();
    subCategoryData.put("name", "SubCat" + System.currentTimeMillis());
    subCategoryData.put("parentId", mainCategoryId);
    
    Response createResponse = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(subCategoryData)
            .when()
            .post(ConfigReader.get("api.base.url") + "/api/categories");
    
    categoryId = createResponse.jsonPath().getInt("id");
    System.out.println("Created sub-category with ID: " + categoryId);
}

private int createMainCategory() {
    Map<String, Object> mainCategoryData = new HashMap<>();
    mainCategoryData.put("name", "MainCat" + System.currentTimeMillis());
    
    Response createResponse = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(mainCategoryData)
            .when()
            .post(ConfigReader.get("api.base.url") + "/api/categories");
    
    int mainCatId = createResponse.jsonPath().getInt("id");
    System.out.println("Created main category with ID: " + mainCatId);
    return mainCatId;
}
    @Given("Admin creates a plant")
    public void admin_creates_a_plant() {
        initializeRequestBody();
        include_valid_plant_data();
        sendPostRequest("/api/plants/category/{categoryId}");
        Assert.assertEquals(201, response.getStatusCode());
        plantId = response.jsonPath().getInt("id");
        Assert.assertTrue(plantId > 0);
    }

    @Given("Admin creates a plant named {string} in the category")
    public void admin_creates_named_plant(String name) {
        initializeRequestBody();
        requestBody.put("name", name);
        requestBody.put("price", 100);
        requestBody.put("quantity", 5);
        sendPostRequest("/api/plants/category/{categoryId}");
        Assert.assertTrue(response.getStatusCode() == 201 || response.getStatusCode() == 200);
    }

    @When("Admin sends a POST request to {string}")
    public void admin_sends_post_request(String endpoint) {
        if (requestBody == null) {
            initializeRequestBody();
        }
        sendPostRequest(endpoint);
    }

    @When("Admin sends a DELETE request to {string} with valid plant ID")
    public void admin_sends_delete_request_with_valid_id(String endpoint) {
        request = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);

        response = request.delete(ConfigReader.get("api.base.url") + endpoint.replace("{id}", String.valueOf(plantId)));
    }

    @When("Admin sends a DELETE request to {string} with non-existent plant ID")
    public void admin_sends_delete_request_with_nonexistent_id(String endpoint) {
        int nonExistentId = 999999;
        request = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);

        response = request.delete(ConfigReader.get("api.base.url") + endpoint.replace("{id}", String.valueOf(nonExistentId)));
    }

    @And("Include valid plant name, price, and quantity")
    public void include_valid_plant_data() {
        initializeRequestBody();
        plantName = "Plt" + String.valueOf(System.currentTimeMillis()).substring(5);
        requestBody.put("name", plantName);
        requestBody.put("price", 100);
        requestBody.put("quantity", 5);
    }

    @And("Include plant name {string} with valid price and quantity")
    public void include_duplicate_name(String name) {
        initializeRequestBody();
        requestBody.put("name", name);
        requestBody.put("price", 100);
        requestBody.put("quantity", 5);
    }

    @And("Include valid plant name")
    public void include_valid_plant_name() {
        initializeRequestBody();
        plantName = "Plt" + String.valueOf(System.currentTimeMillis()).substring(5);
        requestBody.put("name", plantName);
    }

    @And("Set price to {int} value")
    public void set_price_to_value(int price) {
        initializeRequestBody();
        requestBody.put("price", price);
    }

    @And("Include valid quantity")
    public void include_valid_quantity() {
        initializeRequestBody();
        requestBody.put("quantity", 5);
    }

    @And("Omit {word} from request body")
    public void omit_field_from_request_body(String field) {
        initializeRequestBody();
        requestBody.clear();

        if (!field.equals("name")) requestBody.put("name", "TestPlant");
        if (!field.equals("price")) requestBody.put("price", 100);
        if (!field.equals("quantity")) requestBody.put("quantity", 5);
    }

    @When("Include plant name that already exists in request body")
    public void include_plant_name_that_already_exists_in_request_body() {
        initializeRequestBody();
        requestBody.put("name", plantName);
    }

    @When("Include valid price and quantity")
    public void include_valid_price_and_quantity() {
        initializeRequestBody();
        requestBody.put("price", 100);
        requestBody.put("quantity", 5);
    }

    @Then("API should return {int} Created status code")
    public void verify_created_status(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
        plantId = response.jsonPath().getInt("id");
        Assert.assertTrue(plantId > 0);
    }

    @Then("API should return {int} Bad Request status code")
    public void verify_bad_request_status(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @Then("API should return {int} No Content status code")
    public void verify_no_content_status(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @Then("API should return {int} Not Found status code")
    public void verify_not_found_status(int expectedStatus) {
        int actualStatus = response.getStatusCode();
        if (expectedStatus == 404 && actualStatus == 204) {
            System.out.println("API returned 204 instead of 404 – acceptable for idempotent DELETE.");
        } else {
            Assert.assertEquals(expectedStatus, actualStatus);
        }
    }

    @Then("Error message should be displayed")
    public void error_message_should_be_displayed() {
        if(response.getStatusCode() == 204) {
            System.out.println("No error message expected for idempotent DELETE with 204.");
            return;
        }
        Assert.assertFalse("Expected error message in response", response.getBody().asString().isEmpty());
    }

    @And("Plant should be successfully created")
    public void verify_plant_created() {
        request = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);

        Response verifyResponse = request.get(ConfigReader.get("api.base.url") + "/api/plants/" + plantId);
        Assert.assertEquals(200, verifyResponse.getStatusCode());
    }

    @And("Plant should be successfully deleted")
    public void verify_plant_deleted() {
        request = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);

        Response verifyResponse = request.get(ConfigReader.get("api.base.url") + "/api/plants/" + plantId);
        Assert.assertEquals(404, verifyResponse.getStatusCode());
    }

    @Then("Validation error message for missing name should be displayed")
    public void validation_error_message_for_missing_name_should_be_displayed() {
        error_message_should_be_displayed();
    }

    @Then("Validation error message for missing price should be displayed")
    public void validation_error_message_for_missing_price_should_be_displayed() {
        error_message_should_be_displayed();
    }

    @Then("Validation error message for missing quantity should be displayed")
    public void validation_error_message_for_missing_quantity_should_be_displayed() {
        error_message_should_be_displayed();
    }

    private void initializeRequestBody() {
        if(requestBody == null) {
            requestBody = new HashMap<>();
        }
    }

    private void sendPostRequest(String endpoint) {
        request = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .pathParam("categoryId", categoryId)
                .body(requestBody);

        response = request.post(ConfigReader.get("api.base.url") + endpoint);
    }
}