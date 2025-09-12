package sampleTest.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class web_test_setup {

  public static class setup_result {

    public WebDriver driver;
    public String web_url;
    public String api_post_url;
    public String api_endpoint;
  }

  public static setup_result setup(String env) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode envs = mapper.readTree(new File("dataFiles/envs.json"));
    JsonNode envConfig = envs.get(env);

    setup_result result = new setup_result();
    result.driver = null; // No WebDriver needed for API tests
    result.web_url = envConfig.get("webURL").asText();
    result.api_post_url = envConfig.get("postUrl").asText();
    result.api_endpoint = envConfig.get("endpoint").asText();
    return result;
  }

  public static setup_result setup(String env, Object options, BrowserType browserType)
      throws Exception {
    WebDriver driver;

    switch (browserType) {
      case FIREFOX:
        WebDriverManager.firefoxdriver().setup();
        driver =
            options != null ? new FirefoxDriver((FirefoxOptions) options) : new FirefoxDriver();
        break;
      case CHROME:
      default:
        WebDriverManager.chromedriver().setup();
        driver = options != null ? new ChromeDriver((ChromeOptions) options) : new ChromeDriver();
        break;
    }

    ObjectMapper mapper = new ObjectMapper();
    JsonNode envs = mapper.readTree(new File("dataFiles/envs.json"));
    JsonNode envConfig = envs.get(env);

    setup_result result = new setup_result();
    result.driver = driver;
    result.web_url = envConfig.get("webURL").asText();
    result.api_post_url = envConfig.get("postUrl").asText();
    result.api_endpoint = envConfig.get("endpoint").asText();
    return result;
  }
}
