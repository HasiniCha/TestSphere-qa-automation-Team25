package com.qaautomation.pages.sales;

import com.qaautomation.pages.BasePage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SalesPage extends BasePage {

  private By sellPlantButton = By.cssSelector("a.btn.btn-primary.btn-sm.mb-3");
  private By salesTable = By.cssSelector("table");
  private By salesTableRows = By.cssSelector("table tbody tr");
  private By deleteButton = By.cssSelector("button.btn-outline-danger");
  private By successMessage = By.xpath(
    "//div[contains(@class,'alert-success')]"
  );
private By tableRows = By.xpath("//table/tbody/tr");
  public SalesPage(WebDriver driver) {
    super(driver);
  }

  public boolean isOnSalesPage() {
    return driver.getCurrentUrl().contains("/sales");
  }

  public void clickSellPlantButton() {
    click(sellPlantButton);
  }

  public boolean verifySaleExists(String plantName, String quantity) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(salesTable));

    By saleRow = By.xpath(
      "//table/tbody/tr[contains(.,'" +
      plantName +
      "') and contains(.,'" +
      quantity +
      "')]"
    );

    try {
      WebElement row = driver.findElement(saleRow);
      return row.isDisplayed();
    } catch (Exception e) {
      return false;
    }
  }

  public WebElement getLatestSaleRow() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(salesTableRows));
    List<WebElement> rows = driver.findElements(salesTableRows);

    if (rows.isEmpty()) {
      return null;
    }

    return rows.get(0);
  }

