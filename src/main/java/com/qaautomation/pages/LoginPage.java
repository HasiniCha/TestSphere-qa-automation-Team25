package com.qaautomation.pages;

import com.qaautomation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By successAlert = By.className("alert-success"); 
    private By errorAlert = By.cssSelector(".alert.alert-danger");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void loginAsAdmin() {
        type(usernameField, ConfigReader.get("app.admin.username"));
        type(passwordField, ConfigReader.get("app.admin.password"));
        click(loginButton);
    }

    public void loginAsUser() {
        type(usernameField, ConfigReader.get("app.user.username"));
        type(passwordField, ConfigReader.get("app.user.password"));
        click(loginButton);
    }

    // NEW: Method to enter individual credentials for more granular testing
    public void enterUsername(String username) {
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
    }

    public void clickLogin() {
        click(loginButton);
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }



public String getErrorMessageText() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(errorAlert)).getText();
}

public String getErrorMessageColor() {
    // This gets the CSS color value (usually in rgba format)
    return driver.findElement(errorAlert).getCssValue("color");
}

}