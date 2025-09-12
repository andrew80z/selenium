package sampleTest.testFiles;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import sampleTest.config.BrowserConfig;
import sampleTest.config.BrowserType;
import sampleTest.config.WebTestSetup;

public class BaseTest {

  protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
  protected WebDriver driver;
  protected String baseUrl;
  private static final List<WebDriver> webDrivers = new ArrayList<>();
  private static final String SCREENSHOT_DIR = "test-output/screenshots";

  @BeforeSuite
  public void suiteSetup() {
    // Create screenshots directory
    new File(SCREENSHOT_DIR).mkdirs();
    logger.info("Test suite started");
  }

  @BeforeMethod
  public void setup() throws Exception {
    String browserName = System.getProperty("browser", "chrome");
    String env = System.getProperty("env", "qa");
    logger.info("Setting up test with browser: {} in environment: {}", browserName, env);

    BrowserType browserType = BrowserType.fromString(browserName);
    BrowserConfig.BrowserConfiguration config = BrowserConfig.getBrowserConfig(browserType);

    WebTestSetup.SetupResult result = WebTestSetup.setup(env, config.options, browserType);
    this.driver = result.driver;
    this.baseUrl = result.web_url;

    if (driver != null) {
      webDrivers.add(driver);
    }
  }

  @AfterMethod
  public void teardown(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
      captureScreenshot(result.getName());
    }

    if (driver != null) {
      try {
        driver.quit();
        webDrivers.remove(driver);
        Thread.sleep(1000); // Small delay to ensure proper cleanup
      } catch (IllegalStateException e) {
        logger.error("Browser already closed: {}", e.getMessage());
      } catch (InterruptedException e) {
        logger.error("Sleep interrupted during driver cleanup: {}", e.getMessage());
        Thread.currentThread().interrupt();
      }
    }
  }

  @AfterSuite
  public void suiteTeardown() {
    // Cleanup any remaining WebDriver instances
    for (WebDriver instance : new ArrayList<>(webDrivers)) {
      try {
        instance.quit();
        webDrivers.remove(instance);
      } catch (IllegalStateException e) {
        logger.error("Browser already closed: {}", e.getMessage());
      }
    }

    // Kill any remaining WebDriver processes
    try {
      if (System.getProperty("os.name").toLowerCase().contains("win")) {
        ProcessBuilder processBuilder =
            new ProcessBuilder(
                "taskkill", "/F", "/IM", "chromedriver.exe", "/IM", "geckodriver.exe");
        Process process = processBuilder.start();
        process.waitFor(); // Wait for the process to complete
      } else {
        ProcessBuilder processBuilder = new ProcessBuilder("pkill", "-f", "(chrome|gecko)driver");
        Process process = processBuilder.start();
        process.waitFor(); // Wait for the process to complete
      }
    } catch (IOException e) {
      logger.error("Error executing process to kill WebDriver: {}", e.getMessage());
    } catch (InterruptedException e) {
      logger.error("Process interrupted while killing WebDriver: {}", e.getMessage());
      Thread.currentThread().interrupt();
    }

    logger.info("Test suite completed");
  }

  protected void captureScreenshot(String testName) {
    if (driver instanceof TakesScreenshot) {
      try {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("%s/%s_%s.png", SCREENSHOT_DIR, testName, timestamp);
        FileUtils.copyFile(screenshotFile, new File(fileName));
        logger.info("Screenshot saved: {}", fileName);
      } catch (IOException e) {
        logger.error("Failed to capture screenshot: {}", e.getMessage());
      }
    }
  }
}
