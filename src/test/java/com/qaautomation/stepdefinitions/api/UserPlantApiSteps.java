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

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserPlantApiSteps {

    private Response response;
    private RequestSpecification request;
    private String token;
    private int plantId;
    private int categoryId;


    // BACKGROUND & SETUP
    @Given("User is authenticated for plants API")
    public void user_is_authenticated_for_plants_api() {
        String username = ConfigReader.get("app.user.username");
        String password = ConfigReader.get("app.user.password");

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

        Assert.assertNotNull("User login token is null", token);
    }

    @Given("Plant with valid ID exists for retrieval")
    public void plant_with_valid_id_exists_for_retrieval() {
       
        Response plantsResponse = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get(ConfigReader.get("api.base.url") + "/api/plants/paged");
        
        if (plantsResponse.getStatusCode() == 200) {
            try {
                plantId = plantsResponse.jsonPath().getInt("content[0].id");
                System.out.println("Using existing plant with ID: " + plantId);
            } catch (Exception e) {
                Assert.fail("No plants found in the system. Please ensure at least one plant exists in the database.");
            }
        } else {
            Assert.fail("Failed to retrieve plants. Status: " + plantsResponse.getStatusCode());
        }
    }

    @Given("At least one plant exists in the api system")
    public void at_least_one_plant_exists_in_the_system() {
       
        Response plantsResponse = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get(ConfigReader.get("api.base.url") + "/api/plants/paged");
        
        if (plantsResponse.getStatusCode() == 200) {
            try {
                List<Map<String, Object>> plants = plantsResponse.jsonPath().getList("content");
                Assert.assertTrue("At least one plant should exist in the system", 
                                plants != null && plants.size() > 0);
                System.out.println("Found " + plants.size() + " plants in the system");
            } catch (Exception e) {
                Assert.fail("No plants found in the system.");
            }
        }
    }

    @Given("Multiple plants exist in the system")
    public void multiple_plants_exist_in_the_system() {
     
        Response plantsResponse = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get(ConfigReader.get("api.base.url") + "/api/plants/paged");
        
        if (plantsResponse.getStatusCode() == 200) {
            try {
                List<Map<String, Object>> plants = plantsResponse.jsonPath().getList("content");
                Assert.assertTrue("Multiple plants should exist in the system for sorting test", 
                                plants != null && plants.size() >= 2);
                System.out.println("Found " + plants.size() + " plants for sorting test");
            } catch (Exception e) {
                Assert.fail("Not enough plants found in the system for sorting test.");
            }
        }
    }

 @Given("Plants exist in a specific category")
public void plants_exist_in_a_specific_category() {
 
    Response plantsResponse = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .when()
            .get(ConfigReader.get("api.base.url") + "/api/plants/paged");
    
    if (plantsResponse.getStatusCode() == 200) {
        try {
          
            System.out.println("=== PLANTS RESPONSE DEBUG ===");
            System.out.println("Response: " + plantsResponse.prettyPrint());
            System.out.println("============================");
            
            List<Map<String, Object>> plants = plantsResponse.jsonPath().getList("content");
            Assert.assertTrue("At least one plant should exist", plants != null && plants.size() > 0);
            
            System.out.println("First plant keys: " + plants.get(0).keySet());
            System.out.println("First plant data: " + plants.get(0));
            
            
            Object categoryIdObj = plants.get(0).get("categoryId");
            if (categoryIdObj == null) {
                categoryIdObj = plants.get(0).get("category_id");
            }
            if (categoryIdObj == null) {
                categoryIdObj = plants.get(0).get("category");
                if (categoryIdObj instanceof Map) {
         
                    categoryIdObj = ((Map<String, Object>) categoryIdObj).get("id");
                }
            }
            
            Assert.assertNotNull("Category ID should not be null in plant data", categoryIdObj);
            
            categoryId = categoryIdObj instanceof Integer ? 
                (Integer) categoryIdObj : Integer.parseInt(categoryIdObj.toString());
            
            System.out.println("Using category ID: " + categoryId + " from existing plants");
        } catch (Exception e) {
            System.err.println("Error extracting category ID: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Failed to get category ID from existing plants. Error: " + e.getMessage());
        }
    } else {
        Assert.fail("Failed to retrieve plants. Status: " + plantsResponse.getStatusCode());
    }
}

    // WHEN STEPS
   
    @When("User sends a GET request to {string} with valid plant ID")
    public void user_sends_get_request_with_valid_plant_id(String endpoint) {
        request = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);

        response = request.get(ConfigReader.get("api.base.url") + endpoint.replace("{id}", String.valueOf(plantId)));
    }

    @When("User sends a GET request to {string}")
    public void user_sends_get_request(String endpoint) {
        request = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);

        response = request.get(ConfigReader.get("api.base.url") + endpoint);
    }

    @When("User sends a GET request to {string} with sort parameter {string}")
    public void user_sends_get_request_with_sort_parameter(String endpoint, String sortParam) {
        request = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .queryParam("sort", "price,desc");

        response = request.get(ConfigReader.get("api.base.url") + endpoint);
    }

    @When("User sends a GET request to {string} with category filter")
    public void user_sends_get_request_with_category_filter(String endpoint) {
        request = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .queryParam("categoryId", categoryId);

        response = request.get(ConfigReader.get("api.base.url") + endpoint);
    }

   @When("User sends a GET request to {string} with non-existent category ID")