public WebElement deleteSale() {
    // 1. Locate the first row's delete button
    WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(salesTableRows));
    WebElement deleteBtn = firstRow.findElement(By.cssSelector("button.btn-outline-danger"));
    
    // 2. Click it
    deleteBtn.click();
    
    // 3. Handle Alert
    wait.until(ExpectedConditions.alertIsPresent());
    driver.switchTo().alert().accept();
    
    // 4. Wait for Success Message
    wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
    
    return firstRow; // Return the row element so the step def can check it's gone
}

  public boolean isSaleDeleted(String saleName) {
    try {
      By deletedRowLocator = By.xpath(
        "//table/tbody/tr[td[contains(text(),'" + saleName + "')]]"
      );

      return wait.until(
        ExpectedConditions.invisibilityOfElementLocated(deletedRowLocator)
      );
    } catch (Exception e) {
      List<WebElement> rows = driver.findElements(
        By.xpath("//table/tbody/tr[td[contains(text(),'" + saleName + "')]]")
      );
      return rows.isEmpty();
    }
  }

  public boolean isSuccessMessageDisplayed() {
    try {
      WebElement toast = wait.until(
        ExpectedConditions.visibilityOfElementLocated(successMessage)
      );
      return toast.isDisplayed();
    } catch (Exception e) {
      return false;
    }
  }
 public int getSalesRowCount() {
        return driver.findElements(tableRows).size();
    }

  public void clickColumnHeader(String columnName) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(salesTable));

    By columnLinkLocator = By.xpath(
      "//th/a[contains(text(),'" + columnName + "')]"
    );

    try {
      WebElement columnLink = wait.until(
        ExpectedConditions.elementToBeClickable(columnLinkLocator)
      );
      columnLink.click();

      Thread.sleep(1000);
    } catch (Exception e) {
      throw new RuntimeException(
        "Could not find or click column header: " +
        columnName +
        ". Error: " +
        e.getMessage()
      );
    }
  }

  public List<String> getColumnValues(String columnName) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(salesTable));

    int columnIndex = getColumnIndex(columnName);

    List<WebElement> rows = driver.findElements(salesTableRows);
    List<String> values = new ArrayList<>();

    for (WebElement row : rows) {
      List<WebElement> cells = row.findElements(By.tagName("td"));
      if (cells.size() > columnIndex) {
        String cellText = cells.get(columnIndex).getText().trim();

        if (!cellText.isEmpty()) {
          values.add(cellText);
        }
      }
    }

    return values;
  }

  private int getColumnIndex(String columnName) {
    List<WebElement> headers = driver.findElements(
      By.xpath("//table/thead/tr/th")
    );

    for (int i = 0; i < headers.size(); i++) {
      try {
        WebElement link = headers.get(i).findElement(By.tagName("a"));
        String linkText = link.getText().trim();

        if (
          linkText.equalsIgnoreCase(columnName) ||
          linkText.toLowerCase().contains(columnName.toLowerCase())
        ) {
          return i;
        }
      } catch (Exception e) {
        continue;
      }
    }

    switch (columnName.toLowerCase()) {
      case "plant":
        return 0;
      case "quantity":
        return 1;
      case "total price":
        return 2;
      case "sold at":
        return 3;
      default:
        return 0;
    }
  }

  public boolean isSortedAscending(List<String> values) {
    if (values.size() <= 1) return true;

    if (isNumericList(values)) {
      return isNumericallySortedAscending(values);
    }

    List<String> sorted = new ArrayList<>(values);
    sorted.sort(String.CASE_INSENSITIVE_ORDER);

    return values.equals(sorted);
  }

  public boolean isSortedDescending(List<String> values) {
    if (values.size() <= 1) return true;

    if (isNumericList(values)) {
        return isNumericallySortedDescending(values);
    }

    // Instead of sorting a copy and comparing lists, 
    // compare items one-by-one to see if the order is broken.
    for (int i = 0; i < values.size() - 1; i++) {
        String current = values.get(i);
        String next = values.get(i + 1);
        
        // compareTo returns < 0 if current comes before next
        // In descending (Z-A), current should be >= next
        if (current.compareToIgnoreCase(next) < 0) {
            System.out.println("DEBUG: Sort failure. Found '" + current + "' followed by '" + next + "'");
            return false;
        }
    }
    return true;
}

  private boolean isNumericList(List<String> values) {
    if (values.isEmpty()) return false;

    try {
      Double.parseDouble(values.get(0).replace(",", ""));
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean isNumericallySortedAscending(List<String> values) {
    List<Double> numericValues = values
      .stream()
      .map(s -> Double.parseDouble(s.replace(",", "")))
      .collect(java.util.stream.Collectors.toList());

    List<Double> sorted = new ArrayList<>(numericValues);
    sorted.sort(Double::compare);

    System.out.println("Original (numeric): " + numericValues);
    System.out.println("Expected ASC (numeric): " + sorted);

    return numericValues.equals(sorted);
  }

  private boolean isNumericallySortedDescending(List<String> values) {
    List<Double> numericValues = values
      .stream()
      .map(s -> Double.parseDouble(s.replace(",", "")))
      .collect(java.util.stream.Collectors.toList());

    List<Double> sorted = new ArrayList<>(numericValues);
    sorted.sort((a, b) -> Double.compare(b, a));

    System.out.println("Original (numeric): " + numericValues);
    System.out.println("Expected DESC (numeric): " + sorted);

    return numericValues.equals(sorted);
  }

  public boolean isSalesTableDisplayed() {
    try {
      return driver.findElement(salesTable).isDisplayed();
    } catch (Exception e) {
      return false;
    }
  }

  public boolean isSellPlantButtonVisible() {
    return !driver.findElements(sellPlantButton).isEmpty();
  }

  public boolean isDeleteButtonVisible() {
    return !driver.findElements(deleteButton).isEmpty();
  }

  private By nextPageLink = By.xpath(
    "//li[not(contains(@class,'disabled'))]/a[contains(text(),'Next')]"
  );
  private By pageTwoLink = By.xpath("//li/a[text()='2']");
  private By activePageItem = By.cssSelector("li.page-item.active a.page-link");

  public void scrollToBottom() {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
  }

  public void clickNextPage() {
    WebElement nextBtn = wait.until(
      ExpectedConditions.elementToBeClickable(nextPageLink)
    );

    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].click();",
        nextBtn
      );

    wait.until(
      ExpectedConditions.textToBePresentInElementLocated(activePageItem, "2")
    );
  }

  public void clickPageTwo() {
    wait.until(ExpectedConditions.elementToBeClickable(pageTwoLink)).click();
  }

  public String getActivePageText() {
    return wait
      .until(ExpectedConditions.visibilityOfElementLocated(activePageItem))
      .getText();
  }
}
