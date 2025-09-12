package sampleTest.pageFiles;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ABTestingPage {

  private final WebDriver driver;

  public ABTestingPage(WebDriver driver) {
    this.driver = driver;
  }

  public WebElement getExampleElement() {
    WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
    return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("example")));
  }

  public void openAbPage(String baseUrl) {

    driver.get(baseUrl + "/abtest");
  }
}
