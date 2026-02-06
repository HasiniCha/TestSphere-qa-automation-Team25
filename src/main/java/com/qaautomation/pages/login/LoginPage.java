package com.qaautomation.pages;

import com.qaautomation.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");

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
}