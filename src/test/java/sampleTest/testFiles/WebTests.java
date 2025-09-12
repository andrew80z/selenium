package sampleTest.testFiles;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sampleTest.pageFiles.ABTestingPage;
import sampleTest.pageFiles.CheckboxesPage;

public class WebTests extends BaseTest {

  private ABTestingPage abTestingPage;
  private CheckboxesPage checkboxesPage;

  @BeforeMethod
  @Override
  public void setup() throws Exception {
    super.setup();
    this.abTestingPage = new ABTestingPage(driver);
    this.checkboxesPage = new CheckboxesPage(driver);
    logger.info("Page objects initialized for test execution");
  }

  @Test(
      groups = {"ui"},
      description = "Verify A/B Test page content variations")
  public void testABTestPage() {
    Reporter.log("========== TEST START: A/B Test Page Verification ========== <br><br>", true);
    Reporter.log("Step 1: Test Configuration<br>", true);
    Reporter.log("• Base URL: " + baseUrl + "<br><br>", true);

    String[] possibleHeadings = {"A/B Test Variation 1", "A/B Test Control"};
    String expectedParagraph =
        "Also known as split testing."
            + " This is a way in which businesses are able to simultaneously test and learn different versions of a page"
            + " to see which text and/or functionality works best towards a desired outcome (e.g. a user action such as a click-through).";

    Reporter.log("Step 2: Test Data<br>", true);
    Reporter.log("• Expected headings: " + String.join(" or ", possibleHeadings) + "<br>", true);
    Reporter.log("• Expected paragraph content is prepared<br><br>", true);

    // Navigate to the /abtest endpoint
    Reporter.log("Step 3: Navigation<br>", true);
    Reporter.log("• Navigating to A/B Test page<br><br>", true);
    abTestingPage.openAbPage(baseUrl);

    // Verify the presence of the element with class "example"
    Reporter.log("Step 4: Element Verification<br>", true);
    Reporter.log("• Checking for 'example' element presence<br>", true);
    WebElement exampleElement = abTestingPage.getExampleElement();
    Assert.assertNotNull(
        exampleElement, "Element with class 'example' is not present on the page.");
    Reporter.log("• Result: Example element found successfully<br><br>", true);
    // Validate the text content of the element
    Reporter.log("Step 5: Content Validation<br>", true);
    Reporter.log("• Getting text content from example element<br>", true);
    String actualText = exampleElement.getText();
    Reporter.log("• Actual text content:<br>" + actualText + "<br><br>", true);

    // Check if actual text contains any of the possible headings
    Reporter.log("Step 6: Heading Verification<br>", true);
    boolean headingMatches = false;
    for (String heading : possibleHeadings) {
      Reporter.log("• Checking for heading: " + heading + "<br>", true);
      if (actualText.contains(heading)) {
        headingMatches = true;
        Reporter.log("• Found matching heading: " + heading + "<br><br>", true);
        break;
      }
    }
    Assert.assertTrue(headingMatches, "Heading text does not match any expected variation.");
    Assert.assertTrue(actualText.contains(expectedParagraph), "Paragraph text does not match.");

    Reporter.log("<br>========== TEST COMPLETED SUCCESSFULLY ==========<br>", true);
  }

  @Test(
      groups = {"ui"},
      description = "Verify Checkboxes page functionality")
  public void testCheckboxesPage() {
    Reporter.log("========== TEST START: Checkboxes Page Verification ==========<br><br>", true);

    Reporter.log("Step 1: Test Configuration<br>", true);
    Reporter.log("• Base URL: " + baseUrl + "<br><br>", true);

    Reporter.log("Step 2: Test Data<br>", true);
    String[] expectedCheckboxLabels = {"checkbox 1", "checkbox 2"};
    Reporter.log(
        "• Expected checkbox labels: " + String.join(", ", expectedCheckboxLabels) + "<br><br>",
        true);

    Reporter.log("Step 3: Navigation<br>", true);
    Reporter.log("• Navigating to Checkboxes page<br><br>", true);
    checkboxesPage.openCheckboxesPage(baseUrl);

    Reporter.log("Step 4: Checkbox Elements Verification<br>", true);
    for (int i = 1; i <= expectedCheckboxLabels.length; i++) {
      Reporter.log("• Verifying checkbox " + i + "<br>", true);
      WebElement checkbox = checkboxesPage.getCheckbox(i);
      Assert.assertNotNull(checkbox, "Checkbox " + i + " is not present on the page.");
      String actualLabel = checkboxesPage.getCheckboxLabel(i);
      Assert.assertEquals(
          actualLabel, expectedCheckboxLabels[i - 1], "checkbox " + i + " label does not match.");
      Reporter.log("  - Label verified: " + actualLabel + "<br>", true);
    }
    Reporter.log("<br>", true);

    Reporter.log("Step 5: Default State Verification<br>", true);
    WebElement checkbox2 = checkboxesPage.getCheckbox(2);
    Reporter.log("• Checking checkbox 2 default state<br>", true);
    Assert.assertTrue(checkbox2.isSelected(), "Checkbox 2 should be selected by default.");
    Reporter.log("• Checkbox 2 is selected by default as expected<br>", true);

    WebElement checkbox1 = checkboxesPage.getCheckbox(1);
    Reporter.log("• Checking checkbox 1 default state<br>", true);
    Assert.assertFalse(checkbox1.isSelected(), "Checkbox 1 should not be selected by default.");
    Reporter.log("• Checkbox 1 is unselected by default as expected<br><br>", true);

    Reporter.log("Step 6: Checkbox Interaction<br>", true);
    Reporter.log("• Selecting checkbox 1<br>", true);
    checkboxesPage.selectCheckbox(1);
    checkbox1 = checkboxesPage.getCheckbox(1);
    Assert.assertTrue(checkbox1.isSelected(), "Checkbox 1 should be selected.");
    Reporter.log("• Checkbox 1 successfully selected<br><br>", true);

    Reporter.log("========== TEST COMPLETED SUCCESSFULLY ==========<br>", true);
  }
}
