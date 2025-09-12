package sampleTest.config;

public enum BrowserType {
  CHROME,
  FIREFOX;

  public static BrowserType fromString(String browser) {
    if (browser == null || browser.isEmpty()) {
      return CHROME; // default browser
    }
    try {
      return BrowserType.valueOf(browser.toUpperCase());
    } catch (IllegalArgumentException e) {
      return CHROME; // default to Chrome if invalid browser name
    }
  }
}
