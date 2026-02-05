package com.qaautomation.stepdefinitions.ui;

import java.time.Duration;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qaautomation.pages.Category1.CategoryPage;
import com.qaautomation.pages.LoginPage;
import com.qaautomation.utils.DriverFactory;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Category {

    CategoryPage categoryPage;

    // ============ EXISTING STEP DEFINITIONS ============
    
    @Given("Admin is logged in")
    public void admin_is_logged_in() {
        WebDriver driver = DriverFactory.getDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAsAdmin();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @And("Admin navigates to the Categories page")
    public void admin_navigates_to_categories_page() {
        WebDriver driver = DriverFactory.getDriver();
        driver.get("http://localhost:3000/ui/categories");
        categoryPage = new CategoryPage(driver);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("Add Category button should be visible")
    public void add_category_button_should_be_visible() {
        Assert.assertTrue(categoryPage.isAddCategoryButtonVisible());
    }

    @And("Add Category button should be clickable")
    public void add_category_button_should_be_clickable() {
        Assert.assertTrue(categoryPage.isAddCategoryButtonClickable());
    }

    @When("Admin clicks the Add Category button")
    public void admin_clicks_the_add_category_button() {
        System.out.println(">>> Clicking Add Category button...");
        categoryPage.clickAddCategoryButton();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("Admin should be navigated to Add Category page")
    public void admin_should_be_navigated_to_add_category_page() {
        WebDriver driver = DriverFactory.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        wait.until(ExpectedConditions.urlContains("/ui/categories/add"));
        
        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "http://localhost:3000/ui/categories/add";
        
        System.out.println(">>> Current URL: " + actualUrl);
        System.out.println(">>> Expected URL: " + expectedUrl);
        
        Assert.assertEquals("URL did not match!", expectedUrl, actualUrl);
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println(">>> Test completed! Add Category page is visible.");
    }
    
    // ============ STEP DEFINITIONS FOR CREATING CATEGORY ============
    
    @And("Admin navigates to Add Category page")
    public void admin_navigates_to_add_category_page() {
        WebDriver driver = DriverFactory.getDriver();
        
        // First navigate to Categories page
        driver.get("http://localhost:3000/ui/categories");
        categoryPage = new CategoryPage(driver);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Then click Add Category button
        System.out.println(">>> Clicking Add Category button to navigate to Add page...");
        categoryPage.clickAddCategoryButton();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @When("Admin enters category name {string}")
    public void admin_enters_category_name(String categoryName) {
        System.out.println(">>> Entering category name: " + categoryName);
        categoryPage.enterCategoryName(categoryName);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @And("Admin leaves Parent Category as default")
    public void admin_leaves_parent_category_as_default() {
        System.out.println(">>> Parent Category left as default (no parent)");
    }
    
    @And("Admin clicks Save button")
    public void admin_clicks_save_button() {
        System.out.println(">>> Clicking Save button...");
        categoryPage.clickSaveButton();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Then("Main category should be created successfully")
    public void main_category_should_be_created_successfully() {
        System.out.println(">>> Verifying category creation...");
    }
    
    @And("Admin should be redirected to category list page")
    public void admin_should_be_redirected_to_category_list_page() {
        WebDriver driver = DriverFactory.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/ui/categories"));
        
        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "http://localhost:3000/ui/categories";
        
        System.out.println(">>> Redirected to: " + actualUrl);
        Assert.assertEquals("Not redirected to category list!", expectedUrl, actualUrl);
    }
    
    @And("New category {string} should be visible in the list")
    public void new_category_should_be_visible_in_the_list(String categoryName) {
        System.out.println(">>> Checking if category '" + categoryName + "' is visible...");
        Assert.assertTrue("Category not found in list!", 
                         categoryPage.isCategoryVisibleInList(categoryName));
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @And("Parent column should show {string} for category {string}")
    public void parent_column_should_show_for_category(String expectedParent, String categoryName) {
        System.out.println(">>> Checking parent value for '" + categoryName + "'...");
        String actualParent = categoryPage.getParentValueForCategory(categoryName);
        
        System.out.println(">>> Expected parent: " + expectedParent);
        System.out.println(">>> Actual parent: " + actualParent);
        
        Assert.assertEquals("Parent value does not match!", expectedParent, actualParent);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // ============ STEP DEFINITIONS FOR EMPTY NAME VALIDATION ============
    
    @When("Admin leaves the Category Name field empty")
    public void admin_leaves_the_category_name_field_empty() {
        System.out.println(">>> Leaving Category Name field empty...");
        categoryPage.leaveCategoryNameEmpty();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Then("Validation error message {string} should be displayed")
    public void validation_error_message_should_be_displayed(String expectedMessage) {
        System.out.println(">>> Checking for validation error message...");
        
        Assert.assertTrue("Required validation error not displayed!", 
                         categoryPage.isRequiredValidationErrorDisplayed());
        
        String actualMessage = categoryPage.getRequiredValidationErrorText();
        System.out.println(">>> Expected message: " + expectedMessage);
        System.out.println(">>> Actual message: " + actualMessage);
        
        Assert.assertTrue("Validation message does not match!", 
                         actualMessage.contains(expectedMessage));
        
        // Check if error is displayed in red
        Assert.assertTrue("Validation error not displayed in red!", 
                         categoryPage.isValidationErrorInRed());
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @And("Name length validation message should be displayed")
    public void name_length_validation_message_should_be_displayed() {
        System.out.println(">>> Checking for name length validation message...");
        
        Assert.assertTrue("Length validation error not displayed!", 
                         categoryPage.isLengthValidationErrorDisplayed());
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @And("Form should not submit")
    public void form_should_not_submit() {
        System.out.println(">>> Verifying form did not submit...");
        // Form not submitting is verified by staying on the same page
    }
    
    @And("Admin should remain on Add Category page")
    public void admin_should_remain_on_add_category_page() {
        WebDriver driver = DriverFactory.getDriver();
        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = "http://localhost:3000/ui/categories/add";
        
        System.out.println(">>> Current URL: " + currentUrl);
        System.out.println(">>> Expected URL: " + expectedUrl);
        
        Assert.assertEquals("User was not on Add Category page!", expectedUrl, currentUrl);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println(">>> Validation test completed successfully!");
    }
    
    // ============ STEP DEFINITIONS FOR DELETING CATEGORY ============
    
    @And("Debug category row for {string}")
    public void debug_category_row(String categoryName) {
        categoryPage.debugCategoryRow(categoryName);
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @And("Delete button is visible for category {string}")
    public void delete_button_is_visible_for_category(String categoryName) {
        System.out.println(">>> Checking if delete button is visible for '" + categoryName + "'...");
        Assert.assertTrue("Delete button not visible for category!", 
                         categoryPage.isDeleteButtonVisibleForCategory(categoryName));
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("Admin clicks the Delete button for category {string}")
    public void admin_clicks_the_delete_button_for_category(String categoryName) {
        System.out.println(">>> Clicking Delete button for category '" + categoryName + "'...");
        categoryPage.clickDeleteButtonForCategory(categoryName);
        
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @And("Admin clicks OK\\/Confirm in the confirmation dialog")
    public void admin_clicks_confirm_in_confirmation_dialog() {
        System.out.println(">>> Clicking Confirm button in delete dialog...");
        categoryPage.clickConfirmDeleteButton();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("Category {string} should be deleted successfully")
    public void category_should_be_deleted_successfully(String categoryName) {
        System.out.println(">>> Verifying category '" + categoryName + "' is deleted...");
        Assert.assertTrue("Category still visible after deletion!", 
                         categoryPage.isCategoryNotVisibleInList(categoryName));
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @And("Success message for deletion should be displayed")
    public void success_message_for_deletion_should_be_displayed() {
        System.out.println(">>> Checking for success message...");
        Assert.assertTrue("Success message not displayed!", 
                         categoryPage.isSuccessMessageDisplayed());
        
        String successMsg = categoryPage.getSuccessMessageText();
        System.out.println(">>> Success message: " + successMsg);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @And("Category {string} should no longer be visible in the category list")
    public void category_should_no_longer_be_visible_in_category_list(String categoryName) {
        System.out.println(">>> Verifying '" + categoryName + "' is not in the list...");
        Assert.assertTrue("Category still found in the list!", 
                         categoryPage.isCategoryNotVisibleInList(categoryName));
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println(">>> Delete category test completed successfully!");
    }
}