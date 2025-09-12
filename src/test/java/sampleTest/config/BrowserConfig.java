package sampleTest.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserConfig {

  private static final String CONFIG_FILE = "/dataFiles/browser-config.json";

  public static Object getBrowserOptions(BrowserType browserType) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode config = mapper.readTree(BrowserConfig.class.getResourceAsStream(CONFIG_FILE));
    JsonNode browserConfig = config.get(browserType.toString().toLowerCase());

    switch (browserType) {
      case FIREFOX:
        return createFirefoxOptions(browserConfig);
      case CHROME:
      default:
        return createChromeOptions(browserConfig);
    }
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