public void user_sends_get_request_with_nonexistent_category_id(String endpoint) {
    int nonExistentCategoryId = 999999;
    request = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON);

    response = request.get(ConfigReader.get("api.base.url") + endpoint.replace("{categoryId}", String.valueOf(nonExistentCategoryId)));
}

    
    // THEN STEPS

    @Then("API should return 200 OK status code")
    public void verify_ok_status() {
        Assert.assertEquals("Expected 200 OK status code", 200, response.getStatusCode());
    }

    @Then("User API should return 404 Not Found status code")
    public void verify_not_found_status() {
        Assert.assertEquals("Expected 404 Not Found status code", 404, response.getStatusCode());
    }

    @And("Response contains plant details")
    public void verify_response_contains_plant_details() {
        Assert.assertNotNull("Plant ID should not be null", response.jsonPath().get("id"));
        Assert.assertNotNull("Plant name should not be null", response.jsonPath().get("name"));
        Assert.assertNotNull("Plant price should not be null", response.jsonPath().get("price"));
        
      
        int retrievedPlantId = response.jsonPath().getInt("id");
        Assert.assertEquals("Plant ID should match", plantId, retrievedPlantId);
    }

  @And("Response contains summary with total plants and low stock count")
public void verify_response_contains_summary() {

    System.out.println("=== API RESPONSE DEBUG ===");
    System.out.println("Response Body: " + response.getBody().asString());
    System.out.println("=========================");
    
    Assert.assertNotNull("Total plants should not be null", response.jsonPath().get("totalPlants"));
    Assert.assertNotNull("Low stock plants should not be null", response.jsonPath().get("lowStockPlants")); 
    
    int totalPlants = response.jsonPath().getInt("totalPlants");
    int lowStockPlants = response.jsonPath().getInt("lowStockPlants"); 
    
    Assert.assertTrue("Total plants should be greater than or equal to 0", totalPlants >= 0);
    Assert.assertTrue("Low stock plants should be greater than or equal to 0", lowStockPlants >= 0); 
    
    System.out.println("Summary - Total Plants: " + totalPlants + ", Low Stock Plants: " + lowStockPlants); 
}

   @And("Plants are sorted by price in descending order")
public void verify_plants_sorted_by_price() {
    List<Map<String, Object>> plants = response.jsonPath().getList("content");
    
    if (plants != null && plants.size() > 1) {
        for (int i = 0; i < plants.size() - 1; i++) {
            Object currentPriceObj = plants.get(i).get("price");
            Object nextPriceObj = plants.get(i + 1).get("price");
            
         
            double currentPrice = convertToDouble(currentPriceObj);
            double nextPrice = convertToDouble(nextPriceObj);
            
            Assert.assertTrue("Plants should be sorted by price in descending order", 
                            currentPrice >= nextPrice);
        }
        System.out.println("Verified plants are sorted by price in descending order");
    } else {
        System.out.println("Less than 2 plants found, sorting cannot be fully verified");
    }
}


private double convertToDouble(Object obj) {
    if (obj instanceof Integer) {
        return ((Integer) obj).doubleValue();
    } else if (obj instanceof Float) {
        return ((Float) obj).doubleValue();
    } else if (obj instanceof Double) {
        return (Double) obj;
    } else {
        return Double.parseDouble(obj.toString());
    }
}

  @And("Response contains only plants from the specified category")
public void verify_response_contains_only_plants_from_category() {
    List<Map<String, Object>> plants = response.jsonPath().getList("content");
    
    Assert.assertNotNull("Plants list should not be null", plants);
    Assert.assertTrue("Should have at least one plant", plants.size() > 0);
    
    for (Map<String, Object> plant : plants) {
       
        Object categoryObj = plant.get("category");
        
        Assert.assertNotNull("Category should not be null in plant data", categoryObj);
        Assert.assertTrue("Category should be a Map object", categoryObj instanceof Map);
        
        Map<String, Object> category = (Map<String, Object>) categoryObj;
        Object categoryIdObj = category.get("id");
        
        Assert.assertNotNull("Category ID should not be null", categoryIdObj);
        
        int plantCategoryId = categoryIdObj instanceof Integer ? 
            (Integer) categoryIdObj : Integer.parseInt(categoryIdObj.toString());
        
        Assert.assertEquals("All plants should belong to the specified category", 
                          categoryId, plantCategoryId);
    }
    System.out.println("Verified all " + plants.size() + " plants belong to category ID: " + categoryId);
}

    @And("User error message should be displayed")
    public void error_message_should_be_displayed() {
        String responseBody = response.getBody().asString();
        Assert.assertFalse("Expected error message in response", responseBody.isEmpty());
    }
  @And("Response should contain category not found error message")
public void verify_category_not_found_error() {
    String responseBody = response.getBody().asString();
    Assert.assertFalse("Response body should not be empty", responseBody.isEmpty());
    
   
    String errorMessage = response.jsonPath().getString("message");
    Assert.assertNotNull("Error message should be present", errorMessage);
    Assert.assertTrue("Error should indicate category not found", 
                     errorMessage.toLowerCase().contains("category") || 
                     errorMessage.toLowerCase().contains("not found"));
    
    System.out.println("Verified: Category not found error message displayed");
}
}