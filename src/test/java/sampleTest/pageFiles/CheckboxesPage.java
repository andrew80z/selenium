package sampleTest.pageFiles;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckboxesPage {

  private static class Locators {

    private static final String CHECKBOXES_FORM_ID = "checkboxes";

    public static By checkboxByIndex(int index) {
      return By.xpath(String.format("//form[@id='%s']/input[%d]", CHECKBOXES_FORM_ID, index));
    }

    public static By checkboxLabelByIndex(int index) {
      return By.xpath(
          String.format(
              "//form[@id='%s']/input[%d]/following-sibling::text()[1]",
              CHECKBOXES_FORM_ID, index));
    }

    public static By allCheckboxes() {
      return By.xpath(
          String.format("//form[@id='%s']/input[@type='checkbox']", CHECKBOXES_FORM_ID));
    }
  }

  private final WebDriver driver;

  public CheckboxesPage(WebDriver driver) {
    this.driver = driver;
  }

  public WebElement getCheckbox(int index) {
    WebDriverWait wait = new WebDriverWait(driver, 10);
    return wait.until(ExpectedConditions.presenceOfElementLocated(Locators.checkboxByIndex(index)));
  }

  public List<WebElement> getAllCheckboxes() {
    WebDriverWait wait = new WebDriverWait(driver, 10);
    return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(Locators.allCheckboxes()));
  }

  public void selectCheckbox(int index) {
    WebElement checkbox = getCheckbox(index);
    if (!checkbox.isSelected()) {
      checkbox.click();
    }
  }

  public void unselectCheckbox(int index) {
    WebElement checkbox = getCheckbox(index);
    if (checkbox.isSelected()) {
      checkbox.click();
    }
  }

  public void openCheckboxesPage(String baseUrl) {
    driver.get(baseUrl + "/checkboxes");
  }

  public String getCheckboxLabel(int index) {
    // Get the checkbox element first to ensure it exists
    getCheckbox(index);

    // Execute JavaScript to get the text node content
    String script =
        String.format(
            "return document.evaluate(\"//form[@id='%s']/input[%d]/following-sibling::text()[1]\", document, null, XPathResult.STRING_TYPE, null).stringValue;",
            Locators.CHECKBOXES_FORM_ID, index);
    String labelText =
        (String) ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script);
    return labelText.trim();
  }
}
