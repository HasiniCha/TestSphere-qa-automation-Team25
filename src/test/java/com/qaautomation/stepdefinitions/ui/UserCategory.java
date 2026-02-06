package com.qaautomation.stepdefinitions.ui;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qaautomation.pages.Category1.UserCategoryPage;
import com.qaautomation.utils.ConfigReader;
import com.qaautomation.utils.DriverFactory;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UserCategory {

    UserCategoryPage userCategoryPage;

    @Given("User is logged in")
    public void user_is_logged_in() {
        WebDriver driver = DriverFactory.getDriver();
        
        // Navigate to login page
        driver.get(ConfigReader.get("app.url"));
        
        // Perform User login directly here (without modifying LoginPage)
        driver.findElement(By.name("username")).sendKeys(ConfigReader.get("app.user.username"));
        driver.findElement(By.name("password")).sendKeys(ConfigReader.get("app.user.password"));
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        
        System.out.println(">>> User logged in successfully");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("User navigates to the category list page")
    public void user_navigates_to_the_category_list_page() {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("http://localhost:8080/ui/categories");
        userCategoryPage = new UserCategoryPage(driver);
        
        System.out.println(">>> User navigating to category list page...");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("User should be redirected to {string} successfully")
    public void user_should_be_redirected_to_successfully(String expectedUrl) {
        WebDriver driver = DriverFactory.getDriver();
        String actualUrl = userCategoryPage.getCurrentUrl();
        String fullExpectedUrl = "http://localhost:8080" + expectedUrl;
        
        System.out.println(">>> Expected URL: " + fullExpectedUrl);
        System.out.println(">>> Actual URL: " + actualUrl);
        
        Assert.assertEquals("User was not redirected to the correct page!", 
                           fullExpectedUrl, actualUrl);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("Category list should be visible to User")
    public void category_list_should_be_visible_to_user() {
        System.out.println(">>> Checking if category list is visible...");
        
        Assert.assertTrue("Category list is not visible to User!", 
                         userCategoryPage.isCategoryListVisible());
        
        System.out.println(">>> Category list is visible to User");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println(">>> User category test completed successfully!");
    }
    @Then("Add Category button should not be visible to User")
    public void add_category_button_should_not_be_visible_to_user() {
        System.out.println(">>> Checking if Add Category button is hidden from User...");
        
        Assert.assertTrue("Add Category button is visible to User (but should be hidden)!", 
                         userCategoryPage.isAddCategoryButtonNotVisible());
        
        System.out.println(">>> Add Category button is correctly hidden from User");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println(">>> User button absence test completed successfully!");
    }
    @When("User clicks Edit icon for a category")
    public void user_clicks_edit_icon_for_a_category() {
        System.out.println(">>> User clicking Edit icon...");
        userCategoryPage.clickEditIcon();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("User modifies the category name")
    public void user_modifies_the_category_name() {
        System.out.println(">>> User modifying category name...");
        userCategoryPage.modifyCategoryName("Modified");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("User clicks Save button")
    public void user_clicks_save_button() {
        System.out.println(">>> User clicking Save button...");
        userCategoryPage.clickSaveButton();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("An error message should appear")
    public void an_error_message_should_appear() {
        System.out.println(">>> Checking if error message appears...");
        
        Assert.assertTrue("Error message did not appear for User trying to edit!", 
                         userCategoryPage.isErrorMessageDisplayed());
        
        System.out.println(">>> Error message correctly appeared - User cannot edit");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println(">>> User edit restriction test completed successfully!");
    }
   @When("User clicks on the Parent column header")
public void user_clicks_on_the_parent_column_header() {
    System.out.println(">>> User clicking Parent column header to sort...");
    userCategoryPage.clickParentColumnHeader();
    
    try {
        Thread.sleep(1500);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

@Then("Categories should be sorted by parent category")
public void categories_should_be_sorted_by_parent_category() {
    System.out.println(">>> Verifying categories are sorted...");
    
    Assert.assertTrue("Category list not displayed after sorting!", 
                     userCategoryPage.isCategorySorted());
    
    System.out.println(">>> Categories sorted successfully");
}

@When("User clicks on the Parent column header again")
public void user_clicks_on_the_parent_column_header_again() {
    System.out.println(">>> User clicking Parent column header again (reverse sort)...");
    userCategoryPage.clickParentColumnHeader();
    
    try {
        Thread.sleep(1500);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

@Then("Sort order should toggle to descending")
public void sort_order_should_toggle_to_descending() {
    System.out.println(">>> Verifying sort order toggled...");
    
    Assert.assertTrue("Sort did not toggle properly!", 
                     userCategoryPage.isCategorySorted());
    
    System.out.println(">>> Sort order toggled successfully");
    System.out.println(">>> User category sorting test completed!");
} 
@When("User enters {string} in search field")
public void user_enters_in_search_field(String searchText) {
    System.out.println(">>> User entering search text...");
    userCategoryPage.enterSearchText(searchText);
}

@When("User selects a parent category from dropdown")
public void user_selects_a_parent_category_from_dropdown() {
    System.out.println(">>> User selecting parent category...");
    userCategoryPage.selectParentCategory("3455@");
}

@When("User clicks Search button")
public void user_clicks_search_button() {
    System.out.println(">>> User clicking Search button...");
    userCategoryPage.clickSearchButton();
    
    try {
        Thread.sleep(1500);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

@When("User clicks Reset button")
public void user_clicks_reset_button() {
    System.out.println(">>> User clicking Reset button...");
    userCategoryPage.clickResetButton();
    
    try {
        Thread.sleep(1500);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

@Then("Search field should be cleared")
public void search_field_should_be_cleared() {
    System.out.println(">>> Verifying search field is cleared...");
    Assert.assertTrue("Search field is not cleared!", 
                     userCategoryPage.isSearchFieldCleared());
    System.out.println(">>> Search field cleared successfully");
}

@Then("Parent dropdown should reset to default")
public void parent_dropdown_should_reset_to_default() {
    System.out.println(">>> Verifying parent dropdown is reset...");
    Assert.assertTrue("Parent dropdown is not reset!", 
                     userCategoryPage.isParentDropdownReset());
    System.out.println(">>> Parent dropdown reset successfully");
}

@Then("Full category list should be displayed")
public void full_category_list_should_be_displayed() {
    System.out.println(">>> Verifying full category list is displayed...");
    Assert.assertTrue("Category list is not displayed!", 
                     userCategoryPage.isCategoryListVisible());
    System.out.println(">>> Reset test completed successfully!");
}
}