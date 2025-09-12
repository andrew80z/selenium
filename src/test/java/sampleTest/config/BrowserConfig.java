package sampleTest.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserConfig {

  private static final String CONFIG_FILE = "/dataFiles/browser-config.json";

  public static class BrowserConfiguration {

    public String driverVersion;
    public Object options;
  }

  public static BrowserConfiguration getBrowserConfig(BrowserType browserType) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode config = mapper.readTree(BrowserConfig.class.getResourceAsStream(CONFIG_FILE));
    JsonNode browserConfig = config.get(browserType.toString().toLowerCase());

    BrowserConfiguration configuration = new BrowserConfiguration();
    configuration.driverVersion = browserConfig.path("driver").path("version").asText("latest");

    switch (browserType) {
      case FIREFOX:
        configuration.options = createFirefoxOptions(browserConfig);
        break;
      case CHROME:
      default:
        configuration.options = createChromeOptions(browserConfig);
        break;
    }

    return configuration;
  }

  private static ChromeOptions createChromeOptions(JsonNode config) {
    ChromeOptions options = new ChromeOptions();

    // Check if headless mode is enabled via system property
    boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
    if (headless) {
      options.addArguments("--headless=new");
    }

    if (config.has("options")) {
      JsonNode optionsNode = config.get("options");

      // Add arguments
      if (optionsNode.has("arguments")) {
        optionsNode.get("arguments").forEach(arg -> options.addArguments(arg.asText()));
      }

      // Add preferences
      if (optionsNode.has("preferences")) {
        optionsNode
            .get("preferences")
            .fields()
            .forEachRemaining(
                pref ->
                    options.addArguments("--" + pref.getKey() + "=" + pref.getValue().asText()));
      }
    }

    return options;
  }

  private static FirefoxOptions createFirefoxOptions(JsonNode config) {
    FirefoxOptions options = new FirefoxOptions();

    // Check if headless mode is enabled via system property
    boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
    if (headless) {
      options.addArguments("--headless");
    }

    if (config.has("options")) {
      JsonNode optionsNode = config.get("options");

      // Add arguments
      if (optionsNode.has("arguments")) {
        optionsNode.get("arguments").forEach(arg -> options.addArguments(arg.asText()));
      }

      // Add preferences
      if (optionsNode.has("preferences")) {
        optionsNode
            .get("preferences")
            .fields()
            .forEachRemaining(
                pref -> options.addPreference(pref.getKey(), pref.getValue().asText()));
      }
    }

    return options;
  }
}
