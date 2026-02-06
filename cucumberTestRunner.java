package com.qatraining.tests.ui.cucumber;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Cucumber Test Runner using JUnit 5 Platform
 * Executes all feature files in the features directory
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, 
    value = "pretty, html:target/cucumber-reports, json:target/cucumber-reports/Cucumber.json, junit:target/cucumber-reports/Cucumber.xml")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, 
    value = "com.qatraining.tests.ui.cucumber")
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME,
    value = "@TC-UI-CATEGORY-ADMIN-001 or @TC-UI-CATEGORY-ADMIN-002 or @TC-UI-CATEGORY-ADMIN-003 or @TC-UI-CATEGORY-ADMIN-004 or @TC-UI-CATEGORY-ADMIN-005 or @TC-UI-CATEGORY-USER-001 or @TC-UI-CATEGORY-USER-002 or @TC-UI-CATEGORY-USER-003 or @TC-UI-CATEGORY-USER-004 or @TC-UI-CATEGORY-USER-005")
public class CucumberTestRunner {
    // This class serves as the entry point for Cucumber tests using JUnit 5 Platform
}
