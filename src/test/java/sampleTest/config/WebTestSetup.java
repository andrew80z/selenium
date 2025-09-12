package sampleTest.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebTestSetup {

  public static class SetupResult {

    public WebDriver driver;
    public String web_url;
    public String api_post_url;
    public String api_endpoint;
  }

  private static final String ENV_CONFIG_FILE = "/dataFiles/envs.json";

  private static JsonNode loadEnvConfig(String env) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode envs = mapper.readTree(WebTestSetup.class.getResourceAsStream(ENV_CONFIG_FILE));
    return envs.get(env);
  }

  public static SetupResult setup(String env) throws Exception {
    JsonNode envConfig = loadEnvConfig(env);
    SetupResult result = new SetupResult();
    result.driver = null; // No WebDriver needed for API tests
    result.web_url = envConfig.get("webURL").asText();
    result.api_post_url = envConfig.get("postUrl").asText();
    result.api_endpoint = envConfig.get("endpoint").asText();
    return result;
  }

  public static SetupResult setup(String env, Object options, BrowserType browserType)
      throws Exception {
    // Load environment configuration first
    JsonNode envConfig = loadEnvConfig(env);

    // Setup WebDriver based on browser type
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

    // Create and populate the result object
    SetupResult result = new SetupResult();
    result.driver = driver;
    result.web_url = envConfig.get("webURL").asText();
    result.api_post_url = envConfig.get("postUrl").asText();
    result.api_endpoint = envConfig.get("endpoint").asText();
    return result;
  }
}
