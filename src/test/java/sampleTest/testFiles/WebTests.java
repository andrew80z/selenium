package sampleTest.testFiles;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import sampleTest.pageFiles.ABTestingPage;
import sampleTest.pageFiles.BrowserConfig;
import sampleTest.pageFiles.BrowserType;
import sampleTest.pageFiles.web_test_setup;

public class WebTests {

    // WebDriver and baseUrl initialization
    private WebDriver driver;
    private String baseUrl;
    private ABTestingPage abTestingPage;

    @BeforeMethod
    public void setup() throws Exception {
        String browserName = System.getProperty("browser", "chrome");
        String env = System.getProperty("env", "qa");

        BrowserType browserType = BrowserType.fromString(browserName);
        Object options = BrowserConfig.getBrowserOptions(browserType);

        web_test_setup.setup_result result = web_test_setup.setup(env, options, browserType);
        this.driver = result.driver;
        this.baseUrl = result.web_url;
        this.abTestingPage = new ABTestingPage(driver);
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testABTestPage() {
        String[] possibleHeadings = {"A/B Test Variation 1", "A/B Test Control"};
        String expectedParagraph = "Also known as split testing. This is a way in which businesses are able to simultaneously test and learn different versions of a page to see which text and/or functionality works best towards a desired outcome (e.g. a user action such as a click-through).";

        // Navigate to the /abtest endpoint
        abTestingPage.openAbPage(baseUrl);

        // Verify the presence of the element with class "example"
        WebElement exampleElement = abTestingPage.getExampleElement();
        Assert.assertNotNull(exampleElement, "Element with class 'example' is not present on the page.");

        // Validate the text content of the element
        String actualText = exampleElement.getText();
        //System.out.println("Actual text: " + actualText);

        // Check if actual text contains any of the possible headings
        boolean headingMatches = false;
        for (String heading : possibleHeadings) {
            if (actualText.contains(heading)) {
                headingMatches = true;
                break;
            }
        }
        Assert.assertTrue(headingMatches, "Heading text does not match any expected variation.");
        Assert.assertTrue(actualText.contains(expectedParagraph), "Paragraph text does not match.");
    }
}
